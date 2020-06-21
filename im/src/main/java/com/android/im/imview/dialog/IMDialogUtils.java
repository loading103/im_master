package com.android.im.imview.dialog;

import android.app.AlertDialog;
import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.im.R;
import com.android.nettylibrary.utils.IMDensityUtil;

public class IMDialogUtils {

    private AlertDialog shareDialog;
    private TextView mTvTitle;
    private TextView mTvContent;
    private TextView mTvCancle;
    private TextView mTvSure;
    private RelativeLayout rlCancle;
    public static IMDialogUtils instance;

    public static IMDialogUtils getInstance() {
        synchronized (IMDialogUtils.class) {
            if(instance == null)
                instance = new IMDialogUtils();
        }
        return instance;
    }

    /**
     * 2个按钮，取消默认对话框消失
     */
    public   void showCommonDialog(Context context,String content,  View.OnClickListener listener){
        AlertDialog.Builder builder=new AlertDialog.Builder(context);
        final View dialogView =  View.inflate(context, R.layout.layout_dialog_common, null);
        mTvTitle = dialogView.findViewById(R.id.im_tv_title);
        mTvContent = dialogView.findViewById(R.id.im_tv_content);
        mTvCancle = dialogView.findViewById(R.id.tv_cancle);
        builder.setCancelable(false);
        mTvSure = dialogView.findViewById(R.id.tv_ensure);
        if(!TextUtils.isEmpty(content)){
            mTvContent.setText(content);
        }
        mTvSure.setOnClickListener(listener);
        mTvCancle.setOnClickListener(new View.OnClickListener() {
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

    public void dismissCommonDiglog() {
        if(shareDialog!=null && shareDialog.isShowing()){
            shareDialog.dismiss();
        }
    }
    /**
     * 提示重复登录
     */
    public  void showReLoginDialog(Context context, String content, final View.OnClickListener canclelistener, View.OnClickListener ensureLisener){
        AlertDialog.Builder builder=new AlertDialog.Builder(context);
        final View dialogView =  View.inflate(context, R.layout.layout_dialog_common, null);
        mTvTitle = dialogView.findViewById(R.id.im_tv_title);
        mTvContent = dialogView.findViewById(R.id.im_tv_content);
        mTvCancle = dialogView.findViewById(R.id.tv_cancle);
        mTvSure = dialogView.findViewById(R.id.tv_ensure);
        mTvContent.setText(content);
        mTvSure.setOnClickListener(ensureLisener);
        mTvCancle.setOnClickListener(canclelistener);
        builder.setCancelable(false);
        mTvSure.setText("重新登录");
        mTvCancle.setText("退出");
        builder.setView(dialogView);
        shareDialog =  builder.show();
        Window window = shareDialog.getWindow();
        window.setWindowAnimations(R.style.ActionSheetDialogAnimations);  //添加动画
        WindowManager.LayoutParams params = window.getAttributes();

        params.width = IMDensityUtil.getScreenWidth(context)- IMDensityUtil.dip2px(context,70);
        shareDialog.getWindow().setAttributes(params);
        shareDialog.getWindow().setBackgroundDrawable(context.getResources().getDrawable(R.drawable.im_shape__bg));
    }

    /**
     * 提示群信息
     */
    public  void showGroupInforDialog(Context context, String content, final View.OnClickListener canclelistener, View.OnClickListener ensureLisener){
        AlertDialog.Builder builder=new AlertDialog.Builder(context);
        final View dialogView =  View.inflate(context, R.layout.layout_dialog_common, null);
        mTvTitle = dialogView.findViewById(R.id.im_tv_title);
        mTvContent = dialogView.findViewById(R.id.im_tv_content);
        mTvCancle = dialogView.findViewById(R.id.tv_cancle);
        mTvSure = dialogView.findViewById(R.id.tv_ensure);
        mTvContent.setText(content);
        mTvSure.setOnClickListener(ensureLisener);
        mTvCancle.setOnClickListener(canclelistener);
        builder.setCancelable(false);
        mTvSure.setText("删除");
        mTvCancle.setText("取消");
        builder.setView(dialogView);
        shareDialog =  builder.show();
        Window window = shareDialog.getWindow();
        window.setWindowAnimations(R.style.ActionSheetDialogAnimations);  //添加动画
        WindowManager.LayoutParams params = window.getAttributes();

        params.width = IMDensityUtil.getScreenWidth(context)- IMDensityUtil.dip2px(context,70);
        shareDialog.getWindow().setAttributes(params);
        shareDialog.getWindow().setBackgroundDrawable(context.getResources().getDrawable(R.drawable.im_shape__bg));
    }


    /**
     * 提示打开通知
     */
    public  void showOpenNoticeDialog(Context context,View.OnClickListener ensureLisener){
        AlertDialog.Builder builder=new AlertDialog.Builder(context);
        final View dialogView =  View.inflate(context, R.layout.layout_dialog_common, null);
        mTvTitle = dialogView.findViewById(R.id.im_tv_title);
        mTvContent = dialogView.findViewById(R.id.im_tv_content);
        mTvCancle = dialogView.findViewById(R.id.tv_cancle);
        mTvSure = dialogView.findViewById(R.id.tv_ensure);
        mTvContent.setText("尊敬的用户您好!关闭通知将有可能不能正常接收通知消息，请开启通知");
        mTvSure.setOnClickListener(ensureLisener);
        mTvSure.setText("去开启");
        builder.setCancelable(false);
        mTvCancle.setOnClickListener(new View.OnClickListener() {
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
    /**
     * 提示版本更新
     */
    public  void showUpdataVersonDialog(Context context,String verson,String content,boolean isfouced,View.OnClickListener ensureLisener,View.OnClickListener cancleLisener){
        AlertDialog.Builder builder=new AlertDialog.Builder(context);
        final View dialogView =  View.inflate(context, R.layout.layout_dialog_update, null);
        mTvTitle = dialogView.findViewById(R.id.im_tv_title);
        mTvContent = dialogView.findViewById(R.id.im_tv_content);
        rlCancle = dialogView.findViewById(R.id.ll_im_container);
        mTvSure = dialogView.findViewById(R.id.tv_ensure);
        mTvTitle.setText(verson);
        mTvContent.setText(content);
        mTvSure.setOnClickListener(ensureLisener);
        builder.setCancelable(false);
        rlCancle.setOnClickListener(cancleLisener);
        if(isfouced){
            rlCancle.setVisibility(View.GONE);
        }else {
            rlCancle.setVisibility(View.VISIBLE);
        }
        builder.setView(dialogView);
        shareDialog =  builder.show();
        Window window = shareDialog.getWindow();
        window.setWindowAnimations(R.style.ActionSheetDialogAnimations);  //添加动画
        WindowManager.LayoutParams params = window.getAttributes();

        params.width = IMDensityUtil.getScreenWidth(context)- IMDensityUtil.dip2px(context,70);
        shareDialog.getWindow().setAttributes(params);
        shareDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
    }

}
