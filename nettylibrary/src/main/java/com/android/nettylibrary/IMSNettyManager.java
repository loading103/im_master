package com.android.nettylibrary;

import android.content.Context;
import com.android.nettylibrary.greendao.GreenDaoManager;
import com.android.nettylibrary.greendao.greenutils.DaoUtils;
import com.android.nettylibrary.utils.IMPreferenceUtil;

/**
 * @author jinxin
 * 剑之所指，心之所向
 * @date 2019/8/4
 */
public class IMSNettyManager {
    private Context context;
    public DaoUtils daoUtils;
    private static IMSNettyManager instance;

    public Context getContext() {
        return context;
    }

    public static IMSNettyManager getInstance() {
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
            synchronized (IMSNettyManager.class) {
                if(instance == null)
                    instance = new IMSNettyManager(context.getApplicationContext());
            }
    }

    public IMSNettyManager(Context context) {
        this.context = context;
        GreenDaoManager.init(context);
        daoUtils= DaoUtils.getInstance();
    }


    /**
     * 获取本地自己的userid
     */
    public static String getMyUseId(){
        String userId = IMPreferenceUtil.getPreference_String(IMSConfig.SAVE_USERID, IMSConfig.USER_ID);
        return userId;
    }
    public static String getMyToken(){
        String userId = IMPreferenceUtil.getPreference_String(IMSConfig.SAVE_IM_TOKEN, null);
        return userId;
    }

    /**
     * 链接异常 给im  module提供接口处理
     */
    public void regetImToken(int type){
        if(listener!=null){
            listener.onregetHostIp(type);
        }

    }

    public interface reGetHostIpListener{
        void  onregetHostIp(int type);
    }
    private  reGetHostIpListener listener;

    public void setonHostipListener(reGetHostIpListener listener) {
        this.listener = listener;
    }
}
