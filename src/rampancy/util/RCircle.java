package rampancy.util;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;

import rampancy.Const;

@SuppressWarnings("serial")
public class RCircle extends Ellipse2D.Double implements RDrawable {
    public RPoint center;
    public double radius;
    public Color color;
    public boolean filled;

    public RCircle(RPoint center, double radius) {
        this(center, radius, Const.DEFAULT_COLOR);
    }

    public RCircle(RPoint center, double radius, Color color) {
        this(center, radius, color, false);
    }

    public RCircle(RPoint center, double radius, Color color, boolean filled) {
        super(center.x - radius, center.y - radius, radius * 2, radius * 2);
        this.center = center.clone();
        this.radius = radius;
        this.color = color;
        this.filled = filled;
    }

    public void draw(Graphics2D g) {
        Color old = g.getColor();
        g.setColor(color);
        if (filled) {
            g.fill(this);
        } else {
            g.draw(this);
        }
        g.setColor(old);
    }
}
