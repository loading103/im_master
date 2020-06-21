package com.android.im.imui.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
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
import com.android.im.imadapter.IMChatGroupRecyclerAdapter;
import com.android.im.imbean.IMBetDetailBean;
import com.android.im.imbean.IMBetGetBean;
import com.android.im.imbean.IMChoosePicBean;
import com.android.im.imbean.IMCompanyBean;
import com.android.im.imbean.IMSingleRedBackBean;
import com.android.im.imbean.UpdataPickureBean;
import com.android.im.imeventbus.FinishGroupInforEvent;
import com.android.im.imeventbus.IMAppFinishEvent;
import com.android.im.imeventbus.IMAppUpdataEvent;
import com.android.im.imeventbus.IMAtSomeOneEvent;
import com.android.im.imeventbus.IMCreatMessageEvent;
import com.android.im.imeventbus.IMDeleteGroupEvent;
import com.android.im.imeventbus.IMGroupMessageEvent;
import com.android.im.imeventbus.IMLoginTimeOutEvent;
import com.android.im.imeventbus.IMMessagGroupRedEvevt;
import com.android.im.imeventbus.IMMessagGroupShareEvevt;
import com.android.im.imeventbus.IMMessageDetelGroupEvevt;
import com.android.im.imeventbus.IMMessageGroupNotice;
import com.android.im.imeventbus.IMUpdataGroupNoticeEvent;
import com.android.im.imeventbus.IMZhuaFaDataEvent;
import com.android.im.imeventbus.VideoBeanEvent;
import com.android.im.imutils.IMImageLoadUtil;
import com.android.im.imutils.SoundUtils;
import com.android.nettylibrary.bean.IMMessageReceiveEvent;
import com.android.im.imeventbus.IMRegetGroupKickOutEvent;
import com.android.im.imeventbus.IMSendGroupMessageEvent;
import com.android.im.imeventbus.IMUpdataGroupInforEvent;
import com.android.im.iminterface.IMUploadFileInterface;
import com.android.im.imnet.IMBaseConstant;
import com.android.im.imnet.IMHttpResultObserver;
import com.android.im.imnet.bean.IMUpdataFileBean;
import com.android.im.imnet.http.IMHttpsService;
import com.android.im.imutils.IMChooseUtils;
import com.android.im.imutils.IMSmoothScrollLayoutManager;
import com.android.im.imutils.IMSoftKeyBoardListener;
import com.android.im.imutils.IMStatusBarUtil;
import com.android.im.imutils.IMStopClickFast;
import com.android.im.imutils.IMUpdataFileUtils;
import com.android.im.imview.IMAlwaysMarqueeTextView;
import com.android.im.imview.IMEmojiLayoutView;
import com.android.im.imview.MyChatHeadView;
import com.android.im.imview.dialog.IMGroupTanChuaDiglog;
import com.android.nettylibrary.IMSConfig;
import com.android.nettylibrary.bean.IMMessageBean;
import com.android.nettylibrary.greendao.entity.IMConversationBean;
import com.android.nettylibrary.greendao.entity.IMGroupBean;
import com.android.nettylibrary.greendao.entity.IMConversationDetailBean;
import com.android.nettylibrary.greendao.greenutils.DaoUtils;
import com.android.nettylibrary.http.IMGetIMMemberData;
import com.android.nettylibrary.http.IMGroupMemberBean;
import com.android.nettylibrary.http.IMGroupNoticeBean;
import com.android.nettylibrary.http.IMGroupNoticeDialogBean;
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
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static com.android.im.IMSManager.okHttpClient;
import static com.android.im.imnet.IMBaseConstant.JSON;
import static com.android.im.imview.recordvideo.config.constants.NotifyConstants.NOTIFY_RECORD_OPERATE;


public class IMGroupChatActivity extends IMBaseActivity implements View.OnClickListener, IMChatGroupRecyclerAdapter.SendErrorListener, IMEmojiLayoutView.SendListener, IMChatGroupRecyclerAdapter.onItemClickListener, IMUploadFileInterface, MP3RecordView.OnRecordCompleteListener {
    private static final int REQUEST_CODE_CHOOSE = 10001;
    private static final int REQUEST_CODE_CHOOSE_VIDEO = 10002;
    private LinearLayout mLlContent;
    private IMEmojiLayoutView ll_emoji;
    private RecyclerView mRecycle;
    private LinearLayout mllcontainer;
    private ImageView mIvFinish;
    private TextView mTvTitle;
    private ImageView mIvRight;
    private RelativeLayout mRefinish;
    private IMAlwaysMarqueeTextView mMarqueeView;
    private RelativeLayout mllNotice;
    private IMChatGroupRecyclerAdapter adapter;
    private List<IMConversationDetailBean> messagelist = new ArrayList();
    private DaoUtils daoUtils;
    private String userId;
    private IMConversationBean group;
    private RelativeLayout mrltop;
    private TextView mtvtop;
    private RelativeLayout mLlButtom;
    private TextView mtvButtom;
    private TextView mtvChufa;
    private LinearLayout mllChufa;
    private  MP3RecordView mViewRecord;
    private int page=0;
    private int pageSize=20;
    private int addtopnumber=0;//用来记录多少条新消息
    private List<com.android.nettylibrary.greendao.entity.IMGroupMemberBean> databeans;
    private String fileUrl;
    private ImageView mIvBg;
    public boolean isReadCard=true;
    public boolean isFollowCard=true;
    public boolean isAllowText=true;
    public  Thread mThread; //单开线程获取群通告 还有群成员信息
    public boolean isfirstIn;  //判断是不是第一次进入该群（第一次进来的无群成员信息）
    public String mAudioPath;
    private IMUpdataFileUtils imUpdataFileUtils;
    private Handler handler=new Handler();
    private IMGroupNoticeBean groupInforbean;
    private String chatBgJson;
    private RefreshLayout refreshLayout;
    private RelativeLayout mllRight;
    private int type;//消息类型

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_im_chat);
        getWindow().setSoftInputMode( WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        IMStatusBarUtil.setTranslucent(this,0);
        IMStatusBarUtil.setLightMode(this);
        EventBus.getDefault().register(this);
        initFindViewById();
        initView();
        initData();
    }
    private void initFindViewById() {
        mIvFinish = findViewById(R.id.tv_top_finish);
        mTvTitle = findViewById(R.id.tv_top_title);
        mRefinish = findViewById(R.id.re_top_finish);
        mIvRight = findViewById(R.id.iv_top_right);

        mIvRight.setImageResource(R.mipmap.im_chat_title_more);
        mLlContent=findViewById(R.id.llContent);
        ll_emoji=findViewById(R.id.ll_emoji);
        mrltop = findViewById(R.id.im_rl_top);
        mtvtop = findViewById(R.id.im_tv_top);
        mLlButtom = findViewById(R.id.im_rl_buttom);
        mtvButtom = findViewById(R.id.im_tv_buttom);
        mtvChufa = findViewById(R.id.im_tv_cf);
        mllChufa = findViewById(R.id.im_ll_cf);
        mllNotice= findViewById(R.id.im_ll_notice);
        mViewRecord =findViewById(R.id.view_record);
        mRecycle=findViewById(R.id.recycle);
        mIvBg = findViewById(R.id.im_iv_bg);
        mllRight= findViewById(R.id.ll_right);

        mMarqueeView= findViewById(R.id.mMarqueeView);
        mllcontainer=findViewById(R.id.ll_im_container);
        refreshLayout = findViewById(R.id.refreshLayout);
        mrltop.setOnClickListener(this);
        mLlButtom.setOnClickListener(this);
        mRefinish.setOnClickListener(this);
        mllRight.setOnClickListener(this);
        ((View) refreshLayout).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                ll_emoji.hidellMore();
                return false;
            }
        });
        chatBgJson = IMPreferenceUtil.getPreference_String(IMSConfig.CHOOSE_CHAT_BG, "");
        if(!TextUtils.isEmpty(chatBgJson)){
            IMImageLoadUtil.CommonImageBgLoadCp(this,chatBgJson,mIvBg);
        }

    }
    public void initView() {
        ll_emoji.setContentView(mLlContent,true);
        ll_emoji.setOnSendListener(this);
        ll_emoji.setAudioRecord(mViewRecord);
        daoUtils=DaoUtils.getInstance();
        imUpdataFileUtils=new IMUpdataFileUtils(this);

        initRecycle();
        RefreshHandView();
        mViewRecord.setOnRecordCompleteListener(this);
        IMSoftKeyBoardListener.setListener(this, new IMSoftKeyBoardListener.OnSoftKeyBoardChangeListener() {
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
        });
    }
    @Override
    public void onComplete(String filePath, int duration) {
        int time = duration / 1000;
        String str = "文件地址：" + filePath + "\n\n录音时长:" + duration / 1000 + "秒";
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                UpdataPickureBean bean=new UpdataPickureBean();
                bean.setUrl(filePath);
                bean.setDurtime(time);
                bean.setTime(System.currentTimeMillis());
                sendAudioData(bean);
                imUpdataFileUtils.handleUpdataAduioPickure(IMGroupChatActivity.this,new File(filePath),bean);
            }
        },300);
    }

    @Override
    protected void onStart() {
        super.onStart();
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


    private String newNumber;//多少条新消息
    private int  buttomNumer; //多少质底消息
    private int  visibleItemCount;//获取它的启示可见item数
    private int  firstCompletelyVisibleItemPosition;//获取它的可见firstitem的位置
    @SuppressLint("WrongConstant")
    private void initRecycle() {
        final IMSmoothScrollLayoutManager layoutManager =  new IMSmoothScrollLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager .VERTICAL);
        layoutManager.setReverseLayout(true);
        mRecycle.setLayoutManager(layoutManager);
        mRecycle.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                ll_emoji.hidellMore();
                if( !recyclerView.canScrollVertically(1)){   //判断滑动到底部
                    buttomNumer=0;
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
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                refreshlayout.finishRefresh(200/*,false*/);//传入false表示刷新失败
                List<IMConversationDetailBean> beans = daoUtils.queryConversationDetailDataAccordField(group.getGroupId(), page*pageSize+addtopnumber,pageSize);
                if(beans==null || beans.size()==0|| page==0){
                    page = page + 1;
                    return;
                }
                page=page+1;
                if(page*pageSize>Integer.parseInt(newNumber)){
                    mrltop.setVisibility(View.GONE);
                }
                messagelist.addAll(beans);
                adapter.notifyDataSetChanged();

            }
        });
    }


    private void initData() {
        userId = IMPreferenceUtil.getPreference_String(IMSConfig.SAVE_USERID, IMSConfig.USER_ID);
        group = (IMConversationBean) getIntent().getSerializableExtra("group");
        IMGroupBean groupBean = daoUtils.queryGroupBean(group.getGroupId());
        if(groupBean!=null){
            if(groupBean.getGroupName().length()>10){
                mTvTitle.setText(groupBean.getGroupName().substring(0,10)+"...");
            }else {
                mTvTitle.setText(groupBean.getGroupName());
            }
        }
        databeans = daoUtils.queryGroupAllMemberBean(group.getGroupId());
        hanleTotleSwith();
        initDataView();
        mThread=new getDataThread();
        mThread.start();
    }
    /**
     * 初始化数据
     */
    private void initDataView() {
        //显示几条新消息
        IMPreferenceUtil.setPreference_Boolean(group.getGroupId()+IMSConfig.IM_SOMEONE_AT_ME,false);//把对应@我的消息清空
        newNumber = IMPreferenceUtil.getPreference_String(group.getGroupId() + IMSConfig.IM_UNEREAD_CONVERSATION_CHAT, "1");
        if(Integer.parseInt(newNumber)>5 && Integer.parseInt(newNumber)>visibleItemCount ){
            mrltop.setVisibility(View.VISIBLE);
            mtvtop.setText(newNumber +"条新消息");
        }

        IMPreferenceUtil.setPreference_String(group.getGroupId() + IMSConfig.IM_UNEREAD_CONVERSATION_CHAT, "0");//把对应人员的未读消息清空
        IMSManager.getInstance().getUnreadMessageNumber();
        //分页加载
        List<IMConversationDetailBean> imGroupDetailBeans = daoUtils.queryConversationDetailDataAccordField(group.getGroupId(), page*pageSize+addtopnumber,pageSize);
        if(imGroupDetailBeans==null || imGroupDetailBeans.size()==0){
            return;
        }
        for (int i = 0; i < imGroupDetailBeans.size(); i++) {
            if(imGroupDetailBeans.get(i).getSendstate()==IMSConfig.IM_SEND_STATE_SENDING){
                imGroupDetailBeans.get(i).setSendstate(IMSConfig.IM_SEND_STATE_FAILE);
                daoUtils.updateConversationDetailData(imGroupDetailBeans.get(i));
            }
        }
        page=page+1;
        messagelist.addAll(imGroupDetailBeans);
        adapter=new IMChatGroupRecyclerAdapter(this,messagelist,group);
        adapter.setOnItemClickListener(new IMChatGroupRecyclerAdapter.onItemClickInterface() {
            @Override
            public void setOnItemClickListener() {
                ll_emoji.hidellMore();
            }
        });
        adapter.setListener(this);
        mRecycle.setAdapter(adapter);
        adapter.setSendErrorListener(this);
    }




    private class getDataThread extends Thread {
        @Override
        public void run() {
            getNoticeList();
            //打开当前界面是发送一条全部已读的消息
            IMSMsgManager.sendGroupReadMessage( group.getGroupId());
        }
    }
    /**
     * 获取群公告
     */
    public void getNoticeList() {
        IMGetIMMemberData bean=new IMGetIMMemberData();
        bean.setGroupId(group.getGroupId());
        String json = new Gson().toJson(bean);
        RequestBody body = RequestBody.create(JSON, json);
        Request request = new Request.Builder()
                .url(IMSConfig.HTTP_GET_GROUP_NOTICE)
                .addHeader("Authorization", IMPreferenceUtil.getPreference_String(IMSConfig.SAVE_TOKEN,""))
                .post(body)
                .build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                groupInforbean = new Gson().fromJson(response.body().string(), IMGroupNoticeBean.class);
                if(groupInforbean.getCode().equals("NO_LOGIN")){
                    EventBus.getDefault().post(new IMLoginTimeOutEvent());
                }
                getGroupMember(groupInforbean);
            }
        });

    }
    /**
     * 获取群成员信息，保存本地
     * data代表进入界面获取通知，没有的时候是接收系统消息
     */
    public void getGroupMember(final IMGroupNoticeBean data) {
        IMGetIMMemberData bean=new IMGetIMMemberData();
        bean.setGroupId(group.getGroupId());
        String json = new Gson().toJson(bean);
        RequestBody body = RequestBody.create(JSON, json);
        Request request = new Request.Builder()
                .url(IMSConfig.HTTP_IM_GROUP_MEMBER)
                .addHeader("Authorization", IMPreferenceUtil.getPreference_String(IMSConfig.SAVE_TOKEN,""))
                .post(body)
                .build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                IMGroupMemberBean bean = new Gson().fromJson(response.body().string(), IMGroupMemberBean.class);
                IMLogUtil.d("MyOwnTag:","(onResponse) " +"获取群公告"+new Gson().toJson(data));
                IMLogUtil.d("MyOwnTag:", "onResponse " + "群成员信息"+new Gson().toJson(bean));
                List<IMGroupMemberBean.GroupMemberDetaiData> groups = bean.getData();
                saveIMGroupMemberList(groups,data);
            }
        });
    }

    /**
     * 跟新群信息，然后跟新权限和通知等消息
     */
    private void saveIMGroupMemberList(final List<IMGroupMemberBean.GroupMemberDetaiData> groups, final IMGroupNoticeBean data) {
        if(groups==null){
            return;
        }
        updataMemberInforList(groups);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                hanleForbitAndBlack();
                if(data!=null){  //收到禁言消息时候不会有通知消息
                    handleNoticeList(data);
                }

            }
        });
    }
    /**
     * 跟新群成员数据库数据
     */
    private void updataMemberInforList(List<IMGroupMemberBean.GroupMemberDetaiData> groups) {
        if(groups!=null || groups.size()>0){
            daoUtils.deleteGroupAllMemberData(group.getGroupId());
        }
        for (int i = 0; i < groups.size(); i++) {
            com.android.nettylibrary.greendao.entity.IMGroupMemberBean imGroupMemberBean = daoUtils.queryGroupMemberBean(group.getConversationId(),groups.get(i).getMemberId());
            if(imGroupMemberBean==null){  //
                imGroupMemberBean=new com.android.nettylibrary.greendao.entity.IMGroupMemberBean();
                imGroupMemberBean.setAvatar(groups.get(i).getAvatar());
                imGroupMemberBean.setNickName(TextUtils.isEmpty(groups.get(i).getNickName())?"":groups.get(i).getNickName());
                imGroupMemberBean.setGroupId(groups.get(i).getGroupId());
                imGroupMemberBean.setLable(groups.get(i).getLable());
                imGroupMemberBean.setLevel(groups.get(i).getLevel());
                imGroupMemberBean.setGroupName(groups.get(i).getGroupName());
                imGroupMemberBean.setMemberId(groups.get(i).getMemberId());
                imGroupMemberBean.setTitle(groups.get(i).getTitle());
                imGroupMemberBean.setBlacklist(groups.get(i).getBlacklist());
                imGroupMemberBean.setForbiddenWords(groups.get(i).getForbiddenWords());
                imGroupMemberBean.setIsViewTitle(groups.get(i).getIsViewTitle());
                imGroupMemberBean.setGmId(groups.get(i).getGmId());
                daoUtils.insertGroupMemberData(imGroupMemberBean.getGroupId(),imGroupMemberBean);
            }else {
                imGroupMemberBean.setAvatar(groups.get(i).getAvatar());
                imGroupMemberBean.setNickName(TextUtils.isEmpty(groups.get(i).getNickName())? "":groups.get(i).getNickName());
                imGroupMemberBean.setGroupId(groups.get(i).getGroupId());
                imGroupMemberBean.setLable(groups.get(i).getLable());
                imGroupMemberBean.setLevel(groups.get(i).getLevel());
                imGroupMemberBean.setGroupName(groups.get(i).getGroupName());
                imGroupMemberBean.setMemberId(groups.get(i).getMemberId());
                imGroupMemberBean.setTitle(groups.get(i).getTitle());
                imGroupMemberBean.setBlacklist(groups.get(i).getBlacklist());
                imGroupMemberBean.setForbiddenWords(groups.get(i).getForbiddenWords());
                imGroupMemberBean.setIsViewTitle(groups.get(i).getIsViewTitle());
                imGroupMemberBean.setGmId(groups.get(i).getGmId());
                daoUtils.updateGroupMemberData(imGroupMemberBean);
            }
        }
        if(databeans==null || databeans.size()==0){   //如果是第一次进 需要先获取群成员信息在刷新界面，后续的不需要
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    initDataView();
                }
            });
        }
    }


    /**
     * 处理群公告数据(和权限数据)
     */
    private void handleNoticeList( IMGroupNoticeBean bean) {
        if(bean.getData()==null || bean.getData().getGroupNotice()==null){
            return;
        }
        //如果开启了滚动通知且滚动通知内容补位空，显示跑马灯

        if(bean.getData().getGroupNotice().getScrollNoticeStatus()!=null && bean.getData().getGroupNotice().getScrollNoticeStatus().equals("Y") && bean.getData().getGroupNotice().getScrollNoticeContent()!=null){
            StringBuffer stringBuffer=new StringBuffer();
            for (int i = 0; i < bean.getData().getGroupNotice().getScrollNoticeContent().size(); i++) {
                stringBuffer.append(bean.getData().getGroupNotice().getScrollNoticeContent().get(i)+"      ");
            }
            String ss = stringBuffer.toString().trim();
            if(!isactived){
                return;
            }
            showLoadingDialog();
            dismissLoadingDialog();
            mMarqueeView.setText("                                                                   "+ss);
            mMarqueeView.setMarqueeRepeatLimit(-1);
            mllNotice.setVisibility(View.VISIBLE);
            Animation animation = AnimationUtils.loadAnimation(IMGroupChatActivity.this, R.anim.im_in_group_notice);
            mllNotice.startAnimation(animation);
            animation.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {

                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });

        }else {
            mllNotice.setVisibility(View.GONE);
        }
    }






    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.ll_right) {
            mllRight.setEnabled(false);
            if(groupInforbean==null || groupInforbean.getData()==null){
                Intent intent = new Intent(this, IMGroupInforActivity.class);
                intent.putExtra("user", group);
                startActivity(intent);
                mllRight.setEnabled(true);
                return;
            }
            if (groupInforbean.getData().getGroupOwner().equals(IMSManager.getMyUseId())) {
                Intent intent = new Intent(this, IMGroupOwnerInforActivity.class);
                intent.putExtra("user", group);
                intent.putExtra("bean",groupInforbean);
                startActivity(intent);
            }else {
                Intent intent = new Intent(this, IMGroupInforActivity.class);
                intent.putExtra("user", group);
                intent.putExtra("bean",groupInforbean);
                startActivity(intent);
            }
            mllRight.setEnabled(true);
        } else if (i == R.id.re_top_finish) {
            finish();
        }else if(i==R.id.im_rl_top){
            mrltop.setVisibility(View.GONE);
            addtopnumber = Integer.parseInt(newNumber);
            if(addtopnumber>pageSize){   //当未读消息大于一页10条消息时，请求未读消息的条数，后续的下拉刷新都要加这个数字，默认为0
                List<IMConversationDetailBean> beans = daoUtils.queryConversationDetailDataAccordField(group.getGroupId(),0,addtopnumber);
                if(beans==null || beans.size()==0){
                    return;
                }
                page=0;
                messagelist.clear();
                messagelist.addAll(beans);
                adapter.notifyDataSetChanged();
            }
            mRecycle.smoothScrollToPosition(addtopnumber);
        }else if(i==R.id.im_rl_buttom){
            mLlButtom.setVisibility(View.GONE);
            mRecycle.smoothScrollToPosition(0);
        }
    }
    /**
     * 发送消息(先调用获取消息id的接口)
     */


    public void getMessageID(final EditText view, final String content, final IMMessageDataJson bean, final int type, final String groupId, final String userIdString) {

        String messageFigId = IMSMsgManager.getMessageFigId();
        if(!TextUtils.isEmpty(messageFigId)){
            sendMessage(view,content,bean,type,groupId,messageFigId,userIdString);
        }else {
            String Uid= UUID.randomUUID().toString();
            sendMessage(view,content,bean,type,groupId,Uid,userIdString);
            IMSHttpManager.getInstance().IMHttpGetIMToken(false);
        }
        //先判断消息池子是不是有20条消息，不够就请求50条消息
        if(IMSMsgManager.getNeedGetMessageIds()) {
            IMSHttpManager.getInstance().IMHttpGetIMFigId();
        }
    }
    /**
     *发送消息(groupId)
     * 是用来发送群分享用的，不是分享的话全部为null
     */
    private void sendMessage(EditText view,String content,  IMMessageDataJson bean, int type,String groupId,String messageId,String userIdString) {
        String UID = messageId;
        //构造一个bean更新界面,把内容传进去跟新当前界面
        final IMConversationDetailBean message = new IMConversationDetailBean();
        message.setFingerprint(UID);
        message.setMsgType(type);
        message.setData(IMSMsgManager.handleMessageDataJson(userIdString, content, bean, type));
        message.setTimestamp(System.currentTimeMillis());
        message.setGroupId(groupId);
        message.setSenderId(IMSManager.getMyUseId());
        message.setConversationId(groupId);
        message.setSendstate(IMSConfig.IM_SEND_STATE_SENDING); //发送中
        if(bean!=null && !TextUtils.isEmpty(bean.getOwnerurl())){
            message.setOwnurl(bean.getOwnerurl());
        }
        if(view!=null){
            view.setText("");
        }
        if(groupId!=null && !groupId.equals(group.getGroupId())){
            message.setReceiverId(groupId);
            EventBus.getDefault().post(new IMSendGroupMessageEvent(groupId,message)); //通知上一个页面跟新它的发送状态
            IMSMsgManager.sendGroupMessage(message,groupId,daoUtils,false);
        }else {
            message.setReceiverId(group.getGroupId());
            messagelist.add(0,message);     //发送一条新消息 跟新ui
            if(messagelist.size()==1){
                creatReceiveGroupConversation(message);
                adapter=new IMChatGroupRecyclerAdapter(IMGroupChatActivity.this,messagelist,group);
                adapter.setOnItemClickListener(new IMChatGroupRecyclerAdapter.onItemClickInterface() {
                    @Override
                    public void setOnItemClickListener() {
                        adapter.setOnItemClickListener(new IMChatGroupRecyclerAdapter.onItemClickInterface() {
                            @Override
                            public void setOnItemClickListener() {
                                ll_emoji.hidellMore();
                            }
                        });
                    }
                });
                adapter.setListener(this);
                mRecycle.setAdapter(adapter);
                adapter.setSendErrorListener(this);
            }else {
                adapter.notifyDataSetChanged();
            }
            EventBus.getDefault().post(new IMSendGroupMessageEvent(group.getGroupId(),message)); //通知上一个页面跟新它的发送状态
            IMSMsgManager.sendGroupMessage(message,group.getGroupId(),daoUtils,false);
        }
        mRecycle.scrollToPosition(0);
        startDelayTime(message);//发送十秒钟不成功就显示发送失败
    }
    /**
     * 发送消息失败，点击重新发送
     */
    @Override
    public void onClick(IMConversationDetailBean bean) {
        messagelist.remove(bean);
        bean.setSendstate(IMSConfig.IM_SEND_STATE_SENDING);
        bean.setTimestamp(System.currentTimeMillis());
        messagelist.add(0,bean);
        if(messagelist.size()==1){
            creatReceiveGroupConversation(bean);
            adapter=new IMChatGroupRecyclerAdapter(IMGroupChatActivity.this,messagelist,group);
            adapter.setOnItemClickListener(new IMChatGroupRecyclerAdapter.onItemClickInterface() {
                @Override
                public void setOnItemClickListener() {
                    ll_emoji.hidellMore();
                }
            });
            adapter.setListener(this);
            mRecycle.setAdapter(adapter);
            adapter.setSendErrorListener(this);
        }else {
            adapter.notifyDataSetChanged();
        }
        if(bean.getMsgType()==IMSConfig.IM_MESSAGE_PICTURE && !TextUtils.isEmpty(bean.getOwnurl())){
            updataPickAgain(bean);
        }else if(bean.getMsgType()==IMSConfig.IM_MESSAGE_AUDIO && !TextUtils.isEmpty(bean.getOwnurl())){
            updataAudioAgain(bean);
        }else if(bean.getMsgType()==IMSConfig.IM_MESSAGE_VIDEO && !TextUtils.isEmpty(bean.getOwnurl())){
            updataVideoAgain(bean);
        }else {
            IMSMsgManager.sendGroupMessage(bean,group.getGroupId()+"",daoUtils,false);
        }
        EventBus.getDefault().post(new IMSendGroupMessageEvent(group.getGroupId()+"",bean)); //通知上一个页面跟新它的发送状态
        startDelayTime(bean);//发送十秒钟不成功就显示发送失败
    }

    /**
     * 发送消息
     */
    @Override
    public void onSendListener(final EditText view, int type) {
        if (type == IMEmojiLayoutView.TYPE_SEND_MESSAGE) {  //发送消息
            if(textswith){
                if(isAllowText){
                    String message = view.getText().toString();
                    String userIdString = ll_emoji.getUserIdString();
                    view.getText().clear();
                    getMessageID(view,message,null,IMSConfig.IM_MESSAGE_TEXT,null,userIdString);
                }else {
                    showSingleCommonDialog("你还没有发送文字表情的权限，请联系管理员开通权限");
                }
            }else {
                showSingleCommonDialog("文字表情功能暂未开放");
            }

        } else if(type== IMEmojiLayoutView.TYPE_CHOOSE_PHOTO){  //拍照
            if(picswith){
                IMChooseUtils.choosePhotoForResult(this,REQUEST_CODE_CHOOSE,9,false);
            }else {
                showSingleCommonDialog("图片功能暂未开放");
            }
        }else if(type==IMEmojiLayoutView.TYPE_SHARE_MESSAGE){  //分享
            if(shareswith){
                ShareMessage();
            }else {
                showSingleCommonDialog("分享功能暂未开放");
            }
        }else if(type==IMEmojiLayoutView.TYPE_SEND_REDPICK){  //发送红包
            if(redswith){
                ChooseRedPicked();
            }else {
                showSingleCommonDialog("红包功能暂未开放");
            }
        }else  if(type==IMEmojiLayoutView.TYPE_SEND_VIDEO){
            if(picswith){
//                startActivityForResult(new Intent(IMGroupChatActivity.this, IMMediaRecordActivity.class), NOTIFY_RECORD_OPERATE);
               startActivity(new Intent(IMGroupChatActivity.this, IMVideoRecordActivity.class));

            }else {
                showSingleCommonDialog("视频功能暂未开放");
            }
        }else  if(type==IMEmojiLayoutView.TYPE_PlAY){
            adapter.setAudioStoped();
        }else  if(type==IMEmojiLayoutView.TYPE_SET_TOP){
            if(mRecycle!=null){
                mRecycle.scrollToPosition(0);
            }
        }else  if(type==IMEmojiLayoutView.TYPE_SET_AT){
            if (TextUtils.isEmpty(group.getGroupId())) {
                return;
            }
            Intent intent = new Intent(this, IMTotleMemberActivity.class);
            intent.putExtra("groupId", group.getGroupId());
            intent.putExtra("isat",true);
            startActivity(intent);
        }
    }



    private Timer timer;
    private TimerTask timerTask;
    private  List<IMConversationDetailBean>sendlists=new ArrayList<>();//用来记录发送期间的消息列表

    /**
     * 成功收到服务器响应的消息就移除去，消息里面有个时间戳，在发送消息的时候只开启一个计数器（，每秒轮询消息队列的消息，
     * 有超过15秒的就更新这个消息的UI，并且移除消息队列，消息队列为空或者页面关闭时，停止这个计数器
     *
     */
    public  void  startDelayTime(IMConversationDetailBean message){
        sendlists.add(message);
        if (timer == null) {
            timer = new Timer();
        }
        timerTask = new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(sendlists.size()==0){
                            if(timer!=null){
                                timer.cancel();
                                timer = null;
                            }
                        }
                        for (int i = 0; i < sendlists.size(); i++) {
                            long time = (System.currentTimeMillis() - sendlists.get(i).getTimestamp()) / 1000;
                            if(time>15){
                                for (int j = 0; j < messagelist.size(); j++) {
                                    if(TextUtils.isEmpty(messagelist.get(j).getFingerprint())){
                                        continue;
                                    }
                                    if(TextUtils.isEmpty(sendlists.get(i).getFingerprint())){
                                        continue;
                                    }
                                    if(messagelist.get(j).getFingerprint().equals(sendlists.get(i).getFingerprint())){
                                        messagelist.get(j).setSendstate(IMSConfig.IM_SEND_STATE_FAILE);
                                        adapter.notifyDataSetChanged();
                                    }
                                }
                                sendlists.remove(i);
                            }
                        }
                    }
                });
            }
        };

        timer.schedule(timerTask,0,1000);
    }


    @Override
    protected void onResume() {
        super.onResume();
        ll_emoji.onResume();
    }

    @Override
    public void onBackPressed() {
        if(ll_emoji.onBackPressed()){
            super.onBackPressed();
        }
    }


    public boolean isactived=true;
    @Override
    protected void onDestroy() {
        super.onDestroy();
        isactived=false;
        if(timer!=null){
            timer.cancel();
            timer = null;
        }
        if(adapter!=null){
            adapter.onDestroy();
        }
        EventBus.getDefault().unregister(this);

    }

    /**
     * @界面回调
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(IMAtSomeOneEvent event) {
        com.android.nettylibrary.greendao.entity.IMGroupMemberBean data = event.getData();
        String s = ll_emoji.getmEtContent().getText().toString();
        if(TextUtils.isEmpty(s)){
            return;
        }

        int index=ll_emoji.getmEtContent().getSelectionStart();
        ll_emoji.getmEtContent().getText().delete(index-1,index);
        ll_emoji.getmEtContent().addAtSpan("@",data.getNickName(),data.getMemberId());
    }
    /**
     * 清空消息记录刷新界面
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(IMMessageDetelGroupEvevt event) {
        messagelist.clear();
        mRecycle.setAdapter(null);
    }
    /**
     * 转发图片视屏更新当前界面
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onZhuanfaData(IMZhuaFaDataEvent event) {
        String conversonId = event.getConversonId();
        if(!conversonId.equals(group.getConversationId())){
            return;
        }
        messagelist.add(0,event.getBean());
        adapter.notifyDataSetChanged();
    }
    /**
     * 处理接收到服务器的数据
     * 如果当前界面在对应的用户聊天界面，则在对应界面的接收消息出把未读消息值至为0
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(IMGroupMessageEvent event) {
        if(!event.isIsgroup()){
            return;
        }
        IMPreferenceUtil.setPreference_String(group.getGroupId() + IMSConfig.IM_UNEREAD_CONVERSATION_CHAT, "0");
        IMMessageBean message =event.getMessageMessage();
        if(!TextUtils.isEmpty(message.getGroupId())){         //正常的消息 发送都有groupId，只有系统消息没得
            if(event!=null && event.getMessageMessage().getType()== MessageProtobuf.ImMessage.TypeEnum.SERVERRECEIPT){  //是发送消息成功的回执
                handleSeverReciptView(message);
            }else if(message!=null && !TextUtils.isEmpty(message.getData()) && message.getGroupId().equals(group.getGroupId())){
                //不是回执消息代表主动接收到消息
                handleRecieveMessage(message);
                SoundUtils.playSong(this,R.raw.receive);
            }
        }
        if(event!=null && event.getMessageMessage().getType()== MessageProtobuf.ImMessage.TypeEnum.SYSTEM ){//被禁言或是被拉黑(此处系统消息外部groupid==null不能判断是一个群)
            handleSystemMessage(message);
        }
    }


    /**
     * 被禁言或是被拉黑(此处系统消息外部groupid==null不能判断是一个群)   sendKickOutMessage("你已被移除了群聊");
     */
    private void handleSystemMessage(IMMessageBean message) {
        IMMessageDataJson bean = new Gson().fromJson(message.getData(), IMMessageDataJson.class);
        if(message.getData()==null){
            return;
        }
        switch (Integer.parseInt(bean.getSysCode())) {
            case IMSConfig.IM_MESSAGE_BLACK:
            case IMSConfig.IM_MESSAGE_NO_SPEAK:
            case IMSConfig.IM_MESSAGE_NO_BLACK:
            case IMSConfig.IM_MESSAGE_CAN_SPEAK:
                String grouId = (String) bean.getMeta();
                if(grouId.equals(group.getGroupId())){
                    handleRecieveMessage(message);
                    getGroupMember(null);
                }
                break;
            case IMSConfig.IM_MESSAGE_GROUP_LIMITE:   //群状态权限变更
                IMGroupNoticeBean.IMGroupNoticeDetail datas = new Gson().fromJson(new Gson().toJson(bean.getMeta()), IMGroupNoticeBean.IMGroupNoticeDetail.class);
                if(!TextUtils.isEmpty(datas.getGroupId())&& datas.getGroupId().equals(group.getGroupId())){
                    getNoticeList();
                }
                break;
            case IMSConfig.IM_MESSAGE_GROUP_DIALOG:   //群弹窗
                IMGroupNoticeDialogBean data = new Gson().fromJson(new Gson().toJson(bean.getMeta()), IMGroupNoticeDialogBean.class);
                if(!TextUtils.isEmpty(data.getGroupId())&& data.getGroupId().equals(group.getGroupId())){
                    IMGroupTanChuaDiglog diglog=new IMGroupTanChuaDiglog(this);
                    diglog.showTextDiglog(data);
                }
                break;
            case IMSConfig.IM_MESSAGE_SEND_NO_GROUP:   //不是群成员
            case IMSConfig.IM_MESSAGE_REMOVE_GROUP:   //不是群成员
                SendMessageNoGroup(message);
                break;
            case IMSConfig.IM_MESSAGE_CLOPNEGROUP://群聊解散
                handleRecieveMessage(message);
                break;
            case IMSConfig.IM_MESSAGE_KIOUTSELF: //当成普通消息处理
                handleRecieveMessage(message);
                break;
        }

    }

    /**
     * 被踢出群之后要跟新当前界面（数据保存在会话界面）
     */
    private void SendMessageNoGroup(IMMessageBean message) {
        IMSystemNoGroupBean dataJson = new Gson().fromJson(message.getData(), IMSystemNoGroupBean.class);
        if(dataJson!=null && !TextUtils.isEmpty(dataJson.getFingerprint())) {
            for (int i = 0; i < messagelist.size(); i++) {
                if (TextUtils.isEmpty(messagelist.get(i).getFingerprint())) {
                    continue;
                }
                if (messagelist.get(i).getFingerprint().equals(dataJson.getFingerprint())) {
                    messagelist.get(i).setSendstate(IMSConfig.IM_SEND_STATE_FAILE);
                    messagelist.get(i).setTimestamp(message.getTimestamp()-1);
                    adapter.notifyDataSetChanged();
                }
            }
        }
    }

    /**
     * 处理服务器收到的消息
     */
    private void handleRecieveMessage(IMMessageBean message) {
        IMPreferenceUtil.setPreference_String(group.getGroupId() + IMSConfig.IM_UNEREAD_CONVERSATION_CHAT, "0");
        IMPreferenceUtil.setPreference_Boolean(group.getGroupId()+IMSConfig.IM_SOMEONE_AT_ME,false);//把对应@我的消息清空
        //显示底部新消息逻辑
        if(firstCompletelyVisibleItemPosition>2){
            mLlButtom.setVisibility(View.VISIBLE);
            buttomNumer=buttomNumer+1;
            mtvButtom.setText(buttomNumer+"");
        }else {
            mLlButtom.setVisibility(View.GONE);
            buttomNumer=0;
        }


        IMConversationDetailBean data= new IMConversationDetailBean();
        data.setFingerprint(message.getFingerprint());
        data.setSenderId(message.getSenderId());
        data.setReceiverId(IMSManager.getMyUseId());
        data.setMsgType(message.getMsgType());
        data.setLastMsgSendType(IMSConfig.MSG_RECIEVE);
        data.setData(message.getData());
        data.setTimestamp(message.getTimestamp());
        messagelist.add(0,data);
        if(getOffLinemessage){
            //置顶的按置顶时间降序排列，不置顶的按消息的时间降序排列
            Collections.sort(messagelist, new Comparator<IMConversationDetailBean>() {

                @Override
                public int compare(IMConversationDetailBean o1, IMConversationDetailBean o2) {
                    if (o1.getTimestamp() > o2.getTimestamp()) {
                        return 1;
                    }
                    if (o1.getTimestamp() == o2.getTimestamp()) {
                        return 0;
                    }
                    return -1;
                }
            });
            IMLogUtil.d("MyOwnTag:", "message.getTimestamp() --排序-" +message.getTimestamp());
        }else {
            IMLogUtil.d("MyOwnTag:", "message.getTimestamp() --不排序-" +message.getTimestamp());
        }
        if(messagelist.size()==1){
            adapter=new IMChatGroupRecyclerAdapter(IMGroupChatActivity.this,messagelist,group);
            adapter.setOnItemClickListener(new IMChatGroupRecyclerAdapter.onItemClickInterface() {
                @Override
                public void setOnItemClickListener() {
                    ll_emoji.hidellMore();
                }
            });
            adapter.setListener(this);
            adapter.setSendErrorListener(this);
            mRecycle.setAdapter(adapter);
        }else {
            adapter.notifyDataSetChanged();
        }
    }

    /**
     * 处理发消息成功服务器的回执
     */
    private void handleSeverReciptView(IMMessageBean message) {
        //更新UI发送状态
        for (int i = 0; i < sendlists.size(); i++) {
            if(sendlists.get(i).getFingerprint().equals(message.getFingerprint())){
                sendlists.remove(i);
            }
        }

        for (int i = 0; i < messagelist.size(); i++) {
            if(TextUtils.isEmpty(messagelist.get(i).getFingerprint())){
                continue;
            }
            if(messagelist.get(i).getFingerprint().equals(message.getFingerprint())){
                messagelist.get(i).setSendstate(IMSConfig.IM_SEND_STATE_SUCCESS);
                type=Handlemessage(messagelist.get(i));
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
     * 处理数据源类型 通过msgtype和sendtype组合
     */
    private  int Handlemessage(IMConversationDetailBean messagelist) {
        IMMessageDataJson bean =null;
        try{
            bean = new Gson().fromJson(messagelist.getData(), IMMessageDataJson.class);
        }catch (Exception e){
            return IMSConfig.MSG_CHAT_GROUP;
        }
        int type = Integer.parseInt(bean.getType());
        if(messagelist.getLastMsgSendType()== IMSConfig.MSG_RECIEVE && type==IMSConfig.IM_MESSAGE_TEXT){
            return  IMSConfig.MSG_LEFT_TEXT ;
        }else if(messagelist.getLastMsgSendType()==IMSConfig.MSG_SEND && type==IMSConfig.IM_MESSAGE_TEXT){
            return  IMSConfig.MSG_RIGHT_TEXT;
        }else if(messagelist.getLastMsgSendType()==IMSConfig.MSG_RECIEVE && type==IMSConfig.IM_MESSAGE_PICTURE){
            return  IMSConfig.MSG_LEFT_IMG;
        }else if(messagelist.getLastMsgSendType()==IMSConfig.MSG_SEND && type==IMSConfig.IM_MESSAGE_PICTURE){
            return  IMSConfig.MSG_RIGHT_IMG;
        }else if( type==IMSConfig.IM_MESSAGE_PUSH){
            return  IMSConfig.MSG_PUSH;
        }else if(messagelist.getLastMsgSendType()==IMSConfig.MSG_SEND && type==IMSConfig.IM_MESSAGE_REPACKED){
            return  IMSConfig.MSG_RIGHT_REPACKED;
        }else if(messagelist.getLastMsgSendType()==IMSConfig.MSG_RECIEVE && type==IMSConfig.IM_MESSAGE_REPACKED){
            return  IMSConfig.MSG_LEFT_REPACKED;
        }else if(messagelist.getLastMsgSendType()==IMSConfig.MSG_SEND && type==IMSConfig.IM_MESSAGE_VIDEO){
            return  IMSConfig.MSG_RIGHT_VIDEO;
        }else if(messagelist.getLastMsgSendType()==IMSConfig.MSG_RECIEVE && type==IMSConfig.IM_MESSAGE_VIDEO){
            return  IMSConfig.MSG_LEFT_VIDEO;
        }else if (messagelist.getLastMsgSendType() == IMSConfig.MSG_RECIEVE && type == IMSConfig.IM_MESSAGE_AUDIO) {
            return IMSConfig.MSG_LEFT_AUDIO;
        }else if (messagelist.getLastMsgSendType() == IMSConfig.MSG_SEND && type == IMSConfig.IM_MESSAGE_AUDIO) {
            return IMSConfig.MSG_RIGHT_AUDIO;
        }else if (messagelist.getLastMsgSendType() == IMSConfig.IM_MESSAGE_SYSTEM) {
            return IMSConfig.MSG_CHAT_GROUP;
        }
        return 0;
    }

    /**
     * 踢群之后给提示
     *
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(final IMRegetGroupKickOutEvent event) {
        if( event.getGroups().getGroupId()==null || group.getGroupId()==null){
            return;
        }
        if( event.getGroups().getGroupId().equals(group.getGroupId())){
            sendKickOutMessage("你已被移除了群聊");
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
     * 被提出群聊之后会发一条模拟通知消息
     */
    private void sendKickOutMessage(String title) {
        String UID = UUID.randomUUID().toString();
        //构造一个bean更新界面,把内容传进去跟新当前界面
        final IMConversationDetailBean message = new IMConversationDetailBean();
        message.setFingerprint(UID);
        message.setMsgType(10000);
        message.setData(IMSMsgManager.handleMessageDataJson(null,title , null, 10000));
        message.setTimestamp(System.currentTimeMillis());
        message.setReceiverId(group.getGroupId());
        messagelist.add(0,message);     //发送一条新消息 跟新ui
        if(messagelist.size()==1){
            adapter=new IMChatGroupRecyclerAdapter(IMGroupChatActivity.this,messagelist,group);
            adapter.setOnItemClickListener(new IMChatGroupRecyclerAdapter.onItemClickInterface() {
                @Override
                public void setOnItemClickListener() {
                    ll_emoji.hidellMore();
                }
            });
            adapter.setListener(this);
            adapter.setSendErrorListener(this);
            mRecycle.setAdapter(adapter);
        }else {
            adapter.notifyDataSetChanged();
        }
    }



    /**
     * 发送红包成功消息
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(IMMessagGroupRedEvevt event) {
        IMSingleRedBackBean bean = event.getImRedBackBean();
        IMGroupBean group = event.getGroup();
        if(bean==null){
            return;
        }
        IMMessageDataJson json=new IMMessageDataJson();
        json.setTitle(bean.getRedPacket().getTitle());
        json.setRedPacketId(bean.getRedPacket().getRedPacketId());
        json.setDesc(bean.getRedPacket().getDesc());
        json.setStyle("STYLE_DEFAULT");
        json.setType(bean.getRedPacket().getType());
        getMessageID(null,"[红包]",json,IMSConfig.IM_MESSAGE_REPACKED,null,null);
    }

    /**
     * 解散群聊
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(IMDeleteGroupEvent event) {
        finish();
    }
    /**
     * 分享成功发送消息
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(IMMessagGroupShareEvevt event) {
        IMBetDetailBean bean = event.getBean();
        List<String> groups = event.getGroups();
        if(bean==null){
            return;
        }
        for (int i = 0; i < groups.size(); i++) {
            IMMessageDataJson json=new IMMessageDataJson();
            if(event.getShareType().equals("1")){ //跟投
                json.setGameId(bean.getGameId());
                json.setGameName(bean.getGameName());
                json.setBetOrderId(bean.getBettingOrderId());
                json.setAmount(bean.getBettingAmount());
                json.setContent(bean.getAwardNumber());
                json.setNum(bean.getNumber());
                json.setPlayMethod(bean.getPlayMethod());
                json.setUserId(IMSManager.getMyUseId());
                json.setSealingTime(bean.getSealingTime());
                json.setBetAmount(bean.getBettingAmount());
                json.setBettingMultiples(bean.getBettingMultiples());
                json.setBettingTotalAmount(bean.getBettingTotalAmount());
                getMessageID(null,"跟投分享",json,IMSConfig.IM_MESSAGE_FOLLOWHEAD,groups.get(i),null);
            }else {                 //红单
                json.setGameId(bean.getGameId());
                json.setGameName(bean.getGameName());
                json.setBetOrderId(bean.getBettingOrderId());
                json.setAmount(bean.getBettingAmount());
                json.setContent(bean.getAwardNumber());
                json.setNum(bean.getNumber());
                json.setWinAmount(bean.getMediumBonus());
                json.setGainAmount(bean.getProfitAndLoss());
                json.setSealingTime(bean.getSealingTime());
                json.setBetAmount(bean.getBettingAmount());
                getMessageID(null,"红单分享",json,IMSConfig.IM_MESSAGE_READCARD,groups.get(i),null);
            }
        }
    }
    /**
     * 选择图片处理
     */
    List<String> mSelected;
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        String token = IMPreferenceUtil.getPreference_String(IMSConfig.SAVE_IM_TOKEN, null);
        if (requestCode == REQUEST_CODE_CHOOSE && resultCode == RESULT_OK) {
            mSelected = Matisse.obtainPathResult(data);
            if(mSelected==null && mSelected.size()==0){
                return;
            }
            if(!mSelected.get(0).endsWith("jpg") && !mSelected.get(0).endsWith("png") && !mSelected.get(0).endsWith("jpeg")){
                updataPickture(mSelected,true);
            }else {
                updataPickture(mSelected,false);
            }
        }else  if (requestCode == REQUEST_CODE_CHOOSE_VIDEO && resultCode == RESULT_OK) {   //选择图片成功之后上传
            mSelected = Matisse.obtainPathResult(data);
            if(mSelected==null && mSelected.size()==0){
                return;
            }
            updataPickture(mSelected,true);
        }
    }
    /**
     * 录制拍摄回调
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onUpdateHeadView(VideoBeanEvent event) {
        if(TextUtils.isEmpty(event.getUrl())){
            Toast.makeText(this, "拍摄失败", Toast.LENGTH_SHORT).show();
            return;
        }
        if(event.isVideo()){
            UpdataPickureBean datas=new UpdataPickureBean();
            datas.setUrl(event.getUrl());
            datas.setTime(System.currentTimeMillis());
            datas.setThumbImagePath("");
            sendVideoData(datas);
            imUpdataFileUtils.handleUpdataVideo(IMGroupChatActivity.this,datas);
        }else {
            List<String>  datas=new ArrayList<>();
            datas.add(event.getUrl());
            imUpdataFileUtils.handleCompressPickure(this,datas,false);
        }
    }



    /**
     * 上传图片和视频
     */
    private void updataPickture(final List<String> mSelected, final boolean isVideo) {
        if(isVideo){
            for (int i = 0; i <mSelected.size() ; i++) {
                UpdataPickureBean datas=new UpdataPickureBean();
                datas.setUrl(mSelected.get(i));
                datas.setTime(System.currentTimeMillis());
                datas.setThumbImagePath("");
                sendVideoData(datas);
                imUpdataFileUtils.handleUpdataVideo(IMGroupChatActivity.this,datas);
            }
        }else {
            imUpdataFileUtils.handleCompressPickure(IMGroupChatActivity.this,mSelected,false);
        }
    }
    /**
     * 点击头像实现@功能
     */
    @Override
    public void onItemClick(int position) {
        String senderId = messagelist.get(position).getSenderId();   //从本地查找到
        com.android.nettylibrary.greendao.entity.IMGroupMemberBean imGroupMemberBean = daoUtils.queryGroupMemberBean(group.getGroupId(),senderId);
        if(imGroupMemberBean==null){
            return;
        }
        ll_emoji.getmEtContent().addAtSpan("@",imGroupMemberBean.getNickName(),senderId);
        ll_emoji.showKeyboard();
    }

    /**
     * 点击进入分享界面
     */

    private void ShareMessage() {
        Intent intent=new Intent(this,IMBetRecordActivity.class);
        intent.putExtra("readcard",isReadCard);
        intent.putExtra("followcard",isFollowCard);
        startActivity(intent);
    }
    /**
     * 点击发送红包按钮
     */
    private void ChooseRedPicked() {
        Intent intent=new Intent(this,IMSendRedPickActivity.class);
        intent.putExtra("group",group);
        startActivity(intent);
    }
    /**
     * 处理被拉黑和被禁言
     */
    public void hanleForbitAndBlack() {
        final com.android.nettylibrary.greendao.entity.IMGroupMemberBean bean = daoUtils.queryGroupMemberBean(group.getConversationId(),IMSManager.getMyUseId());
        if(bean==null  || !totleswith){// totleswith=false说明总开关没有打开，直接提示此功能没有打开
            return;
        }
        String personType = IMPreferenceUtil.getPreference_String(IMSConfig.SAVE_USERTYPE, "");
        if(Integer.parseInt(personType)==7){
            ll_emoji.setViewEnble(false);
            mllChufa.setVisibility(View.VISIBLE);
            mtvChufa.setText("请登录");
            return;
        }
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(!TextUtils.isEmpty(bean.getBlacklist()) && bean.getBlacklist().equals("N")){
                    if(!TextUtils.isEmpty(bean.getForbiddenWords()) && bean.getForbiddenWords().equals("N")){
                        ll_emoji.setViewEnble(true);
                        mllChufa.setVisibility(View.GONE);
                        mtvChufa.setText("");
                    }else{
                        ll_emoji.setViewEnble(false);
                        mllChufa.setVisibility(View.VISIBLE);
                        mtvChufa.setText("你已经被禁言");
                    }
                }else{
                    ll_emoji.setViewEnble(false);
                    mllChufa.setVisibility(View.VISIBLE);
                    mtvChufa.setText("你已被拉黑");
                }
            }
        });
    }
    /**
     * 处理总开关
     */
    private boolean textswith=true;
    private boolean picswith=true;
    private boolean redswith=true;
    private boolean shareswith=true;
    private boolean totleswith=true;
    public void hanleTotleSwith() {
        String dataJson = IMPreferenceUtil.getPreference_String(IMSConfig.SAVE_AVAILAVBLE_SWICH, "");
        if(!TextUtils.isEmpty(dataJson)){
            IMCompanyBean bean = new Gson().fromJson(dataJson, IMCompanyBean.class);
            if(bean.getIsAvailable().equals("Y") && bean.getSwitchOfGroup().equals("Y")){
                totleswith=true;
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
                if(bean.getSwitchOfBetting().equals("N")){
                    shareswith=false;
                }else {
                    shareswith=true;
                }
            }else {
                totleswith=false;
                ll_emoji.setViewEnble(false);
                mllChufa.setVisibility(View.VISIBLE);
                mtvChufa.setText(getResources().getString(R.string.im_swith_no));
            }
        }
    }

    /**
     * 修改群聊的名字
     */

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onUpdateHeadView(IMUpdataGroupInforEvent event) {
        IMConversationBean groupBean = event.groupBean;
        group=groupBean;
        mTvTitle.setText(groupBean.getGroupName());
    }
    /**
     * 修改群公告
     */

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onUpdateNoticeView(IMUpdataGroupNoticeEvent event) {
        IMGroupNoticeBean groupNoticeEvent = event.groupInforbean;
        groupInforbean=groupNoticeEvent;
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
        if(bean.getSwitchOfGroup().equals("N")){  //关闭群聊
            showCommonDialog("群聊功能已被禁用，点击确认返回会话界面", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dissCommonDialog();
                    finish();
                }
            });
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
            if(bean.getSwitchOfBetting().equals("N")){
                shareswith=false;
            }else {
                shareswith=true;
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(FinishGroupInforEvent event) {
        finish();
    }

    /**
     * 处理群系统通知打开（每日弹窗）
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onUpdateHeadView(IMMessageGroupNotice event) {
        if(event==null || TextUtils.isEmpty(event.getJson())){
            return;
        }
        IMGroupNoticeDialogBean data = new Gson().fromJson(event.getJson(), IMGroupNoticeDialogBean.class);
        if(!TextUtils.isEmpty(data.getGroupId())&& data.getGroupId().equals(group.getGroupId())){
            IMGroupTanChuaDiglog diglog=new IMGroupTanChuaDiglog(this);
            diglog.showTextDiglog(data);
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
    public void onUpLoadErrroListener(String message, int type) {
        if(type==1){//图片
        }else if(type==2){//录音
        }else if(type==3){//视屏
        }
    }

    @Override
    public void onUpLoadProcessListener(int process) {
        Log.e("----","process="+process);
    }
    /**
     * 创建单聊会话(然后在通知聊天界面添加todo)
     */
    private void creatReceiveGroupConversation(IMConversationDetailBean message) {
        IMConversationBean bean = IMSMsgManager.creatSendGroupConversation(group.getConversationId(), message);
        if(bean==null){
            return;
        }
        daoUtils.insertConversationData(bean);
        EventBus.getDefault().post(new IMCreatMessageEvent(bean));
    }

    public String getGroupId() {
        return group.getConversationId();
    }



    /**
     * 图片先上屏幕
     */
    public void sendPickureData(UpdataPickureBean bean) {
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
                getMessagePickID(null, "[图片]", IMSConfig.IM_MESSAGE_PICTURE,beans,bean.getTime());
                IMLogUtil.d("MyOwnTag:", "IMSMsgManager ----" +width+"---height=-"+height);
            }
        });
    }
    /**
     * 图片先上屏幕
     */
    public void sendAudioData(UpdataPickureBean beans) {
        if(beans==null || TextUtils.isEmpty(beans.getUrl()) ){
            Toast.makeText(IMGroupChatActivity.this, "录音失败，请重新发送", Toast.LENGTH_SHORT).show();
            return;
        }
        IMMessageDataJson bean = new IMMessageDataJson();
        bean.setDuration(beans.getDurtime()+"");
        bean.setOwnerurl(beans.getUrl());
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
     * 发送消息(图片)
     */
    public void getMessagePickID(final EditText view, final String content, int type,final IMMessageDataJson bean, long time) {
        String messageFigId = IMSMsgManager.getMessageFigId();
        if(!TextUtils.isEmpty(messageFigId)){
            sendPicMessage(view,content,bean,messageFigId,time,null,type);
        }else {
            String Uid= UUID.randomUUID().toString();
            sendPicMessage(view,content,bean,Uid,time,null,type);
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
    private void sendPicMessage(EditText view, String content, IMMessageDataJson bean,String uid,long time,String groupId,int type) {

        String UID = uid;
        //构造一个bean更新界面,把内容传进去跟新当前界面
        final IMConversationDetailBean message = new IMConversationDetailBean();
        message.setFingerprint(UID);
        message.setMsgType(type);
        message.setData(IMSMsgManager.handleMessageDataJson(null, content, bean, type));
        message.setTimestamp(System.currentTimeMillis());
        message.setGroupId(groupId);
        message.setPictureTime(time);
        message.setSenderId(IMSManager.getMyUseId());
        message.setConversationId(groupId);
        message.setSendstate(IMSConfig.IM_SEND_STATE_SENDING); //发送中
        if(bean!=null && !TextUtils.isEmpty(bean.getOwnerurl())){
            message.setOwnurl(bean.getOwnerurl());
        }
        if(bean!=null && !TextUtils.isEmpty(bean.getOwnerfirstFrame())){
            message.setOwnfirstFrame(bean.getOwnerfirstFrame());
        }
        if(view!=null){
            view.setText("");
        }
        if(groupId!=null && !groupId.equals(group.getGroupId())){
            message.setReceiverId(groupId);
            EventBus.getDefault().post(new IMSendGroupMessageEvent(groupId,message)); //通知上一个页面跟新它的发送状态
            IMSMsgManager.sendGroupMessage(message,groupId,daoUtils,true);
        }else {
            message.setReceiverId(group.getGroupId());
            messagelist.add(0,message);     //发送一条新消息 跟新ui
            if(messagelist.size()==1){
                creatReceiveGroupConversation(message);
                adapter=new IMChatGroupRecyclerAdapter(IMGroupChatActivity.this,messagelist,group);
                adapter.setOnItemClickListener(new IMChatGroupRecyclerAdapter.onItemClickInterface() {
                    @Override
                    public void setOnItemClickListener() {
                        adapter.setOnItemClickListener(new IMChatGroupRecyclerAdapter.onItemClickInterface() {
                            @Override
                            public void setOnItemClickListener() {
                                ll_emoji.hidellMore();
                            }
                        });
                    }
                });
                adapter.setListener(this);
                mRecycle.setAdapter(adapter);
                adapter.setSendErrorListener(this);
            }else {
                adapter.notifyDataSetChanged();
            }
            EventBus.getDefault().post(new IMSendGroupMessageEvent(group.getGroupId(),message)); //通知上一个页面跟新它的发送状态
            IMSMsgManager.sendGroupMessage(message,group.getGroupId(),daoUtils,true);
        }
        mRecycle.scrollToPosition(0);
        startDelayTime(message);//发送十秒钟不成功就显示发送失败

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
                //真正发送图片消息
                IMSMsgManager.sendGroupMessage(messagelist.get(i),group.getGroupId(),daoUtils,false);
            }
        }
    }
    /**
     * 上传资源后发送消息（音屏）
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
                IMSMsgManager.sendGroupMessage(messagelist.get(i),group.getGroupId(),daoUtils,false);
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
                //真正发送视屏消息
                IMSMsgManager.sendGroupMessage( messagelist.get(i), group.getGroupId(), daoUtils,false);//
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
        imUpdataFileUtils.handleUpdataAduioPickure(IMGroupChatActivity.this,new File(data.getOwnurl()),bean);
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



}
