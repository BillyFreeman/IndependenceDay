package com.victor.independenceday.objects.ammo;

import android.graphics.Bitmap;
import android.graphics.PointF;
import android.graphics.Rect;

import com.victor.independenceday.objects.ObjectConstants;
import com.victor.independenceday.physics.Trajectory;

/**
 * Created by Віктор on 29.09.2015.
 */
public class Rocket extends Ammo {

    public static final float ROCKET_SIZE = .03f;
    public static final float ROCKET_DISTANCE = .45f;
    public static final float NEXT_SHOT_DISTANCE = .08f;

    private int bitmapHeight;
    private int bitmapWidth;
    private Rect src;

    public Rocket(Trajectory trajectory, Bitmap bitmap, PointF basePoint, float size) {
        super(trajectory, bitmap, basePoint, size, size);
        super.setSpeed(ObjectConstants.ROCKET_SPEED);
        bitmapHeight = bitmap.getHeight();
        bitmapWidth = bitmap.getWidth();

        src = new Rect();
        src.left = 0;
        src.top = 0;
        src.right = bitmapWidth;
        src.bottom = bitmapHeight;
        setSrc(src);

        setWidth(size * (((float) bitmapWidth) / ((float) bitmapHeight)) * 1f);
        refreshDst();

        setLunchPointY(basePoint.y);
        setDistance(ROCKET_DISTANCE);
        setNextShotDistance(NEXT_SHOT_DISTANCE);
    }

    @Override
    public void changeSprite(int order) {

    }

    @Override
    public int getSpriteNumber() {
        return 0;
    }

    @Override
    public float getXPrev() {
        return 0;
    }

    @Override
    public void setXPrev(float x) {

    }
}
