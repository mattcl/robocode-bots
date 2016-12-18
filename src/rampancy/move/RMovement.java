package rampancy.move;

import rampancy.RampantRobot;
import rampancy.util.RDrawable;
import rampancy.util.REnemy;

import robocode.HitByBulletEvent;

abstract public class RMovement implements RDrawable {
    protected RampantRobot referenceBot;

    public void updateReferenceBot(RampantRobot referenceBot) {
        this.referenceBot = referenceBot;
    }

    abstract public void update(REnemy enemy);
    abstract public void onHitByBullet(HitByBulletEvent e);
    abstract public void execute();
}
