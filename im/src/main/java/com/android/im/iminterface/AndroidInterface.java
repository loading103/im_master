package com.android.im.iminterface;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;
import android.webkit.JavascriptInterface;
import android.widget.Toast;

import com.android.im.imui.activity.SmallProgramActivity;
import com.just.agentweb.AgentWeb;

/**
 * Created by Administrator on 2019/12/26.
 * Describe:
 */
public class AndroidInterface {
    private Handler deliver = new Handler(Looper.getMainLooper());
    private AgentWeb agent;
    private Context context;

    public AndroidInterface(AgentWeb agent, Context context) {
        this.agent = agent;
        this.context = context;
    }

    @JavascriptInterface
    public void rouseWindow(final String msg) {
        deliver.post(new Runnable() {
            @Override
            public void run() {
                Log.i("Info", "main Thread:" + Thread.currentThread());
                ((SmallProgramActivity)context).showAgreeDialog();
            }
        });
        Log.i("Info", "Thread:" + Thread.currentThread());
    }

    @JavascriptInterface
    public void screenOrientation(final String msg) {
        deliver.post(new Runnable() {
            @Override
            public void run() {
                Log.i("Info", "main Thread:" + Thread.currentThread());
                if(!TextUtils.isEmpty(msg)){
                    if(msg.equals("landscape")){
                        ((SmallProgramActivity)context).setScreen(2);
                    }else if(msg.equals("portrait")){
                        ((SmallProgramActivity)context).setScreen(1);
                    }
                }
            }
        });
        Log.i("Info", "Thread:" + Thread.currentThread());
    }
}
