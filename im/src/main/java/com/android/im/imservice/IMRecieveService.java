package com.android.im.imservice;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Vibrator;
import android.text.TextUtils;

import com.android.im.IMSHttpManager;
import com.android.im.IMSManager;
import com.android.im.IMSMsgManager;
import com.android.im.imbean.IMKickOutGroupBean;
import com.android.im.imeventbus.IMAppUpdataEvent;
import com.android.im.imeventbus.IMGroupMessageEvent;
import com.android.im.imeventbus.IMMessageGroupNotice;
import com.android.im.imeventbus.IMRegetGroupKickOutEvent;
import com.android.im.imutils.SoundUtils;
import com.android.nettylibrary.IMSConfig;
import com.android.nettylibrary.IMSNettyManager;
import com.android.nettylibrary.bean.IMMessageBean;
import com.android.nettylibrary.event.CEventCenter;
import com.android.nettylibrary.event.Events;
import com.android.nettylibrary.event.I_CEventListener;
import com.android.nettylibrary.greendao.entity.IMConversationBean;
import com.android.nettylibrary.greendao.entity.IMConversationDetailBean;
import com.android.nettylibrary.greendao.entity.IMGroupBean;
import com.android.nettylibrary.greendao.entity.IMSystemBean;
import com.android.nettylibrary.greendao.greenutils.DaoUtils;
import com.android.nettylibrary.http.IMMessageDataJson;
import com.android.nettylibrary.http.IMMessageKictOutJson;
import com.android.nettylibrary.http.IMSystemNoGroupBean;
import com.android.nettylibrary.netty.IMSClientBootstrap;
import com.android.nettylibrary.netty.NettyTcpClient;
import com.android.nettylibrary.protobuf.MessageProtobuf;
import com.android.nettylibrary.utils.IMLogUtil;
import com.android.nettylibrary.utils.IMPreferenceUtil;
import com.google.gson.Gson;

import org.greenrobot.eventbus.EventBus;

/**
 * 消息注册接收器
 */
public class IMRecieveService extends Service implements Runnable, I_CEventListener {
    private Thread mThread;
    private TimerThread mTimeThread;
    public int lastRecieveTime = 0; //记录最后接收消息的时间多少秒   120秒之后重连一次（处理时间久了接收消息报错的问题）
    private long lastVoicetime = 0; //记录上一次铃声的时间
    private static final String[] EVENTS = {
            Events.CHAT_SINGLE_MESSAGE,
            Events.CHAT_GROUP_MESSAGE
    };

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);
    }

    @SuppressLint("WrongConstant")
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, START_STICKY, startId);
    }

    @Override
    public boolean onUnbind(Intent intent) {
        return super.onUnbind(intent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        CEventCenter.unregisterEventListener(this, EVENTS);
    }

    private final IBinder mBinder = new MyBind();

    @Override
    public IBinder onBind(Intent intent) {
        if (mThread == null) {//如果线程为空 则创建一条
            mThread = new Thread(this);
            mTimeThread = new TimerThread();  //開啟一條縣城 检测时间重连
            mThread.start();
            mTimeThread.start();
        }
        return mBinder;
    }

    public class MyBind extends Binder {
        public IMRecieveService getService() {
            return IMRecieveService.this;
        }
    }

    @Override
    public void run() {        // 开始执行后台任务
        try {
            String preference_string = IMPreferenceUtil.getPreference_String(IMSConfig.IM_SAVE_HOST, IMSConfig.IMS_SERVICE_IP);
            IMSClientBootstrap.getInstance().init(preference_string, 1, getBaseContext());
            CEventCenter.registerEventListener(this, EVENTS);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 接收消息回调
     */
    @Override
    public void onCEvent(String topic, int msgCode, int resultCode, Object obj) {
        final IMMessageBean message = (IMMessageBean) obj;
        lastRecieveTime = 0;
        //过滤群自己发的消息 但是不能过滤红包(消息去重)
        if (handleSelfMessage(message)) {
            return;
        }
        //收到系统消息
        if (message.getType() == MessageProtobuf.ImMessage.TypeEnum.SYSTEM) {
            if (handleSysetemMessage(message)) {
                return;
            }
        }
        //收到拉取离线消息
        if (message.getType() == MessageProtobuf.ImMessage.TypeEnum.OFFLINEEVN) {
            IMSHttpManager.getInstance().IMHttpGetOffLineMessage();
            return;
        }
        //提示音1秒间隔提示一次(只提示消息声音)
        SoundUtils.getInstance().VoidandShakeSetting(message);

        switch (topic) {
            //单聊插入更新数据库是根据是不是单聊的sendid和cotumo判断是不是同一个人 然后再根据finger指纹判断是不是同一条消息
            case Events.CHAT_SINGLE_MESSAGE:
                if (singleHandleGroupSystemMessage(message)) {
                    return;
                }
                HandlerSigleMessage(message);
                break;
            //群聊插入更新数据库是根据是不是groupid 判断是不是同一个人 然后再根据finger指纹判断是不是同一条消息(此处逻辑类似)
            case Events.CHAT_GROUP_MESSAGE:
                HandlerGroupMessage(message, 0);
                break;
            default:
                break;
        }
    }

    /**
     * 处理单聊消息
     */
    private void HandlerSigleMessage(IMMessageBean message) {
        if (message.getType() != MessageProtobuf.ImMessage.TypeEnum.USERSTATUS && message.getType() !=
                MessageProtobuf.ImMessage.TypeEnum.SYSTEM && message.getType() !=
                MessageProtobuf.ImMessage.TypeEnum.READ) {
            if (message.getType() != MessageProtobuf.ImMessage.TypeEnum.SERVERRECEIPT) {  //收到的不是服务器的回执（代表是消息）
                //离线消息和消息都先保存数据库，不然离线消息不好处理
                //取出本地保存的未读条数，接到一条消息 未读消息加一，如果当前界面在对应的用户聊天界面，则在对应界面的接收消息出把值至为0
                String number = IMPreferenceUtil.getPreference_String(message.getSenderId() + IMSConfig.IM_UNEREAD_CONVERSATION_CHAT, "0");
                int unreadnumber = Integer.parseInt(number) + 1;
                IMPreferenceUtil.setPreference_String(message.getSenderId() + IMSConfig.IM_UNEREAD_CONVERSATION_CHAT, unreadnumber + "");
                IMSManager.getInstance().getUnreadMessageNumber();
                //收到消息时候保存数据库
                saveMessageIntoDao(message, false, 0);
            }
        }
        if (message.getType() == MessageProtobuf.ImMessage.TypeEnum.READ) {  //收到的已读回执跟新数据库的已读字段
            IMSMsgManager.updateReadedMessage(message);
        }
        EventBus.getDefault().post(new IMGroupMessageEvent(message, false));
    }

    /**
     * 处理跑到单聊消息中的群聊系统消息（因为这些消息没有groupId,所以判断到单聊消息来了）
     * 禁言。解除禁言因为没有加grouid,所以在这里进行发送到群里面做处
     */
    private boolean singleHandleGroupSystemMessage(IMMessageBean message) {
        if (!TextUtils.isEmpty(message.getData())) {
            IMMessageDataJson bean = new Gson().fromJson(message.getData(), IMMessageDataJson.class);
            if (Integer.parseInt(bean.getType()) == IMSConfig.IM_MESSAGE_SYSTEM) {
                switch (Integer.parseInt(bean.getSysCode())) {
                    case IMSConfig.IM_MESSAGE_BLACK:
                    case IMSConfig.IM_MESSAGE_NO_SPEAK:
                    case IMSConfig.IM_MESSAGE_NO_BLACK:
                    case IMSConfig.IM_MESSAGE_CAN_SPEAK:
                    case IMSConfig.IM_MESSAGE_KIOUTSELF:
                    case IMSConfig.IM_MESSAGE_CLOPNEGROUP:
                        HandlerGroupMessage(message, Integer.parseInt(bean.getSysCode()));
                        return true;
                    case IMSConfig.IM_MESSAGE_KF_UPDATE:      //客服用户信息
                    case IMSConfig.IM_MESSAGE_KF_DELETE:
                    case IMSConfig.IM_MESSAGE_KF_FOBIT:
                    case IMSConfig.IM_MESSAGE_KF_ADD:
                    case IMSConfig.IM_MESSAGE_KF_OFFLINE:
                    case IMSConfig.IM_MESSAGE_KF_ONLINE:
                    case IMSConfig.IM_MESSAGE_DELETE_COVER:
                    case IMSConfig.IM_MESSAGE_ADD_COVER:
                        return false;
                    case IMSConfig.IM_MESSAGE_GROUP_DIALOG:
                        if (bean.getMeta() == null) {
                            return true;
                        }
                        EventBus.getDefault().post(new IMMessageGroupNotice(new Gson().toJson(bean.getMeta())));
                        return true;
                    case IMSConfig.IM_MESSAGE_GROUP_LIMITE:
                        return false;
                    default:
                        return false;
                }
            }
        }
        return false;
    }

    /**
     * 处理群聊消息(isSystem=true代表系统消息 ，这里不能保存)
     */
    private void HandlerGroupMessage(IMMessageBean message, int sysCode) {
        if (sysCode != IMSConfig.IM_MESSAGE_GROUP_LIMITE && sysCode != IMSConfig.IM_MESSAGE_GROUP_DIALOG) {  //如果是不需要显示出来的消息 这里过滤（群权限改变）
            if (message.getType() != MessageProtobuf.ImMessage.TypeEnum.SERVERRECEIPT) {  //收到的不是服务器的回执（代表是消息）
                String grouip = null;
                IMMessageDataJson bean = new Gson().fromJson(message.getData(), IMMessageDataJson.class);
                switch (sysCode) {
                    case IMSConfig.IM_MESSAGE_BLACK:
                    case IMSConfig.IM_MESSAGE_NO_SPEAK:
                    case IMSConfig.IM_MESSAGE_NO_BLACK:
                    case IMSConfig.IM_MESSAGE_CAN_SPEAK:
                        grouip = (String) bean.getMeta();
                        break;
                    case IMSConfig.IM_MESSAGE_KIOUTSELF:
                    case IMSConfig.IM_MESSAGE_CLOPNEGROUP:
                        IMMessageKictOutJson data = new Gson().fromJson(new Gson().toJson(bean.getMeta()), IMMessageKictOutJson.class);
                        grouip = data.getGroupId();
                        break;
                    default:
                        grouip = message.getGroupId();
                        break;
                }
                //取出本地保存的未读条数，接到一条消息 未读消息加一，如果当前界面在对应的用户聊天界面，则在对应界面的接收消息出把值至为0
                String number = IMPreferenceUtil.getPreference_String(grouip + IMSConfig.IM_UNEREAD_CONVERSATION_CHAT, "0");
                int unreadnumber = Integer.parseInt(number) + 1;
                IMPreferenceUtil.setPreference_String(grouip + IMSConfig.IM_UNEREAD_CONVERSATION_CHAT, unreadnumber + "");
                IMSManager.getInstance().getUnreadMessageNumber();
                saveMessageIntoDao(message, true, sysCode);
            }
        }
        //如果不是系统消息才判断@我   不然报数据解析异常
        if (message.getType() != MessageProtobuf.ImMessage.TypeEnum.SYSTEM) {
            haveSomeoneAtMe(message);//群聊判断是不是有人@我
        }
        EventBus.getDefault().post(new IMGroupMessageEvent(message, true));
    }


    /**
     * 过滤群自己发的消息 但是不能过滤红包
     */
    private boolean handleSelfMessage(IMMessageBean message) {
        if (message != null && message.getType() == MessageProtobuf.ImMessage.TypeEnum.USERMSG && !TextUtils.isEmpty(message.getGroupId())) {
            if (message.getSenderId().equals(IMSNettyManager.getMyUseId())) {
                return true;
            }
            return false;
        }
        return false;
    }

    /**
     * 群聊判断是不是有人@我
     */
    private void haveSomeoneAtMe(IMMessageBean message) {
        if (TextUtils.isEmpty(message.getData())) {
            return;
        }
        IMMessageDataJson bean = new Gson().fromJson(message.getData(), IMMessageDataJson.class);
        if (bean.getNotices() == null || bean.getNotices().size() == 0) {
            return;
        }
        for (int i = 0; i < bean.getNotices().size(); i++) {
            if (bean.getNotices().get(i).equals(IMSManager.getMyUseId())) {
                IMPreferenceUtil.setPreference_Boolean(message.getGroupId() + IMSConfig.IM_SOMEONE_AT_ME, true);
                break;
            }
        }
    }

    /**
     * 保存数据库
     */
    private void saveMessageIntoDao(IMMessageBean message, boolean isgroup, int sysCode) {
        if (message != null && !TextUtils.isEmpty(message.getData())) {
            if (!isgroup) {
                IMSMsgManager.saveRecieveInsertMember(message);
                IMSMsgManager.saveRecieveInsertMessage(message);//再把消息保存至消息表中
            } else {
                IMSMsgManager.saveRecieveInsertGroupMember(message, sysCode);
                IMSMsgManager.saveRecieveInsertGroupMessage(message, sysCode);//再把消息保存至消息表中
            }

        }
    }

    /**
     * 计时线程（未收到消息每隔5分钟重连）
     */
    private class TimerThread extends Thread {
        @Override
        public void run() {
            while (true) {
                try {
                    if (lastRecieveTime >= 300) {
                        NettyTcpClient.getInstance().resetConnect();
                        lastRecieveTime = 0;
                    }
                    lastRecieveTime = lastRecieveTime + 1;
                    Thread.sleep(1000);//每隔1s执行一次
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 处理系统消息(当返回值为false时候，代表系统消息需要在对应的界面去处理,为true代表拦截)
     * (部分系统消息可能跟新数据库，皆在这里跟新)
     */
    private boolean handleSysetemMessage(IMMessageBean message) {
        if (TextUtils.isEmpty(message.getData())) {
            return true;
        }
        //重复过滤系统消息
        if (!TextUtils.isEmpty(message.getFingerprint())) {
            IMSystemBean imSystemBean = DaoUtils.getInstance().querySystemBean(message.getFingerprint());
            if (imSystemBean != null) {
                return true;
            }
            imSystemBean = new IMSystemBean();
            imSystemBean.setFingerprint(message.getFingerprint());
            DaoUtils.getInstance().insertSystemData(imSystemBean);
        }
        //处理不同类型系统消息
        IMMessageDataJson bean = new Gson().fromJson(message.getData(), IMMessageDataJson.class);
        if (Integer.parseInt(bean.getType()) == IMSConfig.IM_MESSAGE_SYSTEM) {  //表示系统消息类型
            if (TextUtils.isEmpty(bean.getSysCode())) {
                return true;
            }
            switch (Integer.parseInt(bean.getSysCode())) {
                case IMSConfig.IM_MESSAGE_JOINGROUP:
                case IMSConfig.IM_MESSAGE_KIOUTGROUP: //剔除群聊
                case IMSConfig.IM_MESSAGE_KIOUTSELF: //剔除群聊
                case IMSConfig.IM_MESSAGE_QUITGROUP: //退出群聊
                case IMSConfig.IM_MESSAGE_CLOPNEGROUP://解散群聊
                case IMSConfig.IM_MESSAGE_BLACK://拉黑
                case IMSConfig.IM_MESSAGE_NO_SPEAK: //禁言
                case IMSConfig.IM_MESSAGE_NO_BLACK://解除拉黑
                case IMSConfig.IM_MESSAGE_CAN_SPEAK://解除禁言
                case IMSConfig.IM_MESSAGE_GROUP_LIMITE: //群消息变更(和4000)
                case IMSConfig.IM_MESSAGE_ADD_COVER://增加会话
                case IMSConfig.IM_MESSAGE_DELETE_COVER:  //删除会话
                case IMSConfig.IM_MESSAGE_GROUP_DIALOG://每日弹窗
                case IMSConfig.IM_MESSAGE_ADD_FRIEND_SUCCESS://别人添加你为好有
                case IMSConfig.IM_MESSAGE_ADD_FRIEND_WAIT: //别人添加你为好有
                case IMSConfig.IM_MESSAGE_UPDATE_INFORE: //个人信息修改
                    return false;
                case IMSConfig.IM_MESSAGE_SELFJOINGROUP:
                case IMSConfig.IM_MESSAGE_GROUP_PESSION:
                    return true;
                case IMSConfig.IM_MESSAGE_TOTLE_OPEN: //修改主开关应用配置
                    String dataJson = new Gson().toJson(bean.getMeta());
                    IMPreferenceUtil.setPreference_String(IMSConfig.SAVE_AVAILAVBLE_SWICH, dataJson);
                    EventBus.getDefault().post(new IMAppUpdataEvent(dataJson));
                    return true;
                case IMSConfig.IM_MESSAGE_SEND_NO_FRIEND:   //别人不是你的友
                    IMSMsgManager.sendMessageNoFriend(message);
                    return false;
                case IMSConfig.IM_MESSAGE_FORBIT_FRIEND:   //屏蔽好友
                    IMSMsgManager.sendMessageForbit(message);
                    return false;
                case IMSConfig.IM_MESSAGE_SEND_NO_GROUP:   //解散该群
                case IMSConfig.IM_MESSAGE_REMOVE_GROUP: //被移除该群
                    IMSMsgManager.sendMessageNoGroup(message);
                    return false;
                case IMSConfig.IM_MESSAGE_CLX_TEAM:   //彩乐信团队的消息
                case IMSConfig.IM_MESSAGE_CLX_fENG:   //彩乐信团队的消息
                    IMSMsgManager.saveClxTeamSystemMessage(message);
                    return false;
            }
        }
        return true;
    }
}