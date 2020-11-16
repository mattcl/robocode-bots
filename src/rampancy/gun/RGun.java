package rampancy.gun;

import rampancy.RampantRobot;
import rampancy.util.REnemy;

abstract public class RGun {
    protected RampantRobot referenceBot;

    public void updateReferenceBot(RampantRobot referenceBot) {
        this.referenceBot = referenceBot;
    }
    
    public void shotFired(RBullet bullet) {
    	// no op by default
    }

    abstract public String name();
    abstract public void update(REnemy enemy);
    abstract public RFiringSolution firingSolution(REnemy enemy, double desiredPower);
}
