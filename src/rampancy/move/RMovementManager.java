package rampancy.move;

import java.awt.Graphics2D;

import rampancy.RampantRobot;
import rampancy.util.RDrawable;
import rampancy.util.REnemy;
import rampancy.util.RWaveManager;

import robocode.util.Utils;

public class RMovementManager implements RDrawable {
    protected RampantRobot referenceBot;
    protected RWaveManager waveManager;

    public RMovementManager(RampantRobot referenceBot) {
        updateReferenceBot(referenceBot);
    }

    public void updateReferenceBot(RampantRobot referenceBot) {
        this.referenceBot = referenceBot;
        this.waveManager = new RWaveManager(referenceBot);
    }

    public void update(REnemy enemy) {
        this.waveManager.maybeAddForEnemy(enemy);
    }

    public void execute() {
        this.waveManager.update(this.referenceBot.getTime());
    }

    public void move(double goAngle, double dist) {
        double angle = Utils.normalRelativeAngle(goAngle - this.referenceBot.getHeadingRadians());
        if (Math.abs(angle) > (Math.PI/2)) {
            if (angle < 0) {
                referenceBot.setTurnRightRadians(Math.PI + angle);
            } else {
                referenceBot.setTurnLeftRadians(Math.PI - angle);
            }
            referenceBot.setBack(dist);
        } else {
            if (angle < 0) {
                referenceBot.setTurnLeftRadians(-angle);
            } else {
                referenceBot.setTurnRightRadians(angle);
            }
            referenceBot.setAhead(dist);
        }
    }

    public void draw(Graphics2D g) {
        this.waveManager.draw(g);
    }
}
