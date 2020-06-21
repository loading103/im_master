package com.android.im.imdialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;

import com.android.im.R;
import com.android.im.imbean.SmallProgramBean;
import com.android.im.imui.activity.IMSmallChoosePersonActivity;
import com.blankj.utilcode.util.ToastUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;


/**
 * 分享
 * @author Wolf on 2019/12/25.
 * @name
 */
public class SmallProgramShareDialog extends DialogFragment {

    private View view;
    private AlertDialog alertDialog;
    private SmallProgramBean data;
    private boolean isIndex;

    public interface Callback {
        void onClick(int position, AlertDialog alertDialog);
    }

    private Callback callback;

    public void show(FragmentManager fragmentManager, SmallProgramBean data, boolean isIndex) {
        this.data=data;
        this.isIndex=isIndex;
        show(fragmentManager, "ViewDialogFragment");
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        alertDialog = new AlertDialog.Builder(getActivity())
                .create();
        alertDialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimations;
        alertDialog.setView(new EditText(getContext()));
        if (!(getActivity()).isFinishing()) {
            alertDialog.show();
        }
        final Window window = alertDialog.getWindow();
        window.getDecorView().setBackgroundColor(getActivity().getResources().getColor(R.color
                .translate));
        window.getDecorView().setPadding(0, 0, 0, 0);//去掉padding

        LayoutInflater inflater = getActivity().getLayoutInflater();
        view = inflater.inflate(R.layout.dialog_small_program_share, null);

        window.setContentView(view);
        window.setLayout(
                window.getContext().getResources().getDisplayMetrics().widthPixels,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        window.setGravity(Gravity.BOTTOM);

        alertDialog.setCanceledOnTouchOutside(true);

        initView();

        return alertDialog;
    }

    private void initView() {
        TextView tvCancer = view.findViewById(R.id.tv_cancer);
        TextView tvName = view.findViewById(R.id.tv_name);
        ImageView ivHead = view.findViewById(R.id.iv_head);
        LinearLayout llHome = view.findViewById(R.id.ll_home);
        LinearLayout llCollection = view.findViewById(R.id.ll_collection);
        LinearLayout llShare = view.findViewById(R.id.ll_share);
        LinearLayout llScan = view.findViewById(R.id.ll_scan);
        TextView tvHome = view.findViewById(R.id.tv_home);
        TextView tvCollection = view.findViewById(R.id.tv_collection);
        ImageView ivHome = view.findViewById(R.id.iv_home);
        ImageView ivCollection = view.findViewById(R.id.iv_collection);

        tvCancer.setOnClickListener(v -> dismiss());
        llHome.setOnClickListener(v -> {
            callback.onClick(0,alertDialog);
        });
        llCollection.setOnClickListener(v -> {
            callback.onClick(1,alertDialog);
        });
        llShare.setOnClickListener(v -> {
            if("N".equals(data.getIsOnline())){
                ToastUtils.showShort("该小程序已下架");
                return;
            }
            Intent intent=new Intent(getContext(), IMSmallChoosePersonActivity.class);
            intent.putExtra("data",data);
            getActivity().startActivity(intent);
        });
        llScan.setOnClickListener(v -> {
            callback.onClick(3,alertDialog);
        });
        Glide.with(this).load(data.getTwoImage())
                .apply(RequestOptions.bitmapTransform(new CircleCrop()))
                .into(ivHead);
        tvName.setText(data.getProgramName());
        if(isIndex){
            ivHome.setImageResource(R.mipmap.small_home2);
            llHome.setClickable(false);
        }else {
            ivHome.setImageResource(R.mipmap.small_hone);
            llHome.setClickable(true);
        }

        if(data.getIsConllection()!=null&&data.getIsConllection().equals("Y")){
            ivCollection.setImageResource(R.mipmap.small_collection_share2);
            tvCollection.setText("从我的小程序中移除");
        }else {
            ivCollection.setImageResource(R.mipmap.small_collection_share);
            tvCollection.setText("添加到我的小程序");
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof Callback) {
            callback = (Callback) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement Callback");
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        callback = null;
    }
}
