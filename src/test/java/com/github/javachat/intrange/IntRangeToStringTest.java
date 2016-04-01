package com.github.javachat.intrange;

import com.github.javachat.common.BoundType;
import org.testng.annotations.Test;

import static org.assertj.core.api.Assertions.assertThat;

public final class IntRangeToStringTest
{
    @Test
    public void firstToStringTest()
    {
        final IntRange range = new IntRange(3, BoundType.OPEN, 5,
            BoundType.OPEN);
        assertThat(range.toString()).isEqualTo("(3..5)");
    }
}
