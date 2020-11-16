package rampancy.gun;

import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import rampancy.RampantRobot;
import rampancy.util.RDrawable;
import rampancy.util.REnemy;

import robocode.Bullet;
import robocode.util.Utils;

public class RGunManager implements RDrawable {
    protected RampantRobot referenceBot;
    protected long fireTime;
    protected HashMap<String, RGun> guns;
    protected RFiringSolution lockedSolution;
    protected ArrayList<RBullet> bullets;
    protected HashMap<String, REnemy> enemies;

    public RGunManager(RampantRobot referenceBot) {
        this.referenceBot = referenceBot;
        this.bullets = new ArrayList<RBullet>();
        this.guns = new HashMap<String, RGun>();
        this.enemies = new HashMap<String, REnemy>();
    }

    public void updateReferenceBot(RampantRobot referenceBot) {
        this.referenceBot = referenceBot;
        this.bullets = new ArrayList<RBullet>();
        this.fireTime = 0;
        for (RGun gun : this.guns.values()) {
        	gun.updateReferenceBot(referenceBot);
        }
    }

    public void addGun(RGun gun) {
        this.guns.put(gun.name(), gun);
        gun.updateReferenceBot(this.referenceBot);
    }

    public void update(REnemy enemy) {
    	if (!this.enemies.containsKey(enemy.name)) {
    		this.enemies.put(enemy.name, enemy);
    	}
        for (RGun gun : guns.values()) {
            gun.update(enemy);
        }
    }

    public void execute() {
        if (fireTime == referenceBot.getTime() && referenceBot.getGunTurnRemainingRadians() == 0) {
            if (lockedSolution != null) {
                Bullet shot = referenceBot.setFireBullet(lockedSolution.power);
                if (shot != null) {
                    RBullet bullet = new RBullet(shot, lockedSolution);
                    bullets.add(bullet);
                    lockedSolution.gun.shotFired(bullet);
                    lockedSolution = null;
                }
            }
        }
        maybeLockFiringSolution();
        updateBullets();
    }

    public void draw(Graphics2D g) {
        for (RBullet bullet : bullets) {
            bullet.draw(g);
        }
    }

    protected void maybeLockFiringSolution() {
    	REnemy target = this.getTarget();
    	double desiredPower = this.getDesiredPower(target);
    	
    	if (target == null) {
    		return;
    	}
    	
        double maxProbability = -1;
        RFiringSolution maxSolution = null;

        List<RFiringSolution> firingSolutions = new ArrayList<RFiringSolution>();
        for (RGun gun : guns.values()) {
            RFiringSolution solution = gun.firingSolution(target, desiredPower);
            if (solution != null) {
                firingSolutions.add(solution);
                if (solution.hitProbability > maxProbability) {
                    maxProbability = solution.hitProbability;
                    maxSolution = solution;
                }
            }
        }

        if (maxSolution != null) {
            this.lockedSolution = maxSolution;
            this.fireTime = this.referenceBot.getTime() + 1;
            this.referenceBot.setTurnGunRightRadians(Utils.normalRelativeAngle(this.lockedSolution.firingAngle));
        }
    }
    
    protected REnemy getTarget() {
    	if (this.enemies.size() == 1) {
    		for (REnemy enemy : this.enemies.values()) {
    			return enemy;
    		}
    	}
    	
    	return null;
    }
    
    protected double getDesiredPower(REnemy enemy) {
    	double energy = this.referenceBot.getEnergy();
    	if (energy < 30) {
			return 1.0;
    	}
    	
    	return 2.0;
    }

    protected void updateBullets() {
        List<RBullet> inactive = new ArrayList<RBullet>();
        for (RBullet bullet : bullets) {
            if (!bullet.active()) {
                inactive.add(bullet);
            }
        }
        bullets.removeAll(inactive);
    }
}
