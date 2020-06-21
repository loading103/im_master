package com.android.im.imui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.android.im.R;
import com.android.im.imbean.IMHttpCommonBean;
import com.android.im.imeventbus.IMMessageDeteAlllEvevt;
import com.android.im.imeventbus.IMMessageDetelEvevt;
import com.android.im.imeventbus.IMMessageNoticelPersonEvevt;
import com.android.im.imnet.IMHttpResultObserver;
import com.android.im.imnet.http.IMHttpsService;
import com.android.im.imutils.IMImageLoadUtil;
import com.android.im.imutils.IMStatusBarUtil;
import com.android.im.imutils.IMStopClickFast;
import com.android.nettylibrary.IMSConfig;
import com.android.nettylibrary.greendao.entity.IMConversationBean;
import com.android.nettylibrary.greendao.entity.IMPersonBean;
import com.android.nettylibrary.greendao.greenutils.DaoUtils;
import com.android.nettylibrary.utils.IMPreferenceUtil;
import com.blankj.utilcode.util.SizeUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.gson.Gson;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;
import okhttp3.RequestBody;

import static com.android.im.imnet.IMBaseConstant.JSON;


public class IMPersonalInforActivity extends IMBaseActivity implements View.OnClickListener {
    private ImageView mIvFinish;
    private TextView mTvTitle;
    private TextView mTvContent;
    private ImageView mIvHead;
    private TextView mTvName;
    private TextView mTvId;
    private ImageView mIvZhid;
    private ImageView mIvNotice;
    private ImageView mRlClear;
    private RelativeLayout mRlhositoy;
    private LinearLayout mRlSigne;
    public IMConversationBean customer;
    private Boolean isNoticed;
    private Boolean isZhiding;
    private Boolean isTouShu;
    private Handler handler=new Handler();
    private ImageView mIvFobit;
    private RelativeLayout mRltousu;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_im_costum_infor);
        initView();
        initData();
    }
    private void initView() {
        IMStatusBarUtil.setTranslucent(this,0);
        IMStatusBarUtil.setLightMode(this);
        mIvFinish = findViewById(R.id.tv_top_finish);
        mTvTitle = findViewById(R.id.tv_top_title);
        mIvFinish.setVisibility(View.VISIBLE);
        mIvHead = findViewById(R.id.im_iv_header);
        mTvName = findViewById(R.id.im_tv_name);
        mIvZhid = findViewById(R.id.im_iv_zd);
        mIvNotice = findViewById(R.id.im_iv_notice);
        mRlClear = findViewById(R.id.im_rl_clear);
        mRlhositoy = findViewById(R.id.im_rl_hositoy);
        mRlSigne = findViewById(R.id.im_ll_signer);
        mTvId= findViewById(R.id.im_tv_id);
        mIvFobit = findViewById(R.id.im_iv_fobit);
        mRltousu= findViewById(R.id.im_rl_ts);
        mTvContent= findViewById(R.id.im_tv_content);

        mRlClear.setOnClickListener(this);
        mRlhositoy.setOnClickListener(this);
        mRlSigne.setOnClickListener(this);
        mIvZhid.setOnClickListener(this);
        mIvNotice.setOnClickListener(this);
        mIvFinish.setOnClickListener(this);
        mIvHead.setOnClickListener(this);
        mIvFobit.setOnClickListener(this);
        mRltousu.setOnClickListener(this);
    }

    private void initData() {
        customer = (IMConversationBean)getIntent().getSerializableExtra("user");
        IMPersonBean bean = DaoUtils.getInstance().queryMessageBean(customer.getConversationId());
        if(bean!=null){
            mTvName.setText(bean.getNickName());
//            IMImageLoadUtil.ImageLoadBlueCircle(IMPersonalInforActivity.this,bean.getAvatar(),mIvHead);
            Glide.with(getApplicationContext()).load(bean.getAvatar())
                    .apply(RequestOptions.bitmapTransform(new RoundedCornersTransformation(SizeUtils.dp2px(5), 0)))
                    .into(mIvHead);
        }else {
            mTvName.setText(customer.getConversationName());
//            IMImageLoadUtil.ImageLoadBlueCircle(IMPersonalInforActivity.this,customer.getConversationavatar(),mIvHead);
            Glide.with(getApplicationContext()).load(customer.getConversationavatar())
                    .apply(RequestOptions.bitmapTransform(new RoundedCornersTransformation(SizeUtils.dp2px(5), 0)))
                    .into(mIvHead);
        }
        mTvTitle.setText(getResources().getString(R.string.im_chat_detail));
        if(customer!=null){
            isNoticed = IMPreferenceUtil.getPreference_Boolean(customer.getConversationId() + IMSConfig.IM_CONVERSATION_NO_NOTICE, false);
            isZhiding = IMPreferenceUtil.getPreference_Boolean(customer.getConversationId() + IMSConfig.IM_CONVERSATION_ZHI_DING, false);
            isTouShu  = IMPreferenceUtil.getPreference_Boolean(customer.getConversationId() + IMSConfig.IM_CONVERSATION_TOUSU, false);
            if(isNoticed){
                mIvNotice.setImageResource(R.mipmap.im_chat_detail_on);
            }else{
                mIvNotice.setImageResource(R.mipmap.im_chat_detail_off);
            }
            if(isZhiding){
                mIvZhid.setImageResource(R.mipmap.im_chat_detail_on);
            }else{
                mIvZhid.setImageResource(R.mipmap.im_chat_detail_off);
            }
            if(isTouShu){
                mTvContent.setText("关闭屏蔽后，您将可以与该用户互相发送消息");
                mIvFobit.setImageResource(R.mipmap.im_chat_detail_on);
            }else{
                mTvContent.setText("开启屏蔽后，您将无法与该用户互相发送消息");
                mIvFobit.setImageResource(R.mipmap.im_chat_detail_off);
            }
        }
    }
    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.tv_top_finish){
            finish();
        }else if(v.getId()==R.id.im_rl_hositoy){
            gotoIMSerachActivity();
        }else if(v.getId()==R.id.im_rl_clear){
            showCommonDialog("是否确认清空聊天记录？", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dissCommonDialog();
                    cleaSucess();
                }
            });
        }else if(v.getId()==R.id.im_iv_notice){
            handleNoticeData();
        }else if(v.getId()==R.id.im_iv_zd){
            handleSetTopData();
        }else if(v.getId()==R.id.im_iv_fobit){
            handleShieldData();
        }else if(v.getId()==R.id.im_rl_ts){
            if(IMStopClickFast.isFastClick()){
                Intent intent=new Intent(IMPersonalInforActivity.this, IMComplaintActivity.class);
                intent.putExtra("personId",customer.getConversationId());
                startActivity(intent);
            }
        }else if(v.getId()==R.id.im_iv_header){
            Intent intent=new Intent(IMPersonalInforActivity.this, IMContactDetailActivity.class);
            IMPersonBean data=new IMPersonBean();
            data.setMemberId(customer.getConversationId());
            data.setNickName(customer.getConversationName());
            data.setAvatar(customer.getConversationavatar());
            intent.putExtra("bean", data);
            intent.putExtra("isreturn",true);
            startActivity(intent);
            finish();
        }
    }

    public void cleaSucess() {
        //更新会话数据库发送状态
        List<IMConversationBean> persondatas = daoUtils.queryAllConversationData();
        for (int i = 0; i < persondatas.size(); i++) {
            if(persondatas.get(i).getConversationId().equals(customer.getConversationId())){
                persondatas.get(i).setLastMessageSendstate(IMSConfig.IM_SEND_STATE_SUCCESS);
                persondatas.get(i).setLastMessageTime(System.currentTimeMillis());
                persondatas.get(i).setLastMessageContent("");
                daoUtils.updateConversationData(persondatas.get(i));
                //更新消息数据库发送状态
                daoUtils.deleteConversationDetailAccordField(persondatas.get(i).getConversationId());
                EventBus.getDefault().post(new IMMessageDetelEvevt());
                EventBus.getDefault().post(new IMMessageDeteAlllEvevt(customer.getConversationId()));
            }
        }
        Toast.makeText(this, "清空聊天记录成功", Toast.LENGTH_SHORT).show();
    }
    /**
     * 点击查询历史按钮
     */
    private void gotoIMSerachActivity() {
        if(IMStopClickFast.isFastClick()){
            Intent intent=new Intent(this, IMInformationSerachActivity.class);
            intent.putExtra("customer",customer);
            startActivity(intent);
        }
    }
    /**
     * 点击通知按钮
     */
    private void handleNoticeData() {
        if(isNoticed){
            mIvNotice.setImageResource(R.mipmap.im_chat_detail_off);
            IMPreferenceUtil.setPreference_Boolean(customer.getConversationId() + IMSConfig.IM_CONVERSATION_NO_NOTICE,false);
            EventBus.getDefault().post(new IMMessageNoticelPersonEvevt());
            isNoticed=!isNoticed;
        }else {
            mIvNotice.setImageResource(R.mipmap.im_chat_detail_on);
            IMPreferenceUtil.setPreference_Boolean(customer.getConversationId() + IMSConfig.IM_CONVERSATION_NO_NOTICE,true);
            EventBus.getDefault().post(new IMMessageNoticelPersonEvevt());
            isNoticed=!isNoticed;
        }
    }

    /**
     * 点击置顶按钮
     */
    private void handleSetTopData() {
        IMPersonBean imPersonBean = daoUtils.queryMessageBean(customer.getMemberId());
        if(isZhiding){
            mIvZhid.setImageResource(R.mipmap.im_chat_detail_off);
            IMPreferenceUtil.setPreference_Boolean(customer.getConversationId() + IMSConfig.IM_CONVERSATION_ZHI_DING,false);
            isZhiding=!isZhiding;
            imPersonBean.setIsSetTop(false);
            imPersonBean.setSetTopTime(System.currentTimeMillis());
            //为了重启app删除本地数据失效
            IMPreferenceUtil.setPreference_Boolean(IMSConfig.IM_IS_SET_TOP+customer.getConversationId(),false);
            IMPreferenceUtil.setPreference_String(IMSConfig.IM_SET_TOP_TIME+customer.getConversationId(),System.currentTimeMillis()+"");
        }else {
            mIvZhid.setImageResource(R.mipmap.im_chat_detail_on);
            IMPreferenceUtil.setPreference_Boolean(customer.getConversationId() + IMSConfig.IM_CONVERSATION_ZHI_DING,true);
            isZhiding=!isZhiding;
            imPersonBean.setIsSetTop(true);
            imPersonBean.setSetTopTime(System.currentTimeMillis());
            IMPreferenceUtil.setPreference_Boolean(IMSConfig.IM_IS_SET_TOP+customer.getConversationId(),true);
            IMPreferenceUtil.setPreference_String(IMSConfig.IM_SET_TOP_TIME+customer.getConversationId(),System.currentTimeMillis()+"");
        }
        daoUtils.updateMessageData(imPersonBean);
        EventBus.getDefault().post(new IMMessageNoticelPersonEvevt());
    }

    /**
     * 点击屏蔽按钮
     */
    private void handleShieldData() {
        boolean isforbit = IMPreferenceUtil.getPreference_Boolean(customer.getConversationId() + IMSConfig.IM_CONVERSATION_TOUSU, false);
        IMPersonBean imPersonBean = daoUtils.queryMessageBean(customer.getConversationId());
        if(imPersonBean==null) {
            return;
        }
        if(isforbit){
            httpShieldData("N");
        }else {
            showCommonDialog("您确认要屏蔽此用户吗？", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dissCommonDialog();
                    httpShieldData("Y");
                }
            });
        }
    }
    /**
     * 点击屏蔽按钮
     */
    private void httpShieldData(String forbit) {
        showLoadingDialog();
        IMHttpCommonBean bean = new IMHttpCommonBean();
        bean.setCustomerId(customer.getConversationId());
        bean.setIsBlock(forbit);
        String json = new Gson().toJson(bean);
        RequestBody body = RequestBody.create(JSON, json);
        IMHttpsService.getShieldPerson(body, new IMHttpResultObserver<String>() {
            @Override
            public void onSuccess(String s, String message) {
                dismissLoadingDialog();
                if(forbit.equals("Y")){
                    mIvFobit.setImageResource(R.mipmap.im_chat_detail_on);
                    mTvContent.setText("关闭屏蔽后，您将可以与该用户互相发送消息");
                    IMPreferenceUtil.setPreference_Boolean(customer.getConversationId()+IMSConfig.IM_CONVERSATION_TOUSU,true);
                    EventBus.getDefault().post(new IMMessageNoticelPersonEvevt());
                }else {
                    mIvFobit.setImageResource(R.mipmap.im_chat_detail_off);
                    IMPreferenceUtil.setPreference_Boolean(customer.getConversationId()+IMSConfig.IM_CONVERSATION_TOUSU,false);
                    mTvContent.setText("开启屏蔽后，您将无法与该用户互相发送消息");
                    EventBus.getDefault().post(new IMMessageNoticelPersonEvevt());
                }

            }
            @Override
            public void _onError(Throwable e) {
                dismissLoadingDialog();
                Toast.makeText(IMPersonalInforActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void _onErrorLocal(Throwable e, String message, String code) {
                dismissLoadingDialog();
                Toast.makeText(IMPersonalInforActivity.this, message, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
