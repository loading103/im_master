package com.android.im.imutils;/**
 * Created by del on 17/3/28.
 */

/**
 * created by csx at 17/3/28
 * 图片加载处理类(一下的方法是基于glide3.7实现的，现在修改为最新版本4.9，如果开发者是3.7的，可以复制下面的方法替换IMImageLoadUtil的方法）
 */
public class IMImageLoadUtil_37 {
//    /**
//     * 加载网络图片
//     */
//    public static void ImageLoad(Context context, String url, ImageView imageView) {
//        Glide.with(context)
//                .load(url)
//                .placeholder(R.mipmap.im_icon_stub)
//                .error(R.mipmap.im_icon_stub)
//                .dontAnimate()
//                .into(imageView);
//    }
//
//    /**
//     * 加载圆形图片
//     */
//    public static void ImageLoadCircle(Context context, String url, ImageView imageView) {
//        Glide.with(context)
//                .load(url)
//                .placeholder(R.mipmap.im_icon_stub)
//                .error(R.mipmap.im_headview_head)
//                .dontAnimate()
//                .bitmapTransform(new IMGlideCircleTransform(context))
//                .diskCacheStrategy(DiskCacheStrategy.ALL)
//                .into(imageView);
//    }
//
//    /**
//     * 加载圆角图片
//     */
//    public static void ImageLoadRound(Context context, String url,int corns, ImageView imageView) {
//        Glide.with(context)
//                .load(url)
//                .placeholder(R.mipmap.im_icon_stub)
//                .error(R.mipmap.im_icon_stub)
//                .dontAnimate()
//                .bitmapTransform(new IMGlideRoundTransform(context,corns))
//                .diskCacheStrategy(DiskCacheStrategy.ALL)
//                .into(imageView);
//    }
//
//
//    /**
//     * 加载普通类型的图片fitCenter
//     */
//    public static void CommonImageLoadFc(Context context, String url, ImageView imageView) {
//        Glide.with(context)
//                .load(url)
//                .placeholder(R.mipmap.im_icon_stub)
//                .error(R.mipmap.im_icon_stub)
//                .diskCacheStrategy(DiskCacheStrategy.ALL)
//                .fitCenter()
//                .into(imageView);
//    }
//
//    /**
//     * 加载普通类型的图片centerCrop
//     */
//    public static void CommonImageLoadCp(Context context, String url, ImageView imageView) {
//        Glide.with(context)
//                .load(url)
//                .placeholder(R.mipmap.im_icon_stub)
//                .error(R.mipmap.im_icon_stub)
//                .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
//                .centerCrop()
//                .into(imageView);
//    }
//
//
//    /**
//     * 聊天列表图片自适应
//     */
//    public static  void glideLoadUrl(final Context context, IMMessageDataJson bean , final IMBubbleImageView imageView, final List<IMGroupDetailBean> messagelist, final int position) {
//        //获取图片显示在ImageView后的宽高
//        imageView.setTag(imageView.getId(), position);
//        int width = messagelist.get(position).getWidth();
//        int height = messagelist.get(position).getHight();
//        if(width!=0 && height!=0){
//            setLayoutManager(context,width,height,imageView);
//        }
//        final String url = bean.getThumbImages().get(0);
//        Glide.with(context)
//                .load(url)
//                .asBitmap()//强制Glide返回一个Bitmap对象
//                .fitCenter()
//                .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
//                .into(new SimpleTarget<Bitmap>() {
//                    @Override
//                    public void onResourceReady(Bitmap bitmap, GlideAnimation<? super Bitmap> glideAnimation) {
//                        int  width=bitmap.getWidth();
//                        int  height=bitmap.getHeight();
//                        messagelist.get(position).setWidth(width);
//                        messagelist.get(position).setHight(height);
//                        if ((int)(imageView.getTag(imageView.getId())) == position) {
//                            setLayoutManager(context,width,height,imageView);
//                            imageView.setImageBitmap(bitmap);
//                        }
//                    }
//                });
//    }
//
//    /**
//     * 聊天列表图片自适应
//     */
//    public static  void glidePersonLoadUrl(final Context context, IMMessageDataJson bean , final IMBubbleImageView imageView, final List<IMPersonDetailBean> messagelist, final int position) {
//        //获取图片显示在ImageView后的宽高
//        imageView.setTag(imageView.getId(), position);
//        int width = messagelist.get(position).getWidth();
//        int height = messagelist.get(position).getHight();
//        if(width!=0 && height!=0){
//            setLayoutManager(context,width,height,imageView);
//        }
//        final String url = bean.getThumbImages().get(0);
//        Glide.with(context)
//                .load(url)
//                .asBitmap()//强制Glide返回一个Bitmap对象
//                .fitCenter()
//                .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
//                .into(new SimpleTarget<Bitmap>() {
//                    @Override
//                    public void onResourceReady(Bitmap bitmap, GlideAnimation<? super Bitmap> glideAnimation) {
//                        int  width=bitmap.getWidth();
//                        int  height=bitmap.getHeight();
//                        messagelist.get(position).setWidth(width);
//                        messagelist.get(position).setHight(height);
//                        if ((int)(imageView.getTag(imageView.getId())) == position) {
//                            setLayoutManager(context,width,height,imageView);
//                            imageView.setImageBitmap(bitmap);
//                        }
//                    }
//                });
//
//    }
//
//    public static void  setLayoutManager(Context context,int w,int h ,ImageView view){
//        ViewGroup.LayoutParams params = view.getLayoutParams();
//        int maxw= IMDensityUtil.dip2px(context,w > h ? 200 : 150);
//        int minw= IMDensityUtil.dip2px(context,w > h ? 150 : 100);
//        params.width=Math.max(Math.min(maxw,w), minw);
//        params.height= (int) (1.0f * h/w*params.width);
//        view.setLayoutParams(params);
//    }
}
