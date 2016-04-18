package com.github.javachat.intrange;

import com.github.javachat.common.Constants;
import org.testng.annotations.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.shouldHaveThrown;

public class IntRangeIntersectionTest {
    @Test
    public void testIntersectionOfNonConnectedRanges() {
        try {
            IntRange lower = IntRange.closedOpen(1, 4);
            IntRange higher = IntRange.closedOpen(6, 9);

            lower.intersection(higher);
            shouldHaveThrown(IllegalArgumentException.class);
        } catch (IllegalArgumentException e) {
            assertThat(e).hasMessage(String.format(Constants.NO_CONNECTION, "[1..4)", "[6..9)"));
        }
    }

    @Test
    public void testIntersectionOfConnectedRanges() {
        IntRange lower = IntRange.closedOpen(1, 5);
        IntRange higher = IntRange.closedOpen(3, 7);

        IntRange intersection = lower.intersection(higher);
        assertThat(intersection).isNotNull();
        assertThat(intersection).isEqualTo(IntRange.closedOpen(3, 5));
    }

    @Test
    public void testIntersectionWithEmptyIntersection() {
        IntRange lower = IntRange.closedOpen(2, 4);
        IntRange higher = IntRange.closedOpen(4, 6);

        IntRange intersection = lower.intersection(higher);
        assertThat(intersection).isNotNull();
        assertThat(intersection.isEmpty()).isTrue();
        assertThat(intersection).isEqualTo(IntRange.closedOpen(4, 4));
    }

    @Test
    public void testIntersectionWithUnboundedRange() {
        IntRange infinite = IntRange.all();
        IntRange finite = IntRange.closedOpen(2, 4);

        IntRange intersection = infinite.intersection(finite);
        assertThat(intersection).as("Intersection between bounded and unbounded range should be bounded range").isEqualTo(finite);
    }
}
