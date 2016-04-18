package com.github.javachat.intrange;

import com.github.javachat.common.BoundType;

/**
 * Helper class {@link IntRange} to represent a combination of value and boundary type.
 * <p>
 * This class is used in cases where a boundary value and type from two ranges can be chosen.
 */
class IntCut {
    private int value;
    private BoundType boundType;
    private boolean isBounded;

    IntCut(int value, BoundType boundType, boolean isBounded) {
        this.value = value;
        this.boundType = boundType;
        this.isBounded = isBounded;
    }

    int getValue() {
        return value;
    }

    BoundType getBoundType() {
        return boundType;
    }

    public boolean isBounded() {
        return isBounded;
    }

    static IntCut open(int value) {
        return new IntCut(value, BoundType.OPEN, true);
    }

    static IntCut closed(int value) {
        return new IntCut(value, BoundType.CLOSED, true);
    }

    static IntCut min(IntCut first, IntCut second) {
        return (first.value <= second.value) ? first : second;
    }

    static IntCut max(IntCut first, IntCut second) {
        return (first.value >= second.value) ? first : second;
    }
}
