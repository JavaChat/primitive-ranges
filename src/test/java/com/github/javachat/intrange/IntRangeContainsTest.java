package com.github.javachat.intrange;

import com.github.javachat.common.BoundType;
import org.testng.annotations.Test;

import static org.assertj.core.api.Assertions.assertThat;

public final class IntRangeContainsTest
{
    @Test
    public void singleValueRangeTest()
    {
        final IntRange range = new IntRange(1, BoundType.CLOSED, 2,
            BoundType.OPEN);

        assertThat(range.contains(1)).isTrue();
        assertThat(range.contains(2)).isFalse();
        assertThat(range.contains(0)).isFalse();
    }

    @Test
    public void rangeTest1()
    {
        final IntRange range = new IntRange(-7, BoundType.OPEN, 5,
            BoundType.OPEN);

        assertThat(range.contains(3)).isTrue();
        assertThat(range.contains(-7)).isFalse();
        assertThat(range.contains(5)).isFalse();
        assertThat(range.contains(-6)).isTrue();
        assertThat(range.contains(4)).isTrue();
        assertThat(range.contains(0)).isTrue();
    }
}
