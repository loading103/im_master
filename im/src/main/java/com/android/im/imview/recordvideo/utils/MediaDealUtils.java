package com.android.im.imview.recordvideo.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.media.MediaMetadataRetriever;

import com.android.im.IMSManager;
import com.android.im.R;
import com.android.im.imview.recordvideo.bean.MediaInfoBean;
import com.android.im.imview.recordvideo.bean.item.MediaItem;
import com.android.im.imview.recordvideo.config.constants.ProConstants;

import java.io.File;
import java.io.FileInputStream;
import java.util.Random;


/**
 * detail: 多媒体处理工具类
 * Created by Ttt
 */
public final class MediaDealUtils {

    // 上下文
    Context mContext;
    // 日志Tag
    private static final String TAG = MediaDealUtils.class.getSimpleName();
    // 单例对象
    private static MediaDealUtils instance;

    // == 单例 ==

    private MediaDealUtils(){
    }

    public static MediaDealUtils getInstance(){
        if (instance == null){
            instance = new MediaDealUtils();
        }
        return instance;
    }

    // ==

    /**
     * 获取上下文
     * @return
     */
    private Context getContext(){
        if (this.mContext != null){
            return this.mContext;
        } else if (IMSManager.getInstance().getContext() != null){
            // 进行初始化
            initCtx(IMSManager.getInstance().getContext());
            // 并且返回上下文
            return this.mContext;
        }
        return null;
    }

    // ==

    /**
     * 初始化上下文
     * @param mContext
     */
    public void initCtx(Context mContext){
        if (this.mContext == null){
            if (mContext != null){
                this.mContext = mContext.getApplicationContext();
            }
        }
    }

    /**
     * 获取随即文件名
     * @return
     */
    private String getRandomName(){
        return DevUtils.MD5((System.currentTimeMillis() + "" + new Random().nextInt(500000)));
    }

    /**
     * 获取文件路径
     * @param suffix 后缀
     * @return
     */
    private String getFilePath(String suffix){
        return getFilePath(getRandomName(), suffix);
    }

    /**
     * 获取文件路径
     * @param fName 文件名
     * @param suffix 后缀
     * @return
     */
    public String getFilePath(String fName, String suffix){
        return DevUtils.getCachePath(getContext(), ProConstants.AP_VIDEO_RECORD_PATH) + File.separator + fName + suffix;
    }

    /**
     * 获取文件路径 - 封面地址
     * @param fName 文件名
     * @param suffix 后缀
     * @return
     */
    public String getConverFilePath(String fName, String suffix){
        return DevUtils.getCachePath(getContext(), ProConstants.AP_VIDEO_RECORD_COVER_PATH) + File.separator + fName + suffix;
    }

    // ==

    // ======================
    // == 上传前处理操作 ====
    // ======================

    /**
     * 上传前处理
     * @param mediaInfoBean 多媒体信息实体类
     */
    public void uploadDeal(final MediaInfoBean mediaInfoBean){
        // 防止文件不存在
        File file = new File(mediaInfoBean.getMediaUri());
        if (!file.exists()){
            // 文件不存在
            return;
        }
        // 后台运行
        new Thread(new Runnable() {
            @Override
            public void run() {
                // 处理上传文件
                dealUpload(mediaInfoBean);
            }
        }).start();
    }

    /**
     * 处理上传文件
     * @param mediaInfoBean 多媒体信息实体类
     */
    private void dealUpload(MediaInfoBean mediaInfoBean){
        // 判断是否图片类型
      if (mediaInfoBean.getMediaTypeEnum() == MediaItem.MediaTypeEnum.VIDEO){
            // 生成缩略图,并返回路径
            String thumbImagePath = createMediaThumbnail(mediaInfoBean);
            // 初始化File
            File thumbImageFile = new File(thumbImagePath + "");
            // 判断是否存在缩略图
            if (!thumbImageFile.exists()){
                // 缩略图生成失败
                return;
            }
            // 设置缩略图地址
            mediaInfoBean.setMediaCover(thumbImagePath);
            // 缩放图片
            dealVideoCover(mediaInfoBean);
            // 获取视频文件信息对象
            File videoFile = new File(mediaInfoBean.getMediaUri());
            // 获取视频名
            String videoName = DevUtils.getFileNotSuffix(videoFile.getName());
            // 获取缩略图名字
            String videoImgName = DevUtils.getFileNotSuffix(DevUtils.getName(mediaInfoBean.getMediaCover()));
            // 获取新的视频名字
            String nVideoName = videoFile.getAbsolutePath().replace(videoName, videoImgName);
            // 修改视频文件名
            boolean isResult = new File(mediaInfoBean.getMediaUri()).renameTo(new File(nVideoName));
            // 更新地址
            mediaInfoBean.setMediaUri(nVideoName);
            // 获取处理结果
//            // 处理上传视频
//            IMVideoDealUtils.INSTANCE.dealUploadVideo(conversationItem, videoInfoItem);
        }
    }


    /**
     * 保存多媒体文件缩略图
     * @param mediaInfoBean 多媒体信息实体类
     */
    public String createMediaThumbnail(MediaInfoBean mediaInfoBean){
        // 缩略图路径
        String thumbImagePath = null;
        // 获取缩略图
        Bitmap bitmap = null;
        // ==
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        try {
            // 获取多媒体资源地址
            String rPath = mediaInfoBean.getMediaUri();
            // 设置视频路径
            retriever.setDataSource(mediaInfoBean.getMediaUri());
            // 获取缩略图
            bitmap = retriever.getFrameAtTime();
            // 获取视频时间
            String duration = retriever.extractMetadata(android.media.MediaMetadataRetriever.METADATA_KEY_DURATION);
            // 获取视频宽度
            int vWidth = DevUtils.toInt(retriever.extractMetadata(android.media.MediaMetadataRetriever.METADATA_KEY_VIDEO_WIDTH), -1);
            // 获取视频高度
            int vHeight = DevUtils.toInt(retriever.extractMetadata(android.media.MediaMetadataRetriever.METADATA_KEY_VIDEO_HEIGHT), -1);
            // 因为是竖屏录制, 所以统一高度大于宽度
            int calcWidth = vWidth, calcHeight = vHeight;
            // 进行处理判断
            if (mediaInfoBean.isPortrait()){
                if (vWidth > vHeight){
                    calcWidth = vHeight;
                    calcHeight = vWidth;
                }
            } else {
                if (vWidth < vHeight){
                    calcWidth = vHeight;
                    calcHeight = vWidth;
                }
            }
            // 设置宽高信息
            mediaInfoBean.setMediaWidth(calcWidth);
            mediaInfoBean.setMediaHeight(calcHeight);
            // 视频前缀
            String rPrefix = "video_" + calcHeight + "_" + calcWidth + "_" + ((mediaInfoBean.isPortrait()) ? 1 : 0) + "_";
            // 获取这个图片的宽和高
            if(bitmap != null){
                // 转换时间
                float vFloatTime = DevUtils.toFloat(duration, 0l);
                // 计算时间
                int vIntTime = Math.round(vFloatTime / 1000f);
                // 保存视频时间
                mediaInfoBean.setMediaTime(vIntTime);
                // 保存文件大小
                mediaInfoBean.setMediaSize(new File(rPath).length());
                // 判断时间
                String vStrTime = (vIntTime >= 10) ? "10" : (vIntTime + "");
                // 拼接文件名
                String fName = rPrefix + vStrTime + "_" + ((int) ((double) new File(rPath).length() / 1024d)) + "_" + DevUtils.getFileNotSuffix(new File(rPath).getName());
                // 拼接文件名
                thumbImagePath = getFilePath(fName, ".jpg");
                // 保存图片
                DevUtils.saveBitmap(thumbImagePath, bitmap, Bitmap.CompressFormat.JPEG, 70);
            }
        } catch (Exception e) {
        } finally {
            if(retriever != null){
                try {
                    // 释放资源
                    retriever.release();
                } catch (Exception e2) {
                }
            }
            // 释放资源
            if(bitmap != null && !bitmap.isRecycled()){
                bitmap.recycle();
            }
            bitmap = null;
        }
        // 返回缩略图路径
        return thumbImagePath;
    }

    /**
     * 处理视频封面
     * @param mediaInfoBean 多媒体信息实体类
     */
    private void dealVideoCover(MediaInfoBean mediaInfoBean){
        // 是否竖屏
        boolean isPortrait = mediaInfoBean.isPortrait();
        // 判断时间
        String vStrTime = (mediaInfoBean.getMediaTime() >= 10) ? "10" : (mediaInfoBean.getMediaTime()  + "");
        // 视频前缀
        String rPrefix = "video_" + mediaInfoBean.getMediaHeight() + "_" + mediaInfoBean.getMediaWidth() + "_" + ((mediaInfoBean.isPortrait()) ? 1 : 0);
        // 拼接后续时间_大小_文件名
        rPrefix += "_" + vStrTime + "_" + ((int) ((double) mediaInfoBean.getMediaSize() / 1024d)) + "_";
        // 计算宽度高度
        int width = isPortrait ? 253 : 450;
        int height = isPortrait ? 450 : 253;
        int playIconWidth = 90; // 播放图片宽度 = 正方形
        // --
        // 初始化画笔
        Paint paint = new Paint();
        paint.setColor(Color.WHITE);
        paint.setAntiAlias(true);
        // 生成32位图，存在alpha通道
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        // 创建画布
        Canvas canvas = new Canvas(bitmap);
        // 背景色透明
        canvas.drawColor(Color.TRANSPARENT);
        // 缩略图
        Bitmap thumbBitmap = null;
        try {
            //450x253
            //253x450
            thumbBitmap = BitmapFactory.decodeStream(new FileInputStream(mediaInfoBean.getMediaCover()));
            // 缩略图宽度高度
            float thumbWidth = (float) mediaInfoBean.getMediaWidth();
            float thumbHeight = (float) mediaInfoBean.getMediaHeight();
            // 判断是否属于竖屏
            if (isPortrait){
                // 获取倍数
                float scaleY_w = (thumbHeight / (float) height);
                // 计算高度比例
                float scaleY = ((float) height / thumbHeight);
                // 计算宽度比例
                float scaleX = ((float) width / thumbWidth);
                // 如果符合条件, 则进行生成
                if (thumbWidth >= (scaleY_w * (float) width)){
                    // 获取相差的宽度
                    int diffWidth = (((int) thumbWidth - ((int) (width * scaleY_w))) / 2);
                    // Matrix类进行图片处理
                    Matrix matrix = new Matrix();
                    matrix.postScale(scaleY, scaleY);
                    // 生成新的图片
                    Bitmap scaleBitmap = Bitmap.createBitmap(thumbBitmap, diffWidth, 0, ((int) (width * scaleY_w)), (int) thumbHeight, matrix, true);
                    // 绘制头像
                    canvas.drawBitmap(scaleBitmap, 0, 0, null);
                } else {
                    // Matrix类进行图片处理
                    Matrix matrix = new Matrix();
                    matrix.postScale(scaleX, scaleY);
                    // 生成新的图片
                    Bitmap scaleBitmap = Bitmap.createBitmap(thumbBitmap, 0, 0, (int) thumbWidth, (int) thumbHeight, matrix, true);
                    // 绘制头像
                    canvas.drawBitmap(scaleBitmap, 0, 0, null);
                }
            } else {
                // 获取倍数
                float scaleY_h = (thumbWidth / (float) width);
                // 计算高度比例
                float scaleY = ((float) width / thumbWidth);
                // 计算宽度比例
                float scaleX = ((float) height / thumbHeight);
                // 如果符合条件, 则进行生成
                if (thumbHeight >= (scaleY_h * (float) height)){
                    // 获取相差的宽度
                    int diffHeight = (((int) thumbHeight - ((int) (height * scaleY_h))) / 2);
                    // Matrix类进行图片处理
                    Matrix matrix = new Matrix();
                    matrix.postScale(scaleY, scaleY);
                    // 生成新的图片
                    Bitmap scaleBitmap = Bitmap.createBitmap(thumbBitmap, 0, diffHeight, (int) thumbWidth, ((int) (height * scaleY_h)), matrix, true);
                    // 绘制头像
                    canvas.drawBitmap(scaleBitmap, 0, 0, null);
                } else {
                    // Matrix类进行图片处理
                    Matrix matrix = new Matrix();
                    matrix.postScale(scaleY, scaleX);
                    // 生成新的图片
                    Bitmap scaleBitmap = Bitmap.createBitmap(thumbBitmap, 0, 0, (int) thumbWidth, (int) thumbWidth, matrix, true);
                    // 绘制头像
                    canvas.drawBitmap(scaleBitmap, 0, 0, null);
                }
            }
        } catch (Exception e){
        }
        // 获取新的图片路径 = 缩略图
        String nCoverImagePath = getConverFilePath(rPrefix + DevUtils.getFileNotSuffix(new File(mediaInfoBean.getMediaUri()).getName()), ".jpg");
        // 保存图片
        DevUtils.saveBitmap(nCoverImagePath, bitmap, Bitmap.CompressFormat.JPEG, 80);
        // =
        try {
            // 播放图标 - Play Icon 图片
            Bitmap piBitmap = BitmapFactory.decodeResource(getContext().getResources(), R.drawable.im_chat_play_big);
            // 宽度高度
            int piWidth = piBitmap.getWidth(), piHeight = piBitmap.getHeight();
            // --
            float scaleX = ((float) playIconWidth / ((float) piWidth));
            float scaleY = ((float) playIconWidth / ((float) piHeight));
            // Matrix类进行图片处理
            Matrix matrix = new Matrix();
            matrix.postScale(scaleX, scaleY);
            // 生成新的图片
            Bitmap piCaleBitmap = Bitmap.createBitmap(piBitmap, 0, 0, piWidth, piHeight, matrix, true);
            // 绘制播放图标
            canvas.drawBitmap(piCaleBitmap, (width - playIconWidth) / 2, (height - playIconWidth) / 2, null);
        } catch (Exception e){
        }
        // ==
//        try {
//            // 绘制时间
//            Paint ttPaint = new Paint();
//            ttPaint.setColor(Color.WHITE);
//            ttPaint.setAntiAlias(true);
//            ttPaint.setTextSize(30f);
//            // 获取字体内容宽度
//            float tWidth = paint.measureText(vStrTime);
//            // 获取字体内容高度
//            Paint.FontMetrics fm = paint.getFontMetrics();
//            float tHeight = (float) Math.ceil(fm.descent - fm.ascent);
//            // 计算移动位置
//            if (isPortrait){
//                canvas.drawText(vStrTime, (width / 2 - tWidth), height - tHeight, ttPaint);
//            } else {
//                canvas.drawText(vStrTime, (width / 2 - tWidth), height - tHeight, ttPaint);
//            }
//        } catch (Exception e){
//        }
        // 删除旧的图片
        new File(mediaInfoBean.getMediaCover() + "").delete();
        // 获取新的图片路径
        String nImagePath = getFilePath(rPrefix + DevUtils.getFileNotSuffix(new File(mediaInfoBean.getMediaUri()).getName()), ".jpg");
        // 保存新的缩略图地址
        mediaInfoBean.setMediaCover(nImagePath);
        // 保存图片
        DevUtils.saveBitmap(nImagePath, bitmap, Bitmap.CompressFormat.JPEG, 80);
    }


}
