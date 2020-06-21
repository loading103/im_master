package com.rhby.cailexun.net.http;


import com.android.im.imbean.SplashAdBean;
import com.android.im.imnet.IMHttpResult;
import com.rhby.cailexun.bean.BrowseRecordBean;
import com.rhby.cailexun.bean.LoginBean;
import com.android.im.imbean.MyBgBeanData;
import com.rhby.cailexun.bean.VersonBeanData;
import com.rhby.cailexun.net.HttpResult;
import com.android.nettylibrary.greendao.entity.IMPersonBean;
import com.android.nettylibrary.http.IMLoginData;

import java.util.List;

import io.reactivex.Observable;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.POST;



public interface ApiManagerService<T> {
    /**
     * id登录
     */
    @POST("mock/ssologin-data")
    Observable<HttpResult<IMLoginData>> httpCreatNewUserJsonJson(
            @Body RequestBody body
    );

    /**
     * 获取验证码
     */
    @POST("sms/login-verify")
    Observable<HttpResult<String>> getCode(
            @Body RequestBody body
    );

    /**
     * 登录 手机号码+短信验证码登陆（loginType=2）
     *      账号+密码登陆（loginType=1）
     */
    @POST("ssologin")
    Observable<HttpResult<LoginBean>> doLogin(
            @Body RequestBody body
    );

    /**
     * 登录 手机号码+短信验证码登陆（loginType=2）
     *      账号+密码登陆（loginType=1）
     *      为了解决后台返回data字段数据类型不一致的问题
     */
    @POST("ssologin")
    Observable<HttpResult<Object>> doLogin2(
            @Body RequestBody body
    );

    /**
     * 查询浏览记录
     */
    @POST("discover-browser/get")
    Observable<HttpResult<List<BrowseRecordBean>>> getBrowseRecord(
            @Body RequestBody body
    );

    /**
     * 获取个人信息
     */
    @POST("customer/self-info")
    Observable<HttpResult<IMPersonBean>> getPersonInfo(
            @Body RequestBody body
    );

    /**
     * 注册-获取短信验证码
     */
    @POST("sms/registry-verify")
    Observable<HttpResult<String>> getRegisterCode(
            @Body RequestBody body
    );

    /**
     * 注册-校验短信验证码
     */
    @POST("registry/valid")
    Observable<HttpResult<Object>> checkCode(
            @Body RequestBody body
    );
    /**
     * 注册-查看用户名是否被使用
     */
    @POST("registry/check")
    Observable<HttpResult<String>> checkUsername(
            @Body RequestBody body
    );
    /**
     * 注册
     */
    @POST("registry/commit")
    Observable<HttpResult<LoginBean>> doRegisterCommit(
            @Body RequestBody body
    );

    /**
     * 修改密码-获取短信验证码(步骤1)
     */
    @POST("sms/credentials-verify")
    Observable<HttpResult<Object>> getCredentialsVerify(
            @Body RequestBody body
    );

    /**
     * 修改密码-校验短信验证码(步骤2)
     */
    @POST("customer/modify-credentials-validate")
    Observable<HttpResult<String>> checkCodeForget(
            @Body RequestBody body
    );

    /**
     * 修改密码-提交新密码（步骤3）
     */
    @POST("customer/modify-credentials-commit")
    Observable<HttpResult<String>> doForgetCommit(
            @Body RequestBody body
    );
    /**
     * 修改手机号码-获取短信验证码(步骤1)
     */
    @POST("sms/modify-mobile-verify")
    Observable<HttpResult<Object>> getCodeModify(
            @Body RequestBody body
    );
    /**
     * 修改手机号码-校验短信验证码（步骤2）
     */
    @POST("customer/modify-mobile-commit")
    Observable<HttpResult<Object>> doCommitModify(
            @Body RequestBody body
    );
    /**
     * 修改会员信息
     */
    @POST("customer/update")
    Observable<HttpResult<Object>> updatePersonData(
            @Body RequestBody body
    );
    /**
     *获取广告
     */
    @POST("front-ads/get")
    Observable<IMHttpResult<List<SplashAdBean>>>httpGetAdJson(
    );
    /**
     *获取版本跟新
     */
    @POST("commons/check-update")
    Observable<IMHttpResult<VersonBeanData>>httpGetVersonJson(
            @Body RequestBody body
    );
    /**
     *获取背景图片
     */
    @POST("commons/backgroup")
    Observable<HttpResult<MyBgBeanData>>httpGetMyBgJson(
    );


}