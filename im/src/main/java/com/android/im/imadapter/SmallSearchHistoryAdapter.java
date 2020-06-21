package com.android.im.imadapter;

import android.widget.ImageView;

import com.android.im.R;
import com.android.im.imbean.SerachHistoryBean;
import com.android.im.imbean.SmallProgramBean;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.module.DraggableModule;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;

/**
 * Created by Wolf on 2019/12/19.
 * Describe:搜索历史
 */
public class SmallSearchHistoryAdapter extends BaseQuickAdapter<SerachHistoryBean, BaseViewHolder> implements DraggableModule {
    public SmallSearchHistoryAdapter() {
        super(R.layout.item_serach_history);
    }

    @Override
    protected void convert(BaseViewHolder helper, SerachHistoryBean item) {
        helper.setText(R.id.tv_name,item.getName());
    }
}