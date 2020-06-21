package com.android.im.imui.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.im.R;
import com.android.im.imadapter.IMGroupInforAdapter;
import com.android.im.imadapter.baseadapter.IMBaseRecycleViewAdapter_T;
import com.android.im.imbean.IMBetGetBean;
import com.android.im.imeventbus.FinishChoosePersonEvent;
import com.android.im.imeventbus.FinishGroupInforEvent;
import com.android.im.imeventbus.IMDeleteGroupEvent;
import com.android.im.imeventbus.IMMessageDeteAlllEvevt;
import com.android.im.imeventbus.IMMessageDetelEvevt;
import com.android.im.imeventbus.IMMessageDetelGroupEvevt;
import com.android.im.imeventbus.IMMessageNoticelGroupEvevt;
import com.android.im.imeventbus.IMMessageNoticelPersonEvevt;
import com.android.im.imnet.IMHttpResultObserver;
import com.android.im.imnet.http.IMHttpsService;
import com.android.im.imui.fragment.IMConversationFragment;
import com.android.im.imutils.IMImageLoadUtil;
import com.android.im.imutils.IMStatusBarUtil;
import com.android.im.imutils.IMStopClickFast;
import com.android.im.imview.IMRoundAngleImageView;
import com.android.im.imview.dialog.IMDialogUtils;
import com.android.im.imview.dialog.IMTextDetailDiglog;
import com.android.nettylibrary.IMSConfig;
import com.android.nettylibrary.greendao.entity.IMConversationBean;
import com.android.nettylibrary.greendao.entity.IMGroupBean;
import com.android.nettylibrary.greendao.entity.IMGroupMemberBean;
import com.android.nettylibrary.http.IMGetIMMemberData;
import com.android.nettylibrary.http.IMGroupNoticeBean;
import com.android.nettylibrary.utils.IMDensityUtil;
import com.android.nettylibrary.utils.IMLogUtil;
import com.android.nettylibrary.utils.IMPreferenceUtil;
import com.blankj.utilcode.util.SizeUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.gson.Gson;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import cc.shinichi.library.ImagePreview;
import jp.wasabeef.glide.transformations.RoundedCornersTransformation;
import okhttp3.MediaType;
import okhttp3.RequestBody;


public class IMGroupInforActivity extends IMBaseActivity implements View.OnClickListener {
    private ImageView mIvFinish;
    private TextView mTvName;
    private TextView mTvChakan;
    private TextView mTvNotice;
    private TextView mTvTitle;
    private LinearLayout mRlNotice;
    private ImageView mIvZhid;
    private ImageView mGroupHeader;
    private ImageView mIvNotice;
    private ImageView mRlback;
    private RelativeLayout mRlClear;
    private RelativeLayout mRlTouSu;
    private RelativeLayout mRlhositoy;
    private LinearLayout mRlcontain;
    public IMConversationBean groupBean;
    public IMTextDetailDiglog detailDiglog;
    public RecyclerView mRecycleView;
    private String groupId;
    private IMGroupNoticeBean groupInforbean;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_im_group_infor);
        EventBus.getDefault().register(this);
        initView();
        initData();
    }
    private void initView() {
        IMStatusBarUtil.setTranslucent(this,0);
        IMStatusBarUtil.setLightMode(this);
        mIvFinish = findViewById(R.id.tv_top_finish);
        mTvTitle = findViewById(R.id.tv_top_title);
        mIvFinish.setVisibility(View.VISIBLE);
        mTvName = findViewById(R.id.im_tv_name);
        mRlNotice = findViewById(R.id.im_rl_notice);
        mTvChakan= findViewById(R.id.tv_chakan);
        mIvZhid = findViewById(R.id.im_iv_zd);
        mIvNotice = findViewById(R.id.im_iv_notice);
        mRlClear = findViewById(R.id.im_rl_clear);
        mRlhositoy = findViewById(R.id.im_rl_hositoy);
        mRlTouSu = findViewById(R.id.im_rl_ts);
        mGroupHeader = findViewById(R.id.iv_group_head);
        mRlback = findViewById(R.id.im_rl_back);
        mRecycleView = findViewById(R.id.recycle);
        mTvNotice= findViewById(R.id.tv_right_notice);
        mRlTouSu.setOnClickListener(this);
        mRlClear.setOnClickListener(this);
        mRlhositoy.setOnClickListener(this);
        mIvZhid.setOnClickListener(this);
        mRlNotice.setOnClickListener(this);
        mIvFinish.setOnClickListener(this);
        mRlback.setOnClickListener(this);
        mTvChakan.setOnClickListener(this);
        mIvNotice.setOnClickListener(this);
        mGroupHeader.setOnClickListener(this);
    }

    private boolean isNoticed;
    private boolean isSetTop;
    private void initData() {
        detailDiglog=new IMTextDetailDiglog(this);
        groupBean = (IMConversationBean)getIntent().getSerializableExtra("user");
        groupInforbean = (IMGroupNoticeBean)getIntent().getSerializableExtra("bean");
        mTvTitle.setText("群聊详情");
        isNoticed = IMPreferenceUtil.getPreference_Boolean(groupBean.getConversationId() + IMSConfig.IM_CONVERSATION_NO_NOTICE, false);
        isSetTop = IMPreferenceUtil.getPreference_Boolean(groupBean.getConversationId() + IMSConfig.IM_CONVERSATION_ZHI_DING, false);
        if(isSetTop){
            mIvZhid.setImageResource(R.mipmap.im_chat_detail_on);
        }else{
            mIvZhid.setImageResource(R.mipmap.im_chat_detail_off);
        }
        if(isNoticed){
            mIvNotice.setImageResource(R.mipmap.im_chat_detail_on);
        }else{
            mIvNotice.setImageResource(R.mipmap.im_chat_detail_off);
        }
        if(groupInforbean==null){
            getGroupData();
        }else {
            IMGroupNoticeBean.IMGroupNoticeDetail data = groupInforbean.getData();
            getGroupData(data);
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    /**
     * 获取群信息
     * groupNotice":{"tipBoxStatus":"N","fixedNoticeStatus":"N","tipBoxInterval":0,"textStatus":"N",
     * "pictureStatus":"N","scrollNoticeStatus":"N","tipBoxRule":0},"groupOwner":"10245d82133b92b13310ab930d86",
     * "groupId":"30725d83242b92b13373bb528ca3","groupAvatar":"g","memberCount":12,"blacklist":"N",
     * "groupCharacteristic":"http://148.70.26.130:8888/group1/M00/00/33/rBsAAl2DJCeAFVNAAAALtlc1FoA274.png","noticeReminder":"N",
     * "groupFunction":{"betsSwitch":"OFF","textSwitch":"OFF","pictureSwitch":"OFF","redEnvSwitch":"OFF","redBillSwitch":"OFF"},
     * "groupName":"测试红包群","usePageTemplate":"","startTime":":","endTime":":","showCharacteristic":"N","messageFree":"N",
     * "config":{"betsStatus":"N","minTitle":1,"titleStatus":"N","minBets":15.00,
     * "redEnvStatus":"N","callStatus":"N","gainStatus":"N","hitStatus":"N"},"forbiddenWords":"N"
     */
    private String content;
    private Handler handler=new Handler();
    private void getGroupData() {
        final IMGetIMMemberData bean=new IMGetIMMemberData();
        bean.setGroupId(groupBean.getGroupId());
        String json = new Gson().toJson(bean);
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"),json);
        showLoadingDialog();
        IMHttpsService.getGroupDataJson(body, new IMHttpResultObserver<IMGroupNoticeBean.IMGroupNoticeDetail>() {
            @Override
            public void onSuccess(final IMGroupNoticeBean.IMGroupNoticeDetail bean, String message) {
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        dismissLoadingDialog();
                    }
                },1000);
                IMLogUtil.d("MyOwnTag:", "getGroupData " +"(onResponse) " +"群信息"+new Gson().toJson(bean));
                if(bean==null){
                   handleNoGroupInfor();
                    return;
                }
                mTvTitle.setText("群聊详情("+bean.getMemberCount()+")");
                initRecycleView(bean);
                groupId=bean.getGroupId();
                mTvName.setText(bean.getGroupName());
                String name = IMPreferenceUtil.getPreference_String(IMSConfig.SAVE_NAME, "");
                if( bean.getGroupNotice()!=null &&  bean.getGroupNotice().getFixedNoticeContent()!=null){
                    content=bean.getGroupNotice().getFixedNoticeContent();
                }
                if(TextUtils.isEmpty(bean.getGroupAvatar())){
                    mGroupHeader.setVisibility(View.GONE);
                }else {
                    mGroupHeader.setVisibility(View.VISIBLE);
//                    IMImageLoadUtil.ImageLoadlineCircle(IMGroupInforActivity.this,bean.getGroupAvatar(),mGroupHeader);
                    Glide.with(getApplicationContext()).load(bean.getGroupAvatar())
                            .apply(RequestOptions.bitmapTransform(new RoundedCornersTransformation(SizeUtils.dp2px(5), 0)))
                            .into(mGroupHeader);

                }
                if(bean.getGroupNotice()!=null && !TextUtils.isEmpty(bean.getGroupNotice().getFixedNoticeContent())){
                    mTvNotice.setText(bean.getGroupNotice().getFixedNoticeContent());
                }
            }

            @Override
            public void _onError(Throwable e) {
                dismissLoadingDialog();
                Toast.makeText(IMGroupInforActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void _onErrorLocal(Throwable e, String message, String code) {
                dismissLoadingDialog();
                Toast.makeText(IMGroupInforActivity.this,message, Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void handleNoGroupInfor() {
        IMDialogUtils.getInstance().showGroupInforDialog(IMGroupInforActivity.this, "该群已经被群主解散，无法查看群信息，是否删除该群聊", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dissCommonDialog();
                finish();

            }
        }, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dissCommonDialog();
                EventBus.getDefault().post(new IMDeleteGroupEvent(groupBean.getConversationId()));
                finish();
            }
        });
    }

    /**
     * 显示群成员
     */
    public IMGroupInforAdapter madapter;
    private void initRecycleView(IMGroupNoticeBean.IMGroupNoticeDetail bean) {
        IMGroupMemberBean ower= daoUtils.queryGroupMemberBean(bean.getGroupId(),bean.getGroupOwner());
        List<IMGroupMemberBean> members = daoUtils.queryGroupAllMemberBean(bean.getGroupId(),10);
        if(members==null || members.size()==0 ||ower==null){
            return;
        }
        if(members.contains(ower)){
            members.remove(ower);
            members.add(0,ower);
        }else {
            members.remove(0);
            members.add(0,ower);
        }
        if(members.size()>=10){
            mTvChakan.setVisibility(View.VISIBLE);
        }else {
            mTvChakan.setVisibility(View.GONE);
        }

        GridLayoutManager gridLayoutManager=new GridLayoutManager(IMGroupInforActivity.this,5);
        mRecycleView.setLayoutManager(gridLayoutManager);
        mRecycleView.setNestedScrollingEnabled(false);
        List<IMGroupMemberBean> datas=new ArrayList<>();
        madapter=new IMGroupInforAdapter(IMGroupInforActivity.this,R.layout.item_group_infor_contact,members);
        mRecycleView.setAdapter(madapter);
        madapter.setOnItemClickListner(new IMBaseRecycleViewAdapter_T.OnItemClickListner() {
            @Override
            public void onItemClickListner(View v, int position, Object t) {
                if(IMStopClickFast.isFastClick()) {
                    Intent intent = new Intent(IMGroupInforActivity.this, IMMemberInforActivity.class);
                    intent.putExtra("bean", members.get(position));
                    startActivity(intent);
                }
            }
        });
    }
    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.tv_top_finish){
            finish();
        }else if(v.getId()==R.id.im_rl_hositoy){
            if(IMStopClickFast.isFastClick()) {
                Intent intent = new Intent(this, IMInformationSerachActivity.class);
                intent.putExtra("group", groupBean);
                startActivity(intent);
            }
        }else if(v.getId()==R.id.im_rl_clear){
            showCommonDialog("是否确认清空该群聊天记录？", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dissCommonDialog();
                    cleaSucess();
                }
            });

        }else if(v.getId()==R.id.im_iv_zd){
            IMConversationBean imPersonBean = daoUtils.queryConversationBean(groupBean.getConversationId());
            if(imPersonBean==null){
                return;
            }
            if(isSetTop){
                mIvZhid.setImageResource(R.mipmap.im_chat_detail_off);
                IMPreferenceUtil.setPreference_Boolean(groupBean.getConversationId() + IMSConfig.IM_CONVERSATION_ZHI_DING,false);
                isSetTop=!isSetTop;
                imPersonBean.setIsSetTop(false);
                imPersonBean.setSetTopTime(System.currentTimeMillis());
                //为了重启app删除本地数据失效
                IMPreferenceUtil.setPreference_Boolean(IMSConfig.IM_IS_SET_TOP+groupBean.getConversationId(),false);
                IMPreferenceUtil.setPreference_String(IMSConfig.IM_SET_TOP_TIME+groupBean.getConversationId(),System.currentTimeMillis()+"");

            }else {
                mIvZhid.setImageResource(R.mipmap.im_chat_detail_on);
                IMPreferenceUtil.setPreference_Boolean(groupBean.getConversationId() + IMSConfig.IM_CONVERSATION_ZHI_DING,true);
                isSetTop=!isSetTop;
                imPersonBean.setIsSetTop(true);
                imPersonBean.setSetTopTime(System.currentTimeMillis());
                IMPreferenceUtil.setPreference_Boolean(IMSConfig.IM_IS_SET_TOP+groupBean.getConversationId(),true);
                IMPreferenceUtil.setPreference_String(IMSConfig.IM_SET_TOP_TIME+groupBean.getConversationId(),System.currentTimeMillis()+"");
            }
            daoUtils.updateConversationData(imPersonBean);
            EventBus.getDefault().post(new IMMessageNoticelPersonEvevt());
        }else if(v.getId()==R.id.im_iv_notice){
            if(!isNoticed){
                mIvNotice.setImageResource(R.mipmap.im_chat_detail_off);
                IMPreferenceUtil.setPreference_Boolean(groupBean.getConversationId() + IMSConfig.IM_CONVERSATION_NO_NOTICE,false);
                EventBus.getDefault().post(new IMMessageNoticelPersonEvevt());
                isNoticed=!isNoticed;
            }else {
                mIvNotice.setImageResource(R.mipmap.im_chat_detail_on);
                IMPreferenceUtil.setPreference_Boolean(groupBean.getConversationId() + IMSConfig.IM_CONVERSATION_NO_NOTICE,true);
                EventBus.getDefault().post(new IMMessageNoticelPersonEvevt());
                isNoticed=!isNoticed;
            }
        } else if(v.getId()==R.id.im_rl_notice){
            detailDiglog.showTextDiglog("群公告",content);
        } else if(v.getId()== R.id.im_rl_back){
            showCommonDialog("退出群聊会清空该群所有信息，是否确认退出？", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dissCommonDialog();
                    quitGroupChat();
                }
            });


        } else if(v.getId()== R.id.tv_chakan){
            if(IMStopClickFast.isFastClick()) {
                if (TextUtils.isEmpty(groupId)) {
                    return;
                }
                Intent intent = new Intent(this, IMTotleMemberActivity.class);
                intent.putExtra("groupId", groupId);
                startActivity(intent);
            }
        }else if(v.getId()==R.id.iv_group_head){
            if(IMStopClickFast.isFastClick()) {
//                Intent intent = new Intent(this, IMPhotoViewActivity.class);
//                intent.putExtra("url", groupBean.getConversationavatar());
//                intent.putExtra("type", "1");
//                startActivity(intent);
                ImagePreview.getInstance()
                        // 上下文，必须是activity，不需要担心内存泄漏，本框架已经处理好；
                        .setContext(this)

                        .setIndex(0)

                        .setLoadStrategy(ImagePreview.LoadStrategy.Default)
                        // 缩放动画时长，单位ms
                        .setZoomTransitionDuration(1000)

                        .setImage(groupBean.getConversationavatar())
                        //设置是否显示下载按钮
                        .setShowDownButton(false)
                        // 开启预览
                        .start();
            }
        }else if(v.getId()==R.id.im_rl_ts){
            if(IMStopClickFast.isFastClick()) {
                Intent intent=new Intent(IMGroupInforActivity.this, IMComplaintActivity.class);
                intent.putExtra("groupId",groupId);
                startActivity(intent);
            }
        }
    }
    /**
     * 退出群聊
     */
    private void quitGroupChat() {
        IMBetGetBean bean = new IMBetGetBean();
        bean.setGroupId(groupBean.getConversationId());
        String json = new Gson().toJson(bean);
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"),json);

        IMHttpsService.quetNewGroupJson(body, new IMHttpResultObserver<String>() {
            @Override
            public void onSuccess(String data, String message) {
                finish();
                EventBus.getDefault().post(new IMDeleteGroupEvent(groupBean.getConversationId()));
            }
            @Override
            public void _onError(Throwable e) {
                Toast.makeText(IMGroupInforActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();

            }

            @Override
            public void _onErrorLocal(Throwable e, String message, String code) {
                Toast.makeText(IMGroupInforActivity.this, message, Toast.LENGTH_SHORT).show();
            }
        });
    }



    public void cleaSucess() {
        //更新会话数据库发送状态
        List<IMConversationBean> persondatas = daoUtils.queryAllConversationData();
        for (int i = 0; i < persondatas.size(); i++) {
            if(persondatas.get(i).getConversationId().equals(groupBean.getConversationId())){
                persondatas.get(i).setLastMessageSendstate(IMSConfig.IM_SEND_STATE_SUCCESS);
                persondatas.get(i).setLastMessageTime(System.currentTimeMillis());
                persondatas.get(i).setLastMessageContent("");
                daoUtils.updateConversationData(persondatas.get(i));
                //更新消息数据库发送状态
                daoUtils.deleteConversationDetailAccordField(persondatas.get(i).getConversationId());
                EventBus.getDefault().post(new IMMessageDetelGroupEvevt(persondatas.get(i).getConversationId()));
            }
        }
        Toast.makeText(this, "清空聊天记录成功", Toast.LENGTH_SHORT).show();
    }


    private void getGroupData( IMGroupNoticeBean.IMGroupNoticeDetail  bean) {
        mTvTitle.setText("群聊详情("+bean.getMemberCount()+")");
        initRecycleView(bean);
        groupId=bean.getGroupId();
        IMLogUtil.d("MyOwnTag:", "getGroupData " +"(onResponse) " +"群信息"+new Gson().toJson(bean));
        mTvName.setText(bean.getGroupName());
        String name = IMPreferenceUtil.getPreference_String(IMSConfig.SAVE_NAME, "");
        if( bean.getGroupNotice()!=null &&  bean.getGroupNotice().getFixedNoticeContent()!=null){
            content=bean.getGroupNotice().getFixedNoticeContent();
        }
        if(TextUtils.isEmpty(bean.getGroupAvatar())){
            mGroupHeader.setVisibility(View.GONE);
        }else {
            mGroupHeader.setVisibility(View.VISIBLE);
//            IMImageLoadUtil.ImageLoadlineCircle(IMGroupInforActivity.this,bean.getGroupAvatar(),mGroupHeader);
            Glide.with(getApplicationContext()).load(bean.getGroupAvatar())
                    .apply(RequestOptions.bitmapTransform(new RoundedCornersTransformation(SizeUtils.dp2px(5), 0)))
                    .into(mGroupHeader);
        }

        if(bean.getGroupNotice()!=null && !TextUtils.isEmpty(bean.getGroupNotice().getFixedNoticeContent())){
            mTvNotice.setText(bean.getGroupNotice().getFixedNoticeContent());
        }
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(FinishGroupInforEvent event) {
        finish();
    }

}
