package com.android.nettylibrary.handler;

import android.util.SparseArray;

import com.android.nettylibrary.bean.MessageType;


public class MessageHandlerFactory {

    private MessageHandlerFactory() {

    }

    private static final SparseArray<IMessageHandler> HANDLERS = new SparseArray<>();

    static {
        /** 单聊消息处理handler */
        HANDLERS.put(MessageType.ReceiverIdType.PERSONAL.getReceiverIdType(), new SingleChatMessageHandler());
        /** 群聊消息处理handler */
        HANDLERS.put(MessageType.ReceiverIdType.GROUP.getReceiverIdType(), new GroupChatMessageHandler());
        /** 服务端返回的消息发送状态报告处理handler */
        HANDLERS.put(MessageType.ReceiverIdType.NONE.getReceiverIdType(), new ServerReportMessageHandler());
    }

    /**
     * 根据消息类型获取对应的处理handler
     *
     * @param msgType
     * @return
     */
    public static IMessageHandler getHandlerByMsgType(int msgType) {
        return HANDLERS.get(msgType);
    }
}
