package com.android.im.imview.dialog;

import android.app.AlertDialog;
import android.content.Context;
import android.text.TextUtils;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.im.R;
import com.android.im.imutils.IMImageLoadUtil;
import com.android.nettylibrary.http.IMGroupNoticeDialogBean;
import com.android.nettylibrary.utils.IMDensityUtil;

public class IMGroupTanChuaDiglog {
    private Context context;
    private AlertDialog shareDialog;
    private TextView mTvTitle;
    private TextView mTvContent;
    private ImageView mIvContent;
    private TextView mTvBtn;
    public IMGroupTanChuaDiglog(Context context) {
        this.context=context;
    }
    public void showTextDiglog(IMGroupNoticeDialogBean data) {
        AlertDialog.Builder builder=new AlertDialog.Builder(context);
        final View dialogView =  View.inflate(context, R.layout.layout_dialog_group_detail, null);
        mTvContent = dialogView.findViewById(R.id.im_tv_content);
        mIvContent = dialogView.findViewById(R.id.im_iv_content);
        mTvBtn = dialogView.findViewById(R.id.im_tv_btn);
        mTvContent.setText(TextUtils.isEmpty(data.getTextContent())?"":data.getTextContent());
        if(TextUtils.isEmpty(data.getPictureContent())){
            mIvContent.setVisibility(View.GONE);
        }else {
            mIvContent.setVisibility(View.VISIBLE);
            IMImageLoadUtil.CommonImageLoadCp(context,data.getPictureContent(),mIvContent);
        }
        mTvContent.setMaxHeight(IMDensityUtil.dip2px(context, 150));
        mTvContent.setMovementMethod(ScrollingMovementMethod.getInstance());

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
