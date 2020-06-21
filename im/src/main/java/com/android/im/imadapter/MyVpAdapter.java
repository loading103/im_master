package com.android.im.imadapter;

import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.im.DaoManager;
import com.android.im.R;
import com.android.im.imbean.DaoSession;
import com.android.im.imbean.SmallProgramBean;
import com.android.im.imbean.SmallProgramBeanDao;
import com.android.im.imeventbus.UpdateLatestEvent;
import com.android.im.imui.activity.IMQRCActivity;
import com.android.im.imui.activity.SmallProgramActivity;
import com.blankj.utilcode.util.LogUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.chad.library.adapter.base.listener.OnItemDragListener;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

/**
 * Created by Wolf on 2019/12/19.
 * Describe:我的收藏-VP
 */
public class MyVpAdapter extends BaseQuickAdapter< List<SmallProgramBean>, BaseViewHolder> {
    public MyVpAdapter() {
        super(R.layout.item_vp_rv);
    }


    @Override
    protected void convert(BaseViewHolder helper, List<SmallProgramBean> item) {
        RecyclerView recyclerView = helper.getView(R.id.recyclerView);

        GridLayoutManager layoutManager=new GridLayoutManager(getContext(),5);
        recyclerView.setLayoutManager(layoutManager);
        MyCollectionAdapter myCollectionAdapter=new MyCollectionAdapter();
        myCollectionAdapter.getDraggableModule().setDragEnabled(true);
        myCollectionAdapter.getDraggableModule().setOnItemDragListener(new OnItemDragListener() {
            @Override
            public void onItemDragStart(RecyclerView.ViewHolder viewHolder, int pos) {

            }

            @Override
            public void onItemDragMoving(RecyclerView.ViewHolder source, int from, RecyclerView.ViewHolder target, int to) {

            }

            @Override
            public void onItemDragEnd(RecyclerView.ViewHolder viewHolder, int pos) {

            }
        });
        myCollectionAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                SmallProgramBean bean=myCollectionAdapter.getData().get(position);
                bean.setIsConllection("Y");
//                Intent intent=new Intent(getContext(), SmallProgramActivity.class);
//                intent.putExtra("data",bean);
//                getContext().startActivity(intent);
                  new SmallProgramActivity().toActivity(getContext(),bean);
                //添加进数据库
//                SmallProgramBeanDao smallProgramBeanDao = DaoManager.getInstance().getDaoSession().getSmallProgramBeanDao();
//                SmallProgramBean smallProgramBean=smallProgramBeanDao.queryBuilder().where(SmallProgramBeanDao.Properties.ProgramId.eq(item.get(position).getProgramId())).unique();
//                if(smallProgramBean!=null){
//                    smallProgramBeanDao.delete(smallProgramBean);
//                    smallProgramBeanDao.insert(item.get(position));
//                }else {
//                    smallProgramBeanDao.insert(item.get(position));
//                }
//                EventBus.getDefault().post(new UpdateLatestEvent());
            }
        });
        ((ViewGroup) recyclerView.getParent()).setClipChildren(false);
        recyclerView.setAdapter(myCollectionAdapter);
        myCollectionAdapter.setNewData(item);
    }
}
