package com.rubbersoft.android.valveleakage.model;

import android.view.animation.BounceInterpolator;

import static java.lang.Math.abs;
import static java.lang.Math.sin;

/**
 * Created by Muhammad Muzammil on 30-Dec-15.
 */
public class CustomInerpolator extends BounceInterpolator {

    @Override
    public float getInterpolation(float t) {
        return (float) abs(sin(3.142*t))/16;
    }
}
