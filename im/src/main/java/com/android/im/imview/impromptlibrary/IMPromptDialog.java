package com.android.im.imview.impromptlibrary;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.util.Log;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.ScaleAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;

import com.android.im.R;


/**
 * Created by limxing on 2017/5/7.
 * https://www.github.com/limxing
 */

public class IMPromptDialog {
    private final String TAG = "IMPromptDialog";

    private InputMethodManager inputmanger;
    private Animation outAnim;
    private Animation inAnim;
    private IMPromptView IMPromptView;
    private ViewGroup decorView;
    private ValueAnimator dissmissAnim;
    private boolean dissmissAnimCancle;
    private boolean outAnimRunning;
    public static long viewAnimDuration = 300;
    private boolean isShowing;
    private AnimationSet inSheetAnim;
    private AlphaAnimation outSheetAnim;
    private AnimationSet inDefaultAnim;
    private AnimationSet outDefaultAnim;
    private IMOnAdClickListener adListener;
    private Runnable runnable;
    private Handler handler;

    /**
     * 设置进入 进出动画持续的事件默认300毫秒
     *
     * @param viewAnimDuration 毫秒
     */
    public void setViewAnimDuration(long viewAnimDuration) {
        this.viewAnimDuration = viewAnimDuration;
    }

    public long getViewAnimDuration() {
        return viewAnimDuration;
    }

    public void onDetach() {
        isShowing = false;
    }


    public IMPromptDialog(Activity context) {
        this(IMBuilder.getDefaultIMBuilder(), context);
    }

    public IMPromptDialog(IMBuilder IMBuilder, Activity context) {
        decorView = (ViewGroup) context.getWindow().getDecorView().findViewById(android.R.id.content);

        IMPromptView = new IMPromptView(context, IMBuilder, this);
        initAnim(context.getResources().getDisplayMetrics().widthPixels,
                context.getResources().getDisplayMetrics().heightPixels);
        inputmanger = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        handler = new Handler();
    }


    private void initAnim(int widthPixels, int heightPixels) {

        inDefaultAnim = new AnimationSet(true);
        ScaleAnimation scaleAnimation = new ScaleAnimation(2, 1f, 2,
                1f, widthPixels * 0.5f, heightPixels * 0.45f);
        inDefaultAnim.addAnimation(scaleAnimation);

        AlphaAnimation alphaAnimation = new AlphaAnimation(0, 1);
        inDefaultAnim.addAnimation(alphaAnimation);
        inDefaultAnim.setDuration(viewAnimDuration);
        inDefaultAnim.setFillAfter(false);
        inDefaultAnim.setInterpolator(new DecelerateInterpolator());

        outDefaultAnim = new AnimationSet(true);
        scaleAnimation = new ScaleAnimation(1, 2, 1,
                2, widthPixels * 0.5f, heightPixels * 0.45f);
        alphaAnimation = new AlphaAnimation(1, 0);
        alphaAnimation.setDuration(200);
        outDefaultAnim.addAnimation(scaleAnimation);
        outDefaultAnim.addAnimation(alphaAnimation);
        outDefaultAnim.setDuration(viewAnimDuration);
        outDefaultAnim.setFillAfter(false);
        outDefaultAnim.setInterpolator(new AccelerateInterpolator());

        alphaAnimation = new AlphaAnimation(0, 1);
        scaleAnimation = new ScaleAnimation(1, 1, 1,
                1, widthPixels * 0.5f, heightPixels * 0.5f);
        inSheetAnim = new AnimationSet(true);
        inSheetAnim.addAnimation(alphaAnimation);
        inSheetAnim.addAnimation(scaleAnimation);
        inSheetAnim.setDuration(viewAnimDuration);
        inSheetAnim.setFillAfter(false);


        outSheetAnim = new AlphaAnimation(1, 0);
        outSheetAnim.setDuration(viewAnimDuration);
        outSheetAnim.setFillAfter(false);


    }

    public void setOutAnim(Animation outAnim) {
        this.outAnim = outAnim;
    }

    public void setInAnim(Animation inAnim) {
        this.inAnim = inAnim;
    }

    /**
     * 立刻关闭窗口
     */
    public void dismissImmediately() {
        if (isShowing && !outAnimRunning) {
            decorView.removeView(IMPromptView);
            isShowing = false;
        }
    }

    /**
     * close
     */
    public void dismiss() {

        if (isShowing && !outAnimRunning) {
            if (IMPromptView.getIMBuilder().withAnim && outAnim != null) {
//                outAnim.setStartOffset(delayTime);
                if (IMPromptView.getCurrentType() == IMPromptView.PROMPT_LOADING) {
                    outAnim.setStartOffset(IMPromptView.getIMBuilder().loadingDuration);
                } else {
                    outAnim.setStartOffset(0);
                }
                if (IMPromptView.getCurrentType() == IMPromptView.CUSTOMER_LOADING) {
                    IMPromptView.stopCustomerLoading();
                }

                IMPromptView.dismiss();
                IMPromptView.startAnimation(outAnim);
                outAnim.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {
                        outAnimRunning = true;
                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        decorView.removeView(IMPromptView);
                        outAnimRunning = false;
                        isShowing = false;
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
            } else {
                dismissImmediately();
            }

        }

    }

    public void showError(String msg) {
        showError(msg, true);

    }

    public void showError(String msg, boolean withAnim) {
        showSomthing(R.mipmap.im_ic_prompt_error, IMPromptView.PROMPT_ERROR, msg, withAnim);

    }

    public void showInfo(String msg) {
        showInfo(msg, true);
    }

    public void showInfo(String msg, boolean withAnim) {
        showSomthing(R.mipmap.im_ic_prompt_info, IMPromptView.PROMPT_INFO, msg, withAnim);
    }

    public void showWarn(String msg) {
        showWarn(msg, true);
    }

    public void showWarn(String msg, boolean withAnim) {
        showSomthing(R.mipmap.im_ic_prompt_warn, IMPromptView.PROMPT_WARN, msg, withAnim);
    }

    public void showSuccess(String msg) {
        showSuccess(msg, true);
    }

    public void showSuccess(String msg, boolean withAnim) {
        showSomthing(R.mipmap.im_ic_prompt_success, IMPromptView.PROMPT_SUCCESS, msg, withAnim);
    }

    public void showSuccessDelay(final String msg, long delay) {
        decorView.postDelayed(new Runnable() {
            @Override
            public void run() {
                showSuccess(msg);
            }
        }, delay);

    }

    /**
     * show custome dialog
     *
     * @param icon
     * @param msg
     */
    public void showCustom(int icon, String msg) {
        showCustom(icon, msg, true);
    }

    public void showCustom(int icon, String msg, boolean withAnim) {
        showSomthing(icon, IMPromptView.PROMPT_CUSTOM, msg, withAnim);
    }

    private void showSomthing(int icon, int promptError, String msg, boolean withAnim) {
        inAnim = inDefaultAnim;
        outAnim = outDefaultAnim;
        IMBuilder IMBuilder = com.android.im.imview.impromptlibrary.IMBuilder.getDefaultIMBuilder();
        IMBuilder.text(msg);
        IMBuilder.icon(icon);
        closeInput();
        checkLoadView(withAnim);
        if (isShowing) {
            IMPromptView.setIMBuilder(IMBuilder);
            IMPromptView.showSomthing(promptError);
            dissmissAni(false);
        }
    }

    public void showWarnAlert(String text, IMPromptButton button) {
        showWarnAlert(text, button, false);
    }

    public void showWarnAlert(String text, IMPromptButton button, boolean withAnim) {
        showAlert(text, withAnim, button);
    }

    /**
     * 展示警告对话框
     *
     * @param text
     * @param button1
     * @param button2
     * @param withAnim 是否动画进入
     */
    public void showWarnAlert(String text, IMPromptButton button1, IMPromptButton button2, boolean withAnim) {
        showAlert(text, withAnim, button1, button2);
    }

    public void showWarnAlert(String text, IMPromptButton button1, IMPromptButton button2) {
        showWarnAlert(text, button1, button2, true);
    }

    public void showAlertSheet(String title, boolean withAnim, IMPromptButton... button) {
        showAlert(title, withAnim, button);
    }

    private void showAlert(String text, boolean withAnim, IMPromptButton... button) {
        if (button.length > 2) {
            Log.i(TAG, "showAlert: " + IMPromptView.getScrollY());

            inAnim = inSheetAnim;
            outAnim = outSheetAnim;
        } else {
            inAnim = inDefaultAnim;
            outAnim = outDefaultAnim;
        }
        IMBuilder IMBuilder = com.android.im.imview.impromptlibrary.IMBuilder.getAlertDefaultIMBuilder();
        IMBuilder.text(text);
        IMBuilder.icon(R.mipmap.im_ic_prompt_alert_warn);
        closeInput();
        IMPromptView.setIMBuilder(IMBuilder);
        checkLoadView(withAnim);
        IMPromptView.showSomthingAlert(button);
        dissmissAni(true);


    }

    public ImageView showAd(boolean withAnim, IMOnAdClickListener listener) {
        this.adListener = listener;
        inAnim = inSheetAnim;
        outAnim = outSheetAnim;
        IMBuilder IMBuilder = com.android.im.imview.impromptlibrary.IMBuilder.getDefaultIMBuilder();
        IMBuilder.touchAble(false);
//        IMBuilder.text(msg);
//        IMBuilder.icon(icon);
//        IMBuilder.
        closeInput();
        checkLoadView(withAnim);
        IMPromptView.setIMBuilder(IMBuilder);
        IMPromptView.showAd();
        dissmissAni(true);
        return IMPromptView;
    }

    /**
     * 展示loading
     *
     * @param msg      信息
     * @param withAnim 是否动画进入
     */
    public void showLoading(String msg, boolean withAnim) {
        inAnim = inDefaultAnim;
        outAnim = outDefaultAnim;
        if (IMPromptView.getCurrentType() != IMPromptView.PROMPT_LOADING) {
            IMBuilder IMBuilder = com.android.im.imview.impromptlibrary.IMBuilder.getDefaultIMBuilder();
            IMBuilder.icon(R.mipmap.im_ic_prompt_loading);
            IMBuilder.text(msg);
            IMPromptView.setIMBuilder(IMBuilder);
            closeInput();
            checkLoadView(withAnim);
            IMPromptView.showLoading();
            dissmissAni(true);
        } else {
            IMPromptView.setText(msg);
        }
    }

    /**
     * 展示加载中
     *
     * @param msg
     */
    public void showLoading(String msg) {
        showLoading(msg, true);
    }

    /** 延迟展示loading
     * @param msg
     * @param time
     */
    public void showLoadingWithDelay(final String msg, long time) {
        if (runnable != null) handler.removeCallbacks(runnable);
        runnable = new Runnable() {
            @Override
            public void run() {
                showLoading(msg);
            }
        };
        handler.postDelayed(runnable, time);

    }

    /**
     * promptview isshowing
     *
     * @param withAnim
     */
    private void checkLoadView(boolean withAnim) {
        if (!isShowing) {
            decorView.addView(IMPromptView);
            isShowing = true;
            if (IMPromptView.getIMBuilder().withAnim && inAnim != null && withAnim)
                IMPromptView.startAnimation(inAnim);
        }
    }

    /**
     * dismiss dialog and start animation
     */
    private void dissmissAni(boolean isCancle) {
        if (dissmissAnim == null) {
            dissmissAnim = ValueAnimator.ofInt(0, 1);
            dissmissAnim.setDuration(IMPromptView.getIMBuilder().stayDuration);
            dissmissAnim.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {

                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    if (!dissmissAnimCancle) {
                        dismiss();
                    }
                }

                @Override
                public void onAnimationCancel(Animator animation) {

                }

                @Override
                public void onAnimationRepeat(Animator animation) {

                }
            });
        } else if (dissmissAnim.isRunning()) {
            dissmissAnimCancle = true;
            dissmissAnim.end();

        }
        if (!isCancle) {
            dissmissAnim.start();
            dissmissAnimCancle = false;
        }
    }

    protected void closeInput() {
        if (decorView != null) {
            inputmanger.hideSoftInputFromWindow(decorView.getWindowToken(), 0);

        }
//        window.closeAllPanels();
    }

    /**
     * 获取提示框的配置类
     *
     * @return
     */
    public IMBuilder getDefaultBuilder() {
        return IMBuilder.getDefaultIMBuilder();
    }

    /**
     * 获取对话框的配置类
     *
     * @return
     */
    public IMBuilder getAlertDefaultBuilder() {
        return IMBuilder.getAlertDefaultIMBuilder();
    }

    /**
     * 处理返回键，需要用户自行调用
     *
     * @return
     */
    public boolean onBackPressed() {
        if (isShowing && IMPromptView.getCurrentType() == IMPromptView.PROMPT_LOADING) {
            return false;
        }
        if (isShowing && (IMPromptView.getCurrentType() == IMPromptView.PROMPT_ALERT_WARN ||
                IMPromptView.getCurrentType() == IMPromptView.PROMPT_AD)) {
            dismiss();
            return false;
        } else {
            return true;
        }
    }


    public void onAdClick() {
        if (adListener != null) {
            adListener.onAdClick();
        }
    }

    /**
     * 加载自定义的loading
     *
     * @param logo_loading 图片数组
     * @param msg          展现消息
     */
    public void showCustomLoading(int logo_loading, String msg) {

        inAnim = inDefaultAnim;
        outAnim = outDefaultAnim;
        if (IMPromptView.getCurrentType() != IMPromptView.CUSTOMER_LOADING) {
            IMBuilder IMBuilder = com.android.im.imview.impromptlibrary.IMBuilder.getDefaultIMBuilder();
            IMBuilder.icon(logo_loading);
            IMBuilder.text(msg);
            IMPromptView.setIMBuilder(IMBuilder);
            closeInput();
            checkLoadView(true);
            IMPromptView.showCustomLoading();
            dissmissAni(true);
        } else {
            IMPromptView.setText(msg);
        }

    }

    /**
     * 延迟加载自定义loading
     *
     * @param logo_loading
     * @param msg
     * @param time
     */
    public void showCustomerLoadingWithDelay(final int logo_loading, final String msg, long time) {
        runnable = new Runnable() {
            @Override
            public void run() {
                showCustomLoading(logo_loading, msg);
            }
        };
        handler.postDelayed(runnable, time);
    }
}
