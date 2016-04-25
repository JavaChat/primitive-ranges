package com.github.javachat.intrange;

import org.testng.annotations.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class IntRangeSetAddTest {
    @Test
    public void testAddOneElement() {
        IntRangeSet set = IntRangeSet.create();
        set.add(IntRange.closedOpen(1, 5));

        assertThat(set.asRanges()).containsOnly(IntRange.closedOpen(1, 5));
    }

    @Test
    public void testAddDisconnectedElement() {
        IntRangeSet set = IntRangeSet.create();
        set.add(IntRange.closedOpen(1, 5));
        set.add(IntRange.closedOpen(10, 20));

        assertThat(set.asRanges()).containsOnly(IntRange.closedOpen(1, 5), IntRange.closedOpen(10, 20));
    }

    @Test
    public void testAddingEmptyRangeDoesNothing() {
        IntRangeSet set = IntRangeSet.create();
        set.add(IntRange.closed(1, 1));

        assertThat(set.asRanges()).isEmpty();
    }

    @Test
    public void testConnectedRangesAreCoalesced() {
        IntRangeSet set = IntRangeSet.create();
        set.add(IntRange.closedOpen(1, 5));
        set.add(IntRange.closedOpen(2, 7));

        assertThat(set.asRanges()).containsOnly(IntRange.closedOpen(1, 7));
    }

    @Test
    public void testAddInfiniteRange() {
        IntRangeSet set = IntRangeSet.create();

        set.add(IntRange.all());
        assertThat(set.asRanges()).containsOnly(IntRange.all());

        set.add(IntRange.closed(1, 5));
        assertThat(set.asRanges()).as("bounded range should have been coalesced into unbounded range").containsOnly(IntRange.all());
    }
}
