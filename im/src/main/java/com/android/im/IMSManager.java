package com.android.im;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.text.TextUtils;
import android.widget.ImageView;

import com.android.im.imemoji.IImageLoader;
import com.android.im.imemoji.LQREmotionKit;
import com.android.im.imeventbus.IMAppFinishEvent;
import com.android.im.iminterface.IMManegeLoginInterface;
import com.android.im.iminterface.IMManegerInterface;
import com.android.im.imnet.base.IMServiceFactory;
import com.android.im.imservice.IMRecieveService;
import com.android.im.imutils.IMImageLoadUtil;
import com.android.nettylibrary.IMSConfig;
import com.android.nettylibrary.IMSNettyManager;
import com.android.nettylibrary.bean.IMMessageReceiveEvent;
import com.android.nettylibrary.greendao.entity.IMConversationBean;
import com.android.nettylibrary.greendao.greenutils.DaoUtils;
import com.android.nettylibrary.imconstant.IMSClientFactory;
import com.android.nettylibrary.netty.IMSClientBootstrap;
import com.android.nettylibrary.utils.IMLogUtil;
import com.android.nettylibrary.utils.IMPreferenceUtil;
import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

import org.greenrobot.eventbus.EventBus;

import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;

/**
 * @author jinxin
 * 剑之所指，心之所向
 * @date 2019/8/4
 */
public class IMSManager  {
    private Context context;
    public static IMSManager instance;
    public   static OkHttpClient okHttpClient;
    private IMRecieveService mService;
    private ServiceConnection mConn;
    private IMManegerInterface manegerInterface;
    private IMManegeLoginInterface loginListener;
    private boolean isBind;
    public void setManegerInterface(IMManegerInterface manegerInterface) {
        this.manegerInterface = manegerInterface;
    }

    public Context getContext() {
        return context;
    }

    public static IMSManager getInstance() {
        if (instance == null) {
            throw new RuntimeException("必须先调用静态方法init(Context context)");
        }
        return instance;
    }


    public static void init(Context context) {
        if (context == null) {
            throw new NullPointerException("context不能为空");
        }
        if(instance == null)
            synchronized (IMSManager.class) {
                if(instance == null)
                    instance = new IMSManager(context.getApplicationContext());
            }
    }

    /**
     * 初始化module里面的属性，并且开启im的流程
     */
    public IMSManager(final Context context) {
        this.context = context;
        initImageLoader(context);
        //初始化表情和网络
        initEmojiAndNet(context);
        //初始化PreferenceUtil
        //初始化网络请求
        IMServiceFactory.init(context);
        //初始化Netty
        IMSNettyManager.init(context);
        //初始化httpManager
        IMSHttpManager.init(context);
        //提供外部接口调用的监听
        IMSNettyManager.getInstance().setonHostipListener(new IMSNettyManager.reGetHostIpListener() {
            @Override
            public void onregetHostIp(int type) {
                if(type==IMSConfig.MSG_SYSTEM_RECONNET){
                    EventBus.getDefault().post(new IMMessageReceiveEvent("0"));
                    IMSHttpManager.getInstance().regetImToken(true,false);
                }else if(type==IMSConfig.MSG_SYSTEM_GETOFFLIN_MSG){
                    IMLogUtil.d("MyOwnTag:", "IMSManager ----断线重连重新获取ip222" );
                    if(!IMSHttpManager.getInstance().isIsoffline()){//=true代表正在接收中 这里就不用再发
                        EventBus.getDefault().post(new IMMessageReceiveEvent("2"));
                    }
                    IMSHttpManager.getInstance().setIsoffline(true);
                    IMSHttpManager.getInstance().IMHttpGetOffLineMessage();
                }else  if(type==IMSConfig.MSG_SYSTEM_RELOGIN){
                    EventBus.getDefault().post(new IMAppFinishEvent());
                    reLoginData();
                }else  if(type==IMSConfig.MSG_SYSTEM_DISSCONNECT){
                    //这里代表的是im已经断开
                    IMLogUtil.d("MyOwnTag:", "IMSManager ----imservice已经停止重新启动im服务");
                    if(isBind){
                        context.unbindService(mConn);
                        if(mService!=null){
                            mService.stopSelf();
                        }
                        isBind=false;
                    }
                    IMSHttpManager.getInstance().setIsstarted(false);
                    IMSClientBootstrap.getInstance().setActive(false);
                    IMSClientFactory.getIMSClient().close();
                    IMSHttpManager.getInstance().regetImToken(false,true);
                }
            }
        });
    }


    /**
     * 登录初始化（传了参数才能启动im）
     */
    public void  IMLogin(String uid,String appId){
        IMPreferenceUtil.setPreference_String(IMSConfig.SAVE_APPID, appId);
        IMSHttpManager.getInstance().creatNewUser(uid,appId,true);//true代表第一次开启service,后续不开启
    }
    /**
     * 登录初始化（传了参数四个参数）
     */
    public void  IMLoginParams( String authParm ,  String nonceStr , String signAture , String timeStamp,String appId){
        IMPreferenceUtil.setPreference_String(IMSConfig.SAVE_APPID, appId);
        IMSHttpManager.getInstance().IMHttpLogin(authParm,nonceStr,signAture,timeStamp,appId,true);
    }


    /**
     * 获取本地自己的userid
     */
    public static String getMyUseId(){
        String userId = IMPreferenceUtil.getPreference_String(IMSConfig.SAVE_USERID, "");
        return userId;
    }
    /**
     * 获取本地自己的userid
     */
    public static String getMyUserName(){
        String userId = IMPreferenceUtil.getPreference_String(IMSConfig.SAVE_USER_NAME, "");
        return userId;
    }
    /**
     * 获取本地自己的userid
     */
    public static String getMyPhone(){
        String userId = IMPreferenceUtil.getPreference_String(IMSConfig.PHONE, "");
        return userId;
    }
    /**
     * 获取本地自己的signature
     */
    public static String getMySignature(){
        String signature = IMPreferenceUtil.getPreference_String(IMSConfig.SIGNATURE, "");
        return signature;
    }
    /**
     * 获取本地自己的userid
     */
    public static String getMyNickName(){
        String userId = IMPreferenceUtil.getPreference_String(IMSConfig.SAVE_NAME,"");
        return userId;
    }

    /**
     * 获取本地自己的headurl
     */
    public static String getMyHeadView(){
        String userId = IMPreferenceUtil.getPreference_String(IMSConfig.SAVE_HEADURL, IMSConfig.USER_ID);
        return userId;
    }
    /**
     * 获取本地自己的MyBgurl
     */
    public static String getMyBgUrl(){
        String url = IMPreferenceUtil.getPreference_String(IMSConfig.CHOOSE_MY_BG,"");
        return url;
    }

    public static void setMyBgUrl(String url){
        if(!TextUtils.isEmpty(url)){
            IMPreferenceUtil.setPreference_String(IMSConfig.CHOOSE_MY_BG,url);
        }
    }
    /**
     /**
     * 获取im聊天的token
     */
    public static String getMyToken(){
        String token = IMPreferenceUtil.getPreference_String(IMSConfig.SAVE_IM_TOKEN, null);
        return token;
    }
    /**
     * 获取im聊天的token
     */
    public static String getAppId(){
        String appid = IMPreferenceUtil.getPreference_String(IMSConfig.SAVE_APPID, null);
        return appid;
    }
    /**
     * 初始化ImageLoader
     */
    private void initImageLoader(Context context) {
        ImageLoaderConfiguration.Builder config = new ImageLoaderConfiguration.Builder(context);
        config.threadPriority(Thread.NORM_PRIORITY - 2);
        config.denyCacheImageMultipleSizesInMemory();
        config.memoryCache(new LruMemoryCache(10 * 1024 * 1024));
        config.diskCacheSize(50 * 1024 * 1024); // 50 MiB
        config.tasksProcessingOrder(QueueProcessingType.LIFO);
        ImageLoader.getInstance().init(config.build());
    }
    /**
     * 初初始化表情和网络
     */
    private void initEmojiAndNet(Context context) {
        //初始化表情
        LQREmotionKit.init(context, new IImageLoader() {
            @Override
            public void displayImage(Context context, String path, ImageView imageView) {
                IMImageLoadUtil.CommonImageLoadCp(context,path,imageView);
            }
        });
        //初始化开启接受消息的服务
        okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(5, TimeUnit.MINUTES)
                .readTimeout(5, TimeUnit.MINUTES)
                .build();
    }

    /**
     * 开启服务 进行接收消息
     */

    public void startService() {
        Intent intent = new Intent(context, IMRecieveService.class);
        mConn = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                IMRecieveService.MyBind bind = (IMRecieveService.MyBind) service;
                mService = bind.getService();
            }
            @Override
            public void onServiceDisconnected(ComponentName name) {
            }
        };
        isBind = context.bindService(intent, mConn, Context.BIND_AUTO_CREATE);
    }
    /**
     * 监听
     */
    public void addModuleClickListener(int type) {
//        if(manegerInterface!=null){
//            manegerInterface.setOnModuleClick(type);
//        }
    }

    public void setLoginInListener(boolean success,String message) {
        if(loginListener!=null){
            loginListener.LoginSucess(success,message);
        }
    }


    public void setLoginListener(IMManegeLoginInterface loginListener) {
        this.loginListener = loginListener;
    }


    /**
     * 获取未读数目(app模块调用)
     */
    public void  getUnreadMessageNumber(){
        if(manegerInterface!=null){
            int number=0;
            List<IMConversationBean> beans = DaoUtils.getInstance().queryAllConversationData();
            if(beans!=null){
                for (int i = 0; i < beans.size(); i++) {
                    String a = IMPreferenceUtil.getPreference_String(beans.get(i).getConversationId() + IMSConfig.IM_UNEREAD_CONVERSATION_CHAT, "0");
                    number=number+Integer.parseInt(a);
                }
                manegerInterface.getUnReadnumber(number+"");
            }else {
                manegerInterface.getUnReadnumber("0");
            }
        }
    }

    /**
     * 重复登录同一个账号（多点登录关闭im）
     */
    public void  reLoginData(){
        if(manegerInterface!=null){
            try {
                if(isBind){
                    context.unbindService(mConn);
                    if(mService!=null){
                        mService.stopSelf();
                    }
                    isBind=false;
                }
                IMSHttpManager.getInstance().setIsstarted(false);
                IMSClientBootstrap.getInstance().setActive(false);
                IMSClientFactory.getIMSClient().close();
                manegerInterface.setOnUserReLogin();
            }catch (Exception e){
                IMLogUtil.d("MyOwnTag:", "IMSManager ----  e.printStackTra" );
                e.printStackTrace();
            }

        }
    }
    /**
     * 退出登录(更换id时候清楚数据)
     */
    public void ImChatLoginOut() {
        String  username= IMPreferenceUtil.getPreference_String(IMSConfig.SAVE_USER_NAME, "");
        String  phone= IMPreferenceUtil.getPreference_String(IMSConfig.PHONE, "");
        IMPreferenceUtil.clean();
        IMPreferenceUtil.setPreference_Boolean(IMSConfig.SAVE_LOGIN_DATA, true);
        IMPreferenceUtil.setPreference_String(IMSConfig.SAVE_USER_NAME,username);
        IMPreferenceUtil.setPreference_Boolean(IMSConfig.FIRST_OPEN,false);
        IMPreferenceUtil.setPreference_String(IMSConfig.PHONE,phone);
        try{
            if(isBind){
                context.unbindService(mConn);
                mService.stopSelf();
                isBind=false;
            }
            IMSHttpManager.getInstance().setIsstarted(false);
            IMSClientBootstrap.getInstance().setActive(false);
            IMSClientFactory.getIMSClient().close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * 登录（本地保存有token.在splsh直接登录）
     */
    public void ImChatLoginIn() {
        IMSHttpManager.getInstance().regetImToken(false,true);
    }



}
