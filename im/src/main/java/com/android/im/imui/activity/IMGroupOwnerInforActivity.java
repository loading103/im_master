package com.android.im.imui.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.im.IMSManager;
import com.android.im.R;
import com.android.im.imadapter.IMGroupInforAdapter;
import com.android.im.imadapter.baseadapter.IMBaseRecycleViewAdapter_T;
import com.android.im.imbean.IMBetGetBean;
import com.android.im.imbean.IMImageViewBean;
import com.android.im.imeventbus.FinishGroupInforEvent;
import com.android.im.imeventbus.IMDeleteGroupEvent;
import com.android.im.imeventbus.IMMessageAddGroupInforEvent;
import com.android.im.imeventbus.IMMessageDetelGroupEvevt;
import com.android.im.imeventbus.IMMessageNoticelPersonEvevt;
import com.android.im.imeventbus.IMMessageUpdataGroupInforEvent;
import com.android.im.imeventbus.IMUpdataGroupInforEvent;
import com.android.im.imeventbus.IMUpdataGroupNoticeEvent;
import com.android.im.imnet.IMHttpResultObserver;
import com.android.im.imnet.http.IMHttpsService;
import com.android.im.imutils.IMBase64ImageUtils;
import com.android.im.imutils.IMChooseUtils;
import com.android.im.imutils.IMImageLoadUtil;
import com.android.im.imutils.IMStatusBarUtil;
import com.android.im.imutils.IMStopClickFast;
import com.android.im.imview.dialog.IMGroupOwnerDiglog;
import com.android.im.imview.dialog.IMTextDetailDiglog;
import com.android.nettylibrary.IMSConfig;
import com.android.nettylibrary.greendao.entity.IMConversationBean;
import com.android.nettylibrary.greendao.entity.IMGroupMemberBean;
import com.android.nettylibrary.greendao.greenutils.DaoUtils;
import com.android.nettylibrary.http.IMGetIMMemberData;
import com.android.nettylibrary.http.IMGroupNoticeBean;
import com.android.nettylibrary.utils.IMLogUtil;
import com.android.nettylibrary.utils.IMPreferenceUtil;
import com.blankj.utilcode.util.SizeUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.gson.Gson;
import com.zhihu.matisse.Matisse;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import cc.shinichi.library.ImagePreview;
import jp.wasabeef.glide.transformations.RoundedCornersTransformation;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static com.android.im.IMSManager.okHttpClient;
import static com.android.im.imnet.IMBaseConstant.JSON;


public class IMGroupOwnerInforActivity extends IMBaseActivity implements View.OnClickListener, IMGroupOwnerDiglog.OnFinishListener {
    private ImageView mIvFinish;
    private TextView mTvName;
    private TextView mTvChakan;
    private TextView mTvTitle;
    private LinearLayout mRlNotice;
    private ImageView mIvZhid;
    private ImageView mGroupHeader;
    private ImageView mIvNotice;
    private ImageView mRlback;
    private RelativeLayout mRlTouSu;
    private RelativeLayout mRlClear;
    private RelativeLayout mRlhositoy;
    private LinearLayout mRlcontain;
    public IMConversationBean groupBean;
    public IMTextDetailDiglog detailDiglog;
    public RecyclerView mRecycleView;
    private String groupId;
    private LinearLayout mRlName;
    private RelativeLayout mRlHead;
    private RelativeLayout mRlRemove;
    private RelativeLayout mRlAdd;

    private TextView mTvNotice;
    private TextView mTvRightName;

    private IMGroupNoticeBean groupInforbean;
    private List<IMGroupMemberBean> members;
    private IMGroupMemberBean ower;
    private IMGroupOwnerDiglog groupOwnerDiglog;
    private static final int REQUEST_CODE_CHOOSE = 10001;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_im_group_owner_infor);
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
        mRlName = findViewById(R.id.im_rl_name);
        mRlHead = findViewById(R.id.im_rl_head);
        mRlRemove = findViewById(R.id.im_rl_remove);
        mRlAdd = findViewById(R.id.im_rl_add);
        mGroupHeader = findViewById(R.id.iv_group_head);
        mRlback = findViewById(R.id.im_rl_back);
        mRecycleView = findViewById(R.id.recycle);

        mTvNotice = findViewById(R.id.tv_right_notice);
        mTvRightName = findViewById(R.id.tv_right_name);

        mRlTouSu.setOnClickListener(this);
        mRlClear.setOnClickListener(this);
        mRlhositoy.setOnClickListener(this);
        mIvZhid.setOnClickListener(this);
        mRlNotice.setOnClickListener(this);
        mIvFinish.setOnClickListener(this);
        mRlback.setOnClickListener(this);
        mTvChakan.setOnClickListener(this);
        mRlName.setOnClickListener(this);
        mRlHead.setOnClickListener(this);
        mRlRemove.setOnClickListener(this);
        mRlAdd.setOnClickListener(this);
        mIvNotice.setOnClickListener(this);
        mGroupHeader.setOnClickListener(this);
    }


    private boolean isSetTop;
    private boolean isNoticed;
    private void initData() {
        detailDiglog=new IMTextDetailDiglog(this);
        groupOwnerDiglog = new IMGroupOwnerDiglog(this);
        groupBean = (IMConversationBean)getIntent().getSerializableExtra("user");
        groupInforbean = (IMGroupNoticeBean)getIntent().getSerializableExtra("bean");
        mTvTitle.setText("群聊详情");
        isSetTop = IMPreferenceUtil.getPreference_Boolean(groupBean.getConversationId() + IMSConfig.IM_CONVERSATION_ZHI_DING, false);
        isNoticed = IMPreferenceUtil.getPreference_Boolean(groupBean.getConversationId() + IMSConfig.IM_CONVERSATION_NO_NOTICE, false);
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
        getGroupData(groupInforbean);

    }

    private String content;
    private Handler handler=new Handler();
    private void getGroupData(IMGroupNoticeBean  bean) {
        initRecycleView(bean);
        groupId=bean.getData().getGroupId();
        IMLogUtil.d("MyOwnTag:", "getGroupData " +"(onResponse) " +"群信息"+new Gson().toJson(bean));
        mTvName.setText(groupBean.getConversationName());
        mTvRightName.setText(groupBean.getConversationName());
        String name = IMPreferenceUtil.getPreference_String(IMSConfig.SAVE_NAME, "");
        mTvTitle.setText("群聊详情("+bean.getData().getMemberCount()+")");
        if( bean.getData().getGroupNotice()!=null &&  bean.getData().getGroupNotice().getFixedNoticeContent()!=null){
            content=bean.getData().getGroupNotice().getFixedNoticeContent();
            mTvNotice.setText(content);
        }
        if(TextUtils.isEmpty(bean.getData().getGroupAvatar())){
            mGroupHeader.setVisibility(View.GONE);
        }else {
            mGroupHeader.setVisibility(View.VISIBLE);
//            IMImageLoadUtil.ImageLoadlineCircle(IMGroupOwnerInforActivity.this,bean.getData().getGroupAvatar(),mGroupHeader);
            Glide.with(getApplicationContext()).load(bean.getData().getGroupAvatar())
                    .apply(RequestOptions.bitmapTransform(new RoundedCornersTransformation(SizeUtils.dp2px(5), 0)))
                    .into(mGroupHeader);
        }

    }

    /**
     * 显示群成员
     */
    public IMGroupInforAdapter madapter;
    private void initRecycleView(IMGroupNoticeBean bean) {
        ower = daoUtils.queryGroupMemberBean(bean.getData().getGroupId(),IMSManager.getMyUseId());
        members = daoUtils.queryGroupAllMemberBean(bean.getData().getGroupId(),10);
        if(members ==null || members.size()==0 || ower ==null){
            return;
        }
        if(members.contains(ower)){
            members.remove(ower);
            members.add(0, ower);
        }else {
            members.remove(0);
            members.add(0, ower);
        }
        if(members.size()>=10){
            mTvChakan.setVisibility(View.VISIBLE);
        }else {
            mTvChakan.setVisibility(View.GONE);
        }
        GridLayoutManager gridLayoutManager=new GridLayoutManager(IMGroupOwnerInforActivity.this,5);
        mRecycleView.setLayoutManager(gridLayoutManager);
        mRecycleView.setNestedScrollingEnabled(false);
        List<IMGroupMemberBean> datas=new ArrayList<>();
        madapter=new IMGroupInforAdapter(IMGroupOwnerInforActivity.this,R.layout.item_group_infor_contact, members);
        mRecycleView.setAdapter(madapter);
        madapter.setOnItemClickListner(new IMBaseRecycleViewAdapter_T.OnItemClickListner() {
            @Override
            public void onItemClickListner(View v, int position, Object t) {
                if(IMStopClickFast.isFastClick()) {
                    Intent intent = new Intent(IMGroupOwnerInforActivity.this, IMMemberInforActivity.class);
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
            groupOwnerDiglog.showPersonalDiglog(false,content,this);
        } else if(v.getId()== R.id.im_rl_back){
            showCommonDialog("解散群聊会清空该群所有信息，是否确认解散？", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dissCommonDialog();
                    removeGroupChat();
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
        } else if(v.getId()== R.id.im_rl_name){
            groupOwnerDiglog.showPersonalDiglog(true,groupBean.getConversationName(),this);
        }
        else if(v.getId()== R.id.im_rl_head){
            IMChooseUtils.choosePhotoHeadForResult(this,REQUEST_CODE_CHOOSE,1);
        }
        else if(v.getId()== R.id.im_rl_remove){
            if(IMStopClickFast.isFastClick()) {
                if (TextUtils.isEmpty(groupId)) {
                    return;
                }
                Intent intent = new Intent(this, IMRemoveMemberActivity.class);
                intent.putExtra("groupId", groupId);
                startActivity(intent);
            }
        } else if(v.getId()== R.id.im_rl_add){
            if(IMStopClickFast.isFastClick()) {
                if (TextUtils.isEmpty(groupId)) {
                    return;
                }
                Intent intent = new Intent(this, IMCreatGroupActivity.class);
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
                Intent intent=new Intent(IMGroupOwnerInforActivity.this, IMComplaintActivity.class);
                intent.putExtra("groupId",groupId);
                startActivity(intent);
            }
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    public void cleaSucess() {
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

    /**
     * 移除群聊
     */
    private void removeGroupChat() {
        IMBetGetBean bean = new IMBetGetBean();
        bean.setGroupId(groupBean.getConversationId());
        String json = new Gson().toJson(bean);
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"),json);
        IMHttpsService.deleteNewGroupJson(body, new IMHttpResultObserver<String>() {
            @Override
            public void onSuccess(String data, String message) {
                finish();
                EventBus.getDefault().post(new IMDeleteGroupEvent(groupBean.getConversationId()));
            }
            @Override
            public void _onError(Throwable e) {

            }

            @Override
            public void _onErrorLocal(Throwable e, String message, String code) {

            }
        });
    }
    /**
     * 处理群系统通知打开（每日弹窗）
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onUpdateHeadView(IMMessageUpdataGroupInforEvent event) {
        refushData();
    }
    /**
     *邀请群聊
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onUpdateHeadView(IMMessageAddGroupInforEvent event) {
        IMGetIMMemberData bean=new IMGetIMMemberData();
        bean.setGroupId(groupId);
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
                com.android.nettylibrary.http.IMGroupMemberBean bean = new Gson().fromJson(response.body().string(), com.android.nettylibrary.http.IMGroupMemberBean.class);
                IMLogUtil.d("MyOwnTag:", "onResponse " + "群成员信息"+new Gson().toJson(bean));
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        List<com.android.nettylibrary.http.IMGroupMemberBean.GroupMemberDetaiData> groups = bean.getData();
                        updataMemberInforList(groups);
                        refushData();
                    }
                });
            }
        });
    }


    /**
     * 跟新群成员数据库数据
     */
    private void updataMemberInforList(List<com.android.nettylibrary.http.IMGroupMemberBean.GroupMemberDetaiData> groups) {
        if(groups!=null || groups.size()>0){
            daoUtils.deleteGroupAllMemberData(groupId);
        }
        for (int i = 0; i < groups.size(); i++) {
            com.android.nettylibrary.greendao.entity.IMGroupMemberBean imGroupMemberBean = daoUtils.queryGroupMemberBean(groupId,groups.get(i).getMemberId());
            if(imGroupMemberBean==null){  //
                imGroupMemberBean=new com.android.nettylibrary.greendao.entity.IMGroupMemberBean();
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
                daoUtils.updateGroupMemberData(imGroupMemberBean);
            }
        }
    }

    public void refushData(){
        List<IMGroupMemberBean> imGroupMemberBeans = daoUtils.queryGroupAllMemberBean(groupInforbean.getData().getGroupId(), 10);
        if(members ==null || members.size()==0 ){
            return;
        }
        members.clear();
        members.addAll(imGroupMemberBeans);
        if(members.contains(ower)){
            members.remove(ower);
            members.add(0,ower);
        }else {
            members.remove(0);
            members.add(0,ower);
        }
        madapter.notifyDataSetChanged();
    }

    /**
     * 选择图片处理
     */
    List<String> mSelected;
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_CHOOSE && resultCode == RESULT_OK) {   //选择图片成功之后上传
            mSelected = Matisse.obtainPathResult(data);
            if (mSelected == null && mSelected.size() == 0) {
                return;
            }
            updataPickture(mSelected);
        }
    }


    /**
     * 上传图片
     */
    private void updataPickture(List<String> mSelected) {
        for (int i = 0; i < mSelected.size(); i++) {
            showLoadingDialog();
            final IMBetGetBean bean = new IMBetGetBean();
            bean.setBase64Data(IMBase64ImageUtils.imageToBase64(mSelected.get(i)));
            String json = new Gson().toJson(bean);
            RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json);
            IMHttpsService.getUpdataPicture(body, new IMHttpResultObserver<IMImageViewBean>() {
                @Override
                public void onSuccess(IMImageViewBean data, String message) {
                    dismissLoadingDialog();
                    IMLogUtil.d("上传图片成功==="+new Gson().toJson(data));
                    UpdateHeadUrl(data,null,false);
                }

                @Override
                public void _onError(Throwable e) {
                    dismissLoadingDialog();
                    Toast.makeText(IMGroupOwnerInforActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }

                @Override
                public void _onErrorLocal(Throwable e, String message, String code) {
                    dismissLoadingDialog();
                    Toast.makeText(IMGroupOwnerInforActivity.this, message, Toast.LENGTH_SHORT).show();
                }
            });
        }
    }


    /**
     * 修改群（公告和名字）
     */
    @Override
    public void setOnFinishListener(Boolean isname,String content) {
        if(isname){
            UpdateHeadUrl(null, content,true);
        }else {
            UpdateGroupNotice(content);
        }
    }


    /**
     * 修改头像或是群名字
     */
    private void UpdateHeadUrl(final IMImageViewBean data,String name,boolean isname) {
        showLoadingDialog();
        final IMBetGetBean bean = new IMBetGetBean();
        if(data!=null){
            bean.setGroupAvatar(data.getUrl());
        }
        if(name!=null){
            bean.setGroupName(name);
        }
        bean.setGroupId(groupId);
        String json = new Gson().toJson(bean);
        Log.e("-------修改群信息",json);
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json);
        IMHttpsService.getUpdateGroupInforJson(body, new IMHttpResultObserver<String>() {
            @Override
            public void onSuccess(String ss, String message) {
                dismissLoadingDialog();
                IMConversationBean bean1 = daoUtils.queryConversationBean(groupBean.getConversationId());
                if(bean1==null){
                    return;
                }
                if(!isname){
                    groupBean.setConversationavatar(data.getUrl());
                    groupBean.setGroupAvatar(data.getUrl());
                    IMImageLoadUtil.ImageLoadlineCircle(IMGroupOwnerInforActivity.this,data.getUrl(),mGroupHeader);

                    bean1.setConversationavatar(name);
                    bean1.setGroupAvatar(name);
                }else {
                    mTvName.setText(name);
                    mTvRightName.setText(name);
                    groupBean.setGroupName(name);
                    groupBean.setConversationName(name);
                    Toast.makeText(IMGroupOwnerInforActivity.this, "修改群名称成功", Toast.LENGTH_SHORT).show();
                    bean1.setGroupName(name);
                    bean1.setConversationName(name);
                }

                DaoUtils.getInstance().updateConversationData(bean1);
                EventBus.getDefault().post(new IMUpdataGroupInforEvent(groupBean));
            }

            @Override
            public void _onError(Throwable e) {
                dismissLoadingDialog();
                Toast.makeText(IMGroupOwnerInforActivity.this, "修改失败，请稍后再试", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void _onErrorLocal(Throwable e, String message, String code) {
                dismissLoadingDialog();
                Toast.makeText(IMGroupOwnerInforActivity.this, "修改失败，请稍后再试", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * 修改群公告
     */
    private void UpdateGroupNotice(String contents) {
        showLoadingDialog();
        final IMBetGetBean bean = new IMBetGetBean();
        bean.setGroupId(groupId);
        bean.setFixedNoticeStatus("Y");
        bean.setFixedNoticeContent(contents);
        String json = new Gson().toJson(bean);
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json);
        IMHttpsService.getUpdateGroupNoticeJson(body, new IMHttpResultObserver<String>() {
            @Override
            public void onSuccess(String ss, String message) {
                dismissLoadingDialog();
                Toast.makeText(IMGroupOwnerInforActivity.this, "修改公告成功", Toast.LENGTH_SHORT).show();
                content=contents;
                mTvNotice.setText(content);
                groupInforbean.getData().getGroupNotice().setFixedNoticeContent(contents);
                EventBus.getDefault().post(new IMUpdataGroupNoticeEvent(groupInforbean));
            }

            @Override
            public void _onError(Throwable e) {
                dismissLoadingDialog();
                Toast.makeText(IMGroupOwnerInforActivity.this, "修改失败，请稍后再试", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void _onErrorLocal(Throwable e, String message, String code) {
                dismissLoadingDialog();
                Toast.makeText(IMGroupOwnerInforActivity.this, "修改失败，请稍后再试", Toast.LENGTH_SHORT).show();
            }
        });
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(FinishGroupInforEvent event) {
        finish();
    }
}
