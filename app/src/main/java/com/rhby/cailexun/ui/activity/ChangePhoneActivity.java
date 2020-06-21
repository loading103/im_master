package com.rhby.cailexun.ui.activity;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.im.imutils.IMStatusBarUtil;
import com.rhby.cailexun.R;
import com.rhby.cailexun.bean.CommonBean;
import com.rhby.cailexun.net.HttpResultObserver;
import com.rhby.cailexun.net.http.HttpsService;
import com.rhby.cailexun.ui.base.BaseActivity;
import com.android.nettylibrary.IMSConfig;
import com.android.nettylibrary.utils.IMPreferenceUtil;
import com.blankj.utilcode.util.EncodeUtils;
import com.blankj.utilcode.util.KeyboardUtils;
import com.blankj.utilcode.util.RegexUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.google.gson.Gson;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.MediaType;
import okhttp3.RequestBody;

/**
 * Created by Wolf on 2019/12/2.
 * Describe:更改手机号
 */
public class ChangePhoneActivity extends BaseActivity {
    @BindView(R.id.tv_top_title)
    TextView tvTopTitle;
    @BindView(R.id.tv_phone)
    TextView tvPhone;
    @BindView(R.id.et_phone)
    EditText etPhone;
    @BindView(R.id.et_code)
    EditText etCode;
    @BindView(R.id.tv_code)
    TextView tvCode;
    @BindView(R.id.btn_change)
    Button btnChange;
    @BindView(R.id.ll_root)
    LinearLayout llRoot;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_phone);
        IMStatusBarUtil.setTranslucent(this,0);
        IMStatusBarUtil.setLightMode(this);
        initView();
    }

    private void initView() {
        tvTopTitle.setText("更换手机");
        String phone = IMPreferenceUtil.getPreference_String(IMSConfig.PHONE, "获取手机号失败");
        if(phone.length()==11){
            String s=phone.substring(0,3)+" "+phone.substring(3,7)+" "+phone.substring(7,11);
            tvPhone.setText(s);
        }else {
            tvPhone.setText(phone);
        }
    }

    @OnClick({R.id.iv_top_finish, R.id.tv_code, R.id.btn_change, R.id.ll_root})
    public void onViewClicked(View view) {
        String mobile = etPhone.getText().toString().trim();
        String mobile_base64 = EncodeUtils.base64Encode2String(mobile.getBytes());
        String code=etCode.getText().toString();
        switch (view.getId()) {
            case R.id.iv_top_finish:
                finish();
                break;
            case R.id.tv_code:
                if(mobile.isEmpty()) {
                    ToastUtils.showShort("请输入手机号");
                    return;
                }
                if(!RegexUtils.isMobileExact(mobile)) {
                    ToastUtils.showShort("请输入正确的手机号");
                    return;
                }
                getCodeModify(mobile_base64);
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
                break;
            case R.id.btn_change:
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
                doCommitModify(mobile_base64,code_base64);
                break;
            case R.id.ll_root:
                KeyboardUtils.hideSoftInput(this);
                break;
        }
    }

    /**
     * 修改手机号
     * @param mobile_base64
     * @param code_base64
     */
    private void doCommitModify(String mobile_base64, String code_base64) {
        CommonBean bean=new CommonBean();
        bean.setMobile(mobile_base64);
        bean.setVerifyCode(code_base64);

        String json = new Gson().toJson(bean);
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"),json);
        showLoadingDialog();
        HttpsService.doCommitModify(body, new HttpResultObserver<Object>() {
            @Override
            public void onSuccess(Object s, String message) {
                dismissLoadingDialog();
                ToastUtils.showShort("修改手机号成功");
//                IMSManager.getInstance().ImChatLoginOut();
//                JPushInterface.deleteAlias(ChangePhoneActivity.this,1);
//                startActicity(LoginNewActivity.class);
//                EventBus.getDefault().post(new MessageEvent("exit_login"));
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
     * 获取验证码
     * @param mobile_base64
     */
    private void getCodeModify(String mobile_base64) {
        CommonBean bean=new CommonBean();
        bean.setMobile(mobile_base64);

        String json = new Gson().toJson(bean);
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"),json);
        showLoadingDialog();
        HttpsService.getCodeModify(body, new HttpResultObserver<Object>() {
            @Override
            public void onSuccess(Object s, String message) {
                dismissLoadingDialog();
                ToastUtils.showShort("发送成功");
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
}
