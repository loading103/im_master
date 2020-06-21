package com.android.nettylibrary.handler;

import android.util.Log;

import com.android.nettylibrary.bean.IMMessageBean;
import com.android.nettylibrary.bean.IMSingleMessage;
import com.android.nettylibrary.event.CEventCenter;
import com.android.nettylibrary.event.Events;


public class SingleChatMessageHandler extends AbstractMessageHandler {

    private static final String TAG = SingleChatMessageHandler.class.getSimpleName();
    @Override
    protected void action(IMMessageBean msg) {
        IMSingleMessage builder = new IMSingleMessage();
        Log.d(TAG, "收到单聊消息，message=" + msg.toString());
        builder.setData(msg.getData());
        builder.setClientId(msg.getClientId());
        builder.setFingerprint(msg.getFingerprint());
        builder.setSenderId(msg.getSenderId());
        builder.setReceiverId(msg.getReceiverId());
        builder.setTimestamp(msg.getTimestamp());
        builder.setType(msg.getType());
        CEventCenter.dispatchEvent(Events.CHAT_SINGLE_MESSAGE, 0, 0, msg);
    }
}
