package com.geoxus.core.common.util;

public class GXTypeOfUtils {
    private GXTypeOfUtils() {

    }

    public static Class<Boolean> typeof(final boolean expr) {
        return Boolean.TYPE;
    }

    public static Class<Character> typeof(final char expr) {
        return Character.TYPE;
    }

    public static Class<Byte> typeof(final byte expr) {
        return Byte.TYPE;
    }

    public static Class<Short> typeof(final short expr) {
        return Short.TYPE;
    }

    public static Class<Integer> typeof(final int expr) {
        return Integer.TYPE;
    }

    public static Class<Long> typeof(final long expr) {
        return Long.TYPE;
    }

    public static Class<Float> typeof(final float expr) {
        return Float.TYPE;
    }

    public static Class<Double> typeof(final double expr) {
        return Double.TYPE;
    }

    public static Class<?> typeof(final Object expr) {
        return expr == null ? null : expr.getClass();
    }
}
