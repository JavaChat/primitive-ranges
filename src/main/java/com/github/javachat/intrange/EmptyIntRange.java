package com.github.javachat.intrange;

public final class EmptyIntRange
    extends IntRange
{
    @Override
    public boolean contains(final int value)
    {
        return false;
    }
}
