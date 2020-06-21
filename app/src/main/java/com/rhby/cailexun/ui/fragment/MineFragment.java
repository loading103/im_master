package com.rhby.cailexun.ui.fragment;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.im.IMSManager;
import com.android.im.imeventbus.IMImageViewUpdateEvent;
import com.android.im.imutils.IMImageLoadUtil;
import com.android.im.imutils.IMStopClickFast;
import com.android.im.imeventbus.SetMineBgSuccessedEvent;
import com.rhby.cailexun.R;
import com.rhby.cailexun.event.SignatureEvent;
import com.rhby.cailexun.ui.activity.AccountSettingsActivity;
import com.rhby.cailexun.ui.activity.GeneralSettingsActicity;
import com.rhby.cailexun.ui.activity.LatestBrowsingActivity;
import com.rhby.cailexun.ui.activity.PersonInforActivity;
import com.rhby.cailexun.ui.activity.QrCodeActivity;
import com.rhby.cailexun.ui.base.BaseFragment;
import com.rhby.cailexun.widget.BesselImageView;
import com.rhby.cailexun.widget.ScrollFrameLayout;
import com.android.nettylibrary.IMSConfig;
import com.android.nettylibrary.utils.IMPreferenceUtil;
import com.blankj.utilcode.util.SizeUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.OnClick;
import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

public class MineFragment extends BaseFragment {
    @BindView(R.id.im_iv_head)
    ImageView mIvHead;
    @BindView(R.id.im_tv_name)
    TextView mTvName;
    @BindView(R.id.im_tv_id)
    TextView mTvId;
    @BindView(R.id.im_tv_signe)
    TextView mTvSign;
    @BindView(R.id.im_rl_person)
    RelativeLayout mRlPerson;
    @BindView(R.id.im_rl_scan)
    RelativeLayout mRlScan;
    @BindView(R.id.im_rl_common_set)
    RelativeLayout mRlCommonSet;
    @BindView(R.id.im_rl_account_set)
    RelativeLayout mRlAccountSet;
    @BindView(R.id.im_iv_hua)
    ImageView mIvHua;
    @BindView(R.id.ll_hua)
    RelativeLayout mllHua;
    @BindView(R.id.ll_View)
    RelativeLayout mllView;
    @BindView(R.id.tv_line)
    TextView tvLine;

    private String url;
    private String name,usename,sign;
    private BesselImageView mImageView;
    private ScrollFrameLayout mScrollFrameLayout;
    private boolean ismoveed;

    @Override
    protected void initView(View view) {
        EventBus.getDefault().register(this);
        url = IMPreferenceUtil.getPreference_String(IMSConfig.SAVE_HEADURL,"");
        name = IMPreferenceUtil.getPreference_String(IMSConfig.SAVE_NAME,"");
        usename = IMPreferenceUtil.getPreference_String(IMSConfig.SAVE_USER_NAME,"");
        sign = IMPreferenceUtil.getPreference_String(IMSConfig.SIGNATURE,"");
//        IMImageLoadUtil.ImageLoadBlueCircle(getActivity(), url,mIvHead);
        Glide.with(getContext()).load(url)
                .apply(RequestOptions.bitmapTransform(new RoundedCornersTransformation(SizeUtils.dp2px(10), 0)))
                .into(mIvHead);


        mTvName.setText(name);
        mTvId.setText(IMSManager.getMyPhone());
        mTvSign.setText(TextUtils.isEmpty(sign)?"":sign);
        if(TextUtils.isEmpty(sign)){
            tvLine.setVisibility(View.GONE);
        }

        mImageView = view.findViewById(R.id.image_view);
        //保存的背景
        String myBgUrl = IMSManager.getMyBgUrl();
        IMImageLoadUtil.CommonImageBgLoadCp(getActivity(),myBgUrl,mImageView);

        mScrollFrameLayout = view.findViewById(R.id.scrollFrameLayout);
        mScrollFrameLayout.bindScrollerView(view.findViewById(R.id.content_view));
        mScrollFrameLayout.bindBesselImageView(mImageView);
        mScrollFrameLayout.setUpChangeListener(new ScrollFrameLayout.isChangeListener() {
            @Override
            public void setOnUpChanged(boolean isScrollUp) {
                if(isScrollUp){
                    startDown();
                }else {
                    startUp();
                }
            }
        });
        mIvHua.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scrollMethoer();
            }
        });
        mImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mScrollFrameLayout.startScroll(false);
            }
        });

        startUp();
    }

    private void startUp() {
        AnimationDrawable ad = (AnimationDrawable) getResources().getDrawable(
                R.drawable.anim_list_up);
        if (ad != null ){
            ad.stop();
            mIvHua.clearAnimation();
            mIvHua.setImageDrawable(ad);
            ad.start();
        }
    }

    private void startDown() {
        AnimationDrawable ad = (AnimationDrawable) getResources().getDrawable(
                R.drawable.anim_list_down);
        if (ad != null ){
            ad.stop();
            mIvHua.clearAnimation();
            mIvHua.setImageDrawable(ad);
            ad.start();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_person;
    }

    @OnClick({R.id.im_rl_latest,R.id.im_rl_person, R.id.im_rl_scan, R.id.im_rl_common_set, R.id.im_rl_account_set,R.id.ll_View})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.im_rl_latest:
                if(IMStopClickFast.isFastClick()){
                    startActivity(new Intent(getActivity(), LatestBrowsingActivity.class));
                }
                break;
            case R.id.im_rl_person:
                if(IMStopClickFast.isFastClick()) {
                    Intent intent = new Intent(getActivity(), PersonInforActivity.class);
                    startActivity(intent);
                }
                break;
            case R.id.im_rl_scan:
                if(IMStopClickFast.isFastClick()) {
                    Intent intent1 = new Intent(getActivity(), QrCodeActivity.class);
                    startActivity(intent1);
                }
                break;
            case R.id.im_rl_common_set:
                if(IMStopClickFast.isFastClick()) {
                    startActivity(new Intent(getActivity(), GeneralSettingsActicity.class));
                }
                break;
            case R.id.im_rl_account_set:
                if(IMStopClickFast.isFastClick()) {
                    Intent intent2 = new Intent(getActivity(), AccountSettingsActivity.class);
                    startActivity(intent2);
                }
                break;
            case R.id.ll_View:
                mScrollFrameLayout.startScroll(false);
                break;
        }
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(SetMineBgSuccessedEvent event) {
        String json = IMPreferenceUtil.getPreference_String(IMSConfig.CHOOSE_MY_BG, "");
        IMImageLoadUtil.CommonImageBgLoadCp(getActivity(),json,mImageView);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(IMImageViewUpdateEvent event) {
        String urls = IMPreferenceUtil.getPreference_String(IMSConfig.SAVE_HEADURL, "");
        String names = IMPreferenceUtil.getPreference_String(IMSConfig.SAVE_NAME, "");
//        IMImageLoadUtil.ImageLoadBlueCircle(getActivity(), urls,mIvHead);

        Glide.with(getContext()).load(urls)
                .apply(RequestOptions.bitmapTransform(new RoundedCornersTransformation(SizeUtils.dp2px(10), 0)))
                .into(mIvHead);
        mTvName.setText(names);
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(SignatureEvent event) {
        mTvSign.setText(event.getMsg());
        if(TextUtils.isEmpty(event.getMsg())){
            tvLine.setVisibility(View.GONE);
        }else {
            tvLine.setVisibility(View.VISIBLE);
        }
    }





    public void   scrollMethoer(){
        boolean up = mScrollFrameLayout.isUp();
        if(up){
            mScrollFrameLayout.startScroll(false);
        }else {
            mScrollFrameLayout.startScroll(true);
        }
    }
}
