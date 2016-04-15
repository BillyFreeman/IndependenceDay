package com.victor.independenceday.objects.ammo;

import android.graphics.Bitmap;
import android.graphics.PointF;

import com.victor.independenceday.objects.AbstractObject;
import com.victor.independenceday.physics.Trajectory;

/**
 * Created by Віктор on 30.09.2015.
 */
public abstract class Ammo extends AbstractObject {

    private float lunchPointY;
    private float distance;
    private float nextShotDistance;

    protected Ammo(Trajectory trajectory, Bitmap bitmap, PointF basePoint, float width, float height) {
        super(trajectory, bitmap, basePoint, width, height);
    }

    public float getLounchPointY() {
        return lunchPointY;
    }

    public void setLunchPointY(float lounchPointY) {
        this.lunchPointY = lounchPointY;
    }

    public float getDistance() {
        return distance;
    }

    public void setDistance(float distance) {
        this.distance = distance;
    }

    public float getNextShotDistance() {
        return nextShotDistance;
    }

    public void setNextShotDistance(float nextShotDistance) {
        this.nextShotDistance = nextShotDistance;
    }
}
