package com.android.im.imutils;

import android.animation.ArgbEvaluator;
import android.content.Context;
import android.content.SharedPreferences;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import static android.content.Context.MODE_PRIVATE;

/**
 * @author by songhang on 2018/2/21
 */

public class TimeUtil {

    public static final long DAY = 24 * 3600 * 1000;
    private static final String PREFERENCE_TIME_NAME = "time";
    private static final String PREFERENCE_USAGE_NAME = "usage";
    private static final String PREFERENCE_USAGE_SEEN_KEY = "seen_key";
    private static final ArgbEvaluator sArgbEvaluator = new ArgbEvaluator();
    private static final SimpleDateFormat sSimpleDateFormat = new SimpleDateFormat("yyyy_MM_dd", Locale.getDefault());

    /**
     * Save used time in sharedPreferences
     * @param context the context
     * @return  used time
     */
    public static long getTodayUsedTime(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFERENCE_TIME_NAME, MODE_PRIVATE);
        Date date = new Date(System.currentTimeMillis());
        String today = sSimpleDateFormat.format(date);
        return sharedPreferences.getLong(today, 0);
    }

    public static void setTodayUsedTime(Context context, long time) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFERENCE_TIME_NAME, MODE_PRIVATE);
        SharedPreferences.Editor edit = sharedPreferences.edit();
        Date date = new Date(System.currentTimeMillis());
        String today = sSimpleDateFormat.format(date);
        edit.putLong(today, time);
        edit.apply();
    }

    public static void setSeenAppUsage(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFERENCE_USAGE_NAME, MODE_PRIVATE);
        SharedPreferences.Editor edit = sharedPreferences.edit();
        edit.putBoolean(PREFERENCE_USAGE_SEEN_KEY, true);
        edit.apply();
    }

    public static boolean isSeenAppUsage(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFERENCE_USAGE_NAME, MODE_PRIVATE);
        return sharedPreferences.getBoolean(PREFERENCE_USAGE_SEEN_KEY, false);
    }

    public static String timeToString(long time) {
        long s = time / 1000;
        if (s < 0) {
            return "err";
        } else if (s < 60) {
            return s + "s";
        } else if (s < 3600) {
            return s / 60 + "m" + timeToString(s % 60 * 1000);
        } else if (s <= 86400) { //A DAY
            return s / 3600 + "h" + timeToString(s % 3600 * 1000);
        } else if (s <= 86400 * 31) { //A MONTH
            return s / (86400) + "d" + timeToString(s % 86400 * 1000);
        }
        //if time > A MONTH, return err
        return "err";
    }
    public static String timeToStrings(long time) {
        long s = time / 1000;
        if (s < 0) {
            return "err";
        } else if (s < 60) {
            return s + "秒";
        } else if (s < 3600) {
            return s / 60 + "分钟";
        } else if (s <= 86400* 31) { //A DAY
            return s / 3600 + "小时" + timeToStrings(s % 3600 * 1000);
        }
//        else if (s <= 86400 * 31) { //A MONTH
//            return s / (86400) + "天" + timeToStrings(s % 86400 * 1000);
//        }
        //if time > A MONTH, return err
        return "err";
    }
    public static long getZeroTime(long time) {
        Calendar calendar = Calendar.getInstance(TimeZone.getDefault());
        calendar.setTimeInMillis(time);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTimeInMillis();
    }

    public static boolean isSameDay(long time1, long time2) {
        return getZeroTime(time1) == getZeroTime(time2);
    }

}
