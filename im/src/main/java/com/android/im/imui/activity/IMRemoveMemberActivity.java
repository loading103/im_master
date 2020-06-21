package com.android.im.imui.activity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.im.IMSManager;
import com.android.im.R;
import com.android.im.imadapter.IMContactSortAdapter;
import com.android.im.imadapter.IMCreatContactSortAdapter;
import com.android.im.imbean.IMHttpCommonBean;
import com.android.im.imeventbus.IMCreatGroupEvent;
import com.android.im.imeventbus.IMMessageUpdataGroupInforEvent;
import com.android.im.imnet.IMHttpResultObserver;
import com.android.im.imnet.http.IMHttpsService;
import com.android.im.imutils.IMStatusBarUtil;
import com.android.im.imview.IMCharacterParser;
import com.android.im.imview.IMPinyinComparator;
import com.android.im.imview.IMSideBar;
import com.android.nettylibrary.IMSConfig;
import com.android.nettylibrary.greendao.entity.IMGroupBean;
import com.android.nettylibrary.greendao.entity.IMGroupMemberBean;
import com.android.nettylibrary.greendao.entity.IMPersonBean;
import com.android.nettylibrary.greendao.greenutils.DaoUtils;
import com.android.nettylibrary.http.IMGetIMMemberData;
import com.android.nettylibrary.http.IMGroupNoticeBean;
import com.android.nettylibrary.utils.IMLogUtil;
import com.android.nettylibrary.utils.IMPreferenceUtil;
import com.blankj.utilcode.util.KeyboardUtils;
import com.google.gson.Gson;

import org.greenrobot.eventbus.EventBus;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static com.android.im.IMSManager.okHttpClient;
import static com.android.im.imnet.IMBaseConstant.JSON;

/**
 * 移除群聊是移除群成员   邀请群聊是邀请好友  实体类不一样  不在一个类里面
 */

public class IMRemoveMemberActivity extends IMBaseActivity implements View.OnClickListener, AbsListView.OnScrollListener, TextWatcher {
    ImageView mIvBack;
    TextView mTvTitle;
    TextView mTvRight;
    IMSideBar bar;
    ListView mlistView;
    TextView mOverlayTv;
    LinearLayout mLayoutOverlay;
    private IMPinyinComparator pinyinComparator;
    private IMCharacterParser characterParser;
    public String groupId;
    private List<IMGroupMemberBean> beans;
    private List<IMGroupMemberBean>datas=new ArrayList<>();
    private EditText mTvSearch;
    private LinearLayout llSearch;

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
        mlistView=findViewById(R.id.list);
        mOverlayTv=findViewById(R.id.overlay);
        mLayoutOverlay=findViewById(R.id.lin_head);
        mTvRight.setVisibility(View.VISIBLE);
        mIvBack.setOnClickListener(this);
        mTvRight.setOnClickListener(this);

        groupId=getIntent().getStringExtra("groupId");
        mTvRight.setText("移除");
        mTvTitle.setText("移除群聊");
    }


    private void initView() {
        // 实例化汉字转拼音类
        characterParser = IMCharacterParser.getInstance();
        pinyinComparator = new IMPinyinComparator();
        mlistView.setOnScrollListener(this);
        mlistView.addHeaderView(newHeadView());
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
//        if(TextUtils.isEmpty(groupId)){
//            return;
//        }
//        beans = daoUtils.queryGroupAllMemberBean(groupId);
//        if(beans ==null || beans.size()==0){
//            return;
//        }
//        for (int i = 0; i < beans.size(); i++) {
//            if(beans.get(i).getMemberId().equals(IMSManager.getMyUseId())){
//                beans.remove(i);
//            }
//        }
//        bindViewAdapter(beans);

        if(TextUtils.isEmpty(groupId)){
            return;
        }
        getGroupMember(groupId);

    }

    public void getGroupMember(final String groupId) {
        showLoadingDialog();
        IMGetIMMemberData bean=new IMGetIMMemberData();
        bean.setGroupId(groupId);
        String json = new Gson().toJson(bean);
        RequestBody body = RequestBody.create(JSON, json);
        Request request = new Request.Builder()
                .url(IMSConfig.HTTP_IM_GROUP_MEMBER)
                .addHeader("Authorization", IMPreferenceUtil.getPreference_String(IMSConfig.SAVE_TOKEN,""))
                .post(body)
                .build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                dismissLoadingDialog();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                com.android.nettylibrary.http.IMGroupMemberBean bean = new Gson().fromJson(response.body().string(), com.android.nettylibrary.http.IMGroupMemberBean.class);
                List<com.android.nettylibrary.http.IMGroupMemberBean.GroupMemberDetaiData> groups = bean.getData();
                if(groups==null){
                    return;
                }
                updataMemberInforList(groups);
            }
        });
    }

    /**
     * 跟新群成员数据库数据
     */
    private void updataMemberInforList(List<com.android.nettylibrary.http.IMGroupMemberBean.GroupMemberDetaiData> groups) {
        if(groups!=null || groups.size()>0){
            daoUtils.deleteGroupAllMemberData(groupId);
        }
        for (int i = 0; i < groups.size(); i++) {
            com.android.nettylibrary.greendao.entity.IMGroupMemberBean imGroupMemberBean = daoUtils.queryGroupMemberBean(groupId,groups.get(i).getMemberId());
            if(imGroupMemberBean==null){  //
                imGroupMemberBean=new com.android.nettylibrary.greendao.entity.IMGroupMemberBean();
                imGroupMemberBean.setAvatar(groups.get(i).getAvatar());
                imGroupMemberBean.setNickName(TextUtils.isEmpty(groups.get(i).getNickName())? "":groups.get(i).getNickName());
                imGroupMemberBean.setGroupId(groups.get(i).getGroupId());
                imGroupMemberBean.setLable(groups.get(i).getLable());
                imGroupMemberBean.setLevel(groups.get(i).getLevel());
                imGroupMemberBean.setGroupName(groups.get(i).getGroupName());
                imGroupMemberBean.setMemberId(groups.get(i).getMemberId());
                imGroupMemberBean.setTitle(groups.get(i).getTitle());
                imGroupMemberBean.setBlacklist(groups.get(i).getBlacklist());
                imGroupMemberBean.setForbiddenWords(groups.get(i).getForbiddenWords());
                imGroupMemberBean.setIsViewTitle(groups.get(i).getIsViewTitle());
                imGroupMemberBean.setGmId(groups.get(i).getGmId());
                daoUtils.insertGroupMemberData(imGroupMemberBean.getGroupId(),imGroupMemberBean);
            }else {
                imGroupMemberBean.setAvatar(groups.get(i).getAvatar());
                imGroupMemberBean.setNickName(TextUtils.isEmpty(groups.get(i).getNickName())? "":groups.get(i).getNickName());
                imGroupMemberBean.setGroupId(groups.get(i).getGroupId());
                imGroupMemberBean.setLable(groups.get(i).getLable());
                imGroupMemberBean.setLevel(groups.get(i).getLevel());
                imGroupMemberBean.setGroupName(groups.get(i).getGroupName());
                imGroupMemberBean.setMemberId(groups.get(i).getMemberId());
                imGroupMemberBean.setTitle(groups.get(i).getTitle());
                imGroupMemberBean.setBlacklist(groups.get(i).getBlacklist());
                imGroupMemberBean.setForbiddenWords(groups.get(i).getForbiddenWords());
                imGroupMemberBean.setIsViewTitle(groups.get(i).getIsViewTitle());
                imGroupMemberBean.setGmId(groups.get(i).getGmId());
                daoUtils.updateGroupMemberData(imGroupMemberBean);
            }
        }
        beans = daoUtils.queryGroupAllMemberBean(groupId);
        if(beans ==null || beans.size()==0){
            return;
        }
        for (int i = 0; i < beans.size(); i++) {
            if(beans.get(i).getMemberId().equals(IMSManager.getMyUseId())){
                beans.remove(i);
            }
        }
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                dismissLoadingDialog();
                bindViewAdapter(beans);
            }
        });

    }



    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.tv_top_finish){
            finish();
        }else if(v.getId()==R.id.tv_top_right){
            if(mAdapter!=null){
                List<IMGroupMemberBean> choosedList = mAdapter.getChoosedList();
                if(choosedList.size()==0){
                    Toast.makeText(this, "请选择人员", Toast.LENGTH_SHORT).show();
                }else {
                    removeMemberHttpData(choosedList);
                }
            }
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
        mTvSearch.setHint("搜索用户");
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

    private IMCreatContactSortAdapter mAdapter;
    public void bindViewAdapter(List<IMGroupMemberBean> data){
        // 根据a-z进行排序源数据
        List<IMGroupMemberBean> beans = filledData(data);
        Collections.sort(beans, pinyinComparator);
        this.datas.clear();
        this.datas.addAll(beans);
        setLetter(datas);
        if(mAdapter==null){
            mAdapter = new IMCreatContactSortAdapter(this,datas);
            mlistView.setAdapter(mAdapter);
        }else {
            mAdapter.notifyDataSetChanged();
        }
    }

    /**
     * 获取需要显示哪些字母
     */
    private void setLetter(List<IMGroupMemberBean> datas) {
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
    private List<IMGroupMemberBean> filledData(List<IMGroupMemberBean> date) {
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
            IMGroupMemberBean data = datas.get(i);
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

    /**
     * 移除群聊
     */
    private void removeMemberHttpData(List<IMGroupMemberBean> choosedList) {
        showLoadingDialog();
        IMHttpCommonBean bean = new IMHttpCommonBean();
        List<String> memberIds=new ArrayList<>();
        for (int i = 0; i < choosedList.size(); i++) {
            memberIds.add(choosedList.get(i).getMemberId());
        }

        bean.setMemberIdList(memberIds);
        bean.setGroupId(groupId);
        String json = new Gson().toJson(bean);
        Log.e("MyOwnTag:", "IMRemoveMemberActivity ----" +json);
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"),json);
        IMHttpsService.removeGroupMemberJson(body, new IMHttpResultObserver<String>() {
            @Override
            public void onSuccess(String data, String message) {
                dismissLoadingDialog();
                Toast.makeText(IMRemoveMemberActivity.this, "成功移除群聊", Toast.LENGTH_SHORT).show();
                for (int i = 0; i < choosedList.size(); i++) {
                    if(beans.contains(choosedList.get(i))){
                        beans.remove(choosedList.get(i));
                        daoUtils.deleteGroupMemberData(groupId,choosedList.get(i).getMemberId());
                    }
                }
                EventBus.getDefault().post(new IMMessageUpdataGroupInforEvent());
                mAdapter.notifyDataSetChanged();
                finish();
            }
            @Override
            public void _onError(Throwable e) {
                dismissLoadingDialog();
                Toast.makeText(IMRemoveMemberActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void _onErrorLocal(Throwable e, String message, String code) {
                dismissLoadingDialog();
                Toast.makeText(IMRemoveMemberActivity.this,message, Toast.LENGTH_SHORT).show();
            }
        });
    }

    public String getGroupId() {
        return groupId;
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
        List<IMGroupMemberBean> beans =new ArrayList<>();
        List<IMGroupMemberBean> beans1 =  daoUtils.queryGroupAllMemberBean(groupId);
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
        for (int i = 0; i < beans.size(); i++) {
            if(beans.get(i).getMemberId().equals(IMSManager.getMyUseId())){
                beans.remove(i);
            }
        }
        bindViewAdapter(beans);
    }
}
