package com.github.javachat.common;

/**
 * The type of bounds for a primitive range.
 *
 * <p>This is in fact an exact emulation of Guava's <a target="_blank"
 * href="http://google.github.io/guava/releases/19.0/api/docs/com/google/common/collect/BoundType.html">BoundType</a>.
 * </p>
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
