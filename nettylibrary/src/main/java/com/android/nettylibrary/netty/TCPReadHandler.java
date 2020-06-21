package com.android.nettylibrary.netty;

import android.text.TextUtils;
import android.util.Log;
import android.widget.TextView;

import com.android.nettylibrary.IMSConfig;
import com.android.nettylibrary.IMSNettyManager;
import com.android.nettylibrary.bean.IMMessageBean;
import com.android.nettylibrary.bean.IMMessageReceiveEvent;
import com.android.nettylibrary.protobuf.MessageProtobuf;
import com.android.nettylibrary.utils.IMLogUtil;

import org.greenrobot.eventbus.EventBus;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * <p>@ProjectName:     NettyChat</p>
 * <p>@ClassName:       TCPReadHandler.java</p>
 * <p>@PackageName:     com.freddy.im.netty</p>
 * <b>
 * <p>@Description:     消息接收处理handler</p>
 * </b>
 * <p>@author:          FreddyChen</p>
 * <p>@date:            2019/04/07 21:40</p>
 * <p>@email:           chenshichao@outlook.com</p>
 */
public class TCPReadHandler extends ChannelInboundHandlerAdapter {

    private NettyTcpClient imsClient;



    public TCPReadHandler(NettyTcpClient imsClient) {
        this.imsClient = imsClient;
    }


    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
        System.err.println("用户链接成功。。。。"+ctx.channel());
        String hostId = ((InetSocketAddress) ctx.channel().remoteAddress()).getAddress().getHostName();
        System.err.println("hostId000。。。1。"+NettyTcpClient.getInstance().getCurrentHost());
        NettyTcpClient.connectMap.put(NettyTcpClient.getInstance().getCurrentHost(),1);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        super.channelInactive(ctx);
        System.err.println("用户连接断开。。。。");
        EventBus.getDefault().post(new IMMessageReceiveEvent("0"));
        String hostId = ((InetSocketAddress) ctx.channel().remoteAddress()).getAddress().getHostName();
        System.err.println("hostId111。。。0。"+NettyTcpClient.getInstance().getCurrentHost());
        NettyTcpClient.connectMap.put(NettyTcpClient.getInstance().getCurrentHost(),0);
        Channel channel = ctx.channel();
        if (channel != null) {
            channel.close();
            ctx.close();
        }

        if(channel!=null && !channel.isActive()){
            imsClient.resetConnect(false);
        }
    }



    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        super.exceptionCaught(ctx, cause);
        System.err.println("TCPReadHandler 用户连接断开。。。。");
        EventBus.getDefault().post(new IMMessageReceiveEvent("0"));
        String hostId = ((InetSocketAddress) ctx.channel().remoteAddress()).getAddress().getHostName();
        System.err.println("hostId333。。。0。"+NettyTcpClient.getInstance().getCurrentHost());
        NettyTcpClient.connectMap.put(NettyTcpClient.getInstance().getCurrentHost(),0);
        Channel channel = ctx.channel();
        if (channel != null) {
            channel.close();
            ctx.close();
        }
//        IMSNettyManager.getInstance().regetImToken(IMSConfig.MSG_SYSTEM_RECONNET);//断线重新获取ip 端口  token
        // 触发重连
        if(channel!=null && !channel.isActive()){
            imsClient.resetConnect(false);
        }
    }

    public List<String> lastMsgId=new ArrayList<>();//用于过滤重复数据
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        MessageProtobuf.ImMessage message = (MessageProtobuf.ImMessage) msg;
        System.out.println("------收到服务器消息，messagege=" +message);
        //重复登录
        if(message!=null && message.getType()== MessageProtobuf.ImMessage.TypeEnum.USERSTATUS ){
            if(message.getUserStatus()==MessageProtobuf.ImMessage.UserStatusEnum.SQUEEZEOUT){ //代表别人登录你的账号
                IMSNettyManager.getInstance().regetImToken(IMSConfig.MSG_SYSTEM_RELOGIN);
                return;
            }
        }
        //登录失效
        if(message!=null && message.getType()== MessageProtobuf.ImMessage.TypeEnum.TOKEN){
            MessageProtobuf.ImMessage.ResponseStatusEnum responseStatus = message.getResponseStatus();
            if(responseStatus == MessageProtobuf.ImMessage.ResponseStatusEnum.FAIL){
                IMSNettyManager.getInstance().regetImToken(IMSConfig.MSG_SYSTEM_RECONNET);
                return;
            }
        }

        //过滤重复消息（）
        if(!TextUtils.isEmpty(message.getFingerprint())){ //用于过滤重复数据只支持text(todo)
            if(lastMsgId.contains(message.getFingerprint())){
                if(message.getType().equals(MessageProtobuf.ImMessage.TypeEnum.USERMSG)){
                    return;
                }
            }else{
                if(message.getType().equals(MessageProtobuf.ImMessage.TypeEnum.USERMSG)){
                    lastMsgId.add(message.getFingerprint());
                }
            }
        }


        if (message == null ) {
            return;
        }

        int msgType = message.getTypeValue();

        if (msgType == imsClient.getServerSentReportMsgType() ) {
            imsClient.getMsgTimeoutTimerManager().remove(message.getFingerprint());
            imsClient.getMsgDispatcher().receivedMsg(message);
        } else {
            // 收到消息后，立马给服务端回一条消息接收状态报告,若果是手的奥ack  就不发了
            if(message.getType()!= MessageProtobuf.ImMessage.TypeEnum.ACK){
                MessageProtobuf.ImMessage receivedReportMsg = buildReceivedReportMsg(message);
                if(receivedReportMsg != null) {
                    imsClient.sendMsg(receivedReportMsg);
                }
                // 接收消息，由消息转发器转发到应用层
                imsClient.getMsgDispatcher().receivedMsg(message);
            }

        }

    }

    /**
     * 收到服务器消息后回执
     * @param
     * @return
     */
    private MessageProtobuf.ImMessage buildReceivedReportMsg(MessageProtobuf.ImMessage msg) {
        if (TextUtils.isEmpty(msg.getFingerprint())) {
            return null;
        }
        MessageProtobuf.ImMessage.Builder builder = MessageProtobuf.ImMessage.newBuilder();
        builder.addFingerprints(msg.getFingerprint());
        builder.setType(MessageProtobuf.ImMessage.TypeEnum.RECEIPT);
        if(!TextUtils.isEmpty(IMSNettyManager.getMyToken())){
            builder.setToken(IMSNettyManager.getMyToken());
        }
        return builder.build();

    }
}
