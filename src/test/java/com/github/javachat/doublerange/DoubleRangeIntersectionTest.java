package com.github.javachat.doublerange;

import com.github.javachat.common.BoundType;
import com.github.javachat.common.Constants;
import org.testng.annotations.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.shouldHaveThrown;

public class DoubleRangeIntersectionTest {
    @Test
    public void testIntersectionWithUnconnectedRanges() {
        DoubleRange lower = DoubleRange.openClosed(0, 1);
        DoubleRange higher = DoubleRange.openClosed(2, 3);
        try {
            lower.intersection(higher);
            shouldHaveThrown(IllegalArgumentException.class);
        } catch (IllegalArgumentException e) {
            String msg = String.format(Constants.NO_CONNECTION, lower, higher);
            assertThat(e).hasMessage(msg);
        }
    }

    @Test
    public void testIntersectionNonEmpty() {
        DoubleRange lower = DoubleRange.closed(1, 5);
        DoubleRange higher = DoubleRange.open(3, 7);

        DoubleRange intersection = lower.intersection(higher);
        assertThat(intersection).isNotNull();
        assertThat(intersection.isEmpty()).as("Intersection (3..5] is not empty").isFalse();
        assertThat(intersection.lowerEndpoint()).as("lower bound").isEqualTo(3);
        assertThat(intersection.lowerBoundType()).as("lower bound type should be open " +
                "(as the lower bound of the 'bigger' range is open").isEqualTo(BoundType.OPEN);
        assertThat(intersection.upperEndpoint()).as("higher bound").isEqualTo(5);
        assertThat(intersection.upperBoundType()).as("higher bound should be closed " +
                "(as the upper bound of the 'smaller' range is closed)").isEqualTo(BoundType.CLOSED);
    }

    @Test
    public void testIntersectionEmpty() {
        DoubleRange lower = DoubleRange.closedOpen(1, 5);
        DoubleRange higher = DoubleRange.closedOpen(5, 7);

        DoubleRange intersection = lower.intersection(higher);
        assertThat(intersection.lowerEndpoint()).as("lower bound").isEqualTo(5);
        assertThat(intersection.lowerBoundType()).as("lower bound type should be closed " +
                "(lower boundary does not contain value)").isEqualTo(BoundType.CLOSED);
        assertThat(intersection.upperEndpoint()).as("upper bound").isEqualTo(5);
        assertThat(intersection.upperBoundType()).as("upper bound type should be open " +
                "(upper boundary does contain value)").isEqualTo(BoundType.OPEN);
        assertThat(intersection.isEmpty()).as("Intersection [5..5) should be empty").isTrue();
    }

    @Test
    public void testInfiniteIntersection() {
        DoubleRange infinite = DoubleRange.all();
        DoubleRange finite = DoubleRange.closedOpen(5, 7);

        DoubleRange intersection = infinite.intersection(finite);
        assertThat(intersection).isEqualTo(finite);
    }
}
