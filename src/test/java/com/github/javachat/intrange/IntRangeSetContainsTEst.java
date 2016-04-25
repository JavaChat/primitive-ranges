package com.github.javachat.intrange;

import org.testng.annotations.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class IntRangeSetContainsTest {
    @Test
    public void testEmptyRangeSetDoesNotContainValues() {
        IntRangeSet set = IntRangeSet.create();
        assertThat(set.contains(Integer.MIN_VALUE)).as("Empty Set does not contain values").isFalse();
        assertThat(set.contains(0)).as("Empty Set does not contain values").isFalse();
        assertThat(set.contains(Integer.MAX_VALUE)).as("Empty Set does not contain values").isFalse();
    }

    @Test
    public void testContainsSingleRange() {
        IntRangeSet set = IntRangeSet.create(IntRange.closedOpen(2, 5));

        assertThat(set.contains(2)).isFalse();
        assertThat(set.contains(3)).isTrue();
        assertThat(set.contains(5)).isTrue();
        assertThat(set.contains(6)).isFalse();
    }

    @Test
    public void testContainsDisconnectedRange() {
        IntRangeSet set = IntRangeSet.create(IntRange.closedOpen(1, 3), IntRange.closedOpen(10, 15));

        assertThat(set.contains(1)).isFalse();
        assertThat(set.contains(2)).isTrue();
        assertThat(set.contains(5)).isFalse();
        assertThat(set.contains(10)).isFalse();
        assertThat(set.contains(11)).isTrue();
    }
}
