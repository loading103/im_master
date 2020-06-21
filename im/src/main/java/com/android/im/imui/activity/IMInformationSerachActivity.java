package com.android.im.imui.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.im.IMSManager;
import com.android.im.R;
import com.android.im.imadapter.IMGroupHistoryAdapter;
import com.android.im.imutils.IMHideInPutUtil;
import com.android.im.imutils.IMStatusBarUtil;
import com.android.nettylibrary.IMSConfig;
import com.android.nettylibrary.greendao.entity.IMConversationBean;
import com.android.nettylibrary.greendao.entity.IMConversationDetailBean;
import com.android.nettylibrary.greendao.entity.IMGroupBean;
import com.android.nettylibrary.greendao.entity.IMGroupMemberBean;
import com.android.nettylibrary.greendao.entity.IMPersonBean;
import com.android.nettylibrary.http.IMGetIMMemberData;
import com.android.nettylibrary.http.IMGroupHistoryRoot;
import com.android.nettylibrary.http.IMMessageDataJson;
import com.android.nettylibrary.utils.IMLogUtil;
import com.android.nettylibrary.utils.IMPreferenceUtil;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static com.android.im.IMSManager.okHttpClient;
import static com.android.im.imnet.IMBaseConstant.JSON;

/**
 * 单聊，群聊的历史记录都在此界面
 */
public class IMInformationSerachActivity extends IMBaseActivity implements View.OnClickListener {
    private ImageView mIvFinish;
    private TextView mIvRight;
    private TextView mTvnoContent;
    private EditText mETContent;
    public IMConversationBean groupBean;
    public IMConversationBean customer;
    public LinearLayout mllContent;
    public LinearLayout mllserarch;
    public RecyclerView mRecycle;
    private List<IMConversationDetailBean> beans;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_im_searche_infor);
        IMStatusBarUtil.setLightMode(this);           //设置白底黑字
        IMStatusBarUtil.setColor(this, getResources().getColor(R.color.color_F5F6F7),0);
        initView();
    }

    private void initView() {
        mIvFinish = findViewById(R.id.tv_top_finish);
        mIvRight = findViewById(R.id.tv_top_right);
        mETContent = findViewById(R.id.im_etContent);
        mTvnoContent = findViewById(R.id.im_tv_no_content);
        mRecycle = findViewById(R.id.recycle);
        initRecycle();
        mllContent = findViewById(R.id.llContent);
        mllserarch = findViewById(R.id.ll_serarch);
        mIvFinish.setVisibility(View.VISIBLE);
        mIvRight.setVisibility(View.VISIBLE);
        mIvFinish.setOnClickListener(this);

        mIvRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!TextUtils.isEmpty(mETContent.getText().toString())){
                    mETContent.setText("");
                }else {
                    finish();
                }
            }
        });
        mETContent.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(TextUtils.isEmpty(s)){
                    mTvnoContent.setVisibility(View.GONE);
                    mRecycle.setVisibility(View.GONE);
                    mllContent.setVisibility(View.GONE);
                    mIvRight.setVisibility(View.VISIBLE);
                    mllserarch.setVisibility(View.VISIBLE);
                }else {
                    mllserarch.setVisibility(View.GONE);
                    mllContent.setVisibility(View.VISIBLE);
                    mIvRight.setVisibility(View.VISIBLE);
                    initData();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }
    @SuppressLint("WrongConstant")
    private void initRecycle() {
        LinearLayoutManager layoutManager =  new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager .VERTICAL);
        mRecycle.setLayoutManager(layoutManager);
    }
    private void initData() {
        groupBean=(IMConversationBean)getIntent().getSerializableExtra("group");
        customer=(IMConversationBean)getIntent().getSerializableExtra("customer");
        if(groupBean!=null){
            beans = reSearchContent(groupBean.getConversationId());
        }else if(customer!=null){
            beans =reSearchContent(customer.getConversationId());
        }
        if(beans==null || beans.size()==0){
            mTvnoContent.setVisibility(View.VISIBLE);
            mRecycle.setVisibility(View.GONE);
            return;
        }
        mTvnoContent.setVisibility(View.GONE);
        mRecycle.setVisibility(View.VISIBLE);
        HandlerData(beans);



    }

    /**
     * 搜索查询类容（模糊查询）
     */
    private  List<IMConversationDetailBean> reSearchContent(String conversationId) {
        String content = mETContent.getText().toString();
        List<IMConversationDetailBean> beans =new ArrayList<>();
        List<IMConversationDetailBean> beans1 =  daoUtils.queryConversationDetailDataAccordField(conversationId);
        if(beans1==null ||TextUtils.isEmpty(content)){
            return null;
        }
        if(!TextUtils.isEmpty(content)){
            Pattern pattern = Pattern.compile(content);
            for (int i = 0; i <beans1.size(); i++) {
                if(TextUtils.isEmpty(beans1.get(i).getData())){
                    continue;
                }
                IMMessageDataJson bean = new Gson().fromJson(beans1.get(i).getData(),IMMessageDataJson.class);
                if(TextUtils.isEmpty(bean.getText())){
                    continue;
                }
                Matcher matcher = pattern.matcher(bean.getText());
                if(matcher.find()){
                    beans.add(beans1.get(i));
                }
            }
        }else {
            beans.clear();
        }
        return beans;
    }


    private IMGroupHistoryAdapter adapter;
    private void HandlerData(final List<IMConversationDetailBean> records) {
        if(groupBean!=null) {
            adapter = new IMGroupHistoryAdapter(this, R.layout.layout_im_item_group_history, records,groupBean.getConversationName(),groupBean.getConversationavatar());
        }else {
            adapter = new IMGroupHistoryAdapter(this, R.layout.layout_im_item_group_history, records,customer.getConversationName(),customer.getConversationavatar());
        }
        mRecycle.setAdapter(adapter);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.tv_top_finish) {
            finish();
        }
    }

    /**
     * 点击软键盘外面的区域关闭软键盘
     * @return
     */
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (IMHideInPutUtil.isShouldHideInput(v, ev)) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm != null) {
                    assert v != null;
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                    v.clearFocus();
                }
            }
            return super.dispatchTouchEvent(ev);
        }
        return getWindow().superDispatchTouchEvent(ev) || onTouchEvent(ev);
    }
}
