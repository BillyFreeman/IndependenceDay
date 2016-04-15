package com.victor.independenceday.userinfo;

import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;

/**
 * Created by Віктор on 30.09.2015.
 */
public class RaptorHealth {

    private int startHealth;
    private int healthLeft;
    private float leftIndex;

    private RectF body;
    private RectF emptyBody;
    private Paint paint;
    private Paint emptySpace;


    public RaptorHealth(int health, RectF container) {
        this.startHealth = health;

        body = new RectF();
        body.left = container.left + container.width() * .05f;
        body.top = container.top + container.height() * .05f;
        body.right = body.left + container.width() * .9f;
        body.bottom = body.top + container.height() * .5f;

        emptyBody = new RectF(body.left, body.top, body.right, body.bottom);

        paint = new Paint();
        paint.setColor(Color.rgb(237, 67, 67));
        paint.setAntiAlias(true);

        emptySpace = new Paint();
        emptySpace.setColor(Color.rgb(0, 0, 0));
        emptySpace.setAntiAlias(true);
    }

    public RectF getBody() {
        return body;
    }

    public RectF getEmptyBody() {
        return emptyBody;
    }

    public Paint getPaint() {
        return paint;
    }

    public Paint getEmptySpace() {
        return emptySpace;
    }

    public void recalculate(int healthLeft) {
        this.healthLeft = healthLeft < 0 ? 0 : healthLeft;
        leftIndex = ((float) this.healthLeft) / ((float) this.startHealth);
        body.right = body.left + (body.width() * leftIndex);
    }
}
