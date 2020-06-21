package com.android.im.imui.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.im.IMSHttpManager;
import com.android.im.IMSManager;
import com.android.im.IMSMsgManager;
import com.android.im.R;
import com.android.im.imadapter.IMChatRecyclerAdapter;
import com.android.im.imbean.IMCompanyBean;
import com.android.im.imbean.IMSingleRedBackBean;
import com.android.im.imbean.UpdataPickureBean;
import com.android.im.imeventbus.IMAppFinishEvent;
import com.android.im.imeventbus.IMAppUpdataEvent;
import com.android.im.imeventbus.IMDeleteFriendSucessEvent;
import com.android.im.imeventbus.IMDeleteFriendSucessedEvent;
import com.android.im.imeventbus.IMGroupMessageEvent;
import com.android.im.imeventbus.IMMessagSingleRedEvevt;
import com.android.im.imeventbus.IMMessageDetelEvevt;
import com.android.im.imeventbus.IMMessageNoticelPersonEvevt;
import com.android.im.imeventbus.IMSendGroupMessageEvent;
import com.android.im.imeventbus.IMZhuaFaDataEvent;
import com.android.im.imeventbus.VideoBeanEvent;
import com.android.im.iminterface.IMUploadFileInterface;
import com.android.im.imnet.bean.IMUpdataFileBean;
import com.android.im.imutils.IMPersonChatUtils;
import com.android.im.imutils.IMSoftKeyBoardListener;
import com.android.im.imutils.IMStatusBarUtil;
import com.android.im.imutils.IMStopClickFast;
import com.android.im.imutils.IMUpdataFileUtils;
import com.android.im.imutils.SoundUtils;
import com.android.im.imview.IMEmojiLayoutView;
import com.android.im.imview.MyChatHeadView;
import com.android.nettylibrary.IMSConfig;
import com.android.nettylibrary.bean.IMMessageBean;
import com.android.nettylibrary.bean.IMMessageReceiveEvent;
import com.android.nettylibrary.greendao.entity.IMConversationBean;
import com.android.nettylibrary.greendao.entity.IMConversationDetailBean;
import com.android.nettylibrary.greendao.entity.IMPersonBean;
import com.android.nettylibrary.greendao.greenutils.DaoUtils;
import com.android.nettylibrary.http.IMMessageDataJson;
import com.android.nettylibrary.http.IMSystemNoGroupBean;
import com.android.nettylibrary.protobuf.MessageProtobuf;
import com.android.nettylibrary.utils.IMLogUtil;
import com.android.nettylibrary.utils.IMPreferenceUtil;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.gson.Gson;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.view.MP3RecordView;
import com.zhihu.matisse.Matisse;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


public class IMPersonalChatActivity extends IMBaseActivity implements View.OnClickListener, IMChatRecyclerAdapter.SendErrorListener, IMEmojiLayoutView.SendListener, IMUploadFileInterface, OnRefreshListener, MP3RecordView.OnRecordCompleteListener, View.OnTouchListener, IMSoftKeyBoardListener.OnSoftKeyBoardChangeListener, IMChatRecyclerAdapter.onItemClickInterface {
    public static final int REQUEST_CODE_CHOOSE = 10001;
    public static final int REQUEST_CODE_CHOOSE_VIDEO = 10002;
    private LinearLayout mLlContent;
    public IMEmojiLayoutView ll_emoji;
    private RecyclerView mRecycle;
    private LinearLayout mllcontainer;
    private LinearLayout ll_imrecharge;
    private ImageView mIvFinish;
    public TextView mTvTitle,mTvState;
    private ImageView mIvRight;
    public IMChatRecyclerAdapter adapter;
    private List<IMConversationDetailBean> messagelist = new ArrayList();
    private DaoUtils daoUtils;
    private String userId;
    private IMConversationBean customer;
    private RelativeLayout mrltop;
    private TextView mtvtop;
    private RelativeLayout mLlButtom;
    private RelativeLayout mllRight;
    private TextView mtvButtom;
    private MP3RecordView mViewRecord;
    private ImageView mIvBg;
    private int page = 0;
    private int pageSize = 20;
    private int addtopnumber = 0;//用来记录多少条新消息
    private IMUpdataFileUtils imUpdataFileUtils;
    private Handler handler=new Handler();

    private String newNumber;//多少条新消息
    private int buttomNumer; //多少质底消息
    private int visibleItemCount;//获取它的启示可见item数
    private int firstCompletelyVisibleItemPosition;//获取它的可见firstitem的位置
    private RefreshLayout refreshLayout;
    private int type;//消息类型
    public TextView mTvForBit;
    private IMPersonChatUtils chatUtils;
    private List<IMConversationDetailBean> sendlists = new ArrayList<>();//用来记录发送期间的消息列表
    private boolean textswith=true;
    private boolean picswith=true;
    private boolean redswith=true;
    public List<String> mSelected;//选择图片处理
    public String ownUrl;
    private LinearLayoutManager layoutManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_im_chat);
        EventBus.getDefault().register(this);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        IMStatusBarUtil.setTranslucent(this,0);
        IMStatusBarUtil.setLightMode(this);
        initFindViewById();
        initListener();
        initView();
        initData();
    }

    private void initFindViewById() {
        mIvFinish = findViewById(R.id.tv_top_finish);
        mTvTitle = findViewById(R.id.tv_top_title);
        mTvState= findViewById(R.id.tv_top_state);
        mIvRight = findViewById(R.id.iv_top_right);
        mIvRight.setImageResource(R.mipmap.im_chat_title_more);
        mLlContent = findViewById(R.id.llContent);
        mIvBg = findViewById(R.id.im_iv_bg);
        ll_imrecharge = findViewById(R.id.ll_imrecharge);
        mrltop = findViewById(R.id.im_rl_top);
        mtvtop = findViewById(R.id.im_tv_top);
        mLlButtom = findViewById(R.id.im_rl_buttom);
        mtvButtom = findViewById(R.id.im_tv_buttom);
        mllRight= findViewById(R.id.ll_right);
        mTvForBit = findViewById(R.id.im_tv_cancle);
        mRecycle = findViewById(R.id.recycle);
        ll_emoji = findViewById(R.id.ll_emoji);
        mViewRecord =findViewById(R.id.view_record);
        refreshLayout =  findViewById(R.id.refreshLayout);
    }

    private void initListener() {
        daoUtils = DaoUtils.getInstance();
        mIvFinish.setOnClickListener(this);
        mrltop.setOnClickListener(this);
        mLlButtom.setOnClickListener(this);
        mllRight.setOnClickListener(this);
        mTvForBit.setOnClickListener(this);
        chatUtils = new IMPersonChatUtils(this);
        ((View) refreshLayout).setOnTouchListener(this);
        imUpdataFileUtils=new IMUpdataFileUtils(this);
        mViewRecord.setOnRecordCompleteListener(this);
        IMSoftKeyBoardListener.setListener(this,this);
        ll_emoji.setContentView(mLlContent, false);
        ll_emoji.setOnSendListener(this);
        ll_emoji.setAudioRecord(mViewRecord);
    }

    public void initView() {
        chatUtils.setBgUrl(mIvBg);
        initRecycleView();
        RefreshHandView();
    }

    /**
     * 初始化RecyceView
     */
    private void initRecycleView() {
        layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        layoutManager.setReverseLayout(true);
        mRecycle.setLayoutManager(layoutManager);
        mRecycle.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                ll_emoji.hidellMore();
                if (!recyclerView.canScrollVertically(1)) {   //
                    buttomNumer = 0;
                    mLlButtom.setVisibility(View.GONE);
                }
            }
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                visibleItemCount = layoutManager.getChildCount();
                firstCompletelyVisibleItemPosition = layoutManager.findFirstCompletelyVisibleItemPosition();
            }
        });
    }
    /**
     * 上拉下拉刷新数据
     */
    private void RefreshHandView() {
        refreshLayout.setEnableRefresh(true);//是否启用下拉刷新功能
        refreshLayout.setEnableLoadMore(false);//是否启用上拉加载功能
        refreshLayout.setRefreshHeader(new MyChatHeadView(this));
        refreshLayout.setReboundDuration(100);
        refreshLayout.setOnRefreshListener(this);
    }

    private void initData() {
        userId = IMPreferenceUtil.getPreference_String(IMSConfig.SAVE_USERID, IMSConfig.USER_ID);
        customer = (IMConversationBean) getIntent().getSerializableExtra("user");
        if(customer==null || TextUtils.isEmpty(customer.getMemberId())){
            return;
        }
        if(customer.getConversationId().equals(IMSConfig.CLX_ID)){  //如果是彩乐信团队
            caiLeXinTeamVisible();
        }else{
            caiLeXinTeamGone();//隐藏彩乐信团队界面
            getTitleName();  //获取标题名字
            setForBitVisible();//是不是把好友屏蔽
            showUnreadNumber(); //显示几条新消息
        }
        //把外部未读消息置,刷新首页角标
        IMPreferenceUtil.setPreference_String(customer.getMemberId() + IMSConfig.IM_UNEREAD_CONVERSATION_CHAT, "0");
        IMSManager.getInstance().getUnreadMessageNumber();
        //获取数据库数据
        getListDataView();
    }


    /**
     * 显示彩乐信团队
     */
    private void caiLeXinTeamVisible() {
        ll_emoji.setVisibility(View.GONE);
        mIvRight.setVisibility(View.GONE);
        mTvTitle.setText(customer.getConversationName());
    }
    /**
     * 隐藏彩乐信团队
     */
    private void caiLeXinTeamGone() {
        ll_emoji.setVisibility(View.VISIBLE);
        mIvRight.setVisibility(View.VISIBLE);
        chatUtils.getPerosonState(customer);
    }

    /**
     * 获取标题名字
     */
    private void getTitleName() {
        IMPersonBean bean = daoUtils.queryMessageBean(customer.getConversationId());
        if(bean!=null){
            if(TextUtils.isEmpty(bean.getNickName())){
                mTvTitle.setText(customer.getConversationName());
            }else {
                if(bean.getNickName().length()>10){
                    mTvTitle.setText(bean.getNickName().substring(0,10)+"...");
                }else {
                    mTvTitle.setText(bean.getNickName());
                }
                customer.setAvatar(bean.getAvatar());
            }
        }else {
            mTvTitle.setText(customer.getConversationName());
        }
    }
    /**
     * 是不是屏蔽了该好友
     */
    public void  setForBitVisible(){
        boolean isforbit = IMPreferenceUtil.getPreference_Boolean(customer.getConversationId() + IMSConfig.IM_CONVERSATION_TOUSU, false);
        if(isforbit){
            mTvForBit.setVisibility(View.VISIBLE);
            ll_emoji.setVisibility(View.GONE);
        }else {
            mTvForBit.setVisibility(View.GONE);
            ll_emoji.setVisibility(View.VISIBLE);
        }
    }
    /**
     * 显示未读消息数目和发送已读消息
     */
    private void showUnreadNumber() {
        //显示几条新消息
        newNumber = IMPreferenceUtil.getPreference_String(customer.getMemberId() + IMSConfig.IM_UNEREAD_CONVERSATION_CHAT, "1");
        if (Integer.parseInt(newNumber) > 5 && Integer.parseInt(newNumber) > visibleItemCount) {
            mrltop.setVisibility(View.VISIBLE);
            mtvtop.setText(newNumber + "条新消息");
        }
        //打开当前界面是发送一条全部已读的消息
        IMSMsgManager.sendReadMessage(null, customer.getMemberId());
    }

    /**
     * 获取数据库数据
     */
    private void getListDataView() {
        List<IMConversationDetailBean> beans = daoUtils.queryConversationDetailDataAccordField(customer.getMemberId(), page * pageSize + addtopnumber, pageSize);
        if (beans == null || beans.size() == 0) {
            return;
        }
        for (int i = 0; i < beans.size(); i++) {
            if(beans.get(i).getSendstate()==IMSConfig.IM_SEND_STATE_SENDING){
                beans.get(i).setSendstate(IMSConfig.IM_SEND_STATE_FAILE);
                daoUtils.updateConversationDetailData(beans.get(i));
            }
        }
        page = page + 1;
        messagelist.addAll(beans);
        adapter = new IMChatRecyclerAdapter(this, messagelist, customer);
        adapter.setOnItemClickListener(this);
        mRecycle.setAdapter(adapter);
        adapter.setSendErrorListener(this);
    }

    @Override
    public void onClick(View v) {
        if (IMStopClickFast.isFastClick()) {
            if (v.getId() == R.id.ll_right) {
                chatUtils.rightClick(customer);
            } else if (v.getId() == R.id.tv_top_finish) {
                finish();
            } else if (v.getId() == R.id.im_rl_top) {
                rightTopClick();
            } else if (v.getId() == R.id.im_rl_buttom) {
                mLlButtom.setVisibility(View.GONE);
                mRecycle.scrollToPosition(0);
            } else if (v.getId() == R.id.im_tv_cancle) {
                cancleClick();
            }
        }
    }
    /**
     * 取消屏蔽提示框
     */
    private void cancleClick() {
        showCommonDialog("您确认要取消屏蔽吗？", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dissCommonDialog();
                chatUtils.httpShieldData("N",customer);
            }
        });
    }
    /**
     * 点击多少新消息
     */
    private void rightTopClick() {
        mrltop.setVisibility(View.GONE);
        addtopnumber = Integer.parseInt(newNumber);
        if (addtopnumber > pageSize) {   //当未读消息大于一页10条消息时，请求未读消息的条数，后续的下拉刷新都要加这个数字，默认为0
            List<IMConversationDetailBean> beans = daoUtils.queryConversationDetailDataAccordField(customer.getMemberId(), 0, addtopnumber);
            if (beans == null || beans.size() == 0) {
                return;
            }
            page = 0;
            messagelist.clear();
            messagelist.addAll(beans);
            adapter.notifyDataSetChanged();
        }
        mRecycle.smoothScrollToPosition(addtopnumber);
    }

    /**
     * 发送消息(先调用获取消息id的接口)
     */
    public void getMessageID(final EditText view, final String content, final IMMessageDataJson bean, final int type) {
        String messageFigId = IMSMsgManager.getMessageFigId();
        if(!TextUtils.isEmpty(messageFigId)){
            sendMessage(view,content,bean,type,messageFigId);
        }else {
            String Uid= UUID.randomUUID().toString();
            sendMessage(view,content,bean,type,Uid);
            IMSHttpManager.getInstance().IMHttpGetIMToken(false);
        }
        //先判断消息池子是不是有20条消息，不够就请求50条消息
        if(IMSMsgManager.getNeedGetMessageIds()) {
            IMSHttpManager.getInstance().IMHttpGetIMFigId();
        }
    }
    /**
     * 发送消息(先调用获取消息id的接口)
     */
    private void sendMessage(EditText view, String content, IMMessageDataJson bean, int type,String uid) {
        //构造一个bean更新界面,把内容传进去跟新当前界面
        final IMConversationDetailBean message = new IMConversationDetailBean();
        message.setFingerprint(uid);
        message.setReceiverId(customer.getMemberId());
        message.setConversationId(customer.getMemberId());
        message.setMsgType(type);
        message.setData(IMSMsgManager.handleMessageDataJson(null, content, bean, type));
        message.setTimestamp(System.currentTimeMillis());
        message.setSendstate(IMSConfig.IM_SEND_STATE_SENDING); //发送中
        if(bean!=null && !TextUtils.isEmpty(bean.getOwnerurl())){
            message.setOwnurl(bean.getOwnerurl());
        }
        messagelist.add(0, message);     //发送一条新消息 跟新ui
        if (messagelist.size() == 1) {
            chatUtils.creatReceivePersonConversation(message,customer);
            adapter = new IMChatRecyclerAdapter(IMPersonalChatActivity.this, messagelist, customer);
            mRecycle.setAdapter(adapter);
            adapter.setSendErrorListener(this);
            adapter.setOnItemClickListener(this);
        } else {
            adapter.notifyDataSetChanged();
        }
        mRecycle.scrollToPosition(0);
        IMSMsgManager.sendMessage(message, customer.getMemberId(), daoUtils,false);//
        EventBus.getDefault().post(new IMSendGroupMessageEvent(customer.getMemberId(), message)); //通知上一个页面跟新它的发送状态
        if (view != null) {
            view.setText("");
        }
        chatUtils.startDelayTime(message,sendlists,messagelist,adapter);//发送十秒钟不成功就显示发送失败
    }

    /**
     * 发送消息失败，点击重新发送
     */
    @Override
    public void onClick(IMConversationDetailBean bean) {
        messagelist.remove(bean);
        bean.setSendstate(IMSConfig.IM_SEND_STATE_SENDING);
        bean.setTimestamp(System.currentTimeMillis());
        messagelist.add(0, bean);
        if (messagelist.size() == 1) {
            chatUtils.creatReceivePersonConversation(bean,customer);
            adapter = new IMChatRecyclerAdapter(IMPersonalChatActivity.this, messagelist, customer);
            adapter.setOnItemClickListener(this);
            adapter.setSendErrorListener(this);
            mRecycle.setAdapter(adapter);
        } else {
            adapter.notifyDataSetChanged();
        }

        if(bean.getMsgType()==IMSConfig.IM_MESSAGE_PICTURE && !TextUtils.isEmpty(bean.getOwnurl())){
            updataPickAgain(bean);
        }else if(bean.getMsgType()==IMSConfig.IM_MESSAGE_AUDIO && !TextUtils.isEmpty(bean.getOwnurl())){
            updataAudioAgain(bean);
        }else if(bean.getMsgType()==IMSConfig.IM_MESSAGE_VIDEO && !TextUtils.isEmpty(bean.getOwnurl())){
            updataVideoAgain(bean);
        }else{
            IMSMsgManager.sendMessage(bean, customer.getMemberId(), daoUtils, false);
        }
        EventBus.getDefault().post(new IMSendGroupMessageEvent(customer.getMemberId(), bean)); //通知上一个页面跟新它的发送状态
        chatUtils.startDelayTime(bean,sendlists,messagelist,adapter);//发送十秒钟不成功就显示发送失败
    }

    /**
     * 发送消息
     */
    @Override
    public void onSendListener(EditText view, int type) {
        switch (type){
            case IMEmojiLayoutView.TYPE_SEND_MESSAGE:
                chatUtils.sendTextMessage(textswith,view);
                break;
            case IMEmojiLayoutView.TYPE_CHOOSE_PHOTO:
                chatUtils.sendPicMessage(picswith);
                break;
            case IMEmojiLayoutView.TYPE_SHARE_MESSAGE:
                chatUtils.ShareMessage();
                break;
            case IMEmojiLayoutView.TYPE_SEND_REDPICK:
                chatUtils.sendRedPacketMessage(customer,redswith);
                break;
            case IMEmojiLayoutView.TYPE_SEND_VIDEO:
                chatUtils.sendVideoMessage(picswith);
                break;
            case IMEmojiLayoutView.TYPE_PlAY:
                try{
                    adapter.setAudioStoped();
                }catch (Exception e){
                    return;
                }
                break;
            case IMEmojiLayoutView.TYPE_SET_TOP:
                if(mRecycle!=null){
                    mRecycle.scrollToPosition(0);
                }
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        ll_emoji.onResume();
    }
    @Override
    protected void onStop() {
        if(adapter!=null){
            adapter.stop();
        }
        super.onStop();
    }

    @Override
    protected void onPause() {
        if(adapter!=null){
            adapter.pause();
        }
        super.onPause();
    }
    @Override
    public void onBackPressed() {
        if (ll_emoji.onBackPressed()) {
            super.onBackPressed();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (chatUtils != null) {
            chatUtils.ondesory();
        }
        if(adapter!=null){
            adapter.onDestroy();
        }
        EventBus.getDefault().unregister(this);
    }

    /**
     * 处理接收到服务器的数据
     * 如果当前界面在对应的用户聊天界面，则在对应界面的接收消息出把未读消息值至为0
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(IMGroupMessageEvent event) {
        if(event.isIsgroup()){
            return;
        }
        IMMessageBean message = event.getMessageMessage();
        if(event != null && event.getMessageMessage().getType() == MessageProtobuf.ImMessage.TypeEnum.USERSTATUS){
            return;
        } else if(event != null && event.getMessageMessage().getType() == MessageProtobuf.ImMessage.TypeEnum.SYSTEM){
            handlerSystemMessage(message);
        } else if (event != null && event.getMessageMessage().getType() == MessageProtobuf.ImMessage.TypeEnum.READ) {
            chatUtils.recieveRead(message,messagelist,adapter);
        } else if (event != null && event.getMessageMessage().getType() == MessageProtobuf.ImMessage.TypeEnum.SERVERRECEIPT) {
            handlerServerReceipt(message);
        } else {  //不是回执消息代表主动接收到消息
            handlerMessageData(message);
        }
    }
    /**
     *
     * 主动接收到消息
     */
    private void handlerMessageData(IMMessageBean message) {
        if (message != null && !TextUtils.isEmpty(message.getData()) && message.getSenderId().equals(customer.getMemberId())) {
            //不是回执消息代表主动接收到消息
            IMPreferenceUtil.setPreference_String(customer.getMemberId() + IMSConfig.IM_UNEREAD_CONVERSATION_CHAT, "0");
            //显示底部新消息逻辑
            if (firstCompletelyVisibleItemPosition > 2) {
                mLlButtom.setVisibility(View.VISIBLE);
                buttomNumer = buttomNumer + 1;
                mtvButtom.setText(buttomNumer + "");
            } else {
                mLlButtom.setVisibility(View.GONE);
                buttomNumer = 0;
            }
            IMConversationDetailBean data = new IMConversationDetailBean();
            data.setFingerprint(message.getFingerprint());
            data.setSenderId(message.getSenderId());
            data.setReceiverId(IMSManager.getMyUseId());
            data.setMsgType(message.getMsgType());
            data.setLastMsgSendType(IMSConfig.MSG_RECIEVE);
            data.setData(message.getData());
            data.setTimestamp(message.getTimestamp());
            messagelist.add(0, data);
            if (messagelist.size() == 1) {
                adapter = new IMChatRecyclerAdapter(IMPersonalChatActivity.this, messagelist, customer);
                adapter.setOnItemClickListener(this);
                adapter.setSendErrorListener(this);
                mRecycle.setAdapter(adapter);
            } else {
                adapter.notifyDataSetChanged();
            }
            //当前界面接收到一条消息发送一条已读的回执
            IMSMsgManager.sendReadMessage(message.getFingerprint(), customer.getMemberId());
            SoundUtils.playSong(this, R.raw.receive);
        }
    }

    /**
     * 处理回执消息
     */
    private void handlerServerReceipt(IMMessageBean message) {
        //更新UI发送状态
        for (int i = 0; i < sendlists.size(); i++) {
            if (sendlists.get(i).getFingerprint().equals(message.getFingerprint())) {
                sendlists.remove(i);
            }
        }
        for (int i = 0; i < messagelist.size(); i++) {
            if (TextUtils.isEmpty(messagelist.get(i).getFingerprint())) {
                continue;
            }
            if (messagelist.get(i).getFingerprint().equals(message.getFingerprint())) {
                messagelist.get(i).setSendstate(IMSConfig.IM_SEND_STATE_SUCCESS);
                type=chatUtils.Handlemessage(messagelist.get(i));
            }
        }
        adapter.notifyDataSetChanged();
        if(type==IMSConfig.MSG_RIGHT_AUDIO){//声音
            SoundUtils.playSong(this,R.raw.send_sound);
        }else {
            SoundUtils.playSong(this,R.raw.send);
        }
    }
    /**
     * 系统消息(已经不是好友了)
     */
    private void handlerSystemMessage(IMMessageBean message) {
        if(message.getData()==null) {
            return;
        }
        IMPreferenceUtil.setPreference_String(customer.getMemberId() + IMSConfig.IM_UNEREAD_CONVERSATION_CHAT, "0");
        IMSystemNoGroupBean dataJson = new Gson().fromJson(message.getData(), IMSystemNoGroupBean.class);

        switch (Integer.parseInt(dataJson.getSysCode())) {
            case IMSConfig.IM_MESSAGE_SEND_NO_FRIEND:
                handlerForbitFriend(message, getResources().getString(R.string.is_not_friend));
                break;
            case IMSConfig.IM_MESSAGE_FORBIT_FRIEND:
                handlerForbitFriend(message, getResources().getString(R.string.you_forbit));
                break;
            case IMSConfig.IM_MESSAGE_CLX_TEAM:
            case IMSConfig.IM_MESSAGE_CLX_fENG:
                handlerClxMessage(message);
                break;
        }
    }

    /**
     * 处理彩乐信团队消息
     */
    private void handlerClxMessage(IMMessageBean message) {
        page=0;
        //不是回执消息代表主动接收到消息
        List<IMConversationDetailBean> beans = daoUtils.queryConversationDetailDataAccordField(customer.getMemberId(), page * pageSize + addtopnumber, pageSize);
        if (beans == null) {
            return;
        }
        messagelist.clear();
        messagelist.addAll(beans);
        adapter.notifyDataSetChanged();
    }
    /**
     * (您已被屏蔽 或是你们不是好友  )发送失败
     */
    private void handlerForbitFriend(IMMessageBean message,String content) {
        for (int i = 0; i < messagelist.size(); i++) {
            if (TextUtils.isEmpty(messagelist.get(i).getFingerprint())) {
                continue;
            }
            if(messagelist.get(i).getSendstate()==IMSConfig.IM_SEND_STATE_SENDING){
                messagelist.get(i).setSendstate(IMSConfig.IM_SEND_STATE_FAILE);
                messagelist.get(i).setTimestamp(message.getTimestamp());
                addNoFriendMessage(message,content);
                adapter.notifyDataSetChanged();
            }
        }
    }
    /**
     * 提示你们已经不是好友
     */
    private void addNoFriendMessage(IMMessageBean message,String content) {
        IMConversationDetailBean data= new IMConversationDetailBean();
        data.setMsgType(IMSConfig.IM_MESSAGE_SYSTEM);
        data.setLastMsgSendType(IMSConfig.MSG_RECIEVE);
        data.setData(content);
        data.setTimestamp(message.getTimestamp()+1);
        messagelist.add(0,data);
        //保存数据库
        IMConversationDetailBean bean=new IMConversationDetailBean();
        bean.setConversationId(customer.getConversationId());
        bean.setMsgType(IMSConfig.IM_MESSAGE_SYSTEM);
        bean.setData(content);
        bean.setTimestamp(message.getTimestamp()+1);
        bean.setFingerprint(UUID.randomUUID()+"");
        daoUtils.insertConversationDetailData(bean);
        EventBus.getDefault().post(new IMDeleteFriendSucessedEvent(message.getReceiverId()));
    }
    /**
     * 清空消息记录刷新界面
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(IMMessageDetelEvevt event) {
        messagelist.clear();
        mRecycle.setAdapter(null);
    }

    /**
     * 添加好友删除好友需要刷新好友界面
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(IMDeleteFriendSucessEvent event) {
        finish();
    }


    /**
     * 自己屏蔽该好友
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onNoticeEvent(IMMessageNoticelPersonEvevt event) {
        setForBitVisible();
    }
    /**
     * 红包成功发送消息
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(IMMessagSingleRedEvevt event) {
        IMSingleRedBackBean bean = event.getImRedBackBean();
        IMPersonBean customer = event.getCustomer();
        if(bean==null){
            return;
        }
        IMMessageDataJson json=new IMMessageDataJson();
        json.setTitle(bean.getRedPacket().getTitle());
        json.setRedPacketId(bean.getRedPacket().getRedPacketId());
        json.setDesc(bean.getRedPacket().getDesc());
        json.setStyle("STYLE_DEFAULT");
        json.setType(bean.getRedPacket().getType());
        getMessageID(null,"[红包]",json,IMSConfig.IM_MESSAGE_REPACKED);
    }
    /**
     * 转发图片视屏更新当前界面
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onZhuanfaData(IMZhuaFaDataEvent event) {
        String conversonId = event.getConversonId();
        if(!conversonId.equals(customer.getConversationId())){
            return;
        }
        messagelist.add(0,event.getBean());
        adapter.notifyDataSetChanged();
    }


    public void hanleTotleSwith() {
        String dataJson = IMPreferenceUtil.getPreference_String(IMSConfig.SAVE_AVAILAVBLE_SWICH, "");
        if(!TextUtils.isEmpty(dataJson)){
            IMCompanyBean bean = new Gson().fromJson(dataJson, IMCompanyBean.class);
            if(bean.getIsAvailable().equals("N")){
                ll_emoji.setViewEnble(false);
                ll_emoji.setContent(getResources().getString(R.string.im_swith_no));
            }else {
                if(bean.getSwitchOfText().equals("N")){
                    textswith=false;
                }else {
                    textswith=true;
                }
                if(bean.getSwitchOfPic().equals("N")){
                    picswith=false;
                }else {
                    picswith=true;
                }
                if(bean.getSwitchOfRedpacket().equals("N")){
                    redswith=false;
                }else {
                    redswith=true;
                }
            }
        }
    }

    /**
     * 处理总开关的系统消息
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onUpdateHeadView(IMAppUpdataEvent event) {
        if(event==null || TextUtils.isEmpty(event.getDataJson())){
            return;
        }
        IMCompanyBean bean = new Gson().fromJson(event.getDataJson(), IMCompanyBean.class);
        if(bean.getSwitchOfText().equals("N")){
            textswith=false;
        }else {
            textswith=true;
        }
        if(bean.getSwitchOfPic().equals("N")){
            picswith=false;
        }else {
            picswith=true;
        }
        if(bean.getSwitchOfRedpacket().equals("N")){
            redswith=false;
        }else {
            redswith=true;
        }
    }
    /**
     * 获取离校消息接收种
     */
    public boolean getOffLinemessage=false;//默认不是在获取离线消息的情况
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getOffineMessage(IMMessageReceiveEvent event) {
        if(event.isIsloading().equals("1")){
            getOffLinemessage=true;
        }else {
            getOffLinemessage=false;
        }
    }
    /**
     * 处理群系统通知打开（每日弹窗）
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onUpdateHeadView(IMAppFinishEvent event) {
        finish();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_CHOOSE && resultCode == RESULT_OK) {   //选择图片成功之后上传
            mSelected = Matisse.obtainPathResult(data);
            if(mSelected==null && mSelected.size()==0){
                return;
            }
            if(!mSelected.get(0).endsWith("jpg") && !mSelected.get(0).endsWith("png") && !mSelected.get(0).endsWith("jpeg")){
                updataPickture(mSelected,true);
            }else {
                updataPickture(mSelected,false);
            }
        }else  if (requestCode == REQUEST_CODE_CHOOSE_VIDEO && resultCode == RESULT_OK) {   //选择视屏成功之后上传
            mSelected = Matisse.obtainPathResult(data);
            if(mSelected==null && mSelected.size()==0){
                return;
            }
            updataPickture(mSelected,true);
        }
    }

    /**
     * 录制视频回调
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onUpdateHeadView(VideoBeanEvent event) {
        if(TextUtils.isEmpty(event.getUrl())){
            Toast.makeText(this, "拍摄失败", Toast.LENGTH_SHORT).show();
            return;
        }
        if(event.isVideo()) {
            ownUrl = event.getUrl();
            UpdataPickureBean datas = new UpdataPickureBean();
            datas.setUrl(event.getUrl());
            datas.setTime(System.currentTimeMillis());
            datas.setThumbImagePath("");
            sendVideoData(datas);
            imUpdataFileUtils.handleUpdataVideo(IMPersonalChatActivity.this, datas);
        }else {
            List<String>  datas=new ArrayList<>();
            datas.add(event.getUrl());
            imUpdataFileUtils.handleCompressPickure(this,datas,true);
        }
    }
    /**
     * 上传图片和视频
     */
    private void updataPickture(final List<String> mSelected,final boolean isVideo) {
        if(isVideo){
            for (int i = 0; i < mSelected.size(); i++) {
                ownUrl = mSelected.get(i);
                UpdataPickureBean datas=new UpdataPickureBean();
                datas.setUrl(mSelected.get(i));
                datas.setTime(System.currentTimeMillis());
                datas.setThumbImagePath("");
                sendVideoData(datas);
                imUpdataFileUtils.handleUpdataVideo(IMPersonalChatActivity.this,datas);
            }
        }else {   //获取图片之后先压缩在上传
            imUpdataFileUtils.handleCompressPickure(IMPersonalChatActivity.this,mSelected,true);
        }
    }


    /**
     * 图片先上屏幕
     */
    public void sendPickureDatas(UpdataPickureBean bean) {
        final IMMessageDataJson beans = new IMMessageDataJson();
        if(TextUtils.isEmpty(bean.getUrl())){
            return;
        }
        //获取图片真正的宽高
        Glide.with(IMSManager.getInstance().getContext()).asBitmap().load(bean.getUrl()).into(new SimpleTarget<Bitmap>() {
            @Override
            public void onResourceReady(Bitmap bitmap, Transition<? super Bitmap> transition) {
                int width = bitmap.getWidth();
                int height = bitmap.getHeight();
                beans.setOwnerurl(bean.getUrl());
                beans.setHight(height+"");
                beans.setWidth(width+"");
                getMessagePickID(null, "[图片]",IMSConfig.IM_MESSAGE_PICTURE, beans,bean.getTime());
                IMLogUtil.d("MyOwnTag:", "IMSMsgManager ----" +width+"---height=-"+height);
            }
        });

    }
    /**
     * 音频先上屏幕
     */
    public void sendAudioData(UpdataPickureBean beans) {
        if(beans==null || TextUtils.isEmpty(beans.getUrl()) ){
            Toast.makeText(IMPersonalChatActivity.this, "录音失败，请重新发送", Toast.LENGTH_SHORT).show();
            return;
        }
        IMMessageDataJson bean = new IMMessageDataJson();
        bean.setDuration(beans.getDurtime()+"");
        bean.setOwnerurl(beans.getUrl());
        bean.setAudioUrl("");
        getMessagePickID(null, "[语音]", IMSConfig.IM_MESSAGE_AUDIO, bean,beans.getTime());
    }
    /**
     * 视频先上屏幕
     */
    public void sendVideoData(UpdataPickureBean beans) {
        if(beans==null || TextUtils.isEmpty(beans.getUrl()) ){
            return;
        }
        IMMessageDataJson bean = new IMMessageDataJson();
        bean.setVideoUrl("");
        bean.setFirstFrame("");
        bean.setFileType("");
        bean.setOwnerurl(beans.getUrl());
        bean.setOwnerfirstFrame(beans.getThumbImagePath());
        getMessagePickID(null, "[视频]", IMSConfig.IM_MESSAGE_VIDEO, bean,beans.getTime());
    }

    /**
     * 发送消息(先调用获取消息id的接口)
     */
    public void getMessagePickID(final EditText view, final String content,int type, final IMMessageDataJson bean, long time) {
        String messageFigId = IMSMsgManager.getMessageFigId();
        if(!TextUtils.isEmpty(messageFigId)){
            sendPicMessage(view,content,bean,messageFigId,time,type);
        }else {
            String Uid= UUID.randomUUID().toString();
            sendPicMessage(view,content,bean,Uid,time,type);
            IMSHttpManager.getInstance().IMHttpGetIMToken(false);
        }
        //先判断消息池子是不是有20条消息，不够就请求50条消息
        if(IMSMsgManager.getNeedGetMessageIds()) {
            IMSHttpManager.getInstance().IMHttpGetIMFigId();
        }
    }
    /**
     * 发送消息(图片)
     */
    private void sendPicMessage(EditText view, String content, IMMessageDataJson bean,String uid,long time,int type) {
        //构造一个bean更新界面,把内容传进去跟新当前界面
        IMConversationDetailBean message= chatUtils.creatIMConversationDetailBean(uid,time,type,content,bean,customer);
        messagelist.add(0, message);     //发送一条新消息 跟新ui
        if (messagelist.size() == 1) {
            chatUtils.creatReceivePersonConversation(message,customer);
            adapter = new IMChatRecyclerAdapter(IMPersonalChatActivity.this, messagelist, customer);
            mRecycle.setAdapter(adapter);
            adapter.setSendErrorListener(this);
            adapter.setOnItemClickListener(this);
        } else {
            adapter.notifyDataSetChanged();
        }
        mRecycle.scrollToPosition(0);
        IMSMsgManager.sendMessage(message, customer.getMemberId(),daoUtils,true);//bushi不是真正的发送，只是保存数据库
        EventBus.getDefault().post(new IMSendGroupMessageEvent(customer.getMemberId(), message)); //通知上一个页面跟新它的发送状态
        if (view != null) {
            view.setText("");
        }
        chatUtils.startDelayTime(message,sendlists,messagelist,adapter);//发送十秒钟不成功就显示发送失败
    }



    /**
     * 上传资源后发送消息（图片）
     */
    @Override
    public void onUpLoadImageListener(IMUpdataFileBean imUpdataFile,String path,UpdataPickureBean data) {
        if(imUpdataFile==null ||imUpdataFile.data==null ||imUpdataFile.data.getFilePath()==null ){
            return;
        }
        for (int i = 0; i < messagelist.size(); i++) {
            if(data.getTime()==messagelist.get(i).getPictureTime()){
                List<String> images = new ArrayList<>();
                List<String> thumbImages = new ArrayList<>();
                images.add(imUpdataFile.data.getFilePath());
                thumbImages.add(imUpdataFile.data.getThumbnailpath());
                String data1 = messagelist.get(i).getData();
                IMMessageDataJson bean = new Gson().fromJson(data1, IMMessageDataJson.class);
                bean.setImages(images);
                bean.setThumbImages(thumbImages);
                messagelist.get(i).setData(new Gson().toJson(bean));
                adapter.notifyDataSetChanged();
                IMSMsgManager.sendMessage( messagelist.get(i), customer.getMemberId(), daoUtils,false);
            }
        }
    }

    /**
     * 上传资源后发送消息（录音）
     */
    @Override
    public void onUpLoadAudioListener(IMUpdataFileBean imUpdataFile, UpdataPickureBean data) {

        if(imUpdataFile==null ||imUpdataFile.data==null ||imUpdataFile.data.getFilePath()==null ){
            return;
        }
        for (int i = 0; i < messagelist.size(); i++) {
            if(data.getTime()==messagelist.get(i).getPictureTime()){

                String data1 = messagelist.get(i).getData();
                IMMessageDataJson bean = new Gson().fromJson(data1, IMMessageDataJson.class);
                bean.setAudioUrl(imUpdataFile.data.getFilePath());
                messagelist.get(i).setData(new Gson().toJson(bean));
                adapter.notifyDataSetChanged();
                //真正发送图片消息
                IMSMsgManager.sendMessage( messagelist.get(i), customer.getMemberId(), daoUtils,false);//
            }
        }
    }

    /**
     * 上传资源后发送消息（视屏）
     */
    @Override
    public void onUpLoadVideoListener(IMUpdataFileBean imUpdataFile,String pathurl,  UpdataPickureBean data) {
        if(imUpdataFile==null ||imUpdataFile.data==null ||imUpdataFile.data.getFilePath()==null ){
            return;
        }
        for (int i = 0; i < messagelist.size(); i++) {
            if(data.getTime()==messagelist.get(i).getPictureTime()){
                String data1 = messagelist.get(i).getData();
                IMMessageDataJson bean = new Gson().fromJson(data1, IMMessageDataJson.class);
                bean.setVideoUrl(imUpdataFile.data.getFilePath());
                bean.setFirstFrame(imUpdataFile.data.getThumbnailpath());
                bean.setFileType(imUpdataFile.data.getFileType());
                messagelist.get(i).setData(new Gson().toJson(bean));
                adapter.notifyDataSetChanged();
                //真正发送图片消息
                IMSMsgManager.sendMessage( messagelist.get(i), customer.getMemberId(), daoUtils,false);//
            }
        }

    }
    /**
     * 重新发送图片(重新上传图片 需要)
     */
    private void updataPickAgain(IMConversationDetailBean bean) {
        List<String>data=new ArrayList<>();
        data.add(bean.getOwnurl());

        UpdataPickureBean updataPickureBean= new UpdataPickureBean();
        updataPickureBean.setUrl(bean.getOwnurl());
        updataPickureBean.setTime(bean.getPictureTime());
        File file = new File(bean.getOwnurl());
        imUpdataFileUtils.handleUpdataPickure(this,file,updataPickureBean);
    }
    /**
     * 重新发送音频(重新上传音频 需要)
     */
    private void updataAudioAgain(IMConversationDetailBean data) {
        if(data==null || data.getData()==null){
            return;
        }
        IMMessageDataJson s = new Gson().fromJson(data.getData(), IMMessageDataJson.class);
        UpdataPickureBean bean=new UpdataPickureBean();
        bean.setUrl(data.getOwnurl());
        if(!TextUtils.isEmpty(s.getDuration())){
            bean.setDurtime(Integer.parseInt(s.getDuration()));
        }
        bean.setTime(data.getPictureTime());
        imUpdataFileUtils.handleUpdataAduioPickure(this,new File(data.getOwnurl()),bean);
    }

    /**
     * 重新发送音频(重新上传音频 需要)
     */
    private void updataVideoAgain(IMConversationDetailBean data) {
        if(data==null || data.getData()==null){
            return;
        }
        UpdataPickureBean bean=new UpdataPickureBean();
        bean.setUrl(data.getOwnurl());
        bean.setThumbImagePath(data.getOwnfirstFrame());
        bean.setTime(data.getPictureTime());
        imUpdataFileUtils. handleUpdataVideo(this,bean);
    }
    /**
     * 录音完成
     */
    @Override
    public void onComplete(String filePath, int duration) {
        int time = duration / 1000;
        String str = "文件地址：" + filePath + "\n录音时长:" + duration / 1000 + "秒";
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                UpdataPickureBean bean=new UpdataPickureBean();
                bean.setUrl(filePath);
                bean.setDurtime(time);
                bean.setTime(System.currentTimeMillis());
                sendAudioData(bean);
                imUpdataFileUtils.handleUpdataAduioPickure(IMPersonalChatActivity.this,new File(filePath),bean);
            }
        },300);
    }


    @Override
    public void onUpLoadProcessListener(int process) {
        Log.e("----","process="+process);
    }
    /**
     * 上传资源后发送消息（上传失败）
     */
    @Override
    public void onUpLoadErrroListener(String message,int type) {
        if(type==1){//图片
        }else if(type==2){//录音
        }else if(type==3){//视屏
            dismissProgress();
        }
        Toast.makeText(IMPersonalChatActivity.this, message, Toast.LENGTH_SHORT).show();

    }
    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        refreshLayout.finishRefresh(200/*,false*/);//传入false表示刷新失败
        List<IMConversationDetailBean> beans = daoUtils.queryConversationDetailDataAccordField(customer.getMemberId(), page * pageSize + addtopnumber, pageSize);
        if (beans == null || beans.size() == 0 || page==0) {
            page = page + 1;
            return;
        }
        page = page + 1;
        if (page * pageSize > Integer.parseInt(newNumber)) {
            mrltop.setVisibility(View.GONE);
        }
        messagelist.addAll(beans);
        adapter.notifyDataSetChanged();

    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        ll_emoji.hidellMore();
        return false;
    }

    @Override
    public void keyBoardShow(int height) {
        if(mRecycle==null){
            return;
        }
        mRecycle.scrollToPosition(0);
    }

    @Override
    public void keyBoardHide(int height) {

    }

    @Override
    public void setOnItemClickListener() {
        ll_emoji.hidellMore();
    }
}