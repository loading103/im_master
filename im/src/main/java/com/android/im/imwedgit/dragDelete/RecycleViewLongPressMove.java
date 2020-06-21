package com.android.im.imwedgit.dragDelete;

import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.android.im.imwedgit.dragDelete.MoveLayoutManager;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ScreenUtils;
import com.blankj.utilcode.util.SizeUtils;

import java.util.Collections;
import java.util.List;

/**
 * Created by Administrator on 2019/12/31.
 * Describe:
 */
public class RecycleViewLongPressMove implements View.OnTouchListener {

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
    public RecycleViewLongPressMove(RecyclerView recyclerView, List<?> list, View removeView) {
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
            LogUtils.i("WOLF12","fromPosition："+fromPosition);
            LogUtils.i("WOLF12","toPosition："+toPosition);
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
            onLongPressMoveLisener.onMoveView(false,false,positionTag);
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
                    onLongPressMoveLisener.onOperation(positionTag);
                }
                if (isDeleteInView(removeView, xDown, yDown)) {
                    onLongPressMoveLisener.onDelete(positionTag);
                }
            }
            onLongPressMoveLisener.onNomalView();
            isLongPress = false;
            moveLayoutManager.setScrollEnabled(true);
        } else if (event.getAction() == MotionEvent.ACTION_MOVE) {
            if (isLongPress) {
                onLongPressMoveLisener.onMoveView(isTouchPointInView(removeView, xDown, yDown),isDeleteInView(removeView, xDown, yDown),positionTag);
            }
        }
        return false;
    }

    private boolean isTouchPointInView(View view, float x, float y) {
        if (view == null) {
            return false;
        }
//        Log.i("WOLF10","X:"+x);
        Log.i("WOLF10","Y:"+y);
       /* int[] location = new int[2];
        view.getLocationOnScreen(location);
        int left = location[0];
        int top = location[1];
        Log.i("WOLF11","left:"+left);
        Log.i("WOLF11","top:"+top);
        int right = left + view.getMeasuredWidth();
        int bottom = top + view.getMeasuredHeight();
        //view.isClickable() &&
        if (y >= top && y <= bottom && x >= left
                && x <= right) {
            return true;
        }
        return false;*/

//       if(y> ScreenUtils.getScreenHeight()-SizeUtils.dp2px(100)){
//
//       }

       if(y> SizeUtils.dp2px(155) &&y<SizeUtils.dp2px(333)){
           return true;
       }else {
           return false;
       }
    }

    private boolean isDeleteInView(View view, float x, float y) {
        if (view == null) {
            return false;
        }
        Log.i("WOLF","y:"+y+"____________:"+(ScreenUtils.getScreenHeight()-SizeUtils.dp2px(200)));
       if(y> (ScreenUtils.getScreenHeight()-SizeUtils.dp2px(235))){
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
        void onNomalView();//正常
        void onMoveView(boolean isTouchPointInView,boolean isTouchDeleteInView,int position);//移动
        void onOperation(int position);//操作
        void onDelete(int position);//删除
    }

}