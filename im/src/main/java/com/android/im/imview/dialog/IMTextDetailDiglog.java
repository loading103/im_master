package com.android.im.imview.dialog;

import android.app.AlertDialog;
import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.android.im.R;
import com.android.nettylibrary.utils.IMDensityUtil;

public class IMTextDetailDiglog {
    private Context context;
    private AlertDialog shareDialog;
    private TextView mTvTitle;
    private TextView mTvContent;
    private TextView mTvBtn;
    public IMTextDetailDiglog(Context context) {
        this.context=context;
    }
    public void showTextDiglog(String title,String content) {
        AlertDialog.Builder builder=new AlertDialog.Builder(context);
        final View dialogView =  View.inflate(context, R.layout.layout_dialog_text_detail, null);
        mTvTitle = dialogView.findViewById(R.id.im_tv_title);
        mTvContent = dialogView.findViewById(R.id.im_tv_content);
        mTvBtn = dialogView.findViewById(R.id.im_tv_btn);
        mTvTitle.setText(title);
        if(title.equals("群公告")){
            mTvContent.setText(TextUtils.isEmpty(content)?"暂无公告":content);
        }else {
            mTvContent.setText(TextUtils.isEmpty(content)?"暂无签名":content);
        }

        mTvBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareDialog.dismiss();
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
}
