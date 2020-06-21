package com.android.im.imadapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.ImageDecoder;
import android.graphics.drawable.AnimationDrawable;
import android.media.AudioManager;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.os.Handler;
import android.text.TextPaint;
import android.text.TextUtils;
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
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.android.im.IMSManager;
import com.android.im.IMSMsgManager;
import com.android.im.R;
import com.android.im.imbean.IMBetGetBean;
import com.android.im.imbean.IMClxTeamMessageBean;
import com.android.im.imbean.IMGetRedPickBean;
import com.android.im.imbean.IMImageInfo;
import com.android.im.imbean.IMRedPickInformationBean;
import com.android.im.imbean.SmallProgramBean;
import com.android.im.imemoji.MoonUtils;
import com.android.im.imnet.IMHttpResultObserver;
import com.android.im.imnet.http.IMHttpsService;
import com.android.im.imui.activity.IMContactDetailActivity;
import com.android.im.imui.activity.IMNewFriendInforlActivity;
import com.android.im.imui.activity.IMPersonalChatActivity;
import com.android.im.imui.activity.IMPhotoViewActivity;
import com.android.im.imui.activity.IMSSimplePlayerActivity;
import com.android.im.imui.activity.IMSingleRedPickDetailActivity;
import com.android.im.imui.activity.IMSmalSharePersonActivity;
import com.android.im.imui.activity.IMWebActivity;
import com.android.im.imui.activity.SmallProgramActivity;
import com.android.im.imutils.IMImageLoadUtil;
import com.android.im.imutils.IMSavePictureUtils;
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
import com.android.nettylibrary.greendao.entity.IMPersonBean;
import com.android.nettylibrary.greendao.greenutils.DaoUtils;
import com.android.nettylibrary.http.IMMessageDataJson;
import com.android.nettylibrary.utils.IMDensityUtil;
import com.android.nettylibrary.utils.IMLogUtil;
import com.android.nettylibrary.utils.IMPreferenceUtil;
import com.android.nettylibrary.utils.IMTimeData;
import com.android.nettylibrary.utils.IMTimeUtils;
import com.blankj.utilcode.util.SizeUtils;
import com.blankj.utilcode.util.TimeUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.gson.Gson;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import cc.shinichi.library.ImagePreview;
import cc.shinichi.library.bean.ImageInfo;
import cc.shinichi.library.view.listener.OnBigImageLongClickListener;
import cc.shinichi.library.view.listener.OnOriginProgressListener;
import okhttp3.MediaType;
import okhttp3.RequestBody;

import static com.android.nettylibrary.IMSConfig.MSG_CHAT_GROUP;
import static com.android.nettylibrary.IMSConfig.MSG_CLX_FENG;
import static com.android.nettylibrary.IMSConfig.MSG_CLX_WARN;
import static com.android.nettylibrary.IMSConfig.MSG_LEFT_AUDIO;
import static com.android.nettylibrary.IMSConfig.MSG_LEFT_IMG;
import static com.android.nettylibrary.IMSConfig.MSG_LEFT_REPACKED;
import static com.android.nettylibrary.IMSConfig.MSG_LEFT_SMALL;
import static com.android.nettylibrary.IMSConfig.MSG_LEFT_TEXT;
import static com.android.nettylibrary.IMSConfig.MSG_LEFT_VIDEO;
import static com.android.nettylibrary.IMSConfig.MSG_PUSH;
import static com.android.nettylibrary.IMSConfig.MSG_RIGHT_AUDIO;
import static com.android.nettylibrary.IMSConfig.MSG_RIGHT_IMG;
import static com.android.nettylibrary.IMSConfig.MSG_RIGHT_REPACKED;
import static com.android.nettylibrary.IMSConfig.MSG_RIGHT_SMALL;
import static com.android.nettylibrary.IMSConfig.MSG_RIGHT_TEXT;
import static com.android.nettylibrary.IMSConfig.MSG_RIGHT_VIDEO;
/**
 * 聊天的对话界面
 */

public class IMChatRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>  {
    private Context context;
    private List<IMConversationDetailBean> messagelist = new ArrayList<>();

    public Handler handler;
    private LayoutInflater mLayoutInflater;
    private Animation an;
    private IMConversationBean customer;
    private DaoUtils daoUtils;
    private IMShowFollowDiglog imShowFollowDiglog;
    private CustomDialog mRedPacketDialog;
    private View mRedPacketDialogView;
    private RedPacketViewHolder mRedPacketViewHolder;
    private String headurl;
    private final String types;
    private MediaPlayer mediaPlayer;
    private AnimationDrawable animationDrawable;


    public IMChatRecyclerAdapter(Context context, List<IMConversationDetailBean> messagelist, IMConversationBean customer) {
        this.context = context;
        mLayoutInflater = LayoutInflater.from(context);
        handler = new Handler();
        this.customer=customer;
        this.messagelist=messagelist;
        daoUtils=DaoUtils.getInstance();
        imShowFollowDiglog =new IMShowFollowDiglog(context);
        headurl = IMPreferenceUtil.getPreference_String(IMSConfig.SAVE_HEADURL, "");
        types = IMPreferenceUtil.getPreference_String(IMSConfig.SAVE_USERTYPE, "0");
    }
    /**
     * 不同类型的布局(同一种消息分左右两种不同的布局)
     */
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = null;
        RecyclerView.ViewHolder holder = null;
        switch (viewType) {
            case MSG_LEFT_TEXT:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_im_left_text, parent, false);
                holder = new TextLeftViewHolder(view);
                break;
            case MSG_RIGHT_TEXT:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_im_right_text, parent, false);
                holder = new TextRightViewHolder(view);
                break;
            case MSG_LEFT_IMG:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_im_left_img, parent, false);
                holder = new ImageLeftViewHolder(view);
                break;
            case MSG_RIGHT_IMG:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_im_right_img, parent, false);
                holder = new ImageRightViewHolder(view);
                break;
            case MSG_LEFT_VIDEO:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_im_left_video, parent, false);
                holder = new VideoLeftViewHolder(view);
                break;
            case MSG_RIGHT_VIDEO:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_im_right_video, parent, false);
                holder = new  VideoRightViewHolder(view);
                break;
            case MSG_LEFT_REPACKED:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_im_left_redpack, parent, false);
                holder = new RedPickLeftViewHolder(view);
                break;
            case MSG_RIGHT_REPACKED:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_im_right_redpack, parent, false);
                holder = new RedPickRightViewHolder(view);
                break;
            case MSG_PUSH:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_im_push, parent, false);
                holder = new PushViewHolder(view);
                break;
            case MSG_CHAT_GROUP:
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
            case MSG_CLX_WARN:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_im_clx_warn, parent, false);
                holder = new ChatClxWarnViewHolder(view);
                break;
            case MSG_CLX_FENG:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_im_clx_result, parent, false);
                holder = new ChatClxResultViewHolder(view);
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
     * 第3种类型（图片左边）
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
     * 第9种类型（推送）
     */
    class PushViewHolder extends RecyclerView.ViewHolder {
        private ImageView im_iv_head;
        private TextView im_tv_content;
        private TextView im_tv_time;
        private TextView im_tv_title;
        private LinearLayout iv_ll_detail;
        private LinearLayout ll_im_container;
        public PushViewHolder(View view) {
            super(view);
            im_iv_head =  view.findViewById(R.id.im_iv_head);
            im_tv_time =  view.findViewById(R.id.im_tv_time);
            im_tv_title =  view.findViewById(R.id.im_tv_title);
            im_tv_content =  view.findViewById(R.id.im_tv_content);
            iv_ll_detail =  view.findViewById(R.id.iv_ll_detail);
            ll_im_container =  view.findViewById(R.id.ll_im_container);
        }
    }
    /**
     * 第10种类型（音频左边）
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
     * 第11种类型（音频右边）
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
     * 第12种类型（分享小程序）
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
     * 第13种类型（分享小程序）
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
    /**
     *  第14种类型（系统消息）
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
     *  第15种类型（彩乐信团队）
     */
    class  ChatClxWarnViewHolder extends RecyclerView.ViewHolder {
        private TextView mTvChatTime;
        private TextView mTvTitle;
        private TextView mTvContent;
        private TextView mTvTime;
        public ChatClxWarnViewHolder(View view) {
            super(view);
            mTvChatTime =  view.findViewById(R.id.im_chat_time);
            mTvContent =  view.findViewById(R.id.im_tv_content);
            mTvTitle =  view.findViewById(R.id.im_tv_title);
            mTvTime =  view.findViewById(R.id.im_tv_time);
        }
    }
    /**
     *  第16种类型（彩乐信团队）
     */
    class  ChatClxResultViewHolder extends RecyclerView.ViewHolder {
        private TextView mTvChatTime;
        private TextView mTvTitle;
        private TextView mTvPerson;
        private TextView mTvTime;
        private TextView mTvResult;
        private TextView mTvWay;
        private TextView mTvContent;
        public ChatClxResultViewHolder(View view) {
            super(view);
            mTvChatTime =  view.findViewById(R.id.im_chat_time);
            mTvTitle =  view.findViewById(R.id.im_tv_title);
            mTvTime =  view.findViewById(R.id.im_tv_time);
            mTvContent =  view.findViewById(R.id.im_tv_content);

            mTvResult =  view.findViewById(R.id.im_tv_result);
            mTvPerson =  view.findViewById(R.id.im_tv_person);
            mTvWay =  view.findViewById(R.id.im_tv_way);


        }
    }
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
                VideoLeftLayout(( VideoLeftViewHolder) holder, tbub, position);
                break;
            case MSG_RIGHT_VIDEO:
                VideoRightLayout(( VideoRightViewHolder) holder, tbub, position);
                break;
            case MSG_RIGHT_REPACKED:
                RedPickRightLayout((RedPickRightViewHolder) holder, tbub, position);
                break;
            case MSG_LEFT_REPACKED:
                RedPickLeftLayout((RedPickLeftViewHolder) holder, tbub, position);
                break;
            case MSG_PUSH:
                PushLayout((PushViewHolder) holder, tbub, position);
                break;
            case MSG_CHAT_GROUP:
                ChatGroupLayout((ChatGroupViewHolder) holder, tbub, position);
                break;
            case MSG_LEFT_AUDIO:
                AudioLeftLayout((AudioLeftViewHolder) holder, tbub, position);
                break;
            case MSG_RIGHT_AUDIO:
                AudioRightLayout((AudioRightViewHolder)holder, tbub, position);
                break;
            case MSG_LEFT_SMALL:
                ShareSmallLeftLayout((ShareSmallLeftViewHolder) holder, tbub, position);
                break;
            case MSG_RIGHT_SMALL:
                ShareSmallRightLayout((ShareSmallRightViewHolder) holder, tbub, position);
                break;
            case MSG_CLX_WARN:
                cLxWarnHandleLayout((ChatClxWarnViewHolder) holder, tbub, position);
                break;
            case MSG_CLX_FENG:
                cLxResultHandleLayout((ChatClxResultViewHolder) holder, tbub, position);
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
        handlerCommonViewdata(holder.mllUser,holder.mTvChatTime,tbub,customer.getAvatar(),holder.mIvHeadView,position, holder.mllitem);
        MoonUtils.identifyFaceExpression(context,  holder.mContent, IMSMsgManager.getTextData(tbub.getData()) , ImageSpan.ALIGN_BOTTOM);
        holder.tvTime.setText(TimeUtils.millis2String(tbub.getTimestamp(),new SimpleDateFormat("HH:mm")));
        holder.tvTime2.setText(TimeUtils.millis2String(tbub.getTimestamp(),new SimpleDateFormat("HH:mm")));

        TextPaint textPaint = holder.mContent.getPaint();
        IMMessageDataJson bean = new Gson().fromJson(tbub.getData(),IMMessageDataJson.class);
        float textPaintWidth = textPaint.measureText(bean.getText());

        if(SizeUtils.px2dp(textPaintWidth)<160&&!bean.getText().contains("\\n")){
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
        handlerCommonViewdata(holder.mllUser,holder.mTvChatTime,tbub,headurl,holder.mIvHeadView,position, holder.mllitem);
        MoonUtils.identifyFaceExpression(context,  holder.mContent,IMSMsgManager.getTextData(tbub.getData()), ImageSpan.ALIGN_BOTTOM);
        Log.d("WOLF","文字消息");
        //发送状态的变化
        IMMessageDataJson bean = new Gson().fromJson(tbub.getData(),IMMessageDataJson.class);
        float textPaintWidth =  holder.mContent.getPaint().measureText(bean.getText());
        sendStateChange( holder.mIvFail,tbub,holder.tvTime,holder.tvTime2,SizeUtils.px2dp(textPaintWidth),position,MSG_RIGHT_TEXT);
        IMLogUtil.d("MyOwnTag:", "IMChatRecyclerAdapter -position1="+position+"---dp="+SizeUtils.px2dp(textPaintWidth)+"-- measureView(holder.mllText)=="+ SizeUtils.px2dp(measureView(holder.mllText)));
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                showPopupWindow(1,holder.mllText,holder.mContent,position,tbub);
                return false;
            }
        });
    }


    public  int measureView( View view) {
        ViewGroup.LayoutParams lp = view.getLayoutParams();
        if (lp == null) {
            lp = new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
            );
        }
        int widthSpec = ViewGroup.getChildMeasureSpec(0, 0, lp.width);
        int lpHeight = lp.height;
        int heightSpec;
        if (lpHeight > 0) {
            heightSpec = View.MeasureSpec.makeMeasureSpec(lpHeight, View.MeasureSpec.EXACTLY);
        } else {
            heightSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        }
        view.measure(widthSpec, heightSpec);
        return view.getMeasuredWidth();
    }
    /**
     * 处理第3种类型数据
     */
    private void ImageLeftLayout(final ImageLeftViewHolder holder, final IMConversationDetailBean tbub, final int position) {
        handlerCommonViewdata(holder.mllUser,holder.mTvChatTime,tbub,customer.getAvatar(),holder.mIvHeadView,position, holder.mllitem);

        IMMessageDataJson bean = new Gson().fromJson(tbub.getData(),IMMessageDataJson.class);
        if(bean.getThumbImages()==null ||bean.getThumbImages().size()==0){
            return;
        }
        String url = (String) bean.getThumbImages().get(0);
        IMImageLoadUtil.glidePersonLoadUrl(context,url,holder.mImageView,messagelist,position);
        holder.mImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPhoto(customer.getMemberId(),tbub);
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
        handlerCommonViewdata(holder.mllUser,holder.mTvChatTime,tbub,headurl,holder.mIvHeadView,position, holder.mllitem);
        IMMessageDataJson bean = new Gson().fromJson(tbub.getData(),IMMessageDataJson.class);
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
        IMImageLoadUtil.glidePersonLoadUrl(context,url,holder.mImageView,messagelist,position);

        holder.mImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPhoto(customer.getMemberId(),tbub);
            }
        });
        //发送状态的变化
        sendStateChange(holder.mIvFail,tbub,holder.tvTime,new TextView(context),0,position,MSG_RIGHT_IMG);
        holder.mImageView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                showPopupWindow(2,holder.mImageView,holder.mImageView,position,tbub);
                return false;
            }
        });
    }
    /**
     * 处理第5种类型数据
     */
    private void VideoLeftLayout(final VideoLeftViewHolder holder, final IMConversationDetailBean tbub, final int position) {
        handlerCommonViewdata(holder.mllUser,holder.mTvChatTime,tbub,customer.getAvatar(),holder.mIvHeadView,position, holder.mllitem);
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
        holder.tvTime.setText(TimeUtils.millis2String(tbub.getTimestamp(),new SimpleDateFormat("HH:mm")));
        holder.tvTime.setCompoundDrawablesWithIntrinsicBounds(null,
                null, null, null);
        holder.mImageView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                showPopupWindow(3,holder.mImageView,holder.mImageView,position,tbub);
                return false;
            }
        });
    }
    /**
     * 处理第6种类型数据
     */
    private void VideoRightLayout(final VideoRightViewHolder holder, final IMConversationDetailBean tbub, final int position) {
        handlerCommonViewdata(holder.mllUser,holder.mTvChatTime,tbub,headurl,holder.mIvHeadView,position, holder.mllitem);
        final IMMessageDataJson bean = new Gson().fromJson(tbub.getData(),IMMessageDataJson.class);
        //如果有本地缩略图显示缩略图，没有的显示网络生成的缩略图，还没有的(发送的时候)就根据url生成缩略图
        boolean isownfirstframe = !TextUtils.isEmpty(tbub.getOwnfirstFrame()) && new File(tbub.getOwnfirstFrame()).exists();
        boolean isownurl = !TextUtils.isEmpty(tbub.getOwnurl()) && new File(tbub.getOwnurl()).exists();
        if(isownfirstframe) {
            IMImageLoadUtil.CommonImageLoadCp(context, tbub.getOwnfirstFrame(), holder.mImageView);
        } else if(!isownfirstframe && !TextUtils.isEmpty(bean.getFirstFrame())) {
            IMImageLoadUtil.CommonImageLoadCp(context, bean.getFirstFrame(), holder.mImageView);
        } else if(!isownfirstframe && TextUtils.isEmpty(bean.getFirstFrame()) && isownurl) {
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
            IMImageLoadUtil.CommonImageLoadCp(context,  "", holder.mImageView);
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
        sendStateChange(holder.mIvFail,tbub,holder.tvTime,new TextView(context),0,position,MSG_RIGHT_VIDEO);
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
    private void RedPickLeftLayout(final RedPickLeftViewHolder holder, final IMConversationDetailBean tbub, final int position) {
        handlerCommonViewdata(holder.mllUser,holder.mTvChatTime,tbub,customer.getAvatar(),holder.mIvHeadView,position, holder.mllitem);
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
                    getRedPickHttpData(bean,position,false);
                }
            }
        });
    }
    /**
     * 处理第8种类型数据
     */
    private void RedPickRightLayout(final RedPickRightViewHolder holder, final IMConversationDetailBean tbub, final int position) {
        handlerCommonViewdata(holder.mllUser,holder.mTvChatTime,tbub,headurl,holder.mIvHeadView,position, holder.mllitem);
        //处理自适应
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
                    getRedPickHttpData(bean,position,true);
                }
            }
        });
        //发送状态的变化
        sendStateChange(holder.mIvFail,tbub,holder.mTvReaded,holder.mTvNoReaded,0,position,MSG_RIGHT_REPACKED);
    }
    /**
     * 处理第9种类型数据
     * {type=USERMSG, userStatus=null, clientId='a93bb270f1532bb9cea41dc449747014', senderId='10245d81e81292b13310ab930d82', receiverId='20485d81ffce92b1336c7adfba19', groupId='null', fingerprint='e7076b2e-3bec-4689-b558-4566f007b7ae', data='{"title":"游戏新用户充值送大礼!!!","content":"","img":"http://148.70.26.130:8888/group1/M00/00/33/rBsAAl2CYfmAY2OqAABC0dd1Cq002.jpeg","url":"http://132.232.122.151:9020/kefu-front/tpl/material/7","type":"5000","text":"推送消息"}', timestamp=1568878634506, deviceId='null', responseStatus=null, msgType=0, token='null'}
     */
    private void PushLayout(final  PushViewHolder holder, final IMConversationDetailBean tbub, final int position) {
        handleTimeContent(holder.im_tv_time,tbub.getTimestamp(),position);
        final IMMessageDataJson bean = new Gson().fromJson(tbub.getData(),IMMessageDataJson.class);
        holder.im_tv_title.setText(bean.getTitle());
        holder.im_tv_content.setText(bean.getDesc());
        IMImageLoadUtil.CommonImageLoadCp(context,bean.getImage(),holder.im_iv_head);
        holder.ll_im_container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context, IMWebActivity.class);
                intent.putExtra("url",bean.getLink());
                context.startActivity(intent);
            }
        });
    }
    /**
     *处理第10种类型数据
     */
    private void ChatGroupLayout(final ChatGroupViewHolder holder, final IMConversationDetailBean tbub, final int position) {
        handleTimeContent(holder.mTvChatTime,tbub.getTimestamp(),position);
//        IMMessageDataJson bean = new Gson().fromJson(tbub.getData(), IMMessageDataJson.class);
        holder.mTvContent.setText(tbub.getData());
    }

    /**
     * 处理第11种类型数据(音频)
     */
    private void AudioLeftLayout(final AudioLeftViewHolder holder, final IMConversationDetailBean tbub, final int position) {
        handlerCommonViewdata(holder.mllUser,holder.mTvChatTime,tbub,customer.getAvatar(),holder.mIvHeadView,position, holder.mllitem);
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
     * 处理第12种类型数据
     */
    private void AudioRightLayout(final AudioRightViewHolder holder, final IMConversationDetailBean tbub, final int position) {
        handlerCommonViewdata(holder.mllUser,holder.mTvChatTime,tbub,headurl,holder.mIvHeadView,position, holder.mllitem);
        //内容
        IMMessageDataJson bean = new Gson().fromJson(tbub.getData(),IMMessageDataJson.class);

        int a = (int) Double.parseDouble(bean.getDuration());
        holder.mContent.setText(a+"\"");
        IMLogUtil.d("MyOwnTag:", "IMChatGroupRecyclerAdapter --bean.getDuration()--" +bean.getDuration());
        ViewGroup.LayoutParams linearParams =  holder.mllView.getLayoutParams();
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
        sendStateChange(holder.mIvFail,tbub,holder.tvTime,new TextView(context),0,position,MSG_RIGHT_AUDIO);
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
        handlerCommonViewdata(holder.mllUser,holder.mTvChatTime,tbub,customer.getAvatar(),holder.mIvHeadView,position, holder.mllitem);
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
                json.setProgramUrl(bean.getProgramUrl());
                json.setShareTitle(bean.getShareTitle());
//                intent.putExtra("data",json);
////                context.startActivity(intent);
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
        handlerCommonViewdata(holder.mllUser,holder.mTvChatTime,tbub,headurl,holder.mIvHeadView,position, holder.mllitem);
        //内容
        IMMessageDataJson bean = new Gson().fromJson(tbub.getData(),IMMessageDataJson.class);
        holder.ImTvname.setText(bean.getShareTitle());
        holder.ImTvtitle.setText(bean.getProgramName());
        IMImageLoadUtil.ImageLoad(context,bean.getShareImage(),holder.mIvImage);
        IMImageLoadUtil.CommonImageLoadCp(context,bean.getTwoImage(),holder.mIvLogo);
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
        //发送状态的变化
        sendStateChange(holder.mIvFail,tbub,holder.tvTime,new TextView(context),0,position,MSG_RIGHT_SMALL);
    }
    //处理彩乐信消息
    private void cLxWarnHandleLayout(final ChatClxWarnViewHolder holder, final IMConversationDetailBean tbub, final int position) {
        handleTimeContent(holder.mTvChatTime,tbub.getTimestamp(),position);
        Log.e("--data=====-",tbub.getData());
        holder.mTvTime.setText(IMTimeData.stampToTime(tbub.getTimestamp()+"","HH:mm"));
        try {
            IMClxTeamMessageBean  dataJson = new Gson().fromJson(tbub.getData(), IMClxTeamMessageBean.class);
            holder.mTvTitle.setText(dataJson.getMeta().getMap().getMsgTitle());
            holder.mTvContent.setText(dataJson.getMeta().getMap().getMsgContent());
        }catch (Exception e){
            return;
        }
    }
    //处理结果消息
    private void cLxResultHandleLayout(final ChatClxResultViewHolder holder, final IMConversationDetailBean tbub, final int position) {
        handleTimeContent(holder.mTvChatTime,tbub.getTimestamp(),position);
        holder.mTvTime.setText(IMTimeData.stampToTime(tbub.getTimestamp()+"","HH:mm"));
        try {
            IMClxTeamMessageBean  dataJson = new Gson().fromJson(tbub.getData(), IMClxTeamMessageBean.class);
            holder.mTvTitle.setText(dataJson.getMeta().getMap().getMsgTitle());
            holder.mTvContent.setText(dataJson.getMeta().getMap().getMsgContent());
            if(!TextUtils.isEmpty(dataJson.getMeta().getMap().getIsIllegal()) && dataJson.getMeta().getMap().getIsIllegal().equals("Y")){
                holder.mTvResult.setText("审核结果：发现违规行为");
            }else {
                holder.mTvResult.setText("审核结果：未发现违规行为");
            }
            if(!TextUtils.isEmpty(dataJson.getMeta().getMap().getUsername()) ){
                holder.mTvPerson.setText("投诉对象："+dataJson.getMeta().getMap().getUsername());
            }
            switch (dataJson.getMeta().getMap().getPenalizeMethod()){
                case 1:
                    holder.mTvWay.setText("处理方式：严重警告");
                    break;
                case 2:
                    holder.mTvWay.setText("处理方式：封禁2小时");
                    break;
                case 3:
                    holder.mTvWay.setText("处理方式：封禁4小时");
                    break;
                case 4:
                    holder.mTvWay.setText("处理方式：封禁一天");
                    break;
                case 5:
                    holder.mTvWay.setText("处理方式：封禁7天");
                    break;
                case 6:
                    holder.mTvWay.setText("处理方式：封禁365天");
                    break;
                case 7:
                    holder.mTvWay.setText("处理方式：永久封禁");
                    break;
            }

        }catch (Exception e){
            return;
        }

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


    /**
     * 处理数据源类型 通过msgtype和sendtype组合
     */
    private  int Handlemessage(IMConversationDetailBean messagelist) {
        IMMessageDataJson bean =null;
        try{
            bean = new Gson().fromJson(messagelist.getData(), IMMessageDataJson.class);
        }catch (Exception e){
            return IMSConfig.MSG_CHAT_GROUP;
        }
        int type = Integer.parseInt(bean.getType());
        if(messagelist.getLastMsgSendType()== IMSConfig.MSG_RECIEVE && type==IMSConfig.IM_MESSAGE_TEXT){
            return  IMSConfig.MSG_LEFT_TEXT ;
        }else if(messagelist.getLastMsgSendType()==IMSConfig.MSG_SEND && type==IMSConfig.IM_MESSAGE_TEXT){
            return  IMSConfig.MSG_RIGHT_TEXT;
        }else if(messagelist.getLastMsgSendType()==IMSConfig.MSG_RECIEVE && type==IMSConfig.IM_MESSAGE_PICTURE){
            return  IMSConfig.MSG_LEFT_IMG;
        }else if(messagelist.getLastMsgSendType()==IMSConfig.MSG_SEND && type==IMSConfig.IM_MESSAGE_PICTURE){
            return  IMSConfig.MSG_RIGHT_IMG;
        }else if( type==IMSConfig.IM_MESSAGE_PUSH){
            return  IMSConfig.MSG_PUSH;
        }else if(messagelist.getLastMsgSendType()==IMSConfig.MSG_SEND && type==IMSConfig.IM_MESSAGE_REPACKED){
            return  IMSConfig.MSG_RIGHT_REPACKED;
        }else if(messagelist.getLastMsgSendType()==IMSConfig.MSG_RECIEVE && type==IMSConfig.IM_MESSAGE_REPACKED){
            return  IMSConfig.MSG_LEFT_REPACKED;
        }else if(messagelist.getLastMsgSendType()==IMSConfig.MSG_SEND && type==IMSConfig.IM_MESSAGE_VIDEO){
            return  IMSConfig.MSG_RIGHT_VIDEO;
        }else if(messagelist.getLastMsgSendType()==IMSConfig.MSG_RECIEVE && type==IMSConfig.IM_MESSAGE_VIDEO){
            return  IMSConfig.MSG_LEFT_VIDEO;
        }else if (messagelist.getLastMsgSendType() == IMSConfig.MSG_RECIEVE && type == IMSConfig.IM_MESSAGE_AUDIO) {
            return IMSConfig.MSG_LEFT_AUDIO;
        }else if (messagelist.getLastMsgSendType() == IMSConfig.MSG_SEND && type == IMSConfig.IM_MESSAGE_AUDIO) {
            return IMSConfig.MSG_RIGHT_AUDIO;
        }else if (messagelist.getLastMsgSendType() == IMSConfig.MSG_SEND && type == IMSConfig.IM_MESSAGE_SHARE_SMALL) {
            return IMSConfig.MSG_RIGHT_SMALL;
        }else if (messagelist.getLastMsgSendType() == IMSConfig.MSG_RECIEVE && type == IMSConfig.IM_MESSAGE_SHARE_SMALL) {
            return IMSConfig.MSG_LEFT_SMALL;
        }else if (messagelist.getLastMsgSendType() == IMSConfig.IM_MESSAGE_SYSTEM) {
            return IMSConfig.MSG_CHAT_GROUP;
        }else if (messagelist.getLastMsgSendType() == IMSConfig.MSG_CLXTEAM_WARN) {
            return IMSConfig.MSG_CLX_WARN;
        }else if (messagelist.getLastMsgSendType() == IMSConfig.MSG_CLXTEAM_RESULT) {
            return IMSConfig.MSG_CLX_FENG;
        }
        return 0;
    }

    private SendErrorListener sendErrorListener;
    public interface SendErrorListener {
        public void onClick(IMConversationDetailBean bean);
    }
    public void setSendErrorListener(SendErrorListener sendErrorListener) {
        this.sendErrorListener = sendErrorListener;
    }
    /**
     * 获取红包信息
     * {"amount":"0.20","avatar":"http:","createTime":1568797496000,"endTime":0,"isGrab":"N","nickName":"周玲娟",
     * "number":1,"redPacketId":"2e12c1ac90d24ee899a7c345062d9510","remark":"11086彩金红包",
     * "selfRecord":null,"state":"0","title":"恭喜发财，大吉大利！","type":"1","userId":"1659036","userType":"2"
     */
    public void  getRedPickHttpData(final IMMessageDataJson dataJson, final int position , final boolean isright){
        ((IMPersonalChatActivity)context).showLoadingDialog();
        final IMBetGetBean bean = new IMBetGetBean();
        bean.setPacketId(dataJson.getRedPacketId());
        String json = new Gson().toJson(bean);
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json);
        IMHttpsService.getRedStatePickJson(body, new IMHttpResultObserver<IMRedPickInformationBean>() {
            @Override
            public void onSuccess(IMRedPickInformationBean imBetDetailBean, String message) {
                ((IMPersonalChatActivity)context).dismissLoadingDialog();
                if (imBetDetailBean == null) {
                    return;
                }

                if(isright){//此处是判断是不是自己发出的红包，如果是，就直接跳转到详情界面，若果红包被领取了需要更新红包的状态哦 红包状态：0=未领完，1=已领完，2=部分退回，3=全部退回
                    if(imBetDetailBean.getState().equals("1")){
                        SaveDataAndChangeView(position,dataJson);
                    }
                    Intent intent =new Intent(context, IMSingleRedPickDetailActivity.class);
                    intent.putExtra("bean",imBetDetailBean);
                    intent.putExtra("customer",customer);
                    context.startActivity(intent);
                    return;
                }
                //此处是自己收到收到红包、先判断自己领取没，没有领取 再判断是不是过期
//                if(imBetDetailBean.getSelfRecord()==null && !imBetDetailBean.getState().equals("1")){
                if(imBetDetailBean.getIsGrab().equals("N") ){
                    showRedPickDialog(imBetDetailBean,dataJson,position);
                }else {
                    Intent intent =new Intent(context, IMSingleRedPickDetailActivity.class);
                    intent.putExtra("dataJson",dataJson.getRedPacketId());
                    intent.putExtra("customer",customer);
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
                ((IMPersonalChatActivity)context).dismissLoadingDialog();
            }

            @Override
            public void _onErrorLocal(Throwable e, String message, String code) {
                ((IMPersonalChatActivity)context).dismissLoadingDialog();
            }
        });
    }
    /**
     * 红包弹窗
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
                SaveDataAndChangeView(position,dataJson);
                getQiangRedPickHttpData(dataJson);
                mRedPacketDialog.dismiss();
            }
        });
        mRedPacketDialog.show();
    }

    /**
     * 动画完了调用抢红包的接口
     */
    public void  getQiangRedPickHttpData(final IMMessageDataJson dataJson){
        ((IMPersonalChatActivity)context).showLoadingDialog();
        final IMBetGetBean imBetDetailBean = new IMBetGetBean();
        imBetDetailBean.setPacketId(dataJson.getRedPacketId());
        String json = new Gson().toJson(imBetDetailBean);
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json);
        IMHttpsService.getRedPickJson(body, new IMHttpResultObserver<IMGetRedPickBean>() {
            @Override
            public void onSuccess(IMGetRedPickBean data, String message) {
                ((IMPersonalChatActivity)context).dismissLoadingDialog();
                IMLogUtil.d("MyOwnTag:", "RedPacketViewHolder " +"(抢红包成功) " + new Gson().toJson(data));
                Intent intent =new Intent(context, IMSingleRedPickDetailActivity.class);
                intent.putExtra("dataJson",dataJson.getRedPacketId());
                intent.putExtra("customer",customer);
                context.startActivity(intent);
            }

            @Override
            public void _onError(Throwable e) {
                ((IMPersonalChatActivity)context).dismissLoadingDialog();
                Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void _onErrorLocal(Throwable e, String message, String code) {
                ((IMPersonalChatActivity)context).dismissLoadingDialog();
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
     * 处理公共的头像时间数据
     */
    private void handlerCommonViewdata(LinearLayout mllUser, TextView mTvChatTime, IMConversationDetailBean tbub, String avatar, IMRoundAngleImageView mIvHeadView,int position,View item) {
        mllUser.setVisibility(View.GONE);//单聊隐藏用户姓名和头衔这些
        handleTimeContent(mTvChatTime,tbub.getTimestamp(),position);
        IMImageLoadUtil.ImageLoadCircle(context,avatar,mIvHeadView);

        mIvHeadView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(IMStopClickFast.isFastClick()){
                    if (avatar.equals(IMSManager.getMyHeadView())) { //如果是自己
                        Intent intent = new Intent(context, IMNewFriendInforlActivity.class);
                        IMPersonBean data = new IMPersonBean();
                        data.setCustomerId(IMSManager.getMyUseId());
                        data.setMemberId(IMSManager.getMyUseId());
                        data.setNickName(IMSManager.getMyNickName());
                        data.setAvatar(IMSManager.getMyHeadView());
                        intent.putExtra("bean", data);
                        context.startActivity(intent);
                    } else {
                        Intent intent = new Intent(context, IMContactDetailActivity.class);
                        IMPersonBean data = new IMPersonBean();
                        data.setMemberId(customer.getConversationId());
                        data.setNickName(customer.getConversationName());
                        data.setAvatar(customer.getConversationavatar());
                        intent.putExtra("bean", data);
                        intent.putExtra("isreturn", true);
                        context.startActivity(intent);
                    }
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
     * 右边发送消息的状态
     */
    @SuppressLint("NewApi")
    private void sendStateChange(ImageView mIvFail, IMConversationDetailBean tbub, TextView mTvReaded, TextView mTvNoReaded, int contentWidth,final int position, int type) {
        if(tbub.getSendstate()==1){
            an = AnimationUtils.loadAnimation(context, R.anim.im_update_loading_progressbar_anim);
            LinearInterpolator lin = new LinearInterpolator();
            an.setInterpolator(lin);
            an.setRepeatCount(-1);
            an.startNow();
            mIvFail.setImageResource(R.mipmap.im_ic_prompt_loading);
            mIvFail.startAnimation(an);
            mIvFail.setVisibility(View.VISIBLE);
            mTvReaded.setVisibility(View.GONE);
            mTvNoReaded.setVisibility(View.GONE);
        }else  if(tbub.getSendstate()==0) { //0是成功
            mIvFail.setVisibility(View.INVISIBLE);
            if(tbub.getIsReaded()){     //已读未读
                if(type==MSG_RIGHT_TEXT){
                    mTvReaded.setText(TimeUtils.millis2String(tbub.getTimestamp(),new SimpleDateFormat("HH:mm")));
                    mTvNoReaded.setText(TimeUtils.millis2String(tbub.getTimestamp(),new SimpleDateFormat("HH:mm")));
                    mTvReaded.setCompoundDrawablesWithIntrinsicBounds(null,
                            null, context.getResources().getDrawable(R.mipmap.chat_yd, null), null);
                    mTvNoReaded.setCompoundDrawablesWithIntrinsicBounds(null,
                            null, context.getResources().getDrawable(R.mipmap.chat_yd, null), null);
                    IMMessageDataJson bean = new Gson().fromJson(tbub.getData(),IMMessageDataJson.class);
                    if(contentWidth< 160 &&!bean.getText().contains("\\n")){
                        mTvReaded.setVisibility(View.VISIBLE);
                        mTvNoReaded.setVisibility(View.GONE);
                    }else {
                        mTvReaded.setVisibility(View.GONE);
                        mTvNoReaded.setVisibility(View.VISIBLE);
                    }
                }else if(type==MSG_RIGHT_AUDIO){
                    mTvReaded.setVisibility(View.VISIBLE);
                    mTvReaded.setText(TimeUtils.millis2String(tbub.getTimestamp(),new SimpleDateFormat("HH:mm")));
                    mTvReaded.setCompoundDrawablesWithIntrinsicBounds(null,
                            null, context.getResources().getDrawable(R.mipmap.chat_yd, null), null);
                }else if(type==MSG_RIGHT_IMG){
                    mTvReaded.setVisibility(View.VISIBLE);
                    mTvReaded.setText(TimeUtils.millis2String(tbub.getTimestamp(),new SimpleDateFormat("HH:mm")));
                    mTvReaded.setCompoundDrawablesWithIntrinsicBounds(null,
                            null, context.getResources().getDrawable(R.mipmap.chat_yd_white, null), null);
                }else if(type==MSG_RIGHT_SMALL){
                    mTvReaded.setVisibility(View.VISIBLE);
                    mTvReaded.setText(TimeUtils.millis2String(tbub.getTimestamp(),new SimpleDateFormat("HH:mm")));
                    mTvReaded.setCompoundDrawablesWithIntrinsicBounds(null,
                            null, context.getResources().getDrawable(R.mipmap.chat_yd_white, null), null);
                }else if(type==MSG_RIGHT_VIDEO){
                    mTvReaded.setVisibility(View.VISIBLE);
                    mTvReaded.setText(TimeUtils.millis2String(tbub.getTimestamp(),new SimpleDateFormat("HH:mm")));
                    mTvReaded.setCompoundDrawablesWithIntrinsicBounds(null,
                            null, context.getResources().getDrawable(R.mipmap.chat_yd_white, null), null);
                }

            }else {
                if(type==MSG_RIGHT_TEXT){
                    mTvReaded.setText(TimeUtils.millis2String(tbub.getTimestamp(),new SimpleDateFormat("HH:mm")));
                    mTvNoReaded.setText(TimeUtils.millis2String(tbub.getTimestamp(),new SimpleDateFormat("HH:mm")));
                    mTvReaded.setCompoundDrawablesWithIntrinsicBounds(null,
                            null, context.getResources().getDrawable(R.mipmap.chat_cg, null), null);
                    mTvNoReaded.setCompoundDrawablesWithIntrinsicBounds(null,
                            null, context.getResources().getDrawable(R.mipmap.chat_cg, null), null);
                    IMMessageDataJson bean = new Gson().fromJson(tbub.getData(),IMMessageDataJson.class);
                    if(contentWidth<160&&!bean.getText().contains("\\n")){
                        mTvReaded.setVisibility(View.VISIBLE);
                        mTvNoReaded.setVisibility(View.GONE);
                    }else {
                        mTvReaded.setVisibility(View.GONE);
                        mTvNoReaded.setVisibility(View.VISIBLE);
                    }
                }else if(type==MSG_RIGHT_AUDIO){
                    mTvReaded.setVisibility(View.VISIBLE);
                    mTvReaded.setText(TimeUtils.millis2String(tbub.getTimestamp(),new SimpleDateFormat("HH:mm")));
                    mTvReaded.setCompoundDrawablesWithIntrinsicBounds(null,
                            null, context.getResources().getDrawable(R.mipmap.chat_cg, null), null);
                }else if(type==MSG_RIGHT_IMG){
                    mTvReaded.setVisibility(View.VISIBLE);
                    mTvReaded.setText(TimeUtils.millis2String(tbub.getTimestamp(),new SimpleDateFormat("HH:mm")));
                    mTvReaded.setCompoundDrawablesWithIntrinsicBounds(null,
                            null, context.getResources().getDrawable(R.mipmap.chat_cg_white, null), null);
                } else if(type==MSG_RIGHT_SMALL){
                    mTvReaded.setVisibility(View.VISIBLE);
                    mTvReaded.setText(TimeUtils.millis2String(tbub.getTimestamp(),new SimpleDateFormat("HH:mm")));
                    mTvReaded.setCompoundDrawablesWithIntrinsicBounds(null,
                            null, context.getResources().getDrawable(R.mipmap.chat_cg_white, null), null);
                }else if(type==MSG_RIGHT_VIDEO){
                    mTvReaded.setVisibility(View.VISIBLE);
                    mTvReaded.setText(TimeUtils.millis2String(tbub.getTimestamp(),new SimpleDateFormat("HH:mm")));
                    mTvReaded.setCompoundDrawablesWithIntrinsicBounds(null,
                            null, context.getResources().getDrawable(R.mipmap.chat_cg_white, null), null);
                }
            }
        }else {
            mIvFail.setVisibility(View.VISIBLE);
            mTvReaded.setVisibility(View.VISIBLE);
            mTvReaded.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
            mTvReaded.setText(TimeUtils.millis2String(System.currentTimeMillis(),new SimpleDateFormat("HH:mm")));

            mTvNoReaded.setVisibility(View.GONE);
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
//                        animationDrawable.start();
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
    public interface  onItemClickInterface{
        void setOnItemClickListener();
    };
    private onItemClickInterface onItemClickListener;

    public void setOnItemClickListener(onItemClickInterface onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
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
    /**
     * type=1文字   2图片   3视屏  4是录音
     */
    private  PopupWindow mPopupWindow;
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
    /**
     * 图片浏览器
     */
    private void showPhoto(String memberId, IMConversationDetailBean bean) {
        List<IMImageInfo>list=new ArrayList<>();
        List<String>thumbnailUrls=new ArrayList<>();
        List<ImageInfo>datas=new ArrayList<>();
        int currentPosition=0;
        IMMessageDataJson json = new Gson().fromJson(bean.getData(), IMMessageDataJson.class);
        List<IMConversationDetailBean> imPersonDetailBeans = daoUtils.queryConversationDetailDataAccordField(memberId);
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
                .setContext(((IMPersonalChatActivity)context))

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
