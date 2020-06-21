package com.rhby.cailexun.ui.activity;

import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.android.im.IMSManager;
import com.android.im.imutils.IMImageLoadUtil;
import com.android.im.imutils.IMStatusBarUtil;
import com.android.im.imbean.IMQrcodeBean;
import com.rhby.cailexun.R;
import com.rhby.cailexun.ui.base.BaseActivity;
import com.android.nettylibrary.IMSConfig;
import com.android.nettylibrary.utils.IMPreferenceUtil;
import com.blankj.utilcode.util.SizeUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.gson.Gson;;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.bingoogolapple.qrcode.zxing.QRCodeEncoder;
import jp.wasabeef.glide.transformations.RoundedCornersTransformation;


public class QrCodeActivity extends BaseActivity implements View.OnClickListener, DialogInterface.OnClickListener {
    @BindView(R.id.iv_top_finish)
    RelativeLayout mllFinish;
    @BindView(R.id.iv_top_more)
    ImageView mIvMore;
    @BindView(R.id.im_iv_head)
    ImageView mIvHead;
    @BindView(R.id.im_tv_name)
    TextView mTvName;
    @BindView(R.id.im_tv_id)
    TextView mTvId;
    @BindView(R.id.im_iv_content)
    ImageView mIvContent;
    @BindView(R.id.im_iv_bg)
    ImageView mIvBg;
    private String name;
    private String url;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrcode_infor);
        ButterKnife.bind(this);
        initView();
        initData();
    }
    private void initView() {
        IMStatusBarUtil.setTranslucentForImageView(this,0,null);
        mllFinish.setOnClickListener(this);
        mIvMore.setOnClickListener(this);
    }

    private void initData() {
        name = IMPreferenceUtil.getPreference_String(IMSConfig.SAVE_NAME, "");
        url = IMPreferenceUtil.getPreference_String(IMSConfig.SAVE_HEADURL, "");
        mTvName.setText(name);

        //保存的背景
//        String json = IMPreferenceUtil.getPreference_String(IMSConfig.CHOOSE_MY_BG, "");
//        if(!TextUtils.isEmpty(json)){
//            ChoosePicBean bean = new Gson().fromJson(json, ChoosePicBean.class);
//            mIvBg.setImageResource(bean.getRes());
//        }
        IMImageLoadUtil.CommonImageBgLoadCp(this,IMSManager.getMyBgUrl(),mIvBg);
//        IMImageLoadUtil.ImageLoadBlueCircle(this,url,mIvHead);
        Glide.with(getApplicationContext()).load(url)
                .apply(RequestOptions.bitmapTransform(new RoundedCornersTransformation(SizeUtils.dp2px(10), 0)))
                .into(mIvHead);

        mTvId.setText("ID:"+IMSManager.getMyUserName());

        IMQrcodeBean bean=new IMQrcodeBean();
        bean.setType("card");
        bean.setCodeId(IMSManager.getMyUseId());
        String toJson = new Gson().toJson(bean);
        Log.e("-toJson-",toJson);
        Bitmap mBitmap =QRCodeEncoder.syncEncodeQRCode(toJson,400);
//        Bitmap mBitmap = CodeUtils.createImage(IMSManager.getMyUseId(), 400, 400, null);
        mIvContent.setImageBitmap(mBitmap);

    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.iv_top_finish){
            finish();
        }else  if(v.getId()==R.id.iv_top_more){
            finish();
        }
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
    }
}
