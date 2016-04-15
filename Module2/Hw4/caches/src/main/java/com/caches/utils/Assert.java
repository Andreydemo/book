package com.caches.utils;

public final class Assert {

    private Assert() {
    }

    public static void hasText(String var, String message) {
        if (var == null || var.trim().isEmpty()) {
            throw new IllegalArgumentException(message);
        }
    }

    public static void notNull(Object var, String message) {
        if (var == null) {
            throw new IllegalArgumentException(message);
        }
    }
}
