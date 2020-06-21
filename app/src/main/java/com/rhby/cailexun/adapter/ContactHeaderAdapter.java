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

package com.rhby.cailexun.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.widget.ImageView;

import com.android.im.R;
import com.android.im.imadapter.baseadapter.IMBaseRecycleViewAdapter_T;
import com.android.im.imadapter.baseadapter.IMBaseViewHolder;
import com.android.im.imview.IMRoundAngleImageView1;
import com.android.nettylibrary.IMSConfig;
import com.android.nettylibrary.greendao.entity.IMConversationBean;
import com.blankj.utilcode.util.SizeUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;
import java.util.List;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;


/**
 * 会话界面
 */
public class ContactHeaderAdapter extends IMBaseRecycleViewAdapter_T<IMConversationBean> {

    private Context context;

    private List<IMConversationBean> datas = new ArrayList<>();


    public ContactHeaderAdapter(Context context, int layoutId, List<IMConversationBean> datas) {
        super(context, layoutId, datas);
        this.context=context;
        this.datas=datas;
    }

    @Override
    protected void convert(IMBaseViewHolder holder, final int position, IMConversationBean bean) {
        holder.setText(R.id.item_name,datas.get(position).getConversationName());
        if(datas.get(position).getConversationId().equals(IMSConfig.CLX_ID)){//如果是彩乐信团队  加载本地头像
            Glide.with(context).load(R.mipmap.im_clx_grou).diskCacheStrategy(DiskCacheStrategy.ALL).dontAnimate().into((IMRoundAngleImageView1) holder.getView(R.id.item_head));
        }else {
            if(TextUtils.isEmpty(datas.get(position).getConversationavatar())){
                return;
            }
            Glide.with(context).load(datas.get(position)
                    .getConversationavatar())
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .dontAnimate()
                    .fitCenter()
                    .apply(RequestOptions.bitmapTransform(new RoundedCornersTransformation(SizeUtils.dp2px(5),0)))
                    .into((ImageView) holder.getView(R.id.item_head));
        }
    }

}                                                                                          