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
import android.text.TextUtils;
import android.text.style.ImageSpan;
import android.view.View;
import android.widget.ImageView;

import com.android.im.IMSMsgManager;
import com.android.im.R;
import com.android.im.imadapter.baseadapter.IMBaseRecycleViewAdapter_T;
import com.android.im.imadapter.baseadapter.IMBaseViewHolder;
import com.android.im.imemoji.MoonUtils;
import com.android.nettylibrary.IMSConfig;
import com.android.nettylibrary.greendao.entity.IMGroupBean;
import com.android.nettylibrary.http.IMMessageDataJson;
import com.android.nettylibrary.utils.IMPreferenceUtil;
import com.android.nettylibrary.utils.IMTimeData;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import static com.android.nettylibrary.IMSConfig.IM_SEND_STATE_FAILE;
import static com.android.nettylibrary.IMSConfig.IM_SEND_STATE_SENDING;
import static com.android.nettylibrary.IMSConfig.IM_SEND_STATE_SUCCESS;


/**
 * 会话界面
 */
public class IMGroupAdapter extends IMBaseRecycleViewAdapter_T<IMGroupBean> {

    private Context context;

    private List<IMGroupBean> datas = new ArrayList<>();


    public IMGroupAdapter(Context context, int layoutId, List<IMGroupBean> datas) {
        super(context, layoutId, datas);
        this.context=context;
        this.datas=datas;
    }

    @Override
    protected void convert(IMBaseViewHolder holder, final int position, IMGroupBean bean) {
        IMGroupBean imConversationBean = datas.get(position);
        if(imConversationBean.getLastMessageTime()!=0){
            String time = IMTimeData.getTimeFormatText(imConversationBean.getLastMessageTime());
            holder.setText(R.id.im_tv_time,time);
        }
        holder.setCircleImageLoader(R.id.im_iv_head,imConversationBean.getGroupAvatar());

        if(!TextUtils.isEmpty(datas.get(position).getGroupCharacteristic()) && datas.get(position).getShowCharacteristic().equals("Y") ){
            holder.setImageLoader(R.id.im_iv_leve,datas.get(position).getGroupCharacteristic());
        }
        if(!TextUtils.isEmpty(imConversationBean.getGroupName())){
            if(imConversationBean.getGroupName().length()>11){
                holder.setText(R.id.im_tv_name,imConversationBean.getGroupName().substring(0,11)+"..");
            }else {
                holder.setText(R.id.im_tv_name,imConversationBean.getGroupName());
            }
        }
        //判断是不是有人@我
        boolean hasOneAtme = IMPreferenceUtil.getPreference_Boolean(imConversationBean.getGroupId() + IMSConfig.IM_SOMEONE_AT_ME, false);
        if(hasOneAtme){
            holder.getView(R.id.im_tv_at).setVisibility(View.VISIBLE);
        }else {
            holder.getView(R.id.im_tv_at).setVisibility(View.GONE);
        }

        //设置发送消息的状态
        setSendState(holder,imConversationBean);
        boolean isNotice = IMPreferenceUtil.getPreference_Boolean(bean.getGroupId() + IMSConfig.IM_GROUP_NO_NOTICE,false);
        if(!isNotice){
            holder.getView(R.id.iv_no_notice).setVisibility(View.GONE);
            //设置未读消息
            setUnReadNumber(holder,imConversationBean);
            holder.getView(R.id.iv_has_message).setVisibility(View.GONE);
        }else {
            holder.getView(R.id.iv_no_notice).setVisibility(View.VISIBLE);
            holder.getView(R.id.im_tv_unread).setVisibility(View.GONE);
            String unReadnumer = IMPreferenceUtil.getPreference_String(imConversationBean.getGroupId() + IMSConfig.IM_UNEREAD_GROUP_CHAT, "0");//把对应人员的未读消息取出
            if(!unReadnumer.equals("0")) {
                holder.getView(R.id.iv_has_message).setVisibility(View.VISIBLE);
            }else {
                holder.getView(R.id.iv_has_message).setVisibility(View.GONE);
            }
        }
        if(TextUtils.isEmpty(bean.getLastMessage())){
            MoonUtils.identifyFaceExpression(context,  holder.getView(R.id.im_tv_content), "", ImageSpan.ALIGN_BOTTOM);
            holder.setText(R.id.im_tv_time,"");
            return;
        }
        MoonUtils.identifyFaceExpression(context,  holder.getView(R.id.im_tv_content), IMSMsgManager.getTextData(bean.getLastMessage()), ImageSpan.ALIGN_BOTTOM);

    }
    /**
     * 设置未读消息条数
     */
    private void setUnReadNumber(IMBaseViewHolder holder, IMGroupBean imConversationBean) {
        String unReadnumer = IMPreferenceUtil.getPreference_String(imConversationBean.getGroupId() + IMSConfig.IM_UNEREAD_GROUP_CHAT, "0");//把对应人员的未读消息取出
        if(!unReadnumer.equals("0")){
            if(Integer.parseInt(unReadnumer)>99){
                unReadnumer="99+";
                holder.getView(R.id.im_tv_unread).setVisibility(View.GONE);
                holder.getView(R.id.im_tv_unread_1).setVisibility(View.VISIBLE);
                holder.setText(R.id.im_tv_unread_1,unReadnumer);
            }else {
                holder.getView(R.id.im_tv_unread).setVisibility(View.VISIBLE);
                holder.getView(R.id.im_tv_unread_1).setVisibility(View.GONE);
                holder.setText(R.id.im_tv_unread,unReadnumer);
            }
        }else {
            holder.getView(R.id.im_tv_unread).setVisibility(View.INVISIBLE);
            holder.getView(R.id.im_tv_unread_1).setVisibility(View.GONE);
        }
    }
    /**
     * 设置发送消息的状态
     * -收到服务器消息，messagege=type: TOKEN
     * responseStatus: FAIL
     */
    private void setSendState(IMBaseViewHolder holder, IMGroupBean imConversationBean) {
        if(imConversationBean.getLastSendstate()== IM_SEND_STATE_SUCCESS){
            holder.getView(R.id.im_iv_state).setVisibility(View.GONE);

        }else  if(imConversationBean.getLastSendstate()== IM_SEND_STATE_SENDING){
            holder.getView(R.id.im_iv_state).setVisibility(View.VISIBLE);
            ((ImageView)holder.getView(R.id.im_iv_state)).setImageResource(R.mipmap.im_chat_send_state);
        }else if(imConversationBean.getLastSendstate()== IM_SEND_STATE_FAILE) {
            holder.getView(R.id.im_iv_state).setVisibility(View.VISIBLE);
            ((ImageView)holder.getView(R.id.im_iv_state)).setImageResource(R.mipmap.im_msg_state_fail_resend_pressed);
        }else {
            holder.getView(R.id.im_iv_state).setVisibility(View.GONE);
        }
    }


}