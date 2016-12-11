package test.util;

import static org.junit.Assert.*;
import org.junit.*;

import rampancy.util.RPoint;

public class TestRPoint {
    RPoint point = new RPoint(1.0, 2.0);

    @Test
    public void constructor() {
        assertEquals("x not set correctly", 1.0, point.x, 0.0);
        assertEquals("y not set correctly", 2.0, point.y, 0.0);
    }

    @Test
    public void projecting() {
        double angle = 1.3;
        double distance = 11.2;
        RPoint expected = new RPoint(
            point.x + Math.sin(angle) * distance,
            point.y + Math.cos(angle) * distance
        );

        assertEquals("projected point was not expected", expected, point.projectTo(angle, distance));
    }
}
