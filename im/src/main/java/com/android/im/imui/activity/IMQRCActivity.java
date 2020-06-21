package com.android.im.imui.activity;


import android.content.Intent;
import android.graphics.Color;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Vibrator;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

import com.android.im.IMSManager;
import com.android.im.R;
import com.android.im.imbean.IMBetGetBean;
import com.android.im.imbean.IMQrcodeBean;
import com.android.im.imbean.SmallProgramBean;
import com.android.im.imnet.IMHttpResultObserver;
import com.android.im.imnet.http.IMHttpsService;
import com.android.im.imutils.IMChooseUtils;
import com.android.im.imutils.IMStatusBarUtil;
import com.android.nettylibrary.greendao.entity.IMPersonBean;
import com.android.nettylibrary.http.IMPersonBeans;
import com.android.nettylibrary.http.IMUserInforBean;
import com.android.nettylibrary.utils.IMLogUtil;
import com.blankj.utilcode.util.KeyboardUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.gson.Gson;
import com.zhihu.matisse.Matisse;

import java.io.IOException;
import java.util.List;

import cn.bingoogolapple.qrcode.core.QRCodeView;
import cn.bingoogolapple.qrcode.zxing.ZXingView;
import okhttp3.MediaType;
import okhttp3.RequestBody;

import static com.android.im.imnet.IMBaseConstant.JSON;

/**
 * 二维码扫描
 */
public class IMQRCActivity extends IMBaseActivity implements View.OnClickListener, QRCodeView.Delegate {
    private ImageView mIvFinish;
    private ImageView mIvRight;
    private TextView mTvTitle;
    private ZXingView mZXingView;
    private RelativeLayout rlPic;
    private RelativeLayout rlLight;
    private TextView tvLight;
    private static final int REQUEST_CODE_CHOOSE = 10003;
    private boolean isLitghting=false;//是否灯光打开
    private TextView tvContinue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrc);
        initView();
    }
    private void initView() {
        IMStatusBarUtil.setTranslucent(this,0);
        IMStatusBarUtil.setLightMode(this);
        mIvFinish = findViewById(R.id.tv_top_finish);
        mTvTitle = findViewById(R.id.tv_top_title);
        mIvRight = findViewById(R.id.iv_top_right);
        mZXingView = findViewById(R.id.zxingview);
        rlPic = findViewById(R.id.rl_pic);
        rlLight = findViewById(R.id.rl_light);
        tvContinue = findViewById(R.id.tv_continue);
        tvLight = findViewById(R.id.tv_light);
        mIvRight.setVisibility(View.INVISIBLE);
        mTvTitle.setText("扫一扫");
        mIvFinish.setOnClickListener(this);
        mIvRight.setOnClickListener(this);
        rlPic.setOnClickListener(this);
        rlLight.setOnClickListener(this);
        tvContinue.setOnClickListener(this);
        mZXingView.setOnClickListener(this);

        mZXingView.setDelegate(this);

    }

    @Override
    protected void onStart() {
        super.onStart();
        mZXingView.startCamera(); // 打开后置摄像头开始预览，但是并未开始识别
//        mZXingView.startCamera(Camera.CameraInfo.CAMERA_FACING_FRONT); // 打开前置摄像头开始预览，但是并未开始识别
        mZXingView.startSpotAndShowRect(); // 显示扫描框，并开始识别
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.iv_top_finish){
            finish();
        }else  if(v.getId()==R.id.tv_top_finish){
            finish();
        }else  if(v.getId()==R.id.rl_pic){//相册
            mZXingView.closeFlashlight();
            isLitghting=false;
            IMChooseUtils.choosePhotoHeadForResult(this, REQUEST_CODE_CHOOSE, 1);
        }else  if(v.getId()==R.id.rl_light){//灯光
            if(isLitghting){
                mZXingView.closeFlashlight();
                tvLight.setTextColor(getResources().getColor(R.color.color_cccccc));
                tvLight.setCompoundDrawablesWithIntrinsicBounds(null,
                        getResources().getDrawable(R.mipmap.sao_dark, null), null, null);
            }else {
                mZXingView.openFlashlight();
                tvLight.setTextColor(getResources().getColor(R.color.color_02AEFD));
                tvLight.setCompoundDrawablesWithIntrinsicBounds(null,
                        getResources().getDrawable(R.mipmap.sao_light, null),null , null);
            }
            isLitghting=!isLitghting;
        }else  if(v.getId()==R.id.tv_continue){//继续扫描
            tvContinue.setVisibility(View.GONE);
            mZXingView.startCamera();
            mZXingView.startSpotAndShowRect(); // 显示扫描框，并开始识别
        }else  if(v.getId()==R.id.zxingview){
            if(tvContinue.getVisibility()==View.VISIBLE){
                tvContinue.setVisibility(View.GONE);
                mZXingView.startCamera();
                mZXingView.startSpotAndShowRect();
            }
        }
    }



    @Override
    protected void onStop() {
        mZXingView.stopCamera(); // 关闭摄像头预览，并且隐藏扫描框
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        mZXingView.onDestroy(); // 销毁二维码扫描控件
        super.onDestroy();
    }
    private void vibrate() {
        Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        vibrator.vibrate(200);
    }

    @Override
    public void onScanQRCodeSuccess(String result) {
       IMLogUtil.d("WOLF", "result:" + result);
        mZXingView.stopCamera();
        if(!TextUtils.isEmpty(result)){
            vibrate();
            playSong();
            try{
                IMQrcodeBean bean = new Gson().fromJson(result, IMQrcodeBean.class);
                if(bean.getType().equals("card")){
                    getPersonInfor(bean.getCodeId());
                }else if(bean.getType().equals("sp")){
                    getProgramDetails(bean.getCodeId());
                }
            }catch (Exception e){
                ToastUtils.showShort("无法识别二维码");
                return;
            }

        }else {
            tvContinue.setVisibility(View.VISIBLE);
            ToastUtils.showShort("未发现可用二维码");
//            mZXingView.startCamera();
//            mZXingView.startSpotAndShowRect();
        }

    }

    /**
     * 小程序详情
     */
    private void getProgramDetails(String programId) {
        showLoadingDialog();
        final IMBetGetBean bean = new IMBetGetBean();
        bean.setProgramId(programId);
        String json = new Gson().toJson(bean);
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json);
        IMHttpsService.getProgramDetails(body, new IMHttpResultObserver<SmallProgramBean>() {
            @Override
            public void onSuccess(SmallProgramBean data, String message) {
                dismissLoadingDialog();
//                Intent intent = new Intent(IMQRCActivity.this, SmallProgramActivity.class);
//                intent.putExtra("data", data);
//                startActivity(intent);
                new SmallProgramActivity().toActivity(IMQRCActivity.this,data);
                finish();
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

    private void playSong() {
        SoundPool soundPool;
        //实例化SoundPool

        //sdk版本21是SoundPool 的一个分水岭
        if (Build.VERSION.SDK_INT >= 21) {
            SoundPool.Builder builder = new SoundPool.Builder();
            //传入最多播放音频数量,
            builder.setMaxStreams(1);
            //AudioAttributes是一个封装音频各种属性的方法
            AudioAttributes.Builder attrBuilder = new AudioAttributes.Builder();
            //设置音频流的合适的属性
            attrBuilder.setLegacyStreamType(AudioManager.STREAM_MUSIC);
            //加载一个AudioAttributes
            builder.setAudioAttributes(attrBuilder.build());
            soundPool = builder.build();
        } else {
            /**
             * 第一个参数：int maxStreams：SoundPool对象的最大并发流数
             * 第二个参数：int streamType：AudioManager中描述的音频流类型
             *第三个参数：int srcQuality：采样率转换器的质量。 目前没有效果。 使用0作为默认值。
             */
            soundPool = new SoundPool(1, AudioManager.STREAM_MUSIC, 0);
        }

        //可以通过四种途径来记载一个音频资源：
        //1.通过一个AssetFileDescriptor对象
        //int load(AssetFileDescriptor afd, int priority)
        //2.通过一个资源ID
        //int load(Context context, int resId, int priority)
        //3.通过指定的路径加载
        //int load(String path, int priority)
        //4.通过FileDescriptor加载
        //int load(FileDescriptor fd, long offset, long length, int priority)
        //声音ID 加载音频资源,这里用的是第二种，第三个参数为priority，声音的优先级*API中指出，priority参数目前没有效果，建议设置为1。
        final int voiceId = soundPool.load(this,R.raw.bibi , 1);
        //异步需要等待加载完成，音频才能播放成功
        soundPool.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
            @Override
            public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
                if (status == 0) {
                    //第一个参数soundID
                    //第二个参数leftVolume为左侧音量值（范围= 0.0到1.0）
                    //第三个参数rightVolume为右的音量值（范围= 0.0到1.0）
                    //第四个参数priority 为流的优先级，值越大优先级高，影响当同时播放数量超出了最大支持数时SoundPool对该流的处理
                    //第五个参数loop 为音频重复播放次数，0为值播放一次，-1为无限循环，其他值为播放loop+1次
                    //第六个参数 rate为播放的速率，范围0.5-2.0(0.5为一半速率，1.0为正常速率，2.0为两倍速率)
                    soundPool.play(voiceId, 1, 1, 1, 0, 1);
                }
            }
        });
    }

    /**
     * 选择图片处理
     */
    List<String> mSelected;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_CHOOSE && resultCode == RESULT_OK) {
            mSelected = Matisse.obtainPathResult(data);
            if (mSelected == null && mSelected.size() == 0) {
                return;
            }
            mZXingView.decodeQRCode(mSelected.get(0));
        }
    }


    @Override
    public void onCameraAmbientBrightnessChanged(boolean isDark) {
        // 这里是通过修改提示文案来展示环境是否过暗的状态，接入方也可以根据 isDark 的值来实现其他交互效果
        String tipText = mZXingView.getScanBoxView().getTipText();
        String ambientBrightnessTip = "\n环境过暗，请打开闪光灯";
        if (isDark) {
            if (!tipText.contains(ambientBrightnessTip)) {
                mZXingView.getScanBoxView().setTipText(tipText + ambientBrightnessTip);
            }
        } else {
            if (tipText.contains(ambientBrightnessTip)) {
                tipText = tipText.substring(0, tipText.indexOf(ambientBrightnessTip));
                mZXingView.getScanBoxView().setTipText(tipText);
            }
        }
    }

    @Override
    public void onScanQRCodeOpenCameraError() {
        Log.e("WOLF", "打开相机出错");
    }

    /**
     * 通过id添加好友
     */
    private void getPersonInfor(String Id) {
        showLoadingDialog();
        IMPersonBeans bean = new IMPersonBeans();
        bean.setCustomerId(Id);
        String json = new Gson().toJson(bean);
        RequestBody body = RequestBody.create(JSON, json);
        com.google.android.exoplayer2.util.Log.e("----",json);
        IMHttpsService.GetMemberInforJson(body,new IMHttpResultObserver<IMUserInforBean.UserInforData>() {
            @Override
            public void onSuccess(IMUserInforBean.UserInforData data, String message) {
                dismissLoadingDialog();
                if(data==null){//内容非ID
                    tvContinue.setVisibility(View.VISIBLE);
                    return;
                }
                IMPersonBean bean=new IMPersonBean();
                bean.setCustomerId(data.getCustomerId());
                bean.setMemberId(data.getCustomerId());
                bean.setAvatar(data.getAvatar());
                bean.setNickName(data.getNickName());
                Intent intent=new Intent(IMQRCActivity.this,IMNewFriendInforlActivity.class);
                intent.putExtra("bean",bean);
                startActivity(intent);
                finish();

            }

            @Override
            public void _onError(Throwable e) {
                dismissLoadingDialog();
                Toast.makeText(IMQRCActivity.this,e.getMessage(), Toast.LENGTH_SHORT).show();
                tvContinue.setVisibility(View.VISIBLE);
            }

            @Override
            public void _onErrorLocal(Throwable e, String message, String code) {
                dismissLoadingDialog();
                Toast.makeText(IMQRCActivity.this,message, Toast.LENGTH_SHORT).show();
                tvContinue.setVisibility(View.VISIBLE);
            }
        });
    }
}
