package com.rhby.cailexun.ui.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.im.imadapter.baseadapter.IMBaseRecycleViewAdapter_T;
import com.android.im.imbean.IMChoosePicBean;
import com.android.im.imutils.IMChooseMineBg;
import com.android.im.imutils.IMStatusBarUtil;
import com.rhby.cailexun.R;
import com.rhby.cailexun.adapter.ChooseChatBgAdapter;
import com.rhby.cailexun.bean.ChoosePicBean;
import com.android.im.imbean.MyBgBeanData;
import com.rhby.cailexun.event.SetBgSuccessedEvent;
import com.android.im.imeventbus.SetMineBgSuccessedEvent;
import com.rhby.cailexun.net.HttpResultObserver;
import com.rhby.cailexun.net.http.HttpsService;
import com.rhby.cailexun.ui.base.BaseActivity;
import com.android.nettylibrary.IMSConfig;
import com.android.nettylibrary.greendao.entity.IMChatBgBean;
import com.android.nettylibrary.greendao.greenutils.DaoUtils;
import com.android.nettylibrary.utils.IMPreferenceUtil;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Wolf on 2019/11/22.
 * Describe:通用设置
 */
public class ChooseChatBgActicity extends BaseActivity implements IMBaseRecycleViewAdapter_T.OnItemClickListner {
    @BindView(R.id.tv_top_title)
    TextView tvTopTitle;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    private ChooseChatBgAdapter adapter;
    private List<IMChoosePicBean> datas;
    private ChoosePicBean choosePicBean;
    private boolean isminebg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_bg);
        EventBus.getDefault().register(this);
        IMStatusBarUtil.setTranslucent(this,0);
        IMStatusBarUtil.setLightMode(this);
        initView();
    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    private void initView() {
        isminebg = getIntent().getBooleanExtra("isminebg", false);
        initSmartLayout();
        initRecycleView();
        if(isminebg){
            tvTopTitle.setText("我的背景");
            initDatas();
        }else {
            tvTopTitle.setText("聊天背景");
//            getChatBg();
            initDatas();
        }
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
        GridLayoutManager layoutManager =  new GridLayoutManager(this,3);
        layoutManager.setOrientation(LinearLayoutManager .VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
    }

    /**
     * 我的背景
     */
    private void initDatas() {
        showLoadingDialog();
        HttpsService.getMyBgJson(new HttpResultObserver<MyBgBeanData>() {
            @Override
            public void onSuccess(MyBgBeanData myBgBeanData, String message) {
                dismissLoadingDialog();
                if(myBgBeanData==null){
                    return;
                }
                handData(myBgBeanData);
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




    private void handData(MyBgBeanData myBgBeanData) {
        datas=getPicDatas(myBgBeanData);
        adapter=new ChooseChatBgAdapter(this,R.layout.layout_item_chat_bg,datas);
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListner(this);
    }
    /**
     * 我的背景数据源
     */
    private List<IMChoosePicBean> getPicDatas(MyBgBeanData myBgBeanData) {
        datas = new ArrayList<>();
        String bgurl =null;
        boolean   hastrue=false;
        if(isminebg){
            datas = IMChooseMineBg.getPicDatas(true,myBgBeanData);
            bgurl = IMPreferenceUtil.getPreference_String(IMSConfig.CHOOSE_MY_BG, "");
        }else {
            datas = IMChooseMineBg.getPicDatas(false,myBgBeanData);
            bgurl = IMPreferenceUtil.getPreference_String(IMSConfig.CHOOSE_CHAT_BG, "");
        }
        if(!TextUtils.isEmpty(bgurl)){
            for (int i = 0; i < datas.size(); i++) {
                if(bgurl.equals(datas.get(i).getUrl())){
                    datas.get(i).setChoosed(true);
                    hastrue=true;
                }else {
                    datas.get(i).setChoosed(false);
                }
            }
        }else {
            for (int i = 0; i < datas.size(); i++) {
                datas.get(i).setChoosed(false);
            }
        }
        if(!hastrue){
            datas.get(0).setChoosed(true);
        }
        return datas;
    }

    /**
     * 聊天背景数据源
     */
    public  void getChatBg() {
        List<IMChatBgBean> imChatBgBeans = DaoUtils.getInstance().queryChatBgBean();
        if(imChatBgBeans==null){
            return ;
        }
        List<String>urls=new ArrayList<>();
        for (int i = 0; i < imChatBgBeans.size(); i++) {
            if(imChatBgBeans.get(i).getIschoose()){
                urls.add(imChatBgBeans.get(i).getUrl());
            }
        }
        datas = IMChooseMineBg.getPicDatas(false,urls);
        boolean   hastrue=false;
        String bgurl = IMPreferenceUtil.getPreference_String(IMSConfig.CHOOSE_CHAT_BG, "");
        if(!TextUtils.isEmpty(bgurl)){
            for (int i = 0; i < datas.size(); i++) {
                if(bgurl.equals(datas.get(i).getUrl())){
                    datas.get(i).setChoosed(true);
                    hastrue=true;
                }else {
                    datas.get(i).setChoosed(false);
                }
            }
        }else {
            for (int i = 0; i < datas.size(); i++) {
                datas.get(i).setChoosed(false);
            }
        }
        if(!hastrue){
            datas.get(0).setChoosed(true);
        }

        adapter=new ChooseChatBgAdapter(this,R.layout.layout_item_chat_bg,datas);
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListner(this);
    }




    @OnClick({R.id.iv_top_finish})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_top_finish:
                finish();
                break;

        }
    }

    @Override
    public void onItemClickListner(View v, int position, Object t) {
        Intent intent=new Intent(this,ChatBgEnsuredActicity.class);
        intent.putExtra("bean", datas.get(position).getUrl());
        intent.putExtra("isminebg",isminebg);
        intent.putExtra("position",position);
        startActivity(intent);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(SetBgSuccessedEvent event) {
            finish();
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(SetMineBgSuccessedEvent event) {
        finish();
    }
}
