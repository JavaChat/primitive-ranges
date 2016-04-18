package com.github.javachat.intrange;

import org.testng.annotations.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class IntRangeEnclosesTest {
    @Test
    public void testUnboundedRangeEnclosesAllOtherRanges() {
        IntRange infinite = IntRange.all();

        assertThat(infinite.encloses(IntRange.openClosed(1, 3))).as("Discrete range must be included in unbounded range").isTrue();
        assertThat(infinite.encloses(IntRange.atLeast(3))).as("Semi-discrete range must be included in unbounded range");
        assertThat(infinite.encloses(infinite)).as("Unbounded range must enclose itself").isTrue();
    }

    @Test
    public void testBoundedRangeEnclosesSelf() {
        IntRange range = IntRange.closedOpen(1, 3);

        assertThat(range.encloses(range)).isTrue();
    }

    @Test
    public void testBoundedRangeEnclosure() {
        IntRange range = IntRange.closedOpen(1, 10);

        assertThat(range.encloses(IntRange.closedOpen(2, 9))).isTrue();
        assertThat(range.encloses(IntRange.closedOpen(0, 11))).isFalse();
    }
}
