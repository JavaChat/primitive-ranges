package com.github.javachat.doublerange;

import org.testng.annotations.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class DoubleRangeSpanTest {
    @Test
    public void testEnclosedRangeSpan() {
        DoubleRange outer = DoubleRange.open(0, 10);
        DoubleRange inner = DoubleRange.open(3, 6);

        DoubleRange spanningRange = outer.span(inner);
        assertThat(spanningRange).isEqualTo(outer);
    }

    @Test
    public void testUnconnectedRanges() {
        DoubleRange lower = DoubleRange.closed(1, 3);
        DoubleRange higher = DoubleRange.open(5, 7);

        DoubleRange spanningRange = lower.span(higher);
        assertThat(spanningRange).isEqualTo(DoubleRange.closedOpen(1, 7));
    }

    @Test
    public void testConnectedRanges() {
        DoubleRange lower = DoubleRange.closed(1, 5);
        DoubleRange higher = DoubleRange.open(3, 7);

        DoubleRange spanningRange = lower.span(higher);
        assertThat(spanningRange).isEqualTo(DoubleRange.closedOpen(1, 7));
    }

    @Test
    public void spanInfinity() {
        DoubleRange infinite = DoubleRange.all();
        DoubleRange finite = DoubleRange.open(3, 7);

        DoubleRange spanningRange = infinite.span(finite);
        assertThat(spanningRange).isEqualTo(infinite);

        DoubleRange inverseSpan = finite.span(infinite);
        assertThat(inverseSpan).isEqualTo(infinite);
    }
}
