package com.android.im.imview.dialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.im.R;
import com.android.im.imbean.IMBetDetailBean;
import com.android.im.imbean.IMBetGetBean;
import com.android.im.imbean.IMGameTypeBean;
import com.android.im.imnet.IMHttpResultObserver;
import com.android.im.imnet.http.IMHttpsService;
import com.android.im.imui.activity.IMBetDetailActivity;
import com.android.im.imui.activity.IMGroupChatActivity;
import com.android.nettylibrary.http.IMMessageDataJson;
import com.android.nettylibrary.utils.IMDensityUtil;
import com.android.nettylibrary.utils.IMLogUtil;
import com.android.nettylibrary.utils.IMTimeData;
import com.google.gson.Gson;

import java.util.List;

import okhttp3.MediaType;
import okhttp3.RequestBody;

public class IMShowFollowDiglog implements View.OnClickListener {
    private Context context;
    private AlertDialog followDialog;
    private AlertDialog followResultDialog;
    private ImageView im_iv_gamehead;
    private TextView im_tv_name;
    private TextView im_tv_qh;
    private TextView im_tv_time;
    private EditText im_et_content;
    private TextView im_tv_follow;
    private TextView im_tv_game;
    private TextView im_tv_content;
    private TextView im_tv_tz;
    private TextView im_tv_money;
    private RelativeLayout im_rl_cancle;
    private IMBetDetailBean bean;
    public String moneynumb;
    private CountDownTimer timer;
    private String hours;
    private String minutes;
    private String seconds;

    public IMShowFollowDiglog(Context context) {
        this.context=context;
    }
    /**
     * 展示跟投界面
     */
    public void showFollowBetDiglog(final IMBetDetailBean bean) {
        this.bean=bean;
        AlertDialog.Builder builder=new AlertDialog.Builder(context);
        View dialogView =  View.inflate(context, R.layout.layout_dialog_follow_bet, null);
        im_iv_gamehead = dialogView.findViewById(R.id.im_iv_gamehead);
        im_tv_name = dialogView.findViewById(R.id.im_tv_name);
        im_tv_qh = dialogView.findViewById(R.id.im_tv_qh);
        im_tv_time = dialogView.findViewById(R.id.im_tv_time);
        im_tv_game = dialogView.findViewById(R.id.im_tv_game);
        im_tv_content = dialogView.findViewById(R.id.im_tv_content);
        im_tv_money = dialogView.findViewById(R.id.im_tv_money);
        im_rl_cancle = dialogView.findViewById(R.id.im_rl_cancle);
        im_et_content = dialogView.findViewById(R.id.im_et_content);
        im_tv_follow = dialogView.findViewById(R.id.im_tv_follow);
        im_tv_tz = dialogView.findViewById(R.id.im_tv_tz);
        im_et_content.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(TextUtils.isEmpty(s)){
                    im_tv_tz.setText(bean.getBettingAmount()+"元("+ bean.getBettingMultiples()+"倍)");
                    im_tv_money.setText(bean.getBettingTotalAmount());
                    return;
                }
                double v = Double.parseDouble(s.toString());
                int bettingMultiples = Integer.parseInt(bean.getBettingMultiples());
                double v1 = v / bettingMultiples;
                im_tv_tz.setText(v1+"元("+ bean.getBettingMultiples()+"倍)");
                im_tv_money.setText(s.toString());
            }
        });

        setFollowBetDate(bean);
        im_tv_follow.setOnClickListener(this);
        im_rl_cancle.setOnClickListener(this);
        builder.setView(dialogView);
        followDialog =  builder.show();

        Window window=followDialog.getWindow();
        WindowManager.LayoutParams params = followDialog.getWindow().getAttributes();
        params.width = IMDensityUtil.getScreenWidth(context) - IMDensityUtil.dip2px(context,70);
        window.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.im_shape__bg));
    }

    /**
     * 展示跟投成功界面
     */
    private TextView mtvName;
    private ImageView mivResult;
    private TextView mtvResult;
    private TextView mtvMoney;
    private TextView mtvTime;
    private TextView mivShare;
    private TextView mivFollow;
    private View mView;
    public void showFollowBetResultDiglog(boolean successed) {
        AlertDialog.Builder builder=new AlertDialog.Builder(context);
        View dialogView =  View.inflate(context, R.layout.layout_dialog_follow_result, null);
        mivResult = dialogView.findViewById(R.id.im_iv_result);
        mtvResult = dialogView.findViewById(R.id.im_tv_result);
        mtvName = dialogView.findViewById(R.id.im_tv_name);
        mtvMoney = dialogView.findViewById(R.id.im_tv_money);
        mtvTime = dialogView.findViewById(R.id.im_tv_time);
        mivShare = dialogView.findViewById(R.id.im_iv_share);
        mivFollow = dialogView.findViewById(R.id.im_iv_follow);
        mView = dialogView.findViewById(R.id.im_tv_view);
        setFollowBetResultDate(successed);
        mivFollow.setOnClickListener(this);
        mivShare.setOnClickListener(this);
        builder.setView(dialogView);
        followResultDialog =  builder.show();

        Window window=followResultDialog.getWindow();
        WindowManager.LayoutParams params = followResultDialog.getWindow().getAttributes();
        params.width = IMDensityUtil.getScreenWidth(context) - IMDensityUtil.dip2px(context,50);
        window.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.im_shape__bg));

    }
    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.im_tv_follow){
            if(TextUtils.isEmpty(im_et_content.getText())){
                Toast.makeText(context, "请输入金额", Toast.LENGTH_SHORT).show();
                return;
            }
            int i = Integer.parseInt(im_et_content.getText().toString());
            if(i<1){
                Toast.makeText(context, "金额不能为0", Toast.LENGTH_SHORT).show();
                return;
            }
            followDialog.cancel();
            moneynumb=im_et_content.getText().toString().trim();
            followBetting(bean.getBettingOrderId(), im_et_content.getText().toString().trim());
        }else  if(v.getId()==R.id.im_rl_cancle){
            followDialog.cancel();
        }else  if(v.getId()==R.id.im_iv_share){
            if(mivShare.getText().equals("分享到群")){
                Intent intent=new Intent(context, IMBetDetailActivity.class);
                intent.putExtra("statue","UNSETTLED_ACCOUNTS");
                intent.putExtra("bettingOrderId",bean.getBettingOrderId());
                context.startActivity(intent);
            }else  if(mivShare.getText().equals("继续投注")){
                getFlowData(bean.getBettingOrderId());
            }
            followResultDialog.cancel();
        }else  if(v.getId()==R.id.im_iv_follow){
            if(mivFollow.getText().equals("继续投注")){
                getFlowData(bean.getBettingOrderId());
            }
            followResultDialog.cancel();
        }
    }

    public void getFlowData(String bettingOrderId) {
        final IMBetGetBean bean = new IMBetGetBean();
        bean.setBettingOrderId(bettingOrderId);
        String json = new Gson().toJson(bean);
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json);
        IMHttpsService.getBetDatailJson(body, new IMHttpResultObserver<IMBetDetailBean>() {
            @Override
            public void onSuccess(IMBetDetailBean imBetDetailBean, String message) {
                if (imBetDetailBean == null) {
                    return;
                }

               showFollowBetDiglog(imBetDetailBean);
            }

            @Override
            public void _onError(Throwable e) {
                Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void _onErrorLocal(Throwable e, String message, String code) {
                Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * 展示跟投数据
     */
    private void setFollowBetDate(IMBetDetailBean bean) {
        im_tv_name.setText(bean.getGameName());
        im_tv_qh.setText(bean.getNumber());
//        im_tv_content.setText(bean.getContent());
        im_tv_game.setText(bean.getPlayMethod()+"("+ bean.getAwardNumber()+")");
        im_tv_tz.setText(bean.getBettingAmount()+"("+ bean.getBettingMultiples()+"倍)");
        im_tv_money.setText(bean.getBettingTotalAmount());
        if(TextUtils.isEmpty(bean.getSealingTime())){
            return;
        }
        long time = Math.abs(System.currentTimeMillis() - Long.parseLong(bean.getSealingTime()));
        getCountDownTime(time);
        List<IMGameTypeBean> gameBeans = IMGameTypeBean.getGameBeans();
        for (int i = 0; i < gameBeans.size(); i++) {
            if(gameBeans.get(i).getGameName().equals(bean.getGameName())){
                im_iv_gamehead.setImageResource(gameBeans.get(i).getImageId());
                break;
            }
        }
    }
    /**
     * 展示跟投结果数据
     */
    private void setFollowBetResultDate(boolean successed) {
        if(successed){
            mtvResult.setText("恭喜你，投注成功！");
            mivResult.setImageResource(R.mipmap.im_share_sucess);
            mtvName.setText(bean.getGameName()+":");
            mtvMoney.setText(moneynumb+"元");
            mtvTime.setText("投注时间:"+IMTimeData.stampToTime(System.currentTimeMillis()+"",null));
            mivShare.setText("分享到群");
            mivFollow.setText("继续投注");
            mtvName.setVisibility(View.VISIBLE);
            mtvMoney.setVisibility(View.VISIBLE);
            mtvTime.setVisibility(View.VISIBLE);
            mView.setVisibility(View.VISIBLE);
        }else {
            mtvResult.setText("投注失败");
            mtvName.setVisibility(View.GONE);
            mtvMoney.setVisibility(View.GONE);
            mtvTime.setVisibility(View.GONE);
            mView.setVisibility(View.GONE);
            mivResult.setImageResource(R.mipmap.im_share_fail);
            mivShare.setText("继续投注");
            mivFollow.setText("确定");
        }

    }
    /**
     * 点击跟投
     */
    private void followBetting(String betOrderId, String trim) {
        final IMBetGetBean bean=new IMBetGetBean();
        bean.setGameId(betOrderId);
        bean.setBettingOnlyAmount(trim);
        String json = new Gson().toJson(bean);
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"),json);
        IMLogUtil.d("MyOwnTag:", "跟投参数----" +json);
        ((IMGroupChatActivity)context).showLoadingDialog();
        IMHttpsService.getFollowBetJson(body, new IMHttpResultObserver<String>() {
            @Override
            public void onSuccess(String s, String message) {
                ((IMGroupChatActivity)context).dismissLoadingDialog();
                showFollowBetResultDiglog(true);
            }

            @Override
            public void _onError(Throwable e) {
                ((IMGroupChatActivity)context).dismissLoadingDialog();
                showFollowBetResultDiglog(false);
            }

            @Override
            public void _onErrorLocal(Throwable e, String message, String code) {
                ((IMGroupChatActivity)context).dismissLoadingDialog();
                showFollowBetResultDiglog(false);
            }
        }) ;
    }

    /**
     * 倒计时
     */
    private void getCountDownTime(long timeStemp ) {
        if(timer!=null){
            timer.cancel();
            timer=null;
        }
        timer = new CountDownTimer(timeStemp, 1000) {
            @Override
            public void onTick(long l) {
                long day = l / (1000 * 24 * 60 * 60); //单位天
                long hour = (l - day * (1000 * 24 * 60 * 60)) / (1000 * 60 * 60); //单位时
                long minute = (l - day * (1000 * 24 * 60 * 60) - hour * (1000 * 60 * 60)) / (1000 * 60); //单位分
                long second = (l - day * (1000 * 24 * 60 * 60) - hour * (1000 * 60 * 60) - minute * (1000 * 60)) / 1000;//单位秒
               if(hour<10){
                   hours= "0"+hour;
               }else {
                   hours=hour+"";
               }
                if(minute<10){
                    minutes = "0"+minute;
                }else {
                    minutes =minute+"";
                }
                if(second<10){
                    seconds = "0"+second;
                }else {
                    seconds =second+"";
                }
                im_tv_time.setText(hours + ":" + minutes + ":" + seconds );
            }
            @Override
            public void onFinish() {
                //倒计时为0时执行此方法
                im_tv_time.setText("00:00:00");
                im_tv_follow.setEnabled(false);
                im_tv_follow.setBackground(context.getResources().getDrawable(R.drawable.im_shape_bg_gray6));
            }
        };
        timer.start();
    }
}
