package com.android.im.imview.dialog;

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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.im.R;
import com.android.nettylibrary.utils.IMDensityUtil;

public class IMGroupOwnerDiglog implements TextWatcher {
    private Context context;
    private AlertDialog shareDialog;
    private TextView mTvTitle;
    private TextView mTvCancle;
    private EditText mTvContent;
    private TextView mTvNumber;
    private TextView mTvBtn;
    public boolean   isnames;
    public IMGroupOwnerDiglog(Context context) {
        this.context=context;
    }
    public void showPersonalDiglog(boolean isname, String content, OnFinishListener onFinishListener) {
        AlertDialog.Builder builder=new AlertDialog.Builder(context);
        final View dialogView =  View.inflate(context, R.layout.layout_dialog_group_owner, null);
        mTvTitle = dialogView.findViewById(R.id.im_tv_title);
        mTvContent = dialogView.findViewById(R.id.im_et_content);
        mTvNumber = dialogView.findViewById(R.id.im_tv_number);
        mTvBtn = dialogView.findViewById(R.id.im_tv_btn);
        mTvCancle=dialogView.findViewById(R.id.im_tv_cancle);
        isnames=isname;
        if(isname){
            mTvTitle.setText("群聊名称");
            mTvContent.setHint("请输入你的群聊名称");
            mTvNumber.setText("0/20");
            mTvContent.setFilters(new InputFilter[]{new InputFilter.LengthFilter(20)});
        }else {
            mTvTitle.setText("聊公告");
            mTvContent.setHint("请输入你的群聊公告");
            mTvNumber.setText("0/150");
            mTvContent.setFilters(new InputFilter[]{new InputFilter.LengthFilter(150)});
        }
        if(!TextUtils.isEmpty(content)){
            mTvContent.setText(content);
            if(isname){
                mTvNumber.setText(content.length()+"/20");
            }else {
                mTvNumber.setText(content.length()+"/150");
            }
        }
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
                mTvNumber.setText(s.toString().length()+"/20");
            }else {
                mTvNumber.setText(s.toString().length()+"/150");
            }
        }else {
            if(isnames){
                mTvNumber.setText("0/20");
            }else {
                mTvNumber.setText("0/150");
            }
        }
    }




    public  interface  OnFinishListener{
      void   setOnFinishListener(Boolean isname,String content);
    }
    private  OnFinishListener onFinishListener;

    public void setOnFinishListener(OnFinishListener onFinishListener) {
        this.onFinishListener = onFinishListener;
    }
}