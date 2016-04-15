package com.victor.independenceday.userinfo;

import android.graphics.Bitmap;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;

/**
 * Created by Віктор on 05.11.2015.
 */
public class ProgressContainer {

    private Bitmap bitmap;
    private Rect src;
    private RectF dst;
    private Paint paint;

    private int displayWidth;
    private int displayHeight;

    public ProgressContainer(Bitmap bitmap, int displayWidth, int displayHeight){
        this.bitmap = bitmap;
        this.displayWidth = displayWidth;
        this.displayHeight = displayHeight;
        init();
    }

    public void init(){
        paint = new Paint();
        src = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        dst = new RectF();
        float index = (float) bitmap.getWidth() / (float) bitmap.getHeight();
        dst.top = 2f;
        dst.left = displayWidth * .1f;
        dst.right = displayWidth * .9f;
        dst.bottom = dst.top + (dst.right - dst.left) / index;
    }


    public Bitmap getBitmap() {
        return bitmap;
    }

    public Rect getSrc() {
        return src;
    }

    public RectF getDst() {
        return dst;
    }

    public Paint getPaint() {
        return paint;
    }
}
