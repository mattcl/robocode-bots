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
}
