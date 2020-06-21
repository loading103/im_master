package com.rhby.cailexun.ui.activity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.ContextCompat;

import com.android.im.IMSHttpManager;
import com.android.im.IMSManager;
import com.android.im.IMSMsgManager;
import com.android.im.iminterface.IMManegeLoginInterface;
import com.android.im.imnet.IMHttpResultObserver;
import com.android.im.imnet.http.IMHttpsService;
import com.android.im.imutils.IMStatusBarUtil;
import com.rhby.cailexun.R;
import com.rhby.cailexun.bean.CommonBean;
import com.rhby.cailexun.bean.LoginBean;
import com.rhby.cailexun.dialog.VerificationDiglog;
import com.rhby.cailexun.net.HttpResultObserver;
import com.rhby.cailexun.net.http.HttpsService;
import com.rhby.cailexun.ui.base.BaseActivity;
import com.rhby.cailexun.utils.AnimUtils;
import com.rhby.cailexun.widget.CustomSlideToUnlockView;
import com.android.nettylibrary.IMSConfig;
import com.android.nettylibrary.http.IMUserInforBean;
import com.android.nettylibrary.utils.IMDensityUtil;
import com.android.nettylibrary.utils.IMLogUtil;
import com.android.nettylibrary.utils.IMMd5Util;
import com.android.nettylibrary.utils.IMPreferenceUtil;
import com.blankj.utilcode.util.EncodeUtils;
import com.blankj.utilcode.util.GsonUtils;
import com.blankj.utilcode.util.KeyboardUtils;
import com.blankj.utilcode.util.RegexUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.google.gson.Gson;

import butterknife.BindView;
import butterknife.OnClick;
import cn.jpush.android.api.JPushInterface;
import okhttp3.MediaType;
import okhttp3.RequestBody;

/**
 * 登录（新）
 */
public class LoginNewActivity extends BaseActivity implements VerificationDiglog.OnFinishListener {
    private static final String PWD = "0f39a69ae7438edabb309aba5e65936e";//签名密码
    private static final String CIRCLE_CODE = "tomato";//圈码
    @BindView(R.id.et_account)
    EditText etAccount;
    @BindView(R.id.et_number)
    EditText etNumber;
    @BindView(R.id.et_password)
    EditText etPassword;
    @BindView(R.id.tv_code)
    TextView tvCode;
    @BindView(R.id.btn_login)
    Button btnLogin;
    //    @BindView(R.id.iv_bg)
//    FrameLayout ivBg;
    @BindView(R.id.tv_agree)
    TextView tvAgree;
    @BindView(R.id.ll_phone)
    LinearLayout llPhone;
    @BindView(R.id.ll_account)
    LinearLayout llAccount;
    @BindView(R.id.et_account2)
    EditText etAccount2;
    @BindView(R.id.et_number2)
    EditText etNumber2;
    @BindView(R.id.et_password2)
    EditText etPassword2;
    @BindView(R.id.tv_code2)
    TextView tvCode2;
    @BindView(R.id.tv_phone)
    TextView tvPhone;
    @BindView(R.id.ll_root)
    LinearLayout llRoot;
    @BindView(R.id.slide_to_unlock)
    CustomSlideToUnlockView mCustomSlideToUnlockView;
    private VerificationDiglog mDiglog;
    private int loginErrNum = 0;//登录出错次数

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_news);
        IMStatusBarUtil.setTranslucentForImageView(this, 0, null);
        IMStatusBarUtil.setLightMode(this);
        setSwipeBackEnable(false);
        initView();
        showAgreeData();
    }
    private void initView() {
        String useName = IMPreferenceUtil.getPreference_String(IMSConfig.SAVE_USER_NAME, "");
        if (!TextUtils.isEmpty(useName)) {
            etAccount.setText(useName);
            etNumber.setText("彩乐讯");
        }
        initAgree();
        String  phone= IMPreferenceUtil.getPreference_String(IMSConfig.PHONE, "");
        if(!TextUtils.isEmpty(phone)){
            etAccount2.setText(phone);
            etAccount2.setSelection(phone.length());
        }
        //滑动登录
        mCustomSlideToUnlockView.setmCallBack(new CustomSlideToUnlockView.CallBack() {
            @Override
            public void onSlide(int distance) {

            }

            @Override
            public void onUnlocked() {
                HttpLoginIn();
            }
        });
    }

    /**
     * 设置用户协议相关
     */
    private void initAgree() {
        SpannableString spannableString = new SpannableString(tvAgree.getText().toString());
        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putString("title", "用户协议");
                bundle.putString("url", IMSConfig.HTTP_USER_URL);
                startActicity(CommonWebActivity.class, bundle);
            }

            @Override
            public void updateDrawState(TextPaint ds) {
                ds.setColor(ContextCompat.getColor(LoginNewActivity.this, R.color.color_0BD2CF));//设置颜色
                ds.setUnderlineText(false);//去掉下划线
            }
        };
        spannableString.setSpan(clickableSpan, 8, 14, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);

        ClickableSpan clickableSpan2 = new ClickableSpan() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putString("title", "隐私条款");
                bundle.putString("url",  IMSConfig.HTTP_PRIVATE_URL);
                startActicity(CommonWebActivity.class, bundle);
            }

            @Override
            public void updateDrawState(TextPaint ds) {
                ds.setColor(ContextCompat.getColor(LoginNewActivity.this, R.color.color_0BD2CF));//设置颜色
                ds.setUnderlineText(false);//去掉下划线
            }
        };
        spannableString.setSpan(clickableSpan2, 14, tvAgree.getText().toString().length(), Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
        ForegroundColorSpan colorSpan = new ForegroundColorSpan(ContextCompat.getColor(this, R.color.color_0BD2CF));
        spannableString.setSpan(colorSpan, 14, tvAgree.getText().toString().length(), Spannable.SPAN_EXCLUSIVE_INCLUSIVE);


        tvAgree.setText(spannableString);
        tvAgree.setMovementMethod(LinkMovementMethod.getInstance());
    }

    @OnClick({R.id.tv_code, R.id.btn_login, R.id.tv_register, R.id.tv_code2, R.id.tv_phone, R.id.ll_root})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_code:
//                startActivity(new Intent(this, LoginNew2Activity.class));
                AnimUtils.FlipAnimatorXViewShowLR(llAccount, llPhone, 300);
                break;
            case R.id.btn_login:
                HttpLoginIn();
                break;
            case R.id.tv_register:
                Bundle bundle = new Bundle();
                bundle.putString("type", "注册");
                startActicity(PhoneVerificationActivity.class, bundle);
                break;
            case R.id.tv_code2:
                String mobile = etAccount2.getText().toString();
                String timestamp = System.currentTimeMillis() + "";
                if (mobile.isEmpty()) {
                    ToastUtils.showShort("请输入手机号");
                    return;
                }
                String signature = sign(PWD, mobile, CIRCLE_CODE, timestamp);
                String mobile_base64 = EncodeUtils.base64Encode2String(mobile.getBytes());
                getCodeMessage(signature, mobile_base64, timestamp, CIRCLE_CODE);
                new CountDownTimer(30000, 1000) {
                    @Override
                    public void onTick(long millisUntilFinished) {
                        if(tvCode2!=null){
                            tvCode2.setText(millisUntilFinished / 1000 + "s");
                            tvCode2.setClickable(false);
                        }
                    }

                    @Override
                    public void onFinish() {
                        if(tvCode2!=null){
                            tvCode2.setText("获取验证码");
                            tvCode2.setClickable(true);
                        }
                    }
                }.start();
                break;
            case R.id.tv_phone:
                AnimUtils.FlipAnimatorXViewShowLR(llPhone, llAccount, 300);
                break;
            case R.id.ll_root:
                KeyboardUtils.hideSoftInput(this);
                break;
        }
    }

    /**
     * 手机登录
     */
    private void loginPhone() {
        String mobile2 = etAccount2.getText().toString();
        String code2 = etPassword2.getText().toString();
        if (mobile2.isEmpty()) {
            ToastUtils.showShort("请输入手机号");
            mCustomSlideToUnlockView.resetView();
            return;
        }
        if (!RegexUtils.isMobileExact(mobile2)) {
            ToastUtils.showShort("请输入正确的手机号");
            mCustomSlideToUnlockView.resetView();
            return;
        }
        if (code2.isEmpty()) {
            ToastUtils.showShort("请输入验证码");
            mCustomSlideToUnlockView.resetView();
            return;
        }
        //显示用户协议弹窗
        boolean logindata = IMPreferenceUtil.getPreference_Boolean(IMSConfig.SAVE_LOGIN_DATA, false);
        if(!logindata){
            showLoginAgreeDialog();
            return;
        }
        String mobile_base64_2 = EncodeUtils.base64Encode2String(mobile2.getBytes());
        String code2_base64 = EncodeUtils.base64Encode2String(code2.getBytes());
        LoginSuccessListener();
        doLogin(CIRCLE_CODE, 2 + "", mobile_base64_2, code2_base64);
    }


    public void LoginSuccessListener() {
        showLoadingDialog();
        IMSManager.getInstance().setLoginListener(new IMManegeLoginInterface() {
            @Override
            public void LoginSucess(boolean type, String message) {
                dismissLoadingDialog();
                if (type) {
                    String userId = IMPreferenceUtil.getPreference_String(IMSConfig.SAVE_USERID, "");
                    //测试环境：TEST_+customerId
                    //正式环境：RELEASE_+customerId
                    JPushInterface.setAlias(LoginNewActivity.this, 1, "TEST_" + userId);
                    startActivity(new Intent(LoginNewActivity.this, MainActivity.class));
                    finish();
                } else {
                    Toast.makeText(LoginNewActivity.this, message, Toast.LENGTH_SHORT).show();
                    if(mCustomSlideToUnlockView!=null){
                        mCustomSlideToUnlockView.resetView();
                    }
                }
            }
        });
    }

    /**
     * 登录-手机号登录
     */
    private void doLogin(String circleCode, String loginType, String mobile_base64, String verifyCode) {
        CommonBean bean = new CommonBean();
        bean.setCircleCode(circleCode);
        bean.setLoginType(loginType);
        bean.setMobile(mobile_base64);
        bean.setVerifyCode(verifyCode);

        String json = new Gson().toJson(bean);
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json);
        HttpsService.doLogin2(body, new HttpResultObserver<Object>() {
            @Override
            public void onSuccess(Object s, String message) {
                LoginBean loginBean = GsonUtils.fromJson(GsonUtils.toJson(s), LoginBean.class);
                IMPreferenceUtil.setPreference_String(IMSConfig.SAVE_TOKEN, loginBean.getToken());    //保存token之后用token回去个人信息
                IMPreferenceUtil.setPreference_String(IMSConfig.PHONE, etAccount2.getText().toString().trim());    //保存手机号
                IMHttpGetSelfInfor();
            }

            @Override
            public void _onError(Throwable e) {
                dismissLoadingDialog();
                ToastUtils.showShort(e.getMessage());
                mCustomSlideToUnlockView.resetView();
                loginErrNum++;
            }

            @Override
            public void _onErrorLocal(Throwable e, String message, String code) {
                dismissLoadingDialog();
                if (!TextUtils.isEmpty(message)) {
                    ToastUtils.showShort(message);
                    mCustomSlideToUnlockView.resetView();
                }
                if (code.equals("MOBILE_USER_NOT_FOUND")) {//验证成功，但手机号未注册
                    Bundle bundle = new Bundle();
                    bundle.putString("data", message);
                    bundle.putString("mobile_base64", mobile_base64);
                    dismissLoadingDialog();
                    mCustomSlideToUnlockView.resetView();
                    startActicity(SetInformationActivity.class, bundle);
                }

                loginErrNum++;
            }
        });
    }

    /**
     * 用token回去个人信息(获取成功userid之后)
     */
    public void IMHttpGetSelfInfor()  {
        IMHttpsService.GetSelfInforJson(new IMHttpResultObserver<IMUserInforBean.UserInforData>() {
            @Override
            public void onSuccess(IMUserInforBean.UserInforData data, String message) {
                IMLogUtil.d("MyOwnTag:", "IMSNettyManager " +"(onResponse) " +"获取个人信息成功"+new Gson().toJson(data));
                if(!TextUtils.isEmpty(data.getNickName())){
                    //保存用户信息
                    IMSMsgManager.SaveLoginData(data);
                    //处理背景图
                    IMSManager.getInstance().setMyBgUrl(data.getBgUrl());
                    //获取好友和群信息
                    IMSHttpManager.getInstance().IMHttpGetConversationList(true,true);

                }else {
                    dismissLoadingDialog();
                    mCustomSlideToUnlockView.resetView();
                    startActicity(SetInformationActivity.class);
                }
            }

            @Override
            public void _onError(Throwable e) {
                IMSManager.getInstance().setLoginInListener(false,e.getMessage());
            }

            @Override
            public void _onErrorLocal(Throwable e, String message, String code) {
                IMSManager.getInstance().setLoginInListener(false,message);
            }
        });
    }


    /**
     * 获取短信
     *
     * @param circleCode
     */
    private void getCodeMessage(String signature, String mobile_base64, String timestamp, String circleCode) {
        CommonBean bean = new CommonBean();
        bean.setCircleCode(circleCode);
        bean.setSignature(signature);
        bean.setMobile(mobile_base64);
        bean.setTimestamp(timestamp);

        String json = new Gson().toJson(bean);
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json);
        HttpsService.creatGetCodeJson(body, new HttpResultObserver<String>() {
            @Override
            public void onSuccess(String s, String message) {
                ToastUtils.showShort("获取验证码成功");
            }

            @Override
            public void _onError(Throwable e) {
                ToastUtils.showShort(e.getMessage());
            }

            @Override
            public void _onErrorLocal(Throwable e, String message, String code) {
                ToastUtils.showShort(message);
            }
        });
    }

    /**
     * 签名
     */
    private String sign(String pwd, String mobile, String circleCode, String timestamp) {
        return IMMd5Util.md5(pwd + "#" + mobile + "#" + circleCode + "#" + timestamp);
    }


    //第一次显示用户隐私协议
    private void showAgreeData() {
        boolean logindata = IMPreferenceUtil.getPreference_Boolean(IMSConfig.SAVE_LOGIN_DATA, false);
        if(!logindata){
            showLoginAgreeDialog();
        }
    }

    /**
     * 提示用户协议
     */
    private AlertDialog shareDialog;
    public  void showLoginAgreeDialog(){
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        final View dialogView =  View.inflate(this, com.android.im.R.layout.layout_dialog_login_user, null);
        LinearLayout  ll_contain_1 = dialogView.findViewById(com.android.im.R.id.ll_contain_1);
        LinearLayout  ll_contain_2 = dialogView.findViewById(com.android.im.R.id.ll_contain_2);
        TextView  im_tv_1 = dialogView.findViewById(com.android.im.R.id.im_tv_1);
        TextView  im_tv_2 = dialogView.findViewById(com.android.im.R.id.im_tv_2);
        View  im_lin_1 = dialogView.findViewById(com.android.im.R.id.im_lin_1);
        View  im_lin_2 = dialogView.findViewById(com.android.im.R.id.im_lin_2);
        WebView im_web = dialogView.findViewById(com.android.im.R.id.im_web);
        TextView  mTvCancle = dialogView.findViewById(com.android.im.R.id.tv_cancle);
        TextView  mTvSure = dialogView.findViewById(com.android.im.R.id.tv_ensure);
        WebSettings settings = im_web.getSettings();
        settings.setDomStorageEnabled(true);
        settings.setUseWideViewPort(true);
        settings.setLoadWithOverviewMode(true);
        im_web.loadUrl(IMSConfig.HTTP_USER_URL);
        im_tv_1.setTextColor(getResources().getColor(R.color.blue0));
        im_tv_2.setTextColor(getResources().getColor(R.color.color_666666));
        im_lin_1.setVisibility(View.VISIBLE);
        im_lin_2.setVisibility(View.GONE);
        ll_contain_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                im_tv_1.setTextColor(getResources().getColor(R.color.blue0));
                im_tv_2.setTextColor(getResources().getColor(R.color.color_666666));
                im_lin_1.setVisibility(View.VISIBLE);
                im_lin_2.setVisibility(View.GONE);
                im_web.loadUrl(IMSConfig.HTTP_USER_URL);
            }
        });
        ll_contain_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                im_tv_1.setTextColor(getResources().getColor(R.color.color_666666));
                im_tv_2.setTextColor(getResources().getColor(R.color.blue0));
                im_lin_1.setVisibility(View.GONE);
                im_lin_2.setVisibility(View.VISIBLE);
                im_web.loadUrl(IMSConfig.HTTP_PRIVATE_URL);
            }
        });

        mTvSure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IMPreferenceUtil.setPreference_Boolean(IMSConfig.SAVE_LOGIN_DATA, true);
                mCustomSlideToUnlockView.resetView();
                shareDialog.dismiss();
            }
        });
        builder.setCancelable(false);
        mTvCancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareDialog.dismiss();
                IMPreferenceUtil.setPreference_Boolean(IMSConfig.SAVE_LOGIN_DATA, false);
                mCustomSlideToUnlockView.resetView();
            }
        });
        builder.setView(dialogView);
        shareDialog =  builder.show();
        Window window = shareDialog.getWindow();
        window.setWindowAnimations(com.android.im.R.style.ActionSheetDialogAnimations);  //添加动
        WindowManager.LayoutParams params = window.getAttributes();

        params.width = IMDensityUtil.getScreenWidth(this)- IMDensityUtil.dip2px(this,70);
        shareDialog.getWindow().setAttributes(params);
        shareDialog.getWindow().setBackgroundDrawable(getResources().getDrawable(R.drawable.im_shape__bg));
    }

    /**
     * 点击登录
     */
    private void HttpLoginIn() {
        if (loginErrNum > 99) {
            if (mDiglog == null) {
                mDiglog = new VerificationDiglog(LoginNewActivity.this);
            }
            mDiglog.setOnFinishListener(LoginNewActivity.this);

            mDiglog.showVerificationDiglog();
        } else {
            boolean logindata = IMPreferenceUtil.getPreference_Boolean(IMSConfig.SAVE_LOGIN_DATA, false);
            if(!logindata){
                showLoginAgreeDialog();
                return;
            }
            loginPhone();
        }
    }

    @Override
    public void onFinish(Boolean isSuccess) {
        if (isSuccess) {
            loginPhone();
        }
    }
}
