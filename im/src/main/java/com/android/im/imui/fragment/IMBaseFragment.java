package com.android.im.imui.fragment;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.android.im.R;
import com.android.im.imview.dialog.IMDialogMessageTypeTwo;
import com.android.im.imview.imloadingview.IMLoadingDialog;
import com.android.im.imview.imloadingview.IMMyLoadingDialog;
import com.android.nettylibrary.greendao.greenutils.DaoUtils;


/**
 * Created by lenovo-s on 2016/10/20.
 */

public abstract class IMBaseFragment extends Fragment {

    public DaoUtils daoUtils;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=initView();
        daoUtils=DaoUtils.getInstance();
        iniData();
        return view;
    }

    public abstract void iniData();

    public abstract View initView();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    /**
     * 对话框
     */
    public void showDialog(String title, String message, DialogInterface.OnClickListener listener ){

        final AlertDialog.Builder normalDialog =
                new AlertDialog.Builder(getActivity());
        normalDialog.setTitle(title);
        normalDialog.setMessage(message);
        normalDialog.setPositiveButton("确定", listener);
        normalDialog.setNegativeButton("取消",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        // 显示
        normalDialog.show();
    }
    /**
     * 对话框
     */
    public void showEnsureDialog(String title, String message, DialogInterface.OnClickListener listener ){

        final AlertDialog.Builder normalDialog =
                new AlertDialog.Builder(getActivity());
        normalDialog.setTitle(title);
        normalDialog.setMessage(message);
        normalDialog.setPositiveButton("确定", listener);
        // 显示
        normalDialog.show();
    }
    /**
     * 显示等候对话框
     */
    private IMMyLoadingDialog waittingDialog;
    public void showLoadingDialog() {
        waittingDialog= new IMMyLoadingDialog(getActivity());
        waittingDialog.show();
    }

    public void dismissLoadingDialog() {
        if (waittingDialog != null) {
            waittingDialog.dismiss();
        }
    }

    /**
     * 显示加载界面loading(第二种自定义)
     */
    protected IMDialogMessageTypeTwo dialogMessage;
    protected void showLoadingDialogTypeTwo() {
        if (dialogMessage == null) {
            dialogMessage = new IMDialogMessageTypeTwo(getActivity());
            dialogMessage.setType(1);
        }
        dialogMessage.setMessage("正在加载，请稍后...");
        dialogMessage.showDialog();
    }

    protected void dismissLoadingDialogTypeTwo() {
        if (dialogMessage != null) {
            dialogMessage.dissmissDialog();
            dialogMessage = null;
        }
    }
}
