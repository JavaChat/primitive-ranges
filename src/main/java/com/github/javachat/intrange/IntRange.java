package com.github.javachat.intrange;

import com.github.javachat.common.BoundType;

import java.util.Objects;
import java.util.function.IntPredicate;

public final class IntRange
{
    static final String ILLEGAL_BOUNDS = "lower bound %d must be less than or"
        + " equal to upper bound %d";

    static final String ILLEGAL_OPEN_RANGE = "when specifying an open range, "
        + "the lower bound must be strictly less than the upper bound";

    private final BoundType lowerBoundType;
    private final BoundType upperBoundType;
    private final int lowerBound;
    private final int upperBound;
    private final IntPredicate lowerCheck;
    private final IntPredicate upperCheck;

    IntRange(final int lowerBound, final BoundType lowerBoundType,
        final int upperBound, final BoundType upperBoundType)
    {
        if (Integer.compare(lowerBound, upperBound) > 0)
            throw new IllegalArgumentException(String.format(ILLEGAL_BOUNDS,
                lowerBound, upperBound));

        if (lowerBound == upperBound
            && lowerBoundType == BoundType.OPEN
            && lowerBoundType == upperBoundType)
            throw new IllegalArgumentException(ILLEGAL_OPEN_RANGE);

        this.lowerBound = lowerBound;
        this.lowerBoundType = Objects.requireNonNull(lowerBoundType);
        this.upperBound = upperBound;
        this.upperBoundType = Objects.requireNonNull(upperBoundType);

        lowerCheck = lowerBoundType == BoundType.CLOSED
            ? value -> value >= lowerBound
            : value -> value > lowerBound;

        upperCheck = upperBoundType == BoundType.CLOSED
            ? value -> value <= upperBound
            : value -> value < upperBound;
    }

    public boolean contains(final int value)
    {
        return lowerCheck.test(value) && upperCheck.test(value);
    }

    public boolean encloses(final IntRange other)
    {
        // TODO
        return false;
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(lowerBound, lowerBoundType, upperBound,
            upperBoundType);
    }

    @SuppressWarnings("OverlyComplexBooleanExpression")
    @Override
    public boolean equals(final Object obj)
    {
        if (this == obj)
            return true;
        if (!(obj instanceof IntRange)) // also takes care of obj == null
            return false;
        final IntRange other = (IntRange) obj;
        return lowerBound == other.lowerBound
            && upperBound == other.upperBound
            && lowerBoundType == other.lowerBoundType
            && upperBoundType == other.upperBoundType;
    }

    @Override
    public String toString()
    {
        @SuppressWarnings("StringBufferReplaceableByString")
        final StringBuilder sb = new StringBuilder();

        sb.append(lowerBoundType == BoundType.CLOSED ? "[" : "(");
        sb.append(lowerBound).append("..").append(upperBound);
        sb.append(upperBoundType == BoundType.CLOSED ? "]" : ")");

        return sb.toString();
    }
}
