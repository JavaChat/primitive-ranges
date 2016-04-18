package com.github.javachat.intrange;

import org.testng.annotations.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class IntRangeObjectMethodsTest {
    @Test
    public void testEqualsWithDiscreteRanges() {
        IntRange range = IntRange.openClosed(2, 4);

        assertThat(range.equals(range)).as("not equal to same instance").isTrue();
        assertThat(range.equals(IntRange.openClosed(2, 4))).as("not equal to equal instance").isTrue();
        assertThat(range.equals(IntRange.closedOpen(2, 4))).as("not equal to different instance").isFalse();
        assertThat(IntRange.all().equals(IntRange.all())).as("equal test works on unbounded range");
    }
}
