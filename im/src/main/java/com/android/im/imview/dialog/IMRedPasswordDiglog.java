package com.android.im.imview.dialog;

import android.app.AlertDialog;
import android.content.Context;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.im.R;
import com.android.im.imui.activity.IMSendRedPickActivity;
import com.android.im.imview.IMSeparatedEditText;
import com.android.nettylibrary.utils.IMDensityUtil;

public class IMRedPasswordDiglog implements View.OnClickListener, TextWatcher {

    private Context context;
    private AlertDialog followDialog;
    private TextView im_tv_money;
    private RelativeLayout im_rl_cancle;
    private IMSeparatedEditText hollow;
    private EditText password;
    private TextView mTvEnsure;
    private TextView mTvEnsure_1;
    public IMRedPasswordDiglog(Context context) {
        this.context=context;
    }
    /**
     * 展示跟投界面
     */
    public void showRedPasswordDiglog(String money) {
        AlertDialog.Builder builder=new AlertDialog.Builder(context);
        View dialogView =  View.inflate(context, R.layout.layout_dialog_pay_word, null);
        im_tv_money = dialogView.findViewById(R.id.im_tv_money);
        im_rl_cancle = dialogView.findViewById(R.id.im_rl_cancle);
        hollow =   dialogView.findViewById(R.id.edit_hollow);
        password =   dialogView.findViewById(R.id.edit_password);
        mTvEnsure =   dialogView.findViewById(R.id.im_tv_ensure);
        mTvEnsure_1 =   dialogView.findViewById(R.id.im_tv_ensure_1);
        password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
            @Override
            public void afterTextChanged(Editable s) {
                if(s.toString().length()==12){
                    Toast.makeText(context,  context.getResources().getString(R.string.im_pay_password), Toast.LENGTH_SHORT).show();
                }
                if(s.toString().length()<4 || s.toString().length()>12){
                    mTvEnsure_1.setVisibility(View.VISIBLE);
                    mTvEnsure.setVisibility(View.GONE);
                }else {
                    mTvEnsure_1.setVisibility(View.GONE);
                    mTvEnsure.setVisibility(View.VISIBLE);
                }
            }
        });
        mTvEnsure.setOnClickListener(this);
        im_rl_cancle.setOnClickListener(this);
        hollow.addTextChangedListener(this);
        if(!TextUtils.isEmpty(money)){
            im_tv_money.setText(money);
        }
        builder.setView(dialogView);
        followDialog =  builder.show();
        Window window = followDialog.getWindow();
        window.setWindowAnimations(R.style.ActionSheetDialogAnimations);  //添加动画
        WindowManager.LayoutParams params = window.getAttributes();
        params.width = IMDensityUtil.getScreenWidth(context) - IMDensityUtil.dip2px(context,70);
        window.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.im_shape__bg));
    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.im_rl_cancle){
            followDialog.dismiss();
        }else  if (v.getId()==R.id.im_tv_ensure){
            if(password.getText().length()<4){
                Toast.makeText(context, context.getResources().getString(R.string.im_pay_password), Toast.LENGTH_SHORT).show();
                return;
            }
            followDialog.dismiss();
            ((IMSendRedPickActivity)context).IMSendRedPickData(password.getText().toString());
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        if(hollow.getText().length()==6){
            followDialog.dismiss();
            ((IMSendRedPickActivity)context).IMSendRedPickData(hollow.getText().toString());
        }
    }
}
