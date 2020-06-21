package com.android.im.imui.fragment;

import android.view.View;

import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.android.im.IMSManager;
import com.android.im.R;
import com.android.im.imadapter.StaggeredGridAdapter;
import com.android.im.imbean.MyBgBeanData;
import com.android.im.imbean.MyBgBeanTotleData;
import com.android.im.imnet.IMHttpResultObserver;
import com.android.im.imnet.http.IMHttpsService;
import com.android.nettylibrary.greendao.entity.IMChatBgBean;
import com.android.nettylibrary.greendao.greenutils.DaoUtils;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import java.util.ArrayList;
import java.util.List;


public class IMNewFindFragment extends IMBaseFragment implements View.OnClickListener {
    private View view;
    private SmartRefreshLayout refreshLayout;
    private RecyclerView mRecyclerView;
    public List<MyBgBeanTotleData> data=new ArrayList<>();
    @Override
    public View initView() {
        view = View.inflate(getActivity(), R.layout.fragment_im_find_new, null);
        refreshLayout = view.findViewById(R.id.refreshLayout);
        mRecyclerView = view.findViewById(R.id.recyclerView);
        initNewView();
        return view;
    }

    private void initNewView() {
        initRefreshHandView();
        initRecycleView();
    }



    @Override
    public void iniData() {
        showLoadingDialog();
        IMHttpsService.getMyBgJson(new IMHttpResultObserver<MyBgBeanData>() {
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
        if(myBgBeanData.getChatBackgroupList()!=null && myBgBeanData.getChatBackgroupList().size()>0){
            //把第一张白色的默认为不显示，但是要加入数据库中
            IMChatBgBean bean=new IMChatBgBean();
            bean.setIsmine(IMSManager.getMyUseId());
            bean.setUrl(myBgBeanData.getChatBackgroupList().get(0));
            bean.setIschoose(true);
            DaoUtils.getInstance().insertChatData(bean);
            for (int i = 1; i < myBgBeanData.getChatBackgroupList().size(); i++) {
                saveDBChatBg(myBgBeanData.getChatBackgroupList().get(i));
                data.add(new MyBgBeanTotleData(myBgBeanData.getChatBackgroupList().get(i),1));
            }
        }
        StaggeredGridAdapter staggeredGridAdapter = new StaggeredGridAdapter(getActivity(), data);
        mRecyclerView.setAdapter(staggeredGridAdapter);
    }

    @Override
    public void onClick(View v) {
    }

    private void initRefreshHandView() {
        refreshLayout.setEnableRefresh(false);//是否启用下拉刷新功能
        refreshLayout.setEnableLoadMore(false);//是否启用上拉加载功能
        refreshLayout.setEnablePureScrollMode(true);//是否启用纯滚动模式
        refreshLayout.setEnableOverScrollBounce(true);//是否启用越界回弹
        refreshLayout.setEnableOverScrollDrag(true);//是否启用越界拖动（仿苹果效果）1.0.4
    }

    private void initRecycleView() {
        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        staggeredGridLayoutManager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_NONE);
        mRecyclerView.setLayoutManager(staggeredGridLayoutManager);
    }


    /**
     * 聊天背景保存数据库
     */
    private void saveDBChatBg(String url) {
        IMChatBgBean bean=new IMChatBgBean();
        bean.setIsmine(IMSManager.getMyUseId());
        bean.setUrl(url);
        DaoUtils.getInstance().insertChatData(bean);
    }
}
