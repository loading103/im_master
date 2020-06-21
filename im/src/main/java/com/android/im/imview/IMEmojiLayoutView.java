package com.android.im.imview;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.im.R;
import com.android.im.imemoji.EmotionKeyboard;
import com.android.im.imemoji.EmotionLayout;
import com.android.im.imemoji.IEmotionSelectedListener;
import com.android.im.imutils.IMStopClickFast;
import com.android.nettylibrary.utils.IMLogUtil;
import com.blankj.utilcode.util.NetworkUtils;
import com.view.MP3RecordView;

public class IMEmojiLayoutView extends LinearLayout implements View.OnClickListener, IEmotionSelectedListener {
    RelativeLayout mRlAlbum;
    RelativeLayout mRlredPack;
    RelativeLayout mRlVideo;
    RelativeLayout mRlShare;
    Context mcontext;
    ImageView mIvAudio;
    IMMsgEditText mEtContent;
    TextView  mTvBlack;
    ImageView mIvEmo;
    ImageView mIvMore;
    Button mBtnSend;
    FrameLayout mFlEmotionView;
    EmotionLayout mElEmotion;
    LinearLayout mLlMore;
    RelativeLayout mLlContain;
    TextView mBtnAudio;

    private EmotionKeyboard mEmotionKeyboard;

    private boolean isgroup=false;//判断是群聊界面还是在单聊界面
    public Activity activity;
    public static final int TYPE_SEND_MESSAGE=0;
    public static final int TYPE_CHOOSE_PHOTO=1;
    public static final int TYPE_SHARE_MESSAGE=2;
    public static final int TYPE_SEND_REDPICK=3;
    public static final int TYPE_SEND_VIDEO=4;
    public static final int TYPE_PlAY=5;
    public static final int TYPE_SET_TOP=6;//把消息推至底部
    public static final int TYPE_SET_AT=7;//监听@
    public IMEmojiLayoutView(Context context) {
        this(context,null);
    }

    public IMEmojiLayoutView(Context context, AttributeSet attrs) {
        this(context,attrs,0);
    }

    public IMEmojiLayoutView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mcontext=context;
        initView();
    }


    /**
     * 初始化界面元素
     */
    private void initView() {
        LayoutInflater.from(mcontext).inflate(R.layout.layout_im_emoji,this);
        mRlAlbum = findViewById(R.id.rl_Album);
        mRlredPack = findViewById(R.id.rl_redpack);
        mRlVideo = findViewById(R.id.rl_video);
        mRlShare= findViewById(R.id.rl_share);
        mIvAudio=findViewById(R.id.ivAudio);
        mEtContent=findViewById(R.id.etContent);
        mTvBlack=findViewById(R.id.etName);
        mIvEmo=findViewById(R.id.ivEmo);
        mIvMore=findViewById(R.id.ivMore);
        mBtnSend=findViewById(R.id.btnSend);
        mFlEmotionView=findViewById(R.id.flEmotionView);
        mElEmotion=findViewById(R.id.elEmotion);
        mLlMore=findViewById(R.id.llMore);
        mLlContain=findViewById(R.id.im_rl_contain);
        mBtnAudio=findViewById(R.id.btnAudio);
        mElEmotion.attachEditText(mEtContent);
        if(isgroup){
            mLlContain.setVisibility(INVISIBLE);
        }else {
            mLlContain.setVisibility(INVISIBLE);
        }
        mIvAudio.setOnClickListener(this);
        mRlAlbum.setOnClickListener(this);
        mRlredPack.setOnClickListener(this);
        mRlVideo.setOnClickListener(this);
        mRlShare.setOnClickListener(this);
    }

    public IMMsgEditText getmEtContent() {
        return mEtContent;
    }
    public String getUserIdString(){
        String userIdString = mEtContent.getUserIdString();
        return userIdString;
    }

    /**
     * 传递view进来让键盘绑定在该view下
     */
    public void setContentView(View view,boolean isGroup) {
        if(isGroup){
            mLlContain.setVisibility(INVISIBLE);
        }else {
            mLlContain.setVisibility(INVISIBLE);
        }
        initListener(view);
        initEmotionKeyboard(view);
    }
    /**
     * 设置输入键盘参数
     */
    @SuppressLint("ClickableViewAccessibility")
    public int textNumber=0;
    public void initListener(View view) {
        mElEmotion.setEmotionSelectedListener(this);
        mElEmotion.setEmotionAddVisiable(true);
        mElEmotion.setEmotionSettingVisiable(true);
        view.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        closeBottomAndKeyboard();
                        break;
                }
                return false;
            }
        });
        mEtContent.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (mEtContent.getText().toString().trim().length() > 0) {
                    mBtnSend.setVisibility(View.VISIBLE);
                    mIvMore.setVisibility(View.GONE);
                } else {
                    mBtnSend.setVisibility(View.GONE);
                    mIvMore.setVisibility(View.VISIBLE);
                }

            }
            @Override
            public void afterTextChanged(Editable s) {
                if(TextUtils.isEmpty(s.toString())){
                    textNumber=0;
                    return;
                }
                String lastIndex = s.subSequence(s.length() - 1, s.length()).toString();
                if(!TextUtils.isEmpty(s.toString()) && lastIndex.equals("@") && textNumber<s.toString().length()){
                    if(listener!=null){
                        listener.onSendListener(null,TYPE_SET_AT);
                    }
                }
                textNumber=s.toString().length();
                Log.e("-textNumber---",textNumber+"");
            }

        });

        mBtnSend.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (IMStopClickFast.isSendFastClick()) {
                    if (listener != null) {
                        if(TextUtils.isEmpty(mEtContent.getText().toString())){
                            IMLogUtil.d("MyOwnTag:", "IMEmojiLayoutView ----消息为空" );
                            return;
                        }

                        listener.onSendListener(mEtContent, TYPE_SEND_MESSAGE);
                    }
                }else {
                }
            }
        });
    }
    /**
     * 设置输入键盘显示隐藏逻辑
     */
    private void initEmotionKeyboard(View view) {
        mEmotionKeyboard = EmotionKeyboard.with((Activity) mcontext);
        mEmotionKeyboard.bindToEditText(mEtContent);
        mEmotionKeyboard.bindToContent(view);
        mEmotionKeyboard.setEmotionLayout(mFlEmotionView);
        mEmotionKeyboard.bindToEmotionButton(mIvEmo, mIvMore);
        mIvAudio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mBtnAudio.isShown()) {
                    hideAudioButton();
                    mEtContent.requestFocus();
                    if (mEmotionKeyboard != null) {
                        mEmotionKeyboard.showSoftInput();
                    }
                } else {
                    if(listener!=null){
                        listener.onSendListener(null,TYPE_PlAY);
                    }
                    showAudioButton();
                    hideEmotionLayout();
                    hideMoreLayout();
                }
            }
        });
        mEmotionKeyboard.setOnEmotionButtonOnClickListener(new EmotionKeyboard.OnEmotionButtonOnClickListener() {
            @Override
            public boolean onEmotionButtonOnClickListener(View view) {
                    int i = view.getId();
                    if (i == R.id.ivEmo) {
                        if (!mElEmotion.isShown()) {
                            if (mLlMore.isShown()) {
                                showEmotionLayout();
                                hideMoreLayout();
                                hideAudioButton();
                                return true;
                            }
                        } else if (mElEmotion.isShown() && !mLlMore.isShown()) {
                            mIvEmo.setImageResource(R.mipmap.im_ic_cheat_emo);
                            return false;
                        }
                        showEmotionLayout();
                        hideMoreLayout();
                        hideAudioButton();
                        if(listener!=null){
                            listener.onSendListener(null,TYPE_SET_TOP);
                        }
                    } else if (i == R.id.ivMore) {
                        if (!mLlMore.isShown()) {
                            if (mElEmotion.isShown()) {
                                showMoreLayout();
                                hideEmotionLayout();
                                hideAudioButton();
                                return true;
                            }
                        }
                        showMoreLayout();
                        hideEmotionLayout();
                        hideAudioButton();
                        if(listener!=null){
                            listener.onSendListener(null,TYPE_SET_TOP);
                        }
                    }
                    return false;
            }
        });
    }


    public  void  showKeyboard(){
        if (mEmotionKeyboard != null) {
            mEmotionKeyboard.showSoftInput();
        }
    }
    private void showAudioButton() {
        mBtnAudio.setVisibility(View.VISIBLE);
        mEtContent.setVisibility(View.GONE);
        mIvAudio.setImageResource(R.mipmap.im_ic_cheat_keyboard);
        if (mFlEmotionView.isShown()) {
            if (mEmotionKeyboard != null) {
                mEmotionKeyboard.interceptBackPress();
            }
        } else {
            if (mEmotionKeyboard != null) {
                mEmotionKeyboard.hideSoftInput();
            }
        }
    }

    private void hideAudioButton() {
        mEtContent.setVisibility(View.VISIBLE);
        mBtnAudio.setVisibility(View.GONE);
        mIvAudio.setImageResource(R.mipmap.im_key_voice);
    }

    private void showEmotionLayout() {
        mElEmotion.setVisibility(View.VISIBLE);
        mIvEmo.setImageResource(R.mipmap.im_ic_cheat_keyboard);
    }

    private void hideEmotionLayout() {
        mElEmotion.setVisibility(View.GONE);
        mIvEmo.setImageResource(R.mipmap.im_ic_cheat_emo);
    }



    public void setContent(String ss) {
        mTvBlack.setText(ss);
    }

    private void showMoreLayout() {
        mLlMore.setVisibility(View.VISIBLE);
    }

    private void hideMoreLayout() {
        mLlMore.setVisibility(View.GONE);
    }

    private void closeBottomAndKeyboard() {
        mElEmotion.setVisibility(View.GONE);
        mLlMore.setVisibility(View.GONE);
        if (mEmotionKeyboard != null) {
            mEmotionKeyboard.interceptBackPress();
        }
    }

    /**
     * 点击返回键调用 如果打开了 先关闭键盘
     */
    public boolean onBackPressed() {
        if (mElEmotion.isShown() || mLlMore.isShown()) {
            mEmotionKeyboard.interceptBackPress();
            return false;
        }
        return  true;
    }

    public void  onResume(){
        mEtContent.clearFocus();
    }
    @Override
    public void onEmojiSelected(String key) {
    }
    @Override
    public void onStickerSelected(String categoryName, String stickerName, String stickerBitmapPath) {
    }
    @Override
    public void onClick(View v) {
        if(!NetworkUtils.isConnected()){
            Toast.makeText(mcontext, "网络不通，请检查网络设置", Toast.LENGTH_SHORT).show();
            return;
        }
        if (v.getId() == R.id.rl_Album) {
            if(hasPickurePerssion) {
                showTakePhotoDialog();
            }else {
                showEnsureDialog("温馨提示！", "你还没有发送图片的权限，请联系管理员开通权限");
            }
        } else if (v.getId() == R.id.rl_redpack) {
            if(hasReadPickPerssion){
                if(listener!=null){
                    listener.onSendListener(null,TYPE_SEND_REDPICK);
                }
            }else {
                showEnsureDialog("温馨提示！", "你还没有发送红包的权限，请联系管理员开通权限");
            }
        } else if (v.getId() == R.id.rl_video) {
            if(hasPickurePerssion){
                if(listener!=null){
                    listener.onSendListener(null,TYPE_SEND_VIDEO);
                }
            }else {
                showEnsureDialog("温馨提示！", "你还没有发送视频的权限，请联系管理员开通权限");
            }

        }else if (v.getId() == R.id.rl_share) {
            if(listener!=null){
                listener.onSendListener(null,TYPE_SHARE_MESSAGE);
            }
        }else if (v.getId() == R.id.ivAudio) {
            if(listener!=null){
                listener.onSendListener(null,TYPE_PlAY);
            }
        }
    }
    /**
     * 选择弹窗
     */
    public void  showTakePhotoDialog(){
        if(listener!=null){
            listener.onSendListener(null,TYPE_CHOOSE_PHOTO);
        }
    }

    public void setViewEnble(boolean enble) {
        mEtContent.setEnabled(enble);
        mBtnSend.setEnabled(enble);
        mIvEmo.setEnabled(enble);
        mIvMore.setEnabled(enble);
        mLlMore.setEnabled(enble);
        mIvAudio.setEnabled(enble);
        if(enble){
            mTvBlack.setVisibility(GONE);
            mEtContent.setVisibility(VISIBLE);
        }else {
            mTvBlack.setVisibility(INVISIBLE);
            mEtContent.setVisibility(GONE);
        }
    }

    /**
     * 隐藏更多界面
     */
    public void hidellMore() {
        mEmotionKeyboard.hideSoftInput();
        mEmotionKeyboard.interceptBackPress();
    }

    public void setAudioRecord(MP3RecordView mViewRecord) {
        mViewRecord.setRootView(mBtnAudio);
    }

    /**
     * 发送消息回调
     */
    public  interface SendListener{
        void  onSendListener(EditText view, int type);
    }
    private SendListener listener;

    public void setOnSendListener(SendListener listener) {
        this.listener = listener;
    }


    /**
     * 传递view进来让键盘绑定在该view下
     */
    public  boolean hasPickurePerssion=true;
    public  boolean hasReadPickPerssion=true;
    public void setPicketView(boolean isVisivble) {
        hasPickurePerssion=isVisivble;
    }
    /**
     * 传递view进来让键盘绑定在该view下
     */
    public void setRedPackView(boolean isVisivble) {
        hasReadPickPerssion=isVisivble;
    }

    /**
     * 对话框
     */
    public void showEnsureDialog(String title, String message){

        final AlertDialog.Builder normalDialog =
                new AlertDialog.Builder(mcontext);
        normalDialog.setTitle(title);
        normalDialog.setMessage(message);
        normalDialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        // 显示
        normalDialog.show();
    }
}
