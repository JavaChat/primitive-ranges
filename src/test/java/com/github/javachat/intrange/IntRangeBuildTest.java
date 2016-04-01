package com.github.javachat.intrange;

import com.github.javachat.common.BoundType;
import org.testng.annotations.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.shouldHaveThrown;

public final class IntRangeBuildTest
{
    @Test
    public void boundsCheck1()
    {
        try {
            new IntRange(3, BoundType.CLOSED, 2, BoundType.CLOSED);
            shouldHaveThrown(IllegalArgumentException.class);
        } catch (IllegalArgumentException e) {
            final String msg = String.format(IntRange.ILLEGAL_BOUNDS, 3, 2);
            assertThat(e).hasMessage(msg);
        }
    }
}
