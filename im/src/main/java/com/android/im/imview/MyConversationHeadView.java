package com.android.im.imview;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;

import com.android.im.R;
import com.android.im.imbean.IMChoosePicBean;
import com.android.nettylibrary.IMSConfig;
import com.android.nettylibrary.utils.IMDensityUtil;
import com.android.nettylibrary.utils.IMLogUtil;
import com.android.nettylibrary.utils.IMPreferenceUtil;
import com.google.gson.Gson;
import com.scwang.smartrefresh.layout.api.RefreshHeader;
import com.scwang.smartrefresh.layout.api.RefreshKernel;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.constant.RefreshState;
import com.scwang.smartrefresh.layout.constant.SpinnerStyle;

public class MyConversationHeadView extends LinearLayout implements RefreshHeader {

    private ImageView mheadView;//头部

    private int mZoomViewWidth=0;

    private int mZoomViewHeight=0;
    public MyConversationHeadView(Context context) {
        super(context);
        initView(context);
    }
    public MyConversationHeadView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.initView(context);
    }
    public MyConversationHeadView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.initView(context);
    }
    private void initView(Context context) {
        setGravity(Gravity.CENTER);
        View view = LayoutInflater.from(context).inflate(R.layout.layout_im_header, this);
        mheadView=view.findViewById(R.id.im_head_view);
    }

    @NonNull
    public View getView() {
        return this;//真实的视图就是自己，不能返回null
    }
    @Override
    public SpinnerStyle getSpinnerStyle() {
        return SpinnerStyle.Translate;//指定为平移，不能null
    }
    @Override
    public void onStartAnimator(RefreshLayout layout, int headHeight, int maxDragHeight) {
    }
    @Override
    public int onFinish(RefreshLayout layout, boolean success) {
        clearFocus();
        return 100;//延迟500毫秒之后再弹回

    }
    @Override
    public void onStateChanged(RefreshLayout refreshLayout, RefreshState oldState, RefreshState newState) {
        switch (newState) {
            case None:
            case PullDownToRefresh:
                if (mZoomViewWidth <= 0 || mZoomViewHeight <= 0) {
                    mZoomViewWidth = mheadView.getMeasuredWidth();
                    mZoomViewHeight = mheadView.getMeasuredHeight();
                }
                break;
            case Refreshing:
                break;
            case ReleaseToRefresh:
                break;
        }
    }
    @Override
    public boolean isSupportHorizontalDrag() {
        return false;
    }
    @Override
    public void onInitialized(RefreshKernel kernel, int height, int maxDragHeight) {
    }

    @Override
    public void onMoving(boolean isDragging, float percent, int offset, int height, int maxDragHeight) {
        IMLogUtil.d("MyOwnTag:", "onMoving ---offset-" +offset+"--height--"+height+"--maxDragHeight--"+maxDragHeight);
        setZoom(offset);

    }

    @Override
    public void onReleased(@NonNull RefreshLayout refreshLayout, int height, int maxDragHeight) {
    }

    @Override
    public void onHorizontalDrag(float percentX, int offsetX, int offsetMax) {
    }
    @Override
    public void setPrimaryColors(@ColorInt int ... colors){
    }


    public void setZoom(float zoom) {
        if (mZoomViewWidth <= 0 || mZoomViewHeight <= 0) {
            return;
        }
        ViewGroup.LayoutParams lp = mheadView.getLayoutParams();
        lp.width =  mZoomViewWidth ;
        lp.height = (int) (mZoomViewHeight * ((mZoomViewWidth + zoom) / mZoomViewWidth));
//        ((MarginLayoutParams) lp).setMargins(-(lp.width - mZoomViewWidth) / 2, 0, 0, 0);
        mheadView.setLayoutParams(lp);
    }

}