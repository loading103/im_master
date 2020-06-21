package com.android.im.imui.fragment;//package com.android.im.imui.fragment;

import android.content.Intent;
import android.view.View;
import android.widget.RelativeLayout;

import com.android.im.R;
import com.android.im.imui.activity.IMQRCActivity;
import com.android.im.imui.activity.SmallProgramSearchActivity;


public class IMFindFragment extends IMBaseFragment implements View.OnClickListener {
    private View view;
    private RelativeLayout mllScan;
    private RelativeLayout mllXcx;
    @Override
    public View initView() {
        view = View.inflate(getActivity(), R.layout.fragment_im_finds, null);
        mllScan=view.findViewById(R.id.im_rl_scan);
        mllXcx=view.findViewById(R.id.im_rl_xcx);
        mllScan.setOnClickListener(this);
        mllXcx.setOnClickListener(this);
        return view;
    }
    @Override
    public void iniData() {
    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.im_rl_scan){
            Intent intent2 = new Intent(getActivity(), IMQRCActivity.class);
            getActivity().startActivity(intent2);
        }else  if(v.getId()==R.id.im_rl_xcx){
            Intent intent = new Intent( getActivity(), SmallProgramSearchActivity.class);
            getActivity().startActivity(intent);
        }
    }
}
