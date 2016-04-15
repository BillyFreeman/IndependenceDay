package com.victor.independenceday.ui;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.RectF;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.victor.independenceday.R;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Віктор on 04.11.2015.
 */
public class ExitButton extends View {

    private int width;
    private int height;
    private RectF oRect;
    private RectF iRect;
    private PointF center;
    private Bitmap bitmap;
    private Rect src;
    private RectF dst;

    private Timer timer;
    private ViewGroup container;

    private Paint paint;
    private Paint bitPaint;
    private int bitAlpha;
    private int bitAlphaTo;

    private float degrees;
    private float step;
    private float stepTo;

    public ExitButton(Context context) {
        super(context);
    }

    public ExitButton(Context context, ViewGroup parent) {
        this(context);
        this.container = parent;

        oRect = new RectF();
        iRect = new RectF();
        bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_exit_button);
        src = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        dst = new RectF();

        degrees = 0f;
        stepTo = 1f;

        paint = new Paint();
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(Color.rgb(169, 214, 215));
        paint.setAntiAlias(true);

        bitPaint = new Paint();
        bitAlpha = 75;
        bitAlphaTo = 125;

        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (oRect.right <= 0) {
                    width = container.getWidth();
                    height = container.getHeight();

                    center = new PointF((float) width / 2, (float) height / 2);

                    float rad = center.x * .05f;
                    oRect.top = 0 + rad;
                    oRect.left = 0 + rad;
                    oRect.bottom = height - rad;
                    oRect.right = width - rad;

                    rad = center.x * .15f;
                    iRect.top = oRect.top + rad;
                    iRect.left = oRect.left + rad;
                    iRect.bottom = oRect.bottom - rad;
                    iRect.right = oRect.right - rad;

                    rad = center.x * .5f;
                    dst.top = 0 + rad;
                    dst.left = 0 + rad;
                    dst.bottom = height - rad;
                    dst.right = width - rad;
                }
                degrees = degrees + step == 180f ? 0 : degrees + step;
                step = step < stepTo ? step + .5f : stepTo;
                bitAlpha = bitAlpha + 5 >= bitAlphaTo ? bitAlphaTo : bitAlpha + 5;
                bitPaint.setAlpha(bitAlpha);
                postInvalidate();
            }
        }, 0, 20);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        paint.setStrokeWidth(10f);
        canvas.drawArc(oRect, 0 + degrees, 100f, false, paint);
        canvas.drawArc(oRect, 180f + degrees, 100f, false, paint);

        paint.setStrokeWidth(4f);
        canvas.drawCircle(center.x, center.y, iRect.right / 2, paint);
        canvas.drawArc(iRect, 0 - degrees, 100f, false, paint);
        canvas.drawArc(iRect, 180f - degrees, 100f, false, paint);

        canvas.drawBitmap(bitmap, src, dst, bitPaint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return super.onTouchEvent(event);
    }

    public void setStep(float step) {
        this.stepTo = step;
    }

    public  void setIconAlpha(int alpha){
        this.bitAlphaTo = alpha;
    }
}
