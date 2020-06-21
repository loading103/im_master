package com.rhby.cailexun.ui.fragment;

import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.im.IMSManager;
import com.android.im.imeventbus.IMImageViewUpdateEvent;
import com.android.im.imeventbus.SetMineBgSuccessedEvent;
import com.android.im.imui.activity.SmallProgramSearchActivity;
import com.android.im.imutils.IMImageLoadUtil;
import com.android.im.imutils.IMPersonUtils;
import com.android.im.imutils.IMStopClickFast;
import com.rhby.cailexun.R;
import com.rhby.cailexun.event.SignatureEvent;
import com.rhby.cailexun.ui.activity.AboutUsActicity;
import com.rhby.cailexun.ui.activity.AccountSettingsActivity;
import com.rhby.cailexun.ui.activity.AppDataSaveActivity;
import com.rhby.cailexun.ui.activity.GeneralSettingsActicity;
import com.rhby.cailexun.ui.activity.PersonInforActivity;
import com.rhby.cailexun.ui.activity.QrCodeActivity;
import com.rhby.cailexun.ui.base.BaseFragment;
import com.rhby.cailexun.widget.NewBesselImageView;
import com.android.nettylibrary.IMSConfig;
import com.android.nettylibrary.utils.IMPreferenceUtil;
import com.shehuan.niv.NiceImageView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.OnClick;
import cc.shinichi.library.ImagePreview;

public class NewMineFragment extends BaseFragment {
    @BindView(R.id.im_iv_headview)
    NewBesselImageView mIvBg;
    @BindView(R.id.im_iv_head)
    NiceImageView mIvHead;
    @BindView(R.id.im_tv_name)
    TextView mTvName;
    @BindView(R.id.im_ll_edit)
    LinearLayout mllEdit;
    @BindView(R.id.im_tv_signe)
    TextView mTvSigne;
    @BindView(R.id.im_tv_title)
    TextView mTvTitle;
    @BindView(R.id.im_tv_no_title)
    TextView mTvNoTitle;
    @BindView(R.id.im_iv_level)
    ImageView mIvLevel;
    @BindView(R.id.im_iv_type)
    ImageView mIvType;
    @BindView(R.id.im_ll_title)
    RelativeLayout mRlTitle;
    @BindView(R.id.im_rl_person)
    LinearLayout mllPerson;
    @BindView(R.id.im_rl_scan)
    LinearLayout mllScan;
    @BindView(R.id.im_rl_common_set)
    LinearLayout mllCommSet;
    @BindView(R.id.im_rl_account_set)
    LinearLayout mllAccountSet;
    @BindView(R.id.im_rl_chucun)
    LinearLayout mllChucun;
    @BindView(R.id.im_rl_about_us)
    LinearLayout mllAboutUs;
    @BindView(R.id.im_rl_xcx)
    LinearLayout mllXcx;
    private String title;
    private String type;
    private String level;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_mine_new;
    }

    @Override
    protected void initView(View view) {
        EventBus.getDefault().register(this);
        title = IMPreferenceUtil.getPreference_String(IMSConfig.SAVE_TITLE, "");
        type = IMPreferenceUtil.getPreference_String(IMSConfig.SAVE_USERTYPE, "");
        level = IMPreferenceUtil.getPreference_String(IMSConfig.SAVE_LEVEL, "");
        //保存的背景
        String myBgUrl = IMSManager.getMyBgUrl();
        IMImageLoadUtil.ImageLoad(getActivity(),myBgUrl,mIvBg);
        mTvName.setText(IMSManager.getMyNickName());
        IMPersonUtils.setSignaTure(mTvSigne,IMSManager.getMySignature());
        IMPersonUtils.setHeadUrl(mIvHead,IMSManager.getMyHeadView());
        IMPersonUtils.setTitile(mRlTitle,mTvNoTitle,mTvTitle,title);
        IMPersonUtils.setVip(level,mIvLevel);

    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @OnClick({R.id.im_rl_person, R.id.im_rl_scan, R.id.im_rl_common_set, R.id.im_rl_account_set,R.id.im_rl_chucun,
            R.id.im_rl_about_us, R.id.im_rl_xcx,R.id.im_iv_head})
    public void onViewClicked(View view) {
        if(IMStopClickFast.isFastClick()) {
            switch (view.getId()) {
                case R.id.im_rl_person:
                    Intent intent = new Intent(getActivity(), PersonInforActivity.class);
                    startActivity(intent);
                    break;
                case R.id.im_rl_scan:
                    Intent intent1 = new Intent(getActivity(), QrCodeActivity.class);
                    startActivity(intent1);
                    break;
                case R.id.im_rl_common_set:
                    startActivity(new Intent(getActivity(), GeneralSettingsActicity.class));
                    break;
                case R.id.im_rl_account_set:
                    Intent intent2 = new Intent(getActivity(), AccountSettingsActivity.class);
                    startActivity(intent2);
                    break;
                case R.id.im_rl_chucun:
                    startActivity(new Intent(getActivity(), AppDataSaveActivity.class));
                    break;
                case R.id.im_rl_about_us:
                    Intent intent3 = new Intent(getActivity(), AboutUsActicity.class);
                    startActivity(intent3);
                    break;

                case R.id.im_rl_xcx:
                    Intent intent4 = new Intent(getActivity(), SmallProgramSearchActivity.class);
                    getActivity().startActivity(intent4);
                    break;
                case R.id.im_iv_head:
//                    Intent intent5= new Intent(getActivity(), IMPhotoViewActivity.class);
//                    intent5.putExtra("url", IMSManager.getMyHeadView());
//                    intent5.putExtra("type", "1");
//                    startActivity(intent5);

                    ImagePreview.getInstance()
                            // 上下文，必须是activity，不需要担心内存泄漏，本框架已经处理好；
                            .setContext(getActivity())

                            .setIndex(0)

                            .setLoadStrategy(ImagePreview.LoadStrategy.Default)
                            // 缩放动画时长，单位ms
                            .setZoomTransitionDuration(1000)

                            .setImage( IMSManager.getMyHeadView())
                            //设置是否显示下载按钮
                            .setShowDownButton(false)
                            // 开启预览
                            .start();

                    break;
            }

        }
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(SetMineBgSuccessedEvent event) {
        String json = IMPreferenceUtil.getPreference_String(IMSConfig.CHOOSE_MY_BG, "");
        IMImageLoadUtil.CommonImageBgLoadCp(getActivity(),json,mIvBg);

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(IMImageViewUpdateEvent event) {
        String urls = IMPreferenceUtil.getPreference_String(IMSConfig.SAVE_HEADURL, "");
        String names = IMPreferenceUtil.getPreference_String(IMSConfig.SAVE_NAME, "");
        IMPersonUtils.setHeadUrl(mIvHead,urls);
        mTvName.setText(names);
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(SignatureEvent event) {
        mTvSigne.setText(event.getMsg());
    }

}
