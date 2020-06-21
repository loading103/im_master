package com.rhby.cailexun.ui.base;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import androidx.annotation.NonNull;

import com.android.im.R;
import com.android.im.imui.activity.IMSwipeBackActivity;
import com.android.im.imview.dialog.IMDialogMessageTypeTwo;
import com.android.im.imview.dialog.IMIosCommonDiglog;
import com.android.im.imview.imloadingview.IMMyLoadingDialog;
import com.android.nettylibrary.utils.IMLogUtil;
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
import pub.devrel.easypermissions.EasyPermissions;

public class BaseActivity extends IMSwipeBackActivity implements EasyPermissions.PermissionCallbacks {
    private Unbinder unbinder;
    private  IMIosCommonDiglog  commonDiglog;
    static {
        //设置全局的Header构建
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

    public IMMyLoadingDialog loadingDialog;
    String[] perms = {Manifest.permission.CAMERA,
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE};
    private IMDialogMessageTypeTwo dialogMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestPermissions();
    }

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        unbinder = ButterKnife.bind(this);
        commonDiglog=new IMIosCommonDiglog(this);
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
    public void showSingleCommonDialog( String message, View.OnClickListener listener) {
        commonDiglog.showSingleCommonDiglog(message,listener);
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }

    /**
     * 加载中
     */
    public void showLoadingDialog() {
        if(loadingDialog==null){
            loadingDialog = new IMMyLoadingDialog(this);
            loadingDialog.show();
        }else {
            loadingDialog.show();
        }
    }
    public void dismissLoadingDialog() {
        if (loadingDialog != null) {
            loadingDialog.dismiss();
        }
    }
    /**
     * 动态添加权限
     */
    private void requestPermissions() {
        if (EasyPermissions.hasPermissions(this, perms)) {
            //todo 已经获取了权限
            IMLogUtil.d("tag", "BaseActivity " + "(requestPermissions) " + true, 45, "perms = " + perms);
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

    /**
     * 显示加载界面loading(第二种自定义)
     */
    protected void showLoadingDialogTypeTwo(String msg) {
        if (dialogMessage == null) {
            dialogMessage = new IMDialogMessageTypeTwo(this);
            dialogMessage.setType(1);
        }
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

    public void startActicity(Class<?> cls){
        startActivity(new Intent(this,cls));
    }
    public void startActicity(Class<?> cls,Bundle bundle){
        startActivity(new Intent(this,cls).putExtras(bundle));
    }
}
