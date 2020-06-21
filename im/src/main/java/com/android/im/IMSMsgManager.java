package com.android.im;

import android.text.TextUtils;

import com.android.im.imbean.IMClxTeamMessageBean;
import com.android.im.imeventbus.IMCreatMessageEvent;
import com.android.im.imeventbus.IMSendGroupMessageEvent;
import com.android.im.imeventbus.IMZhuaFaDataEvent;
import com.android.nettylibrary.IMSConfig;
import com.android.nettylibrary.bean.IMMessageBean;
import com.android.nettylibrary.greendao.entity.IMConversationBean;
import com.android.nettylibrary.greendao.entity.IMConversationDetailBean;
import com.android.nettylibrary.greendao.entity.IMGroupBean;
import com.android.nettylibrary.greendao.entity.IMPersonBean;
import com.android.nettylibrary.greendao.greenutils.DaoUtils;
import com.android.nettylibrary.handler.MessageProcessor;
import com.android.nettylibrary.http.IMMessagSystemJson;
import com.android.nettylibrary.http.IMMessageDataJson;
import com.android.nettylibrary.http.IMMessageKictOutJson;
import com.android.nettylibrary.http.IMSystemNoGroupBean;
import com.android.nettylibrary.http.IMSystemSomeOneAddGroup;
import com.android.nettylibrary.http.IMUserInforBean;
import com.android.nettylibrary.netty.NettyTcpClient;
import com.android.nettylibrary.protobuf.MessageProtobuf;
import com.android.nettylibrary.utils.IMLogUtil;
import com.android.nettylibrary.utils.IMPreferenceUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;


public class IMSMsgManager {

    /**
     * 保存用户信息
     */
    public static void SaveLoginData(IMUserInforBean.UserInforData data) {
        IMPreferenceUtil.setPreference_String(IMSConfig.SAVE_USERID,data.getCustomerId());
        IMPreferenceUtil.setPreference_String(IMSConfig.SAVE_NAME,data.getNickName());
        IMPreferenceUtil.setPreference_String(IMSConfig.SIGNATURE,data.getSignature());
        IMPreferenceUtil.setPreference_String(IMSConfig.SAVE_HEADURL,data.getAvatar());
        IMPreferenceUtil.setPreference_String(IMSConfig.SAVE_USERTYPE,data.getType());
        IMPreferenceUtil.setPreference_String(IMSConfig.SAVE_TITLE,data.getTitle());
        IMPreferenceUtil.setPreference_String(IMSConfig.SAVE_LEVEL,data.getLevel());
        IMPreferenceUtil.setPreference_String(IMSConfig.SAVE_MONEY,data.getLotteryBalance());
        IMPreferenceUtil.setPreference_String(IMSConfig.SAVE_PAYWORD,data.getIsPayPwd());
        IMPreferenceUtil.setPreference_String(IMSConfig.SAVE_THIRDUID,data.getThirdUid());
        IMPreferenceUtil.setPreference_String(IMSConfig.SAVE_FRIENDLYVALU,data.getIsFriendValid());
        String username = IMPreferenceUtil.getPreference_String(IMSConfig.SAVE_USER_NAME, "");
        if(!username.equals(data.getUsername())){
            DaoUtils.getInstance().deleteMessageData();
        }
        IMPreferenceUtil.setPreference_String(IMSConfig.SAVE_USER_NAME,data.getUsername());
    }

    /**（单聊）
     * 发送消息到服务器（发送的消息和本地保存的字段不一样）
     */
    public static void sendMessage(IMConversationDetailBean messages, String customId, DaoUtils daoUtils,boolean ispicture) {
        IMMessageBean message = new IMMessageBean();
        message.setFingerprint(messages.getFingerprint());
        message.setMsgType(messages.getMsgType());

        message.setData(messages.getData());
        message.setType(MessageProtobuf.ImMessage.TypeEnum.USERMSG);
        message.setReceiverId(customId);
        if(!TextUtils.isEmpty(IMSManager.getMyToken())){
            message.setToken(IMSManager.getMyToken());
        }
        if(!TextUtils.isEmpty(messages.getOwnurl())){
            saveSendInsertMessage(message,daoUtils,messages.getOwnurl(),messages.getOwnfirstFrame(),messages.getPictureTime(),ispicture);
        }else {
            saveSendInsertMessage(message,daoUtils,null,null,messages.getPictureTime(),ispicture);
        }
    }
    /**
     *（单聊）
     * 发送信息保存数据库
     * 把消息保存至消息表中和更新跟新会话列表的人员信息表(保存时间   处理发送消息退出界面进入发送失败的情况，服务器为准)
     */
    public static  void saveSendInsertMessage(IMMessageBean message, DaoUtils daoUtils ,String ownurl,String ownfirstframe,long time ,boolean ispicture) {
        //先保存消息表
        IMConversationDetailBean bean=new IMConversationDetailBean();
        bean.setFingerprint(message.getFingerprint());
        bean.setData(message.getData());
        bean.setMsgType(message.getMsgType());
        bean.setLastMsgSendType(IMSConfig.MSG_SEND);    //自己发送的
        bean.setTimestamp(System.currentTimeMillis());
        bean.setSendstate(IMSConfig.IM_SEND_STATE_SENDING); //1是发送中
        bean.setUserId(message.getReceiverId());
        bean.setReceiverId(message.getReceiverId());
        bean.setIsReaded(false);
        bean.setPictureTime(time);
        bean.setConversationId(message.getReceiverId());
        bean.setCurrentUid(IMSManager.getMyUseId());
        if(!TextUtils.isEmpty(ownurl)){
            bean.setOwnurl(ownurl);
        }
        if(!TextUtils.isEmpty(ownfirstframe)){
            bean.setOwnfirstFrame(ownfirstframe);
        }
        IMConversationDetailBean data = daoUtils.queryConversationDetailDataAccordFieldFiger(message.getFingerprint());
        if(data==null){
            daoUtils.insertConversationDetailData(bean);    //如果是发送失败的数据重新发送数据 需要更新数据库  不能插入
        }else {
            data.setData(message.getData());
            data.setSendstate(IMSConfig.IM_SEND_STATE_SENDING); //1是发送中
            daoUtils.updateConversationDetailData(data);
        }
        //在跟新会话列表的人员信息表
        IMConversationBean imPersonBean = daoUtils.queryConversationBean(message.getReceiverId());
        if(imPersonBean==null){   //主页界面没有当前的群就创建item会话
            IMConversationBean dataws = IMSMsgManager.creatSendPersonConversation(message.getReceiverId(), bean);
            if(dataws==null){
                return;
            }
            daoUtils.insertConversationData(dataws);
            EventBus.getDefault().post(new IMCreatMessageEvent(dataws));
            imPersonBean=dataws;
        }
        imPersonBean.setLastMessageContent(message.getData());
        IMMessageDataJson json = new Gson().fromJson(message.getData(), IMMessageDataJson.class);
        imPersonBean.setLastMessageType(Integer.parseInt(json.getType()));

        imPersonBean.setLastMessageSendType(IMSConfig.MSG_SEND);
        imPersonBean.setLastMessageSendstate(IMSConfig.IM_SEND_STATE_SENDING); //1是发送中
        imPersonBean.setLastMessageTime(System.currentTimeMillis());
        imPersonBean.setLastMessageId(message.getFingerprint());
        imPersonBean.setLastSendId(IMSManager.getMyUseId());
        daoUtils.updateConversationData(imPersonBean);
        if(!ispicture){
            MessageProcessor.getInstance().sendMsg(message);
        }
    }



    /**
     * （单聊）
     * 收到消息把消息更新至会话列表中数据库
     */
    public static  void saveRecieveInsertMember(IMMessageBean message) {

        String userId="";
        if(message.getSenderId().equals(IMSManager.getMyUseId())){
            userId=message.getReceiverId();
        }else {
            userId=message.getSenderId();
        }

        IMConversationBean imConversationBean =null;
        imConversationBean = DaoUtils.getInstance().queryConversationBean(userId);
        if (imConversationBean != null) { //本地有好友 插入数会话人员列表更新界面
            long lastMessageTime = imConversationBean.getLastMessageTime(); //比对收到的最新一条消息是不是大于保存的一条消息时间 ，大于的话才保存（避免离线消息）
            long messagetime = message.getTimestamp();
            if(messagetime>lastMessageTime){
                imConversationBean.setLastMessageContent(message.getData());
                IMMessageDataJson json = new Gson().fromJson(message.getData(), IMMessageDataJson.class);
                imConversationBean.setLastMessageType(Integer.parseInt(json.getType()));
                imConversationBean.setLastMessageSendType(IMSConfig.MSG_RECIEVE);
                imConversationBean.setLastMessageTime(message.getTimestamp());
                if(!TextUtils.isEmpty(message.getSenderId())){
                    imConversationBean.setLastSendId(message.getSenderId());
                }
                DaoUtils.getInstance().updateConversationData(imConversationBean);
            }
        }
    }
    /**
     *
     * （单聊）
     * 收到消息把消息保存至消息表中
     * (userid)是人或是群 在message表中的标志，如果是人，则为发送的人sendid，若果是群则是groupid
     */
    public static  void saveRecieveInsertMessage(IMMessageBean message) {
        String userId="";
        if(message.getSenderId().equals(IMSManager.getMyUseId())){
            userId=message.getReceiverId();
        }else {
            userId=message.getSenderId();
        }
        IMConversationDetailBean bean=new IMConversationDetailBean();
        bean.setFingerprint(message.getFingerprint());
        bean.setTimestamp(message.getTimestamp());
        bean.setData(message.getData());
        bean.setMsgType(message.getMsgType());
        bean.setLastMsgSendType(IMSConfig.MSG_RECIEVE);
        bean.setSenderId(message.getSenderId());
        bean.setUserId(userId);
        bean.setConversationId(userId);
        bean.setCurrentUid(IMSManager.getMyUseId());
        DaoUtils.getInstance().insertConversationDetailData(bean);
    }

    /**
     *
     * （单聊）
     *更新数据库消息的已读信息
     */
    public static  void updateReadedMessage(IMMessageBean message) {
        if(TextUtils.isEmpty(message.getFingerprint())){
            List<IMConversationDetailBean> datas = DaoUtils.getInstance().queryConversationDetailDataAccordField(message.getSenderId());
            for (int i = 0; i < datas.size(); i++) {
                if(!datas.get(i).getIsReaded()){
                    datas.get(i).setIsReaded(true);
                    DaoUtils.getInstance().updateConversationDetailData( datas.get(i));
                }

            }
        }else {
            IMConversationDetailBean imPersonDetailBean = DaoUtils.getInstance().queryConversationDetailDataAccordFieldFiger(message.getFingerprint());
            if(imPersonDetailBean==null){
                return;
            }
            imPersonDetailBean.setIsReaded(true);
            DaoUtils.getInstance().updateConversationDetailData(imPersonDetailBean);
        }
    }

    /**
     * （群聊）
     * 发送消息到服务器（发送的消息和本地保存的字段不一样）
     * isPicture//如果是图片上屏幕就不准发出这条消息
     */
    public static void sendGroupMessage(IMConversationDetailBean messages, String groupId, DaoUtils daoUtils,boolean isPicture) {
        IMMessageBean message = new IMMessageBean();
        message.setFingerprint(messages.getFingerprint());
        message.setMsgType(messages.getMsgType());
        message.setData(messages.getData());
        message.setType(MessageProtobuf.ImMessage.TypeEnum.USERMSG);
        if(!TextUtils.isEmpty(IMSManager.getMyToken())){
            message.setToken(IMSManager.getMyToken());
        }
        message.setGroupId(groupId);
        if(!TextUtils.isEmpty(messages.getOwnurl())){
            saveSendInsertGroupMessage(message,daoUtils,messages.getOwnurl(),isPicture,messages.getPictureTime());
        }else {
            saveSendInsertGroupMessage(message,daoUtils,null,isPicture,messages.getPictureTime());
        }


    }
    /**
     *（群聊）
     * 发送信息保存数据库
     * 把消息保存至消息表中和更新跟新会话列表的人员信息表(保存时间   处理发送消息退出界面进入发送失败的情况，服务器为准)
     */
    public static  void saveSendInsertGroupMessage(IMMessageBean message, DaoUtils daoUtils,String ownurl,boolean isPicture,long pictureTime) {
        //先保存消息表
        IMConversationDetailBean bean=new IMConversationDetailBean();
        bean.setFingerprint(message.getFingerprint());
        bean.setData(message.getData());
        bean.setMsgType(message.getMsgType());
        bean.setLastMsgSendType(IMSConfig.MSG_SEND);    //自己发送的
        bean.setTimestamp(System.currentTimeMillis());
        bean.setSendstate(IMSConfig.IM_SEND_STATE_SENDING); //1是发送中
        bean.setPictureTime(pictureTime);
        bean.setUserId(message.getGroupId());   //userid在此处是关联表用的字段
        bean.setGroupId(message.getGroupId());
        bean.setConversationId(message.getGroupId());
        bean.setCurrentUid(IMSManager.getMyUseId());
        if(!TextUtils.isEmpty(ownurl)){
            bean.setOwnurl(ownurl);
        }
        IMConversationDetailBean imGroupDetailBean = daoUtils.queryConversationDetailDataAccordFieldFiger(message.getFingerprint());
        if(imGroupDetailBean==null){
            daoUtils.insertConversationDetailData(bean);    //如果是发送失败的数据重新发送数据 需要更新数据库  不能插入
        }else {
            imGroupDetailBean.setData(message.getData());
            imGroupDetailBean.setSendstate(IMSConfig.IM_SEND_STATE_SENDING); //1是发送中
            daoUtils.updateConversationDetailData(imGroupDetailBean);
        }

        //在跟新会话列表的人员信息表
        IMConversationBean groupBean = daoUtils.queryConversationBean(message.getGroupId());
        if(groupBean==null){   //主页界面没有当前的群就创建item会话
            IMConversationBean data = creatSendGroupConversation(message.getGroupId(), bean);
            if(data==null){
                return;
            }
            daoUtils.insertConversationData(data);
            EventBus.getDefault().post(new IMCreatMessageEvent(data));
            groupBean=data;
        }
        groupBean.setLastMessageContent(message.getData());
        IMMessageDataJson json = new Gson().fromJson(message.getData(), IMMessageDataJson.class);
        groupBean.setLastMessageType(Integer.parseInt(json.getType()));
        groupBean.setLastMessageSendType(IMSConfig.MSG_SEND);
        groupBean.setLastMessageSendstate(IMSConfig.IM_SEND_STATE_SENDING); //1是发送中
        groupBean.setLastMessageTime(System.currentTimeMillis());
        groupBean.setLastMessageId(message.getFingerprint());
        groupBean.setLastSendId(IMSManager.getMyUseId());
        daoUtils.updateConversationData(groupBean);
        if(!isPicture){ //如果是图片上屏幕就不准发出这条消息
            MessageProcessor.getInstance().sendMsg(message);
        }
    }

    /**
     *  (群聊)
     * 收到消息把消息更新至会话列表中数据库
     */
    public static  void saveRecieveInsertGroupMember(IMMessageBean message,int sysCode) {
        IMConversationBean conversationBean =null;
        String groupId=null;
        switch (sysCode) {
            case IMSConfig.IM_MESSAGE_BLACK:
            case IMSConfig.IM_MESSAGE_NO_SPEAK:
            case IMSConfig.IM_MESSAGE_NO_BLACK:
            case IMSConfig.IM_MESSAGE_CAN_SPEAK:
                IMMessageDataJson bean = new Gson().fromJson(message.getData(), IMMessageDataJson.class);
                groupId = (String) bean.getMeta();
                break;
            case IMSConfig.IM_MESSAGE_KIOUTSELF:
            case IMSConfig.IM_MESSAGE_CLOPNEGROUP:
                IMMessageDataJson bean1 = new Gson().fromJson(message.getData(), IMMessageDataJson.class);
                IMMessageKictOutJson data = new Gson().fromJson(new Gson().toJson(bean1.getMeta()), IMMessageKictOutJson.class);
                groupId = data.getGroupId();
                break;
            default:
                groupId = message.getGroupId();
                break;
        }
        conversationBean =  DaoUtils.getInstance().queryConversationBean(groupId);
        if (conversationBean != null) { //本地有好友 插入数会话人员列表更新界面
            long lastMessageTime = conversationBean.getLastMessageTime(); //比对收到的最新一条消息是不是大于保存的一条消息时间 ，大于的话才保存（避免离线消息）
            long messagetime = message.getTimestamp();
            long a=messagetime-lastMessageTime;
            if(a>0) {
                conversationBean.setLastMessageContent(message.getData());
                IMMessageDataJson json = new Gson().fromJson(message.getData(), IMMessageDataJson.class);
                String  type=json.getType();
                conversationBean.setLastMessageType(Integer.parseInt(type));
                conversationBean.setLastMessageSendType(IMSConfig.MSG_RECIEVE);
                if(!TextUtils.isEmpty(message.getSenderId())){
                    conversationBean.setLastSendId(message.getSenderId());
                }
                conversationBean.setLastMessageTime(message.getTimestamp());
                DaoUtils.getInstance().updateConversationData(conversationBean);
            }
        }
    }
    /**
     * (群聊)
     * 收到消息把消息保存至消息表中
     */
    public static  void saveRecieveInsertGroupMessage(IMMessageBean message,int sysCode) {

        String groupId=null;
        switch (sysCode) {
            case IMSConfig.IM_MESSAGE_BLACK:
            case IMSConfig.IM_MESSAGE_NO_SPEAK:
            case IMSConfig.IM_MESSAGE_NO_BLACK:
            case IMSConfig.IM_MESSAGE_CAN_SPEAK:
                IMMessageDataJson bean = new Gson().fromJson(message.getData(), IMMessageDataJson.class);
                groupId = (String) bean.getMeta();
                break;
            case IMSConfig.IM_MESSAGE_KIOUTSELF:
            case IMSConfig.IM_MESSAGE_CLOPNEGROUP:
                IMMessageDataJson bean1 = new Gson().fromJson(message.getData(), IMMessageDataJson.class);
                IMMessageKictOutJson data = new Gson().fromJson(new Gson().toJson(bean1.getMeta()), IMMessageKictOutJson.class);
                groupId = data.getGroupId();
                break;
            default:
                groupId = message.getGroupId();
                break;
        }
        IMConversationDetailBean bean=new IMConversationDetailBean();
        bean.setFingerprint(message.getFingerprint());
        bean.setTimestamp(message.getTimestamp());
        bean.setData(message.getData());
        bean.setMsgType(message.getMsgType());
        bean.setLastMsgSendType(IMSConfig.MSG_RECIEVE);
        bean.setSenderId(message.getSenderId());
        bean.setGroupId(groupId);
        bean.setUserId(groupId);
        bean.setConversationId(groupId);
        bean.setCurrentUid(IMSManager.getMyUseId());
        DaoUtils.getInstance().insertConversationDetailData(bean);
    }


    /**
     * 转成字符串
     * userid@哪些人
     */
    public static String handleMessageDataJson(String userid,String text,IMMessageDataJson data,int type) {
        IMMessageDataJson bean=new IMMessageDataJson();
        //添加@功能
        if(!TextUtils.isEmpty(userid)){
            String[] split = userid.split(",");
            List<String>userids=new ArrayList<>();
            for (int i = 0; i < split.length; i++) {
                userids.add(split[i]);
            }
            bean.setNotices(userids);
        }
        bean.setType(type+"");
        bean.setText(text);
        bean.setNickName(IMSManager.getMyNickName());
        bean.setAvaterUrl(IMSManager.getMyHeadView());
        //不同类型添加数据
        switch (type){
            case IMSConfig.IM_MESSAGE_TEXT:     //文字表情
                bean.setText(text);
                break;
            case IMSConfig.IM_MESSAGE_PICTURE:     //图片
                if(data==null){
                    return null;
                }
                bean.setText(text);
                bean.setImages(data.getImages());
                bean.setThumbImages(data.getThumbImages());
                bean.setOwnerurl(data.getOwnerurl());
                bean.setWidth(data.getWidth());
                bean.setHight(data.getHight());
                break;
            case IMSConfig.IM_MESSAGE_VIDEO:     //视频
                bean.setVideoUrl(data.getVideoUrl());
                bean.setFirstFrame(data.getFirstFrame());
                bean.setText(text);
                break;
            case IMSConfig.IM_MESSAGE_AUDIO:     //音频
                bean.setAudioUrl(data.getAudioUrl());
                bean.setDuration(data.getDuration());
                bean.setText(text);
                break;
            case IMSConfig.IM_MESSAGE_REPACKED:     //红包消息
                bean.setDesc(data.getDesc());
                bean.setRedPacketId(data.getRedPacketId());
                bean.setText(text);
                bean.setStyle("STYLE_DEFAULT");
                bean.setTitle(data.getTitle());
                break;
            case IMSConfig.IM_MESSAGE_FOLLOWHEAD:     //跟投消息
                bean.setText(text);
                bean.setGameId(data.getGameId());
                bean.setGameName(data.getGameName());
                bean.setBetOrderId(data.getBetOrderId());
                bean.setAmount(data.getAmount());
                bean.setContent(data.getContent());
                bean.setNum(data.getNum());
                bean.setPlayMethod(data.getPlayMethod());
                bean.setUserId(IMSManager.getMyUseId());
                bean.setSealingTime(data.getSealingTime());
                bean.setBettingTotalAmount(data.getBettingTotalAmount());
                bean.setBettingMultiples(data.getBettingMultiples());
                break;
            case IMSConfig.IM_MESSAGE_READCARD:     //红单消息
                bean.setText(text);
                bean.setGameId(data.getGameId());
                bean.setGameName(data.getGameName());
                bean.setBetOrderId(data.getBetOrderId());
                bean.setAmount(data.getAmount());
                bean.setContent(data.getContent());
                bean.setNum(data.getNum());
                bean.setBetAmount(data.getBetAmount());
                bean.setWinAmount(data.getWinAmount());
                bean.setGainAmount(data.getGainAmount());
                bean.setSealingTime(data.getSealingTime());
                break;
            case IMSConfig.IM_MESSAGE_PLAN:     //计划消息
                bean.setVideoUrl(text);
                bean.setFirstFrame(text);   //todo
                break;
            case IMSConfig.IM_MESSAGE_SHARE_SMALL:
                bean.setShareImage(data.getShareImage());
                bean.setTwoImage(data.getTwoImage());
                bean.setThreeImage(data.getThreeImage());
                bean.setShareTitle(data.getShareTitle());
                bean.setAppId(data.getAppId());
                bean.setProgramId(data.getProgramId());
                bean.setProgramName(data.getProgramName());
                bean.setProgramUrl(data.getProgramUrl());
                bean.setText(text);
                break;
            default:
                bean.setText(text);
                break;
        }
        String json = new Gson().toJson(bean);
        return json;
    }

    public static boolean getIsSystem(String data) {
        if(data==null){
            return false;
        }
        if(data.equals("")){
            return false;
        }
        try{
            IMMessageDataJson  bean = new Gson().fromJson(data, IMMessageDataJson.class);
            if(Integer.parseInt(bean.getType())==IMSConfig.IM_MESSAGE_SYSTEM){
                return true;
            }else {
                return false;
            }
        }catch (Exception e){
            return false;
        }
    }
    /**
     * 字符串提取文字消息
     */
    public static String getTextData(String data) {
        if(data==null){
            return null;
        }
        if(data.equals("")){
            return "";
        }
        IMMessageDataJson bean =null;
        try{
            bean = new Gson().fromJson(data, IMMessageDataJson.class);
        }catch (Exception e){
            if(!TextUtils.isEmpty(data)  && (data.equals("您的好友申请已通过") ||data.equals("通过好友申请")||data.equals("您有新的好友")) ){
//                return "你们已经是好友了,快来聊天吧！";
                return "";
            }
            return data;
        }

        if(Integer.parseInt(bean.getType())==IMSConfig.IM_MESSAGE_REPACKED){
            return "[红包]";
        }else if(Integer.parseInt(bean.getType())==IMSConfig.IM_MESSAGE_PICTURE) {
            return "[图片]";
        }else   if(Integer.parseInt(bean.getType())==IMSConfig.IM_MESSAGE_FOLLOWHEAD) {
            return "[跟投分享]";
        }else   if(Integer.parseInt(bean.getType())==IMSConfig.IM_MESSAGE_READCARD) {
            return "[红单分享]";
        }else    if(Integer.parseInt(bean.getType())==IMSConfig.IM_MESSAGE_VIDEO) {
            return "[视频]";
        }else    if(Integer.parseInt(bean.getType())==IMSConfig.IM_MESSAGE_PUSH) {
            return "[推送消息]";
        }
        if(!TextUtils.isEmpty(bean.getSysCode())){
            if(Integer.parseInt(bean.getSysCode())==IMSConfig.IM_MESSAGE_BLACK){
                return "你已被拉入黑名单";
            }else if(Integer.parseInt(bean.getSysCode())==IMSConfig.IM_MESSAGE_NO_BLACK){
                return "你已被解除黑名单";
            }else if(Integer.parseInt(bean.getSysCode())==IMSConfig.IM_MESSAGE_CAN_SPEAK){
                return "你已被解除禁言";
            }else if(Integer.parseInt(bean.getSysCode())==IMSConfig.IM_MESSAGE_NO_SPEAK){
                return "你已被禁言";
            }else if(Integer.parseInt(bean.getSysCode())==IMSConfig.IM_MESSAGE_KIOUTSELF){
                IMSHttpManager.getInstance().IMHttpGetGroupList(null,false,false);
                return "你已移除群聊";
            }else if(Integer.parseInt(bean.getSysCode())==IMSConfig.IM_MESSAGE_CLOPNEGROUP){
                IMSHttpManager.getInstance().IMHttpGetGroupList(null,false,false);
                return "该群已群主被解散";
            }else if(Integer.parseInt(bean.getSysCode())==IMSConfig.IM_MESSAGE_JOINGROUP){
                IMLogUtil.d("MyOwnTag:", "IMSMsgManager ----" +new Gson().toJson(bean.getMeta()));
                List<IMSystemSomeOneAddGroup> result = new Gson().fromJson(new Gson().toJson(bean.getMeta()), new TypeToken<List<IMSystemSomeOneAddGroup>>() {}.getType());
                if(bean.getText().contains("进入群聊") && result!=null &&result.size()>0) {
                    return (result.get(0).getNickName() + "进入了群聊");
                }
            }else if(Integer.parseInt(bean.getSysCode())==IMSConfig.IM_MESSAGE_KIOUTGROUP){
                List<IMSystemSomeOneAddGroup> result = new Gson().fromJson(new Gson().toJson(bean.getMeta()), new TypeToken<List<IMSystemSomeOneAddGroup>>() {}.getType());
                if(bean.getText().contains("剔除群聊") && result!=null &&result.size()>0) {
                    if(TextUtils.isEmpty(result.get(0).getNickName())){
                        IMPersonBean personBean = DaoUtils.getInstance().queryMessageBean(result.get(0).getMemberId());
                        if(personBean==null){
                            result.get(0).setNickName("有人");
                        }else {
                            result.get(0).setNickName(!TextUtils.isEmpty(personBean.getNickName())?personBean.getNickName():personBean.getNickName());
                        }

                    }
                    if(result.size()==1){
                        return (result.get(0).getNickName() + "被移除了群聊");
                    }else {
                        return (result.get(0).getNickName() + "等人被移除了群聊");
                    }

                }
            }else if(Integer.parseInt(bean.getSysCode())==IMSConfig.IM_MESSAGE_QUITGROUP){
                List<IMSystemSomeOneAddGroup> result = new Gson().fromJson(new Gson().toJson(bean.getMeta()), new TypeToken<List<IMSystemSomeOneAddGroup>>() {}.getType());
                if(bean.getText().contains("退出群聊") && result!=null &&result.size()>0) {
                    if(TextUtils.isEmpty(result.get(0).getNickName())){
                        IMPersonBean personBean = DaoUtils.getInstance().queryMessageBean(result.get(0).getMemberId());
                        if(personBean==null){
                            result.get(0).setNickName("有人");
                        }else {
                            result.get(0).setNickName(!TextUtils.isEmpty(personBean.getNickName())?personBean.getNickName():personBean.getNickName());
                        }

                    }
                    if(result.size()==1){
                        return (result.get(0).getNickName() + "退出了群聊");
                    }else {
                        return (result.get(0).getNickName() + "等人退出了群聊");
                    }

                }
            }else if(Integer.parseInt(bean.getSysCode())==IMSConfig.IM_MESSAGE_ADD_FRIEND_SUCCESS){
//                return "你们已经是好友了,快来聊天吧！";
            }
        }
        return  bean.getText();
    }




    /**（单聊）
     * 发送已读的消息(当fingerId==null,代表全部已读)
     */
    public static void sendReadMessage(String fingerId, String customId) {
        IMMessageBean message = new IMMessageBean();
        if(!TextUtils.isEmpty(fingerId)){
            message.setFingerprint(fingerId);
        }
        if(!TextUtils.isEmpty(IMSManager.getMyToken())){
            message.setToken(IMSManager.getMyToken());
        }
        message.setReceiverId(customId);
        message.setType(MessageProtobuf.ImMessage.TypeEnum.READ);
        MessageProcessor.getInstance().sendMsg(message);
    }

    /**（群聊）
     * 发送已读的消息(当fingerId==null,代表全部已读)
     */
    public static void sendGroupReadMessage(String groupId) {
        IMMessageBean message = new IMMessageBean();
        if(!TextUtils.isEmpty(IMSManager.getMyToken())){
            message.setToken(IMSManager.getMyToken());
        }
        message.setGroupId(groupId);
        message.setType(MessageProtobuf.ImMessage.TypeEnum.READ);
        MessageProcessor.getInstance().sendMsg(message);
    }
    /**
     * 获取一个Id，重新保存剩下的
     */
    public static String getMessageFigId() {
        String ids = IMPreferenceUtil.getPreference_String(IMSConfig.SAVE_MESSAGE_FIGID, "");
        if(TextUtils.isEmpty(ids)){
            return  null;
        }
        String[] split = ids.split(",");
        StringBuffer stringBuffer=new StringBuffer();
        for (int i = 1; i < split.length; i++) {
            if(i==split.length-1){
                stringBuffer.append(split[i]);
            }else {
                stringBuffer.append(split[i]+",");
            }
        }
        IMLogUtil.d("MyOwnTag:", "IMSHttpManager ----剩余消息池id :" +stringBuffer.toString());
        IMPreferenceUtil.setPreference_String(IMSConfig.SAVE_MESSAGE_FIGID,stringBuffer.toString());
        return split[0];
    }

    /**
     * 检查是不是需要获取消息Id列表
     */
    public static boolean getNeedGetMessageIds() {
        String ids = IMPreferenceUtil.getPreference_String(IMSConfig.SAVE_MESSAGE_FIGID, "");
        if(TextUtils.isEmpty(ids)){
            return  true;
        }
        String[] split = ids.split(",");
        if(split.length<20){
            return  true;
        }
        return false;
    }

    /**
     * 创建群聊会话(接受到的)
     */
    public static IMConversationBean creatReceiveGroupConversation(IMMessageBean message) {
        IMGroupBean groupBean = DaoUtils.getInstance().queryGroupBean(message.getGroupId());
        if(groupBean==null){
            return null;
        }
        IMSHttpManager.getInstance().IMHttpGetGroupList(null,false,false);
        IMConversationBean bean=new IMConversationBean();
        bean.setConversationavatar(groupBean.getGroupAvatar());
        bean.setGroupAvatar(groupBean.getGroupAvatar());
        bean.setConversationId(groupBean.getGroupId());
        bean.setConversationName(groupBean.getGroupName());
        bean.setGroupId(groupBean.getGroupId());
        bean.setLastMessageContent(message.getData());
        bean.setLastMessageSendType(IMSConfig.MSG_RECIEVE);
        bean.setLastMessageType(message.getMsgType());
        bean.setIsOnline("Y");
        bean.setLastMessageSendstate(IMSConfig.IM_SEND_STATE_SUCCESS);
        bean.setLastMessageTime(message.getTimestamp());
        bean.setLastMessageId(message.getFingerprint());
        return bean;
    }
    public static IMConversationBean creatSendGroupConversation(String conversationId,IMConversationDetailBean message) {
        IMGroupBean groupBean = DaoUtils.getInstance().queryGroupBean( conversationId);
        //data==null说明会话列表没得才需要 创建单聊会话
        IMConversationBean data =  DaoUtils.getInstance().queryConversationBean(groupBean.getGroupId());
        if(groupBean==null || data!=null){
            return null;
        }
        IMConversationBean bean=new IMConversationBean();
        bean.setConversationavatar(groupBean.getGroupAvatar());
        bean.setGroupAvatar(groupBean.getGroupAvatar());
        bean.setConversationId(groupBean.getGroupId());
        bean.setConversationName(groupBean.getGroupName());
        bean.setGroupId(groupBean.getGroupId());
        bean.setLastMessageContent(message.getData());
        bean.setLastMessageSendType(IMSConfig.MSG_SEND);
        bean.setLastMessageType(message.getMsgType());
        bean.setLastMessageSendstate(IMSConfig.IM_SEND_STATE_SENDING);
        bean.setLastMessageTime(message.getTimestamp());
        bean.setLastMessageId(message.getFingerprint());
        return bean;
    }
    /**
     * 创建单聊聊会话(接受到的)
     */
    public static IMConversationBean creatReceivePersonConversation(IMMessageBean message) {
        if(TextUtils.isEmpty(message.getSenderId())) {
            return null;
        }
        IMPersonBean personBean =null;
        if(!message.getSenderId().equals(IMSManager.getMyUseId())){
            personBean = DaoUtils.getInstance().queryMessageBean(message.getSenderId());
        } else {
            personBean = DaoUtils.getInstance().queryMessageBean(message.getReceiverId());
        }
        if(personBean==null){
            return null;
        }
        IMConversationBean bean=new IMConversationBean();
        bean.setConversationavatar(personBean.getAvatar());
        bean.setConversationId(personBean.getMemberId());
        bean.setAvatar(personBean.getAvatar());
        bean.setConversationName(TextUtils.isEmpty(personBean.getNickName())?personBean.getNickName():personBean.getNickName());
        bean.setMemberId(personBean.getMemberId());
        bean.setStatus(personBean.getStatus());                  //用户在线离线状态
        bean.setLastMessageContent(message.getData());
        bean.setLastMessageSendType(IMSConfig.MSG_RECIEVE);
        bean.setLastMessageType(message.getMsgType());
        bean.setLastMessageSendstate(IMSConfig.IM_SEND_STATE_SUCCESS);
        bean.setLastMessageTime(message.getTimestamp());
        bean.setLastMessageId(message.getFingerprint());
        return bean;

    }

    /**
     * 创建应用团队(接受到的)
     */
    public static IMConversationBean creatClxTeam(IMMessageBean message) {
        IMPersonBean personBean = DaoUtils.getInstance().queryMessageBean(message.getSenderId());
        if(personBean==null){
            return null;
        }
        IMConversationBean bean=new IMConversationBean();
        bean.setConversationavatar(personBean.getAvatar());
        bean.setConversationId(personBean.getMemberId());
        bean.setAvatar(personBean.getAvatar());
        bean.setConversationName(TextUtils.isEmpty(personBean.getNickName())?personBean.getNickName():personBean.getNickName());
        bean.setMemberId(personBean.getMemberId());
        bean.setStatus(personBean.getStatus());                  //用户在线离线状态
        bean.setLastMessageContent(message.getData());
        bean.setLastMessageSendType(IMSConfig.MSG_RECIEVE);
        bean.setLastMessageType(message.getMsgType());
        bean.setLastMessageSendstate(IMSConfig.IM_SEND_STATE_SUCCESS);
        bean.setLastMessageTime(message.getTimestamp());
        bean.setLastMessageId(message.getFingerprint());
        return bean;
    }
    /**
     * 创建单聊聊会话(添加好友成功)800001
     */
    public static IMConversationBean creatReceiveSystemPersonConversation(IMMessageBean message) {
        IMMessageDataJson s = new Gson().fromJson(message.getData(), IMMessageDataJson.class);
        IMMessagSystemJson data = new Gson().fromJson(new Gson().toJson(s.getMeta()), IMMessagSystemJson.class);
        IMPersonBean personBean =null;
        if(data.getAcceptCustomerId().equals(IMSManager.getMyUseId())){//同意添加别人    会话界面显示申请人信息
            personBean = DaoUtils.getInstance().queryMessageBean(data.getCustomerId());
        }else {//反之被同意添加别人    会话界面显示被申请人信息
            personBean = DaoUtils.getInstance().queryMessageBean(data.getAcceptCustomerId());
        }
        if(personBean==null){
            return null;
        }
        IMConversationBean bean=new IMConversationBean();
        bean.setConversationavatar(personBean.getAvatar());
        bean.setAvatar(personBean.getAvatar());
        bean.setConversationId(personBean.getMemberId());
        bean.setConversationName(TextUtils.isEmpty(personBean.getNickName())?personBean.getNickName():personBean.getNickName());
        bean.setMemberId(personBean.getMemberId());
        bean.setStatus(personBean.getStatus());                  //用户在线离线状态
        bean.setLastMessageContent(s.getText());
        bean.setLastMessageSendType(IMSConfig.MSG_RECIEVE);
        bean.setLastMessageType(message.getMsgType());
        bean.setLastMessageSendstate(IMSConfig.IM_SEND_STATE_SUCCESS);
        bean.setLastMessageTime(message.getTimestamp());
        bean.setLastMessageId(message.getFingerprint());
        return bean;
    }
    public static IMConversationBean creatSendPersonConversation(String conversationId,IMConversationDetailBean message) {
        IMPersonBean personBean = DaoUtils.getInstance().queryMessageBean(conversationId);
        if(personBean==null || TextUtils.isEmpty(personBean.getMemberId())){
            return null;
        }
        //data==null说明会话列表没得才需要 创建单聊会话
        IMConversationBean data = DaoUtils.getInstance().queryConversationBean(personBean.getMemberId());
        if(personBean==null || data!=null){
            return null;
        }
        IMConversationBean bean=new IMConversationBean();
        bean.setConversationavatar(personBean.getAvatar());
        bean.setAvatar(personBean.getAvatar());
        bean.setConversationId(personBean.getMemberId());
        bean.setConversationName(TextUtils.isEmpty(personBean.getNickName())?personBean.getNickName():personBean.getNickName());
        bean.setMemberId(personBean.getMemberId());
        bean.setStatus(personBean.getStatus());                  //用户在线离线状态
        bean.setLastMessageContent(message.getData());
        bean.setLastMessageSendType(IMSConfig.MSG_SEND);
        bean.setLastMessageType(message.getMsgType());
        bean.setLastMessageSendstate(IMSConfig.IM_SEND_STATE_SENDING);
        bean.setLastMessageTime(message.getTimestamp());
        bean.setLastMessageId(message.getFingerprint());
        return bean;
    }
    /**
     * 将数据(置顶)
     */
    public static   List<IMConversationBean>  getOrderData( List<IMConversationBean> beans){
        List<IMConversationBean> topBeans=new ArrayList<>();
        List<IMConversationBean> blwBeans=new ArrayList<>();
        for (int i = 0; i < beans.size(); i++) {
            if(beans.get(i).getIsSetTop()){
                topBeans.add(beans.get(i));
            }else {
                blwBeans.add(beans.get(i));
            }
        }
        //置顶的按置顶时间降序排列，不置顶的按消息的时间降序排列
        Collections.sort(topBeans, new Comparator<IMConversationBean>() {

            @Override
            public int compare(IMConversationBean o1, IMConversationBean o2) {
                if (o1.getSetTopTime() > o2.getSetTopTime()) {
                    return -1;
                }
                if (o1.getSetTopTime() == o2.getSetTopTime()) {
                    return 0;
                }
                return 1;
            }
        });
        Collections.sort(blwBeans, new Comparator<IMConversationBean>() {

            @Override
            public int compare(IMConversationBean o1, IMConversationBean o2) {
                if (o1.getLastMessageTime() > o2.getLastMessageTime()) {
                    return -1;
                }
                if (o1.getLastMessageTime() == o2.getLastMessageTime()) {
                    return 0;
                }
                return 1;
            }
        });
        beans.clear();
        beans.addAll(topBeans);
        beans.addAll(blwBeans);
        return  beans;
    }

    /**
     * 小程序分享(bean代表小程序   content代表说)
     */
    public static void ShareSmallProgress(IMMessageDataJson bean,String content, List<String> groupIds ,List<String> personIds) {
        IMMessageDataJson json=new IMMessageDataJson();
        if(bean!=null){
            json.setTwoImage(bean.getTwoImage());
            json.setThreeImage(bean.getThreeImage());
            json.setAppId(bean.getAppId());
            json.setShareImage(bean.getShareImage());
            json.setProgramId(bean.getProgramId());
            json.setProgramUrl(bean.getProgramUrl());
            json.setProgramName(bean.getProgramName());
            json.setShareTitle(bean.getShareTitle());
            if(groupIds!=null){
                for (int i = 0; i < groupIds.size(); i++) {
                    getMessageID("小程序分享",json,IMSConfig.IM_MESSAGE_SHARE_SMALL,groupIds.get(i),null);
                }
            }else {
                for (int i = 0; i < personIds.size(); i++) {
                    getMessageID("小程序分享",json,IMSConfig.IM_MESSAGE_SHARE_SMALL,null,personIds.get(i));
                }
            }
        }else {
            json.setText(content);
            if(groupIds!=null){
                for (int i = 0; i < groupIds.size(); i++) {
                    getMessageID(content,json,IMSConfig.IM_MESSAGE_TEXT,groupIds.get(i),null);
                }
            }else {
                for (int i = 0; i < personIds.size(); i++) {
                    getMessageID(content,json,IMSConfig.IM_MESSAGE_TEXT,null,personIds.get( i));
                }
            }
        }
    }

    public static void getMessageID(final String content, final IMMessageDataJson bean, final int type, final String groupId,final String personId) {

        String messageFigId = IMSMsgManager.getMessageFigId();
        if(!TextUtils.isEmpty(messageFigId)){
            if(!TextUtils.isEmpty(groupId)){
                sendGroupMessage(content,bean,type,groupId,messageFigId);
            }else {
                sendPersonMessage(content,bean,type,personId,messageFigId);
            }
        }else {
            String Uid= UUID.randomUUID().toString();
            if(!TextUtils.isEmpty(groupId)) {
                sendGroupMessage(content, bean, type, groupId, Uid);
            }else {
                sendPersonMessage(content,bean,type,personId,messageFigId);
            }
            IMSHttpManager.getInstance().IMHttpGetIMToken(false);
        }
        //先判断消息池子是不是有20条消息，不够就请求50条消息
        if(IMSMsgManager.getNeedGetMessageIds()) {
            IMSHttpManager.getInstance().IMHttpGetIMFigId();
        }
    }
    /**
     * 是用来发送群分享用的
     */
    public  static void sendGroupMessage(String content,  IMMessageDataJson bean, int type,String groupId,String messageId) {

        //构造一个bean更新界面,把内容传进去跟新当前界面
        final IMConversationDetailBean message = new IMConversationDetailBean();
        message.setFingerprint(messageId);
        message.setMsgType(type);
        message.setData(handleMessageDataJson(null, content, bean, type));
        message.setTimestamp(System.currentTimeMillis());
        message.setGroupId(groupId);
        message.setSenderId(IMSManager.getMyUseId());
        message.setConversationId(groupId);
        message.setSendstate(IMSConfig.IM_SEND_STATE_SENDING); //发送中
        message.setReceiverId(groupId);
        EventBus.getDefault().post(new IMSendGroupMessageEvent(groupId,message)); //通知上一个页面跟新它的发送状态
        IMSMsgManager.sendGroupMessage(message,groupId,DaoUtils.getInstance(),false);
    }

    /**
     * 是用来发送群分享用的
     */
    public  static void sendPersonMessage(String content,  IMMessageDataJson bean, int type,String personId,String messageId) {
        //构造一个bean更新界面,把内容传进去跟新当前界面
        final IMConversationDetailBean message = new IMConversationDetailBean();
        message.setFingerprint(messageId);
        message.setReceiverId(personId);
        message.setConversationId(personId);
        message.setTimestamp(System.currentTimeMillis());
        message.setMsgType(type);
        message.setData(IMSMsgManager.handleMessageDataJson(null, content, bean, type));
        message.setTimestamp(System.currentTimeMillis());
        message.setSendstate(IMSConfig.IM_SEND_STATE_SENDING); //发送中
        EventBus.getDefault().post(new IMSendGroupMessageEvent(personId, message)); //通知上一个页面跟新它的发送状态
        IMSMsgManager.sendMessage(message, personId, DaoUtils.getInstance(),false);
    }


    /**
     * 模拟发送消息
     *   getMessageID(view, message, null, IMSConfig.IM_MESSAGE_TEXT);
     */
    public static void getMessageID(final String content, final int type,final String personId) {
        String messageFigId = IMSMsgManager.getMessageFigId();
        if(!TextUtils.isEmpty(messageFigId)){
            sendAddFriendMessage(content,type,messageFigId,personId);
        }else {
            String Uid= UUID.randomUUID().toString();
            sendAddFriendMessage(content,type,Uid,personId);
            IMSHttpManager.getInstance().IMHttpGetIMToken(false);
        }
        //先判断消息池子是不是有20条消息，不够就请求50条消息
        if(IMSMsgManager.getNeedGetMessageIds()) {
            IMSHttpManager.getInstance().IMHttpGetIMFigId();
        }
    }

    /**
     * 发送消息(先调用获取消息id的接口)
     */
    private static void sendAddFriendMessage( String content,int type,String FigId,final String personId) {
        //构造一个bean更新界面,把内容传进去跟新当前界面
        final IMConversationDetailBean message = new IMConversationDetailBean();
        message.setFingerprint(FigId);
        message.setReceiverId(personId);
        message.setConversationId(personId);
        message.setMsgType(type);
        message.setData(IMSMsgManager.handleMessageDataJson(null, content, null, type));
        message.setTimestamp(System.currentTimeMillis());
        message.setSendstate(IMSConfig.IM_SEND_STATE_SENDING); //发送中
        IMSMsgManager.sendMessage(message,personId, DaoUtils.getInstance(),false);//
        EventBus.getDefault().post(new IMSendGroupMessageEvent(personId, message)); //通知上一个页面跟新它的发送状态
    }

    /**
     * 被移除该群(更新数据库)发送消息时候失败提示
     */
    public static void sendMessageNoGroup(IMMessageBean message) {
        if(TextUtils.isEmpty(message.getData())){
            return;
        }
        IMSystemNoGroupBean dataJson = new Gson().fromJson(message.getData(), IMSystemNoGroupBean.class);
        if(dataJson==null){
            return;
        }
        //从消息管理器移除
        if (!TextUtils.isEmpty(dataJson.getFingerprint())) {
            NettyTcpClient.getInstance().getMsgTimeoutTimerManager().remove(dataJson.getFingerprint());
        }
        //跟新会话表
        if(!TextUtils.isEmpty(message.getGroupId())) {
            IMConversationBean bean = DaoUtils.getInstance().queryConversationBean(message.getGroupId());
            if (bean != null) {
//                bean.setLastMessageSendstate(IMSConfig.IM_SEND_STATE_FAILE);
//                bean.setLastMessageTime(message.getTimestamp());
//                DaoUtils.getInstance().updateConversationData(bean);
                bean.setLastMessageSendType(IMSConfig.MSG_RECIEVE);
                bean.setLastMessageTime(message.getTimestamp());
                bean.setLastMessageContent(dataJson.getText());
                DaoUtils.getInstance().updateConversationData(bean);
            }
        }
        //跟新消息表
        if( !TextUtils.isEmpty(dataJson.getFingerprint())){
            IMConversationDetailBean data =  DaoUtils.getInstance().queryConversationDetailDataAccordFieldFiger(message.getFingerprint());
            if(data!=null){
                data.setSendstate(IMSConfig.IM_SEND_STATE_FAILE);
                data.setTimestamp(message.getTimestamp()-1);
                DaoUtils.getInstance().updateConversationDetailData(data);
            }
        }
    }

    /**
     * 被删除好友(更新数据库)发送消息时候失败提示
     */
    public static void sendMessageNoFriend(IMMessageBean message) {
        if(TextUtils.isEmpty(message.getData())){
            return;
        }
        IMSystemNoGroupBean dataJson = new Gson().fromJson(message.getData(), IMSystemNoGroupBean.class);
        NettyTcpClient.getInstance().getMsgTimeoutTimerManager().remove(message.getFingerprint());
        NettyTcpClient.getInstance().getMsgTimeoutTimerManager().remove(dataJson.getFingerprint());

        if (!TextUtils.isEmpty(message.getSenderId())) {
            IMConversationBean bean = DaoUtils.getInstance().queryConversationBean(message.getSenderId());
            if (bean != null) {
                bean.setLastMessageSendType(IMSConfig.MSG_RECIEVE);
                bean.setLastMessageTime(message.getTimestamp() + 1);
                bean.setLastMessageContent(dataJson.getText());
                DaoUtils.getInstance().updateConversationData(bean);
            }
        }
        if (!TextUtils.isEmpty(dataJson.getFingerprint())) {
            IMConversationDetailBean data = DaoUtils.getInstance().queryConversationDetailDataAccordFieldFiger(dataJson.getFingerprint());
            if (data != null) {
                data.setSendstate(IMSConfig.IM_SEND_STATE_FAILE);
                data.setTimestamp(message.getTimestamp());
                DaoUtils.getInstance().updateConversationDetailData(data);
            }
        }
    }
    /**
     * 被屏蔽好友(更新数据库)发送消息时候失败提示
     */
    public static void sendMessageForbit(IMMessageBean message) {
        if(TextUtils.isEmpty(message.getData())){
            return;
        }
        IMSystemNoGroupBean dataJson = new Gson().fromJson(message.getData(), IMSystemNoGroupBean.class);
        NettyTcpClient.getInstance().getMsgTimeoutTimerManager().remove(message.getFingerprint());
        NettyTcpClient.getInstance().getMsgTimeoutTimerManager().remove(dataJson.getFingerprint());

        if (!TextUtils.isEmpty(message.getSenderId())) {
            IMConversationBean bean = DaoUtils.getInstance().queryConversationBean(message.getSenderId());
            if (bean != null) {
                bean.setLastMessageSendType(IMSConfig.MSG_RECIEVE);
                bean.setLastMessageTime(message.getTimestamp() + 1);
                bean.setLastMessageContent(dataJson.getText());
                DaoUtils.getInstance().updateConversationData(bean);
            }
        }
        if (!TextUtils.isEmpty(dataJson.getFingerprint())) {
            IMConversationDetailBean data = DaoUtils.getInstance().queryConversationDetailDataAccordFieldFiger(dataJson.getFingerprint());
            if (data != null) {
                data.setSendstate(IMSConfig.IM_SEND_STATE_FAILE);
                data.setTimestamp(message.getTimestamp());
                DaoUtils.getInstance().updateConversationDetailData(data);
            }
        }
    }
    /**
     * 应用团队的消息(更新数据库)
     */
    public static void saveClxTeamSystemMessage(IMMessageBean message) {
        if(TextUtils.isEmpty(message.getData())){
            return;
        }
        IMClxTeamMessageBean dataJson =null;
        try {
            dataJson = new Gson().fromJson(message.getData(), IMClxTeamMessageBean.class);
        }catch (Exception e){
            return;
        }
        //未读消息
        String number = IMPreferenceUtil.getPreference_String(IMSConfig.CLX_ID + IMSConfig.IM_UNEREAD_CONVERSATION_CHAT, "0");
        int unreadnumber = Integer.parseInt(number) + 1;
        IMPreferenceUtil.setPreference_String(IMSConfig.CLX_ID +IMSConfig.IM_UNEREAD_CONVERSATION_CHAT, unreadnumber + "");
        //从会话列表数据库查看是不是有彩乐信团队
        IMConversationBean bean = DaoUtils.getInstance().queryConversationBean(IMSConfig.CLX_ID);
        if (bean != null) {
            bean.setLastMessageSendType(IMSConfig.MSG_CLXTEAM_WARN);
            bean.setLastMessageType(message.getMsgType());
            bean.setLastMessageTime(message.getTimestamp());
            bean.setLastMessageContent(dataJson.getMeta().getMap().getMsgTitle());
            DaoUtils.getInstance().updateConversationData(bean);
        }else {
            IMConversationBean bean1=new IMConversationBean();
            bean1.setConversationavatar("");
            bean1.setConversationId(IMSConfig.CLX_ID);
            bean1.setMemberId(IMSConfig.CLX_ID);
            bean1.setConversationName("应用团队");
            bean1.setLastMessageContent(dataJson.getMeta().getMap().getMsgTitle());
            bean1.setLastMessageSendType(IMSConfig.MSG_CLXTEAM_WARN);
            bean1.setLastMessageType(message.getMsgType());
            bean1.setLastMessageSendstate(IMSConfig.IM_SEND_STATE_SUCCESS);
            bean1.setLastMessageTime(message.getTimestamp());
            bean1.setLastMessageId(message.getFingerprint());
            DaoUtils.getInstance().insertConversationData(bean1);
        }
        //添加消息表
        if (!TextUtils.isEmpty(message.getFingerprint())) {
            IMConversationDetailBean data = DaoUtils.getInstance().queryConversationDetailDataAccordFieldFiger(message.getFingerprint());
            if (data == null) {
                IMConversationDetailBean bean2=new IMConversationDetailBean();
                bean2.setFingerprint(message.getFingerprint());
                bean2.setTimestamp(message.getTimestamp());
                bean2.setData(message.getData());
                bean2.setMsgType(message.getMsgType());
//                if(dataJson.getMeta().getMap().getMsgType().equals("ACCOUNT_WARN")){
//                    bean2.setLastMsgSendType(IMSConfig.MSG_CLXTEAM_WARN);
//                }else
                if(dataJson.getMeta().getMap().getMsgType().equals("COMPLAINT_RESULT")){
                    bean2.setLastMsgSendType(IMSConfig.MSG_CLXTEAM_RESULT);
                }else {
                    bean2.setLastMsgSendType(IMSConfig.MSG_CLXTEAM_WARN);
                }
                bean2.setSenderId(IMSConfig.CLX_ID);
                bean2.setGroupId(IMSConfig.CLX_ID);
                bean2.setUserId(IMSConfig.CLX_ID);
                bean2.setConversationId(IMSConfig.CLX_ID);
                bean2.setCurrentUid(IMSManager.getMyUseId());
                DaoUtils.getInstance().insertConversationDetailData(bean2);
            }
        }
    }



    /**
     * 转发图片视屏
     */
    public static void ShareSendText(IMConversationDetailBean bean,String personId,String groupId) {
        if(bean==null){
            return;
        }
        getMessageID(bean,personId,groupId);
    }

    public static void getMessageID(IMConversationDetailBean bean,String personId,String groupId) {

        String messageFigId = IMSMsgManager.getMessageFigId();
        if(!TextUtils.isEmpty(messageFigId)){
            if(!TextUtils.isEmpty(groupId)){
                sendGroupMessages(bean,groupId,messageFigId);
            }else {
                sendPersonMessages(bean,personId,messageFigId);
            }
        }else {
            String Uid= UUID.randomUUID().toString();
            if(!TextUtils.isEmpty(groupId)){
                sendPersonMessages(bean,groupId,messageFigId);
            }else {
                sendPersonMessages(bean,personId,messageFigId);
            }
            IMSHttpManager.getInstance().IMHttpGetIMToken(false);
        }
        //先判断消息池子是不是有20条消息，不够就请求50条消息
        if(IMSMsgManager.getNeedGetMessageIds()) {
            IMSHttpManager.getInstance().IMHttpGetIMFigId();
        }
    }
    /**
     * 转发到群
     */
    public  static void sendGroupMessages(IMConversationDetailBean bean,String groupId,String fingId) {
        bean.setFingerprint(fingId);
        bean.setSenderId(IMSManager.getMyUseId());
        bean.setReceiverId(groupId);
        bean.setGroupId(groupId);
        bean.setConversationId(groupId);
        bean.setTimestamp(System.currentTimeMillis());
        bean.setSendstate(IMSConfig.IM_SEND_STATE_SENDING); //发送中
        EventBus.getDefault().post(new IMSendGroupMessageEvent(groupId,bean)); //通知上一个页面跟新它的发送状态
        EventBus.getDefault().post(new IMZhuaFaDataEvent(bean,groupId)); //通知上一个页面跟新它的发送状态
        IMSMsgManager.sendGroupMessage(bean,groupId,DaoUtils.getInstance(),false);
    }

    /**
     * 转发到人
     */
    public  static void sendPersonMessages(IMConversationDetailBean bean,String personId,String fingId) {
        //构造一个bean更新界面,把内容传进去跟新当前界面
        bean.setFingerprint(fingId);
        bean.setSenderId(IMSManager.getMyUseId());
        bean.setReceiverId(personId);
        bean.setGroupId(personId);
        bean.setConversationId(personId) ;
        bean.setTimestamp(System.currentTimeMillis());
        bean.setTimestamp(System.currentTimeMillis());
        bean.setSendstate(IMSConfig.IM_SEND_STATE_SENDING); //发送中
        EventBus.getDefault().post(new IMSendGroupMessageEvent(personId, bean)); //通知上一个页面跟新它的发送状态
        EventBus.getDefault().post(new IMZhuaFaDataEvent(bean,personId)); //通知上一个页面跟新它的发送状态
        IMSMsgManager.sendMessage(bean, personId, DaoUtils.getInstance(),false);
    }
}
