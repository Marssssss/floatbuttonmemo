package com.example.clevercong.floatbutton;

/**
 * Created by clevercong on 2017/5/21.
 */

public class Contants {
    public static final long DOUBLE_CLICK_INTERVAL = 500; // 500ms
    public static final long DOUBLE_CLICK_TWICE_INTERVAL = 1000; // 1s
    public static final long LONG_PRESS_INTERVAL = 500; // 500ms

    // EVENT
    private static final int EVENT_BASE = 1000;
    public static final int EVENT_CLICK = EVENT_BASE + 1;
    public static final int EVENT_DOUBLE_CLICK = EVENT_BASE + 2;
    public static final int EVENT_LONG_PRESS = EVENT_BASE + 3;
}
