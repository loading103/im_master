package com.android.im.imview;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.android.im.R;
import com.android.im.imadapter.IMPopWindowAdapter;
import com.android.im.imadapter.IMPopWindowsAdapter;
import com.android.im.imview.impopwindow.IMCustomPopWindow;
import com.android.im.imview.impopwindow.IMPopWindow;
import com.android.im.imview.impromptlibrary.IMPromptButton;
import com.android.im.imview.impromptlibrary.IMPromptButtonListener;
import com.android.im.imview.impromptlibrary.IMPromptDialog;
import com.android.nettylibrary.IMSConfig;
import com.android.nettylibrary.greendao.entity.IMGroupBean;
import com.android.nettylibrary.greendao.entity.IMPersonBean;
import com.android.nettylibrary.greendao.greenutils.DaoUtils;
import com.android.nettylibrary.utils.IMDensityUtil;
import com.android.nettylibrary.utils.IMPreferenceUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 对面列表弹消息哪里
 */
public class IMPopWindowUtils {

    public Context context;
    public IMPopWindowsAdapter adapter;
    public  static IMPopWindowUtils instance;
    public IMPopWindowUtils(Context context) {
        this.context=context;
    }

    public static IMPopWindowUtils getInstance(Context context) {
        if(instance==null){
            instance=new IMPopWindowUtils(context);
        }
        return instance;
    }

    /**
     * 全部已读，消息设置
     */
    public void showPopListView( View view){
        List<String> datas=new ArrayList<>();
        datas.add("创建群组");
        datas.add("添加好友");
        datas.add("扫一扫");
        View contentView = LayoutInflater.from(context).inflate(R.layout.layout_im_popwindow_list,null);
        ListView listView =  contentView.findViewById(R.id.im_listview);
        adapter = new IMPopWindowsAdapter(context,datas);
        listView.setAdapter(adapter);
        //创建并显示popWindow
        final IMPopWindow mListPopWindow= new IMPopWindow.PopupWindowBuilder(context)
                .setView(contentView)
                .size(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT)//显示大小
                .setOutsideTouchable(true)
                .setOnDissmissListener(new PopupWindow.OnDismissListener() {
                    @Override
                    public void onDismiss() {
                        if(listener!=null){
                            listener.OnListener(3);

                        }
                    }
                })
                .setTouchable(true)
                .setAnimationStyle(R.style.CustomPopWindowStyle)
                .create()
                .showAsDropDown(view,0, IMDensityUtil.dip2px(context,5));

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(listener!=null){
                    listener.OnListener(position);
                }
                mListPopWindow.dissmiss();
            }
        });
    }


    public   interface OnClickListener{
        void OnListener(int position);
    }
    private OnClickListener listener;

    public void setOnListener(OnClickListener listener) {
        this.listener = listener;
    }

}
