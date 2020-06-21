package com.android.im.imutils;

import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.im.IMSManager;
import com.android.im.R;
import com.android.nettylibrary.IMSConfig;
import com.android.nettylibrary.utils.IMPreferenceUtil;
import com.blankj.utilcode.util.SizeUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.shehuan.niv.NiceImageView;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

public class IMPersonUtils {

    /**
     * 获取im聊天的token
     */
    public static String getAppId(){
        String appid = IMPreferenceUtil.getPreference_String(IMSConfig.SAVE_APPID, null);
        return appid;
    }
    /**
     /**
     * 获取im聊天的token
     */
    public static String getMyToken(){
        String token = IMPreferenceUtil.getPreference_String(IMSConfig.SAVE_IM_TOKEN, null);
        return token;
    }

    /**
     * 获取本地自己的userid
     */
    public static String getMyPhone(){
        String userId = IMPreferenceUtil.getPreference_String(IMSConfig.PHONE, "");
        return userId;
    }
    /**
     * 设置VIP等级
     */
    public static void setVip(String leve, ImageView leveImage){
        switch (leve) {
            case "VIP1":
                leveImage.setImageResource(R.mipmap.im_member_v1);
                break;
            case "VIP2":
                leveImage.setImageResource(R.mipmap.im_member_v2);
                break;
            case "VIP3":
                leveImage.setImageResource(R.mipmap.im_member_v3);
                break;
            case "VIP4":
                leveImage.setImageResource(R.mipmap.im_member_v4);
                break;
            case "VIP5":
                leveImage.setImageResource(R.mipmap.im_member_v5);
                break;
            case "VIP6":
                leveImage.setImageResource(R.mipmap.im_member_v6);
                break;
            case "VIP7":
                leveImage.setImageResource(R.mipmap.im_member_v7);
                break;
            case "VIP8":
                leveImage.setImageResource(R.mipmap.im_member_v8);
                break;
            case "VIP9":
                leveImage.setImageResource(R.mipmap.im_member_v9);
                break;
            case "VIP10":
                leveImage.setImageResource(R.mipmap.im_member_v10);
                break;
            case "VIP11":
                leveImage.setImageResource(R.mipmap.im_member_v11);
                break;
            case "VIP12":
                leveImage.setImageResource(R.mipmap.im_member_v12);
                break;
            case "VIP13":
                leveImage.setImageResource(R.mipmap.im_member_v13);
                break;
            default:
                leveImage.setVisibility(View.GONE);
        }
    }
    /**
     * 设置用户类型
     */
    public static void setUseType(String type, TextView mTvType){
        //会员-类型：1=试玩用户,2=注册用户,3=充值用户,4=VIP用户,5=内部用户
        if(type.equals("1")){
            mTvType.setText("");
        }else  if(type.equals("2")){
            mTvType.setText("");
        }else  if(type.equals("3")){
            mTvType.setText("");
        }else  if(type.equals("4")){
            mTvType.setText("");
        }else  if(type.equals("5")){
            mTvType.setText("");
        }else  if(type.equals("6")){
            mTvType.setText("");
        } else  if(type.equals("7")){
            mTvType.setText("");
        } else  if(type.equals("8")){
            mTvType.setText("");
        }
        mTvType.setBackground(IMSManager.getInstance().getContext().getResources().getDrawable(R.mipmap.im_img_vip));
    }
    /**
     * 设置用户头衔
     */
    public static void setTitile(RelativeLayout mRlTitle, TextView mTvNoTiTle, TextView mTvTitle,String title) {
        if( !TextUtils.isEmpty(title)) {
            mTvNoTiTle.setVisibility(View.GONE);
            mRlTitle.setVisibility(View.VISIBLE);
            mTvTitle.setText(title);
        }else {
//            mRlTitle.setVisibility(View.GONE);
//            mTvNoTiTle.setVisibility(View.VISIBLE);
            mTvNoTiTle.setVisibility(View.GONE);
            mRlTitle.setVisibility(View.VISIBLE);
            mTvTitle.setText("平民");
        }
    }

    /**
     * 设置用户头像(有边框)
     */
    public static void setHeadUrl(NiceImageView mIvHead, String myHeadView) {
        Glide.with(IMSManager.getInstance().getContext()).load(myHeadView)
                .apply(RequestOptions.bitmapTransform(new RoundedCornersTransformation(SizeUtils.dp2px(10), 0)))
                .into(mIvHead);
    }
    /**
     * 设置用户头像(个性签名)
     */
    public static void setSignaTure(TextView mTvSigne, String signature) {
        if(!TextUtils.isEmpty(signature)){
            mTvSigne.setVisibility(View.VISIBLE);
            mTvSigne.setText(signature);
        }else {
            mTvSigne.setVisibility(View.GONE);
        }
    }
    /**
     * 设置通用背景(个性签名)
     */
    public static void setBgUrl(ImageView mIvBg, String bgUrl) {
        if(!TextUtils.isEmpty(bgUrl)){
            IMImageLoadUtil.CommonImageBgLoadCp(IMSManager.getInstance().getContext(),bgUrl,mIvBg);
        }
    }
}
