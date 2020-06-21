package com.android.im;

import android.content.Context;

import com.android.im.imbean.DaoMaster;
import com.android.im.imbean.DaoSession;

import org.greenrobot.greendao.query.QueryBuilder;

/**
 * Created by Administrator on 2019/12/24.
 * Describe:
 */
public class DaoManager {
    private static final String DB_NAME="small_program.db";
    private final Context context;
    private DaoMaster.DevOpenHelper mSQLiteOpenHelper;
    private DaoSession mDaoSession;
    private DaoMaster mDaoMaster;
    private static DaoManager mInstance;

    public DaoManager(Context context) {
        this.context=context;
    }

    public  static void init(Context context){
        if(mInstance == null) {
            mInstance = new DaoManager(context);
        }
    }
    public  static DaoManager getInstance(){
        return mInstance;
    }

    //获取DaoSession，从而获取各个表的操作DAO类
    public DaoSession getDaoSession() {
        if (mDaoSession == null) {
            initDataBase();
        }
        return mDaoSession;
    }

    //初始化数据库及相关类
    private void initDataBase(){
        setDebugMode(true);//默认开启Log打印
        mSQLiteOpenHelper = new DaoMaster.DevOpenHelper(context, DB_NAME, null);//建库
        mDaoMaster = new DaoMaster(mSQLiteOpenHelper.getWritableDatabase());
        mDaoSession = mDaoMaster.newSession();
        mDaoSession.clear();//清空所有数据表的缓存
    }

    //是否开启Log
    public void setDebugMode(boolean flag) {
        QueryBuilder.LOG_SQL = flag;
        QueryBuilder.LOG_VALUES = flag;
    }
}
