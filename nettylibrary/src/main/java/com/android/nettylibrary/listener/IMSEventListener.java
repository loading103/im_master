package com.android.nettylibrary.listener;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.text.TextUtils;

import com.android.nettylibrary.IMSConfig;
import com.android.nettylibrary.bean.MessageType;
import com.android.nettylibrary.builder.MessageBuilder;
import com.android.nettylibrary.handler.MessageProcessor;
import com.android.nettylibrary.protobuf.MessageProtobuf;
import com.android.nettylibrary.utils.IMDeviceIdUtil;
import com.android.nettylibrary.utils.IMPreferenceUtil;

import java.util.UUID;

import static com.android.nettylibrary.bean.MessageType.SERVERRECEIPT;

/**
 * <p>@ProjectName:     NettyChat</p>
 * <p>@ClassName:       IMSEventListener.java</p>
 * <p>@PackageName:     com.freddy.chat.im</p>
 * <b>
 * <p>@Description:     与ims交互的listener</p>
 * </b>
 * <p>@author:          FreddyChen</p>
 * <p>@date:            2019/04/07 23:55</p>
 * <p>@email:           chenshichao@outlook.com</p>
 */
public class IMSEventListener implements OnEventListener {

    private String userId;
    private String token;
    public IMSEventListener() {
        this.userId = IMPreferenceUtil.getPreference_String(IMSConfig.SAVE_USERID, IMSConfig.USER_ID);
        this.token = IMPreferenceUtil.getPreference_String(IMSConfig.SAVE_IM_TOKEN, IMSConfig.USER_ID);
    }

    /**
     * 接收ims转发过来的消息
     *
     * @param msg
     */
    @Override
    public void dispatchMsg(MessageProtobuf.ImMessage msg) {
        MessageProcessor.getInstance().receiveMsg(MessageBuilder.getMessageByProtobuf(msg));
    }

    /**
     * 网络是否可用
     *
     * @return
     */
    @Override
    public boolean isNetworkAvailable(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = cm.getActiveNetworkInfo();
        return info != null && info.isConnected();
    }

    /**
     * 设置ims重连间隔时长，0表示默认使用ims的值
     *
     * @return
     */
    @Override
    public int getReconnectInterval() {
        return 0;
    }

    /**
     * 设置ims连接超时时长，0表示默认使用ims的值
     *
     * @return
     */
    @Override
    public int getConnectTimeout() {
        return 0;
    }

    /**
     * 设置应用在前台时ims心跳间隔时长，0表示默认使用ims的值
     *
     * @return
     */
    @Override
    public int getForegroundHeartbeatInterval() {
        return 0;
    }

    /**
     * 设置应用在后台时ims心跳间隔时长，0表示默认使用ims的值
     *
     * @return
     */
    @Override
    public int getBackgroundHeartbeatInterval() {
        return 0;
    }

    /**
     *(类型登录)
     */
    @Override
    public   MessageProtobuf.ImMessage getHandshakeMsg() {
        MessageProtobuf.ImMessage.Builder message = MessageProtobuf.ImMessage.newBuilder();
        message.setFingerprint(UUID.randomUUID().toString());
        message.setType(MessageProtobuf.ImMessage.TypeEnum.INFORMATION);
        String deviceId = IMDeviceIdUtil.getDeviceID();
        message.setDeviceType(MessageProtobuf.ImMessage.DeviceTypeEnum.Android);
        String token = IMPreferenceUtil.getPreference_String(IMSConfig.SAVE_IM_TOKEN, "");
        if(!TextUtils.isEmpty(token)){
            message.setToken(token);
        }
        if(!TextUtils.isEmpty(deviceId)){
            message.setDeviceId(deviceId);
        }
        return message.build();
    }
    /**
     * 构建心跳消息
     *
     * @return
     */
    @Override
    public MessageProtobuf.ImMessage getHeartbeatMsg() {
        MessageProtobuf.ImMessage.Builder builder = MessageProtobuf.ImMessage.newBuilder();
        builder.setType(MessageProtobuf.ImMessage.TypeEnum.HEARTBEAT);
        if (!TextUtils.isEmpty(IMPreferenceUtil.getPreference_String(IMSConfig.SAVE_IM_TOKEN, ""))) {
            builder.setToken(IMPreferenceUtil.getPreference_String(IMSConfig.SAVE_IM_TOKEN, ""));
        }
        builder.setDeviceType(MessageProtobuf.ImMessage.DeviceTypeEnum.Android);
        return   builder.build();
    }
    /**
     * 服务端返回的消息发送状态报告消息类型
     * @return
     */
    @Override
    public int getServerSentReportMsgType() {
        return SERVERRECEIPT.getMsgType();
    }

    /**
     * 客户端提交的消息接收状态报告消息类型
     *
     * @return
     */
    @Override
    public int getClientReceivedReportMsgType() {
        return MessageType.RECEIPT.getMsgType();
    }

    /**
     * 设置ims消息发送超时重发次数，0表示默认使用ims的值
     *
     * @return
     */
    @Override
    public int getResendCount() {
        return 0;
    }

    /**
     * 设置ims消息发送超时重发间隔时长，0表示默认使用ims的值
     *
     * @return
     */
    @Override
    public int getResendInterval() {
        return 0;
    }
}
