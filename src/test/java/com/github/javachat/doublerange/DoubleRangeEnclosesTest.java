package com.github.javachat.doublerange;

import com.github.javachat.common.BoundType;
import org.testng.annotations.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class DoubleRangeEnclosesTest {
    private static final String ENCLOSURE_DESC = "%s should enclose %s";
    private static final String SELF_ENCLOSURE_DESC = "%s should enclose itself";

    @Test
    public void testSimpleEnclosure() {
        DoubleRange outer = new DoubleRange(0.0, BoundType.OPEN, 10.0, BoundType.OPEN);
        DoubleRange inner = new DoubleRange(4.0, BoundType.OPEN, 6.0, BoundType.OPEN);

        assertThat(outer.encloses(inner)).as(ENCLOSURE_DESC, outer, inner).isTrue();
    }

    @Test
    public void testSimpleNonEnclosure() {
        DoubleRange lower = DoubleRange.open(0, 9);
        DoubleRange higher = DoubleRange.open(10, 20);

        assertThat(lower.encloses(higher)).as("%s should not enclose unrelated %s", lower, higher).isFalse();
    }

    @Test
    public void testOpenRangeEnclosesSelf() {
        DoubleRange one = DoubleRange.open(0, 9);
        DoubleRange two = DoubleRange.open(0, 9);

        assertThat(one.encloses(two)).as(SELF_ENCLOSURE_DESC, one).isTrue();
    }

    @Test
    public void testOpenClosedRangeEnclosesSelf() {
        DoubleRange one = DoubleRange.openClosed(0, 9);
        DoubleRange two = DoubleRange.openClosed(0, 9);

        assertThat(one.encloses(two)).as(SELF_ENCLOSURE_DESC, one).isTrue();
    }

    @Test
    public void testClosedDoesNotEncloseOpenCounterpart() {
        DoubleRange closed = DoubleRange.closed(0, 9);
        DoubleRange open = DoubleRange.open(0, 9);

        assertThat(closed.encloses(open)).as("%s should not enclose open counterpart %s", closed, open).isFalse();
    }

    @Test
    public void testOpenBoundaryDoesNotEncloseClosedBoundary() {
        DoubleRange withOpenBoundary = DoubleRange.openClosed(3, 6);
        DoubleRange withClosedBoundary = DoubleRange.closed(3, 6);

        assertThat(withOpenBoundary.encloses(withClosedBoundary)).as("%s should not contain %s").isFalse();
    }

    @Test
    public void testInnerRangeDoesNotEncloseOuter() {
        DoubleRange inner = DoubleRange.closed(4, 5);
        DoubleRange outer = DoubleRange.open(3, 6);

        assertThat(inner.encloses(outer)).isFalse();
    }

    @Test
    public void testInfinityEnclosure() {
        DoubleRange infinite = DoubleRange.all();
        DoubleRange finite = DoubleRange.openClosed(3, 6);

        assertThat(infinite.encloses(finite)).as("infinity encloses any range").isTrue();
        assertThat(finite.encloses(infinite)).as("finite range can not enclose infinite range").isFalse();
    }
}
