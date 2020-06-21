package com.rhby.cailexun.adapter;

import android.widget.ImageView;

import com.rhby.cailexun.R;
import com.rhby.cailexun.bean.BrowseRecordBean;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

/**
 * Created by Wolf on 2019/11/22.
 * Describe:最近浏览
 */
public class LatestBrowsingAdapter extends BaseQuickAdapter<BrowseRecordBean,BaseViewHolder> {
    public LatestBrowsingAdapter() {
        super(R.layout.item_latest_browsing);
    }

    @Override
    protected void convert(BaseViewHolder helper, BrowseRecordBean item) {
        helper.setText(R.id.tv_title,item.getTitle())
                .setText(R.id.tv_content,item.getSubTitle());
        Glide.with(getContext()).load(item.getFaceImage())
                .apply(RequestOptions.bitmapTransform(new RoundedCornersTransformation(10,0)))
                .into((ImageView) helper.getView(R.id.iv_content));
    }
}