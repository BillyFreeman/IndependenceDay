package com.victor.independenceday.physics;

import android.graphics.PointF;

/**
 * Created by Віктор on 29.09.2015.
 */
public class Sinusoid implements Trajectory {

    private float axis;
    private double amplitude;
    private int dirrection;

    int height;
    int width;
    float hPoint;
    float wPoint;

    public static final int B = 100;

    public Sinusoid(float axis, double amplitude, int width, int height) {

        super();
        this.axis = axis;
        this.amplitude = (1d / amplitude);
        dirrection = (int) Math.round(Math.random());

        this.height = height;
        this.width = width;
        this.wPoint = ((float) width) / 1000;
        this.hPoint = ((float) height) / 1000;
    }

    @Override
    public PointF getNextPoint(PointF basePoint, int speed) {
        float y = (float) (axis + B * Math.sin(amplitude * (basePoint.y + speed * hPoint)));
        y = dirrection == 1 ? y : axis - (y - axis);
        basePoint = new PointF(y, basePoint.y + speed * hPoint);
        return basePoint;
    }

    public float getAxis() {
        return axis;
    }

    public void setAxis(float axis) {
        this.axis = axis;
    }

    public void setAmplitude(float amplitude) {
        this.amplitude = amplitude;
    }

    @Override
    public Trajectory setWidth(int height) {
        this.height = height;
        this.hPoint = ((float) height) / 1000;
        return this;
    }

    @Override
    public Trajectory setHeight(int width) {
        this.width = width;
        this.wPoint = ((float) width) / 1000;
        return this;
    }
}
