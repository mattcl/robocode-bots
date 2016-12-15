package rampancy.gun;

import rampancy.RampantRobot;
import rampancy.util.REnemy;

abstract public class RGun {
    protected RampantRobot referenceBot;

    public void updateReferenceBot(RampantRobot referenceBot) {
        this.referenceBot = referenceBot;
    }

    abstract public String name();
    abstract public void update(REnemy enemy);
    abstract public RFiringSolution firingSolution();
}
