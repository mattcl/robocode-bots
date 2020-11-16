package rampancy.gun;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;

import rampancy.util.RPoint;
import rampancy.util.RBattlefield;
import rampancy.util.RCircle;
import rampancy.util.RDrawable;
import rampancy.util.REnemy;
import rampancy.util.RState;
import rampancy.util.RUtil;

public class RCTGun extends RGun {
    public static final int MAX_ITERATIONS = 100;

    public RCTGun() {

    }

    public String name() {
        return "Circular Targeting Gun";
    }

    public void update(REnemy enemy) {
    	// no op
    }

    public RFiringSolution firingSolution(REnemy enemy, double desiredPower) {
        ArrayList<RPoint> examinedLocations = new ArrayList<RPoint>();
        RState state = enemy.currentState();

        if (state == null) {
            return null;
        }

        double velocity = state.velocity;
        double deltaH = state.deltaH;
        double curHeading = state.heading;
        RPoint targetLocation = state.location;

        int time = 1;
        double power = -1;
        for (; time < MAX_ITERATIONS; time++) {
            curHeading += deltaH;
            RPoint dest = targetLocation.projectTo(curHeading, velocity);
            power = RUtil.getRequiredPower(this.referenceBot.location(), dest, time);
            if (power > desiredPower) {
                break;
            }

            if (!RBattlefield.globalBattlefield().validBotLocation(dest)) {
                break;
            }
            
            targetLocation = dest;
            examinedLocations.add(dest);
            
			if (RUtil.almostEqual(power, desiredPower, 0.2)) {
				break;
			}

        }

        if (examinedLocations.isEmpty()) {
            return null;
        }
        
		if (RUtil.almostEqual(power, desiredPower, 0.2)) {
			double firingAngle = targetLocation.absoluteBearingFrom(this.referenceBot.location()) -
				this.referenceBot.getGunHeadingRadians();

			return new RFiringSolution(this, power, firingAngle, new Drawable(examinedLocations));
		}
		
		return null;
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
