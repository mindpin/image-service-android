package com.mindpin.image_service_android.utils;

import android.graphics.Point;
import com.nineoldandroids.animation.TypeEvaluator;

/**
 * Created by DD on 14-10-17.
 */
public class PointEvaluator implements TypeEvaluator {
    public Object evaluate(float fraction, Object startValue, Object endValue) {
        Point startPoint = (Point) startValue;
        Point endPoint = (Point) endValue;
        return new Point((int)(startPoint.x + fraction * (endPoint.x - startPoint.x)),
                (int)(startPoint.y + fraction * (endPoint.y - startPoint.y)));
    }
}