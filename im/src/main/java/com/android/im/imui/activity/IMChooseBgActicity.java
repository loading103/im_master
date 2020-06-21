package com.android.im.imui.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.im.R;
import com.android.im.imbean.MyBgBeanTotleData;
import com.android.im.imutils.IMImageLoadUtil;
import com.android.im.imutils.IMStatusBarUtil;
import com.android.nettylibrary.greendao.entity.IMChatBgBean;
import com.android.nettylibrary.greendao.greenutils.DaoUtils;

public class IMChooseBgActicity extends IMBaseActivity implements View.OnClickListener {
    private  ImageView mIvBg;
    private  TextView mTvChoose;
    private MyBgBeanTotleData bean;
    private IMChatBgBean bean1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_im_choose_bg);
        IMStatusBarUtil.setTranslucentForImageView(this,0,null);
        initView();
    }

    private void initView() {
        mIvBg = findViewById(R.id.iv_bg);
        mTvChoose = findViewById(R.id.tv_choose);
        mTvChoose.setOnClickListener(this);
        bean=(MyBgBeanTotleData)getIntent().getSerializableExtra("bean");
        bean1 = DaoUtils.getInstance().queryChatBgBean(bean.getPicurl());
        if(bean1==null){
            return;
        }
        if(bean1.getIschoose()){
            mTvChoose.setText("从聊天背景中移除");
            mTvChoose.setTextColor(getResources().getColor(R.color.red6));
        }else {
            mTvChoose.setText("加入聊天背景");
            mTvChoose.setTextColor(getResources().getColor(R.color.color_999999));
        }
        IMImageLoadUtil.ImageLoad(this, bean.getPicurl(),mIvBg);
    }
    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.tv_choose) {
            bean1= DaoUtils.getInstance().queryChatBgBean(bean.getPicurl());
            if(bean1==null){
                return;
            }
            if(bean1.getIschoose()){
                mTvChoose.setText("加入聊天背景");
                mTvChoose.setTextColor(getResources().getColor(R.color.color_999999));
            }else {
                mTvChoose.setText("从聊天背景中移除");
                mTvChoose.setTextColor(getResources().getColor(R.color.red6));
            }
            bean1.setIschoose(!bean1.getIschoose());
            DaoUtils.getInstance().updateChatBgData(bean1);
        }
    }
}
