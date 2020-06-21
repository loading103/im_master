package com.android.nettylibrary.utils;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.widget.Toast;

import androidx.annotation.StringRes;

import com.android.nettylibrary.IMSNettyManager;

/**
 * Created by rwz on 2017/3/9.
 */

public class IMToastUtil {

    private static IMToastUtil INSTANCE;
    private Toast mToast;
    private Toast mCustomToast; //自定义toast

    private static Context getContext() {
        return IMSNettyManager.getInstance().getContext();
    }

    public static IMToastUtil getInstance() {
        if (INSTANCE == null) {
            synchronized (IMToastUtil.class) {
                if (INSTANCE == null) {
                    INSTANCE = new IMToastUtil();
                }
            }
        }
        return INSTANCE;
    }

    public void showShortSingle(final @StringRes int stringRes) {
        showShortSingle(getContext().getResources().getString(stringRes));
    }

    public void showShortSingle(final String string) {
        if (TextUtils.isEmpty(string)) {
            return;
        }
        if (isMainThread()) {
            getToast().setText(string);
            mToast.show();
        } else {
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    getToast().setText(string);
                    //已在主线程中，可以更新UI
                    mToast.show();
                }
            });
        }
    }

    public void showCustomShortSingle(@StringRes int stringRes) {
        showCustomShortSingle(getContext().getResources().getString(stringRes));
    }

    public void showCustomShortSingle(final String string) {
        if (TextUtils.isEmpty(string)) {
            return;
        }
        if (isMainThread()) {
            setCustomText(string).show();
        } else {
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    setCustomText(string).show();
                }
            });
        }
    }

    private Toast getToast() {
        if (mToast == null) {
            mToast = Toast.makeText(getContext(), "", Toast.LENGTH_SHORT);
        }
        return mToast;
    }

    private Toast getCustomToast() {
        if (mCustomToast == null) {
            mCustomToast = Toast.makeText(getContext(), "", Toast.LENGTH_SHORT);
        }
        return mCustomToast;
    }

    private Toast setCustomText(String text) {
        Toast customToast = getCustomToast();
        return customToast;
    }

    /**
     * 一定在主线程显示
     * @param stringRes
     */
    public static void showShort(@StringRes int stringRes) {
        if (isMainThread()) {
            showText(getContext().getResources().getString(stringRes), false);
        } else {
            showTextOnBackgroundThread(getContext().getResources().getString(stringRes), false);
        }
    }
    public static void showLong(@StringRes int stringRes) {
        if (isMainThread()) {
            showText(getContext().getResources().getString(stringRes), true);
        } else {
            showTextOnBackgroundThread(getContext().getResources().getString(stringRes), true);
        }
    }

    public static void showShort(String text) {
        if (TextUtils.isEmpty(text)) {
            return;
        }
        if (isMainThread()) {
            showText(text, false);
        } else {
            showTextOnBackgroundThread(text, false);
        }
    }

    public static void showLong(String text) {
        if (TextUtils.isEmpty(text)) {
            return;
        }
        if (isMainThread()) {
            showText(text, true);
        } else {
            showTextOnBackgroundThread(text, true);
        }
    }



    private static void showTextOnBackgroundThread(final String text, final boolean isLong) {
        Handler mainHandler = new Handler(Looper.getMainLooper());
        mainHandler.post(new Runnable() {
            @Override
            public void run() {
                //已在主线程中，可以更新UI
                showText(text, isLong);
            }
        });
    }

    private static void showText(String text, boolean isLong) {
        if (isLong) {
            Toast.makeText(getContext(), text, Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(getContext(), text, Toast.LENGTH_SHORT).show();
        }
    }

    public static boolean isMainThread() {
        return Looper.myLooper() == Looper.getMainLooper();
    }



}
