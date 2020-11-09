package rampancy.move;

import java.awt.Graphics2D;

import rampancy.RampantRobot;
import rampancy.util.RDrawable;
import rampancy.util.REnemy;

import robocode.HitByBulletEvent;

public class RMovementManager implements RDrawable {
    protected RMovement movement;

    public RMovementManager() {
        this.movement = null;
    }
    
    public void setMovement(RMovement movement) {
    	this.movement = movement;
    }

    public void updateReferenceBot(RampantRobot referenceBot) {
        this.movement.updateReferenceBot(referenceBot);
    }

    public void update(REnemy enemy) {
        this.movement.update(enemy);
    }

    public void onHitByBullet(HitByBulletEvent e) {
        this.movement.onHitByBullet(e);
    }

    public void execute() {
        this.movement.execute();
    }

    public void draw(Graphics2D g) {
        this.movement.draw(g);
    }
}
