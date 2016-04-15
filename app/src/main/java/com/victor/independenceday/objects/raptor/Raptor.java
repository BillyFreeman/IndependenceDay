package com.victor.independenceday.objects.raptor;

import android.graphics.Bitmap;
import android.graphics.PointF;
import android.graphics.Rect;

import com.victor.independenceday.objects.AbstractObject;
import com.victor.independenceday.objects.ObjectConstants;
import com.victor.independenceday.physics.Trajectory;

/**
 * Created by Віктор on 27.09.2015.
 */
public class Raptor extends AbstractObject {

    private static Raptor raptor;

    private int bitmapHeight;
    private int bitmapWidth;
    private Rect src;

    private static final int SPRITE_START = 4;
    private static final int SPRITE_AMOUNT = 7;

    public static final float RAPTOR_SIZE = .12f;

    private int spriteNumber;

    private Raptor(Trajectory trajectory, Bitmap bitmap, PointF basePoint, float size) {
        super(trajectory, bitmap, basePoint, size, size);
        bitmapHeight = bitmap.getHeight();
        bitmapWidth = bitmap.getWidth() / SPRITE_AMOUNT;
        setHealthPoints(ObjectConstants.RAPTOR_HEALTH);

        src = new Rect();
        src.left = bitmapWidth * (SPRITE_START - 1);
        src.top = 0;
        src.right = bitmapWidth * SPRITE_START;
        src.bottom = bitmapHeight;
        setSrc(src);

        setWidth(size * (((float) bitmapWidth) / ((float) bitmapHeight)) * 1f);
        refreshDst();

        spriteNumber = SPRITE_START;
        setDemage(ObjectConstants.RAPTOR_DAMAGE);
    }

    @Override
    public void changeSprite(int order) {
        src = getSrc();
        src.left = bitmapWidth * (order - 1);
        src.right = bitmapWidth * order;
        setSrc(src);
        spriteNumber = order;
    }

    public static Raptor getRaptor(Trajectory t, Bitmap b, PointF p, float f) {
        if (raptor == null) {
            raptor = new Raptor(t, b, p, f);
        }
        return raptor;
    }

    @Override
    public int getSpriteNumber() {
        return spriteNumber;
    }

    @Override
    public float getXPrev() {
        return 0;
    }

    @Override
    public void setXPrev(float x) {

    }
}
