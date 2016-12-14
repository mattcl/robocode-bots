package rampancy.gun;

import rampancy.util.RPoint;

public class RFiringSolution {
    public double power;
    public double firingAngle;
    public RPoint targetLocation;
    public long flightTime;

    public RFiringSolution(double power, double firingAngle) {
        this.power = power;
        this.firingAngle = firingAngle;
    }
}
