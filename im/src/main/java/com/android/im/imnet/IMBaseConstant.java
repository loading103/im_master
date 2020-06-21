package com.android.im.imnet;

import okhttp3.MediaType;

/**
 * @author jinxin
 * 剑之所指，心之所向
 * @date 2019/8/4
 */
public class IMBaseConstant {
    //项目回调的不同类型
    public static int IM_CLICK_PERSON_PLAY_GAME = 1; //单聊聊天界面的玩游戏点击
    public static int IM_CLICK_GROUP_PLAY_GAME = 2; //群聊聊天界面的玩游戏点击
    public static int IM_CLICK_RED_PICK_GAME = 3; //领取红包之后点击继续玩游戏
    public static int IM_CLICK_BLANK = 4; //银行卡充值
    public static int IM_CLICK_WEICHAT = 5; //微信充值
    public static int IM_CLICK_APLIY = 6; //支付宝充值
    public static int IM_CLICK_TIXIAN = 7; //体现
    public static int IM_LOGIN_IN = 8; //登录成功

    //Okhttp的JSON
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
}