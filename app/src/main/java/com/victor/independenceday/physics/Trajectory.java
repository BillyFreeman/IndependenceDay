package com.victor.independenceday.physics;

import android.graphics.PointF;

/**
 * Created by Віктор on 27.09.2015.
 */
public interface Trajectory {

    PointF getNextPoint(PointF basePoint, int speed);
    Trajectory setWidth(int width);
    Trajectory setHeight(int height);
}
