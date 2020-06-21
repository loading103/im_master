package com.android.im.imui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.im.IMSMsgManager;
import com.android.im.R;
import com.android.im.imadapter.IMSmallGroupContactSortAdapter;
import com.android.im.imbean.IMBetGetBean;
import com.android.im.imbean.SmallProgramBean;
import com.android.im.imeventbus.FinishChoosePersonEvent;
import com.android.im.imeventbus.IMGoToFirstFragmentEvent;
import com.android.im.imnet.IMHttpResultObserver;
import com.android.im.imnet.http.IMHttpsService;
import com.android.im.imutils.IMStatusBarUtil;
import com.android.im.imview.IMCharacterParser;
import com.android.im.imview.IMPinyinGroupComparator;
import com.android.im.imview.IMSideBar;
import com.android.im.imview.dialog.IMShareSmallDiglog;
import com.android.nettylibrary.greendao.entity.IMGroupBean;
import com.android.nettylibrary.greendao.entity.IMPersonBean;
import com.android.nettylibrary.greendao.greenutils.DaoUtils;
import com.android.nettylibrary.http.IMMessageDataJson;
import com.android.nettylibrary.utils.IMLogUtil;
import com.blankj.utilcode.util.KeyboardUtils;
import com.google.gson.Gson;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.MediaType;
import okhttp3.RequestBody;


public class IMSmallChooseGroupActivity extends IMBaseActivity implements View.OnClickListener, AbsListView.OnScrollListener, TextWatcher {
    ImageView mIvBack;
    TextView mTvTitle;
    TextView mTvRight;
    IMSideBar bar;
    ListView mlistView;
    TextView mOverlayTv;
    LinearLayout mLayoutOverlay;
    RelativeLayout reListview;
    private IMPinyinGroupComparator pinyinComparator;
    private IMCharacterParser characterParser;
    public String groupId;
    private List<IMGroupBean> beans;
    private List<IMGroupBean>datas=new ArrayList<>();
    private IMSmallGroupContactSortAdapter mAdapter;
    private EditText mTvSearch;
    private LinearLayout llSearch;
    private View view_empty;
    private IMShareSmallDiglog diglog;
    private SmallProgramBean bean;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_totle_member);
        IMStatusBarUtil.setTranslucent(this,0);
        IMStatusBarUtil.setLightMode(this);
        findById();
        initView();
        initData();
    }

    private void findById() {
        mIvBack=findViewById(R.id.tv_top_finish);
        mTvTitle=findViewById(R.id.tv_top_title);
        mTvRight=findViewById(R.id.tv_top_right);
        bar=findViewById(R.id.bar);
        mTvRight.setVisibility(View.VISIBLE);
        mTvRight.setText("确认");
        mlistView=findViewById(R.id.list);
        mOverlayTv=findViewById(R.id.overlay);
        mLayoutOverlay=findViewById(R.id.lin_head);
        reListview=findViewById(R.id.re_listview);
        mIvBack.setOnClickListener(this);
        mTvRight.setOnClickListener(this);
        diglog = new IMShareSmallDiglog(this);
    }


    private void initView() {
        // 实例化汉字转拼音类
        characterParser = IMCharacterParser.getInstance();
        pinyinComparator = new IMPinyinGroupComparator();
        mlistView.setOnScrollListener(this);
//        mlistView.addHeaderView(newHeadView());
        bar.setVisibility(View.GONE);
        // 设置右侧触摸监听
        bar.setOnTouchingLetterChangedListener(new IMSideBar.OnTouchingLetterChangedListener() {
            @Override
            public void onTouchingLetterChanged(String s) {
                // 该字母首次出现的位置
                int position = mAdapter.getPositionForSection(s.charAt(0));
                if (position != -1) {
                    mlistView.setSelection(position);
                }
            }
        });
    }


    private void initData() {
        mTvTitle.setText("选择群聊");
        showLoadingDialog();
        bean=(SmallProgramBean)getIntent().getSerializableExtra("data");

        IMHttpsService.GetGroupListJson(new IMHttpResultObserver<List<IMGroupBean>>() {
            @Override
            public void onSuccess(List<IMGroupBean> groups, String message) {
                if(groups==null || groups.size()==0){
                    bar.setVisibility(View.INVISIBLE);
                    view_empty = LayoutInflater.from(IMSmallChooseGroupActivity.this).inflate(R.layout.empty_group, reListview, true);
                    mlistView.setEmptyView(view_empty);
                    dismissLoadingDialog();
                    return;
                }
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        daoUtils.deleteGroupAllData();
                        if(groups!=null && groups.size()>0) {
                            for (int i = 0; i < groups.size(); i++) {
                                daoUtils.insertGroupData(groups.get(i));
                            }
                            bindViewAdapter(groups);
                        }
                    }
                }).start();
            }

            @Override
            public void _onError(Throwable e) {
                dismissLoadingDialog();
                Toast.makeText(IMSmallChooseGroupActivity.this,e.getMessage(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void _onErrorLocal(Throwable e, String message, String code) {
                dismissLoadingDialog();
                Toast.makeText(IMSmallChooseGroupActivity.this,message, Toast.LENGTH_SHORT).show();
            }
        });


    }


    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.tv_top_finish){
            finish();
        }else if(v.getId()==R.id.tv_top_right){
            List<IMGroupBean>beans=new ArrayList<>();
            for (int i = 0; i < datas.size(); i++) {
                if(datas.get(i).getIschoosed()){
                    beans.add(datas.get(i));
                }
            }
            if(beans==null || beans.size()==0){
                Toast.makeText(this, "请选择群组", Toast.LENGTH_SHORT).show();
                return;
            }
            if(beans.size()>9){
                Toast.makeText(this, "同时最多分享9个群组", Toast.LENGTH_SHORT).show();
                return;
            }
            diglog.showPersonalDiglog(null,beans,bean,new IMShareSmallDiglog.OnSendListener() {
                @Override
                public void setOnSendListener(String content) {


                    EventBus.getDefault().post(new IMGoToFirstFragmentEvent());
                    List<String> ids=new ArrayList<>();
                    for (int i = 0; i < beans.size(); i++) {
                        ids.add(beans.get(i).getGroupId());
                    }
                    IMMessageDataJson json=new IMMessageDataJson();
                    json.setTwoImage(bean.getTwoImage());
                    json.setThreeImage(bean.getThreeImage());
                    json.setShareImage(bean.getShareImage());
                    json.setProgramId(bean.getProgramId());
                    json.setProgramName(bean.getProgramName());
                    json.setShareTitle(bean.getShareTitle());
                    json.setProgramUrl(bean.getProgramUrl());
                    IMSMsgManager.ShareSmallProgress(json,null,ids,null);
                    if(!TextUtils.isEmpty(content)){
                        IMSMsgManager.ShareSmallProgress(null,content,ids,null);
                    }
                    Toast.makeText(IMSmallChooseGroupActivity.this, "分享成功", Toast.LENGTH_SHORT).show();
                    EventBus.getDefault().post(new FinishChoosePersonEvent());
                    finish();
                }
            });
        }
    }

    /**
     * 添加头部
     */
    private View newHeadView() {
        View view = LayoutInflater.from(this).inflate(R.layout.item_contacts_header,null);
        mTvSearch = view.findViewById(R.id.im_tv_serch);
        mTvSearch.addTextChangedListener(this);
        llSearch = view.findViewById(R.id.ll_search);
        KeyboardUtils.registerSoftInputChangedListener(this, height -> {
            if(height==0){
                int newHeight = LinearLayout.LayoutParams.WRAP_CONTENT;
                LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) mTvSearch.getLayoutParams();
                lp.width = newHeight;
                mTvSearch.setLayoutParams(lp);
                mTvSearch.clearFocus();
            }else {
                int newHeight = LinearLayout.LayoutParams.MATCH_PARENT;
                LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) mTvSearch.getLayoutParams();
                lp.width = newHeight;
                mTvSearch.setLayoutParams(lp);
            }
        });
        llSearch.setOnClickListener(v -> KeyboardUtils.showSoftInput(mTvSearch));
        return  view;
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        KeyboardUtils.hideSoftInput(this);
    }


    public void bindViewAdapter(List<IMGroupBean> data){
        // 根据a-z进行排序源数据
        List<IMGroupBean> beans = filledData(data);
        Collections.sort(beans, pinyinComparator);
        this.datas.clear();
        this.datas.addAll(beans);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                setLetter(datas);
                if(mAdapter==null){
                    mAdapter = new IMSmallGroupContactSortAdapter(IMSmallChooseGroupActivity.this,datas);
                    mlistView.setAdapter(mAdapter);
                }else {
                    mAdapter.notifyDataSetChanged();
                }
                dismissLoadingDialog();
            }
        });

    }
    /**
     * 获取需要显示哪些字母
     */
    private void setLetter(List<IMGroupBean> datas) {
        if(datas==null || datas.size()==0){
            return;
        }
        StringBuffer sb=new StringBuffer();
        for (int i = 0; i < datas.size(); i++) {
            if(!sb.toString().contains(datas.get(i).getLetter())){
                sb.append(datas.get(i).getLetter());
            }
        }
        IMLogUtil.d("MyOwnTag:", "ContactListFragment ----" +sb.toString());
        bar.setVisibility(View.VISIBLE);
        bar.setLetter(sb.toString());
    }
    /**
     * 为数据源添加字母letter的字段 用来排序
     * @param date
     * @return
     */
    private List<IMGroupBean> filledData(List<IMGroupBean> date) {
        for (int i = 0; i < date.size(); i++) {
            // 汉字转换成拼音
            String pinyin = characterParser.getSelling(date.get(i).getGroupName());
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
            IMGroupBean data = datas.get(i);
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
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        reSearchContent(s.toString());
    }

    /**
     * 搜索查询类容（模糊查询）
     */
    private void reSearchContent(String toString) {
        List<IMGroupBean> beans =new ArrayList<>();
        List<IMGroupBean> beans1 =  DaoUtils.getInstance().queryAllGroupData();
        if(beans1==null){
            return;
        }
        if(!TextUtils.isEmpty(toString)){
            Pattern pattern = Pattern.compile(toString);
            for (int i = 0; i <beans1.size(); i++) {
                Matcher matcher = pattern.matcher(beans1.get(i).getGroupName());
                if(matcher.find()){
                    beans.add(beans1.get(i));
                }
            }
        }else {
            beans.addAll(beans1);
        }
        bindViewAdapter(beans);
    }
}
