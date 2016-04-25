package com.github.javachat.longrange;

import com.github.javachat.common.BoundType;

/**
 * Helper class {@link IntRange} to represent a combination of value and boundary type.
 * <p>
 * This class is used in cases where a boundary value and type from two ranges can be chosen.
 */
public class LongCut implements Comparable<LongCut> {
    private long value;
    private BoundType boundType;
    private boolean isBounded;

    LongCut(long value, BoundType boundType, boolean isBounded) {
        this.value = value;
        this.boundType = boundType;
        this.isBounded = isBounded;
    }

    long getValue() {
        return value;
    }

    BoundType getBoundType() {
        return boundType;
    }

    public boolean isBounded() {
        return isBounded;
    }

    static LongCut open(int value) {
        return new LongCut(value, BoundType.OPEN, true);
    }

    static LongCut closed(int value) {
        return new LongCut(value, BoundType.CLOSED, true);
    }

    static LongCut min(LongCut first, LongCut second) {
        return (first.value <= second.value) ? first : second;
    }

    static LongCut max(LongCut first, LongCut second) {
        return (first.value >= second.value) ? first : second;
    }

    @Override
    public int compareTo(LongCut o) {
        return Long.compare(this.value, o.value);
    }

}
