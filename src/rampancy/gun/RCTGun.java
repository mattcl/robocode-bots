package rampancy.gun;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;

import rampancy.util.RPoint;
import rampancy.util.RCircle;
import rampancy.util.RDrawable;
import rampancy.util.REnemy;
import rampancy.util.RState;
import rampancy.util.RUtil;

public class RCTGun extends RGun {
    public static final int MAX_ITERATIONS = 100;

    protected REnemy lastScanned;

    public RCTGun() {

    }

    public String name() {
        return "Circular Targeting Gun";
    }

    public void update(REnemy enemy) {
        this.lastScanned = enemy;
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
            double power = RUtil.getRequiredPower(this.referenceBot.location(), dest, time);
            if (power > maxShotPower) {
                break;
            }

            if (power > bestShotPower) {
                bestShotPower = power;
            }

            targetLocation = dest;
            examinedLocations.add(dest);
        }

        double firingAngle = targetLocation.absoluteBearingFrom(this.referenceBot.location()) -
            this.referenceBot.getGunHeadingRadians();

        return new RFiringSolution(bestShotPower, firingAngle, new Drawable(examinedLocations));
    }

    class Drawable implements RDrawable {
        private ArrayList<RPoint> locations;

        public Drawable(ArrayList<RPoint> locations) {
            this.locations = locations;
        }

        public void draw(Graphics2D g) {
            Color old = g.getColor();
            g.setColor(Color.white);
            for (RPoint p : locations) {
                p.draw(g);
            }

            RPoint last = locations.get(locations.size() - 1);
            new RCircle(last, 2).draw(g);
            g.setColor(old);
        }
    }
}
