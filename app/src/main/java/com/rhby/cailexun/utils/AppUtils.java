package com.rhby.cailexun.utils;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;

/**
 * Created by Wolf on 2019/11/25.
 * Describe:
 */
public class AppUtils {
    public static String getLauncherClassName(Context context) {
        ComponentName launchComponent = getLauncherComponentName(context);
        if (launchComponent == null) {
            return "";
        } else {
            return launchComponent.getClassName();
        }
    }

    private static ComponentName getLauncherComponentName(Context context) {
        Intent launchIntent = context.getPackageManager().getLaunchIntentForPackage(context
                .getPackageName());
        if (launchIntent != null) {
            return launchIntent.getComponent();
        } else {
            return null;
        }
    }
}
