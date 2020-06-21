package com.android.im.imui.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
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

import com.android.im.IMSManager;
import com.android.im.R;
import com.android.im.imadapter.IMCreatGroupAdapter;
import com.android.im.imadapter.IMGroupContactSortAdapter;
import com.android.im.imbean.IMBetGetBean;
import com.android.im.imbean.IMHttpCommonBean;
import com.android.im.imbean.IMImageViewBean;
import com.android.im.imeventbus.IMCreatGroupEvent;
import com.android.im.imeventbus.IMMessageAddGroupInforEvent;
import com.android.im.imeventbus.IMMessageUpdataGroupInforEvent;
import com.android.im.imnet.IMHttpResultObserver;
import com.android.im.imnet.http.IMHttpsService;
import com.android.im.imutils.IMStatusBarUtil;
import com.android.im.imutils.IMStopClickFast;
import com.android.im.imview.IMCharacterParser;
import com.android.im.imview.IMPinyinPersonComparator;
import com.android.im.imview.IMSideBar;
import com.android.nettylibrary.greendao.entity.IMConversationBean;
import com.android.nettylibrary.greendao.entity.IMGroupBean;
import com.android.nettylibrary.greendao.entity.IMGroupMemberBean;
import com.android.nettylibrary.greendao.entity.IMPersonBean;
import com.android.nettylibrary.greendao.greenutils.DaoUtils;
import com.android.nettylibrary.utils.IMLogUtil;
import com.blankj.utilcode.util.KeyboardUtils;
import com.google.gson.Gson;
import com.othershe.combinebitmap.CombineBitmap;
import com.othershe.combinebitmap.layout.DingLayoutManager;
import com.othershe.combinebitmap.layout.WechatLayoutManager;
import com.othershe.combinebitmap.listener.OnProgressListener;

import org.greenrobot.eventbus.EventBus;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.MediaType;
import okhttp3.RequestBody;


public class IMCreatGroupActivity extends IMBaseActivity implements View.OnClickListener, AbsListView.OnScrollListener, TextWatcher {
    ImageView mIvBack;
    TextView mTvTitle;
    TextView mTvRight;
    IMSideBar bar;
    ListView mlistView;
    TextView mOverlayTv;
     LinearLayout llEmpty;
    LinearLayout mLayoutOverlay;
    TextView mTvNoContent;
    RelativeLayout mRllisview;
    private IMPinyinPersonComparator pinyinComparator;
    private IMCharacterParser characterParser;
    public String groupId;
    private List<IMPersonBean> beans;
    private List<IMPersonBean>datas=new ArrayList<>();
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
        llEmpty = findViewById(R.id.ll_empty);
        mTvRight.setVisibility(View.VISIBLE);
        mTvNoContent=findViewById(R.id.im_no_tv);
        mRllisview=findViewById(R.id.re_listview);
        mIvBack.setOnClickListener(this);
        mTvRight.setOnClickListener(this);
        groupId=getIntent().getStringExtra("groupId");
        if(TextUtils.isEmpty(groupId)){
            mTvRight.setText("创建");
            mTvTitle.setText("创建群聊");
        }else {
            mTvRight.setText("邀请");
            mTvTitle.setText("邀请群聊");
        }
    }


    private void initView() {
        // 实例化汉字转拼音类
        characterParser = IMCharacterParser.getInstance();
        pinyinComparator = new IMPinyinPersonComparator();
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
        beans = daoUtils.queryAllMessageData();
        if(beans ==null || beans.size()==0){
            return;
        }

        if(!TextUtils.isEmpty(groupId)){
            List<IMGroupMemberBean> data = daoUtils.queryGroupAllMemberBean(groupId);
            List<IMPersonBean> list=new ArrayList<>();
            if(data!=null && data.size()>0){
                for (int i = 0; i < beans.size(); i++) {
                    for (int j = 0; j < data.size(); j++) {
                        if(beans.get(i).getMemberId().equals(data.get(j).getMemberId())){
                            list.add(beans.get(i));
                        }
                    }
                }
                if(list.size()>0){
                    beans.removeAll(list);
                }
            }
        }

        for (int i = 0; i < beans.size(); i++) {
            beans.get(i).setIschoosed(false);
        }
        bindViewAdapter(beans);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        KeyboardUtils.hideSoftInput(this);
    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.tv_top_finish){
            finish();
        }else if(v.getId()==R.id.tv_top_right){
            if(IMStopClickFast.isFastClick()){
                if(mAdapter!=null){
                    List<IMPersonBean> choosedList = mAdapter.getChoosedList();
                    if(choosedList.size()==0){
                        Toast.makeText(this, "请选择人员", Toast.LENGTH_SHORT).show();
                    }else {
                        showLoadingDialog();
                        if(TextUtils.isEmpty(groupId)){
                            CreatGroupHttpData(choosedList);
                        }else {
                            addGroupHttpData(choosedList);
                        }
                    }
                }
            }
        }
    }




    private View newHeadView() {
        View view = LayoutInflater.from(this).inflate(R.layout.item_contacts_header,null);
        mTvSearch = view.findViewById(R.id.im_tv_serch);
        mTvSearch.addTextChangedListener(this);
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
        view.findViewById(R.id.ll_search).setOnClickListener(v -> KeyboardUtils.showSoftInput(mTvSearch));
        return  view;
    }




    private IMCreatGroupAdapter mAdapter;
    public void bindViewAdapter(List<IMPersonBean> data){
        // 根据a-z进行排序源数据
        List<IMPersonBean> beans = filledData(data);
        Collections.sort(beans, pinyinComparator);
        if(beans!=null && beans.size()>0){
            for (int i = 0; i < beans.size(); i++) {
                if(!TextUtils.isEmpty(beans.get(i).getIsKefu()) && beans.get(i).getIsKefu().equals("Y")){
                    beans.remove(i);
                }
            }
        }
        if(beans ==null || beans.size()==0){
            mRllisview.setVisibility(View.INVISIBLE);
            llEmpty.setVisibility(View.VISIBLE);
            mTvNoContent.setText("您暂时没有能添加的好友~");
            return;
        }
        llEmpty.setVisibility(View.GONE);
        bar.setVisibility(View.VISIBLE);

        this.datas.clear();
        this.datas.addAll(beans);
        setLetter(datas);
        if(mAdapter==null){
            mAdapter = new IMCreatGroupAdapter(this,datas);
            mlistView.setAdapter(mAdapter);
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



    /**
     * 邀请群聊
     *
     */
    private void addGroupHttpData(List<IMPersonBean> choosedList) {
        final IMBetGetBean data = new IMBetGetBean();
        data.setGroupId(groupId);
        List<String>Ids=new ArrayList<>();
        for (int i = 0; i < choosedList.size(); i++) {
            Ids.add(choosedList.get(i).getMemberId());
        }
        data.setMemberIdList(Ids);
        String json = new Gson().toJson(data);
        Log.e("-邀请群聊---",json);
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json);
        IMHttpsService.addMemberMemberJson(body, new IMHttpResultObserver<String>() {
            @Override
            public void onSuccess(String data, String message) {
                dismissLoadingDialog();
                EventBus.getDefault().post(new IMMessageAddGroupInforEvent());
                mAdapter.notifyDataSetChanged();
                finish();
            }

            @Override
            public void _onError(Throwable e) {
                dismissLoadingDialog();
                Toast.makeText(IMCreatGroupActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void _onErrorLocal(Throwable e, String message, String code) {
                dismissLoadingDialog();
                Toast.makeText(IMCreatGroupActivity.this, message, Toast.LENGTH_SHORT).show();
            }
        });
    }


    /**
     * 创建群聊
     *  {"code":"OK","data":null,"message":"成功"}
     */
    private void CreatGroupHttpData( List<IMPersonBean> choosedList) {
        IMHttpCommonBean bean = new IMHttpCommonBean();
        StringBuffer sb=new StringBuffer();
        List<String>data=new ArrayList<>();
        List<String>urls=new ArrayList<>();
        data.add(IMSManager.getMyUseId());
        urls.add(IMSManager.getMyHeadView());

        sb.append( IMSManager.getMyNickName()+"、");
        for (int i = 0; i < choosedList.size(); i++) {
            data.add(choosedList.get(i).getMemberId());
            urls.add(choosedList.get(i).getAvatar());
            if(i==choosedList.size()-1){
                sb.append(choosedList.get(i).getNickName());
            }else {
                sb.append(choosedList.get(i).getNickName()+"、");
            }
        }
        String name=null;
        if(sb.length()>15){
            name= sb.toString().substring(0, 16);
        }else {
            name=sb.toString();
        }
        bean.setMemberIdList(data);
        bean.setGroupName(name);
        httpgetGroup(bean);
//        if(urls.size()>9){
//            loadDingBitmap(bean,urls,9);
//        }else {
//            loadDingBitmap(bean,urls,urls.size());
//        }
    }


//    private void loadDingBitmap(IMHttpCommonBean bean,List<String>urls, int count) {
//        if(count>4){
//            CombineBitmap.init(IMCreatGroupActivity.this)
//                    .setLayoutManager(new WechatLayoutManager())
//                    .setSize(180)
//                    .setGap(2) //单个图片之间的距离，单位dp，默认0dp
//                    .setUrls(getUrls(urls,count))
//                    .setOnProgressListener(new OnProgressListener() {
//                        @Override
//                        public void onStart() {
//
//                        }
//
//                        @Override
//                        public void onComplete(Bitmap bitmap) {
//                            handleUpdataPickure(bean,bitmap);
//                        }
//                    })
//                    .build();
//        }else {
//            CombineBitmap.init(IMCreatGroupActivity.this)
//                    .setLayoutManager(new DingLayoutManager())
//                    .setSize(180)
//                    .setGap(2) //单个图片之间的距离，单位dp，默认0dp
//                    .setUrls(getUrls(urls,count))
//                    .setOnProgressListener(new OnProgressListener() {
//                        @Override
//                        public void onStart() {
//
//                        }
//
//                        @Override
//                        public void onComplete(Bitmap bitmap) {
//                            handleUpdataPickure(bean,bitmap);
//                        }
//                    })
//                    .build();
//        }
//
//    }
//
//    private String[] getUrls(List<String>datas,int count) {
//        String[] urls=datas.toArray(new String[count]);
//        return urls;
//    }


//    /**
//     * 上传头像生成url，然后设置成url
//     */
//
//    public void handleUpdataPickure(IMHttpCommonBean bean,Bitmap bitmap) {
//
//        final IMBetGetBean beans = new IMBetGetBean();
//        beans.setBase64Data(bitmapToBase64(bitmap));
//        String json = new Gson().toJson(beans);
//        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json);
//        IMHttpsService.getUpdataPicture(body, new IMHttpResultObserver<IMImageViewBean>() {
//            @Override
//            public void onSuccess(IMImageViewBean data, String message) {
//
//                IMLogUtil.d("上传图片成功==="+new Gson().toJson(data));
//                if(data==null){
//                    return;
//                }
//                bean.setGroupAvatar(data.getUrl());
//                httpgetGroup(bean);
//            }
//
//            @Override
//            public void _onError(Throwable e) {
//                dismissLoadingDialog();
//                Toast.makeText(IMCreatGroupActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
//            }
//
//            @Override
//            public void _onErrorLocal(Throwable e, String message, String code) {
//                dismissLoadingDialog();
//                Toast.makeText(IMCreatGroupActivity.this, message, Toast.LENGTH_SHORT).show();
//            }
//        });
//    }

    /**
     * 传入群信息，生成群
     */
    private Handler handler=new Handler();
    public  void httpgetGroup(IMHttpCommonBean bean){
        String json = new Gson().toJson(bean);
        Log.e("------","创建群聊"+json);
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"),json);
        IMHttpsService.creatNewGroupJson(body, new IMHttpResultObserver<IMHttpCommonBean>() {
            @Override
            public void onSuccess(IMHttpCommonBean data, String message) {
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        dismissLoadingDialog();
                        if(data==null){
                            return;
                        }
                        Intent intent=new Intent(IMCreatGroupActivity.this,IMGroupChatActivity.class);
                        intent.putExtra("group",  saveCommonBean(data));
                        startActivity(intent);
                        finish();
                    }
                },1000);
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
     * 保存新会话进数据库
     */
    private IMConversationBean saveCommonBean(IMHttpCommonBean data) {
        //保存新会话进数据库
        IMConversationBean bean=new IMConversationBean();
        bean.setConversationavatar(data.getGroupAvatar());
        bean.setConversationId(data.getGroupId());
        bean.setConversationName(data.getGroupName());
        bean.setGroupId(data.getGroupId());
        daoUtils.insertConversationData(bean);
        //保存新群进数据库(如果没有)
        IMGroupBean groupBean=new IMGroupBean();
        groupBean.setGroupName(data.getGroupName());
        groupBean.setGroupId(data.getGroupId());
        groupBean.setGroupAvatar(data.getGroupAvatar());
        IMGroupBean groupBean1 = daoUtils.queryGroupBean(data.getGroupId());
        if(groupBean1==null){
            daoUtils.insertGroupData(groupBean);
        }
        IMSManager.getInstance().getUnreadMessageNumber();
        return bean;
    }

    private static String bitmapToBase64(Bitmap bitmap) {
        String result = null;
        ByteArrayOutputStream baos = null;
        try {
            if (bitmap != null) {
                baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);

                baos.flush();
                baos.close();

                byte[] bitmapBytes = baos.toByteArray();
                result = Base64.encodeToString(bitmapBytes, Base64.DEFAULT);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (baos != null) {
                    baos.flush();
                    baos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return result;
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
        //移除群里面的人
        beans = daoUtils.queryAllMessageData();
        if(beans ==null || beans.size()==0){
            return;
        }
        if(!TextUtils.isEmpty(groupId)){
            List<IMGroupMemberBean> data = daoUtils.queryGroupAllMemberBean(groupId);
            List<IMPersonBean> list=new ArrayList<>();
            if(data!=null && data.size()>0){
                for (int i = 0; i < beans.size(); i++) {
                    for (int j = 0; j < data.size(); j++) {
                        if(beans.get(i).getMemberId().equals(data.get(j).getMemberId())){
                            list.add(beans.get(i));
                        }
                    }
                }
                if(list.size()>0){
                    beans.removeAll(list);
                }
            }
        }
        for (int i = 0; i < beans.size(); i++) {
            beans.get(i).setIschoosed(false);
        }
        //查询逻辑
        List<IMPersonBean> lists =new ArrayList<>();
        if(!TextUtils.isEmpty(toString)){
            Pattern pattern = Pattern.compile(toString);
            for (int i = 0; i <beans.size(); i++) {
                Matcher matcher = pattern.matcher(beans.get(i).getNickName());
                if(matcher.find()){
                    lists.add(beans.get(i));
                }
            }
        }else {
            lists.addAll(beans);
        }
        bindViewAdapter(lists);
    }
}
