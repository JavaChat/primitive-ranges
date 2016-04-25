package com.github.javachat.floatrange;

import com.github.javachat.common.BoundType;
import com.github.javachat.common.Constants;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.Arrays;
import java.util.Locale;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.function.DoublePredicate;
import java.util.stream.IntStream;

/**
 * A range of float values.
 * <p>
 * This is meant as a drop-in replacement for
 * <a href="http://google.github.io/guava/releases/19.0/api/docs/com/google/common/collect/Range.html">Range&lt;Double&gt;</a>,
 * therefore the semantics of the Guava class apply to this class as well.
 * <p>
 * In regard to float specifics, this implementation follows Java semantics, this means:
 * <p>
 * <ul>
 * <li>Any float lies between +Infinity and -Infinity</li>
 * <li>NaN always lies in an all range</li>
 * <li>NaN never lies in a range with discrete boundaries</li>
 * </ul>
 */
public class FloatRange {
    /**
     * The all range that spans every float value (but not {@link Double#NaN})
     */
    static final FloatRange INFINITE_RANGE = new FloatRange(Float.NEGATIVE_INFINITY, BoundType.OPEN,
            Float.POSITIVE_INFINITY, BoundType.OPEN);

    // Exception messages
    static final String ILLEGAL_BOUND = "lower bound %.5f must be less than or equal " +
            "to upper bound %.5f";
    static final String ILLEGAL_OPEN_RANGE = "when specifying an open range, "
            + "the lower bound must be strictly less than the upper bound";
    static final String BOUNDARY_IS_NAN = "No boundary can be NaN";

    // Formatting specifics
    static final DecimalFormatSymbols TO_STRING_FORMAT_SYMBOLS;
    static final NumberFormat TO_STRING_FORMAT;

    static {
        // Better to have consistent formatting symbols.
        TO_STRING_FORMAT_SYMBOLS = DecimalFormatSymbols.getInstance(Locale.US);
        TO_STRING_FORMAT_SYMBOLS.setInfinity(Constants.INFINITY_REPRESENTATION);
        TO_STRING_FORMAT = new DecimalFormat("#0.0#####", TO_STRING_FORMAT_SYMBOLS);
    }

    private final BoundType lowerBoundType;
    private final BoundType upperBoundType;
    private final float lowerBound;
    private final float upperBound;
    private final DoublePredicate lowerCheck;
    private final DoublePredicate upperCheck;

    /**
     * @see <a href="http://google.github.io/guava/releases/19.0/api/docs/com/google/common/collect/Range.html#open(C, C)">Guava JavaDoc</a>
     */
    public static FloatRange open(float lowerBound, float upperBound) {
        return new FloatRange(lowerBound, BoundType.OPEN, upperBound, BoundType.OPEN);
    }

    /**
     * @see <a href="http://google.github.io/guava/releases/19.0/api/docs/com/google/common/collect/Range.html#closed(C, C)">Guava JavaDoc</a>
     */
    public static FloatRange closed(float lowerBound, float upperBound) {
        return new FloatRange(lowerBound, BoundType.CLOSED, upperBound, BoundType.CLOSED);
    }

    /**
     * @see <a href="http://google.github.io/guava/releases/19.0/api/docs/com/google/common/collect/Range.html#closedOpen(C, C)">Guava JavaDoc</a>
     */
    public static FloatRange closedOpen(float lowerBound, float upperBound) {
        return new FloatRange(lowerBound, BoundType.CLOSED, upperBound, BoundType.OPEN);
    }

    /**
     * @see <a href="http://google.github.io/guava/releases/19.0/api/docs/com/google/common/collect/Range.html#openClosed(C, C)">Guava JavaDoc</a>
     */
    public static FloatRange openClosed(float lowerBound, float upperBound) {
        return new FloatRange(lowerBound, BoundType.OPEN, upperBound, BoundType.CLOSED);
    }

    /**
     * @see <a href="http://google.github.io/guava/releases/19.0/api/docs/com/google/common/collect/Range.html#range(C, com.google.common.collect.BoundType, C, com.google.common.collect.BoundType)">Guava JavaDoc</a>
     */
    public static FloatRange range(float lowerBound, BoundType lowerBoundType, float upperBound, BoundType upperBoundType) {
        return new FloatRange(lowerBound, lowerBoundType, upperBound, upperBoundType);
    }


    /**
     * @see <a href="http://google.github.io/guava/releases/19.0/api/docs/com/google/common/collect/Range.html#lessThan(C)">Guava JavaDoc</a>
     */
    public static FloatRange lessThan(float endpoint) {
        return upTo(endpoint, BoundType.CLOSED);
    }


    /**
     * @see <a href="http://google.github.io/guava/releases/19.0/api/docs/com/google/common/collect/Range.html#atMost(C)">Guava JavaDoc</a>
     */
    public static FloatRange atMost(float endpoint) {
        return upTo(endpoint, BoundType.OPEN);
    }


    /**
     * @see <a href="http://google.github.io/guava/releases/19.0/api/docs/com/google/common/collect/Range.html#upTo(C, com.google.common.collect.BoundType)">Guava JavaDoc</a>
     */
    public static FloatRange upTo(float endpoint, BoundType boundType) {
        return range(Float.NEGATIVE_INFINITY, BoundType.OPEN, endpoint, boundType);
    }


    /**
     * @see <a href="http://google.github.io/guava/releases/19.0/api/docs/com/google/common/collect/Range.html#greaterThan(C)">Guava JavaDoc</a>
     */
    public static FloatRange greaterThan(float endpoint) {
        return downTo(endpoint, BoundType.CLOSED);
    }


    /**
     * @see <a href="http://google.github.io/guava/releases/19.0/api/docs/com/google/common/collect/Range.html#atLeast(C)">Guava JavaDoc</a>
     */
    public static FloatRange atLeast(float endpoint) {
        return downTo(endpoint, BoundType.OPEN);
    }


    /**
     * @see <a href="http://google.github.io/guava/releases/19.0/api/docs/com/google/common/collect/Range.html#downTo(C, com.google.common.collect.BoundType)">Guava JavaDoc</a>
     */
    public static FloatRange downTo(float endpoint, BoundType boundType) {
        return range(endpoint, boundType, Float.POSITIVE_INFINITY, BoundType.OPEN);
    }

    /**
     * @see <a href="http://google.github.io/guava/releases/19.0/api/docs/com/google/common/collect/Range.html#singleton(C)">Guava JavaDoc</a>
     */
    public static FloatRange singleton(float value) {
        return FloatRange.closed(value, value);
    }

    /**
     * @see <a href="http://google.github.io/guava/releases/19.0/api/docs/com/google/common/collect/Range.html#encloseAll(java.lang.Iterable)">Guava JavaDoc</a>
     */
    public static FloatRange encloseAll(float... values) {
        if (values.length == 0) {
            throw new NoSuchElementException("Given array is empty");
        }

        // Create a sorted copy and use the first and last element.
        float[] copy = Arrays.copyOf(values, values.length);
        Arrays.sort(copy);
        return FloatRange.closed(copy[0], copy[copy.length - 1]);
    }

    /**
     * @see <a href="http://google.github.io/guava/releases/19.0/api/docs/com/google/common/collect/Range.html#all()">Guava JavaDoc</a>
     */
    public static FloatRange all() {
        return INFINITE_RANGE;
    }

    FloatRange(float lowerBound, BoundType lowerBoundType, float upperBound, BoundType upperBoundType) {
        if (Double.isNaN(lowerBound) || Double.isNaN(upperBound)) {
            throw new IllegalArgumentException(BOUNDARY_IS_NAN);
        }

        if (Double.compare(lowerBound, upperBound) > 0) {
            throw new IllegalArgumentException(String.format(ILLEGAL_BOUND, lowerBound, upperBound));
        }

        if (lowerBound == upperBound
                && lowerBoundType == BoundType.OPEN
                && lowerBoundType == upperBoundType) {
            throw new IllegalArgumentException(ILLEGAL_OPEN_RANGE);
        }

        this.lowerBound = lowerBound;
        this.upperBound = upperBound;
        this.lowerBoundType = Objects.requireNonNull(lowerBoundType);
        this.upperBoundType = Objects.requireNonNull(upperBoundType);

        if (Double.isInfinite(lowerBound)) {
            lowerCheck = value -> true;
        } else {
            lowerCheck = (lowerBoundType == BoundType.OPEN)
                    ? value -> value >= lowerBound
                    : value -> value > lowerBound;
        }

        if (Double.isInfinite(upperBound)) {
            upperCheck = value -> true;
        } else {
            upperCheck = (upperBoundType == BoundType.OPEN)
                    ? value -> value <= upperBound
                    : value -> value < upperBound;
        }
    }

    private FloatRange(FloatCut lower, FloatCut upper) {
        this(lower.getValue(), lower.getBoundType(), upper.getValue(), upper.getBoundType());
    }

    /**
     * @see <a href="http://google.github.io/guava/releases/19.0/api/docs/com/google/common/collect/Range.html#hasLowerBound()">Guava JavaDoc</a>
     */
    public boolean hasLowerBound() {
        return Double.isFinite(lowerBound);
    }


    /**
     * @see <a href="http://google.github.io/guava/releases/19.0/api/docs/com/google/common/collect/Range.html#lowerEndpoint()">Guava JavaDoc</a>
     */
    public float lowerEndpoint() {
        return lowerBound;
    }


    /**
     * @see <a href="http://google.github.io/guava/releases/19.0/api/docs/com/google/common/collect/Range.html#lowerBoundType()">Guava JavaDoc</a>
     */
    public BoundType lowerBoundType() {
        return lowerBoundType;
    }


    /**
     * @see <a href="http://google.github.io/guava/releases/19.0/api/docs/com/google/common/collect/Range.html#hasUpperBound()">Guava JavaDoc</a>
     */
    public boolean hasUpperBound() {
        return Double.isFinite(upperBound);
    }


    /**
     * @see <a href="http://google.github.io/guava/releases/19.0/api/docs/com/google/common/collect/Range.html#upperEndpoint()">Guava JavaDoc</a>
     */
    public float upperEndpoint() {
        return upperBound;
    }


    /**
     * @see <a href="http://google.github.io/guava/releases/19.0/api/docs/com/google/common/collect/Range.html#upperBoundType()">Guava JavaDoc</a>
     */
    public BoundType upperBoundType() {
        return upperBoundType;
    }


    /**
     * @see <a href="http://google.github.io/guava/releases/19.0/api/docs/com/google/common/collect/Range.html#isEmpty()">Guava JavaDoc</a>
     */
    public boolean isEmpty() {
        return lowerBound == upperBound;
    }

    /**
     * @see <a href="http://google.github.io/guava/releases/19.0/api/docs/com/google/common/collect/Range.html#contains(C)">Guava JavaDoc</a>
     */
    public boolean contains(float value) {
        return lowerCheck.test(value) && upperCheck.test(value);
    }


    /**
     * @see <a href="http://google.github.io/guava/releases/19.0/api/docs/com/google/common/collect/Range.html#containsAll(java.lang.Iterable)">Guava JavaDoc</a>
     */
    public boolean containsAll(float... values) {
        return IntStream.range(0, values.length)
                .mapToObj(idx -> values[idx]) // Boxing necessary, there is no FloatStream, so use float -> Float -> float instead
                .allMatch(this::contains);
    }

    /**
     * @see <a href="http://google.github.io/guava/releases/19.0/api/docs/com/google/common/collect/Range.html#encloses(com.google.common.collect.Range)">Guava JavaDoc</a>
     */
    public boolean encloses(FloatRange other) {
        return equals(other) // Enclosure is reflexive.
                || (lowerCheck.test(other.lowerBound)
                && lowerCheck.test(other.upperBound)
                && upperCheck.test(other.lowerBound)
                && upperCheck.test(other.upperBound));
    }


    /**
     * @see <a href="http://google.github.io/guava/releases/19.0/api/docs/com/google/common/collect/Range.html#isConnected(com.google.common.collect.Range)">Guava JavaDoc</a>
     */
    public boolean isConnected(FloatRange other) {
        return (this.lowerCheck.test(other.upperBound) && this.upperCheck.test(other.upperBound))
                || (this.upperCheck.test(other.lowerBound) && this.lowerCheck.test(other.lowerBound));
    }


    /**
     * @see <a href="http://google.github.io/guava/releases/19.0/api/docs/com/google/common/collect/Range.html#intersection(com.google.common.collect.Range)">Guava JavaDoc</a>
     */
    public FloatRange intersection(FloatRange other) {
        if (!this.isConnected(other)) {
            throw new IllegalArgumentException(String.format(Constants.NO_CONNECTION, this, other));
        }

        return new FloatRange(
                FloatCut.max(this.toLowerCut(), other.toLowerCut()),
                FloatCut.min(this.toUpperCut(), other.toUpperCut())
        );
    }


    /**
     * @see <a href="http://google.github.io/guava/releases/19.0/api/docs/com/google/common/collect/Range.html#span(com.google.common.collect.Range)">Guava JavaDoc</a>
     */
    public FloatRange span(FloatRange other) {
        return new FloatRange(
                FloatCut.min(this.toLowerCut(), other.toLowerCut()),
                FloatCut.max(this.toUpperCut(), other.toUpperCut())
        );
    }

    FloatCut toLowerCut() {
        return new FloatCut(lowerBound, lowerBoundType);
    }

    FloatCut toUpperCut() {
        return new FloatCut(upperBound, upperBoundType);
    }

    @Override
    public int hashCode() {
        return Objects.hash(lowerBound, lowerBoundType, upperBound, upperBoundType);
    }


    /**
     * @see <a href="http://google.github.io/guava/releases/19.0/api/docs/com/google/common/collect/Range.html#eqauals()">Guava JavaDoc</a>
     */
    @SuppressWarnings("OverlyComplexBooleanExpression")
    @Override
    public boolean equals(final Object obj) {
        if (this == obj)
            return true;
        if (!(obj instanceof FloatRange)) // also takes care of obj == null
            return false;
        final FloatRange other = (FloatRange) obj;
        return lowerBound == other.lowerBound
                && upperBound == other.upperBound
                && lowerBoundType == other.lowerBoundType
                && upperBoundType == other.upperBoundType;
    }


    /**
     * @see <a href="http://google.github.io/guava/releases/19.0/api/docs/com/google/common/collect/Range.html#toString()">Guava JavaDoc</a>
     */
    @Override
    public String toString() {
        @SuppressWarnings("StringBufferReplaceableByString")
        final StringBuilder sb = new StringBuilder();

        sb.append(lowerBoundType == BoundType.CLOSED ? "[" : "(");
        sb.append(TO_STRING_FORMAT.format(lowerBound)).append("..").append(TO_STRING_FORMAT.format(upperBound));
        sb.append(upperBoundType == BoundType.CLOSED ? "]" : ")");

        return sb.toString();
    }
}
