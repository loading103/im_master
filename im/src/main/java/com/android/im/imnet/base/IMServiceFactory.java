package com.android.im.imnet.base;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.android.im.imnet.IMBaseConstant;
import com.android.nettylibrary.IMSConfig;
import com.android.nettylibrary.utils.IMLogUtil;
import com.android.nettylibrary.utils.IMPreferenceUtil;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import io.reactivex.annotations.NonNull;
import okhttp3.Cache;
import okhttp3.FormBody;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;


public class IMServiceFactory {

    private final Gson mGsonDateFormat;
    private Retrofit retrofit;
    private   static IMServiceFactory mInstance;
    private Context context;

    public IMServiceFactory(Context context) {
        this.context=context;
        mGsonDateFormat = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd hh:mm:ss")
                .create();
    }
    public  static void init(Context context){
        if(mInstance == null) {
            mInstance = new IMServiceFactory(context);
        }
    }

    public static IMServiceFactory getInstance() {
        return mInstance;
    }

    /**
     * create a service
     *
     * isim==true  代表正常的http
     * ==false  代表im那边的http
     */
    public <S> S createService(Class<S> serviceClass,boolean isim) {
        String baseUrl=null;
        if(isim){
            baseUrl=IMSConfig.HTTP_BASE_URL;
        }else {
            baseUrl=IMSConfig.HTTP_BASE_IM_URL;
        }
        retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(getOkHttpClient(isim))
                .addConverterFactory(GsonConverterFactory.create(mGsonDateFormat))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();

        return retrofit.create(serviceClass);
    }

    private final static long DEFAULT_TIMEOUT = 20;
    private OkHttpClient getOkHttpClient(final boolean isim) {
        //定制OkHttp
        OkHttpClient.Builder httpClientBuilder = new OkHttpClient.Builder();

        httpClientBuilder.connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS);
        // 添加通用的Header
        String token =null;
        if(isim){
            token = IMPreferenceUtil.getPreference_String(IMSConfig.SAVE_TOKEN, "");
        }else {
            token = IMPreferenceUtil.getPreference_String(IMSConfig.SAVE_IM_TOKEN, "");
        }

        final String finalToken = token;
        httpClientBuilder.addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request request = chain.request();
                Request.Builder builder = request.newBuilder();
                if(!TextUtils.isEmpty(finalToken)){
                    if(isim){
                        builder.addHeader("Authorization", finalToken);
                        IMLogUtil.d("tag", "Authorization " +"(Authorization)token== " + finalToken);
                    }else {
                        builder.addHeader("Authorization", "bearer "+finalToken);
                        Log.e("tag", "access_token " +"(access_token)token== " + finalToken);
                    }

                }
                return chain.proceed(builder.build());
            }
        });
        //设置超时时间
        httpClientBuilder.connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS);
        httpClientBuilder.writeTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS);
        httpClientBuilder.readTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS);
        httpClientBuilder.addNetworkInterceptor(new LogInterceptor());

        //设置缓存
        File httpCacheDirectory = new File(context.getCacheDir(), "OkHttpCache");
        httpClientBuilder.cache(new Cache(httpCacheDirectory, 10 * 1024 * 1024));
        return httpClientBuilder.build();
    }

    /**
     *打印日志
     */
    public class LogInterceptor implements Interceptor {
        @Override
        public Response intercept(@NonNull Interceptor.Chain chain) throws IOException {
            Request request = chain.request();
            String url = request.url().toString();
            String method = request.method();
            Log.e("---请求数据：", String.format(Locale.getDefault(), " %s请求： url = %s", method, url));
            //the request body
            RequestBody requestBody = request.body();
            if (requestBody != null) {
                if("POST".equals(method)){
                    StringBuilder sb = new StringBuilder();
                    if (request.body() instanceof FormBody) {
                        FormBody body = (FormBody) request.body();
                        for (int i = 0; i < body.size(); i++) {
                            sb.append(body.encodedName(i) + " ： " + body.encodedValue(i) + ",");
                        }
                        sb.delete(sb.length() - 1, sb.length());
                        Log.e("---传递参数：", sb.toString());
                    }else {
                        Log.e("---传递参数：", requestBody.toString());
                    }
                }
            }
            Response response = chain.proceed(request);
            ResponseBody body = response.body();
            BufferedSource source;
            if (body != null) {
                source = body.source();
                source.request(Long.MAX_VALUE); // Buffer the entire body.
                Buffer buffer = source.buffer();
                Charset charset = Charset.defaultCharset();
                MediaType contentType = body.contentType();
                if (contentType != null) {
                    charset = contentType.charset(charset);
                }
                if (charset != null) {
                    String bodyString = buffer.clone().readString(charset);
                    Log.e("---返回数据", String.format("%s", bodyString));
                }
            }
            return response;
        }
    }
}
