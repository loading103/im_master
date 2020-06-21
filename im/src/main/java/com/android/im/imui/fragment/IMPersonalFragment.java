//package com.android.im.imui.fragment;
//
//import android.annotation.SuppressLint;
//import android.content.DialogInterface;
//import android.content.Intent;
//import android.os.Handler;
//import android.text.TextUtils;
//import android.view.View;
//
//import androidx.recyclerview.widget.LinearLayoutManager;
//import androidx.recyclerview.widget.RecyclerView;
//
//import com.android.im.IMSHttpManager;
//import com.android.im.IMSManager;
//import com.android.im.R;
//import com.android.im.imadapter.IMConversationAdapter;
//import com.android.im.imadapter.baseadapter.IMBaseRecycleViewAdapter_T;
//import com.android.im.imeventbus.IMImageViewUpdateEvent;
//import com.android.im.imeventbus.IMMessageDeteAlllEvevt;
//import com.android.im.imeventbus.IMMessageEvent;
//import com.android.im.imeventbus.IMMessageNoticelPersonEvevt;
//import com.android.im.imeventbus.IMRegetPersonListEvent;
//import com.android.im.imeventbus.IMSndMessageEvent;
//import com.android.im.imui.activity.IMPersonalChatActivity;
//import com.android.im.imutils.IMStopClickFast;
//import com.android.nettylibrary.IMSConfig;
//import com.android.nettylibrary.bean.IMMessageBean;
//import com.android.nettylibrary.greendao.entity.IMPersonBean;
//import com.android.nettylibrary.greendao.entity.IMPersonDetailBean;
//import com.android.nettylibrary.http.IMMessageDataJson;
//import com.android.nettylibrary.http.IMSystemOnlineBean;
//import com.android.nettylibrary.protobuf.MessageProtobuf;
//import com.android.nettylibrary.utils.IMLogUtil;
//import com.android.nettylibrary.utils.IMPreferenceUtil;
//import com.google.gson.Gson;
//import com.scwang.smartrefresh.layout.api.RefreshLayout;
//
//import org.greenrobot.eventbus.EventBus;
//import org.greenrobot.eventbus.Subscribe;
//import org.greenrobot.eventbus.ThreadMode;
//
//import java.util.ArrayList;
//import java.util.Collections;
//import java.util.Comparator;
//import java.util.List;
//import java.util.Timer;
//import java.util.TimerTask;
//
//
//public class IMPersonalFragment extends IMBaseFragment implements  IMBaseRecycleViewAdapter_T.OnItemClickListner {
//    private View view;
//    private RecyclerView mRecyclerView;
//    private List<IMPersonBean> persons=new ArrayList<>();
//    private List<IMPersonDetailBean> datas=new ArrayList<>();
//    private IMConversationAdapter adapter;
//    private RefreshLayout refreshLayout;
//    public static final int IM_CHAT_RESTQUECODE=10001;
//    @Override
//    public View initView() {
//        view = View.inflate(getActivity(), R.layout.fragment_im_conversationlist, null);
//        mRecyclerView=view.findViewById(R.id.recyclerView);
//        refreshLayout = view.findViewById(R.id.refreshLayout);
//        try {
//            EventBus.getDefault().register(this);
//        }catch (Exception e){
//            e.printStackTrace();
//        }
//
//        return view;
//    }
//    @Override
//    public void iniData() {
//        initRecycle();
//        RefreshHandView();
//        getdata();
//        EventBus.getDefault().post(new IMImageViewUpdateEvent("url"));
//
//
//    }
//
//    /**
//     * 初始化recycle
//     */
//    @SuppressLint("WrongConstant")
//    private void initRecycle() {
//        LinearLayoutManager layoutManager =  new  LinearLayoutManager(getActivity());
//        layoutManager.setOrientation(LinearLayoutManager .VERTICAL);
//        mRecyclerView.setLayoutManager(layoutManager);
//    }
//    /**
//     * 获取数据库里面的数据(本地人员数据+消息数据组装)
//     */
//    private Handler handler=new Handler();
//    private void getdata() {
//        List<IMPersonBean> imPersonBeans = daoUtils.queryAllMessageData();
//        if (imPersonBeans==null){
//            return;
//        }
//        getOrderData(imPersonBeans);
//
//        adapter=new IMConversationAdapter(getActivity(),R.layout.fragment_im_item_conversationlist,persons);
//        mRecyclerView .setAdapter(adapter);
//        adapter.setOnItemClickListner(this);
//    }
//
//    /**
//     * 上拉下拉刷新数据
//     */
//    private void RefreshHandView() {
//        refreshLayout.setEnableRefresh(false);//是否启用下拉刷新功能
//        refreshLayout.setEnableLoadMore(false);//是否启用上拉加载功能
//        refreshLayout.setEnablePureScrollMode(true);//是否启用纯滚动模式
//        refreshLayout.setEnableOverScrollBounce(true);//是否启用越界回弹
//        refreshLayout.setEnableOverScrollDrag(true);//是否启用越界拖动（仿苹果效果）1.0.4
//    }
//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if(requestCode==IM_CHAT_RESTQUECODE){
//            IMSManager.getInstance().getUnreadMessageNumber();
//        }
//        if(adapter==null){
//            return;
//        }
//        adapter.notifyDataSetChanged();
//    }
//
//    @Override
//    public void onDestroy() {
//        super.onDestroy();
//        if(timer!=null){
//            timer.cancel();
//            timer = null;
//        }
//        EventBus.getDefault().unregister(this);
//    }
//
//    /**
//     * 收到服务器消息3种（1是回执，2.是消息，3.用户状态，4已读未读,5系统消息）
//     */
//
//    private boolean isremove;
//    @Subscribe(threadMode = ThreadMode.MAIN)
//    public void onMessageEvent(IMMessageEvent event) {
//        IMLogUtil.d("MyOwnTag:", "IMPersonalFragment ----" +new Gson().toJson(event));
//        if(event==null){
//            return;
//        }
//        IMMessageBean message =event.getMessageMessage();
//        if(message.getType()== MessageProtobuf.ImMessage.TypeEnum.READ){//如果是已读未读，这里暂时不处理
//            return;
//        }else if (message.getType()== MessageProtobuf.ImMessage.TypeEnum.USERSTATUS){ //如果是客服状态消息
//            handlUserdataMessage(message);
//        }else if (message.getType()== MessageProtobuf.ImMessage.TypeEnum.SYSTEM){ //如果是客服状态消息
//            handleSystemMessage(message);
//        }
//        else if(message.getType()== MessageProtobuf.ImMessage.TypeEnum.SERVERRECEIPT){  //是发送消息成功的回执
//            handleServerReciptMessage(message);
//        }else {  //不为空代表被动接收消息
//            handlerRecieveMessage(message);
//        }
//    }
//    /**
//     * 处理收到的普通消息
//     */
//    private void handlerRecieveMessage(IMMessageBean message) {
//        if (message != null && !TextUtils.isEmpty(message.getData())) {
//            //用来处理父类的红点
//            int tableindex =  ((IMCommonListFragment)(IMPersonalFragment.this.getParentFragment())).getTableindex();
//            if(tableindex==1){
//                ((IMCommonListFragment)(IMPersonalFragment.this.getParentFragment())).setVisible(false);
//            }
//
//            for (int i = 0; i < persons.size(); i++) {
//                if (persons.get(i).getMemberId().equals(message.getSenderId())) {
//                    if(persons.get(i).getLastMessageTime()<message.getTimestamp()){  //如果最新的消息大于现在的消息才跟新这个界面
//                        persons.get(i).setLastmessage(message.getData());
//                        IMMessageDataJson json = new Gson().fromJson(message.getData(), IMMessageDataJson.class);
//                        persons.get(i).setLastMsgType(Integer.parseInt(json.getType()));
//                        persons.get(i).setLastMsgSendType(IMSConfig.MSG_RECIEVE);
//                        persons.get(i).setLastMessageTime(message.getTimestamp());
//                    }
//                    persons.get(i).setLastsendstate(IMSConfig.IM_SEND_STATE_SUCCESS);
//                    IMPersonBean imPersonBean = persons.get(i);
//                    persons.remove(i);
//                    persons.add(0, imPersonBean);
//                }
//                getOrderData(persons);
//                adapter.notifyDataSetChanged();
//            }
//        } else { //todo    本地无好友列表增加数据
//
//        }
//    }
//
//    /**
//     * 处理消息回执
//     */
//    private void handleServerReciptMessage(IMMessageBean message) {
//        isremove=false;
//        for (int i = 0; i < sendlists.size(); i++) {
//            if(sendlists.get(i).getFingerprint().equals(message.getFingerprint())){
//                sendlists.remove(i);
//                isremove=true;
//            }
//        }
//        //跟新界面
//        haveResponse=true;
//        upDateView(message.getFingerprint(),true);
//        //保存数据库
//        //更新会话数据库发送状态
//        List<IMPersonBean> dbdatas = daoUtils.queryAllMessageData();
//        for (int i = 0; i < dbdatas.size(); i++) {
//            if(TextUtils.isEmpty(dbdatas.get(i).getLastmessageId())){
//                continue;
//            }
//            if(dbdatas.get(i).getLastmessageId().equals(message.getFingerprint()) || isremove){
//                dbdatas.get(i).setLastsendstate(IMSConfig.IM_SEND_STATE_SUCCESS);
//                dbdatas.get(i).setLastMessageTime(message.getTimestamp());
//                daoUtils.updateMessageData( dbdatas.get(i));
//                //更新消息数据库发送状态
//                List<IMPersonDetailBean> datas = daoUtils.queryMessageDataAccordField(dbdatas.get(i).getMemberId());
//                for (int j = 0; j < datas.size(); j++) {
//                    if(datas.get(j).getFingerprint().equals(message.getFingerprint())){
//                        datas.get(j).setSendstate(IMSConfig.IM_SEND_STATE_SUCCESS);
//                        datas.get(j).setTimestamp(message.getTimestamp());
//                        daoUtils.updateMessageDetailData( datas.get(j));
//                        break;
//                    }
//                }
//                break;
//            }
//        }
//    }
//    /**
//     * 如果是用户上线下线状态
//     */
//    private void handlUserdataMessage(IMMessageBean message) {
//        IMMessageDataJson dataJson=new Gson().fromJson(message.getData(),IMMessageDataJson.class);
//        if(dataJson==null || TextUtils.isEmpty(dataJson.getSysCode())){
//            IMLogUtil.d("MyOwnTag:", "户上线下线状态==空 ----" +message.getData());
//            return;
//        }
//        switch (Integer.parseInt(dataJson.getSysCode())){
//            case IMSConfig.IM_MESSAGE_KF_UPDATE:  //添加修改重新拉去数据
//            case IMSConfig.IM_MESSAGE_KF_ADD:
//                IMSHttpManager.getInstance().IMHttpGetConversationList();
//                break;
//            case IMSConfig.IM_MESSAGE_KF_DELETE: //添加修改禁止都重新拉去数据（有弹窗提示）
//            case IMSConfig.IM_MESSAGE_KF_FOBIT:
//                IMSystemOnlineBean.Meta metas = new Gson().fromJson(new Gson().toJson(dataJson.getMeta()), IMSystemOnlineBean.Meta.class);
//                showDialog("温馨提示！", "你的客户" + metas.getName() + "已被禁用，请点击确认重新刷新客服", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        IMSHttpManager.getInstance().IMHttpGetConversationList();
//                    }
//                });
//                break;
//            case IMSConfig.IM_MESSAGE_KF_OFFLINE://上线下线状态就是跟新界面信息，不存在保存
//            case IMSConfig.IM_MESSAGE_KF_ONLINE:
//                IMSystemOnlineBean.Meta meta = new Gson().fromJson(new Gson().toJson(dataJson.getMeta()), IMSystemOnlineBean.Meta.class);
//                if(meta!=null){
//                    for (int i = 0; i < persons.size(); i++) {
//                        if(persons.get(i).getMemberId().equals(meta.getMemberId())){
//                            persons.get(i).setStatus(meta.getStatus());
//                            adapter.notifyDataSetChanged();
//                        }
//                    }
//                }
//                break;
//
//        }
//    }
//    /**
//     * 如果是系统消息
//     */
//    private void handleSystemMessage(IMMessageBean message) {
//        IMMessageDataJson dataJson=new Gson().fromJson(message.getData(),IMMessageDataJson.class);
//        if(dataJson==null || TextUtils.isEmpty(dataJson.getSysCode())){
//            IMLogUtil.d("MyOwnTag:", "系统消息" +message.getData());
//            return;
//        }
//        switch (Integer.parseInt(dataJson.getSysCode())){
//            case IMSConfig.IM_MESSAGE_DELETE_COVER:
//            case IMSConfig.IM_MESSAGE_ADD_COVER:
//                IMSHttpManager.getInstance().IMHttpGetConversationList();
//                break;
//        }
//    }
//
//
//    /**
//     *
//     * 详情界面发送消息，通知这边更改状态(还未从服务器获取)
//     */
//    boolean  haveResponse=true;//用来判断是不是接收到消息回执  没有接收10秒后显示发送失败
//    @Subscribe(threadMode = ThreadMode.MAIN)
//    public void onSendMessageEvent(IMSndMessageEvent event) {
//        final String customId = event.getCustomId();
//        final IMPersonDetailBean data = event.getData();
//        //发送消息时修改对应item的发送状态（发送时候在控制类里面保存发送消息，所以这里不保存）
//        for (int i = 0; i < persons.size(); i++) {
//            if(persons.get(i).getMemberId().equals(customId)){
//                persons.get(i).setLastmessageId(data.getFingerprint());
//                persons.get(i).setLastMsgType(data.getMsgType());
//                persons.get(i).setLastMsgSendType(IMSConfig.MSG_SEND);
//                persons.get(i).setLastMessageTime(data.getTimestamp());
//                persons.get(i).setLastsendstate(IMSConfig.IM_SEND_STATE_SENDING);
//                IMPersonBean imPersonBean = persons.get(i);
//                persons.remove(i);
//                persons.add(0, imPersonBean);
//                getOrderData(persons);
//                adapter.notifyDataSetChanged();
//                break;
//            }
//        }
//        haveResponse=false;
//        startDelayTime(data);//发送15秒钟不成功就跟新数据库
//        handler.postDelayed(new Runnable() { //发送15秒钟不成功就跟新界面
//            @Override
//            public void run() {
//                if(!haveResponse) {
//                    upDateView(data.getFingerprint(), false);
//                }
//            }
//        },15000);
//    }
//
//    /**
//     * 处理用户信息状态
//     */
//    @Subscribe(threadMode = ThreadMode.MAIN)
//    public void onMessageEvent(IMRegetPersonListEvent event) {
//        IMLogUtil.d("MyOwnTag:", "IMRegetPersonListEvent ----跟新用户信息" );
//        List<IMPersonBean> person = event.getGroups();
//        if(adapter==null){
//            return;
//        }
//        persons.clear();
//        persons.addAll(person);
//        adapter.notifyDataSetChanged();
//
//    }
//    /**
//     * 秒打扰刷新
//     * @param event
//     */
//    @Subscribe(threadMode = ThreadMode.MAIN)
//    public void onNoticeEvent(IMMessageNoticelPersonEvevt event) {
//        List<IMPersonBean> imPersonBeans = daoUtils.queryAllMessageData();
//        if (imPersonBeans==null){
//            return;
//        }
//        getOrderData(imPersonBeans);
//        adapter.notifyDataSetChanged();
//    }
//    /**
//     * 清空消息记录刷新界面
//     * @param event
//     */
//    @Subscribe(threadMode = ThreadMode.MAIN)
//    public void onMessageEvent(IMMessageDeteAlllEvevt event) {
//        if(TextUtils.isEmpty(event.getPersonalId())){
//            for (int i = 0; i < persons.size(); i++) {
//                persons.get(i).setLastmessage("暂无消息");
//                persons.get(i).setLastMessageTime(0);
//                persons.get(i).setLastsendstate(0);
//                IMPreferenceUtil.setPreference_String(persons.get(i).getMemberId() + IMSConfig.IM_UNEREAD_PERSON_CHAT, "0");//把对应人员的未读消息清空
//            }
//        }else {
//            for (int i = 0; i < persons.size(); i++) {
//                if(persons.get(i).getMemberId().equals(event.getPersonalId())){
//                    persons.get(i).setLastmessage("暂无消息");
//                    persons.get(i).setLastMessageTime(0);
//                    persons.get(i).setLastsendstate(0);
//                }
//            }
//        }
//
//        getOrderData(persons);
//        adapter.notifyDataSetChanged();
//    }
//    /**
//     *  自己发送消息
//     * （收到或是没有收到）消息回执跟新界面
//     */
//    private void upDateView( String fingerprintid,boolean haveRes) {
//        haveResponse=haveRes;
//        for (int i = 0; i < persons.size(); i++) {
//            if(TextUtils.isEmpty(persons.get(i).getLastmessageId())){
//                continue;
//            }
//            if(persons.get(i).getLastmessageId().equals(fingerprintid)){
//                if(haveResponse){  //收到了代表success
//                    persons.get(i).setLastsendstate(IMSConfig.IM_SEND_STATE_SUCCESS);
//                }else {
//                    persons.get(i).setLastsendstate(IMSConfig.IM_SEND_STATE_FAILE);
//                }
//            }
//            getOrderData(persons);
//            adapter.notifyDataSetChanged();
//        }
//    }
//
//    private Timer timer;
//    private TimerTask timerTask;
//    private  List<IMPersonDetailBean>sendlists=new ArrayList<>();//用来记录发送期间的消息列表
//
//    /**
//     * 成功收到服务器响应的消息就移除去，消息里面有个时间戳，在发送消息的时候只开启一个计数器（，每秒轮询消息队列的消息，
//     * 有超过15秒的就更新这个消息的UI，并且移除消息队列，消息队列为空或者页面关闭时，停止这个计数器
//     *
//     */
//    public  void  startDelayTime(IMPersonDetailBean message){
//        sendlists.add(message);
//        if (timer == null) {
//            timer = new Timer();
//        }
//        timerTask = new TimerTask() {
//            @Override
//            public void run() {
//                if(sendlists.size()==0){
//                    if(timer!=null){
//                        timer.cancel();
//                        timer = null;
//                    }
//                }
//                for (int i = 0; i < sendlists.size(); i++) {
//                    long time = (System.currentTimeMillis() - sendlists.get(i).getTimestamp()) / 1000;
//                    if(time>15){
//                        //保存数据库
//                        //更新会话数据库发送状态ad05b655-efaf-4514-b660-06476e154a4f
//                        List<IMPersonBean> dbdatas = daoUtils.queryAllMessageData();
//                        for (int j = 0; j < dbdatas.size();j++) {
//                            if(!TextUtils.isEmpty(dbdatas.get(j).getLastmessageId())){
//                                if (dbdatas.get(j).getLastmessageId().equals(sendlists.get(i).getFingerprint())) {
//                                    dbdatas.get(j).setLastsendstate(IMSConfig.IM_SEND_STATE_FAILE);
//                                    dbdatas.get(j).setLastMessageTime(sendlists.get(i).getTimestamp());
//                                    daoUtils.updateMessageData(dbdatas.get(i));
//                                }
//                            }
//                            //更新消息数据库发送状态
//                            List<IMPersonDetailBean> datas = daoUtils.queryMessageDataAccordField(dbdatas.get(i).getMemberId());
//                            for (int k= 0;k< datas.size(); k++) {
//                                if(TextUtils.isEmpty(sendlists.get(i).getFingerprint()) ||TextUtils.isEmpty(datas.get(k).getFingerprint())){
//                                    continue;
//                                }
//                                if (datas.get(k).getFingerprint().equals(sendlists.get(i).getFingerprint())) {
//                                    datas.get(k).setSendstate(IMSConfig.IM_SEND_STATE_FAILE);
//                                    datas.get(k).setTimestamp(sendlists.get(i).getTimestamp());
//                                    daoUtils.updateMessageDetailData(datas.get(k));
//                                    break;
//                                }
//                            }
//                        }
//                        sendlists.remove(i);
//                    }
//                }
//            }
//        };
//        timer.schedule(timerTask,0,1000);
//    }
//
//
//
//
//
//    /**
//     * 将数据排序处理
//     */
//    public  List<IMPersonBean>  getOrderData( List<IMPersonBean> beans){
//        List<IMPersonBean> topBeans=new ArrayList<>();
//        List<IMPersonBean> blwBeans=new ArrayList<>();
//        for (int i = 0; i < beans.size(); i++) {
//            if(beans.get(i).getIsSetTop()){
//                topBeans.add(beans.get(i));
//            }else {
//                blwBeans.add(beans.get(i));
//            }
//        }
//        //置顶的按置顶时间降序排列，不置顶的按消息的时间降序排列
//        Collections.sort(topBeans, new Comparator<IMPersonBean>() {
//
//            @Override
//            public int compare(IMPersonBean o1, IMPersonBean o2) {
//                if (o1.getSetTopTime() > o2.getSetTopTime()) {
//                    return -1;
//                }
//                if (o1.getSetTopTime() == o2.getSetTopTime()) {
//                    return 0;
//                }
//                return 1;
//            }
//        });
//        Collections.sort(blwBeans, new Comparator<IMPersonBean>() {
//
//            @Override
//            public int compare(IMPersonBean o1, IMPersonBean o2) {
//                if (o1.getLastMessageTime() > o2.getLastMessageTime()) {
//                    return -1;
//                }
//                if (o1.getLastMessageTime() == o2.getLastMessageTime()) {
//                    return 0;
//                }
//                return 1;
//            }
//        });
//        persons.clear();
//        persons.addAll(topBeans);
//        persons.addAll(blwBeans);
//        return  persons;
//    }
//
//
//    @Override
//    public void onItemClickListner(View v, int position, Object t) {
//        if(IMStopClickFast.isFastClick()){
//            Intent intent=new Intent(getActivity(), IMPersonalChatActivity.class);
//            intent.putExtra("user",persons.get(position));
//            startActivityForResult(intent,IM_CHAT_RESTQUECODE);
//        }
//    }
//}
