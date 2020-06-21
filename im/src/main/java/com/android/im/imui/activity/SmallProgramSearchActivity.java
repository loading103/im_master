package com.android.im.imui.activity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.fastjson.JSONArray;
import com.android.im.DaoManager;
import com.android.im.R;
import com.android.im.R2;
import com.android.im.R2;
import com.android.im.imadapter.LatestUseSearchAdapter;
import com.android.im.imbean.IMBetGetBean;
import com.android.im.imbean.RecentlyUseBean;
import com.android.im.imbean.SmallProgramBean;
import com.android.im.imbean.SmallProgramBeanDao;
import com.android.im.imdialog.SmallProgramAgreeDialog;
import com.android.im.imeventbus.UpdateLatestEvent;
import com.android.im.imeventbus.UpdateSerachEvent;
import com.android.im.imnet.IMHttpResultObserver;
import com.android.im.imnet.http.IMHttpsService;
import com.android.im.imutils.IMStatusBarUtil;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.SizeUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemChildClickListener;
import com.google.gson.Gson;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import jp.wasabeef.glide.transformations.RoundedCornersTransformation;
import okhttp3.MediaType;
import okhttp3.RequestBody;

/**
 * Created by Administrator on 2019/12/23.
 * Describe:小程序搜索——最近浏览
 */
public class SmallProgramSearchActivity extends IMBaseActivity {
    private static final int IS_UPDATE = 7;
    @BindView(R2.id.tv_top_title)
    TextView tvTopTitle;
    @BindView(R2.id.rl_top)
    RelativeLayout rlTop;
    @BindView(R2.id.recyclerView)
    RecyclerView recyclerView;
    private List<SmallProgramBean> data;
    private LatestUseSearchAdapter adapter;
    private ImageView iv1;
    private ImageView iv2;
    private ImageView iv3;
    private boolean isUpdate=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_small_program_search);
        IMStatusBarUtil.setTranslucent(this, 0);
        IMStatusBarUtil.setLightMode(this);
        EventBus.getDefault().register(this);
        initView();
        getRecentlyUse();
    }
    private void initView() {
        tvTopTitle.setText("小程序");
        rlTop.setBackgroundColor(getResources().getColor(R.color.color_F3F3F3));

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new LatestUseSearchAdapter();
        View header = LayoutInflater.from(this).inflate(R.layout.header_latest_browsing_search, null);
        iv1 = header.findViewById(R.id.iv1);
        iv2 = header.findViewById(R.id.iv2);
        iv3 = header.findViewById(R.id.iv3);
        header.findViewById(R.id.rl_my).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(SmallProgramSearchActivity.this,IMSmallMyCollectedActivity.class);
                startActivityForResult(intent,IS_UPDATE);
            }
        });
        header.findViewById(R.id.ll_search).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActicity(SmallSearchActivity.class);
            }
        });
        adapter.addHeaderView(header);
        adapter.addChildClickViewIds(R.id.ll_collection, R.id.ll_delete,R.id.content);
        adapter.setOnItemChildClickListener(new OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                SmallProgramBean bean=((SmallProgramBean)adapter.getData().get(position));
                if (view.getId() == R.id.ll_collection) {
                    if(bean.getIsConllection().equals("Y")){
                        cancleCollection(bean.getProgramId(),position);
                    }else {
                        addCollection(bean.getProgramId(),position);
                    }
                }else if(view.getId() == R.id.ll_delete){
                    addRecentlyUse(bean.getUseId(),position);
                }else if(view.getId() == R.id.content){
//                    Intent intent=new Intent(SmallProgramSearchActivity.this, SmallProgramActivity.class);
//                    intent.putExtra("data",bean);
//                    startActivityForResult(intent,IS_UPDATE);
                    new SmallProgramActivity().toActivity(SmallProgramSearchActivity.this,bean,IS_UPDATE);

                }
            }
        });
        recyclerView.setAdapter(adapter);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==IS_UPDATE&&resultCode==RESULT_OK){
            isUpdate=true;
            getRecentlyUse();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(UpdateSerachEvent event) {
        isUpdate=true;
        getRecentlyUse();
    }

    @OnClick(R2.id.tv_top_finish)
    public void onViewClicked() {
        finish();

    }

    /**
     * 获取最近使用
     */
    private void getRecentlyUse() {
        showLoadingDialog();
        final IMBetGetBean bean = new IMBetGetBean();
//        bean.setCurrent("0");
//        bean.setSize("10");
        String json = new Gson().toJson(bean);
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json);
        IMHttpsService.getRecentlyUse(body, new IMHttpResultObserver<RecentlyUseBean>() {
            @Override
            public void onSuccess(RecentlyUseBean data, String message) {
                dismissLoadingDialog();
                getCollection();
                adapter.setNewData(data.getRecords());
                if(!isUpdate){
                    recyclerView.scrollBy(0, SizeUtils.dp2px(102.5f));
                }
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
     * 添加收藏
     * @param programId
     * @param position
     */
    private void addCollection(String programId, int position) {
        showLoadingDialog();
        final IMBetGetBean bean = new IMBetGetBean();
        bean.setProgramId(programId);
        String json = new Gson().toJson(bean);
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json);
        IMHttpsService.addCollection(body, new IMHttpResultObserver<String>() {
            @Override
            public void onSuccess(String data, String message) {
                dismissLoadingDialog();
                ToastUtils.showShort("添加成功");
                adapter.getItem(position).setIsConllection("Y");
                adapter.notifyItemChanged(position+1);
                EventBus.getDefault().post(new UpdateLatestEvent());
                getCollection();
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
     * 取消收藏
     */
    private void cancleCollection(String programId, int position) {
        showLoadingDialog();
        final IMBetGetBean bean = new IMBetGetBean();
        bean.setProgramId(programId);
        String json = new Gson().toJson(bean);
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json);
        IMHttpsService.cancleCollection(body, new IMHttpResultObserver<String>() {
            @Override
            public void onSuccess(String data, String message) {
                dismissLoadingDialog();
                Toast.makeText(SmallProgramSearchActivity.this, "移除成功", Toast.LENGTH_SHORT).show();
                adapter.getItem(position).setIsConllection("N");
                adapter.notifyItemChanged(position+1);
                EventBus.getDefault().post(new UpdateLatestEvent());
                getCollection();
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
     * 删除最近使用
     */
    private void addRecentlyUse(String useId, int position) {
        showLoadingDialog();
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), "["+useId+"]");
        IMHttpsService.deleteRecentlyUse(body, new IMHttpResultObserver<String>() {
            @Override
            public void onSuccess(String data, String message) {
                dismissLoadingDialog();
                adapter.remove(position);
                EventBus.getDefault().post(new UpdateLatestEvent());
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
     * 获取收藏
     */
    private void getCollection() {
//        showLoadingDialog();
        final IMBetGetBean bean = new IMBetGetBean();
        String json = new Gson().toJson(bean);
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json);
        IMHttpsService.getCollection(body, new IMHttpResultObserver<List<SmallProgramBean>>() {
            @Override
            public void onSuccess(List<SmallProgramBean> data, String message) {
                dismissLoadingDialog();
                if(data.size()==0){
                    iv1.setVisibility(View.GONE);
                    iv2.setVisibility(View.GONE);
                    iv3.setVisibility(View.GONE);
                    return;
                }
                for (int i = 0; i < data.size(); i++) {
                    if(i==0){
                        Glide.with(getApplicationContext()).load(data.get(0).getTwoImage())
                                .apply(RequestOptions.bitmapTransform(new CircleCrop()))
                                .into(iv1);
                        iv1.setVisibility(View.VISIBLE);
                        iv2.setVisibility(View.GONE);
                    }
                    if(i==1){
                        Glide.with(getApplicationContext()).load(data.get(1).getTwoImage())
                                .apply(RequestOptions.bitmapTransform(new CircleCrop()))
                                .into(iv2);
                        iv1.setVisibility(View.VISIBLE);
                        iv2.setVisibility(View.VISIBLE);
                        iv3.setVisibility(View.GONE);
                    }
                    if(i==2){
                        Glide.with(getApplicationContext()).load(data.get(2).getTwoImage())
                                .apply(RequestOptions.bitmapTransform(new CircleCrop()))
                                .into(iv3);
                        iv1.setVisibility(View.VISIBLE);
                        iv2.setVisibility(View.VISIBLE);
                        iv3.setVisibility(View.VISIBLE);
                    }
                }
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
