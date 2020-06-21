package com.android.im.imui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.android.im.IMSMsgManager;
import com.android.im.R;
import com.android.im.imadapter.IMShareContactSortAdapter;
import com.android.im.imeventbus.FinishChoosePersonEvent;
import com.android.im.imutils.IMStatusBarUtil;
import com.android.im.imutils.PinyinComparator;
import com.android.im.imview.IMCharacterParser;
import com.android.im.imview.IMSideBar;
import com.android.im.imview.dialog.IMShareSmallDiglog;
import com.android.nettylibrary.greendao.entity.IMConversationDetailBean;
import com.android.nettylibrary.greendao.entity.IMPersonBean;
import com.android.nettylibrary.greendao.greenutils.DaoUtils;
import com.android.nettylibrary.utils.IMLogUtil;
import com.blankj.utilcode.util.KeyboardUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class IMSmalSharePersonActivity extends IMBaseActivity implements View.OnClickListener, AbsListView.OnScrollListener, TextWatcher, AdapterView.OnItemClickListener {
    ImageView mIvBack;
    TextView mTvTitle;
    TextView mTvRight;
    IMSideBar bar;
    ListView mlistView;
    TextView mOverlayTv;
    LinearLayout mLayoutOverlay;
    private PinyinComparator pinyinComparator;
    private IMCharacterParser characterParser;
    private List<IMPersonBean> beans;
    private List<IMPersonBean>datas=new ArrayList<>();
    private EditText mTvSearch;
    private LinearLayout llSearch;
    private IMShareSmallDiglog diglog;
    private IMConversationDetailBean bean;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_totle_member);
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
        bar=findViewById(R.id.bar);
        mTvRight.setVisibility(View.GONE);
        mTvRight.setText("确认");
        mlistView=findViewById(R.id.list);
        mOverlayTv=findViewById(R.id.overlay);
        mLayoutOverlay=findViewById(R.id.lin_head);
        mIvBack.setOnClickListener(this);
        mLayoutOverlay=findViewById(R.id.lin_head);
        mTvRight.setOnClickListener(this);
        diglog = new IMShareSmallDiglog(this);
    }


    private void initView() {
        // 实例化汉字转拼音类
        characterParser = IMCharacterParser.getInstance();
        pinyinComparator = new PinyinComparator();
        mlistView.setOnScrollListener(this);
//        mlistView.addHeaderView(newHeadView());
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
        bean=(IMConversationDetailBean)getIntent().getSerializableExtra("bean");
        beans= DaoUtils.getInstance().queryAllMessageData();
        if(beans ==null || beans.size()==0){
            return;
        }
        mTvTitle.setText("选择联系人");
        mlistView.addHeaderView(HeadView());
        bindViewAdapter(beans);
    }

    @Override
    public void onClick(View v) {
        if(v.getId()== R.id.tv_top_finish){
            finish();
        }else if(v.getId()== R.id.tv_top_right) {

        }
    }

    private View HeadView() {
        View view=LayoutInflater.from(this).inflate(R.layout.item_choose_group,null);
        LinearLayout mllayout = view.findViewById(R.id.ll_item);
        mllayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(IMSmalSharePersonActivity.this, IMSmallShareGroupActivity.class);
                intent.putExtra("bean",bean);
                startActivity(intent );
            }
        });
        return view;
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        KeyboardUtils.hideSoftInput(this);
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(FinishChoosePersonEvent event) {
        finish();
    }
    private IMShareContactSortAdapter mAdapter;
    public void bindViewAdapter(List<IMPersonBean> data){
        // 根据a-z进行排序源数据
        List<IMPersonBean> beans = filledData(data);
        Collections.sort(beans, pinyinComparator);
        this.datas.clear();
        this.datas.addAll(beans);
        setLetter(datas);
        if(mAdapter==null){
            mAdapter = new IMShareContactSortAdapter(this,datas);
            mlistView.setAdapter(mAdapter);
            mlistView.setOnItemClickListener(this);
        }else {
            mAdapter.notifyDataSetChanged();
        }
    }

    /**
     * 获取需要显示哪些字母
     */
    private void setLetter(List<IMPersonBean> datas) {
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
        bar.setLetter(sb.toString());
    }
    /**
     * 为数据源添加字母letter的字段 用来排序
     * @param date
     * @return
     */
    private List<IMPersonBean> filledData(List<IMPersonBean> date) {
        for (int i = 0; i < date.size(); i++) {
            // 汉字转换成拼音
            String pinyin = characterParser.getSelling(date.get(i).getNickName());
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
            IMPersonBean data = datas.get(i);
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
        List<IMPersonBean> beans =new ArrayList<>();
        List<IMPersonBean> beans1 =   DaoUtils.getInstance().queryAllMessageData();
        if(beans1==null){
            return;
        }
        if(!TextUtils.isEmpty(toString)){
            Pattern pattern = Pattern.compile(toString);
            for (int i = 0; i <beans1.size(); i++) {
                Matcher matcher = pattern.matcher(beans1.get(i).getNickName());
                if(matcher.find()){
                    beans.add(beans1.get(i));
                }
            }
        }else {
            beans.addAll(beans1);
        }
        bindViewAdapter(beans);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        String customerId = datas.get((int) id).getCustomerId();
        IMSMsgManager.ShareSendText(bean,customerId,null);
        finish();
    }
}
