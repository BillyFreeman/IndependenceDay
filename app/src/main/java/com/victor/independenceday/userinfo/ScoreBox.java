package com.victor.independenceday.userinfo;

import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Typeface;

/**
 * Created by Віктор on 30.09.2015.
 */
public class ScoreBox {

    private final String title = "score: ";
    private String value = "0";
    private String result;
    private int start = 0;
    private int end;
    private float x;
    private float y;
    private Paint paint;

    private RectF container;

    public ScoreBox(RectF container) {
        this.container = container;
        this.x = container.left + container.width() * .38f;
        this.y = container.bottom - container.height() * .25f;

        paint = new Paint();
        paint.setTypeface(Typeface.SANS_SERIF);
        paint.setTextSize(container.height() * .35f);
        paint.setColor(Color.rgb(169, 214, 215));
        paint.setAntiAlias(true);

        this.result = title + value;
    }

    public void setString(long score) {
        value = Long.toString(score);
        result = title + value;
        end = result.length();
        if (score >= 10000) {
            this.x = container.left + container.width() * .35f;
        } else if (score >= 1000) {
            this.x = container.left + container.width() * .368f;
        }
    }

    public long getString() {
        return Long.parseLong(value);
    }

    public String getResult() {
        return result;
    }

    public int getStart() {
        return start;
    }

    public int getEnd() {
        return end;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public Paint getPaint() {
        return paint;
    }
}
