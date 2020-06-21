package com.android.im.imui.activity;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.android.im.IMSManager;
import com.android.im.R;
import com.android.im.imbean.IMChoosePicBean;
import com.android.im.imutils.IMImageLoadUtil;
import com.android.im.imview.dialog.IMDialogMessageTypeTwo;
import com.android.im.imview.dialog.IMDialogUtils;
import com.android.im.imview.dialog.IMIosCommonDiglog;
import com.android.im.imview.imloadingview.IMLoadingDialog;
import com.android.im.imview.imloadingview.IMMyLoadingDialog;
import com.android.nettylibrary.IMSConfig;
import com.android.nettylibrary.greendao.greenutils.DaoUtils;
import com.android.nettylibrary.utils.IMLogUtil;
import com.android.nettylibrary.utils.IMPreferenceUtil;
import com.google.gson.Gson;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.DefaultRefreshFooterCreator;
import com.scwang.smartrefresh.layout.api.DefaultRefreshHeaderCreator;
import com.scwang.smartrefresh.layout.api.RefreshFooter;
import com.scwang.smartrefresh.layout.api.RefreshHeader;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.footer.ClassicsFooter;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import me.imid.swipebacklayout.lib.app.SwipeBackActivity;
import pub.devrel.easypermissions.EasyPermissions;

public class IMBaseActivity extends IMSwipeBackActivity implements EasyPermissions.PermissionCallbacks{
    public DaoUtils daoUtils;
    private Unbinder unbinder;
    //    public IMMyLoadingDialog waittingDialog;
    public IMMyLoadingDialog waittingDialog;
    public IMDialogMessageTypeTwo dialogMessage;
    private IMIosCommonDiglog commonDiglog;
    static {
        //设置全局的Header构建器
        SmartRefreshLayout.setDefaultRefreshHeaderCreator(new DefaultRefreshHeaderCreator() {
            @Override
            public RefreshHeader createRefreshHeader(Context context, RefreshLayout layout) {
                layout.setPrimaryColorsId(R.color.color_EE555555, android.R.color.white);//全局设置主题颜色
                return new ClassicsHeader(context);
            }
        });
        //设置全局的Footer构建器
        SmartRefreshLayout.setDefaultRefreshFooterCreator(new DefaultRefreshFooterCreator() {
            @Override
            public RefreshFooter createRefreshFooter(Context context, RefreshLayout layout) {
                //指定为经典Footer，默认是 BallPulseFooter
                return new ClassicsFooter(context).setDrawableSize(20);
            }
        });

    }
    String[] perms = { Manifest.permission.CAMERA,
            Manifest.permission.RECORD_AUDIO,
            android.Manifest.permission.READ_EXTERNAL_STORAGE,
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        daoUtils = DaoUtils.getInstance();  //初始化greendao
        commonDiglog=new IMIosCommonDiglog(this);
        requestPermissions();
    }

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        unbinder = ButterKnife.bind(this);
    }

    /**
     * 对话框(仿ios)
     */
    public void showCommonDialog( String message, View.OnClickListener listener) {
        commonDiglog.showCommonDiglog(message,listener);
    }
    public void dissCommonDialog() {
        commonDiglog.dismissCommonDiglog();
    }
    public void showSingleCommonDialog( String message ) {
        commonDiglog.showSingleCommonDiglog(message);
    }
    /**
     * 显示等候对话框
     */
    public void showLoadingDialog() {
        waittingDialog= new IMMyLoadingDialog(this);
        waittingDialog.show();
    }

    public void dismissLoadingDialog() {
        if (waittingDialog != null) {
            waittingDialog.dismiss();
        }
    }
    public IMLoadingDialog loadingDialog;
    public void showLoadingDialogs() {
        loadingDialog= new IMLoadingDialog(this);
        loadingDialog.show();
    }

    public void dismissLoadingDialogs() {
        if (loadingDialog != null) {
            loadingDialog.dismiss();
        }
        dismissLoadingDialogTypeTwo();
    }
    /**
     * 显示加载界面loading(第二种自定义)
     */
    protected void showLoadingDialogTypeTwo(String msg) {
        if (dialogMessage != null) {
            dialogMessage.dissmissDialog();
            dialogMessage = null;
        }
        dialogMessage = new IMDialogMessageTypeTwo(this);
        dialogMessage.setType(1);
        if(!TextUtils.isEmpty(msg)){
            dialogMessage.setMessage(msg);
        }
        dialogMessage.showDialog();
    }

    protected void dismissLoadingDialogTypeTwo() {
        if (dialogMessage != null) {
            dialogMessage.dissmissDialog();
            dialogMessage = null;
        }
    }

    /**
     * 显示加载界面loading第二种支持上传进度等
     */
    protected IMDialogMessageTypeTwo progressDialog;
    public void showLoadingDialogProgress(int progeress) {
        if (progressDialog == null) {
            progressDialog = new IMDialogMessageTypeTwo(this);
            progressDialog.setType(1);
            progressDialog.showDialog();
        }
        if(!progressDialog.isShowing()){
            progressDialog.showDialog();
        }
        progressDialog.setProgress(progeress);
    }

    public void dismissProgress() {
        if(progressDialog!=null){
            progressDialog.dissmissDialog();
            progressDialog = null;
        }
    }
    /**
     * 设置添加屏幕的背景透明度
     *
     * @param bgAlpha
     */
    public void backgroundAlpha(Activity context, float bgAlpha) {
        WindowManager.LayoutParams lp = context.getWindow().getAttributes();
        lp.alpha = bgAlpha;
        context.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        context.getWindow().setAttributes(lp);
    }
    /**
     * 动态添加权限
     */
    private void requestPermissions() {
        if (EasyPermissions.hasPermissions(this, perms)) {
            //todo 已经获取了权限
            IMLogUtil.d("tag", "BaseActivity " +"(requestPermissions) " + true , 45, "perms = " + perms);
        } else {
            //没有获取了权限，申请权限
            EasyPermissions.requestPermissions(this, "为了优化您的使用体验，请打开下列必要的权限", 0, perms);
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {

    }

    @Override
    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {

    }


    public void  setViewBackGroup(ImageView mIv){
        IMImageLoadUtil.CommonImageBgLoadCp(this, IMSManager.getMyBgUrl(),mIv);
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }
    public void startActicity(Class<?> cls){
        startActivity(new Intent(this,cls));
    }
    public void startActicity(Class<?> cls,Bundle bundle){
        startActivity(new Intent(this,cls).putExtras(bundle));
    }
}
