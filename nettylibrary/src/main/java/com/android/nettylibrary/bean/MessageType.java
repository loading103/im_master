package com.android.nettylibrary.bean;

/**
 * 消息类型
 */
public enum MessageType {
    //心跳数据
    HEARTBEAT(0),
    //注册
    INFORMATION(1),
    //用户消息
    USERMSG(2),
    //客户端消息回执
    RECEIPT(3),
    //客户端位置
    POSITION(4),
    //服务端消息回执
    SERVERRECEIPT(5),
    //服务端ack消息回执 客户端确认收到消息
    ACK(6),
    //群被踢消息
    KICKOUT(7),
    //群禁言
    PROHIBITIONS(8),
    //系统消息
    SYSTEM(9),
    //用户状态消息
    USERSTATUS(10),
    //用户信息改变
    INFORMATIONCHANGE(11),

    READ(12),

    TOKEN(13),//   token相关消息

    DELETE(14);//删除消息

    private int msgType;

    MessageType(int msgType) {
        this.msgType = msgType;
    }

    public int getMsgType() {
        return this.msgType;
    }

    /**
     * 消息类型
     */
    public enum ReceiverIdType {
        //个人单聊
        PERSONAL(0),
        //群组群聊
        GROUP(1),
        //没有特定功能
        NONE(2);
        private int receiverIdType;
        ReceiverIdType(int receiverIdType) {
            this.receiverIdType = receiverIdType;
        }
        public int getReceiverIdType() {
            return this.receiverIdType;
        }
    }
}
