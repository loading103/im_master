package com.rhby.cailexun.dialog;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.view.WindowManager;

import com.rhby.cailexun.R;
import com.rhby.cailexun.widget.DragImageView;
import com.android.nettylibrary.utils.IMDensityUtil;

/**
 * Created by Wolf on 2019/12/02.
 * Describe:滑动验证dialog
 */
public class VerificationDiglog{
    private Context context;
    private AlertDialog shareDialog;
    private DragImageView dragView;
    private Handler handler;

    public VerificationDiglog(Context context) {
        this.context=context;
    }
    public void showVerificationDiglog() {
        AlertDialog.Builder builder=new AlertDialog.Builder(context);
        final View dialogView =  View.inflate(context, R.layout.dialog_verification, null);
        initView(dialogView);
        initEvent();

        builder.setView(dialogView);
        shareDialog =  builder.show();
        WindowManager.LayoutParams params = shareDialog.getWindow().getAttributes();
        params.width = IMDensityUtil.getScreenWidth(context)- IMDensityUtil.dip2px(context,70);
        shareDialog.getWindow().setAttributes(params);
        shareDialog.getWindow().setBackgroundDrawable(context.getResources().getDrawable(R.drawable.im_shape__bg));
    }

    private void initView(View dialogView) {
        dragView = dialogView.findViewById(R.id.dragView);
        dragView.setUp(BitmapFactory.decodeResource(context.getResources(), R.drawable.drag_cover),
                BitmapFactory.decodeResource(context.getResources(), R.drawable.drag_block),
                BitmapFactory.decodeResource(context.getResources(), R.drawable.drag_cover_c),
                0.377f);
    }

    protected void initEvent() {
        dragView.setDragListenner(new DragImageView.DragListenner() {
            @Override
            public void onDrag(float position) {
//                Toast.makeText(context, position + "", Toast.LENGTH_SHORT).show();
                if (Math.abs(position - 0.637) > 0.012)
                    dragView.fail();
                else {
                    dragView.ok();
                    runUIDelayed(new Runnable() {
                        @Override
                        public void run() {
                            dragView.reset();
                            if(onFinishListener!=null){
                                onFinishListener.onFinish(true);
                            }
                            shareDialog.dismiss();
                        }
                    }, 2000);
                }
            }

        });
    }

    public void runUIDelayed(Runnable run, int de) {
        if (handler == null)
            handler = new Handler(Looper.getMainLooper());
        handler.postDelayed(run, de);
    }

    public  interface  OnFinishListener{
        void  onFinish(Boolean isSuccess);
    }
    private OnFinishListener onFinishListener;

    public void setOnFinishListener(OnFinishListener onFinishListener) {
        this.onFinishListener = onFinishListener;
    }
}