package com.android.im.imui.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.android.im.IMSManager;
import com.android.im.R;
import com.android.im.imbean.IMAddFriendBean;
import com.android.im.imeventbus.FinishGroupInforEvent;
import com.android.im.imeventbus.IMAddFriendSucessEvent;
import com.android.im.imnet.IMHttpResultObserver;
import com.android.im.imnet.http.IMHttpsService;
import com.android.im.imutils.IMImageLoadUtil;
import com.android.im.imutils.IMPersonUtils;
import com.android.im.imutils.IMStatusBarUtil;
import com.android.nettylibrary.IMSConfig;
import com.android.nettylibrary.greendao.entity.IMConversationBean;
import com.android.nettylibrary.greendao.entity.IMGroupMemberBean;
import com.android.nettylibrary.greendao.entity.IMPersonBean;
import com.android.nettylibrary.http.IMPersonBeans;
import com.android.nettylibrary.http.IMUserInforBean;
import com.android.nettylibrary.utils.IMPreferenceUtil;
import com.blankj.utilcode.util.SizeUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.exoplayer2.util.Log;
import com.google.gson.Gson;
import com.shehuan.niv.NiceImageView;

import org.greenrobot.eventbus.EventBus;

import cc.shinichi.library.ImagePreview;
import jp.wasabeef.glide.transformations.RoundedCornersTransformation;
import okhttp3.RequestBody;

import static com.android.im.imnet.IMBaseConstant.JSON;


public class IMMemberInforActivity extends IMBaseActivity implements View.OnClickListener, DialogInterface.OnClickListener {
    private RelativeLayout mIvFinish;
    private ImageView mIvRight;
    private ImageView mIvLevel;
    private NiceImageView mIvHead;
    private TextView mTvName;
    private TextView mTvSigne;
    private TextView mTvId;
    private TextView mTvTitle;
    private TextView mTvShoot;
    private TextView mTvType;
    private TextView mIvSend;
    private RelativeLayout mRlTitle;
    private RelativeLayout mRlShoot;
    private IMGroupMemberBean person ;
    private IMPersonBean personBean;
    private  TextView mTvNoTitle;
    private  TextView mTvNoMz;
    private String headUrl;
    private  ImageView mIvBg;
    private String sendId;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_im_member_infor);
        initView();
        initData();
    }
    private void initView() {
        IMStatusBarUtil.setTranslucentForImageView(this,0,null);
        mIvFinish = findViewById(R.id.iv_top_finish);
        mIvRight = findViewById(R.id.iv_top_right);
        mIvHead = findViewById(R.id.im_iv_head);
        mTvName = findViewById(R.id.im_tv_name);
        mTvId = findViewById(R.id.im_tv_id);
        mTvSigne = findViewById(R.id.im_tv_signe);
        mIvBg=findViewById(R.id.im_iv_bg);
        mIvLevel = findViewById(R.id.im_iv_level);
        mTvNoTitle=findViewById(R.id.im_no_title);
        mTvTitle = findViewById(R.id.im_tv_title);
        mTvShoot = findViewById(R.id.im_tv_shoot);
        mIvSend = findViewById(R.id.im_iv_send);
        mRlTitle = findViewById(R.id.im_ll_title);
        mRlShoot = findViewById(R.id.im_ll_shoot);
        mTvNoMz=findViewById(R.id.im_no_mz);
        mTvType=findViewById(R.id.im_tv_type);
        mTvNoLeve=findViewById(R.id.im_no_leve);
        mIvFinish.setOnClickListener(this);
        mIvRight.setOnClickListener(this);
        mIvSend.setOnClickListener(this);
        mIvHead.setOnClickListener(this);
    }

    private void initData() {
        //保存的背景
        person=(IMGroupMemberBean)getIntent().getSerializableExtra("bean");
        if(person==null){
            sendId = getIntent().getStringExtra("sendId");
        }
        if(person==null && TextUtils.isEmpty(sendId)){
            return;
        }
        if(person!=null && person.getMemberId().equals(IMSManager.getMyUseId())){
            handlerSelfData();
            return;
        }
        if(person!=null) {
            personBean = daoUtils.queryMessageBean(person.getMemberId());
            if (personBean == null) {
//                mIvSend.setImageResource(R.mipmap.im_member_add);
                mIvSend.setText("添加好友");
                mIvRight.setVisibility(View.GONE);
            } else {
//                mIvSend.setImageResource(R.mipmap.im_member_send);
                mIvSend.setText("发送消息");
                mIvRight.setVisibility(View.VISIBLE);
            }
        }else {
//            mIvSend.setImageResource(R.mipmap.im_member_add);
            mIvSend.setText("添加好友");
            mIvRight.setVisibility(View.GONE);
        }
        getHttpData();
    }
    /**
     * 如果是自己
     */
    private void handlerSelfData() {
        String title = IMPreferenceUtil.getPreference_String(IMSConfig.SAVE_TITLE, "");
        String type = IMPreferenceUtil.getPreference_String(IMSConfig.SAVE_USERTYPE, "");
        String level = IMPreferenceUtil.getPreference_String(IMSConfig.SAVE_LEVEL, "");
        setViewBackGroup(mIvBg);
        mIvSend.setVisibility(View.GONE);
        mIvRight.setVisibility(View.GONE);

        headUrl=IMSManager.getMyHeadView();

        mTvName.setText(IMSManager.getMyNickName());

        IMPersonUtils.setSignaTure(mTvSigne,IMSManager.getMySignature());

        IMPersonUtils.setHeadUrl(mIvHead,IMSManager.getMyHeadView());

        IMPersonUtils.setTitile(mRlTitle,mTvNoTitle,mTvTitle,title);

        setMemberType(type);

        handleVipLeve(mIvLevel,level);
    }
    /**
     * 获取群成员信息
     */
    private void getHttpData() {
        showLoadingDialog();
        IMPersonBeans bean = new IMPersonBeans();
        if(person==null){
            bean.setCustomerId(sendId);
        }else {
            bean.setCustomerId(person.getMemberId());
        }
        String json = new Gson().toJson(bean);
        RequestBody body = RequestBody.create(JSON, json);
        IMHttpsService.GetMemberInforJson(body,new IMHttpResultObserver<IMUserInforBean.UserInforData>() {
            @Override
            public void onSuccess(IMUserInforBean.UserInforData data, String message) {
                dismissLoadingDialog();
                handlerData(data);
            }

            @Override
            public void _onError(Throwable e) {
                dismissLoadingDialog();
                IMSManager.getInstance().setLoginInListener(false,e.getMessage());
                Toast.makeText(IMMemberInforActivity.this,e.getMessage(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void _onErrorLocal(Throwable e, String message, String code) {
                dismissLoadingDialog();
                IMSManager.getInstance().setLoginInListener(false,message);
                Toast.makeText(IMMemberInforActivity.this,message, Toast.LENGTH_SHORT).show();
            }
        });
    }
    private IMUserInforBean.UserInforData bean0;
    private void handlerData(IMUserInforBean.UserInforData data) {
        if(data==null){
            return;
        }
        IMImageLoadUtil.CommonImageBgLoadCp(this, data.getBgUrl(),mIvBg);
        if(data.getCustomerId().equals(IMSManager.getMyUseId())){
            mIvSend.setVisibility(View.GONE);
        }
        bean0 = data;
        headUrl=data.getAvatar();

        mTvName.setText(data.getNickName());
        IMPersonUtils.setHeadUrl(mIvHead,data.getAvatar());
        IMPersonUtils.setSignaTure(mTvSigne,data.getSignature());
        setMemberType(data.getType());
        handleVipLeve(mIvLevel,data.getLevel());
        IMPersonUtils.setTitile(mRlTitle,mTvNoTitle,mTvTitle,data.getTitle());
    }


    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.iv_top_finish){
            finish();
        }else  if(v.getId()==R.id.iv_top_right){
            finish();
        }else if(v.getId()==R.id.im_iv_send){
            if(personBean==null){
                if(!TextUtils.isEmpty(bean0.getIsFriendValid()) && bean0.getIsFriendValid().equals("N")){
                    addFriendData();
                }else {
                    Intent intent=new Intent(IMMemberInforActivity.this,IMAddFriendVertyActivity.class);
                    intent.putExtra("bean",bean0);
                    startActivity(intent);
                }
            }else {
                goToIMPersonalChatActivity();
            }
        }else  if(v.getId()==R.id.im_iv_head){
//            Intent intent = new Intent(this, IMPhotoViewActivity.class);
//            intent.putExtra("url", headUrl);
//            intent.putExtra("type", "1");
//            startActivity(intent);
            ImagePreview.getInstance()
                    // 上下文，必须是activity，不需要担心内存泄漏，本框架已经处理好；
                    .setContext(this)

                    .setIndex(0)

                    .setLoadStrategy(ImagePreview.LoadStrategy.Default)
                    // 缩放动画时长，单位ms
                    .setZoomTransitionDuration(1000)

                    .setImage(headUrl)
                    //设置是否显示下载按钮
                    .setShowDownButton(false)
                    // 开启预览
                    .start();
        }
    }

    /**
     * 添加好友
     */
    private void addFriendData() {
        IMPersonBeans bean = new IMPersonBeans();
        bean.setAcceptCustomerId(bean0.getCustomerId());
        String json = new Gson().toJson(bean);
        RequestBody body = RequestBody.create(JSON, json);
        Log.e("----",json);
        IMHttpsService.addNewFriendJson(body,new IMHttpResultObserver<IMAddFriendBean>() {
            @Override
            public void onSuccess(IMAddFriendBean data, String message) {
                dismissLoadingDialog();
                if(message.contains("成功")){
                    Toast.makeText(IMMemberInforActivity.this,"添加成功", Toast.LENGTH_SHORT).show();
                    EventBus.getDefault().post(new IMAddFriendSucessEvent(bean0));
                    finish();
                }
            }
            @Override
            public void _onError(Throwable e) {
                dismissLoadingDialog();
                IMSManager.getInstance().setLoginInListener(false,e.getMessage());
                Toast.makeText(IMMemberInforActivity.this,e.getMessage(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void _onErrorLocal(Throwable e, String message, String code) {
                dismissLoadingDialog();
                IMSManager.getInstance().setLoginInListener(false,message);
                Toast.makeText(IMMemberInforActivity.this,message, Toast.LENGTH_SHORT).show();
            }
        });
    }
    /**
     * 如果是好友，就跳转到好友界面
     */
    private void goToIMPersonalChatActivity() {
        Intent intent=new Intent(IMMemberInforActivity.this, IMPersonalChatActivity.class);
        IMConversationBean bean=new IMConversationBean();
        bean.setMemberId(person.getMemberId());
        bean.setConversationId(person.getMemberId());
        bean.setConversationName(person.getNickName());
        bean.setConversationavatar(person.getAvatar());
        intent.putExtra("user",bean);
        startActivity(intent);
        EventBus.getDefault().post(new FinishGroupInforEvent());
        finish();
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
    }


    /**
     * 用户类型
     */
    private void setMemberType(String data) {
        IMPersonUtils.setUseType(data,mTvType);
    }
    /**
     * 处理vip等级
     */
    private TextView mTvNoLeve;
    private void handleVipLeve(ImageView leveImage, String leve) {
        if(TextUtils.isEmpty(leve)){
            mTvNoLeve.setVisibility(View.VISIBLE);
            return;
        }
        mTvNoLeve.setVisibility(View.GONE);
        IMPersonUtils.setVip(leve,leveImage);
    }

}
