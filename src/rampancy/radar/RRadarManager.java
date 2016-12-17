package rampancy.radar;

import java.util.HashMap;

import rampancy.RampantRobot;
import rampancy.util.REnemy;
import rampancy.util.RState;

import robocode.util.Utils;

public class RRadarManager {
    protected HashMap<String, RState> scans;
    protected RampantRobot referenceBot;

    public RRadarManager(RampantRobot referenceBot) {
        updateReferenceBot(referenceBot);
    }

    public void updateReferenceBot(RampantRobot referenceBot) {
        this.referenceBot = referenceBot;
        this.scans = new HashMap<String, RState>();
    }

    public void update(REnemy enemy) {
        RState state = enemy.currentState();
        if (state == null) {
            return;
        }
        scans.put(enemy.name, state);
    }

    public void execute() {
        referenceBot.setTurnRadarRightRadians(Double.POSITIVE_INFINITY);
        if (scans.size() == 1) {
            for (RState state : scans.values()) {
                double bearing = referenceBot.location().absoluteBearingTo(state.location);
                double radarTurn = bearing - referenceBot.getRadarHeadingRadians();
                referenceBot.setTurnRadarRightRadians(2.0 * Utils.normalRelativeAngle(radarTurn));
            }
        } else if (referenceBot.getRadarTurnRemaining() == 0.0) {
            referenceBot.setTurnRadarRightRadians(Double.POSITIVE_INFINITY);
        }
    }
}
