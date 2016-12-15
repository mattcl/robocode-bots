package rampancy.util;

import robocode.ScannedRobotEvent;
import robocode.util.Utils;

import rampancy.RampantRobot;

public class RState {

    public RPoint location;
    public double distance;
    public double absoluteBearing;
    public double velocity;
    public double lateralVelocity;
    public double advancingVelocity;
    public double heading;
    public double deltaH;
    public double deltaV;
    public double energy;
    public double deltaE;
    public double adjustedDeltaE;
    public int directionTraveling;
    public long time;

    public RState() { }

    public RState(RampantRobot robot, ScannedRobotEvent e) {
        RState lastState = robot.currentState();
        this.location    = new RPoint(robot.getX(), robot.getY());
        this.heading     = robot.getHeadingRadians();
        this.velocity    = robot.getVelocity();
        this.energy      = robot.getEnergy();
        this.time        = e.getTime();

        this.deltaH      = lastState == null ? 0 : heading - lastState.heading;
        this.deltaV      = lastState == null ? 0 : velocity - lastState.velocity;
    }

    public RState(RampantRobot reference, RState lastState, ScannedRobotEvent e) {
        this.heading            = e.getHeadingRadians();
        this.distance           = e.getDistance();
        this.velocity           = e.getVelocity();
        this.energy             = e.getEnergy();
        this.time               = e.getTime();
        this.absoluteBearing    = Utils.normalAbsoluteAngle(reference.getHeadingRadians() + e.getBearingRadians());
        this.lateralVelocity    = velocity * Math.sin(heading - absoluteBearing);
        this.advancingVelocity  = velocity * -1 * Math.cos(heading - absoluteBearing);
        this.directionTraveling = lateralVelocity >= 0 ? 1 : -1;

        this.location          = reference.location().projectTo(absoluteBearing, distance);
        this.deltaH            = lastState == null ? 0 : heading - lastState.heading;
        this.deltaV            = lastState == null ? 0 : velocity - lastState.velocity;
        this.deltaE            = lastState == null ? 0 : energy - lastState.energy;
        this.adjustedDeltaE    = this.deltaE;
    }

    public RState(RState state) {
        this(
            state.location,
            state.distance,
            state.absoluteBearing,
            state.velocity,
            state.lateralVelocity,
            state.advancingVelocity,
            state.heading,
            state.deltaH,
            state.deltaV,
            state.energy,
            state.deltaE,
            state.adjustedDeltaE,
            state.directionTraveling,
            state.time
        );
    }

    public RState(
        RPoint location,
        double distance,
        double absoluteBearing,
        double velocity,
        double lateralVelocity,
        double advancingVelocity,
        double heading,
        double deltaH,
        double deltaV,
        double energy,
        double deltaE,
        double adjustedDeltaE,
        int directionTraveling,
        long time
    ) {
        this.location           = location.clone();
        this.distance           = distance;
        this.absoluteBearing    = absoluteBearing;
        this.velocity           = velocity;
        this.lateralVelocity    = lateralVelocity;
        this.advancingVelocity  = advancingVelocity;
        this.heading            = heading;
        this.deltaH             = deltaH;
        this.deltaV             = deltaV;
        this.energy             = energy;
        this.deltaE             = deltaE;
        this.adjustedDeltaE     = deltaE;
        this.directionTraveling = directionTraveling;
        this.time               = time;
    }

    public RState clone() {
        return new RState(this);
    }
}
