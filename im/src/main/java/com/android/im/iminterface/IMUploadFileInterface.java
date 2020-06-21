package com.android.im.iminterface;

import com.android.im.imbean.UpdataPickureBean;
import com.android.im.imnet.bean.IMUpdataFileBean;

import java.io.File;

/**
 * 上传文件
 */
public interface IMUploadFileInterface {
    /**
     *获取上传音频
     */
    void  onUpLoadAudioListener(IMUpdataFileBean bean, UpdataPickureBean recordtime);

    /**
     * 获取上传图片
     */
    void  onUpLoadImageListener(IMUpdataFileBean bean,  String path, UpdataPickureBean data);
    /**
     * 获取上传视屏
     */
    void  onUpLoadVideoListener(IMUpdataFileBean bean,String pathurl, UpdataPickureBean thumbPath);
    /**
     * 上传失败
     */
    void  onUpLoadErrroListener(String message,int type);
    /**
     * 上传进度
     */
    void  onUpLoadProcessListener(int process);
}