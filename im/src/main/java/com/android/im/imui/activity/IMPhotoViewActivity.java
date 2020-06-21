package com.android.im.imui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.TextView;

import androidx.viewpager.widget.ViewPager;

import com.android.im.R;
import com.android.im.imadapter.IMPhotoViewsPager;
import com.android.im.imutils.IMStatusBarUtil;
import com.android.im.imview.IMPhotoViewPager;
import com.android.nettylibrary.greendao.entity.IMConversationDetailBean;
import com.android.nettylibrary.greendao.greenutils.DaoUtils;
import com.android.nettylibrary.http.IMMessageDataJson;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class IMPhotoViewActivity  extends IMBaseActivity{

    private int currentPosition;
    private IMPhotoViewsPager adapter;
    private TextView mTvImageCount;
    private List<String> Urls;
    private IMPhotoViewPager  mViewPager;
    private DaoUtils daoUtils;

    private String url;
    private String type;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_im_photo_view);
        IMStatusBarUtil.setColor(this,getResources().getColor(R.color.black));
        initView();
        initData();
    }

    private void initView() {
        mViewPager=findViewById(R.id.mviewPager);
        mTvImageCount = (TextView) findViewById(R.id.page);
        daoUtils = DaoUtils.getInstance();
    }
    private void initData() {
        Intent intent = getIntent();
        url = intent.getStringExtra("url");
        type = intent.getStringExtra("type");
        String userid = intent.getStringExtra("userid");
        String groupid = intent.getStringExtra("groupid");
        Urls =new ArrayList<>();
        if(type==null){//多张张图片查看大图(url!=null,修改界面进来的，反之是聊天界面进来的)
            if(TextUtils.isEmpty(groupid) && !TextUtils.isEmpty(userid)){
                IMConversationDetailBean bean = (IMConversationDetailBean) intent.getSerializableExtra("bean");
                IMMessageDataJson json = new Gson().fromJson(bean.getData(), IMMessageDataJson.class);
                List<IMConversationDetailBean> imPersonDetailBeans = daoUtils.queryConversationDetailDataAccordField(userid);
                if(imPersonDetailBeans==null){
                    return;
                }
                for (int i = imPersonDetailBeans.size()-1; i >=0; i--) {
                    IMMessageDataJson imMessageDataJson = new Gson().fromJson(imPersonDetailBeans.get(i).getData(), IMMessageDataJson.class);
                    if(!imMessageDataJson.getType().equals("2000")){
                        continue;
                    }
                    Urls.addAll(imMessageDataJson.getImages());
                }
                for (int i = 0; i < Urls.size(); i++) {
                    if(Urls.get(i).equals(json.getImages().get(0))){
                        currentPosition=i;
                    }
                }
            }else {
                IMConversationDetailBean bean = (IMConversationDetailBean) intent.getSerializableExtra("bean");
                IMMessageDataJson json = new Gson().fromJson(bean.getData(), IMMessageDataJson.class);
                List<IMConversationDetailBean> imPersonDetailBeans = daoUtils.queryConversationDetailDataAccordField(groupid);
                if(imPersonDetailBeans==null){
                    return;
                }
                for (int i = imPersonDetailBeans.size()-1; i >=0; i--) {
                    IMMessageDataJson imMessageDataJson = new Gson().fromJson(imPersonDetailBeans.get(i).getData(), IMMessageDataJson.class);
                    if(!imMessageDataJson.getType().equals("2000")){
                        continue;
                    }
                    Urls.addAll(imMessageDataJson.getImages());
                }
                for (int i = 0; i < Urls.size(); i++) {
                    if(Urls.get(i).equals(json.getImages().get(0))){
                        currentPosition=i;
                    }
                }
            }
        }else {   //修改头像点进来单张图片查看大图
            Urls.add(url);
            currentPosition=0;
        }
        adapter = new IMPhotoViewsPager(Urls, this);
        mViewPager.setAdapter(adapter);
        mViewPager.setCurrentItem(currentPosition, false);
        mTvImageCount.setText(currentPosition+1 + "/" + Urls.size());
        mViewPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                currentPosition = Urls.size()-position;
                mTvImageCount.setText(currentPosition + 1 + "/" + Urls.size());
            }
        });
    }
}
