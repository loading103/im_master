package com.rhby.cailexun.application;

import android.app.Application;
import android.content.Context;

import androidx.multidex.MultiDex;

import com.android.im.DaoManager;
import com.android.im.IMSManager;
import com.rhby.cailexun.net.base.ServiceFactory;
import com.android.nettylibrary.utils.IMPreferenceUtil;
import com.simple.spiderman.SpiderMan;
import com.tencent.bugly.crashreport.CrashReport;
import com.umeng.analytics.MobclickAgent;
import com.umeng.commonsdk.UMConfigure;

import cn.jpush.android.api.JPushInterface;


public class MyApplication extends Application {
    public  AppThread mthread;
    public static  Context context;

    public static Context getContext() {
        return context;
    }
    @Override
    public void onCreate() {
        super.onCreate();
        SpiderMan.init(this);
        context=getApplicationContext();
        mthread=new AppThread();
        mthread.start();
    }
    private class AppThread  extends Thread{
        @Override
        public void run() {
            //初始化网络
            ServiceFactory.init(context);
            IMPreferenceUtil.init(context);
            //初始化im
            IMSManager.init(context);
            //初始化友盟统计
            initUMeng();
            //极光
            JPushInterface.setDebugMode(true);
            JPushInterface.init(context);
            //Bugly
            CrashReport.initCrashReport(getApplicationContext(), "7a2900c148", false);

            DaoManager.init(context);
        }
    }

    // 选用LEGACY_AUTO页面采集模式
    private void initUMeng() {
        UMConfigure.init(this,"5dddea12570df36a5f000dee","MyChannelID",UMConfigure.DEVICE_TYPE_PHONE, null);
        UMConfigure.setLogEnabled(true);
        MobclickAgent.setPageCollectionMode(MobclickAgent.PageMode.AUTO);
    }
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }
}
