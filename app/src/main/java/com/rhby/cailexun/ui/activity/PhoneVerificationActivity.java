package com.rhby.cailexun.ui.activity;

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
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.android.im.imutils.IMStatusBarUtil;
import com.rhby.cailexun.R;
import com.rhby.cailexun.bean.CommonBean;
import com.rhby.cailexun.net.HttpResultObserver;
import com.rhby.cailexun.net.http.HttpsService;
import com.rhby.cailexun.ui.base.BaseActivity;
import com.android.nettylibrary.utils.IMMd5Util;
import com.blankj.utilcode.util.EncodeUtils;
import com.blankj.utilcode.util.KeyboardUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.RegexUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.google.gson.Gson;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.MediaType;
import okhttp3.RequestBody;

/**
 * Created by Wolf on 2019/11/28.
 * Describe:手机验证
 */
public class PhoneVerificationActivity extends BaseActivity {
    private static final String CIRCLE_CODE = "tomato";//圈码
    private static final String PWD = "0f39a69ae7438edabb309aba5e65936e";//签名密码
    @BindView(R.id.tv_top_title)
    TextView tvTopTitle;
    @BindView(R.id.et_phone)
    EditText etPhone;
    @BindView(R.id.et_code)
    EditText etCode;
    @BindView(R.id.btn_register)
    Button btnRegister;
    @BindView(R.id.tv_code)
    TextView tvCode;
    @BindView(R.id.tv_agree)
    TextView tvAgree;
    private String type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_verification);
        ButterKnife.bind(this);
        IMStatusBarUtil.setTranslucent(this, 0);
        IMStatusBarUtil.setLightMode(this);
        initView();
    }

    private void initView() {
        tvTopTitle.setText("手机验证");
        type = getIntent().getStringExtra("type");
        if(type.equals("注册")){
            tvAgree.setVisibility(View.VISIBLE);
            initAgree();
        }else if(type.equals("修改密码")){
            tvAgree.setVisibility(View.GONE);
        }
    }

    /**
     * 设置用户协议相关
     */
    private void initAgree() {
        SpannableString spannableString = new SpannableString(tvAgree.getText().toString());
        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(View view) {
                Bundle bundle=new Bundle();
                bundle.putString("title","用户协议");
                bundle.putString("url","http://65.52.160.124:9020/discover/agreement/index.html");
                startActicity(CommonWebActivity.class,bundle);
            }

            @Override
            public void updateDrawState(TextPaint ds) {
                ds.setColor(ContextCompat.getColor(PhoneVerificationActivity.this, R.color.colorPrimary));//设置颜色
                ds.setUnderlineText(false);//去掉下划线
            }
        };
        spannableString.setSpan(clickableSpan, 8, 14, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);

        ClickableSpan clickableSpan2 = new ClickableSpan() {
            @Override
            public void onClick(View view) {
                Bundle bundle=new Bundle();
                bundle.putString("title","隐私条款");
                bundle.putString("url","http://65.52.160.124:9020/discover/privacy-terms/index.html");
                startActicity(CommonWebActivity.class,bundle);
            }

            @Override
            public void updateDrawState(TextPaint ds) {
                ds.setColor(ContextCompat.getColor(PhoneVerificationActivity.this, R.color.color_02AEFD));//设置颜色
                ds.setUnderlineText(false);//去掉下划线
            }
        };
        spannableString.setSpan(clickableSpan2, 16, 22, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
        ForegroundColorSpan colorSpan = new ForegroundColorSpan(ContextCompat.getColor(this, R.color.color_02AEFD));
        spannableString.setSpan(colorSpan, 8, 14, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);

        ForegroundColorSpan colorSpan2 = new ForegroundColorSpan(ContextCompat.getColor(this, R.color.color_02AEFD));
        spannableString.setSpan(colorSpan2, 16, 22, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);

        tvAgree.setText(spannableString);
        tvAgree.setMovementMethod(LinkMovementMethod.getInstance());
    }

    /**
     *
     * 获取验证码--注册
     * @param circleCode
     */
    private void getRegisterCode(String signature, String mobile_base64, String timestamp, String circleCode) {
        CommonBean bean=new CommonBean();
        bean.setCircleCode(circleCode);
        bean.setSignature(signature);
        bean.setMobile(mobile_base64);
        bean.setTimestamp(timestamp);

        LogUtils.i("WOLF","circleCode:"+circleCode);
        LogUtils.i("WOLF","mobile_base64:"+mobile_base64);
        LogUtils.i("WOLF","signature:"+signature);
        LogUtils.i("WOLF","timestamp:"+timestamp);

        String json = new Gson().toJson(bean);
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"),json);
        showLoadingDialog();
        HttpsService.getRegisterCode(body, new HttpResultObserver<String>() {
            @Override
            public void onSuccess(String s, String message) {
                dismissLoadingDialog();
                ToastUtils.showShort("发送成功");
                new CountDownTimer(60000, 1000) {
                    @Override
                    public void onTick(long millisUntilFinished) {
                        tvCode.setText(millisUntilFinished/1000+"s");
                        tvCode.setClickable(false);
                    }

                    @Override
                    public void onFinish() {
                        tvCode.setText("获取验证码");
                        tvCode.setClickable(true);
                    }
                }.start();
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
     *
     * 获取验证码——忘记密码
     */
    private void getCredentialsVerify(String mobile_base64) {
        CommonBean bean=new CommonBean();
        bean.setMobile(mobile_base64);

        String json = new Gson().toJson(bean);
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"),json);
        showLoadingDialog();
        HttpsService.getCredentialsVerify(body, new HttpResultObserver<Object>() {
            @Override
            public void onSuccess(Object s, String message) {
                dismissLoadingDialog();
                ToastUtils.showShort("发送成功");
                new CountDownTimer(60000, 1000) {
                    @Override
                    public void onTick(long millisUntilFinished) {
                        tvCode.setText(millisUntilFinished/1000+"s");
                        tvCode.setClickable(false);
                    }

                    @Override
                    public void onFinish() {
                        tvCode.setText("获取验证码");
                        tvCode.setClickable(true);
                    }
                }.start();
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
     *
     * 效验验证码——注册
     */
    private void checkRegisterCode(String circleCode,String mobile_base64, String code_base64) {
        CommonBean bean=new CommonBean();
        bean.setCircleCode(circleCode);
        bean.setMobile(mobile_base64);
        bean.setVerifyCode(code_base64);

        String json = new Gson().toJson(bean);
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"),json);
        showLoadingDialog();
        HttpsService.checkCode(body, new HttpResultObserver<String>() {
            @Override
            public void onSuccess(String s, String message) {
                dismissLoadingDialog();
                startActivity(new Intent(PhoneVerificationActivity.this,RegisterActivity.class)
                        .putExtra("data",s).putExtra("mobile_base64",mobile_base64));
                finish();
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
     *
     * 效验验证码——忘记密码
     */
    private void checkCodeForget(String mobile_base64, String code_base64) {
        CommonBean bean=new CommonBean();
        bean.setMobile(mobile_base64);
        bean.setVerifyCode(code_base64);

        String json = new Gson().toJson(bean);
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"),json);
        showLoadingDialog();
        HttpsService.checkCodeForget(body, new HttpResultObserver<String>() {
            @Override
            public void onSuccess(String s, String message) {
                dismissLoadingDialog();
                Bundle bundle=new Bundle();
                bundle.putString("mobile_base64",mobile_base64);
                bundle.putString("receiptId",s);
               startActicity(ChangePasswordActivity.class,bundle);
               finish();
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


    @OnClick({R.id.iv_top_finish, R.id.btn_register,R.id.tv_code,R.id.ll_root})
    public void onViewClicked(View view) {
        String mobile=etPhone.getText().toString();
        String mobile_base64 = EncodeUtils.base64Encode2String(mobile.getBytes());
        switch (view.getId()) {
            case R.id.iv_top_finish:
                finish();
                break;
            case R.id.btn_register:
                String code=etCode.getText().toString();
                if(mobile.isEmpty()) {
                    ToastUtils.showShort("请输入手机号");
                    return;
                }
                if(!RegexUtils.isMobileExact(mobile)) {
                    ToastUtils.showShort("请输入正确的手机号");
                    return;
                }
                if(TextUtils.isEmpty(code)){
                    ToastUtils.showShort("请输入验证码");
                    return;
                }
                String code_base64 = EncodeUtils.base64Encode2String(code.getBytes());
                if(type.equals("注册")){
                    checkRegisterCode(CIRCLE_CODE,mobile_base64,code_base64);
                }else {
                    checkCodeForget(mobile_base64,code_base64);
                }
                break;
            case R.id.tv_code:
                String timestamp=System.currentTimeMillis()+"";
                if(mobile.isEmpty()) {
                    ToastUtils.showShort("请输入手机号");
                    return;
                }
                if(!RegexUtils.isMobileExact(mobile)) {
                    ToastUtils.showShort("请输入正确的手机号");
                    return;
                }
                String signature = sign(PWD, mobile, CIRCLE_CODE, timestamp);
                if(type.equals("注册")){
                    getRegisterCode(signature,mobile_base64,timestamp,CIRCLE_CODE);
                }else {
                    getCredentialsVerify(mobile_base64);
                }
                break;
            case R.id.ll_root:
                KeyboardUtils.hideSoftInput(this);
                break;
        }
    }

    /**
     * 签名
     */
    private String sign(String pwd,String mobile,String circleCode,String timestamp) {
        return IMMd5Util.md5(pwd+"#"+mobile+"#"+circleCode+"#"+timestamp);
    }
}
