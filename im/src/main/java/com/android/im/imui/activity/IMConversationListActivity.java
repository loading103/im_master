//package com.android.im.ui.activity;
//
//import android.os.Bundle;
//import android.support.annotation.Nullable;
//import android.support.v4.view.ViewPager;
//import android.view.View;
//import android.widget.AdapterView;
//import android.widget.CheckBox;
//import android.widget.ImageView;
//import android.widget.RelativeLayout;
//import android.widget.Spinner;
//import android.widget.Toast;
//
//import com.android.im.R;
//import com.android.im.adapter.IMFragmentAdapter;
//import com.android.im.adapter.IMPopWindowAdapter;
//import com.android.im.im.IMSConfig;
//import com.android.im.utils.DensityUtil;
//import com.android.im.utils.IMImageLoadUtil;
//import com.android.im.utils.IMPreferenceUtil;
//import com.android.im.utils.IMStatusBarUtil;
//
//import java.util.ArrayList;
//import java.util.List;
//
///**
// * 聊天列表界面（放在activity中）
// */
//
//public class IMConversationListActivity extends IMBaseActivity implements View.OnClickListener {
//
//    private ViewPager mViewpager;
//    private CheckBox mCb1;
//    private CheckBox mCb2;
//    private View mlin1;
//    private View mlin2;
//    private View mRed1;
//    private View mRed2;
//    private ImageView mIvBack;
//    private RelativeLayout mRlcontain;
//    private Spinner mSpinnerSelf;
//    @Override
//    public void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_im_conversationlist);
//        IMStatusBarUtil.setColor(this,getResources().getColor(R.color.blue_01D3FC));
//        initView();
//        initData();
//        IMFragmentAdapter fragmentAdapter= new IMFragmentAdapter(getSupportFragmentManager());
//        mViewpager.setAdapter(fragmentAdapter);
//        mViewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
//            @Override
//            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
//
//            }
//
//            @Override
//            public void onPageSelected(int position) {
//                Toast.makeText(getBaseContext(), ""+position, Toast.LENGTH_SHORT).show();
//            }
//
//            @Override
//            public void onPageScrollStateChanged(int state) {
//
//            }
//        });
//    }
//
//    private void initData() {
//        String headurl = IMPreferenceUtil.getPreference_String(IMSConfig.SAVE_HEADURL, "");
//        IMImageLoadUtil.ImageLoadCircle(this,headurl,mIvBack);
//    }
//
//    private void initView() {
//        mViewpager=findViewById(R.id.viewpager);
//        mCb1 = findViewById(R.id.im_cb_1);
//        mCb2 = findViewById(R.id.im_cb_2);
//        mlin1 = findViewById(R.id.im_lin_1);
//        mlin2 = findViewById(R.id.im_lin_2);
//        mRed1 = findViewById(R.id.im_red_1);
//        mRed2 = findViewById(R.id.im_red_2);
//        mIvBack=findViewById(R.id.im_iv_back);
//        mRlcontain=findViewById(R.id.im_rl_contain);
//        mSpinnerSelf=findViewById(R.id.sp_iv_add);
//        IMStatusBarUtil.setTranslucentForImageView(this, 0,mRlcontain);
//        mCb1.setOnClickListener(this);
//        mCb2.setOnClickListener(this);
//        initSpinnerView();
//    }
//
//    @Override
//    public void onClick(View v) {
//        if ( v.getId()== R.id.im_cb_1) {
//            tabOneChanged(0);
//        } else if ( v.getId() == R.id.im_cb_2) {
//            tabOneChanged(1);
//        }
//    }
//
//    /**
//     * 点击table变化
//     */
//    private void tabOneChanged(int type) {
//        if(type==0){
//            mViewpager.setCurrentItem(0);
//            mlin1.setVisibility(View.VISIBLE);
//            mlin2.setVisibility(View.GONE);
//            mCb2.setChecked(false);
//            mCb1.setChecked(true);
//        }else {
//            mCb2.setChecked(true);
//            mCb1.setChecked(false);
//            mlin1.setVisibility(View.GONE);
//            mlin2.setVisibility(View.VISIBLE);
//            mViewpager.setCurrentItem(1);
//        }
//    }
//
//    /**
//     * 设置spinner
//     */
//    public boolean isFirstClick=false;
//    public void initSpinnerView(){
//        List<String> dataList=new ArrayList<>();
//        dataList.add(getResources().getString(R.string.im_msg_setting));
//        dataList.add(getResources().getString(R.string.im_totle_readed));
//        IMPopWindowAdapter adapter = new IMPopWindowAdapter(this,dataList);
//        mSpinnerSelf.setAdapter(adapter);
//        mSpinnerSelf.setDropDownVerticalOffset(DensityUtil.dip2px(this,30));
//        mSpinnerSelf.setGravity(View.TEXT_ALIGNMENT_GRAVITY);
//        mSpinnerSelf.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
//                if(!isFirstClick){
//                    isFirstClick=true;
//                    return;
//                }
//                if(pos==0){
//                    Toast.makeText(IMConversationListActivity.this, "全部已读", Toast.LENGTH_SHORT).show();
//                }else {
//                    Toast.makeText(IMConversationListActivity.this, "消息设置", Toast.LENGTH_SHORT).show();
//                }
//            }
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//            }
//        });
//    }
//}
