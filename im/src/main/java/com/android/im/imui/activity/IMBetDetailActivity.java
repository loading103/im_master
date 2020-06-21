package com.android.im.imui.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.im.R;
import com.android.im.imbean.IMBetDetailBean;
import com.android.im.imbean.IMBetGetBean;
import com.android.im.imnet.IMHttpResultObserver;
import com.android.im.imnet.http.IMHttpsService;
import com.android.im.imutils.IMStatusBarUtil;
import com.android.im.imview.dialog.IMBetShareDiglog;
import com.android.nettylibrary.utils.IMTimeData;
import com.google.gson.Gson;

import okhttp3.MediaType;
import okhttp3.RequestBody;

/**
 * 注单明细
 */
public class IMBetDetailActivity extends IMBaseActivity implements View.OnClickListener {
    private ImageView mIvBack;
    private TextView mTvBack;
    private TextView mTvTitle;
    private RelativeLayout mRlBack;
    private TextView mTvQs;
    private TextView mTvName;
    private TextView mTvPhone_1;
    private TextView mTvPhone_2;
    private TextView mTvMoney_bet;
    private TextView mTvMoney_single;
    private TextView mTvMoney_win;
    private TextView mTvNubmber;
    private TextView mTvTime;
    private TextView mTvState;
    private TextView mTvYk;
    private TextView mTvShare;
    private String bettingOrderId;
    private IMBetDetailBean bean;
    private String statue;//投注状态： UNSETTLED_ACCOUNTS:未结算 NOT_WINNING_THE_PRIZE:未中奖 HAS_WON_THE_PRIZE:已中奖	是	[string]
    private IMBetShareDiglog shareDiglog;
    private boolean isReadCard;
    private boolean isFollowCard;
    private TextView mTvBuy;
    private TextView mTvBuyContent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_im_bet_detail);
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
        mTvBack.setVisibility(View.GONE);
        mTvBack.setText(getResources().getString(R.string.im_back));
        mTvTitle.setText(getResources().getString(R.string.im_bet_details));
        mRlBack.setOnClickListener(this);
        //主内容
        mTvQs = findViewById(R.id.im_tv_qs);
        mTvName = findViewById(R.id.im_tv_wf);
        mTvPhone_1 = findViewById(R.id.im_tv_phone_1);
        mTvPhone_2 = findViewById(R.id.im_tv_phone_2);
        mTvNubmber = findViewById(R.id.im_tv_number);
        mTvMoney_single = findViewById(R.id.im_tv_money_single);
        mTvMoney_bet = findViewById(R.id.im_tv_money_bet);
        mTvMoney_win = findViewById(R.id.im_tv_money_win);
        mTvTime = findViewById(R.id.im_tv_time);
        mTvState = findViewById(R.id.im_tv_state);
        mTvYk = findViewById(R.id.im_tv_yk);
        mTvShare = findViewById(R.id.im_tv_share);
        mTvBuy= findViewById(R.id.im_tv_buy);
        mTvBuyContent= findViewById(R.id.im_tv_buycontent);
        mTvShare.setOnClickListener(this);
    }

    private void initData() {
        shareDiglog = new IMBetShareDiglog(this);
        isReadCard= getIntent().getBooleanExtra("readcard",true);
        isFollowCard= getIntent().getBooleanExtra("followcard",true);
        statue = getIntent().getStringExtra("statue");
        bettingOrderId = getIntent().getStringExtra("bettingOrderId");
        if (statue.equals("NOT_WINNING_THE_PRIZE")) {
            mTvShare.setVisibility(View.GONE);
        } else {
            mTvShare.setVisibility(View.VISIBLE);
        }
        getHttdata(bettingOrderId);
    }

    /**
     * 请求数据
     */
    private IMBetDetailBean beans;

    private void getHttdata(String bettingOrderId) {
        final IMBetGetBean bean = new IMBetGetBean();
        bean.setBettingOrderId(bettingOrderId);
        String json = new Gson().toJson(bean);
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json);
        showLoadingDialog();
        IMHttpsService.getBetDatailJson(body, new IMHttpResultObserver<IMBetDetailBean>() {
            @Override
            public void onSuccess(IMBetDetailBean imBetDetailBean, String message) {
                dismissLoadingDialog();
                if (imBetDetailBean == null) {
                    return;
                }
                beans = imBetDetailBean;
                handleHttpData(imBetDetailBean);
            }

            @Override
            public void _onError(Throwable e) {
                dismissLoadingDialog();
                Toast.makeText(IMBetDetailActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void _onErrorLocal(Throwable e, String message, String code) {
                dismissLoadingDialog();
                Toast.makeText(IMBetDetailActivity.this, message, Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * 处理数据
     */
    private void handleHttpData(IMBetDetailBean imBetDetailBean) {
        bean = imBetDetailBean;
        mTvQs.setText(bean.getNumber() == null ? "" : bean.getNumber());
        mTvName.setText(bean.getPlayMethod() == null ? "" : bean.getPlayMethod());
        mTvPhone_1.setText(bean.getGameId() == null ? "" : bean.getGameId());
        mTvPhone_2.setText(TextUtils.isEmpty(bean.getLotteryNumber()) ? "待开奖" : bean.getLotteryNumber());
        mTvNubmber.setText(bean.getBettingNumber() == null ? "" : bean.getBettingNumber());
        mTvMoney_single.setText(bean.getBettingAmount() == null ? "" : bean.getBettingAmount());
        mTvMoney_win.setText(bean.getMediumBonus() == null ? "" : bean.getMediumBonus());
        mTvMoney_bet.setText(bean.getBettingTotalAmount() == null ? "" : bean.getBettingTotalAmount());
        mTvTime.setText(bean.getTradeTime() == null ? "" : IMTimeData.stampToTime(bean.getTradeTime(), "yyyy-MM-dd HH:mm"));
        mTvYk.setText(bean.getProfitAndLoss() == null ? "" : bean.getProfitAndLoss());
        if (!TextUtils.isEmpty(bean.getPlayMethod()) && !TextUtils.isEmpty(bean.getGameId())) {
            mTvBuyContent.setText(bean.getPlayMethod() + "(" + bean.getAwardNumber() + ")");
        }
        if (!TextUtils.isEmpty(bean.getBettingAmount()) && !TextUtils.isEmpty(bean.getBettingNumber())){
            mTvBuy.setText("每单" + bean.getBettingAmount() + "元，共" + bean.getBettingNumber() + "单");
        }
        //投注状态： UNSETTLED_ACCOUNTS:未结算 NOT_WINNING_THE_PRIZE:未中奖 HAS_WON_THE_PRIZE:已中奖
        if (bean.getStatus().equals("UNSETTLED_ACCOUNTS")) {
            mTvState.setText("未结算");
        } else if (bean.getStatus().equals("NOT_WINNING_THE_PRIZE")) {
            mTvState.setText("未中奖");
        } else if (bean.getStatus().equals("HAS_WON_THE_PRIZE")) {
            mTvState.setText("已中奖");
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.rl_top_finish) {
            finish();
        } else if (v.getId() == R.id.im_tv_share) {
            if(statue.equals("HAS_WON_THE_PRIZE")){
                if(isReadCard){
                    shareDiglog.showShareDiglog(bean, beans);
                }else {
                    Toast.makeText(this, "你还没有红单分享的权限", Toast.LENGTH_SHORT).show();
                }
            }else if(statue.equals("UNSETTLED_ACCOUNTS")){
                if(isFollowCard){
                    shareDiglog.showShareDiglog(bean, beans);
                }else {
                    Toast.makeText(this, "你还没有注单分享的权限", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
}

