package com.example.clevercong.floatbutton.asr;

import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;

import com.iflytek.cloud.ErrorCode;
import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.RecognizerListener;
import com.iflytek.cloud.RecognizerResult;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechRecognizer;

import java.util.HashMap;
import java.util.LinkedHashMap;

/**
 * Created by maxf on 2017/5/24.
 */
public class AsrApi {
    private final String TAG = AsrApi.class.getSimpleName();

    // 语音听写对象
    private SpeechRecognizer mRecognizer;
    // 用HashMap存储听写结果
    private HashMap<String, String> mIatResults = new LinkedHashMap<String, String>();
    // 引擎类型
    private String mEngineType = SpeechConstant.TYPE_CLOUD;
    //静音超时时间
    private final String SILENCE_TIMEOUT = "4000";
    //语言
    private final String LANGUAGE = "zh_cn";
    //地区
    private final String ACCENT = "mandarin";

    public enum State{
        IDLE,
        RUNNING
    }
    private State mState = State.IDLE;
    private ApiCallback mCallback;
    private Context mContext;

    private static AsrApi sInstance;
    public static AsrApi getInstance(Context context) {
        if (sInstance == null) {
            return new AsrApi(context);
        }
        return sInstance;
    }
    private AsrApi(Context context) {
        mContext = context;
        init();
    }

    private void init() {
        mRecognizer = SpeechRecognizer.createRecognizer(mContext, mInitListener);
    }

    public void setContext(Context context) {
        if (mContext != context) {
            mContext = context;
            init();
        } else {
            Log.d(TAG, "no need to update context");
        }
    }

    public void setCallback(ApiCallback callback) {
        mCallback = callback;
    }

    public State getState() {
        return mState;
    }

    public int startAsr() {
        if (mCallback == null) {
            Log.e(TAG, "need set callback object first before start Asr!");
            return -3;
        }

        if (mState == State.RUNNING) {
            Log.e(TAG, "now ASR is already running, please stop first");
            return -4;
        }
        setParam();
        int ret = mRecognizer.startListening(mRecognizerListener);
        return ret;
    }

    public void stopAsr() {
        if (mState == State.IDLE) {
            return;
        }
        mRecognizer.stopListening();
    }

    public void cancelAsr() {
        if (mState == State.IDLE) {
            return;
        }
        mRecognizer.cancel();
    }

    /**
     * 参数设置
     *
     * @param
     * @return
     */
    private void setParam() {
        // 清空参数
        mRecognizer.setParameter(SpeechConstant.PARAMS, null);

        // 设置听写引擎
        mRecognizer.setParameter(SpeechConstant.ENGINE_TYPE, mEngineType);
        // 设置返回结果格式
        mRecognizer.setParameter(SpeechConstant.RESULT_TYPE, "json");

        // 设置语言
        mRecognizer.setParameter(SpeechConstant.LANGUAGE, LANGUAGE);
        // 设置语言区域
        mRecognizer.setParameter(SpeechConstant.ACCENT, ACCENT);

        // 设置语音前端点:静音超时时间，即用户多长时间不说话则当做超时处理
        mRecognizer.setParameter(SpeechConstant.VAD_BOS, SILENCE_TIMEOUT);

        // 设置语音后端点:后端点静音检测时间，即用户停止说话多长时间内即认为不再输入， 自动停止录音
        mRecognizer.setParameter(SpeechConstant.VAD_EOS, "1000");

        // 设置标点符号,设置为"0"返回结果无标点,设置为"1"返回结果有标点
        mRecognizer.setParameter(SpeechConstant.ASR_PTT, "1");

        // 设置音频保存路径，保存音频格式支持pcm、wav，设置路径为sd卡请注意WRITE_EXTERNAL_STORAGE权限
        // 注：AUDIO_FORMAT参数语记需要更新版本才能生效
        mRecognizer.setParameter(SpeechConstant.AUDIO_FORMAT,"wav");
        mRecognizer.setParameter(SpeechConstant.ASR_AUDIO_PATH, Environment.getExternalStorageDirectory()+"/msc/iat.wav");
    }

    /**
     * 初始化监听器。
     */
    private InitListener mInitListener = new InitListener() {

        @Override
        public void onInit(int code) {
            Log.d(TAG, "SpeechRecognizer init() code = " + code);
            if (code != ErrorCode.SUCCESS) {
                //to do
            }
        }
    };

    /**
     * 听写监听器。
     */
    private RecognizerListener mRecognizerListener = new RecognizerListener() {

        @Override
        public void onBeginOfSpeech() {
            // 此回调表示：sdk内部录音机已经准备好了，用户可以开始语音输入
            mCallback.onBeginOfSpeech();
        }

        @Override
        public void onError(SpeechError error) {
            // Tips：
            // 错误码：10118(您没有说话)，可能是录音机权限被禁，需要提示用户打开应用的录音权限。
            // 如果使用本地功能（语记）需要提示用户开启语记的录音权限。
            mCallback.onError(error);
        }

        @Override
        public void onEndOfSpeech() {
            // 此回调表示：检测到了语音的尾端点，已经进入识别过程，不再接受语音输入
            mCallback.onEndOfSpeech();
        }

        @Override
        public void onResult(RecognizerResult results, boolean isLast) {
            Log.d(TAG, results.getResultString());
            mCallback.onResult(results, isLast);
        }

        @Override
        public void onVolumeChanged(int volume, byte[] data) {
            mCallback.onVolumeChanged(volume, data);
        }

        @Override
        public void onEvent(int eventType, int arg1, int arg2, Bundle obj) {
            // 以下代码用于获取与云端的会话id，当业务出错时将会话id提供给技术支持人员，可用于查询会话日志，定位出错原因
            // 若使用本地能力，会话id为null
            //	if (SpeechEvent.EVENT_SESSION_ID == eventType) {
            //		String sid = obj.getString(SpeechEvent.KEY_EVENT_SESSION_ID);
            //		Log.d(TAG, "session id =" + sid);
            //	}

        }
    };

    interface ApiCallback {
        public void onBeginOfSpeech();
        public void onError(SpeechError error);
        public void onEndOfSpeech();
        public void onResult(RecognizerResult results, boolean isLast);
        public void onVolumeChanged(int volume, byte[] data);
    }
}
