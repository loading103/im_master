
package com.android.im.imadapter;

import android.content.Context;
import android.widget.ImageView;

import com.android.im.R;
import com.android.im.imadapter.baseadapter.IMBaseRecycleViewAdapter_T;
import com.android.im.imadapter.baseadapter.IMBaseViewHolder;
import com.android.im.imutils.IMImageLoadUtil;
import com.android.nettylibrary.greendao.entity.IMPersonBean;

import java.util.List;

public class IMAddFriedAdapter extends IMBaseRecycleViewAdapter_T<IMPersonBean> {
    private Context context;
    private List<IMPersonBean> datas;

    public IMAddFriedAdapter(Context context, int layoutId, List<IMPersonBean> datas) {
        super(context, layoutId, datas);
        this.context=context;
        this.datas=datas;
    }
    @Override
    protected void convert(IMBaseViewHolder holder, final int position, IMPersonBean bean) {

        holder.setText(R.id.im_tv_name,bean.getNickName());
        IMImageLoadUtil.ImageLoadCircle(context,bean.getAvatar(),(ImageView)holder.getView(R.id.im_iv_head));
    }
}