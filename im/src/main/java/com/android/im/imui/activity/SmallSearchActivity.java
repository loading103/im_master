package com.android.im.imui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.im.DaoManager;
import com.android.im.R;
import com.android.im.R2;
import com.android.im.imadapter.SmallSearchAdapter;
import com.android.im.imadapter.SmallSearchHistoryAdapter;
import com.android.im.imbean.IMBetGetBean;
import com.android.im.imbean.SerachHistoryBean;
import com.android.im.imbean.SerachHistoryBeanDao;
import com.android.im.imbean.SmallProgramBean;
import com.android.im.imnet.IMHttpResultObserver;
import com.android.im.imnet.http.IMHttpsService;
import com.android.im.imutils.IMStatusBarUtil;
import com.blankj.utilcode.util.KeyboardUtils;
import com.blankj.utilcode.util.LogUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemChildClickListener;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.MediaType;
import okhttp3.RequestBody;

/**
 * Created by Administrator on 2019/12/26.
 * Describe:小程序搜索
 */
public class SmallSearchActivity extends IMBaseActivity {
    @BindView(R2.id.et_content)
    EditText etContent;
    @BindView(R2.id.tv_cancer)
    TextView tvCancer;
    @BindView(R2.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R2.id.recyclerView2)
    RecyclerView recyclerView2;
    private SmallSearchAdapter adapter;
    private SmallSearchHistoryAdapter adapter2;
    private List<SerachHistoryBean> list;//搜索历史

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_small_search);
        ButterKnife.bind(this);
        IMStatusBarUtil.setTranslucent(this, 0);
        IMStatusBarUtil.setLightMode(this);
        initView();
        initData();
    }

    private void initData() {
        SerachHistoryBeanDao serachHistoryBeanDao = DaoManager.getInstance().getDaoSession().getSerachHistoryBeanDao();
        list = serachHistoryBeanDao.loadAll();
        Collections.reverse(list);
        adapter2.setNewData(list);
    }

    private void initView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);

        LinearLayoutManager layoutManager2 = new LinearLayoutManager(this);
        layoutManager2.setOrientation(RecyclerView.VERTICAL);
        recyclerView2.setLayoutManager(layoutManager2);

        adapter = new SmallSearchAdapter();
        adapter.setEmptyView(LayoutInflater.from(this).inflate(R.layout.empty_search_null, null));
        adapter.setUseEmpty(false);
        adapter.addChildClickViewIds(R.id.content);
        adapter.setOnItemChildClickListener((adapter, view, position) -> {
            if (view.getId() == R.id.content) {
                KeyboardUtils.hideSoftInput(etContent);
//                etContent.setEnabled(false);
                getProgramDetails(((SmallProgramBean) adapter.getData().get(position)).getProgramId());
            }
        });
        recyclerView.setAdapter(adapter);

        adapter2 = new SmallSearchHistoryAdapter();
        adapter2.addChildClickViewIds(R.id.ll_root, R.id.iv_delete);
        adapter2.setOnItemChildClickListener((adapter, view, position) -> {
            if (view.getId() == R.id.ll_root) {
                etContent.setText(list.get(position).getName());
                etContent.setSelection(list.get(position).getName().length());
                getSearchProgram(list.get(position).getName());
                adapter2.setNewData(null);
            } else if (view.getId() == R.id.iv_delete) {
                SerachHistoryBeanDao serachHistoryBeanDao = DaoManager.getInstance().getDaoSession().getSerachHistoryBeanDao();
                SerachHistoryBean serachHistoryBean = serachHistoryBeanDao.queryBuilder().where(SerachHistoryBeanDao.
                        Properties.Name.eq(adapter2.getData().get(position).getName())).unique();
                serachHistoryBeanDao.delete(serachHistoryBean);
                adapter2.remove(position);
            }
        });
        recyclerView2.setAdapter(adapter2);

        etContent.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (TextUtils.isEmpty(s.toString())) {
                    initData();
                    adapter.setNewData(null);
                    return;
                }
                adapter2.setNewData(null);
                getSearchProgram(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        etContent.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {//搜索按键action
                    if (TextUtils.isEmpty(etContent.getText().toString())) {
                        return true;
                    }
                    //开始搜索
                    adapter2.setNewData(null);
                    getSearchProgram(etContent.getText().toString());

                    SerachHistoryBeanDao serachHistoryBeanDao = DaoManager.getInstance().getDaoSession().getSerachHistoryBeanDao();
                    SerachHistoryBean serachHistoryBean = serachHistoryBeanDao.queryBuilder().where(SerachHistoryBeanDao.
                            Properties.Name.eq(etContent.getText().toString())).unique();
                    if (serachHistoryBean != null) {
                        serachHistoryBeanDao.delete(serachHistoryBean);
                        serachHistoryBeanDao.insert(new SerachHistoryBean(etContent.getText().toString()));
                    } else {
                        List<SerachHistoryBean> list=serachHistoryBeanDao.loadAll();
                        if(list.size()==10){
                            serachHistoryBeanDao.delete(list.get(0));
                        }
                        serachHistoryBeanDao.insert(new SerachHistoryBean(etContent.getText().toString()));
                    }
                    KeyboardUtils.hideSoftInput(etContent);
                    return true;
                }
                return false;
            }
        });

        new Handler().postDelayed(() -> KeyboardUtils.showSoftInput(etContent), 100);
    }

    @OnClick(R2.id.tv_cancer)
    public void onViewClicked() {
        finish();
    }

    /**
     * 搜索小程序
     */
    private void getSearchProgram(String key) {
        adapter.setUseEmpty(true);
//        showLoadingDialog();
        final IMBetGetBean bean = new IMBetGetBean();
        bean.setSearchKeyword(key);
        String json = new Gson().toJson(bean);
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json);
        IMHttpsService.getSearchProgram(body, new IMHttpResultObserver<List<SmallProgramBean>>() {
            @Override
            public void onSuccess(List<SmallProgramBean> data, String message) {
                dismissLoadingDialog();
                adapter.setNewData(data);
            }

            @Override
            public void _onError(Throwable e) {
                dismissLoadingDialog();
            }

            @Override
            public void _onErrorLocal(Throwable e, String message, String code) {
                dismissLoadingDialog();
            }
        });
    }

    /**
     * 小程序详情
     */
    private void getProgramDetails(String programId) {
        showLoadingDialog();
        final IMBetGetBean bean = new IMBetGetBean();
        bean.setProgramId(programId);
        String json = new Gson().toJson(bean);
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json);
        IMHttpsService.getProgramDetails(body, new IMHttpResultObserver<SmallProgramBean>() {
            @Override
            public void onSuccess(SmallProgramBean data, String message) {
                KeyboardUtils.hideSoftInput(etContent);
                dismissLoadingDialog();
//                Intent intent = new Intent(SmallSearchActivity.this, SmallProgramActivity.class);
//                intent.putExtra("data", data);
//                startActivity(intent);
                new SmallProgramActivity().toActivity(SmallSearchActivity.this,data);
            }

            @Override
            public void _onError(Throwable e) {
                dismissLoadingDialog();
            }

            @Override
            public void _onErrorLocal(Throwable e, String message, String code) {
                dismissLoadingDialog();
            }
        });
    }
}
