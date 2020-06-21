package com.android.im.imutils;


import com.android.nettylibrary.utils.IMLogUtil;

public class IMStopClickFast {
    // 两次点击按钮之间的点击间隔不能少于2000毫秒
    private static final int MIN_CLICK_DELAY_TIMETWO = 2500;
    private static final int MIN_CLICK_DELAY_TIME = 1000;
    private static final int MIN_CLICK_DELAY_TIMES = 500;
    private static long lastClickTime;

    public static boolean isFastClick() {
        boolean flag = false;
        long curClickTime = System.currentTimeMillis();
        if ((curClickTime - lastClickTime) >= MIN_CLICK_DELAY_TIME) {
            flag = true;
        }
        IMLogUtil.d("MyOwnTag:", "IMStopClickFast ----" +(curClickTime - lastClickTime)+flag);

        lastClickTime = curClickTime;
        return flag;
    }


    public static boolean isSendFastClick() {
        boolean flag = false;
        long curClickTime = System.currentTimeMillis();
        if ((curClickTime - lastClickTime) >= MIN_CLICK_DELAY_TIMES) {
            flag = true;
        }
        lastClickTime = curClickTime;
        return flag;
    }


    public static boolean isFastTWOClick() {
        boolean flag = false;
        long curClickTime = System.currentTimeMillis();
        if ((curClickTime - lastClickTime) >= MIN_CLICK_DELAY_TIMETWO) {
            flag = true;
        }
        IMLogUtil.d("MyOwnTag:", "IMStopClickFast ----" +(curClickTime - lastClickTime)+flag);

        lastClickTime = curClickTime;
        return flag;
    }
}
