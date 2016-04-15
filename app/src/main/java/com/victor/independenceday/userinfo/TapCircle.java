package com.victor.independenceday.userinfo;

import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;

/**
 * Created by Віктор on 30.09.2015.
 */
public class TapCircle {

    private PointF center;
    private float radius;
    private float startRadius;
    private Paint paint;

    public TapCircle(PointF center, float startRadius) {
        this.center = center;
        this.startRadius = startRadius;
        this.radius = startRadius;

        paint = new Paint();
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(3f);
        paint.setColor(Color.rgb(40, 255, 228));
        paint.setAntiAlias(true);
    }

    public void recalculate(){
        radius = (radius - 4) < 0 ? startRadius : radius - 4;
    }

    public PointF getCenter() {
        return center;
    }

    public float getRadius() {
        return radius;
    }

    public Paint getPaint() {
        return paint;
    }
}
