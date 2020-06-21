package com.android.im.imview.imredpickedview;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.im.R;
import com.android.im.imbean.IMBetGetBean;
import com.android.im.imbean.IMGetRedPickBean;
import com.android.im.imbean.IMRedPickInformationBean;
import com.android.im.imnet.IMHttpResultObserver;
import com.android.im.imnet.http.IMHttpsService;
import com.android.im.imui.activity.IMRedPickDetailActivity;
import com.android.im.imui.activity.IMRedPickRecordActivity;
import com.android.im.imutils.IMImageLoadUtil;
import com.android.nettylibrary.http.IMMessageDataJson;
import com.android.nettylibrary.utils.IMLogUtil;
import com.google.gson.Gson;

import okhttp3.MediaType;
import okhttp3.RequestBody;


public class RedPacketViewHolder implements View.OnClickListener {
    ImageView mIvClose;
    ImageView mIvAvatar;
    TextView mTvName;
    TextView mTvMsg;
    TextView mTvNo;
    TextView mTvRecord;
    ImageView mIvOpen;
    private Context mContext;

    private int type;
    private OnRedPacketDialogClickListener mListener;

    private int[] mImgResIds = new int[]{
            R.mipmap.icon_open_red_packet1,
            R.mipmap.icon_open_red_packet2,
            R.mipmap.icon_open_red_packet3,
            R.mipmap.icon_open_red_packet4,
            R.mipmap.icon_open_red_packet5,
            R.mipmap.icon_open_red_packet6,
            R.mipmap.icon_open_red_packet7,
            R.mipmap.icon_open_red_packet8,
            R.mipmap.icon_open_red_packet9,
            R.mipmap.icon_open_red_packet10,
            R.mipmap.icon_open_red_packet11,
            R.mipmap.icon_open_red_packet1,
            R.mipmap.icon_open_red_packet2,
            R.mipmap.icon_open_red_packet3,
            R.mipmap.icon_open_red_packet4,
            R.mipmap.icon_open_red_packet5,
            R.mipmap.icon_open_red_packet6,
            R.mipmap.icon_open_red_packet7,
            R.mipmap.icon_open_red_packet8,
            R.mipmap.icon_open_red_packet9,
            R.mipmap.icon_open_red_packet10,
            R.mipmap.icon_open_red_packet11,
            R.mipmap.icon_open_red_packet1,
            R.mipmap.icon_open_red_packet1,
    };
    private FrameAnimation mFrameAnimation;

    private IMRedPickInformationBean bean;
    private IMMessageDataJson dataJson;
    public RedPacketViewHolder(Context context, View view,final IMMessageDataJson dataJson) {
        mContext = context;
        mIvClose=view.findViewById(R.id.iv_close);
        mIvAvatar=view.findViewById(R.id.iv_avatar);
        mTvName=view.findViewById(R.id.tv_name);
        mTvMsg=view.findViewById(R.id.tv_msg);
        mIvOpen=view.findViewById(R.id.iv_open);
        mIvClose.setOnClickListener(this);
        mIvOpen.setOnClickListener(this);
        mTvNo=view.findViewById(R.id.tv_no);
        mTvRecord=view.findViewById(R.id.im_tv_record);
        mTvRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent(mContext, IMRedPickDetailActivity.class);
                intent.putExtra("dataJson",dataJson.getRedPacketId());
                mContext.startActivity(intent);
                if (mListener != null) {
                    mListener.onCloseClick();
                }
            }
        });
    }

    /**
     * 先判断是不是过期，未过期半段是不是抢完了
     */
    private void initData() {
        long createTime = bean.getCreateTime();
        long l = createTime + 24 * 3600 * 1000;
        if(bean.getState().equals("3")){
            mTvMsg.setVisibility(View.GONE);
            mIvOpen.setVisibility(View.GONE);
            mTvNo.setVisibility(View.VISIBLE);
            mTvRecord.setVisibility(View.GONE);
            mTvNo.setText("该红包已经超过24小时了，无法领取");
        }else if(bean.getState().equals("2")){
            mTvMsg.setVisibility(View.GONE);
            mIvOpen.setVisibility(View.GONE);
            mTvNo.setVisibility(View.VISIBLE);
            mTvRecord.setVisibility(View.VISIBLE);
            mTvNo.setText("该红包已经超过24小时了，无法领取");
        } else {
            if(bean.getState().equals("1") ){//红包抢完了
                mTvMsg.setVisibility(View.GONE);
                mIvOpen.setVisibility(View.GONE);
                mTvNo.setVisibility(View.VISIBLE);
                mTvRecord.setVisibility(View.VISIBLE);
                mTvNo.setText("手慢了，红包派完了");
            }else {
                if(bean.getRecordList()!=null && bean.getRecordList().size()==bean.getNumber()){
                    mTvMsg.setVisibility(View.GONE);
                    mIvOpen.setVisibility(View.GONE);
                    mTvNo.setVisibility(View.VISIBLE);
                    mTvRecord.setVisibility(View.VISIBLE);
                    mTvNo.setText("手慢了，红包派完了");
                }else {
                    mTvMsg.setVisibility(View.VISIBLE);
                    mTvNo.setVisibility(View.GONE);
                    mIvOpen.setVisibility(View.VISIBLE);
                    mTvRecord.setVisibility(View.GONE);
                }

            }
        }
    }

    public void onClick(View view) {
        if(view.getId()==R.id.iv_close){
            stopAnim();
            if (mListener != null) {
                mListener.onCloseClick();
            }
        }else  if(view.getId()==R.id.iv_open){
            if (mFrameAnimation != null) {
                //如果正在转动，则直接返回
                return;
            }
            startAnim();
        }
    }

    public void setData(RedPacketEntity entity, IMRedPickInformationBean bean, IMMessageDataJson dataJson) {
        this.bean=bean;
        this.dataJson=dataJson;
        IMImageLoadUtil.ImageLoadCircle(mContext,entity.avatar,mIvAvatar);
        mTvName.setText(entity.name);
        mTvMsg.setText(entity.remark);
        //初始化数据
        initData();
    }

    public void startAnim() {
        mFrameAnimation = new FrameAnimation(mIvOpen, mImgResIds, 65, false);
        mFrameAnimation.setAnimationListener(new FrameAnimation.AnimationListener() {
            @Override
            public void onAnimationStart() {
                Log.i("", "start");
            }

            @Override
            public void onAnimationEnd() {
                if (mListener != null) {
                    mListener.onOpenClick();
                    stopAnim();
                }
            }

            @Override
            public void onAnimationRepeat() {
                Log.i("", "repeat");
            }

            @Override
            public void onAnimationPause() {
                mIvOpen.setBackgroundResource(R.mipmap.icon_open_red_packet1);
            }
        });
    }

    public void stopAnim() {
        if (mFrameAnimation != null) {
            mFrameAnimation.release();
            mFrameAnimation = null;
        }
    }

    public void setOnRedPacketDialogClickListener(OnRedPacketDialogClickListener listener) {
        mListener = listener;
    }
}
