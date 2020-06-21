package com.android.nettylibrary.handler;

import android.util.Log;

import com.android.nettylibrary.bean.IMMessageBean;
import com.android.nettylibrary.bean.IMSingleMessage;
import com.android.nettylibrary.event.CEventCenter;
import com.android.nettylibrary.event.Events;

public class GroupChatMessageHandler extends AbstractMessageHandler {

    private static final String TAG = GroupChatMessageHandler.class.getSimpleName();

    @Override
    protected void action(IMMessageBean message) {
        Log.d(TAG, "收到群聊消息，message=" + message.toString());
        IMSingleMessage builder = new IMSingleMessage();
        builder.setData(message.getData());
        builder.setClientId(message.getClientId());
        builder.setFingerprint(message.getFingerprint());
        builder.setSenderId(message.getSenderId());
        builder.setGroupId(message.getGroupId());
        builder.setReceiverId(message.getReceiverId());
        builder.setTimestamp(message.getTimestamp());
        builder.setType(message.getType());
        CEventCenter.dispatchEvent(Events.CHAT_GROUP_MESSAGE, 0, 0, message);

    }
}
