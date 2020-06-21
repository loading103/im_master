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
 * 右边边（基类）
 */
public class BaseRightViewHolder extends RecyclerView.ViewHolder {
    //用户信息
    public IMRoundAngleImageView mIvHeadView;
    public TextView mTvChatTime;
    public TextView mTvName;
    public ImageView mIvLevel;
    public TextView mTvTitle;
    public RelativeLayout mllTitle;
    public LinearLayout mllUser;
    //发送状态
    public ImageView mIvFail;
    public TextView mTvReaded;
    public TextView mTvNoReaded;
    public RelativeLayout mllitem;
    public TextView tvTime;
    public TextView tvTime2;
    public BaseRightViewHolder(View view) {
        super(view);
        mIvHeadView =  view.findViewById(R.id.im_iv_head);
        mTvChatTime =  view.findViewById(R.id.im_chat_time);
        mTvName =  view.findViewById(R.id.im_tv_name);
        mIvLevel =  view.findViewById(R.id.im_iv_level);
        mllTitle =  view.findViewById(R.id.im_ll_title);
        mTvTitle =  view.findViewById(R.id.im_tv_title);
        mllUser  = view.findViewById(R.id.im_ll_user);
        //发送状态
        mIvFail =  view.findViewById(R.id.im_iv_fail);
        mTvReaded =  view.findViewById(R.id.im_tv_readed);
        mTvNoReaded =  view.findViewById(R.id.im_tv_no_readed);
        mllitem=  view.findViewById(R.id.ll_item);
        tvTime=  view.findViewById(R.id.tv_time);
        tvTime2=  view.findViewById(R.id.tv_time2);
    }
}
