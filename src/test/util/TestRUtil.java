package test.util;

import static org.junit.Assert.*;
import org.junit.*;

import rampancy.util.RUtil;

public class TestRUtil {
    @Test
    public void signCalculations() {
        assertEquals(RUtil.sign(2.1), 1);
        assertEquals(RUtil.sign(0), 0);
        assertEquals(RUtil.sign(-3.2), -1);
    }

    @Test
    public void nonZeroSignCalculations() {
        assertEquals(RUtil.nonZeroSign(2.1), 1);
        assertEquals(RUtil.nonZeroSign(0), 1);
        assertEquals(RUtil.nonZeroSign(-3.2), -1);
    }

    @Test
    public void limiting() {
        assertEquals(RUtil.limit(1.0, 0.5, 1.5), 1.0, 0.0);
        assertEquals(RUtil.limit(1.0, 0.5, 1.0), 1.0, 0.0);
        assertEquals(RUtil.limit(1.0, 1.0, 1.5), 1.0, 0.0);
        assertEquals(RUtil.limit(1.0, 0.5, 0.9), 0.9, 0.0);
        assertEquals(RUtil.limit(1.0, 1.2, 1.5), 1.2, 0.0);
        assertEquals(RUtil.limit(1.0, 1.0, 1.0), 1.0, 0.0);
    }

    @Test
    public void normalizing() {
        // Easy stuff
        assertEquals(RUtil.normalize(0, 0, 100), 0.0, 0.0);
        assertEquals(RUtil.normalize(100, 0, 100), 1.0, 0.0);
        assertEquals(RUtil.normalize(50, 0, 100), 0.5, 0.0);
        assertEquals(RUtil.normalize(25, 0, 100), 0.25, 0.0);
        assertEquals(RUtil.normalize(75, 0, 100), 0.75, 0.0);

        // Non 0-100 range
        assertEquals(RUtil.normalize(100, 0, 200), 0.5, 0.0);

        // limits
        assertEquals(RUtil.normalize(-1, 0, 100), 0.0, 0.0);
        assertEquals(RUtil.normalize(101, 0, 100), 1.0, 0.0);

        // nonstandard ranges
        assertEquals(RUtil.normalize(50, 50, 100), 0.0, 0.0);
        assertEquals(RUtil.normalize(75, 50, 100), 0.5, 0.0);
        assertEquals(RUtil.normalize(100, 50, 100), 1.0, 0.0);

        assertEquals(RUtil.normalize(-50, -50, 50), 0.0, 0.0);
        assertEquals(RUtil.normalize(0, -50, 50), 0.5, 0.0);
        assertEquals(RUtil.normalize(50, -50, 50), 1.0, 0.0);

        assertEquals(RUtil.normalize(-25, -25, 75), 0.0, 0.0);
        assertEquals(RUtil.normalize(25, -25, 75), 0.5, 0.0);
        assertEquals(RUtil.normalize(75, -25, 75), 1.0, 0.0);
    }

    @Test
    public void gettingIndexFromNormalized() {
        assertEquals(RUtil.normalizedToIndex(0.0, 10), 0);
        assertEquals(RUtil.normalizedToIndex(0.73, 10), 7);
        assertEquals(RUtil.normalizedToIndex(0.29, 10), 2);

        // At the edge we should return the max index instead of the size
        assertEquals(RUtil.normalizedToIndex(1.0, 10), 9);
    }
}
