package com.android.im.imui.fragment;

import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.im.IMSHttpManager;
import com.android.im.IMSManager;
import com.android.im.IMSMsgManager;
import com.android.im.R;
import com.android.im.imadapter.IMConversationnListAdapter;
import com.android.im.imadapter.baseadapter.IMBaseRecycleViewAdapter_T;
import com.android.im.imbean.IMBetGetBean;
import com.android.im.imbean.IMForBitBean;
import com.android.im.imbean.IMPersonalBean;
import com.android.im.imbean.RecentlyUseBean;
import com.android.im.imbean.SmallProgramBean;
import com.android.im.imeventbus.IMAddCollectionEvent;
import com.android.im.imeventbus.IMAddFriendSucessEvent;
import com.android.im.imeventbus.IMAgreeAddFriendSucessEvent;
import com.android.im.imeventbus.IMCancerCollectionEvent;
import com.android.im.imeventbus.IMCreatMessageEvent;
import com.android.im.imeventbus.IMDeleteFriendSucessEvent;
import com.android.im.imeventbus.IMDeleteGroupEvent;
import com.android.im.imeventbus.IMDeleteRecentEvent;
import com.android.im.imeventbus.IMDeleteShowEvent;
import com.android.im.imeventbus.IMGroupMessageEvent;
import com.android.im.imeventbus.IMMessageDeteAlllEvevt;
import com.android.im.imeventbus.IMMessageDetelGroupEvevt;
import com.android.im.imeventbus.IMMessageNoticeContentEvent;
import com.android.im.imeventbus.IMMessageNoticelGroupEvevt;
import com.android.im.imeventbus.IMMessageNoticelPersonEvevt;
import com.android.im.imeventbus.IMNewFriendUnReadNumber;
import com.android.im.imeventbus.IMPushMessageEvent;
import com.android.im.imeventbus.IMRegetGroupKickOutEvent;
import com.android.im.imeventbus.IMRegetGroupListEvent;
import com.android.im.imeventbus.IMSendGroupMessageEvent;
import com.android.im.imeventbus.IMUpdataGroupInforEvent;
import com.android.im.imeventbus.MainUpdateEvent;
import com.android.im.imeventbus.UpdateLatestEvent;
import com.android.im.imnet.IMHttpResultObserver;
import com.android.im.imnet.http.IMHttpsService;
import com.android.im.imui.activity.IMAddFriendActivity;
import com.android.im.imui.activity.IMCreatGroupActivity;
import com.android.im.imui.activity.IMGroupChatActivity;
import com.android.im.imui.activity.IMPersonalChatActivity;
import com.android.im.imui.activity.IMQRCActivity;
import com.android.im.imutils.IMStopClickFast;
import com.android.im.imview.MyConversationHeadView;
import com.android.im.imview.dialog.IMDialogUtils;
import com.android.im.imview.impopwindow.IMCustomPopWindow;
import com.android.im.imview.impopwindow.IMPopWindowUtil;
import com.android.im.imwedgit.MyRelativeLayout;
import com.android.im.imwedgit.pullextend.ExtendLayout;
import com.android.im.imwedgit.pullextend.ExtendListHeader;
import com.android.im.imwedgit.pullextend.IExtendLayout;
import com.android.im.imwedgit.pullextend.PullExtendLayout;
import com.android.nettylibrary.IMSConfig;
import com.android.nettylibrary.bean.IMMessageBean;
import com.android.nettylibrary.bean.IMMessageReceiveEvent;
import com.android.nettylibrary.bean.IMSendingStateBean;
import com.android.nettylibrary.greendao.entity.IMConversationBean;
import com.android.nettylibrary.greendao.entity.IMConversationDetailBean;
import com.android.nettylibrary.greendao.entity.IMGroupBean;
import com.android.nettylibrary.greendao.entity.IMPersonBean;
import com.android.nettylibrary.greendao.greenutils.DaoUtils;
import com.android.nettylibrary.http.IMGetIMMemberData;
import com.android.nettylibrary.http.IMGroupNoticeBean;
import com.android.nettylibrary.http.IMMessagSystemJson;
import com.android.nettylibrary.http.IMMessageDataJson;
import com.android.nettylibrary.http.IMMessageKictOutJson;
import com.android.nettylibrary.http.IMPersonBeans;
import com.android.nettylibrary.http.IMSystemNoGroupBean;
import com.android.nettylibrary.http.IMSystemSomeOneAddGroup;
import com.android.nettylibrary.http.IMUserInforBean;
import com.android.nettylibrary.protobuf.MessageProtobuf;
import com.android.nettylibrary.utils.IMLogUtil;
import com.android.nettylibrary.utils.IMPreferenceUtil;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ScreenUtils;
import com.blankj.utilcode.util.SizeUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.MediaType;
import okhttp3.RequestBody;

import static com.android.im.imadapter.IMConversationnListAdapter.CLICK_TYPE_DELETE;
import static com.android.im.imadapter.IMConversationnListAdapter.CLICK_TYPE_NOTICE;
import static com.android.im.imadapter.IMConversationnListAdapter.CLICK_TYPE_SEARCH;
import static com.android.im.imadapter.IMConversationnListAdapter.CLICK_TYPE_TOP;
import static com.android.im.imnet.IMBaseConstant.JSON;


public class IMConversationFragment extends IMBaseFragment implements IMBaseRecycleViewAdapter_T.OnItemClickListner, IMConversationnListAdapter.onClickListener, IMPopWindowUtil.OnViewItemClickListener {
    private View view;
    private RecyclerView mRecyclerView;
    private ExtendListHeader extendListHeader;
    private PullExtendLayout pullExtend;

    boolean haveResponse = true;//用来判断是不是接收到消息回执  没有接收10秒后显示发送失败
    private Handler handler = new Handler();
    private IMConversationnListAdapter adapter;
    private List<IMConversationBean> conversationlistBeans = new ArrayList<>();
    public static final int IM_CHAT_RESTQUECODE = 10002;
    private boolean isremove;
    private List<IMConversationBean> savedatas;
    private MyConversationHeadView myConversationHeadView;
    private boolean isSearch=false;//是否是搜索
    private MyRelativeLayout rlBottom;
    private LinearLayout llTop;
    private ImageView imIvAdd2;
    private RelativeLayout ll_right2;
    private RelativeLayout llLeft2;
    private TextView imIvBack;
    private Animation operatingAnim;
    private Animation operatingAnim1;
    private IMCustomPopWindow mPop;
    private boolean showchoose=false;
    private TextView  mTvTitle;
    private LinearLayout llTitle;
    private RelativeLayout rlDelete;
    private List<SmallProgramBean> recordsData;
    private List<SmallProgramBean> collectionData;
    private FrameLayout fl;
    private TextView tvDelete;
    private boolean isShowHeader=false;

    @Override
    public View initView() {
        EventBus.getDefault().register(this);
        view = View.inflate(getActivity(), R.layout.fragment_im_conversationlist, null);
        mRecyclerView = view.findViewById(R.id.recyclerView);
        mTvTitle = view.findViewById(R.id.im_tv_title);
        extendListHeader = view.findViewById(R.id.extend_header);
        rlBottom = view.findViewById(R.id.rl_bottom);
        pullExtend = view.findViewById(R.id.pull_extend);
        llTop = view.findViewById(R.id.ll_top);
        imIvAdd2 = view.findViewById(R.id.im_iv_add2);
        ll_right2= view.findViewById(R.id.ll_right2);
        llLeft2 = view.findViewById(R.id.ll_left2);
        imIvBack = view.findViewById(R.id.im_iv_back);
        llTitle = view.findViewById(R.id.ll_title);
        rlDelete = view.findViewById(R.id.rl_delete);
        fl = view.findViewById(R.id.fl);
        tvDelete = view.findViewById(R.id.tv_delete);
        pullExtend.setPullLoadEnabled(false);
        return view;
    }


    @Override
    public void iniData() {
        initAnim();
        initRecycle();
        getdata();
        initListener();
        getRecentlyUse();
    }
    public boolean isShowHeader() {
        return isShowHeader;
    }
    public void resetHeader(){
        pullExtend.closeExtendHeadAndFooter();
    }
    /**
     * 更新最近浏览
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(final UpdateLatestEvent event) {
        getRecentlyUse();
    }
    /**
     * 删除最近浏览
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(final IMDeleteRecentEvent event) {
        if(recordsData.size()>5){
            if(event.getPosition()==4){
                return;
            }
        }
        deleteRecentlyUse(recordsData.get(event.getPosition()).getUseId(),event.getPosition());
    }
    /**
     * 取消收藏
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(final IMCancerCollectionEvent event) {
        cancleCollection(collectionData.get(event.getPosition()).getProgramId());
    }
    /**
     * 更新删除视图
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(final IMDeleteShowEvent event) {
        if(event.isShow()){
            if(rlDelete.getVisibility()!=View.VISIBLE){
                startAnimation(rlDelete,1);
                rlDelete.setVisibility(View.VISIBLE);
                fl.setVisibility(View.GONE);
            }
        }else {
            if(rlDelete.getVisibility()==View.VISIBLE){
                startAnimation(rlDelete,2);
            }
        }

        if(event.isCanDelete()){
            tvDelete.setText("松手即可删除");
            Drawable drawable= getResources().getDrawable(R.mipmap.small_delete_open);
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            tvDelete.setCompoundDrawables(null,drawable,null,null);
            rlDelete.setAlpha(0.7f);
        }else {
            tvDelete.setText("拖动此处删除");
            Drawable drawable= getResources().getDrawable(R.mipmap.small_delete);
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            tvDelete.setCompoundDrawables(null,drawable,null,null);
            rlDelete.setAlpha(1f);
        }
    }
    /**
     * 删除按钮动画
     * @param view
     * @param type
     */
    private void startAnimation(View view,int type){
        ValueAnimator animator;
        if(type==1){
            animator = ValueAnimator.ofInt(0,66);
        }else {
            animator = ValueAnimator.ofInt(66,0);
        }
        animator.setDuration(200);//播放时长
        animator.setRepeatCount(0);//重放次数
        animator.setRepeatMode(ValueAnimator.RESTART);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int currentValue = (int) animation.getAnimatedValue();
                //改变后的值发赋值给对象的属性值
                RelativeLayout.LayoutParams linearParams =(RelativeLayout.LayoutParams) view.getLayoutParams();
                linearParams.height =SizeUtils.dp2px(currentValue);
                view.setLayoutParams(linearParams);
                //刷新视图
                view.requestLayout();
                if(type==2&&currentValue==0){
                    rlDelete.setVisibility(View.GONE);
                    fl.setVisibility(View.VISIBLE);
                }
            }
        });
        animator.start();
    }
    @SuppressLint("ClickableViewAccessibility")
    private void initListener() {
        IMPopWindowUtil.getInstance(getContext()).setOnViewItemListener(this);
        extendListHeader.setStateLayout(new ExtendLayout.StateLayout() {
            @Override
            public void onStateChange(IExtendLayout.State state) {
                LogUtils.i("WOLF",state.name());
                switch (state){
                    case arrivedListHeight:
                        rlBottom.setVisibility(View.VISIBLE);
                        llTop.setVisibility(View.GONE);
                        llTitle.setVisibility(View.VISIBLE);
                        EventBus.getDefault().post(new MainUpdateEvent("2"));
                        isShowHeader=true;
                        break;
                    case RESET:
                        rlBottom.setVisibility(View.GONE);
                        llTop.setVisibility(View.VISIBLE);
                        llTitle.setVisibility(View.GONE);
                        EventBus.getDefault().post(new MainUpdateEvent("1"));
                        isShowHeader=false;
                        break;
                }
            }
        });
        extendListHeader.setPullDownLisentner(new ExtendListHeader.PullDownLisentner() {
            @Override
            public void onPullDown(int offset) {
                if(offset< ScreenUtils.getScreenHeight() -SizeUtils.dp2px(50)){
                    rlBottom.setVisibility(View.GONE);
                }
            }
        });
        rlBottom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pullExtend.closeExtendHeadAndFooter();
            }
        });

        pullExtend.setOnPullListener(new PullExtendLayout.OnPullListener() {
            @Override
            public void onPull(float deltaY) {
                if(deltaY>0){
                    llTop.setVisibility(View.GONE);
                }
            }
        });

        ll_right2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IMPopWindowUtil.getInstance(getActivity()).showPopListView(imIvAdd2);
                imIvAdd2.startAnimation(operatingAnim);
                if (imIvBack.getText().equals("完成")) {
                    adapter.setBianJi();
                    imIvBack.setText("编辑");
                    adapter.setShowChoose(false);
                    adapter.notifyDataSetChanged();
                }
            }
        });

        llLeft2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (imIvBack.getText().equals("编辑")) {
                    imIvBack.setText("完成");
                    adapter.notifyDataSetChanged();
                    adapter.setShowChoose(true);
                } else {
                    imIvBack.setText("编辑");
                    adapter.setShowChoose(false);
                    adapter.notifyDataSetChanged();
                }
            }
        });
    }

    /**
     * 获取收藏
     */
    private void getCollection() {
        final IMBetGetBean bean = new IMBetGetBean();
        String json = new Gson().toJson(bean);
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json);
        IMHttpsService.getCollection(body, new IMHttpResultObserver<List<SmallProgramBean>>() {
            @Override
            public void onSuccess(List<SmallProgramBean> data, String message) {
                dismissLoadingDialog();
                collectionData=data;
//                for (int i = 0; i < 40; i++) {
//                    data.add(data.get(0));
//                }
                extendListHeader.setMyCollectionData(data);
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
     * 获取最近使用
     */
    private void getRecentlyUse() {
        final IMBetGetBean bean = new IMBetGetBean();
        String json = new Gson().toJson(bean);
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json);
        IMHttpsService.getRecentlyUse(body, new IMHttpResultObserver<RecentlyUseBean>() {
            @Override
            public void onSuccess(RecentlyUseBean data, String message) {
                dismissLoadingDialog();
                recordsData=data.getRecords();
                extendListHeader.setRecentlyUseData(data.getRecords());
                getCollection();
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
     * 添加收藏
     */
    public void addCollection(String programId) {
        final IMBetGetBean bean = new IMBetGetBean();
        bean.setProgramId(programId);
        String json = new Gson().toJson(bean);
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json);
        IMHttpsService.addCollection(body, new IMHttpResultObserver<String>() {
            @Override
            public void onSuccess(String data, String message) {
                dismissLoadingDialog();
                EventBus.getDefault().post(new UpdateLatestEvent());
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
     * 删除最近使用
     */
    private void deleteRecentlyUse(String useId, int position) {
        showLoadingDialog();
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), "["+useId+"]");
        IMHttpsService.deleteRecentlyUse(body, new IMHttpResultObserver<String>() {
            @Override
            public void onSuccess(String data, String message) {
                dismissLoadingDialog();
                EventBus.getDefault().post(new UpdateLatestEvent());
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
     * 取消收藏
     */
    private void cancleCollection(String programId) {
        showLoadingDialog();
        final IMBetGetBean bean = new IMBetGetBean();
        bean.setProgramId(programId);
        String json = new Gson().toJson(bean);
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json);
        IMHttpsService.cancleCollection(body, new IMHttpResultObserver<String>() {
            @Override
            public void onSuccess(String data, String message) {
                dismissLoadingDialog();
                EventBus.getDefault().post(new UpdateLatestEvent());
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
     * 加群成功之后刷新当前界面
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(IMAddCollectionEvent event) {
        addCollection(event.getBean().getProgramId());
    }

    /**
     * 初始化旋转动画
     */
    private void initAnim() {
        operatingAnim = AnimationUtils.loadAnimation(getContext(), R.anim.anim_rotate_45);
        operatingAnim.setFillAfter(true);
        operatingAnim1 = AnimationUtils.loadAnimation(getContext(), R.anim.anim_rotate_90);
        operatingAnim1.setFillAfter(true);
    }
    /**
     * 初始化recycle
     */
    @SuppressLint("WrongConstant")
    private void initRecycle() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(layoutManager);
    }

    /**
     * 获取数据库里面的数据(本地人员数据+消息数据组装)
     */
    private void getdata() {
        conversationlistBeans = daoUtils.queryAllConversationData();
        if (conversationlistBeans == null) {
            return;
        }
        IMSMsgManager.getOrderData(conversationlistBeans);
        adapterNotifyDataSetChangedView();

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == IM_CHAT_RESTQUECODE) {
            IMSManager.getInstance().getUnreadMessageNumber();
        }
        if (adapter == null) {
            return;
        }
        IMSMsgManager.getOrderData(conversationlistBeans);
        adapterNotifyDataSetChangedView();
    }

    @Override
    public void onResume() {
        super.onResume();
        IMSManager.getInstance().getUnreadMessageNumber();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
        EventBus.getDefault().unregister(this);
    }

    /**
     * 收到服务器消息3种（1是回执，2.是消息，3.用户状态，4已读未读,5系统消息）
     */

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(IMGroupMessageEvent event) {
        if (event == null || event.getMessageMessage() == null) {
            return;
        }
        IMMessageBean message = event.getMessageMessage();
        if (!TextUtils.isEmpty(message.getData()) && (message.getType() == MessageProtobuf.ImMessage.TypeEnum.SYSTEM || message.getMsgType()==9) ) {
            handlerSystemMessage(message);
        } else if (message.getType() == MessageProtobuf.ImMessage.TypeEnum.READ) {//如果是已读未读
            handlerReadedMessage(message);
        } else if (message.getType() == MessageProtobuf.ImMessage.TypeEnum.USERSTATUS) { //如果是客服状态消息
            handlUserdataMessage(message);
        } else if (message.getType() == MessageProtobuf.ImMessage.TypeEnum.SERVERRECEIPT) {  //是发送消息成功的回执
            handleServerReciptMessage(message);
        } else {  //不为空代表被动接收消息
            handleReceiveMessageData(message);
        }
    }
    /**
     * 处理已读未读
     */
    private void handlerReadedMessage(IMMessageBean message) {
        String conversationlistId=null;
        if(TextUtils.isEmpty(message.getGroupId()) && TextUtils.isEmpty(message.getSenderId())){
            return;
        }
        if(!TextUtils.isEmpty(message.getGroupId())){
            conversationlistId=message.getGroupId();
        }else{
            conversationlistId=message.getSenderId();
        }
        for (int i = 0; i < conversationlistBeans.size(); i++) {
            if (conversationlistBeans.get(i).getConversationId().equals(conversationlistId)) {
                conversationlistBeans.get(i).setIsReaded("2");
                IMConversationBean bean = daoUtils.queryConversationBean(conversationlistId);
                if(bean!=null){
                    bean.setIsReaded("2");
                    daoUtils.updateConversationData(bean);
                }
                adapter.notifyDataSetChanged();
            }
        }
    }

    /**
     * 处理系统消息
     */
    private void handlerSystemMessage(IMMessageBean message) {
        IMMessageDataJson s = new Gson().fromJson(message.getData(), IMMessageDataJson.class);
        switch (Integer.parseInt(s.getSysCode())){
            case IMSConfig.IM_MESSAGE_GROUP_PESSION:
            case IMSConfig.IM_MESSAGE_GROUP_LIMITE:
                updataGroup(s);
                break;
            case IMSConfig.IM_MESSAGE_ADD_FRIEND_SUCCESS:
                IMMessagSystemJson bean = new Gson().fromJson(new Gson().toJson(s.getMeta()), IMMessagSystemJson.class);
                String customerId =null;
                if(!bean.getAcceptCustomerId().equals(IMSManager.getMyUseId())){ //如果申请人是别人
                    return;    //让接收方等着收消息
                }else {
                    customerId = bean.getCustomerId();  //让接收方同意添加好友的时候，发送一条消息
                }
                getHttpPersonDetailData(customerId, message, s.getText());
                IMLogUtil.d("MyOwnTag:", "IMConversationFragment ----" + s.getText());
                break;
            case IMSConfig.IM_MESSAGE_ADD_FRIEND_WAIT: //通知被申请人收到添加好友的请求
                EventBus.getDefault().post(new IMNewFriendUnReadNumber(null));
                break;
            case IMSConfig.IM_MESSAGE_SEND_NO_FRIEND: //如果不是好友  消息发送失败
                handleNoFriend(message);
                break;
            case IMSConfig.IM_MESSAGE_FORBIT_FRIEND: //如果被好友屏蔽  消息发送失败
                handleNoFriend(message);
                break;
            case IMSConfig.IM_MESSAGE_SEND_NO_GROUP: //如果不是群成员  消息发送失败
            case IMSConfig.IM_MESSAGE_REMOVE_GROUP:
                handleNoGroup(message);
                break;
            case IMSConfig.IM_MESSAGE_JOINGROUP: //有人进去了群聊
                handleJoinGroup(message,s);
                break;
            case IMSConfig.IM_MESSAGE_CLOPNEGROUP: //解散了群聊
                closeGroup(message);
                break;
            case IMSConfig.IM_MESSAGE_KIOUTSELF: //自己被提出了群聊 再发消息收到20001
            case IMSConfig.IM_MESSAGE_KIOUTGROUP: //自己被提出了群聊 再发消息收到20001
            case IMSConfig.IM_MESSAGE_QUITGROUP://退出了群聊
                adapterNotifyDataSetChangedView();
                break;
            case IMSConfig.IM_MESSAGE_CLX_TEAM: //彩乐讯团队的消息
            case IMSConfig.IM_MESSAGE_CLX_fENG: //彩乐讯团队(封号消息)
                if(Integer.parseInt(s.getSysCode())==IMSConfig.IM_MESSAGE_CLX_fENG){
                    EventBus.getDefault().post(new IMPushMessageEvent(message));
                }
                List<IMConversationBean> beans = daoUtils.queryAllConversationData();
                if (conversationlistBeans == null) {
                    return;
                }
                conversationlistBeans.clear();
                conversationlistBeans.addAll(beans);
                IMSMsgManager.getOrderData(conversationlistBeans);
                adapterNotifyDataSetChangedView();
                break;
        }
    }

    /**
     * 更新群信息
     */
    private void updataGroup(IMMessageDataJson s) {
        if(s.getMeta()==null){
            return;
        }
        IMGroupNoticeBean.IMGroupNoticeDetail datas = new Gson().fromJson(new Gson().toJson( s.getMeta()), IMGroupNoticeBean.IMGroupNoticeDetail.class);
        IMGroupBean groupBean = daoUtils.queryGroupBean(datas.getGroupId());
        if(groupBean!=null){
            groupBean.setGroupName(datas.getGroupName());
            groupBean.setGroupAvatar(datas.getGroupAvatar());
            daoUtils.updateGroupData(groupBean);
            adapter.notifyDataSetChanged();
        }
    }
    /**
     * 更新个人信息
     */
    private void updataPerson(IMMessageBean message) {
        IMMessageDataJson s = new Gson().fromJson(message.getData(), IMMessageDataJson.class);
        if(s.getMeta()==null){
            return;
        }
        //如果是好友信息修改  要跟新2张表   还刷新界面
        IMPersonalBean bean = new Gson().fromJson(new Gson().toJson(s.getMeta()), IMPersonalBean.class);
        IMPersonBean bean1 = daoUtils.queryMessageBean(bean.getCustomerId());
        IMConversationBean bean2 = daoUtils.queryConversationBean(bean.getCustomerId());
        if(bean1!=null){
            bean1.setNickName(bean.getNickName());
            bean1.setNickName(bean.getNickName());
            bean1.setAvatar(bean.getAvatar());
            if(!TextUtils.isEmpty(bean.getSignature())){
                bean1.setSignature(bean.getSignature());
            }
            daoUtils.updateMessageData(bean1);
        }
        if(bean2!=null){
            bean2.setConversationName(bean.getNickName());
            bean2.setConversationavatar(bean.getAvatar());
            bean2.setAvatar(bean.getAvatar());
            daoUtils.updateMessageData(bean1);
        }
        adapter.notifyDataSetChanged();
    }

    /**
     * 解散了群聊
     */
    private void closeGroup(IMMessageBean message) {
        IMMessageDataJson bean1 = new Gson().fromJson(message.getData(), IMMessageDataJson.class);
        IMMessageKictOutJson data = new Gson().fromJson(new Gson().toJson(bean1.getMeta()), IMMessageKictOutJson.class);
        String groupId = data.getGroupId();
        if(conversationlistBeans.size()>0) { //会话列表有该会话  1576737822931 1576737763887
            for (int i = 0; i < conversationlistBeans.size(); i++) {
                if (conversationlistBeans.get(i).getConversationId().equals(groupId)) {
                    conversationlistBeans.get(i).setLastMessageContent("该群聊已被群主解散");
                    IMMessageDataJson json = new Gson().fromJson(message.getData(), IMMessageDataJson.class);
                    conversationlistBeans.get(i).setLastMessageType(Integer.parseInt(json.getType()));
                    conversationlistBeans.get(i).setLastMessageSendType(IMSConfig.MSG_RECIEVE);
                    conversationlistBeans.get(i).setLastMessageTime(message.getTimestamp());
                    //保存数据库
                    IMConversationBean bean = daoUtils.queryConversationBean(conversationlistBeans.get(i).getConversationId());
                    if(bean!=null){
                        bean.setLastMessageContent("该群聊已被群主解散");
                        bean.setLastMessageTime(message.getTimestamp());
                        bean.setLastMessageType(Integer.parseInt(json.getType()));
                        daoUtils.updateConversationData(bean);
                    }
                    IMSHttpManager.getInstance().IMHttpGetGroupList(null,false,false);
                    conversationlistBeans.get(i).setLastMessageSendstate(IMSConfig.IM_SEND_STATE_SUCCESS);
                    IMConversationBean inconversation = conversationlistBeans.get(i);
                    conversationlistBeans.remove(i);
                    conversationlistBeans.add(0, inconversation);
                    IMSMsgManager.getOrderData(conversationlistBeans);
                    adapterNotifyDataSetChangedView();
                }
            }
        }
    }
    /**
     * 如果不是好友  消息发送失败
     *  {"type":"10000","text":"你们已经不是好友，请添加好友发送消息","sysCode":"10001","fingerprint":"664857349185343519"}
     */
    private void handleNoFriend(IMMessageBean message) {
        IMSystemNoGroupBean dataJson = new Gson().fromJson(message.getData(), IMSystemNoGroupBean.class);
        for (int i = 0; i < conversationlistBeans.size(); i++) {
            if(TextUtils.isEmpty(conversationlistBeans.get(i).getConversationId())){
                continue;
            }
            if(conversationlistBeans.get(i).getConversationId().equals(message.getSenderId())){

                conversationlistBeans.get(i).setLastMessageSendType(IMSConfig.MSG_RECIEVE);
                conversationlistBeans.get(i).setLastMessageTime(message.getTimestamp()+1);
                conversationlistBeans.get(i).setLastMessageContent(dataJson.getText());
                IMSMsgManager.getOrderData(conversationlistBeans);
                adapterNotifyDataSetChangedView();
            }
        }

    }
    /**
     * 如果不是好友  消息发送失败
     *  "type":"10000","text":"你已被移除该群","sysCode":"20001","fingerprint":"663754510870515714"}
     */
    private void handleNoGroup(IMMessageBean message) {
        IMSystemNoGroupBean dataJson = new Gson().fromJson(message.getData(), IMSystemNoGroupBean.class);
        //跟新界面
        for (int i = 0; i < conversationlistBeans.size(); i++) {
            if (TextUtils.isEmpty(conversationlistBeans.get(i).getConversationId())) {
                continue;
            }
            if (conversationlistBeans.get(i).getConversationId().equals(message.getGroupId())) {
                conversationlistBeans.get(i).setLastMessageSendType(IMSConfig.MSG_RECIEVE);
                conversationlistBeans.get(i).setLastMessageTime(message.getTimestamp()+1);
                conversationlistBeans.get(i).setLastMessageContent(dataJson.getText());
                IMSMsgManager.getOrderData(conversationlistBeans);
                adapterNotifyDataSetChangedView();
            }
        }
    }
    /**
     * 处理收到的是别人发的消息
     */
    private void handleReceiveMessageData(IMMessageBean message) {
        if (message == null || TextUtils.isEmpty(message.getData())) {
            return;
        }
        //如果是群聊的消息
        String conversationlistId=null;
        if(TextUtils.isEmpty(message.getGroupId()) && TextUtils.isEmpty(message.getSenderId())){
            return;
        }
        if(!TextUtils.isEmpty(message.getGroupId())){
            conversationlistId=message.getGroupId();
        }else{
            conversationlistId=message.getSenderId();
        }

        IMConversationBean conversationBean = daoUtils.queryConversationBean(conversationlistId);
        List<IMConversationDetailBean> beans = daoUtils.queryConversationDetailDataAccordField(conversationlistId, 0, 1);

        if(conversationBean!=null){ //会话列表有该会话
            for(int i = 0; i < conversationlistBeans.size(); i++) {
                if (conversationlistBeans.get(i).getConversationId().equals(conversationlistId)) {
                    if (conversationlistBeans.get(i).getLastMessageTime() < message.getTimestamp()) {  //如果最新的消息大于现在的消息才跟新这个界面
                        conversationlistBeans.get(i).setLastMessageContent(message.getData());
                        IMMessageDataJson json = new Gson().fromJson(message.getData(), IMMessageDataJson.class);
                        conversationlistBeans.get(i).setLastMessageType(Integer.parseInt(json.getType()));
                        conversationlistBeans.get(i).setLastMessageSendType(IMSConfig.MSG_RECIEVE);
                        conversationlistBeans.get(i).setLastMessageTime(message.getTimestamp());
                    }
                    conversationlistBeans.get(i).setLastMessageSendstate(IMSConfig.IM_SEND_STATE_SUCCESS);
                    IMConversationBean inconversation = conversationlistBeans.get(i);
                    conversationlistBeans.remove(i);
                    conversationlistBeans.add(0, inconversation);
                    String content=inconversation.getConversationName()+" : "+ IMSMsgManager.getTextData(inconversation.getLastMessageContent());
                    EventBus.getDefault().post(new IMMessageNoticeContentEvent(content));
                    IMSMsgManager.getOrderData(conversationlistBeans);
                    adapterNotifyDataSetChangedView();
                }
            }
        }else { //会话列表没有该会话（）
            IMLogUtil.d("MyOwnTag:", "IMConversationFragment ----" +message.getGroupId());
            creatReceiveConversation(message);
        }
    }
    /**
     * 处理消息回执
     */
    private void handleServerReciptMessage(IMMessageBean message) {
        isremove = false;
        for (int i = 0; i < sendlists.size(); i++) {
            if (sendlists.get(i).getFingerId().equals(message.getFingerprint())) {
                sendlists.remove(i);
                isremove = true;
            }
        }
        //跟新界面
        haveResponse = true;
        upDateView(message.getFingerprint(), true);
        IMConversationBean bean = daoUtils.queryConversationBeanByFinger(message.getFingerprint());
        IMConversationDetailBean data = daoUtils.queryConversationDetailDataAccordFieldFiger(message.getFingerprint());
        if( data==null){
            return;
        }
        if(bean!=null){
            bean.setLastMessageSendstate(IMSConfig.IM_SEND_STATE_SUCCESS);
            bean.setLastMessageTime(message.getTimestamp());
            bean.setIsReaded("1");
            daoUtils.updateConversationData(bean);
        }
        data.setSendstate(IMSConfig.IM_SEND_STATE_SUCCESS);
        data.setTimestamp(message.getTimestamp());
        daoUtils.updateConversationDetailData(data);
    }

    /**
     * 加群成功之后刷新当前界面
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(IMRegetGroupListEvent event) {

    }
    /**
     * 踢群之后给提示
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(final IMRegetGroupKickOutEvent event) {
    }
    /**
     * 详情界面发送消息，通知这边更改状态,还未从服务器获取(单聊群聊一起)
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onSendMessageEvent(IMSendGroupMessageEvent event) {
        handlerSendGroupMessage(event);
    }
    /**
     * 清空消息记录刷新界面
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(IMMessageDetelGroupEvevt event) {
        handlerDeleteGroupMessage(event.getGroupId());
    }
    /**
     * 清空消息记录刷新界面
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(IMMessageDeteAlllEvevt event) {
        handlerDeleteGroupMessage(event.getPersonalId());
    }


    /**
     * 添加好友删除好友需要刷新好友界面
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(IMDeleteFriendSucessEvent event) {
        String customerId = event.getBean();
        if(!TextUtils.isEmpty(customerId) && conversationlistBeans!=null && conversationlistBeans.size()>0){
            for (int i = 0; i < conversationlistBeans.size(); i++) {
                if(conversationlistBeans.get(i).getConversationId().equals(customerId)){
                    conversationlistBeans.remove(i);
                    adapterNotifyDataSetChangedView();
                }
            }
            IMConversationBean bean = daoUtils.queryConversationBean(customerId);
            List<IMConversationDetailBean> beans = daoUtils.queryConversationDetailDataAccordField(customerId);
            daoUtils.deleteConversationData(bean);
            daoUtils.deleteConversationDetailAccordField(customerId);
            IMSManager.getInstance().getUnreadMessageNumber();
        }
    }
    /**
     * 修改群聊的头像
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onUpdateHeadView(IMUpdataGroupInforEvent event) {
        IMConversationBean groupBean = event.groupBean;
        if(conversationlistBeans!=null && conversationlistBeans.size()>0){
            for (int i = 0; i < conversationlistBeans.size(); i++) {
                if(conversationlistBeans.get(i).getConversationId().equals(groupBean.getConversationId())){
                    conversationlistBeans.get(i).setConversationName(groupBean.getConversationName());
                    conversationlistBeans.get(i).setConversationavatar(groupBean.getConversationavatar());
                }
            }
        }
        adapterNotifyDataSetChangedView();
    }
    /**
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onNoticeEvent(IMMessageNoticelGroupEvevt event) {
        IMSMsgManager.getOrderData(conversationlistBeans);
        adapterNotifyDataSetChangedView();
    }


    /**
     *创建单聊会话刷新界面
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onCreatConversationEvent(IMCreatMessageEvent event) {
        IMConversationBean messageMessage = event.getMessageMessage();
        //防止创建重复的item
        if(conversationlistBeans.size()>0){
            for (int i = 0; i < conversationlistBeans.size(); i++) {
                if(conversationlistBeans.get(i).getConversationId().equals(messageMessage.getConversationId())){
                    conversationlistBeans.remove(i);
                }
            }
        }
        conversationlistBeans.add(0,messageMessage);
        IMSMsgManager.getOrderData(conversationlistBeans);
        adapterNotifyDataSetChangedView();
    }
    /**
     * 有人进入了群聊
     * {"avatar":"http://65.52.160.124:9050/group1/M00/00/37/CgABFV3n1HWAOBPsAAArO_5pThc92.jpeg","blacklist":"N","forbiddenWords":"N","gmId":1539.0,"gmtCreate":1.57588047E12,"gmtUpdate":1.57588047E12,"groupId":"30725dee07167a85dd14133c5eff","isViewHit":"N","isViewTitle":"N","level":"","logicalDeletion":"E","memberId":"20485de7d4567a85dd69fb8ed2b7",
     * "memberType":"1","messageFree":"N","name":"amubox","noticeReminder":"N","points":"0","userRole":"2"}
     */
    private void handleJoinGroup(IMMessageBean message,IMMessageDataJson bean) {
        List<IMSystemSomeOneAddGroup> result = new Gson().fromJson(new Gson().toJson(bean.getMeta()), new TypeToken<List<IMSystemSomeOneAddGroup>>() {}.getType());
        if(result==null || result.size()==0){
            return;
        }
        //如果有会话
        boolean  havebean=false;
        if(conversationlistBeans!=null && conversationlistBeans.size()>0){
            String name="";
            if(result.size()>1){
                if(TextUtils.isEmpty(result.get(0).getNickName())){
                    name=result.get(1).getNickName()+"等人";
                }else  if(TextUtils.isEmpty(result.get(1).getNickName())){
                    name=result.get(0).getNickName()+"等人";
                }else {
                    name=result.get(0).getNickName()+"、"+result.get(1).getNickName()+"等人";
                }
            }else {
                name=result.get(0).getNickName();
            }
            for (int i = 0; i < conversationlistBeans.size(); i++) {
                if(conversationlistBeans.get(i).getConversationId().equals(result.get(0).getGroupId())){
                    conversationlistBeans.get(i).setLastMessageContent(name+"进入了群聊");
                    if(message.getTimestamp()==0){
                        conversationlistBeans.get(i).setLastMessageTime(System.currentTimeMillis());
                    }else {
                        conversationlistBeans.get(i).setLastMessageTime(message.getTimestamp());
                    }
                    adapterNotifyDataSetChangedView();
                    havebean=true;
                }
            }
        }
        //如果没有会话
        if(!havebean){
            IMPreferenceUtil.setPreference_String(message.getGroupId() + IMSConfig.IM_UNEREAD_CONVERSATION_CHAT, "0");
            IMConversationBean newbean= IMSMsgManager.creatReceiveGroupConversation(message);
            String name="";
            if(result.size()>1){
                if(TextUtils.isEmpty(result.get(0).getNickName())){
                    name=result.get(1).getNickName()+"等人";
                }else  if(TextUtils.isEmpty(result.get(1).getNickName())){
                    name=result.get(0).getNickName()+"等人";
                }else {
                    name=result.get(0).getNickName()+"、"+result.get(1).getNickName()+"等人";
                }
            }else {
                name=result.get(0).getNickName();
            }
            if(newbean==null){
                getHttpGroupDetailData(message.getGroupId(),message,true,name);
            }else {
                newbean.setLastMessageContent(name+"进入了群聊");
                if(message.getTimestamp()==0){
                    newbean.setLastMessageTime(System.currentTimeMillis());
                }else {
                    newbean.setLastMessageTime(message.getTimestamp());
                }
                daoUtils.insertConversationData(newbean);
                //防止创建重复的item
                if(conversationlistBeans.size()>0){
                    for (int i = 0; i < conversationlistBeans.size(); i++) {
                        if(conversationlistBeans.get(i).getConversationId().equals(message.getGroupId())){
                            conversationlistBeans.remove(i);
                        }
                    }
                }
                conversationlistBeans.add(0, newbean);
                IMSMsgManager.getOrderData(conversationlistBeans);
                adapterNotifyDataSetChangedView();
            }

        }
    }

    /**
     *创建单聊会话（加好友成功）
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onCreatFriendConversation(IMAddFriendSucessEvent event) {
        IMUserInforBean.UserInforData data1 = event.getBean();
//        //保存新群进数据库
        IMPersonBean groupBean=new IMPersonBean();
        groupBean.setNickName(data1.getNickName());
        groupBean.setMemberId(data1.getCustomerId());
        groupBean.setAvatar(data1.getAvatar());
        daoUtils.insertMessageData(groupBean);
    }

    /**
     * 解散群聊（从数据库删除）
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(IMDeleteGroupEvent event) {
        String groupId = event.getGroupId();
        daoUtils.deleteConversationData(groupId);
        daoUtils.deleteConversationDetailAccordField(groupId);
        daoUtils.deleteGroupData(groupId);
        daoUtils.deleteGroupMemberData(groupId);
        for (int i = 0; i < conversationlistBeans.size(); i++) {
            if(conversationlistBeans.get(i).getConversationId().equals(groupId)){
                conversationlistBeans.remove(i);
            }
        }
        adapterNotifyDataSetChangedView();
        IMSHttpManager.getInstance().IMHttpGetGroupList(null,false,false);
    }
    /**
     * 置顶和关闭通知
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onNoticeEvent(IMMessageNoticelPersonEvevt event) {
        List<IMConversationBean> imPersonBeans = daoUtils.queryAllConversationData();
        if (imPersonBeans==null){
            return;
        }
        IMSMsgManager.getOrderData(conversationlistBeans);
        adapterNotifyDataSetChangedView();
    }

    /**
     *  自己发送消息
     * （收到或是没有收到）消息回执跟新界面
     */
    private void upDateView( String fingerprintid,boolean haveRes) {
        haveResponse=haveRes;
        for (int i = 0; i < conversationlistBeans.size(); i++) {
            if(TextUtils.isEmpty(conversationlistBeans.get(i).getLastMessageId())){
                continue;
            }
            if(conversationlistBeans.get(i).getLastMessageId().equals(fingerprintid)){
                if(haveResponse){  //收到了代表success
                    conversationlistBeans.get(i).setLastMessageSendstate(IMSConfig.IM_SEND_STATE_SUCCESS);
                }else {
                    conversationlistBeans.get(i).setLastMessageSendstate(IMSConfig.IM_SEND_STATE_FAILE);
                }
            }
            IMSMsgManager.getOrderData(conversationlistBeans);
            adapterNotifyDataSetChangedView();
        }
    }

    private Timer timer;
    private TimerTask timerTask;
    private  List<IMSendingStateBean>sendlists=new ArrayList<>();//用来记录发送期间的消息列表
    @Override
    public void onItemClickListner(View v, int position, Object t) {
        if (IMStopClickFast.isFastClick()) {
            if(!TextUtils.isEmpty(conversationlistBeans.get(position-1).getGroupId())){
                Intent intent = new Intent(getActivity(), IMGroupChatActivity.class);
                intent.putExtra("group", conversationlistBeans.get(position-1));
                changeChooseStated();
                startActivityForResult(intent, IM_CHAT_RESTQUECODE);
            }else {
                Intent intent=new Intent(getActivity(), IMPersonalChatActivity.class);
                intent.putExtra("user",conversationlistBeans.get(position-1));
                changeChooseStated();
                startActivityForResult(intent,IM_CHAT_RESTQUECODE);
            }
        }
    }
    /**
     * 清空消息记录刷新界面
     */
    private void handlerDeleteGroupMessage(String conversonId) {
        if(TextUtils.isEmpty(conversonId)){
            for (int i = 0; i < conversationlistBeans.size(); i++) {
                conversationlistBeans.get(i).setLastMessageContent("[暂无消息]");
                conversationlistBeans.get(i).setLastMessageTime(0);
                conversationlistBeans.get(i).setLastMessageTime(System.currentTimeMillis());
                IMPreferenceUtil.setPreference_String(conversationlistBeans.get(i).getConversationId() + IMSConfig.IM_UNEREAD_CONVERSATION_CHAT, "0");//把对应人员的未读消息清空
                conversationlistBeans.get(i).setLastMessageSendstate(IMSConfig.IM_SEND_STATE_SUCCESS);
                daoUtils.updateConversationData( conversationlistBeans.get(i));
            }
        }else {
            for (int i = 0; i < conversationlistBeans.size(); i++) {
                if(conversationlistBeans.get(i).getConversationId().equals(conversonId)){
                    conversationlistBeans.get(i).setLastMessageContent("[暂无消息]");
                    conversationlistBeans.get(i).setLastMessageTime(0);
                    conversationlistBeans.get(i).setLastMessageTime(System.currentTimeMillis());
                    conversationlistBeans.get(i).setLastMessageSendstate(IMSConfig.IM_SEND_STATE_SUCCESS);
                    daoUtils.updateConversationData( conversationlistBeans.get(i));
                }
            }
        }
        IMSMsgManager.getOrderData(conversationlistBeans);
        adapterNotifyDataSetChangedView();
    }

    /**
     * 详情界面发送消息，通知这边更改状态(还未从服务器获取)(群聊)
     */
    private void handlerSendGroupMessage(IMSendGroupMessageEvent event) {
        final String conversationId = event.getGroupId();
        final IMConversationDetailBean data = event.getData();
        sendlists.add(new IMSendingStateBean(data.getFingerprint(),data.getTimestamp(),conversationId));
        //发送消息时修改对应item的发送状态（发送时候在控制类里面保存发送消息，所以这里不保存）
        for (int i = 0; i < conversationlistBeans.size(); i++) {
            if(conversationlistBeans.get(i).getConversationId().equals(conversationId)){
                conversationlistBeans.get(i).setLastMessageId(data.getFingerprint());
                conversationlistBeans.get(i).setLastMessageType(data.getMsgType());
                conversationlistBeans.get(i).setLastMessageSendType(IMSConfig.MSG_SEND);
                conversationlistBeans.get(i).setLastMessageTime(data.getTimestamp());
                conversationlistBeans.get(i).setLastMessageContent(data.getData());
                conversationlistBeans.get(i).setLastMessageSendstate(IMSConfig.IM_SEND_STATE_SENDING);
                IMConversationBean groupBean = conversationlistBeans.get(i);
                conversationlistBeans.remove(i);
                conversationlistBeans.add(0, groupBean);
                IMSMsgManager.getOrderData(conversationlistBeans);
                adapterNotifyDataSetChangedView();
                break;
            }
        }
        haveResponse=false;
        startDelayTime(data);//发送15秒钟不成功就跟新数据库
        handler.postDelayed(new Runnable() { //发送15秒钟不成功就跟新界面
            @Override
            public void run() {
                if(!haveResponse) {
                    upDateView(data.getFingerprint(), false);
                }
            }
        },15000);
    }

    /**
     * 成功收到服务器响应的消息就移除去，消息里面有个时间戳，在发送消息的时候只开启一个计数器（，每秒轮询消息队列的消息，
     * 有超过15秒的就更新这个消息的UI，并且移除消息队列，消息队列为空或者页面关闭时，停止这个计数器
     *
     */
    public  void  startDelayTime(IMConversationDetailBean message){
        if (timer == null) {
            timer = new Timer();
        }
        timerTask = new TimerTask() {
            @Override
            public void run() {
                handlerTimer();

            }
        };
        timer.schedule(timerTask,0,1000);
    }

    /**
     *定时器逻辑
     */
    private void handlerTimer() {
        if(sendlists.size()==0){
            if(timer!=null){
                timer.cancel();
                timer = null;
            }
        }
        for (int i = 0; i < sendlists.size(); i++) {
            long time = (System.currentTimeMillis() - sendlists.get(i).getTime()) / 1000;
            if(time>15){
                //保存数据库
                //更新会话数据库发送状态ad05b655-efaf-4514-b660-06476e154a4f
                List<IMConversationBean> dbdatas = daoUtils.queryAllConversationData();
                for (int j = 0; j < dbdatas.size();j++) {
                    if(!TextUtils.isEmpty(dbdatas.get(j).getLastMessageId())){
                        if (dbdatas.get(j).getConversationId().equals(sendlists.get(i).getConversationId())) {
                            dbdatas.get(j).setLastMessageSendstate(IMSConfig.IM_SEND_STATE_FAILE);
                            dbdatas.get(j).setLastMessageTime(sendlists.get(i).getTime());
                            daoUtils.updateConversationData(dbdatas.get(j));
                        }
                    }
                    //更新消息数据库发送状态
                    List<IMConversationDetailBean> datas = daoUtils.queryConversationDetailorderAscrdField(dbdatas.get(i).getConversationId());
                    for (int k= 0;k< datas.size(); k++) {
                        if(TextUtils.isEmpty(sendlists.get(i).getFingerId()) ||TextUtils.isEmpty(datas.get(k).getFingerprint())){
                            continue;
                        }
                        if (datas.get(k).getFingerprint().equals(sendlists.get(i).getFingerId())) {
                            datas.get(k).setSendstate(IMSConfig.IM_SEND_STATE_FAILE);
                            datas.get(k).setTimestamp(sendlists.get(i).getTime());
                            daoUtils.updateConversationDetailData(datas.get(k));
                            break;
                        }
                    }
                }
                sendlists.remove(i);
            }
        }
    }

    /**
     * 创建会话(接受到的)
     */
    private void creatReceiveConversation(IMMessageBean message) {
        IMConversationBean bean=null;
        if(!TextUtils.isEmpty(message.getGroupId())){
            bean= IMSMsgManager.creatReceiveGroupConversation(message);
            if(bean==null){
                getHttpGroupDetailData(message.getGroupId(),message,false,"");
            }
        }else {
            bean= IMSMsgManager.creatReceivePersonConversation(message);
            if(bean==null){
                getHttpPersonDetailData(message.getSenderId(),message,null);
            }
        }
        if(bean==null) {
            return;
        }
        daoUtils.insertConversationData(bean);
        //防止创建重复的item
        if(conversationlistBeans.size()>0){
            for (int i = 0; i < conversationlistBeans.size(); i++) {
                if(conversationlistBeans.get(i).getConversationId().equals(bean.getConversationId())){
                    conversationlistBeans.remove(i);
                }
            }
        }

        conversationlistBeans.add(0, bean);
        IMSMsgManager.getOrderData(conversationlistBeans);
        adapterNotifyDataSetChangedView();
        IMSManager.getInstance().getUnreadMessageNumber();
    }
    /**
     * 获取好有信息(如果本地没有相应的好友消息)
     * content是系统消息800001才有
     */
    private void getHttpPersonDetailData(String senderId,IMMessageBean messages,String content) {
        IMPersonBeans bean = new IMPersonBeans();
        bean.setCustomerId(senderId);
        String json = new Gson().toJson(bean);
        RequestBody body = RequestBody.create(JSON, json);
        IMHttpsService.GetMemberInforJson(body,new IMHttpResultObserver<IMUserInforBean.UserInforData>() {
            @Override
            public void onSuccess(IMUserInforBean.UserInforData data, String message) {
                if(data==null){
                    return;
                }
                IMPersonBean imPersonBean=new IMPersonBean();
                imPersonBean.setNickName(data.getNickName());
                imPersonBean.setCustomerId(data.getCustomerId());
                imPersonBean.setAvatar(data.getAvatar());
                imPersonBean.setMemberId(data.getCustomerId());

                //取出本地保存的未读条数，接到一条消息 未读消息加一，如果当前界面在对应的用户聊天界面，则在对应界面的接收消息出把值至为0
                List<IMConversationDetailBean> beans = daoUtils.queryConversationDetailDataAccordField(senderId);
                Log.e("-----beans---",(beans==null)+"");
                if(content==null){
                    daoUtils.insertMessageData(imPersonBean);
                    EventBus.getDefault().post(new IMAgreeAddFriendSucessEvent());
                    IMConversationBean bean1 = IMSMsgManager.creatReceivePersonConversation(messages);
                    daoUtils.insertConversationData(bean1);
                    //防止创建重复的item
                    if(conversationlistBeans.size()>0){
                        for (int i = 0; i < conversationlistBeans.size(); i++) {
                            if(conversationlistBeans.get(i).getConversationId().equals(bean1.getConversationId())){
                                conversationlistBeans.remove(i);
                            }
                        }
                    }
                    conversationlistBeans.add(0, bean1);
                    IMSMsgManager.getOrderData(conversationlistBeans);
                    adapterNotifyDataSetChangedView();
                } else {
                    imPersonBean.setLastmessage(content);
                    daoUtils.insertMessageData(imPersonBean);
                    EventBus.getDefault().post(new IMAgreeAddFriendSucessEvent());
                    IMConversationBean bean1 = IMSMsgManager.creatReceiveSystemPersonConversation(messages);
                    daoUtils.insertConversationData(bean1);
                    //防止创建重复的item
                    if(conversationlistBeans.size()>0){
                        for (int i = 0; i < conversationlistBeans.size(); i++) {
                            if(conversationlistBeans.get(i).getConversationId().equals(bean1.getConversationId())){
                                conversationlistBeans.remove(i);
                            }
                        }
                    }

                    conversationlistBeans.add(0, bean1);
                    IMSMsgManager.getOrderData(conversationlistBeans);
                    adapterNotifyDataSetChangedView();

                    String number = IMPreferenceUtil.getPreference_String(senderId + IMSConfig.IM_UNEREAD_CONVERSATION_CHAT, "0");
                    int unreadnumber = Integer.parseInt(number) + 1;
                    IMPreferenceUtil.setPreference_String(senderId + IMSConfig.IM_UNEREAD_CONVERSATION_CHAT, unreadnumber + "");
                    IMSManager.getInstance().getUnreadMessageNumber();

                    IMSMsgManager.getMessageID("我们现在已经是好友了，快来聊天吧！",IMSConfig.IM_MESSAGE_TEXT,senderId);
                }

            }

            @Override
            public void _onError(Throwable e) {
                Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void _onErrorLocal(Throwable e, String message, String code) {
                Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * 获取群信息(如果本地没有相应的群消息)
     */
    private void getHttpGroupDetailData(String groupId,IMMessageBean messages,boolean creatgroup,String name) {
        final IMGetIMMemberData bean=new IMGetIMMemberData();
        bean.setGroupId(groupId);
        String json = new Gson().toJson(bean);
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"),json);
        IMHttpsService.getGroupDataJson(body, new IMHttpResultObserver<IMGroupNoticeBean.IMGroupNoticeDetail>() {
            @Override
            public void onSuccess(final IMGroupNoticeBean.IMGroupNoticeDetail bean, String message) {
                if(bean==null || TextUtils.isEmpty(bean.getGroupId())){
                    return;
                }
                IMGroupBean groupbean=new IMGroupBean();
                groupbean.setGroupId(bean.getGroupId());
                groupbean.setGroupAvatar(bean.getGroupAvatar());
                groupbean.setGroupName(bean.getGroupName());
                groupbean.setGroupOwner(TextUtils.isEmpty(bean.getGroupOwner())?"":bean.getGroupOwner());
                groupbean.setMemberCount(bean.getMemberCount());
                daoUtils.insertGroupData(groupbean);
                IMConversationBean bean1 = IMSMsgManager.creatReceiveGroupConversation(messages);
                if(bean1==null){
                    return;
                }
                if(creatgroup){
                    bean1.setLastMessageContent(name+"进入了群聊");
                    if(messages.getTimestamp()==0){
                        bean1.setLastMessageTime(System.currentTimeMillis());
                    }else {
                        bean1.setLastMessageTime(messages.getTimestamp());
                    }
                }
                daoUtils.insertConversationData(bean1);

                //防止创建重复的item
                if(conversationlistBeans.size()>0){
                    for (int i = 0; i < conversationlistBeans.size(); i++) {
                        if(conversationlistBeans.get(i).getConversationId().equals(bean.getGroupId())){
                            conversationlistBeans.remove(i);
                        }
                    }
                }

                conversationlistBeans.add(0, bean1);
                IMSMsgManager.getOrderData(conversationlistBeans);
                adapterNotifyDataSetChangedView();
                IMSManager.getInstance().getUnreadMessageNumber();
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
     *置顶，通知，删除
     */
    @Override
    public void onClick(int type, int position,String content) {
        switch (type){
            case CLICK_TYPE_DELETE:
                daoUtils.deleteConversationDetailAccordField(conversationlistBeans.get(position-1).getConversationId());
                daoUtils.deleteConversationData(conversationlistBeans.get(position-1).getConversationId());
                IMPreferenceUtil.setPreference_String(conversationlistBeans.get(position-1).getConversationId() + IMSConfig.IM_UNEREAD_CONVERSATION_CHAT, "0");
                conversationlistBeans.remove(position-1);
                IMSManager.getInstance().getUnreadMessageNumber();
                IMSMsgManager.getOrderData(conversationlistBeans);
                adapter.notifyItemRemoved(position);
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        adapterNotifyDataSetChangedView();
                        IMSManager.getInstance().getUnreadMessageNumber();
                    }
                },900);

                break;
            case CLICK_TYPE_TOP:
                IMSMsgManager.getOrderData(conversationlistBeans);
                adapterNotifyDataSetChangedView();
                break;
            case CLICK_TYPE_NOTICE:
                IMSManager.getInstance().getUnreadMessageNumber();
                break;
            case CLICK_TYPE_SEARCH:
                List<IMConversationBean> beans =new ArrayList<>();
                List<IMConversationBean> beans1 = daoUtils.queryAllConversationData();
                if(beans1==null){
                    return;
                }
                if(!TextUtils.isEmpty(content)){
                    Pattern pattern = Pattern.compile(content);
                    for (int i = 0; i <beans1.size(); i++) {
                        Matcher matcher = pattern.matcher(beans1.get(i).getConversationName());
                        if(matcher.find()){
                            beans.add(beans1.get(i));
                        }
                    }
                }else {
                    beans.addAll(beans1);
                }
                conversationlistBeans.clear();
                conversationlistBeans.addAll(beans);
                isSearch=true;
                adapterNotifyDataSetChangedView();
                break;
        }
    }
    /**
     *  获取数据刷新界面
     */
    private void adapterNotifyDataSetChangedView() {
        if(adapter==null){
            adapter = new IMConversationnListAdapter(getActivity(), conversationlistBeans);
            mRecyclerView.setAdapter(adapter);
            adapter.setOnClickListener(this);
            adapter.setOnItemClickListner(this);
        }else {
            adapter.setType(isSearch);
            isSearch=false;
            adapter.notifyDataSetChanged();
        }

    }

    /**
     * 如果是用户上线下线状态
     */
    private void handlUserdataMessage(IMMessageBean message) {
        if(message.getUserStatus()== MessageProtobuf.ImMessage.UserStatusEnum.OFFLINE){
            IMPersonBean bean = DaoUtils.getInstance().queryMessageBean(message.getSenderId());
            if(bean==null){
                return;
            }
            bean.setIsOnline("N");
            DaoUtils.getInstance().updateMessageData(bean);
            adapterNotifyDataSetChangedView();
        }else if(message.getUserStatus()== MessageProtobuf.ImMessage.UserStatusEnum.ONLINE){
            IMPersonBean bean = DaoUtils.getInstance().queryMessageBean(message.getSenderId());
            if(bean==null){
                return;
            }
            bean.setIsOnline("Y");
            DaoUtils.getInstance().updateMessageData(bean);
            adapterNotifyDataSetChangedView();
        }else if(message.getUserStatus()== MessageProtobuf.ImMessage.UserStatusEnum.CHANGE){
            updataPerson(message);
            EventBus.getDefault().post(new IMAgreeAddFriendSucessEvent());
        }

    }

    public void changeChooseStated(){
        if(adapter!=null){
            adapter.setBianJi();
        }
    }

    /**
     * 获取链接状态
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getOffineMessage(IMMessageReceiveEvent event) {
        if(event.isIsloading().equals("2")){
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    setmTvtitle("聊天");
                }
            },600);

        }else if(event.isIsloading().equals("1")){

        }else if(event.isIsloading().equals("0")){
            setmTvtitle("未连接");
        }
    }

    public void setmTvtitle(final String title){
        try {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if(mTvTitle!=null){
                        mTvTitle.setText(title);
                    }
                }
            });
        }catch (Exception e){
            return;
        }
    }

    @Override
    public void OnViewItemListener(int position) {
        switch (position) {
            case 0:
                Intent intent = new Intent(getActivity(), IMCreatGroupActivity.class);
                getActivity().startActivity(intent);
                break;
            case 1:
                Intent intent1 = new Intent(getActivity(), IMAddFriendActivity.class);
                getActivity().startActivity(intent1);
                break;
            case 2:
                Intent intent2 = new Intent(getActivity(), IMQRCActivity.class);
                getActivity().startActivity(intent2);
                break;
            case 3:
                IMDialogUtils.getInstance().showCommonDialog(getActivity(), "您是否确认要把所有消息置为已读状态？", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        IMDialogUtils.getInstance().dismissCommonDiglog();
                        IMPopWindowUtil.getInstance(getActivity()).setTotleRed();   //更新数据库未读消息
                        EventBus.getDefault().post(new IMMessageNoticelGroupEvevt());
                        EventBus.getDefault().post(new IMMessageNoticelPersonEvevt());
                        IMSManager.getInstance().getUnreadMessageNumber();
                    }
                });
                break;
            case 4:   //代表加号消失
                if (imIvAdd2 == null) {
                    return;
                }
                imIvAdd2.startAnimation(operatingAnim1);
                break;
        }
    }
}
