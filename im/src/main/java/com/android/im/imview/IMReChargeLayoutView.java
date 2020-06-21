package com.android.im.imview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.im.IMSManager;
import com.android.im.R;
import com.android.im.imnet.IMBaseConstant;

public class IMReChargeLayoutView extends LinearLayout implements View.OnClickListener {

    private TextView mtvBank;
    private TextView mtvWx;
    private TextView mtvApliy;
    private TextView mtvGetmoney;

    public IMReChargeLayoutView(Context context) {
        this(context,null);
    }

    public IMReChargeLayoutView(Context context, AttributeSet attrs) {
        this(context,attrs,0);
    }

    public IMReChargeLayoutView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }
    /**
     * 初始化界面元素
     */
    private void initView() {
        LayoutInflater.from(getContext()).inflate(R.layout.layout_im_recharge,this);
        mtvBank = findViewById(R.id.tv_bank_pay);
        mtvWx = findViewById(R.id.tv_wx_pay);
        mtvApliy = findViewById(R.id.tv_apliy_pay);
        mtvGetmoney = findViewById(R.id.tv_get_money);
        mtvBank.setOnClickListener(this);
        mtvWx.setOnClickListener(this);
        mtvApliy.setOnClickListener(this);
        mtvGetmoney.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.tv_bank_pay){
            IMSManager.getInstance().addModuleClickListener(IMBaseConstant.IM_CLICK_BLANK);
        }else if(v.getId()==R.id.tv_wx_pay){
            IMSManager.getInstance().addModuleClickListener(IMBaseConstant.IM_CLICK_WEICHAT);
        }else if(v.getId()==R.id.tv_apliy_pay){
            IMSManager.getInstance().addModuleClickListener(IMBaseConstant.IM_CLICK_APLIY);
        }else if(v.getId()==R.id.tv_get_money){
            IMSManager.getInstance().addModuleClickListener(IMBaseConstant.IM_CLICK_TIXIAN);
        }
    }


}
