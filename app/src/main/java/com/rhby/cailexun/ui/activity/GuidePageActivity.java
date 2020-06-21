package com.rhby.cailexun.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

import com.android.im.imutils.IMStatusBarUtil;
import com.rhby.cailexun.R;
import com.rhby.cailexun.ui.base.BaseActivity;

import butterknife.BindView;
import cn.bingoogolapple.bgabanner.BGABanner;
import cn.bingoogolapple.bgabanner.BGALocalImageSize;

/**
 * Created by Administrator on 2019/12/7.
 * Describe:引导页
 */
public class GuidePageActivity extends BaseActivity {
    @BindView(R.id.banner_guide_content)
    BGABanner mContentBanner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide_page);
        IMStatusBarUtil.setTranslucentForImageView(this,0,null);
        initView();
    }

    private void initView() {
        // Bitmap 的宽高在 maxWidth maxHeight 和 minWidth minHeight 之间
        BGALocalImageSize localImageSize = new BGALocalImageSize(720, 1280, 320, 640);
        //设置数据源
        mContentBanner.setData(localImageSize, ImageView.ScaleType.CENTER_CROP,
                R.mipmap.guide1,
                R.mipmap.guide2,
                R.mipmap.guide3);
        mContentBanner.setEnterSkipViewIdAndDelegate(0, 0, new BGABanner.GuideDelegate() {
            @Override
            public void onClickEnterOrSkip() {
                loginActivity();
            }
        });
        mContentBanner.setDelegate(new BGABanner.Delegate<ImageView, String>() {
            @Override
            public void onBannerItemClick(BGABanner banner, ImageView itemView, String model, int position) {
                if(position==2){
                    loginActivity();
                }
            }
        });
    }

    /**
     * 判断是否进入登录界面
     */
    private void loginActivity() {
        Intent intent=new Intent(GuidePageActivity.this, LoginNewActivity.class);
        startActivity(intent);
        finish();
    }
}
