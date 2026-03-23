package com.test08.global.util;

import java.time.LocalTime;

public final class OrderTimeUtil {

    private OrderTimeUtil() {}

    public static boolean isAfterTime() {
        if (LocalTime.now().isAfter(LocalTime.of(14, 0))) {
            return true;
        }
        return false;
    }
}
