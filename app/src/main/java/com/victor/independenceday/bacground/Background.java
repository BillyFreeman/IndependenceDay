package com.victor.independenceday.bacground;

import android.graphics.Bitmap;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;

import com.victor.independenceday.objects.ObjectConstants;

/**
 * Created by Віктор on 30.10.2015.
 */
public class Background {

    private Bitmap bitmap;
    private Rect src;
    private RectF dst;
    private Paint paint;
    private int baseLine;
    private float index;

    private float adapterIndex;

    private int height;
    private int width;

    public Background(Bitmap bitmap, int width, int height, int baseLine) {
        this.bitmap = bitmap;
        this.height = height;
        this.width = width;
        this.baseLine = baseLine;
        init(baseLine);
    }

    public void init(int baseLine) {
        dst = new RectF();
        initDst(baseLine);
        index = (float) height / (float) width;
        adapterIndex = (float) bitmap.getWidth() / (float) width;
        src = new Rect();
        src.bottom = bitmap.getHeight();
        initSrc(0);
        paint = new Paint();
    }

    public void initSrc(int offset) {
        src.bottom = (int) (src.bottom - offset * adapterIndex);
        src.right = bitmap.getWidth();
        src.left = 0;
        src.top = (int) (src.bottom - src.right * this.index);
    }

    public void initDst(int dBaseline) {
        dst.top = dBaseline;
        dst.left = 0;
        dst.bottom = dBaseline + this.height;
        dst.right = dst.left + this.width;
    }

    public void doNextMove() {
        if (baseLine < 0 || src.top <= 0) {
            baseLine += ObjectConstants.BACKGROUND_SPEED;
            initDst(baseLine);
        } else {
            initSrc(ObjectConstants.BACKGROUND_SPEED);
        }
    }


    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public Rect getSrc() {
        return src;
    }

    public void setSrc(Rect src) {
        this.src = src;
    }

    public RectF getDst() {
        return dst;
    }

    public void setDst(RectF dst) {
        this.dst = dst;
    }

    public Paint getPaint() {
        return paint;
    }

    public void setPaint(Paint paint) {
        this.paint = paint;
    }

    public int getBaseLine() {
        return baseLine;
    }

    public void setBaseLine(int baseLine) {
        this.baseLine = baseLine;
    }
}
