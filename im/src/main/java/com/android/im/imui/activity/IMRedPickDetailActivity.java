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

import com.android.im.IMSManager;
import com.android.im.R;
import com.android.im.imbean.IMBetGetBean;
import com.android.im.imbean.IMRedPickInformationBean;
import com.android.im.imnet.IMBaseConstant;
import com.android.im.imnet.IMHttpResultObserver;
import com.android.im.imnet.http.IMHttpsService;
import com.android.im.imutils.IMImageLoadUtil;
import com.android.im.imutils.IMStatusBarUtil;
import com.android.nettylibrary.utils.IMTimeData;
import com.google.gson.Gson;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.RequestBody;

/**
 * 红包详情
 */
public class IMRedPickDetailActivity extends IMBaseActivity implements View.OnClickListener {
    private TextView mTvRight;
    private RelativeLayout mRlBack;

    private ImageView mIvavter;
    private TextView mTvName;
    private TextView mTvContent;
    private TextView mTvMoney;
    private TextView mTvPlay;
    private TextView mTvDec;
    private LinearLayout mllContain;
    private LinearLayout mllTop;
    private LinearLayout mllType1;
    private LinearLayout mllType2;
    private ImageView mIvavterType;
    private TextView mTvNameType;
    private List<IMRedPickInformationBean.RecordList> data;

    private String   packetId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_im_red_detail);
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
        mIvavter = findViewById(R.id.im_iv_avatar);
        mTvName = findViewById(R.id.im_tv_name);
        mTvContent = findViewById(R.id.im_tv_content);
        mTvMoney = findViewById(R.id.im_tv_money);
        mTvPlay = findViewById(R.id.im_tv_play);
        mTvDec = findViewById(R.id.im_tv_dec);
        mTvRight = findViewById(R.id.tv_top_right);
        mllContain = findViewById(R.id.im_ll_container);
        mllTop = findViewById(R.id.ll_im_top);
        mllType1 = findViewById(R.id.im_ll_type1);
        mllType2 = findViewById(R.id.im_ll_type2);
        mIvavterType = findViewById(R.id.im_iv_head_type);
        mTvNameType = findViewById(R.id.im_tv_name_type);
        mTvPlay.setOnClickListener(this);
    }

    private Boolean   isOutData=false;
    private void initData() {
        packetId=getIntent().getStringExtra("dataJson");
        getRedPickHttpData();
    }

    /**
     * 请求数据
     */
    public void  getRedPickHttpData(){
        final IMBetGetBean bean = new IMBetGetBean();
        bean.setPacketId(packetId);
        String json = new Gson().toJson(bean);
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json);
        showLoadingDialog();
        IMHttpsService.getLastRedStatePickJson(body, new IMHttpResultObserver<IMRedPickInformationBean>() {
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
                Toast.makeText(IMRedPickDetailActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void _onErrorLocal(Throwable e, String message, String code) {
                dismissLoadingDialog();
                Toast.makeText(IMRedPickDetailActivity.this, message, Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void initDataView(IMRedPickInformationBean bean) {
        if(bean.getSelfRecord()!=null){               //自己抢了红包
            mTvMoney.setText(bean.getSelfRecord().getGrabAmount());
            mllType1.setVisibility(View.VISIBLE);
            mllTop.setVisibility(View.VISIBLE);
            mllType2.setVisibility(View.GONE);
        }else {
            mllType1.setVisibility(View.GONE);
            mllTop.setVisibility(View.GONE);
            mllType2.setVisibility(View.VISIBLE);
            mTvNameType.setText(bean.getNickName()+"的红包");
        }
        IMImageLoadUtil.ImageLoadCircle(this,bean.getAvatar(),mIvavter);
        mTvName.setText(bean.getNickName()+"的红包");
        mTvContent.setText(bean.getTitle());
        hanleTime(bean);
        data = bean.getRecordList();
        HandlerDataView(bean);

    }



    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.rl_top_finish) {
            finish();
        }else if(v.getId() == R.id.tv_top_right) {
            Intent intent =new Intent(this, IMRedPickRecordActivity.class);
            startActivity(intent);
        }else if(v.getId() ==  R.id.im_tv_play) {
            IMSManager.getInstance().addModuleClickListener(IMBaseConstant.IM_CLICK_RED_PICK_GAME);
        }

    }

    /**
     * 处理数据源
     */
    private void HandlerDataView(IMRedPickInformationBean bean) {
        if(data==null){
            return;
        }
        List<IMRedPickInformationBean.RecordList> datas = data;

        Collections.sort(data, new Comparator<IMRedPickInformationBean.RecordList>(){
            public int compare(IMRedPickInformationBean.RecordList o1, IMRedPickInformationBean.RecordList o2) {
                //按照学生的年龄进行升序排列
                if(Double.parseDouble(o1.getGrabAmount()) > Double.parseDouble(o2.getGrabAmount())){
                    return -1;
                }
                if(Double.parseDouble(o1.getGrabAmount())==Double.parseDouble(o2.getGrabAmount())){
                    return 0;
                }
                return 1;
            }
        });

        for (int i = 0; i < datas.size(); i++) {
            View view= LayoutInflater.from(this).inflate(R.layout.item_im_red_list,null);
            ImageView mIvHead = view.findViewById(R.id.im_iv_head);
            TextView mTvName= view.findViewById(R.id.im_tv_name);
            TextView mTvTime= view.findViewById(R.id.im_tv_time);
            TextView mTvZj= view.findViewById(R.id.im_tv_zj);
            TextView mTvMoney= view.findViewById(R.id.im_tv_money);

            if( bean.getRecordList().size()==bean.getNumber()&& datas.get(i).getCustomerId().equals(data.get(0).getCustomerId())){
                mTvZj.setVisibility(View.VISIBLE);
            }else {
                mTvZj.setVisibility(View.GONE);
            }
            IMImageLoadUtil.ImageLoadCircle(IMRedPickDetailActivity.this,datas.get(i).getAvatar(),mIvHead);
            mTvName.setText(datas.get(i).getNickName());
            mTvTime.setText(IMTimeData.stampToTime(datas.get(i).getGrabTime()+"","MM-dd"));
            mTvMoney.setText(datas.get(i).getGrabAmount()+"元");
            mllContain.addView(view);
        }
    }


    /**
     * 处理抢了几个红包
     */
    private void hanleTime(IMRedPickInformationBean bean) {
        if(bean.isComplete()){   //抢完了
            long endTime = bean.getEndTime();
            long createTime = bean.getCreateTime();
            if(endTime-createTime>0){
                long[] a = IMTimeData.getDistanceTimes(endTime + "", createTime + "");
                if(a[0]!=0){
                    mTvDec.setText(bean.getNumber()+"个红包,"+a[0]+"天抢完");
                }else if(a[0]==0 && a[1]!=0){
                    mTvDec.setText(bean.getNumber()+"个红包,"+a[1]+"小时"+a[2]+"分钟抢完");
                }else if(a[0]==0 && a[1]==0&&a[2]!=0){
                    mTvDec.setText(bean.getNumber()+"个红包,"+a[2]+"分"+a[3]+"秒抢完");
                }else if(a[0]==0 && a[1]==0&&a[2]==0&&a[3]!=0){
                    mTvDec.setText(bean.getNumber()+"个红包,"+a[3]+"秒抢完");
                }

            }else {
                mTvDec.setText(bean.getNumber()+"个红包已抢完");
            }
        }else {
            double a=0.00;
            for (int i = 0; i < bean.getRecordList().size(); i++) {
                double v = Double.parseDouble(bean.getRecordList().get(i).getGrabAmount());
                a=a+v;
            }
            mTvDec.setText("领取"+bean.getRecordList().size()+"/"+bean.getNumber()+"个，共"+ String.format("%.2f", a)+"/"+ bean.getAmount()+"元");
        }
    }

}

