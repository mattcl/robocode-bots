package rampancy.util;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.LinkedList;

import rampancy.Const;
import rampancy.RampantRobot;

import robocode.ScannedRobotEvent;

public class REnemy implements RStateful, RDrawable {
    public String name;
    protected RampantRobot referenceBot;
    protected LinkedList<RState> states;
    protected LinkedList<Double> preScanEnergyAdjustments;

    public REnemy(String name, RampantRobot referenceBot) {
        this.name = name;
        this.referenceBot = referenceBot;
        this.states = new LinkedList<RState>();
        this.preScanEnergyAdjustments = new LinkedList<Double>();
    }

    public void updateReferenceBot(RampantRobot referenceBot) {
        this.referenceBot = referenceBot;
        this.preScanEnergyAdjustments = new LinkedList<Double>();
    }

    public RPoint location() {
        RState currentState = currentState();
        if (currentState == null) {
            return null;
        }
        return currentState().location;
    }

    public RRect botRect() {
        return new RRect(this.location(), Const.BOT_RADIUS);
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

    public void notePreSecanAdjustement(double val) {
        this.preScanEnergyAdjustments.add(val);
    }

    public RState updateState(ScannedRobotEvent e) {
        RState state = new RState(referenceBot, currentState(), e);

        // adjust for bullet hits and energy gain
        while (!preScanEnergyAdjustments.isEmpty()) {
            double adjustment = preScanEnergyAdjustments.pop();
            state.adjustedDeltaE += adjustment;
        }
        states.push(state);
        if(states.size() > Const.MAX_HISTORY_DEPTH) {
            states.removeLast();
        }
        return state;
    }

    public void draw(Graphics2D g) {
        Color old = g.getColor();
        g.setColor(Const.DEFAULT_COLOR);
        this.botRect().draw(g);
        g.setColor(old);
    }
}
