package rampancy;

import rampancy.util.RBattlefield;
import rampancy.util.RState;
import rampancy.util.RUtil;
import rampancy.util.data.SegmentFn;

public class SegmentFns {
    public static final int DISTANCE_SEGMENTS = 12;
    public static final int WALL_DISTANCE_SEGMENTS = 3;

    public static final SegmentFn DISTANCE = new SegmentFn() {
        public int numSegments() {
            return DISTANCE_SEGMENTS;
        }

        public int segment(RState state) {
            double normalized = RUtil.normalize(state.distance, 0, Const.MAX_SCAN_RANGE);
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
