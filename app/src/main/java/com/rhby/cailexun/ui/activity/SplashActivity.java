package com.rhby.cailexun.ui.activity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;

import com.android.im.IMSHttpManager;
import com.android.im.IMSManager;
import com.android.im.imbean.IMBetGetBean;
import com.android.im.imbean.SplashAdBean;
import com.android.im.imnet.IMHttpResultObserver;
import com.android.im.imui.activity.IMWebActivity;
import com.android.im.imutils.IMImageLoadUtil;
import com.android.im.imutils.IMStatusBarUtil;
import com.android.im.imview.dialog.IMDialogUtils;
import com.rhby.cailexun.R;
import com.rhby.cailexun.bean.VersonBeanData;
import com.rhby.cailexun.net.HttpResultObserver;
import com.rhby.cailexun.net.http.HttpsService;
import com.rhby.cailexun.ui.base.BaseActivity;
import com.rhby.cailexun.utils.MyUpdateUtils;
import com.rhby.cailexun.utils.MyUtils;
import com.rhby.cailexun.widget.CutTimeDownView;
import com.android.nettylibrary.IMSConfig;
import com.android.nettylibrary.greendao.entity.IMPersonBean;
import com.android.nettylibrary.utils.IMLogUtil;
import com.android.nettylibrary.utils.IMPreferenceUtil;
import com.blankj.utilcode.util.NetworkUtils;
import com.google.gson.Gson;

import java.io.File;
import java.util.List;

import butterknife.OnClick;
import okhttp3.MediaType;
import okhttp3.RequestBody;

public class SplashActivity extends BaseActivity implements CutTimeDownView.OnFinishListener, View.OnClickListener {
    private String token;
    private String name;
    private ImageView mIvBg;
    private CutTimeDownView skipTv;
    public  Handler mhandle=new Handler();
    private List<SplashAdBean> datas;
    private int index;//请求广告是一个列表  ，随机选一张
    public  boolean   getdata=false;


    private int versionCode;// 版本号
    private String versionName;// 版本名
    private VersonBeanData beanData;
    public boolean isqzup;   //判断是不是强制跟新
    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 601:
                    //对话框通知用户升级程序
                    String qz =  msg.obj+"";
                    //如果是q 强制升级
                    if (qz.equals("Y")) {
                        isqzup=true;
                        showUpdataDialog();
                    } else {
                        isqzup=false;
                        showUpdataDialog();
                    }
                    break;
                case 603:
                    //下载apk失败
                    if(!isqzup){
                        loginActivity();
                        Toast.makeText(getApplication(), "下载新版本失败,缺少必要的权限", Toast.LENGTH_SHORT).show();
                    }else {
                        loginActivity();
                        Toast.makeText(getApplication(), "下载失败,请在权限管理中打开必要的权限", Toast.LENGTH_LONG).show();
                    }
                    break;
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        IMStatusBarUtil.setTranslucentForImageView(this,0,null);
        skipTv=findViewById(R.id.skipTv);
        mIvBg=findViewById(R.id.iv_bg);
        if(IMPreferenceUtil.getPreference_Boolean(IMSConfig.FIRST_OPEN, true)){
            IMPreferenceUtil.setPreference_Boolean(IMSConfig.FIRST_OPEN,false);
            startActicity(GuidePageActivity.class);
            finish();
            return;
        }
        //获取当前版本信息(版本跟新)
        getMyVersion();
        checkVersion();
    }


    private void getHttpData() {
        final IMBetGetBean bean = new IMBetGetBean();
        bean.setLocationCode("start_app");
        String json = new Gson().toJson(bean);
        IMLogUtil.d("MyOwnTag:", "SplashActivity ----" +json);
        if(!NetworkUtils.isConnected()){
            Toast.makeText(SplashActivity.this, "网络不通，请检查网络设置", Toast.LENGTH_SHORT).show();
            ifAutoLogin(3000);
            return ;
        }
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json);
        HttpsService.getGetAdJson(body, new IMHttpResultObserver<List<SplashAdBean>>() {
            @Override
            public void onSuccess(List<SplashAdBean> s, String message) {
                if(s==null || s.size()==0){
                    ifAutoLogin(3000);
                    return;
                }
                getdata=true;
                index = (int) (Math.random() *  s.size());
                String time = s.get(index).getAutoTime() + "000";
                ifAutoLogin(Integer.parseInt(time));
                try {
                    handleImageView(s);
                }catch (Exception e){
                    return;
                }
            }

            @Override
            public void _onError(Throwable e) {
                ifAutoLogin(3000);
            }

            @Override
            public void _onErrorLocal(Throwable e, String message, String code) {
                ifAutoLogin(3000);
            }
        });
    }

    /**
     * 显示图片
     */
    private void handleImageView(List<SplashAdBean> s) {
        datas = s;
        if(!s.get(index).getAdsImgUrl().endsWith(".gif")){
            IMImageLoadUtil.CommonSplashImageLoadCp(SplashActivity.this,s.get(index).getAdsImgUrl(),mIvBg);
        }else {
            IMImageLoadUtil.CommonGifLoadCp(SplashActivity.this,s.get(index).getAdsImgUrl(),mIvBg);
        }
    }

    /**
     * 跳转到登录页面还是主页面
     * */
    private void ifAutoLogin(long time) {
        token = IMPreferenceUtil.getPreference_String(IMSConfig.SAVE_TOKEN, "");
        name = IMPreferenceUtil.getPreference_String(IMSConfig.SAVE_NAME, "");
        //本地有保存 优先登录im
        mhandle.postDelayed(new Runnable() {
            @Override
            public void run() {
                if(!TextUtils.isEmpty(token)){
//                    IMSManager.getInstance().ImChatLoginIn();
                    //此处是刷新会话列表好友在线离线状态
                    IMSHttpManager.getInstance().IMHttpGetConversationList(false,false);
                }
            }
        },1000);
        skipTv.setTotalTime(time);
        skipTv.setOnFinishListener(this);
    }

    @OnClick({R.id.skipTv,R.id.iv_bg})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.skipTv:
                skipTv.setOnDestoryed();
                loginActivity();
                break;
            case R.id.iv_bg:
                if(getdata){
                    if(datas==null || datas.size()==0){
                        return;
                    }
                    skipTv.setOnDestoryed();
                    Intent intent=new Intent(SplashActivity.this, IMWebActivity.class);
                    intent.putExtra("url",datas.get(index).getUrl());
                    startActivityForResult(intent,10001);
                }else {
                    loginActivity();
                }

                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        skipTv.setVisibility(View.GONE);
        if(requestCode==10001){
            loginActivity();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        skipTv.setOnDestoryed();
    }
    /**
     * 倒计时结束
     */
    @Override
    public void setOnFinishListener() {
        loginActivity();
    }
    /**
     * 判断是否进入登录界面(先获取个人信息半段是不是账号过期或是封号)
     */
    private void loginActivity() {
        getPersonInfo();

    }

    /**
     * 查询个人信息(此接口无用 ，用来检测 看是不是被封号或是登录超时)
     */
    private void getPersonInfo() {
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"),"");
        HttpsService.getPersonInfo(body, new HttpResultObserver<IMPersonBean>() {
            @Override
            public void onSuccess(IMPersonBean s, String message) {
                startActivity();
            }
            @Override
            public void _onError(Throwable e) {
                startActivity();
            }
            @Override
            public void _onErrorLocal(Throwable e, String message, String code) {
                if(code.equals("NO_LOGIN")){
                    IMSManager.getInstance().ImChatLoginOut();
                    Intent intent=new Intent(SplashActivity.this, LoginNewActivity.class);
                    startActivity(intent);
                }

            }
        });
    }


    public void  startActivity(){
        if(TextUtils.isEmpty(token) || TextUtils.isEmpty(name)){
            Intent intent=new Intent(SplashActivity.this, LoginNewActivity.class);
            startActivity(intent);
        }else {
            IMSManager.getInstance().ImChatLoginIn();
            Intent intent=new Intent(SplashActivity.this, MainActivity.class);
            startActivity(intent);
        }
        finish();
    }
    /**
     * 获取versioncode 和versionname
     */
    public void getMyVersion() {
        // 获取自己的版本信息
        PackageManager pm = getPackageManager();
        try {
            PackageInfo packageInfo = pm.getPackageInfo(getPackageName(), 0);
            // 版本号
            versionCode = packageInfo.versionCode;
            // 版本名
            versionName = packageInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {

        }
    }
    private String downUrl;
    private void checkVersion() {
        final IMBetGetBean bean = new IMBetGetBean();
        bean.setSystemType("1");
        String json = new Gson().toJson(bean);
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json);
        //{"code":"OK","data":{"code":"1","systemType":"1","downloadUrl":"http://download.tuofub.com/app/tuofub/tuofubao.apk","downloadType":1,"isForce":"Y",
        // "version":"1.0.0","updateDesc":"tomatao 聊天app上线了"},"message":"成功","serverTime":1578119816264}
        HttpsService.getVersionJson(body, new IMHttpResultObserver<VersonBeanData>() {
            @Override
            public void onSuccess(VersonBeanData s, String message) {
                if(s==null){
                    getHttpData();
                    return;
                }
                beanData = s;
                downUrl=s.getDownloadUrl();
                String currentVersion="V" + versionCode + "." + versionName;
                String  getVerson="V" + s.getCode() + "." + s.getVersion();
                if(TextUtils.isEmpty(s.getCode())){
                    getHttpData();//广告
                    return;
                }
                Boolean update = MyUtils.isNeedUpdate(SplashActivity.this, currentVersion,getVerson);
                if(!update){
                    Log.e("-----", "版本号相同无需升级：：" + currentVersion);
                    getHttpData();//广告
                }else{
                    Log.e("-----", "版本号不同 ,提示用户升级：：" + currentVersion);
                    Message msg = Message.obtain();
                    msg.obj = s.getIsForce();
                    msg.what = 601;
                    handler.sendMessage(msg);
                }
            }

            @Override
            public void _onError(Throwable e) {
                getHttpData();
            }

            @Override
            public void _onErrorLocal(Throwable e, String message, String code) {
                getHttpData();
            }
        });
    }

    /**
     * 家长端升级对话框
     */
    protected void showUpdataDialog() {
        IMDialogUtils.getInstance().showUpdataVersonDialog(this,beanData.getVersion(),beanData.getUpdateDesc(),isqzup, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IMDialogUtils.getInstance().dismissCommonDiglog();
                try{
                    downLoadApk();
                }catch (Exception e){
                    Toast.makeText(SplashActivity.this, "请在权限设置里打开访问SD卡权限", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
        }, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IMDialogUtils.getInstance().dismissCommonDiglog();
                loginActivity();
            }
        });
    }

    /**
     * 从服务器中下载APK
     */
    protected void downLoadApk() {
        final ProgressDialog pd;    //进度条对话框
        pd = new ProgressDialog(this);
        pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        pd.setCanceledOnTouchOutside(false);
        pd.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
                    dialog.dismiss();
                    finish();
                }
                return false;
            }
        });
        pd.setMessage("正在下载更新");
        pd.setIndeterminate(false);
        pd.show();
        new Thread() {
            @Override
            public void run() {
                try {
                    File file = MyUpdateUtils.getFileFromServer(beanData.getDownloadUrl(), pd);
                    sleep(3000);
                    installApk(file);
                    pd.dismiss(); //结束掉进度条对话框
                } catch (Exception e) {
                    Message msg = new Message();
                    msg.what = 603;
                    handler.sendMessage(msg);
                    pd.dismiss(); //结束掉进度条对话框
                    e.printStackTrace();
                }
            }
        }.start();
    }
    /**
     * 安装apk
     */
    protected void installApk(File file) {
        Intent intent = new Intent();
        //执行动作
        intent.setAction(Intent.ACTION_VIEW);
        //执行的数据类型
        intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");

        //判断是否是AndroidN以及更高的版本，兼容7.0以上安卓版本
        if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.N) {
            Uri contentUri = FileProvider.getUriForFile(this,"com.rhby.cailexun.fileprovider",file);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.setDataAndType(contentUri,"application/vnd.android.package-archive");
        }else{
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setDataAndType(Uri.fromFile(file),"application/vnd.android.package-archive");
        }
        startActivity(intent);
        finish();
    }
}
