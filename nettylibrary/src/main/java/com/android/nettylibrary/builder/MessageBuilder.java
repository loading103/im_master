package com.android.nettylibrary.builder;


import android.text.TextUtils;

import com.android.nettylibrary.IMSConfig;
import com.android.nettylibrary.bean.IMMessageBean;
import com.android.nettylibrary.protobuf.MessageProtobuf;
import com.android.nettylibrary.utils.IMPreferenceUtil;


/**
 * <p>@ProjectName:     BoChat</p>
 * <p>@ClassName:       MessageBuilder.java</p>
 * <p>@PackageName:     com.bochat.app.message</p>
 * <b>
 * <p>@Description:     消息转换</p>
 * </b>
 * <p>@author:          FreddyChen</p>
 * <p>@date:            2019/02/07 17:26</p>
 * <p>@email:           chenshichao@outlook.com</p>
 */
public class MessageBuilder {
    /**
     * 根据业务消息对象获取protoBuf消息对应的builder
     *
     * @return
     */
    public static MessageProtobuf.ImMessage.Builder getProtoBufMessageBuilderByAppMessage(IMMessageBean msg) {
        MessageProtobuf.ImMessage.Builder builder = MessageProtobuf.ImMessage.newBuilder();
        if(!TextUtils.isEmpty(msg.getData())){
            builder.setData(msg.getData());
        }
        if(!TextUtils.isEmpty(msg.getFingerprint())){
            builder.setFingerprint(msg.getFingerprint());
        }
        if(!TextUtils.isEmpty(msg.getSenderId())){
            builder.setSenderId(msg.getSenderId());
        }
        if(!TextUtils.isEmpty(msg.getGroupId())){
            builder.setGroupId(msg.getGroupId());
        }
        if(!TextUtils.isEmpty(msg.getReceiverId())){
            builder.setReceiverId(msg.getReceiverId());
        }
        if(!TextUtils.isEmpty(msg.getDeviceId())){
            builder.setDeviceId(msg.getDeviceId());
        }
        String token = IMPreferenceUtil.getPreference_String(IMSConfig.SAVE_IM_TOKEN, "");
        if(!TextUtils.isEmpty(token)){
            builder.setToken(token);
        }
        if(!TextUtils.isEmpty(msg.getClientId())){
            builder.setClientId(msg.getClientId());
        }
        builder.setDeviceType(MessageProtobuf.ImMessage.DeviceTypeEnum.Android);
        builder.setTimestamp(msg.getTimestamp());
        builder.setType(msg.getType());
        return builder;
    }

    /**
     * 通过protobuf消息对象获取业务消息对象
     */
    public static IMMessageBean getMessageByProtobuf(MessageProtobuf.ImMessage message) {
        IMMessageBean messages = new IMMessageBean();
        if(!TextUtils.isEmpty(message.getData())){
            messages.setData(message.getData());
        }
        if(!TextUtils.isEmpty(message.getFingerprint())){
            messages.setFingerprint(message.getFingerprint());
        }
        if(!TextUtils.isEmpty(message.getSenderId())){
            messages.setSenderId(message.getSenderId());
        }
        if(!TextUtils.isEmpty(message.getGroupId())){
            messages.setGroupId(message.getGroupId());
        }
        if(!TextUtils.isEmpty(message.getReceiverId())){
            messages.setReceiverId(message.getReceiverId());
        }
        if(!TextUtils.isEmpty(message.getDeviceId())){
            messages.setDeviceId(message.getDeviceId());
        }
        messages.setUserStatus(message.getUserStatus());
        messages.setClientId(message.getClientId());
        messages.setTimestamp(message.getTimestamp());
        messages.setType(message.getType());
        messages.setMsgType(message.getMsgType());
        return messages;
    }
}
