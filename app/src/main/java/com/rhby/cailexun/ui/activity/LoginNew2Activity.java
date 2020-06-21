package com.rhby.cailexun.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.im.IMSHttpManager;
import com.android.im.IMSManager;
import com.android.im.iminterface.IMManegeLoginInterface;
import com.android.im.imutils.IMStatusBarUtil;
import com.rhby.cailexun.R;
import com.rhby.cailexun.bean.CommonBean;
import com.rhby.cailexun.bean.LoginBean;
import com.rhby.cailexun.net.HttpResultObserver;
import com.rhby.cailexun.net.http.HttpsService;
import com.rhby.cailexun.ui.base.BaseActivity;
import com.android.nettylibrary.IMSConfig;
import com.android.nettylibrary.utils.IMMd5Util;
import com.android.nettylibrary.utils.IMPreferenceUtil;
import com.blankj.utilcode.util.EncodeUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.google.gson.Gson;

import butterknife.BindView;
import butterknife.OnClick;
import cn.jpush.android.api.JPushInterface;
import okhttp3.MediaType;
import okhttp3.RequestBody;

/**
 * 登录 新 手机号
 */
public class LoginNew2Activity extends BaseActivity {
//    测试环境：0f39a69ae7438edabb309aba5e65936e
//    预上线环境：0f39a69ae7438edabb309aba5e65936e
//    生产环境：6f9394462886bedec1b2c76ccf9d2365
    private static final String PWD = "0f39a69ae7438edabb309aba5e65936e";//签名密码
    @BindView(R.id.et_account)
    EditText etAccount;
    @BindView(R.id.et_number)
    EditText etNumber;
    @BindView(R.id.et_password)
    EditText etPassword;
    @BindView(R.id.tv_code)
    TextView tvCode;
    @BindView(R.id.tv_phone)
    TextView tvPhone;
    @BindView(R.id.btn_login)
    Button btnLogin;
    @BindView(R.id.iv_bg)
    ImageView ivBg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_new2);
        IMStatusBarUtil.setTranslucentForImageView(this, 0, null);
       initView();
    }

    private void initView() {
//        AnimationDrawable ad = (AnimationDrawable) getResources().getDrawable(
//                R.drawable.anim_list_login);
//        if (ad != null ){
//            ad.stop();
//            ivBg.clearAnimation();
//            ivBg.setImageDrawable(ad);
//            ad.start();
//        }
    }

    @OnClick({R.id.tv_code, R.id.tv_phone, R.id.btn_login,R.id.iv_back})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_code:
                String mobile=etAccount.getText().toString();
                String circleCode="11086";
                String timestamp=System.currentTimeMillis()+"";
                if(mobile.isEmpty()) {
                    ToastUtils.showShort("请输入手机号");
                    return;
                }
                if(circleCode.isEmpty()) {
                    ToastUtils.showShort("请输入圈码");
                    return;
                }
                String signature = sign(PWD, mobile, circleCode, timestamp);
                String mobile_base64 = EncodeUtils.base64Encode2String(mobile.getBytes());
                LogUtils.i("WOLF","signature:"+signature);
                LogUtils.i("WOLF","mobile_base64:"+mobile_base64);
                LogUtils.i("WOLF","timestamp:"+timestamp);
                LogUtils.i("WOLF","circleCode:"+circleCode);
                getCodeMessage(signature,mobile_base64,timestamp,circleCode);
                break;
            case R.id.tv_phone:
                startActivity(new Intent(this,LoginNewActivity.class));
                break;
            case R.id.btn_login:
                String mobile2=etAccount.getText().toString();
                String circleCode2=etNumber.getText().toString();
                String code2=etPassword.getText().toString();
                if(mobile2.isEmpty()) {
                    ToastUtils.showShort("请输入手机号");
                    return;
                }
                if(circleCode2.isEmpty()) {
                    ToastUtils.showShort("请输入圈码");
                    return;
                }
                if(code2.isEmpty()) {
                    ToastUtils.showShort("请输入验证码");
                    return;
                }
                String mobile_base64_2 = EncodeUtils.base64Encode2String(mobile2.getBytes());
                String code2_base64 = EncodeUtils.base64Encode2String(code2.getBytes());
                loginIn();
                doLogin(circleCode2,2+"",mobile_base64_2,code2_base64);
                break;
            case R.id.iv_back:
                finish();
                break;
        }
    }

    /**
     * 登录-手机号登录
     */
    private void doLogin(String circleCode,String loginType,String mobile_base64,String verifyCode) {
        CommonBean bean=new CommonBean();
        bean.setCircleCode(circleCode);
        bean.setLoginType(loginType);
        bean.setMobile(mobile_base64);
        bean.setVerifyCode(verifyCode);

        String json = new Gson().toJson(bean);
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"),json);
        HttpsService.doLogin(body, new HttpResultObserver<LoginBean>() {
            @Override
            public void onSuccess(LoginBean s, String message) {
                dismissLoadingDialogTypeTwo();
                IMPreferenceUtil.setPreference_String(IMSConfig.SAVE_TOKEN,s.getToken());    //保存token之后用token回去个人信息
                IMSHttpManager.getInstance().IMHttpGetSelfInfor(); //调用http这边的个人信息
                String imtoken = IMPreferenceUtil.getPreference_String(IMSConfig.SAVE_IM_TOKEN, "");
                if( TextUtils.isEmpty(imtoken)){   //如果本地无token,获取imtoken，本地有token,直接可以用
                    IMSHttpManager.getInstance().IMHttpGetIMToken(true);
                }else {
                    IMSHttpManager.getInstance().IMHttpGetHostIp(true);
                }
            }

            @Override
            public void _onError(Throwable e) {
                dismissLoadingDialogTypeTwo();
                ToastUtils.showShort(e.getMessage());
            }

            @Override
            public void _onErrorLocal(Throwable e, String message, String code) {
                dismissLoadingDialogTypeTwo();
                ToastUtils.showShort(message);
            }
        });
    }

    /**
     *
     * 获取短信
     * @param circleCode
     */
    private void getCodeMessage(String signature, String mobile_base64, String timestamp, String circleCode) {
        CommonBean bean=new CommonBean();
        bean.setCircleCode(circleCode);
        bean.setSignature(signature);
        bean.setMobile(mobile_base64);
        bean.setTimestamp(timestamp);

        String json = new Gson().toJson(bean);
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"),json);
        showLoadingDialog();
        HttpsService.creatGetCodeJson(body, new HttpResultObserver<String>() {
            @Override
            public void onSuccess(String s, String message) {
                dismissLoadingDialog();

            }

            @Override
            public void _onError(Throwable e) {
                dismissLoadingDialog();
                ToastUtils.showShort(e.getMessage());
            }

            @Override
            public void _onErrorLocal(Throwable e, String message, String code) {
                dismissLoadingDialog();
                ToastUtils.showShort(message);
            }
        });



    }

    /**
     * 签名
     */
    private String sign(String pwd,String mobile,String circleCode,String timestamp) {
        return IMMd5Util.md5(pwd+"#"+mobile+"#"+circleCode+"#"+timestamp);
    }


    public  void  loginIn(){
//        showLoadingDialogTypeTwo("正在登录，请稍后...");
        showLoadingDialog();
        IMSManager.getInstance().setLoginListener(new IMManegeLoginInterface() {
            @Override
            public void LoginSucess(boolean type,String message) {
//                dismissLoadingDialogTypeTwo();
                dismissLoadingDialog();
                if(type){
                    String userId = IMPreferenceUtil.getPreference_String(IMSConfig.SAVE_USERID, "");
                    JPushInterface.setAlias(LoginNew2Activity.this,1,userId);
                    startActivity(new Intent(LoginNew2Activity.this, MainActivity.class));
                    finish();
                }else {
                    Toast.makeText(LoginNew2Activity.this, message, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
