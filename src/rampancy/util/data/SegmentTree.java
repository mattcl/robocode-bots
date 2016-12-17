package rampancy.util.data;

import java.util.ArrayList;

import rampancy.util.RState;

public class SegmentTree {
    protected Node rootNode;

    public SegmentTree(ArrayList<? extends SegmentFn> segemntFns) {
        this.rootNode = new Node(0, segemntFns);
    }

    public Node getSegment(RState state) {
        return this.rootNode.getSegment(state);
    }

    class Node {
        protected GuessFactorArray stats;
        protected Node[] children;
        protected int visits;
        protected int splitThreshold;
        protected int depth;
        protected ArrayList<? extends SegmentFn> segmentFns;
        protected SegmentFn segmentFn;

        public Node(int depth, ArrayList<? extends SegmentFn> segmentFns) {
            this(depth, segmentFns, new GuessFactorArray());
        }

        public Node(int depth, ArrayList<? extends SegmentFn> segmentFns, GuessFactorArray stats) {
            this.depth = depth;
            this.children = null;
            this.stats = stats;
            this.segmentFns = segmentFns;
            this.segmentFn = segmentFns.get(depth);
        }

        public Node getSegment(RState state) {
            if (children == null) {
                return this;
            }
            int bin = segmentFn.segment(state);

            if (children[bin] == null) {
                Node child = new Node(depth + 1, segmentFns, stats.copy());
                children[bin] = child;
            }

            return children[bin].getSegment(state);
        }

        public void markVisit() {
            this.visits++;
            if (segmentFns != null && this.visits >= this.splitThreshold && depth < segmentFns.size() - 1) {
                this.split();
            }
        }

        protected void split() {
            this.children = new Node[segmentFn.numSegments()];
        }
    }
}
