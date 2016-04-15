package com.victor.independenceday.physics;

import android.graphics.PointF;

/**
 * Created by Віктор on 29.09.2015.
 */
public class BackLinearVertical implements Trajectory {

    int height;
    int width;
    float hPoint;
    float wPoint;

    public  BackLinearVertical(int width, int height){
        this.height = height;
        this.width = width;
        this.wPoint = ((float) width) / 1000;
        this.hPoint = ((float) height) / 1000;
    }

    @Override
    public PointF getNextPoint(PointF basePoint, int speed) {
        return new PointF(basePoint.x, basePoint.y - speed * hPoint);
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
