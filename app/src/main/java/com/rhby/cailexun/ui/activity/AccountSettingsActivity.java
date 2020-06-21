package com.rhby.cailexun.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.im.IMSManager;
import com.android.im.imbean.IMBetGetBean;
import com.android.im.imnet.IMHttpResultObserver;
import com.android.im.imnet.http.IMHttpsService;
import com.android.im.imutils.IMStatusBarUtil;
import com.rhby.cailexun.R;
import com.rhby.cailexun.event.MessageEvent;
import com.rhby.cailexun.ui.base.BaseActivity;
import com.rhby.cailexun.widget.SettingsOptionView;
import com.android.nettylibrary.IMSConfig;
import com.android.nettylibrary.utils.IMPreferenceUtil;
import com.google.gson.Gson;
import com.suke.widget.SwitchButton;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.OnClick;
import cn.jpush.android.api.JPushInterface;
import okhttp3.MediaType;
import okhttp3.RequestBody;

/**
 * Created by Wolf on 2019/11/21.
 * Describe:账号设置
 */
public class AccountSettingsActivity extends BaseActivity implements SwitchButton.OnCheckedChangeListener {
    @BindView(R.id.tv_top_title)
    TextView tvTopTitle;
    @BindView(R.id.re_top_finish)
    RelativeLayout reTopFinish;
    @BindView(R.id.tv_top_right)
    TextView tvTopRight;
    @BindView(R.id.iv_top_right)
    ImageView ivTopRight;
    @BindView(R.id.rl_top)
    RelativeLayout rlTop;
    @BindView(R.id.sov1)
    SettingsOptionView sov1;
    @BindView(R.id.sov2)
    SettingsOptionView sov2;
    @BindView(R.id.sov3)
    SettingsOptionView sov3;
    @BindView(R.id.sov4)
    SettingsOptionView sov4;
    @BindView(R.id.sov5)
    SettingsOptionView sov5;
    @BindView(R.id.sov6)
    SettingsOptionView sov6;
    @BindView(R.id.sov7)
    SettingsOptionView sov7;
    @BindView(R.id.sov8)
    SettingsOptionView sov8;
    @BindView(R.id.btn_exit)
    TextView btnExit;
    private boolean needFriend;
    private boolean isAuthen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_settings);
        IMStatusBarUtil.setTranslucent(this,0);
        IMStatusBarUtil.setLightMode(this);
        EventBus.getDefault().register(this);
        initView();
    }

    private void initView() {
        tvTopTitle.setText("账号设置");
        boolean isNewMsg = IMPreferenceUtil.getPreference_Boolean(IMSConfig.IM_SETTING_APP_NOTICE, true);
        boolean isVoice = IMPreferenceUtil.getPreference_Boolean(IMSConfig.IM_SETTING_VOICE, true);
        boolean isShake = IMPreferenceUtil.getPreference_Boolean(IMSConfig.IM_SETTING_SHOKE, false);
        String mes = IMPreferenceUtil.getPreference_String(IMSConfig.SAVE_FRIENDLYVALU, "N");
        if(mes.equals("Y")){
            isAuthen=true;
        }else {
            isAuthen=false;
        }
        sov1.setCheck(isNewMsg);
        sov2.setCheck(isVoice);
        sov3.setCheck(isShake);
        sov4.setCheck(isAuthen);
        SwitchButton sb1 = sov1.getSwitchButton();
        sb1.setTag("1");
        sb1.setOnCheckedChangeListener(this);
        SwitchButton sb2 = sov2.getSwitchButton();
        sb2.setTag("2");
        sb2.setOnCheckedChangeListener(this);
        SwitchButton sb3 = sov3.getSwitchButton();
        sb3.setTag("3");
        sb3.setOnCheckedChangeListener(this);
        SwitchButton sb4 = sov4.getSwitchButton();
        sb4.setTag("4");
        sb4.setOnCheckedChangeListener(this);

        updatePhone();
    }

    /**
     * 更新手机号
     */
    private void updatePhone() {
        String phone = IMPreferenceUtil.getPreference_String(IMSConfig.PHONE, "获取手机号失败");
        if(phone.length()==11){
            String s=phone.substring(0,3)+" "+phone.substring(3,7)+" "+phone.substring(7,11);
            sov7.setRightText(s);
        }else {
            sov7.setRightText(phone);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(MessageEvent event) {
        if (event.getMsg().equals("exit_login")) {
            finish();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        updatePhone();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @OnClick({R.id.iv_top_finish, R.id.sov5,R.id.sov6, R.id.btn_exit,R.id.sov7,R.id.sov8})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_top_finish:
                finish();
                break;
            case R.id.sov5://修改密码
                Bundle bundle=new Bundle();
                bundle.putString("type","修改密码");
                startActicity(PhoneVerificationActivity.class,bundle);
                break;
            case R.id.sov6://关于我们
                startActicity(AboutUsActicity.class);
                break;
            case R.id.btn_exit://退出登录
                showCommonDialog("是否确认退出登录?", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dissCommonDialog();
                        IMSManager.getInstance().ImChatLoginOut();
                        JPushInterface.deleteAlias(AccountSettingsActivity.this,1);
                        startActivity(new Intent(AccountSettingsActivity.this, LoginNewActivity.class));
                        EventBus.getDefault().post(new MessageEvent("exit_login"));
                        finish();
                    }
                });


                break;
            case R.id.sov7://更换手机号
                startActicity(ChangePhoneActivity.class);
                break;
            case R.id.sov8://清除缓存
                startActivity(new Intent(AccountSettingsActivity.this, AppDataSaveActivity.class));
                break;
        }
    }

    @Override
    public void onCheckedChanged(SwitchButton view, boolean isChecked) {
        String tag = (String) view.getTag();
        switch (tag){
            case "1":
                sov1.getSwitchButton().setChecked(isChecked);
                IMPreferenceUtil.setPreference_Boolean(IMSConfig.IM_SETTING_APP_NOTICE,isChecked);
                break;
            case "2":
                sov2.getSwitchButton().setChecked(isChecked);
                IMPreferenceUtil.setPreference_Boolean(IMSConfig.IM_SETTING_VOICE,isChecked);
                break;
            case "3":
                sov3.getSwitchButton().setChecked(isChecked);
                IMPreferenceUtil.setPreference_Boolean(IMSConfig.IM_SETTING_SHOKE,isChecked);
                break;
            case "4":
                sov4.getSwitchButton().setChecked(isChecked);
                if(isChecked){
                    UpdataFriendVerity("Y",isChecked);
                }else {
                    UpdataFriendVerity("N",isChecked);
                }

                break;
        }
    }

    public void  UpdataFriendVerity(String content,boolean isChecked){
        showLoadingDialog();
        final IMBetGetBean bean = new IMBetGetBean();
        bean.setIsFriendValid(content);
        String json = new Gson().toJson(bean);
        Log.e("-------修改个人信息",json);
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json);
        IMHttpsService.getUpdataInfor(body, new IMHttpResultObserver<String>() {
            @Override
            public void onSuccess(String ss, String message) {
                dismissLoadingDialog();
                IMPreferenceUtil.setPreference_String(IMSConfig.SAVE_FRIENDLYVALU,content);
                Toast.makeText(AccountSettingsActivity.this, "修改成功", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void _onError(Throwable e) {
                dismissLoadingDialog();
                Toast.makeText(AccountSettingsActivity.this, "修改失败，请稍后再试", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void _onErrorLocal(Throwable e, String message, String code) {
                dismissLoadingDialog();
                Toast.makeText(AccountSettingsActivity.this, "修改失败，请稍后再试", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
