package com.android.im.imview.dialog;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.im.R;
import com.android.im.imadapter.IMGroupListAdapter;
import com.android.im.imadapter.baseadapter.IMBaseRecycleViewAdapter_T;
import com.android.im.imbean.IMBetDetailBean;
import com.android.im.imeventbus.IMMessagGroupShareEvevt;
import com.android.im.imeventbus.IMMessagShareEnsureEvevt;
import com.android.im.imui.activity.IMBetDetailActivity;
import com.android.im.imutils.IMSmoothScrollLayoutManager;
import com.android.nettylibrary.greendao.entity.IMGroupBean;
import com.android.nettylibrary.greendao.greenutils.DaoUtils;
import com.android.nettylibrary.utils.IMDensityUtil;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

public class IMBetShareDiglog implements View.OnClickListener, IMBaseRecycleViewAdapter_T.OnItemClickListner {
    private Context context;
    private AlertDialog shareDialog;
    private RelativeLayout mRlCancle;
    private ImageView mIvShare;
    private RecyclerView mRecycleView;
    private List<IMGroupBean> datas;
    private IMGroupListAdapter adapter;
    private IMBetDetailBean beans;
    private IMBetDetailBean bean;
    public IMBetShareDiglog(Context context) {
        this.context=context;
    }
    /**
     * 展示分享界面
     */
    public void showShareDiglog(IMBetDetailBean bean,IMBetDetailBean beans) {
        this.bean=bean;
        this.beans=beans;
        AlertDialog.Builder builder=new AlertDialog.Builder(context);
        View dialogView =  View.inflate(context, R.layout.layout_dialog_select_img, null);
        mRlCancle = dialogView.findViewById(R.id.im_rl_cancle);
        mIvShare = dialogView.findViewById(R.id.im_iv_share);
        mRecycleView = dialogView.findViewById(R.id.im_recycleView);
        initRecycle(mRecycleView);
        setShareGroupDatas();
        adapter = new IMGroupListAdapter(context,R.layout.item_im_group_diaglog, datas);
        mRecycleView.setAdapter(adapter);
        adapter.setOnItemClickListner(this);
        mRlCancle.setOnClickListener(this);
        mIvShare.setOnClickListener(this);
        builder.setView(dialogView);
        shareDialog =  builder.show();

        WindowManager.LayoutParams params = shareDialog.getWindow().getAttributes();
        params.width = IMDensityUtil.getScreenWidth(context)- IMDensityUtil.dip2px(context,70);
        shareDialog.getWindow().setAttributes(params);
        shareDialog.getWindow().setBackgroundDrawable(context.getResources().getDrawable(R.drawable.im_shape__bg));
    }
    /**
     * 展示成功分享界面
     */
    private AlertDialog resultDialog;
    private void showShareResultDiglog(boolean issucessed) {
        AlertDialog.Builder builder=new AlertDialog.Builder(context);
        View dialogView =  View.inflate(context, R.layout.layout_dialog_share_result, null);
        ImageView  mIvResult =  dialogView.findViewById(R.id.im_iv_result);
        TextView mTvResult =  dialogView.findViewById(R.id.im_tv_result);
        TextView  mTvShare =  dialogView.findViewById(R.id.im_iv_share);
        TextView  mTvEnsure =  dialogView.findViewById(R.id.im_iv_ensure);
        if(issucessed){
            mTvResult.setText(context.getResources().getString(R.string.im_share_success));
            mIvResult.setImageResource(R.mipmap.im_share_sucess);
        }else {
            mTvResult.setText(context.getResources().getString(R.string.im_share_fail));
            mIvResult.setImageResource(R.mipmap.im_share_fail);
        }

        mTvShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resultDialog.dismiss();
                showShareDiglog(bean,beans);
            }
        });
        mTvEnsure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resultDialog.dismiss();
                EventBus.getDefault().post(new IMMessagShareEnsureEvevt());
                ((IMBetDetailActivity)context).finish();
            }
        });
        builder.setView(dialogView);
        resultDialog =  builder.show();
        WindowManager.LayoutParams params = shareDialog.getWindow().getAttributes();
        params.width = IMDensityUtil.getScreenWidth(context)- IMDensityUtil.dip2px(context,70);
        resultDialog.getWindow().setAttributes(params);
        resultDialog.getWindow().setBackgroundDrawable(context.getResources().getDrawable(R.drawable.im_shape__bg));
    }

    /**
     * 群消息分享
     */
    private void hanldeShareGroup(List<IMGroupBean> datas) {
        List<String> groups = new ArrayList<>();
        for (int i = 0; i < datas.size(); i++) {
            if (datas.get(i).getIschoosed()) {
                groups.add(datas.get(i).getGroupId());
            }
        }
        if (groups.size() == 0) {
            Toast.makeText(context, "请先选择群再进行分享", Toast.LENGTH_SHORT).show();
            return;
        }
        showShareResultDiglog(true);
        if (bean.getStatus().equals("UNSETTLED_ACCOUNTS")) {  //1是跟投，2是红单
            EventBus.getDefault().post(new IMMessagGroupShareEvevt(beans, groups, "1"));
        } else {
            EventBus.getDefault().post(new IMMessagGroupShareEvevt(beans, groups, "2"));
        }
    }
    /**
     * 获取群列表datas
     */
    private void setShareGroupDatas() {
        datas = new ArrayList<>();
        List<IMGroupBean> list = getGroupData();//获取群数据
        if(list==null || list.size()==0){
            Toast.makeText(context, "你还没有添加群聊，请先添加群~", Toast.LENGTH_SHORT).show();
            return;
        }
        //获取群数据
        for (int i = 0; i < list.size(); i++) {      //判断是否在当前群才能够进行分享
            if(list.get(i).getInGroup().equals("Y")){
                datas.add(list.get(i));
            }
        }
    }

    /**
     * 获取数据库的每个群消息
     */
    private  List<IMGroupBean>  getGroupData() {
        List<IMGroupBean> imGroupBeans = DaoUtils.getInstance().queryAllGroupData();
        if(imGroupBeans==null){
            return null;
        }
        for (int i = 0; i < imGroupBeans.size(); i++) {
            imGroupBeans.get(i).setIschoosed(false);
        }
        return imGroupBeans;
    }
    @SuppressLint("WrongConstant")
    private void initRecycle(RecyclerView mRecycleView) {
        //主内容
        IMSmoothScrollLayoutManager layoutManager =  new IMSmoothScrollLayoutManager(context);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        layoutManager.setReverseLayout(true);
        mRecycleView.setLayoutManager(layoutManager);
    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.im_rl_cancle){
            shareDialog.dismiss();
        }else if(v.getId()==R.id.im_iv_share){
            hanldeShareGroup(datas);
            shareDialog.dismiss();
        }
    }

    /**
     * 群item点击
     */
    @Override
    public void onItemClickListner(View v, int position, Object t) {
        for (int i = 0; i < datas.size(); i++) {
            if(position==i){
                if(datas.get(i).getIschoosed()){
                    datas.get(i).setIschoosed(false);
                }else {
                    datas.get(i).setIschoosed(true);
                }
            }
        }
        adapter.notifyDataSetChanged();
    }
}
