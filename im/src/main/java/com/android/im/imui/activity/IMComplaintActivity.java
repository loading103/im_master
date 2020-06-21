package com.android.im.imui.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.im.IMSManager;
import com.android.im.R;
import com.android.im.imadapter.IMImageAdapter;
import com.android.im.imbean.IMBetGetBean;
import com.android.im.imbean.IMImageViewBean;
import com.android.im.imnet.IMHttpResultObserver;
import com.android.im.imnet.http.IMHttpsService;
import com.android.im.imutils.IMBase64ImageUtils;
import com.android.im.imutils.IMChooseUtils;
import com.android.im.imutils.IMEditSoftShowUtils;
import com.android.im.imutils.IMStatusBarUtil;
import com.android.nettylibrary.utils.IMLogUtil;
import com.google.gson.Gson;
import com.zhihu.matisse.Matisse;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import cc.shinichi.library.ImagePreview;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import top.zibin.luban.CompressionPredicate;
import top.zibin.luban.Luban;
import top.zibin.luban.OnCompressListener;

import static com.shuyu.gsyvideoplayer.utils.FileUtils.getPath;


public class IMComplaintActivity extends IMBaseActivity implements View.OnClickListener, TextWatcher {
    private ImageView mIvFinish;
    private TextView mTvTitle;
    private TextView mTvEnsure;
    private RecyclerView mRecycleView;
    private EditText mEtContent;
    private TextView mTvNumber;
    private TextView mIvNumber;
    private IMImageAdapter adapter;
    public static final int REQUEST_CODE_CHOOSES = 10001;
    private List<String> picPaths = new ArrayList<String>();
    private String groupId;
    private String personId;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_im_complete);
        initView();
        initData();
    }
    private void initView() {
        IMStatusBarUtil.setTranslucent(this,0);
        IMStatusBarUtil.setLightMode(this);
        mIvFinish = findViewById(R.id.tv_top_finish);
        mTvTitle = findViewById(R.id.tv_top_title);
        mRecycleView = findViewById(R.id.recycle);
        mTvEnsure = findViewById(R.id.tv_ensure);
        mEtContent= findViewById(R.id.im_et_content);
        mTvNumber= findViewById(R.id.im_tv_number);
        mIvNumber= findViewById(R.id.im_iv_number);
        mIvFinish.setVisibility(View.VISIBLE);
        mIvFinish.setOnClickListener(this);
        mTvEnsure.setOnClickListener(this);
        mEtContent.addTextChangedListener(this);
        mTvTitle.setText("投诉");
    }
    @SuppressLint("WrongConstant")
    private void initData() {
        personId=getIntent().getStringExtra("personId");
        groupId=getIntent().getStringExtra("groupId");
        GridLayoutManager manager = new GridLayoutManager(this, 4);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecycleView.setLayoutManager(manager);
        adapter = new IMImageAdapter(picPaths,IMComplaintActivity.this);
        mRecycleView.setAdapter(adapter);
        adapter.setChoosePicListener(new IMImageAdapter.ChoosePicListener() {
            @Override
            public void onHanlerAddPic(int position, int type) {
                switch (type){
                    case 0:    //添加图片
                        IMChooseUtils.choosePhotoForResult(IMComplaintActivity.this,REQUEST_CODE_CHOOSES,4-picPaths.size(),true);
                        break;
                    case 1:   //删除图片
                        picPaths.remove(position);
                        mIvNumber.setText(picPaths.size()+"/4");
                        adapter.notifyDataSetChanged();
                        if(picPaths.size()==0){
                            mTvEnsure.setBackground(getResources().getDrawable(R.drawable.im_shape_bg_gry15));
                        }
                        break;
                    case 2:   //查看图片
//                        Intent intent= new Intent(IMComplaintActivity.this, IMPhotoViewActivity.class);
//                        intent.putExtra("url", picPaths.get(position));
//                        intent.putExtra("type", "1");
//                        startActivity(intent);
                        ImagePreview.getInstance()
                                // 上下文，必须是activity，不需要担心内存泄漏，本框架已经处理好；
                                .setContext(IMComplaintActivity.this)

                                .setIndex(0)

                                .setLoadStrategy(ImagePreview.LoadStrategy.Default)
                                // 缩放动画时长，单位ms
                                .setZoomTransitionDuration(1000)

                                .setImageList(picPaths)
                                //设置是否显示下载按钮
                                .setShowDownButton(false)
                                // 开启预览
                                .start();


                        break;
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.tv_top_finish){
            finish();
        }else if(v.getId()==R.id.tv_ensure){
            if(TextUtils.isEmpty(mEtContent.getText().toString())){
                Toast.makeText(this, "请输入举报内容", Toast.LENGTH_SHORT).show();
                return;
            }
            if(picPaths.size()==0){
                Toast.makeText(this, "请上传图片", Toast.LENGTH_SHORT).show();
                return;
            }
            handlerCommitContent();
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_CHOOSES && resultCode == RESULT_OK) {
            List<String>  mSelected = Matisse.obtainPathResult(data);
            if(mSelected==null && mSelected.size()==0){
                return;
            }
            handleCompressPickure(mSelected);
        }
    }

    /**
     * 上传图片资源(先压缩)
     */
    public  void  handleCompressPickure( final List<String> mSelected){
        Luban.with(this)
                .load(mSelected)
                .ignoreBy(100)
                .setTargetDir(getPath())
                .filter(new CompressionPredicate() {
                    @Override
                    public boolean apply(String path) {
                        return !(TextUtils.isEmpty(path) || path.toLowerCase().endsWith(".gif"));
                    }
                })
                .setCompressListener(new OnCompressListener() {
                    @Override
                    public void onStart() {
                    }

                    @Override
                    public void onSuccess(File file) {  //压缩成功上传
                        picPaths.add(file.getPath());
                        mIvNumber.setText(picPaths.size()+"/4");
                        if(TextUtils.isEmpty(mEtContent.getText().toString())){
                            mTvEnsure.setBackground(getResources().getDrawable(R.drawable.im_shape_bg_gry15));
                        }else {
                            mTvEnsure.setBackground(getResources().getDrawable(R.drawable.im_shape_bg_blue15));
                        }

                        adapter.notifyDataSetChanged();
                    }
                    @Override
                    public void onError(Throwable e) {
                    }
                }).launch();
    }



    /**
     * 举报（上传成功之后要删除生成的压缩图）
     */
    private  List<String>urls=new ArrayList<>();
    private void handlerCommitContent() {
        urls.clear();
        showLoadingDialog();
        for (int i = 0; i < picPaths.size(); i++) {
            final IMBetGetBean bean = new IMBetGetBean();
            bean.setBase64Data(IMBase64ImageUtils.imageToBase64(picPaths.get(i)));
            String json = new Gson().toJson(bean);
            RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json);
            int finalI = i;
            IMHttpsService.getUpdataPicture(body, new IMHttpResultObserver<IMImageViewBean>() {
                @Override
                public void onSuccess(IMImageViewBean data, String message) {
                    if(data==null){
                        return;
                    }
                    urls.add(data.getUrl());
                    if(finalI ==picPaths.size()-1){
                        Log.e("上传图片完毕，开始举报","删除图片");
                        dismissLoadingDialog();
                        ComplaintPersonDate(urls);

                    }
                }

                @Override
                public void _onError(Throwable e) {
                    dismissLoadingDialog();
                    Toast.makeText(IMComplaintActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }

                @Override
                public void _onErrorLocal(Throwable e, String message, String code) {
                    dismissLoadingDialog();
                    Toast.makeText(IMComplaintActivity.this, message, Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    /**
     * 删除本地生成的压缩图片
     */
    public void deleteFilePickDate() {
        for (int i = 0; i < picPaths.size(); i++) {
            File file = new File(picPaths.get(i));
            //删除系统缩略图
            getContentResolver().delete(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, MediaStore.Images.Media.DATA + "=?", new String[]{picPaths.get(i)});
            //删除手机中图片
            file.delete();
        }
    }
    /**
     * 举报用户
     */
    public void ComplaintPersonDate(List<String>  urls) {
        final IMBetGetBean bean = new IMBetGetBean();
        if(!TextUtils.isEmpty(groupId)){
            bean.setRespondentsObjId(groupId);
            bean.setComplaintType("2");
        }else {
            bean.setRespondentsObjId(personId);
            bean.setComplaintType("1");
        }
        if(!TextUtils.isEmpty(mEtContent.getText().toString())){
            bean.setComplaintContent(mEtContent.getText().toString());
        }
        bean.setComplaintImageList(urls);
        String json = new Gson().toJson(bean);
        Log.e("-举报用户--",json);
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json);
        IMHttpsService.getComplaintPerson(body, new IMHttpResultObserver<Object>() {
            @Override
            public void onSuccess(Object s, String message) {
                dismissLoadingDialog();
                deleteFilePickDate();
                Toast.makeText(IMComplaintActivity.this, message, Toast.LENGTH_SHORT).show();
                finish();
            }


            @Override
            public void _onError(Throwable e) {
                dismissLoadingDialog();
                Toast.makeText(IMComplaintActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void _onErrorLocal(Throwable e, String message, String code) {
                dismissLoadingDialog();
                Toast.makeText(IMComplaintActivity.this, message, Toast.LENGTH_SHORT).show();
            }
        });
    }

















    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        if(!TextUtils.isEmpty(s.toString())){
            mTvNumber.setText(s.toString().length()+"/500");
            if(picPaths.size()==0){
                mTvEnsure.setBackground(getResources().getDrawable(R.drawable.im_shape_bg_gry15));
            }else {
                mTvEnsure.setBackground(getResources().getDrawable(R.drawable.im_shape_bg_blue15));
            }
        }else {
            mTvNumber.setText("0/500");
            mTvEnsure.setBackground(getResources().getDrawable(R.drawable.im_shape_bg_gry15));
        }

    }
    //点击EditText之外的区域隐藏键盘
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (IMEditSoftShowUtils.isShouldHideInput(v, ev)) {
                if(IMEditSoftShowUtils.hideInputMethod(this, v)) {
                    return true; //隐藏键盘时，其他控件不响应点击事件==》注释则不拦截点击事件
                }
            }
        }
        return super.dispatchTouchEvent(ev);
    }
}
