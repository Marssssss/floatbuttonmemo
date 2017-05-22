package com.example.clevercong.floatbutton;

import android.app.Application;

import com.example.clevercong.floatbutton.utils.LogUtils;
import com.iflytek.cloud.SpeechUtility;

/**
 * Created by clevercong on 2017/5/22.
 */

public class MainApp extends Application {
    private static final String TAG = "MainApp";

    @Override
    public void onCreate() {
        // 应用程序入口处调用，避免手机内存过小，杀死后台进程后通过历史intent进入Activity造成SpeechUtility对象为null
        // 如在Application中调用初始化，需要在Mainifest中注册该Applicaiton
        // 注意：此接口在非主进程调用会返回null对象，如需在非主进程使用语音功能，请增加参数：SpeechConstant.FORCE_LOGIN+"=true"
        // 参数间使用半角“,”分隔。
        // 设置你申请的应用appid,请勿在'='与appid之间添加空格及空转义符
        logi("OnCreate");
        // 注意： appid 必须和下载的SDK保持一致，否则会出现10407错误

        SpeechUtility.createUtility(this, "appid=" + getString(R.string.app_id));

        // 以下语句用于设置日志开关（默认开启），设置成false时关闭语音云SDK日志打印
        // Setting.setShowLog(false); // import com.iflytek.cloud.Setting;
        super.onCreate();
    }

    private void logi(String s) {
        LogUtils.logi(TAG, s);
    }
}
