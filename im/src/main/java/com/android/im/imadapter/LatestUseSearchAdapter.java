package com.android.im.imadapter;

import android.widget.ImageView;


import com.android.im.R;
import com.android.im.imbean.SmallProgramBean;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

/**
 * Created by Wolf on 2019/12/23.
 * Describe:搜索——最近浏览
 */
public class LatestUseSearchAdapter extends BaseQuickAdapter<SmallProgramBean, BaseViewHolder> {
    public LatestUseSearchAdapter() {
        super(R.layout.item_latest_browsing_search);
    }

    @Override
    protected void convert(BaseViewHolder helper, SmallProgramBean item) {
        Glide.with(getContext()).load(item.getTwoImage())
                .apply(RequestOptions.bitmapTransform(new CircleCrop()))
                .into((ImageView) helper.getView(R.id.iv_content));
        helper.setText(R.id.tv_content, item.getProgramName());
        if (item.getIsConllection().equals("N")) {
            helper.setBackgroundResource(R.id.ll_collection, R.color.color_21B8FD)
                    .setText(R.id.tv_collection, "添加到我的小程序")
                    .setVisible(R.id.iv_collection, false);
        } else {
            helper.setBackgroundResource(R.id.ll_collection, R.color.color_cccccc);
            helper.setText(R.id.tv_collection, "从我的小程序中移除")
                    .setVisible(R.id.iv_collection, true);
        }
    }
}