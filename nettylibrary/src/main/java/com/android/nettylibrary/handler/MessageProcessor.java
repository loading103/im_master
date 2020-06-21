package com.android.nettylibrary.handler;

import android.text.TextUtils;
import android.util.Log;

import com.android.nettylibrary.IMSConfig;
import com.android.nettylibrary.IMSNettyManager;
import com.android.nettylibrary.bean.IMMessageBean;
import com.android.nettylibrary.builder.IMessageProcessor;
import com.android.nettylibrary.builder.MessageBuilder;
import com.android.nettylibrary.netty.IMSClientBootstrap;
import com.android.nettylibrary.utils.IMCThreadPoolExecutor;

/**
 * <p>@ProjectName:     NettyChat</p>
 * <p>@ClassName:       MessageProcessor.java</p>
 * <p>@PackageName:     com.freddy.chat.im</p>
 * <b>
 * <p>@Description:     消息处理器</p>
 * </b>
 * <p>@author:          FreddyChen</p>
 * <p>@date:            2019/04/10 03:27</p>
 * <p>@email:           chenshichao@outlook.com</p>
 */
public class MessageProcessor implements IMessageProcessor {

    private static final String TAG = MessageProcessor.class.getSimpleName();

    private MessageProcessor() {

    }

    private static class MessageProcessorInstance {
        private static final IMessageProcessor INSTANCE = new MessageProcessor();
    }

    public static IMessageProcessor getInstance() {
        return MessageProcessorInstance.INSTANCE;
    }

    /**
     * 接收消息
     * @param message
     */
    @Override
    public void receiveMsg(final IMMessageBean message) {
        IMCThreadPoolExecutor.runInBackground(new Runnable() {

            @Override
            public void run() {
                try {
                    IMessageHandler messageHandler = null;
                    if (TextUtils.isEmpty(message.getGroupId())) { //如果有groupid 就是群聊 反之单聊
                        messageHandler = MessageHandlerFactory.getHandlerByMsgType(0);
                    } else {
                        messageHandler = MessageHandlerFactory.getHandlerByMsgType(1);
                    }
                    if (messageHandler != null) {
                        messageHandler.execute(message);
                    } else {
                        Log.e(TAG, "未找到消息处理handler，msgType=" + message.getType());
                    }
                } catch (Exception e) {
                    Log.e(TAG, "消息处理出错，reason=" + e.getMessage());
                }
            }
        });
    }


    /**
     * 发送消息
     *
     * @param message
     */
    @Override
    public void sendMsg(final IMMessageBean message) {
        IMCThreadPoolExecutor.runInBackground(new Runnable() {

            @Override
            public void run() {
                boolean isActive = IMSClientBootstrap.getInstance().isActive();
                if (isActive) {
                    IMSClientBootstrap.getInstance().sendMessage(MessageBuilder.getProtoBufMessageBuilderByAppMessage(message).build());
                } else {
                    Log.e("-----", "发送消息失败");
                    IMSNettyManager.getInstance().regetImToken(IMSConfig.MSG_SYSTEM_DISSCONNECT);
                }
            }
        });
    }

}
