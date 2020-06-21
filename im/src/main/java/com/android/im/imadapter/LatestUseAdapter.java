package com.android.im.imadapter;

import android.widget.ImageView;

import com.android.im.R;
import com.android.im.imbean.SmallProgramBean;
import com.blankj.utilcode.util.LogUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.module.DraggableModule;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

/**
 * Created by Wolf on 2019/12/19.
 * Describe:最近使用
 */
public class LatestUseAdapter extends BaseQuickAdapter<SmallProgramBean, BaseViewHolder> implements DraggableModule {
    public LatestUseAdapter() {
        super(R.layout.item_latest_use);
    }

    @Override
    protected void convert(BaseViewHolder helper, SmallProgramBean item) {
        if(item.isFlag()){
            Glide.with(getContext()).load(R.mipmap.small_more)
                    .apply(RequestOptions.bitmapTransform(new CircleCrop()))
                    .into((ImageView) helper.getView(R.id.iv_content));
            helper.setText(R.id.tv_content,"");
        }else {
            Glide.with(getContext()).load(item.getTwoImage())
                    .apply(RequestOptions.bitmapTransform(new CircleCrop()))
                    .into((ImageView) helper.getView(R.id.iv_content));
            helper.setText(R.id.tv_content,item.getProgramName());
        }

//        LogUtils.i("WOLF","isDrag:"+item.isDrag());
//        if(item.isDrag()){
//            helper.setTextColor(R.id.tv_content,R.color.color_21B8FD);
//        }else {
//            helper.setTextColor(R.id.tv_content,R.color.white);
//        }
    }
}