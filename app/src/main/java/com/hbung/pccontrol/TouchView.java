package com.hbung.pccontrol;

import android.content.Context;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

/**
 * 作者　　: 李坤
 * 创建时间:2017/3/1　14:39
 * 邮箱　　：496546144@qq.com
 * <p>
 * 功能介绍：
 */

public class TouchView extends View {
    GestureDetector gestureDetector;
    OnCallback onCallback;

    public void setOnCallback(OnCallback onCallback) {
        this.onCallback = onCallback;
    }

    public TouchView(Context context) {
        this(context, null);
    }

    public TouchView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TouchView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context, attrs);
    }

    private void initView(Context context, AttributeSet attrs) {
        gestureDetector = new GestureDetector(onGestureListener);
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return gestureDetector.onTouchEvent(event);
    }

    GestureDetector.SimpleOnGestureListener onGestureListener = new GestureDetector.SimpleOnGestureListener() {

        @Override
        public boolean onDown(MotionEvent e) {
            return true;
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            return false;
        }

        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            return false;
        }

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            if (onCallback != null) {
                MoveData d = new MoveData();
                d.distanceX = (int) distanceX;
                d.distanceY = (int) distanceY;
                onCallback.onMove(d);
            }
            return true;

        }
    };

    public interface OnCallback {
        void onMove(MoveData moveData);
    }
}
