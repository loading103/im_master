package com.android.im.imutils;

import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.android.im.R;
import com.android.nettylibrary.greendao.entity.IMConversationDetailBean;
import com.android.nettylibrary.http.IMMessageDataJson;
import com.android.nettylibrary.utils.IMDensityUtil;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.load.resource.gif.GifDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.transition.DrawableCrossFadeFactory;
import com.bumptech.glide.request.transition.Transition;

import java.util.List;

import jp.wasabeef.glide.transformations.CropCircleWithBorderTransformation;
import jp.wasabeef.glide.transformations.RoundedCornersTransformation;


/**
 * @author jinxin
 * 剑之所指，心之所向
 * @date 2019/8/4
 *加载图片类
 */
public class IMImageLoadUtil {
    /**
     * 加载网络图片
     */
    public static void ImageLoad(Context context, String url, ImageView imageView) {
        Glide.with(context)
                .load(url)
                .placeholder(R.mipmap.im_icon_stub_loading)
                .error(R.mipmap.im_icon_stub)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .dontAnimate()
                .into(imageView);
    }
    /**
     * 加载圆形图片
     */
    public static void ImageCircleLoad(Context context, String url, ImageView imageView) {
        RequestOptions requestOptions = RequestOptions.circleCropTransform();
        Glide.with(context)
                .load(url)
                .placeholder(R.mipmap.im_chat_headview_loading)
                .error(R.mipmap.im_headview_head)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .dontAnimate()
                .apply(requestOptions)
                .into(imageView);
    }
    /**
     * 加载圆形图片(带边框)
     */
    public static void ImageLineCircleLoad(Context context, String url, ImageView imageView) {
        Glide.with(context)
                .load(url)
                .apply(new RequestOptions().bitmapTransform(new CircleCrop())
                        .transform(new IMGlideCircleWithBorder(context, 2,context.getResources().getColor(R.color.color_0BD2CF))))
                .placeholder(R.mipmap.im_chat_headview_loading)
                .error(R.mipmap.im_headview_head)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .dontAnimate()
                .into(imageView);
    }
    /**
     *(新版改成圆角图片)
     */
    public static void ImageLoadCircle(Context context, String url, ImageView imageView) {
        RoundedCornersTransformation transform = new RoundedCornersTransformation(IMDensityUtil.dip2px(context,5),0);
        RequestOptions options = new RequestOptions().placeholder(R.mipmap.im_icon_stub).error(R.mipmap.im_icon_stub).transform(transform);
        Glide.with(context)
                .asBitmap()
                .load(url)
                .dontAnimate()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .apply(options)
                .dontAnimate()
                .into(imageView);
    }


    public static void ImageLoadBlueCircle(Context context, String url, ImageView imageView) {
        RoundedCornersTransformation transform = new RoundedCornersTransformation(IMDensityUtil.dip2px(context,5),0);
        RequestOptions options = new RequestOptions().placeholder(R.mipmap.im_icon_stub).error(R.mipmap.im_icon_stub).transform(transform).transform(new CropCircleWithBorderTransformation(2,context.getResources().getColor(R.color.color_0BD2CF)));
        Glide.with(context)
                .asBitmap()
                .load(url)
                .dontAnimate()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .apply(options)
                .into(imageView);
    }

    public static void ImageLoadlineCircle(Context context, String url, ImageView imageView) {
        Glide.with(context)
                .load(url)
                .apply(new RequestOptions().bitmapTransform(new CircleCrop())
                        .transform(new IMGlideCircleWithBorder(context, 3,context.getResources().getColor(R.color.color_0BD2CF))))
                .placeholder(R.mipmap.im_chat_headview_loading)
                .error(R.mipmap.im_headview_head)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .dontAnimate()
                .into(imageView);
    }


    /**
     * 加载头型圆形图片
     */
    public static void ImageHeadLoadUrl(Context context, String url, ImageView imageView) {
        Glide.with(context)
                .load(url)
                .placeholder(R.mipmap.im_chat_headview_loading)
                .error(R.mipmap.im_headview_head)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .dontAnimate()
                .into(imageView);
    }
    /**
     * 加载圆角图片
     */
    public static void ImageLoadRound(Context context, String url, ImageView imageView) {
        RoundedCornersTransformation transform = new RoundedCornersTransformation(IMDensityUtil.dip2px(context,8),0);
        RequestOptions options = new RequestOptions().placeholder(R.mipmap.im_icon_stub).error(R.mipmap.im_icon_stub).transform(transform);
        Glide.with(context)
                .asBitmap()
                .load(url)
                .dontAnimate()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .apply(options)
                .into(imageView);
    }


    /**
     * 加载普通类型的图片fitCenter
     */
    public static void CommonImageLoadFc(Context context, String url, ImageView imageView) {
        final ObjectAnimator anim = ObjectAnimator.ofInt(imageView, "ImageLevel", 0, 10000);
        anim.setDuration(800);
        anim.setRepeatCount(ObjectAnimator.INFINITE);
        anim.start();

        Glide.with(context)
                .load(url)
                .placeholder(R.drawable.im_image_loading)
                .error(R.mipmap.im_icon_stub)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        anim.cancel();
                        return false;
                    }
                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        anim.cancel();
                        return false;
                    }
                })
                .fitCenter()
                .into(imageView);
    }

    /**
     * 加载普通类型的图片centerCrop
     */
    public static void CommonImageLoadCp(Context context, String url, ImageView imageView) {
        Glide.with(context)
                .load(url)
                .placeholder(R.mipmap.im_icon_stub_loading)
                .error(R.mipmap.im_icon_stub)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .centerCrop()
                .into(imageView);
    }

    public static void CommonImageBgLoadCp(Context context, String url, ImageView imageView) {
        Glide.with(context)
                .load(url)
                .placeholder(R.mipmap.im_icon_stub)
                .error(R.mipmap.im_mine_01)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .centerCrop()
                .into(imageView);
    }
    /**
     * 加载渐渐显示的效果(只在启动页使用)
     */
    public static void CommonSplashImageLoadCp(Context context, String url, ImageView imageView) {
        DrawableCrossFadeFactory drawableCrossFadeFactory = new DrawableCrossFadeFactory.Builder(300).setCrossFadeEnabled(true).build();
        Glide.with(context)
                .load(url)
                .placeholder(R.mipmap.splash_bg)
                .error(R.mipmap.splash_bg)
                .transition(DrawableTransitionOptions.with(drawableCrossFadeFactory))
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .centerCrop()
                .into(imageView);
    }
    /**
     * 加载渐渐显示的效果(只在启动页使用)
     */
    public static void CommonGifLoadCp(Context context, String url, ImageView imageView) {
        Glide.with(context).load(url).listener(new RequestListener() {
            @Override
            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target target, boolean isFirstResource) {
                return false;
            }

            @Override
            public boolean onResourceReady(Object resource, Object model, Target target, DataSource dataSource, boolean isFirstResource) {
                if (resource instanceof GifDrawable) {
                    //加载一次
                    ((GifDrawable)resource).setLoopCount(-1);
                }
                return false;
            }

        }).into(imageView);
    }
    /**
     * 聊天列表图片自适应
     */
    @SuppressLint("CheckResult")
    public static  void glideLoadUrl(final Context context, String url , final ImageView imageView, final List<IMConversationDetailBean> messagelist, final int position) {
        //获取图片显示在ImageView后的宽高
        imageView.setTag(imageView.getId(), position);
        int width =0;
        int height =0;
        if(!TextUtils.isEmpty(messagelist.get(position).getWidth())){
            width = Integer.parseInt(messagelist.get(position).getWidth());
        }
        if(!TextUtils.isEmpty(messagelist.get(position).getHight())){
            height = Integer.parseInt(messagelist.get(position).getHight());
        }
        boolean hasdata=false;
        if(width!=0 && height!=0){
            hasdata=true;
            setLayoutManager(context,width,height,imageView,url);
        }
        boolean finalHasdata = hasdata;
        Glide.with(context)
                .asBitmap()
                .load(url)
                .fitCenter()
                .placeholder(R.mipmap.im_icon_stub_loading)
                .error(R.mipmap.im_icon_stub)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(@NonNull Bitmap bitmap, @Nullable Transition<? super Bitmap> transition) {
                        int width = bitmap.getWidth();
                        int height = bitmap.getHeight();
                        messagelist.get(position).setWidth(width+"");
                        messagelist.get(position).setHight(height+"");
                        if ((int) (imageView.getTag(imageView.getId())) == position) {
                            if(!finalHasdata){
                                setLayoutManager(context, width, height, imageView,url);
                            }
                            imageView.setImageBitmap(bitmap);
                        }
                    }
                });
    }

    /**
     * 聊天列表图片自适应
     */
    /**
     * 聊天列表图片自适应
     */
    public static  void glidePersonLoadUrl(final Context context, String url , final ImageView imageView, final List<IMConversationDetailBean> messagelist, final int position) {
        //获取图片显示在ImageView后的宽高
        imageView.setTag(imageView.getId(), position);
        int width =0;
        int height =0;
        if(!TextUtils.isEmpty(messagelist.get(position).getWidth())){
            width = Integer.parseInt(messagelist.get(position).getWidth());
        }
        if(!TextUtils.isEmpty(messagelist.get(position).getHight())){
            height = Integer.parseInt(messagelist.get(position).getHight());
        }
        boolean hasdata=false;
        if(width!=0 && height!=0){
            hasdata=true;
            setLayoutManager(context,width,height,imageView,url);
        }
        boolean finalHasdata = hasdata;
        Glide.with(context)
                .asBitmap()//强制Glide返回一个Bitmap对象
                .load(url)
                .fitCenter()
                .placeholder(R.mipmap.im_icon_stub_loading)
                .error(R.mipmap.im_icon_stub)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(@NonNull Bitmap bitmap, @Nullable Transition<? super Bitmap> transition) {
                        int  width=bitmap.getWidth();
                        int  height=bitmap.getHeight();
                        messagelist.get(position).setWidth(width+"");
                        messagelist.get(position).setHight(height+"");
                        if ((int)(imageView.getTag(imageView.getId())) == position) {
                            if(!finalHasdata){
                                setLayoutManager(context,width,height,imageView,url);
                            }
                            imageView.setImageBitmap(bitmap);
                        }
                    }
                });
    }

    public static void  setLayoutManager(Context context,int w,int h ,ImageView view,String url){
        ViewGroup.LayoutParams params = view.getLayoutParams();
        int maxw=0;
        int minw=0;
        if(!url.contains("GSYVideo")){
            maxw= IMDensityUtil.dip2px(context,w > h ? 200 : 150);
            minw= IMDensityUtil.dip2px(context,w > h ? 150 : 100);
        }else {
            maxw= IMDensityUtil.dip2px(context,w > h ? 150 : 100);
            minw= IMDensityUtil.dip2px(context,w > h ? 100 : 50);
        }
        params.width=Math.max(Math.min(maxw,w), minw);
        params.height= (int) (1.0f * h/w*params.width);
        view.setLayoutParams(params);
    }


    /**
     * 加载网络图片
     */
    public static void ImageCharLoad(Context context, String url, ImageView imageView) {
        Glide.with(context)
                .load(url)
                .error(R.mipmap.im_icon_stub)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .dontAnimate()
                .into(imageView);
    }
}
