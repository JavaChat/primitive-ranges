package com.github.javachat.doublerange;

import org.testng.annotations.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class DoubleRangeObjectMethodsTest {
    @Test
    public void testFunctionalEquality() {
        assertThat(DoubleRange.open(0, 1)).isEqualTo(DoubleRange.open(0, 1));
        assertThat(DoubleRange.open(0, 1)).isNotEqualTo(DoubleRange.open(1, 2));
        assertThat(DoubleRange.open(0, 1)).isNotEqualTo(DoubleRange.openClosed(0, 1));
        assertThat(DoubleRange.open(0, 1)).isNotEqualTo(DoubleRange.closedOpen(0, 1));
        assertThat(DoubleRange.open(0, 1)).isNotEqualTo(DoubleRange.closed(0, 1));
    }

    @Test
    public void testEqualToContract() {
        final DoubleRange range = DoubleRange.closed(0, 10);
        assertThat(range.equals(null)).as("null detection").isFalse();
        assertThat(range.equals("")).as("different type").isFalse();
        assertThat(range.equals(range)).as("reflexive").isTrue();
    }


    @Test
    public void testHashCode() {
        final DoubleRange range = DoubleRange.closed(0, 10);
        final DoubleRange sameRange = DoubleRange.closed(0, 10);
        final DoubleRange otherRange = DoubleRange.closed(3, 6);

        assertThat(range.hashCode()).as("identity").isEqualTo(range.hashCode());
        assertThat(range.hashCode()).as("equal objects").isEqualTo(sameRange.hashCode());
        assertThat(range.hashCode()).as("different object").isNotEqualTo(otherRange.hashCode());
    }
}
