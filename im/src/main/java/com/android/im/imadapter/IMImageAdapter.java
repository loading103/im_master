package com.android.im.imadapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.recyclerview.widget.RecyclerView;

import com.android.im.R;
import com.android.nettylibrary.utils.IMDensityUtil;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;


public class IMImageAdapter extends RecyclerView.Adapter<IMImageAdapter.MyViewHolder> {
    private Context context;
    private List<String> picPaths = new ArrayList<String>();

    public IMImageAdapter(List<String> picPaths, Context context) {
        this.picPaths = picPaths;
        this.context = context;
    }

    @Override
    public IMImageAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        IMImageAdapter.MyViewHolder holder = new IMImageAdapter.MyViewHolder(LayoutInflater.from(context).inflate(R.layout.im_item_add_image, parent, false));
        return holder;
    }
    @Override
    public void onBindViewHolder(IMImageAdapter.MyViewHolder holder, final int position) {

        if (position == (getItemCount() - 1)) {
            holder.iv.setImageResource(R.mipmap.im_iv_add);
            holder.iv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(choosePicListener!=null){
                        choosePicListener.onHanlerAddPic(0,0);
                    }
                }
            });
            holder.del.setVisibility(View.GONE);
        }

        if (picPaths.size() == 0 || position > picPaths.size() - 1){
            return;
        }

        final String path = picPaths.get(position);
        holder.del.setVisibility(View.VISIBLE);
        holder.del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(choosePicListener!=null){
                    choosePicListener.onHanlerAddPic(0,1);
                }
            }
        });
        holder.iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(choosePicListener!=null){
                    choosePicListener.onHanlerAddPic(position,2);
                }
            }
        });
        Glide.with(context).load(path).thumbnail(0.8f).error(R.mipmap.im_bg_header).into(holder.iv);
    }
    @Override
    public int getItemCount() {
        if (picPaths.size() >= 4) {
            return 4;
        } else {
            return picPaths.size() + 1;
        }
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView iv, del;
        public MyViewHolder(View view) {
            super(view);
            iv =  view.findViewById(R.id.iv_item_news_image);
            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) iv.getLayoutParams();
            int screenWidth = (IMDensityUtil.getScreenWidth(context)-120)/4;
            layoutParams.height = screenWidth;
            layoutParams.width = screenWidth;
            iv.setLayoutParams(layoutParams);
            del =  view.findViewById(R.id.iv_item_del);
        }
    }


    public interface ChoosePicListener{
        void     onHanlerAddPic(int position,int type);
    }
    private  ChoosePicListener choosePicListener;

    public void setChoosePicListener(ChoosePicListener choosePicListener) {
        this.choosePicListener = choosePicListener;
    }
}