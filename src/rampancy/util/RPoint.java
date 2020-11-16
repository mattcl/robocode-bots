package rampancy.util;

import java.awt.Graphics2D;
import java.awt.geom.Point2D;
import java.awt.geom.Ellipse2D;

import robocode.util.Utils;

@SuppressWarnings("serial")
public class RPoint extends Point2D.Double implements RDrawable {

    public RPoint(double x, double y) {
        super(x, y);
    }

    public RPoint(Point2D.Double point) {
        super(point.x, point.y);
    }

    public RPoint clone() {
        return (RPoint) super.clone();
    }

    public RPoint projectTo(double angle, double distance) {
        return new RPoint(
            x + Math.sin(angle) * distance,
            y + Math.cos(angle) * distance
        );
    }

    public double absoluteBearingTo(Point2D.Double target) {
        return Utils.normalAbsoluteAngle(
                Math.atan2(target.x - x, target.y - y));
    }

    public double absoluteBearingFrom(Point2D.Double target) {
        return Utils.normalAbsoluteAngle(
                Math.atan2(x - target.x, y - target.y));
    }

    public void draw(Graphics2D g) {
        g.fill(new Ellipse2D.Double(x, y, 2.0, 2.0));
    }
}
