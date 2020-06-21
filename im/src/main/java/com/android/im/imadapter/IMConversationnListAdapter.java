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

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.im.IMSManager;
import com.android.im.IMSMsgManager;
import com.android.im.R;
import com.android.im.imadapter.baseadapter.IMBaseRecycleViewAdapter_T;
import com.android.im.imeventbus.IMMessageNoticelGroupEvevt;
import com.android.im.imeventbus.IMMessageNoticelPersonEvevt;
import com.android.im.imui.activity.IMAddFriendActivity;
import com.android.im.imui.activity.IMCreatGroupActivity;
import com.android.im.imui.activity.IMQRCActivity;
import com.android.im.imutils.IMImageLoadUtil;
import com.android.im.imutils.IMStopClickFast;
import com.android.im.imview.IMDrawableCenterTextView;
import com.android.im.imview.dialog.IMDialogUtils;
import com.android.im.imview.impopwindow.IMPopWindowUtil;
import com.android.nettylibrary.IMSConfig;
import com.android.nettylibrary.greendao.entity.IMConversationBean;
import com.android.nettylibrary.greendao.entity.IMGroupBean;
import com.android.nettylibrary.greendao.entity.IMPersonBean;
import com.android.nettylibrary.greendao.greenutils.DaoUtils;
import com.android.nettylibrary.http.IMMessageDataJson;
import com.android.nettylibrary.utils.IMNetworkUtil;
import com.android.nettylibrary.utils.IMPreferenceUtil;
import com.android.nettylibrary.utils.IMTimeData;
import com.blankj.utilcode.util.KeyboardUtils;
import com.blankj.utilcode.util.LogUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.gson.Gson;
import com.mcxtzhang.swipemenulib.SwipeMenuLayout;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import q.rorbin.badgeview.Badge;
import q.rorbin.badgeview.QBadgeView;

import static com.android.nettylibrary.IMSConfig.IM_SEND_STATE_FAILE;
import static com.android.nettylibrary.IMSConfig.IM_SEND_STATE_SENDING;
import static com.android.nettylibrary.IMSConfig.IM_SEND_STATE_SUCCESS;

/**
 * 会话界面
 */
public class IMConversationnListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements View.OnClickListener,  IMPopWindowUtil.OnViewItemClickListeners {
    private Context context;
    private LayoutInflater mLayoutInflater;
    private List<IMConversationBean> datas = new ArrayList<>();
    public static final int MSG_TYPE_ONE = 0;//标题
    public static final int MSG_TYPE_TWO = 1;//内容
    public static final int MSG_TYPE_THREE = 2;//空布局

    public static final int CLICK_TYPE_DELETE = 0;//删除
    public static final int CLICK_TYPE_TOP = 1;//置顶
    public static final int CLICK_TYPE_NOTICE = 2;//通知
    public static final int CLICK_TYPE_SEARCH = 3;//通知
    private IMBaseRecycleViewAdapter_T.OnItemClickListner onItemClickListner;//单击事件
    private SwipeMenuLayout mSmContainer;  //全局删除OnViewItemListener
    private boolean showchoose = false;  //是不是显示置顶和提醒
    private ImageView mIvTopAdd; //全局add
    private Animation anim;
    private Animation anim1;
    private Handler handler=new Handler();
    private boolean isSearch=false;

    public void setOnItemClickListner(IMBaseRecycleViewAdapter_T.OnItemClickListner onItemClickListner) {
        this.onItemClickListner = onItemClickListner;
    }

    public IMConversationnListAdapter(Context context, List<IMConversationBean> datas) {
        this.context = context;
        this.datas = datas;
        mLayoutInflater = LayoutInflater.from(context);
        IMPopWindowUtil.getInstance(context).setOnViewItemListeners(this);
        initAnim();

    }

    /**
     * 初始化旋转动画
     */
    private void initAnim() {
        anim = AnimationUtils.loadAnimation(context, R.anim.anim_rotate_45);
        anim.setFillAfter(true);
        anim1 = AnimationUtils.loadAnimation(context, R.anim.anim_rotate_90);
        anim1.setFillAfter(true);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = null;
        RecyclerView.ViewHolder holder = null;
        switch (viewType) {
            case MSG_TYPE_ONE:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_im_conversation_head, parent, false);
                holder = new TypeOneHolder(view);
                break;
            case MSG_TYPE_TWO:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_im_item_conversation, parent, false);
                holder = new TypeTwoHolder(view);
                break;
            case MSG_TYPE_THREE:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.empty_chat, parent, false);
                holder = new TypeThreeHolder(view);
                break;
        }
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        int itemViewType = getItemViewType(position);
        switch (itemViewType) {
            case MSG_TYPE_ONE:
                HandleTitlMessageData((TypeOneHolder) holder);
                break;
            case MSG_TYPE_TWO:
                IMConversationBean tbub = datas.get(position - 1);
                HandleMessageData((TypeTwoHolder) holder, tbub, position);
                break;
        }
    }

    @Override
    public int getItemCount() {
        if(datas.size()==0&&!isSearch){
            return datas.size() + 2;
        }else {
            return datas.size() + 1;
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return MSG_TYPE_ONE;
        } else if (datas.size() == 0&&!isSearch) {
            return MSG_TYPE_THREE;
        } else {
            return MSG_TYPE_TWO;
        }

    }

    /**
     * 是否是搜索
     * @param isSearch
     */
    public void setType(boolean isSearch) {
        this.isSearch=isSearch;
    }


    class TypeOneHolder extends RecyclerView.ViewHolder {
        private ImageView mIvadd;
        private TextView mTvTitle;
        private TextView mTvbanJi;
        private EditText mTvSerch;
        private RelativeLayout mllleft;
        private RelativeLayout mllright;
        private LinearLayout llSearch;

        public TypeOneHolder(View view) {
            super(view);
            mTvbanJi = view.findViewById(R.id.im_iv_back);
            mTvTitle = view.findViewById(R.id.im_tv_title);
            mIvadd = view.findViewById(R.id.im_iv_add);
            mllleft = view.findViewById(R.id.ll_left);
            mllright = view.findViewById(R.id.ll_right);
            mTvSerch = view.findViewById(R.id.im_tv_serch);
            llSearch = view.findViewById(R.id.ll_search);
        }
    }

    class TypeTwoHolder extends RecyclerView.ViewHolder {
        private ImageView mIvHead;
        private ImageView mIvHasMsg;
        private ImageView mIvState;
        private ImageView mIvLeve;
        private ImageView im_iv_state;
        private RelativeLayout mIvChoosed;
        private ImageView mIvNoNotice;
        private ImageView mIvReaded;
        private TextView mTvname;
        private TextView mTvTime;
        private TextView mTvState;
        private TextView mTvAt;
        private TextView mTvContent;
        private TextView mTvUnread;
        private LinearLayout mllContainer;
        private SwipeMenuLayout mSmContainer;
        private Badge badge;
        private IMDrawableCenterTextView mTvTop;
        private IMDrawableCenterTextView mTvbtnUnRead;
        private IMDrawableCenterTextView mTvDelete;

        public TypeTwoHolder(View view) {
            super(view);
            mIvHead = view.findViewById(R.id.im_iv_head);
            mIvHasMsg = view.findViewById(R.id.iv_has_message);
            mIvState = view.findViewById(R.id.iv_iv_state);
            mTvname = view.findViewById(R.id.im_tv_name);
            mIvLeve = view.findViewById(R.id.im_iv_leve);
            mTvTime = view.findViewById(R.id.im_tv_time);
            mTvState = view.findViewById(R.id.im_tv_state);
            mTvAt = view.findViewById(R.id.im_tv_at);
            im_iv_state = view.findViewById(R.id.im_iv_state);
            mTvContent = view.findViewById(R.id.im_tv_content);
            mllContainer = view.findViewById(R.id.im_container);
            mTvUnread = view.findViewById(R.id.im_tv_unread);
            mIvNoNotice = view.findViewById(R.id.iv_no_notice);
            mIvChoosed = view.findViewById(R.id.im_iv_choosed);
            mSmContainer = view.findViewById(R.id.ll_im_container);
            mIvReaded = view.findViewById(R.id.im_iv_readed);
            mTvTop = view.findViewById(R.id.btnTop);
            mTvbtnUnRead = view.findViewById(R.id.btnUnRead);
            mTvDelete = view.findViewById(R.id.btnDelete);

            badge = new QBadgeView(context).bindTarget(itemView.findViewById(R.id.im_tv_unread));
            badge.setBadgeGravity(Gravity.CENTER | Gravity.END);
            badge.setBadgeTextSize(10, true);
            badge.setBadgePadding(4, true);

        }
    }

    class TypeThreeHolder extends RecyclerView.ViewHolder {
        public TypeThreeHolder(View view) {
            super(view);
        }
    }


    /**
     * 处理第1种数据类型
     */
    private TextView mTvBanJi, mTvTitle;

    private void HandleTitlMessageData(TypeOneHolder holder) {
        mIvTopAdd = holder.mIvadd;
        mTvBanJi = holder.mTvbanJi;
        mTvTitle = holder.mTvTitle;
        if (!IMNetworkUtil.isConnected()) {
            mTvTitle.setText("未连接");
        }
        holder.mTvSerch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (onClickListener != null) {
                    onClickListener.onClick(CLICK_TYPE_SEARCH, 0, s.toString().trim());
                }
            }
        });
        holder.mllleft.setOnClickListener(this);
        holder.mllright.setOnClickListener(this);
        KeyboardUtils.registerSoftInputChangedListener((Activity) context, height -> {
            if (height == 0) {
                if (holder.mTvSerch.getText().toString().trim().length() == 0) {
                    int newHeight = LinearLayout.LayoutParams.WRAP_CONTENT;
                    LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) holder.mTvSerch.getLayoutParams();
                    lp.width = newHeight;
                    holder.mTvSerch.setLayoutParams(lp);
                }
                holder.mTvSerch.clearFocus();
            } else {
                int newHeight = LinearLayout.LayoutParams.MATCH_PARENT;
                //注意这里，到底是用ViewGroup还是用LinearLayout或者是FrameLayout，主要是看你这个EditTex
                //控件所在的父控件是啥布局，如果是LinearLayout，那么这里就要改成LinearLayout.LayoutParams
                LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) holder.mTvSerch.getLayoutParams();
                lp.width = newHeight;
                holder.mTvSerch.setLayoutParams(lp);
            }
        });
        holder.llSearch.setOnClickListener(v -> KeyboardUtils.showSoftInput(holder.mTvSerch));
        LogUtils.i("WOLF","isSearch:"+isSearch);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if(isSearch){
                    KeyboardUtils.showSoftInput(holder.mTvSerch);
                    isSearch=!isSearch;
                }
            }
        },50);

        KeyboardUtils.hideSoftInput(holder.mTvSerch);

    }

    public void setTitle(String title) {
        if (mTvTitle != null) {
            mTvTitle.setText(title);
        }
    }

    public String getTitle() {
        if (mTvTitle != null) {
            return mTvTitle.getText().toString();
        }
        return "会话";
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.im_tv_serch) {
            Toast.makeText(context, "查询", Toast.LENGTH_SHORT).show();
        }
        else if (v.getId() == R.id.ll_right) {
            IMPopWindowUtil.getInstance(context).showPopListViews(mIvTopAdd);
            mIvTopAdd.startAnimation(anim);
            if (mTvBanJi.getText().equals("完成")) {
                setBianJi();
            }
        } else if (v.getId() == R.id.ll_left) {
            if (mTvBanJi.getText().equals("编辑")) {
                mTvBanJi.setText("完成");
                notifyDataSetChanged();
                showchoose = true;
            } else {
                mTvBanJi.setText("编辑");
                showchoose = false;
                notifyDataSetChanged();
            }
        }
    }

    public void setBianJi() { mTvBanJi.setText("编辑");
        showchoose = false;
        notifyDataSetChanged();

    }

    public void setShowChoose(boolean isShow) {
        showchoose = isShow;
    }
    /**
     * 处理第二种数据类型
     */

    private void HandleMessageData(TypeTwoHolder holder, IMConversationBean imConversationBean, int position) {
        //时间
        if (imConversationBean.getLastMessageTime() != 0) {
            String time = IMTimeData.getTimeFormatText(imConversationBean.getLastMessageTime());
            holder.mTvTime.setText(time);
        }
        //设置发送消息的状态
        setSendState(holder, imConversationBean);
        //点击编辑和不编辑
        if (showchoose) {
            holder.mllContainer.setEnabled(false);
            holder.mIvChoosed.setVisibility(View.VISIBLE);
            holder.mTvTop.setVisibility(View.GONE);
            holder.mTvbtnUnRead.setVisibility(View.GONE);
            holder.mSmContainer.setSwipeEnable(false);
            holder.mIvChoosed.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    IMDialogUtils.getInstance().showCommonDialog(context, "删除后，将清空该聊天的消息记录", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            IMDialogUtils.getInstance().dismissCommonDiglog();
                            if (onClickListener != null) {
                                onClickListener.onClick(CLICK_TYPE_DELETE, position, null);
                            }
                        }
                    });
                }
            });
        } else {
            holder.mSmContainer.setSwipeEnable(true);
            holder.mllContainer.setEnabled(true);
            holder.mIvChoosed.setVisibility(View.GONE);
            holder.mTvTop.setVisibility(View.VISIBLE);
            holder.mTvbtnUnRead.setVisibility(View.VISIBLE);
        }
        //点击item
        holder.mllContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemClickListner != null) {
                    onItemClickListner.onItemClickListner(v, position, null);
                }
            }
        });

        //点击删除
        holder.mTvDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(IMStopClickFast.isFastClick()) {
                    IMDialogUtils.getInstance().showCommonDialog(context, "删除后，将清空该聊天的消息记录，是否确认删除", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            IMDialogUtils.getInstance().dismissCommonDiglog();
                            IMPreferenceUtil.setPreference_Boolean(datas.get(position - 1).getConversationId() + IMSConfig.IM_CONVERSATION_ZHI_DING, false);
                            holder.badge.setBadgeNumber(0);
                            if (onClickListener != null) {
                                onClickListener.onClick(CLICK_TYPE_DELETE, position, null);
                            }
                        }
                    });
                }
            }
        });
        //点击关闭通知
        holder.mTvbtnUnRead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (IMStopClickFast.isFastClick()) {
                    boolean isNoticed = IMPreferenceUtil.getPreference_Boolean(datas.get(position - 1).getConversationId() + IMSConfig.IM_CONVERSATION_NO_NOTICE, false);
                    if (isNoticed) {
                        holder.mTvbtnUnRead.setText("关闭通知");
                        IMPreferenceUtil.setPreference_Boolean(datas.get(position - 1).getConversationId() + IMSConfig.IM_CONVERSATION_NO_NOTICE, false);
                        notifyItemChanged(position);
                    } else {
                        holder.mTvbtnUnRead.setText("打开通知");
                        IMPreferenceUtil.setPreference_Boolean(datas.get(position - 1).getConversationId() + IMSConfig.IM_CONVERSATION_NO_NOTICE, true);
                        notifyItemChanged(position);
                    }
                    IMSManager.getInstance().getUnreadMessageNumber();
                }
            }
        });
        //点击置顶
        holder.mTvTop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(IMStopClickFast.isFastClick()){
                    boolean isSetTop = IMPreferenceUtil.getPreference_Boolean(datas.get(position - 1).getConversationId() + IMSConfig.IM_CONVERSATION_ZHI_DING, false);
                    IMConversationBean bean = DaoUtils.getInstance().queryConversationBean(datas.get(position - 1).getConversationId());
                    holder.mSmContainer.smoothClose();
                    if (isSetTop) {
                        holder.mTvbtnUnRead.setText("置顶");
                        IMPreferenceUtil.setPreference_Boolean(datas.get(position - 1).getConversationId() + IMSConfig.IM_CONVERSATION_ZHI_DING, false);
                        IMPreferenceUtil.setPreference_Boolean(IMSConfig.IM_IS_SET_TOP + datas.get(position - 1).getConversationId(), true);
                        IMPreferenceUtil.setPreference_String(IMSConfig.IM_SET_TOP_TIME + datas.get(position - 1).getConversationId(), System.currentTimeMillis() + "");
                        bean.setIsSetTop(false);
                        bean.setSetTopTime(System.currentTimeMillis());
                    } else {
                        holder.mTvbtnUnRead.setText("取消置顶");
                        IMPreferenceUtil.setPreference_Boolean(datas.get(position - 1).getConversationId() + IMSConfig.IM_CONVERSATION_ZHI_DING, true);
                        IMPreferenceUtil.setPreference_Boolean(IMSConfig.IM_IS_SET_TOP + datas.get(position - 1).getConversationId(), false);
                        IMPreferenceUtil.setPreference_String(IMSConfig.IM_SET_TOP_TIME + datas.get(position - 1).getConversationId(), System.currentTimeMillis() + "");
                        bean.setIsSetTop(true);
                        bean.setSetTopTime(System.currentTimeMillis());
                    }
                    DaoUtils.getInstance().updateConversationData(bean);
                    if(onClickListener!=null){
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                onClickListener.onClick(CLICK_TYPE_TOP,position,null);
                            }
                        },500);
                    }
                }
            }
        });

        //在线离线
//        holder.mIvState.setVisibility(View.GONE);//群默认没有
//        if (!TextUtils.isEmpty(datas.get(position - 1).getMemberId())) {//如果有个人id  说明是单人会话 需要小时在线还是离线
//            String memberId = datas.get(position - 1).getMemberId();
//            IMPersonBean bean = DaoUtils.getInstance().queryMessageBean(memberId);
//            if (bean != null && !TextUtils.isEmpty(bean.getIsOnline())) {
//                if (bean.getIsOnline().equals("Y")) {
//                    holder.mIvState.setVisibility(View.GONE);
//                    holder.mIvState.setImageResource(R.mipmap.im_chat_leave);
//                } else {
//                    holder.mIvState.setVisibility(View.GONE);
//                    holder.mIvState.setImageResource(R.mipmap.im_chat_offline);
//                }
//            }
//        }

        //屏蔽好友功能
        holder.mIvState.setVisibility(View.GONE);//群默认没有
        if (!TextUtils.isEmpty(datas.get(position - 1).getMemberId())) {//如果有个人id  说明是单人会话
            String memberId = datas.get(position - 1).getMemberId();
            boolean isforbit = IMPreferenceUtil.getPreference_Boolean(memberId + IMSConfig.IM_CONVERSATION_TOUSU, false);
            if (isforbit) {
                holder.mIvState.setImageResource(R.mipmap.im_forbit_1);
                holder.mIvState.setVisibility(View.VISIBLE);
            } else {
                holder.mIvState.setVisibility(View.GONE);
            }
        }

            //已读未读(每次从数据库查询已读状态)
            IMConversationBean bean = DaoUtils.getInstance().queryConversationBean(imConversationBean.getConversationId());
            if (bean != null) {
                if (TextUtils.isEmpty(bean.getIsReaded()) || TextUtils.isEmpty(datas.get(position - 1).getMemberId())) {
                    holder.mIvReaded.setVisibility(View.GONE);
                } else if (bean.getIsReaded().equals("1")) {
                    holder.mIvReaded.setVisibility(View.VISIBLE);
                    holder.mIvReaded.setImageResource(R.mipmap.chat_cg);
                } else if (bean.getIsReaded().equals("2")) {
                    holder.mIvReaded.setVisibility(View.VISIBLE);
                    holder.mIvReaded.setImageResource(R.mipmap.chat_yd);
                }
            }

            //头像和姓名
            String headurl = imConversationBean.getConversationavatar();
            String name = imConversationBean.getConversationName();
            //单人从单人数据库里面取
            if (TextUtils.isEmpty(imConversationBean.getGroupId())) {
                holder.mIvLeve.setVisibility(View.GONE);
                IMPersonBean bean1 = DaoUtils.getInstance().queryMessageBean(imConversationBean.getConversationId());
                if (bean1 != null) {
                    headurl = bean1.getAvatar();
                    name = bean1.getNickName();
                }
                if(imConversationBean.getConversationId().equals(IMSConfig.CLX_ID)){//如果是彩乐信团队  加载本地头像
                    Glide.with(context).load(R.mipmap.im_clx_group).diskCacheStrategy(DiskCacheStrategy.ALL).dontAnimate().into(holder.mIvHead);
                }else {
                    IMImageLoadUtil.ImageLoadCircle(context, headurl, holder.mIvHead);
                }

            } else {
                holder.mIvLeve.setVisibility(View.VISIBLE);
                IMGroupBean groupBean = DaoUtils.getInstance().queryGroupBean(imConversationBean.getConversationId());
                if (groupBean != null) {
                    headurl = groupBean.getGroupAvatar();
                    name = groupBean.getGroupName();
                }
                IMImageLoadUtil.ImageLoadCircle(context, headurl, holder.mIvHead);
            }

            if (!TextUtils.isEmpty(name)) {
                if (name.length() > 11) {
                    holder.mTvname.setText(name.substring(0, 11) + "..");
                } else {
                    holder.mTvname.setText(name);
                }
            }
            //判断是不是有人@我>
            boolean hasOneAtme = IMPreferenceUtil.getPreference_Boolean(imConversationBean.getConversationId() + IMSConfig.IM_SOMEONE_AT_ME, false);
            if (hasOneAtme) {
                holder.mTvAt.setVisibility(View.VISIBLE);

            } else {
                holder.mTvAt.setVisibility(View.GONE);
            }

            //是不是置顶，是不是取消通知
            boolean isNoticed = IMPreferenceUtil.getPreference_Boolean(datas.get(position - 1).getConversationId() + IMSConfig.IM_CONVERSATION_NO_NOTICE, false);
            boolean isSetTop = IMPreferenceUtil.getPreference_Boolean(datas.get(position - 1).getConversationId() + IMSConfig.IM_CONVERSATION_ZHI_DING, false);
            if (isSetTop) {
                holder.mllContainer.setBackgroundColor(context.getResources().getColor(R.color.color_F6F5FA));
                holder.mTvTop.setText("取消置顶");
            } else {
                holder.mllContainer.setBackgroundColor(context.getResources().getColor(R.color.white));
                holder.mTvTop.setText("置顶");
            }
            if (!isNoticed) { //没有打开
                holder.mTvbtnUnRead.setText("关闭通知");
                holder.mIvHasMsg.setVisibility(View.GONE);
                holder.mIvNoNotice.setVisibility(View.GONE);
                //设置未读消息
                setUnReadNumber(holder, imConversationBean,true);
            } else {
                holder.mTvbtnUnRead.setText("打开通知");
                holder.mIvNoNotice.setVisibility(View.VISIBLE);
                String unReadnumer = IMPreferenceUtil.getPreference_String(imConversationBean.getGroupId() + IMSConfig.IM_UNEREAD_CONVERSATION_CHAT, "0");//把对应人员的未读消息取出
                //设置未读消息
                setUnReadNumber(holder, imConversationBean,false);
            }

            if (TextUtils.isEmpty(imConversationBean.getLastMessageContent())) {
                holder.mTvTime.setText("");
                holder.mTvContent.setText("");
                return;
            }

            try{//群聊要判断是哪个人发的消息
                if (!TextUtils.isEmpty(imConversationBean.getGroupId())) {
                    IMMessageDataJson dataJson = new Gson().fromJson(imConversationBean.getLastMessageContent(), IMMessageDataJson.class);
                    if (!TextUtils.isEmpty(dataJson.getNickName())) {
                        holder.mTvContent.setText(dataJson.getNickName() + " : " + IMSMsgManager.getTextData(imConversationBean.getLastMessageContent()));
                    }else {
                        holder.mTvContent.setText(IMSMsgManager.getTextData(imConversationBean.getLastMessageContent()));
                    }
                }else {
                    holder.mTvContent.setText(IMSMsgManager.getTextData(imConversationBean.getLastMessageContent()));
                }
            }catch (Exception e){
                holder.mTvContent.setText(IMSMsgManager.getTextData(imConversationBean.getLastMessageContent()));
            }
        }

        /**
         * 设置未读消息条数
         */
        private void setUnReadNumber(TypeTwoHolder holder, IMConversationBean imConversationBean,boolean isred) {
            String unReadnumer = IMPreferenceUtil.getPreference_String(imConversationBean.getConversationId() + IMSConfig.IM_UNEREAD_CONVERSATION_CHAT, "0");//把对应人员的未读消息取出
            if (!unReadnumer.equals("0")) {
                holder.mIvReaded.setVisibility(View.GONE);
                holder.mTvUnread.setVisibility(View.GONE);
                if (Integer.parseInt(unReadnumer) > 99) {
                    unReadnumer = "99+";
                    holder.badge.setBadgeText(unReadnumer);
                } else {
                    holder.badge.setBadgeText(unReadnumer);
                }
            }else {
                holder.badge.setBadgeNumber(0);
            }
            holder.badge.setOnDragStateChangedListener(new Badge.OnDragStateChangedListener() {
                @Override
                public void onDragStateChanged(int dragState, Badge badge, View targetView) {
                    if (dragState == STATE_SUCCEED) {
                        IMPreferenceUtil.setPreference_String(imConversationBean.getConversationId() + IMSConfig.IM_UNEREAD_CONVERSATION_CHAT, "0");
                        IMSManager.getInstance().getUnreadMessageNumber();
                    }
                }
            });
            if(isred){
                holder.badge.setBadgeBackgroundColor(context.getResources().getColor(R.color.red7));
            }else {
                holder.badge.setBadgeBackgroundColor(context.getResources().getColor(R.color.color_adadad));
            }
        }
        /**
         * 设置发送消息的状态
         * -收到服务器消息，messagege=type: TOKEN
         * responseStatus: FAIL
         */
        private void setSendState(TypeTwoHolder holder, IMConversationBean imConversationBean) {
            if (imConversationBean.getLastMessageSendstate() == IM_SEND_STATE_SUCCESS) {
                holder.im_iv_state.setVisibility(View.GONE);
            } else if (imConversationBean.getLastMessageSendstate() == IM_SEND_STATE_SENDING) {
                holder.im_iv_state.setVisibility(View.VISIBLE);
                holder.im_iv_state.setImageResource(R.mipmap.im_chat_send_state);
            } else if (imConversationBean.getLastMessageSendstate() == IM_SEND_STATE_FAILE) {
                holder.im_iv_state.setVisibility(View.VISIBLE);
                holder.im_iv_state.setImageResource(R.mipmap.im_msg_state_fail_resend_pressed);
            } else {
                holder.im_iv_state.setVisibility(View.GONE);
            }

            if(imConversationBean.getLastMessageSendType()==IMSConfig.MSG_RECIEVE){
                holder.im_iv_state.setVisibility(View.GONE);
            }
        }

        @Override
        public void OnViewItemListeners(int position) {
            switch (position) {
                case 0:
                    Intent intent = new Intent(context, IMCreatGroupActivity.class);
                    context.startActivity(intent);
                    break;
                case 1:
                    Intent intent1 = new Intent(context, IMAddFriendActivity.class);
                    context.startActivity(intent1);
                    break;
                case 2:
                    Intent intent2 = new Intent(context, IMQRCActivity.class);
                    context.startActivity(intent2);
                    break;
                case 3:
                    IMDialogUtils.getInstance().showCommonDialog(context, "您是否确认要把所有消息置为已读状态？", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            IMDialogUtils.getInstance().dismissCommonDiglog();
                            IMPopWindowUtil.getInstance(context).setTotleRed();   //更新数据库未读消息
                            EventBus.getDefault().post(new IMMessageNoticelGroupEvevt());
                            EventBus.getDefault().post(new IMMessageNoticelPersonEvevt());
                            IMSManager.getInstance().getUnreadMessageNumber();
                        }
                    });
                    break;
                case 4:   //代表加号消失
                    if (mIvTopAdd == null) {
                        return;
                    }
                    mIvTopAdd.startAnimation(anim1);
                    break;
            }

        }


        public interface onClickListener {
            void onClick(int type, int position, String content);
        }

        private onClickListener onClickListener;

        public void setOnClickListener(IMConversationnListAdapter.onClickListener onClickListener) {
            this.onClickListener = onClickListener;
        }
    }