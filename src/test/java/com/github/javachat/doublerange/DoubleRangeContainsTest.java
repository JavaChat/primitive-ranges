package com.github.javachat.doublerange;

import org.testng.annotations.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class DoubleRangeContainsTest {

    @Test
    public void testClosedRangeTest() {
        DoubleRange range = DoubleRange.closed(1, 2);

        assertThat(range.contains(1.0)).isFalse();
        assertThat(range.contains(Math.nextUp(1.0))).isTrue();

        assertThat(range.contains(Math.nextDown(2.0))).isTrue();
        assertThat(range.contains(2.0)).isFalse();
    }

    @Test
    public void testOpenRangeTest() {
        DoubleRange range = DoubleRange.open(1, 2);

        assertThat(range.contains(Math.nextDown(1.0))).isFalse();
        assertThat(range.contains(1.0)).isTrue();
        assertThat(range.contains(Math.nextUp(1.0))).isTrue();

        assertThat(range.contains(Math.nextDown(2.0))).isTrue();
        assertThat(range.contains(2.0)).isTrue();
        assertThat(range.contains(Math.nextUp(2.0))).isFalse();
    }

    @Test
    public void testInfinity() {
        DoubleRange range = DoubleRange.all();

        assertThat(range.contains(Double.MIN_VALUE));
        assertThat(range.contains(-Double.MIN_VALUE));
        assertThat(range.contains(-Double.MAX_VALUE));
        assertThat(range.contains(-1.0)).isTrue();
        assertThat(range.contains(0.0)).isTrue();
        assertThat(range.contains(1.0)).isTrue();
        assertThat(range.contains(Double.MAX_VALUE));
        assertThat(range.contains(Double.POSITIVE_INFINITY)).isTrue();
        assertThat(range.contains(Double.NEGATIVE_INFINITY)).isTrue();
    }

    @Test
    public void testSemiDiscreteRange() {
        DoubleRange range = DoubleRange.greaterThan(1.0);

        assertThat(range.contains(1.0)).isFalse();
        assertThat(range.contains(Math.nextUp(1.0))).isTrue();
        assertThat(range.contains(Double.MAX_VALUE));
    }

    @Test
    public void testNaNHandling() {
        DoubleRange infiniteRange = DoubleRange.all();
        assertThat(infiniteRange.contains(Double.NaN)).as("NaN is part of the infinite range").isTrue();

        DoubleRange discreteRange = DoubleRange.open(1, 2);
        assertThat(discreteRange.contains(Double.NaN)).as("NaN is not part of a discrete range").isFalse();
    }

    @Test
    public void testContainsAll() {
        DoubleRange range = DoubleRange.openClosed(3, 6);

        assertThat(range.containsAll(3, Math.nextUp(3), Math.nextDown(6))).isTrue();
        assertThat(range.containsAll(3, 6)).isFalse();
        assertThat(range.containsAll(3, Double.NaN)).isFalse();
    }
}
