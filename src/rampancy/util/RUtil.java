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

	public static double getRequiredPower(RPoint origin, RPoint target, int time) {
		double velocity = origin.distance(target) / time;

		if (velocity >= Const.MAX_BULLET_VELOCITY_BOUND) {
			return -1;
		}

		double power = getBulletPower(velocity);

		if (power > Const.MAX_BULLET_POWER) {
			return -1;
		}

		return power;
	}

	public static double getBulletDamage(double power) {
		return Rules.getBulletDamage(power);
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

	public static double limit(double value, double min, double max) {
		return Math.max(min, Math.min(value, max));
	}

	public static double wallSmoothing(RPoint location, double goAngle, int direction, double distanceToCenterOfOrbit) {
		RBattlefield bf = RBattlefield.globalBattlefield();
		double wallStick = Math.min(distanceToCenterOfOrbit, Const.WALL_STICK);

		RPoint projectedLocation = location.projectTo(goAngle, wallStick);
		if(bf.validBotLocation(projectedLocation))
			return goAngle; // no change needed

		double topDist   = bf.innerDistanceFromTop(location);
		double rightDist = bf.innerDistanceFromRight(location);
		double leftDist  = bf.innerDistanceFromLeft(location);
		double botDist   = bf.innerDistanceFromBot(location);

		boolean top   = topDist <= wallStick;
		boolean bot   = botDist <= wallStick;
		boolean right = rightDist <= wallStick;
		boolean left  = leftDist <= wallStick;

		boolean clockwise = direction > 0;

		boolean smoothTop   = top   && (!(right || left) || (right && !clockwise) || (left  && clockwise));
		boolean smoothBot   = bot   && (!(right || left) || (left  && !clockwise) || (right && clockwise));
		boolean smoothRight = right && (!(top   || bot)  || (bot   && !clockwise) || (top   && clockwise));
		boolean smoothLeft  = left  && (!(top   || bot)  || (top   && !clockwise) || (bot   && clockwise));

		double newAngle;

		double tolerance = 2;

		if(smoothTop) {
			newAngle = (topDist < tolerance ? Math.PI / 2 : Math.acos(topDist / wallStick)) * direction;

		} else if(smoothBot) {
			newAngle = (botDist < tolerance ? Math.PI / 2 : Math.acos(botDist / wallStick)) * direction + Math.PI;
		} else if(smoothRight) {
			newAngle = (rightDist < tolerance ? Math.PI / 2 : Math.acos(rightDist / wallStick)) * direction + Math.PI / 2;
		} else if(smoothLeft) {
			newAngle = (leftDist < tolerance ? Math.PI / 2 : Math.acos(leftDist / wallStick)) * direction + 3 * Math.PI / 2;
		} else {
			System.err.println("Smoothing Error!");
			return 0;
		}
		return newAngle;
	}
}
