package rampancy.move;

import java.util.ArrayList;

import rampancy.util.RState;
import rampancy.util.data.SegmentFn;
import rampancy.util.data.SegmentTree;

public class RSurfer extends RMovement {
    protected SegmentTree segmentTree;

    public RSurfer() {
        ArrayList<SegmentFn> segmentFns = new ArrayList<SegmentFn>();
        segmentFns.add(new SegmentFn() {
            public int numSegments() {
                return 3;
            }

            public int segment(RState state) {
                return 0;
            }
        });
        this.segmentTree = new SegmentTree(segmentFns);
    }
}
