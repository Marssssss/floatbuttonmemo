package com.example.clevercong.floatbutton.utils;

import android.util.Log;

/**
 * Created by clevercong on 2017/5/21.
 */

public class LogUtils {
    static boolean DEBUG = true;
    static boolean INFO = true;

    public static void logd(String TAG, String s) {
        if (DEBUG) {
            Log.d(TAG, s);
        }
    }

    public static void logi(String TAG, String s) {
        if (INFO) {
            Log.i(TAG, s);
        }
    }

    public static void loge(String TAG, String s) {
        Log.e(TAG, s);
    }
}
