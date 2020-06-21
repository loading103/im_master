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
import android.widget.ImageView;

import com.android.im.R;
import com.android.im.imadapter.baseadapter.IMBaseRecycleViewAdapter_T;
import com.android.im.imadapter.baseadapter.IMBaseViewHolder;
import com.android.im.imbean.IMBettingOrders;
import com.android.im.imbean.IMGameTypeBean;
import com.android.nettylibrary.utils.IMTimeData;

import java.util.ArrayList;
import java.util.List;


/**
 * 投注记录
 */
public class IMBetListAdapter extends IMBaseRecycleViewAdapter_T<IMBettingOrders> {

    private Context context;

    private List<IMBettingOrders> datas = new ArrayList<>();
    private List<IMGameTypeBean> gamesDatas = new ArrayList<>();

    public IMBetListAdapter(Context context, int layoutId, List<IMBettingOrders> datas) {
        super(context, layoutId, datas);
        this.context=context;
        this.datas=datas;
        gamesDatas= IMGameTypeBean.getGameBeans();
    }

    /** UNSETTLED_ACCOUNTS:未结算 NOT_WINNING_THE_PRIZE:未中奖 HAS_WON_THE_PRIZE:已中奖	是	[string]
     * {"amount":"0.000","bettingOrderId":"201909031168827411220926464","gameId":"3",
     * "gameName":"新疆时时彩","investAmount":"500.000","investContent":"6","status":"UNSETTLED_ACCOUNTS","tradeTime":1567551945000}
     */
    @Override
    protected void convert(IMBaseViewHolder holder, final int position, IMBettingOrders bean) {
        if(bean.getStatus().equals("UNSETTLED_ACCOUNTS")){//未结算显示蓝色
            ((ImageView)holder.getView(R.id.im_iv_state)).setImageResource(R.mipmap.im_bt_no_js);
            ((ImageView)holder.getView(R.id.im_iv_type)).setImageResource(R.mipmap.im_bt_jiaobiao_blue);
            holder.getView(R.id.im_iv_type).setVisibility(View.VISIBLE);
        }else  if(bean.getStatus().equals("NOT_WINNING_THE_PRIZE")){
            ((ImageView)holder.getView(R.id.im_iv_state)).setImageResource(R.mipmap.im_bt_no_win);
            holder.getView(R.id.im_iv_type).setVisibility(View.GONE);
        }else if(bean.getStatus().equals("HAS_WON_THE_PRIZE")){//未中奖红色
            ((ImageView)holder.getView(R.id.im_iv_state)).setImageResource(R.mipmap.im_bt_has_win);
            ((ImageView)holder.getView(R.id.im_iv_type)).setImageResource(R.mipmap.im_bt_jiaobiao_red);
            holder.getView(R.id.im_iv_type).setVisibility(View.VISIBLE);
        }
        String orderId="";
        if(bean.getBettingOrderId()!=null && bean.getBettingOrderId().length()>10){
            orderId=bean.getBettingOrderId().substring(0,6)+"****"+bean.getBettingOrderId().substring(bean.getBettingOrderId().length()-3,bean.getBettingOrderId().length());
        }
        holder.setText(R.id.im_tv_order,"单号："+orderId);
        holder.setText(R.id.im_tv_time, IMTimeData.stampToTime(bean.getTradeTime(),"yyyy-MM-dd HH:mm"));
        holder.setText(R.id.im_tv_money,bean.getInvestAmount());
        holder.setText(R.id.im_tv_name,bean.getGameName());
        for (int i = 0; i < gamesDatas.size(); i++) {
            if(gamesDatas.get(i).getGameName().equals(bean.getGameName())){
                ((ImageView)holder.getView(R.id.im_iv_head)).setImageResource(gamesDatas.get(i).getImageId());
                break;
            }
        }
    }
}