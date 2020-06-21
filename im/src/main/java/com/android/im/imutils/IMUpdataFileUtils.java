package com.android.im.imutils;

import android.app.Activity;
import android.content.Intent;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.android.im.IMSManager;
import com.android.im.imbean.UpdataPickureBean;
import com.android.im.iminterface.IMUploadFileInterface;
import com.android.im.imnet.bean.IMUpdataFileBean;
import com.android.im.imui.activity.IMGroupChatActivity;
import com.android.im.imui.activity.IMPersonalChatActivity;
import com.android.im.imview.recordvideo.bean.MediaInfoBean;
import com.android.im.imview.recordvideo.bean.item.MediaItem;
import com.android.im.imview.recordvideo.bean.item.media.VideoInfoItem;
import com.android.im.imview.recordvideo.config.constants.KeyConstants;
import com.android.im.imview.recordvideo.utils.DevUtils;
import com.android.im.imview.recordvideo.utils.MediaDealUtils;
import com.android.nettylibrary.IMSConfig;
import com.android.nettylibrary.utils.IMLogUtil;
import com.android.nettylibrary.utils.IMPreferenceUtil;
import com.google.gson.Gson;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import top.zibin.luban.CompressionPredicate;
import top.zibin.luban.Luban;
import top.zibin.luban.OnCompressListener;

import static com.android.im.IMSManager.okHttpClient;
import static com.shuyu.gsyvideoplayer.utils.FileUtils.getPath;

public class IMUpdataFileUtils {
    public static  final  int   TYPE_IMAGE=1;
    public static  final  int   TYPE_AUDIO=2;
    public static  final  int   TYPE_VIDEO=3;
    public IMUpdataFileUtils(IMUploadFileInterface onUpdataFileListener) {
        this.onUpdataFileListener = onUpdataFileListener;
    }
    private IMUploadFileInterface onUpdataFileListener;

    /**
     * 上传录
     */
    public  void handleUpdataAduioPickure(Activity activity,File file, UpdataPickureBean data ) {
        String token = IMPreferenceUtil.getPreference_String(IMSConfig.SAVE_IM_TOKEN, null);
        RequestBody fileBody = RequestBody.create(MediaType.parse("application/otcet-stream"), file);
        if(TextUtils.isEmpty(token)){
            onUpdataFileListener.onUpLoadErrroListener("登录超时，请重新登录",TYPE_AUDIO);
            return;
        }
        MultipartBody multipartBody =null;
        try {
            multipartBody = new MultipartBody.Builder().setType(MultipartBody.FORM)
                    .addFormDataPart("access_token", token)
                    .addFormDataPart("file", file.getName(), fileBody)
                    .build();
        }catch (Exception e){
            e.printStackTrace();
            multipartBody = new MultipartBody.Builder().setType(MultipartBody.FORM)
                    .addFormDataPart("access_token", token)
                    .addFormDataPart("file", testStr(file.getName()), fileBody)
                    .build();
        }
        //创建Request
        Request request = new Request.Builder()
                .url(IMSConfig.HTTP_IM_UPDATE_IMAGE)
                .post(multipartBody)
                .build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(onUpdataFileListener!=null){
                            onUpdataFileListener.onUpLoadErrroListener( e.getMessage(),TYPE_AUDIO);
                        }
                    }
                });
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                IMUpdataFileBean bean =null;
                try{
                    bean = new Gson().fromJson(response.body().string(), IMUpdataFileBean.class);
                }catch (Exception e){
                    return;
                }
                IMUpdataFileBean finalBean = bean;
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(onUpdataFileListener!=null){
                            onUpdataFileListener.onUpLoadAudioListener(finalBean,data);
                        }
                    }
                });
            }
        });
    }
    /**
     * 上传图片资源(先压缩)
     */
    public  void  handleCompressPickure(Activity activity, final List<String> mSelected, boolean isSingleChat){
        Luban.with(activity)
                .load(mSelected)
                .ignoreBy(100)
                .setTargetDir(getPath())
                .filter(new CompressionPredicate() {
                    @Override
                    public boolean apply(String path) {
                        return !(TextUtils.isEmpty(path) || path.toLowerCase().endsWith(".gif"));
                    }
                })
                .setCompressListener(new OnCompressListener() {
                    @Override
                    public void onStart() {
                    }

                    @Override
                    public void onSuccess(File file) {  //压缩成功上传
                        UpdataPickureBean bean= new UpdataPickureBean();
                        bean.setUrl(file.getPath());
                        bean.setTime(System.currentTimeMillis());
                        if(isSingleChat){
                            ((IMPersonalChatActivity)activity).sendPickureDatas(bean);
                        }else {
                            ((IMGroupChatActivity)activity).sendPickureData(bean);
                        }

                        handleUpdataPickure(activity,file,bean);

                    }
                    @Override
                    public void onError(Throwable e) {
                    }
                }).launch();
    }
    public void handleUpdataPickure(Activity activity,File file,UpdataPickureBean beans) {
        String token = IMPreferenceUtil.getPreference_String(IMSConfig.SAVE_IM_TOKEN, null);
        if(TextUtils.isEmpty(token)){
            onUpdataFileListener.onUpLoadErrroListener("登录超时，请重新登录",TYPE_IMAGE);
            return;
        }
        RequestBody fileBody = RequestBody.create(MediaType.parse("application/otcet-stream"), file);
        //创建MultipartBody,给RequestBody进行设置
        MultipartBody multipartBody =null;
        try {
            multipartBody = new MultipartBody.Builder().setType(MultipartBody.FORM)
                    .addFormDataPart("access_token", token)
                    .addFormDataPart("file", file.getName(), fileBody)
                    .build();
        }catch (Exception e){
            e.printStackTrace();
            multipartBody = new MultipartBody.Builder().setType(MultipartBody.FORM)
                    .addFormDataPart("access_token", token)
                    .addFormDataPart("file", testStr(file.getName()), fileBody)
                    .build();
        }

        //创建Request
        Request request = new Request.Builder()
                .url(IMSConfig.HTTP_IM_UPDATE_IMAGE)
                .post(multipartBody)
                .build();

        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(onUpdataFileListener!=null){
                            onUpdataFileListener.onUpLoadErrroListener(e.getMessage(),TYPE_IMAGE);
                            Log.e("----上传图片失败-",e.getMessage());
                        }
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.e("----上传图片-","成功");
                IMUpdataFileBean bean =null;
                try{
                    bean = new Gson().fromJson(response.body().string(), IMUpdataFileBean.class);
                }catch (Exception e){
                    return;
                }
                IMUpdataFileBean finalBean = bean;
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(onUpdataFileListener!=null){
                            onUpdataFileListener.onUpLoadImageListener(finalBean,file.getPath(), beans);
                        }
                    }
                });
            }
        });
    }

    /**
     * 上传视频资源（进度条）
     */
    public void handleUpdataVideo(Activity activity,final UpdataPickureBean bean) {
        String token = IMPreferenceUtil.getPreference_String(IMSConfig.SAVE_IM_TOKEN, null);
        if(TextUtils.isEmpty(token)){
            onUpdataFileListener.onUpLoadErrroListener("登录超时，请重新登录",TYPE_VIDEO);
            return;
        }
        File file = new File(bean.getUrl());
        RequestBody fileBody = RequestBody.create(MediaType.parse("application/otcet-stream"), file);
        MultipartBody multipartBody =null;
        try {
            multipartBody = new MultipartBody.Builder().setType(MultipartBody.FORM)
                    .addFormDataPart("access_token", token)
                    .addFormDataPart("file", file.getName(), fileBody)
                    .build();
        }catch (Exception e){
            e.printStackTrace();
            multipartBody = new MultipartBody.Builder().setType(MultipartBody.FORM)
                    .addFormDataPart("access_token", token)
                    .addFormDataPart("file", testStr(file.getName()), fileBody)
                    .build();
        }
        ExMultipartBody exMultipartBody = new ExMultipartBody(multipartBody, new ExMultipartBody.UploadProgressListener() {
            @Override
            public void onProgress(final long total, final long current) {
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(onUpdataFileListener!=null){
                            onUpdataFileListener.onUpLoadProcessListener((int)((current*1.0f/total)*100));
                        }
                    }
                });

            }
        });
        //创建Request
        Request request = new Request.Builder()
                .url(IMSConfig.HTTP_IM_UPDATE_IMAGE)
                .post(exMultipartBody)
                .build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(onUpdataFileListener!=null){
                            onUpdataFileListener.onUpLoadErrroListener(e.getMessage(),TYPE_VIDEO);
                        }
                    }
                });


            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                IMUpdataFileBean imUpdataFile =null;
                try{
                    imUpdataFile = new Gson().fromJson(response.body().string(), IMUpdataFileBean.class);
                }catch (Exception e){
                    return;
                }
                IMLogUtil.d("MyOwnTag:", "上传视频 " + "(onSuccess) " + new Gson().toJson(imUpdataFile.data));
                IMUpdataFileBean finalImUpdataFile = imUpdataFile;
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(onUpdataFileListener!=null){
                            onUpdataFileListener.onUpLoadVideoListener(finalImUpdataFile,bean.getUrl(),bean);
                        }
                    }
                });
            }
        });
    }

    private static String REGEX_CHINESE = "[\u4e00-\u9fa5]";// 中文正则

    public String testStr(String str) {
        if(TextUtils.isEmpty(str)){
            return System.currentTimeMillis()+".png";
        }
        // 去除中文
        Pattern pat = Pattern.compile(REGEX_CHINESE);
        Matcher mat = pat.matcher(str);
        return  mat.replaceAll("");
    }
}
