package rampancy.util.data;

import rampancy.util.RState;

public interface SegmentFn {
    public int numSegments();
    public int segment(RState state);
}
