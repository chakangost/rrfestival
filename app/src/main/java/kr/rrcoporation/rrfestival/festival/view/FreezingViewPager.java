package kr.rrcoporation.rrfestival.festival.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

public class FreezingViewPager extends ViewPager {

    private boolean isEnabled;

    public FreezingViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        isEnabled = true;
    }

    public FreezingViewPager(Context context) {
        super(context);
        isEnabled = true;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (isEnabled) {
            return super.onInterceptTouchEvent(ev);
        } else {
            return false;
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (isEnabled) {
            return super.onTouchEvent(ev);
        } else {
            return false;
        }
    }

    public void setPagingEnabled(boolean enabled) {
        this.isEnabled = enabled;
    }
}
