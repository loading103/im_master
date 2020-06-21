package com.rhby.cailexun.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
import com.blankj.utilcode.util.KeyboardUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.RegexUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.google.gson.Gson;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.jpush.android.api.JPushInterface;
import okhttp3.MediaType;
import okhttp3.RequestBody;

/**
 * Created by Wolf on 2019/11/28.
 * Describe:注册
 */
public class RegisterActivity extends BaseActivity {
    private static final String CIRCLE_CODE = "tomato";//圈码
    @BindView(R.id.tv_top_title)
    TextView tvTopTitle;
    @BindView(R.id.et_account)
    EditText etAccount;
    @BindView(R.id.et_password)
    EditText etPassword;
    @BindView(R.id.et_password2)
    EditText etPassword2;
    @BindView(R.id.btn_register)
    Button btnRegister;
    private String data;
    private String mobile_base64;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        IMStatusBarUtil.setTranslucent(this, 0);
        IMStatusBarUtil.setLightMode(this);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        tvTopTitle.setText("注册");
        data=getIntent().getStringExtra("data");
        mobile_base64=getIntent().getStringExtra("mobile_base64");

    }

    /**
     *
     * 查看用户名是否被使用
     */
    private void checkUsername(String circleCode,String username_base64) {
        CommonBean bean=new CommonBean();
        bean.setCircleCode(circleCode);
        bean.setUsername(username_base64);
        LogUtils.i("WOLF","圈码："+circleCode+"用户名："+username_base64);
        String json = new Gson().toJson(bean);
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"),json);
        showLoadingDialog();
        HttpsService.checkUsername(body, new HttpResultObserver<Object>() {
            @Override
            public void onSuccess(Object s, String message) {
                dismissLoadingDialog();
                String password = IMMd5Util.md5(etPassword.getText().toString());
                LogUtils.i("WOLF","CIRCLE_CODE:"+CIRCLE_CODE);
                LogUtils.i("WOLF","mobile_base64:"+mobile_base64);
                LogUtils.i("WOLF","password:"+password);
                LogUtils.i("WOLF","data:"+data);
                LogUtils.i("WOLF","username_base64:"+username_base64);
                doRegisterCommit(CIRCLE_CODE,mobile_base64,password,data,username_base64);
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
     * 提交注册信息
     */
    private void doRegisterCommit(String circleCode,String mobile_base64
            ,String password,String receiptId,String username_base64) {
        CommonBean bean=new CommonBean();
        bean.setCircleCode(circleCode);
        bean.setMobile(mobile_base64);
        bean.setPassword(password);
        bean.setReceiptId(receiptId);
        bean.setUsername(username_base64);

        LogUtils.i("WOLF","circleCode:"+circleCode);
        LogUtils.i("WOLF","mobile_base64:"+mobile_base64);
        LogUtils.i("WOLF","password:"+password);
        LogUtils.i("WOLF","receiptId:"+receiptId);
        LogUtils.i("WOLF","username_base64:"+username_base64);

        String json = new Gson().toJson(bean);
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"),json);
        showLoadingDialog();
        HttpsService.doRegisterCommit(body, new HttpResultObserver<LoginBean>() {
            @Override
            public void onSuccess(LoginBean s, String message) {
                dismissLoadingDialog();
                ToastUtils.showShort("注册成功");
                loginIn();
                IMPreferenceUtil.setPreference_String(IMSConfig.SAVE_TOKEN, s.getToken());    //保存token之后用token回去个人信息
                IMSHttpManager.getInstance().IMHttpGetSelfInfor(); //调用http这边的个人信息
                String imtoken = IMPreferenceUtil.getPreference_String(IMSConfig.SAVE_IM_TOKEN, "");
                if (TextUtils.isEmpty(imtoken)) {   //如果本地无token,获取imtoken，本地有token,直接可以用
                    IMSHttpManager.getInstance().IMHttpGetIMToken(true);
                } else {
                    IMSHttpManager.getInstance().IMHttpGetHostIp(true);
                }
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

    @OnClick({R.id.iv_top_finish, R.id.btn_register,R.id.ll_root})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_top_finish:
                finish();
                break;
            case R.id.btn_register:
                String username=etAccount.getText().toString();
                String password=etPassword.getText().toString();
                String password2=etPassword2.getText().toString();
                if(TextUtils.isEmpty(username)){
                    ToastUtils.showShort("请输入用户名");
                    return;
                }
                if(TextUtils.isEmpty(password)){
                    ToastUtils.showShort("请输入密码");
                    return;
                }
                String passRegex = "^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{6,18}$";
                if(!RegexUtils.isMatch(passRegex,password)){
                    ToastUtils.showShort("密码为6-18位字母与数字的组合");
                    return;
                }
                if(TextUtils.isEmpty(password2)){
                    ToastUtils.showShort("请再次输入密码");
                    return;
                }
                if(!password.equals(password2)){
                    ToastUtils.showShort("两次密码不一致，请重新输入");
                    return;
                }
                String username_base64 = EncodeUtils.base64Encode2String(username.getBytes());
                checkUsername(CIRCLE_CODE,username_base64);
                break;
            case R.id.ll_root:
                KeyboardUtils.hideSoftInput(this);
                break;
        }
    }
    public void loginIn() {
//        showLoadingDialogTypeTwo("正在登录，请稍后...");
        showLoadingDialog();
        IMSManager.getInstance().setLoginListener(new IMManegeLoginInterface() {
            @Override
            public void LoginSucess(boolean type, String message) {
//                dismissLoadingDialogTypeTwo();
                dismissLoadingDialog();
                if (type) {
                    String userId = IMPreferenceUtil.getPreference_String(IMSConfig.SAVE_USERID, "");
                    //测试环境：TEST_+customerId
                    //正式环境：RELEASE_+customerId
                    JPushInterface.setAlias(RegisterActivity.this, 1, "TEST_"+userId);
                    LogUtils.i("WOLF", "用户ID:" + userId);
                    startActivity(new Intent(RegisterActivity.this, MainActivity.class));
                    finish();
                } else {
                    Toast.makeText(RegisterActivity.this, message, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

}
