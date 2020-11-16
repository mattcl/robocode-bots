package rampancy.gun;

import rampancy.util.REnemy;
import rampancy.util.RState;

public class RDisabledGun extends RGun {
    public String name() {
        return "Disabled Robot Gun";
    }

    public void update(REnemy enemy) {
    	// no op
    }

    public RFiringSolution firingSolution(REnemy enemy, double desiredPower) {
        RState state = enemy.currentState();
        if (state == null || state.energy > 0) {
            return null;
        }
        double firingAngle = state.location.absoluteBearingFrom(this.referenceBot.location()) -
            this.referenceBot.getGunHeadingRadians();
        
        // a probability of 1.1 is not actually possible, but we want this to be
        // selected always for disabled enemies
        return new RFiringSolution(this, 0.1, firingAngle).withProbability(1.1);
    }
}
