package com.example.clevercong.floatbutton.handler;


import android.os.Handler;
import android.os.Message;

import com.example.clevercong.floatbutton.utils.LogUtils;

import static com.example.clevercong.floatbutton.Contants.*;

/**
 * Created by clevercong on 2017/5/21.
 */

public class FloatWindowSmallViewHandler extends Handler {
    private static final String TAG = "FloatWindowSmallViewHandler";
    private long mLastDoubleClickTime;

    public FloatWindowSmallViewHandler() {

    }

    @Override
    public void handleMessage(Message msg) {
        logd("handleMessage: msg.what = " + msg.what);
        switch (msg.what) {
            case EVENT_CLICK:
                break;
            case EVENT_DOUBLE_CLICK:
                handleDoubleClick();
                break;
            case EVENT_LONG_PRESS:
                break;
            default:
                break;
        }
        super.handleMessage(msg);
    }

    private void handleDoubleClick() {
        // 对多击事件进行过滤
        if (System.currentTimeMillis() - mLastDoubleClickTime < DOUBLE_CLICK_TWICE_INTERVAL) {
            return;
        }
        mLastDoubleClickTime = System.currentTimeMillis();
    }

    private void logd(String s) {
        LogUtils.logd(TAG, s);
    }
}
