package com.example.clevercong.floatbutton.handler;


import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.example.clevercong.floatbutton.MyWindowManager;
import com.example.clevercong.floatbutton.asr.AsrApi;
import com.example.clevercong.floatbutton.utils.LogUtils;
import com.iflytek.cloud.RecognizerResult;
import com.iflytek.cloud.SpeechError;

import static com.example.clevercong.floatbutton.Contants.DOUBLE_CLICK_TWICE_INTERVAL;
import static com.example.clevercong.floatbutton.Contants.EVENT_CLICK;
import static com.example.clevercong.floatbutton.Contants.EVENT_DOUBLE_CLICK;
import static com.example.clevercong.floatbutton.Contants.EVENT_LONG_PRESS;

/**
 * Created by clevercong on 2017/5/21.
 */

public class FloatWindowSmallViewHandler extends Handler implements AsrApi.ApiCallback{
    private static final String TAG = "FloatWindowSmallViewHandler";
    private long mLastDoubleClickTime;
    private Context mContext;
    private AsrApi mAsrApi;

    public FloatWindowSmallViewHandler(Context context) {
        this.mContext = context;
        mAsrApi = AsrApi.getInstance(context);
        mAsrApi.setCallback(this);
    }

    @Override
    public void handleMessage(Message msg) {
        logd("handleMessage: msg.what = " + msg.what);
        switch (msg.what) {
            case EVENT_CLICK:
                handleClick();
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

    private void handleClick() {
        AsrApi.State currentState = mAsrApi.getState();
        if (currentState == AsrApi.State.IDLE) {
            mAsrApi.startAsr();
        } else if (currentState == AsrApi.State.RUNNING) {
            mAsrApi.stopAsr();
        }
    }

    private void handleDoubleClick() {
        // 对多击事件进行过滤
        if (System.currentTimeMillis() - mLastDoubleClickTime < DOUBLE_CLICK_TWICE_INTERVAL) {
            return;
        }
        mLastDoubleClickTime = System.currentTimeMillis();
        openBigWindow();
    }

    private void openBigWindow() {
        MyWindowManager.createBigWindow(mContext);
        MyWindowManager.removeSmallWindow(mContext);
    }

    private void logd(String s) {
        LogUtils.logd(TAG, s);
    }

    @Override
    public void onBeginOfSpeech() {
        logd("onBeginOfSpeech");
        MyWindowManager.createRecordView(mContext);
    }

    @Override
    public void onError(SpeechError error) {
        logd("onError");
    }

    @Override
    public void onEndOfSpeech() {
        logd("onEndOfSpeech");
    }

    @Override
    public void onResult(RecognizerResult results, boolean isLast) {
        logd("onResult");
        MyWindowManager.removeRecordView(mContext);
    }

    @Override
    public void onVolumeChanged(int volume, byte[] data) {
        logd("onVolumeChanged");
    }
}
