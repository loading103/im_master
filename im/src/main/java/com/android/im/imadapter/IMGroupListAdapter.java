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
import android.widget.TextView;

import com.android.im.R;
import com.android.im.imadapter.baseadapter.IMBaseRecycleViewAdapter_T;
import com.android.im.imadapter.baseadapter.IMBaseViewHolder;
import com.android.im.imbean.IMGameStatusBean;
import com.android.im.imutils.IMImageLoadUtil;
import com.android.nettylibrary.greendao.entity.IMGroupBean;

import java.util.ArrayList;
import java.util.List;

/**
 * 投注记录（状态）
 */
public class IMGroupListAdapter extends IMBaseRecycleViewAdapter_T<IMGroupBean> {

    private Context context;

    private List<IMGroupBean> datas = new ArrayList<>();


    public IMGroupListAdapter(Context context, int layoutId, List<IMGroupBean> datas) {
        super(context, layoutId, datas);
        this.context=context;
        this.datas=datas;
    }

    @Override
    protected void convert(IMBaseViewHolder holder, final int position, IMGroupBean bean) {
        IMImageLoadUtil.ImageLoadCircle(context,bean.getGroupAvatar(),(ImageView) holder.getView(R.id.im_iv_head));
        holder.setText(R.id.im_tv_name,bean.getGroupName());
        if(bean.getIschoosed()){
            ((ImageView) holder.getView(R.id.im_iv_choose)).setImageResource(R.mipmap.im_iv_choose_yes);
        }else {
            ((ImageView) holder.getView(R.id.im_iv_choose)).setImageResource(R.mipmap.im_iv_choose_no);
        }
    }


}