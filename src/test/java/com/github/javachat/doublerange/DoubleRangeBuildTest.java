package com.github.javachat.doublerange;

import com.github.javachat.common.BoundType;
import org.testng.annotations.Test;

import java.util.NoSuchElementException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.shouldHaveThrown;

public class DoubleRangeBuildTest {
    @Test
    public void testIllegalBounds() {
        try {
            new DoubleRange(3.0, BoundType.CLOSED, 2.0, BoundType.CLOSED);
            shouldHaveThrown(IllegalArgumentException.class);
        } catch (IllegalArgumentException e) {
            final String msg = String.format(DoubleRange.ILLEGAL_BOUND, 3.0, 2.0);
            assertThat(e).hasMessage(msg);
        }
    }

    @Test
    public void testIllegalOpenRange() {
        try {
            new DoubleRange(3, BoundType.OPEN, 3, BoundType.OPEN);
            shouldHaveThrown(IllegalArgumentException.class);
        } catch (IllegalArgumentException e) {
            assertThat(e).hasMessage(DoubleRange.ILLEGAL_OPEN_RANGE);
        }
    }

    @Test
    public void testNotANumber() {
        try {
            new DoubleRange(Double.NaN, BoundType.OPEN, 3, BoundType.OPEN);
            shouldHaveThrown(IllegalArgumentException.class);
        } catch (IllegalArgumentException e) {
            assertThat(e).hasMessage(DoubleRange.BOUNDARY_IS_NAN);
        }

        try {
            new DoubleRange(2, BoundType.OPEN, Double.NaN, BoundType.OPEN);
            shouldHaveThrown(IllegalArgumentException.class);
        } catch (IllegalArgumentException e) {
            assertThat(e).hasMessage(DoubleRange.BOUNDARY_IS_NAN);
        }
    }

    @Test
    public void testIllegalInfinities() {
        try {
            new DoubleRange(Double.POSITIVE_INFINITY, BoundType.OPEN, Double.NEGATIVE_INFINITY, BoundType.OPEN);
            shouldHaveThrown(IllegalArgumentException.class);
        } catch (IllegalArgumentException e) {
            final String msg = String.format(DoubleRange.ILLEGAL_BOUND, Double.POSITIVE_INFINITY, Double.NEGATIVE_INFINITY);
            assertThat(e).hasMessage(msg);
        }
    }

    @Test
    public void testEncloseAllFactoryMethod() {
        DoubleRange range = DoubleRange.encloseAll(-1, 0, 1);
        assertThat(range).isEqualTo(DoubleRange.closed(-1, 1));
    }

    @Test(expectedExceptions = NoSuchElementException.class)
    public void testEncloseAllFactoryMethodEmptyArray() {
        DoubleRange.encloseAll();
    }
}
