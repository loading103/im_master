package com.android.im;

import android.content.Context;
import android.os.Handler;
import android.text.TextUtils;
import android.text.style.UpdateAppearance;
import android.util.Log;
import android.widget.Toast;

import com.android.im.imbean.IMAliyunTokenBean;
import com.android.im.imbean.IMChoosePicBean;
import com.android.im.imbean.IMCompanyBean;
import com.android.im.imbean.IMSSLoginBean;
import com.android.im.imbean.IMTypeBean;
import com.android.im.imeventbus.IMAppUpdataEvent;
import com.android.im.imutils.IMChooseMineBg;
import com.android.nettylibrary.bean.IMMessageReceiveEvent;
import com.android.im.imeventbus.IMRegetGroupListEvent;
import com.android.im.imeventbus.IMRegetPersonListEvent;
import com.android.im.imnet.IMHttpResultObserver;
import com.android.im.imnet.http.IMHttpsService;
import com.android.nettylibrary.IMSConfig;
import com.android.nettylibrary.IMSNettyManager;
import com.android.nettylibrary.bean.IMMessageBean;
import com.android.nettylibrary.event.CEventCenter;
import com.android.nettylibrary.event.Events;
import com.android.nettylibrary.greendao.entity.IMConversationDetailBean;
import com.android.nettylibrary.greendao.entity.IMGroupBean;
import com.android.nettylibrary.greendao.entity.IMPersonBean;
import com.android.nettylibrary.greendao.greenutils.DaoUtils;
import com.android.nettylibrary.http.IMGetIMMemberData;
import com.android.nettylibrary.http.IMHostBean;
import com.android.nettylibrary.http.IMImTokenBean;
import com.android.nettylibrary.http.IMLoginData;
import com.android.nettylibrary.http.IMMessageIdBeans;
import com.android.nettylibrary.http.IMOffLineMsgBean;
import com.android.nettylibrary.http.IMPersonBeans;
import com.android.nettylibrary.http.IMUserInforBean;
import com.android.nettylibrary.netty.NettyTcpClient;
import com.android.nettylibrary.protobuf.MessageProtobuf;
import com.android.nettylibrary.utils.IMLogUtil;
import com.android.nettylibrary.utils.IMPreferenceUtil;
import com.google.gson.Gson;

import org.greenrobot.eventbus.EventBus;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static com.android.im.IMSManager.okHttpClient;
import static com.android.im.imnet.IMBaseConstant.JSON;
import static com.android.nettylibrary.IMSConfig.HTTP_BASE_URL;
import static com.android.nettylibrary.IMSConfig.HTTP_IM_GET_HOST;
import static com.android.nettylibrary.IMSConfig.HTTP_IM_GET_MESSAGEID_LIST;
import static com.android.nettylibrary.IMSConfig.HTTP_IM_GET_OFFLINE;


public class IMSHttpManager {
    private DaoUtils daoUtils;
    public static Context mcontext;
    private static IMSHttpManager mInstance;
    public boolean isstarted;
    public  static void init(Context context){
        if(mInstance == null) {
            mInstance = new IMSHttpManager(context);
        }
    }
    public  static IMSHttpManager getInstance(){
        return mInstance;
    }

    public IMSHttpManager(Context context) {
        mcontext=context;
        daoUtils=DaoUtils.getInstance();
    }
    /**
     * 第1种直接传uid
     */
    public void  creatNewUser(String uid,String appId, final boolean isstart) {
        IMPersonBeans bean = new IMPersonBeans();
        bean.setUid(uid);
        bean.setAppId(appId);
        String json = new Gson().toJson(bean);
        RequestBody body = RequestBody.create(JSON, json);
        IMHttpsService.creatNewUserJson( body, new IMHttpResultObserver<IMLoginData>() {
            @Override
            public void onSuccess(IMLoginData data, String message) {
                if(data!=null){
                    IMLogUtil.d("MyOwnTag:", "id登录成功 " +"(onResponse) " +new Gson().toJson(data));
                    IMHttpLogin(data.getAuthParam(),data.getNonceStr(),data.getSignature(),data.getTimestamp(),IMSManager.getAppId(),isstart);
                }
            }
            @Override
            public void _onError(Throwable e) {
                IMSManager.getInstance().setLoginInListener(false,e.getMessage());
                Toast.makeText(mcontext, e.getMessage(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void _onErrorLocal(Throwable e, String message, String code) {
                IMSManager.getInstance().setLoginInListener(false,message);
                Toast.makeText(mcontext, message, Toast.LENGTH_SHORT).show();
            }
        });
    }
    /**
     * 第2种直接传四个参数
     */
    public IMSHttpManager(Context context, String authParm , String nonceStr , String signAture , String timeStamp, String appId) {
        mcontext=context;
        daoUtils=DaoUtils.getInstance();
        IMHttpLogin(authParm,nonceStr,signAture,timeStamp,appId,true);//true代表第一次开启service,后续不开启
    }
    /**
     * HTTP先登录保存token   然后用token回去个人信息(单点登录)
     */
    public void IMHttpLogin(final String authParm , final String nonceStr ,final String signAture ,final String timeStamp ,String appId , final boolean isstart) {
        IMLoginData bean=new IMLoginData();
        bean.setAuthParam(authParm);
        bean.setSignature(signAture);
        bean.setNonceStr(nonceStr);
        bean.setAppId(appId);
        bean.setTimestamp(timeStamp);
        String json = new Gson().toJson(bean);
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"),json);
        IMHttpsService.LoginJson( body, new IMHttpResultObserver<IMSSLoginBean>() {
            @Override
            public void onSuccess(IMSSLoginBean data, String message) {
                if(data!=null){
                    IMLogUtil.d("MyOwnTag:", "IMSNettyManager " +"(onResponse) " +"单点登录，新token=   "+data);
                    IMPreferenceUtil.setPreference_String(IMSConfig.SAVE_TOKEN,data.getToken());    //保存token之后用token回去个人信息
                    IMPreferenceUtil.setPreference_String(IMSConfig.SAVE_ClIENTID,data.getClientId());    //clientId
                    IMPreferenceUtil.setPreference_String(IMSConfig.SAVE_CLIENTSCRENT,data.getClientSecret());    //clientSecret
                    IMPreferenceUtil.setPreference_String(IMSConfig.SAVE_AUTHPARM,authParm);
                    IMPreferenceUtil.setPreference_String(IMSConfig.SAVE_NONCESTR,nonceStr);
                    IMPreferenceUtil.setPreference_String(IMSConfig.SAVE_SIGNATURE,signAture);
                    IMPreferenceUtil.setPreference_String(IMSConfig.SAVE_TIMESTAMP,timeStamp);

                    IMHttpGetSelfInfor(); //调用http这边的个人信息

                    String imtoken = IMPreferenceUtil.getPreference_String(IMSConfig.SAVE_IM_TOKEN, "");
                    if( TextUtils.isEmpty(imtoken)){   //如果本地无token,获取imtoken，本地有token,直接可以用
                        IMHttpGetIMToken(isstart);
                    }else {
                        IMHttpGetHostIp(isstart);
                    }
                }
            }

            @Override
            public void _onError(Throwable e) {
                IMSManager.getInstance().setLoginInListener(false,e.getMessage());
                Toast.makeText(mcontext, e.getMessage(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void _onErrorLocal(Throwable e, String message, String code) {
                IMSManager.getInstance().setLoginInListener(false,message);
                Toast.makeText(mcontext,message, Toast.LENGTH_SHORT).show();
            }
        });
    }
    /**
     * 用token回去个人信息(获取成功userid之后)
     */
    public void IMHttpGetSelfInfor()  {
        IMHttpsService.GetSelfInforJson(new IMHttpResultObserver<IMUserInforBean.UserInforData>() {
            @Override
            public void onSuccess(IMUserInforBean.UserInforData data, String message) {
                IMLogUtil.d("MyOwnTag:", "IMSNettyManager " +"(onResponse) " +"获取个人信息成功"+new Gson().toJson(data));
                //处理背景图
                if(!TextUtils.isEmpty(data.getBgUrl())){
                    IMSManager.setMyBgUrl(data.getBgUrl());
                }
                IMSMsgManager.SaveLoginData(data);
                IMHttpGetConversationList(true,false);
            }
            @Override
            public void _onError(Throwable e) {
                IMSManager.getInstance().setLoginInListener(false,e.getMessage());
                Toast.makeText(mcontext,e.getMessage(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void _onErrorLocal(Throwable e, String message, String code) {
                IMSManager.getInstance().setLoginInListener(false,message);
                Toast.makeText(mcontext,message, Toast.LENGTH_SHORT).show();
            }
        });
    }
    /**
     * 获取到与自己相关的会话人员列表信息
     */
    public  void IMHttpGetConversationList(boolean  needLogin,boolean needstartservice) {
        IMHttpsService.GetConversationListJson(new IMHttpResultObserver<List<IMPersonBean>>() {
            @Override
            public void onSuccess(List<IMPersonBean> persons, String message) {
                IMLogUtil.d("MyOwnTag:", "IMHttpGetConversationList获取会话人员列表信息成功" + new Gson().toJson(persons));
                IMHttpGetGroupList(persons,needLogin,needstartservice);
                //获取群成功之后让群列表界面进行刷新（这里主要是处理加群和被踢掉群的情况）(SDK时候用的)
                //EventBus.getDefault().post(new IMRegetPersonListEvent(persons));
            }

            @Override
            public void _onError(Throwable e) {
                IMSManager.getInstance().setLoginInListener(false,e.getMessage());
                Toast.makeText(mcontext,e.getMessage(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void _onErrorLocal(Throwable e, String message, String code) {
                IMSManager.getInstance().setLoginInListener(false,message);
                Toast.makeText(mcontext,message, Toast.LENGTH_SHORT).show();

            }
        });
    }
    /**
     * 获取到与自己相关的群列表信息
     */
    public   void IMHttpGetGroupList(List<IMPersonBean> persons,boolean  needLogin,boolean  needstartservice) {
        IMHttpsService.GetGroupListJson(new IMHttpResultObserver<List<IMGroupBean>>() {
            @Override
            public void onSuccess(List<IMGroupBean> groups, String message) {
                IMLogUtil.d("MyOwnTag:", "IMHttpGetConversationList获取到与自己相关的群列表信息成功" + new Gson().toJson(groups));
                UpdateFriendsAndGroup(persons,groups,needLogin,needstartservice);
                //（SDK使用）
                //IMHttpGetCompanySwith();（SDK使用）
                //获取群成功之后让群列表界面进行刷新（这里主要是处理加群和被踢掉群的情况）
                // EventBus.getDefault().post(new IMRegetGroupListEvent(groups));
            }



            @Override
            public void _onError(Throwable e) {
                IMSManager.getInstance().setLoginInListener(false,e.getMessage());
                Toast.makeText(mcontext,e.getMessage(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void _onErrorLocal(Throwable e, String message, String code) {
                IMSManager.getInstance().setLoginInListener(false,message);
                Toast.makeText(mcontext,message, Toast.LENGTH_SHORT).show();
            }
        });
    }



    /**
     * 获取总公司的开关信息
     */
    private void IMHttpGetCompanySwith() {
        IMHttpsService.getCompanySwithJson(new IMHttpResultObserver<IMCompanyBean>() {
            @Override
            public void onSuccess(IMCompanyBean data, String message) {
                String datajson = new Gson().toJson(data);
                IMLogUtil.d("MyOwnTag:", "IMSHttpManager 获取总开关成功--：" +datajson);
                IMPreferenceUtil.setPreference_String(IMSConfig.SAVE_AVAILAVBLE_SWICH,datajson);
                EventBus.getDefault().post(new IMAppUpdataEvent(datajson));
            }

            @Override
            public void _onError(Throwable e) {
                Toast.makeText(mcontext,e.getMessage(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void _onErrorLocal(Throwable e, String message, String code) {
                Toast.makeText(mcontext,message, Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * im失效了直接走im后面的接口获取imtoken
     */

    public void regetImToken(boolean tokenfailed,boolean isstart){
        IMLogUtil.d("MyOwnTag","------im失效了重新获取ip---"+tokenfailed);
        if(tokenfailed){
            IMHttpGetIMToken(isstart);
        }else {
            IMHttpGetHostIp(isstart);
        }
    }



    /**
     * IM获取token(获取不到指纹会重新获取token)
     */
    public   void IMHttpGetIMToken(final boolean isstart) {
        IMHttpsService.getImTokenJson(new IMHttpResultObserver<IMImTokenBean>() {
            @Override
            public void onSuccess(IMImTokenBean persons, String message) {
                IMLogUtil.d("MyOwnTag:", "IMSNettyManager " +"(Response) " +"获取IMToken成功："+persons.getToken());
                IMPreferenceUtil.setPreference_String(IMSConfig.SAVE_IM_TOKEN, persons.getToken());
                IMHttpGetHostIp(isstart);
            }

            @Override
            public void _onError(Throwable e) {
                Toast.makeText(mcontext,e.getMessage(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void _onErrorLocal(Throwable e, String message, String code) {
                Toast.makeText(mcontext,message, Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * 动态获取ip
     */
    public void IMHttpGetHostIp(final boolean isstart) {
        String token = IMPreferenceUtil.getPreference_String(IMSConfig.SAVE_IM_TOKEN, null);
        Request request = new Request.Builder()
                .url(HTTP_IM_GET_HOST+"?access_token="+token)
                .get()
                .build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                IMLogUtil.d("MyOwnTag:", "IMSNettyManager " +"(onFailure) " +"动态获取ip");
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                IMHostBean bean =null;
                try {
                    bean = new Gson().fromJson(response.body().string(), IMHostBean.class);
                }catch (Exception e){
                    return;
                }
                IMLogUtil.d("MyOwnTag1:", "IMSNettyManager " +"(Response) " +"动态获取ip："+new Gson().toJson(bean));
                if(bean==null || bean.getData()==null){
                    IMPreferenceUtil.setPreference_String(IMSConfig.IM_SAVE_HOST,IMSConfig.IMS_SERVICE_IP);
                }else {
                    IMPreferenceUtil.setPreference_String(IMSConfig.IM_SAVE_HOST,bean.getData().getSocketAddress());
                }
                //获取离线
//                IMHttpGetOffLineMessage();
                //获取消息指纹
                if(IMSMsgManager.getNeedGetMessageIds()) {
                    IMSHttpManager.getInstance().IMHttpGetIMFigId();
                }
                if(isstart){
                    isstarted=true;
                    IMSManager.getInstance().startService();
                }
            }
        });
    }


    /**
     *获取消息池ID列表
     */
    public  void IMHttpGetIMFigId() {
        final String token = IMPreferenceUtil.getPreference_String(IMSConfig.SAVE_IM_TOKEN, "");
        Request request = new Request.Builder()
                .url(HTTP_IM_GET_MESSAGEID_LIST+"?access_token="+token+"&number=50")
                .get()
                .build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                IMLogUtil.d("MyOwnTag:", "IMSNettyManager " +"(onFailure) " +"获取消息池ID列表："+e.getMessage());
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                IMMessageIdBeans data =null;
                try {
                    data = new Gson().fromJson(response.body().string(), IMMessageIdBeans.class);
                }catch (Exception e){
                    return;
                }
                if(data==null || data.getData()==null){
                    return;
                }
                List<String> persons=data.getData();
                StringBuffer stringBuffer=new StringBuffer();
                if(!TextUtils.isEmpty(IMPreferenceUtil.getPreference_String(IMSConfig.SAVE_MESSAGE_FIGID,""))){
                    stringBuffer.append(IMPreferenceUtil.getPreference_String(IMSConfig.SAVE_MESSAGE_FIGID,""));
                }
                if(persons!=null && persons.size()>0){
                    for (int i = 0; i < persons.size(); i++) {
                        if(i==persons.size()-1){
                            stringBuffer.append(persons.get(i));
                        } else if(i==0){
                            if(TextUtils.isEmpty(stringBuffer.toString())){
                                stringBuffer.append(persons.get(i)+",");
                            }else {
                                stringBuffer.append(","+persons.get(i)+",");
                            }
                        }
                        else {
                            stringBuffer.append(persons.get(i)+",");
                        }
                    }
                    IMPreferenceUtil.setPreference_String(IMSConfig.SAVE_MESSAGE_FIGID,stringBuffer.toString());
                }
            }
        });
    }

    /**
     * 链接成功之后获取离线消息
     */
    public final int size=200;
    public  final int current=1;

    public boolean isoffline=false;// 判断是不是在获取离线

    public boolean isIsoffline() {
        return isoffline;
    }

    public void setIsoffline(boolean isoffline) {
        this.isoffline = isoffline;
    }

    public void IMHttpGetOffLineMessage() {
        final String token = IMPreferenceUtil.getPreference_String(IMSConfig.SAVE_IM_TOKEN, "");
        Request request = new Request.Builder()
                .url(HTTP_IM_GET_OFFLINE+"current="+current+"&size="+size+"&access_token="+token)
                .get()
                .build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                isoffline=false;
                data.clear();
                EventBus.getDefault().post(new IMMessageReceiveEvent("2"));
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try {
                    IMOffLineMsgBean bean = new Gson().fromJson(response.body().string(), IMOffLineMsgBean.class);
                    IMLogUtil.d("MyOwnTag:", "IMSHttpManager 离线消息--132.232.122.151 6610--" +new Gson().toJson(bean));
                    if(bean!=null && bean.getData()!=null &bean.getData().getRecords()!=null){
                        handleOffLineMessage(bean);
                    }
                }catch (Exception e){
                    data.clear();
                    EventBus.getDefault().post(new IMMessageReceiveEvent("2"));
                }
            }
        });
    }

    private List<String> data=new ArrayList<>();//用来过滤离线消息去重
    private void handleOffLineMessage(IMOffLineMsgBean bean) {
        List<IMOffLineMsgBean.IMoffLineMsgDatailBean> records = bean.getData().getRecords();
        List<String>datas=new ArrayList<>();

        //接受到离线消息需要发回执消息才能确保收到消息了
        for (int i = 0; i < records.size(); i++) {
            datas.add(records.get(i).getFingerprint());
        }
        MessageProtobuf.ImMessage receivedReportMsg = buildReceivedReportMsg(datas);
        if(receivedReportMsg != null) {
            NettyTcpClient.getInstance().sendMsg(receivedReportMsg);
        }

        for (int i = 0; i < records.size(); i++) {
            IMMessageBean msg = new IMMessageBean();
            msg.setData(records.get(i).getData());
            msg.setClientId(records.get(i).getClientId());
            msg.setFingerprint(records.get(i).getFingerprint());
            msg.setSenderId(records.get(i).getSenderId());
            msg.setReceiverId(records.get(i).getReceiverId());
            msg.setTimestamp(records.get(i).getSendTime());
            msg.setMsgType(Integer.parseInt(records.get(i).getType()));

            //用来过滤离线消息去重
            if(!data.contains(records.get(i).getFingerprint())){
                data.add(records.get(i).getFingerprint());
                IMConversationDetailBean imPersonDetailBean = daoUtils.queryConversationDetailDataAccordFieldFiger(records.get(i).getFingerprint());
                if(imPersonDetailBean!=null){
                    continue;
                }
                if(!msg.getSenderId().equals(IMSManager.getMyUseId())){
                    if(!TextUtils.isEmpty(records.get(i).getGroupId()) ){
                        msg.setGroupId(records.get(i).getGroupId());
                        CEventCenter.dispatchEvent(Events.CHAT_GROUP_MESSAGE, 0, 0, msg);
                    }else {
                        CEventCenter.dispatchEvent(Events.CHAT_SINGLE_MESSAGE, 0, 0, msg);
                    }
                }
            }
        }
        if(bean.getData().getPages()>=1 && bean.getData().getRecords()!=null && bean.getData().getRecords().size()>0 && size<bean.getData().getTotal() ){
            IMHttpGetOffLineMessage();
            IMLogUtil.d("MyOwnTag:", "IMSHttpManager " +"current= "+current+"--totle="+bean.getData().getTotal());
        }else {
            IMLogUtil.d("MyOwnTag:", "IMSHttpManager " +"current=1 (end)");
            isoffline=false;
            EventBus.getDefault().post(new IMMessageReceiveEvent("2"));
            data.clear();
        }
    }

    public boolean isIsstarted() {
        return isstarted;
    }

    public void setIsstarted(boolean isstarted) {
        this.isstarted = isstarted;
    }

    /**
     * 收到服务器消息后回执
     * @param
     * @return
     */
    private MessageProtobuf.ImMessage buildReceivedReportMsg( List<String> fightid) {
        if (fightid==null || fightid.size()==0) {
            return null;
        }
        MessageProtobuf.ImMessage.Builder builder = MessageProtobuf.ImMessage.newBuilder();
        builder.addAllFingerprints(fightid);
        builder.setType(MessageProtobuf.ImMessage.TypeEnum.RECEIPT);
        if(!TextUtils.isEmpty(IMSNettyManager.getMyToken())){
            builder.setToken(IMSNettyManager.getMyToken());
        }
        return builder.build();
    }

    /**
     * 接口获取好友和群聊（跟新数据库）
     */
    private void UpdateFriendsAndGroup(List<IMPersonBean> persons, List<IMGroupBean> groups, boolean isLogin,boolean  needstartservice) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                if (persons != null && persons.size() > 0) {
                    daoUtils.deleteMessageData();
                    for (int i = 0; i < persons.size(); i++) {
                        persons.get(i).setMemberId(persons.get(i).getCustomerId());
                        daoUtils.insertMessageData(persons.get(i));
                        if(!TextUtils.isEmpty(persons.get(i).getIsBlock()) && persons.get(i).getIsBlock().equals("Y")){
                            IMPreferenceUtil.setPreference_Boolean(persons.get(i).getCustomerId()+IMSConfig.IM_CONVERSATION_TOUSU,true);
                        }else {
                            IMPreferenceUtil.setPreference_Boolean(persons.get(i).getCustomerId()+IMSConfig.IM_CONVERSATION_TOUSU,false);
                        }
                    }
                }

                if(isLogin){
                    //登录成功回调
                    IMSManager.getInstance().setLoginInListener(true, null);
                    if (needstartservice){
                        //获取IM参数并且启动im
                        String imtoken = IMPreferenceUtil.getPreference_String(IMSConfig.SAVE_IM_TOKEN, "");
                        if (TextUtils.isEmpty(imtoken)) {   //如果本地无token,获取imtoken，本地有token,直接可以用
                            IMSHttpManager.getInstance().IMHttpGetIMToken(true);
                        } else {
                            IMSHttpManager.getInstance().IMHttpGetHostIp(true);
                        }
                    }

                }
                if (groups != null && groups.size() > 0) {
                    daoUtils.deleteGroupAllData();
                    for (int i = 0; i < groups.size(); i++) {
                        daoUtils.insertGroupData(groups.get(i));
                    }
                }
            }
        }).start();
    }


    /**
     * 获取Aliyun OSS 认证Token
     */
    public void IMHttpGetAliyunToken()  {
//        IMGetIMMemberData bean=new IMGetIMMemberData();
//        String json = new Gson().toJson(bean);
//        RequestBody body = RequestBody.create(JSON, json);
//        Request request = new Request.Builder()
//                .url(HTTP_BASE_URL+"aliyun-oss/token")
//                .addHeader("Authorization", IMPreferenceUtil.getPreference_String(IMSConfig.SAVE_TOKEN,""))
//                .post(body)
//                .build();
//        Log.e("请求数据：",HTTP_BASE_URL+"aliyun-oss/token");
//        Call call = okHttpClient.newCall(request);
//        call.enqueue(new Callback() {
//            @Override
//            public void onFailure(Call call, IOException e) {
//                IMLogUtil.e("MyOwnTag:", "获取Aliyun OSS 认证Token---失败-" );
//            }
//            @Override
//            public void onResponse(Call call, Response response) throws IOException {
//                try {
//                    IMAliyunTokenBean bean = new Gson().fromJson(response.body().string(), IMAliyunTokenBean.class);
//                    IMLogUtil.e("MyOwnTag:", "获取Aliyun OSS 认证Token----" +new Gson().toJson(bean));
//                    IMPreferenceUtil.setPreference_String(Config.OSS_SECURITY_TOKEN, !TextUtils.isEmpty(bean.getSecurityToken())?bean.getSecurityToken():"");
//                    IMPreferenceUtil.setPreference_String(Config.OSS_ACCESS_KEY_ID, !TextUtils.isEmpty(bean.getAccessKeyId())?bean.getAccessKeyId():"");
//                    IMPreferenceUtil.setPreference_String(Config.OSS_ACCESS_KEY_SECRET, !TextUtils.isEmpty(bean.getAccessKeySecret())?bean.getAccessKeySecret():"");
//
//                }catch (Exception e){
//                }
//            }
//        });
    }
}
