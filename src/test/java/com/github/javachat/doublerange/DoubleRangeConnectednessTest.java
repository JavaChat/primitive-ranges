package com.github.javachat.doublerange;

import org.testng.annotations.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class DoubleRangeConnectednessTest {

    public static final String CONNECTION_DESC = "%s is connected to %s (both contain %s)";

    @Test
    public void testSimpleNonConnectedness() {
        DoubleRange lower = DoubleRange.openClosed(2, 4);
        DoubleRange higher = DoubleRange.openClosed(5, 7);

        assertThat(lower.isConnected(higher)).as("Unrelated ranges %s, %s should not be connected", lower, higher)
                .isFalse();
    }

    @Test
    public void testSimpleConnection() {
        DoubleRange lower = DoubleRange.closedOpen(2, 4);
        DoubleRange higher = DoubleRange.closedOpen(3, 6);

        assertThat(lower.isConnected(higher)).as(CONNECTION_DESC, lower, higher, DoubleRange.closedOpen(3, 4)).isTrue();
    }

    @Test
    public void testConnectionThroughEmptyRange() {
        DoubleRange lower = DoubleRange.closedOpen(2, 4);
        DoubleRange higher = DoubleRange.closedOpen(4, 6);

        assertThat(lower.isConnected(higher)).as(CONNECTION_DESC, lower, higher, DoubleRange.closedOpen(4, 4)).isTrue();
    }

    @Test
    public void testConnectedInfinity() {
        DoubleRange infinite = DoubleRange.all();
        DoubleRange finite = DoubleRange.closedOpen(2, 4);

        assertThat(infinite.isConnected(finite))
                .as("%s and %s should be connected, %s is enclosed by both", infinite, finite, finite)
                .isTrue();
    }
}
