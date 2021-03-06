package rampancy.util;

import robocode.Rules;
import robocode.util.Utils;

import java.util.ArrayList;

import rampancy.Const;
import rampancy.extern.MoveSim;
import rampancy.move.RMoveChoice;
import rampancy.util.data.GuessFactorArray;

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

    public static boolean almostEqual(double value1, double value2, double tolerance) {
        return Math.abs(value1 - value2) <= tolerance;
    }

    public static double limit(double value, double min, double max) {
        return Math.max(min, Math.min(value, max));
    }

    public static double normalize(double value, double valueMin, double valueMax) {
        double range = valueMax - valueMin;
        double limited = limit(value, valueMin, valueMax);
        return (limited - valueMin) / range;
    }

    public static int normalizedToIndex(double normalizedValue, int numIndices) {
        int index = (int) (normalizedValue * numIndices);
        if (index == numIndices) {
            index--;
        }
        return index;
    }

    public static double rollingAverage(double existing, double newValue, int n) {
        return (existing * n + newValue) / ((double)n + 1.0);
    }

    public static double guessFactor(int bin, int num_bins) {
        int offset = bin - (num_bins - 1) / 2;
        return (double) offset / (double) ((num_bins - 1) / 2);
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
    
    public static double getDistanceModifier(RPoint botLocation, RPoint center) {
    	double dist = botLocation.distance(center);
    	if (dist < 200) {
    		// danger zone
    		return 0.875;
    	} else if (dist < 400) {
    		return 0.175 / 3.0;
    	} else if (dist > 500) {
    		return -0.175 / 3.0;
    	}
    	return 0.0;
    }
    
    public static double getOrbitAngle(RPoint botLocation, RPoint center, int direction) {
    	double distanceModifier = RUtil.getDistanceModifier(botLocation, center);
    	return RUtil.getOrbitAngle(botLocation, center, direction, distanceModifier);
    }
    
    public static double getOrbitAngle(RPoint botLocation, RPoint center, int direction, double distanceModifier) {
        double distance = botLocation.distance(center);
        // just make sure we don't have a dumb value here
        direction = nonZeroSign(direction);

        double orbitAngle = center.absoluteBearingTo(botLocation) + ((Math.PI / 2.0 - distanceModifier) * direction);
        
        
        return wallSmoothing(botLocation, orbitAngle, direction, distance);
    }

    public static void adjustSimForOrbit(MoveSim sim, RPoint center, int direction) {
        RPoint botLocation = new RPoint(sim.position);
        double orbitAngle = RUtil.getOrbitAngle(botLocation, center, direction);

        sim.angleToTurn = Utils.normalRelativeAngle(orbitAngle - sim.heading);
        sim.direction = 1;

        if (Math.abs(sim.angleToTurn) > Math.PI / 2) {
            sim.angleToTurn = Utils.normalRelativeAngle(sim.angleToTurn - Math.PI);
            sim.direction = -1;
        }
    }
    
    public static MoveSim makeMoveSim(RState robotState) {
        RPoint botLocation = robotState.location;
        MoveSim sim = new MoveSim();
        sim.setLocation(botLocation.x, botLocation.y);
        sim.heading = robotState.heading;
        sim.velocity = robotState.velocity;
        return sim;
    }

    public static RPoint simulateOrbit(MoveSim sim, RPoint center, int direction) {
        adjustSimForOrbit(sim, center, direction);
        sim.step();
        return new RPoint(sim.position);
    }
    
    public static ArrayList<RMoveChoice> simulateDirection(long startTime, RState rootState, RWave wave, int direction) {
    	wave = wave.copy();
    	ArrayList<RMoveChoice> locations = new ArrayList<RMoveChoice>();
    	
        MoveSim sim = RUtil.makeMoveSim(rootState);
        
        for(int i = 0; i < 1000; i++) {
        	RPoint loc = RUtil.simulateOrbit(sim, wave.origin, direction);
        	RMoveChoice location = new RMoveChoice(loc, -1, direction, startTime + i);
        	location.wave = wave;
			locations.add(location);
			
			wave.tick();
			if (wave.hasBroken(location.position)) {
				break;
			}
        }
    	return locations;
    }
    
    public static double roughMaxEscapeAngle(double velocity) {
        return Math.asin(8.0 / velocity);
    }

    public static double maxEscapeAngle(int direction) {
    	return 0.0;
    }
}
