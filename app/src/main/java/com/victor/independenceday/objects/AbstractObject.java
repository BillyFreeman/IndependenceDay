package com.victor.independenceday.objects;

import android.graphics.Bitmap;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.RectF;

import com.victor.independenceday.physics.Trajectory;

/**
 * Created by Віктор on 27.09.2015.
 */
public abstract class AbstractObject {

    private long score;

    private int healthPoints;
    private int defence;
    private int demage;

    private Bitmap bitmap;
    private Rect src;
    private RectF dst;

    private float width;
    private float height;

    private int speed;

    private PointF basePoint;
    private Trajectory trajectory;

    protected AbstractObject(Trajectory trajectory, Bitmap bitmap, PointF basePoint, float width, float height) {

        this.trajectory = trajectory;
        this.bitmap = bitmap;
        this.basePoint = basePoint;
        this.width = width;
        this.height = height;
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

    public float getWidth() {
        return width;
    }

    public float getHeight() {
        return height;
    }

    public PointF getBasePoint() {
        return basePoint;
    }

    public Trajectory getTrajectory() {
        return trajectory;
    }

    public int getSpeed() {
        return speed;
    }

    public int getHealthPoints() {
        return healthPoints;
    }

    public int getDefence() {
        return defence;
    }

    public void setDefence(int defence) {
        this.defence = defence;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public void setSrc(Rect src) {
        this.src = src;
    }

    public void setDst(RectF dst) {
        this.dst = dst;
    }

    public void setBasePoint(PointF basePoint) {
        this.basePoint = basePoint;
    }

    public void setTrajectory(Trajectory trajectory) {
        this.trajectory = trajectory;
    }

    public void setWidth(float width) {
        this.width = width;
    }

    public void setHeight(float height) {
        this.height = height;
    }

    public void setHealthPoints(int healhPoints) {
        this.healthPoints = healhPoints;
    }

    public void doNextMove() {
        if (trajectory != null && basePoint != null) {
            basePoint = trajectory.getNextPoint(basePoint, speed);
            refreshDst();
        }
    }

    public void doNextMove(PointF basePoint) {
        this.basePoint = basePoint;
        refreshDst();
    }

    public void refreshDst() {
        dst = new RectF();
        dst.left = basePoint.x - width / 2;
        dst.top = basePoint.y - height / 2;
        dst.right = dst.left + width;
        dst.bottom = dst.top + height;
    }

    public abstract void changeSprite(int order);

    public abstract int getSpriteNumber();

    public abstract float getXPrev();

    public abstract void setXPrev(float x);

    public int getDemage() {
        return demage;
    }

    public void setDemage(int demage) {
        this.demage = demage;
    }

    public long getScore() {
        return score;
    }

    public void setScore(long score) {
        this.score = score;
    }
}
