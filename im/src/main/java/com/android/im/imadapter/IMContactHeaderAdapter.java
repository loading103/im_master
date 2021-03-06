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

import com.android.im.R;
import com.android.im.imadapter.baseadapter.IMBaseRecycleViewAdapter_T;
import com.android.im.imadapter.baseadapter.IMBaseViewHolder;
import com.android.im.imutils.IMImageLoadUtil;
import com.android.nettylibrary.greendao.entity.IMConversationBean;

import java.util.ArrayList;
import java.util.List;


/**
 * 会话界面
 */
public class IMContactHeaderAdapter extends IMBaseRecycleViewAdapter_T<String> {

    private Context context;

    private List<String> datas = new ArrayList<>();


    public IMContactHeaderAdapter(Context context, int layoutId, List<String> datas) {
        super(context, layoutId, datas);
        this.context=context;
        this.datas=datas;
    }

    @Override
    protected void convert(IMBaseViewHolder holder, final int position, String bean) {
//        holder.setText(R.id.item_name,datas.get(position).getConversationName());
        IMImageLoadUtil.ImageLoadCircle(context,datas.get(position),holder.getView(R.id.item_head));
    }

}