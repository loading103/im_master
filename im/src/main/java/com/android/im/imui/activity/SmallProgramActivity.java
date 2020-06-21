package com.android.im.imui.activity;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.im.IMSManager;
import com.android.im.R;
import com.android.im.R2;
import com.android.im.imbean.AgreeBean;
import com.android.im.imbean.IMBetGetBean;
import com.android.im.imbean.SmallProgramBean;
import com.android.im.imdialog.SmallProgramAgreeDialog;
import com.android.im.imdialog.SmallProgramQRCodeDialog;
import com.android.im.imdialog.SmallProgramShareDialog;
import com.android.im.imeventbus.UpdateLatestEvent;
import com.android.im.imeventbus.UpdateMyCollectedEvent;
import com.android.im.imeventbus.UpdateSerachEvent;
import com.android.im.iminterface.AndroidInterface;
import com.android.im.imnet.IMHttpResultObserver;
import com.android.im.imnet.http.IMHttpsService;
import com.android.im.imutils.IMStatusBarUtil;
import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ScreenUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;
import com.google.gson.Gson;
import com.just.agentweb.AgentWeb;
import com.just.agentweb.AgentWebConfig;
import com.just.agentweb.WebChromeClient;
import com.just.agentweb.WebViewClient;
import com.wang.avi.AVLoadingIndicatorView;

import org.greenrobot.eventbus.EventBus;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.bingoogolapple.qrcode.zxing.QRCodeEncoder;
import jp.wasabeef.glide.transformations.CropCircleWithBorderTransformation;
import okhttp3.MediaType;
import okhttp3.RequestBody;

/**
 * Created by Administrator on 2019/12/24.
 * Describe:小程序
 */
public class SmallProgramActivity extends IMBaseActivity implements SmallProgramShareDialog.Callback, SmallProgramAgreeDialog.Callback {
    @BindView(R2.id.iv_content)
    ImageView ivContent;
    @BindView(R2.id.tv_content)
    TextView tvContent;
    @BindView(R2.id.indicator)
    AVLoadingIndicatorView indicator;
    @BindView(R2.id.rl_right)
    RelativeLayout rlRight;
    @BindView(R2.id.rl_left)
    RelativeLayout rlLeft;
    @BindView(R2.id.ll_root)
    LinearLayout llRoot;
    @BindView(R2.id.rl_loading)
    RelativeLayout rlLoading;
    private AgentWeb mAgentWeb;
    private String programUrl;
    private String programName;
    private String twoImage;
    private SmallProgramBean data;
    private boolean isIndex = true;
    private boolean flag = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_small_program);
        ButterKnife.bind(this);
        IMStatusBarUtil.setTranslucent(this, 0);
        IMStatusBarUtil.setLightMode(this);
        initView();
        addRecentlyUse(data.getProgramId());
    }

    private void initView() {
        data = (SmallProgramBean) getIntent().getSerializableExtra("data");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            setTaskDescription(new ActivityManager.TaskDescription(data.getProgramName()));
        }
        if (data != null && !TextUtils.isEmpty(data.getProgramId())) {
            getProgramDetails(data.getProgramId());
        }
        tvContent.setText(data.getProgramName());
        Glide.with(this).load(data.getThreeImage())
                .apply(RequestOptions.bitmapTransform(new CircleCrop()))
                .into(ivContent);
        indicator.show();
        mAgentWeb = AgentWeb.with(this)
                .setAgentWebParent(llRoot, new LinearLayout.LayoutParams(-1, -1))
                .closeIndicator()
                .setWebChromeClient(mWebChromeClient)
                .setWebViewClient(mWebViewClient)
                .setSecurityType(AgentWeb.SecurityType.STRICT_CHECK)
                .createAgentWeb()
                .ready()
                .go(data.getProgramUrl());

        mAgentWeb.getJsInterfaceHolder().addJavaObject("android", new AndroidInterface(mAgentWeb, this));
    }

    public void toActivity(Context context, SmallProgramBean bean) {
        initFlags(context, bean, -1);
    }

    public void toActivity(Context context, SmallProgramBean bean, int requestCode) {
        initFlags(context, bean, requestCode);
    }

    private void initFlags(Context context, SmallProgramBean bean, int requestCode) {
        ActivityManager manager = (ActivityManager) context.getSystemService(ACTIVITY_SERVICE);
        List<ActivityManager.AppTask> taskList = null;
        boolean isNew = true;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            taskList = manager.getAppTasks();
            for (ActivityManager.AppTask task : taskList) {
                ActivityManager.RecentTaskInfo taskInfo = task.getTaskInfo();
                LogUtils.i("WOLF", "taskDescription:" + taskInfo.taskDescription.toString());
                if (taskInfo.taskDescription.toString().contains(bean.getProgramName())) {
                    task.moveToFront();
                    isNew = false;
                }
            }
            if (isNew) {
                Intent intent = new Intent(context, SmallProgramActivity.class);
                intent.putExtra("data", bean);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_DOCUMENT);
                intent.addFlags(Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
                context.startActivity(intent);
//                if (requestCode == -1) {
//                    context.startActivity(intent);
//                } else {
//                    ((Activity) context).startActivityForResult(intent, requestCode);
//                }
            }
        } else {
            Intent intent = new Intent(context, SmallProgramActivity.class);
            intent.putExtra("data", bean);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_DOCUMENT);
            intent.addFlags(Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
            context.startActivity(intent);
//            if (requestCode == -1) {
//                context.startActivity(intent);
//            } else {
//                ((Activity) context).startActivityForResult(intent, requestCode);
//            }
        }
    }

    private void getProgramDetails(String programId) {
        final IMBetGetBean bean = new IMBetGetBean();
        bean.setProgramId(programId);
        String json = new Gson().toJson(bean);
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json);
        IMHttpsService.getProgramDetails(body, new IMHttpResultObserver<SmallProgramBean>() {
            @Override
            public void onSuccess(SmallProgramBean bean, String message) {
                data = bean;
            }

            @Override
            public void _onError(Throwable e) {
            }

            @Override
            public void _onErrorLocal(Throwable e, String message, String code) {
            }
        });
    }

    private WebViewClient mWebViewClient = new WebViewClient() {
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
        }
    };
    private WebChromeClient mWebChromeClient = new WebChromeClient() {
        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            if (newProgress == 100) {
                rlLoading.setVisibility(View.GONE);
            } else {
                rlLoading.setVisibility(View.VISIBLE);
            }
        }
    };

    public void showAgreeDialog() {
        new SmallProgramAgreeDialog().show(getSupportFragmentManager(), data);
    }

    public void setScreen(int type) {
        if (type == 0) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);//此为默认值
        } else if (type == 1) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);//强制为竖屏
        } else if (type == 2) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);//强制为横屏
        }
    }

    public SmallProgramBean getData() {
        return data;
    }

    public void setData(SmallProgramBean data) {
        this.data = data;
    }

    @Override
    public void onBackPressed() {
        if (!mAgentWeb.back()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//                Intent intent = new Intent();
//                intent.setClassName(SmallProgramActivity.this, "com.android.myimprotect.ui.activity.MainActivity");
//                startActivity(intent);

//                finishAndRemoveTask();

                ActivityManager manager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
                List<ActivityManager.AppTask> taskList = null;
                taskList = manager.getAppTasks();
                for (ActivityManager.AppTask task : taskList) {
                    ActivityManager.RecentTaskInfo taskInfo = task.getTaskInfo();
                    LogUtils.i("WOLF", "taskDescription:" + taskInfo.taskDescription.toString());

                    //TODO 可能会遇到问题，到时候再改
                    if (taskInfo.taskDescription.toString().contains("Label: null")) {
                        task.moveToFront();
                    }
                }
            } else {
                finish();
            }
        }
    }

    @Override
    protected void onPause() {
        mAgentWeb.getWebLifeCycle().onPause();
        super.onPause();

    }

    @Override
    protected void onResume() {
        mAgentWeb.getWebLifeCycle().onResume();
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        mAgentWeb.getWebLifeCycle().onDestroy();
        super.onDestroy();
    }

    @OnClick({R2.id.rl_right, R2.id.rl_left})
    public void onViewClicked(View view) {
        if (view.getId() == R.id.rl_right) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                ActivityManager manager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
                List<ActivityManager.AppTask> taskList = null;
                taskList = manager.getAppTasks();
                for (ActivityManager.AppTask task : taskList) {
                    ActivityManager.RecentTaskInfo taskInfo = task.getTaskInfo();
                    LogUtils.i("WOLF", "taskDescription:" + taskInfo.taskDescription.toString());
                    //TODO 可能会遇到问题，到时候再改
                    if (taskInfo.taskDescription.toString().contains("Label: null")) {
                        task.moveToFront();
                    }
                }
            } else {
                finish();
            }
        } else if (view.getId() == R.id.rl_left) {
            if (mAgentWeb.getWebCreator().getWebView().canGoBack()) {
                isIndex = false;
            } else {
                isIndex = true;
            }
            new SmallProgramShareDialog().show(getSupportFragmentManager(), data, isIndex);
        }
    }

    @Override
    public void onClick(int position, AlertDialog alertDialog) {
        switch (position) {
            case 0:
                mAgentWeb.getUrlLoader().loadUrl(data.getProgramUrl());
                alertDialog.dismiss();
                break;
            case 1:
                if ("N".equals(data.getIsOnline())) {
                    ToastUtils.showShort("该小程序已下架");
                    return;
                }
                if (data.getIsConllection() != null && data.getIsConllection().equals("Y")) {
                    cancleCollection(data.getProgramId(), alertDialog);
                } else {
                    addCollection(data.getProgramId(), alertDialog);
                }
                break;
            case 3:
                new SmallProgramQRCodeDialog().show(getSupportFragmentManager(), data);
                break;
        }
    }

    /**
     * 添加收藏
     *
     * @param programId
     * @param alertDialog
     */
    private void addCollection(String programId, AlertDialog alertDialog) {
        showLoadingDialog();
        final IMBetGetBean bean = new IMBetGetBean();
        bean.setProgramId(programId);
        String json = new Gson().toJson(bean);
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json);
        IMHttpsService.addCollection(body, new IMHttpResultObserver<String>() {
            @Override
            public void onSuccess(String data, String message) {
                dismissLoadingDialog();
                ToastUtils.showShort("添加成功");
                getData().setIsConllection("Y");
                alertDialog.dismiss();
                EventBus.getDefault().post(new UpdateLatestEvent());
//                setResult(RESULT_OK);
                EventBus.getDefault().post(new UpdateSerachEvent());
                EventBus.getDefault().post(new UpdateMyCollectedEvent());
            }

            @Override
            public void _onError(Throwable e) {
                dismissLoadingDialog();
            }

            @Override
            public void _onErrorLocal(Throwable e, String message, String code) {
                dismissLoadingDialog();
            }
        });
    }


    /**
     * 取消收藏
     */
    private void cancleCollection(String programId, AlertDialog alertDialog) {
        showLoadingDialog();
        final IMBetGetBean bean = new IMBetGetBean();
        bean.setProgramId(programId);
        String json = new Gson().toJson(bean);
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json);
        IMHttpsService.cancleCollection(body, new IMHttpResultObserver<String>() {
            @Override
            public void onSuccess(String data, String message) {
                dismissLoadingDialog();
                Toast.makeText(SmallProgramActivity.this, "移除成功", Toast.LENGTH_SHORT).show();
                getData().setIsConllection("N");
                alertDialog.dismiss();
                EventBus.getDefault().post(new UpdateLatestEvent());
//                setResult(RESULT_OK);
                EventBus.getDefault().post(new UpdateSerachEvent());
                EventBus.getDefault().post(new UpdateMyCollectedEvent());
            }

            @Override
            public void _onError(Throwable e) {
                dismissLoadingDialog();
            }

            @Override
            public void _onErrorLocal(Throwable e, String message, String code) {
                dismissLoadingDialog();
            }
        });
    }

    /**
     * 添加最近使用
     */
    private void addRecentlyUse(String programId) {
//        showLoadingDialog();
        final IMBetGetBean bean = new IMBetGetBean();
        bean.setProgramId(programId);
        String json = new Gson().toJson(bean);
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json);
        IMHttpsService.addRecentlyUse(body, new IMHttpResultObserver<String>() {
            @Override
            public void onSuccess(String data, String message) {
                dismissLoadingDialog();
                EventBus.getDefault().post(new UpdateLatestEvent());
                EventBus.getDefault().post(new UpdateSerachEvent());
            }

            @Override
            public void _onError(Throwable e) {
                dismissLoadingDialog();
            }

            @Override
            public void _onErrorLocal(Throwable e, String message, String code) {
                dismissLoadingDialog();
            }
        });
    }

    @Override
    public void onAgreeClick(int position, AlertDialog alertDialog) {
        if (position == 0) {
            mAgentWeb.getJsAccessEntrace().quickCallJs("takeData", "-1");
            alertDialog.dismiss();
        } else if (position == 1) {
            AgreeBean bean = new AgreeBean();
            bean.setUserName(IMSManager.getMyNickName());
            bean.setUserImg(IMSManager.getMyHeadView());
            String json = new Gson().toJson(bean);
            mAgentWeb.getJsAccessEntrace().quickCallJs("takeData", json);
            alertDialog.dismiss();
        }
    }
}
