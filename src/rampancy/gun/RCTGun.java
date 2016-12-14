package rampancy.gun;

import java.util.ArrayList;

import rampancy.util.RPoint;
import rampancy.util.REnemy;
import rampancy.util.RState;
import rampancy.util.RUtil;

public class RCTGun extends RGun {
    public static final int MAX_ITERATIONS = 100;

    protected REnemy lastScanned;

    public RCTGun() {

    }

    public void update(REnemy enemy) {

    }

    public RFiringSolution firingSolution() {
        if (this.lastScanned == null) {
            return null;
        }

        ArrayList<RPoint> examinedLocations = new ArrayList<RPoint>();
        RState state = lastScanned.currentState();

        if (state == null) {
            return null;
        }

        double velocity = state.velocity;
        double deltaH = state.deltaH;
        double curHeading = state.heading;
        RPoint targetLocation = state.location;

        double bestShotPower = state.distance < 20 ? 3.0 : 0.5;
        double maxShotPower = 2.5;

        int time = 1;
        for (; time < MAX_ITERATIONS; time++) {
            curHeading += deltaH;
            RPoint dest = targetLocation.projectTo(curHeading, velocity);
            double power RUtil.getRequiredPower(dest);
        }

        return null;
    }
}
