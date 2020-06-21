
package com.android.im.imadapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import com.android.im.R;
import com.android.im.imadapter.baseadapter.IMBaseRecycleViewAdapter_T;
import com.android.im.imadapter.baseadapter.IMBaseViewHolder;
import com.android.im.imbean.IMFriendsBean;
import com.android.im.imutils.IMImageLoadUtil;
import com.android.nettylibrary.greendao.entity.IMPersonBean;

import java.util.List;

public class IMNewFriedAdapter extends IMBaseRecycleViewAdapter_T<IMFriendsBean> {
    private Context context;
    private List<IMFriendsBean> datas;
    public static final int VIEW_TYPE_ITEM = 1;
    public static final int VIEW_TYPE_EMPTY = 0;

    public IMNewFriedAdapter(Context context, int layoutId, List<IMFriendsBean> datas) {
        super(context, layoutId, datas);
        this.context=context;
        this.datas=datas;
    }

    @Override
    protected void convert(IMBaseViewHolder holder, final int position, IMFriendsBean bean) {

        holder.setText(R.id.im_tv_name,bean.getNickName());
        IMImageLoadUtil.ImageLoadCircle(context,bean.getAvatar(),(ImageView)holder.getView(R.id.im_iv_head));
        //申请状态:1=等待通过，2=通过，3=拒绝,4=已过期
        switch (Integer.parseInt(bean.getApplyStatus())){
            case 1:
                holder.getView(R.id.im_ll_add).setVisibility(View.VISIBLE);
                holder.getView(R.id.im_tv_added).setVisibility(View.GONE);
                break;
            case 2:
                holder.getView(R.id.im_ll_add).setVisibility(View.GONE);
                holder.getView(R.id.im_tv_added).setVisibility(View.VISIBLE);
                holder.setText(R.id.im_tv_added,"已添加");
                break;
            case 3:
                holder.getView(R.id.im_ll_add).setVisibility(View.GONE);
                holder.getView(R.id.im_tv_added).setVisibility(View.VISIBLE);
                holder.setText(R.id.im_tv_added,"已拒绝");
                break;
            case 4:
                holder.getView(R.id.im_ll_add).setVisibility(View.GONE);
                holder.getView(R.id.im_tv_added).setVisibility(View.VISIBLE);
                holder.setText(R.id.im_tv_added,"已过期");
                break;
        }
        holder.getView(R.id.im_tv_add).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(onaddListener!=null){
                    onaddListener.setOnAddListener(true,position);
                }

            }
        });
        holder.getView(R.id.im_tv_no).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(onaddListener!=null){
                    onaddListener.setOnAddListener(false,position);
                }
            }
        });
    }



    public interface onAddListener{
        void  setOnAddListener(boolean isadd,int position);
    }
    private onAddListener onaddListener;

    public void setOnaddListener(onAddListener onaddListener) {
        this.onaddListener = onaddListener;
    }
}