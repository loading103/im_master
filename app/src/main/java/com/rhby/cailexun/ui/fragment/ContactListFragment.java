package com.rhby.cailexun.ui.fragment;

import android.animation.Keyframe;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.content.Intent;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.im.IMSManager;
import com.android.im.imadapter.baseadapter.IMBaseRecycleViewAdapter_T;
import com.android.im.imeventbus.IMAddFriendSucessEvent;
import com.android.im.imeventbus.IMAgreeAddFriendSucessEvent;
import com.android.im.imeventbus.IMContactReshLastEvent;
import com.android.im.imeventbus.IMDeleteFriendSucessEvent;
import com.android.im.imeventbus.IMDeleteFriendSucessedEvent;
import com.android.im.imeventbus.IMGoToFirstFragmentEvent;
import com.android.im.imeventbus.IMNumberEvent;
import com.android.im.imnet.IMHttpResultObserver;
import com.android.im.imnet.http.IMHttpsService;
import com.android.im.imui.activity.IMAddFriendActivity;
import com.android.im.imui.activity.IMCreatGroupActivity;
import com.android.im.imui.activity.IMGroupChatActivity;
import com.android.im.imui.activity.IMNewFriendActivity;
import com.android.im.imui.activity.IMPersonalChatActivity;
import com.android.im.imui.activity.IMTotleGroupActivity;
import com.android.im.imui.activity.IMQRCActivity;
import com.android.im.imutils.IMStopClickFast;
import com.android.im.imview.IMPopWindowUtils;
import com.rhby.cailexun.R;
import com.rhby.cailexun.adapter.ContactHeaderAdapter;
import com.rhby.cailexun.adapter.ContactSortAdapter;
import com.rhby.cailexun.ui.base.BaseFragment;
import com.rhby.cailexun.widget.CharacterParser;
import com.android.im.imutils.PinyinComparator;
import com.rhby.cailexun.widget.contactview.SideBar;
import com.android.nettylibrary.IMSConfig;
import com.android.nettylibrary.greendao.entity.IMConversationBean;
import com.android.nettylibrary.greendao.entity.IMPersonBean;
import com.android.nettylibrary.greendao.greenutils.DaoUtils;
import com.android.nettylibrary.utils.IMLogUtil;
import com.blankj.utilcode.util.KeyboardUtils;
import com.google.gson.Gson;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;

public class ContactListFragment extends BaseFragment implements AbsListView.OnScrollListener, View.OnClickListener, IMPopWindowUtils.OnClickListener, AdapterView.OnItemClickListener, TextWatcher {
    @BindView(R.id.bar)
    SideBar bar;
    @BindView(R.id.list)
    ListView mlistView;
    @BindView(R.id.overlay)
    TextView mOverlayTv;
    @BindView(R.id.lin_head)
    LinearLayout mLayoutOverlay;
    @BindView(R.id.im_iv_add)
    ImageView mIvAdd;
    @BindView(R.id.im_iv_finish)
    ImageView mIvFinish;
    @BindView(R.id.im_ll_finish)
    RelativeLayout mllFinish;
    @BindView(R.id.ll_right)
    RelativeLayout mllRight;
    private PinyinComparator pinyinComparator;
    private CharacterParser characterParser;
    private RecyclerView mRecycle;
    private ContactHeaderAdapter adapter;
    private List<IMConversationBean> list;
    private TextView mTvNumber;
    private TextView mAddNumber;
    private EditText mTvSearch;
    private Animation operatingAnim;
    private Animation operatingAnim1;
    private LinearLayout llSearch;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_contact_list;
    }

    @Override
    protected void initView(View view) {
        mllFinish.setOnClickListener(this);
        mllRight.setOnClickListener(this);
        initAnim();
        // 实例化汉字转拼音类
        EventBus.getDefault().register(this);
        characterParser = CharacterParser.getInstance();
        pinyinComparator = new PinyinComparator();
        mlistView.setOnScrollListener(this);
        mlistView.addHeaderView(newHeadView());
        // 设置右侧触摸监听

        bar.setOnTouchingLetterChangedListener(new SideBar.OnTouchingLetterChangedListener() {
            @Override
            public void onTouchingLetterChanged(String s) {
                // 该字母首次出现的位置
                int position = mAdapter.getPositionForSection(s.charAt(0));
                if (position != -1) {
                    mlistView.setSelection(position+1);
                }
            }
        });
        initdata();
    }

    /**
     * 初始化旋转动画
     */
    private void initAnim() {
        operatingAnim = AnimationUtils.loadAnimation(getActivity(), com.android.im.R.anim.anim_rotate_45);
        operatingAnim.setFillAfter(true);
        operatingAnim1 = AnimationUtils.loadAnimation(getActivity(), com.android.im.R.anim.anim_rotate_90);
        operatingAnim1.setFillAfter(true);
    }



    private void initdata() {
        nameList= DaoUtils.getInstance().queryAllMessageData();
        IMPopWindowUtils.getInstance(getActivity()).setOnListener(this);
        if(nameList==null || nameList.size()==0){
            mLayoutOverlay.setVisibility(View.GONE);
        }
        BindViewAdapter(nameList);
    }
    private List<IMPersonBean>nameList=new ArrayList<>();
    private List<IMPersonBean>datas=new ArrayList<>();
    private ContactSortAdapter mAdapter;
    public void BindViewAdapter(List<IMPersonBean> data){
        // 根据a-z进行排序源数据
        List<IMPersonBean> IMPersonBeans = filledData(data);
        Collections.sort(filledData(IMPersonBeans), pinyinComparator);
        this.datas.clear();
        this.datas.addAll(IMPersonBeans);
        setLetter(datas);
        if(mAdapter==null){
            mAdapter = new ContactSortAdapter(getActivity(),datas);
            mlistView.setAdapter(mAdapter);
            mlistView.setOnItemClickListener(this);
        }else {
            mAdapter.notifyDataSetChanged();
        }
    }

    /**
     * 获取需要显示哪些字母
     */
    private void setLetter(List<IMPersonBean> datas) {
        if(datas==null || datas.size()==0){
            return;
        }
        StringBuffer sb=new StringBuffer();
        for (int i = 0; i < datas.size(); i++) {
            if(!sb.toString().contains(datas.get(i).getLetter())){
                sb.append(datas.get(i).getLetter());
            }
        }
        IMLogUtil.d("MyOwnTag:", "ContactListFragment ----" +sb.toString());
        bar.setLetter(sb.toString());
    }

    /**
     * 为数据源添加字母letter的字段 用来排序
     * @param date
     * @return
     */
    private List<IMPersonBean> filledData(List<IMPersonBean> date) {
        for (int i = 0; i < date.size(); i++) {
            // 汉字转换成拼音
            String pinyin = characterParser.getSelling(date.get(i).getNickName());
            String sortString = pinyin.substring(0, 1).toUpperCase();
            // 正则表达式，判断首字母是否是英文字母，ru如果是数字则为#
            if (sortString.matches("[A-Z]")) {
                date.get(i).setLetter(sortString.toUpperCase());
            } else {
                date.get(i).setLetter("#");
            }
        }
        return date;
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {

    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        if(datas.size()==0){
            return;
        }
        if(firstVisibleItem==0){//当为第一个时影藏，避免listview下拉时显示2个A导航条
            mLayoutOverlay.setVisibility(View.GONE);
        }else {
            mLayoutOverlay.setVisibility(View.VISIBLE);
        }
        if(firstVisibleItem>=1){
            firstVisibleItem=firstVisibleItem-1;
        }
        String letter = datas.get(firstVisibleItem).getLetter();
        bar.changeLetter(letter.charAt(0));
        int nextPosition = firstVisibleItem + 1;
//        //下一个item所在的分组
        int nextSelection = getSelectionFromPosition(nextPosition);
        if(getPositionFromSelection(nextSelection) == nextPosition) {
            View v = mlistView.getChildAt(0);
            int offsetY = v.getBottom() - mOverlayTv.getHeight();
            if(offsetY < 0){
                mLayoutOverlay.setPadding(0, offsetY, 0, 0);
            }else{
                mLayoutOverlay.setPadding(0, 0, 0, 0);
            }
        }else{
            mLayoutOverlay.setPadding(0, 0, 0, 0);
        }
        mOverlayTv.setText(letter+"");
    }

    /**
     * 获取数据源中，该字母对应分组的第一条数据 在数据源中所处的位置
     * @param letter
     * @return
     */
    private int getPositionFromSelection(int letter){
        for (int i = 0; i < datas.size(); i++) {
            IMPersonBean data = datas.get(i);
            if(data.getLetter().charAt(0) == letter ){
                return i;
            }
        }
        return -1;
    }
    private int getSelectionFromPosition(int position){
        if(datas==null || datas.size()<=1){
            return -1;
        }
        return datas.get(position).getLetter().charAt(0);
    }

    /**
     * 添加头部
     */
    private View newHeadView() {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.item_contacts_typeone,null);
        mTvSearch = view.findViewById(R.id.im_tv_serch);
        mTvNumber = view.findViewById(R.id.mtv_person);
        mRecycle = view.findViewById(R.id.recycler);
        mAddNumber = view.findViewById(R.id.tv_unread_addnumber);
        llSearch = view.findViewById(R.id.ll_search);
        RelativeLayout    mTvFriend =  view.findViewById(R.id.rl_new_friend);
        RelativeLayout     mTvgroup =  view.findViewById(R.id.rl_new_group);
        RelativeLayout     mTvClx =  view.findViewById(R.id.rl_group);
        mTvFriend.setOnClickListener(this);
        mTvgroup.setOnClickListener(this);
        mTvClx.setOnClickListener(this);
        mTvSearch.addTextChangedListener(this);
        list = DaoUtils.getInstance().queryAllConversationData(10);
        if(list==null || list.size()==0){
            mTvNumber.setText("最近联系人");
            mRecycle.setVisibility(View.GONE);
        }else {
            mTvNumber.setText("最近联系人("+list.size()+")");
            mRecycle.setVisibility(View.VISIBLE);
            adapter = new ContactHeaderAdapter(getActivity(),R.layout.item_latest_contact, list);
            mRecycle.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
            mRecycle.setNestedScrollingEnabled(false);
            mRecycle.setAdapter(adapter);
            adapter.setOnItemClickListner(new IMBaseRecycleViewAdapter_T.OnItemClickListner() {
                @Override
                public void onItemClickListner(View v, int position, Object t) {
                    if(IMStopClickFast.isFastClick()) {
                        if (!TextUtils.isEmpty(list.get(position).getGroupId())) {
                            Intent intent = new Intent(getActivity(), IMGroupChatActivity.class);
                            intent.putExtra("group", list.get(position));
                            getActivity().startActivity(intent);
                            EventBus.getDefault().post(new IMGoToFirstFragmentEvent());
                        } else {
                            Intent intent = new Intent(getActivity(), IMPersonalChatActivity.class);
                            intent.putExtra("user", list.get(position));
                            startActivity(intent);
                            EventBus.getDefault().post(new IMGoToFirstFragmentEvent());
                        }
                    }
                }
            });
        }
        KeyboardUtils.registerSoftInputChangedListener(getActivity(), height -> {
            if(height==0){
                int newHeight = LinearLayout.LayoutParams.WRAP_CONTENT;
                LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) mTvSearch.getLayoutParams();
                lp.width = newHeight;
                mTvSearch.setLayoutParams(lp);
                mTvSearch.clearFocus();
            }else {
                int newHeight = LinearLayout.LayoutParams.MATCH_PARENT;
                LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) mTvSearch.getLayoutParams();
                lp.width = newHeight;
                mTvSearch.setLayoutParams(lp);
            }
        });
        llSearch.setOnClickListener(v -> KeyboardUtils.showSoftInput(mTvSearch));
        return  view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.rl_new_friend:
                if(IMStopClickFast.isFastClick()) {
                    Intent intent = new Intent(getActivity(), IMNewFriendActivity.class);
                    startActivity(intent);
                }
                break;
            case R.id.rl_new_group:
                if(IMStopClickFast.isFastClick()) {
                    Intent intent1 = new Intent(getActivity(), IMTotleGroupActivity.class);
                    startActivity(intent1);
                }
                break;
            case R.id.im_ll_finish:
                ObjectAnimator animator = tada(mIvFinish);
                animator.setRepeatCount(0);
                animator.start();
                break;
            case R.id.ll_right:
                IMPopWindowUtils.getInstance(getActivity()).showPopListView(mIvAdd);
                mIvAdd.startAnimation(operatingAnim);
                break;
            case R.id.rl_group:
                if(IMStopClickFast.isFastClick()){
                    IMConversationBean bean=new IMConversationBean();
                    bean.setConversationId(IMSConfig.CLX_ID);
                    bean.setMemberId(IMSConfig.CLX_ID);
                    bean.setConversationName("彩乐讯团队");
                    Intent intent = new Intent(getActivity(), IMPersonalChatActivity.class);
                    intent.putExtra("user",bean);
                    startActivity(intent);
//                    EventBus.getDefault().post(new IMGoToFirstFragmentEvent());
                }
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//        if(IMStopClickFast.isFastClick()) {
//            Intent intent = new Intent(getActivity(), IMContactDetailActivity.class);
//            intent.putExtra("bean", datas.get((int) id));
//            startActivity(intent);
//        }
    }

    /**
     * 刷新最近联系人
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(final IMContactReshLastEvent event) {
        List<IMConversationBean> bean = DaoUtils.getInstance().queryAllConversationData(10);
        if(bean==null || bean.size()==0){
            mTvNumber.setText("最近联系人");
            mRecycle.setVisibility(View.GONE);
        }else {
            mRecycle.setVisibility(View.VISIBLE);
            list.clear();
            list.addAll(bean);
            if(adapter!=null){
                adapter.notifyDataSetChanged();
            }else {
                adapter = new ContactHeaderAdapter(getActivity(),R.layout.item_latest_contact, list);
                mRecycle.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
                mRecycle.setNestedScrollingEnabled(false);
                mRecycle.setAdapter(adapter);
                adapter.setOnItemClickListner(new IMBaseRecycleViewAdapter_T.OnItemClickListner() {
                    @Override
                    public void onItemClickListner(View v, int position, Object t) {
                        if(!TextUtils.isEmpty(list.get(position).getGroupId())){
                            Intent intent=new Intent(getActivity(), IMGroupChatActivity.class);
                            intent.putExtra("group", list.get(position));
                            startActivity(intent);
                            EventBus.getDefault().post(new IMGoToFirstFragmentEvent());
                        }else {
                            Intent intent=new Intent(getActivity(), IMPersonalChatActivity.class);
                            intent.putExtra("user", list.get(position));
                            startActivity(intent);
                            EventBus.getDefault().post(new IMGoToFirstFragmentEvent());
                        }

                    }
                });
            }

            mTvNumber.setText("最近联系人("+list.size()+")");
        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void OnListener(int position) {
        switch (position) {
            case 0:
                Intent intent = new Intent(getActivity(), IMCreatGroupActivity.class);
                startActivity(intent);
                break;
            case 1:
                Intent intent1 = new Intent(getActivity(), IMAddFriendActivity.class);
                startActivity(intent1);
                break;
            case 2:
                Intent intent2 = new Intent(getActivity(), IMQRCActivity.class);
                startActivity(intent2);
                break;
            case 3:
                if(mIvAdd==null ){
                    return;
                }
                mIvAdd.startAnimation(operatingAnim1);
                break;
        }
    }

    /**
     * 添加好友删除好友需要刷新好友界面
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(final IMAddFriendSucessEvent event) {
        getData();

    }
    /**
     * 添加好友删除好友需要刷新好友界面
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(final IMAgreeAddFriendSucessEvent event) {
        getData();

    }
    /**
     * 添加好友删除好友需要刷新好友界面
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(final IMDeleteFriendSucessEvent event) {
        getData();
    }
    /**
     * 添加好友删除好友需要刷新好友界面
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(final IMDeleteFriendSucessedEvent event) {
        getData();
    }
    /**
     * 通知通讯录里面我的朋友旁边的未读消息数
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(final IMNumberEvent event) {
        int number = event.getNumber();
        setNoReadAddNumber(number);
    }
    private void getData() {
        IMHttpsService.GetConversationListJson(new IMHttpResultObserver<List<IMPersonBean>>() {
            @Override
            public void onSuccess(List<IMPersonBean> persons, String message) {
                IMLogUtil.d("MyOwnTag:", "IMHttpGetConversationList获取会话人员列表信息成功" + new Gson().toJson(persons));
                if (persons != null && persons.size() > 0) {
                    DaoUtils.getInstance().deleteMessageData();
                    for (int i = 0; i < persons.size(); i++) {
                        persons.get(i).setMemberId(persons.get(i).getCustomerId());
                        persons.get(i).setNickName(persons.get(i).getNickName());
                        DaoUtils.getInstance().insertMessageData(persons.get(i));
                    }
                }
                List<IMPersonBean> beans = DaoUtils.getInstance().queryAllMessageData();
                BindViewAdapter(beans);
            }
            @Override
            public void _onError(Throwable e) {
                IMSManager.getInstance().setLoginInListener(false,e.getMessage());
                Toast.makeText(getActivity(),e.getMessage(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void _onErrorLocal(Throwable e, String message, String code) {
                IMSManager.getInstance().setLoginInListener(false,message);
                Toast.makeText(getActivity(),message, Toast.LENGTH_SHORT).show();
            }
        });
    }

    public  void  setNoReadAddNumber(int number){
        if(mAddNumber==null){
            return;
        }
        if(number==0){
            mAddNumber.setVisibility(View.GONE);
        }else {
            mAddNumber.setVisibility(View.VISIBLE);
            mAddNumber.setText(number+"");
        }

    }


    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        reSearchContent(s.toString());

    }
    /**
     * 搜索查询类容（模糊查询）
     */
    private void reSearchContent(String toString) {
        List<IMPersonBean> beans =new ArrayList<>();
        List<IMPersonBean> beans1 =  DaoUtils.getInstance().queryAllMessageData();
        if(beans1==null){
            return;
        }
        if(!TextUtils.isEmpty(toString)){
            Pattern pattern = Pattern.compile(toString);
            for (int i = 0; i <beans1.size(); i++) {
                Matcher matcher = pattern.matcher(beans1.get(i).getNickName());
                if(matcher.find()){
                    beans.add(beans1.get(i));
                }
            }
        }else {
            beans.addAll(beans1);
        }
        BindViewAdapter(beans);
    }


    public static ObjectAnimator tada(View view) {
        return tada(view, 1f);
    }
    public static ObjectAnimator tada(View view, float shakeFactor) {

        PropertyValuesHolder pvhScaleX = PropertyValuesHolder.ofKeyframe(View.SCALE_X,
                Keyframe.ofFloat(0f, 1f),
                Keyframe.ofFloat(.1f, .9f),
                Keyframe.ofFloat(.2f, .9f),
                Keyframe.ofFloat(.3f, 1.1f),
                Keyframe.ofFloat(.4f, 1.1f),
                Keyframe.ofFloat(.5f, 1.1f),
                Keyframe.ofFloat(.6f, 1.1f),
                Keyframe.ofFloat(.7f, 1.1f),
                Keyframe.ofFloat(.8f, 1.1f),
                Keyframe.ofFloat(.9f, 1.1f),
                Keyframe.ofFloat(1f, 1f)
        );

        PropertyValuesHolder pvhScaleY = PropertyValuesHolder.ofKeyframe(View.SCALE_Y,
                Keyframe.ofFloat(0f, 1f),
                Keyframe.ofFloat(.1f, .9f),
                Keyframe.ofFloat(.2f, .9f),
                Keyframe.ofFloat(.3f, 1.1f),
                Keyframe.ofFloat(.4f, 1.1f),
                Keyframe.ofFloat(.5f, 1.1f),
                Keyframe.ofFloat(.6f, 1.1f),
                Keyframe.ofFloat(.7f, 1.1f),
                Keyframe.ofFloat(.8f, 1.1f),
                Keyframe.ofFloat(.9f, 1.1f),
                Keyframe.ofFloat(1f, 1f)
        );

        PropertyValuesHolder pvhRotate = PropertyValuesHolder.ofKeyframe(View.ROTATION,
                Keyframe.ofFloat(0f, 0f),
                Keyframe.ofFloat(.1f, -6f * shakeFactor),
                Keyframe.ofFloat(.2f, -6f * shakeFactor),
                Keyframe.ofFloat(.3f, 6f * shakeFactor),
                Keyframe.ofFloat(.4f, -6f * shakeFactor),
                Keyframe.ofFloat(.5f, 6f * shakeFactor),
                Keyframe.ofFloat(.6f, -6f * shakeFactor),
                Keyframe.ofFloat(.7f, 6f * shakeFactor),
                Keyframe.ofFloat(.8f, -6f * shakeFactor),
                Keyframe.ofFloat(.9f, 6f * shakeFactor),
                Keyframe.ofFloat(1f, 0)
        );

        return ObjectAnimator.ofPropertyValuesHolder(view, pvhScaleX, pvhScaleY, pvhRotate).
                setDuration(1000);
    }
}
