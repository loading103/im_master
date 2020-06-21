package com.android.nettylibrary.netty;

import android.content.Context;

import androidx.annotation.NonNull;

import com.android.nettylibrary.imconstant.IMSClientFactory;
import com.android.nettylibrary.interfac.IMSClientInterface;
import com.android.nettylibrary.listener.IMSConnectStatusListener;
import com.android.nettylibrary.listener.IMSEventListener;
import com.android.nettylibrary.protobuf.MessageProtobuf;

import java.util.Vector;

/**
 * <p>@ProjectName:     NettyChat</p>
 * <p>@ClassName:       IMSClientBootstrap.java</p>
 * <p>@PackageName:     com.freddy.chat.im</p>
 * <b>
 * <p>@Description:     应用层的imsClient启动器</p>
 * </b>
 * <p>@author:          FreddyChen</p>
 * <p>@date:            2019/04/08 00:25</p>
 * <p>@email:           chenshichao@outlook.com</p>
 */
public class IMSClientBootstrap {
    public Context  context;

    public Context getContext() {
        return context;
    }

    private static final IMSClientBootstrap INSTANCE = new IMSClientBootstrap();
    private IMSClientInterface imsClient;
    private boolean isActive;

    private IMSClientBootstrap() {

    }

    public static IMSClientBootstrap getInstance() {
        return INSTANCE;
    }


    public synchronized void init( String hosts, int appStatus,@NonNull Context context) {
        System.out.println("init IMLibClientBootstrap error,ims hosts is null");
        this.context=context.getApplicationContext();
        if (!isActive()) {
            Vector<String> serverUrlList = convertHosts(hosts);
            if (serverUrlList == null || serverUrlList.size() == 0) {
                System.out.println("init IMLibClientBootstrap error,ims hosts is null");
                return;
            }

            isActive = true;
            System.out.println("init IMLibClientBootstrap, servers=" + hosts);
            if (null != imsClient) {
                imsClient.close();
            }
            imsClient = IMSClientFactory.getIMSClient();
            updateAppStatus(appStatus);
            imsClient.init(serverUrlList, new IMSEventListener(), new IMSConnectStatusListener());
        }
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    /**
     * 发送消息
     *
     * @param msg
     */
    public void sendMessage(MessageProtobuf.ImMessage msg) {
        if (isActive) {
            imsClient.sendMsg(msg);
        }
    }

    private Vector<String> convertHosts(String hosts) {
//        if (hosts != null && hosts.length() > 0) {
//            JSONArray hostArray = JSONArray.parseArray(hosts);
//            if (null != hostArray && hostArray.size() > 0) {
//                Vector<String> serverUrlList = new Vector<String>();
//                JSONObject host;
//                for (int i = 0; i < hostArray.size(); i++) {
//                    host = JSON.parseObject(hostArray.get(i).toString());
//                    serverUrlList.add(host.getString("host") + " "
//                            + host.getInteger("port"));
//                }
//                return serverUrlList;
//            }
//        }

        Vector<String> serverUrlList = new Vector<String>();
        String replace = hosts.replace(":", " ");
        serverUrlList.add(replace);
        return serverUrlList;
    }

    public void updateAppStatus(int appStatus) {
        if (imsClient == null) {
            return;
        }

        imsClient.setAppStatus(appStatus);
    }
}
