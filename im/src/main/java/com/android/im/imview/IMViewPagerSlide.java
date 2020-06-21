package com.android.im.imview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

import androidx.viewpager.widget.ViewPager;


public class IMViewPagerSlide extends ViewPager {

    private boolean scrollable = true;


    public IMViewPagerSlide(Context context) {
        super(context);
    }

    public IMViewPagerSlide(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (!scrollable) {
            return false;
        }
        return super.onTouchEvent(ev);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (!scrollable) {
            return false;
        }
        return super.onInterceptTouchEvent(ev);
    }

    public boolean isCanScrollble() {
        return scrollable;
    }

    public void setCanScrollble(boolean scrollble) {
        this.scrollable = scrollble;
    }
}
