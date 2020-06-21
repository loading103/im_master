package com.rhby.cailexun.utils;

import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.android.im.IMSManager;
import com.android.im.R;
import com.rhby.cailexun.application.MyApplication;
import com.rhby.cailexun.ui.activity.MainActivity;
import com.android.nettylibrary.utils.IMLogUtil;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;


public class NotifityUtils {
    private int notificationId=0;
    private boolean mIsSupportedBade = true;//是否显示角标
    public static NotifityUtils instance;

    public static NotifityUtils getInstance() {
        if (instance == null) {
            synchronized (NotifityUtils.class) {
                if(instance == null)
                    instance = new NotifityUtils();
            }
        }
        return instance;
    }

    /**
     * 设置桌面图标角标
     *
     * @param number
     */
    public void updateCorner(int number,String content) {
        setBadgeNumXM(number,MyApplication.getContext(),content);
        setBadgeNumHW(number);
        setBadgeNumSX(number,MyApplication.getContext());
    }

    /**
     * 华为角标
     *
     * @param num
     */
    public void setBadgeNumHW(int num) {
        try {
            Bundle bunlde = new Bundle();
            bunlde.putString("package", "com.rhby.cailexun"); // com.test.badge is your package name
            bunlde.putString("class", "com.rhby.cailexun.ui.activity.SplashActivity"); // com.test. badge.MainActivity        is your apk main activity
            bunlde.putInt("badgenumber", num);
            MyApplication.getContext().getContentResolver().call(Uri.parse("content://com.huawei.android.launcher.settings/badge/"), "change_badge", null, bunlde);
        } catch (Exception e) {
            IMLogUtil.d("MyOwnTag:", "NotifityUtils ----" +e.getMessage());
            mIsSupportedBade = false;
        }
    }

    /**
     * 小米角标
     * @param count
     * @param context
     * @return
     */
    public boolean setBadgeNumXM(int count, Context context,String content) {
        if(count==0){
            return false;
        }
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        if (notificationManager == null) {
            return false;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // 8.0之后添加角标需要NotificationChannel
            //需要在创建CHANNEL时确定
//            NotificationManager.IMPORTANCE_MIN: 静默;。
//            NotificationManager.IMPORTANCE_HIGH:随系统使用声音或振动
            NotificationChannel channel = new NotificationChannel("badge", "badge", NotificationManager.IMPORTANCE_MIN);
            channel.setShowBadge(true);
            notificationManager.createNotificationChannel(channel);
        }
        if(!TextUtils.isEmpty(content.trim())){
            Intent intent = new Intent(context, MainActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
            Notification notification = new NotificationCompat.Builder(context, "badge")
                    .setDefaults(NotificationCompat.FLAG_ONLY_ALERT_ONCE)//统一消除声音和震动
                    .setContentTitle("彩乐讯")
                    .setContentText(content)
                    .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.mipmap.im_about_us))
                    .setSmallIcon(R.mipmap.im_about_us)
                    .setAutoCancel(true)
                    .setContentIntent(pendingIntent)
                    .setChannelId("badge")
                    .setNumber(count)
                    .setBadgeIconType(NotificationCompat.BADGE_ICON_SMALL).build();
            // 小米
            try {
                Field field = notification.getClass().getDeclaredField("extraNotification");
                Object extraNotification = field.get(notification);
                Method method = extraNotification.getClass().getDeclaredMethod("setMessageCount", int
                        .class);
                method.invoke(extraNotification, count);
            } catch (Exception e) {
                e.printStackTrace();
                mIsSupportedBade = false;
            }
            notificationManager.notify(notificationId, notification);
        }
        return true;
    }

    /**
     * 三星角标
     * @param count
     * @param context
     * @return
     */
    private  boolean setBadgeNumSX(int count, Context context) {
        try {
            String launcherClassName = AppUtils.getLauncherClassName(context);
            if (TextUtils.isEmpty(launcherClassName)) {
                return false;
            }
            Intent intent = new Intent("android.intent.action.BADGE_COUNT_UPDATE");
            intent.putExtra("badge_count", count);
            intent.putExtra("badge_count_package_name", context.getPackageName());
            intent.putExtra("badge_count_class_name", launcherClassName);
            context.sendBroadcast(intent);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            mIsSupportedBade = false;
            return false;
        }
    }

    /** 判断程序是否在后台运行 */
    public  boolean isRunBackground() {
        ActivityManager activityManager = (ActivityManager) IMSManager.getInstance().getContext().getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> appProcesses = activityManager.getRunningAppProcesses();
        for (ActivityManager.RunningAppProcessInfo appProcess : appProcesses) {
            if (appProcess.processName.equals(IMSManager.getInstance().getContext().getPackageName())) {
                if (appProcess.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_BACKGROUND) {
                    // 表明程序在后台运行
                    return true;
                } else {
                    return false;
                }
            }
        }
        return false;
    }


    /**
     * 检查是否开启通知
     */
    public boolean isNotificationEnabled( ) {
        boolean isOpened = false;
        try {
            isOpened = NotificationManagerCompat.from(MyApplication.getContext()).areNotificationsEnabled();
        } catch (Exception e) {
            e.printStackTrace();
            isOpened = false;
        }
        return isOpened;

    }
    /**
     * 打开通知设置
     */
    public void openNotice(){
        Intent intent = new Intent();
        if (Build.VERSION.SDK_INT >= 26) {
            // android 8.0引导
            intent.setAction("android.settings.APP_NOTIFICATION_SETTINGS");
            intent.putExtra("android.provider.extra.APP_PACKAGE", MyApplication.getContext().getPackageName());
        } else if (Build.VERSION.SDK_INT >= 21) {
            // android 5.0-7.0
            intent.setAction("android.settings.APP_NOTIFICATION_SETTINGS");
            intent.putExtra("app_package", MyApplication.getContext().getPackageName());
            intent.putExtra("app_uid", MyApplication.getContext().getApplicationInfo().uid);
        } else {
            // 其他
            intent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
            intent.setData(Uri.fromParts("package", MyApplication.getContext().getPackageName(), null));
        }
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        MyApplication.getContext().startActivity(intent);
    }
}
