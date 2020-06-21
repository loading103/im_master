package com.android.im.imview.imloadingview;


import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.android.im.R;

import pl.droidsonroids.gif.GifImageView;


/**
 * LoadingDialog.java description:
 *
 */
public class IMMyLoadingDialog {
	private Dialog mDialog;
	private Activity currentActivity;
	private GifImageView tv_progress;
	private IMAVLoadingIndicatorView animLoding;

	public IMMyLoadingDialog(Context context) {
		currentActivity = (Activity) context;
		LayoutInflater inflater = LayoutInflater.from(context);
		View view = inflater.inflate(R.layout.layout_im_my_loading, null);
		tv_progress = view.findViewById(R.id.progressBar);
		tv_progress.setImageResource(R.mipmap.im_loading_02);
		mDialog = new Dialog(context, R.style.MyDialog);
		mDialog.setContentView(view);
		mDialog.setCancelable(false);
		//去掉遮罩层（全透明）
		mDialog.getWindow().setDimAmount(0f);

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
}
