package com.android.im.imdialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;

import com.android.im.IMSManager;
import com.android.im.R;
import com.android.im.imbean.SmallProgramBean;
import com.android.nettylibrary.IMSConfig;
import com.android.nettylibrary.utils.IMPreferenceUtil;
import com.blankj.utilcode.util.ToastUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;


/**
 * 授权
 * @author Wolf on 2019/12/25.
 * @name
 */
public class SmallProgramAgreeDialog extends DialogFragment {

    private View view;
    private AlertDialog alertDialog;
    private SmallProgramBean data;

    public interface Callback {
        void onAgreeClick(int position, AlertDialog alertDialog);
    }

    private Callback callback;

    public void show(FragmentManager fragmentManager, SmallProgramBean data) {
        this.data=data;
        show(fragmentManager, "ViewDialogFragment");
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        alertDialog = new AlertDialog.Builder(getActivity())
                .create();

//        alertDialog.getWindow().getAttributes().windowAnimations = R.style.FragmentDialogAnimation;

        alertDialog.setView(new EditText(getContext()));//解决dialog中et无法弹出输入法的问题

        if (!(getActivity()).isFinishing()) {
            alertDialog.show();
        }

        final Window window = alertDialog.getWindow();
        window.getDecorView().setBackgroundColor(getActivity().getResources().getColor(R.color
                .translate));
        window.getDecorView().setPadding(0, 0, 0, 0);//去掉padding

        LayoutInflater inflater = getActivity().getLayoutInflater();
        view = inflater.inflate(R.layout.dialog_small_program_login, null);

        window.setContentView(view);
        window.setLayout(
                window.getContext().getResources().getDisplayMetrics().widthPixels,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        window.setGravity(Gravity.CENTER);

        alertDialog.setCanceledOnTouchOutside(false);

        initView();

        return alertDialog;
    }

    private void initView() {
        TextView tvCancer = view.findViewById(R.id.tv_cancer);
        TextView tvAgree = view.findViewById(R.id.tv_agree);
        ImageView ivPerson = view.findViewById(R.id.iv_person);
        TextView tvUername = view.findViewById(R.id.tv_username);
        TextView tvInfo = view.findViewById(R.id.tv_info);
        ImageView ivHead = view.findViewById(R.id.iv_head);
        TextView tvName = view.findViewById(R.id.tv_name);
        tvCancer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callback.onAgreeClick(0,alertDialog);
            }
        });
        tvAgree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callback.onAgreeClick(1,alertDialog);
            }
        });
        Glide.with(this).load(IMSManager.getMyHeadView())
                .apply(RequestOptions.bitmapTransform(new CircleCrop()))
                .into(ivPerson);
        Glide.with(this).load(data.getTwoImage())
                .apply(RequestOptions.bitmapTransform(new CircleCrop()))
                .into(ivHead);
        tvUername.setText(IMSManager.getMyNickName());
        tvInfo.setText(IMSManager.getMySignature());
        tvName.setText(data.getProgramName());
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
