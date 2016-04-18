package com.github.javachat.intrange;

import org.testng.annotations.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class IntRangeConnectednessTest {
    @Test
    public void testUnrelatedRangesAreNotConnected() {
        IntRange lower = IntRange.open(1, 3);
        IntRange higher = IntRange.open(6, 9);
        assertThat(lower.isConnected(higher)).as("Unconnected ranges should not be connected").isFalse();
    }

    @Test
    public void testDirectlyConnectedRanges() {
        IntRange lower = IntRange.closedOpen(1, 5);
        IntRange higher = IntRange.closedOpen(3, 7);

        assertThat(lower.isConnected(higher)).isTrue();
        assertThat(higher.isConnected(lower)).isTrue();
    }

    @Test
    public void testConnectedViaEmptyIntersection() {
        IntRange lower = IntRange.closedOpen(2, 4);
        IntRange higher = IntRange.closedOpen(4, 6);

        assertThat(lower.isConnected(higher)).as("Empty intersection still makes two ranges connected");
    }
}
