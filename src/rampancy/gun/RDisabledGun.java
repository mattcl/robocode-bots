package rampancy.gun;

import rampancy.util.REnemy;
import rampancy.util.RState;

public class RDisabledGun extends RGun {
    protected REnemy lastScanned;

    public String name() {
        return "Disabled Robot Gun";
    }

    public void update(REnemy enemy) {
        this.lastScanned = enemy;
    }

    public RFiringSolution firingSolution() {
        RState state = lastScanned.currentState();
        if (state == null || state.energy > 0) {
            return null;
        }
        double firingAngle = state.location.absoluteBearingFrom(this.referenceBot.location()) -
            this.referenceBot.getGunHeadingRadians();

        return new RFiringSolution(0.1, firingAngle).withProbability(1);
    }
}
