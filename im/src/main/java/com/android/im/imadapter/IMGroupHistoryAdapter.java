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
import android.view.View;

import com.android.im.IMSMsgManager;
import com.android.im.R;
import com.android.im.imadapter.baseadapter.IMBaseRecycleViewAdapter_T;
import com.android.im.imadapter.baseadapter.IMBaseViewHolder;
import com.android.nettylibrary.greendao.entity.IMConversationDetailBean;
import com.android.nettylibrary.http.IMGroupHistoryRoot;
import com.android.nettylibrary.utils.IMDensityUtil;
import com.android.nettylibrary.utils.IMTimeData;

import java.util.ArrayList;
import java.util.List;


/**
 * 群历史消息
 */
public class IMGroupHistoryAdapter extends IMBaseRecycleViewAdapter_T<IMConversationDetailBean> {

    private Context context;

    private List<IMConversationDetailBean> datas = new ArrayList<>();
    public  String  name;
    public  String  url;


    public IMGroupHistoryAdapter(Context context, int layoutId, List<IMConversationDetailBean> datas,String name,String url) {
        super(context, layoutId, datas);
        this.context=context;
        this.datas=datas;
        this.name=name;
        this.url=url;
    }
    @Override
    protected void convert(IMBaseViewHolder holder, final int position, IMConversationDetailBean bean) {
        holder.setText(R.id.im_tv_name,name);
        holder.setText(R.id.im_tv_time, IMTimeData.stampToTime(bean.getTimestamp()+"","MM-dd HH:mm"));
        holder.setText(R.id.im_tv_content, IMSMsgManager.getTextData(bean.getData()));
        holder.setCircleImageLoader(R.id.im_iv_head,url);

        if(position==datas.size()-1 && datas.size()>10){
            holder.getView(R.id.im_tv_com).setVisibility(View.VISIBLE);
        }else {
            holder.getView(R.id.im_tv_com).setVisibility(View.GONE);
        }
    }


}