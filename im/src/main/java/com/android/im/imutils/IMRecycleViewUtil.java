package com.android.im.imutils;

import android.annotation.SuppressLint;
import android.content.Context;

import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;


/**
 * Created by del on 17/1/4.
 * recycle
 */
public class IMRecycleViewUtil {

    @SuppressLint("WrongConstant")
    public static<T extends  RecyclerView.Adapter>  void setRecyclView(Context context, RecyclerView recyclerView, String layout, String orientation,
                                                                       String str, boolean hasFixedSize, T t)
    {
        if("linearlayout".equals(layout))
        {
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
            if("h".equals(orientation)) {
                linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
            }else {
                linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
            }
            recyclerView.setLayoutManager(linearLayoutManager);
            if("simple".equals(str))
            {
//                recyclerView.addItemDecoration(new DeviderDecoration_Simple(context));
            }else {
                if("sticky".equals(str))
                {
//                    recyclerView.addItemDecoration(new StickyRecyclerHeadersDecoration((StickyRecyclerHeadersAdapter) t));
                }
            }
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            recyclerView.setHasFixedSize(hasFixedSize);
        }
        recyclerView.setAdapter(t);

    }
    @SuppressLint("WrongConstant")
    public static<T extends  RecyclerView.Adapter>  void setRecyclView(Context context, RecyclerView recyclerView, String layout, String orientation,
                                                                       RecyclerView.ItemAnimator itemAnimator, boolean hasFixedSize, T t)
    {
        if("linearlayout".equals(layout))
        {
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
            if("h".equals(orientation)) {
                linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
            }else {
                linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
            }
            recyclerView.setLayoutManager(linearLayoutManager);
            recyclerView.setItemAnimator(itemAnimator);
            recyclerView.setHasFixedSize(hasFixedSize);
        }


        recyclerView.setAdapter(t);

    }


    public static<T extends  RecyclerView.Adapter>  void setRecyclView(Context context,RecyclerView recyclerView,RecyclerView.LayoutManager mLayoutManager,
                                                                       RecyclerView.ItemAnimator itemAnimator,boolean hasFixedSize,T t)
    {
            recyclerView.setLayoutManager(mLayoutManager);
            recyclerView.setItemAnimator(itemAnimator);
            recyclerView.setHasFixedSize(hasFixedSize);
        recyclerView.setAdapter(t);
    }

    public static<T extends  RecyclerView.Adapter>  void setStaggeredRecycleView(Context context,RecyclerView recyclerView,int num, T t)
    {
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(num, StaggeredGridLayoutManager.HORIZONTAL));
        recyclerView.setAdapter(t);



    }


    public static<T extends  RecyclerView.Adapter> void setGridView(Context context,RecyclerView recyclerView,int position,
                                                                       T t)
    {
        GridLayoutManager gridLayoutManager = new GridLayoutManager(context, position);
            recyclerView.setLayoutManager(gridLayoutManager);

            recyclerView.setAdapter(t);

    }

    /**
     * 中间有横线
     */
    public static <T extends RecyclerView.Adapter> void setRecyclView(Context context, RecyclerView recyclerView, RecyclerView.ItemDecoration deviderDecoration_simple, T t) {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.addItemDecoration(deviderDecoration_simple);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(t);

    }

    //倒叙排列
    @SuppressLint("WrongConstant")
    public static<T extends  RecyclerView.Adapter>  void setRecyclView2(Context context, RecyclerView recyclerView, String layout, String orientation,
                                                                        RecyclerView.ItemAnimator itemAnimator, boolean hasFixedSize, T t)
    {
        if("linearlayout".equals(layout))
        {
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
            if("h".equals(orientation)) {
                linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
            }else {
                linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
//                linearLayoutManager.setStackFromEnd(true);
                linearLayoutManager.setReverseLayout(true);//列表翻转
            }
            recyclerView.setLayoutManager(linearLayoutManager);
            recyclerView.setItemAnimator(itemAnimator);
            recyclerView.setHasFixedSize(hasFixedSize);
        }


        recyclerView.setAdapter(t);

    }


}
