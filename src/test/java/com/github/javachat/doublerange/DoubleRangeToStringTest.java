package com.github.javachat.doublerange;

import com.github.javachat.common.BoundType;
import org.assertj.core.api.Assertions;
import org.testng.annotations.Test;

import static org.assertj.core.api.Assertions.assertThat;


public class DoubleRangeToStringTest {
    @Test
    public void testOpenRange() {
        DoubleRange range = new DoubleRange(1, BoundType.OPEN, 2, BoundType.OPEN);
        assertThat(range.toString()).isEqualTo("(1.0..2.0)");
    }

    @Test
    public void testClosedRange() {
        DoubleRange range = new DoubleRange(1, BoundType.CLOSED, 2, BoundType.CLOSED);
        assertThat(range.toString()).isEqualTo("[1.0..2.0]");
    }

    @Test
    public void testHighPrecisionNumbers() {
        DoubleRange range = new DoubleRange(Math.E, BoundType.CLOSED, Math.PI, BoundType.CLOSED);
        assertThat(range.toString()).isEqualTo("[2.718282..3.141593]");
    }

    @Test
    public void testInfinities() {
        DoubleRange range = new DoubleRange(Double.NEGATIVE_INFINITY, BoundType.OPEN,
                Double.POSITIVE_INFINITY, BoundType.OPEN);
        assertThat(range.toString()).isEqualTo("(-Infinity..Infinity)");
    }
}
