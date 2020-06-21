package com.android.im.imui.activity;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.im.R;
import com.android.im.imbean.IMRedSendJsonBean;
import com.android.im.imbean.IMSingleRedBackBean;
import com.android.im.imeventbus.IMMessagGroupRedEvevt;
import com.android.im.imeventbus.IMMessagSingleRedEvevt;
import com.android.im.imnet.IMHttpResultObserver;
import com.android.im.imnet.http.IMHttpsService;
import com.android.im.imutils.IMHideInPutUtil;
import com.android.im.imutils.IMStatusBarUtil;
import com.android.im.imview.dialog.IMRedPasswordDiglog;
import com.android.nettylibrary.IMSConfig;
import com.android.nettylibrary.greendao.entity.IMGroupBean;
import com.android.nettylibrary.greendao.entity.IMGroupMemberBean;
import com.android.nettylibrary.greendao.entity.IMPersonBean;
import com.android.nettylibrary.utils.IMLogUtil;
import com.android.nettylibrary.utils.IMMd5Util;
import com.android.nettylibrary.utils.IMPreferenceUtil;
import com.google.gson.Gson;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import okhttp3.MediaType;
import okhttp3.RequestBody;

/**
 * 注单明细
 */
public class IMSendRedPickActivity extends IMBaseActivity implements View.OnClickListener, TextWatcher {
    private ImageView mIvBack;
    private TextView mTvBack;
    private TextView mTvTitle;
    private RelativeLayout mRlBack;


    private EditText mEtMoney;
    private EditText mEtNumber;
    private EditText mEtContent;
    private TextView mTvPerson;
    private TextView mTvTotle;
    private TextView mTvSend;
    private TextView mTvJe;
    private TextView mTvSq;
    private RelativeLayout mRlNumber;

    private ImageView mIvPin;
    private IMGroupBean group;
    private IMPersonBean customer;
    private Double mymoney;
    private IMRedPasswordDiglog diglog;
    private String needPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_im_send_redpicked);
        IMStatusBarUtil.setColor(this, getResources().getColor(R.color.color_05A9FE), 0);
        initView();
        initData();
    }

    private void initView() {
        //状态栏
        mIvBack = findViewById(R.id.iv_top_finish);
        mTvBack = findViewById(R.id.tv_top_finish);
        mTvTitle = findViewById(R.id.tv_top_title);
        mRlBack = findViewById(R.id.rl_top_finish);
        mTvBack.setVisibility(View.GONE);
        mTvBack.setText(getResources().getString(R.string.im_back));
        mTvTitle.setText(getResources().getString(R.string.im_send_red));
        mRlBack.setOnClickListener(this);

        mEtMoney = findViewById(R.id.im_et_money);
        mEtNumber = findViewById(R.id.im_et_number);
        mEtContent = findViewById(R.id.im_et_content);
        mTvPerson = findViewById(R.id.im_tv_person);
        mTvTotle = findViewById(R.id.im_tv_tltle);
        mTvSend = findViewById(R.id.im_tv_send);
        mTvJe = findViewById(R.id.im_tv_je);
        mIvPin = findViewById(R.id.im_iv_pin);
        mTvSq = findViewById(R.id.im_tv_sq);
        mRlNumber = findViewById(R.id.im_rl_number);
        mTvSend.setOnClickListener(this);
        mEtMoney.addTextChangedListener(this);
        mEtNumber.addTextChangedListener(this);
        mEtContent.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(s.length()==10){
                    Toast.makeText(IMSendRedPickActivity.this, "内容不能超过10个字", Toast.LENGTH_SHORT).show();
                }
            }
        });
        mEtMoney.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(!TextUtils.isEmpty(s)){
                    double v = Double.parseDouble(s.toString());
                    if(group!=null){
                        if(v>20000){
                            mEtMoney.setText(20000.00+"");
                            mEtMoney.setSelection(mEtMoney.getText().toString().length());
                            Toast.makeText(IMSendRedPickActivity.this, "红包总金额不能超过20000元", Toast.LENGTH_SHORT).show();
                        }
                    }else {
                        if(v>200){
                            mEtMoney.setText(200.00+"");
                            mEtMoney.setSelection(mEtMoney.getText().toString().length());
                            Toast.makeText(IMSendRedPickActivity.this, "单个红包金额不能超过200元", Toast.LENGTH_SHORT).show();
                        }
                    }

                }
            }
        });
        mEtNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(!TextUtils.isEmpty(s)){
                    double v = Double.parseDouble(s.toString());
                    if(v>100){
                        mEtNumber.setText(100+"");
                        mEtNumber.setSelection(mEtNumber.getText().toString().length());
                        Toast.makeText(IMSendRedPickActivity.this, "红包个数不能超过100", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
            }
        });
    }

    private void initData() {
        diglog = new IMRedPasswordDiglog(this);
        group=(IMGroupBean)getIntent().getSerializableExtra("group");
        customer=(IMPersonBean)getIntent().getSerializableExtra("customer");
        mymoney = Double.parseDouble(IMPreferenceUtil.getPreference_String(IMSConfig.SAVE_MONEY, "0"));
        if(group!=null){
            List<IMGroupMemberBean> imGroupMemberBeans = daoUtils.queryGroupAllMemberBean(group.getGroupId());
            if(imGroupMemberBeans==null || imGroupMemberBeans.size()==0){
                return;
            }
            mTvPerson.setText("本群有"+imGroupMemberBeans.size()+"人");
            mIvPin.setVisibility(View.VISIBLE);
            mTvSq.setVisibility(View.VISIBLE);
            mRlNumber.setVisibility(View.VISIBLE);
            mTvJe.setText("总金额");
        }else {
            mIvPin.setVisibility(View.GONE);
            mTvSq.setVisibility(View.GONE);
            mRlNumber.setVisibility(View.GONE);
            mTvJe.setText("单个金额");
        }

    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.rl_top_finish) {
            finish();
        } else if (v.getId() == R.id.im_tv_send) {
            if(VerifyMessage()){
                return;
            }
            if(group!=null){
                double totleMonth = Double.parseDouble(mEtMoney.getText().toString());
                int    number=Integer.parseInt(mEtNumber.getText().toString());
                if(totleMonth/number>200){
                    Toast.makeText(this, "单个红包数不能超过200元", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
//            double v1 = Double.parseDouble(mTvTotle.getText().toString().trim());
//            if(mymoney<v1){
//                Toast.makeText(this, "余额不足，请重新输入金额", Toast.LENGTH_SHORT).show();
//                return;
//            }
            needPassword = IMPreferenceUtil.getPreference_String(IMSConfig.SAVE_PAYWORD, "N");
            if(needPassword.equals("Y")){
                diglog.showRedPasswordDiglog(mTvTotle.getText().toString().trim());
            }else {
                IMSendRedPickData(null);
            }
        }
    }

    /**
     * http发送红包
     * {"redPacket":{"desc":"11086彩金红包","metas":null,"redPacketId":"5950b0952f6f4b9386bb364c1776f80d"
     * ,"style":"STYLE_DEFAULT","title":"恭喜发财，大吉大利！","type":"4000"},"isAudit":false}
     */
    public void IMSendRedPickData(String password) {
        String passwords = IMMd5Util.md5(password);
        IMRedSendJsonBean bean=new IMRedSendJsonBean();
        if (group != null) { //群发
            setGroupData(bean,passwords);
        }else {   //单发
            setSingleData(bean,passwords);
        }
        String json = new Gson().toJson(bean);
        Log.e("---红包参数====",json);
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"),json);
        IMLogUtil.d("MyOwnTag:", "发送红包 " +"(参数)=== " +json);
        showLoadingDialog();
        IMHttpsService.sendRedPickJson(body, new IMHttpResultObserver<IMSingleRedBackBean>() {
            @Override
            public void onSuccess(IMSingleRedBackBean imRedBackBean, String message) {
                dismissLoadingDialog();
                if(group==null){
                    EventBus.getDefault().post(new IMMessagSingleRedEvevt(imRedBackBean, customer));
                }else {
                    EventBus.getDefault().post(new IMMessagGroupRedEvevt(imRedBackBean, group));
                }
                finish();
            }

            @Override
            public void _onError(Throwable e) {
                dismissLoadingDialog();
                Toast.makeText(IMSendRedPickActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void _onErrorLocal(Throwable e, String message, String code) {
                dismissLoadingDialog();
                Toast.makeText(IMSendRedPickActivity.this, message, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private boolean VerifyMessage() {
        if(TextUtils.isEmpty(mEtMoney.getText())){
            Toast.makeText(this, "请输入金额", Toast.LENGTH_SHORT).show();
            return true;
        }else if( mEtNumber!=null && (TextUtils.isEmpty(mEtNumber.getText()) ||mEtNumber.getText().equals("0")) ){
            if(group!=null){
                Toast.makeText(this, "请输入红包个数", Toast.LENGTH_SHORT).show();
                return true;
            }
            return false;
        }
        return  false;
    }


    /**
     * 监听界面输入(保持2位小数)
     */
    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        needSaveTwoPoints(s);
    }

    @Override
    public void afterTextChanged(Editable s) {
        try {
            if(!TextUtils.isEmpty(mEtMoney.getText())){
                double v = Double.parseDouble(mEtMoney.getText().toString().trim());
                mTvTotle.setText(String.format("%.2f", v));
            }else {
                mTvTotle.setText("0.00");
            }
        }catch (Exception e){
            Toast.makeText(this, "请检查输入的金额是否有效", Toast.LENGTH_SHORT).show();
        }

    }

    /**
     * 保存2位小数
     */
    private void needSaveTwoPoints(CharSequence s) {
        if (s.toString().contains(".")){
            if (s.length()-1-s.toString().indexOf(".")>2){
                s=s.toString().subSequence(0,s.toString().indexOf(".")+3);
                mEtMoney.setText(s);
                mEtMoney.setSelection(s.length());
            }
        }

        if(s.toString().trim().substring(0).equals(".")){
            s ="0"+s;
            mEtMoney.setText(s);
            mEtMoney.setSelection(2);
        }

        if(s.toString().startsWith("0")
                && s.toString().trim().length()>1){
            if(!s.toString().substring(1,2).equals(".")){
                mEtMoney.setText(s.subSequence(0,1));
                mEtMoney.setSelection(1);
                return;
            }
        }
    }
    /**
     * 构建单个红包消息体
     *
     "acceptUserId": "10245d57e35592b133655f7d7ee2",//红包接收者ID
     "acceptUserType": "1",//红包接收者用户类型
     "amount": "10",//红包金额
     "isGroup": "N",//是否为群红包
     "number": 1,//个数
     "reasons": "测试用户给客服发送-奖励红包",//原因
     "remark": "11086彩金红包",//备注
     "title": "测试用户给客服发送-恭喜发财",//标题
     "type": "1",//红包类型
     "drawPassword":"e10adc3949ba59abbe56e057f20f883e"//可选字段，根据isPayPwd=Y时才需要
     */
    private void setSingleData(IMRedSendJsonBean bean,String pasword) {
        if(!TextUtils.isEmpty(pasword)){
            bean.setDrawPassword(pasword);
        }
        String title=null;
        if(TextUtils.isEmpty(mEtContent.getText().toString())){
            title="恭喜发财，大吉大利！";
        }else {
            title=mEtContent.getText().toString();
        }
        bean.setAcceptUserId(customer.getMemberId());  // 红包接收者用户ID：如果给客服发红包，则为客服ID;如果给用户发红包，则为用户ID
        bean.setAcceptUserType("1");   //红包接受者用户类型：1=客服，2=用户
        bean.setAmount(mTvTotle.getText().toString());
        bean.setIsGroup("N");
        bean.setNumber(1);    //单人只能发一个
        bean.setReasons(title);
        bean.setRemark("11086彩金红包");
        bean.setTitle(title);
        bean.setType("1");          //红包类型：1=固定金额红包，2=随机红包
    }
    /**
     * 构建群红包消息体
     *  "amount": "10",
     *     "groupId": "30725d6cde3892b1336bea778014",
     *     "isGroup": "Y",
     *     "number": 5,
     *     "reasons": "用户发送群红包-奖励红包",
     *     "remark": "11086彩金红包",
     *     "title": "用户发送群红包-测试群红包",
     *     "type": "1",//红包类型
     *     "drawPassword":"e10adc3949ba59abbe56e057f20f883e"//可选字段，根据isPayPwd=Y时才需要
     */
    private void setGroupData(IMRedSendJsonBean bean,String pasword) {
        if(!TextUtils.isEmpty(pasword)){
            bean.setDrawPassword(pasword);
        }
        String title=null;
        if(TextUtils.isEmpty(mEtContent.getText().toString())){
            title="恭喜发财，大吉大利！";
        }else {
            title=mEtContent.getText().toString();
        }
        bean.setAmount(mTvTotle.getText().toString());
        bean.setGroupId(group.getGroupId());
        bean.setIsGroup("Y");
        bean.setNumber((int)Double.parseDouble(mEtNumber.getText().toString()));
        bean.setReasons(title);
        bean.setRemark("11086彩金红包");
        bean.setTitle(title);
        bean.setType("2"); //红包类型：1=固定金额红包，2=随机红包
    }

    /**
     * 点击软键盘外面的区域关闭软键盘
     * @return
     */
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (IMHideInPutUtil.isShouldHideInput(v, ev)) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm != null) {
                    assert v != null;
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                    v.clearFocus();
                }
            }
            return super.dispatchTouchEvent(ev);
        }
        return getWindow().superDispatchTouchEvent(ev) || onTouchEvent(ev);
    }
}

