package com.android.im.imwedgit.dragDelete;

import android.content.Context;
import android.util.AttributeSet;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

public class MoveLayoutManager extends GridLayoutManager {
    private boolean isScrollEnabled = true;

    public MoveLayoutManager(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public MoveLayoutManager(Context context, int spanCount) {
        super(context, spanCount);
    }

    public MoveLayoutManager(Context context, int spanCount, int orientation, boolean reverseLayout) {
        super(context, 5, orientation, reverseLayout);
    }


    @Override
    public boolean canScrollVertically() {
        return isScrollEnabled && super.canScrollVertically();
    }

    public void setScrollEnabled(boolean scrollEnabled) {
        isScrollEnabled = scrollEnabled;
    }
}
