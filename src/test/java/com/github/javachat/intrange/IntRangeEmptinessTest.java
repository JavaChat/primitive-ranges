package com.github.javachat.intrange;

import org.testng.annotations.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class IntRangeEmptinessTest {
    @Test
    public void testFiniteEmptyRanges() {
        IntRange openClosed = IntRange.openClosed(4, 4);
        IntRange closedOpen = IntRange.closedOpen(4, 4);

        assertThat(openClosed.isEmpty()).isTrue();
        assertThat(closedOpen.isEmpty()).isTrue();
    }

    @Test
    public void testFiniteNonEmptyRange() {
        IntRange range = IntRange.openClosed(3, 7);
        assertThat(range.isEmpty()).as("Range %s is not empty (contains 4 discrete values)", range).isFalse();
    }

    @Test
    public void testDiscreteRangeWithNoValuesIsNotEmpty() {
        // As per Guava's JavaDoc example
        IntRange range = IntRange.open(3, 4);
        assertThat(range.isEmpty()).as("Range does not contain values, but should not be empty").isFalse();
    }

    @Test
    public void testInifinteRange() {
        IntRange infinite = IntRange.all();
        assertThat(infinite.isEmpty()).as("Infinite range is not empty").isFalse();
    }
}
