package com.android.im.imui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Base64;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.im.R;
import com.android.im.imbean.IMBetGetBean;
import com.android.im.imbean.IMImageViewBean;
import com.android.im.imeventbus.IMImageViewUpdateEvent;
import com.android.im.imnet.IMHttpResultObserver;
import com.android.im.imnet.http.IMHttpsService;
import com.android.im.imutils.IMChooseUtils;
import com.android.im.imutils.IMImageLoadUtil;
import com.android.im.imutils.IMPersonUtils;
import com.android.im.imutils.IMStatusBarUtil;
import com.android.nettylibrary.IMSConfig;
import com.android.nettylibrary.http.IMUserInforBean;
import com.android.nettylibrary.utils.IMLogUtil;
import com.android.nettylibrary.utils.IMPreferenceUtil;
import com.google.gson.Gson;
import com.zhihu.matisse.Matisse;

import org.greenrobot.eventbus.EventBus;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import cc.shinichi.library.ImagePreview;
import okhttp3.MediaType;
import okhttp3.RequestBody;

public class IMUpdateMyInforActivity  extends IMBaseActivity implements View.OnClickListener {
    private  LinearLayout ll_contain;
    private  ImageView mIvHead;
    private  ImageView mIvleve;
    private  ImageView mIvBack;
    private  ImageView mIvEdit;
    private  TextView mTvName;
    private  TextView mTvContent;
    private  TextView mTvTitle;
    private  TextView mTvMz;
    private  TextView mTvType;
    private  TextView mTvNoTitle;
    private  TextView mTvNoMz;
    private  TextView mTvlevle;
    private  RelativeLayout mllmz;
    private  RelativeLayout mlltitle;
    private static final int REQUEST_CODE_CHOOSE = 10001;
    private String url=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_im_updata_infor);
        initView();
        initData();
    }

    private void initData() {
        IMHttpGetSelfInfor();
    }

    private void initView() {
        ll_contain=findViewById(R.id.ll_contain);
        IMStatusBarUtil.setTranslucentForImageView(this, 0,ll_contain);
        mIvHead=findViewById(R.id.im_iv_head);
        mIvleve=findViewById(R.id.im_iv_level);
        mIvBack=findViewById(R.id.iv_top_finish);
        mTvName=findViewById(R.id.im_tv_name);
        mTvContent=findViewById(R.id.im_tv_content);
        mTvTitle=findViewById(R.id.im_tv_title);
        mTvMz=findViewById(R.id.im_tv_mz);
        mTvType=findViewById(R.id.im_tv_type);
        mlltitle=findViewById(R.id.im_ll_title);
        mllmz=findViewById(R.id.im_ll_mz);
        mIvEdit=findViewById(R.id.im_iv_edit);
        mTvNoMz=findViewById(R.id.im_no_mz);
        mTvlevle=findViewById(R.id.im_no_levle);
        mTvNoTitle=findViewById(R.id.im_no_title);
        mIvBack.setOnClickListener(this);
        mIvHead.setOnClickListener(this);
        mIvEdit.setOnClickListener(this);
    }

    /**
     * 处理个人数据
     */
    public boolean isvisitor=false;//判断是不是游客
    private void HandlerDataView(IMUserInforBean.UserInforData data) {
        if(data.getType().equals("7")){
            isvisitor=true;
        }
        IMPersonUtils.setUseType(data.getType(),mTvType);
        if(isvisitor){
            mlltitle.setVisibility(View.GONE);
            mllmz.setVisibility(View.GONE);
            mIvleve.setVisibility(View.GONE);
            mTvNoTitle.setVisibility(View.VISIBLE);
            mTvNoMz.setVisibility(View.VISIBLE);
            mTvlevle.setVisibility(View.VISIBLE);
            mIvEdit.setVisibility(View.GONE);
        }else {
            mIvEdit.setVisibility(View.GONE);
            mTvlevle.setVisibility(View.GONE);
            mTvNoMz.setVisibility(View.GONE);
            mIvleve.setVisibility(View.GONE);
            mlltitle.setVisibility(View.VISIBLE);
            mllmz.setVisibility(View.VISIBLE);
            mIvleve.setVisibility(View.VISIBLE);

            IMPersonUtils.setVip(data.getLevel(),mIvleve);

            mIvEdit.setVisibility(View.VISIBLE);

            IMPersonUtils.setTitile(mlltitle,mTvNoTitle,mTvTitle,data.getTitle());
        }

        IMImageLoadUtil.ImageLoadCircle(this,data.getAvatar(),mIvHead);
        mTvName.setText(TextUtils.isEmpty(data.getNickName())?data.getUsername():data.getNickName());
        mTvContent.setText(TextUtils.isEmpty(data.getUsername())?"":data.getUsername());

    }

    /**
     * 获取个人数据
     */
    public void IMHttpGetSelfInfor()  {
        showLoadingDialog();
        IMHttpsService.GetSelfInforJson(new IMHttpResultObserver<IMUserInforBean.UserInforData>() {
            @Override
            public void onSuccess(IMUserInforBean.UserInforData data, String message) {
                dismissLoadingDialog();
                HandlerDataView(data);
                url=data.getAvatar();
                IMLogUtil.d("MyOwnTag:", "IMSNettyManager " +"(onFailure) " +"获取个人信息成功");
            }

            @Override
            public void _onError(Throwable e) {
                dismissLoadingDialog();
                Toast.makeText(IMUpdateMyInforActivity.this, "获取数据失败，请稍后再试", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void _onErrorLocal(Throwable e, String message, String code) {
                dismissLoadingDialog();
                Toast.makeText(IMUpdateMyInforActivity.this, "获取数据失败，请稍后再试", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.iv_top_finish){
            finish();
        }else if(v.getId()==R.id.im_iv_head){
//            Intent intent = new Intent(this, IMPhotoViewActivity.class);
//            intent.putExtra("url", url);
//            intent.putExtra("type", "1");
//            startActivity(intent);
            ImagePreview.getInstance()
                    // 上下文，必须是activity，不需要担心内存泄漏，本框架已经处理好；
                    .setContext(this)

                    .setIndex(0)

                    .setLoadStrategy(ImagePreview.LoadStrategy.Default)
                    // 缩放动画时长，单位ms
                    .setZoomTransitionDuration(1000)

                    .setImage(url)
                    //设置是否显示下载按钮
                    .setShowDownButton(false)
                    // 开启预览
                    .start();
        }else if(v.getId()==R.id.im_iv_edit){
            IMChooseUtils.choosePhotoHeadForResult(this,REQUEST_CODE_CHOOSE,1);
        }
    }

    /**
     * 选择图片处理
     */
    List<String> mSelected;
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_CHOOSE && resultCode == RESULT_OK) {   //选择图片成功之后上传
            mSelected = Matisse.obtainPathResult(data);
            if (mSelected == null && mSelected.size() == 0) {
                return;
            }
            updataPickture(mSelected);
        }
    }
    /**
     * 上传图片
     */
    private void updataPickture(List<String> mSelected) {
        for (int i = 0; i < mSelected.size(); i++) {
            showLoadingDialog();
            final IMBetGetBean bean = new IMBetGetBean();
            bean.setBase64Data(imageToBase64(mSelected.get(i)));
            String json = new Gson().toJson(bean);
            RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json);
            IMHttpsService.getUpdataPicture(body, new IMHttpResultObserver<IMImageViewBean>() {
                @Override
                public void onSuccess(IMImageViewBean data, String message) {
                    dismissLoadingDialog();
                    IMLogUtil.d("上传图片成功==="+new Gson().toJson(data));
                    UpdateHeadUrl(data);
                }

                @Override
                public void _onError(Throwable e) {
                    dismissLoadingDialog();
                    Toast.makeText(IMUpdateMyInforActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }

                @Override
                public void _onErrorLocal(Throwable e, String message, String code) {
                    dismissLoadingDialog();
                    Toast.makeText(IMUpdateMyInforActivity.this, message, Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
    /**
     * 修改头像
     */
    private void UpdateHeadUrl(final IMImageViewBean data) {
        if(data==null ){
            return;
        }
        final IMBetGetBean bean = new IMBetGetBean();
        url=data.getUrl();
        bean.setAvatar(data.getUrl());
        String json = new Gson().toJson(bean);
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json);
        IMHttpsService.getUpdataInfor(body, new IMHttpResultObserver<String>() {
            @Override
            public void onSuccess(String ss, String message) {
                IMImageLoadUtil.ImageLoadCircle(IMUpdateMyInforActivity.this,data.getUrl(),mIvHead);
                IMPreferenceUtil.setPreference_String(IMSConfig.SAVE_HEADURL,data.getUrl());
                EventBus.getDefault().post(new IMImageViewUpdateEvent(data.getUrl()));
            }

            @Override
            public void _onError(Throwable e) {
                Toast.makeText(IMUpdateMyInforActivity.this, "修改失败，请稍后再试", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void _onErrorLocal(Throwable e, String message, String code) {
                Toast.makeText(IMUpdateMyInforActivity.this, "修改失败，请稍后再试", Toast.LENGTH_SHORT).show();
            }
        });
    }
    /**
     * 将图片转换成Base64编码的字符串
     */
    private  String imageToBase64(String path){
        if(TextUtils.isEmpty(path)){
            return null;
        }
        InputStream is = null;
        byte[] data = null;
        String result = null;
        try{
            is = new FileInputStream(path);
            //创建一个字符流大小的数组。
            data = new byte[is.available()];
            //写入数组
            is.read(data);
            //用默认的编码格式进行编码
            result = Base64.encodeToString(data,Base64.DEFAULT);
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if(null !=is){
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }
        IMLogUtil.d("MyOwnTag:", "IMUpdateMyInforActivity ----" +result);
        return "data:image/png;base64,"+result;
    }

}
