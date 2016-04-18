package com.github.javachat.doublerange;

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

/**
 * A range of double values.
 * <p>
 * This is meant as a drop-in replacement for
 * <a href="http://google.github.io/guava/releases/19.0/api/docs/com/google/common/collect/Range.html">Range&lt;Double&gt;</a>,
 * therefore the semantics of the Guava class apply to this class as well.
 * <p>
 * In regard to double specifics, this implementation follows Java semantics, this means:
 * <p>
 * <ul>
 * <li>Any double lies between +Infinity and -Infinity</li>
 * <li>NaN always lies in an all range</li>
 * <li>NaN never lies in a range with discrete boundaries</li>
 * </ul>
 */
public class DoubleRange {
    /**
     * The all range that spans every double value (but not {@link Double#NaN})
     */
    static final DoubleRange INFINITE_RANGE = new DoubleRange(Double.NEGATIVE_INFINITY, BoundType.OPEN,
            Double.POSITIVE_INFINITY, BoundType.OPEN);

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
    private final double lowerBound;
    private final double upperBound;
    private final DoublePredicate lowerCheck;
    private final DoublePredicate upperCheck;

    /**
     * @see <a href="http://google.github.io/guava/releases/19.0/api/docs/com/google/common/collect/Range.html#open(C, C)">Guava JavaDoc</a>
     */
    public static DoubleRange open(double lowerBound, double upperBound) {
        return new DoubleRange(lowerBound, BoundType.OPEN, upperBound, BoundType.OPEN);
    }

    /**
     * @see <a href="http://google.github.io/guava/releases/19.0/api/docs/com/google/common/collect/Range.html#closed(C, C)">Guava JavaDoc</a>
     */
    public static DoubleRange closed(double lowerBound, double upperBound) {
        return new DoubleRange(lowerBound, BoundType.CLOSED, upperBound, BoundType.CLOSED);
    }

    /**
     * @see <a href="http://google.github.io/guava/releases/19.0/api/docs/com/google/common/collect/Range.html#closedOpen(C, C)">Guava JavaDoc</a>
     */
    public static DoubleRange closedOpen(double lowerBound, double upperBound) {
        return new DoubleRange(lowerBound, BoundType.CLOSED, upperBound, BoundType.OPEN);
    }

    /**
     * @see <a href="http://google.github.io/guava/releases/19.0/api/docs/com/google/common/collect/Range.html#openClosed(C, C)">Guava JavaDoc</a>
     */
    public static DoubleRange openClosed(double lowerBound, double upperBound) {
        return new DoubleRange(lowerBound, BoundType.OPEN, upperBound, BoundType.CLOSED);
    }

    /**
     * @see <a href="http://google.github.io/guava/releases/19.0/api/docs/com/google/common/collect/Range.html#range(C, com.google.common.collect.BoundType, C, com.google.common.collect.BoundType)">Guava JavaDoc</a>
     */
    public static DoubleRange range(double lowerBound, BoundType lowerBoundType, double upperBound, BoundType upperBoundType) {
        return new DoubleRange(lowerBound, lowerBoundType, upperBound, upperBoundType);
    }


    /**
     * @see <a href="http://google.github.io/guava/releases/19.0/api/docs/com/google/common/collect/Range.html#lessThan(C)">Guava JavaDoc</a>
     */
    public static DoubleRange lessThan(double endpoint) {
        return upTo(endpoint, BoundType.CLOSED);
    }


    /**
     * @see <a href="http://google.github.io/guava/releases/19.0/api/docs/com/google/common/collect/Range.html#atMost(C)">Guava JavaDoc</a>
     */
    public static DoubleRange atMost(double endpoint) {
        return upTo(endpoint, BoundType.OPEN);
    }


    /**
     * @see <a href="http://google.github.io/guava/releases/19.0/api/docs/com/google/common/collect/Range.html#upTo(C, com.google.common.collect.BoundType)">Guava JavaDoc</a>
     */
    public static DoubleRange upTo(double endpoint, BoundType boundType) {
        return range(Double.NEGATIVE_INFINITY, BoundType.OPEN, endpoint, boundType);
    }


    /**
     * @see <a href="http://google.github.io/guava/releases/19.0/api/docs/com/google/common/collect/Range.html#greaterThan(C)">Guava JavaDoc</a>
     */
    public static DoubleRange greaterThan(double endpoint) {
        return downTo(endpoint, BoundType.CLOSED);
    }


    /**
     * @see <a href="http://google.github.io/guava/releases/19.0/api/docs/com/google/common/collect/Range.html#atLeast(C)">Guava JavaDoc</a>
     */
    public static DoubleRange atLeast(double endpoint) {
        return downTo(endpoint, BoundType.OPEN);
    }


    /**
     * @see <a href="http://google.github.io/guava/releases/19.0/api/docs/com/google/common/collect/Range.html#downTo(C, com.google.common.collect.BoundType)">Guava JavaDoc</a>
     */
    public static DoubleRange downTo(double endpoint, BoundType boundType) {
        return range(endpoint, boundType, Double.POSITIVE_INFINITY, BoundType.OPEN);
    }

    /**
     * @see <a href="http://google.github.io/guava/releases/19.0/api/docs/com/google/common/collect/Range.html#singleton(C)">Guava JavaDoc</a>
     */
    public static DoubleRange singleton(double value) {
        return DoubleRange.closed(value, value);
    }

    /**
     * @see <a href="http://google.github.io/guava/releases/19.0/api/docs/com/google/common/collect/Range.html#encloseAll(java.lang.Iterable)">Guava JavaDoc</a>
     */
    public static DoubleRange encloseAll(double... values) {
        if (values.length == 0) {
            throw new NoSuchElementException("Given array is empty");
        }

        // Create a sorted copy and use the first and last element.
        double[] copy = Arrays.copyOf(values, values.length);
        Arrays.sort(copy);
        return DoubleRange.closed(copy[0], copy[copy.length - 1]);
    }

    /**
     * @see <a href="http://google.github.io/guava/releases/19.0/api/docs/com/google/common/collect/Range.html#all()">Guava JavaDoc</a>
     */
    public static DoubleRange all() {
        return INFINITE_RANGE;
    }

    DoubleRange(double lowerBound, BoundType lowerBoundType, double upperBound, BoundType upperBoundType) {
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

    private DoubleRange(DoubleCut lower, DoubleCut upper) {
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
    public double lowerEndpoint() {
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
    public double upperEndpoint() {
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
    public boolean contains(double value) {
        return lowerCheck.test(value) && upperCheck.test(value);
    }


    /**
     * @see <a href="http://google.github.io/guava/releases/19.0/api/docs/com/google/common/collect/Range.html#containsAll(java.lang.Iterable)">Guava JavaDoc</a>
     */
    public boolean containsAll(double... values) {
        return Arrays.stream(values)
                .allMatch(this::contains);
    }


    /**
     * @see <a href="http://google.github.io/guava/releases/19.0/api/docs/com/google/common/collect/Range.html#encloses(com.google.common.collect.Range)">Guava JavaDoc</a>
     */
    public boolean encloses(DoubleRange other) {
        return equals(other) // Enclosure is reflexive.
                || (lowerCheck.test(other.lowerBound)
                && lowerCheck.test(other.upperBound)
                && upperCheck.test(other.lowerBound)
                && upperCheck.test(other.upperBound));
    }


    /**
     * @see <a href="http://google.github.io/guava/releases/19.0/api/docs/com/google/common/collect/Range.html#isConnected(com.google.common.collect.Range)">Guava JavaDoc</a>
     */
    public boolean isConnected(DoubleRange other) {
        return (this.lowerCheck.test(other.upperBound) && this.upperCheck.test(other.upperBound))
                || (this.upperCheck.test(other.lowerBound) && this.lowerCheck.test(other.lowerBound));
    }


    /**
     * @see <a href="http://google.github.io/guava/releases/19.0/api/docs/com/google/common/collect/Range.html#intersection(com.google.common.collect.Range)">Guava JavaDoc</a>
     */
    public DoubleRange intersection(DoubleRange other) {
        if (!this.isConnected(other)) {
            throw new IllegalArgumentException(String.format(Constants.NO_CONNECTION, this, other));
        }

        return new DoubleRange(
                DoubleCut.max(this.toLowerCut(), other.toLowerCut()),
                DoubleCut.min(this.toUpperCut(), other.toUpperCut())
        );
    }


    /**
     * @see <a href="http://google.github.io/guava/releases/19.0/api/docs/com/google/common/collect/Range.html#span(com.google.common.collect.Range)">Guava JavaDoc</a>
     */
    public DoubleRange span(DoubleRange other) {
        return new DoubleRange(
                DoubleCut.min(this.toLowerCut(), other.toLowerCut()),
                DoubleCut.max(this.toUpperCut(), other.toUpperCut())
        );
    }

    private DoubleCut toLowerCut() {
        return new DoubleCut(lowerBound, lowerBoundType);
    }

    private DoubleCut toUpperCut() {
        return new DoubleCut(upperBound, upperBoundType);
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
        if (!(obj instanceof DoubleRange)) // also takes care of obj == null
            return false;
        final DoubleRange other = (DoubleRange) obj;
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
