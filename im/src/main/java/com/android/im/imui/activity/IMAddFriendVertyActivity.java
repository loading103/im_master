package com.android.im.imui.activity;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.android.im.IMSManager;
import com.android.im.R;
import com.android.im.imbean.IMAddFriendBean;
import com.android.im.imnet.IMHttpResultObserver;
import com.android.im.imnet.http.IMHttpsService;
import com.android.im.imutils.IMStatusBarUtil;
import com.android.nettylibrary.http.IMPersonBeans;
import com.android.nettylibrary.http.IMUserInforBean;
import com.google.android.exoplayer2.util.Log;
import com.google.gson.Gson;

import okhttp3.RequestBody;

import static com.android.im.imnet.IMBaseConstant.JSON;
/**
 * 好有验证界面
 */
public class IMAddFriendVertyActivity extends IMBaseActivity implements View.OnClickListener, TextWatcher {
    private ImageView mIvFinish;
    private TextView mTvTitle;
    private EditText metContent;
    private TextView mIvSend;
    private TextView mTvNumber;
    private IMUserInforBean.UserInforData person;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_im_verty_friend);
        initView();
        initData();
    }

    private void initView() {
        IMStatusBarUtil.setTranslucent(this, 0);
        IMStatusBarUtil.setLightMode(this);
        mIvFinish = findViewById(R.id.tv_top_finish);
        mTvTitle = findViewById(R.id.tv_top_title);
        mIvFinish.setVisibility(View.VISIBLE);
        metContent = findViewById(R.id.etContent);
        metContent.setFilters(new InputFilter[]{new InputFilter.LengthFilter(50)});
        mIvSend = findViewById(R.id.im_iv_add);
        mTvNumber = findViewById(R.id.im_tv_number);
        mIvFinish.setOnClickListener(this);
        mIvSend.setOnClickListener(this);
        metContent.addTextChangedListener(this);
    }

    private void initData() {
        mTvTitle.setText("验证申请");
        String name = TextUtils.isEmpty(IMSManager.getMyNickName()) ? "XXX" : IMSManager.getMyNickName();
        metContent.setText("我是" + name);
        metContent.setSelection(metContent.getText().length());
        person = (IMUserInforBean.UserInforData) getIntent().getSerializableExtra("bean");
    }

    private void getHttpData() {
        showLoadingDialog();
        IMPersonBeans bean = new IMPersonBeans();
        bean.setAcceptCustomerId(person.getCustomerId());
        bean.setApplyRemark(metContent.getText().toString());
        String json = new Gson().toJson(bean);
        RequestBody body = RequestBody.create(JSON, json);
        IMHttpsService.addNewFriendJson(body, new IMHttpResultObserver<IMAddFriendBean>() {
            @Override
            public void onSuccess(IMAddFriendBean data, String message) {
                dismissLoadingDialog();
                Toast.makeText(IMAddFriendVertyActivity.this, "申请已提交，请耐心等待", Toast.LENGTH_SHORT).show();
                finish();
            }

            @Override
            public void _onError(Throwable e) {
                dismissLoadingDialog();
                Toast.makeText(IMAddFriendVertyActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                finish();
            }

            @Override
            public void _onErrorLocal(Throwable e, String message, String code) {
                dismissLoadingDialog();
                Toast.makeText(IMAddFriendVertyActivity.this, message, Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.tv_top_finish) {
            finish();
        } else if (v.getId() == R.id.im_iv_add) {
            if (TextUtils.isEmpty(metContent.getText().toString())) {
                Toast.makeText(this, "请输入验证信息", Toast.LENGTH_SHORT).show();
                return;
            }
            getHttpData();
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        if (!TextUtils.isEmpty(s.toString())) {
            mTvNumber.setText(s.toString().length() + "/50");
        } else {
            mTvNumber.setText("0/50");
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View view = getCurrentFocus();
            if (isShouldHideInput(view, ev)) {
                InputMethodManager Object = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                if (Object != null) {
                    Object.hideSoftInputFromWindow(view.getWindowToken(), 0);
                }
            }
        }
        return super.dispatchTouchEvent(ev);
    }

    //判断是否隐藏键盘
    public boolean isShouldHideInput(View v, MotionEvent event) {
        if (v != null && (v instanceof EditText)) {
            int[] leftTop = {0, 0};
            //获取输入框当前的location位置
            v.getLocationInWindow(leftTop);
            int left = leftTop[0];
            int top = leftTop[1];
            int bottom = top + v.getHeight();
            int right = left + v.getWidth();
            if (event.getX() > left && event.getX() < right
                    && event.getY() > top && event.getY() < bottom) {
                // 点击的是输入框区域，保留点击EditText的事件
                return false;
            } else {
                return true;
            }
        }
        return false;
    }
}