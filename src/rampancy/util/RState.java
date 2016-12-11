package rampancy.util;

import robocode.ScannedRobotEvent;
import robocode.util.Utils;


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
    public int directionTraveling;

    public RState() { }

    public RState(RState referenceState, RState lastState, ScannedRobotEvent e) {
        this.heading           = e.getHeadingRadians();
        this.distance          = e.getDistance();
        this.velocity          = e.getVelocity();
        this.energy            = e.getEnergy();
        this.absoluteBearing   = Utils.normalAbsoluteAngle(referenceState.heading + e.getBearingRadians());
        this.lateralVelocity   = velocity * Math.sin(heading - absoluteBearing);
        this.advancingVelocity = velocity * -1 * Math.cos(heading - absoluteBearing);
        this.directionTraveling = lateralVelocity >= 0 ? 1 : -1;

        this.location          = referenceState.location.projectTo(absoluteBearing, distance);
        this.deltaH            = lastState == null ? 0 : heading - lastState.heading;
        this.deltaV            = lastState == null ? 0 : velocity - lastState.velocity;
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
            state.directionTraveling
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
        int directionTraveling
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
        this.directionTraveling = directionTraveling;
    }

    public RState clone() {
        return new RState(this);
    }
}
