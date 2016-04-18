package com.github.javachat.common;

public class Constants {
    private Constants() {
        throw new Error("Not instantiable");
    }

    /**
     * Error message for calls to intersection with two unconnected ranges.
     */
    public static final String NO_CONNECTION = "Cannot create intersection from two unconnected ranges (%s and %s)";

    /**
     * The representation fo infinity to use in #toString
     */
    public static final String INFINITY_REPRESENTATION = "Infinity";
}
