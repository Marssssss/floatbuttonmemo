package com.example.clevercong.floatbutton.handler;


import android.content.Context;
import android.os.Handler;
import android.os.Message;

import com.example.clevercong.floatbutton.MyWindowManager;
import com.example.clevercong.floatbutton.utils.LogUtils;

import static com.example.clevercong.floatbutton.Contants.DOUBLE_CLICK_TWICE_INTERVAL;
import static com.example.clevercong.floatbutton.Contants.EVENT_CLICK;
import static com.example.clevercong.floatbutton.Contants.EVENT_DOUBLE_CLICK;
import static com.example.clevercong.floatbutton.Contants.EVENT_LONG_PRESS;

/**
 * Created by clevercong on 2017/5/21.
 */

public class FloatWindowSmallViewHandler extends Handler {
    private static final String TAG = "FloatWindowSmallViewHandler";
    private long mLastDoubleClickTime;
    private Context mContext;

    public FloatWindowSmallViewHandler(Context context) {
        this.mContext = context;
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
                openBigWindow();
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

    private void openBigWindow() {
        MyWindowManager.createBigWindow(mContext);
        MyWindowManager.removeSmallWindow(mContext);
    }

    private void logd(String s) {
        LogUtils.logd(TAG, s);
    }
}
