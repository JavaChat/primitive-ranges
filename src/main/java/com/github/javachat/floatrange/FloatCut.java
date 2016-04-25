package com.github.javachat.floatrange;

import com.github.javachat.common.BoundType;

/**
 * Helper class {@link FloatCut} to represent a combination of value and boundary type.
 * <p>
 * This class is used in cases where a boundary value and type from two ranges can be chosen.
 */
class FloatCut implements Comparable<FloatCut> {
    private float value;
    private BoundType boundType;

    FloatCut(float value, BoundType boundType) {
        this.value = value;
        this.boundType = boundType;
    }

    float getValue() {
        return value;
    }

    BoundType getBoundType() {
        return boundType;
    }

    static FloatCut open(float value) {
        return new FloatCut(value, BoundType.OPEN);
    }

    static FloatCut closed(float value) {
        return new FloatCut(value, BoundType.CLOSED);
    }

    static FloatCut min(FloatCut first, FloatCut second) {
        return (first.value <= second.value) ? first : second;
    }

    static FloatCut max(FloatCut first, FloatCut second) {
        return (first.value >= second.value) ? first : second;
    }

    @Override
    public int compareTo(FloatCut o) {
        return Double.compare(value, o.value);
    }
}
