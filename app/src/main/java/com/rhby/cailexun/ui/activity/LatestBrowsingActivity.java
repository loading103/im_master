package com.rhby.cailexun.ui.activity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.widget.TextView;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.im.imutils.IMStatusBarUtil;
import com.rhby.cailexun.R;
import com.rhby.cailexun.adapter.LatestBrowsingAdapter;
import com.rhby.cailexun.bean.BrowseRecordBean;
import com.rhby.cailexun.net.HttpResultObserver;
import com.rhby.cailexun.net.http.HttpsService;
import com.rhby.cailexun.ui.base.BaseActivity;
import com.blankj.utilcode.util.ToastUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.MediaType;
import okhttp3.RequestBody;

/**
 * Created by Wolf on 2019/11/22.
 * Describe:最近浏览
 */
public class LatestBrowsingActivity extends BaseActivity {
    @BindView(R.id.tv_top_title)
    TextView tvTopTitle;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;
    private LatestBrowsingAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_latest_browsing);
        IMStatusBarUtil.setTranslucent(this, 0);
        IMStatusBarUtil.setLightMode(this);
        initView();
        getBrowseRecord();
    }

    private void getBrowseRecord() {
        showLoadingDialog();
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"),"");
        HttpsService.getBrowseRecord(body, new HttpResultObserver<List<BrowseRecordBean>>() {
            @Override
            public void onSuccess(List<BrowseRecordBean> s, String message) {
                dismissLoadingDialog();
                adapter.setNewData(s);
            }

            @Override
            public void _onError(Throwable e) {
                dismissLoadingDialog();
                ToastUtils.showShort(e.getMessage());
            }

            @Override
            public void _onErrorLocal(Throwable e, String message, String code) {
                dismissLoadingDialog();
                ToastUtils.showShort(message);
            }
        });
    }

    private void initView() {
        tvTopTitle.setText("最新浏览");
        GridLayoutManager layoutManager = new GridLayoutManager(this, 3);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new LatestBrowsingAdapter();
        adapter.setAnimationEnable(true);
        adapter.setAnimationWithDefault(BaseQuickAdapter.AnimationType.AlphaIn);

        adapter.setEmptyView(LayoutInflater.from(this).inflate(R.layout.empty_latest,null));
        recyclerView.setAdapter(adapter);
        adapter.addHeaderView(LayoutInflater.from(this).inflate(R.layout.view_header_latest, null));
        adapter.setOnItemClickListener((adapter, view, position) -> {

        });
    }

    @OnClick(R.id.iv_top_finish)
    public void onViewClicked() {
        finish();
    }
}
