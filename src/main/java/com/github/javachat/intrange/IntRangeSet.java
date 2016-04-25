package com.github.javachat.intrange;

import com.google.common.collect.ForwardingCollection;

import java.util.*;

/**
 * A set comprising of zero or more nonempty, disconnected IntRanges.
 */
public class IntRangeSet {
    private final NavigableMap<IntCut, IntRange> rangesByLowerBound;

    private IntRangeSet() {
        rangesByLowerBound = new TreeMap<>();
    }

    /**
     * @see <a href="http://google.github.io/guava/releases/19.0/api/docs/com/google/common/collect/TreeRangeSet.html#create()">Guava JavaDoc</a>
     */
    public static IntRangeSet create() {
        return new IntRangeSet();
    }

    /**
     * Helper method for creating IntRanges in tests.
     */
    static IntRangeSet create(IntRange... ranges) {
        IntRangeSet set = new IntRangeSet();

        for (IntRange range : ranges) {
            set.add(range);
        }

        return set;
    }

    /**
     * @see <a href="http://google.github.io/guava/releases/19.0/api/docs/com/google/common/collect/TreeRangeSet.html#add(com.google.common.collect.Range)">Guava Javadoc</a>
     */
    public void add(IntRange range) {
        if (range.isEmpty()) {
            return;
        }

        // Logic taken from Google's RangeSet code, slightly modified for their primitive counterpart.
        // See http://google.github.io/guava/releases/19.0/api/docs/src-html/com/google/common/collect/TreeRangeSet.html#line.149
        IntCut lowerCut = range.toLowerCut();
        IntCut upperCut = range.toUpperCut();

        Map.Entry<IntCut, IntRange> entryBelowLb = rangesByLowerBound.lowerEntry(lowerCut);
        if (entryBelowLb != null) {
            IntRange rangeBelowLb = entryBelowLb.getValue();
            if (rangeBelowLb.upperEndpoint() >= lowerCut.getValue()) {
                if (rangeBelowLb.upperEndpoint() >= upperCut.getValue()) {
                    upperCut = rangeBelowLb.toUpperCut();
                }

                lowerCut = rangeBelowLb.toLowerCut();
            }
        }

        Map.Entry<IntCut, IntRange> entryBelowUb = rangesByLowerBound.floorEntry(upperCut);
        if (entryBelowUb != null) {
            IntRange rangeBelowUB = entryBelowUb.getValue();

            if (rangeBelowUB.upperEndpoint() >= upperCut.getValue()) {
                upperCut = rangeBelowUB.toUpperCut();
            }
        }

        rangesByLowerBound.subMap(lowerCut, upperCut).clear();
        replaceRangeWithSameLowerBound(new IntRange(lowerCut, upperCut));
    }

    private void replaceRangeWithSameLowerBound(IntRange range) {
        if (range.isEmpty()) {
            rangesByLowerBound.remove(range.toLowerCut());
        } else {
            rangesByLowerBound.put(range.toLowerCut(), range);
        }
    }

    private class AsRanges extends ForwardingCollection<IntRange> implements Set<IntRange> {
        @Override
        protected Collection<IntRange> delegate() {
            return rangesByLowerBound.values();
        }
    }

    public Set<IntRange> asRanges() {
        return new AsRanges();
    }

    public boolean contains(int value) {
        return asRanges()
                .stream()
                .anyMatch(range -> range.contains(value));
    }
}
