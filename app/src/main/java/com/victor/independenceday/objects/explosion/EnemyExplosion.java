package com.victor.independenceday.objects.explosion;

import android.graphics.Bitmap;
import android.graphics.PointF;
import android.graphics.Rect;

import com.victor.independenceday.objects.AbstractObject;
import com.victor.independenceday.physics.Trajectory;

/**
 * Created by Віктор on 29.09.2015.
 */
public class EnemyExplosion extends AbstractObject {

    private int bitmapHeight;
    private int bitmapWidth;
    private Rect src;

    private static final int SPRITE_START = 1;
    private static final int SPRITE_AMOUNT_HOR = 5;
    private static final int SPRITE_AMOUNT_VERT = 5;
    public static final int SPRITE_AMOUNT = 25;

    private int spriteNumber;
    private int temp1;
    private int temp2;

    public EnemyExplosion(Trajectory trajectory, Bitmap bitmap, PointF basePoint, float size, int speed) {
        super(trajectory, bitmap, basePoint, size, size);
        bitmapHeight = bitmap.getHeight() / SPRITE_AMOUNT_VERT;
        bitmapWidth = bitmap.getWidth() / SPRITE_AMOUNT_HOR;
        setSpeed(speed);

        changeSprite(SPRITE_START);
    }

    @Override
    public void changeSprite(int order) {
        order = order < SPRITE_AMOUNT ? order : SPRITE_AMOUNT;
        temp1 = order / SPRITE_AMOUNT_HOR;
        temp2 = order % SPRITE_AMOUNT_HOR;

        src = new Rect();
        if(temp1 == 0){
            src.left = bitmapWidth * (order - 1);
        } else {
            src.left = bitmapWidth * (temp2 - 1);
        }
        src.top = bitmapHeight * temp1;
        src.right = src.left + bitmapWidth;
        src.bottom = src.top + bitmapHeight;
        setSrc(src);
        spriteNumber = order;
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
