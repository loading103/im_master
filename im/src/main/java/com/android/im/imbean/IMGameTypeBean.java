package com.android.im.imbean;

import com.android.im.R;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;

public class IMGameTypeBean {
    private String gameId;
    private String  gameName;
    private boolean isChooseed;
    private  int   ImageId;

    public String getGameId() {
        return gameId;
    }

    public void setGameId(String gameId) {
        this.gameId = gameId;
    }

    public String getGameName() {
        return gameName;
    }

    public void setGameName(String gameName) {
        this.gameName = gameName;
    }

    public boolean isChooseed() {
        return isChooseed;
    }

    public void setChooseed(boolean chooseed) {
        isChooseed = chooseed;
    }

    public IMGameTypeBean(String gameName) {
        this.gameName = gameName;
    }

    public int getImageId() {
        return ImageId;
    }

    public void setImageId(int imageId) {
        ImageId = imageId;
    }

    public IMGameTypeBean(String gameId, String gameName, int imageId) {
        this.gameId = gameId;
        this.gameName = gameName;
        ImageId = imageId;
    }

    /**
     * 15	分分时时彩
     * 16	两分时时彩
     * 17	五分时时彩
     * 18	江苏快3
     * 19	湖北快3
     * 20	安徽快3
     * 21	吉林快3
     * 22	10分六合彩
     * 23	极速PK10
     * 24	广东十一选五
     * 25	山东十一选五
     * 26	上海十一选五
     * 27	分分快三
     * 28	快乐时时彩
     */
    public static List<IMGameTypeBean> getGameBeans(){
        List<IMGameTypeBean> bean=new ArrayList<>();
        bean.add(new IMGameTypeBean("","全部", R.mipmap.im_gameico_1));
        bean.add(new IMGameTypeBean("1","重庆时时彩", R.mipmap.im_gameico_1));
        bean.add(new IMGameTypeBean("2","天津时时彩", R.mipmap.im_gameico_2));
        bean.add(new IMGameTypeBean("3","新疆时时彩", R.mipmap.im_gameico_3));
        bean.add(new IMGameTypeBean("4","体彩排列3", R.mipmap.im_gameico_4));
        bean.add(new IMGameTypeBean("5","福彩3D", R.mipmap.im_gameico_5));
        bean.add(new IMGameTypeBean("6","六合彩", R.mipmap.im_gameico_6));
        bean.add(new IMGameTypeBean("7","北京28", R.mipmap.im_gameico_7));
        bean.add(new IMGameTypeBean("8","北京快乐8", R.mipmap.im_gameico_8));
        bean.add(new IMGameTypeBean("9","北京PK10", R.mipmap.im_gameico_9));
        bean.add(new IMGameTypeBean("10","重庆幸运农场", R.mipmap.im_gameico_10));
        bean.add(new IMGameTypeBean("11","广东快乐十分", R.mipmap.im_gameico_11));
        bean.add(new IMGameTypeBean("12","双色球", R.mipmap.im_gameico_12));
        bean.add(new IMGameTypeBean("13","三分时时彩", R.mipmap.im_gameico_13));
        bean.add(new IMGameTypeBean("14","幸运飞艇", R.mipmap.im_gameico_14));
        bean.add(new IMGameTypeBean("15","分分时时彩", R.mipmap.im_gameico_15));
        bean.add(new IMGameTypeBean("16","两分时时彩", R.mipmap.im_gameico_16));
        bean.add(new IMGameTypeBean("17","五分时时彩", R.mipmap.im_gameico_17));
        bean.add(new IMGameTypeBean("18","江苏快3", R.mipmap.im_gameico_18));
        bean.add(new IMGameTypeBean("19","湖北快3", R.mipmap.im_gameico_19));
        bean.add(new IMGameTypeBean("20","安徽快3", R.mipmap.im_gameico_20));
        bean.add(new IMGameTypeBean("21","吉林快3", R.mipmap.im_gameico_21));
        bean.add(new IMGameTypeBean("22","10分六合彩", R.mipmap.im_gameico_22));
        bean.add(new IMGameTypeBean("23","极速PK10", R.mipmap.im_gameico_23));
        bean.add(new IMGameTypeBean("24","广东十一选五", R.mipmap.im_gameico_24));
        bean.add(new IMGameTypeBean("25","山东十一选五", R.mipmap.im_gameico_25));
        bean.add(new IMGameTypeBean("26","上海十一选五", R.mipmap.im_gameico_26));
        bean.add(new IMGameTypeBean("27","分分快三", R.mipmap.im_gameico_27));
        bean.add(new IMGameTypeBean("28","快乐时时彩", R.mipmap.im_gameico_28));
        return bean;

    }

}
