package com.android.im.imutils;

import android.annotation.TargetApi;
import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import com.android.im.IMSManager;
import com.android.im.R;

import java.util.List;

import static android.content.Context.NOTIFICATION_SERVICE;

public class AppOfflLineNotifitys {

    //通知栏设置
    public static void setNoticefication() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String channelId = "chat";
            String channelName = "聊天消息";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            createNotificationChannel(channelId, channelName, importance);

            channelId = "subscribe";
            channelName = "订阅消息";
            importance = NotificationManager.IMPORTANCE_DEFAULT;
            createNotificationChannel(channelId, channelName, importance);
        }
    }
    @TargetApi(Build.VERSION_CODES.O)
    private static void createNotificationChannel(String channelId, String channelName, int importance) {
        NotificationChannel channel = new NotificationChannel(channelId, channelName, importance);
        NotificationManager notificationManager = (NotificationManager) IMSManager.getInstance().getContext().getSystemService(NOTIFICATION_SERVICE);
        notificationManager.createNotificationChannel(channel);
    }


    /**
     * 处理消息
     */
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public static void callNotivity(String bean,int number) {
        if (bean == null ) {
            return;
        }
        NotificationManager manager = (NotificationManager) IMSManager.getInstance().getContext().getSystemService(NOTIFICATION_SERVICE);
        Notification notification = new NotificationCompat.Builder(IMSManager.getInstance().getContext(), "subscribe")
                .setContentTitle("彩乐讯")
                .setContentText("您有新的消息啦~")
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(R.mipmap.im_about_us)
//                .setLargeIcon(BitmapFactory.decodeResource(IMSManager.getInstance().getContext().getResources(), R.mipmap.im_login_logo))
                .setAutoCancel(true)
                .build();
        manager.notify(0, notification);
    }

    /** 判断程序是否在后台运行 */
    public static boolean isRunBackground() {
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
}
