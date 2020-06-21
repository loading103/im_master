package com.android.im.imview;


import com.android.nettylibrary.greendao.entity.IMGroupMemberBean;

import java.util.Comparator;

/**
 * @Description:拼音的比较器
 * @author http://blog.csdn.net/finddreams
 */ 
public class IMPinyinComparator implements Comparator<IMGroupMemberBean> {

	public int compare(IMGroupMemberBean o1, IMGroupMemberBean o2) {
		if (o1.getLetter().equals("@") || o2.getLetter().equals("#")) {
			return -1;
		} else if (o1.getLetter().equals("#") || o2.getLetter().equals("@")) {
			return 1;
		} else {
			return o1.getLetter().compareTo(o2.getLetter());
		}
	}

}
