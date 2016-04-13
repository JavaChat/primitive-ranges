package com.github.javachat.doublerange;

import org.testng.annotations.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class DoubleRangeEmptinessTest {
    @Test
    public void testFiniteNonEmptyRange() {
        DoubleRange range = DoubleRange.closed(3, 6);
        assertThat(range.isEmpty()).as("%s should not be empty").isFalse();
    }

    @Test
    public void testInfiniteRange() {
        DoubleRange range = DoubleRange.all();
        assertThat(range.isEmpty()).as("Infinite range should not be empty").isFalse();
    }

    @Test
    public void testFiniteClosedEmptyRange() {
        DoubleRange range = DoubleRange.closed(4.0, 4.0);
        String description = "%s should be empty (closed range does not contain boundary elements)";
        assertThat(range.isEmpty()).as(description, range).isTrue();
    }

    @Test
    public void testFiniteEmptyRanges() {
        String description = "%s should be empty (contains no elements since no elements lie between 4.0 and 4.0)";
        DoubleRange closedOpen = DoubleRange.closedOpen(4.0, 4.0);
        DoubleRange openClosed = DoubleRange.openClosed(4.0, 4.0);

        assertThat(closedOpen.isEmpty()).as(description, closedOpen).isTrue();
        assertThat(openClosed.isEmpty()).as(description, openClosed).isTrue();
    }
}
