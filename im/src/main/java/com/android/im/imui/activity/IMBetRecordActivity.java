package com.android.im.imui.activity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.im.R;
import com.android.im.imadapter.IMBetListAdapter;
import com.android.im.imadapter.IMPopWindowStateAdapter;
import com.android.im.imadapter.IMPopWindowTypeAdapter;
import com.android.im.imadapter.baseadapter.IMBaseRecycleViewAdapter_T;
import com.android.im.imbean.IMBetGetBean;
import com.android.im.imbean.IMBetListBean;
import com.android.im.imbean.IMBettingOrders;
import com.android.im.imbean.IMGameStatusBean;
import com.android.im.imbean.IMGameTypeBean;
import com.android.im.imeventbus.IMMessagShareEnsureEvevt;
import com.android.im.imnet.IMHttpResultObserver;
import com.android.im.imnet.http.IMHttpsService;
import com.android.im.imutils.IMImageLoadUtil;
import com.android.im.imutils.IMSmoothScrollLayoutManager;
import com.android.im.imutils.IMStatusBarUtil;
import com.android.nettylibrary.utils.IMDensityUtil;
import com.android.nettylibrary.utils.IMLogUtil;
import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.OnTimeSelectChangeListener;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.TimePickerView;
import com.google.gson.Gson;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.RequestBody;

import static android.widget.GridLayout.VERTICAL;

/**
 * 投注记录
 */
public class IMBetRecordActivity extends IMBaseActivity implements View.OnClickListener, IMBaseRecycleViewAdapter_T.OnItemClickListner {
    private ImageView   mIvBack;
    private TextView    mTvBack;
    private TextView    mTvTitle;
    private RelativeLayout mRlBack;
    private ImageView   mIvHead;
    private TextView    mTvName;
    private TextView    mTvMoney;
    private RelativeLayout mRlRefresh;
    private ImageView   mIvRefresh;

    private TextView    mTvState_1;
    private TextView    mTvState_2;
    private TextView    mTvMonth;
    private TextView    mTvNoContent;
    private TextView    mTvDay;
    private ImageView   mIvState_1;
    private ImageView   mIvState_2;
    private ImageView   mIvTime;
    private SmartRefreshLayout mRefreshLayout;
    private RecyclerView mRecycleView;
    private LinearLayout mllContain;
    private RelativeLayout mrlContain;

    private TimePickerView pvTime;
    private IMBetListAdapter mAdapter;
    private RelativeLayout mRlSure;
    private RelativeLayout mRlCancle;
    private RelativeLayout mRlType;
    private RelativeLayout mRlState;
    private int page=1;
    private int pageSize=15;
    private RecyclerView recyclerView;
    private List<IMBettingOrders>mlist=new ArrayList<>();
    private List<IMGameTypeBean> typeBeans=new ArrayList<>();
    private List<IMGameStatusBean> statueBeans=new ArrayList<>();;
    private IMPopWindowStateAdapter statueAdapter;
    private IMPopWindowTypeAdapter typeAdapter;
    private IMGameStatusBean statueBean;
    private IMGameTypeBean typeBean;
    private static  final int TYPE_STATE=1;
    private static  final int TYPE_TYPE=2;
    private int type;
    private int stateposition;//用来状态类型选中之后点击确认保存的位置
    private int typeposition;   //用来记录类型选中之后点击确认保存的位置
    private int currentposition;  //用来记录类型选中之后没有点击确认情况的位置
    private boolean isReadCard;
    private boolean isFollowCard;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_im_bet_record);
        IMStatusBarUtil.setColor(this,getResources().getColor(R.color.color_05A9FE),0);
        EventBus.getDefault().register(this);
        initView();
        initData();
    }

    private void initView() {
        //状态栏
        mIvBack=findViewById(R.id.iv_top_finish);
        mTvBack=findViewById(R.id.tv_top_finish);
        mTvTitle=findViewById(R.id.tv_top_title);
        mRlBack=findViewById(R.id.rl_top_finish);
        mTvBack.setVisibility(View.GONE);
        mTvBack.setText(getResources().getString(R.string.im_back));
        mTvTitle.setText(getResources().getString(R.string.im_bet_record));
        mRlBack.setOnClickListener(this);
        //主内容
        mIvHead=findViewById(R.id.im_iv_head);
        mTvName=findViewById(R.id.im_tv_name);
        mTvMoney=findViewById(R.id.im_tv_money);
        mTvNoContent=findViewById(R.id.im_tv_no_content);
        mRlRefresh=findViewById(R.id.im_rl_refresh);
        mIvRefresh=findViewById(R.id.im_iv_refresh);
        mTvState_1=findViewById(R.id.im_tv_state_1);
        mTvState_2=findViewById(R.id.im_tv_state_2);
        mIvState_1=findViewById(R.id.im_iv_state_1);
        mIvState_2=findViewById(R.id.im_iv_state_2);
        mTvMonth=findViewById(R.id.im_tv_month);
        mTvDay=findViewById(R.id.im_tv_day);
        mIvTime=findViewById(R.id.im_iv_time);
        mRefreshLayout=findViewById(R.id.refreshLayout);
        mRecycleView=findViewById(R.id.recycle);
        recyclerView = findViewById(R.id.im_recycle);
        mRlSure = findViewById(R.id.im_rl_ensure);
        mRlCancle = findViewById(R.id.im_rl_cancle);
        mllContain = findViewById(R.id.ll_im_container);
        mrlContain = findViewById(R.id.rl_im_container);

        mRlType = findViewById(R.id.im_rl_type);
        mRlState = findViewById(R.id.im_rl_state);
        mRlRefresh.setOnClickListener(this);
        mRlType.setOnClickListener(this);
        mRlState.setOnClickListener(this);
        mTvMonth.setOnClickListener(this);
        mTvDay.setOnClickListener(this);
        mIvTime.setOnClickListener(this);
        mRlSure.setOnClickListener(this);
        mRlCancle.setOnClickListener(this);
        isReadCard= getIntent().getBooleanExtra("readcard",true);
        isFollowCard= getIntent().getBooleanExtra("followcard",true);
        initRecycle();
        RefreshHandView();
    }
    @SuppressLint("WrongConstant")
    private void initRecycle() {
        //主内容
        IMSmoothScrollLayoutManager layoutManager =  new IMSmoothScrollLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecycleView.setLayoutManager(layoutManager);
        //状态和类型的内容
        GridLayoutManager gridLayoutManager=new GridLayoutManager(this,3);
        gridLayoutManager.setOrientation(VERTICAL);
        recyclerView.setLayoutManager(gridLayoutManager);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    /**
     * 获取网络数据
     */
    private void initData() {
        showLoadingDialog();
        initTimePicker();                     //时间选择器初始化
        getAllGameType();
        changeInitDayView();//类型数据初始化
        statueBeans = IMGameStatusBean.getdata(); //状态数据初始化
        getBetListData(true,false);//第一个刷新是代表下拉刷新，第二个是代表是不是点击刷新按钮刷新
    }
    /**
     * 上拉下拉刷新数据
     */
    private void RefreshHandView() {
        mRefreshLayout.setEnableRefresh(true);//是否启用下拉刷新功能
        mRefreshLayout.setEnableLoadMore(true);//是否启用上拉加载功能
        mRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                page=1;
                getBetListData(true,false);
            }
        });
        mRefreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                page=page+1;
                getBetListData(false,false);
            }
        });
    }
    /**
     * 获取主类容数据（第一个刷新是代表下拉刷新，第二个是代表是不是点击刷新按钮刷新）
     */
    private String statebean;
    private String typebean;
    private String timebean="1";//默认为1表示本月数据
    private String starttime;
    private void getBetListData(final boolean isRefresh,final boolean Refresh) {
        final IMBetGetBean bean=new IMBetGetBean();
        bean.setPageNo(page+"");
        bean.setPageSize(pageSize+"");
        bean.setState(statebean==null?"":statebean);
        bean.setType(typebean==null?"":typebean);
        if(timebean!=null){
            bean.setTime(timebean);
        }
        if(!TextUtils.isEmpty(starttime)){
            bean.setStartTime(starttime);
            bean.setEndTime(starttime);
        }
        String json = new Gson().toJson(bean);
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"),json);
        IMLogUtil.d("MyOwnTag:", "IMBetRecordActivity " +"(获取主类容数据上传参数) " +json);

        IMHttpsService.getBetListJson(body, new IMHttpResultObserver<IMBetListBean>() {
            @Override
            public void onSuccess(IMBetListBean imBetListBeans, String message) {
                dismissLoadingDialog();
                if(Refresh){
                    mIvRefresh.clearAnimation();
                    Toast.makeText(IMBetRecordActivity.this, "刷新成功", Toast.LENGTH_SHORT).show();
                }
                IMLogUtil.d("MyOwnTag:", "IMBetRecordActivity " +"(onSuccess)-- " +new Gson().toJson(imBetListBeans));
                if(isRefresh){
                    mRefreshLayout.finishRefresh();//传入false表示刷新失败
                }else {
                    mRefreshLayout.finishLoadMore();//传入false表示刷新失败
                }
                if(imBetListBeans!=null && isRefresh){
                    mTvName.setText(imBetListBeans.getNickName());
                    mTvMoney.setText(imBetListBeans.getLotteryBalance()==null?"余额：0元":"余额："+imBetListBeans.getLotteryBalance()+"元");
                    IMImageLoadUtil.ImageLoadCircle(IMBetRecordActivity.this,imBetListBeans.getAvatar(),mIvHead);
                }
                HandlerListBean(imBetListBeans.getBettingOrders(),isRefresh);
            }

            @Override
            public void _onError(Throwable e) {
                dismissLoadingDialog();
                Toast.makeText(IMBetRecordActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void _onErrorLocal(Throwable e, String message, String code) {
                dismissLoadingDialog();
                Toast.makeText(IMBetRecordActivity.this, message, Toast.LENGTH_SHORT).show();
            }
        });
    }
    /**
     * 处理list数据（上拉下拉）
     */
    private void HandlerListBean(List<IMBettingOrders> bean, boolean isrefresh) {
        if(isrefresh  && (bean==null    ||  bean.size()==0)){   //第一次无数据
            mTvNoContent.setVisibility(View.VISIBLE);
            mRefreshLayout.setVisibility(View.GONE);
            return;
        }
        mRefreshLayout.setVisibility(View.VISIBLE);
        if(!isrefresh  && (bean==null || bean.size()==0)){   //上啦无数据
            mRefreshLayout.setNoMoreData(true);
            return;
        }
        if(isrefresh){
            mlist.clear();
            mlist.addAll(bean);
            if(mlist.size()==0){
                mTvNoContent.setVisibility(View.VISIBLE);
                mRefreshLayout.setVisibility(View.GONE);
                return;
            }
            mRefreshLayout.setVisibility(View.VISIBLE);
            mTvNoContent.setVisibility(View.GONE);
            mAdapter = new IMBetListAdapter(this, R.layout.item_im_bet_list,  mlist);
            mRecycleView.setAdapter(mAdapter);
            mAdapter.setOnItemClickListner(this);
        }else {
            mlist.addAll(bean);
            if(mlist.size()==0){
                mTvNoContent.setVisibility(View.VISIBLE);
                mRefreshLayout.setVisibility(View.GONE);
                return;
            }
            mRefreshLayout.setVisibility(View.VISIBLE);
            mTvNoContent.setVisibility(View.GONE);
            mAdapter.notifyDataSetChanged();
        }
    }

    /**
     * 获取所有的游戏类型
     */
    private void getAllGameType() {
        typeBeans = IMGameTypeBean.getGameBeans();
        if(typeBeans==null){
            return;
        }
        for (int i = 0; i < typeBeans.size(); i++) {
            if(i==0){
                typeBeans.get(i).setChooseed(true);
            }else {
                typeBeans.get(i).setChooseed(false);
            }
        }
    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.rl_top_finish){
            finish();
        }else if(v.getId()==R.id.im_rl_refresh){
            startanim();
            getBetListData(true,true);
        }else if(v.getId()==R.id.im_rl_state){
            //点击状态
            type=TYPE_STATE;
            OnClickStateAndType(mTvState_1,mIvState_1,TYPE_STATE);
        }else if(v.getId()==R.id.im_rl_type){
            //点击类型
            type=TYPE_TYPE;
            OnClickStateAndType(mTvState_2,mIvState_2,TYPE_TYPE);
        }else if(v.getId()==R.id.im_tv_month){
            //点击当月
            changeMonthView();
        }else if(v.getId()==R.id.im_tv_day){
            //点击当天
            changeDayView();
        }else if(v.getId()==R.id.im_iv_time){
            pvTime.show(v);
        }else if(v.getId()==R.id.im_rl_cancle){
            clickCancleView();
        }else if(v.getId()==R.id.im_rl_ensure) {
            page=1;
            clickEnsureView();
        }
    }



    /**
     * 点击状态和类型的按钮
     */
    private int lasttype=-1;  //用于点击第二次收回
    private void OnClickStateAndType(TextView mTvState,ImageView mIvState, int type) {
        mTvState.setTextColor(getResources().getColor(R.color.color_31B4FF));
        mIvState.setImageResource(R.mipmap.im_share_jiaobiao_top);
        if(mllContain.getVisibility()==View.VISIBLE && lasttype==type ){
            clickCancleView();
            return;
        }
        mllContain.setVisibility(View.VISIBLE);
        lasttype=type;
        if(type==TYPE_STATE){    //点击状态选择
            ViewGroup.LayoutParams lp=mrlContain.getLayoutParams();
            lp.height= IMDensityUtil.dip2px(this,120);
            mrlContain.setLayoutParams(lp);
            mTvState_2.setTextColor(getResources().getColor(R.color.color_333333));
            mIvState_2.setImageResource(R.mipmap.im_bt_jiaobiao_buttom);
            for (int i = 0; i < statueBeans.size(); i++) {
                if(i==stateposition){
                    statueBeans.get(i).setChooseed(true);
                }else {
                    statueBeans.get(i).setChooseed(false);
                }
            }
            statueAdapter = new IMPopWindowStateAdapter(this,R.layout.item_im_pop_state,statueBeans);
            statueAdapter.setOnItemClickListner(new IMBaseRecycleViewAdapter_T.OnItemClickListner() {
                @Override
                public void onItemClickListner(View v, int position, Object t) {
                    for (int i = 0; i < statueBeans.size(); i++) {
                        if(position==i){
                            statueBeans.get(i).setChooseed(true);
                            currentposition=position;
                        }else {
                            statueBeans.get(i).setChooseed(false);
                        }
                    }
                    statueBean= statueBeans.get(position);
                    statueAdapter.notifyDataSetChanged();
                    page=1;
                    clickEnsureView();
                }
            });
            recyclerView.setAdapter(statueAdapter);
        }else {   //点击状态选择
            ViewGroup.LayoutParams lp=mrlContain.getLayoutParams();
            lp.height= ViewGroup.LayoutParams.MATCH_PARENT ;
            mrlContain.setLayoutParams(lp);
            mTvState_1.setTextColor(getResources().getColor(R.color.color_333333));
            mIvState_1.setImageResource(R.mipmap.im_bt_jiaobiao_buttom);
            for (int i = 0; i < typeBeans.size(); i++) {
                if(i==typeposition){
                    typeBeans.get(i).setChooseed(true);
                }else {
                    typeBeans.get(i).setChooseed(false);
                }
            }
            typeAdapter = new IMPopWindowTypeAdapter(this,R.layout.item_im_pop_state,typeBeans);
            typeAdapter.setOnItemClickListner(new IMBaseRecycleViewAdapter_T.OnItemClickListner() {
                @Override
                public void onItemClickListner(View v, int position, Object t) {
                    for (int i = 0; i < typeBeans.size(); i++) {
                        if(position==i){
                            currentposition=position;
                            typeBeans.get(i).setChooseed(true);
                        }else {
                            typeBeans.get(i).setChooseed(false);
                        }
                    }
                    typeBean= typeBeans.get(position);
                    typeAdapter.notifyDataSetChanged();
                    page=1;
                    clickEnsureView();
                }
            });
            recyclerView.setAdapter(typeAdapter);
        }
    }
    /**
     * 点击当天时间筛选:1-本月,2-今日	是	[string]
     */

    private void changeDayView() {
        timebean="2";
        page=1;
        starttime=null;
        mTvMonth.setBackground(getResources().getDrawable(R.drawable.im_shape_bg_white12));
        mTvMonth.setTextColor(getResources().getColor(R.color.color_999999));
        mTvDay.setBackground(getResources().getDrawable(R.drawable.im_shape_bg_blue12));
        mTvDay.setTextColor(getResources().getColor(R.color.white));
        showLoadingDialog();
        getBetListData(true,false);
    }

    private void changeInitDayView() {
        timebean="2";
        page=1;
        starttime=null;
        mTvMonth.setBackground(getResources().getDrawable(R.drawable.im_shape_bg_white12));
        mTvMonth.setTextColor(getResources().getColor(R.color.color_999999));
        mTvDay.setBackground(getResources().getDrawable(R.drawable.im_shape_bg_blue12));
        mTvDay.setTextColor(getResources().getColor(R.color.white));
        getBetListData(true,false);
    }

    /**
     * 点击当月
     */
    private void changeMonthView() {
        timebean="1";
        page=1;
        starttime=null;
        mTvDay.setBackground(getResources().getDrawable(R.drawable.im_shape_bg_white12));
        mTvDay.setTextColor(getResources().getColor(R.color.color_999999));
        mTvMonth.setBackground(getResources().getDrawable(R.drawable.im_shape_bg_blue12));
        mTvMonth.setTextColor(getResources().getColor(R.color.white));
        showLoadingDialog();
        getBetListData(true,false);
    }

    /**
     * 点击取消
     */
    private void clickCancleView() {
        type=0;
        mTvState_1.setTextColor(getResources().getColor(R.color.color_333333));
        mIvState_1.setImageResource(R.mipmap.im_bt_jiaobiao_buttom);
        mTvState_2.setTextColor(getResources().getColor(R.color.color_333333));
        mIvState_2.setImageResource(R.mipmap.im_bt_jiaobiao_buttom);
        mllContain.setVisibility(View.GONE);
    }
    /**
     * 点击确认
     */
    private void clickEnsureView() {
        mTvState_1.setTextColor(getResources().getColor(R.color.color_333333));
        mIvState_1.setImageResource(R.mipmap.im_bt_jiaobiao_buttom);
        mTvState_2.setTextColor(getResources().getColor(R.color.color_333333));
        mIvState_2.setImageResource(R.mipmap.im_bt_jiaobiao_buttom);
        mllContain.setVisibility(View.GONE);
        if(type==TYPE_STATE){
            stateposition=currentposition;
            statebean=statueBean.getStatusId();
            showLoadingDialog();
            getBetListData(true,false);
            mTvState_1.setText("状态("+statueBean.getStatusName()+")");
        }else {
            typeposition=currentposition;
            typebean=typeBean.getGameId();
            showLoadingDialog();
            getBetListData(true,false);
            mTvState_2.setText("类型("+typeBean.getGameName()+")");
        }
        type=0;
    }
    /**
     * 开起刷新动画
     */

    private void startanim() {
        Animation rotateAnimation = AnimationUtils.loadAnimation(this, R.anim.im_refresh_rote);
        LinearInterpolator interpolator = new LinearInterpolator();
        rotateAnimation.setInterpolator(interpolator);
        rotateAnimation.setFillAfter(true);
        mIvRefresh.startAnimation(rotateAnimation);
    }
    /**
     * 时间选择器
     */
    private void initTimePicker() {//Dialog 模式下，在底部弹出
        pvTime = new TimePickerBuilder(this, new OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {
                SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
                String format = sdf.format(date);
                mTvMonth.setBackground(getResources().getDrawable(R.drawable.im_shape_bg_white12));
                mTvMonth.setTextColor(getResources().getColor(R.color.color_999999));
                mTvDay.setBackground(getResources().getDrawable(R.drawable.im_shape_bg_white12));
                mTvDay.setTextColor(getResources().getColor(R.color.color_999999));
                starttime=format;
                timebean=null;
                showLoadingDialog();
                page=1;
                getBetListData(true,false);
            }
        })
                .setTimeSelectChangeListener(new OnTimeSelectChangeListener() {
                    @Override
                    public void onTimeSelectChanged(Date date) {
                    }
                })
                .setType(new boolean[]{true, true, true, false, false, false})
                .isDialog(true) //默认设置false ，内部实现将DecorView 作为它的父控件。
                .addOnCancelClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                    }
                })
                .build();
        Dialog mDialog = pvTime.getDialog();
        if (mDialog != null) {
            FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    Gravity.BOTTOM);
            params.leftMargin = 0;
            params.rightMargin = 0;
            pvTime.getDialogContainerLayout().setLayoutParams(params);
            Window dialogWindow = mDialog.getWindow();
            if (dialogWindow != null) {
                dialogWindow.setWindowAnimations(com.bigkoo.pickerview.R.style.picker_view_slide_anim);//修改动画样式
                dialogWindow.setGravity(Gravity.BOTTOM);//改成Bottom,底部显示
                dialogWindow.setDimAmount(0.1f);
            }
        }
    }

    @Override
    public void onItemClickListner(View v, int position, Object t) {
        Intent intent=new Intent(this,IMBetDetailActivity.class);
        intent.putExtra("bettingOrderId",mlist.get(position).getBettingOrderId());
        intent.putExtra("statue",mlist.get(position).getStatus());
        intent.putExtra("readcard",isReadCard);
        intent.putExtra("followcard",isFollowCard);
        startActivity(intent);
    }

    /**
     * 分享成功之后通知关闭这个界面
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(IMMessagShareEnsureEvevt event) {
        finish();
    }
}
