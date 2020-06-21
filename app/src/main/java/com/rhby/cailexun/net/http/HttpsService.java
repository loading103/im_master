package com.rhby.cailexun.net.http;

import com.android.im.imbean.SplashAdBean;
import com.android.im.imnet.IMHttpResult;
import com.android.im.imnet.IMHttpResultObserver;
import com.android.im.imnet.IMTransformUtils;
import com.rhby.cailexun.bean.BrowseRecordBean;
import com.rhby.cailexun.bean.LoginBean;
import com.android.im.imbean.MyBgBeanData;
import com.rhby.cailexun.bean.VersonBeanData;
import com.rhby.cailexun.net.HttpResult;
import com.rhby.cailexun.net.HttpResultObserver;
import com.rhby.cailexun.net.TransformUtils;
import com.rhby.cailexun.net.base.ServiceFactory;
import com.android.nettylibrary.greendao.entity.IMPersonBean;
import com.android.nettylibrary.http.IMLoginData;

import java.util.List;

import okhttp3.RequestBody;

public class HttpsService {
    public static void creatNewUserJson(RequestBody body, HttpResultObserver<IMLoginData> httpResultSubscriber) {
        //接口
        ServiceFactory.getInstance()
                .createService(ApiManagerService.class)
                .httpCreatNewUserJsonJson(body)
                .compose(TransformUtils.<HttpResult<IMLoginData>>main_io())
                .subscribe(httpResultSubscriber);
    }

    public static void creatGetCodeJson(RequestBody body, HttpResultObserver<String> httpResultSubscriber) {
        //接口
        ServiceFactory.getInstance()
                .createService(ApiManagerService.class)
                .getCode(body)
                .compose(TransformUtils.<HttpResult<String>>main_io())
                .subscribe(httpResultSubscriber);
    }

    public static void doLogin(RequestBody body, HttpResultObserver<LoginBean> httpResultSubscriber) {
        //接口
        ServiceFactory.getInstance()
                .createService(ApiManagerService.class)
                .doLogin(body)
                .compose(TransformUtils.<HttpResult<LoginBean>>main_io())
                .subscribe(httpResultSubscriber);
    }

    public static void doLogin2(RequestBody body, HttpResultObserver<Object> httpResultSubscriber) {
        //接口
        ServiceFactory.getInstance()
                .createService(ApiManagerService.class)
                .doLogin2(body)
                .compose(TransformUtils.<HttpResult<LoginBean>>main_io())
                .subscribe(httpResultSubscriber);
    }

    public static void getBrowseRecord(RequestBody body, HttpResultObserver<List<BrowseRecordBean>> httpResultSubscriber) {
        //接口
        ServiceFactory.getInstance()
                .createService(ApiManagerService.class)
                .getBrowseRecord(body)
                .compose(TransformUtils.<HttpResult<LoginBean>>main_io())
                .subscribe(httpResultSubscriber);
    }
    public static void getPersonInfo(RequestBody body, HttpResultObserver<IMPersonBean> httpResultSubscriber) {
        //接口
        ServiceFactory.getInstance()
                .createService(ApiManagerService.class)
                .getPersonInfo(body)
                .compose(TransformUtils.<HttpResult<LoginBean>>main_io())
                .subscribe(httpResultSubscriber);
    }
    public static void getRegisterCode(RequestBody body, HttpResultObserver<String> httpResultSubscriber) {
        //接口
        ServiceFactory.getInstance()
                .createService(ApiManagerService.class)
                .getRegisterCode(body)
                .compose(TransformUtils.<HttpResult<String>>main_io())
                .subscribe(httpResultSubscriber);
    }
    public static void checkCode(RequestBody body, HttpResultObserver<String> httpResultSubscriber) {
        //接口
        ServiceFactory.getInstance()
                .createService(ApiManagerService.class)
                .checkCode(body)
                .compose(TransformUtils.<HttpResult<String>>main_io())
                .subscribe(httpResultSubscriber);
    }
    public static void checkUsername(RequestBody body, HttpResultObserver<Object> httpResultSubscriber) {
        //接口
        ServiceFactory.getInstance()
                .createService(ApiManagerService.class)
                .checkUsername(body)
                .compose(TransformUtils.<HttpResult<String>>main_io())
                .subscribe(httpResultSubscriber);
    }
    public static void doRegisterCommit(RequestBody body, HttpResultObserver<LoginBean> httpResultSubscriber) {
        //接口
        ServiceFactory.getInstance()
                .createService(ApiManagerService.class)
                .doRegisterCommit(body)
                .compose(TransformUtils.<HttpResult<String>>main_io())
                .subscribe(httpResultSubscriber);
    }

    public static void getCredentialsVerify(RequestBody body, HttpResultObserver<Object> httpResultSubscriber) {
        //接口
        ServiceFactory.getInstance()
                .createService(ApiManagerService.class)
                .getCredentialsVerify(body)
                .compose(TransformUtils.<HttpResult<String>>main_io())
                .subscribe(httpResultSubscriber);
    }
    public static void checkCodeForget(RequestBody body, HttpResultObserver<String> httpResultSubscriber) {
        //接口
        ServiceFactory.getInstance()
                .createService(ApiManagerService.class)
                .checkCodeForget(body)
                .compose(TransformUtils.<HttpResult<String>>main_io())
                .subscribe(httpResultSubscriber);
    }
    public static void doForgetCommit(RequestBody body, HttpResultObserver<String> httpResultSubscriber) {
        //接口
        ServiceFactory.getInstance()
                .createService(ApiManagerService.class)
                .doForgetCommit(body)
                .compose(TransformUtils.<HttpResult<String>>main_io())
                .subscribe(httpResultSubscriber);
    }
    public static void getCodeModify(RequestBody body, HttpResultObserver<Object> httpResultSubscriber) {
        //接口
        ServiceFactory.getInstance()
                .createService(ApiManagerService.class)
                .getCodeModify(body)
                .compose(TransformUtils.<HttpResult<String>>main_io())
                .subscribe(httpResultSubscriber);
    }
    public static void doCommitModify(RequestBody body, HttpResultObserver<Object> httpResultSubscriber) {
        //接口
        ServiceFactory.getInstance()
                .createService(ApiManagerService.class)
                .doCommitModify(body)
                .compose(TransformUtils.<HttpResult<String>>main_io())
                .subscribe(httpResultSubscriber);
    }
    public static void updatePersonData(RequestBody body, HttpResultObserver<Object> httpResultSubscriber) {
        //接口
        ServiceFactory.getInstance()
                .createService(ApiManagerService.class)
                .updatePersonData(body)
                .compose(TransformUtils.<HttpResult<String>>main_io())
                .subscribe(httpResultSubscriber);
    }

    /**
     * 获取广告
     */
    public static void getGetAdJson(RequestBody body, IMHttpResultObserver<List<SplashAdBean>> httpResultSubscriber) {
        //接口
        ServiceFactory.getInstance()
                .createService(ApiManagerService.class)
                .httpGetAdJson()
                .compose(IMTransformUtils.<IMHttpResult<List<SplashAdBean>>>main_io())
                .subscribe(httpResultSubscriber);
    }
    /**
     * 获取版本跟新
     */
    public static void getVersionJson(RequestBody body, IMHttpResultObserver<VersonBeanData> httpResultSubscriber) {
        //接口
        ServiceFactory.getInstance()
                .createService(ApiManagerService.class)
                .httpGetVersonJson(body)
                .compose(IMTransformUtils.<IMHttpResult<VersonBeanData>>main_io())
                .subscribe(httpResultSubscriber);
    }

    /**
     * 获取背景图片
     */
    public static void getMyBgJson(HttpResultObserver<MyBgBeanData> httpResultSubscriber) {
        //接口
        ServiceFactory.getInstance()
                .createService(ApiManagerService.class)
                .httpGetMyBgJson()
                .compose(TransformUtils.<HttpResult<MyBgBeanData>>main_io())
                .subscribe(httpResultSubscriber);
    }
}
