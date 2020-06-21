package com.rhby.cailexun.ui.fragment;

import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.android.im.IMSManager;
import com.rhby.cailexun.R;
import com.rhby.cailexun.ui.base.BaseFragment;
import com.lzf.easyfloat.EasyFloat;
import com.lzf.easyfloat.interfaces.OnFloatCallbacks;

import org.greenrobot.greendao.annotation.NotNull;

import butterknife.BindView;


public class FindFragment extends BaseFragment {
	private static final String mURL="http://65.52.160.124:9020/discover/?id="+ IMSManager.getMyUseId();
	@BindView(R.id.im_web)
	 WebView mwebView;
	@BindView(R.id.progress_bar)
	 ProgressBar mProgress;

	boolean isMove = false;
	private final int DOUBLE_TAP_TIMEOUT = 200;
	private MotionEvent mCurrentDownEvent;
	private boolean mDoubleTapEnabled = false;
	private MotionEvent mPreviousUpEvent;
	private boolean isLoadingMain=false;//是否正在加载首页

	@Override
	protected int getLayoutId() {
		return R.layout.fragment_find;
	}

	@Override
	protected void initView(View view) {
		initWebView();
		setFloatVisibility(true);
		setFloatVisibility(false);
	}

	private void initWebView() {
		WebSettings settings = mwebView.getSettings();
		settings.setDomStorageEnabled(true);
		settings.setUseWideViewPort(true);
		settings.setLoadWithOverviewMode(true);
		settings.setDomStorageEnabled(true);


		mwebView.setWebChromeClient(new WebChromeClient());
		mwebView.setWebViewClient(new WebViewClient());
		mwebView.getSettings().setJavaScriptEnabled(true);
		//支持JS
		mwebView.setWebChromeClient(new WebChromeClient() {
			@Override
			public void onProgressChanged(WebView view, int newProgress) {
				if(mProgress==null){
					return;
				}
				if(mwebView.canGoBack()){
					EasyFloat.show();
				}else {
					EasyFloat.hide();
				}
				if (newProgress == 100) {
					mProgress.setVisibility(View.GONE);
					if(isLoadingMain){
						mwebView.clearHistory();
						isLoadingMain=false;
					}
				} else {
					if (View.INVISIBLE == mProgress.getVisibility()) {
						mProgress.setVisibility(View.VISIBLE);
					}
					mProgress.setProgress(newProgress);
				}
				super.onProgressChanged(view, newProgress);
			}
			@Override
			public void onReceivedTitle(WebView view, String title) {
				super.onReceivedTitle(view, title);
			}
		});

		mwebView.setWebViewClient(new WebViewClient(){
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				Log.e("-------", url + "===");
				return false;
			}

		});
		mwebView.loadUrl(mURL);
	}

	public void setFloatVisibility(boolean isVisibility){
		if(isVisibility){
			if(EasyFloat.getFloatView(getActivity())==null){
				EasyFloat.with(getActivity()).setLayout(R.layout.view_float)
						.setGravity(Gravity.END|Gravity.BOTTOM, 0, -400)
						.registerCallbacks(new OnFloatCallbacks() {
							private boolean isDraged=false;//是否拖动过
							private int clickCount=0;//点击次数
							Handler handler = new Handler();
							@Override
							public void createdResult(boolean isCreated, @Nullable String msg, @Nullable View view) {
							}
							@Override
							public void show(@NotNull View view) { }
							@Override
							public void hide(@NotNull View view) { }

							@Override
							public void dismiss() { }

							@Override
							public void touchEvent(@NotNull View view, @NotNull MotionEvent event) {
								if (event.getAction() == MotionEvent.ACTION_UP&&!isDraged) {
									clickCount++;
									handler.postDelayed(new Runnable() {
										@Override
										public void run() {
											if (clickCount == 1) {
												if(mwebView!=null&&mwebView.canGoBack()){
													mwebView.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
													mwebView.goBack();
												}
											}else if(clickCount==2){
												if(mwebView!=null){
													isLoadingMain=true;
													mwebView.loadUrl(mURL);
												}
											}
											handler.removeCallbacksAndMessages(null);
											//清空handler延时，并防内存泄漏
											clickCount = 0;//计数清零
										}
									},400);//延时timeout后执行run方法中的代码

							}
							}

							@Override
							public void drag(@NotNull View view, @NotNull MotionEvent event) {
								isDraged=true;
							}

							@Override
							public void dragEnd(@NotNull View view) {
								isDraged=false;
							}
						})
						.show();
			}else {
				if(mwebView!=null&&mwebView.canGoBack()){
					EasyFloat.show();
				}
			}

		}else {
			EasyFloat.hide();
		}

	}

	/**
	 * 重写返回键
	 */
	private long firstTime = 0;
	public boolean clickBack(int keycode, KeyEvent event) {
		if (keycode == KeyEvent.KEYCODE_BACK && mwebView.canGoBack()) {
			mwebView.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
			mwebView.goBack();
		}else {
			twoCilckFinish(keycode);
		}
		return true;
	}
	private void twoCilckFinish(int keyCode) {
		long secondTime = System.currentTimeMillis();
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if ( secondTime - firstTime < 2000) {
				getActivity().finish();
			} else {
				Toast.makeText(getActivity(), "再按一次退出程序", Toast.LENGTH_SHORT).show();
				firstTime = System.currentTimeMillis();
			}
		}
	}

	private boolean isConsideredDoubleTap(MotionEvent firstDown, MotionEvent firstUp, MotionEvent secondDown){
		if (secondDown.getEventTime() - firstUp.getEventTime() > DOUBLE_TAP_TIMEOUT) {
			return false;
		}
		int deltaX =(int) firstUp.getX() - (int)secondDown.getX();
		int deltaY =(int) firstUp.getY()- (int)secondDown.getY();
		return deltaX * deltaX + deltaY * deltaY < 10000;
	}
}
