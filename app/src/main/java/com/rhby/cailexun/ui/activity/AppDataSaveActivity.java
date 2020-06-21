package com.rhby.cailexun.ui.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.im.IMSManager;
import com.android.im.imeventbus.IMMessageDetelGroupEvevt;
import com.android.im.imutils.IMGlideCacheUtil;
import com.android.im.imutils.IMStatusBarUtil;
import com.rhby.cailexun.R;
import com.rhby.cailexun.ui.base.BaseActivity;
import com.android.nettylibrary.greendao.greenutils.DaoUtils;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Wolf on 2019/11/21.
 * Describe:账号设置
 */
public class AppDataSaveActivity extends BaseActivity {
    @BindView(R.id.tv_top_title)
    TextView tvTopTitle;
    @BindView(R.id.re_top_finish)
    RelativeLayout reTopFinish;
    @BindView(R.id.im_rl_clean_data)
    RelativeLayout mRlCleanData;
    @BindView(R.id.im_rl_clean_msg)
    RelativeLayout mRlCleanMsg;
    @BindView(R.id.im_tv_number)
    TextView mTvNumber;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_save);
        IMStatusBarUtil.setTranslucent(this,0);
        IMStatusBarUtil.setLightMode(this);
        initView();
        initData();
    }
    private void initView() {
        tvTopTitle.setText("数据缓存");
    }

    /**
     * 计算缓存大小
     */
    private void initData() {
        String cacheSize = IMGlideCacheUtil.getInstance().getCacheSize(this);
        mTvNumber.setText(cacheSize);

    }
    @OnClick({R.id.iv_top_finish,R.id.im_rl_clean_data,R.id.im_rl_clean_msg})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_top_finish:
                finish();
                break;
            case R.id.im_rl_clean_data:
              showCommonDialog("是否需要清除缓存数据?", new View.OnClickListener() {
                  @Override
                  public void onClick(View v) {
                      dissCommonDialog();
                      IMGlideCacheUtil.getInstance().clearImageAllCache(AppDataSaveActivity.this);
                      String cacheSize = IMGlideCacheUtil.getInstance().getCacheSize(AppDataSaveActivity.this);
                      mTvNumber.setText("0.0Byte");
                  }
              });
                break;
            case R.id.im_rl_clean_msg:
                showCommonDialog("清空所有消息将清空所有的聊天信息和记录，是否确认清空?", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dissCommonDialog();
                        DaoUtils.getInstance().deleteAllConversationDetailData();
                        EventBus.getDefault().post(new IMMessageDetelGroupEvevt());
                        IMSManager.getInstance().getUnreadMessageNumber();
                        Toast.makeText(AppDataSaveActivity.this, "清除成功", Toast.LENGTH_SHORT).show();
                    }
                });
                break;
        }
    }

}
