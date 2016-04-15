package com.victor.independenceday.ui;

import android.os.Handler;

/**
 * Created by Віктор on 05.11.2015.
 */
public class BackUIThread implements Runnable {

    private Handler handler;
    private int timeout;
    private int msg;

    public BackUIThread(Handler handler, int timeout, int msg) {
        this.handler = handler;
        this.timeout = timeout;
        this.msg = msg;
    }

    @Override
    public void run() {
        try {
            Thread.sleep(timeout);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        handler.sendEmptyMessage(msg);
    }
}
