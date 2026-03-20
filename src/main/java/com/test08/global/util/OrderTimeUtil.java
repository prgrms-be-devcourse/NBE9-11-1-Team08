package com.test08.global.util;

import java.time.LocalDateTime;

public final class OrderTimeUtil {

    private OrderTimeUtil() {
    }

    public static LocalDateTime now() {
        return LocalDateTime.now();
    }
}
