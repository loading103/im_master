package com.android.im.imui.activity;

import android.graphics.Color;
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
import com.android.im.imbean.IMSendRedPickRecordList;
import com.android.im.imnet.IMHttpResultObserver;
import com.android.im.imnet.http.IMHttpsService;
import com.android.im.imutils.IMImageLoadUtil;
import com.android.im.imutils.IMStatusBarUtil;
import com.android.im.imview.impromptlibrary.IMPromptButton;
import com.android.im.imview.impromptlibrary.IMPromptButtonListener;
import com.android.im.imview.impromptlibrary.IMPromptDialog;
import com.android.nettylibrary.utils.IMLogUtil;
import com.android.nettylibrary.utils.IMTimeData;
import com.google.gson.Gson;

import java.util.List;

import okhttp3.MediaType;
import okhttp3.RequestBody;

/**
 * 红包记录
 */
public class IMRedPickRecordActivity extends IMBaseActivity implements View.OnClickListener {
    private ImageView mIvBack;
    private ImageView mIvRight;
    private TextView mTvBack;
    private TextView mTvTitle;
    private RelativeLayout mRlBack;
    private LinearLayout mllType;
    private TextView mTvName;
    private TextView mTvMoney;
    private ImageView mIvavter;
    private TextView mTvRedNumber;
    private TextView mTvZjNumber;
    private TextView   mTvState;
    private TextView   mTvSendNumber;
    private LinearLayout mllContain;
    private List<IMSendRedPickRecordList.IMRedPacketList> data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_im_red_record);
        IMStatusBarUtil.setColor(this, getResources().getColor(R.color.color_05A9FE), 0);
        initView();
        initData();
    }

    private void initView() {
        //状态栏
        mIvBack = findViewById(R.id.iv_top_finish);
        mTvBack = findViewById(R.id.tv_top_finish);
        mTvTitle = findViewById(R.id.tv_top_title);
        mRlBack = findViewById(R.id.rl_top_finish);
        mIvRight=findViewById(R.id.iv_top_right);
        mIvRight.setImageResource(R.mipmap.im_chat_more);
        mIvRight.setVisibility(View.VISIBLE);
        mTvBack.setVisibility(View.VISIBLE);
        mTvBack.setText("收到的红包");
        mRlBack.setOnClickListener(this);
        mIvRight.setOnClickListener(this);
        //主内容
        mIvavter = findViewById(R.id.im_iv_avatar);
        mTvName = findViewById(R.id.im_tv_name);
        mTvMoney = findViewById(R.id.im_tv_money);
        mTvRedNumber = findViewById(R.id.im_tv_number_red);
        mTvZjNumber = findViewById(R.id.im_tv_number_zj);
        mllContain = findViewById(R.id.im_ll_container);
        mTvSendNumber = findViewById(R.id.im_tv_send_number);
        mllType = findViewById(R.id.im_ll_type);
    }
    private void initData() {
        getRedPickListHttpData();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.rl_top_finish) {
            finish();
        }else if(v.getId()== R.id.iv_top_right){
            showTakePhotoListView(new IMPromptButtonListener() {
                @Override
                public void onClick(IMPromptButton button) {
                    getSendListHttpData();
                }
            }, new IMPromptButtonListener() {
                @Override
                public void onClick(IMPromptButton button) {
                    getRedPickListHttpData();
                }
            });
        }
    }
    /**
     * 请求数据(发送的红包列表)
     */
    private void getSendListHttpData() {
        showLoadingDialog();
        final IMBetGetBean bean = new IMBetGetBean();
        bean.setPageNo("1");
        bean.setPageSize("1000");
        String json = new Gson().toJson(bean);
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json);
        IMHttpsService.getSendRedPackJson(body, new IMHttpResultObserver<IMSendRedPickRecordList.IMSendRedPickRecordListBean>() {
            @Override
            public void onSuccess(IMSendRedPickRecordList.IMSendRedPickRecordListBean datas, String message) {
                dismissLoadingDialog();
                IMLogUtil.d("MyOwnTag:", "黄金圣斗士 " +"发送红包数==="+new Gson().toJson(bean));
                if (datas == null  ) {
                    return;
                }
                data=datas.getRedPacketList();
                HandlerDataView(true,datas);
            }

            @Override
            public void _onError(Throwable e) {
                dismissLoadingDialog();
            }

            @Override
            public void _onErrorLocal(Throwable e, String message, String code) {
                dismissLoadingDialog();
            }
        });

    }
    /**
     * 请求数据(用户领取的红包列表)
     */
    private void getRedPickListHttpData() {
        showLoadingDialog();
        final IMBetGetBean bean = new IMBetGetBean();
        bean.setPageNo("1");
        bean.setPageSize("1000");
        String json = new Gson().toJson(bean);
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json);
        IMHttpsService.getGetRedPackJson(body, new IMHttpResultObserver<IMSendRedPickRecordList.IMSendRedPickRecordListBean>() {
            @Override
            public void onSuccess(IMSendRedPickRecordList.IMSendRedPickRecordListBean datas, String message) {
                dismissLoadingDialog();
                if (datas == null ) {
                    return;
                }
                data=datas.getRedPacketList();
                HandlerDataView(false,datas);

            }

            @Override
            public void _onError(Throwable e) {
                dismissLoadingDialog();
                Toast.makeText(IMRedPickRecordActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void _onErrorLocal(Throwable e, String message, String code) {
                dismissLoadingDialog();
            }
        });
    }
    /**
     * 处理数据源
     */
    private void HandlerDataView(boolean issend, IMSendRedPickRecordList.IMSendRedPickRecordListBean bean) {
        mllContain.removeAllViews();
        if(data==null || data.size()==0){
            if(issend) {
                mTvMoney.setText("0.00");
                mTvSendNumber.setText("发出红包总数" + 0+ "个");
                mTvName.setText(bean.getNickName() + "共发出");
                IMImageLoadUtil.ImageLoadCircle(this, bean.getAvatar(), mIvavter);
                mTvBack.setText("发出的红包");
                mllType.setVisibility(View.GONE);
                mTvSendNumber.setVisibility(View.VISIBLE);
            }else {
                mTvMoney.setText("0.00");
                mTvRedNumber.setText("0");
                mTvZjNumber.setText("0");
                mTvName.setText(bean.getNickName()+"共收到");
                IMImageLoadUtil.ImageLoadCircle(this,bean.getAvatar(),mIvavter);
                mTvBack.setText("收到的红包");
                mllType.setVisibility(View.VISIBLE);
                mTvSendNumber.setVisibility(View.GONE);
            }
            return;
        }
        for (int i = 0; i < data.size(); i++) {
            View view= LayoutInflater.from(this).inflate(R.layout.item_im_record_list,null);
            TextView mTvName= view.findViewById(R.id.im_tv_name);
            TextView mTvTime= view.findViewById(R.id.im_tv_time);
            TextView mTvZj= view.findViewById(R.id.im_tv_zj);
            TextView mTvMoney= view.findViewById(R.id.im_tv_money);
            TextView  mTvState = view.findViewById(R.id.im_tv_state);
            ImageView mIvPin = view.findViewById(R.id.im_iv_pin);
            if(issend){
                mTvState.setVisibility(View.VISIBLE);
                if(data.get(i).getGradNumber()==data.get(i).getTotalNumber()){
                    mTvState.setText(data.get(i).getGradNumber()+"/"+data.get(i).getTotalNumber()+"个");
                }else {
                    long l = data.get(i).getCreateTime() + 3600 * 24 * 1000;
                    if(l<System.currentTimeMillis()){
                        mTvState.setText("已过期"+data.get(i).getGradNumber()+"/"+data.get(i).getTotalNumber()+"个");
                    }else {
                        mTvState.setText(data.get(i).getGradNumber()+"/"+data.get(i).getTotalNumber()+"个");
                    }
                }
                mTvMoney.setText((data.get(i).getAmount()*1.0f)/100+"元");
                mTvTime.setText(IMTimeData.stampToTime(data.get(i).getCreateTime()+"","yyyy-MM-dd HH:mm"));
                if(data.get(i).getType().equals("2")){
                    mIvPin.setVisibility(View.VISIBLE);
                    mTvName.setText("拼手气红包");
                }else {
                    mIvPin.setVisibility(View.INVISIBLE);
                    mTvName.setText("普通红包");
                }
            }else {
                mTvState.setVisibility(View.GONE);
                mTvName.setText(data.get(0).getNickName());
                mTvMoney.setText(data.get(i).getGrabAmount()+"元");
                mTvTime.setText(IMTimeData.stampToTime(data.get(i).getGrabTime()+"","yyyy-MM-dd HH:mm"));
                if(data.get(i).getType().equals("2")){
                    mIvPin.setVisibility(View.VISIBLE);
                }else {
                    mIvPin.setVisibility(View.INVISIBLE);
                }
            }
            mllContain.addView(view);
        }
        if(issend){
            mTvMoney.setText(bean.getSendTotalAmount());
            mTvSendNumber.setText("发出红包总数"+bean.getSendNumber()+"个");
            mTvName.setText(bean.getNickName()+"共发出");
            IMImageLoadUtil.ImageLoadCircle(this,bean.getAvatar(),mIvavter);

            mTvBack.setText("发出的红包");
            mllType.setVisibility(View.GONE);
            mTvSendNumber.setVisibility(View.VISIBLE);

        }else {
            mTvMoney.setText(bean.getAcceptTotalAmount()+"");
            mTvRedNumber.setText(bean.getAcceptNumber()+"");
            mTvName.setText(bean.getNickName()+"共收到");
            mTvZjNumber.setText(bean.getAcceptBestNumber()+"");
            IMImageLoadUtil.ImageLoadCircle(this,bean.getAvatar(),mIvavter);

            mTvBack.setText("收到的红包");
            mllType.setVisibility(View.VISIBLE);
            mTvSendNumber.setVisibility(View.GONE);
        }

    }
    public void showTakePhotoListView(IMPromptButtonListener listener1, IMPromptButtonListener listener2){
        //创建对象
        IMPromptDialog IMPromptDialog = new IMPromptDialog(this);
        IMPromptButton cancle = new IMPromptButton("取消", null);
//        cancle.setTextColor(Color.parseColor("#0076ff"));
        //设置自定义属性
        IMPromptDialog.getDefaultBuilder().touchAble(true).round(3).loadingDuration(3000);
        IMPromptDialog.showAlertSheet("", true, cancle,new IMPromptButton("发出的红包", listener1), new IMPromptButton("收到的红包", listener2));
    }
}

