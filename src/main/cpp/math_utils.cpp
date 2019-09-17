#ifndef __MATH_UTILS_CPP
#define __MATH_UTILS_CPP

#include "math_utils.h"

double logistic(const double x) {
    return 1.0 / (1.0 + pow(M_E, -x));
}

double dLogistic(const double fx) {
    return fx * (1.0 - fx);
}

double random(const double &lowerBound, const double &upperBound) {
    return lowerBound + ((double) rand() / RAND_MAX) * (upperBound - lowerBound);
}

#endif