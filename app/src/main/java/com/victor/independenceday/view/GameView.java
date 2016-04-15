package com.victor.independenceday.view;

import android.app.Activity;
import android.content.Context;
import android.graphics.PointF;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.victor.independenceday.controller.ViewStateController;

/**
 * Created by Віктор on 27.09.2015.
 */
public class GameView extends SurfaceView implements SurfaceHolder.Callback {

    private ViewStateController controller;
    private Activity context;

    private GameThread mainThread;
    private SurfaceHolder holder;

    private PointF touchPoint;

    private static final String TAG = GameView.class.getSimpleName();

    public GameView(Context context) {
        super(context);
        holder = getHolder();
        holder.addCallback(this);

        this.context = (Activity) context;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        controller = ViewStateController.getController(this.context, getWidth(), getHeight());
        if (mainThread != null) {
            mainThread.start();
        }
    }

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        mainThread = new GameThread(context, holder, controller);
        mainThread.start();
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        touchPoint = new PointF(event.getRawX(), event.getRawY());
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                controller.setTouched(true, touchPoint);
                return true;
            case MotionEvent.ACTION_MOVE:
                controller.onFingerMove(touchPoint);
                return true;
            case MotionEvent.ACTION_UP:
                controller.setTouched(false);
                return true;
        }

        return super.onTouchEvent(event);
    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        if (mainThread.isRunning()) {
            stopRender();
        }
//        boolean retry = true;
//        while (retry) {
//            try {
//                thread.join();
//                retry = false;
//            } catch (InterruptedException e) {
//
//            }
//        }
    }

    public void stopRender() {
        if (mainThread != null) {
            mainThread.setRunning(false);
        }
    }

    public GameThread getMainThread() {
        return mainThread;
    }
}
