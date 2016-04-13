package com.github.javachat.doublerange;

import org.testng.annotations.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class DoubleRangeBoundTest {
    @Test
    public void testFiniteBounds() {
        assertThat(DoubleRange.closed(1, 3).hasLowerBound()).isTrue();
        assertThat(DoubleRange.closed(1, 3).hasUpperBound()).isTrue();
    }

    @Test
    public void testInfiniteBounds() {
        assertThat(DoubleRange.all().hasLowerBound()).isFalse();
        assertThat(DoubleRange.all().hasUpperBound()).isFalse();
    }
}
