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
import com.android.nettylibrary.greendao.entity.IMPersonBean;
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
public class IMConversationAdapter extends IMBaseRecycleViewAdapter_T<IMPersonBean> {

    private Context context;

    private List<IMPersonBean> datas = new ArrayList<>();


    public IMConversationAdapter(Context context, int layoutId, List<IMPersonBean> datas) {
        super(context, layoutId, datas);
        this.context=context;
        this.datas=datas;
    }

    @Override
    protected void convert(IMBaseViewHolder holder, final int position, IMPersonBean bean) {
        IMPersonBean imConversationBean = datas.get(position);
        if(imConversationBean.getLastMessageTime()!=0){
            String time = IMTimeData.getTimeFormatText(imConversationBean.getLastMessageTime());
            holder.setText(R.id.im_tv_time,time);
        }
        holder.setCircleImageLoader(R.id.im_iv_head,imConversationBean.getAvatar());
        if(!TextUtils.isEmpty(imConversationBean.getNickName())){
            if(imConversationBean.getNickName().length()>11){
                holder.setText(R.id.im_tv_name,imConversationBean.getNickName().substring(0,11)+"..");
            }else {
                holder.setText(R.id.im_tv_name,imConversationBean.getNickName());
            }
        }

        //设置客服在线离线状态(推送客服不存在)
        if(datas.get(position).getStatus().equals("1")){
            holder.getView(R.id.iv_iv_state).setVisibility(View.GONE);
        }else if(datas.get(position).getStatus().equals("2")){
            holder.getView(R.id.iv_iv_state).setVisibility(View.GONE);
            ((ImageView)holder.getView(R.id.iv_iv_state)).setImageResource(R.mipmap.im_chat_offline);
        }else if(datas.get(position).getStatus().equals("4")){
            holder.getView(R.id.iv_iv_state).setVisibility(View.GONE);
            ((ImageView)holder.getView(R.id.iv_iv_state)).setImageResource(R.mipmap.im_chat_leave);
        }

        //设置发送消息的状态
        setSendState(holder,imConversationBean);

        boolean isNotice = IMPreferenceUtil.getPreference_Boolean(bean.getMemberId() + IMSConfig.IM_PERSON_NO_NOTICE,false);
        boolean isSetTop = IMPreferenceUtil.getPreference_Boolean(IMSConfig.IM_IS_SET_TOP+bean.getMemberId() ,false);
        if(isSetTop){
            holder.getView(R.id.im_container).setBackgroundColor(context.getResources().getColor(R.color.color_F6F5FA));
        }else {
            holder.getView(R.id.im_container).setBackgroundColor(context.getResources().getColor(R.color.white));
        }

        if(!isNotice){
            holder.getView(R.id.iv_no_notice).setVisibility(View.GONE);
            //设置未读消息
            setUnReadNumber(holder,imConversationBean);
            holder.getView(R.id.iv_has_message).setVisibility(View.GONE);
        }else {
            holder.getView(R.id.iv_no_notice).setVisibility(View.VISIBLE);
            holder.getView(R.id.im_tv_unread).setVisibility(View.GONE);
            String unReadnumer = IMPreferenceUtil.getPreference_String(imConversationBean.getMemberId() + IMSConfig.IM_UNEREAD_PERSON_CHAT, "0");//把对应人员的未读消息取出
            if(!unReadnumer.equals("0")) {
                holder.getView(R.id.iv_has_message).setVisibility(View.VISIBLE);
            }else {
                holder.getView(R.id.iv_has_message).setVisibility(View.GONE);
            }
        }

        IMMessageDataJson bean1 =null;
        try {
            bean1= new Gson().fromJson(bean.getLastmessage(), IMMessageDataJson.class);
            if(bean1==null){
                return;
            }

            if(Integer.parseInt(bean1.getType())==IMSConfig.IM_MESSAGE_PICTURE){
                holder.setText(R.id.im_tv_content,"[图片]");
            }else {
                MoonUtils.identifyFaceExpression(context,  holder.getView(R.id.im_tv_content), IMSMsgManager.getTextData(imConversationBean.getLastmessage()), ImageSpan.ALIGN_BOTTOM);
            }
        }catch (Exception e){

            String message=bean.getLastmessage();
            if(bean.getLastmessage().equals("暂无消息")){
                if(datas.get(position).getStatus().equals("1")){
                    message="[当前在线]";
                }else if(datas.get(position).getStatus().equals("2")){
                    message="[离线请留言]";
                }else if(datas.get(position).getStatus().equals("4")){
                    message="[忙碌中请稍后]";
                }else {
                    message="[暂无推送消息]";
                }
            }
            MoonUtils.identifyFaceExpression(context,  holder.getView(R.id.im_tv_content), message, ImageSpan.ALIGN_BOTTOM);
        }

    }
    /**
     * 设置未读消息条数
     */
    private void setUnReadNumber(IMBaseViewHolder holder, IMPersonBean imConversationBean) {
        String unReadnumer = IMPreferenceUtil.getPreference_String(imConversationBean.getMemberId() + IMSConfig.IM_UNEREAD_PERSON_CHAT, "0");//把对应人员的未读消息取出
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
     */
    private void setSendState(IMBaseViewHolder holder, IMPersonBean imConversationBean) {
        if(imConversationBean.getLastsendstate()== IM_SEND_STATE_SUCCESS){
            holder.getView(R.id.im_iv_state).setVisibility(View.GONE);

        }else  if(imConversationBean.getLastsendstate()== IM_SEND_STATE_SENDING){
            holder.getView(R.id.im_iv_state).setVisibility(View.VISIBLE);
            ((ImageView)holder.getView(R.id.im_iv_state)).setImageResource(R.mipmap.im_chat_send_state);
        }else if(imConversationBean.getLastsendstate()== IM_SEND_STATE_FAILE){
            holder.getView(R.id.im_iv_state).setVisibility(View.VISIBLE);
            ((ImageView)holder.getView(R.id.im_iv_state)).setImageResource(R.mipmap.im_msg_state_fail_resend_pressed);
        }else {
            holder.getView(R.id.im_iv_state).setVisibility(View.GONE);
        }
    }


}