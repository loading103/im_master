package com.android.im.imui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.im.R;
import com.android.im.imbean.IMBetGetBean;
import com.android.im.imbean.IMRedPickInformationBean;
import com.android.im.imnet.IMHttpResultObserver;
import com.android.im.imnet.http.IMHttpsService;
import com.android.im.imutils.IMImageLoadUtil;
import com.android.im.imutils.IMStatusBarUtil;
import com.android.nettylibrary.IMSConfig;
import com.android.nettylibrary.greendao.entity.IMPersonBean;
import com.android.nettylibrary.utils.IMPreferenceUtil;
import com.android.nettylibrary.utils.IMTimeData;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.RequestBody;

/**
 * 红包详情
 */
public class IMSingleRedPickDetailActivity extends IMBaseActivity implements View.OnClickListener {
    private TextView mTvRight;
    private RelativeLayout mRlBack;
    private TextView mTvPlay;
    private TextView mTvDec;
    private LinearLayout mllContain;
    private LinearLayout mllType2;
    private ImageView mIvavterType;
    private TextView mTvNameType;
    private String   packetId;
    private IMPersonBean customer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_im_sigle_red_detail);
        IMStatusBarUtil.setColor(this, getResources().getColor(R.color.color_f25442), 0);
        initView();
        initData();
    }

    private void initView() {
        //状态栏
        mTvRight = findViewById(R.id.tv_top_right);
        mRlBack = findViewById(R.id.rl_top_finish);
        mRlBack.setOnClickListener(this);
        mTvRight.setOnClickListener(this);
        //内容
        mTvPlay = findViewById(R.id.im_tv_play);
        mTvDec = findViewById(R.id.im_tv_dec);
        mTvRight = findViewById(R.id.tv_top_right);
        mllContain = findViewById(R.id.im_ll_container);
        mllType2 = findViewById(R.id.im_ll_type2);
        mIvavterType = findViewById(R.id.im_iv_head_type);
        mTvNameType = findViewById(R.id.im_tv_name_type);
    }

    private IMRedPickInformationBean   data;
    private void initData() {
        data = (IMRedPickInformationBean)getIntent().getSerializableExtra("bean");
        customer=(IMPersonBean)getIntent().getSerializableExtra("customer");
        packetId=getIntent().getStringExtra("dataJson");
        if(data!=null){
            initDataView(data);
        }else {
            getRedPickHttpData();
        }
    }

    /**
     * 请求数据
     *
     */
    public void  getRedPickHttpData(){
        final IMBetGetBean bean = new IMBetGetBean();
        bean.setPacketId(packetId);
        String json = new Gson().toJson(bean);
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json);
        showLoadingDialog();
        IMHttpsService.getRedStatePickJson(body, new IMHttpResultObserver<IMRedPickInformationBean>() {
            @Override
            public void onSuccess(IMRedPickInformationBean imBetDetailBean, String message) {
                dismissLoadingDialog();
                if (imBetDetailBean == null) {
                    return;
                }
                initDataView(imBetDetailBean);
            }

            @Override
            public void _onError(Throwable e) {
                dismissLoadingDialog();
                Toast.makeText(IMSingleRedPickDetailActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void _onErrorLocal(Throwable e, String message, String code) {
                dismissLoadingDialog();
                Toast.makeText(IMSingleRedPickDetailActivity.this,message, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initDataView(IMRedPickInformationBean bean) {
        mTvNameType.setText(bean.getNickName()+"的红包");
        mTvPlay.setText(bean.getTitle());
        IMImageLoadUtil.ImageLoadCircle(this,bean.getAvatar(),mIvavterType);
        hanleTime(bean);
        if(bean.getState().equals("1")){
            HandlerDataView(bean);
        }
    }



    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.rl_top_finish) {
            finish();
        }else if(v.getId() == R.id.tv_top_right) {
            Intent intent =new Intent(this, IMRedPickRecordActivity.class);
            startActivity(intent);
        }
    }

    /**
     * 处理数据源
     */
    private void HandlerDataView(IMRedPickInformationBean bean) {
        View view= LayoutInflater.from(this).inflate(R.layout.item_im_red_list,null);
        ImageView mIvHead = view.findViewById(R.id.im_iv_head);
        TextView mTvName= view.findViewById(R.id.im_tv_name);
        TextView mTvTime= view.findViewById(R.id.im_tv_time);
        TextView mTvZj= view.findViewById(R.id.im_tv_zj);
        TextView mTvMoney= view.findViewById(R.id.im_tv_money);
        if(data==null){
            String url = IMPreferenceUtil.getPreference_String(IMSConfig.SAVE_HEADURL, "");
            String name = IMPreferenceUtil.getPreference_String(IMSConfig.SAVE_NAME, "");
            IMImageLoadUtil.ImageHeadLoadUrl(IMSingleRedPickDetailActivity.this,url,mIvHead);
            mTvName.setText(name);
        }else {
            IMImageLoadUtil.ImageHeadLoadUrl(IMSingleRedPickDetailActivity.this,customer.getAvatar(),mIvHead);
            mTvName.setText(customer.getNickName());
        }
        mTvTime.setText(IMTimeData.stampToTime(bean.getCreateTime()+"","MM-dd"));
        mTvZj.setVisibility(View.GONE);
        mTvMoney.setText(bean.getAmount()+"元");
        mllContain.addView(view);
    }


    /**
     * 处理抢了几个红包
     */
    private void hanleTime(IMRedPickInformationBean bean) {
        if(bean.getState().equals("1")){
            mTvDec.setText("一个红包共"+bean.getAmount()+"元");
        }else{
            mTvDec.setText("红包金额"+bean.getAmount()+"元,等待对方领取。");
        }
    }
}

