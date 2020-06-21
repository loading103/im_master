package com.android.im.imview.dialog;

import android.app.AlertDialog;
import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.im.R;
import com.android.nettylibrary.utils.IMDensityUtil;

public class IMPushMessageDiglog{
    private Context context;
    private AlertDialog shareDialog;
    private TextView mTvTitle;
    private TextView mTvContent;
    private TextView mTvBtn;
    public boolean   isnames;
    public IMPushMessageDiglog(Context context) {
        this.context=context;
    }
    public void showPersonalDiglog(String title, String content, View.OnClickListener onFinishListener) {
        AlertDialog.Builder builder=new AlertDialog.Builder(context);
        final View dialogView =  View.inflate(context, R.layout.layout_dialog_push, null);
        mTvTitle = dialogView.findViewById(R.id.im_tv_title);
        mTvContent = dialogView.findViewById(R.id.im_tv_content);
        mTvBtn = dialogView.findViewById(R.id.tv_ensure);
        if(!TextUtils.isEmpty(title)){
            mTvTitle.setText(title);
        }
        if(!TextUtils.isEmpty(content)){
            mTvContent.setText(content);
        }
        mTvBtn.setOnClickListener(onFinishListener);
        builder.setView(dialogView);
        builder.setCancelable(false);
        shareDialog =  builder.show();

        Window window = shareDialog.getWindow();
        window.setWindowAnimations(R.style.ActionSheetDialogAnimations);  //添加动画

        WindowManager.LayoutParams params = window.getAttributes();
        params.width = IMDensityUtil.getScreenWidth(context)- IMDensityUtil.dip2px(context,70);
        shareDialog.getWindow().setAttributes(params);
        shareDialog.getWindow().setBackgroundDrawable(context.getResources().getDrawable(R.drawable.im_shape__bg));
    }
    public void dissmissPushDiglog(){
        if(shareDialog!=null && shareDialog.isShowing()){
            shareDialog.dismiss();
        }
    }

}