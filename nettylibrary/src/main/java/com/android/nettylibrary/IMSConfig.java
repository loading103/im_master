package com.android.nettylibrary;

import android.os.Environment;

import java.io.File;

/**
 * <p>@ProjectName:     NettyChat</p>
 * <p>@ClassName:       IMSConfig.java</p>
 * <p>@PackageName:     com.freddy.im</p>
 * <b>
 * <p>@Description:     IMS默认配置，若不使用默认配置，应提供set方法给应用层设置</p>
 * </b>
 * <p>@author:          FreddyChen</p>
 * <p>@date:            2019/04/05 05:38</p>
 * <p>@email:           chenshichao@outlook.com</p>
 */
public class IMSConfig {

    public static final String IMS_SERVICE_IP = "";//服务器本地

    public  static final String HTTP_BASE_URL = "http://132.232.122.151:5678/tomato-app/";
    public  static final String HTTP_BASE_IM_URL = "http://132.232.122.151:5678/";

//    public  static final String HTTP_BASE_URL = "http://api-cs.clexin.com/tomato-app/";
//    public  static final String HTTP_BASE_IM_URL = "http://api-cs.clexin.com/";
    //用户协议
    public  static final String HTTP_USER_URL = "http://download.tuofub.com/tomato/user-agreement/index.html";
    //隐私协议
    public  static final String HTTP_PRIVATE_URL = "http://download.tuofub.com/tomato/privacy-policy/index.html";
    //小程序
    public  static final String HTTP_SMALL_URL = "http://demo.clexin.com/index.html";




    //是否需要日志
    public  static final boolean NEED_LOG = true;
    // 默认重连一个周期失败间隔时长
    public static final int DEFAULT_RECONNECT_INTERVAL = 3 * 1000;
    // 连接超时时长
    public static final int DEFAULT_CONNECT_TIMEOUT = 10 * 1000;
    // 默认一个周期重连次数
    public static final int DEFAULT_RECONNECT_COUNT = 3;
    // 默认重连起始延时时长，重连规则：最大n次，每次延时n * 起始延时时长，重连次数达到n次后，重置
    public static final int DEFAULT_RECONNECT_BASE_DELAY_TIME = 3 * 1000;
    // 默认消息发送失败重发次数
    public static final int DEFAULT_RESEND_COUNT = 3;
    // 默认消息重发间隔时长
    public static final int DEFAULT_RESEND_INTERVAL = 8 * 1000;
    // 默认应用在前台时心跳消息间隔时长
    public static final int DEFAULT_HEARTBEAT_INTERVAL_FOREGROUND = 15 * 1000;
    // 默认应用在后台时心跳消息间隔时长
    public static final int DEFAULT_HEARTBEAT_INTERVAL_BACKGROUND = 15 * 1000;
    // 应用在前台标识
    public static final int APP_STATUS_FOREGROUND = 0;
    // 应用在后台标识
    public static final int APP_STATUS_BACKGROUND = -1;
    public static final String KEY_APP_STATUS = "key_app_status";
    // 默认服务端返回的消息发送成功状态报告
    public static final int DEFAULT_REPORT_SERVER_SEND_MSG_SUCCESSFUL = 1;
    // 默认服务端返回的消息发送失败状态报告
    public static final int DEFAULT_REPORT_SERVER_SEND_MSG_FAILURE = 0;
    // ims连接状态：连接中
    public static final int CONNECT_STATE_CONNECTING = 0;
    // ims连接状态：连接成功
    public static final int CONNECT_STATE_SUCCESSFUL = 1;
    // ims连接状态：连接失败
    public static final int CONNECT_STATE_FAILURE = -1;



    //模拟数据
    public static final String USER_ID = "";
    //HTTP
    public static final String GRANT_TYPE="client_credentials";
    public static final String SCOPE="all";
    public static final String GRANT_TYPE_TWO="password";
    public static final String GRANT_TYPE_THREE="refresh_token";



    //更新群成员状态（消息提醒，弹窗提醒
    public  static final String  HTTP_IM_MEMBER_UPDATE=HTTP_BASE_URL+"group-member/update";
    //申请入群
    public  static final String  HTTP_IM_JOIN_GROUP=HTTP_BASE_URL+"group-member/join";
    //申请入群
    public  static final String  HTTP_IM_APPLY_GROUP=HTTP_BASE_URL+"group-member/apply";
    //查询群信息
    public  static final String  HTTP_IM_GROUP_INFOR=HTTP_BASE_URL+"group-member/getGroup";
    //查询会员的客服列表
    public  static final String  HTTP_IM_CONVERSATION_LIST=HTTP_BASE_URL+"customer/conversation";
    //查询群成员
    public  static final String  HTTP_IM_GROUP_MEMBER=HTTP_BASE_URL+"group-member/search";
    //查询群聊天记录
    public  static final String  HTTP_IM_GROUP_MESSAGE_HISTORY=HTTP_BASE_URL+"group-message/search";
    //查询个人聊天记录
    public  static final String  HTTP_IM_PERSONAL_MESSAGE_HISTORY=HTTP_BASE_URL+"customer/history";
    //查询个人聊天记录
    public  static final String  HTTP_IM_LOGIN_UID=HTTP_BASE_URL+"mock/ssologin-data";

    public  static final String  HTTP_GET_RED_LIST=HTTP_BASE_URL+"redEnvelopes/grabRecordList";

    public  static final String  HTTP_SEND_RED_LIST=HTTP_BASE_URL+"redEnvelopes/sendRecordList";

    public  static final String  HTTP_GET_GROUP_NOTICE=HTTP_BASE_URL+"group/info";
    //上传图片(通过Base64字符串上传单张图片)
    public  static final String  HTTP_IM_UPDATE_BASEIMAGE=HTTP_BASE_URL+"uploader/simple-image";

    public  static final String  HTTP_IM_UPDATE_INFOR=HTTP_BASE_URL+"customer/update";
    //IM接口
    public  static final String  HTTP_IM_FIRST_TOKEN=HTTP_BASE_IM_URL+"oauth/token";

    public  static final String  HTTP_IM_BIND_USER=HTTP_BASE_IM_URL+"api/v1/user/bind";

    public  static final String  HTTP_IM_LAST_TOKRN=HTTP_BASE_IM_URL+"oauth/token";

    public  static final String  HTTP_IM_CHECK_TOKEN=HTTP_BASE_IM_URL+"oauth/check_token";

    public  static final String  HTTP_IM_GET_HOST=HTTP_BASE_IM_URL+"api/v1/commons/server";

    public  static final String  HTTP_IM_GET_OFFLINE=HTTP_BASE_IM_URL+"api/v1/message/offlineJosnMsgs?";

    //上传图片
    public  static final String  HTTP_IM_UPDATE_IMAGE=HTTP_BASE_IM_URL+"api/v1/message/uploadFile";

    public  static final String  HTTP_IM_GET_MESSAGEID=HTTP_BASE_IM_URL+"api/v1/commons/fingerprint";

    public  static final String  HTTP_IM_GET_MESSAGEID_LIST=HTTP_BASE_IM_URL+"api/v1/commons/fingerprints";
    public static  final String  SAVE_TOKEN="im_token";  //保存token 的键值
    public static  final String  SAVE_ClIENTID="im_clientId";  //保存clientId 的键值
    public static  final String  SAVE_CLIENTSCRENT="im_clientsceret";  //保存clientsceret的键值
    public static  final String  SAVE_USERID="im_userId";  //保存customerId的键值
    public static  final String  SAVE_HEADURL="im_headurl";  //保存头像
    public static  final String  SAVE_NAME="im_nick_name";  //保存NICKNAME昵称的键值
    public static  final String  SAVE_USER_NAME="im_user_name";  //保存NICKNAME昵称的键值
    public static  final String  SAVE_LEVEL="im_level";  //保存NICKNAME昵称的键值
    public static  final String  SAVE_TITLE="im_title";  //保存NICKNAME昵称的键值
    public static  final String  SAVE_MONEY="im_money";  //保存NICKNAME昵称的键值
    public static  final String  SAVE_PAYWORD="im_payword";  //保存是不是需要输入密码
    public static  final String  SAVE_USERTYPE="im_use_type";  //保存用户类型的键值
    public static  final String  SAVE_IM_TOKEN="im_sec_token";  //保存IM端生成的第二次token
    public static  final String  SAVE_USER_DATA="im_user_date";  //保存IM端生成的用户data
    public static  final String  SAVE_RESH_TOKEN="im_resh_token";  //保存IM端生成的用户data
    public static final String  SAVE_THIRDUID ="im_third_uid" ;
    public static final String  SAVE_FRIENDLYVALU ="im_third_uid" ;
    public static final String  SAVE_BG_PICTURE ="im_mine_bg" ;
    public static final String  SAVE_LOGIN_DATA ="Logindata" ;//第一次显示用户隐私协议


    public static  final String  SAVE_UID="im_uid";  //保存uid
    public static  final String  SAVE_AUTHPARM="im_authParm";  //保存uid
    public static  final String  SAVE_NONCESTR="im_nonceStr";  //保存uid
    public static  final String  SAVE_SIGNATURE="im_signAture";  //保存uid
    public static  final String  SAVE_TIMESTAMP="im_timeStamp";  //保存uid
    public static  final String  IM_SAVE_HOST="save_host_ip";   //保存IP
    public static  final String  SAVE_AVAILAVBLE_SWICH="im_available";//保存总开关(JSon串)
    public static  final String  SAVE_APPID="im_save_appid";  //保存APPID
    public static  final String  SAVE_FIRST_SOFT_HIGT="soft_first_hight";  //保存APPID
    public static  final String  SAVE_MESSAGE_FIGID="im_message_figId";  //保存APPID
    public static final String SAVE_FIRST_NOTICE ="im_first_save" ;
    //保存木个人的消息置顶和消息不提示的功能
    public  static  final String  IM_PERSON_NO_NOTICE="personal_notice";
    public  static  final String  IM_PERSON_ZHI_DING="personal_zhiding";
    //保存群消息置顶和消息不提示的功能
    public  static  final String  IM_GROUP_NO_NOTICE="group_notice";
    public  static  final String  IM_GROUP_QUN_TINGX="group_qun_notice";

    public  static  final String  IM_CONVERSATION_NO_NOTICE="conversation_notice";
    public  static  final String  IM_CONVERSATION_TINGX="conversation_tixing";
    public  static  final String  IM_CONVERSATION_ZHI_DING="conversation_zhiding";
    public  static  final String  IM_CONVERSATION_TOUSU="conversation_toushu";
    // 消息设置(保存消息设置)
    public static  final String  IM_SETTING_PUSH="im_setting_push";
    public static  final String  IM_SETTING_APP_NOTICE="im_setting_app_notice";
    public static  final String  IM_SETTING_SHOKE="im_setting_shoke";
    public static  final String  IM_SETTING_VOICE="im_setting_voice";
    public static  final String  IM_SETTING_AUTHEN="im_setting_authen";
    public static  final String  CHOOSE_CHAT_BG="chat_bg";  //新消息通知
    public static  final String  CHOOSE_MY_BG="mine_bg";  //新消息通知
    //保存单聊 群聊的未读数目
    public  static  final String  IM_UNEREAD_PERSON_CHAT="personal_number";
    public  static  final String  IM_UNEREAD_GROUP_CHAT="group_number";
    public  static  final String  IM_UNEREAD_CONVERSATION_CHAT="conversation_number";
    //保存置顶和置顶时间
    public  static  final String  IM_SET_TOP_TIME="im_set_top_time";
    public  static  final String  IM_IS_SET_TOP="im_is_set_top";
    //保存置顶和置顶时间
    public  static  final String  IM_SOMEONE_AT_ME="im_some_at_me";

    //发送消息的状态
    public static  final int  IM_SEND_STATE_SUCCESS=0;
    public static  final int  IM_SEND_STATE_SENDING=1;
    public static  final int  IM_SEND_STATE_FAILE=2;

    // msgType消息类型
    public static  final int  IM_MESSAGE_TEXT=1000;  //文本消息
    public static  final int  IM_MESSAGE_PICTURE=2000;//图片消息
    public static  final int  IM_MESSAGE_VIDEO=3000; //视频消息
    public static  final int  IM_MESSAGE_AUDIO=3500; //音频消息
    public static  final int  IM_MESSAGE_REPACKED=4000;//红包消息
    public static  final int  IM_MESSAGE_PUSH=5000;//推送消息
    public static  final int  IM_MESSAGE_HUODONG=6000;//活动消息
    public static  final int  IM_MESSAGE_FOLLOWHEAD=7000; //跟投消息
    public static  final int  IM_MESSAGE_READCARD=8000;//红单消息
    public static  final int  IM_MESSAGE_PLAN=9000;//计划消息
    public static  final int  IM_MESSAGE_SYSTEM=10000;//系统消息
    public static  final int  IM_MESSAGE_SHARE_SMALL=11000;//分享小程序
    //SysCode的值（都是系统消息下的不同类型）
    public static  final int  IM_MESSAGE_JOINGROUP=400000;//系统消息(加群)
    public static  final int  IM_MESSAGE_KIOUTGROUP=400001;//系统消息(剔除群聊)
    public static  final int  IM_MESSAGE_KIOUTSELF=400011;//系统消息(自己剔除群聊)
    public static  final int  IM_MESSAGE_CLOPNEGROUP=400021;//系统消息(解散群聊)
    public static  final int  IM_MESSAGE_QUITGROUP=400012;//系统消息(退出群聊)
    public static  final int  IM_MESSAGE_SELFJOINGROUP=400010;//系统消息(加群)
    public static  final int  IM_MESSAGE_BLACK=400002;//系统消息(拉入黑名单)
    public static  final int  IM_MESSAGE_NO_SPEAK=400003;//系统消息(禁言)
    public static  final int  IM_MESSAGE_NO_BLACK=400004;//系统消息(解除黑名单  )
    public static  final int  IM_MESSAGE_CAN_SPEAK=400005;//系统消息(接触禁言)
    public static  final int  IM_MESSAGE_GROUP_PESSION=400006;//系统变更（发送消息的各种权限 公告等）
    public static  final int  IM_MESSAGE_GROUP_LIMITE=400007;//系统变更（群功能权限）
    public static  final int  IM_MESSAGE_GROUP_DIALOG=400008;//每日首次（群功能权限）
    public static  final int  IM_MESSAGE_TOTLE_OPEN=700001;//添加总开关
    public static  final int  IM_MESSAGE_UPDATE_INFORE=200001;//个人信息修改

    public static  final int  IM_MESSAGE_ADD_FRIEND_SUCCESS=800001;//添加朋友成功
    public static  final int  IM_MESSAGE_ADD_FRIEND_WAIT=900001;//添加好友-等待回复

    public static  final int  IM_MESSAGE_FORBIT_FRIEND=10002;//屏蔽好友

    public static  final int  IM_MESSAGE_SEND_NO_FRIEND=10001;//不是好友
    public static  final int  IM_MESSAGE_SEND_NO_GROUP=20001;//解散该群
    public static  final int  IM_MESSAGE_REMOVE_GROUP=20002;//被移除该群

    public static  final int  IM_MESSAGE_CLX_TEAM=510001;//彩乐信团队消息
    public static  final int  IM_MESSAGE_CLX_fENG=200006;//封号提示

    //USERSTATUS用户类型消息
    public static  final int  IM_MESSAGE_KF_UPDATE=100001;//需要客户端更新客服资料
    public static  final int  IM_MESSAGE_KF_DELETE=100002;//客服删除
    public static  final int  IM_MESSAGE_KF_FOBIT=100003;//客服禁用
    public static  final int  IM_MESSAGE_KF_ADD=100004;//客服新增
    public static  final int  IM_MESSAGE_KF_OFFLINE=100005;//客服下线
    public static  final int  IM_MESSAGE_KF_ONLINE=100006;//客服上线
    public static  final int  IM_MESSAGE_DELETE_COVER=300011;//删除会话
    public static  final int  IM_MESSAGE_ADD_COVER=300001;//添加会话

    //发送，接收
    public static final int MSG_SEND = 0;//发送
    public static final int MSG_RECIEVE = 1;//接收
    public static final int MSG_CLXTEAM_WARN = 2;//彩乐信团队警告
    public static final int MSG_CLXTEAM_RESULT = 3;//彩乐信结果
    //消息类型（本地adapter判断类型）
    public static final int MSG_LEFT_TEXT = 0;//接收消息类型字文（文字）
    public static final int MSG_RIGHT_TEXT = 1;//发送消息类型
    public static final int MSG_LEFT_IMG = 2;//接收消息类型（图片）
    public static final int MSG_RIGHT_IMG = 3;//发送消息类型图片
    public static final int MSG_LEFT_VIDEO = 4;//接收消息类型（视屏）
    public static final int MSG_RIGHT_VIDEO = 5;//发送消息类型
    public static final int MSG_LEFT_REPACKED = 6;//接收消息类型(红包)
    public static final int MSG_RIGHT_REPACKED = 7;//发送消息类型
    public static final int MSG_PUSH = 8;//接收消息类型(推送)
    public static final int MSG_LEFT_FOLLOW =9;//接收消息类型(红包)
    public static final int MSG_RIGHT_FOLLOW = 10;//发送消息类型
    public static final int MSG_LEFT_REDCARD =11;//接收消息类型(红包)
    public static final int MSG_RIGHT_REDCARD = 12;//发送消息类型
    public static final int MSG_CHAT_GROUP = 13;//有人加群的消息，有人抢红包的提示
    public static final int MSG_LEFT_AUDIO =14;//接收消息类型（音频）
    public static final int MSG_RIGHT_AUDIO =15;//发送消息类型（音频）
    public static final int MSG_LEFT_SMALL =16;//分享小程序
    public static final int MSG_RIGHT_SMALL =17;//分享小程序
    public static final int MSG_CLX_WARN =18;//彩乐信消息
    public static final int MSG_CLX_FENG =19;//彩乐信消息

    //系统变化类型
    public static final int MSG_SYSTEM_RECONNET = 1;   //断线重连,重新获取token
    public static final int MSG_SYSTEM_GETOFFLIN_MSG = 2;//链接成功，获取离线消息
    public static final int MSG_SYSTEM_RELOGIN = 3;//多人登录同一个账号
    public static final int MSG_SYSTEM_DISSCONNECT = 4;//链接断开
    //新的朋友有几条未读消息
    public static final String SAVE_NEW_FRIEND ="new_friend_number" ;

    public static  final String  PHONE="phone";  //手机号
    public static  final String  SIGNATURE="signature";  //签名
    public static  final String  FIRST_OPEN="first_open";  //第一次打开
    public static  final String  CLX_ID="ID123456789";  //彩乐信团队的ID


    public static final String PICTURI_PATH =   Environment.getExternalStorageDirectory().getPath()+"/jianxin_photo/";


    static {
        File videodir = new File(PICTURI_PATH);
        if (!videodir.exists()) {
            videodir.mkdirs();
        }
    }
}
