package com.android.im.imview.dialog;

import android.app.AlertDialog;
import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.im.R;
import com.android.im.imadapter.IMContactHeaderAdapter;
import com.android.im.imbean.SmallProgramBean;
import com.android.im.imutils.IMImageLoadUtil;
import com.android.nettylibrary.greendao.entity.IMGroupBean;
import com.android.nettylibrary.greendao.entity.IMPersonBean;
import com.android.nettylibrary.utils.IMDensityUtil;

import java.util.ArrayList;
import java.util.List;

public class IMShareSmallDiglog{
    private Context context;
    private AlertDialog shareDialog;
    private TextView mTvName;
    private TextView mTvCancle;
    private EditText mEtContent;
    private TextView mTvEnsure;
    private ImageView mIvHead;
    private ImageView mIvContent;
    private RecyclerView mRecycle;
    private IMContactHeaderAdapter adapter;
    public IMShareSmallDiglog(Context context) {
        this.context=context;
    }
    public void showPersonalDiglog(List<IMPersonBean> beans, List<IMGroupBean>datas, SmallProgramBean bean,OnSendListener onFinishListener) {
        AlertDialog.Builder builder=new AlertDialog.Builder(context);
        final View dialogView =  View.inflate(context, R.layout.layout_share_small_content, null);

        mTvName = dialogView.findViewById(R.id.tv_name);
        mTvCancle = dialogView.findViewById(R.id.tv_cancle);
        mEtContent = dialogView.findViewById(R.id.im_et_content);
        mTvEnsure = dialogView.findViewById(R.id.tv_send);
        mIvHead=dialogView.findViewById(R.id.iv_header);
        mIvContent=dialogView.findViewById(R.id.iv_content);
        mRecycle = dialogView.findViewById(R.id.recycler);
        IMImageLoadUtil.ImageLoad(context,bean.getShareImage(),mIvContent);
        List<String>headurls=new ArrayList<>();
        if(beans!=null && beans.size()>0){
            for (int i = 0; i < beans.size(); i++) {
                headurls.add(beans.get(i).getAvatar());
            }
        }
        if(datas!=null && datas.size()>0){
            for (int i = 0; i < datas.size(); i++) {
                headurls.add(datas.get(i).getGroupAvatar());
            }
        }
        adapter = new IMContactHeaderAdapter(context,R.layout.im_item_latest_contact, headurls);
        mRecycle.setLayoutManager(new GridLayoutManager(context, 5));
        mRecycle.setNestedScrollingEnabled(false);
        mRecycle.setAdapter(adapter);
        mTvCancle.setOnClickListener(v -> shareDialog.dismiss());
        mTvEnsure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String s = mEtContent.getText().toString();
//                if(TextUtils.isEmpty(s)){
//                    Toast.makeText(context, "内容不能为空", Toast.LENGTH_SHORT).show();
//                    return;
//                }
                shareDialog.dismiss();
                onFinishListener.setOnSendListener(s);
            }
        });
        builder.setView(dialogView);
        shareDialog =  builder.show();

        Window window = shareDialog.getWindow();
        window.setWindowAnimations(R.style.ActionSheetDialogAnimations);  //添加动画

        WindowManager.LayoutParams params = window.getAttributes();
        params.width = IMDensityUtil.getScreenWidth(context)- IMDensityUtil.dip2px(context,70);
        shareDialog.getWindow().setAttributes(params);
        shareDialog.getWindow().setBackgroundDrawable(context.getResources().getDrawable(R.drawable.im_shape__bg));
    }




    public  interface  OnSendListener{
      void   setOnSendListener(String content);
    }
    private  OnSendListener onSendListener;

    public void setOnFinishListener(OnSendListener onFinishListener) {
        this.onSendListener = onFinishListener;
    }
}