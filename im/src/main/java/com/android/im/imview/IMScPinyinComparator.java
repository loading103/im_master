package com.android.im.imview;


import com.android.im.imbean.SmallProgramBean;
import com.android.nettylibrary.greendao.entity.IMGroupMemberBean;

import java.util.Comparator;

/**
 * @Description:拼音的比较器
 * @author http://blog.csdn.net/finddreams
 */ 
public class IMScPinyinComparator implements Comparator<SmallProgramBean> {

	public int compare(SmallProgramBean o1, SmallProgramBean o2) {
		if (o1.getLetter().equals("@") || o2.getLetter().equals("#")) {
			return -1;
		} else if (o1.getLetter().equals("#") || o2.getLetter().equals("@")) {
			return 1;
		} else {
			return o1.getLetter().compareTo(o2.getLetter());
		}
	}

}
