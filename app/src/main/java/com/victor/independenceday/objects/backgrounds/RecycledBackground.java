package com.victor.independenceday.objects.backgrounds;

import android.graphics.Bitmap;
import android.graphics.PointF;
import android.graphics.Rect;

import com.victor.independenceday.objects.AbstractObject;
import com.victor.independenceday.objects.ObjectConstants;
import com.victor.independenceday.physics.Trajectory;

/**
 * Created by Віктор on 27.09.2015.
 */
public class RecycledBackground extends AbstractObject {

    public RecycledBackground(Trajectory trajectory, Bitmap bitmap, PointF basePoint, float width, float height) {
        super(trajectory, bitmap, basePoint, width, height);
        setSpeed(ObjectConstants.BACKGROUND_SPEED);
        double index = width / height;
        Rect src = new Rect();
        src.left = 0;
        src.top = 0;
        src.bottom = src.top + bitmap.getHeight();
        if ((bitmap.getWidth() / bitmap.getHeight()) <= index) {
            src.right = bitmap.getWidth();
        } else {
            src.right = (int) (bitmap.getWidth() * index);
        }
        setSrc(src);
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
