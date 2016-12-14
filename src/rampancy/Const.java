package rampancy;

import java.awt.Color;

import robocode.Rules;

public final class Const {
    // colors
    public static final Color DEFAULT_COLOR = Color.WHITE;
    public static final Color WAVE_COLOR = new Color(0x757575);

    public static final double BOT_RADIUS = 18.0;
    public static final double BOT_WIDTH = BOT_RADIUS * 2;
    public static final double MAX_VELOCITY = Rules.MAX_VELOCITY;

    public static final int MAX_HISTORY_DEPTH = 1000;

    public static final double AGAINST_WALL_TOLERANCE = 150;
    public static final double NEAR_WALL_TOLERANCE = AGAINST_WALL_TOLERANCE + 150;

    public static final double MAX_BULLET_POWER = Rules.MAX_BULLET_POWER;
    public static final double MIN_BULLET_POWER = Rules.MIN_BULLET_POWER;
    public static final double MAX_BULLET_VELOCITY_BOUND = 20.0;
}
