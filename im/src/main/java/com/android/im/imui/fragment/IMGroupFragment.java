//package com.android.im.imui.fragment;
//
//import android.annotation.SuppressLint;
//import android.content.DialogInterface;
//import android.content.Intent;
//import android.os.Handler;
//import android.text.TextUtils;
//import android.view.View;
//import android.widget.Toast;
//
//import androidx.recyclerview.widget.LinearLayoutManager;
//import androidx.recyclerview.widget.RecyclerView;
//
//import com.android.im.IMSManager;
//import com.android.im.R;
//import com.android.im.imadapter.IMGroupAdapter;
//import com.android.im.imadapter.baseadapter.IMBaseRecycleViewAdapter_T;
//import com.android.im.imeventbus.IMGroupMessageEvent;
//import com.android.im.imeventbus.IMMessageDetelGroupEvevt;
//import com.android.im.imeventbus.IMMessageNoticelGroupEvevt;
//import com.android.im.imeventbus.IMRegetGroupKickOutEvent;
//import com.android.im.imeventbus.IMRegetGroupListEvent;
//import com.android.im.imeventbus.IMSendGroupMessageEvent;
//import com.android.im.imui.activity.IMGroupChatActivity;
//import com.android.im.imutils.IMStopClickFast;
//import com.android.nettylibrary.IMSConfig;
//import com.android.nettylibrary.bean.IMMessageBean;
//import com.android.nettylibrary.greendao.entity.IMGroupBean;
//import com.android.nettylibrary.greendao.entity.IMGroupDetailBean;
//import com.android.nettylibrary.http.IMGetIMMemberData;
//import com.android.nettylibrary.http.IMLoginSuccessBean;
//import com.android.nettylibrary.http.IMMessageDataJson;
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
//import java.io.IOException;
//import java.util.ArrayList;
//import java.util.Collections;
//import java.util.Comparator;
//import java.util.List;
//import java.util.Timer;
//import java.util.TimerTask;
//
//import okhttp3.Call;
//import okhttp3.Callback;
//import okhttp3.Request;
//import okhttp3.RequestBody;
//import okhttp3.Response;
//
//import static com.android.im.IMSManager.okHttpClient;
//import static com.android.im.imnet.IMBaseConstant.JSON;
//
//
//public class IMGroupFragment extends IMBaseFragment implements IMBaseRecycleViewAdapter_T.OnItemClickListner {
//    private View view;
//    private RecyclerView mRecyclerView;
//    private List<IMGroupBean> groups=new ArrayList<>();
//    private List<IMGroupDetailBean> datas=new ArrayList<>();
//    private IMGroupAdapter adapter;
//    private Handler handler=new Handler();
//    boolean  haveResponse=true;//用来判断是不是接收到消息回执  没有接收10秒后显示发送失败
//    private RefreshLayout refreshLayout;
//    public static final int IM_CHAT_RESTQUECODE=10002;
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
//        return view;
//    }
//    @Override
//    public void iniData() {
//        initRecycle();
//        RefreshHandView();
//        getdata();
//    }
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
//    /**
//     * 初始化recycle
//     */
//    @SuppressLint("WrongConstant")
//    private void initRecycle() {
//        LinearLayoutManager layoutManager =  new LinearLayoutManager(getActivity());
//        layoutManager.setOrientation(LinearLayoutManager .VERTICAL);
//        mRecyclerView.setLayoutManager(layoutManager);
//    }
//    /**
//     * 获取数据库里面的数据(本地人员数据+消息数据组装)
//     */
//    private void getdata() {
//        groups= daoUtils.queryAllGroupData();
//        if(groups==null){
//            return;
//        }
//        adapter=new IMGroupAdapter(getActivity(),R.layout.fragment_im_item_conversationlist, groups);
//        mRecyclerView .setAdapter(adapter);
//        adapter.setOnItemClickListner(this);
//    }
//
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
//     * 收到服务器消息2种（一是回执，刷新界面状态，更新数据库发送状态   二是消息，刷新界面，添加数据到数据库）
//     */
//
//    private boolean isremove;
//    @Subscribe(threadMode = ThreadMode.MAIN)
//    public void onMessageEvent(IMGroupMessageEvent event) {
//        IMMessageBean message =event.getMessageMessage();
//        if(!TextUtils.isEmpty(message.getData()) && message.getType()== MessageProtobuf.ImMessage.TypeEnum.SYSTEM){
//            IMMessageDataJson s = new Gson().fromJson(message.getData(), IMMessageDataJson.class);
//            if(Integer.parseInt(s.getSysCode())==IMSConfig.IM_MESSAGE_GROUP_PESSION ||Integer.parseInt(s.getSysCode())==IMSConfig.IM_MESSAGE_GROUP_LIMITE){
//                return;
//            }
//        }
//        if(event!=null && event.getMessageMessage().getType()== MessageProtobuf.ImMessage.TypeEnum.SERVERRECEIPT){  //是发送消息成功的回执
//            isremove=false;
//            for (int i = 0; i < sendlists.size(); i++) {
//                if(sendlists.get(i).getFingerprint().equals(message.getFingerprint())){
//                    sendlists.remove(i);
//                    isremove=true;
//                }
//            }
//            //跟新界面
//            haveResponse=true;
//            upDateView(message.getFingerprint(),true);
//            //保存数据库
//            //更新会话数据库发送状态
//            List<IMGroupBean> dbdatas = daoUtils.queryAllGroupData();
//            for (int i = 0; i < dbdatas.size(); i++) {
//                if(TextUtils.isEmpty(dbdatas.get(i).getLastFingerId())){
//                    continue;
//                }
//                if(dbdatas.get(i).getLastFingerId().equals(message.getFingerprint()) || isremove){
//                    dbdatas.get(i).setLastSendstate(IMSConfig.IM_SEND_STATE_SUCCESS);
//                    IMLogUtil.d("MyOwnTag:", "message.getTimestamp() --111--" +message.getTimestamp());
//
//                    dbdatas.get(i).setLastMessageTime(message.getTimestamp());
//                    daoUtils.updateGroupData( dbdatas.get(i));
//                    //更新消息数据库发送状态、
//
//                    List<IMGroupDetailBean> datas = daoUtils.queryGroupDataorderAscrdField(message.getGroupId());
//                    for (int j = 0; j < datas.size(); j++) {
//                        if(TextUtils.isEmpty(datas.get(j).getFingerprint())){
//                            continue;
//                        }
//                        if(datas.get(j).getFingerprint().equals(message.getFingerprint())){
//                            datas.get(j).setSendstate(IMSConfig.IM_SEND_STATE_SUCCESS);
//                            datas.get(j).setTimestamp(message.getTimestamp());
//                            IMLogUtil.d("MyOwnTag:", "message.getTimestamp() --222--" +message.getTimestamp());
//                            daoUtils.updateGroupDetailData( datas.get(j));
//                            break;
//                        }
//                    }
//                    break;
//                }
//            }
//        }else {  //不为空代表被动接收消息
//            if (message != null && !TextUtils.isEmpty(message.getData())) {
//                //用来处理父类的红点
//                int tableindex =  ((IMCommonListFragment)(IMGroupFragment.this.getParentFragment())).getTableindex();
//                if(tableindex==0){
//                    ((IMCommonListFragment)(IMGroupFragment.this.getParentFragment())).setVisible(true);
//                }
//                for (int i = 0; i < groups.size(); i++) {
//                    if (groups.get(i).getGroupId().equals(message.getGroupId())) {
//                        if(groups.get(i).getLastMessageTime()<message.getTimestamp()){  //如果最新的消息大于现在的消息才跟新这个界面
//                            groups.get(i).setLastMessage(message.getData());
//                            IMMessageDataJson json = new Gson().fromJson(message.getData(), IMMessageDataJson.class);
//                            groups.get(i).setLastMsgType(Integer.parseInt(json.getType()));
//                            groups.get(i).setLastMsgSendType(IMSConfig.MSG_RECIEVE);
//                            groups.get(i).setLastMessageTime(message.getTimestamp());
//                        }
//                        groups.get(i).setLastSendstate(IMSConfig.IM_SEND_STATE_SUCCESS);
//                        IMGroupBean imPersonBean = groups.get(i);
//                        groups.remove(i);
//                        groups.add(0, imPersonBean);
//                    }
//                    adapter.notifyDataSetChanged();
//                }
//            } else { //todo    本地无好友列表增加数据
//
//            }
//        }
//    }
//
//    /**
//     * 加群成功之后刷新当前界面
//     */
//    @Subscribe(threadMode = ThreadMode.MAIN)
//    public void onMessageEvent(IMRegetGroupListEvent event) {
//        IMLogUtil.d("MyOwnTag:", "IMGroupFragment ----添加群成功，刷新群列表" );
//        List<IMGroupBean> group = event.getGroups();
//        if(adapter==null){
//            return;
//        }
//        Collections.sort(group, new Comparator<IMGroupBean>(){
//            public int compare(IMGroupBean o1, IMGroupBean o2) {
//                //按照学生的年龄进行升序排列
//                if(o1.getLastMessageTime() > o2.getLastMessageTime()){
//                    return -1;
//                }
//                if(o1.getLastMessageTime()==o2.getLastMessageTime()){
//                    return 0;
//                }
//                return 1;
//            }
//        });
//        groups.clear();
//        groups.addAll(group);
//        adapter.notifyDataSetChanged();
//
//    }
//    /**
//     * 踢群之后给提示
//     */
//    @Subscribe(threadMode = ThreadMode.MAIN)
//    public void onMessageEvent(final IMRegetGroupKickOutEvent event) {
//        showEnsureDialog("温馨提示！", "您已经被群管理员移除了" + event.getGroups().getGroupName() + "群聊", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                daoUtils.deleteGroupData(event.getGroups());
//                daoUtils.deleteGroupDataAccordField(event.getGroups().getGroupId());
//                IMPreferenceUtil.setPreference_String(event.getGroups().getGroupId() + IMSConfig.IM_UNEREAD_GROUP_CHAT, "0");
//                for (int i = 0; i < groups.size(); i++) {
//                    if(event.getGroups().getGroupId().equals(groups.get(i).getGroupId())){
//                        groups.remove(i);
//                        adapter.notifyDataSetChanged();
//                        break;
//                    }
//                }
//            }
//        });
//    }
//    /**
//     * 详情界面发送消息，通知这边更改状态(还未从服务器获取)
//     */
//    @Subscribe(threadMode = ThreadMode.MAIN)
//    public void onSendMessageEvent(IMSendGroupMessageEvent event) {
//        handlerSendMessage(event);
//
//    }
//    /**
//     * 清空消息记录刷新界面
//     */
//    @Subscribe(threadMode = ThreadMode.MAIN)
//    public void onMessageEvent(IMMessageDetelGroupEvevt event) {
//        handlerDeleteGroupMessage(event);
//    }
//    /**
//     * 秒打扰刷新
//     * @param event
//     */
//    @Subscribe(threadMode = ThreadMode.MAIN)
//    public void onNoticeEvent(IMMessageNoticelGroupEvevt event) {
//        adapter.notifyDataSetChanged();
//    }
//    /**
//     *  自己发送消息
//     * （收到或是没有收到）消息回执跟新界面
//     */
//    private void upDateView( String fingerprintid,boolean haveRes) {
//        haveResponse=haveRes;
//        for (int i = 0; i < groups.size(); i++) {
//            if(TextUtils.isEmpty(groups.get(i).getLastFingerId())){
//                continue;
//            }
//            if(groups.get(i).getLastFingerId().equals(fingerprintid)){
//                if(haveResponse){  //收到了代表success
//                    groups.get(i).setLastSendstate(IMSConfig.IM_SEND_STATE_SUCCESS);
//                }else {
//                    groups.get(i).setLastSendstate(IMSConfig.IM_SEND_STATE_FAILE);
//                }
//            }
//            adapter.notifyDataSetChanged();
//        }
//    }
//
//    private Timer timer;
//    private TimerTask timerTask;
//    private  List<IMGroupDetailBean>sendlists=new ArrayList<>();//用来记录发送期间的消息列表
//    /**
//     * 成功收到服务器响应的消息就移除去，消息里面有个时间戳，在发送消息的时候只开启一个计数器（，每秒轮询消息队列的消息，
//     * 有超过15秒的就更新这个消息的UI，并且移除消息队列，消息队列为空或者页面关闭时，停止这个计数器
//     *
//     */
//    public  void  startDelayTime(IMGroupDetailBean message){
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
//                        List<IMGroupBean> dbdatas = daoUtils.queryAllGroupData();
//                        for (int j = 0; j < dbdatas.size();j++) {
//                            if(!TextUtils.isEmpty(dbdatas.get(j).getLastFingerId())){
//                                if (dbdatas.get(j).getLastFingerId().equals(sendlists.get(i).getFingerprint())) {
//                                    dbdatas.get(j).setLastSendstate(IMSConfig.IM_SEND_STATE_FAILE);
//                                    dbdatas.get(j).setLastMessageTime(sendlists.get(i).getTimestamp());
//                                    daoUtils.updateGroupData(dbdatas.get(i));
//                                }
//                            }
//                            //更新消息数据库发送状态
//                            List<IMGroupDetailBean> datas = daoUtils.queryGroupDataAccordField(dbdatas.get(i).getGroupId());
//                            for (int k= 0;k< datas.size(); k++) {
//                                if(TextUtils.isEmpty(sendlists.get(i).getFingerprint()) ||TextUtils.isEmpty(datas.get(k).getFingerprint())){
//                                    continue;
//                                }
//                                if (datas.get(k).getFingerprint().equals(sendlists.get(i).getFingerprint())) {
//                                    datas.get(k).setSendstate(IMSConfig.IM_SEND_STATE_FAILE);
//                                    datas.get(k).setTimestamp(sendlists.get(i).getTimestamp());
//                                    daoUtils.updateGroupDetailData(datas.get(k));
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
//    @Override
//    public void onItemClickListner(View v, int position, Object t) {
//        if (IMStopClickFast.isFastClick()) {
//            final IMGroupBean groupBean = groups.get(position);
//            if (groupBean.getInGroup().equals("N")) {
//                applyIntoGroup(groupBean); //申请加入群
//                return;
//            }
//            Intent intent = new Intent(getActivity(), IMGroupChatActivity.class);
//            intent.putExtra("group", groupBean);
//            startActivityForResult(intent, IM_CHAT_RESTQUECODE);
//        }else {
//        }
//    }
//
//    /**
//     * 加入群聊（尝试加入群聊，成功直接进入 失败就弹窗）
//     * @param groupBean
//     */
//
//    private void applyIntoGroup(final IMGroupBean groupBean) {
//        IMGetIMMemberData bean=new IMGetIMMemberData();
//        bean.setGroupId(groupBean.getGroupId());
//        String json = new Gson().toJson(bean);
//        RequestBody body = RequestBody.create(JSON, json);
//        Request request = new Request.Builder()
//                .url(IMSConfig.HTTP_IM_JOIN_GROUP)
//                .addHeader("Authorization", IMPreferenceUtil.getPreference_String(IMSConfig.SAVE_TOKEN,""))
//                .post(body)
//                .build();
//        Call call = okHttpClient.newCall(request);
//        call.enqueue(new Callback() {
//            @Override
//            public void onFailure(Call call, IOException e) {
//                IMLogUtil.d("MyOwnTag:", "IMSUiManager " +"(onFailure) " +"申请加入群聊");
//            }
//            @Override
//            public void onResponse(Call call, final Response response) throws IOException {
//                getActivity().runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        IMLoginSuccessBean bean= null;
//                        try {
//                            bean= new Gson().fromJson(response.body().string(), IMLoginSuccessBean.class);
//                            IMLogUtil.d("MyOwnTag:", "IMSUiManager " +"(尝试加入群聊) " +new Gson().toJson(bean));
//                            if(bean.getCode().equals("NEED_APPLY_JOIN_GROUP")){
//                                showDialog("温馨提示!", "您还不是该群的成员，不能进行此操作，请先申请加入群聊", new DialogInterface.OnClickListener() {
//                                    @Override
//                                    public void onClick(DialogInterface dialog, int which) {
//                                        enterGroup(groupBean); //申请加入群
//                                    }
//                                });
//                            }
//                            if(bean.getCode().equals("NEED_WAIT_JOIN_GROUP_AUDIT")){
//                                Toast.makeText(getActivity(), bean.getMessage(), Toast.LENGTH_SHORT).show();
//                            }
//                            if(bean.getCode().equals("GROUP_THAN_LIMIT")){
//                                Toast.makeText(getActivity(), bean.getMessage(), Toast.LENGTH_SHORT).show();
//                            }
//
//
//
//                        }catch (Exception e){
//                            Toast.makeText(getActivity(), "申请失败，群不存在", Toast.LENGTH_SHORT).show();
//                            return;
//                        }
//                        if(bean.getMessage().equals("成功")){
//                            Intent intent = new Intent(getActivity(), IMGroupChatActivity.class);
//                            intent.putExtra("group", groupBean);
//                            startActivityForResult(intent, IM_CHAT_RESTQUECODE);
//                        }
//                    }
//                });
//            }
//        });
//    }
//    /**
//     * 申请加入群聊
//     * @param groupBean
//     */
//    private void enterGroup(final IMGroupBean groupBean) {
//        IMGetIMMemberData bean=new IMGetIMMemberData();
//        bean.setGroupId(groupBean.getGroupId());
//        String json = new Gson().toJson(bean);
//        RequestBody body = RequestBody.create(JSON, json);
//        Request request = new Request.Builder()
//                .url(IMSConfig.HTTP_IM_APPLY_GROUP)
//                .addHeader("Authorization", IMPreferenceUtil.getPreference_String(IMSConfig.SAVE_TOKEN,""))
//                .post(body)
//                .build();
//        Call call = okHttpClient.newCall(request);
//        call.enqueue(new Callback() {
//            @Override
//            public void onFailure(Call call, IOException e) {
//                IMLogUtil.d("MyOwnTag:", "IMSUiManager " +"(onFailure) " +"申请加入群聊");
//            }
//            @Override
//            public void onResponse(Call call, final Response response) throws IOException {
//                getActivity().runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        try {
//                            IMLoginSuccessBean   bean= new Gson().fromJson(response.body().string(), IMLoginSuccessBean.class);
//                            IMLogUtil.d("MyOwnTag:", "IMSUiManager " +"(申请加入群聊) " +new Gson().toJson(bean));
//                            if(bean.getCode().equals("OK")){
//                                Toast.makeText(getActivity(), "申请成功，请等待审核", Toast.LENGTH_SHORT).show();
//                            }
//
//                        }catch (Exception e){
//                            Toast.makeText(getActivity(), "申请失败，群不存在", Toast.LENGTH_SHORT).show();
//                            return;
//                        }
//                    }
//                });
//            }
//        });
//    }
//    /**
//     * 清空消息记录刷新界面
//     */
//    private void handlerDeleteGroupMessage(IMMessageDetelGroupEvevt event) {
//        String groupId = event.getGroupId();
//        if(TextUtils.isEmpty(groupId)){
//            for (int i = 0; i < groups.size(); i++) {
//                groups.get(i).setLastMessage("[暂无消息]");
//                groups.get(i).setLastMessageTime(0);
//                IMPreferenceUtil.setPreference_String(groups.get(i).getGroupId() + IMSConfig.IM_UNEREAD_GROUP_CHAT, "0");//把对应人员的未读消息清空
//                groups.get(i).setLastSendstate(IMSConfig.IM_SEND_STATE_SUCCESS);
//            }
//        }else {
//            for (int i = 0; i < groups.size(); i++) {
//                if(groups.get(i).getGroupId().equals(groupId)){
//                    groups.get(i).setLastMessage("[暂无消息]");
//                    groups.get(i).setLastMessageTime(0);
//                    groups.get(i).setLastSendstate(IMSConfig.IM_SEND_STATE_SUCCESS);
//                }
//            }
//        }
//
//        adapter.notifyDataSetChanged();
//    }
//
//    /**
//     * 详情界面发送消息，通知这边更改状态(还未从服务器获取)
//     */
//    private void handlerSendMessage(IMSendGroupMessageEvent event) {
//        final String groupId = event.getGroupId();
//        final IMGroupDetailBean data = event.getData();
//        //发送消息时修改对应item的发送状态（发送时候在控制类里面保存发送消息，所以这里不保存）
//        for (int i = 0; i < groups.size(); i++) {
//            if(groups.get(i).getGroupId().equals(groupId)){
//                groups.get(i).setLastFingerId(data.getFingerprint());
//                groups.get(i).setLastMsgType(data.getMsgType());
//                groups.get(i).setLastMsgSendType(IMSConfig.MSG_SEND);
//                groups.get(i).setLastMessageTime(data.getTimestamp());
//                groups.get(i).setLastSendstate(IMSConfig.IM_SEND_STATE_SENDING);
//                IMGroupBean   groupBean = groups.get(i);
//                groups.remove(i);
//                groups.add(0, groupBean);
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
//}
