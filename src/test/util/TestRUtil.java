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
}
