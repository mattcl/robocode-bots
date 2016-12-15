package rampancy.gun;

import java.awt.Graphics2D;

import rampancy.util.RDrawable;
import rampancy.util.RPoint;

public class RFiringSolution implements RDrawable {
    public double power;
    public double firingAngle;
    public RPoint targetLocation;
    public long flightTime;
    public RDrawable drawable;
    public double hitProbability;

    public RFiringSolution(double power, double firingAngle) {
        this(power, firingAngle, null);
    }

    public RFiringSolution(double power, double firingAngle, RDrawable drawable) {
        this.power = power;
        this.firingAngle = firingAngle;
        this.drawable = drawable;
    }

    public RFiringSolution withProbability(double probability) {
        this.hitProbability = probability;
        return this;
    }

    public void draw(Graphics2D g) {
        if (drawable != null) {
            drawable.draw(g);
        }
    }
}
