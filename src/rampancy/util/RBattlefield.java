package rampancy.util;

import java.awt.Color;
import java.awt.Graphics2D;

import rampancy.Const;

public class RBattlefield implements RDrawable {
    protected static RBattlefield globalBattlefield;

    public static void setGlobalBattlefield(RBattlefield battlefield) {
        RBattlefield.globalBattlefield = battlefield;
    }

    public static RBattlefield globalBattlefield() {
        return RBattlefield.globalBattlefield;
    }

    public double width;
    public double height;
    public RRect battlefield;
    public RRect botBoundary;
    public RRect fuzzyBotBoundary;

    public RBattlefield(double width, double height) {
        this.width = width;
        this.height = height;
        this.battlefield = new RRect(0, 0, width, height);
        this.botBoundary = new RRect(
                Const.BOT_RADIUS,
                Const.BOT_RADIUS,
                width - Const.BOT_WIDTH,
                height - Const.BOT_WIDTH);
        this.fuzzyBotBoundary = new RRect(
                Const.BOT_RADIUS - 0.5,
                Const.BOT_RADIUS - 0.5,
                width - Const.BOT_WIDTH + 1,
                height - Const.BOT_WIDTH + 1);
    }

    public boolean validBotLocation(RPoint point) {
        return this.fuzzyBotBoundary.contains(point);
    }

    public double distanceFromTop(RPoint point) {
        return Math.abs(battlefield.height - point.y);
    }

    public double distanceFromBot(RPoint point) {
        return point.y;
    }

    public double distanceFromLeft(RPoint point) {
        return point.x;
    }

    public double distanceFromRight(RPoint point) {
        return Math.abs(battlefield.width - point.x);
    }

    public double innerDistanceFromTop(RPoint point) {
        return Math.max(1, distanceFromTop(point) - Const.BOT_RADIUS);
    }

    public double innerDistanceFromBot(RPoint point) {
        return Math.max(1, distanceFromBot(point) - Const.BOT_RADIUS);
    }

    public double innerDistanceFromRight(RPoint point) {
        return Math.max(1, distanceFromRight(point) - Const.BOT_RADIUS);
    }

    public double innerDistanceFromLeft(RPoint point) {
        return Math.max(1, distanceFromLeft(point) - Const.BOT_RADIUS);
    }

    public void draw(Graphics2D g) {
        Color old = g.getColor();
        g.setColor(Color.GRAY);
        g.draw(botBoundary);
        g.setColor(old);
    }
}
