
package com.rhby.cailexun.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import com.android.im.imadapter.baseadapter.IMBaseRecycleViewAdapter_T;
import com.android.im.imadapter.baseadapter.IMBaseViewHolder;
import com.android.im.imbean.IMChoosePicBean;
import com.android.im.imutils.IMImageLoadUtil;
import com.rhby.cailexun.R;

import java.util.List;

public class ChooseChatBgAdapter extends IMBaseRecycleViewAdapter_T<IMChoosePicBean> {
    private Context context;
    private List<IMChoosePicBean> datas;

    public ChooseChatBgAdapter(Context context, int layoutId, List<IMChoosePicBean> datas) {
        super(context, layoutId, datas);
        this.context=context;
        this.datas=datas;
    }
    @Override
    protected void convert(IMBaseViewHolder holder, final int position, IMChoosePicBean bean) {
        if(bean.isChoosed()){
            holder.getView(R.id.iv_choose).setVisibility(View.VISIBLE);
        }else {
            holder.getView(R.id.iv_choose).setVisibility(View.GONE);
        }
//        ((ImageView)holder.getView(R.id.iv_bg)).setImageResource(bean.getRes());
        IMImageLoadUtil.CommonImageBgLoadCp(context,bean.getUrl(),(ImageView)holder.getView(R.id.iv_bg));
    }
}