package com.android.im.imnet.http;/**
 * Created by del on 17/3/8.
 */

import android.content.Context;

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
import com.android.im.imnet.IMHttpResultObserver;
import com.android.im.imnet.IMTransformUtils;
import com.android.im.imnet.base.IMServiceFactory;
import com.android.im.imnet.bean.IMUpdataFileBean;
import com.android.im.imnet.inteface.IMApiManagerService;
import com.android.nettylibrary.greendao.entity.IMGroupBean;
import com.android.nettylibrary.greendao.entity.IMPersonBean;
import com.android.nettylibrary.http.IMGroupNoticeBean;
import com.android.nettylibrary.http.IMImTokenBean;
import com.android.nettylibrary.http.IMKeFuInforBean;
import com.android.nettylibrary.http.IMLoginData;
import com.android.nettylibrary.http.IMUserInforBean;

import java.io.File;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

/**
 * created by lbw at 17/3/8
 */
public class IMHttpsService {
    /**
     * 图片上传
     */
    public static void upFiles(List<String> path, Context context,IMHttpResultObserver<IMUpdataFileBean.IMUpdataFile> httpResultSubscriber) {
        MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM);//表单类型
        for (int i = 0; i < path.size(); i++) {
            File file = new File(path.get(i));
            RequestBody photoRequestBody = RequestBody.create(MediaType.parse("image/*"), file);
            String name = file.getName();
            String text = name.replaceAll(",", "_");//如果图片名字带有逗号替换掉
            builder.addFormDataPart("file", "", photoRequestBody);
        }
        MultipartBody.Part parts = builder.build().parts().get(0);
        //接口
        IMServiceFactory.getInstance()
                .createService(IMApiManagerService.class,false)
                .upShopFile(parts)
                .compose(IMTransformUtils.<IMHttpResult<List<IMUpdataFileBean.IMUpdataFile>>>main_io())
                .subscribe(httpResultSubscriber);

    }





    /**
     * id登录
     */
    public static void creatNewUserJson(RequestBody body, IMHttpResultObserver<IMLoginData> httpResultSubscriber) {
        //接口
        IMServiceFactory.getInstance()
                .createService(IMApiManagerService.class,true)
                .httpCreatNewUserJsonJson(body)
                .compose(IMTransformUtils.<IMHttpResult<IMLoginData>>main_io())
                .subscribe(httpResultSubscriber);
    }
    /**
     * 单点登录
     */
    public static void LoginJson(RequestBody body, IMHttpResultObserver<IMSSLoginBean> httpResultSubscriber) {
        //接口
        IMServiceFactory.getInstance()
                .createService(IMApiManagerService.class,true)
                .httpLoginJson(body)
                .compose(IMTransformUtils.<IMHttpResult<String>>main_io())
                .subscribe(httpResultSubscriber);
    }
    /**
     * 个人信息（用单点登录token获取个人信息）
     */
    public static void GetSelfInforJson(IMHttpResultObserver<IMUserInforBean.UserInforData> httpResultSubscriber) {
        //接口
        IMServiceFactory.getInstance()
                .createService(IMApiManagerService.class,true)
                .httpGetSelfInforJson()
                .compose(IMTransformUtils.<IMHttpResult<String>>main_io())
                .subscribe(httpResultSubscriber);
    }

    /**
     * 获取会话列表（用单点登录token获取个人信息）
     */
    public static void GetConversationListJson( IMHttpResultObserver<List<IMPersonBean>> httpResultSubscriber) {
        //接口
        IMServiceFactory.getInstance()
                .createService(IMApiManagerService.class,true)
                .httpGetConversationListJson()
                .compose(IMTransformUtils.<IMHttpResult<String>>main_io())
                .subscribe(httpResultSubscriber);
    }
//
    /**
     * 获取群列表（用单点登录token获取个人信息）
     */
    public static void GetGroupListJson(IMHttpResultObserver<List<IMGroupBean>> httpResultSubscriber) {
        //接口
        IMServiceFactory.getInstance()
                .createService(IMApiManagerService.class,true)
                .httpGetGroupListJson()
                .compose(IMTransformUtils.<IMHttpResult<String>>main_io())
                .subscribe(httpResultSubscriber);
    }


    /**
     * 获取客户的个人信息
     */
    public static void getPersonDataJson(RequestBody body,IMHttpResultObserver<IMKeFuInforBean> httpResultSubscriber) {
        //接口
        IMServiceFactory.getInstance()
                .createService(IMApiManagerService.class,true)
                .httpGetPersonDataJson(body)
                .compose(IMTransformUtils.<IMHttpResult<String>>main_io())
                .subscribe(httpResultSubscriber);
    }
    /**
     * 获取群信息
     */
    public static void getGroupDataJson(RequestBody body,IMHttpResultObserver<IMGroupNoticeBean.IMGroupNoticeDetail> httpResultSubscriber) {
        //接口
        IMServiceFactory.getInstance()
                .createService(IMApiManagerService.class,true)
                .httpGetGroupDataJson(body)
                .compose(IMTransformUtils.<IMHttpResult<IMGroupNoticeBean.IMGroupNoticeDetail>>main_io())
                .subscribe(httpResultSubscriber);
    }

    /**
     * 注单列表
     */
    public static void getBetListJson(RequestBody body, IMHttpResultObserver<IMBetListBean> httpResultSubscriber) {
        //接口
        IMServiceFactory.getInstance()
                .createService(IMApiManagerService.class,true)
                .httpGetBetListJson(body)
                .compose(IMTransformUtils.<IMHttpResult<String>>main_io())
                .subscribe(httpResultSubscriber);
    }
    /**
     * 注单详情
     */
    public static void getBetDatailJson(RequestBody body, IMHttpResultObserver<IMBetDetailBean> httpResultSubscriber) {
        //接口
        IMServiceFactory.getInstance()
                .createService(IMApiManagerService.class,true)
                .httpGetBetDatailJson(body)
                .compose(IMTransformUtils.<IMHttpResult<String>>main_io())
                .subscribe(httpResultSubscriber);
    }
    /**
     * 注单游戏类型
     */
    public static void getGamesListJson(RequestBody body, IMHttpResultObserver<IMGameTypeBean> httpResultSubscriber) {
        //接口
        IMServiceFactory.getInstance()
                .createService(IMApiManagerService.class,true)
                .httpGameListJson()
                .compose(IMTransformUtils.<IMHttpResult<String>>main_io())
                .subscribe(httpResultSubscriber);
    }
    /**
     * 注单分享
     */
    public static void getBetShareJson(RequestBody body, IMHttpResultObserver<String> httpResultSubscriber) {
        //接口
        IMServiceFactory.getInstance()
                .createService(IMApiManagerService.class,true)
                .httpBetShareJson(body)
                .compose(IMTransformUtils.<IMHttpResult<String>>main_io())
                .subscribe(httpResultSubscriber);
    }

    /**
     * 跟投
     */
    public static void getFollowBetJson(RequestBody body, IMHttpResultObserver<String> httpResultSubscriber) {
        //接口
        IMServiceFactory.getInstance()
                .createService(IMApiManagerService.class,true)
                .httpFollowBetJson(body)
                .compose(IMTransformUtils.<IMHttpResult<String>>main_io())
                .subscribe(httpResultSubscriber);
    }

    /**
     * 发送红包
     */
    public static void sendRedPickJson(RequestBody body, IMHttpResultObserver<IMSingleRedBackBean> httpResultSubscriber) {
        //接口
        IMServiceFactory.getInstance()
                .createService(IMApiManagerService.class,true)
                .httpSendRedPickJson(body)
                .compose(IMTransformUtils.<IMHttpResult<String>>main_io())
                .subscribe(httpResultSubscriber);
    }

    /**
     * 红包状态
     */
    public static void getRedStatePickJson(RequestBody body, IMHttpResultObserver<IMRedPickInformationBean> httpResultSubscriber) {
        //接口
        IMServiceFactory.getInstance()
                .createService(IMApiManagerService.class,true)
                .httpgetRedStatePickJson(body)
                .compose(IMTransformUtils.<IMHttpResult<String>>main_io())
                .subscribe(httpResultSubscriber);
    }
    /**
     * 红包状态（步骤三）
     */
    public static void getLastRedStatePickJson(RequestBody body, IMHttpResultObserver<IMRedPickInformationBean> httpResultSubscriber) {
        //接口
        IMServiceFactory.getInstance()
                .createService(IMApiManagerService.class,true)
                .httpgetLastRedStatePickJson(body)
                .compose(IMTransformUtils.<IMHttpResult<String>>main_io())
                .subscribe(httpResultSubscriber);
    }

    /**
     * 抢红包
     */
    public static void getRedPickJson(RequestBody body, IMHttpResultObserver<IMGetRedPickBean> httpResultSubscriber) {
        //接口
        IMServiceFactory.getInstance()
                .createService(IMApiManagerService.class,true)
                .httpGetRedPickJsonJson(body)
                .compose(IMTransformUtils.<IMHttpResult<String>>main_io())
                .subscribe(httpResultSubscriber);
    }


    /**
     * 查询会员或者客服发送的红包列表
     */
    public static void getSendRedPackJson(RequestBody body, IMHttpResultObserver<IMSendRedPickRecordList.IMSendRedPickRecordListBean> httpResultSubscriber) {
        //接口
        IMServiceFactory.getInstance()
                .createService(IMApiManagerService.class,true)
                .httpGetSendRedPickListJson(body)
                .compose(IMTransformUtils.<IMHttpResult<IMSendRedPickRecordList.IMSendRedPickRecordListBean>>main_io())
                .subscribe(httpResultSubscriber);
    }

    /**
     * 用户领取的红包列表
     */
    public static void getGetRedPackJson(RequestBody body, IMHttpResultObserver<IMSendRedPickRecordList.IMSendRedPickRecordListBean> httpResultSubscriber) {
        //接口
        IMServiceFactory.getInstance()
                .createService(IMApiManagerService.class,true)
                .httpGetRedPickListJson(body)
                .compose(IMTransformUtils.<IMHttpResult<IMSendRedPickRecordList.IMSendRedPickRecordListBean>>main_io())
                .subscribe(httpResultSubscriber);
    }
    /**
     * 上传图片
     */
    public static void getUpdataPicture(RequestBody body, IMHttpResultObserver<IMImageViewBean> httpResultSubscriber) {
        //接口
        IMServiceFactory.getInstance()
                .createService(IMApiManagerService.class,true)
                .httpGetUpdataPictureJson(body)
                .compose(IMTransformUtils.<IMHttpResult<IMImageViewBean>>main_io())
                .subscribe(httpResultSubscriber);
    }
    /**
     * 上传图片
     */
    public static void getUpdataInfor(RequestBody body, IMHttpResultObserver<String> httpResultSubscriber) {
        //接口
        IMServiceFactory.getInstance()
                .createService(IMApiManagerService.class,true)
                .httpGetUpdataInforJson(body)
                .compose(IMTransformUtils.<IMHttpResult<String>>main_io())
                .subscribe(httpResultSubscriber);
    }

    /**
     * 获取公司的总开关
     */
    public static void getCompanySwithJson( IMHttpResultObserver<IMCompanyBean> httpResultSubscriber) {
        //接口
        IMServiceFactory.getInstance()
                .createService(IMApiManagerService.class,true)
                .httpGetCompanySwithJson()
                .compose(IMTransformUtils.<IMHttpResult<IMCompanyBean>>main_io())
                .subscribe(httpResultSubscriber);
    }

    /**
     * 用户进群
     */
    public static void enterGroupJson(RequestBody body, IMHttpResultObserver<String> httpResultSubscriber) {
        //接口
        IMServiceFactory.getInstance()
                .createService(IMApiManagerService.class,true)
                .httpEnterGroupJson(body)
                .compose(IMTransformUtils.<IMHttpResult<String>>main_io())
                .subscribe(httpResultSubscriber);
    }

    /**
     * 获取ImToken
     */
    public static void getImTokenJson(IMHttpResultObserver<IMImTokenBean> httpResultSubscriber) {
        //接口
        IMServiceFactory.getInstance()
                .createService(IMApiManagerService.class,true)
                .httpGetIMTokenJson()
                .compose(IMTransformUtils.<IMHttpResult<IMImTokenBean>>main_io())
                .subscribe(httpResultSubscriber);
    }
    /**
     * 成员信息（查询成员信息）
     */
    public static void GetMemberInforJson(RequestBody body, IMHttpResultObserver<IMUserInforBean.UserInforData> httpResultSubscriber) {
        //接口
        IMServiceFactory.getInstance()
                .createService(IMApiManagerService.class,true)
                .httpGetMemberInforJson(body)
                .compose(IMTransformUtils.<IMHttpResult<IMUserInforBean.UserInforData>>main_io())
                .subscribe(httpResultSubscriber);
    }
    /**
     * 创建群聊
     */
    public static void creatNewGroupJson(RequestBody body, IMHttpResultObserver<IMHttpCommonBean> httpResultSubscriber) {
        //接口
        IMServiceFactory.getInstance()
                .createService(IMApiManagerService.class,true)
                .httpcreatNewGroupJson(body)
                .compose(IMTransformUtils.<IMHttpResult<IMHttpCommonBean>>main_io())
                .subscribe(httpResultSubscriber);
    }

    /**
     *删除群聊
     */
    public static void deleteNewGroupJson(RequestBody body, IMHttpResultObserver<String> httpResultSubscriber) {
        //接口
        IMServiceFactory.getInstance()
                .createService(IMApiManagerService.class,true)
                .httpdeleteNewGroupJson(body)
                .compose(IMTransformUtils.<IMHttpResult<String>>main_io())
                .subscribe(httpResultSubscriber);
    }
    /**
     * 退出群聊
     */
    public static void quetNewGroupJson(RequestBody body, IMHttpResultObserver<String> httpResultSubscriber) {
        //接口
        IMServiceFactory.getInstance()
                .createService(IMApiManagerService.class,true)
                .httpquetNewGroupJson(body)
                .compose(IMTransformUtils.<IMHttpResult<String>>main_io())
                .subscribe(httpResultSubscriber);
    }

    /**
     * 退出群聊
     */
    public static void removeGroupMemberJson(RequestBody body, IMHttpResultObserver<String> httpResultSubscriber) {
        //接口
        IMServiceFactory.getInstance()
                .createService(IMApiManagerService.class,true)
                .httpRemoveGroupMemberJson(body)
                .compose(IMTransformUtils.<IMHttpResult<String>>main_io())
                .subscribe(httpResultSubscriber);
    }
    /**
     * 用户拉人进群
     */
    public static void addMemberMemberJson(RequestBody body, IMHttpResultObserver<String> httpResultSubscriber) {
        //接口
        IMServiceFactory.getInstance()
                .createService(IMApiManagerService.class,true)
                .httpPullGroupMemberJson(body)
                .compose(IMTransformUtils.<IMHttpResult<String>>main_io())
                .subscribe(httpResultSubscriber);
    }

    /**
     * 用户拉人进群
     */
    public static void serchNewFriendJson(RequestBody body, IMHttpResultObserver<List<IMPersonBean>> httpResultSubscriber) {
        //接口
        IMServiceFactory.getInstance()
                .createService(IMApiManagerService.class,true)
                .httpSerchNewFriendJson(body)
                .compose(IMTransformUtils.<IMHttpResult<List<IMPersonBean>>>main_io())
                .subscribe(httpResultSubscriber);
    }

    /**
     * 添加好友
     */
    public static void addNewFriendJson(RequestBody body, IMHttpResultObserver<IMAddFriendBean> httpResultSubscriber) {
        //接口
        IMServiceFactory.getInstance()
                .createService(IMApiManagerService.class,true)
                .httpAddNewFriendJson(body)
                .compose(IMTransformUtils.<IMHttpResult<IMAddFriendBean>>main_io())
                .subscribe(httpResultSubscriber);
    }

    /**
     * 删除好友
     */
    public static void deleteFriendJson(RequestBody body, IMHttpResultObserver<String> httpResultSubscriber) {
        //接口
        IMServiceFactory.getInstance()
                .createService(IMApiManagerService.class,true)
                .httpDeleteFriendJson(body)
                .compose(IMTransformUtils.<IMHttpResult<String>>main_io())
                .subscribe(httpResultSubscriber);
    }


    /**
     * 获取当前登陆人的申请好友列表
     */
    public static void getFriendApplyJson(IMHttpResultObserver<List<IMFriendsBean>> httpResultSubscriber) {
        //接口
        IMServiceFactory.getInstance()
                .createService(IMApiManagerService.class,true)
                .httpGetFriendApplyJson()
                .compose(IMTransformUtils.<IMHttpResult<List<IMFriendsBean>>>main_io())
                .subscribe(httpResultSubscriber);
    }
    /**
     * 同意添加好友
     */
    public static void getAgreeFriendApplyJson(RequestBody body,IMHttpResultObserver<String> httpResultSubscriber) {
        //接口
        IMServiceFactory.getInstance()
                .createService(IMApiManagerService.class,true)
                .httpAgreeAddFriendJson(body)
                .compose(IMTransformUtils.<IMHttpResult<String>>main_io())
                .subscribe(httpResultSubscriber);
    }
    /**
     * 同意添加好友
     */
    public static void getRefuseFriendApplyJson(RequestBody body,IMHttpResultObserver<String> httpResultSubscriber) {
        //接口
        IMServiceFactory.getInstance()
                .createService(IMApiManagerService.class,true)
                .httpRefuseAddFriendJson(body)
                .compose(IMTransformUtils.<IMHttpResult<String>>main_io())
                .subscribe(httpResultSubscriber);
    }

    /**
     * 同意添加好友
     */
    public static void getApplyForDetailsJson(RequestBody body,IMHttpResultObserver<IMApplyInforBean> httpResultSubscriber) {
        //接口
        IMServiceFactory.getInstance()
                .createService(IMApiManagerService.class,true)
                .httpApplyForDetailsJson(body)
                .compose(IMTransformUtils.<IMHttpResult<IMApplyInforBean>>main_io())
                .subscribe(httpResultSubscriber);
    }

    /**
     * 修改群信息（头像和群名字）
     */
    public static void getUpdateGroupInforJson(RequestBody body,IMHttpResultObserver<String> httpResultSubscriber) {
        //接口
        IMServiceFactory.getInstance()
                .createService(IMApiManagerService.class,true)
                .httpUpdateGroupHeadJson(body)
                .compose(IMTransformUtils.<IMHttpResult<String>>main_io())
                .subscribe(httpResultSubscriber);
    }

    /**
     * 修改群信息（头像和群名字）
     */
    public static void getUpdateGroupNoticeJson(RequestBody body,IMHttpResultObserver<String> httpResultSubscriber) {
        //接口
        IMServiceFactory.getInstance()
                .createService(IMApiManagerService.class,true)
                .httpUpdateGroupNoticeJson(body)
                .compose(IMTransformUtils.<IMHttpResult<String>>main_io())
                .subscribe(httpResultSubscriber);
    }

    /**
     * 获取广告
     */
    public static void getGetAdJson(RequestBody body,IMHttpResultObserver<List<SplashAdBean>> httpResultSubscriber) {
        //接口
        IMServiceFactory.getInstance()
                .createService(IMApiManagerService.class,true)
                .httpGetAdJson(body)
                .compose(IMTransformUtils.<IMHttpResult<List<SplashAdBean>>>main_io())
                .subscribe(httpResultSubscriber);
    }
    /**
     * 获取收藏
     */
    public static void getCollection(RequestBody body,IMHttpResultObserver<List<SmallProgramBean>> httpResultSubscriber) {
        //接口
        IMServiceFactory.getInstance()
                .createService(IMApiManagerService.class,true)
                .getCollection(body)
                .compose(IMTransformUtils.<IMHttpResult<List<SmallProgramBean>>>main_io())
                .subscribe(httpResultSubscriber);
    }
    /**
     * 添加收藏
     */
    public static void addCollection(RequestBody body,IMHttpResultObserver<String> httpResultSubscriber) {
        //接口
        IMServiceFactory.getInstance()
                .createService(IMApiManagerService.class,true)
                .addCollection(body)
                .compose(IMTransformUtils.<IMHttpResult<String>>main_io())
                .subscribe(httpResultSubscriber);
    }
    /**
     * 取消收藏
     */
    public static void cancleCollection(RequestBody body,IMHttpResultObserver<String> httpResultSubscriber) {
        //接口
        IMServiceFactory.getInstance()
                .createService(IMApiManagerService.class,true)
                .cancleCollection(body)
                .compose(IMTransformUtils.<IMHttpResult<String>>main_io())
                .subscribe(httpResultSubscriber);
    }
    /**
     * 最近使用
     */
    public static void getRecentlyUse(RequestBody body,IMHttpResultObserver<RecentlyUseBean> httpResultSubscriber) {
        //接口
        IMServiceFactory.getInstance()
                .createService(IMApiManagerService.class,true)
                .getRecentlyUse(body)
                .compose(IMTransformUtils.<IMHttpResult<String>>main_io())
                .subscribe(httpResultSubscriber);
    }
    /**
     * 搜索小程序
     */
    public static void getSearchProgram(RequestBody body,IMHttpResultObserver<List<SmallProgramBean>> httpResultSubscriber) {
        //接口
        IMServiceFactory.getInstance()
                .createService(IMApiManagerService.class,true)
                .getSearchProgram(body)
                .compose(IMTransformUtils.<IMHttpResult<String>>main_io())
                .subscribe(httpResultSubscriber);
    }
    /**
     * 添加最近使用
     */
    public static void addRecentlyUse(RequestBody body,IMHttpResultObserver<String> httpResultSubscriber) {
        //接口
        IMServiceFactory.getInstance()
                .createService(IMApiManagerService.class,true)
                .addRecentlyUse(body)
                .compose(IMTransformUtils.<IMHttpResult<String>>main_io())
                .subscribe(httpResultSubscriber);
    }
    /**
     * 删除最近使用
     */
    public static void deleteRecentlyUse(RequestBody body,IMHttpResultObserver<String> httpResultSubscriber) {
        //接口
        IMServiceFactory.getInstance()
                .createService(IMApiManagerService.class,true)
                .deleteRecentlyUse(body)
                .compose(IMTransformUtils.<IMHttpResult<String>>main_io())
                .subscribe(httpResultSubscriber);
    }
    /**
     * 小程序详情
     */
    public static void getProgramDetails(RequestBody body,IMHttpResultObserver<SmallProgramBean> httpResultSubscriber) {
        //接口
        IMServiceFactory.getInstance()
                .createService(IMApiManagerService.class,true)
                .getProgramDetails(body)
                .compose(IMTransformUtils.<IMHttpResult<String>>main_io())
                .subscribe(httpResultSubscriber);
    }

    /**
     * 小程序详情
     */
    public static void getAliYunToken(RequestBody body,IMHttpResultObserver<IMAliyunTokenBean> httpResultSubscriber) {
        //接口
        IMServiceFactory.getInstance()
                .createService(IMApiManagerService.class,true)
                .getAliyunToken()
                .compose(IMTransformUtils.<IMHttpResult<IMAliyunTokenBean>>main_io())
                .subscribe(httpResultSubscriber);
    }
    /**
     * 屏蔽某人
     */
    public static void getShieldPerson(RequestBody body,IMHttpResultObserver<String> httpResultSubscriber) {
        //接口
        IMServiceFactory.getInstance()
                .createService(IMApiManagerService.class,true)
                .getShieldPersonJson(body)
                .compose(IMTransformUtils.<IMHttpResult<String>>main_io())
                .subscribe(httpResultSubscriber);
    }
    /**
     * 投诉某人
     */
    public static void getComplaintPerson(RequestBody body,IMHttpResultObserver<Object> httpResultSubscriber) {
        //接口
        IMServiceFactory.getInstance()
                .createService(IMApiManagerService.class,true)
                .getComplaintPersonJson(body)
                .compose(IMTransformUtils.<IMHttpResult<Object>>main_io())
                .subscribe(httpResultSubscriber);
    }
    /**
     * 获取背景图片
     */
    public static void getMyBgJson(IMHttpResultObserver<MyBgBeanData> httpResultSubscriber) {
        //接口
        IMServiceFactory.getInstance()
                .createService(IMApiManagerService.class,true)
                .httpGetMyBgJson()
                .compose(IMTransformUtils.<IMHttpResult<MyBgBeanData>>main_io())
                .subscribe(httpResultSubscriber);
    }

}
