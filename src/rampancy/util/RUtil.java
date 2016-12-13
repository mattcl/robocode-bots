package rampancy.util;

import robocode.Rules;

import rampancy.Const;

public class RUtil {
    public static double getBulletPower(double velocity) {
        return Math.max(0.1, (20.0 - velocity) / 3.0);
    }

    public static double getBulletVelocity(double power) {
        return Rules.getBulletSpeed(power);
    }

    public static int sign(double d) {
        if (d < 0) {
            return -1;
        } else if (d > 0) {
            return 1;
        }
        return 0;
    }

    public static int nonZeroSign(double d) {
        if (d < 0) {
            return -1;
        }
        return 1;
    }
}
