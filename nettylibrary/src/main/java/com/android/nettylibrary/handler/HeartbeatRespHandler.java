package com.android.nettylibrary.handler;


import com.android.nettylibrary.netty.NettyTcpClient;
import com.android.nettylibrary.protobuf.MessageProtobuf;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * <p>@ProjectName:     NettyChat</p>
 * <p>@ClassName:       HeartbeatRespHandler.java</p>
 * <p>@PackageName:     com.freddy.im</p>
 * <b>
 * <p>@Description:     心跳消息响应处理handler</p>
 * </b>
 * <p>@author:          FreddyChen</p>
 * <p>@date:            2019/04/08 01:08</p>
 * <p>@email:           chenshichao@outlook.com</p>
 */
public class HeartbeatRespHandler extends ChannelInboundHandlerAdapter {

    private NettyTcpClient imsClient;

    public HeartbeatRespHandler(NettyTcpClient imsClient) {
        this.imsClient = imsClient;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        MessageProtobuf.ImMessage heartbeatRespMsg = (MessageProtobuf.ImMessage) msg;
        if (heartbeatRespMsg == null ) {
            return;
        }

        MessageProtobuf.ImMessage heartbeatMsg = imsClient.getHeartbeatMsg();
        if (heartbeatMsg == null ) {
            return;
        }

        int heartbeatMsgType = heartbeatMsg.getTypeValue();
        if (heartbeatMsgType == heartbeatRespMsg.getTypeValue()) {
            System.out.println("------收到服务端心跳响应消息，message=" + heartbeatRespMsg);
        } else {
            // 消息透传
            ctx.fireChannelRead(msg);
        }
    }
}
