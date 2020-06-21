package com.android.im.imui.activity;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.im.R;
import com.android.im.imadapter.IMAddFriedAdapter;
import com.android.im.imadapter.baseadapter.IMBaseRecycleViewAdapter_T;
import com.android.im.imbean.IMBetGetBean;
import com.android.im.imeventbus.IMAddFriendSucessEvent;
import com.android.im.imeventbus.IMDeleteFriendSucessEvent;
import com.android.im.imnet.IMHttpResultObserver;
import com.android.im.imnet.http.IMHttpsService;
import com.android.im.imutils.IMStatusBarUtil;
import com.android.nettylibrary.greendao.entity.IMPersonBean;
import com.blankj.utilcode.util.KeyboardUtils;
import com.google.gson.Gson;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.RequestBody;


public class IMAddFriendActivity extends IMBaseActivity implements View.OnClickListener, DialogInterface.OnClickListener, TextView.OnEditorActionListener {
    private ImageView mIvFinish;
    private ImageView mIvRight;
    private TextView mTvTitle;
    private SmartRefreshLayout refreshLayout;
    private RecyclerView recyclerView;
    private IMAddFriedAdapter adapter;
    private EditText mTvSerch;
    private ImageView  mIvSerch;
    private LinearLayout  llSearch;
    private LinearLayout  Nollcontent;
    private TextView  NoTvcontent;
    private List<IMPersonBean> datas=new ArrayList<>();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_im_add_friend);
        EventBus.getDefault().register(this);
        initView();
        initData();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        KeyboardUtils.hideSoftInput(this);
    }

    private void initView() {
        IMStatusBarUtil.setTranslucent(this,0);
        IMStatusBarUtil.setLightMode(this);
        mIvFinish = findViewById(R.id.tv_top_finish);
        mTvTitle = findViewById(R.id.tv_top_title);
        mIvRight = findViewById(R.id.iv_top_right);
        refreshLayout = findViewById(R.id.refreshLayout);
        recyclerView = findViewById(R.id.recyclerView);

        Nollcontent = findViewById(R.id.im_ll_no_content);
        NoTvcontent = findViewById(R.id.im_tv_no_content);
        mTvSerch = findViewById(R.id.im_tv_serch);
        mIvSerch = findViewById(R.id.im_iv_search);
        llSearch = findViewById(R.id.ll_search);
        mIvRight.setVisibility(View.VISIBLE);
        mIvRight.setImageResource(R.mipmap.im_add_friend_scan);
        mTvTitle.setText("添加好友");
        mIvFinish.setOnClickListener(this);
        mIvRight.setOnClickListener(this);
        mIvSerch.setOnClickListener(this);
        mTvSerch.setOnEditorActionListener(this);
        llSearch.setOnClickListener(this);

        KeyboardUtils.registerSoftInputChangedListener(this, height -> {
            if(height==0){
                int newHeight = LinearLayout.LayoutParams.WRAP_CONTENT;
                LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) mTvSerch.getLayoutParams();
                lp.width = newHeight;
                mTvSerch.setLayoutParams(lp);
                mTvSerch.clearFocus();
            }else {
                int newHeight = LinearLayout.LayoutParams.MATCH_PARENT;
                LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) mTvSerch.getLayoutParams();
                lp.width = newHeight;
                mTvSerch.setLayoutParams(lp);
            }
        });
        llSearch.setOnClickListener(v -> KeyboardUtils.showSoftInput(mTvSerch));
    }

    private void initData() {
        initSmartLayout();
        initRecycleView();
    }
    private void initSmartLayout() {
        refreshLayout.setEnableRefresh(false);//是否启用下拉刷新功能
        refreshLayout.setEnableLoadMore(false);//是否启用上拉加载功能
        refreshLayout.setEnablePureScrollMode(true);//是否启用纯滚动模式
        refreshLayout.setEnableOverScrollBounce(true);//是否启用越界回弹
        refreshLayout.setEnableOverScrollDrag(true);//是否启用越界拖动（仿苹果效果）1.0.4
    }
    @SuppressLint("WrongConstant")
    private void initRecycleView() {
        LinearLayoutManager layoutManager =  new  LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager .VERTICAL);
        recyclerView.setLayoutManager(layoutManager);

    }



    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.tv_top_finish){
            finish();
        }else  if(v.getId()==R.id.iv_top_right){
            Intent intent2 = new Intent(this, IMQRCActivity.class);
            startActivity(intent2);
        }else  if(v.getId()==R.id.im_iv_search){
            getHttpData();
        }
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
    }

    /**
     * 添加好友
     */
    private void getHttpData() {
        if(TextUtils.isEmpty(mTvSerch.getText().toString())){
            Toast.makeText(this, "请输入查询内容", Toast.LENGTH_SHORT).show();
            return;
        }
        getSearchFriend();
    }



    private void handleDataView(List<IMPersonBean> s) {
        datas = s;
        adapter = new IMAddFriedAdapter(this,R.layout.item_im_add_friend_two,datas);
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListner(new IMBaseRecycleViewAdapter_T.OnItemClickListner() {
            @Override
            public void onItemClickListner(View v, int position, Object t) {
                Intent intent=new Intent(IMAddFriendActivity.this,IMNewFriendInforlActivity.class);
                intent.putExtra("bean",s.get(position));
                startActivity(intent);
                finish();

            }
        });
    }
    /**
     * 添加好友删除好友需要刷新好友界面
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(final IMAddFriendSucessEvent event) {
        finish();
    }
    /**
     * 添加好友删除好友需要刷新好友界面
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(final IMDeleteFriendSucessEvent event) {
        finish();
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if(actionId==EditorInfo.IME_ACTION_SEARCH){
            if(TextUtils.isEmpty(mTvSerch.getText().toString())){
                Toast.makeText(this, "请输入查询内容", Toast.LENGTH_SHORT).show();
            }else {
                mTvSerch.clearFocus();
                getSearchFriend();
            }
        }
        return false;
    }


    /**
     * 查找好友
     */
    private void getSearchFriend() {
        final IMBetGetBean data = new IMBetGetBean();
        data.setQ(mTvSerch.getText().toString());
        String json = new Gson().toJson(data);
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json);
        showLoadingDialog();
        IMHttpsService.serchNewFriendJson(body, new IMHttpResultObserver<List<IMPersonBean>>() {
            @Override
            public void onSuccess(List<IMPersonBean> s, String message) {
                dismissLoadingDialog();
                if(s==null || s.size()==0){
                    refreshLayout.setVisibility(View.GONE);
                    Nollcontent.setVisibility(View.VISIBLE);
                    NoTvcontent.setText("没有搜索到相关的好友");
                    return;
                }
                refreshLayout.setVisibility(View.VISIBLE);
                Nollcontent.setVisibility(View.GONE);
                handleDataView( s);
            }

            @Override
            public void _onError(Throwable e) {
                dismissLoadingDialog();
                Toast.makeText(IMAddFriendActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void _onErrorLocal(Throwable e, String message, String code) {
                dismissLoadingDialog();
                Toast.makeText(IMAddFriendActivity.this, message, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
