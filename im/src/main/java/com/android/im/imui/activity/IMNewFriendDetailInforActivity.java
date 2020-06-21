package com.android.im.imui.activity;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
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
import com.android.im.imbean.IMApplyInforBean;
import com.android.im.imbean.IMFriendsBean;
import com.android.im.imeventbus.IMAgreeAddFriendSucessedEvent;
import com.android.im.imnet.IMHttpResultObserver;
import com.android.im.imnet.http.IMHttpsService;
import com.android.im.imutils.IMImageLoadUtil;
import com.android.im.imutils.IMPersonUtils;
import com.android.im.imutils.IMStatusBarUtil;
import com.android.nettylibrary.greendao.entity.IMConversationBean;
import com.android.nettylibrary.greendao.entity.IMPersonBean;
import com.android.nettylibrary.http.IMPersonBeans;
import com.blankj.utilcode.util.SizeUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.exoplayer2.util.Log;
import com.google.gson.Gson;
import com.shehuan.niv.NiceImageView;

import org.greenrobot.eventbus.EventBus;

import jp.wasabeef.glide.transformations.CropCircleWithBorderTransformation;
import jp.wasabeef.glide.transformations.RoundedCornersTransformation;
import okhttp3.RequestBody;

import static com.android.im.imnet.IMBaseConstant.JSON;


public class IMNewFriendDetailInforActivity extends IMBaseActivity implements View.OnClickListener, DialogInterface.OnClickListener {
    private ImageView mIvFinish;
    private ImageView mIvRight;
    private ImageView mIvLevel;
    private NiceImageView mIvHead;
    private TextView mTvName;
    private TextView mTvSigne;
    private TextView mTvId;
    private TextView mTvTitle;
    private TextView mTvShoot;
    private TextView mTvType;
    private ImageView mIvSend;
    private TextView mTvContent;
    private RelativeLayout mRlTitle;
    private RelativeLayout mRlShoot;
    private IMFriendsBean person ;
    private  ImageView mIvBg;
    private TextView mTvleve;
    private TextView mTvAdd;
    private TextView mIvAdds;
    private TextView mTvNoTitle;
    private TextView mTvNoMz;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_im_recieve_add);
        initView();
        initData();
    }
    @SuppressLint("WrongViewCast")
    private void initView() {
        IMStatusBarUtil.setTranslucentForImageView(this,0,null);
        mIvFinish = findViewById(R.id.iv_top_finish);
        mIvRight = findViewById(R.id.iv_top_right);
        mIvHead = findViewById(R.id.im_iv_head);
        mTvName = findViewById(R.id.im_tv_name);
        mTvId = findViewById(R.id.im_tv_id);
        mTvSigne = findViewById(R.id.im_tv_signe);
        mIvLevel = findViewById(R.id.im_iv_level);
        mIvBg=findViewById(R.id.im_iv_bg);
        mTvType=findViewById(R.id.im_tv_type);
        mTvContent = findViewById(R.id.im_tv_content);
        mTvTitle = findViewById(R.id.im_tv_title);
        mTvShoot = findViewById(R.id.im_tv_shoot);
        mIvSend = findViewById(R.id.im_iv_send);
        mRlTitle = findViewById(R.id.im_ll_title);
        mTvNoTitle = findViewById(R.id.im_no_title);
        mTvNoMz = findViewById(R.id.im_no_mz);
        mRlShoot = findViewById(R.id.im_ll_shoot);
        mTvleve = findViewById(R.id.im_no_leve);
        mTvAdd = findViewById(R.id.im_tv_added);
        mIvAdds= findViewById(R.id.im_tv_adds);
        mIvFinish.setOnClickListener(this);
        mIvRight.setOnClickListener(this);
        mIvSend.setOnClickListener(this);
        mTvAdd.setOnClickListener(this);
        mIvAdds.setOnClickListener(this);
    }

    private void initData() {
        person=(IMFriendsBean)getIntent().getSerializableExtra("bean");
        if(person==null){
            return;
        }
        getHttpInforData(person.getApplyId());

    }
    /**
     * 获取申请人界面详情
     */
    private void getHttpInforData(String applyId) {
        showLoadingDialog();
        IMPersonBeans bean = new IMPersonBeans();
        bean.setApplyId(applyId);
        String json = new Gson().toJson(bean);
        RequestBody body = RequestBody.create(JSON, json);
        IMHttpsService.getApplyForDetailsJson(body, new IMHttpResultObserver<IMApplyInforBean>() {
            @Override
            public void onSuccess(IMApplyInforBean imApplyInforBean, String message) {
                dismissLoadingDialog();
                handleDataView(imApplyInforBean);
            }

            @Override
            public void _onError(Throwable e) {
                dismissLoadingDialog();
                Toast.makeText(IMNewFriendDetailInforActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void _onErrorLocal(Throwable e, String message, String code) {
                dismissLoadingDialog();
                Toast.makeText(IMNewFriendDetailInforActivity.this,message, Toast.LENGTH_SHORT).show();
            }
        });

    }

    /**
     * 初始化界面
     */
    private void handleDataView(IMApplyInforBean bean) {
        mTvContent.setText(TextUtils.isEmpty(bean.getFriendApply().getApplyRemark())?"无验证消息":"验证消息:"+person.getApplyRemark());
        mTvName.setText(TextUtils.isEmpty(bean.getNickName())?"":bean.getNickName());

        IMPersonUtils.setBgUrl(mIvBg,bean.getBgUrl());

        IMPersonUtils.setHeadUrl(mIvHead,bean.getAvatar());

        IMPersonUtils.setSignaTure(mTvSigne,bean.getSignature());

        handleVipLeve(mIvLevel,bean.getLevel());

        IMPersonUtils.setUseType(bean.getType(),mTvType);

        IMPersonUtils.setTitile(mRlTitle,mTvNoTitle,mTvTitle,bean.getTitle());

        //申请状态:1=等待通过，2=通过，3=拒绝,4=已过期
        if(bean.getFriendApply().getApplyStatus().equals("2")){
            mTvAdd.setText("已添加");
            mTvAdd.setVisibility(View.VISIBLE);
            mIvAdds.setVisibility(View.GONE);
        }else if(bean.getFriendApply().getApplyStatus().equals("3")){
            mTvAdd.setText("已拒绝");
            mTvAdd.setVisibility(View.VISIBLE);
            mIvAdds.setVisibility(View.GONE);
        }else  if(bean.getFriendApply().getApplyStatus().equals("4")){
            mTvAdd.setText("已过期");
            mTvAdd.setVisibility(View.VISIBLE);
            mIvAdds.setVisibility(View.GONE);
        }else  if(bean.getFriendApply().getApplyStatus().equals("1")){
            mTvAdd.setVisibility(View.GONE);
            mIvAdds.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.iv_top_finish){
            finish();
        }else  if(v.getId()==R.id.iv_top_right){
            finish();
        }else if(v.getId()==R.id.im_iv_send){
            if(person!=null){
                Intent intent=new Intent(IMNewFriendDetailInforActivity.this, IMPersonalChatActivity.class);
                IMConversationBean bean=new IMConversationBean();
                bean.setMemberId(person.getCustomerId());
                bean.setConversationId(person.getCustomerId());
                bean.setConversationName(person.getNickName());
                bean.setConversationavatar(person.getAvatar());
                intent.putExtra("user",bean);
                startActivity(intent);
            }
        }else  if(v.getId()==R.id.im_tv_adds){
            agreeAddFriends();
        }
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
    }


    private void handleVipLeve(ImageView leveImage, String leve) {
        if(TextUtils.isEmpty(leve)){
            mTvleve.setVisibility(View.VISIBLE);
            return;
        }
        mTvleve.setVisibility(View.GONE);
        IMPersonUtils.setVip(leve,leveImage);
    }


    /**
     * 同意添加好友
     */
    private void agreeAddFriends() {
        RequestBody bodyData = getBodyData(person.getApplyId());
        IMHttpsService.getAgreeFriendApplyJson(bodyData, new IMHttpResultObserver<String>() {
            @Override
            public void onSuccess(String s, String message) {
                mTvAdd.setText("已添加");
                mTvAdd.setVisibility(View.VISIBLE);
                mIvAdds.setVisibility(View.GONE);
                EventBus.getDefault().post(new IMAgreeAddFriendSucessedEvent());
            }

            @Override
            public void _onError(Throwable e) {
                Toast.makeText(IMNewFriendDetailInforActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void _onErrorLocal(Throwable e, String message, String code) {
                Toast.makeText(IMNewFriendDetailInforActivity.this,message, Toast.LENGTH_SHORT).show();
            }
        });
    }

    public RequestBody  getBodyData(String id){
        IMPersonBeans bean = new IMPersonBeans();
        bean.setApplyId(id);
        String json = new Gson().toJson(bean);
        RequestBody body = RequestBody.create(JSON, json);
        Log.e("--json---",json);
        return  body;
    }
}
