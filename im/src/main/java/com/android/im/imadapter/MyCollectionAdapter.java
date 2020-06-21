package com.android.im.imadapter;

import android.widget.ImageView;

import com.android.im.R;
import com.android.im.imbean.SmallProgramBean;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.module.DraggableModule;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;

/**
 * Created by Wolf on 2019/12/19.
 * Describe:我的收藏
 */
public class MyCollectionAdapter extends BaseQuickAdapter<SmallProgramBean, BaseViewHolder> implements DraggableModule {
    public MyCollectionAdapter() {
        super(R.layout.item_latest_use);
    }


    @Override
    protected void convert(BaseViewHolder helper, SmallProgramBean item) {
        Glide.with(getContext()).load(item.getTwoImage())
                .apply(RequestOptions.bitmapTransform(new CircleCrop()))
                .into((ImageView) helper.getView(R.id.iv_content));
        helper.setText(R.id.tv_content,item.getProgramName());
    }
}