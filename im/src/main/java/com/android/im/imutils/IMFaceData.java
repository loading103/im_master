package com.android.im.imutils;

import com.android.im.R;

import java.util.HashMap;
import java.util.Map;

public class IMFaceData {
    public static Map<String, Integer> gifFaceInfo = new HashMap<String, Integer>();

    public static final String f1 = "[:im_f1]";
    static {
        addString(gifFaceInfo, f1, R.mipmap.im_f1);
    }

    private static void addString(Map<String, Integer> map, String smile,
                                  int resource) {
        map.put(smile, resource);
    }
}