package com.rhby.cailexun.dialog;

import android.app.AlertDialog;
import android.content.Context;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.im.imview.dialog.IMGroupOwnerDiglog;
import com.android.nettylibrary.utils.IMDensityUtil;
import com.rhby.cailexun.R;

public class IMPersonalDiglog implements TextWatcher {
    private Context context;
    private AlertDialog shareDialog;
    private TextView mTvTitle;
    private TextView mTvCancle;
    private EditText mTvContent;
    private TextView mTvNumber;
    private TextView mTvBtn;
    private boolean  isnames;
    public IMPersonalDiglog(Context context) {
        this.context=context;
    }
    public void showPersonalDiglog(boolean isname, String content, OnFinishListener onFinishListener) {
        AlertDialog.Builder builder=new AlertDialog.Builder(context);
        final View dialogView =  View.inflate(context, R.layout.layout_dialog_personal, null);
        mTvTitle = dialogView.findViewById(R.id.im_tv_title);
        mTvContent = dialogView.findViewById(R.id.im_et_content);
        mTvNumber = dialogView.findViewById(R.id.im_tv_number);
        mTvBtn = dialogView.findViewById(R.id.im_tv_btn);
        mTvCancle=dialogView.findViewById(R.id.im_tv_cancle);
        isnames=isname;
        if(isname){
            mTvTitle.setText("昵称");
            mTvContent.setHint("请输入你的昵称");
            mTvNumber.setText(content.length()+"/10");
            mTvContent.setFilters(new InputFilter[]{new InputFilter.LengthFilter(10)});
        }else {
            mTvTitle.setText("个性签名");
            mTvContent.setHint("请输入你的个性签名");
            mTvNumber.setText(content.length()+"/20");
            mTvContent.setFilters(new InputFilter[]{new InputFilter.LengthFilter(20)});
        }
        mTvContent.setText(content);
        mTvContent.addTextChangedListener(this);
        mTvContent.setSelection(mTvContent.getText().length());
        mTvCancle.setOnClickListener(v -> shareDialog.dismiss());
        mTvBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String s = mTvContent.getText().toString();
                if(TextUtils.isEmpty(s)){
                    Toast.makeText(context, "内容不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                shareDialog.dismiss();
                onFinishListener.setOnFinishListener(isname,s);
            }
        });
        builder.setView(dialogView);
        shareDialog =  builder.show();

        Window window = shareDialog.getWindow();
        window.setWindowAnimations(R.style.ActionSheetDialogAnimations);  //添加动画

        WindowManager.LayoutParams params = window.getAttributes();
        params.width = IMDensityUtil.getScreenWidth(context)- IMDensityUtil.dip2px(context,70);
        shareDialog.getWindow().setAttributes(params);
        shareDialog.getWindow().setBackgroundDrawable(context.getResources().getDrawable(R.drawable.im_shape__bg));
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
            if(isnames){
                mTvNumber.setText(s.toString().length()+"/10");
            }else {
                mTvNumber.setText(s.toString().length()+"/20");
            }
        }else {
            if(isnames){
                mTvNumber.setText("0/10");
            }else {
                mTvNumber.setText("0/20");
            }
        }
    }


    public  interface  OnFinishListener{
        void   setOnFinishListener(Boolean isname,String content);
    }
    private IMGroupOwnerDiglog.OnFinishListener onFinishListener;

    public void setOnFinishListener(IMGroupOwnerDiglog.OnFinishListener onFinishListener) {
        this.onFinishListener = onFinishListener;
    }
}