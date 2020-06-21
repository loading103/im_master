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

import com.android.im.IMSManager;
import com.android.im.R;
import com.android.im.imbean.IMAddFriendBean;
import com.android.im.imeventbus.IMAddFriendSucessEvent;
import com.android.im.imeventbus.IMDeleteFriendSucessEvent;
import com.android.im.imeventbus.IMGoToFirstFragmentEvent;
import com.android.im.imeventbus.IMLoginTimeOutEvent;
import com.android.im.imnet.IMHttpResultObserver;
import com.android.im.imnet.http.IMHttpsService;
import com.android.im.imutils.IMImageLoadUtil;
import com.android.im.imutils.IMPersonUtils;
import com.android.im.imutils.IMStatusBarUtil;
import com.android.im.imutils.IMStopClickFast;
import com.android.im.imview.dialog.IMDialogUtils;
import com.android.im.imview.impopwindow.IMPopWindowUtil;
import com.android.im.imview.impromptlibrary.IMPromptButton;
import com.android.im.imview.impromptlibrary.IMPromptButtonListener;
import com.android.nettylibrary.greendao.entity.IMConversationBean;
import com.android.nettylibrary.greendao.entity.IMPersonBean;
import com.android.nettylibrary.http.IMPersonBeans;
import com.android.nettylibrary.http.IMUserInforBean;
import com.blankj.utilcode.util.SizeUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.exoplayer2.util.Log;
import com.google.gson.Gson;
import com.shehuan.niv.NiceImageView;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import cc.shinichi.library.ImagePreview;
import jp.wasabeef.glide.transformations.RoundedCornersTransformation;
import okhttp3.RequestBody;

import static com.android.im.imnet.IMBaseConstant.JSON;


public class IMContactDetailActivity extends IMBaseActivity implements View.OnClickListener, DialogInterface.OnClickListener {
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
    private IMPersonBean person ;
    private IMPersonBean personBean;
    private  TextView mTvNoTitle;
    private  TextView mTvNoMz;
    private  ImageView mIvBg;
    private LinearLayout mllDelete;
    private String headUrl;
    private IMUserInforBean.UserInforData bean0;
    private TextView mTvNoLeve;
    private boolean  isreturn=false; //==true 是单聊界面点击头像进来的   点击发送按钮应该返回
    private boolean isfriend=true;
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
        mIvLevel = findViewById(R.id.im_iv_level);
        mTvNoMz=findViewById(R.id.im_no_mz);
        mIvBg=findViewById(R.id.im_iv_bg);
        mTvNoTitle=findViewById(R.id.im_no_title);
        mTvType=findViewById(R.id.im_tv_type);
        mTvTitle = findViewById(R.id.im_tv_title);
        mTvShoot = findViewById(R.id.im_tv_shoot);
        mIvSend = findViewById(R.id.im_iv_send);
        mTvNoLeve=findViewById(R.id.im_no_leve);
        mRlTitle = findViewById(R.id.im_ll_title);
        mRlShoot = findViewById(R.id.im_ll_shoot);
        mllDelete = findViewById(R.id.ll_delete);
        mllDelete.setOnClickListener(this);
        mIvFinish.setOnClickListener(this);
        mIvRight.setOnClickListener(this);
        mIvSend.setOnClickListener(this);
        mIvHead.setOnClickListener(this);
    }

    private void initData() {
        person=(IMPersonBean)getIntent().getSerializableExtra("bean");
        isreturn=getIntent().getBooleanExtra("isreturn",false);
        if(person==null){
            return;
        }
        personBean = daoUtils.queryMessageBean(person.getMemberId());
        if(personBean ==null){
            mIvSend.setText("添加好友");
            mIvRight.setVisibility(View.GONE);
            isfriend=false;
        }else {
            handlerData();
        }
        getHttpData();
    }
    /**
     * 加载本地缓存
     */
    private void handlerData(){
//        mIvSend.setImageResource(R.mipmap.im_member_send);
        mIvSend.setText("发送消息");
        mIvRight.setVisibility(View.VISIBLE);
        isfriend=true;

        headUrl=personBean.getAvatar();
        //姓名
        mTvName.setText(personBean.getNickName());
        //头像
        IMPersonUtils.setHeadUrl(mIvHead,personBean.getAvatar());
        //个性签名
        IMPersonUtils.setSignaTure(mTvSigne,personBean.getSignature());
        //用户类型
        setMemberType(personBean.getType());
        //vip等级
        handleVipLeve(mIvLevel,personBean.getLevel());
        //头衔
        IMPersonUtils.setTitile(mRlTitle,mTvNoTitle,mTvTitle,personBean.getTitle());
        //背景
        IMPersonUtils.setBgUrl(mIvBg,personBean.getBgUrl());


    }
    /**
     * 获取群成员信息
     */
    private void getHttpData() {
        IMPersonBeans bean = new IMPersonBeans();
        bean.setCustomerId(person.getMemberId());
        String json = new Gson().toJson(bean);
        RequestBody body = RequestBody.create(JSON, json);
        IMHttpsService.GetMemberInforJson(body,new IMHttpResultObserver<IMUserInforBean.UserInforData>() {
            @Override
            public void onSuccess(IMUserInforBean.UserInforData data, String message) {
                handlerHttpData(data);
            }

            @Override
            public void _onError(Throwable e) {
            }

            @Override
            public void _onErrorLocal(Throwable e, String message, String code) {
                Toast.makeText(IMContactDetailActivity.this,message, Toast.LENGTH_SHORT).show();
                if(code.equals("NO_LOGIN")){
                    EventBus.getDefault().post(new IMLoginTimeOutEvent());
                }

            }
        });
    }

    /**
     * 接口更新数据库
     */
    private void handlerHttpData(IMUserInforBean.UserInforData data) {
        if(data==null ){
            return;
        }
        bean0 = data;
        if( (personBean==null ||TextUtils.isEmpty(personBean.getBgUrl())) &&  !TextUtils.isEmpty(data.getBgUrl())){
            IMImageLoadUtil.CommonImageBgLoadCp(this, data.getBgUrl(),mIvBg);
        }
        if(personBean!=null){
            personBean.setBgUrl(data.getBgUrl());
            personBean.setNickName(data.getNickName());
            personBean.setAvatar(data.getAvatar());
            personBean.setIsKefu(data.getIsKefu());
            personBean.setLevel(data.getLevel());
            personBean.setSignature(data.getSignature());
            personBean.setTitle(data.getTitle());
            personBean.setType(data.getType());
            daoUtils.updateMessageData(personBean);
        }else {
            personBean=new IMPersonBean();
            personBean.setBgUrl(data.getBgUrl());
            personBean.setNickName(data.getNickName());
            personBean.setAvatar(data.getAvatar());
            personBean.setIsKefu(data.getIsKefu());
            personBean.setLevel(data.getLevel());
            personBean.setSignature(data.getSignature());
            personBean.setTitle(data.getTitle());
            personBean.setType(data.getType());
            handlerData();
        }
        if(!TextUtils.isEmpty(data.getIsFriend()) && data.getIsFriend().equals("Y")){
            isfriend=true;
            mIvSend.setText("发送消息");
            mIvRight.setVisibility(View.VISIBLE);
        }else {
            isfriend=false;
            mIvSend.setText("添加好友");
            mIvRight.setVisibility(View.VISIBLE);
        }
        if(!TextUtils.isEmpty(personBean.getIsKefu()) && personBean.getIsKefu().equals("Y")){
            mIvRight.setVisibility(View.GONE);
        }
    }
    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.iv_top_finish){
            finish();
        }else  if(v.getId()==R.id.iv_top_right){
            IMPopWindowUtil.getInstance(this).deleteFriendView(this, new IMPromptButtonListener() {
                @Override
                public void onClick(IMPromptButton button) {
                    IMDialogUtils.getInstance().showCommonDialog(IMContactDetailActivity.this,"删除好友将清空好友信息和聊天记录，是否确认删除好友？", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            IMDialogUtils.getInstance().dismissCommonDiglog();
                            if(isfriend){
                                deleteFriend();
                            }else {  //如果不是好友   就因该删除数据库
                                if(bean0!=null && !TextUtils.isEmpty(bean0.getCustomerId())){
                                    EventBus.getDefault().post(new IMDeleteFriendSucessEvent(bean0.getCustomerId()));
                                    finish();
                                }
                            }

                        }
                    });
                }
            });
        }else  if(v.getId()==R.id.ll_delete){
            mllDelete.setVisibility(View.GONE);

        }else if(v.getId()==R.id.im_iv_send){
            if(isfriend){  //是好友发送消息
                if(isreturn){
                    finish();
                }else {
                    goToIMPersonalChatActivity();
                }
            }else { //不是好友添加好友
                if(bean0==null){
                    return;
                }
                if(bean0.getIsFriendValid().equals("N")){
                    addFriendData();
                }else {
                    if(IMStopClickFast.isFastClick()){
                        Intent intent=new Intent(IMContactDetailActivity.this,IMAddFriendVertyActivity.class);
                        intent.putExtra("bean",bean0);
                        startActivity(intent);
                    }

                }
            }

        }else if(v.getId()==R.id.im_iv_head){
            if(IMStopClickFast.isFastClick()) {
//                Intent intent = new Intent(this, IMPhotoViewActivity.class);
//                intent.putExtra("url", headUrl);
//                intent.putExtra("type", "1");
//                startActivity(intent);
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
    }
    /**
     * 就跳转到好友界面
     */
    private void goToIMPersonalChatActivity() {
        if (IMStopClickFast.isFastClick()) {
            Intent intent = new Intent(IMContactDetailActivity.this, IMPersonalChatActivity.class);
            IMConversationBean bean = new IMConversationBean();
            bean.setConversationId(person.getMemberId());
            bean.setConversationName(person.getNickName());
            bean.setConversationavatar(person.getAvatar());
            bean.setMemberId(person.getMemberId());
            intent.putExtra("user", bean);
            startActivity(intent);
            EventBus.getDefault().post(new IMGoToFirstFragmentEvent());
            finish();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    finish();
                }
            }, 1500);
        }
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {

    }
    /**
     * 用户类型
     */
    private void setMemberType(String type) {
     IMPersonUtils.setUseType(type,mTvType);
    }
    /**
     * 处理vip等级
     */
    private void handleVipLeve(ImageView leveImage, String leve) {
        if(TextUtils.isEmpty(leve)){
//            mTvNoLeve.setVisibility(View.VISIBLE);
            mTvNoLeve.setVisibility(View.GONE);
            IMPersonUtils.setVip("VIP1",leveImage);
            return;
        }
        mTvNoLeve.setVisibility(View.GONE);
        IMPersonUtils.setVip(leve,leveImage);
    }
    /**
     * 删除好友
     */
    private void deleteFriend() {
        IMPersonBeans bean = new IMPersonBeans();
        List<String> list=new ArrayList<>();
        if(bean0==null || TextUtils.isEmpty(bean0.getCustomerId())){
            Toast.makeText(IMContactDetailActivity.this,"删除失败，请稍后再试", Toast.LENGTH_SHORT).show();
            return;
        }
        list.add(bean0.getCustomerId());
        bean.setIds(list);
        String json = new Gson().toJson(bean);
        RequestBody body = RequestBody.create(JSON, json);
        Log.e("----",json);
        IMHttpsService.deleteFriendJson(body,new IMHttpResultObserver<String>() {
            @Override
            public void onSuccess(String data, String message) {
                dismissLoadingDialog();
                if(message.contains("成功")){
                    Toast.makeText(IMContactDetailActivity.this,"删除成功", Toast.LENGTH_SHORT).show();
                    EventBus.getDefault().post(new IMDeleteFriendSucessEvent(bean0.getCustomerId()));
                    finish();
                }
            }
            @Override
            public void _onError(Throwable e) {
                dismissLoadingDialog();
                Toast.makeText(IMContactDetailActivity.this,e.getMessage(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void _onErrorLocal(Throwable e, String message, String code) {
                dismissLoadingDialog();
                Toast.makeText(IMContactDetailActivity.this,message, Toast.LENGTH_SHORT).show();
            }
        });
    }
    /**
     * 添加好友
     */
    private void addFriendData() {
        IMPersonBeans bean = new IMPersonBeans();
        bean.setAcceptCustomerId(person.getCustomerId());
        String json = new Gson().toJson(bean);
        RequestBody body = RequestBody.create(JSON, json);
        Log.e("----",json);
        IMHttpsService.addNewFriendJson(body,new IMHttpResultObserver<IMAddFriendBean>() {
            @Override
            public void onSuccess(IMAddFriendBean data, String message) {
                dismissLoadingDialog();
                if(message.contains("成功")){
                    Toast.makeText(IMContactDetailActivity.this,"添加成功", Toast.LENGTH_SHORT).show();
                    EventBus.getDefault().post(new IMAddFriendSucessEvent(bean0));
                    finish();
                }
            }
            @Override
            public void _onError(Throwable e) {
                dismissLoadingDialog();
                IMSManager.getInstance().setLoginInListener(false,e.getMessage());
                Toast.makeText(IMContactDetailActivity.this,e.getMessage(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void _onErrorLocal(Throwable e, String message, String code) {
                dismissLoadingDialog();
                IMSManager.getInstance().setLoginInListener(false,message);
                Toast.makeText(IMContactDetailActivity.this,message, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
