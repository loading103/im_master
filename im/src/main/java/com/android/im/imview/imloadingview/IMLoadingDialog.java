package com.android.im.imview.imloadingview;


import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.android.im.R;


/**
 * LoadingDialog.java description:
 *
 */
public class IMLoadingDialog {

	private Dialog mDialog;
	private Activity currentActivity;
	private TextView tv_progress;
	private IMAVLoadingIndicatorView animLoding;

	public IMLoadingDialog(Context context) {
		currentActivity = (Activity) context;
		LayoutInflater inflater = LayoutInflater.from(context);
		View view = inflater.inflate(R.layout.layout_im_base_loading, null);
		tv_progress = (TextView)view.findViewById(R.id.tv_progress);
		animLoding = (IMAVLoadingIndicatorView) view.findViewById(R.id.anim_loding);
		animLoding.show();
		mDialog = new Dialog(context, R.style.MyDialog);
		mDialog.setContentView(view);
		mDialog.setCanceledOnTouchOutside(false);
		mDialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
			@Override
			public boolean onKey(DialogInterface dialog, int keyCode,
								 KeyEvent event) {
				if (keyCode == KeyEvent.KEYCODE_BACK) {
					dismiss();
					return true;
				}
				return false;
			}
		});
	}
	public IMLoadingDialog(Context context, DialogInterface.OnKeyListener listener) {
		currentActivity = (Activity) context;
		LayoutInflater inflater = LayoutInflater.from(context);
		View view = inflater.inflate(R.layout.layout_im_base_loading, null);
		tv_progress = (TextView)view.findViewById(R.id.tv_progress);
		animLoding = (IMAVLoadingIndicatorView) view.findViewById(R.id.anim_loding);
		animLoding.show();
		mDialog = new Dialog(context, R.style.MyDialog);
		mDialog.setContentView(view);
		mDialog.setCanceledOnTouchOutside(false);
		mDialog.setOnKeyListener(listener);
	}

	public void show() {
		try {
			mDialog.show();
		} catch (Exception e) {
			// TODO: handle exception
		}

	}

	public void dismiss() {
		if (currentActivity != null && !currentActivity.isFinishing()) {
			mDialog.dismiss();
		}
	}
	public boolean isshow() {
		if (mDialog.isShowing()) {
			return true;
		}
		return false;
	}
	public void set_progress(String str){
		tv_progress.setText(str);
	}
}
