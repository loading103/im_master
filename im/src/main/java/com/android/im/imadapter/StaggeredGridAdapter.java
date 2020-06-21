package com.android.im.imadapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import androidx.recyclerview.widget.RecyclerView;

import com.android.im.R;
import com.android.im.imbean.MyBgBeanTotleData;
import com.android.im.imui.activity.IMChooseBgActicity;
import com.android.im.imutils.IMImageLoadUtil;
import com.android.im.imview.IMRoundAngleImageView;
import com.android.nettylibrary.utils.IMDensityUtil;

import java.util.ArrayList;
import java.util.List;

public class StaggeredGridAdapter extends RecyclerView.Adapter<StaggeredGridAdapter.LinearViewHolder> {

    private Context mContext;
    private AdapterView.OnItemClickListener mListener;
    private List<MyBgBeanTotleData> list = new ArrayList<>();
    private  int screenWidth ;
    private  int baseWidth ;
    public StaggeredGridAdapter(Context mContext,List<MyBgBeanTotleData> list) {
        this.mContext = mContext;
        this.list = list;
        screenWidth= IMDensityUtil.getScreenWidth(mContext);
        baseWidth=screenWidth/2;
    }

    @Override
    public StaggeredGridAdapter.LinearViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new LinearViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_staggere_grid_item,null));
    }
    @Override
    public void onBindViewHolder(StaggeredGridAdapter.LinearViewHolder holder, int position) {
        ViewGroup.LayoutParams layoutParams = holder.mImageView.getLayoutParams();
        layoutParams.width=baseWidth-IMDensityUtil.dip2px(mContext,20);
        int height=0;
        if(position==0){
            height=IMDensityUtil.dip2px(mContext,170);
        }else if (position==1){
            height=IMDensityUtil.dip2px(mContext,210);
        }else if (position==2){
            height=IMDensityUtil.dip2px(mContext,210);
        }
        else if (position==3){
            height=IMDensityUtil.dip2px(mContext,190);
        }
        else if (position==4){
            height=IMDensityUtil.dip2px(mContext,220);
        }else if (position==5){
            height=IMDensityUtil.dip2px(mContext,200);
        }else if (position==6){
            height=IMDensityUtil.dip2px(mContext,180);
        }else if (position==7){
            height=IMDensityUtil.dip2px(mContext,210);
        }else if (position==8){
            height=IMDensityUtil.dip2px(mContext,180);
        }else if (position==9){
            height=IMDensityUtil.dip2px(mContext,190);
        } else {
            height= IMDensityUtil.dip2px(mContext,150+20*(int)(Math.random()*(1+6)));
            if(height>IMDensityUtil.dip2px(mContext,250)){
                height=IMDensityUtil.dip2px(mContext,170);
            }
        }
        layoutParams.height=height;
        holder.mImageView.setLayoutParams(layoutParams);
        IMImageLoadUtil.ImageLoad(mContext,list.get(position).getPicurl(), holder.mImageView);
        holder.mImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(mContext, IMChooseBgActicity.class);
                intent.putExtra("bean",list.get(position));
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
    class LinearViewHolder extends RecyclerView.ViewHolder {
        private IMRoundAngleImageView mImageView;
        public LinearViewHolder(View itemView) {

            super(itemView);
            mImageView =  itemView.findViewById(R.id.iv);
        }
    }
}