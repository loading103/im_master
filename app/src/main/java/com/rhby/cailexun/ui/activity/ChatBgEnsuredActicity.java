package com.rhby.cailexun.ui.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.im.imbean.IMBetGetBean;
import com.android.im.imnet.IMHttpResultObserver;
import com.android.im.imnet.http.IMHttpsService;
import com.android.im.imutils.IMImageLoadUtil;
import com.android.im.imutils.IMStatusBarUtil;
import com.rhby.cailexun.R;
import com.rhby.cailexun.event.SetBgSuccessedEvent;
import com.android.im.imeventbus.SetMineBgSuccessedEvent;
import com.rhby.cailexun.ui.base.BaseActivity;
import com.android.nettylibrary.IMSConfig;
import com.android.nettylibrary.utils.IMPreferenceUtil;
import com.google.gson.Gson;

import org.greenrobot.eventbus.EventBus;


import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.MediaType;
import okhttp3.RequestBody;

public class ChatBgEnsuredActicity extends BaseActivity  {
    @BindView(R.id.iv_bg)
    ImageView mIvBg;
    @BindView(R.id.tv_cancle)
    TextView mTvCancle;
    @BindView(R.id.tv_ensure)
    TextView mTvEnsure;
    private String beanUrl;
    private boolean isminebg;
    private int position;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ensure_bg);
        IMStatusBarUtil.setTranslucentForImageView(this,0,null);
        initView();
    }

    private void initView() {
        isminebg = getIntent().getBooleanExtra("isminebg", false);
        beanUrl=(String)getIntent().getSerializableExtra("bean");
        position=getIntent().getIntExtra("position",-1);
        IMImageLoadUtil.CommonImageLoadCp(this,beanUrl,mIvBg);
    }

    @OnClick({R.id.tv_cancle,R.id.tv_ensure})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_cancle:
                finish();
                break;
            case R.id.tv_ensure:
                if(isminebg){
                    IMPreferenceUtil.setPreference_String(IMSConfig.CHOOSE_MY_BG, beanUrl);
                    UpdataFriendVerity(beanUrl);
                    EventBus.getDefault().post(new SetMineBgSuccessedEvent());
                }else {
                    IMPreferenceUtil.setPreference_String(IMSConfig.CHOOSE_CHAT_BG,beanUrl);
                    EventBus.getDefault().post(new SetBgSuccessedEvent());
                    finish();
                }
                break;
        }
    }


    public void  UpdataFriendVerity(String url){
        showLoadingDialog();
        final IMBetGetBean bean = new IMBetGetBean();
        bean.setBgUrl(url);
        String json = new Gson().toJson(bean);
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json);
        IMHttpsService.getUpdataInfor(body, new IMHttpResultObserver<String>() {
            @Override
            public void onSuccess(String ss, String message) {
                dismissLoadingDialog();
                Toast.makeText(ChatBgEnsuredActicity.this, "修改成功", Toast.LENGTH_SHORT).show();
                finish();
            }

            @Override
            public void _onError(Throwable e) {
                dismissLoadingDialog();
                Toast.makeText(ChatBgEnsuredActicity.this, "修改失败，请稍后再试", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void _onErrorLocal(Throwable e, String message, String code) {
                dismissLoadingDialog();
                Toast.makeText(ChatBgEnsuredActicity.this, "修改失败，请稍后再试", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
