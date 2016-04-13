package com.github.javachat.doublerange;

import com.github.javachat.common.BoundType;

/**
 * Helper class {@link DoubleRange} to represent a combination of value and boundary type.
 * <p>
 * This class is used in cases where a boundary value and type from two ranges can be chosen.
 */
class DoubleCut {
    private double value;
    private BoundType boundType;

    DoubleCut(double value, BoundType boundType) {
        this.value = value;
        this.boundType = boundType;
    }

    double getValue() {
        return value;
    }

    BoundType getBoundType() {
        return boundType;
    }

    static DoubleCut open(double value) {
        return new DoubleCut(value, BoundType.OPEN);
    }

    static DoubleCut closed(double value) {
        return new DoubleCut(value, BoundType.CLOSED);
    }

    static DoubleCut min(DoubleCut first, DoubleCut second) {
        return (first.value <= second.value) ? first : second;
    }

    static DoubleCut max(DoubleCut first, DoubleCut second) {
        return (first.value >= second.value) ? first : second;
    }
}
