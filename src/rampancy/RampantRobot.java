package rampancy;

import java.util.LinkedList;

import rampancy.util.RState;
import rampancy.util.RStateful;

import robocode.AdvancedRobot;
import robocode.ScannedRobotEvent;

public class RampantRobot extends AdvancedRobot implements RStateful {
    protected LinkedList<RState> states;

    public RampantRobot() {
        super();
        states = new LinkedList<RState>();
    }

    public void run() {
        super.run();
        this.setAdjustGunForRobotTurn(true);
        this.setAdjustRadarForGunTurn(true);
        this.setAdjustRadarForRobotTurn(true);

        while(true) {

        }
    }

    public void onScannedRobot(ScannedRobotEvent e) {

    }

    public RState currentState() {
        if(states.isEmpty()) {
            return null;
        }
        return states.getFirst();
    }

    public RState lastState() {
        if(states.isEmpty()) {
            return null;
        }
        return states.get(1);
    }

    public RState lastUsableState() {
        if(states.size() < 3) {
            return lastState();
        }
        return states.get(2);
    }

    public RState updateState(ScannedRobotEvent e) {
        RState state = new RState(this, e);
        states.push(state);
        if(states.size() > Const.MAX_HISTORY_DEPTH) {
            states.removeLast();
        }
        return state;
    }
}
