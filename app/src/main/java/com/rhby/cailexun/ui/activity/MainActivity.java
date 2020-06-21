package com.rhby.cailexun.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckedTextView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.airbnb.lottie.LottieAnimationView;
import com.android.im.IMSHttpManager;
import com.android.im.IMSManager;
import com.android.im.imadapter.IMMyViewPagerAdapter;
import com.android.im.imbean.IMClxTeamMessageBean;
import com.android.im.imbean.IMFriendsBean;
import com.android.im.imeventbus.IMContactReshLastEvent;
import com.android.im.imeventbus.IMGoToFirstFragmentEvent;
import com.android.im.imeventbus.IMLoginTimeOutEvent;
import com.android.im.imeventbus.IMMessageNoticeContentEvent;
import com.android.im.imeventbus.IMNewFriendUnReadNumber;
import com.android.im.imeventbus.IMNumberEvent;
import com.android.im.imeventbus.IMPushMessageEvent;
import com.android.im.imeventbus.MainUpdateEvent;
import com.android.im.iminterface.IMManegerInterface;
import com.android.im.imnet.IMHttpResultObserver;
import com.android.im.imnet.http.IMHttpsService;
import com.android.im.imui.fragment.IMConversationFragment;
import com.android.im.imui.fragment.IMFindFragment;
import com.android.im.imutils.IMStatusBarUtil;
import com.android.im.imview.IMViewPagerSlide;
import com.android.im.imview.dialog.IMDialogUtils;
import com.android.im.imview.dialog.IMPushMessageDiglog;
import com.rhby.cailexun.R;
import com.rhby.cailexun.event.MessageEvent;
import com.rhby.cailexun.ui.base.BaseActivity;
import com.rhby.cailexun.ui.fragment.ContactListFragment;
import com.rhby.cailexun.ui.fragment.NewMineFragment;
import com.rhby.cailexun.utils.NotifityUtils;
import com.android.nettylibrary.IMSConfig;
import com.android.nettylibrary.bean.IMMessageBean;
import com.android.nettylibrary.utils.IMPreferenceUtil;
import com.google.gson.Gson;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import cn.jpush.android.api.JPushInterface;
import q.rorbin.badgeview.Badge;
import q.rorbin.badgeview.QBadgeView;

/**
 * @author jinxin
 * 剑之所指，心之所向
 * @date 2019/8/4
 */
public class MainActivity extends BaseActivity implements ViewPager.OnPageChangeListener, OnClickListener, IMManegerInterface {
    @BindView(R.id.viewpager)
    IMViewPagerSlide mViewPager;
    @BindView(R.id.tabs)
    LinearLayout layout;
    @BindView(R.id.iv_tab1)
    LottieAnimationView ivTab1;
    @BindView(R.id.iv_tab2)
    LottieAnimationView ivTab2;
    @BindView(R.id.iv_tab3)
    LottieAnimationView ivTab3;
    @BindView(R.id.iv_tab4)
    LottieAnimationView ivTab4;
    @BindView(R.id.ll_red)
    LinearLayout llRed;
    @BindView(R.id.ll_lin)
    View ll_lin;

    private IMMyViewPagerAdapter mAdapter;
    //    private CheckedTextView[] mCheckedTextViews;
    private LinearLayout[] mLinearLayout;
    private List<Fragment> mFragments = new ArrayList<>();
    public static final int IM_MESSAGE_RELOGIN = 10001;   //账号
    public static final int IM_MESSAGE_UNREAD_NUMBER = 10002;//跟新本地未读消息数目
    private IMFindFragment findFragment;
    private IMConversationFragment imConversationFragment;
    private int iconumber;
    private Badge badge,badge1;
    private IMPushMessageDiglog pushMessageDiglog;
    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case IM_MESSAGE_RELOGIN:
                    IMDialogUtils.getInstance().showReLoginDialog(MainActivity.this,"你的账号已经在其他地方登录，请重新登录", new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            IMDialogUtils.getInstance().dismissCommonDiglog();
                            IMSManager.getInstance().ImChatLoginOut();
                            JPushInterface.deleteAlias(MainActivity.this,1);
                            startActivity(new Intent(MainActivity.this, LoginNewActivity.class));
                            finish();
                        }
                    }, new OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            IMDialogUtils.getInstance().dismissCommonDiglog();
                            IMSHttpManager.getInstance().regetImToken(false,true);

                        }
                    });
                    break;
                case IM_MESSAGE_UNREAD_NUMBER:
                    iconumber = Integer.parseInt((String) msg.obj);
                    if(iconumber>99){
                        badge.setBadgeText("99+");
                    }else {
                        badge.setBadgeNumber(iconumber);
                    }
                    NotifityUtils.getInstance().updateCorner(iconumber,"");
                    break;
            }
            return false;
        }
    });

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        IMStatusBarUtil.setTranslucentForImageViewInFragment(this, 0, null);
        IMStatusBarUtil.setLightMode(this);
        EventBus.getDefault().register(this);
        initBadge();

        pushMessageDiglog=new IMPushMessageDiglog(this);
        setSwipeBackEnable(false);
        mViewPager.setCanScrollble(false);
        imConversationFragment = new IMConversationFragment();
        findFragment = new IMFindFragment();
        mFragments.add(imConversationFragment);
        mFragments.add(new ContactListFragment());
        mFragments.add(findFragment);
        mFragments.add(new NewMineFragment());
        mAdapter = new IMMyViewPagerAdapter(getSupportFragmentManager(), mFragments);
        mViewPager.setAdapter(mAdapter);
        mViewPager.setOnPageChangeListener(this);
        mViewPager.setOffscreenPageLimit(4);
        //让其在主activity中调用（重复登录，点击module的监听）
        IMSManager.getInstance().setManegerInterface(this);
        //im中获取未读数目
        IMSManager.getInstance().getUnreadMessageNumber();
        initTabs();
        getNewFriendData();
        boolean isNeedNotice = IMPreferenceUtil.getPreference_Boolean(IMSConfig.SAVE_FIRST_NOTICE, false);
        if(!NotifityUtils.getInstance().isNotificationEnabled()  && !isNeedNotice ){
            IMDialogUtils.getInstance().showOpenNoticeDialog(MainActivity.this,new OnClickListener() {
                @Override
                public void onClick(View v) {
                    IMDialogUtils.getInstance().dismissCommonDiglog();
                    boolean isNeedNotice = IMPreferenceUtil.getPreference_Boolean(IMSConfig.SAVE_FIRST_NOTICE, true);
                    NotifityUtils.getInstance().openNotice();
                }
            });
        }
    }

    private void initBadge() {
        badge = new QBadgeView(this).bindTarget(findViewById(R.id.unreadnumber));
        badge.setBadgeGravity(Gravity.CENTER | Gravity.END);
        badge.setBadgeTextSize(10, true);
        badge.setBadgePadding(4, true);
        badge.setOnDragStateChangedListener(new Badge.OnDragStateChangedListener() {
            @Override
            public void onDragStateChanged(int dragState, Badge badge, View targetView) {
                if (dragState == STATE_SUCCEED) {
                    IMSManager.getInstance().getUnreadMessageNumber();
                }
            }
        });
        badge1 = new QBadgeView(this).bindTarget(findViewById(R.id.addnumber));
        badge1.setBadgeGravity(Gravity.CENTER | Gravity.END);
        badge1.setBadgeTextSize(10, true);
        badge1.setBadgePadding(4, true);
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(MessageEvent event) {
        if (event.getMsg().equals("exit_login")) {
            finish();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMainUpdateEvent(MainUpdateEvent event) {
        if (event.getMsg().equals("1")) {
            layout.setVisibility(View.VISIBLE);
            llRed.setVisibility(View.VISIBLE);
            ll_lin.setVisibility(View.VISIBLE);
        }else {
            layout.setVisibility(View.GONE);
            llRed.setVisibility(View.GONE);
            ll_lin.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        handler.removeCallbacksAndMessages(null);
    }

    private void initTabs() {
        setCheckPosition(0);
        mLinearLayout = new LinearLayout[layout.getChildCount()];
        for (int i = 0; i < layout.getChildCount(); i++) {
            mLinearLayout[i] = (LinearLayout) layout.getChildAt(i);
            mLinearLayout[i].setOnClickListener(this);
        }
    }

    @Override
    public void onPageScrollStateChanged(int arg0) {

    }

    @Override
    public void onPageScrolled(int position, float offset, int offsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        if (position == mLastIndex) {
            return;
        }
        ((CheckedTextView)mLinearLayout[position].getChildAt(1)).setChecked(true);
        ((CheckedTextView)mLinearLayout[mLastIndex].getChildAt(1)).setChecked(false);
        mLastIndex = position;
    }

    private int mLastIndex;

    @Override
    public void onClick(View v) {
        for (int i = 0; i < mLinearLayout.length; i++) {
            if (v == mLinearLayout[i]) {
                if (i == mLastIndex) {
                    break;
                }
                ((CheckedTextView)mLinearLayout[i].getChildAt(1)).setChecked(true);
                ((CheckedTextView)mLinearLayout[mLastIndex].getChildAt(1)).setChecked(false);
                mLastIndex = i;
                mViewPager.setCurrentItem(i, false);
                if (i == 1) {
                    EventBus.getDefault().post(new IMContactReshLastEvent());
                }
                //为了使首页返回编辑前的状态
                if(i!=0){
                    if(imConversationFragment!=null){
                        imConversationFragment.changeChooseStated();
                    }
                }
                setCheckPosition(i);
                startAnimation(i);
                break;
            }
        }
    }

    /**
     * 设置选定状态
     * @param position
     */
    private void setCheckPosition(int position) {
        switch (position){
            case 0:
                ivTab2.setImageResource(R.mipmap.im_main_contact);
                ivTab3.setImageResource(R.mipmap.im_main_find);
                ivTab4.setImageResource(R.mipmap.im_main_mine);
                break;
            case 1:
                ivTab1.setImageResource(R.mipmap.nav_icon_huihua_n);
                ivTab3.setImageResource(R.mipmap.im_main_find);
                ivTab4.setImageResource(R.mipmap.im_main_mine);
                break;
            case 2:
                ivTab1.setImageResource(R.mipmap.nav_icon_huihua_n);
                ivTab2.setImageResource(R.mipmap.im_main_contact);
                ivTab4.setImageResource(R.mipmap.im_main_mine);
                break;
            case 3:
                ivTab1.setImageResource(R.mipmap.nav_icon_huihua_n);
                ivTab2.setImageResource(R.mipmap.im_main_contact);
                ivTab3.setImageResource(R.mipmap.im_main_find);
                break;
        }
    }
    /**
     * 动画
     * @param position
     */
    private void startAnimation(int position) {
        switch (position){
            case 0:
                ivTab1.setAnimation("huihuajson.json");
                ivTab1.playAnimation();

                break;
            case 1:
                ivTab2.setAnimation("txunl.json");
                ivTab2.playAnimation();
                break;
            case 2:
                ivTab3.setAnimation("faxian.json");
                ivTab3.playAnimation();
                break;
            case 3:
                ivTab4.setAnimation("mine.json");
                ivTab4.playAnimation();
                break;
        }
    }

    /**
     * 处理重复登录的问题
     */
    @Override
    public void setOnUserReLogin() {
        handler.sendEmptyMessage(IM_MESSAGE_RELOGIN);
    }

    /**
     * 获取角标下的未读消息数
     */
    @Override
    public void getUnReadnumber(String number) {
        Message msg = Message.obtain();
        msg.obj = number;
        msg.what = IM_MESSAGE_UNREAD_NUMBER;
        handler.sendMessage(msg);
    }

    /**
     * 进入其他界面。默认返回到会话界面
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(final IMGoToFirstFragmentEvent event) {
        setChooseConverFragment();
    }
    /**
     * 系统推送
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(final IMMessageNoticeContentEvent event) {
        String message = event.getMessage();
        if(!TextUtils.isEmpty(message)){
            if(mLastIndex!=0 ||   NotifityUtils.getInstance().isRunBackground()){
                NotifityUtils.getInstance().updateCorner(iconumber,message);
            }
        }
    }
    /**
     * 进入其他界面。默认返回到会话界面
     */
    public void setChooseConverFragment() {
        if (mLinearLayout == null || mLinearLayout.length == 0) {
            return;
        }
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mLinearLayout[0].performClick();
            }
        }, 1500);
    }
    /**
     * 进入新的朋友界面，刷新当前界面
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(final IMNewFriendUnReadNumber event) {
        List<IMFriendsBean> datas = event.getDatas();
        if(datas==null){   //==null的时候代表有人申请加你好友   收到系统消息
            getNewFriendData();
        }else {
            handleDatas(datas);
        }
    }
    /**
     *你已被举报(被封号)
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(final IMPushMessageEvent event) {
        IMMessageBean message =event.getContent();
        if(message==null || message.getData()==null){
            return;
        }
        getNewFriendData(message);
    }
    /**
     *你登录超时或是未登录
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(final IMLoginTimeOutEvent event) {
        IMDialogUtils.getInstance().showReLoginDialog(MainActivity.this,"登录已超时或未登录，请重新登录", new OnClickListener() {
            @Override
            public void onClick(View v) {
                IMDialogUtils.getInstance().dismissCommonDiglog();
                IMSManager.getInstance().ImChatLoginOut();
                finish();
            }
        }, new OnClickListener() {
            @Override
            public void onClick(View v) {
                IMDialogUtils.getInstance().dismissCommonDiglog();
                IMSManager.getInstance().ImChatLoginOut();
                JPushInterface.deleteAlias(MainActivity.this,1);
                startActivity(new Intent(MainActivity.this, LoginNewActivity.class));
                finish();

            }
        });
    }

    /**
     * 显示封号弹窗
     */
    private void getNewFriendData(IMMessageBean message) {
        try {
            IMClxTeamMessageBean   dataJson = new Gson().fromJson(message.getData(), IMClxTeamMessageBean.class);
            pushMessageDiglog.showPersonalDiglog(dataJson.getMeta().getMap().getMsgTitle(), dataJson.getMeta().getMap().getMsgContent(), new OnClickListener() {
                @Override
                public void onClick(View v) {
                    pushMessageDiglog.dissmissPushDiglog();
                    IMSManager.getInstance().ImChatLoginOut();
                    JPushInterface.deleteAlias(MainActivity.this,1);
                    startActivity(new Intent(MainActivity.this, LoginNewActivity.class));
                    finish();
                }
            });
        }catch (Exception e){
            return;
        }
    }

    /**
     * 获取是不是有新的朋友加你
     */
    private void getNewFriendData() {
        IMHttpsService.getFriendApplyJson(new IMHttpResultObserver<List<IMFriendsBean>>() {
            @Override
            public void onSuccess(List<IMFriendsBean> datas, String message) {
                dismissLoadingDialog();
                if(datas==null ||datas.size()==0){
                    return;
                }
                handleDatas(datas);
            }
            @Override
            public void _onError(Throwable e) {
                dismissLoadingDialog();
                Toast.makeText(MainActivity.this,e.getMessage(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void _onErrorLocal(Throwable e, String message, String code) {
                dismissLoadingDialog();
                Toast.makeText(MainActivity.this,message, Toast.LENGTH_SHORT).show();
            }
        });
    }
    //申请状态:1=等待通过，2=通过，3=拒绝,4=已过期
    private void handleDatas(List<IMFriendsBean> datas) {
        int number=0;
        String data =IMPreferenceUtil.getPreference_String(IMSConfig.SAVE_NEW_FRIEND, "-1");
        if(data.equals("-1")){ //表示第一次
            for (int i = 0; i < datas.size(); i++) {
                if(datas.get(i).getApplyStatus().equals("1")){
                    number=number+1;
                }
                if(number==0){
                    badge1.setBadgeNumber(number);
                    EventBus.getDefault().post(new IMNumberEvent(0));
                }else {
                    badge1.setBadgeNumber(number);
                    //通知通讯录里面我的朋友旁边的未读消息数
                    EventBus.getDefault().post(new IMNumberEvent(number));
                }
            }
        }else {
            int numb = Integer.parseInt(data);
            for (int i = 0; i < datas.size(); i++) {
                number=number+1;
            }
            if(number>numb){
                badge1.setBadgeNumber(number-numb);
                //通知通讯录里面我的朋友旁边的未读消息数
                EventBus.getDefault().post(new IMNumberEvent(number-numb));
            }else {
                badge1.setBadgeNumber(0);
                EventBus.getDefault().post(new IMNumberEvent(0));
            }
        }

    }
    private long firstTime = 0;
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        twoCilckFinish(keyCode);
        return true;
    }

    private void twoCilckFinish(int keyCode) {
        long secondTime = System.currentTimeMillis();
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if(mViewPager.getCurrentItem()==0){
                if(imConversationFragment.isShowHeader()){
                    imConversationFragment.resetHeader();
                }else {
                    if ( secondTime - firstTime < 2000) {
                        finish();
                    } else {
                        Toast.makeText(MainActivity.this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
                        firstTime = System.currentTimeMillis();
                    }
                }
            }else {
                if ( secondTime - firstTime < 2000) {
                    finish();
                } else {
                    Toast.makeText(MainActivity.this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
                    firstTime = System.currentTimeMillis();
                }
            }
        }
    }
}