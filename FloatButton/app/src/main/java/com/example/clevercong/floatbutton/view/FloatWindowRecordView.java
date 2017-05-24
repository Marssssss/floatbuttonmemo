package com.example.clevercong.floatbutton.view;

import android.content.Context;
import android.graphics.Canvas;
import android.os.SystemClock;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.clevercong.floatbutton.Contants;
import com.example.clevercong.floatbutton.MyWindowManager;
import com.example.clevercong.floatbutton.R;
import com.example.clevercong.floatbutton.handler.FloatWindowSmallViewHandler;
import com.example.clevercong.floatbutton.utils.LogUtils;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by maxf on 2017/5/24.
 */
public class FloatWindowRecordView extends LinearLayout{

    private static final String TAG = FloatWindowRecordView.class.getSimpleName();
    ViewGroup mViewGroup;
    Timer timer;

    ImageView mRecordingIcon;
    TextView mRecordingTime;
    long mStartTime;

    public FloatWindowRecordView(Context context) {
        super(context);
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        LayoutInflater.from(context).inflate(R.layout.layout_microphone, mViewGroup);
        View view = findViewById(R.id.microphone_layout);

        mRecordingIcon = (ImageView) findViewById(R.id.iv_recording_icon);
        mRecordingTime = (TextView) findViewById(R.id.tv_recording_time);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mStartTime = SystemClock.currentThreadTimeMillis();
        timer.scheduleAtFixedRate(new UpateTimeTask(), 0, 1000);
    }

    class UpateTimeTask extends TimerTask {
        @Override
        public void run() {
            long currentTime = SystemClock.currentThreadTimeMillis();
            long timeDuration = currentTime - mStartTime;
            //录音时间大于最大录音时长
            if (timeDuration > Contants.MAX_RECORD_TIME) {
                LogUtils.loge(TAG, "update time over the max record time!");
                return;
            } else {
                mRecordingTime.setText(timeDuration/60 + ":" + timeDuration%60);
            }
        }
    }
}
