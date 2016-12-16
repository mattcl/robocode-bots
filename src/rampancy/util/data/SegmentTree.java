package rampancy.util.data;

import java.util.ArrayList;

import rampancy.util.RState;

public class SegmentTree {
    protected Node rootNode;

    public SegmentTree() {
        this.rootNode = new Node();
    }

    public GuessFactorArray getSegment(RState state) {
        return this.rootNode.getSegment(state);
    }

    class Node {
        protected GuessFactorArray stats;
        protected ArrayList<Node> nodes;
        // protected SegmentFunction segmentFunction;
        protected int visits;

        public Node() {
            this.nodes = null;
            this.stats = new GuessFactorArray();
        }

        public GuessFactorArray getSegment(RState state) {
            return null;
        }
    }
}
