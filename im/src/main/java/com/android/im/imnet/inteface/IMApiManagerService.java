package com.android.im.imnet.inteface;


import com.android.im.imbean.IMAliyunTokenBean;
import com.android.im.imbean.MyBgBeanData;
import com.android.im.imbean.RecentlyUseBean;
import com.android.im.imbean.SmallProgramBean;
import com.android.im.imbean.IMAddFriendBean;
import com.android.im.imbean.IMApplyInforBean;
import com.android.im.imbean.IMBetDetailBean;
import com.android.im.imbean.IMBetListBean;
import com.android.im.imbean.IMCompanyBean;
import com.android.im.imbean.IMFriendsBean;
import com.android.im.imbean.IMGameTypeBean;
import com.android.im.imbean.IMGetRedPickBean;
import com.android.im.imbean.IMHttpCommonBean;
import com.android.im.imbean.IMImageViewBean;
import com.android.im.imbean.IMRedPickInformationBean;
import com.android.im.imbean.IMSSLoginBean;
import com.android.im.imbean.IMSendRedPickRecordList;
import com.android.im.imbean.IMSingleRedBackBean;
import com.android.im.imbean.SplashAdBean;
import com.android.im.imnet.IMHttpResult;
import com.android.im.imnet.bean.IMUpdataFileBean;
import com.android.nettylibrary.greendao.entity.IMGroupBean;
import com.android.nettylibrary.greendao.entity.IMPersonBean;
import com.android.nettylibrary.http.IMGroupNoticeBean;
import com.android.nettylibrary.http.IMImTokenBean;
import com.android.nettylibrary.http.IMKeFuInforBean;
import com.android.nettylibrary.http.IMLoginData;
import com.android.nettylibrary.http.IMUserInforBean;

import java.util.List;

import io.reactivex.Observable;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

/**
 * Created by lin on 16/12/21.
 * 用户管理接口
 */
public interface IMApiManagerService<T> {

    /**
     * 上传文件(商家没有token时上传)
     */
    @Multipart
    @POST("/api/v1/message/uploadFile")
    Observable<IMHttpResult<IMUpdataFileBean.IMUpdataFile>> upShopFile(
            @Part MultipartBody.Part file
    );

    /**
     * id登录
     */
    @POST("mock/ssologin-data")
    Observable<IMHttpResult<IMLoginData>> httpCreatNewUserJsonJson(
            @Body RequestBody body
    );

    /**
     * 单点登录
     */
    @POST("ssologin")
    Observable<IMHttpResult<IMSSLoginBean>> httpLoginJson(
            @Body RequestBody body
    );
    /**
     * 个人信息（用单点登录token获取个人信息）
     */
    @POST("customer/self-info")
    Observable<IMHttpResult<IMUserInforBean.UserInforData>> httpGetSelfInforJson();

    /**
     * 获取会话列表（用单点登录token获取个人信息）
     */
    @POST("friendApply/findFriend")
    Observable<IMHttpResult<List<IMPersonBean>>> httpGetConversationListJson();
    /**
     * 获取群列表（用单点登录token获取个人信息）
     */
    @POST("group/listGroup")
    Observable<IMHttpResult<List<IMGroupBean>>> httpGetGroupListJson();

    /**
     * 获取群列表（用单点登录token获取个人信息）
     */
    @POST("member/info")
    Observable<IMHttpResult<IMKeFuInforBean>> httpGetPersonDataJson(
            @Body RequestBody body
    );
    /**
     * 获取群详情
     */
    @POST("group/info")
    Observable<IMHttpResult<IMGroupNoticeBean.IMGroupNoticeDetail>> httpGetGroupDataJson(
            @Body RequestBody body
    );

    /**
     * 投注列表（分享）
     */
    @POST("betting/order-list")
    Observable<IMHttpResult<IMBetListBean>> httpGetBetListJson(
            @Body RequestBody body
    );
    /**
     * 投注详情
     */
    @POST("betting/order-detail")
    Observable<IMHttpResult<IMBetDetailBean>>httpGetBetDatailJson(
            @Body RequestBody body
    );
    /**
     * 投注详情
     */
    @POST("betting/order-list")
    Observable<IMHttpResult<IMGameTypeBean>>httpGameListJson(
    );
    /**
     * 注单分享
     */
    @POST("betting/share")
    Observable<IMHttpResult<String>>httpBetShareJson(
            @Body RequestBody body
    );
    /**
     * 跟投
     */
    @POST("betting/sumbit")
    Observable<IMHttpResult<String>>httpFollowBetJson(
            @Body RequestBody body
    );
    /**
     * 发送包
     */
    @POST("redEnvelopes/createRedPacket")
    Observable<IMHttpResult<IMSingleRedBackBean>>httpSendRedPickJson(
            @Body RequestBody body
    );

    /**
     * 获取红包的信息
     */
    @POST("redEnvelopes/getRedPacketInfo")
    Observable<IMHttpResult<IMRedPickInformationBean>>httpgetRedStatePickJson(
            @Body RequestBody body
    );

    /**
     * 获取红包的信息
     */
    @POST("redEnvelopes/getRedPacketRecords")
    Observable<IMHttpResult<IMRedPickInformationBean>>httpgetLastRedStatePickJson(
            @Body RequestBody body
    );
    /**
     * 抢红包
     */
    @POST("redEnvelopes/grabRedPacketInfo")
    Observable<IMHttpResult<IMGetRedPickBean>>httpGetRedPickJsonJson(
            @Body RequestBody body
    );

    /**
     * 查询会员或者客服发送的红包列表
     */
    @POST("redEnvelopes/sendRecordList")
    Observable<IMHttpResult<IMSendRedPickRecordList.IMSendRedPickRecordListBean>>httpGetSendRedPickListJson(
            @Body RequestBody body
    );

    /**
     * 用户领取的红包列表
     */
    @POST("redEnvelopes/grabRecordList")
    Observable<IMHttpResult<IMSendRedPickRecordList.IMSendRedPickRecordListBean>>httpGetRedPickListJson(
            @Body RequestBody body
    );

    /**
     *base64上传图片
     */
    @POST("uploader/simple-image")
    Observable<IMHttpResult<IMImageViewBean>>httpGetUpdataPictureJson(
            @Body RequestBody body
    );

    /**
     *修改头像
     */
    @POST("customer/update")
    Observable<IMHttpResult<String>>httpGetUpdataInforJson(
            @Body RequestBody body
    );
    /**
     * 获取公司的总开关
     */
    @POST("app/info")
    Observable<IMHttpResult<IMCompanyBean>>httpGetCompanySwithJson(
    );
    /**
     * 记录进群
     */
    @POST("group/enter")
    Observable<IMHttpResult<String>>httpEnterGroupJson(
            @Body RequestBody body
    );
    /**
     * 获取imtoken
     */
    @POST("oauth/token")
    Observable<IMHttpResult<IMImTokenBean>>httpGetIMTokenJson(
    );
    /**
     * 获取群成员信息
     */
    @POST("customer/info")
    Observable<IMHttpResult<IMUserInforBean.UserInforData>>httpGetMemberInforJson(
            @Body RequestBody body
    );

    /**
     * 创建群聊
     */
    @POST("group/create")
    Observable<IMHttpResult<IMHttpCommonBean>>httpcreatNewGroupJson(
            @Body RequestBody body
    );
    /**
     * 解散群聊
     */
    @POST("group/deleteGroups")
    Observable<IMHttpResult<String>>httpdeleteNewGroupJson(
            @Body RequestBody body
    );
    /**
     * 退出群聊
     */
    @POST("group-member/quit")
    Observable<IMHttpResult<String>>httpquetNewGroupJson(
            @Body RequestBody body
    );
    /**
     * 移除群成员
     */
    @POST("group-member/remove")
    Observable<IMHttpResult<String>>httpRemoveGroupMemberJson(
            @Body RequestBody body
    );
    /**
     * 用户拉人进群
     */
    @POST("group-member/pull")
    Observable<IMHttpResult<String>>httpPullGroupMemberJson(
            @Body RequestBody body
    );
    /**
     * 用户拉人进群
     */
    @POST("friendApply/checkFriend")
    Observable<IMHttpResult<List<IMPersonBean>>>httpSerchNewFriendJson(
            @Body RequestBody body
    );
    /**
     * 添加好友
     */
    @POST("friendApply/saveFriend")
    Observable<IMHttpResult<IMAddFriendBean>>httpAddNewFriendJson(
            @Body RequestBody body
    );
    /**
     * 删除好友
     */
    @POST("friendApply/deleteFriend")
    Observable<IMHttpResult<String>>httpDeleteFriendJson(
            @Body RequestBody body
    );
    /**
     * 获取当前登陆人的 申请好友列表
     */
    @POST("friendApply/friendApply")
    Observable<IMHttpResult<List<IMFriendsBean>>>httpGetFriendApplyJson(
    );

    /**
     * 同意添加好友
     */
    @POST("friendApply/agreed")
    Observable<IMHttpResult<String>>httpAgreeAddFriendJson(
            @Body RequestBody body
    );
    /**
     *  拒绝添加操作
     */
    @POST("friendApply/refused")
    Observable<IMHttpResult<String>>httpRefuseAddFriendJson(
            @Body RequestBody body
    );
    /**
     *  申请人详情
     */
    @POST("friendApply/applyForDetails")
    Observable<IMHttpResult<IMApplyInforBean>>httpApplyForDetailsJson(
            @Body RequestBody body
    );


    /**
     *修改群聊头像
     */
    @POST("group/update")
    Observable<IMHttpResult<String>>httpUpdateGroupHeadJson(
            @Body RequestBody body
    );
    /**
     *修改群聊公告
     */
    @POST("group/updateGroupNotice")
    Observable<IMHttpResult<String>>httpUpdateGroupNoticeJson(
            @Body RequestBody body
    );
    /**
     *获取广告
     */
    @POST("front-ads/get")
    Observable<IMHttpResult<List<SplashAdBean>>>httpGetAdJson(
            @Body RequestBody body
    );
    /**
     *获取收藏
     */
    @POST("collection/get")
    Observable<IMHttpResult<List<SmallProgramBean>>>getCollection(
            @Body RequestBody body
    );
    /**
     *添加收藏
     */
    @POST("collection/add")
    Observable<IMHttpResult<String>>addCollection(
            @Body RequestBody body
    );
    /**
     *取消收藏
     */
    @POST("collection/delete")
    Observable<IMHttpResult<String>>cancleCollection(
            @Body RequestBody body
    );
    /**
     *最近使用
     */
    @POST("recently-use/get")
    Observable<IMHttpResult<RecentlyUseBean>>getRecentlyUse(
            @Body RequestBody body
    );
    /**
     *搜索小程序
     */
    @POST("front-program/get")
    Observable<IMHttpResult<List<SmallProgramBean>>>getSearchProgram(
            @Body RequestBody body
    );
    /**
     *添加最近使用
     */
    @POST("recently-use/add")
    Observable<IMHttpResult<String>>addRecentlyUse(
            @Body RequestBody body
    );
    /**
     *删除最近使用
     */
    @POST("recently-use/delete")
    Observable<IMHttpResult<String>>deleteRecentlyUse(
            @Body RequestBody body
    );
    /**
     *小程序详情
     */
    @POST("front-program/detail")
    Observable<IMHttpResult<SmallProgramBean>>getProgramDetails(
            @Body RequestBody body
    );

    /**
     *小程序详情
     */
    @POST("aliyun-oss/token")
    Observable<IMHttpResult<IMAliyunTokenBean>>getAliyunToken(
    );
    /**
     *屏蔽好友
     */
    @POST("customer/shield")
    Observable<IMHttpResult<String>>getShieldPersonJson(
            @Body RequestBody body
    );
    /**
     *投诉Cx
     */
    @POST("complaint/submit")
    Observable<IMHttpResult<Object>>getComplaintPersonJson(
            @Body RequestBody body
    );

    /**
     *获取背景图片
     */
    @POST("commons/backgroup")
    Observable<IMHttpResult<MyBgBeanData>>httpGetMyBgJson(
    );

}

