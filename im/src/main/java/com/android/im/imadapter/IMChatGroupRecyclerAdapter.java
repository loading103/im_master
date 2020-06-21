package com.android.im.imadapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.media.AudioManager;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Handler;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.text.style.ImageSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.android.im.IMSManager;
import com.android.im.IMSMsgManager;
import com.android.im.R;
import com.android.im.imbean.IMBetDetailBean;
import com.android.im.imbean.IMBetGetBean;
import com.android.im.imbean.IMGameTypeBean;
import com.android.im.imbean.IMGetRedPickBean;
import com.android.im.imbean.IMGroupOutBean;
import com.android.im.imbean.IMImageInfo;
import com.android.im.imbean.IMRedPickInformationBean;
import com.android.im.imbean.SmallProgramBean;
import com.android.im.imemoji.MoonUtils;
import com.android.im.imnet.IMHttpResultObserver;
import com.android.im.imnet.http.IMHttpsService;
import com.android.im.imui.activity.IMGroupChatActivity;
import com.android.im.imui.activity.IMMemberInforActivity;
import com.android.im.imui.activity.IMNewFriendInforlActivity;
import com.android.im.imui.activity.IMPersonalChatActivity;
import com.android.im.imui.activity.IMPhotoViewActivity;
import com.android.im.imui.activity.IMRedPickDetailActivity;
import com.android.im.imui.activity.IMSSimplePlayerActivity;
import com.android.im.imui.activity.IMSmalSharePersonActivity;
import com.android.im.imui.activity.SmallProgramActivity;
import com.android.im.imutils.IMImageLoadUtil;
import com.android.im.imutils.IMPersonUtils;
import com.android.im.imutils.IMStopClickFast;
import com.android.im.imview.IMRoundAngleImageView;
import com.android.im.imview.dialog.IMShowFollowDiglog;
import com.android.im.imview.imredpickedview.CustomDialog;
import com.android.im.imview.imredpickedview.OnRedPacketDialogClickListener;
import com.android.im.imview.imredpickedview.RedPacketEntity;
import com.android.im.imview.imredpickedview.RedPacketViewHolder;
import com.android.nettylibrary.IMSConfig;
import com.android.nettylibrary.greendao.entity.IMConversationBean;
import com.android.nettylibrary.greendao.entity.IMConversationDetailBean;
import com.android.nettylibrary.greendao.entity.IMGroupMemberBean;
import com.android.nettylibrary.greendao.entity.IMPersonBean;
import com.android.nettylibrary.greendao.greenutils.DaoUtils;
import com.android.nettylibrary.http.IMMessageDataJson;
import com.android.nettylibrary.http.IMSystemSomeOneAddGroup;
import com.android.nettylibrary.utils.IMDensityUtil;
import com.android.nettylibrary.utils.IMLogUtil;
import com.android.nettylibrary.utils.IMPreferenceUtil;
import com.android.nettylibrary.utils.IMTimeData;
import com.blankj.utilcode.util.SizeUtils;
import com.blankj.utilcode.util.TimeUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import cc.shinichi.library.ImagePreview;
import cc.shinichi.library.bean.ImageInfo;
import cc.shinichi.library.view.listener.OnBigImageLongClickListener;
import okhttp3.MediaType;
import okhttp3.RequestBody;

import static com.android.nettylibrary.IMSConfig.MSG_CHAT_GROUP;
import static com.android.nettylibrary.IMSConfig.MSG_LEFT_AUDIO;
import static com.android.nettylibrary.IMSConfig.MSG_LEFT_FOLLOW;
import static com.android.nettylibrary.IMSConfig.MSG_LEFT_IMG;
import static com.android.nettylibrary.IMSConfig.MSG_LEFT_REDCARD;
import static com.android.nettylibrary.IMSConfig.MSG_LEFT_REPACKED;
import static com.android.nettylibrary.IMSConfig.MSG_LEFT_SMALL;
import static com.android.nettylibrary.IMSConfig.MSG_LEFT_TEXT;
import static com.android.nettylibrary.IMSConfig.MSG_LEFT_VIDEO;
import static com.android.nettylibrary.IMSConfig.MSG_RIGHT_AUDIO;
import static com.android.nettylibrary.IMSConfig.MSG_RIGHT_FOLLOW;
import static com.android.nettylibrary.IMSConfig.MSG_RIGHT_IMG;
import static com.android.nettylibrary.IMSConfig.MSG_RIGHT_REDCARD;
import static com.android.nettylibrary.IMSConfig.MSG_RIGHT_REPACKED;
import static com.android.nettylibrary.IMSConfig.MSG_RIGHT_SMALL;
import static com.android.nettylibrary.IMSConfig.MSG_RIGHT_TEXT;
import static com.android.nettylibrary.IMSConfig.MSG_RIGHT_VIDEO;
/**
 * 聊天的对话界面
 */

public class IMChatGroupRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context context;
    private List<IMConversationDetailBean> messagelist = new ArrayList<>();
    private List<IMGameTypeBean> gamesDatas = new ArrayList<>();
    public Handler handler;
    private LayoutInflater mLayoutInflater;
    private Animation an;
    private IMConversationBean customer;
    private DaoUtils daoUtils;
    private IMShowFollowDiglog imShowFollowDiglog;
    private CustomDialog mRedPacketDialog;
    private View mRedPacketDialogView;
    private RedPacketViewHolder mRedPacketViewHolder;
    private String name;
    private String title;
    private String level;
    private String headurl;
    private final String types;
    private MediaPlayer mediaPlayer;
    private AnimationDrawable animationDrawable;
    private String  groupId;

    public IMChatGroupRecyclerAdapter(Context context, List<IMConversationDetailBean> messagelist, IMConversationBean customer) {
        this.context = context;
        mLayoutInflater = LayoutInflater.from(context);
        handler = new Handler();
        this.customer=customer;
        this.messagelist=messagelist;
        gamesDatas= IMGameTypeBean.getGameBeans();
        daoUtils=DaoUtils.getInstance();
        imShowFollowDiglog =new IMShowFollowDiglog(context);
        name = IMPreferenceUtil.getPreference_String(IMSConfig.SAVE_NAME, "");
        title = IMPreferenceUtil.getPreference_String(IMSConfig.SAVE_TITLE, "");
        level = IMPreferenceUtil.getPreference_String(IMSConfig.SAVE_LEVEL, "");
        headurl = IMPreferenceUtil.getPreference_String(IMSConfig.SAVE_HEADURL, "");
        types = IMPreferenceUtil.getPreference_String(IMSConfig.SAVE_USERTYPE, "0");
        this.groupId=((IMGroupChatActivity)context).getGroupId();
    }
    /**
     * 不同类型的布局(同一种消息分左右两种不同的布局)
     */
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = null;
        RecyclerView.ViewHolder holder = null;
        switch (viewType) {
            case MSG_LEFT_TEXT:      //文字类型
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_im_left_text, parent, false);
                holder = new TextLeftViewHolder(view);
                break;
            case MSG_RIGHT_TEXT:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_im_right_text, parent, false);
                holder = new TextRightViewHolder(view);
                break;
            case MSG_LEFT_IMG:       //图片类型
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_im_left_img, parent, false);
                holder = new ImageLeftViewHolder(view);
                break;
            case MSG_RIGHT_IMG:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_im_right_img, parent, false);
                holder = new ImageRightViewHolder(view);
                break;
            case MSG_LEFT_VIDEO:      //视屏类型
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_im_left_video, parent, false);
                holder = new VideoLeftViewHolder(view);
                break;
            case MSG_RIGHT_VIDEO:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_im_right_video, parent, false);
                holder = new VideoRightViewHolder(view);
                break;
            case MSG_LEFT_REPACKED:     //红包消息
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_im_left_redpack, parent, false);
                holder = new RedPickLeftViewHolder(view);
                break;
            case MSG_RIGHT_REPACKED:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_im_right_redpack, parent, false);
                holder = new RedPickRightViewHolder(view);
                break;
            case MSG_LEFT_FOLLOW:       //跟投消息
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_im_left_follow, parent, false);
                holder = new FollowLeftViewHolder(view);
                break;
            case MSG_RIGHT_FOLLOW:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_im_right_follow, parent, false);
                holder = new FollowRightViewHolder(view);
                break;
            case MSG_LEFT_REDCARD:      //红单消息
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_im_left_redcard, parent, false);
                holder = new RedCardLeftViewHolder(view);
                break;
            case MSG_RIGHT_REDCARD:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_im_right_redcard, parent, false);
                holder = new RedCardRightViewHolder(view);
                break;
            case MSG_CHAT_GROUP:        //系统自定义消息
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_im_join_group, parent, false);
                holder = new ChatGroupViewHolder(view);
                break;

            case MSG_LEFT_AUDIO:       //音频类型
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_im_left_audio, parent, false);
                holder = new AudioLeftViewHolder(view);
                break;
            case MSG_RIGHT_AUDIO:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_im_right_audio, parent, false);
                holder = new AudioRightViewHolder(view);
                break;
            case MSG_LEFT_SMALL:       //音频类型
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_im_left_share_small, parent, false);
                holder = new ShareSmallLeftViewHolder(view);
                break;
            case MSG_RIGHT_SMALL:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_im_right_share_small, parent, false);
                holder = new ShareSmallRightViewHolder(view);
                break;
        }
        return holder;
    }

    /**
     * 第1种类型（文字左边）
     */
    class TextLeftViewHolder extends BaseLeftViewHolder {
        private TextView mContent;
        private LinearLayout mllText;
        public TextLeftViewHolder(View view) {
            super(view);
            mContent =  view.findViewById(R.id.im_tv_content);
            mllText =  view.findViewById(R.id.ll_text);
        }
    }
    /**
     * 第2种类型（文字右边）
     */
    class TextRightViewHolder extends BaseRightViewHolder {
        private TextView mContent;
        private LinearLayout mllText;
        public TextRightViewHolder(View view) {
            super(view);
            mContent =  view.findViewById(R.id.im_tv_content);
            mllText =  view.findViewById(R.id.ll_text);
        }
    }
    /**
     *第3种类型（图片左边）
     */
    class ImageLeftViewHolder extends BaseLeftViewHolder {
        private ImageView mImageView;

        public ImageLeftViewHolder(View view) {
            super(view);
            mImageView =  view.findViewById(R.id.im_iv_image);
        }
    }
    /**
     * 第4种类型（图片右边）
     */
    class ImageRightViewHolder extends BaseRightViewHolder {
        private ImageView mImageView;
        public ImageRightViewHolder(View view) {
            super(view);
            mImageView =  view.findViewById(R.id.im_iv_image);
        }
    }
    /**
     * 第5种类型（视频左边）
     */
    class VideoLeftViewHolder extends BaseLeftViewHolder {
        private ImageView mImageView;
        public VideoLeftViewHolder(View view) {
            super(view);
            mImageView =  view.findViewById(R.id.image_message);
        }
    }
    /**
     * 第6种类型（视频右边）
     */
    class VideoRightViewHolder extends BaseRightViewHolder {
        private ImageView mImageView;

        public VideoRightViewHolder(View view) {
            super(view);
            mImageView =  view.findViewById(R.id.image_message);
        }
    }
    /**
     * 第7种（红包左边）
     */
    class RedPickLeftViewHolder extends BaseLeftViewHolder {
        private LinearLayout mllRed;
        private ImageView mIvRedli;
        private TextView mTvContent;
        private TextView mTvType;
        public RedPickLeftViewHolder(View view) {
            super(view);
            mTvContent = view.findViewById(R.id.im_tv_content);
            mTvType = view.findViewById(R.id.im_tv_type);
            mllRed = view.findViewById(R.id.im_ll_red);
            mIvRedli = view.findViewById(R.id.im_red_li);
        }
    }
    /**
     * 第8种（红包右边）
     */
    class  RedPickRightViewHolder extends BaseRightViewHolder {
        //内容
        private LinearLayout mllRed;
        private ImageView mIvRedli;
        private TextView mTvContent;
        private TextView mTvType;
        public RedPickRightViewHolder(View view) {
            super(view);
            mTvContent = view.findViewById(R.id.im_tv_content);
            mTvType = view.findViewById(R.id.im_tv_type);
            mllRed = view.findViewById(R.id.im_ll_red);
            mIvRedli = view.findViewById(R.id.im_red_li);
        }
    }
    /**
     * 第9种（跟投左边）
     */
    class FollowLeftViewHolder extends BaseLeftViewHolder {
        private ImageView mIvContentHead;
        private TextView mTvContentName;
        private TextView mTvContentQh;
        private TextView mTvContentPlay;
        private TextView mTvContents;
        private TextView mTvContentMoney;
        private TextView mTvContentFollow;
        public FollowLeftViewHolder(View view) {
            super(view);
            mIvContentHead = view.findViewById(R.id.im_content_head);
            mTvContentName = view.findViewById(R.id.im_content_name);
            mTvContentQh = view.findViewById(R.id.im_content_qh);
            mTvContentPlay = view.findViewById(R.id.im_content_play);
            mTvContents = view.findViewById(R.id.im_content_content);
            mTvContentMoney = view.findViewById(R.id.im_content_money);
            mTvContentFollow = view.findViewById(R.id.im_content_follow);
        }
    }
    /**
     * 第10种（跟投右边）
     */
    class FollowRightViewHolder extends BaseRightViewHolder {
        private ImageView mIvContentHead;
        private TextView mTvContentName;
        private TextView mTvContentQh;
        private TextView mTvContentPlay;
        private TextView mTvContents;
        private TextView mTvContentMoney;
        private TextView mTvContentFollow;
        public FollowRightViewHolder(View view) {
            super(view);
            mIvContentHead = view.findViewById(R.id.im_content_head);
            mTvContentName = view.findViewById(R.id.im_content_name);
            mTvContentQh = view.findViewById(R.id.im_content_qh);
            mTvContentPlay = view.findViewById(R.id.im_content_play);
            mTvContents = view.findViewById(R.id.im_content_content);
            mTvContentMoney = view.findViewById(R.id.im_content_money);
            mTvContentFollow = view.findViewById(R.id.im_content_follow);
        }
    }
    /**
     * 第11种（红单左边）
     */
    class RedCardLeftViewHolder extends BaseLeftViewHolder {

        private TextView mIvContentType;
        private TextView mTvContentQh;
        private TextView mTvContentBet;
        private TextView mTvContentWin;
        private TextView mTvContentYl;
        public RedCardLeftViewHolder(View view) {
            super(view);
            mIvContentType = view.findViewById(R.id.im_content_type);
            mTvContentQh = view.findViewById(R.id.im_content_qh);
            mTvContentBet = view.findViewById(R.id.im_content_bet);
            mTvContentWin = view.findViewById(R.id.im_content_win);
            mTvContentYl = view.findViewById(R.id.im_content_yl);
        }
    }
    /**
     * 第12种（红单右边）
     */
    class  RedCardRightViewHolder extends BaseRightViewHolder {
        private TextView mIvContentType;
        private TextView mTvContentQh;
        private TextView mTvContentBet;
        private TextView mTvContentWin;
        private TextView mTvContentYl;
        public RedCardRightViewHolder(View view) {
            super(view);
            mIvContentType = view.findViewById(R.id.im_content_type);
            mTvContentQh = view.findViewById(R.id.im_content_qh);
            mTvContentBet = view.findViewById(R.id.im_content_bet);
            mTvContentWin = view.findViewById(R.id.im_content_win);
            mTvContentYl = view.findViewById(R.id.im_content_yl);
        }
    }
    /**
     * 第12种（消息体）
     */
    class  ChatGroupViewHolder extends RecyclerView.ViewHolder {
        private TextView mTvChatTime;
        private TextView mTvContent;
        public ChatGroupViewHolder(View view) {
            super(view);
            mTvChatTime =  view.findViewById(R.id.im_chat_time);
            mTvContent =  view.findViewById(R.id.im_tv_content);
        }
    }
    /**
     * 第13种类型（音频左边）
     */
    class AudioLeftViewHolder extends BaseLeftViewHolder {
        private TextView mContent;
        private ImageView mIvAudio;
        private LinearLayout mllAudio;
        private LinearLayout mllView;
        private ImageView ivRed;
        public AudioLeftViewHolder(View view) {
            super(view);
            mContent =  view.findViewById(R.id.im_tv_content);
            mIvAudio =  view.findViewById(R.id.im_iv_audio);
            mllAudio =  view.findViewById(R.id.im_ll_audio);
            mllView =  view.findViewById(R.id.im_ll_view);
            ivRed =  view.findViewById(R.id.iv_red);
        }
    }
    /**
     * 第14种类型（音频右边）
     */
    class AudioRightViewHolder extends BaseRightViewHolder {
        private TextView mContent;
        private ImageView mIvAudio;
        private LinearLayout mllAudio;
        private LinearLayout mllView;
        public AudioRightViewHolder(View view) {
            super(view);
            mContent =  view.findViewById(R.id.im_tv_content);
            mIvAudio =  view.findViewById(R.id.im_iv_audio);
            mllAudio =  view.findViewById(R.id.im_ll_audio);
            mllView =  view.findViewById(R.id.im_ll_view);
        }
    }
    /**
     * 第16种类型（分享小程序）
     */
    class ShareSmallLeftViewHolder extends BaseLeftViewHolder {
        private TextView ImTvtitle;
        private TextView ImTvname;
        private IMRoundAngleImageView mIvImage;
        private ImageView mIvLogo;
        public ShareSmallLeftViewHolder(View view) {
            super(view);
            ImTvtitle =  view.findViewById(R.id.im_title);
            ImTvname =  view.findViewById(R.id.im_name);
            mIvImage =  view.findViewById(R.id.im_image);
            mIvLogo =  view.findViewById(R.id.im_logo);
        }
    }
    /**
     * 第17种类型（分享小程序）
     */
    class ShareSmallRightViewHolder extends BaseRightViewHolder {
        private TextView ImTvtitle;
        private TextView ImTvname;
        private ImageView mIvLogo;
        private IMRoundAngleImageView mIvImage;
        public ShareSmallRightViewHolder(View view) {
            super(view);
            ImTvtitle =  view.findViewById(R.id.im_title);
            ImTvname =  view.findViewById(R.id.im_name);
            mIvImage =  view.findViewById(R.id.im_image);
            mIvLogo =  view.findViewById(R.id.im_logo);
        }
    }
    @SuppressLint("NewApi")
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        IMConversationDetailBean tbub = messagelist.get(position);
        int itemViewType = getItemViewType(position);
        switch (itemViewType) {
            case MSG_LEFT_TEXT:
                TextLeftLayout((TextLeftViewHolder) holder, tbub, position);
                break;
            case MSG_RIGHT_TEXT:
                TextRightLayout((TextRightViewHolder) holder, tbub, position);
                break;
            case MSG_LEFT_IMG:
                ImageLeftLayout((ImageLeftViewHolder) holder, tbub, position);
                break;
            case MSG_RIGHT_IMG:
                ImageRightLayout((ImageRightViewHolder) holder, tbub, position);
                break;
            case MSG_LEFT_VIDEO:
                VideoLeftLayout((VideoLeftViewHolder) holder, tbub, position);
                break;
            case MSG_RIGHT_VIDEO:
                VideoRightLayout((VideoRightViewHolder) holder, tbub, position);
                break;
            case MSG_LEFT_FOLLOW:
                FollowLeftLayout((FollowLeftViewHolder) holder, tbub, position);
                break;
            case MSG_RIGHT_FOLLOW:
                FollowRightLayout((FollowRightViewHolder) holder, tbub, position);
                break;
            case MSG_LEFT_REDCARD:
                RedCardLeftLayout((RedCardLeftViewHolder) holder, tbub, position);
                break;
            case MSG_RIGHT_REDCARD:
                RedCardRightLayout((RedCardRightViewHolder) holder, tbub, position);
                break;
            case MSG_LEFT_REPACKED:
                RedPickLeftLayout((RedPickLeftViewHolder) holder, tbub, position);
                break;
            case MSG_RIGHT_REPACKED:
                RedPickRightLayout((RedPickRightViewHolder) holder, tbub, position);
                break;
            case MSG_CHAT_GROUP:
                ChatGroupLayout((ChatGroupViewHolder) holder, tbub, position);
                break;
            case MSG_LEFT_AUDIO:
                AudioLeftLayout((AudioLeftViewHolder) holder, tbub, position);
                break;
            case MSG_RIGHT_AUDIO:
                AudioRightLayout((AudioRightViewHolder) holder, tbub, position);
                break;
            case MSG_LEFT_SMALL:
                ShareSmallLeftLayout((ShareSmallLeftViewHolder) holder, tbub, position);
                break;
            case MSG_RIGHT_SMALL:
                ShareSmallRightLayout((ShareSmallRightViewHolder) holder, tbub, position);
                break;


        }
    }

    @Override
    public int getItemViewType(int position) {
        return Handlemessage(messagelist.get(position));
    }

    @Override
    public int getItemCount() {
        return messagelist.size();
    }
    /**
     * 处理第1种类型数据
     */
    private void TextLeftLayout(final TextLeftViewHolder holder, final IMConversationDetailBean tbub, final int position) {
        handlerLeftCommonViewdata(holder.mllUser,holder.mTvChatTime,tbub,holder.mIvLevel,holder.mIvHeadView, holder.mTvTitle ,holder.mllTitle, holder.mTvName,holder.mIvHeadView,position,holder.mllitem);
        MoonUtils.identifyFaceExpression(context,  holder.mContent,IMSMsgManager.getTextData(tbub.getData()), ImageSpan.ALIGN_BOTTOM);
        holder.tvTime.setText(TimeUtils.millis2String(tbub.getTimestamp(),new SimpleDateFormat("HH:mm")));
        holder.tvTime2.setText(TimeUtils.millis2String(tbub.getTimestamp(),new SimpleDateFormat("HH:mm")));

        TextPaint textPaint = holder.mContent.getPaint();
        IMMessageDataJson bean = new Gson().fromJson(tbub.getData(),IMMessageDataJson.class);
        float textPaintWidth = textPaint.measureText(bean.getText());

        if(SizeUtils.px2dp(textPaintWidth)<160&&!tbub.getData().contains("\\n")){
            holder.tvTime.setVisibility(View.VISIBLE);
            holder.tvTime2.setVisibility(View.GONE);
        }else {
            holder.tvTime.setVisibility(View.GONE);
            holder.tvTime2.setVisibility(View.VISIBLE);
        }

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                showPopupWindow(1,holder.mllText,holder.mContent,position,tbub);
                return false;
            }
        });
    }

    /**
     * 处理第2种类型数据
     */
    private void TextRightLayout(final TextRightViewHolder holder, final IMConversationDetailBean tbub, final int position) {
        handlerRightCommonViewdata( holder.mllUser,holder.mTvChatTime,tbub,holder.mIvLevel, holder.mTvTitle,holder.mllTitle, holder.mTvName,holder.mIvHeadView,position,holder.mllitem);
        MoonUtils.identifyFaceExpression(context,  holder.mContent, IMSMsgManager.getTextData(tbub.getData()), ImageSpan.ALIGN_BOTTOM);
        //发送状态的变化
        sendStateChange( holder.mIvFail,tbub,position);
        holder.tvTime.setText(TimeUtils.millis2String(tbub.getTimestamp(),new SimpleDateFormat("HH:mm")));
        holder.tvTime2.setText(TimeUtils.millis2String(tbub.getTimestamp(),new SimpleDateFormat("HH:mm")));

        TextPaint textPaint = holder.mContent.getPaint();
        IMMessageDataJson bean = new Gson().fromJson(tbub.getData(),IMMessageDataJson.class);
        float textPaintWidth = textPaint.measureText(bean.getText());

        if(SizeUtils.px2dp(textPaintWidth)<160&&!tbub.getData().contains("\\n")){
            holder.tvTime.setVisibility(View.VISIBLE);
            holder.tvTime2.setVisibility(View.GONE);
        }else {
            holder.tvTime.setVisibility(View.GONE);
            holder.tvTime2.setVisibility(View.VISIBLE);
        }
        holder.tvTime.setCompoundDrawablesWithIntrinsicBounds(null,
                null, null, null);
        holder.tvTime2.setCompoundDrawablesWithIntrinsicBounds(null,
                null, null, null);

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                showPopupWindow(1,holder.mllText,holder.mContent,position,tbub);
                return false;
            }
        });
    }

    /**
     *处理第3种类型数据
     */
    private void ImageLeftLayout(final ImageLeftViewHolder holder, final IMConversationDetailBean tbub, final int position) {
        handlerLeftCommonViewdata(holder.mllUser,holder.mTvChatTime,tbub,holder.mIvLevel,holder.mIvHeadView, holder.mTvTitle ,holder.mllTitle, holder.mTvName,holder.mIvHeadView,position,holder.mllitem);
        //内容
        final IMMessageDataJson bean = new Gson().fromJson(tbub.getData(),IMMessageDataJson.class);
        if(bean.getThumbImages()==null ||bean.getThumbImages().size()==0){
            return;
        }
        //处理自适应图片大小
        String url = (String) bean.getThumbImages().get(0);
        IMImageLoadUtil.glideLoadUrl(context,url, holder.mImageView,messagelist,position);
        holder.mImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPhoto(customer.getGroupId(),tbub);
            }
        });
        holder.tvTime.setText(TimeUtils.millis2String(tbub.getTimestamp(),new SimpleDateFormat("HH:mm")));
        holder.tvTime.setCompoundDrawablesWithIntrinsicBounds(null,
                null, null, null);
        holder.mImageView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                showPopupWindow(2,holder.mImageView,holder.mImageView,position,tbub);
                return false;
            }
        });
    }

    /**
     * 处理第4种类型数据
     */
    private void ImageRightLayout(final ImageRightViewHolder holder, final IMConversationDetailBean tbub, final int position) {
        handlerRightCommonViewdata( holder.mllUser,holder.mTvChatTime,tbub,holder.mIvLevel, holder.mTvTitle,holder.mllTitle, holder.mTvName,holder.mIvHeadView,position,holder.mllitem);
        //内容
        final IMMessageDataJson bean = new Gson().fromJson(tbub.getData(), IMMessageDataJson.class);
        //本地是否有对应的本地图片
        boolean isownurl=!TextUtils.isEmpty(bean.getOwnerurl()) && new File(bean.getOwnerurl()).exists();
        if((bean.getThumbImages()==null ||bean.getThumbImages().size()==0) && !isownurl){
            return;
        }
        //处理自适应图片大小
        String url=null;
        if(isownurl){
            url=bean.getOwnerurl();
        } else if(!isownurl &&  bean!=null && bean.getThumbImages()!=null && bean.getThumbImages().size()>0) {
            url = (String) bean.getThumbImages().get(0);
        }else {
            url="";
        }
        IMImageLoadUtil.glideLoadUrl(context, url, holder.mImageView, messagelist, position);
        holder.mImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPhoto(customer.getGroupId(),tbub);
            }
        });
        //发送状态的变化
        sendStateChange(holder.mIvFail, tbub, position);
        holder.tvTime.setText(TimeUtils.millis2String(tbub.getTimestamp(),new SimpleDateFormat("HH:mm")));
        holder.tvTime.setCompoundDrawablesWithIntrinsicBounds(null,
                null, null, null);
        holder.mImageView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                showPopupWindow(2,holder.mImageView,holder.mImageView,position,tbub);
                return false;
            }
        });
    }
    /**
     *
     * 处理第5种类型数据
     */
    private void VideoLeftLayout(final VideoLeftViewHolder holder, final IMConversationDetailBean tbub, final int position) {
        handlerLeftCommonViewdata(holder.mllUser,holder.mTvChatTime,tbub,holder.mIvLevel,holder.mIvHeadView, holder.mTvTitle ,holder.mllTitle, holder.mTvName,holder.mIvHeadView,position,holder.mllitem);
        //内容
        final IMMessageDataJson bean = new Gson().fromJson(tbub.getData(),IMMessageDataJson.class);
        IMImageLoadUtil.CommonImageLoadCp(context,bean.getFirstFrame(),holder.mImageView);
        holder.mImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context, IMSSimplePlayerActivity.class);
                intent.putExtra("videobean",bean);
                context.startActivity(intent);
            }
        });
        holder.mImageView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                showPopupWindow(3,holder.mImageView,holder.mImageView,position,tbub);
                return false;
            }
        });
    }
    /**
     *处理第6种类型数据
     */
    private void VideoRightLayout(final VideoRightViewHolder holder, final IMConversationDetailBean tbub, final int position) {
        handlerRightCommonViewdata( holder.mllUser,holder.mTvChatTime,tbub,holder.mIvLevel, holder.mTvTitle,holder.mllTitle, holder.mTvName,holder.mIvHeadView,position,holder.mllitem);
        //内容
        final IMMessageDataJson bean = new Gson().fromJson(tbub.getData(),IMMessageDataJson.class);
        //如果有本地缩略图显示缩略图，没有的显示网络生成的缩略图，还没有的(发送的时候)就根据url生成缩略图
        boolean isownfirstframe = !TextUtils.isEmpty(tbub.getOwnfirstFrame()) && new File(tbub.getOwnfirstFrame()).exists();
        boolean isownurl = !TextUtils.isEmpty(tbub.getOwnurl()) && new File(tbub.getOwnurl()).exists();
        if(isownfirstframe) {
            IMImageLoadUtil.CommonImageLoadCp(context, tbub.getOwnfirstFrame(), holder.mImageView);
        } else if(!isownfirstframe && !TextUtils.isEmpty(bean.getFirstFrame())) {
            IMImageLoadUtil.CommonImageLoadCp(context, bean.getFirstFrame(), holder.mImageView);
        } else if(!isownfirstframe && TextUtils.isEmpty(bean.getFirstFrame()) && !TextUtils.isEmpty(tbub.getOwnurl())&& new File(tbub.getOwnurl()).exists()) {
            MediaMetadataRetriever media = new MediaMetadataRetriever();
            media.setDataSource(tbub.getOwnurl());
            Bitmap bitmap = media.getFrameAtTime();
            Glide.with(context)
                    .load(bitmap)
                    .placeholder(R.mipmap.im_icon_stub_loading)
                    .error(R.mipmap.im_icon_stub)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .centerCrop()
                    .into(holder.mImageView);
        }else {
            IMImageLoadUtil.CommonImageLoadCp(context, "", holder.mImageView);
        }
        holder.mImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context, IMSSimplePlayerActivity.class);
                intent.putExtra("videobean",bean);
                if(isownurl){
                    intent.putExtra("ownerurl",tbub.getOwnurl());
                }
                context.startActivity(intent);
            }
        });
        //发送状态的变化
        sendStateChange(holder.mIvFail, tbub, position);
        holder.tvTime.setText(TimeUtils.millis2String(tbub.getTimestamp(),new SimpleDateFormat("HH:mm")));
        holder.tvTime.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
        holder.mImageView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                showPopupWindow(3,holder.mImageView,holder.mImageView,position,tbub);
                return false;
            }
        });
    }
    /**
     * 处理第7种类型数据
     */
    private void FollowLeftLayout(final FollowLeftViewHolder holder, final IMConversationDetailBean tbub, final int position) {
        handlerLeftCommonViewdata(holder.mllUser,holder.mTvChatTime,tbub,holder.mIvLevel,holder.mIvHeadView, holder.mTvTitle ,holder.mllTitle, holder.mTvName,holder.mIvHeadView,position,holder.mllitem);
        //内容
        final IMMessageDataJson bean = new Gson().fromJson(tbub.getData(),IMMessageDataJson.class);
        holder.mTvContents.setText(bean.getContent());
        holder.mTvContentName.setText(bean.getGameName());
        holder.mTvContentQh.setText(bean.getNum());
        holder.mTvContentPlay.setText(bean.getPlayMethod());
        holder.mTvContentMoney.setText(bean.getAmount());
        if(TextUtils.isEmpty(bean.getSealingTime())|| Long.parseLong(bean.getSealingTime())<System.currentTimeMillis()){
            holder.mTvContentFollow.setText("已封盘");
            holder.mTvContentFollow.setBackgroundColor(context.getResources().getColor(R.color.color_D1D1D1));
        }else {
            holder.mTvContentFollow.setText("立即跟投");
            holder.mTvContentFollow.setBackground(context.getResources().getDrawable(R.drawable.im_shape_follow));
        }
        holder.mTvContentFollow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(IMStopClickFast.isFastClick()){
                    if(types.equals("7")){
                        Toast.makeText(context, "你还未进行登录，请先登录", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if(TextUtils.isEmpty(bean.getSealingTime())|| Long.parseLong(bean.getSealingTime())<System.currentTimeMillis()){
                        return;
                    }
                    showFollowBetting(bean);
                }
            }
        });
        for (int i = 0; i < gamesDatas.size(); i++) {
            if(gamesDatas.get(i).getGameName().equals(bean.getGameName())){
                holder.mIvContentHead.setImageResource(gamesDatas.get(i).getImageId());
                break;
            }
        }
    }
    /**
     * 处理第8种类型数据
     */
    private void FollowRightLayout(final FollowRightViewHolder holder, final IMConversationDetailBean tbub, final int position) {
        handlerRightCommonViewdata( holder.mllUser,holder.mTvChatTime,tbub,holder.mIvLevel, holder.mTvTitle,holder.mllTitle, holder.mTvName,holder.mIvHeadView,position,holder.mllitem);
        //内容
        final IMMessageDataJson bean = new Gson().fromJson(tbub.getData(),IMMessageDataJson.class);
        holder.mTvContents.setText(bean.getContent());
        holder.mTvContentName.setText(bean.getGameName());

        holder.mTvContentQh.setText(bean.getNum());
        holder.mTvContentPlay.setText(bean.getPlayMethod());
        holder.mTvContentMoney.setText(bean.getAmount());
        if(TextUtils.isEmpty(bean.getSealingTime())|| Long.parseLong(bean.getSealingTime())<System.currentTimeMillis()){
            holder.mTvContentFollow.setText("已封盘");
            holder.mTvContentFollow.setBackgroundColor(context.getResources().getColor(R.color.color_D1D1D1));
        }else {
            holder.mTvContentFollow.setText("立即跟投");
            holder.mTvContentFollow.setBackground(context.getResources().getDrawable(R.drawable.im_shape_follow));
        }
        holder.mTvContentFollow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(types.equals("7")){
                    Toast.makeText(context, "你还未进行登录，登录之后才能跟投", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(TextUtils.isEmpty(bean.getSealingTime())|| Long.parseLong(bean.getSealingTime())<System.currentTimeMillis()){
                    return;
                }
                showFollowBetting(bean);
            }
        });
        for (int i = 0; i < gamesDatas.size(); i++) {
            if(gamesDatas.get(i).getGameName().equals(bean.getGameName())){
                holder.mIvContentHead.setImageResource(gamesDatas.get(i).getImageId());
                break;
            }
        }
        //发送状态的变化
        sendStateChange(holder.mIvFail, tbub, position);
    }
    /**
     * 处理第9种类型数据
     */
    private void RedCardLeftLayout(final RedCardLeftViewHolder holder, final IMConversationDetailBean tbub, final int position) {
        handlerLeftCommonViewdata(holder.mllUser,holder.mTvChatTime,tbub,holder.mIvLevel,holder.mIvHeadView, holder.mTvTitle ,holder.mllTitle, holder.mTvName,holder.mIvHeadView,position,holder.mllitem);
        final IMMessageDataJson bean = new Gson().fromJson(tbub.getData(),IMMessageDataJson.class);
        holder.mIvContentType.setText(bean.getGameName());
        holder.mTvContentQh.setText(bean.getNum());
        holder.mTvContentBet.setText(bean.getBetAmount());
        holder.mTvContentWin.setText(bean.getWinAmount());
        holder.mTvContentYl.setText(bean.getGainAmount());
    }
    /**
     * 处理第10种
     */
    private void RedCardRightLayout(final RedCardRightViewHolder holder, final IMConversationDetailBean tbub, final int position) {
        handlerRightCommonViewdata( holder.mllUser,holder.mTvChatTime,tbub,holder.mIvLevel, holder.mTvTitle,holder.mllTitle, holder.mTvName,holder.mIvHeadView,position,holder.mllitem);
        //内容
        IMMessageDataJson bean = new Gson().fromJson(tbub.getData(),IMMessageDataJson.class);
        holder.mIvContentType.setText(bean.getGameName());
        holder.mTvContentQh.setText(bean.getNum());
        holder.mTvContentBet.setText(bean.getBetAmount());
        holder.mTvContentWin.setText(bean.getWinAmount());
        holder.mTvContentYl.setText(bean.getGainAmount());
        //发送状态的变化
        sendStateChange(holder.mIvFail, tbub, position);
    }
    /**
     * 处理第11种类型数据
     */
    private void RedPickLeftLayout(RedPickLeftViewHolder holder, final IMConversationDetailBean tbub, final int position) {
        handlerLeftCommonViewdata(holder.mllUser,holder.mTvChatTime,tbub,holder.mIvLevel,holder.mIvHeadView, holder.mTvTitle ,holder.mllTitle, holder.mTvName,holder.mIvHeadView,position,holder.mllitem);
        //内容
        if(messagelist.get(position).getGetredpick()){
            holder.mIvRedli.setImageResource(R.mipmap.im_chat_redpack_open);
            holder.mllRed.setBackground(context.getResources().getDrawable(R.mipmap.im_chat_left_redbao_open));
        }else {
            holder.mIvRedli.setImageResource(R.mipmap.im_chat_redpack);
            holder.mllRed.setBackground(context.getResources().getDrawable(R.mipmap.im_chat_left_redbao));
        }
        IMMessageDataJson bean = new Gson().fromJson(tbub.getData(),IMMessageDataJson.class);
        if(!TextUtils.isEmpty(bean.getTitle())){
            if(bean.getTitle().length()>11){
                holder.mTvContent.setText(bean.getTitle().substring(0,11)+"..");
            }else {
                holder.mTvContent.setText(bean.getTitle());
            }
        }
        holder.mllRed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(IMStopClickFast.isFastClick()){
                    if(types.equals("7")){
                        Toast.makeText(context, "你还未进行登录，登录之后才能领取红包", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    IMMessageDataJson bean = new Gson().fromJson(tbub.getData(),IMMessageDataJson.class);
                    getRedPickHttpData(bean,position,messagelist.get(position).getGetredpick());
                }
            }
        });
    }
    /**
     * 处理第12种类型数据(红包是后台发过来的所以没有状态)
     */
    private void RedPickRightLayout(final RedPickRightViewHolder holder, final IMConversationDetailBean tbub, final int position) {

        handlerRightCommonViewdata( holder.mllUser,holder.mTvChatTime,tbub,holder.mIvLevel, holder.mTvTitle,holder.mllTitle, holder.mTvName,holder.mIvHeadView,position,holder.mllitem);
        //内容
        if(messagelist.get(position).getGetredpick()){
            holder.mIvRedli.setImageResource(R.mipmap.im_chat_redpack_open);
            holder.mllRed.setBackground(context.getResources().getDrawable(R.mipmap.im_chat_right_redbao_open));
        }else {
            holder.mIvRedli.setImageResource(R.mipmap.im_chat_redpack);
            holder.mllRed.setBackground(context.getResources ().getDrawable(R.mipmap.im_chat_right_redbao));
        }

        IMMessageDataJson bean = new Gson().fromJson(tbub.getData(),IMMessageDataJson.class);
        if(!TextUtils.isEmpty(bean.getTitle())){
            if(bean.getTitle().length()>11){
                holder.mTvContent.setText(bean.getTitle().substring(0,11)+"..");
            }else {
                holder.mTvContent.setText(bean.getTitle());
            }
        }
        holder.mllRed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(IMStopClickFast.isFastClick()) {
                    if(types.equals("7")){
                        Toast.makeText(context, "你还未进行登录，登录之后才能领取红包", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    IMMessageDataJson bean = new Gson().fromJson(tbub.getData(), IMMessageDataJson.class);
                    getRedPickHttpData(bean, position,messagelist.get(position).getGetredpick());
                }
            }
        });
        //发送状态的变化
        sendStateChange(holder.mIvFail, tbub, position);
    }
    /**
     * 处理第13种类型数据
     */
    @RequiresApi(api = Build.VERSION_CODES.M)
    @SuppressLint("NewApi")
    private void ChatGroupLayout(final ChatGroupViewHolder holder, final IMConversationDetailBean tbub, final int position) {
        handleTimeContent(holder.mTvChatTime,tbub.getTimestamp(),position);
        IMMessageDataJson bean = new Gson().fromJson(tbub.getData(), IMMessageDataJson.class);
        if(TextUtils.isEmpty(bean.getSysCode()) ){
            holder.mTvContent.setText(bean.getText());
        }else {
            switch (Integer.parseInt(bean.getSysCode())){
                case IMSConfig.IM_MESSAGE_JOINGROUP:   //有人进入了群聊
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        handleJoinGroup(bean,holder.mTvContent);
                    }
                    break;
                case IMSConfig.IM_MESSAGE_KIOUTGROUP:   //有人被移除了群聊
                    handleRemoveGroup(bean,holder.mTvContent,true);
                    break;
                case IMSConfig.IM_MESSAGE_QUITGROUP:   //有人退出了群聊
                    handleRemoveGroup(bean,holder.mTvContent,false);
                    break;
                case IMSConfig.IM_MESSAGE_BLACK:     //被拉黑
                case IMSConfig.IM_MESSAGE_NO_SPEAK:   //被拉黑
                case IMSConfig.IM_MESSAGE_NO_BLACK:   //被拉黑
                case IMSConfig.IM_MESSAGE_CAN_SPEAK:   //被拉黑
                    if(bean.getText().equals("解除禁言")){
                        holder.mTvContent.setText("你已被解除禁言");
                    }else  if(bean.getText().equals("禁言")){
                        holder.mTvContent.setText("你已被禁言");
                    }else  if(bean.getText().equals("拉入黑名单")){
                        holder.mTvContent.setText("你已被拉入黑名单");
                    }else  if(bean.getText().equals("解除黑名单")){
                        holder.mTvContent.setText("你已被解除黑名单");
                    }
                    break;
                case IMSConfig.IM_MESSAGE_KIOUTSELF:   //有人被移除了群聊
                    holder.mTvContent.setText("你已被移除群聊");
                    break;
                case IMSConfig.IM_MESSAGE_CLOPNEGROUP:   //有人被移除了群聊
                    holder.mTvContent.setText("该群已被群主解散");
                    break;
                default:
                    holder.mTvContent.setText(bean.getText());
                    break;

            }
        }
    }
    /**
     * 处理第14种类型数据(音频)
     */
    private void AudioLeftLayout(final AudioLeftViewHolder holder, final IMConversationDetailBean tbub, final int position) {
        handlerLeftCommonViewdata(holder.mllUser,holder.mTvChatTime,tbub,holder.mIvLevel,holder.mIvHeadView, holder.mTvTitle ,holder.mllTitle, holder.mTvName,holder.mIvHeadView,position,holder.mllitem);
        final IMMessageDataJson bean = new Gson().fromJson(tbub.getData(),IMMessageDataJson.class);
        IMLogUtil.d("MyOwnTag:", "IMChatGroupRecyclerAdapter --bean.getDuration()--" +bean.getDuration());
        ViewGroup.LayoutParams linearParams =  holder.mllView.getLayoutParams();

        int a = (int) Double.parseDouble(bean.getDuration());
        holder.mContent.setText(a+"\"");
        linearParams.width =(int)((int)(IMDensityUtil.getScreenWidth(context)*1.0f/4)*(a*1.0f/60));
        holder.mllView.setLayoutParams(linearParams);

        if(tbub.getIsVoiceRead()){//语音消息已读未读
            holder.ivRed.setVisibility(View.GONE);
        }else {
            holder.ivRed.setVisibility(View.VISIBLE);
        }

        holder.tvTime.setText(TimeUtils.millis2String(tbub.getTimestamp(),new SimpleDateFormat("HH:mm")));

        holder.mllAudio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(animationDrawable!=null){
                    animationDrawable.selectDrawable(0);
                    animationDrawable.stop();
                    animationDrawable=null;
                }
                animationDrawable = (AnimationDrawable) holder.mIvAudio.getDrawable();
                if (animationDrawable.isRunning()) {
                    animationDrawable.selectDrawable(0);
                    animationDrawable.stop();
                }else {
                    playRecord(bean.getAudioUrl());
                    if(!tbub.getIsVoiceRead()){
                        saveRedDataAndChangeView(position);
                        holder.ivRed.setVisibility(View.GONE);
                    }
                }
            }
        });
        holder.mllAudio.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                showPopupWindow(4,holder.mllView,holder.mllView,position,tbub);
                return false;
            }
        });
    }
    /**
     * 处理第15种类型数据
     */
    private void AudioRightLayout(final AudioRightViewHolder holder, final IMConversationDetailBean tbub, final int position) {
        handlerRightCommonViewdata( holder.mllUser,holder.mTvChatTime,tbub,holder.mIvLevel, holder.mTvTitle,holder.mllTitle, holder.mTvName,holder.mIvHeadView,position,holder.mllitem);
        //内容
        IMMessageDataJson bean = new Gson().fromJson(tbub.getData(),IMMessageDataJson.class);
        IMLogUtil.d("MyOwnTag:", "IMChatGroupRecyclerAdapter --bean.getDuration()--" +bean.getDuration());
        ViewGroup.LayoutParams linearParams =  holder.mllView.getLayoutParams();


        int a = (int) Double.parseDouble(bean.getDuration());
        holder.mContent.setText(a+"\"");
        linearParams.width =(int)((int)(IMDensityUtil.getScreenWidth(context)*1.0f/4)*(a*1.0f/60));
        holder.mllView.setLayoutParams(linearParams);
        //如果本地有本地资源
        boolean isowner = !TextUtils.isEmpty(tbub.getOwnurl()) && new File(tbub.getOwnurl()).exists();
        holder.mllAudio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(animationDrawable!=null){
                    animationDrawable.selectDrawable(0);
                    animationDrawable.stop();
                    animationDrawable=null;
                }
                animationDrawable = (AnimationDrawable) holder.mIvAudio.getDrawable();
                if (animationDrawable.isRunning()) {
                    animationDrawable.selectDrawable(0);
                    animationDrawable.stop();
                    return;
                }
                if(isowner){
                    playRecord(tbub.getOwnurl());
                }else {
                    playRecord(bean.getAudioUrl());
                }
            }
        });
        //发送状态的变化
        sendStateChange(holder.mIvFail, tbub, position);

        holder.tvTime.setText(TimeUtils.millis2String(tbub.getTimestamp(),new SimpleDateFormat("HH:mm")));
        holder.tvTime.setCompoundDrawablesWithIntrinsicBounds(null,
                null, null, null);
        holder.mllAudio.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                showPopupWindow(4,holder.mllView,holder.mllView,position,tbub);
                return false;
            }
        });
    }

    /**
     * 处理第13种类型数据
     */
    private void ShareSmallLeftLayout(final ShareSmallLeftViewHolder holder, final IMConversationDetailBean tbub, final int position) {
        handlerLeftCommonViewdata(holder.mllUser,holder.mTvChatTime,tbub,holder.mIvLevel,holder.mIvHeadView, holder.mTvTitle ,holder.mllTitle, holder.mTvName,holder.mIvHeadView,position,holder.mllitem);
        //内容
        IMMessageDataJson bean = new Gson().fromJson(tbub.getData(),IMMessageDataJson.class);

        holder.mIvImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent=new Intent(context, SmallProgramActivity.class);
                SmallProgramBean json=new SmallProgramBean();
                json.setTwoImage(bean.getTwoImage());
                json.setThreeImage(bean.getThreeImage());
                json.setShareImage(bean.getShareImage());
                json.setProgramId(bean.getProgramId());
                json.setProgramName(bean.getProgramName());
                json.setShareTitle(bean.getShareTitle());
                json.setProgramUrl(bean.getProgramUrl());
//                intent.putExtra("data",json);
//                context.startActivity(intent);
                new SmallProgramActivity().toActivity(context,json);
            }
        });
        holder.ImTvname.setText(bean.getShareTitle());
        holder.ImTvtitle.setText(bean.getProgramName());
        IMImageLoadUtil.CommonImageLoadCp(context,bean.getShareImage(),holder.mIvImage);
        IMImageLoadUtil.CommonImageLoadCp(context,bean.getTwoImage(),holder.mIvLogo);
        holder.tvTime.setText(TimeUtils.millis2String(tbub.getTimestamp(),new SimpleDateFormat("HH:mm")));
        holder.tvTime.setCompoundDrawablesWithIntrinsicBounds(null,
                null, null, null);
    }

    /**
     * 处理第14种类型数据
     */
    private void ShareSmallRightLayout(final ShareSmallRightViewHolder holder, final IMConversationDetailBean tbub, final int position) {
        handlerRightCommonViewdata( holder.mllUser,holder.mTvChatTime,tbub,holder.mIvLevel, holder.mTvTitle,holder.mllTitle, holder.mTvName,holder.mIvHeadView,position,holder.mllitem);        //内容
        IMMessageDataJson bean = new Gson().fromJson(tbub.getData(),IMMessageDataJson.class);
        holder.mIvImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent=new Intent(context, SmallProgramActivity.class);
                SmallProgramBean json=new SmallProgramBean();
                json.setTwoImage(bean.getTwoImage());
                json.setThreeImage(bean.getThreeImage());
                json.setShareImage(bean.getShareImage());
                json.setProgramId(bean.getProgramId());
                json.setShareTitle(bean.getShareTitle());
                json.setProgramName(bean.getProgramName());
                json.setProgramUrl(bean.getProgramUrl());
//                intent.putExtra("data",json);
//                context.startActivity(intent);
                new SmallProgramActivity().toActivity(context,json);
            }
        });

        holder.ImTvname.setText(bean.getShareTitle());
        holder.ImTvtitle.setText(bean.getProgramName());
        IMImageLoadUtil.CommonImageLoadCp(context,bean.getShareImage(),holder.mIvImage);
        IMImageLoadUtil.CommonImageLoadCp(context,bean.getTwoImage(),holder.mIvLogo);
        //发送状态的变化
        sendStateChange(holder.mIvFail, tbub, position);
        holder.tvTime.setText(TimeUtils.millis2String(tbub.getTimestamp(),new SimpleDateFormat("HH:mm")));
        holder.tvTime.setCompoundDrawablesWithIntrinsicBounds(null,
                null, null, null);
    }


    /**
     * 某人被移除群聊(isremove是不是被移除群聊)
     */
    private void handleRemoveGroup( IMMessageDataJson bean,TextView textView,boolean isremove) {
        if(bean.getMeta()==null){
            return;
        }
        List<IMSystemSomeOneAddGroup> data = new Gson().fromJson(new Gson().toJson(bean.getMeta()), new TypeToken<List<IMSystemSomeOneAddGroup>>() {}.getType());
        if(data==null || data.size()==0){
            return;
        }

        StringBuffer name =new StringBuffer();
        for (int i = 0; i <data.size() ; i++) {
            if(!TextUtils.isEmpty(data.get(i).getNickName())){
                if(i==data.size()-1){
                    name.append(data.get(i).getNickName());
                }else {
                    name.append(data.get(i).getNickName()+"、");
                }
            }else {
                IMPersonBean personBean = DaoUtils.getInstance().queryMessageBean(data.get(0).getMemberId());
                if(personBean==null || TextUtils.isEmpty(personBean.getNickName())){
                    return;
                }
                name.append(personBean.getNickName()+"、");
            }
        }
        if(isremove){
            if(data!=null&&data.size()>0 && !TextUtils.isEmpty(name)){
                textView.setText(name+"被移出了群聊" );
                SpannableString spannableString = new SpannableString(name+"被移出了群聊");
                spannableString.setSpan(new ForegroundColorSpan(Color.parseColor("#05A9FE")),
                        0, name.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                textView.setText(spannableString);
            }else {
                textView.setText("有人被移出了群聊");
            }
        }else {
            if(data!=null&&data.size()>0 && !TextUtils.isEmpty(name)){
                textView.setText(name+"退出了群聊" );
                SpannableString spannableString = new SpannableString(name+"退出了群聊");
                spannableString.setSpan(new ForegroundColorSpan(Color.parseColor("#05A9FE")),
                        0, name.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                textView.setText(spannableString);
            }else {
                textView.setText("有人退出了群聊");
            }
        }


    }
    /**
     * 某人加入群聊
     */
    @RequiresApi(api = Build.VERSION_CODES.M)
    private void handleJoinGroup(IMMessageDataJson bean, TextView textView) {
        if(bean.getMeta()==null ){
            return;
        }
        try {
            List<IMSystemSomeOneAddGroup> data = new Gson().fromJson(new Gson().toJson(bean.getMeta()), new TypeToken<List<IMSystemSomeOneAddGroup>>() {}.getType());
            if(data!=null&&data.size()>0){
                StringBuffer name =new StringBuffer();
                for (int i = 0; i <data.size() ; i++) {
                    if(!TextUtils.isEmpty(data.get(i).getNickName())){
                        if(i==data.size()-1){
                            name.append(data.get(i).getNickName());
                        }else {
                            name.append(data.get(i).getNickName()+"、");
                        }
                    }
                }
                textView.setText("欢迎\""+name+"\"加入了群聊" );
                SpannableString spannableString = new SpannableString("欢迎\""+name+"\"加入了群聊");
                spannableString.setSpan(new ForegroundColorSpan(context.getResources().getColor(R.color.blue0)),
                        3, name.length()+3, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                textView.setText(spannableString);
                //某人加入群聊这里不掉接口   直接把数据源添加到数据库
                joinMemberIntoDb(bean,data);
            }else {
                textView.setText("有人加入了群聊" );
            }
        }   catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * 某人加入群聊   直接把数据源添加到数据库
     */
    private void joinMemberIntoDb(IMMessageDataJson bean,  List<IMSystemSomeOneAddGroup> datas) {
        IMGroupMemberBean data = daoUtils.queryGroupMemberBean(groupId,datas.get(0).getMemberId());
        if(data!=null){
            return;
        }
        IMGroupMemberBean newbean=new IMGroupMemberBean();
        newbean.setAvatar(datas.get(0).getAvatar());
        newbean.setNickName(datas.get(0).getNickName());
        newbean.setGroupId(datas.get(0).getGroupId());
        newbean.setMemberId(datas.get(0).getMemberId());
        newbean.setTitle(datas.get(0).getTitle());
        newbean.setBlacklist(datas.get(0).getBlacklist());
        newbean.setForbiddenWords(datas.get(0).getForbiddenWords());
        daoUtils.insertGroupMemberData(newbean.getGroupId(),newbean);
    }

    private void handleTimeContent(TextView tvTime, long timestamp,int position) {
        if(position+1<messagelist.size()) {
            long nexttime = messagelist.get(position + 1).getTimestamp();
            long time = messagelist.get(position).getTimestamp();
            String nexttimes = IMTimeData.stampToTime(nexttime + "", "MM月dd日");
            String times = IMTimeData.stampToTime(time + "", "MM月dd日");
            if(nexttimes.equals(times)){
                tvTime.setVisibility(View.GONE);
            }else {
                tvTime.setVisibility(View.VISIBLE);
                tvTime.setText(times);
            }
        }else {
            tvTime.setVisibility(View.VISIBLE);
            tvTime.setText(IMTimeData.stampToTime( timestamp+"","MM月dd日"));
        }
    }

    private SendErrorListener sendErrorListener;
    public interface SendErrorListener {
        public void onClick(IMConversationDetailBean bean);
    }

    public void setSendErrorListener(SendErrorListener sendErrorListener) {
        this.sendErrorListener = sendErrorListener;
    }

    /**
     * 处理数据源类型 通过msgtype和sendtype组合
     */
    private  int Handlemessage(IMConversationDetailBean messagelist) {

        IMMessageDataJson bean =null;
        try{
            bean = new Gson().fromJson(messagelist.getData(), IMMessageDataJson.class);
        }catch (Exception e){
            return 0;
        }
        int type = Integer.parseInt(bean.getType());
        if(TextUtils.isEmpty(bean.getSysCode())) {    //不是系统消息
            if (messagelist.getLastMsgSendType() == IMSConfig.MSG_RECIEVE && type == IMSConfig.IM_MESSAGE_TEXT) {
                return IMSConfig.MSG_LEFT_TEXT;
            } else if (messagelist.getLastMsgSendType() == IMSConfig.MSG_SEND && type == IMSConfig.IM_MESSAGE_TEXT) {
                return IMSConfig.MSG_RIGHT_TEXT;
            } else if (messagelist.getLastMsgSendType() == IMSConfig.MSG_RECIEVE && type == IMSConfig.IM_MESSAGE_PICTURE) {
                return IMSConfig.MSG_LEFT_IMG;
            } else if (messagelist.getLastMsgSendType() == IMSConfig.MSG_SEND && type == IMSConfig.IM_MESSAGE_PICTURE) {
                return IMSConfig.MSG_RIGHT_IMG;
            } else if (messagelist.getLastMsgSendType() == IMSConfig.MSG_SEND && type == IMSConfig.IM_MESSAGE_READCARD) {
                return IMSConfig.MSG_RIGHT_REDCARD;
            } else if (messagelist.getLastMsgSendType() == IMSConfig.MSG_RECIEVE && type == IMSConfig.IM_MESSAGE_READCARD) {
                return IMSConfig.MSG_LEFT_REDCARD;
            } else if (messagelist.getLastMsgSendType() == IMSConfig.MSG_SEND && type == IMSConfig.IM_MESSAGE_FOLLOWHEAD) {
                return IMSConfig.MSG_RIGHT_FOLLOW;
            } else if (messagelist.getLastMsgSendType() == IMSConfig.MSG_RECIEVE && type == IMSConfig.IM_MESSAGE_FOLLOWHEAD) {
                return IMSConfig.MSG_LEFT_FOLLOW;
            } else if ((messagelist.getLastMsgSendType() == IMSConfig.MSG_SEND && type == IMSConfig.IM_MESSAGE_REPACKED) || (messagelist.getLastMsgSendType() == IMSConfig.MSG_RECIEVE && type == IMSConfig.IM_MESSAGE_REPACKED && messagelist.getSenderId().equals(IMSManager.getMyUseId()))) {
                return IMSConfig.MSG_RIGHT_REPACKED;
            } else if (messagelist.getLastMsgSendType() == IMSConfig.MSG_RECIEVE && type == IMSConfig.IM_MESSAGE_REPACKED && !messagelist.getSenderId().equals(IMSManager.getMyUseId())) {
                return IMSConfig.MSG_LEFT_REPACKED;
            } else if (messagelist.getLastMsgSendType() == IMSConfig.MSG_SEND && type == IMSConfig.IM_MESSAGE_VIDEO) {
                return IMSConfig.MSG_RIGHT_VIDEO;
            } else if (messagelist.getLastMsgSendType() == IMSConfig.MSG_RECIEVE && type == IMSConfig.IM_MESSAGE_VIDEO) {
                return IMSConfig.MSG_LEFT_VIDEO;
            } else if (messagelist.getLastMsgSendType() == IMSConfig.MSG_RECIEVE && type == IMSConfig.IM_MESSAGE_VIDEO) {
                return IMSConfig.MSG_LEFT_VIDEO;
            }else if (messagelist.getLastMsgSendType() == IMSConfig.MSG_RECIEVE && type == IMSConfig.IM_MESSAGE_AUDIO) {
                return MSG_LEFT_AUDIO;
            }else if (messagelist.getLastMsgSendType() == IMSConfig.MSG_SEND && type == IMSConfig.IM_MESSAGE_AUDIO) {
                return IMSConfig.MSG_RIGHT_AUDIO;
            }else if (messagelist.getLastMsgSendType() == IMSConfig.MSG_SEND && type == IMSConfig.IM_MESSAGE_SHARE_SMALL) {
                return IMSConfig.MSG_RIGHT_SMALL;
            }else if (messagelist.getLastMsgSendType() == IMSConfig.MSG_RECIEVE && type == IMSConfig.IM_MESSAGE_SHARE_SMALL) {
                return IMSConfig.MSG_LEFT_SMALL;
            }
            return MSG_CHAT_GROUP;
        }else {         //群系统消息
            int sysCode = Integer.parseInt(bean.getSysCode());
            if(sysCode==IMSConfig.IM_MESSAGE_JOINGROUP || sysCode==IMSConfig.IM_MESSAGE_KIOUTGROUP || sysCode==IMSConfig.IM_MESSAGE_QUITGROUP || sysCode==IMSConfig.IM_MESSAGE_CAN_SPEAK
                    || sysCode==IMSConfig.IM_MESSAGE_BLACK || sysCode==IMSConfig.IM_MESSAGE_NO_BLACK || sysCode==IMSConfig.IM_MESSAGE_NO_SPEAK){
                return MSG_CHAT_GROUP;
            }
            return MSG_CHAT_GROUP;
        }
    }

    /**
     * 点击头像实现@的功能
     */
    public interface onItemClickListener{
        void  onItemClick(int position);
    }
    private onItemClickListener listener;

    public void setListener(onItemClickListener listener) {
        this.listener = listener;
    }

    /**
     * 处理vip等级
     */
    private void handleVipLeve(ImageView leveImage, String leve) {
        if(TextUtils.isEmpty(leve)){
            leveImage.setVisibility(View.GONE);
            return;
        }
        leveImage.setVisibility(View.VISIBLE);
        IMPersonUtils.setVip(leve,leveImage);
    }

    /**
     * 用户跟投
     */
    private void showFollowBetting(IMMessageDataJson bean) {
        getFlowData(bean.getBetOrderId());
    }

    public void getFlowData(String bettingOrderId) {
        ((IMGroupChatActivity)context).showLoadingDialog();
        final IMBetGetBean bean = new IMBetGetBean();
        bean.setBettingOrderId(bettingOrderId);
        String json = new Gson().toJson(bean);
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json);
        IMHttpsService.getBetDatailJson(body, new IMHttpResultObserver<IMBetDetailBean>() {
            @Override
            public void onSuccess(IMBetDetailBean imBetDetailBean, String message) {
                ((IMGroupChatActivity)context).dismissLoadingDialog();
                if (imBetDetailBean == null) {
                    return;
                }
                imShowFollowDiglog.showFollowBetDiglog(imBetDetailBean);
            }

            @Override
            public void _onError(Throwable e) {
                ((IMGroupChatActivity)context).dismissLoadingDialog();
                Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void _onErrorLocal(Throwable e, String message, String code) {
                ((IMGroupChatActivity)context).dismissLoadingDialog();
                Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * 获取红包详情
     * "amount":"2.00","avatar":"","createTime":1568688487000,"endTime":0,"isGrab":"N",
     * "nickName":"温园馥宁","number":1,"redPacketId":"47f67cc7dd26428bb25ac8756eada4ac","remark":"11086彩金红包",
     * "selfRecord":null,"state":"1","title":"恭喜发财，大吉大利！","type":"2","userId":"1659342","userType":"2"
     */
    public void  getRedPickHttpData(final IMMessageDataJson dataJson,final int position,final boolean isopened ){
        ((IMGroupChatActivity)context).showLoadingDialog();
        final IMBetGetBean bean = new IMBetGetBean();
        bean.setPacketId(dataJson.getRedPacketId());
        String json = new Gson().toJson(bean);
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json);
        IMHttpsService.getRedStatePickJson(body, new IMHttpResultObserver<IMRedPickInformationBean>() {
            @Override
            public void onSuccess(IMRedPickInformationBean imBetDetailBean, String message) {
                ((IMGroupChatActivity)context).dismissLoadingDialog();
                if (imBetDetailBean == null) {
                    return;
                }
                if(imBetDetailBean.getIsGrab().equals("N") ){
                    showRedPickDialog(imBetDetailBean,dataJson,position);
                }else {
                    Intent intent =new Intent(context, IMRedPickDetailActivity.class);
                    intent.putExtra("dataJson",dataJson.getRedPacketId());
                    context.startActivity(intent);

                    IMConversationDetailBean bean = daoUtils.queryConversationDetailDataAccordFieldFiger(messagelist.get(position).getFingerprint());
                    if(bean==null){
                        return;
                    }
                    messagelist.get(position).setGetredpick(true);
                    notifyItemChanged(position);
                    bean.setGetredpick(true);
                    daoUtils.updateConversationDetailData(bean);
                }
            }

            @Override
            public void _onError(Throwable e) {
                ((IMGroupChatActivity)context).dismissLoadingDialog();
                Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void _onErrorLocal(Throwable e, String message, String code) {
                ((IMGroupChatActivity)context).dismissLoadingDialog();
                Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
            }
        });
    }
    /**
     * 红包弹窗（点击之后需要保存数据库状态 和修改当前界面）
     *  //红包状态：0=未领完，1=已领完，2=部分退回，3=全部退回
     */
    public void showRedPickDialog(IMRedPickInformationBean imBetDetailBean, IMMessageDataJson dataJson,final int position){
        RedPacketEntity entity = new RedPacketEntity(imBetDetailBean.getNickName(), imBetDetailBean.getAvatar(), imBetDetailBean.getTitle());
        showRedPacketDialog(entity,imBetDetailBean,dataJson,position);
        //当红包被抢完了会调用
        if(imBetDetailBean!=null && (imBetDetailBean.getState().equals("1")||imBetDetailBean.getState().equals("2")||imBetDetailBean.getState().equals("3"))){
            SaveDataAndChangeView(position,dataJson);
        }
    }
    public void showRedPacketDialog(RedPacketEntity entity, IMRedPickInformationBean imBetDetailBean, final IMMessageDataJson dataJson, final int position) {
        if (mRedPacketDialogView == null) {
            mRedPacketDialogView = View.inflate(context, R.layout.layout_dialog_red_packet, null);
            mRedPacketViewHolder = new RedPacketViewHolder(context, mRedPacketDialogView,dataJson);
            mRedPacketDialog = new CustomDialog(context, mRedPacketDialogView, R.style.custom_dialog);
            mRedPacketDialog.setCancelable(false);
        }
        mRedPacketViewHolder.setData(entity,imBetDetailBean,dataJson);
        mRedPacketViewHolder.setOnRedPacketDialogClickListener(new OnRedPacketDialogClickListener() {
            @Override
            public void onCloseClick() {
                mRedPacketDialog.dismiss();
            }

            @Override
            public void onOpenClick() {
                //领取红包,调用接口
                SaveDataAndChangeView(position,dataJson);
                getQiangRedPickHttpData(dataJson);
                mRedPacketDialog.dismiss();
            }
        });
        mRedPacketDialog.show();
    }

    /**
     * 动画完了调用抢红包的接口
     * {"grabAmount":"0.31","grabId":"dbd84c123f9d4ec2ae0e739bfcec29f7","grabTime":1568688781632,
     * "number":2,"redPacketId":"cd7085b1072c409fafff6a806167dc20","title":"恭喜发财，大吉大利！","type":"2"}
     */
    public void  getQiangRedPickHttpData(final IMMessageDataJson dataJson){
        ((IMGroupChatActivity)context).showLoadingDialog();
        final IMBetGetBean imBetDetailBean = new IMBetGetBean();
        imBetDetailBean.setPacketId(dataJson.getRedPacketId());
        String json = new Gson().toJson(imBetDetailBean);
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json);
        IMHttpsService.getRedPickJson(body, new IMHttpResultObserver<IMGetRedPickBean>() {
            @Override
            public void onSuccess(IMGetRedPickBean data, String message) {
                ((IMGroupChatActivity)context).dismissLoadingDialog();
                IMLogUtil.d("MyOwnTag:", "RedPacketViewHolder " +"(抢红包成功) " + new Gson().toJson(data));
                Intent intent =new Intent(context, IMRedPickDetailActivity.class);
                intent.putExtra("dataJson",dataJson.getRedPacketId());
                context.startActivity(intent);
            }

            @Override
            public void _onError(Throwable e) {
                ((IMGroupChatActivity)context).dismissLoadingDialog();
                Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void _onErrorLocal(Throwable e, String message, String code) {
                ((IMGroupChatActivity)context).dismissLoadingDialog();
                Toast.makeText(context,code, Toast.LENGTH_SHORT).show();
            }
        });
    }
    /**
     * 点击红包之后保存数据库打开状态和跟新房钱界面
     */
    private void SaveDataAndChangeView( int position,IMMessageDataJson dataJson){
        IMConversationDetailBean bean = daoUtils.queryConversationDetailDataAccordFieldFiger(messagelist.get(position).getFingerprint());
        if(bean==null){
            return;
        }
        bean.setGetredpick(true);
        messagelist.get(position).setGetredpick(true);
        notifyItemChanged(position);
        daoUtils.updateConversationDetailData(bean);
    }

    /**
     * 语音播放红点保存状态
     */
    private void saveRedDataAndChangeView( int position){
        IMConversationDetailBean bean = daoUtils.queryConversationDetailDataAccordFieldFiger(messagelist.get(position).getFingerprint());
        if(bean==null){
            return;
        }
        bean.setIsVoiceRead(true);
        messagelist.get(position).setIsVoiceRead(true);
//        notifyItemChanged(position);
        daoUtils.updateConversationDetailData(bean);
    }

    /**
     * 右边发送消息的状态
     */
    private void sendStateChange(ImageView mIvFail, IMConversationDetailBean tbub, final int position) {
        if(tbub.getSendstate()==1){
            an = AnimationUtils.loadAnimation(context, R.anim.im_update_loading_progressbar_anim);
            LinearInterpolator lin = new LinearInterpolator();
            an.setInterpolator(lin);
            an.setRepeatCount(-1);
            an.startNow();
            mIvFail.setImageResource(R.mipmap.im_ic_prompt_loading);
            mIvFail.startAnimation(an);
            mIvFail.setVisibility(View.VISIBLE);
        }else  if(tbub.getSendstate()==0) { //0是成功`
            mIvFail.setVisibility(View.INVISIBLE);
        }else {
            mIvFail.setVisibility(View.VISIBLE);
            mIvFail.clearAnimation();
            mIvFail.setImageResource(R.mipmap.im_msg_state_fail_resend_pressed);
            mIvFail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (sendErrorListener != null) {
                        sendErrorListener.onClick(messagelist.get(position));
                    }
                }

            });
        }
    }
    /**
     * 处理公共的头像时间数据(左边)
     */
    private void handlerLeftCommonViewdata(LinearLayout mllUser, TextView mTvChatTime, IMConversationDetailBean tbub, ImageView mIvLevel, IMRoundAngleImageView mIvHeadView, TextView mTvTitle, RelativeLayout mllTitle, TextView mTvName, IMRoundAngleImageView mIvHeadView1, final int position,View item) {
        mllUser.setVisibility(View.VISIBLE);
        handleTimeContent(mTvChatTime,tbub.getTimestamp(),position);
        //从本地查找到处理头像名字
        String senderId = tbub.getSenderId();
        if(TextUtils.isEmpty(senderId)){
            return;
        }
        IMGroupMemberBean imGroupMemberBean = daoUtils.queryGroupMemberBean(groupId,senderId);
        if(imGroupMemberBean!=null){
            if(!TextUtils.isEmpty(imGroupMemberBean.getLevel())){  //vip等级
                handleVipLeve(mIvLevel,imGroupMemberBean.getLevel());
                mIvLevel.setVisibility(View.GONE);
            }else {
                mIvLevel.setVisibility(View.GONE);
            }
            if(!TextUtils.isEmpty(imGroupMemberBean.getTitle()) && !TextUtils.isEmpty(imGroupMemberBean.getIsViewTitle()) && imGroupMemberBean.getIsViewTitle().equals("Y")){  //vip等级
                mTvTitle.setText(imGroupMemberBean.getTitle());
                mTvTitle.setVisibility(View.GONE);
                mllTitle.setVisibility(View.GONE);
            }else {
                mTvTitle.setVisibility(View.GONE);
                mllTitle.setVisibility(View.GONE);
            }
            mTvName.setText(imGroupMemberBean.getNickName());
            IMImageLoadUtil.ImageLoadCircle(context,imGroupMemberBean.getAvatar(),mIvHeadView);
            mIvHeadView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if(listener!=null){
                        listener.onItemClick(position);
                    }
                    return true;
                }
            });
        }else {
            String data = tbub.getData();
            IMGroupOutBean s = new Gson().fromJson(data, IMGroupOutBean.class);
            mTvName.setText(s.getNickName());
            IMImageLoadUtil.ImageLoadCircle(context,s.getAvaterUrl(),mIvHeadView);
        }
        mIvHeadView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (IMStopClickFast.isFastClick()){
                    Intent intent=new Intent(context, IMMemberInforActivity.class);
                    if(imGroupMemberBean!=null){
                        intent.putExtra("bean", imGroupMemberBean);
                    }else {
                        intent.putExtra("sendId", senderId);
                    }
                    context.startActivity(intent);
                }
            }
        });

        item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(onItemClickListener!=null){
                    onItemClickListener.setOnItemClickListener();
                }
            }
        });
    }
    /**
     * 处理公共的头像时间数据(右边)
     */
    private void handlerRightCommonViewdata(LinearLayout mllUser, TextView mTvChatTime, IMConversationDetailBean tbub, ImageView mIvLevel, TextView mTvTitle, RelativeLayout mllTitle, TextView mTvName, IMRoundAngleImageView mIvHeadView,final int position,View item) {
        mllUser.setVisibility(View.GONE);//用户姓名和头衔这些
        handleTimeContent(mTvChatTime,tbub.getTimestamp(),position);
        mTvName.setText(name);
        IMImageLoadUtil.ImageLoadCircle(context,headurl,mIvHeadView);
        item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(onItemClickListener!=null){
                    onItemClickListener.setOnItemClickListener();
                }
            }
        });
        mIvHeadView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(IMStopClickFast.isFastClick()) {
                    Intent intent = new Intent(context, IMNewFriendInforlActivity.class);
                    IMPersonBean data = new IMPersonBean();
                    data.setCustomerId(IMSManager.getMyUseId());
                    data.setMemberId(IMSManager.getMyUseId());
                    data.setNickName(IMSManager.getMyNickName());
                    data.setAvatar(IMSManager.getMyHeadView());
                    intent.putExtra("bean", data);
                    context.startActivity(intent);
                }
            }
        });
    }


    public interface  onItemClickInterface{
        void setOnItemClickListener();
    };
    private onItemClickInterface onItemClickListener;

    public void setOnItemClickListener(onItemClickInterface onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    /**
     * 播放录音
     */
    public void playRecord(String audioUrl)  {
        if(mediaPlayer!=null && mediaPlayer.isPlaying()){
            setAudioStoped();
            return;
        }
        if(animationDrawable!=null) {
            animationDrawable.start();
        }
        mediaPlayer = new MediaPlayer();
        try {
            mediaPlayer.setDataSource(audioUrl);
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            // 通过异步的方式装载媒体资源
            mediaPlayer.prepareAsync();
            mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    // 装载完毕 开始播放流媒体
                    if(mediaPlayer!=null) {
                        mediaPlayer.start();
                        animationDrawable.start();
                    }
                }
            });
            // 设置循环播放
            // mediaPlayer.setLooping(true);
            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    // 在播放完毕被回调
                    animationDrawable.stop();
                    animationDrawable.selectDrawable(0);
                }
            });

            mediaPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {
                @Override
                public boolean onError(MediaPlayer mp, int what, int extra) {
                    // 如果发生错误，重新播放
                    replay(audioUrl);
                    return false;
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
            animationDrawable.selectDrawable(0);
        }
    }

    /**
     * 暂停
     */
    public void pause() {
        if (null == mediaPlayer)
            return;
        if(mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer=null;
        }

        if(animationDrawable!=null){
            animationDrawable.stop();
            animationDrawable.selectDrawable(0);
        }
    }
    /**
     * 重新播放
     */

    protected void replay(String url) {
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer=null;
        }
        if(animationDrawable!=null){
            animationDrawable.stop();
            animationDrawable.selectDrawable(0);
        }
        playRecord(url);
    }
    /**
     * 停止播放
     */
    public void stop() {
        setAudioStoped();
    }

    public void onDestroy() {
        // 在activity结束的时候回收资源
        setAudioStoped();

    }
    public void setAudioStoped(){
        // 在activity结束的时候回收资源
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }
        if(animationDrawable!=null){
            animationDrawable.stop();
            animationDrawable.selectDrawable(0);
        }
    }
    private PopupWindow mPopupWindow;
    private void showPopupWindow(int type,final View views,View textView,int position,IMConversationDetailBean tbub) {
        View contentView = LayoutInflater.from(context).inflate(R.layout.popup_content_layout, null);
        TextView textView1 = contentView.findViewById(R.id.menu_item1);
        if(type==1){
            textView1.setText("复制");
        }else {
            textView1.setText("转发");
        }
        textView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mPopupWindow != null) {
                    mPopupWindow.dismiss();
                }
                //是文字就复制内容
                if(type==1 && !TextUtils.isEmpty(((TextView)textView).getText().toString())){
                    ClipboardManager clipboard = (ClipboardManager)context.getSystemService(Context.CLIPBOARD_SERVICE);
                    clipboard.setText(((TextView)textView).getText().toString());
                }
                //不是文字就转发内容
                if(type!=1){
                    shareSend(tbub);
                }
            }
        });
        contentView.findViewById(R.id.menu_item2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mPopupWindow != null) {
                    mPopupWindow.dismiss();
                }
                if(TextUtils.isEmpty(tbub.getFingerprint())){
                    return;
                }
                messagelist.remove(position);
                IMConversationDetailBean bean = DaoUtils.getInstance().queryConversationDetailDataAccordFieldFiger(tbub.getFingerprint());
                if(bean==null){
                    return;
                }
                DaoUtils.getInstance().deleteConversationDetailData(bean);
                notifyItemRemoved(position);
                if (position != messagelist.size())
                    notifyItemRangeChanged(position, messagelist.size() - position);
            }
        });
        mPopupWindow = new PopupWindow(contentView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        // 判断Y坐标
        int[] location = new int[2];
        views.getLocationOnScreen(location);
        int   x = location[0];
        int   y = location[1]-120;
        mPopupWindow.showAtLocation(views, Gravity.TOP | Gravity.LEFT, x, y);
    }

    /**
     * 转发内容
     */
    private void shareSend( IMConversationDetailBean tbub) {
        Intent intent=new Intent(context, IMSmalSharePersonActivity.class);
        intent.putExtra("bean",tbub);
        context.startActivity(intent);
    }


    /**
     * 图片浏览器
     */
    private void showPhoto(String groupId, IMConversationDetailBean bean) {
        List<IMImageInfo>list=new ArrayList<>();
        List<ImageInfo>datas=new ArrayList<>();
        int currentPosition=0;
        IMMessageDataJson json = new Gson().fromJson(bean.getData(), IMMessageDataJson.class);
        List<IMConversationDetailBean> imPersonDetailBeans = daoUtils.queryConversationDetailDataAccordField(groupId);
        if(imPersonDetailBeans==null){
            return;
        }
        for (int i = imPersonDetailBeans.size()-1; i >=0; i--) {
            IMMessageDataJson imMessageDataJson = new Gson().fromJson(imPersonDetailBeans.get(i).getData(), IMMessageDataJson.class);
            if(!imMessageDataJson.getType().equals("2000")){
                continue;
            }
            boolean haveOwnUrl = !TextUtils.isEmpty(imPersonDetailBeans.get(i).getOwnurl()) && new File(imPersonDetailBeans.get(i).getOwnurl()).exists();
            if(!haveOwnUrl){
                list.add(new IMImageInfo((String) imMessageDataJson.getThumbImages().get(0),(String)imMessageDataJson.getImages().get(0),false));
            }else {
                list.add(new IMImageInfo(imMessageDataJson.getOwnerurl(),imMessageDataJson.getOwnerurl(),true));
            }
        }
        for (int i = 0; i < list.size(); i++) {
            if(TextUtils.isEmpty(list.get(i).getOriginUrl())){
                return;
            }
            if(list.get(i).isMine()){
                boolean haveOwnUrl = !TextUtils.isEmpty(json.getOwnerurl()) && new File(json.getOwnerurl()).exists();
                if(haveOwnUrl && list.get(i).getOriginUrl().equals(json.getOwnerurl())){
                    currentPosition=i;
                }
                if(!haveOwnUrl && list.get(i).getOriginUrl().equals(json.getImages().get(0))){
                    currentPosition=i;
                }
            }else {
                if(list.get(i).getOriginUrl().equals(json.getImages().get(0))){
                    currentPosition=i;
                }
            }
        }
        for (int i = 0; i < list.size(); i++) {
            ImageInfo  data=new ImageInfo();
            data.setOriginUrl(list.get(i).getOriginUrl());
            data.setThumbnailUrl(list.get(i).getThumbnailUrl());
            datas.add(data);
            Log.e("----position=="+i+":",new  Gson().toJson(data));
        }

        ImagePreview.getInstance()
                // 上下文，必须是activity，不需要担心内存泄漏，本框架已经处理好；
                .setContext(((IMGroupChatActivity)context))

                .setIndex(currentPosition)

                .setLoadStrategy(ImagePreview.LoadStrategy.Default)
                // 缩放动画时长，单位ms
                .setZoomTransitionDuration(1000)
                // 设置是否显示顶部的指示器（1/9）默认显示
                .setShowIndicator(true)
                // 长按回调(保存图片)
                .setBigImageLongClickListener(new OnBigImageLongClickListener() {
                    @Override public boolean onLongClick(Activity activity, View view, int position) {
                        //              IMSavePictureUtils.downloadPicture(activity, datas.get(position).getOriginUrl());
                        return true;
                    }
                })
                .setImageInfoList(datas)
                //设置是否显示下载按钮
                .setShowDownButton(false)
                // 开启预览
                .start();
    }
}
