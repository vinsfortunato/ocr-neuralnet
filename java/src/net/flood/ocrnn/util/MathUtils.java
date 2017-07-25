package net.flood.ocrnn.util;

/**
 * @author flood2d
 */
public final class MathUtils {
    /**
     * Calculates the image of x for the logistic function. (Sigmoid function)
     * More info at https://en.wikipedia.org/wiki/Sigmoid_function
     * @param x
     * @return the image of x for the logistic function.
     */
    public static double logistic(double x) {
        return 1.0D / (1.0D + Math.pow(Math.E, -x));
    }

    /**
     * Calculates the derivative of the logistic function at the given x.
     * More info at https://en.wikipedia.org/wiki/Sigmoid_function
     * @param x
     * @return the derivative at x for the logistic function.
     */
    public static double dLogistic(double x) {
        double image = logistic(x);
        return image * (1.0D - image);
    }

    public static <T extends Comparable<T>> T  clamp(T min, T max, T value) {
        if(value.compareTo(min) < 0) {
            return min;
        }
        if(value.compareTo(max) > 0) {
            return max;
        }
        return value;
    }
}
