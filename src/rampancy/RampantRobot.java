package rampancy;

import java.awt.Graphics2D;
import java.util.LinkedList;

import rampancy.gun.RGunManager;
import rampancy.move.RMovementManager;
import rampancy.radar.RRadarManager;
import rampancy.util.RBattlefield;
import rampancy.util.REnemy;
import rampancy.util.REnemyManager;
import rampancy.util.RPoint;
import rampancy.util.RState;
import rampancy.util.RStateful;

import robocode.AdvancedRobot;
import robocode.BulletHitEvent;
import robocode.HitByBulletEvent;
import robocode.ScannedRobotEvent;

abstract public class RampantRobot extends AdvancedRobot implements RStateful {
    protected static RBattlefield battlefield;
    protected static REnemyManager enemyManager;
    protected static RRadarManager radarManager;
    protected static RGunManager gunManager;
    protected static RMovementManager movementManager;

    protected LinkedList<RState> states;

    public RampantRobot() {
        super();
        states = new LinkedList<RState>();
        setup();
    }

    public void setup() {
        if (enemyManager == null) {
            firstTimeSetup();
        }
        enemyManager.updateReferenceBot(this);
        radarManager.updateReferenceBot(this);
        gunManager.updateReferenceBot(this);
        movementManager.updateReferenceBot(this);
    }

    abstract public void setUpColors();

    public void firstTimeSetup() {
        enemyManager = new REnemyManager(this);
        radarManager = new RRadarManager(this);
        gunManager = new RGunManager(this);
        movementManager = new RMovementManager();
    }

    public void run() {
        super.run();
        setUpColors();
        RBattlefield.setGlobalBattlefield(new RBattlefield(this.getBattleFieldWidth(), this.getBattleFieldHeight()));
        this.setAdjustGunForRobotTurn(true);
        this.setAdjustRadarForGunTurn(true);
        this.setAdjustRadarForRobotTurn(true);

        setTurnRadarRightRadians(Double.POSITIVE_INFINITY);

        while(true) {
            updateState(null);
            doMovement();
            doGun();
            doRadar();
            execute();
        }
    }

    public void doMovement() {
        if (movementManager != null) {
            movementManager.execute();
        }
    }

    public void doGun() {
        if (gunManager != null) {
            gunManager.execute();
        }
    }

    public void doRadar() {
        if (radarManager != null) {
            radarManager.execute();
        }
    }

    public void onScannedRobot(ScannedRobotEvent e) {
        REnemy scannedEnemy = enemyManager.get(e.getName());
        scannedEnemy.updateState(e);
        if (radarManager != null) {
            radarManager.update(scannedEnemy);
        }

        if (gunManager != null) {
            gunManager.update(scannedEnemy);
        }

        if (movementManager != null) {
            movementManager.update(scannedEnemy);
        }
    }

    public void onBulletHit(BulletHitEvent e) {
        if (enemyManager != null) {
            enemyManager.onBulletHit(e);
        }
    }

    public void onHitByBullet(HitByBulletEvent e) {
        if (movementManager != null) {
            movementManager.onHitByBullet(e);
        }
    }

    public void onPaint(Graphics2D g) {
        RBattlefield bf = RBattlefield.globalBattlefield();
        if (bf != null) {
            bf.draw(g);
        }

        if (enemyManager != null) {
            enemyManager.draw(g);
        }

        if (gunManager != null) {
            gunManager.draw(g);
        }

        if (movementManager != null) {
            // movementManager.draw(g);
        }
    }

    public RPoint location() {
        return new RPoint(this.getX(), this.getY());
    }

    public RState currentState() {
        if(states.isEmpty()) {
            return null;
        }
        return states.getFirst();
    }

    public RState lastState() {
        if(states.isEmpty()) {
            return null;
        }
        return states.get(1);
    }

    public RState lastUsableState() {
        if(states.size() < 3) {
            return lastState();
        }
        return states.get(2);
    }

    public RState updateState(ScannedRobotEvent e) {
        RState state = new RState(this);
        states.push(state);
        if(states.size() > Const.MAX_HISTORY_DEPTH) {
            states.removeLast();
        }
        return state;
    }
}
