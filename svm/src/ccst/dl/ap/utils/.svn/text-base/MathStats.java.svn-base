package utils;

import java.util.Arrays;
import java.util.Vector;

/**
 *
 * @author misiek (mw219725@gmail.com)
 */
public class MathStats {

    public static Double median(final Vector<Double> vector) {
        // Clone the input vector
        Double[] vectorCopy = new Double[vector.size()];
        int i = 0;
        for (Double el : vector) {
            vectorCopy[i] = Double.valueOf(el.doubleValue());
            i++;
        }

        // sort it
        Arrays.sort(vectorCopy);
        // Get the median
        int mid = vectorCopy.length / 2;
        if (vectorCopy.length % 2 == 1) {
            return (vectorCopy[mid].doubleValue());
        }
        return ((vectorCopy[mid - 1].doubleValue() + vectorCopy[mid].doubleValue()) / 2);

    }
}
