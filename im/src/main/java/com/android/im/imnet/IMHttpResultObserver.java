package com.android.im.imnet;

import com.android.nettylibrary.utils.IMNetworkUtil;
import com.google.gson.JsonSyntaxException;

import java.net.ConnectException;
import java.net.SocketTimeoutException;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public abstract class IMHttpResultObserver<T> implements Observer<IMHttpResult<T>> {

    private Disposable mDisposable;
    @Override
    public void onError(Throwable e) {
        if (e != null) {
            if (e instanceof SocketTimeoutException) {
                _onError(new Throwable("网络连接异常，请稍后再试~"));
            } else if (e instanceof ConnectException) {
                boolean isNet = IMNetworkUtil.isConnected();
                if (!isNet) {
                    _onError(new Throwable("网络不通，请检查网络再试~"));
                }else {
                    _onError(new Throwable("未能连接到服务器~"));
                }

            }else if (e instanceof JsonSyntaxException){
                _onError(new Throwable("解析失败~"));
            }else{
                _onError(new Throwable(e.getMessage()));
            }
        } else {
            _onError(new Throwable("未知错误~"));
        }
    }

    @Override
    public void onComplete() {

    }

    @Override
    public void onSubscribe(Disposable d) {
        mDisposable = d;
    }

    @Override
    public void onNext(IMHttpResult<T> t) {
        if (t.code.endsWith("OK") || (t.status!=null &&t.status.equals("200"))){
            onSuccess(t.data ,t.message);
        }
        else{
            _onErrorLocal(new Throwable(),t.message,t.code);
        }
    }
    //请求成功
    public abstract void onSuccess(T t , String message);
    //网络异常
    public abstract void _onError(Throwable e);
    //本地服务器异常
    public abstract void _onErrorLocal(Throwable e, String message, String code);
    //token过期处理
//    public abstract void _onLoginOut(Throwable e, String message, int code);

    public void unSubscribe() {
        if (mDisposable != null && !mDisposable.isDisposed()) {
            mDisposable.dispose();
        }
    }




}