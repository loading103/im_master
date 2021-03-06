package com.android.nettylibrary.handler;

import com.android.nettylibrary.netty.NettyTcpClient;
import com.android.nettylibrary.protobuf.MessageProtobuf;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * <p>@ProjectName:     NettyChat</p>
 * <p>@ClassName:       LoginAuthRespHandler.java</p>
 * <p>@PackageName:     com.freddy.im</p>
 * <b>
 * <p>@Description:     握手认证消息响应处理handler</p>
 * </b>
 * <p>@author:          FreddyChen</p>
 * <p>@date:            2019/04/07 23:11</p>
 * <p>@email:           chenshichao@outlook.com</p>
 */
public class LoginAuthRespHandler extends ChannelInboundHandlerAdapter {

    private NettyTcpClient imsClient;

    public LoginAuthRespHandler(NettyTcpClient imsClient) {
        this.imsClient = imsClient;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        MessageProtobuf.ImMessage handshakeRespMsg = (MessageProtobuf.ImMessage) msg;
        if (handshakeRespMsg == null ) {
            return;
        }

        MessageProtobuf.ImMessage handshakeMsg = imsClient.getHandshakeMsg();
        if (handshakeMsg == null) {
            return;
        }
        int handshakeMsgType = handshakeMsg.getTypeValue();
        if (handshakeMsgType == handshakeRespMsg.getTypeValue()) {
            System.out.println("------收到服务端im登录成功，message=" + handshakeRespMsg);
            int status = 1;
            try {
//                JSONObject jsonObj = JSON.parseObject(handshakeRespMsg.getExtend());
//                status = jsonObj.getIntValue("status");
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (status == 1) {
                    // 握手成功，马上先发送一条心跳消息，至于心跳机制管理，交由HeartbeatHandler
                    MessageProtobuf.ImMessage heartbeatMsg = imsClient.getHeartbeatMsg();
                    if (heartbeatMsg == null) {
                        return;
                    }

                    // 握手成功，检查消息发送超时管理器里是否有发送超时的消息，如果有，则全部重发
                    imsClient.getMsgTimeoutTimerManager().onResetConnected();

                    System.out.println("-----发送心跳消息1111111：" + heartbeatMsg + "当前心跳间隔为：" + imsClient.getHeartbeatInterval() + "ms\n");
                    imsClient.sendMsg(heartbeatMsg);

                    // 添加心跳消息管理handler
                    imsClient.addHeartbeatHandler();
                } else {
                    imsClient.resetConnect(false);// 握手失败，触发重连
                }
            }
        } else {
            // 消息透传
            ctx.fireChannelRead(msg);
        }
    }
}
