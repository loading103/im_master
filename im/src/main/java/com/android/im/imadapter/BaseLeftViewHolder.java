package com.android.im.imadapter;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.android.im.R;
import com.android.im.imview.IMRoundAngleImageView;
/**
 * 左边（基类公用用户信息部分）
 */
public class  BaseLeftViewHolder extends RecyclerView.ViewHolder {
    //用户信息
    public IMRoundAngleImageView mIvHeadView;
    public TextView mTvChatTime;
    public TextView mTvName;
    public ImageView mIvLevel;
    public TextView mTvTitle;
    public RelativeLayout mllTitle;
    public LinearLayout  mllUser;
    public RelativeLayout mllitem;
    public TextView tvTime;
    public TextView tvTime2;
    public BaseLeftViewHolder(View view) {
        super(view);
        mIvHeadView =  view.findViewById(R.id.im_iv_head);
        mTvChatTime =  view.findViewById(R.id.im_chat_time);

        mllUser  = view.findViewById(R.id.im_ll_user);
        mTvName =  view.findViewById(R.id.im_tv_name);
        mIvLevel =  view.findViewById(R.id.im_iv_level);
        mllTitle =  view.findViewById(R.id.im_ll_title);
        mTvTitle =  view.findViewById(R.id.im_tv_title);
        mllitem=  view.findViewById(R.id.ll_item);
        tvTime=  view.findViewById(R.id.tv_time);
        tvTime2=  view.findViewById(R.id.tv_time2);
    }
}
