package rampancy.util;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Point2D;
import java.awt.geom.Ellipse2D;

import robocode.util.Utils;

@SuppressWarnings("serial")
public class RPoint extends Point2D.Double implements RDrawable {
    public static final Color DEFAULT_COLOR = Color.WHITE;

    public Color color;

    public RPoint(double x, double y) {
        this(x, y, DEFAULT_COLOR);
    }

    public RPoint(double x, double y, Color color) {
        super(x, y);
        this.color = color;
    }

    public RPoint clone() {
        return (RPoint) this.clone();
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
        Color old = g.getColor();
        g.setColor(this.color);
        g.fill(new Ellipse2D.Double(x - 1, y - 1, 3.0, 3.0));
        g.setColor(old);
    }
}
