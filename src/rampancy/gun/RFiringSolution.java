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

    public RFiringSolution(double power, double firingAngle) {
        this.power = power;
        this.firingAngle = firingAngle;
    }

    public RFiringSolution(double power, double firingAngle, RDrawable drawable) {
        this.power = power;
        this.firingAngle = firingAngle;
        this.drawable = drawable;
    }

    public void draw(Graphics2D g) {
        if (drawable != null) {
            drawable.draw(g);
        }
    }
}
