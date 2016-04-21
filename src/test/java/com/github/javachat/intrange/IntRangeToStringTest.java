package com.github.javachat.intrange;

import org.testng.annotations.Test;

import static org.assertj.core.api.Assertions.assertThat;

public final class IntRangeToStringTest {
    @Test
    public void firstToStringTest() {
        final IntRange range = IntRange.open(3, 5);
        assertThat(range.toString()).isEqualTo("(3..5)");
    }

    @Test
    public void testInfiniteSpanTest() {
        final IntRange range = IntRange.all();
        assertThat(range.toString()).isEqualTo("(-Infinity..Infinity)");
    }
}
