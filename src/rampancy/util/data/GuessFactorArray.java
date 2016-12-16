package rampancy.util.data;

import rampancy.Const;
import rampancy.util.RUtil;

public class GuessFactorArray {
    public int visits;
    public double[] probabilities;

    public GuessFactorArray() {
        this(Const.GUESS_FACTOR_BUCKETS);
    }

    public GuessFactorArray(int num_buckets) {
        this.probabilities = new double[num_buckets];
    }

    public void update(double guessFactor) {
        this.visits++;

    }

    public int computeBin(double guessFactor) {
        return (int)RUtil.limit((guessFactor + 1) * ((probabilities.length - 1) / 2), 0, probabilities.length - 1);
    }
}
