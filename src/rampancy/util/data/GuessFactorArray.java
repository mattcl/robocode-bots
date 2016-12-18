package rampancy.util.data;

import java.util.Arrays;

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

    public GuessFactorArray(double[] probabilities) {
        this.probabilities = probabilities;
    }

    public GuessFactorArray copy() {
        GuessFactorArray cp = new GuessFactorArray(Arrays.copyOf(probabilities, probabilities.length));
        cp.visits = visits;
        return cp;
    }

    public void update(double guessFactor) {
        this.visits++;
        int length = Const.NORMAL_DISTRIBUTION.length;
        int bin = computeBin(guessFactor);
        double[] newData = new double[probabilities.length];
        for (int i = 0; i < length; i++) {
            if (i == 0) {
                newData[bin] += Const.NORMAL_DISTRIBUTION[0];
            } else {
                int left = bin - i;
                int right = bin + i;
                if (left >= 0) {
                    newData[left] += Const.NORMAL_DISTRIBUTION[i];
                }

                if (right < length) {
                    newData[right] += Const.NORMAL_DISTRIBUTION[i];
                }

                if (left < 0 && right >= length) {
                    break;
                }
            }
        }

        int weight = Math.min(visits, Const.MAX_AVERAGE_EXISTING_WEIGHT);

        for (int i = 0; i < newData.length; i++) {
            probabilities[i] = RUtil.rollingAverage(probabilities[i], newData[i], weight);
        }
    }

    public double bestFactor() {
        int max = -1;
        double maxValue = -1;
        for (int i = 0; i < probabilities.length; i++) {
            double val = probabilities[i];
            if (val > maxValue) {
                max = i;
                maxValue = val;
            }
        }
        return computeGuessFactor(max);
    }

    public int computeBin(double guessFactor) {
        return (int)RUtil.limit((guessFactor + 1) * ((probabilities.length - 1) / 2), 0, probabilities.length - 1);
    }

    public double computeGuessFactor(int bin) {
        return RUtil.guessFactor(bin, probabilities.length);
    }

    public GuessFactorArray sum(GuessFactorArray other) {
        GuessFactorArray result = this.copy();
        for (int i = 0; i < result.probabilities.length; i++) {
            result.probabilities[i] += other.probabilities[i];
        }
        return result;
    }

    public GuessFactorArray avearge(GuessFactorArray other, double otherWeight) {
        GuessFactorArray result = this.copy();
        for (int i = 0; i < result.probabilities.length; i++) {
            result.probabilities[i] =
                (result.probabilities[i] + (other.probabilities[i] * otherWeight)) / (1.0 + otherWeight);
        }
        return result;
    }
}
