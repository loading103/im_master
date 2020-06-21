package com.android.im.imutils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.im.IMSMsgManager;
import com.android.im.imadapter.IMChatRecyclerAdapter;
import com.android.im.imbean.IMHttpCommonBean;
import com.android.im.imeventbus.IMCreatMessageEvent;
import com.android.im.imeventbus.IMLoginTimeOutEvent;
import com.android.im.imnet.IMHttpResultObserver;
import com.android.im.imnet.http.IMHttpsService;
import com.android.im.imui.activity.IMBetRecordActivity;
import com.android.im.imui.activity.IMPersonalChatActivity;
import com.android.im.imui.activity.IMPersonalInforActivity;
import com.android.im.imui.activity.IMSendRedPickActivity;
import com.android.im.imui.activity.IMVideoRecordActivity;
import com.android.im.imview.dialog.IMIosCommonDiglog;
import com.android.nettylibrary.IMSConfig;
import com.android.nettylibrary.bean.IMMessageBean;
import com.android.nettylibrary.greendao.entity.IMConversationBean;
import com.android.nettylibrary.greendao.entity.IMConversationDetailBean;
import com.android.nettylibrary.greendao.entity.IMPersonBean;
import com.android.nettylibrary.greendao.greenutils.DaoUtils;
import com.android.nettylibrary.http.IMMessageDataJson;
import com.android.nettylibrary.http.IMPersonBeans;
import com.android.nettylibrary.http.IMUserInforBean;
import com.android.nettylibrary.utils.IMPreferenceUtil;
import com.google.gson.Gson;

import org.greenrobot.eventbus.EventBus;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import okhttp3.RequestBody;

import static com.android.im.imnet.IMBaseConstant.JSON;

/**
 * 单聊界面辅助类
 */
public class IMPersonChatUtils {

    private IMIosCommonDiglog diglog;

    public  Context context;

    public Timer timer;

    public TimerTask timerTask;

    public IMPersonChatUtils(Context context) {
        this.context = context;
        diglog = new IMIosCommonDiglog(context);
    }
    /**
     * 聊天背景
     */
    public void setBgUrl(ImageView mIvBg) {
        String chatBgJson = IMPreferenceUtil.getPreference_String(IMSConfig.CHOOSE_CHAT_BG, "");
        if(!TextUtils.isEmpty(chatBgJson)){
            IMImageLoadUtil.CommonImageBgLoadCp(context,chatBgJson,mIvBg);
        }
    }
    /**
     * 获取个人信息（在线离线状态）
     */
    public void getPerosonState(IMConversationBean customer) {
        IMPersonBeans bean = new IMPersonBeans();
        bean.setCustomerId(customer.getConversationId());
        String json = new Gson().toJson(bean);
        RequestBody body = RequestBody.create(JSON, json);
        IMHttpsService.GetMemberInforJson(body,new IMHttpResultObserver<IMUserInforBean.UserInforData>() {
            @Override
            public void onSuccess(IMUserInforBean.UserInforData data, String message) {
                if(data==null){//内容非ID
                    ((IMPersonalChatActivity)context).mTvState.setVisibility(View.GONE);
                    return;
                }
                IMPersonBean bean1 = DaoUtils.getInstance().queryMessageBean(data.getCustomerId());
                if(bean1!=null){
                    bean1.setAvatar(data.getAvatar());
                    bean1.setNickName(data.getNickName());
                    DaoUtils.getInstance().updateMessageData(bean1);
                }
                ((IMPersonalChatActivity)context). mTvState.setVisibility(View.VISIBLE);
                if(!TextUtils.isEmpty(data.getIsOnline()) && data.getIsOnline().equals("N")){
                    ((IMPersonalChatActivity)context). mTvState.setText("（离线）");
                }else {
                    ((IMPersonalChatActivity)context). mTvState.setText("（在线）");
                }
            }
            @Override
            public void _onError(Throwable e) {
            }
            @Override
            public void _onErrorLocal(Throwable e, String message, String code) {
                if(code.equals("NO_LOGIN")){
                    EventBus.getDefault().post(new IMLoginTimeOutEvent());
                }

            }
        });
    }
    /**
     * 发送文字
     */
    public void sendTextMessage(boolean textswith, EditText view) {
        if(textswith){
            String message = view.getText().toString();
            view.getText().clear();
            ((IMPersonalChatActivity)context).getMessageID(view, message, null, IMSConfig.IM_MESSAGE_TEXT);
        }else {
            ((IMPersonalChatActivity)context).showSingleCommonDialog("文字表情功能暂未开放");
        }
    }
    /**
     * 发送图片
     */
    public void sendPicMessage(boolean picswith) {
        if(picswith){
            IMChooseUtils.choosePhotoForResult(context,IMPersonalChatActivity.REQUEST_CODE_CHOOSE,9,false);
        }else {
            ((IMPersonalChatActivity)context).showSingleCommonDialog("图片功能暂未开放");
        }

    }

    /**
     * 点击进入分享界面
     */
    public void ShareMessage() {
        Intent intent=new Intent(context, IMBetRecordActivity.class);
        context.startActivity(intent);
    }

    /**
     * 点击进入发送红包
     */
    public void sendRedPacketMessage(IMConversationBean customer,boolean redswith) {
        if(true){
            String types = IMPreferenceUtil.getPreference_String(IMSConfig.SAVE_USERTYPE, "0");
            if(types.equals("7")){
                ((IMPersonalChatActivity)context).showSingleCommonDialog("你还未进行登录，不能发送红包");
            }else {
                Intent intent=new Intent(context, IMSendRedPickActivity.class);
                intent.putExtra("customer",customer);
                context.startActivity(intent);
            }
        }else {
            ((IMPersonalChatActivity)context).showSingleCommonDialog("红包功能暂未开放");
        }
    }
    /**
     * 点击进入发送视频
     */
    public void sendVideoMessage(boolean videoswith) {
        if(videoswith){
//            ((IMPersonalChatActivity)context).startActivityForResult(new Intent(context, IMMediaRecordActivity.class), NOTIFY_RECORD_OPERATE);
            context.startActivity(new Intent(context, IMVideoRecordActivity.class));
        }else {
            ((IMPersonalChatActivity)context).showSingleCommonDialog("视频功能暂未开放");
        }
    }


    public void startDelayTime(IMConversationDetailBean message, List<IMConversationDetailBean> sendlists, List<IMConversationDetailBean> messagelist, IMChatRecyclerAdapter adapter) {
        sendlists.add(message);
        if (timer == null) {
            timer = new Timer();
        }
        timerTask = new TimerTask() {
            @Override
            public void run() {
                ((Activity)context).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (sendlists.size() == 0) {
                            if(timer!=null){
                                timer.cancel();
                                timer = null;
                            }
                        }
                        for (int i = 0; i < sendlists.size(); i++) {
                            long time = (System.currentTimeMillis() - sendlists.get(i).getTimestamp()) / 1000;
                            if (time > 15) {
                                for (int j = 0; j < messagelist.size(); j++) {
                                    if(TextUtils.isEmpty(messagelist.get(j).getFingerprint())){
                                        continue;
                                    }
                                    if(TextUtils.isEmpty(sendlists.get(i).getFingerprint())){
                                        continue;
                                    }
                                    if (messagelist.get(j).getFingerprint().equals(sendlists.get(i).getFingerprint())) {
                                        messagelist.get(j).setSendstate(IMSConfig.IM_SEND_STATE_FAILE);
                                        adapter.notifyDataSetChanged();
                                    }
                                }
                                sendlists.remove(i);
                            }
                        }
                    }
                });
            }
        };

        timer.schedule(timerTask, 0, 1000);
    }

    /**
     * 构造一个bean更新界面
     */
    public IMConversationDetailBean creatIMConversationDetailBean(String uid, long time, int type, String content, IMMessageDataJson bean, IMConversationBean customer) {
        final IMConversationDetailBean message = new IMConversationDetailBean();
        message.setFingerprint(uid);
        message.setReceiverId(customer.getMemberId());
        message.setConversationId(customer.getMemberId());
        message.setMsgType(type);
        message.setPictureTime(time);
        message.setLastMsgSendType(IMSConfig.MSG_SEND);
        message.setData(IMSMsgManager.handleMessageDataJson(null, content, bean, type));
        message.setTimestamp(System.currentTimeMillis());
        message.setSendstate(IMSConfig.IM_SEND_STATE_SENDING); //发送中
        if(bean!=null && !TextUtils.isEmpty(bean.getOwnerurl())){
            message.setOwnurl(bean.getOwnerurl());
        }
        if(bean!=null && !TextUtils.isEmpty(bean.getOwnerfirstFrame())){
            message.setOwnfirstFrame(bean.getOwnerfirstFrame());
        }
        return message;
    }


    /**
     * 创建单聊会话(然后在通知聊天界面添加todo)
     */
    public void creatReceivePersonConversation(IMConversationDetailBean message, IMConversationBean customer) {
        IMPersonBean bean1 = DaoUtils.getInstance().queryMessageBean(customer.getConversationId());
        if(bean1==null){
            return;
        }
        IMConversationBean bean = IMSMsgManager.creatSendPersonConversation(customer.getConversationId(), message);
        if(bean==null){
            return;
        }
        DaoUtils.getInstance().insertConversationData(bean);
        EventBus.getDefault().post(new IMCreatMessageEvent(bean));
    }

    public void ondesory() {
        if (timer != null) {
            timer.cancel();
            timer= null;
        }
    }

    public void rightClick(IMConversationBean customer) {
        Intent intent = new Intent(context, IMPersonalInforActivity.class);
        intent.putExtra("user", customer);
        context.startActivity(intent);

    }
    /**
     * 点击屏蔽按钮
     */
    public void httpShieldData(String forbit,IMConversationBean customer) {
        ((IMPersonalChatActivity)context).showLoadingDialog();
        IMHttpCommonBean bean = new IMHttpCommonBean();
        bean.setCustomerId(customer.getConversationId());
        bean.setIsBlock(forbit);
        String json = new Gson().toJson(bean);
        RequestBody body = RequestBody.create(JSON, json);
        IMHttpsService.getShieldPerson(body, new IMHttpResultObserver<String>() {
            @Override
            public void onSuccess(String s, String message) {
                ((IMPersonalChatActivity)context).dismissLoadingDialog();
                ((IMPersonalChatActivity)context).mTvForBit.setVisibility(View.GONE);
                ((IMPersonalChatActivity)context).ll_emoji.setVisibility(View.VISIBLE);
                IMPreferenceUtil.setPreference_Boolean(customer.getConversationId()+IMSConfig.IM_CONVERSATION_TOUSU,false);
            }
            @Override
            public void _onError(Throwable e) {
                ((IMPersonalChatActivity)context).dismissLoadingDialog();
                Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void _onErrorLocal(Throwable e, String message, String code) {
                ((IMPersonalChatActivity)context).dismissLoadingDialog();
                Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * 接收已读未读
     */
    public void recieveRead(IMMessageBean message , List<IMConversationDetailBean> messagelist, IMChatRecyclerAdapter adapter) {
        if (TextUtils.isEmpty(message.getFingerprint())) {
            for (int i = 0; i < messagelist.size(); i++) {
                messagelist.get(i).setIsReaded(true);
                adapter.notifyDataSetChanged();
            }
        } else {
            for (int i = 0; i < messagelist.size(); i++) {
                if (messagelist.get(i).getFingerprint().equals(message.getFingerprint())) {
                    messagelist.get(i).setIsReaded(true);
                    adapter.notifyItemChanged(i);
                    break;
                }
            }
        }
    }

    /**
     * 处理数据源类型 通过msgtype和sendtype组合
     */
    public  int Handlemessage(IMConversationDetailBean messagelist) {
        IMMessageDataJson bean =null;
        try{
            bean = new Gson().fromJson(messagelist.getData(), IMMessageDataJson.class);
        }catch (Exception e){
            return IMSConfig.MSG_CHAT_GROUP;
        }
        int type = Integer.parseInt(bean.getType());
        if(messagelist.getLastMsgSendType()== IMSConfig.MSG_RECIEVE && type==IMSConfig.IM_MESSAGE_TEXT){
            return  IMSConfig.MSG_LEFT_TEXT ;
        }else if(messagelist.getLastMsgSendType()==IMSConfig.MSG_SEND && type==IMSConfig.IM_MESSAGE_TEXT){
            return  IMSConfig.MSG_RIGHT_TEXT;
        }else if(messagelist.getLastMsgSendType()==IMSConfig.MSG_RECIEVE && type==IMSConfig.IM_MESSAGE_PICTURE){
            return  IMSConfig.MSG_LEFT_IMG;
        }else if(messagelist.getLastMsgSendType()==IMSConfig.MSG_SEND && type==IMSConfig.IM_MESSAGE_PICTURE){
            return  IMSConfig.MSG_RIGHT_IMG;
        }else if( type==IMSConfig.IM_MESSAGE_PUSH){
            return  IMSConfig.MSG_PUSH;
        }else if(messagelist.getLastMsgSendType()==IMSConfig.MSG_SEND && type==IMSConfig.IM_MESSAGE_REPACKED){
            return  IMSConfig.MSG_RIGHT_REPACKED;
        }else if(messagelist.getLastMsgSendType()==IMSConfig.MSG_RECIEVE && type==IMSConfig.IM_MESSAGE_REPACKED){
            return  IMSConfig.MSG_LEFT_REPACKED;
        }else if(messagelist.getLastMsgSendType()==IMSConfig.MSG_SEND && type==IMSConfig.IM_MESSAGE_VIDEO){
            return  IMSConfig.MSG_RIGHT_VIDEO;
        }else if(messagelist.getLastMsgSendType()==IMSConfig.MSG_RECIEVE && type==IMSConfig.IM_MESSAGE_VIDEO){
            return  IMSConfig.MSG_LEFT_VIDEO;
        }else if (messagelist.getLastMsgSendType() == IMSConfig.MSG_RECIEVE && type == IMSConfig.IM_MESSAGE_AUDIO) {
            return IMSConfig.MSG_LEFT_AUDIO;
        }else if (messagelist.getLastMsgSendType() == IMSConfig.MSG_SEND && type == IMSConfig.IM_MESSAGE_AUDIO) {
            return IMSConfig.MSG_RIGHT_AUDIO;
        }else if (messagelist.getLastMsgSendType() == IMSConfig.IM_MESSAGE_SYSTEM) {
            return IMSConfig.MSG_CHAT_GROUP;
        }
        return 0;
    }
}
