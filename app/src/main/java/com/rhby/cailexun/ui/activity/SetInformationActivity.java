package com.rhby.cailexun.ui.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.im.IMSHttpManager;
import com.android.im.IMSManager;
import com.android.im.imbean.IMBetGetBean;
import com.android.im.imbean.IMImageViewBean;
import com.android.im.iminterface.IMManegeLoginInterface;
import com.android.im.imnet.IMHttpResultObserver;
import com.android.im.imnet.http.IMHttpsService;
import com.android.im.imutils.IMBase64ImageUtils;
import com.android.im.imutils.IMChooseUtils;
import com.android.im.imutils.IMStatusBarUtil;
import com.rhby.cailexun.R;
import com.rhby.cailexun.bean.CommonBean;
import com.rhby.cailexun.net.HttpResultObserver;
import com.rhby.cailexun.net.http.HttpsService;
import com.rhby.cailexun.ui.base.BaseActivity;
import com.android.nettylibrary.IMSConfig;
import com.android.nettylibrary.utils.IMLogUtil;
import com.android.nettylibrary.utils.IMPreferenceUtil;
import com.blankj.utilcode.util.KeyboardUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.gson.Gson;
import com.zhihu.matisse.Matisse;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import cn.jpush.android.api.JPushInterface;
import jp.wasabeef.glide.transformations.CropCircleWithBorderTransformation;
import okhttp3.MediaType;
import okhttp3.RequestBody;

/**
 * Created by Wolf on 2019/12/3.
 * Describe:设置信息
 */
public class SetInformationActivity extends BaseActivity {
    @BindView(R.id.tv_top_title)
    TextView tvTopTitle;
    @BindView(R.id.im_iv_head)
    ImageView imIvHead;
    @BindView(R.id.im_iv_cameras)
    ImageView imIvCameras;
    @BindView(R.id.im_rl_contain)
    RelativeLayout imRlContain;
    @BindView(R.id.et_name)
    EditText etName;
    @BindView(R.id.btn_next)
    Button btnNext;
    private static final int REQUEST_CODE_CHOOSE = 10002;
    private String name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_information);
        IMStatusBarUtil.setTranslucent(this, 0);
        IMStatusBarUtil.setLightMode(this);
        initView();
    }

    private void initView() {
        tvTopTitle.setText("设置信息");
        Glide.with(this).load(R.mipmap.name_head_noboard)
                .apply(RequestOptions.bitmapTransform(new CropCircleWithBorderTransformation(4, Color.parseColor("#02AEFD"))))
                .into(imIvHead);
    }

    /**
     * 选择图片处理
     */
    List<String> mSelected;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_CHOOSE && resultCode == RESULT_OK) {   //选择图片成功之后上传
            mSelected = Matisse.obtainPathResult(data);
            if (mSelected == null && mSelected.size() == 0) {
                return;
            }
            Glide.with(this).load(mSelected.get(0))
                    .apply(RequestOptions.bitmapTransform(new CropCircleWithBorderTransformation(4, Color.parseColor("#02AEFD"))))
                    .into(imIvHead);
        }
    }

    /**
     * 上传图片
     */
    private void updataPickture(List<String> mSelected) {
        for (int i = 0; i < mSelected.size(); i++) {
            showLoadingDialog();
            final IMBetGetBean bean = new IMBetGetBean();
            bean.setBase64Data(IMBase64ImageUtils.imageToBase64(mSelected.get(i)));
            String json = new Gson().toJson(bean);
            RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json);
            IMHttpsService.getUpdataPicture(body, new IMHttpResultObserver<IMImageViewBean>() {
                @Override
                public void onSuccess(IMImageViewBean data, String message) {
//                    dismissLoadingDialog();
                    IMLogUtil.d("上传图片成功===" + new Gson().toJson(data));
                    updatePersonData(data.getUrl(), name);
                }

                @Override
                public void _onError(Throwable e) {
                    dismissLoadingDialog();
                    Toast.makeText(SetInformationActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }

                @Override
                public void _onErrorLocal(Throwable e, String message, String code) {
                    dismissLoadingDialog();
                    Toast.makeText(SetInformationActivity.this, message, Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    @OnClick({R.id.iv_top_finish, R.id.btn_next, R.id.im_rl_contain, R.id.ll_root})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_top_finish:
                finish();
                break;
            case R.id.btn_next:
                name = etName.getText().toString();
                if ("".equals(name)) {
                    ToastUtils.showShort("请输入昵称");
                    return;
                }
                if (name.contains(" ")) {
                    ToastUtils.showShort("昵称不能包含空格");
                    return;
                }
                if (mSelected == null) {
                    updatePersonData("", name);
                } else {
                    updataPickture(mSelected);
                }
                break;
            case R.id.im_rl_contain:
                IMChooseUtils.choosePhotoHeadForResult(this, REQUEST_CODE_CHOOSE, 1);
                break;
            case R.id.ll_root:
                KeyboardUtils.hideSoftInput(this);
                break;
        }
    }

    /**
     * 修改会员信息
     */
    private void updatePersonData(String avatar, String nickName) {
        CommonBean bean = new CommonBean();
        bean.setAvatar(avatar);
        bean.setNickName(nickName);

        String json = new Gson().toJson(bean);
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json);
        showLoadingDialog();
        HttpsService.updatePersonData(body, new HttpResultObserver<Object>() {
            @Override
            public void onSuccess(Object s, String message) {
//                dismissLoadingDialog();
                ToastUtils.showShort("注册成功");

                loginIn();
                String imtoken = IMPreferenceUtil.getPreference_String(IMSConfig.SAVE_IM_TOKEN, "");
                if (TextUtils.isEmpty(imtoken)) {   //如果本地无token,获取imtoken，本地有token,直接可以用
                    IMSHttpManager.getInstance().IMHttpGetIMToken(true);
                } else {
                    IMSHttpManager.getInstance().IMHttpGetHostIp(true);

                }
                IMSHttpManager.getInstance().IMHttpGetSelfInfor();
            }

            @Override
            public void _onError(Throwable e) {
                dismissLoadingDialog();
                ToastUtils.showShort(e.getMessage());
            }

            @Override
            public void _onErrorLocal(Throwable e, String message, String code) {
                dismissLoadingDialog();
                ToastUtils.showShort(message);
            }
        });


    }


    public void loginIn() {
//        showLoadingDialogTypeTwo("正在登录，请稍后...");
        showLoadingDialog();
        IMSManager.getInstance().setLoginListener(new IMManegeLoginInterface() {
            @Override
            public void LoginSucess(boolean type, String message) {
//                dismissLoadingDialogTypeTwo();
                dismissLoadingDialog();
                if (type) {
                    String userId = IMPreferenceUtil.getPreference_String(IMSConfig.SAVE_USERID, "");
                    //测试环境：TEST_+customerId
                    //正式环境：RELEASE_+customerId
                    JPushInterface.setAlias(SetInformationActivity.this, 1, "TEST_" + userId);
                    startActivity(new Intent(SetInformationActivity.this, MainActivity.class));
                    finish();
                } else {
                    Toast.makeText(SetInformationActivity.this, message, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
