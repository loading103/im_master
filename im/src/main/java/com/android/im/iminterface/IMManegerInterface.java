package com.android.im.iminterface;

/**
 * 发送消息回调
 */
public interface  IMManegerInterface{

    /**
     * 重复登录提示
     */
    void  setOnUserReLogin();
    /**
     * 获取未读数目
     */
    void  getUnReadnumber(String number);

}