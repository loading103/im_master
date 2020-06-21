package com.android.im.imui.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.im.R;
import com.android.im.imeventbus.VideoBeanEvent;
import com.android.im.imutils.IMSavePhotoUtil;
import com.android.im.imutils.IMStatusBarUtil;
import com.cjt2325.cameralibrary.JCameraView;
import com.cjt2325.cameralibrary.listener.ClickListener;
import com.cjt2325.cameralibrary.listener.JCameraListener;

import org.greenrobot.eventbus.EventBus;

import java.io.File;


/**
 * Created by cxf on 2018/11/26.
 */

public class IMVideoRecordActivity extends AppCompatActivity implements JCameraListener{
    private JCameraView jCameraView;
    private static final int MAX_DURATIONS = 15000;//最大录制时间15s
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record_video);
        initView();
        initListener();
    }

    private void initListener() {
        jCameraView.setJCameraLisenter(this);
        jCameraView.setLeftClickListener(new ClickListener() {
            @Override
            public void onClick() {
                finish();
            }
        });
    }

    private void initView() {
        IMStatusBarUtil.setTranslucentForImageView(this, 0, null);
        jCameraView=findViewById(R.id.jCameraView);
        jCameraView.setSaveVideoPath(Environment.getExternalStorageDirectory().getPath() + File.separator + "JCamera");
    }

    @Override
    protected void onResume() {
        super.onResume();
        jCameraView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        jCameraView.onPause();
    }

    @Override
    public void captureSuccess(Bitmap bitmap) {
        String photoPath = IMSavePhotoUtil.saveBitmap(bitmap, null);
        EventBus.getDefault().post(new VideoBeanEvent(photoPath,null,false));
        finish();
    }

    @Override
    public void recordSuccess(String url, Bitmap firstFrame) {
        EventBus.getDefault().post(new VideoBeanEvent(url,firstFrame,true));
        finish();
    }

}
