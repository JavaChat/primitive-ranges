package com.github.javachat.common;

/**
 * The type of bounds for a primitive range.
 *
 * <p>This is in fact an exact emulation of Guava's {@link
 * com.google.common.collect.BoundType}.</p>
 */
public enum BoundType
{
    /**
     * Closed boundary: the bound is considered as part of the set
     */
    CLOSED,

    /**
     * Open boundary: the bound is <em>not</em> considered as part of the set
     */
    OPEN,
    ;
}
