package com.github.javachat.longrange;

import com.github.javachat.common.BoundType;
import com.github.javachat.common.Constants;

import java.util.Arrays;
import java.util.Objects;
import java.util.function.LongPredicate;

public final class LongRange {
    static final String ILLEGAL_BOUNDS = "lower bound %d must be less than or"
            + " equal to upper bound %d";

    static final String ILLEGAL_OPEN_RANGE = "when specifying an open range, "
            + "the lower bound must be strictly less than the upper bound";


    private final BoundType lowerBoundType;
    private final BoundType upperBoundType;
    private final long lowerBound;
    private final long upperBound;
    private final LongPredicate lowerCheck;
    private final LongPredicate upperCheck;
    private final boolean hasLowerBound;
    private final boolean hasUpperBound;

    /**
     * @see <a href="http://google.github.io/guava/releases/19.0/api/docs/com/google/common/collect/Range.html#open(C, C)">Guava JavaDoc</a>
     */
    public static LongRange open(final int lowerBound, final int upperBound) {
        return new LongRange(lowerBound, BoundType.OPEN, upperBound, BoundType.OPEN);
    }

    /**
     * @see <a href="http://google.github.io/guava/releases/19.0/api/docs/com/google/common/collect/Range.html#closed(C, C)">Guava JavaDoc</a>
     */
    public static LongRange closed(final int lowerBound, final int upperBound) {
        return new LongRange(lowerBound, BoundType.CLOSED, upperBound, BoundType.CLOSED);
    }

    /**
     * @see <a href="http://google.github.io/guava/releases/19.0/api/docs/com/google/common/collect/Range.html#closedOpen(C, C)">Guava JavaDoc</a>
     */
    public static LongRange closedOpen(final int lowerBound, final int upperBound) {
        return new LongRange(lowerBound, BoundType.CLOSED, upperBound, BoundType.OPEN);
    }

    /**
     * @see <a href="http://google.github.io/guava/releases/19.0/api/docs/com/google/common/collect/Range.html#openClosed(C, C)">Guava JavaDoc</a>
     */
    public static LongRange openClosed(final int lowerBound, final int upperBound) {
        return new LongRange(lowerBound, BoundType.OPEN, upperBound, BoundType.CLOSED);
    }

    /**
     * @see <a href="http://google.github.io/guava/releases/19.0/api/docs/com/google/common/collect/Range.html#range(C, com.google.common.collect.BoundType, C, com.google.common.collect.BoundType)">Guava JavaDoc</a>
     */
    public static LongRange range(final int lowerBound, final BoundType lowerBoundType,
                                  final int upperBound, final BoundType upperBoundType) {
        return new LongRange(lowerBound, lowerBoundType, upperBound, upperBoundType);
    }

    /**
     * @see <a href="http://google.github.io/guava/releases/19.0/api/docs/com/google/common/collect/Range.html#lessThan(C)">Guava JavaDoc</a>
     */
    public static LongRange lessThan(final int endpoint) {
        return LongRange.upTo(endpoint, BoundType.CLOSED);
    }

    /**
     * @see <a href="http://google.github.io/guava/releases/19.0/api/docs/com/google/common/collect/Range.html#atMost(C)">Guava JavaDoc</a>
     */
    public static LongRange atMost(final int endpoint) {
        return LongRange.upTo(endpoint, BoundType.OPEN);
    }

    /**
     * @see <a href="http://google.github.io/guava/releases/19.0/api/docs/com/google/common/collect/Range.html#upTo(C, com.google.common.collect.BoundType)">Guava JavaDoc</a>
     */
    public static LongRange upTo(final int endpoint, final BoundType boundType) {
        // Lower/Higher bounds for (partially) unbounded ranges are ignored, values are purely symbolical.
        return new LongRange(Integer.MIN_VALUE, BoundType.OPEN, false, endpoint, boundType, true);
    }

    /**
     * @see <a href="http://google.github.io/guava/releases/19.0/api/docs/com/google/common/collect/Range.html#greaterThan(C)">Guava JavaDoc</a>
     */
    public static LongRange greaterThan(final int endpoint) {
        return LongRange.downTo(endpoint, BoundType.CLOSED);
    }

    /**
     * @see <a href="http://google.github.io/guava/releases/19.0/api/docs/com/google/common/collect/Range.html#atLeast(C)">Guava JavaDoc</a>
     */
    public static LongRange atLeast(final int endpoint) {
        return LongRange.downTo(endpoint, BoundType.OPEN);
    }

    /**
     * @see <a href="http://google.github.io/guava/releases/19.0/api/docs/com/google/common/collect/Range.html#downTo(C, com.google.common.collect.BoundType)">Guava JavaDoc</a>
     */
    public static LongRange downTo(final int endpoint, final BoundType boundType) {
        return new LongRange(endpoint, boundType, true, Integer.MAX_VALUE, BoundType.OPEN, false);
    }

    /**
     * @see <a href="http://google.github.io/guava/releases/19.0/api/docs/com/google/common/collect/Range.html#all()">Guava JavaDoc</a>
     */
    public static LongRange all() {
        return new LongRange(Integer.MIN_VALUE, BoundType.OPEN, false, Integer.MAX_VALUE, BoundType.OPEN, false);
    }

    /**
     * @see <a href="http://google.github.io/guava/releases/19.0/api/docs/com/google/common/collect/Range.html#singleton(C)">Guava JavaDoc</a>
     */
    public static LongRange singleton(final int value) {
        return LongRange.closed(value, value);
    }

    /**
     * @see <a href="http://google.github.io/guava/releases/19.0/api/docs/com/google/common/collect/Range.html#encloseAll(java.lang.Iterable)">Guava JavaDoc</a>
     */
    public static LongRange encloseAll(int... values) {
        // Create sorted copy, lowest and highest values are the endpoints
        int[] copy = Arrays.copyOf(values, values.length);
        Arrays.sort(copy);
        return LongRange.closed(copy[0], copy[copy.length - 1]);
    }

    /**
     * @param lowerBound     The value for the lower bound. Will be ignored if <code>hasLowerBound == false</code>
     * @param lowerBoundType The type for the lower bound. Will be ignored if <code>hasLowerBound == false</code>
     * @param hasLowerBound  Whether or not the range has a lower bound.
     * @param upperBound     The value for the upper bound. Will be ignored if <code>hasUpperBound == false</code>
     * @param upperBoundType The type for the upper bound. Will be ignored if <code>hasUpperBound == false</code>
     * @param hasUpperBound  Whether or not the range has a upper bound.
     */
    LongRange(final long lowerBound, final BoundType lowerBoundType, final boolean hasLowerBound,
              final long upperBound, final BoundType upperBoundType, final boolean hasUpperBound) {
        if (Long.compare(lowerBound, upperBound) > 0)
            throw new IllegalArgumentException(String.format(ILLEGAL_BOUNDS,
                    lowerBound, upperBound));

        if (lowerBound == upperBound
                && lowerBoundType == BoundType.OPEN
                && lowerBoundType == upperBoundType)
            throw new IllegalArgumentException(ILLEGAL_OPEN_RANGE);

        this.lowerBound = lowerBound;
        this.lowerBoundType = Objects.requireNonNull(lowerBoundType);
        this.hasLowerBound = hasLowerBound;
        this.upperBound = upperBound;
        this.upperBoundType = Objects.requireNonNull(upperBoundType);
        this.hasUpperBound = hasUpperBound;

        if (hasLowerBound) {
            lowerCheck = lowerBoundType == BoundType.OPEN
                    ? value -> value >= lowerBound
                    : value -> value > lowerBound;
        } else {
            lowerCheck = (value -> true);
        }

        if (hasUpperBound) {
            upperCheck = upperBoundType == BoundType.OPEN
                    ? value -> value <= upperBound
                    : value -> value < upperBound;
        } else {
            upperCheck = (value -> true);
        }
    }

    /**
     * Convenience constructor for ranges that have both lower and upper bounds
     */
    LongRange(final int lowerBound, final BoundType lowerBoundType,
              final int upperBound, final BoundType upperBoundType) {
        this(lowerBound, lowerBoundType, true, upperBound, upperBoundType, true);
    }

    /**
     * Convenience constructor with cuts
     */
    LongRange(final LongCut lowerBound, final LongCut upperBound) {
        this(lowerBound.getValue(), lowerBound.getBoundType(), lowerBound.isBounded(),
                upperBound.getValue(), upperBound.getBoundType(), upperBound.isBounded());
    }

    public boolean hasLowerBound() {
        return hasLowerBound;
    }

    public long lowerEndpoint() {
        return lowerBound;
    }

    public BoundType lowerBoundType() {
        return lowerBoundType;
    }

    public boolean hasUpperBound() {
        return hasUpperBound;
    }

    public long upperEndpoint() {
        return upperBound;
    }

    public BoundType upperBoundType() {
        return upperBoundType;
    }

    public boolean isEmpty() {
        return hasLowerBound && hasUpperBound && lowerBound == upperBound;
    }

    public boolean contains(final int value) {
        return lowerCheck.test(value) && upperCheck.test(value);
    }

    public boolean encloses(final LongRange other) {
        return equals(other)
                || (lowerCheck.test(other.lowerBound)
                && lowerCheck.test(other.upperBound)
                && upperCheck.test(other.lowerBound)
                && upperCheck.test(other.upperBound));
    }

    /**
     * @see <a href="http://google.github.io/guava/releases/19.0/api/docs/com/google/common/collect/Range.html#isConnected(com.google.common.collect.Range)">Guava JavaDoc</a>
     */
    public boolean isConnected(LongRange other) {
        return (this.lowerCheck.test(other.upperBound) && this.upperCheck.test(other.upperBound))
                || (this.upperCheck.test(other.lowerBound) && this.lowerCheck.test(other.lowerBound));
    }

    /**
     * @see <a href="http://google.github.io/guava/releases/19.0/api/docs/com/google/common/collect/Range.html#intersection(com.google.common.collect.Range)">Guava JavaDoc</a>
     */
    public LongRange intersection(LongRange other) {
        if (!this.isConnected(other)) {
            throw new IllegalArgumentException(String.format(Constants.NO_CONNECTION, this, other));
        }

        return new LongRange(
                LongCut.max(this.toLowerCut(), other.toLowerCut()),
                LongCut.min(this.toUpperCut(), other.toUpperCut())
        );
    }

    /**
     * @see <a href="http://google.github.io/guava/releases/19.0/api/docs/com/google/common/collect/Range.html#intersection(com.google.common.collect.Range)">Guava JavaDoc</a>
     */
    public LongRange span(LongRange other) {
        return new LongRange(
                LongCut.min(this.toLowerCut(), other.toLowerCut()),
                LongCut.max(this.toUpperCut(), other.toUpperCut())
        );
    }

    LongCut toLowerCut() {
        return new LongCut(lowerBound, lowerBoundType, hasLowerBound);
    }

    LongCut toUpperCut() {
        return new LongCut(upperBound, upperBoundType, hasUpperBound);
    }

    @Override
    public int hashCode() {
        return Objects.hash(lowerBound, lowerBoundType, upperBound,
                upperBoundType);
    }

    @SuppressWarnings("OverlyComplexBooleanExpression")
    @Override
    public boolean equals(final Object obj) {
        if (this == obj)
            return true;
        if (!(obj instanceof LongRange)) // also takes care of obj == null
            return false;
        final LongRange other = (LongRange) obj;
        final boolean isUnboundedInstance = (!hasLowerBound && !hasUpperBound);
        final boolean hasEqualBounds = lowerBound == other.lowerBound
                && upperBound == other.upperBound
                && lowerBoundType == other.lowerBoundType
                && upperBoundType == other.upperBoundType;

        return isUnboundedInstance || hasEqualBounds;
    }

    @Override
    public String toString() {
        @SuppressWarnings("StringBufferReplaceableByString")
        final StringBuilder sb = new StringBuilder();
        sb.append(lowerBoundType == BoundType.CLOSED ? "[" : "(");
        sb.append(hasLowerBound ? lowerBound : "-" + Constants.INFINITY_REPRESENTATION);
        sb.append("..");
        sb.append(hasUpperBound ? upperBound : Constants.INFINITY_REPRESENTATION);
        sb.append(upperBoundType == BoundType.CLOSED ? "]" : ")");

        return sb.toString();
    }
}
