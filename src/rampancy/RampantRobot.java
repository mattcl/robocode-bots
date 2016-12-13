package rampancy;

import java.awt.Graphics2D;
import java.util.LinkedList;

import rampancy.util.REnemy;
import rampancy.util.REnemyManager;
import rampancy.util.RPoint;
import rampancy.util.RState;
import rampancy.util.RStateful;

import robocode.AdvancedRobot;
import robocode.ScannedRobotEvent;
import robocode.util.Utils;

abstract public class RampantRobot extends AdvancedRobot implements RStateful {
    protected static REnemyManager enemyManager;

    protected LinkedList<RState> states;

    public RampantRobot() {
        super();
        states = new LinkedList<RState>();
        setup();
    }

    public void setup() {
        if (enemyManager == null) {
            enemyManager = new REnemyManager(this);
        }
        enemyManager.updateReferenceBot(this);
    }

    public void run() {
        super.run();
        this.setAdjustGunForRobotTurn(true);
        this.setAdjustRadarForGunTurn(true);
        this.setAdjustRadarForRobotTurn(true);

        setTurnRadarRightRadians(Double.POSITIVE_INFINITY);

        while(true) {
            doMovement();
            doGun();
            doRadar();
            execute();
        }
    }

    public void doMovement() {

    }

    public void doGun() {

    }

    public void doRadar() {

        if (getRadarTurnRemaining() == 0.0) {
            setTurnRadarRightRadians(Double.POSITIVE_INFINITY);
        }
        scan();
    }

    public void onScannedRobot(ScannedRobotEvent e) {
        REnemy scannedEnemy = enemyManager.get(e.getName());
        scannedEnemy.updateState(e);

        // TODO: move this?
        double radarTurn = this.getHeadingRadians() + e.getBearingRadians() - this.getRadarHeadingRadians();
        setTurnRadarRightRadians(2.0 * Utils.normalRelativeAngle(radarTurn));
    }

    public void onPaint(Graphics2D g) {
        if (enemyManager != null) {
            enemyManager.draw(g);
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
        RState state = new RState(this, e);
        states.push(state);
        if(states.size() > Const.MAX_HISTORY_DEPTH) {
            states.removeLast();
        }
        return state;
    }
}
