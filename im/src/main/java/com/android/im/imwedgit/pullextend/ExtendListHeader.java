package com.android.im.imwedgit.pullextend;


import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.os.Vibrator;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.android.im.DaoManager;
import com.android.im.R;
import com.android.im.imadapter.LatestUseAdapter;
import com.android.im.imadapter.MyCollectionAdapter;
import com.android.im.imadapter.MyVpAdapter;
import com.android.im.imbean.AddCollectionBean;
import com.android.im.imbean.SmallProgramBean;
import com.android.im.imbean.SmallProgramBeanDao;
import com.android.im.imeventbus.IMAddCollectionEvent;
import com.android.im.imeventbus.IMCancerCollectionEvent;
import com.android.im.imeventbus.IMDeleteRecentEvent;
import com.android.im.imeventbus.IMDeleteShowEvent;
import com.android.im.imui.activity.SmallProgramActivity;
import com.android.im.imui.activity.SmallProgramSearchActivity;
import com.android.im.imui.activity.SmallSearchActivity;
import com.android.im.imutils.Global;
import com.android.im.imwedgit.dragDelete.RecycleViewLongPressMove;
import com.android.im.imwedgit.dragDelete.RecycleViewLongPressMove2;
import com.blankj.utilcode.util.LogUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.chad.library.adapter.base.listener.OnItemDragListener;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import me.relex.circleindicator.CircleIndicator3;

import static android.content.Context.ACTIVITY_SERVICE;


/**
 * 这个类封装了下拉刷新的布局
 */
public class ExtendListHeader extends ExtendLayout implements RecycleViewLongPressMove.OnLongPressMoveLisener,RecycleViewLongPressMove2.OnLongPressMoveLisener {


    private Context context;
    float containerHeight = Global.dip2px(160);
    float listHeight/* = Global.dip2px(460)*/;
    boolean arrivedListHeight = false;
    private RecyclerView mRecyclerView1;
    private RecyclerView mRecyclerView2;
    private LinearLayout canSeeLL;

    /**
     * 原点
     */

    private ExpendPoint mExpendPoint;
    private ImageView upIV;
    private NestedScrollView mNestedScrollView;
    private ViewPager2 vpCollection;
    private LatestUseAdapter latestUseAdapter;
    private LinearLayout llSearch;
    private MyCollectionAdapter myCollectionAdapter;
    private RelativeLayout rlBg;
    private TextView tvLatest;
    private TextView tvSmall;
    private TextView tvNull;
    private boolean isFlagB = false;
    private boolean isFirstIn = true;
    private boolean isFirstOut = true;
    private boolean isVibrator=true;

    /**
     * 构造方法
     *
     * @param context context
     */
    public ExtendListHeader(Context context) {
        super(context);
        init(context);
    }


    /**
     * 构造方法
     *
     * @param context context
     * @param attrs   attrs
     */
    public ExtendListHeader(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(final Context context) {
        this.context = context;
        //获取屏幕的高度
        DisplayMetrics dm = getResources().getDisplayMetrics();
        listHeight = dm.heightPixels;//必须先这样暂定为屏幕高度
    }

    @Override
    public void restListSize(int h) {
        super.restListSize(h);
        listHeight = h;
        Log.e("gdy","当前高度哈哈哈哈哈："+listHeight);
        getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                ViewGroup.LayoutParams layoutParams1 = getLayoutParams();
                Log.e("gdy","改变前1："+layoutParams1.height);
                layoutParams1.height = (int) listHeight;
                setLayoutParams(layoutParams1);
                ViewGroup.LayoutParams layoutParams = canSeeLL.getLayoutParams();
                Log.e("gdy","改变前2："+layoutParams.height);
                layoutParams.height = (int) listHeight;
                canSeeLL.setLayoutParams(layoutParams);
                getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        });
    }


    @Override
    protected void bindView(View container) {
        mRecyclerView1 = findViewById(R.id.list1);
        mRecyclerView2 = findViewById(R.id.list2);
        mExpendPoint = findViewById(R.id.expend_point);
        mNestedScrollView = findViewById(R.id.mNestedScrollView);
//        vpCollection = findViewById(R.id.vp_collection);
//        indicator = findViewById(R.id.indicator);
        upIV = findViewById(R.id.upIV);
        llSearch = findViewById(R.id.ll_search);
        rlBg = findViewById(R.id.rl_bg);
        tvLatest = findViewById(R.id.tv_latest);
        tvSmall = findViewById(R.id.tv_small);
        tvNull = findViewById(R.id.tv_null);
        upIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (resetLayout != null) {
                    resetLayout.onRest();
                }
            }
        });

        tvLatest.setVisibility(GONE);
        tvSmall.setVisibility(GONE);
        rlBg.setVisibility(GONE);
        initRV();
        initRV2();
//        initVP();
        childScrollView = mNestedScrollView;
        canSeeLL = findViewById(R.id.canSeeLL);

        llSearch.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, SmallSearchActivity.class);
                context.startActivity(intent);
            }
        });
    }

    private void initRV() {
        GridLayoutManager layoutManager = new GridLayoutManager(context, 5);
        mRecyclerView1.setLayoutManager(layoutManager);
        latestUseAdapter = new LatestUseAdapter();
//        ((ViewGroup) mRecyclerView1.getParent()).setClipChildren(false);
        latestUseAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                SmallProgramBean bean = latestUseAdapter.getData().get(position);
                if (bean.isFlag()) {
                    Intent intent = new Intent(context, SmallProgramSearchActivity.class);
                    context.startActivity(intent);
                } else {
                    new SmallProgramActivity().toActivity(context,latestUseAdapter.getData().get(position));
                }
            }
        });
        mRecyclerView1.setAdapter(latestUseAdapter);

        RecycleViewLongPressMove recycleViewLongPressMove = new RecycleViewLongPressMove(mRecyclerView1, null, rlBg);
        recycleViewLongPressMove.setOnLongPressMoveLisener(this);
    }

    private void initRV2() {
        GridLayoutManager layoutManager = new GridLayoutManager(context, 5);
        mRecyclerView2.setLayoutManager(layoutManager);
        myCollectionAdapter = new MyCollectionAdapter();
//        ((ViewGroup) mRecyclerView2.getParent()).setClipChildren(false);
        myCollectionAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                SmallProgramBean bean = myCollectionAdapter.getData().get(position);
                if (bean.isFlag()) {
                    Intent intent = new Intent(context, SmallProgramSearchActivity.class);
                    context.startActivity(intent);
                } else {
                    SmallProgramBean smallProgramBean = myCollectionAdapter.getData().get(position);
                    smallProgramBean.setIsConllection("Y");
                    new SmallProgramActivity().toActivity(context,smallProgramBean);
                }
            }
        });
        mRecyclerView2.setAdapter(myCollectionAdapter);

        RecycleViewLongPressMove2 recycleViewLongPressMove2 = new RecycleViewLongPressMove2(mRecyclerView2, null, rlBg);
        recycleViewLongPressMove2.setOnLongPressMoveLisener(this);

//        mRecyclerView2.addOnScrollListener(new RecyclerView.OnScrollListener() {
//            @Override
//            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
//                super.onScrollStateChanged(recyclerView, newState);
//                Log.i("WOLF","===================onScrollStateChanged:"+newState);
//                if(newState==1){
//                    mRecyclerView2.setPadding(10,0,0,0);
//                }else if(newState==0){
//                    mRecyclerView2.setPadding(0,0,0,0);
//                }
//            }
//
//            @Override
//            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
//                super.onScrolled(recyclerView, dx, dy);
//                Log.i("WOLF","===================onScrolled:"+dy);
//
//            }
//
//        });
    }

//    private void initVP() {
//        // 设置方向
//        vpCollection.setOrientation(ViewPager2.ORIENTATION_HORIZONTAL);
//        vpAdapter = new MyVpAdapter();
//        vpCollection.setAdapter(vpAdapter);
//        vpCollection.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
//            @Override
//            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
//                super.onPageScrolled(position, positionOffset, positionOffsetPixels);
//            }
//
//            @Override
//            public void onPageSelected(int position) {
//                super.onPageSelected(position);
//                Log.i("xunming", "当前位置：" + position);
//            }
//
//            @Override
//            public void onPageScrollStateChanged(int state) {
//                super.onPageScrollStateChanged(state);
//            }
//        });
//
//        indicator.setViewPager(vpCollection);
//        vpAdapter.registerAdapterDataObserver(indicator.getAdapterDataObserver());
//    }


    @Override
    protected View createLoadingView(Context context, AttributeSet attrs) {
        return LayoutInflater.from(context).inflate(R.layout.extend_header, null);
    }


    @Override
    public int getContentSize() {
        return (int) (containerHeight);
    }

    @Override
    public int getListSize() {
        return (int) (listHeight);
    }


    @Override
    protected void onReset() {
        mExpendPoint.setVisibility(VISIBLE);
        mExpendPoint.setAlpha(1);
        mExpendPoint.setTranslationY(0);
        childScrollView.setTranslationY(0);
        arrivedListHeight = false;
        upIV.setVisibility(GONE);
        if (stateLayout != null) {
            stateLayout.onStateChange(State.RESET);
        }
    }

    @Override
    protected void onReleaseToRefresh() {

    }

    @Override
    protected void onPullToRefresh() {

    }

    private int nestedScrollViewTop;

    @Override
    protected void onArrivedListHeight() {
        arrivedListHeight = true;
        upIV.setVisibility(View.VISIBLE);

        if (nestedScrollViewTop == 0) {
            int[] intArray = new int[2];
            mNestedScrollView.getLocationOnScreen(intArray);
            nestedScrollViewTop = intArray[1];
        }
        //滑动到canSeeLL的位置
        int[] intArray = new int[2];
        canSeeLL.getLocationOnScreen(intArray);//测量某View相对于屏幕的距离
        int distance = intArray[1] - nestedScrollViewTop;
        Log.e("gdy", "当前位置：" + distance);
//        mNestedScrollView.fling(distance);//添加上这句滑动才有效
//        mNestedScrollView.scrollBy(0,0);

        if (stateLayout != null) {
            stateLayout.onStateChange(State.arrivedListHeight);
        }
    }

    @Override
    protected void onRefreshing() {
    }

    @Override
    public void onPull(int offset) {
        Log.i("WOLF", "offset:" + offset);
        if (!arrivedListHeight) {
            Log.i("WOLF", "A");
            mExpendPoint.setVisibility(VISIBLE);
            float percent = Math.abs(offset) / containerHeight;
            int moreOffset = Math.abs(offset) - (int) containerHeight;
            if (percent <= 1.0f) {
                Log.i("WOLF", "B");
                mExpendPoint.setPercent(percent);
                mExpendPoint.setTranslationY(-Math.abs(offset) / 2 + mExpendPoint.getHeight() / 2);
                childScrollView.setTranslationY(-containerHeight);

                isFlagB = true;
            } else {
                Log.i("WOLF", "C");
                float subPercent = (moreOffset) / (listHeight - containerHeight);
                subPercent = Math.min(1.0f, subPercent);
                mExpendPoint.setTranslationY(-(int) containerHeight / 2 + mExpendPoint.getHeight() / 2 + (int) containerHeight * subPercent / 2);
                mExpendPoint.setPercent(1.0f);
                float alpha = (1 - subPercent * 2);
                mExpendPoint.setAlpha(Math.max(alpha, 0));
                childScrollView.setTranslationY(-(1 - subPercent) * containerHeight);

                if (isFlagB) {
                    Vibrator vibrator = (Vibrator) getContext().getSystemService(getContext().VIBRATOR_SERVICE);
                    vibrator.vibrate(25);
                    isFlagB = false;
                }
            }
        }
        if (Math.abs(offset) >= listHeight) {
            Log.i("WOLF", "D");
            mExpendPoint.setVisibility(GONE);
            childScrollView.setTranslationY(-(Math.abs(offset) - listHeight) / 2);
            mNestedScrollView.scrollTo(0, 0);
        } else {
            Log.i("WOLF", "E");
            if (pullDownLisentner != null) {
                pullDownLisentner.onPullDown(offset);
            }
        }

    }

    /**
     * 我的收藏
     *
     * @param data
     */
    public void setMyCollectionData(List<SmallProgramBean> data) {
        myCollectionAdapter.setNewData(data);

        if (latestUseAdapter.getData().size() == 0 && myCollectionAdapter.getData().size() == 0) {
            tvLatest.setVisibility(GONE);
            tvSmall.setVisibility(GONE);
            rlBg.setVisibility(GONE);
            tvNull.setVisibility(VISIBLE);
        } else if (myCollectionAdapter.getData().size() == 0) {
            tvLatest.setVisibility(VISIBLE);
            tvSmall.setVisibility(VISIBLE);
            rlBg.setVisibility(VISIBLE);
            tvNull.setVisibility(GONE);
        } else {
            tvLatest.setVisibility(VISIBLE);
            tvSmall.setVisibility(VISIBLE);
            rlBg.setVisibility(GONE);
            tvNull.setVisibility(GONE);
        }
    }

    /**
     * 最近使用
     *
     * @param data
     */
    public void setRecentlyUseData(List<SmallProgramBean> data) {
        List<SmallProgramBean> list = new ArrayList<>();
        if (data.size() > 5) {
            for (int i = 0; i < 5; i++) {
                if (i == 4) {
                    data.get(4).setFlag(true);
                }
                list.add(data.get(i));
            }
        } else if (data.size() == 5) {
            for (int i = 0; i < 5; i++) {
                list.add(data.get(i));
            }
        } else {
            list = data;
        }
        latestUseAdapter.setNewData(list);

        if (list.size() == 0 && myCollectionAdapter.getData().size() == 0) {
            tvLatest.setVisibility(GONE);
            tvSmall.setVisibility(GONE);
            rlBg.setVisibility(GONE);
            tvNull.setVisibility(VISIBLE);
        } else if (myCollectionAdapter.getData().size() == 0) {
            tvLatest.setVisibility(VISIBLE);
            tvSmall.setVisibility(VISIBLE);
            rlBg.setVisibility(VISIBLE);
            tvNull.setVisibility(GONE);
        } else {
            tvLatest.setVisibility(VISIBLE);
            tvSmall.setVisibility(VISIBLE);
            rlBg.setVisibility(GONE);
            tvNull.setVisibility(GONE);
        }
    }

    @Override
    public void onNomalView() {
        Log.d("WOLF", "onNomalView");
        EventBus.getDefault().post(new IMDeleteShowEvent(false));
        isVibrator=true;
    }

    @Override
    public void onMoveView(boolean isTouchPointInView,boolean isTouchDeleteInView, int position) {
        LogUtils.i("WOLF", "position:" + position);
        if(position==4&&latestUseAdapter.getData().get(4).getFlag()){
            return;
        }
        if (position == -1) {
            return;
        }
        if(isVibrator){
            isVibrator=false;
            Vibrator vibrator = (Vibrator) getContext().getSystemService(getContext().VIBRATOR_SERVICE);
            vibrator.vibrate(25);
        }
        if (isTouchPointInView) {
            Log.d("WOLF", "onMoveView:!!!!!!!!!!!!!!!!!!!");
//            isFirstOut=true;
//            if (isFirstIn) {
//                latestUseAdapter.getData().get(position).setDrag(true);
//                latestUseAdapter.notifyItemChanged(position);
//                isFirstIn = false;
//            }\
            EventBus.getDefault().post(new IMDeleteShowEvent(true));
        } else {
            Log.d("WOLF", "onMoveView");
//            isFirstIn=true;
//            if (isFirstOut) {
//                latestUseAdapter.getData().get(position).setDrag(false);
//                latestUseAdapter.notifyItemChanged(position);
//                isFirstOut=false;
//            }
            EventBus.getDefault().post(new IMDeleteShowEvent(true));
        }

        if (isTouchDeleteInView) {
            IMDeleteShowEvent event=new IMDeleteShowEvent();
            event.setCanDelete(true);
            event.setShow(true);
            EventBus.getDefault().post(event);
        } else {
            IMDeleteShowEvent event=new IMDeleteShowEvent();
            event.setCanDelete(false);
            event.setShow(true);
            EventBus.getDefault().post(event);
        }
    }

    @Override
    public void onOperation(int position) {
        Log.d("WOLF", "onOperation" + ">" + position);
        if(position==4&&latestUseAdapter.getData().size()>=5){
            if(latestUseAdapter.getData().get(4).isFlag()){
                return;
            }
        }

        boolean same=false;
        for(SmallProgramBean smallProgramBean:myCollectionAdapter.getData()){
            if(smallProgramBean.getProgramId().equals(latestUseAdapter.getData().get(position).getProgramId())){
                same=true;
            }
        }
        if(same){
            return;
        }
        myCollectionAdapter.getData().add(latestUseAdapter.getData().get(position));
        myCollectionAdapter.notifyItemInserted(myCollectionAdapter.getData().size()-1);
        if (myCollectionAdapter.getData().size() == 0) {
            tvLatest.setVisibility(VISIBLE);
            tvSmall.setVisibility(VISIBLE);
            rlBg.setVisibility(VISIBLE);
            tvNull.setVisibility(GONE);
        } else {
            tvLatest.setVisibility(VISIBLE);
            tvSmall.setVisibility(VISIBLE);
            rlBg.setVisibility(GONE);
            tvNull.setVisibility(GONE);
        }
        //收藏操作
        SmallProgramBean bean = latestUseAdapter.getData().get(position);
        EventBus.getDefault().post(new IMAddCollectionEvent(new AddCollectionBean(bean.getProgramId())));
    }

    @Override
    public void onDelete(int position) {
        LogUtils.i("WOLF","删除："+position);
        EventBus.getDefault().post(new IMDeleteRecentEvent(position));
    }

    @Override
    public void onNomalView2() {
        EventBus.getDefault().post(new IMDeleteShowEvent(false));
        isVibrator=true;
    }

    @Override
    public void onMoveView2(boolean isTouchPointInView,boolean isTouchDeleteInView, int position) {
//        EventBus.getDefault().post(new IMDeleteShowEvent(true));
//        mRecyclerView2.setPadding(0,0,0,0);

        if(isVibrator){
            isVibrator=false;
            Vibrator vibrator = (Vibrator) getContext().getSystemService(getContext().VIBRATOR_SERVICE);
            vibrator.vibrate(25);
        }

        if (isTouchDeleteInView) {
            IMDeleteShowEvent event=new IMDeleteShowEvent();
            event.setCanDelete(true);
            event.setShow(true);
            EventBus.getDefault().post(event);
        } else {
            IMDeleteShowEvent event=new IMDeleteShowEvent();
            event.setCanDelete(false);
            event.setShow(true);
            EventBus.getDefault().post(event);
        }
    }

    @Override
    public void onOperation2(int position) {

    }

    @Override
    public void onDelete2(int position) {
        LogUtils.i("WOLF","删除2删除2删除2删除2");
        EventBus.getDefault().post(new IMCancerCollectionEvent(position));
    }

    public interface PullDownLisentner {
        void onPullDown(int offset);
    }

    public PullDownLisentner pullDownLisentner;

    public void setPullDownLisentner(PullDownLisentner pullDownLisentner) {
        this.pullDownLisentner = pullDownLisentner;
    }
}
