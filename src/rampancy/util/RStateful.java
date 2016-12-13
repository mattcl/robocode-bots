package rampancy.util;

import robocode.ScannedRobotEvent;

public interface RStateful {
    public RState updateState(ScannedRobotEvent e);
    public RState currentState();
    public RState lastState();
    public RState lastUsableState();
}
