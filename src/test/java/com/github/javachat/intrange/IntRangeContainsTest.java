package com.github.javachat.intrange;

import org.testng.annotations.Test;

import static org.assertj.core.api.Assertions.assertThat;

public final class IntRangeContainsTest {
    @Test
    public void testSingleValueRange() {
        final IntRange range = IntRange.closedOpen(1, 2);

        assertThat(range.contains(0)).as("Unrelated value should not be contained").isFalse();
        assertThat(range.contains(1)).as("closed bound should not be included").isFalse();
        assertThat(range.contains(2)).as("open bound should be included").isTrue();
    }

    @Test
    public void testOpenClosedRange() {
        final IntRange range = IntRange.openClosed(-7, 5);

        assertThat(range.contains(-7)).isTrue();
        assertThat(range.contains(-6)).isTrue();
        assertThat(range.contains(0)).isTrue();
        assertThat(range.contains(4)).isTrue();
        assertThat(range.contains(5)).isFalse();
    }

    @Test
    public void testUnboundedRange() {
        final IntRange infinite = IntRange.all();

        assertThat(infinite.contains(0)).as("Unbounded range should contain zero").isTrue();
        assertThat(infinite.contains(Integer.MIN_VALUE)).as("Unbounded range should contain biggest representable value").isTrue();
        assertThat(infinite.contains(Integer.MAX_VALUE)).as("Unbounded range should contain smallest representable value").isTrue();
    }

    @Test
    public void testSingletonRange() {
        IntRange singleton = IntRange.singleton(3);
        assertThat(singleton.contains(3)).as("Closed singleton range should not contain value").isFalse();
    }

    @Test
    public void testContainsAll() {
        IntRange range = IntRange.closedOpen(0, 10);
        // TODO test when method exists
    }
}
