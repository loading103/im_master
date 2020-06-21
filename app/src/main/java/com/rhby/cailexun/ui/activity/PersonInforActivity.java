package com.rhby.cailexun.ui.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.android.im.IMSManager;
import com.android.im.IMSMsgManager;
import com.android.im.imbean.IMBetGetBean;
import com.android.im.imbean.IMImageViewBean;
import com.android.im.imeventbus.IMImageViewUpdateEvent;
import com.android.im.imeventbus.IMLoginTimeOutEvent;
import com.android.im.imnet.IMHttpResultObserver;
import com.android.im.imnet.http.IMHttpsService;
import com.android.im.imui.activity.IMBaseActivity;
import com.android.im.imutils.IMBase64ImageUtils;
import com.android.im.imutils.IMChooseUtils;
import com.android.im.imutils.IMPersonUtils;
import com.android.im.imutils.IMStatusBarUtil;
import com.rhby.cailexun.R;
import com.rhby.cailexun.dialog.IMPersonalDiglog;
import com.rhby.cailexun.event.SignatureEvent;
import com.android.nettylibrary.IMSConfig;
import com.android.nettylibrary.http.IMUserInforBean;
import com.android.nettylibrary.utils.IMLogUtil;
import com.android.nettylibrary.utils.IMPreferenceUtil;
import com.blankj.utilcode.util.ToastUtils;
import com.google.gson.Gson;
import com.shehuan.niv.NiceImageView;
import com.zhihu.matisse.Matisse;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import okhttp3.MediaType;
import okhttp3.RequestBody;


public class PersonInforActivity extends IMBaseActivity implements View.OnClickListener, DialogInterface.OnClickListener, IMPersonalDiglog.OnFinishListener {
    private ImageView mIvFinish;
    private ImageView mIvLevel;
    private NiceImageView mIvHead;
    private TextView mTvName;
    private TextView mTvSigne;
    private TextView mTvId;
    private TextView mTvTitle;
    private TextView mTvShoot;
    private TextView mTvType;
    private ImageView mIvCamera;
    private ImageView mIvName;
    private RelativeLayout mRlTitle;
    private LinearLayout mRlSign;
    private IMPersonalDiglog mDiglog;
    private IMUserInforBean.UserInforData mData;
    private TextView mTvNoLeve;
    private ImageView mIvBg;
    private static final int REQUEST_CODE_CHOOSE = 10001;
    private TextView tvRight;
    private  RelativeLayout ll_name;
    private TextView mTvNoTiTle;
    public boolean isvisitor=false;//判断是不是游客
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person_infor);
        initView();
        initData();
        getPersonInfo();
    }
    private void initView() {
        IMStatusBarUtil.setTranslucentForImageView(this,0,null);
        mIvFinish = findViewById(R.id.iv_top_finish);
        mIvHead = findViewById(R.id.im_iv_head);
        mTvName = findViewById(R.id.im_tv_name);
        mTvId = findViewById(R.id.im_tv_id);
        mTvSigne = findViewById(R.id.im_tv_signe);
        mIvLevel = findViewById(R.id.im_iv_level);
        mIvCamera=findViewById(R.id.im_iv_cameras);
        mTvNoLeve=findViewById(R.id.im_no_leve);
        mIvBg=findViewById(R.id.im_iv_bg);
        mTvTitle = findViewById(R.id.im_tv_title);
        mTvShoot = findViewById(R.id.im_tv_shoot);
        mTvType = findViewById(R.id.im_tv_type);
        mRlTitle = findViewById(R.id.im_ll_title);
        mRlSign = findViewById(R.id.im_rl_sign);
        mIvName = findViewById(R.id.im_iv_name);
        tvRight = findViewById(R.id.tv_right);
        ll_name = findViewById(R.id.ll_name);
        mTvNoTiTle= findViewById(R.id.tv_no_title);
        mIvFinish.setOnClickListener(this);
        mIvCamera.setOnClickListener(this);
        mRlSign.setOnClickListener(this);
        ll_name .setOnClickListener(this);
    }

    private void initData() {
        String level = IMPreferenceUtil.getPreference_String(IMSConfig.SAVE_LEVEL, "");
        String title = IMPreferenceUtil.getPreference_String(IMSConfig.SAVE_TITLE, "");
        String type = IMPreferenceUtil.getPreference_String(IMSConfig.SAVE_USERTYPE, "");
        mDiglog=new IMPersonalDiglog(this);
        //保存的背景
        IMPersonUtils.setBgUrl(mIvBg,IMSManager.getMyBgUrl());
        //姓名
        mTvName.setText(IMSManager.getMyNickName());
        //个性签名
        tvRight.setText(IMSManager.getMySignature());
        //头像
        IMPersonUtils.setHeadUrl(mIvHead,IMSManager.getMyHeadView());
        //头衔
        IMPersonUtils.setTitile(mRlTitle,mTvNoTiTle,mTvTitle,title);
        //vip等级
        handleVipLeve(mIvLevel,level);
        //用户类型
        HandlerType(type);
    }
    /**
     *
     * 处理vip等级
     */
    private void handleVipLeve(ImageView leveImage, String leve) {
        if(TextUtils.isEmpty(leve)){
            mTvNoLeve.setVisibility(View.VISIBLE);
            return;
        }
        mTvNoLeve.setVisibility(View.GONE);
        IMPersonUtils.setVip(leve,leveImage);
    }
    /**
     * 处理个人类型
     */
    private void HandlerType(String type) {
        if(type.equals("7")){
            isvisitor=true;
        }
        IMPersonUtils.setUseType(type,mTvType);
    }

    /**
     *请求个人请求（刷新本地保存的数据）
     */
    private void getPersonInfo() {
        IMHttpsService.GetSelfInforJson(new IMHttpResultObserver<IMUserInforBean.UserInforData>() {
            @Override
            public void onSuccess(IMUserInforBean.UserInforData data, String message) {
                if(data==null){
                    return;
                }
                mData=data;
                IMSMsgManager.SaveLoginData(data);
            }
            @Override
            public void _onError(Throwable e) {
                IMSManager.getInstance().setLoginInListener(false,e.getMessage());
                ToastUtils.showShort(e.getMessage());
            }

            @Override
            public void _onErrorLocal(Throwable e, String message, String code) {
                ToastUtils.showShort(message);
                if(code.equals("NO_LOGIN")){
                    EventBus.getDefault().post(new IMLoginTimeOutEvent());
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.iv_top_finish){
            finish();
        }else  if(v.getId()==R.id.iv_top_right){
        }else if(v.getId()==R.id.ll_name){
            String name = IMPreferenceUtil.getPreference_String(IMSConfig.SAVE_NAME, "");
            mDiglog.showPersonalDiglog(true,name,this);
        }else if(v.getId()==R.id.im_rl_sign){
            String sign = IMPreferenceUtil.getPreference_String(IMSConfig.SIGNATURE, "");
            mDiglog.showPersonalDiglog(false,sign,this);
        }else if(v.getId()==R.id.im_iv_cameras){
            IMChooseUtils.choosePhotoHeadForResult(this,REQUEST_CODE_CHOOSE,1);
        }
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {

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
                    UpdateHeadUrl(data,null,null,1);
                }

                @Override
                public void _onError(Throwable e) {
                    dismissLoadingDialog();
                    Toast.makeText(PersonInforActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }

                @Override
                public void _onErrorLocal(Throwable e, String message, String code) {
                    dismissLoadingDialog();
                    Toast.makeText(PersonInforActivity.this, message, Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    /**
     * 修改头像或是群名字
     * (type=1图片  =2姓名  =3签名)
     */
    private void UpdateHeadUrl(final IMImageViewBean data,String name,String signature,int  type) {
        showLoadingDialog();
        final IMBetGetBean bean = new IMBetGetBean();
        if(type==1 && data!=null){
            bean.setAvatar(data.getUrl());
        }
        if(type==2){
            bean.setNickName(name);
        }
        if(type==3){
            bean.setSignature(signature);
        }
        String json = new Gson().toJson(bean);
        Log.e("-------修改个人信息",json);
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json);
        IMHttpsService.getUpdataInfor(body, new IMHttpResultObserver<String>() {
            @Override
            public void onSuccess(String ss, String message) {
                dismissLoadingDialog();
                if(type==1){
                    IMPersonUtils.setHeadUrl(mIvHead,data.getUrl());
                    IMPreferenceUtil.setPreference_String(IMSConfig.SAVE_HEADURL,data.getUrl());
                }else if(type==2){
                    IMPreferenceUtil.setPreference_String(IMSConfig.SAVE_NAME,name);
                    mTvName.setText(name);
                    Toast.makeText(PersonInforActivity.this, "修改姓名成功", Toast.LENGTH_SHORT).show();
                }else if(type==3){
                    IMPreferenceUtil.setPreference_String(IMSConfig.SIGNATURE,signature);
                    Toast.makeText(PersonInforActivity.this, "修改签名成功", Toast.LENGTH_SHORT).show();
                    tvRight.setText(signature);
                    mTvSigne.setText(signature);
                    EventBus.getDefault().post(new SignatureEvent(signature));
                }
                EventBus.getDefault().post(new IMImageViewUpdateEvent());
            }

            @Override
            public void _onError(Throwable e) {
                dismissLoadingDialog();
                Toast.makeText(PersonInforActivity.this, "修改失败，请稍后再试", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void _onErrorLocal(Throwable e, String message, String code) {
                dismissLoadingDialog();
                Toast.makeText(PersonInforActivity.this, "修改失败，请稍后再试", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void setOnFinishListener(Boolean isname, String content) {
        if(isname){
            UpdateHeadUrl(null,content, "",2);
        }else {
            UpdateHeadUrl(null,null, content,3);
        }

    }
}
