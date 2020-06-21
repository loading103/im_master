package com.android.nettylibrary.handler;

import android.util.Log;

import com.android.nettylibrary.bean.IMMessageBean;


public class ServerReportMessageHandler extends AbstractMessageHandler {

    private static final String TAG = ServerReportMessageHandler.class.getSimpleName();

    @Override
    protected void action(IMMessageBean message) {
        Log.d(TAG, "收到消息状态报告，message=" + message);
    }
}
