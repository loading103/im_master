package com.android.im.imui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.android.im.R;
import com.android.im.imadapter.IMCollectionSortAdapter;
import com.android.im.imbean.SmallProgramBean;
import com.android.im.imbean.IMBetGetBean;
import com.android.im.imeventbus.UpdateLatestEvent;
import com.android.im.imeventbus.UpdateMyCollectedEvent;
import com.android.im.imeventbus.UpdateSerachEvent;
import com.android.im.imnet.IMHttpResultObserver;
import com.android.im.imnet.http.IMHttpsService;
import com.android.im.imutils.IMStatusBarUtil;
import com.android.im.imview.IMCharacterParser;
import com.android.im.imview.IMScPinyinComparator;
import com.android.im.imview.IMSideBar;
import com.blankj.utilcode.util.KeyboardUtils;
import com.google.gson.Gson;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.RequestBody;


public class IMSmallMyCollectedActivity extends IMBaseActivity implements View.OnClickListener, AbsListView.OnScrollListener, AdapterView.OnItemClickListener {
    private static final int IS_UPDATE = 6;
    ImageView mIvBack;
    TextView mTvTitle;
    TextView mTvRight;
    IMSideBar bar;
    ListView mlistView;
    TextView mOverlayTv;
    LinearLayout mLayoutOverlay;
    RelativeLayout ll_no_content;
    private IMScPinyinComparator pinyinComparator;
    private IMCharacterParser characterParser;
    public String groupId;
    private List<SmallProgramBean> beans;
    private List<SmallProgramBean>datas=new ArrayList<>();
    private EditText mTvSearch;
    private LinearLayout llSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mine_collected);
        IMStatusBarUtil.setTranslucent(this,0);
        IMStatusBarUtil.setLightMode(this);
        EventBus.getDefault().register(this);
        findById();
        initView();
        initData();
    }

    private void findById() {
        mIvBack=findViewById(R.id.tv_top_finish);
        mTvTitle=findViewById(R.id.tv_top_title);
        mTvRight=findViewById(R.id.tv_top_right);
        ll_no_content=findViewById(R.id.ll_no_content);
        mTvRight.setVisibility(View.VISIBLE);
        mTvRight.setText("编辑");
        bar=findViewById(R.id.bar);
        bar.setVisibility(View.GONE);
        mlistView=findViewById(R.id.list);
        mOverlayTv=findViewById(R.id.overlay);
        mLayoutOverlay=findViewById(R.id.lin_head);
        mIvBack.setOnClickListener(this);
        mTvRight.setOnClickListener(this);
    }


    private void initView() {
        // 实例化汉字转拼音类
        characterParser = IMCharacterParser.getInstance();
        pinyinComparator = new IMScPinyinComparator();
        mlistView.setOnScrollListener(this);
        // 设置右侧触摸监听
        bar.setOnTouchingLetterChangedListener(new IMSideBar.OnTouchingLetterChangedListener() {
            @Override
            public void onTouchingLetterChanged(String s) {
                // 该字母首次出现的位置
                int position = mAdapter.getPositionForSection(s.charAt(0));
                if (position != -1) {
                    mlistView.setSelection(position+1);
                }
            }
        });
    }
    private void initData() {
        mTvTitle.setText("我的小程序");
        getCollection();
    }
    /**
     * 获取收藏
     */
    private void getCollection() {
        showLoadingDialog();
        final IMBetGetBean bean = new IMBetGetBean();
        String json = new Gson().toJson(bean);
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json);
        IMHttpsService.getCollection(body,new IMHttpResultObserver<List<SmallProgramBean>>() {
            @Override
            public void onSuccess(List<SmallProgramBean> data, String message) {
                dismissLoadingDialog();
                if(data==null || data.size()==0){
                    ll_no_content.setVisibility(View.VISIBLE);
                    return;
                }
                ll_no_content.setVisibility(View.GONE);
                bindViewAdapter(data);
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
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==IS_UPDATE&&resultCode==RESULT_OK){
            getCollection();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(UpdateMyCollectedEvent event) {
        getCollection();
    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.tv_top_finish){
            finish();
        }else if(v.getId()==R.id.tv_top_right){
            if (mAdapter==null || datas==null || datas.size()==0){
                Toast.makeText(this, "您暂时未添加小程序", Toast.LENGTH_SHORT).show();
                return;
            }
            if (mTvRight.getText().equals("编辑")) {
                mTvRight.setText("完成");
                mAdapter.setShowChoose(true);
            } else {
                mTvRight.setText("编辑");
                mAdapter.setShowChoose(false);

            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        KeyboardUtils.hideSoftInput(this);
        EventBus.getDefault().unregister(this);
    }

    private IMCollectionSortAdapter mAdapter;
    public void bindViewAdapter(List<SmallProgramBean> data){
        // 根据a-z进行排序源数据
        List<SmallProgramBean> beans = filledData(data);
        Collections.sort(beans, pinyinComparator);
        this.datas.clear();
        this.datas.addAll(beans);
        setLetter(datas);
        if(mAdapter==null){
            mAdapter = new IMCollectionSortAdapter(this,datas);
            mAdapter.setOnClickListener(new IMCollectionSortAdapter.onIMClickListener() {
                @Override
                public void onClick(int position) {
                    cancleCollection(position);
                }
            });
            mlistView.setAdapter(mAdapter);
            mlistView.setOnItemClickListener(this);
        }else {
            mAdapter.notifyDataSetChanged();
        }
    }

    /**
     * 取消收藏
     */
    private void cancleCollection(int position) {
        showLoadingDialog();
        final IMBetGetBean bean = new IMBetGetBean();
        bean.setProgramId(datas.get(position).getProgramId());
        String json = new Gson().toJson(bean);
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json);
        IMHttpsService.cancleCollection(body,new IMHttpResultObserver<String>() {
            @Override
            public void onSuccess(String data, String message) {
                dismissLoadingDialog();
                Toast.makeText(IMSmallMyCollectedActivity.this, "删除成功", Toast.LENGTH_SHORT).show();
                datas.remove(position);
                if(datas.size()==0){
                    mTvRight.setText("编辑");
                    ll_no_content.setVisibility(View.VISIBLE);
                    bar.setVisibility(View.GONE);
                    mAdapter.setShowChoose(false);
                }
                mAdapter.notifyDataSetChanged();
                setResult(RESULT_OK);
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
     * 获取需要显示哪些字母
     */
    private void setLetter(List<SmallProgramBean> datas) {
        if(datas==null || datas.size()==0){
            return;
        }
        StringBuffer sb=new StringBuffer();
        for (int i = 0; i < datas.size(); i++) {
            if(!sb.toString().contains(datas.get(i).getLetter())){
                sb.append(datas.get(i).getLetter());
            }
        }
        bar.setVisibility(View.VISIBLE);
        bar.setLetter(sb.toString());
    }
    /**
     * 为数据源添加字母letter的字段 用来排序
     * @param date
     * @return
     */
    private List<SmallProgramBean> filledData(List<SmallProgramBean> date) {
        for (int i = 0; i < date.size(); i++) {
            // 汉字转换成拼音
            String pinyin = characterParser.getSelling(date.get(i).getProgramName());
            String sortString = pinyin.substring(0, 1).toUpperCase();
            // 正则表达式，判断首字母是否是英文字母，ru如果是数字则为#
            if (sortString.matches("[A-Z]")) {
                date.get(i).setLetter(sortString.toUpperCase());
            } else {
                date.get(i).setLetter("#");
            }
        }
        return date;
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {

    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        if(datas.size()==0){
            return;
        }
        if(firstVisibleItem==0){//当为第一个时影藏，避免listview下拉时显示2个A导航条
            mLayoutOverlay.setVisibility(View.GONE);
        }else {
            mLayoutOverlay.setVisibility(View.VISIBLE);
        }
        if(firstVisibleItem>=1){
            firstVisibleItem=firstVisibleItem-1;
        }
        String letter = datas.get(firstVisibleItem).getLetter();
        bar.changeLetter(letter.charAt(0));
        int nextPosition = firstVisibleItem + 1;
//        //下一个item所在的分组
        int nextSelection = getSelectionFromPosition(nextPosition);
        if(getPositionFromSelection(nextSelection) == nextPosition) {
            View v = mlistView.getChildAt(0);
            int offsetY = v.getBottom() - mOverlayTv.getHeight();
            if(offsetY < 0){
                mLayoutOverlay.setPadding(0, offsetY, 0, 0);
            }else{
                mLayoutOverlay.setPadding(0, 0, 0, 0);
            }
        }else{
            mLayoutOverlay.setPadding(0, 0, 0, 0);
        }
        mOverlayTv.setText(letter+"");
    }

    /**
     * 获取数据源中，该字母对应分组的第一条数据 在数据源中所处的位置
     * @param letter
     * @return
     */
    private int getPositionFromSelection(int letter){
        for (int i = 0; i < datas.size(); i++) {
            SmallProgramBean data = datas.get(i);
            if(data.getLetter().charAt(0) == letter ){
                return i;
            }
        }
        return -1;
    }
    private int getSelectionFromPosition(int position){
        if(datas==null || datas.size()<=1){
            return -1;
        }
        return datas.get(position).getLetter().charAt(0);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//        Intent intent=new Intent(IMSmallMyCollectedActivity.this,SmallProgramActivity.class);
//        intent.putExtra("data",datas.get(position));
//        startActivityForResult(intent,IS_UPDATE);
        new SmallProgramActivity().toActivity(IMSmallMyCollectedActivity.this,datas.get(position),IS_UPDATE);
        setResult(RESULT_OK);
    }
}
