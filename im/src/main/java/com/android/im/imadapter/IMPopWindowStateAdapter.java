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
import android.widget.TextView;

import com.android.im.R;
import com.android.im.imadapter.baseadapter.IMBaseRecycleViewAdapter_T;
import com.android.im.imadapter.baseadapter.IMBaseViewHolder;
import com.android.im.imbean.IMGameStatusBean;
import com.android.im.imbean.IMGameTypeBean;
import com.android.nettylibrary.http.IMGroupHistoryRoot;
import com.android.nettylibrary.utils.IMDensityUtil;
import com.android.nettylibrary.utils.IMTimeData;

import java.util.ArrayList;
import java.util.List;


/**
 * 投注记录（状态）
 */
public class IMPopWindowStateAdapter extends IMBaseRecycleViewAdapter_T<IMGameStatusBean> {

    private Context context;

    private List<IMGameStatusBean> datas = new ArrayList<>();


    public IMPopWindowStateAdapter(Context context, int layoutId, List<IMGameStatusBean> datas) {
        super(context, layoutId, datas);
        this.context=context;
        this.datas=datas;
    }

    @Override
    protected void convert(IMBaseViewHolder holder, final int position, IMGameStatusBean bean) {
        holder.setText(R.id.im_tv_name,bean.getStatusName());
        if(bean.isChooseed()){
            holder.getView(R.id.im_tv_name).setBackgroundResource(R.drawable.im_shape_bg_blue6);
            ((TextView)holder.getView(R.id.im_tv_name)).setTextColor(context.getResources().getColor(R.color.white));
        }else {
            holder.getView(R.id.im_tv_name).setBackgroundResource(R.drawable.im_shape_bg_white5);
            ((TextView)holder.getView(R.id.im_tv_name)).setTextColor(context.getResources().getColor(R.color.color_666666));
        }
    }





}