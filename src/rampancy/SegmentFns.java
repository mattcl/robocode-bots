package rampancy;

import rampancy.util.RBattlefield;
import rampancy.util.RState;
import rampancy.util.RUtil;
import rampancy.util.data.SegmentFn;

public class SegmentFns {
    public static final int ACCELERATING_SEGMENTS = 3;
    public static final int DELTA_H_SEGMENTS = 5;
    public static final int DISTANCE_SEGMENTS = 12;
    public static final int LATERAL_VELOCITY_SEGMENTS = 7;
    public static final int WALL_DISTANCE_SEGMENTS = 3;

    public static final SegmentFn ACCELERATING = new SegmentFn() {
        public int numSegments() {
            return ACCELERATING_SEGMENTS;
        }

        public int segment(RState state) {
            int val = (int) Math.round(3 * state.deltaV);
            if (val < 0) {
                return 1;
            }

            if (val > 0) {
                return 2;
            }

            return 0;
        }
    };

    public static final SegmentFn DELTA_H = new SegmentFn() {
        public int numSegments() {
            return WALL_DISTANCE_SEGMENTS;
        }

        public int segment(RState state) {
            double normalized = RUtil.normalize(state.distance, -Const.MAX_DELTA_H, Const.MAX_DELTA_H);
            return RUtil.normalizedToIndex(normalized, numSegments());
        }
    };

    public static final SegmentFn DISTANCE = new SegmentFn() {
        public int numSegments() {
            return DISTANCE_SEGMENTS;
        }

        public int segment(RState state) {
            double normalized = RUtil.normalize(state.distance, 0, Const.MAX_SCAN_RANGE);
            return RUtil.normalizedToIndex(normalized, numSegments());
        }
    };

    public static final SegmentFn LATERAL_VELOCITY = new SegmentFn() {
        public int numSegments() {
            return LATERAL_VELOCITY_SEGMENTS;
        }

        public int segment(RState state) {
            double normalized = RUtil.normalize(state.lateralVelocity, -Const.MAX_VELOCITY, Const.MAX_VELOCITY);
            return RUtil.normalizedToIndex(normalized, numSegments());
        }
    };

    // We split distance from a wall into 3 categories
    // (against, near, away)
    public static final SegmentFn WALL_DISTANCE = new SegmentFn() {
        public int numSegments() {
            return WALL_DISTANCE_SEGMENTS;
        }

        public int segment(RState state) {
            return RBattlefield.globalBattlefield().locationCategory(state.location);
        }
    };
}
