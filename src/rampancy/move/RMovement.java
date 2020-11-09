package rampancy.move;

import rampancy.RampantRobot;
import rampancy.util.RDrawable;
import rampancy.util.REnemy;

import robocode.HitByBulletEvent;
import robocode.util.Utils;

abstract public class RMovement implements RDrawable {
    protected RampantRobot referenceBot;
    
    public void updateReferenceBot(RampantRobot referenceBot) {
        this.referenceBot = referenceBot;
    }
    
    public RampantRobot getReferenceBot() {
    	return this.referenceBot;
    }

    public void move(double goAngle, double dist) {
        double angle = Utils.normalRelativeAngle(goAngle - this.referenceBot.getHeadingRadians());
        if (Math.abs(angle) > (Math.PI/2)) {
            if (angle < 0) {
                this.referenceBot.setTurnRightRadians(Math.PI + angle);
            } else {
                this.referenceBot.setTurnLeftRadians(Math.PI - angle);
            }
            this.referenceBot.setBack(dist);
        } else {
            if (angle < 0) {
                this.referenceBot.setTurnLeftRadians(-angle);
            } else {
                this.referenceBot.setTurnRightRadians(angle);
            }
            this.referenceBot.setAhead(dist);
        }
    }
    
    abstract public void update(REnemy enemy);
    abstract public void onHitByBullet(HitByBulletEvent e);
    abstract public void execute();
}
