package rampancy.util;

import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;

@SuppressWarnings("serial")
public class RRect extends Rectangle2D.Double implements RDrawable {
    public RRect(RPoint center, double radius) {
        this(center.x, center.y, radius * 2, radius * 2);
    }

    public RRect(RPoint center, double width, double height) {
        this(center.x, center.y, width, height);
    }

    public RRect(double x, double y, double width, double height) {
        super(x, y, width, height);
    }

    public RPoint center() {
        return new RPoint(x + width / 2.0, y + height / 2.0);
    }

    public void draw(Graphics2D g) {
        g.draw(this);
    }
}
