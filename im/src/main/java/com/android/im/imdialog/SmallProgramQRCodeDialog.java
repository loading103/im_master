package com.android.im.imdialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
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
import com.android.im.imbean.IMQrcodeBean;
import com.android.im.imbean.SmallProgramBean;
import com.android.im.imui.activity.IMSmallChoosePersonActivity;
import com.blankj.utilcode.util.ToastUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;
import com.google.gson.Gson;

import cn.bingoogolapple.qrcode.zxing.QRCodeEncoder;
import cn.bingoogolapple.qrcode.zxing.ZXingView;


/**
 * 二维码
 * @author Wolf on 2020/01/04.
 * @name
 */
public class SmallProgramQRCodeDialog extends DialogFragment {

    private View view;
    private AlertDialog alertDialog;
    private SmallProgramBean data;
    private ImageView ivContent;
    private IMQrcodeBean bean;

    public interface Callback {
        void onClick(int position, AlertDialog alertDialog);
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
//        alertDialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimations;
        alertDialog.setView(new EditText(getContext()));
        if (!(getActivity()).isFinishing()) {
            alertDialog.show();
        }
        final Window window = alertDialog.getWindow();
        window.getDecorView().setBackgroundColor(getActivity().getResources().getColor(R.color
                .translate));
        window.getDecorView().setPadding(0, 0, 0, 0);//去掉padding

        LayoutInflater inflater = getActivity().getLayoutInflater();
        view = inflater.inflate(R.layout.dialog_small_program_qrcode, null);

        window.setContentView(view);
        window.setLayout(
                window.getContext().getResources().getDisplayMetrics().widthPixels,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        window.setGravity(Gravity.CENTER);

        alertDialog.setCanceledOnTouchOutside(true);

        initView();

        return alertDialog;
    }

    private void initView() {
        ImageView ivHead = view.findViewById(R.id.iv_head);
        TextView tvTitle = view.findViewById(R.id.tv_title);
        ivContent = view.findViewById(R.id.iv_content);

        bean = new IMQrcodeBean();
        bean.setCodeId(data.getProgramId());
        bean.setType("sp");
        bean.setImage(data.getThreeImage());
        bean.setName(data.getProgramName());

        tvTitle.setText(data.getProgramName());
        Glide.with(this).load(data.getTwoImage())
                .apply(RequestOptions.bitmapTransform(new CircleCrop()))
                .into(ivHead);

        initNetWorkImage(data.getTwoImage(),getActivity());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        callback = null;
    }

    /**
     * 加载网络图片
     * img_url 图片的网址
     */
    public void initNetWorkImage(final String imgUrl, final Activity context) {

        new AsyncTask<Void, Void, Bitmap>() {
            @Override
            protected Bitmap doInBackground(Void... params) {
                Bitmap bitmap = null;
                try {
                    bitmap = Glide.with(context)
                            .asBitmap()
                            .load(imgUrl)
                            .submit(100, 100).get();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return bitmap;
            }

            @Override
            protected void onPostExecute(Bitmap bitmap) {
                Bitmap b = QRCodeEncoder.syncEncodeQRCode(new Gson().toJson(bean), 400, R.color.black, bitmap);
                ivContent.setImageBitmap(b);
            }
        }.execute();
    }
}
