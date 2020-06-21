/*
 * Copyright (C) 2014 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.android.im.imadapter;

import android.content.Context;
import android.widget.ImageView;

import com.android.im.R;
import com.android.im.imadapter.baseadapter.IMBaseRecycleViewAdapter_T;
import com.android.im.imadapter.baseadapter.IMBaseViewHolder;
import com.android.im.imutils.IMImageLoadUtil;
import com.android.nettylibrary.greendao.entity.IMGroupMemberBean;
import com.blankj.utilcode.util.SizeUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.shehuan.niv.NiceImageView;

import java.util.ArrayList;
import java.util.List;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

/**
 * 群成员
 */
public class IMGroupInforAdapter extends IMBaseRecycleViewAdapter_T<IMGroupMemberBean> {

    private Context context;

    private List<IMGroupMemberBean> datas = new ArrayList<>();
    public IMGroupInforAdapter(Context context, int layoutId, List<IMGroupMemberBean> datas) {
        super(context, layoutId, datas);
        this.context=context;
        this.datas=datas;
    }
    @Override
    protected void convert(IMBaseViewHolder holder, final int position, IMGroupMemberBean bean) {
        NiceImageView itemHead = (NiceImageView) holder.getView(R.id.item_head);
        if(position==0){
//            IMImageLoadUtil.ImageLoadBlueCircle(context,bean.getAvatar(),(ImageView) holder.getView(R.id.item_head));
            itemHead.setBorderWidth(1);
            Glide.with(context).load(bean.getAvatar())
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .dontAnimate()
                    .centerCrop()
                    .apply(RequestOptions.bitmapTransform(new RoundedCornersTransformation(SizeUtils.dp2px(5),0)))
                    .into((ImageView) holder.getView(R.id.item_head));
        }else {
            itemHead.setBorderWidth(0);
            IMImageLoadUtil.ImageLoadCircle(context,bean.getAvatar(),(ImageView) holder.getView(R.id.item_head));
        }

        holder.setText(R.id.item_name,bean.getNickName());
    }
}