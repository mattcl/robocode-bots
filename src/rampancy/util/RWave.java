package rampancy.util;

import java.awt.Color;
import java.awt.Graphics2D;

import rampancy.Const;

public class RWave implements RDrawable {

    protected RPoint origin;
    protected Color color;
    protected long timeFired;
    protected double power;
    protected double velocity;
    protected double distanceTraveled;
    protected boolean hasBroken;

    public RWave(RPoint origin, long timeFired, double power, Color color) {
        this.origin = origin.clone();
        this.timeFired = timeFired;
        this.power = power;
        this.velocity = RUtil.getBulletVelocity(power);
        this.distanceTraveled = velocity;
        this.color = color;
        this.hasBroken = false;
    }

    public void update(long time) {
        this.distanceTraveled = (time - timeFired) * velocity;
    }

    public double distanceFrom(RPoint point) {
        return point.distance(origin) - distanceTraveled;
    }

    public double timeToImpact(RPoint point) {
        return distanceFrom(point) / velocity;
    }

    public void markBroken() {
        this.hasBroken = true;
    }

    public boolean hasBroken() {
        return hasBroken;
    }

    public void draw(Graphics2D g) {
        RCircle circle = new RCircle(origin, distanceTraveled, Const.WAVE_COLOR);
        circle.draw(g);
    }
}
