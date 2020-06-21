package com.android.im.imui.activity;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.im.R;
import com.android.im.imadapter.IMNewFriedAdapter;
import com.android.im.imadapter.baseadapter.IMBaseRecycleViewAdapter_T;
import com.android.im.imbean.IMFriendsBean;
import com.android.im.imeventbus.IMAgreeAddFriendSucessEvent;
import com.android.im.imeventbus.IMAgreeAddFriendSucessedEvent;
import com.android.im.imeventbus.IMNewFriendUnReadNumber;
import com.android.im.imnet.IMHttpResultObserver;
import com.android.im.imnet.http.IMHttpsService;
import com.android.im.imutils.IMStatusBarUtil;
import com.android.nettylibrary.IMSConfig;
import com.android.nettylibrary.http.IMPersonBeans;
import com.android.nettylibrary.utils.IMPreferenceUtil;
import com.blankj.utilcode.util.KeyboardUtils;
import com.google.android.exoplayer2.util.Log;
import com.google.gson.Gson;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import okhttp3.RequestBody;

import static com.android.im.imnet.IMBaseConstant.JSON;


public class IMNewFriendActivity extends IMBaseActivity implements View.OnClickListener, DialogInterface.OnClickListener {
    private ImageView mIvFinish;
    private TextView mIvRight;
    private TextView mTvTitle;
    private LinearLayout llEmpty;
    private SmartRefreshLayout refreshLayout;
    private RecyclerView recyclerView;
    private IMNewFriedAdapter adapter;
    private List<IMFriendsBean> bean;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_im_new_friend);
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
        mIvRight = findViewById(R.id.tv_top_right);
        refreshLayout = findViewById(R.id.refreshLayout);
        recyclerView = findViewById(R.id.recyclerView);
        llEmpty = findViewById(R.id.ll_empty);
        mIvRight.setVisibility(View.VISIBLE);
        mIvRight.setText("添加好友");
        mTvTitle.setText("新的朋友");
        mIvFinish.setOnClickListener(this);
        mIvRight.setOnClickListener(this);
    }

    private void initData() {
        initSmartLayout();
        initRecycleView();
        showLoadingDialog();
        getHttpData();
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
        }else  if(v.getId()==R.id.tv_top_right){
            Intent intent=new Intent(IMNewFriendActivity.this,IMAddFriendActivity.class);
            startActivity(intent);
        }
    }
    @Override
    public void onClick(DialogInterface dialog, int which) {
    }

    /**
     * 获取网络数据
     */
    private void getHttpData() {
        IMHttpsService.getFriendApplyJson(new IMHttpResultObserver<List<IMFriendsBean>>() {
            @Override
            public void onSuccess(List<IMFriendsBean> datas, String message) {
                dismissLoadingDialog();
                if(datas==null ||datas.size()==0){
                    llEmpty.setVisibility(View.VISIBLE);
                    return;
                }
                handleDatas(datas);
            }
            @Override
            public void _onError(Throwable e) {
                dismissLoadingDialog();
                Toast.makeText(IMNewFriendActivity.this,e.getMessage(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void _onErrorLocal(Throwable e, String message, String code) {
                dismissLoadingDialog();
                Toast.makeText(IMNewFriendActivity.this,message, Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * 获取数据
     */
    private void handleDatas(List<IMFriendsBean> datas) {
        //检查未读数用的，进来一次保存一次
        int number = 0;
        for (int i = 0; i < datas.size(); i++) {
                number=number+1;
        }
        IMPreferenceUtil.setPreference_String(IMSConfig.SAVE_NEW_FRIEND, number+"");
        EventBus.getDefault().post(new IMNewFriendUnReadNumber(datas));

        if(bean==null){
            bean = datas;
            adapter = new IMNewFriedAdapter(this,R.layout.item_im_new_friend_two,bean);
            recyclerView.setAdapter(adapter);
            adapter.setOnItemClickListner(new IMBaseRecycleViewAdapter_T.OnItemClickListner() {
                @Override
                public void onItemClickListner(View v, int position, Object t) {
                    Intent intent=new Intent(IMNewFriendActivity.this, IMNewFriendDetailInforActivity.class);
                    intent.putExtra("bean",bean.get(position));
                    startActivity(intent);
                }
            });
            adapter.setOnaddListener(new IMNewFriedAdapter.onAddListener() {
                @Override
                public void setOnAddListener(boolean isadd,int position) {
                    if(isadd){
                        agreeAddFriends(position);
                    }else {
                        refuseAddFriends(position);
                    }

                }
            });
        }else {
            bean.clear();
            bean.addAll(datas);
            adapter.notifyDataSetChanged();
        }
    }

    /**
     * 拒绝添加好友
     */
    private void refuseAddFriends(int position) {
        showLoadingDialog();
        RequestBody bodyData = getBodyData(bean.get(position).getApplyId());
        IMHttpsService.getRefuseFriendApplyJson(bodyData, new IMHttpResultObserver<String>() {
            @Override
            public void onSuccess(String s, String message) {
                getHttpData();
            }

            @Override
            public void _onError(Throwable e) {
                dismissLoadingDialog();
                Toast.makeText(IMNewFriendActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void _onErrorLocal(Throwable e, String message, String code) {
                dismissLoadingDialog();
                Toast.makeText(IMNewFriendActivity.this,message, Toast.LENGTH_SHORT).show();
            }
        });
    }
    /**
     * 同意添加好友
     */
    private void agreeAddFriends(int position) {
        showLoadingDialog();
        RequestBody bodyData = getBodyData(bean.get(position).getApplyId());
        IMHttpsService.getAgreeFriendApplyJson(bodyData, new IMHttpResultObserver<String>() {
            @Override
            public void onSuccess(String s, String message) {
                getHttpData();
                EventBus.getDefault().post(new IMAgreeAddFriendSucessEvent());
            }

            @Override
            public void _onError(Throwable e) {
                dismissLoadingDialog();
                Toast.makeText(IMNewFriendActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void _onErrorLocal(Throwable e, String message, String code) {
                dismissLoadingDialog();
                Toast.makeText(IMNewFriendActivity.this,message, Toast.LENGTH_SHORT).show();
            }
        });
    }

    public RequestBody  getBodyData(String id){
        IMPersonBeans bean = new IMPersonBeans();
        bean.setApplyId(id);
        String json = new Gson().toJson(bean);
        RequestBody body = RequestBody.create(JSON, json);
        Log.e("--json---",json);
        return  body;
    }

    /**
     * 如果是点item进入详情统一添加的好友  这里刷新界面
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(final IMAgreeAddFriendSucessedEvent event) {
        showLoadingDialog();
        getHttpData();
        EventBus.getDefault().post(new IMAgreeAddFriendSucessEvent());
    }

}
