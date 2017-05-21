package com.example.clevercong.floatbutton.view;

import android.content.Context;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.clevercong.floatbutton.MyWindowManager;
import com.example.clevercong.floatbutton.R;
import com.example.clevercong.floatbutton.handler.FloatWindowSmallViewHandler;
import com.example.clevercong.floatbutton.utils.LogUtils;

import java.lang.reflect.Field;

import static com.example.clevercong.floatbutton.Contants.*;

/**
 * Created by clevercong on 2017/5/18.
 */

public class FloatWindowSmallView extends LinearLayout {
    private static final String TAG = "FloatWindowSmallView";

    /**
     * 记录小悬浮窗的宽度
     */
    public static int viewWidth;

    /**
     * 记录小悬浮窗的高度
     */
    public static int viewHeight;

    /**
     * 记录系统状态栏的高度
     */
    private static int statusBarHeight;

    /**
     * 用于更新小悬浮窗的位置
     */
    private WindowManager windowManager;

    /**
     * 小悬浮窗的参数
     */
    private WindowManager.LayoutParams mParams;

    /**
     * 记录当前手指位置在屏幕上的横、纵坐标值
     */
    private float xInScreen;
    private float yInScreen;

    /**
     * 记录手指按下时在屏幕上的横、纵坐标的值
     */
    private float xDownInScreen;
    private float yDownInScreen;

    /**
     * 记录手指按下时在小悬浮窗的View上的横、纵坐标的值
     */
    private float xInView;
    private float yInView;

    /**
     * 记录上一次手指单击的时间，用来区分单击、双击
     */
    private long mLastClickTime;
    /**
     * 记录上一次手指长按得时间，用来区分单击、长按
     */
    private long mLastTouchTime;

    public FloatWindowSmallView(Context context) {
        super(context);
        windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        LayoutInflater.from(context).inflate(R.layout.float_window_small, this);
        View view = findViewById(R.id.small_window_layout);
        viewWidth = view.getLayoutParams().width;
        viewHeight = view.getLayoutParams().height;
        TextView percentView = (TextView) findViewById(R.id.percent);
        percentView.setText(MyWindowManager.getUsedPercentValue(context));
    }

    private FloatWindowSmallViewHandler mHandler = new FloatWindowSmallViewHandler();

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                // 手指按下时记录必要数据,纵坐标的值都需要减去状态栏高度
                xInView = event.getX();
                yInView = event.getY();
                xDownInScreen = event.getRawX();
                yDownInScreen = event.getRawY() - getStatusBarHeight();
                xInScreen = event.getRawX();
                yInScreen = event.getRawY() - getStatusBarHeight();
                mLastTouchTime = System.currentTimeMillis();
                break;
            case MotionEvent.ACTION_MOVE:
                xInScreen = event.getRawX();
                yInScreen = event.getRawY() - getStatusBarHeight();
                // 手指移动的时候更新小悬浮窗的位置
                updateViewPosition();
                break;
            case MotionEvent.ACTION_UP:
                // 如果手指离开屏幕时，xDownInScreen和xInScreen相等，且yDownInScreen和yInScreen相等，则视为触发了单击事件。
                if (xDownInScreen == xInScreen && yDownInScreen == yInScreen) {
                    if (System.currentTimeMillis() - mLastClickTime < DOUBLE_CLICK_INTERVAL) {
                        // 间隔小于500ms，添加双击事件，移除单击事件
                        mHandler.removeMessages(EVENT_CLICK);
                        Message dClickMsg = mHandler.obtainMessage(EVENT_DOUBLE_CLICK);
                        mHandler.sendMessage(dClickMsg);
                    } else if (System.currentTimeMillis() - mLastTouchTime < LONG_PRESS_INTERVAL) {
                        // 间隔大于500ms，添加单击事件
                        Message clickMsg = mHandler.obtainMessage(EVENT_CLICK);
                        mHandler.sendMessageDelayed(clickMsg, DOUBLE_CLICK_INTERVAL);
                    } else {
                        // 其余，添加长按时间；注意：受第一条if语句空值，移动位置的长按不发送长按事件
                        Message clickMsg = mHandler.obtainMessage(EVENT_LONG_PRESS);
                        mHandler.sendMessage(clickMsg);
                    }
                    mLastClickTime = System.currentTimeMillis();
                }
                break;
            default:
                break;
        }
        return true;
    }

    /**
     * 将小悬浮窗的参数传入，用于更新小悬浮窗的位置。
     *
     * @param params
     *            小悬浮窗的参数
     */
    public void setParams(WindowManager.LayoutParams params) {
        mParams = params;
    }

    /**
     * 更新小悬浮窗在屏幕中的位置。
     */
    private void updateViewPosition() {
        mParams.x = (int) (xInScreen - xInView);
        mParams.y = (int) (yInScreen - yInView);
        windowManager.updateViewLayout(this, mParams);
    }

    /**
     * 打开大悬浮窗，同时关闭小悬浮窗。
     */
    private void openBigWindow() {
        MyWindowManager.createBigWindow(getContext());
        MyWindowManager.removeSmallWindow(getContext());
    }

    /**
     * 用于获取状态栏的高度。
     *
     * @return 返回状态栏高度的像素值。
     */
    private int getStatusBarHeight() {
        if (statusBarHeight == 0) {
            try {
                Class<?> c = Class.forName("com.android.internal.R$dimen");
                Object o = c.newInstance();
                Field field = c.getField("status_bar_height");
                int x = (Integer) field.get(o);
                statusBarHeight = getResources().getDimensionPixelSize(x);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return statusBarHeight;
    }

    private void logd(String s) {
        LogUtils.logd(TAG, s);
    }

}