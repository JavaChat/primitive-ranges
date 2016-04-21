package com.github.javachat.intrange;

import org.testng.annotations.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class IntRangeSpanTest {
    @Test
    public void testEnclosedRange() {
        IntRange outer = IntRange.closed(0, 10);
        IntRange inner = IntRange.open(3, 6);

        IntRange span = outer.span(inner);

        assertThat(span).isEqualTo(outer);
    }

    @Test
    public void testUnconnectedRanges() {
        IntRange lower = IntRange.closed(0, 3);
        IntRange higher = IntRange.open(5, 7);

        IntRange spanningRange = lower.span(higher);

        assertThat(spanningRange).isEqualTo(IntRange.closedOpen(0, 7));
    }

    @Test
    public void testConnectedRanges() {
        IntRange lower = IntRange.closedOpen(1, 5);
        IntRange higher = IntRange.openClosed(2, 7);

        IntRange spanningRange = lower.span(higher);
        assertThat(spanningRange).isEqualTo(IntRange.closed(1, 7));
    }

    @Test
    public void spanInfinity() {
        IntRange infinite = IntRange.all();
        IntRange finite = IntRange.closedOpen(1, 3);

        IntRange spanningRange = infinite.span(finite);
        assertThat(spanningRange).isEqualTo(IntRange.all());
    }
}
