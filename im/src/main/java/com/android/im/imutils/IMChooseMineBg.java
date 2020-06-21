package com.android.im.imutils;

import com.android.im.imbean.IMChoosePicBean;
import com.android.im.imbean.MyBgBeanData;

import java.util.ArrayList;
import java.util.List;

public class IMChooseMineBg {
    public static List<IMChoosePicBean> getPicDatas(boolean isminebg, MyBgBeanData myBgBeanData) {
        List<IMChoosePicBean> datas = new ArrayList<>();
        if (isminebg) {
            for (int i = 0; i < myBgBeanData.getSelfBackgroupList().size(); i++) {
                datas.add(new IMChoosePicBean(i,  myBgBeanData.getSelfBackgroupList().get(i)));
            }

        } else {
            for (int i = 0; i < myBgBeanData.getChatBackgroupList().size(); i++) {
                datas.add(new IMChoosePicBean(i,  myBgBeanData.getChatBackgroupList().get(i)));
            }
        }
        return datas;
    }

    public static List<IMChoosePicBean> getPicDatas(boolean isminebg, List<String> urls) {
        List<IMChoosePicBean> datas = new ArrayList<>();
        if (isminebg) {
            for (int i = 0; i < urls.size(); i++) {
                datas.add(new IMChoosePicBean(i,  urls.get(i)));
            }

        } else {
            for (int i = 0; i <urls.size(); i++) {
                datas.add(new IMChoosePicBean(i, urls.get(i)));
            }
        }
        return datas;
    }
}