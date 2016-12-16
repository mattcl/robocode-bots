package rampancy;

import java.awt.Color;

import robocode.Rules;

public final class Const {
    // colors
    public static final Color DEFAULT_COLOR = Color.WHITE;
    public static final Color WAVE_COLOR = new Color(0x4B4B4B);

    public static final double BOT_RADIUS = 18.0;
    public static final double BOT_WIDTH = BOT_RADIUS * 2;
    public static final double WALL_STICK = 150.0;
    public static final double MAX_VELOCITY = Rules.MAX_VELOCITY;

    public static final int MAX_HISTORY_DEPTH = 1000;

    public static final double AGAINST_WALL_TOLERANCE = 150;
    public static final double NEAR_WALL_TOLERANCE = AGAINST_WALL_TOLERANCE + 150;

    public static final double MAX_BULLET_POWER = Rules.MAX_BULLET_POWER;
    public static final double MIN_BULLET_POWER = Rules.MIN_BULLET_POWER;
    public static final double MAX_BULLET_VELOCITY_BOUND = 20.0;

    public static final double WAVE_BREAK_TOLERANCE = 30.0;

    public static final int MAX_AVERAGE_EXISTING_WEIGHT = 200;

    public static final int GUESS_FACTOR_BUCKETS = 31;

    public static final double[] NORMAL_DISTRIBUTION = {
        1.9947114020071635,
        1.9481299055542936,
        1.8148111647014693,
        1.6125777293953802,
        1.3667389113125674,
        1.1049080724474742,
        0.8520055428517366,
        0.6266634709928329,
        0.43964488592992823,
        0.2942018217506561,
        0.18778650351213716,
        0.1143297041934262,
        0.06639409722892292,
        0.03677693621106399,
        0.01943109651692862,
        0.00979252654724092,
        0.004707256808773146,
        0.002158324363361749,
        0.0009439331102167652,
        0.00039376890059614726,
        0.00015668134544611742,
        5.946601793537889e-05,
        2.152762446680265e-05,
        7.433597573671502e-06
    };
}
