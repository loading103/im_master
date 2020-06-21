package com.rhby.cailexun.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.im.IMSManager;
import com.android.im.imutils.IMImageLoadUtil;
import com.android.im.imutils.IMStatusBarUtil;
import com.rhby.cailexun.R;
import com.rhby.cailexun.event.SetBgSuccessedEvent;
import com.android.im.imeventbus.SetMineBgSuccessedEvent;
import com.rhby.cailexun.ui.base.BaseActivity;
import com.android.nettylibrary.IMSConfig;
import com.android.nettylibrary.utils.IMPreferenceUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Wolf on 2019/11/22.
 * Describe:通用设置
 */
public class GeneralSettingsActicity extends BaseActivity {
    @BindView(R.id.tv_top_title)
    TextView tvTopTitle;
    @BindView(R.id.iv_bg_1)
    ImageView mIvBg1;
    @BindView(R.id.iv_bg_2)
    ImageView mIvBg2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_general_settings);
        EventBus.getDefault().register(this);
        IMStatusBarUtil.setTranslucent(this,0);
        IMStatusBarUtil.setLightMode(this);
        initView();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
    private void initView() {
        tvTopTitle.setText("通用设置");
        String json = IMPreferenceUtil.getPreference_String(IMSConfig.CHOOSE_CHAT_BG, "");
        String json2= IMSManager.getMyBgUrl();
        if(TextUtils.isEmpty(json)){
            mIvBg1.setVisibility(View.VISIBLE);
        }else {
            mIvBg1.setVisibility(View.VISIBLE);
            IMImageLoadUtil.CommonImageBgLoadCp(this,json,mIvBg1);
        }
        if(TextUtils.isEmpty(json2)){
            mIvBg2.setVisibility(View.VISIBLE);
        }else {
            mIvBg2.setVisibility(View.VISIBLE);
            IMImageLoadUtil.CommonImageBgLoadCp(this,json2,mIvBg2);
        }
    }

    @OnClick({R.id.iv_top_finish, R.id.rl_chat, R.id.rl_my})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_top_finish:
                finish();
                break;
            case R.id.rl_chat:
                Intent intent=new Intent(GeneralSettingsActicity.this, ChooseChatBgActicity.class);
                intent.putExtra("isminebg",false);
                startActivity(intent);
                break;
            case R.id.rl_my:
                Intent intent1=new Intent(GeneralSettingsActicity.this, ChooseChatBgActicity.class);
                intent1.putExtra("isminebg",true);
                startActivity(intent1);
                break;
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(SetBgSuccessedEvent event) {
        mIvBg1.setVisibility(View.VISIBLE);
        String json = IMPreferenceUtil.getPreference_String(IMSConfig.CHOOSE_CHAT_BG, "");
        IMImageLoadUtil.CommonImageBgLoadCp(this,json,mIvBg1);
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(SetMineBgSuccessedEvent event) {
        mIvBg2.setVisibility(View.VISIBLE);
        String json = IMPreferenceUtil.getPreference_String(IMSConfig.CHOOSE_MY_BG, "");
        IMImageLoadUtil.CommonImageBgLoadCp(this,json,mIvBg2);
    }
}
