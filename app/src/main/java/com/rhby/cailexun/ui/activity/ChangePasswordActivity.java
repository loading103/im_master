package com.rhby.cailexun.ui.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.im.IMSManager;
import com.android.im.imutils.IMStatusBarUtil;
import com.rhby.cailexun.R;
import com.rhby.cailexun.bean.CommonBean;
import com.rhby.cailexun.event.MessageEvent;
import com.rhby.cailexun.net.HttpResultObserver;
import com.rhby.cailexun.net.http.HttpsService;
import com.rhby.cailexun.ui.base.BaseActivity;
import com.android.nettylibrary.utils.IMMd5Util;
import com.blankj.utilcode.util.RegexUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.google.gson.Gson;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.OnClick;
import cn.jpush.android.api.JPushInterface;
import okhttp3.MediaType;
import okhttp3.RequestBody;

/**
 * Created by Wolf on 2019/11/30.
 * Describe:修改密码
 */
public class ChangePasswordActivity extends BaseActivity {
    @BindView(R.id.tv_top_title)
    TextView tvTopTitle;
    @BindView(R.id.et_password)
    EditText etPassword;
    @BindView(R.id.et_password2)
    EditText etPassword2;
    @BindView(R.id.btn_change)
    Button btnChange;
    private String receiptId    ;
    private String mobile_base64;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        IMStatusBarUtil.setTranslucent(this, 0);
        IMStatusBarUtil.setLightMode(this);
        initView();
    }

    private void initView() {
        tvTopTitle.setText("修改密码");
        receiptId=getIntent().getStringExtra("receiptId");
        mobile_base64=getIntent().getStringExtra("mobile_base64");
    }

    @OnClick({R.id.iv_top_finish, R.id.btn_change})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_top_finish:
                finish();
                break;
            case R.id.btn_change:
                String password=etPassword.getText().toString();
                String password2=etPassword2.getText().toString();
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
                String password_md5 = IMMd5Util.md5(etPassword.getText().toString());
                doForgetCommit(mobile_base64,password_md5,receiptId);
                break;
        }
    }

    /**
     *
     * 获取验证码——忘记密码
     */
    private void doForgetCommit(String mobile_base64,String password_md5,String receiptId) {
        CommonBean bean=new CommonBean();
        bean.setMobile(mobile_base64);
        bean.setPassword(password_md5);
        bean.setReceiptId(receiptId);

        String json = new Gson().toJson(bean);
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"),json);
        showLoadingDialog();
        HttpsService.doForgetCommit(body, new HttpResultObserver<String>() {
            @Override
            public void onSuccess(String s, String message) {
                dismissLoadingDialog();
                ToastUtils.showShort("修改密码成功，请重新登录");
                IMSManager.getInstance().ImChatLoginOut();
                JPushInterface.deleteAlias(ChangePasswordActivity.this,1);
                startActicity(LoginNewActivity.class);
                EventBus.getDefault().post(new MessageEvent("exit_login"));
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

}
