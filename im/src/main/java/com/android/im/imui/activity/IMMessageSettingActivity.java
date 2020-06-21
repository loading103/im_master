package com.android.im.imui.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationManagerCompat;

import com.android.im.IMSManager;
import com.android.im.R;
import com.android.im.imeventbus.IMMessageDeteAlllEvevt;
import com.android.im.imeventbus.IMMessageDetelGroupEvevt;
import com.android.im.imutils.IMStatusBarUtil;
import com.android.nettylibrary.IMSConfig;
import com.android.nettylibrary.utils.IMPreferenceUtil;

import org.greenrobot.eventbus.EventBus;


public class IMMessageSettingActivity extends IMBaseActivity implements View.OnClickListener {
    private ImageView mIvFinish;
    private TextView mTvTitle;
    private TextView mTvBtn;
    private TextView mTvOpen;

    private ImageView mNotice;
    private ImageView mVoice;
    private ImageView mShake;
    private ImageView mPush;
    private LinearLayout mllopen;
    private Boolean mpushEable=true;
    private Boolean mappNoticeEable=true;
    private Boolean mshakeEable=true;
    private Boolean mvoiceEable=true;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_im_setting_message);
        IMStatusBarUtil.setLightMode(this);           //设置白底黑字
        IMStatusBarUtil.setTranslucentForImageView(this, 0, null);
        initView();
        initData();
    }
    private void initView() {
        mIvFinish = findViewById(R.id.tv_top_finish);
        mTvTitle = findViewById(R.id.tv_top_title);
        mIvFinish.setVisibility(View.VISIBLE);
        mllopen = findViewById(R.id.im_ll_open_notice);
        mNotice = findViewById(R.id.im_iv_app_notice);
        mVoice = findViewById(R.id.im_iv_app_voice);
        mShake = findViewById(R.id.im_iv_app_shake);
        mPush = findViewById(R.id.im_iv_push);
        mTvBtn = findViewById(R.id.im_tv_clear);
        mTvOpen= findViewById(R.id.im_tv_open);

        mIvFinish.setOnClickListener(this);
        mllopen.setOnClickListener(this);
        mNotice.setOnClickListener(this);
        mVoice.setOnClickListener(this);
        mShake.setOnClickListener(this);
        mPush.setOnClickListener(this);
        mTvBtn.setOnClickListener(this);

    }

    /**
     * 初始化各个设置的状态
     */
    private void initData() {
        mappNoticeEable= IMPreferenceUtil.getPreference_Boolean(IMSConfig.IM_SETTING_APP_NOTICE,false);
        mvoiceEable= IMPreferenceUtil.getPreference_Boolean(IMSConfig.IM_SETTING_VOICE,false);
        mshakeEable= IMPreferenceUtil.getPreference_Boolean(IMSConfig.IM_SETTING_SHOKE,false);
        mpushEable= IMPreferenceUtil.getPreference_Boolean(IMSConfig.IM_SETTING_PUSH,false);
        if(mappNoticeEable){
            mNotice.setImageResource(R.mipmap.im_chat_detail_on);
        }else {
            mNotice.setImageResource(R.mipmap.im_chat_detail_off);
        }
        if(mvoiceEable){
            mVoice.setImageResource(R.mipmap.im_chat_detail_on);
        }else {
            mVoice.setImageResource(R.mipmap.im_chat_detail_off);
        }
        if(mshakeEable){
            mShake.setImageResource(R.mipmap.im_chat_detail_on);
        }else {
            mShake.setImageResource(R.mipmap.im_chat_detail_off);
        }
        if(mpushEable){
            mPush.setImageResource(R.mipmap.im_chat_detail_on);
        }else {
            mPush.setImageResource(R.mipmap.im_chat_detail_off);
        }
        if (!isNotificationEnabled(this)) {
            mTvOpen.setText("去开启");
        }else {
            mTvOpen.setText("已开启");
        }
        mTvTitle.setText(getResources().getString(R.string.im_msg_setting));
    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.tv_top_finish){
            finish();
        }else if(v.getId()==R.id.im_ll_open_notice){
            openNotice();
        }else if(v.getId()==R.id.im_iv_app_notice){
            if(mappNoticeEable){
                mNotice.setImageResource(R.mipmap.im_chat_detail_off);
                IMPreferenceUtil.setPreference_Boolean(IMSConfig.IM_SETTING_APP_NOTICE,false);
                mappNoticeEable=!mappNoticeEable;
            }else {
                mNotice.setImageResource(R.mipmap.im_chat_detail_on);
                IMPreferenceUtil.setPreference_Boolean(IMSConfig.IM_SETTING_APP_NOTICE,true);
                mappNoticeEable=!mappNoticeEable;
            }

        }else if(v.getId()==R.id.im_iv_app_voice){
            if(mvoiceEable){
                mVoice.setImageResource(R.mipmap.im_chat_detail_off);
                IMPreferenceUtil.setPreference_Boolean(IMSConfig.IM_SETTING_VOICE,false);
                mvoiceEable=!mvoiceEable;
            }else {
                mVoice.setImageResource(R.mipmap.im_chat_detail_on);
                IMPreferenceUtil.setPreference_Boolean(IMSConfig.IM_SETTING_VOICE,true);
                mvoiceEable=!mvoiceEable;
            }
        }else if(v.getId()==R.id.im_iv_app_shake){

            if(mshakeEable){
                mshakeEable=!mshakeEable;
                mShake.setImageResource(R.mipmap.im_chat_detail_off);
                IMPreferenceUtil.setPreference_Boolean(IMSConfig.IM_SETTING_SHOKE,false);
            }else {
                mshakeEable=!mshakeEable;
                mShake.setImageResource(R.mipmap.im_chat_detail_on);
                IMPreferenceUtil.setPreference_Boolean(IMSConfig.IM_SETTING_SHOKE,true);
            }
        }else if(v.getId()==R.id.im_iv_push){
            if(mpushEable){
                mpushEable=!mpushEable;
                mPush.setImageResource(R.mipmap.im_chat_detail_off);
                IMPreferenceUtil.setPreference_Boolean(IMSConfig.IM_SETTING_PUSH,false);
            }else {
                mpushEable=!mpushEable;
                mPush.setImageResource(R.mipmap.im_chat_detail_on);
                IMPreferenceUtil.setPreference_Boolean(IMSConfig.IM_SETTING_PUSH,true);
            }
        }else if(v.getId()==R.id.im_tv_clear){
            showCommonDialog("是否确认清空全部聊天记录？", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dissCommonDialog();
                    cleaSucess();
                }
            });
        }
    }

    private void cleaSucess() {
        EventBus.getDefault().post(new IMMessageDeteAlllEvevt());
        EventBus.getDefault().post(new IMMessageDetelGroupEvevt());
        Toast.makeText(IMMessageSettingActivity.this, "清除成功", Toast.LENGTH_SHORT).show();
        IMSManager.getInstance().getUnreadMessageNumber();
    }

    /**
     * 检查是否开启通知
     */
    private boolean isNotificationEnabled(Context context) {
        boolean isOpened = false;
        try {
            isOpened = NotificationManagerCompat.from(context).areNotificationsEnabled();
        } catch (Exception e) {
            e.printStackTrace();
            isOpened = false;
        }
        return isOpened;

    }
    /**
     * 打开通知设置
     */
    private void openNotice(){
        Intent intent = new Intent();
        if (Build.VERSION.SDK_INT >= 26) {
            // android 8.0引导
            intent.setAction("android.settings.APP_NOTIFICATION_SETTINGS");
            intent.putExtra("android.provider.extra.APP_PACKAGE", getPackageName());
        } else if (Build.VERSION.SDK_INT >= 21) {
            // android 5.0-7.0
            intent.setAction("android.settings.APP_NOTIFICATION_SETTINGS");
            intent.putExtra("app_package", getPackageName());
            intent.putExtra("app_uid", getApplicationInfo().uid);
        } else {
            // 其他
            intent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
            intent.setData(Uri.fromParts("package", getPackageName(), null));
        }
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivityForResult(intent,1001);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==1001){
            if (!isNotificationEnabled(this)) {
                mTvOpen.setText("去开启");
            }else {
                mTvOpen.setText("已开启");
            }
        }
    }

}
