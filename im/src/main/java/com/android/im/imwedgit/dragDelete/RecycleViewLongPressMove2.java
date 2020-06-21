package com.android.im.imwedgit.dragDelete;

import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ScreenUtils;
import com.blankj.utilcode.util.SizeUtils;

import java.util.List;

/**
 * Created by Administrator on 2019/12/31.
 * Describe:
 */
public class RecycleViewLongPressMove2 implements View.OnTouchListener {

    private MoveLayoutManager moveLayoutManager;
    private List<?> list;
    private View removeView;
    private int positionTag;
    private boolean isLongPress;

    /**
     * 移动拖拽ItemView 到指定区域操作
     *
     * @param recyclerView 当前集合RecyclerView
     * @param list         当前Adater的数据源 移动时排序使用
     * @param removeView   指定的操作View区域
     */
    public RecycleViewLongPressMove2(RecyclerView recyclerView, List<?> list, View removeView) {
        this.list = list;
        this.removeView = removeView;

        moveLayoutManager = new MoveLayoutManager(recyclerView.getContext(),5);
        recyclerView.setLayoutManager(moveLayoutManager);
        itemTouchHelper.attachToRecyclerView(recyclerView);
        recyclerView.setOnTouchListener(this);
    }


    //为RecycleView绑定触摸事件
    ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new ItemTouchHelper.Callback() {

        @Override
        public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
            int dragPosition = viewHolder.getAdapterPosition();//得到拖动ViewHolder的position
            positionTag = dragPosition;

            //首先回调的方法 返回int表示是否监听该方向
            int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN | ItemTouchHelper.START | ItemTouchHelper.END;//拖拽
            return makeMovementFlags(dragFlags, 0);
        }

        @Override
        public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
            //滑动事件
            int fromPosition = viewHolder.getAdapterPosition();//得到拖动ViewHolder的position
            int toPosition = target.getAdapterPosition();//得到目标ViewHolder的position
//            recyclerView.getAdapter().notifyItemMoved(fromPosition, toPosition);//位置变化
            positionTag = fromPosition;
            //数据源位置更换
//            if (fromPosition < toPosition) {
//                for (int i = fromPosition; i < toPosition; i++) {
//                    Collections.swap(list, i, i + 1);
//                }
//            } else {
//                for (int i = fromPosition; i > toPosition; i--) {
//                    Collections.swap(list, i, i - 1);
//                }
//            }
            return false;
        }

        @Override
        public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
            Log.i("WOLF","onSwiped:"+direction);
        }

        @Override
        public boolean isLongPressDragEnabled() {
            moveLayoutManager.setScrollEnabled(false);//禁止滑动
            isLongPress = true;
            onLongPressMoveLisener.onMoveView2(false,false,positionTag);
            return true;
        }
    });

    @Override
    public boolean onTouch(View view, MotionEvent event) {
        if (onLongPressMoveLisener == null) {
            new IllegalArgumentException("请在 RecycleViewLongPressMove 调用 setOnLongPressMoveLisener !!！");
            return true;
        }

//        if(positionTag==4){
//            return true;
//        }

        float xDown = event.getX();
        float yDown = event.getY();
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
        } else if (event.getAction() == MotionEvent.ACTION_UP) {
            if (isLongPress) {
                if (isTouchPointInView(removeView, xDown, yDown)) {
                    onLongPressMoveLisener.onOperation2(positionTag);
                }
                if (isTouchDeleteInView(removeView, xDown, yDown)) {
                    onLongPressMoveLisener.onDelete2(positionTag);
                }
            }
            onLongPressMoveLisener.onNomalView2();
            isLongPress = false;
            moveLayoutManager.setScrollEnabled(true);
        } else if (event.getAction() == MotionEvent.ACTION_MOVE) {
            if (isLongPress) {
                onLongPressMoveLisener.onMoveView2(isTouchPointInView(removeView, xDown, yDown),isTouchDeleteInView(removeView, xDown, yDown),positionTag);
            }
        }
        return false;
    }

    private boolean isTouchPointInView(View view, float x, float y) {
        if (view == null) {
            return false;
        }
        Log.i("WOLF10","Y:"+y);
       if(y> SizeUtils.dp2px(155) &&y<SizeUtils.dp2px(333)){
           return true;
       }else {
           return false;
       }
    }

    private boolean isTouchDeleteInView(View view, float x, float y) {
        if (view == null) {
            return false;
        }
        Log.i("WOLF","y:"+y+"____________:"+(ScreenUtils.getScreenHeight()-SizeUtils.dp2px(200)));
       if(y> (ScreenUtils.getScreenHeight()-SizeUtils.dp2px(400))){
           return true;
       }else {
           return false;
       }
    }

    private OnLongPressMoveLisener onLongPressMoveLisener;

    public void setOnLongPressMoveLisener(OnLongPressMoveLisener onLongPressMoveLisener) {
        this.onLongPressMoveLisener = onLongPressMoveLisener;
    }


    public interface OnLongPressMoveLisener {
        void onNomalView2();//正常
        void onMoveView2(boolean isTouchPointInView,boolean isTouchDeleteInView, int position);//移动
        void onOperation2(int position);//操作
        void onDelete2(int position);//删除
    }

}