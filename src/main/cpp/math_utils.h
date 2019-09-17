#ifndef __MATH_UTILS_H
#define __MATH_UTILS_H

#include <math.h>
#include <stdlib.h>
#include <time.h>

/**
 * Calculates the image of x for the logistic function. (Sigmoid function)
 * More info at https://en.wikipedia.org/wiki/Sigmoid_function
 * @param x
 * @return the image of x for the logistic function.
 */
double logistic(double x);

/**
 * Calculates the derivative of the logistic function.
 * More info at https://en.wikipedia.org/wiki/Sigmoid_function
 * @param fx the image of the logistic function.
 * @return the derivative of f(x) for the logistic function.
 */
double dLogistic(double fx);

/**
 * Generate a random double between the given bounds.
 * @param lowerBound
 * @param upperBound
 * @return the random value.
 */
double random(const double &lowerBound, const double &upperBound);

#endif
