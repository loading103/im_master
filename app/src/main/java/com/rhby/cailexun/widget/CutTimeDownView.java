package com.rhby.cailexun.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;


import com.rhby.cailexun.R;

import java.util.Timer;
import java.util.TimerTask;

import static com.blankj.utilcode.util.ViewUtils.runOnUiThread;

/**
 * Created by Wolf on 2019/11/21.
 */
@SuppressLint("AppCompatCustomView")
public class CutTimeDownView extends TextView {
    //等待总时长
    private  long totalTime = 5000;
    //广告时长
    private long adTime = 0;

    private Timer timer;

    public CutTimeDownView(Context context) {
        this(context, null);
    }

    public CutTimeDownView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CutTimeDownView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    private void initView(Context context) {
        setBackground(context.getResources().getDrawable(R.drawable.im_shape__bg6));
    }

    public void setTotalTime(long totalTime) {
        this.totalTime = totalTime;
        setVisibility(View.VISIBLE);
        countDownNext(totalTime);
    }
    public void setOnDestoryed() {
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }

    private void countDownNext(long totalTime){
        if (timer == null) {
            timer = new Timer();
        }
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (adTime >= totalTime) {
                    if (timer != null) {
                        timer.cancel();
                        timer = null;
                    }
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if(onFinishListener!=null){
                                onFinishListener.setOnFinishListener();
                            }
                        }
                    });
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        setText((totalTime - adTime)/1000+" | 跳过");
                        adTime= adTime+1000;
                    }
                });
            }
        }, 0, 1000);
    }

    public  interface  OnFinishListener{
        void   setOnFinishListener();
    }
    private OnFinishListener onFinishListener;

    public void setOnFinishListener(OnFinishListener onFinishListener) {
        this.onFinishListener = onFinishListener;
    }
}
