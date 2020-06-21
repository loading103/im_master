package com.android.im.imview.impopwindow;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.android.im.IMSManager;
import com.android.im.R;
import com.android.im.imadapter.IMPopWindowAdapter;
import com.android.im.imview.impromptlibrary.IMPromptButton;
import com.android.im.imview.impromptlibrary.IMPromptButtonListener;
import com.android.im.imview.impromptlibrary.IMPromptDialog;
import com.android.nettylibrary.IMSConfig;
import com.android.nettylibrary.greendao.entity.IMConversationBean;
import com.android.nettylibrary.greendao.greenutils.DaoUtils;
import com.android.nettylibrary.utils.IMDensityUtil;
import com.android.nettylibrary.utils.IMPreferenceUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 对面列表弹消息哪里
 */
public class IMPopWindowUtil {

    public Context context;
    public IMPopWindowAdapter adapter;
    public  static IMPopWindowUtil instance;
    public IMPopWindowUtil(Context context) {
        this.context=context;
    }

    public static IMPopWindowUtil getInstance(Context context) {
        if(instance==null){
            instance=new IMPopWindowUtil(context);
        }
        return instance;
    }

    /**
     * 全部已读，消息设置
     * @return
     */
    public IMCustomPopWindow showPopListView(View view){
        List<String> datas=new ArrayList<>();
        datas.add("创建群组");
        datas.add("添加好友");
        datas.add("扫一扫");
        datas.add("全部已读");
        View contentView = LayoutInflater.from(context).inflate(R.layout.layout_im_popwindow_list,null);
        ListView listView =  contentView.findViewById(R.id.im_listview);
        adapter = new IMPopWindowAdapter(context,datas);
        listView.setAdapter(adapter);
        //创建并显示popWindow
        final IMCustomPopWindow mListPopWindow= new IMCustomPopWindow.PopupWindowBuilder(context)
                .setView(contentView)
                .size(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT)//显示大小
                .setOutsideTouchable(true)
                .setOnDissmissListener(new PopupWindow.OnDismissListener() {
                    @Override
                    public void onDismiss() {
                        if(listener!=null){
                            listener.OnViewItemListener(4);

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
                    listener.OnViewItemListener(position);
                }
                mListPopWindow.dissmiss();
            }
        });
        return mListPopWindow;
    }
    public IMCustomPopWindow showPopListViews(View view){
        List<String> datas=new ArrayList<>();
        datas.add("创建群组");
        datas.add("添加好友");
        datas.add("扫一扫");
        datas.add("全部已读");
        View contentView = LayoutInflater.from(context).inflate(R.layout.layout_im_popwindow_list,null);
        ListView listView =  contentView.findViewById(R.id.im_listview);
        adapter = new IMPopWindowAdapter(context,datas);
        listView.setAdapter(adapter);
        //创建并显示popWindow
        final IMCustomPopWindow mListPopWindow= new IMCustomPopWindow.PopupWindowBuilder(context)
                .setView(contentView)
                .size(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT)//显示大小
                .setOutsideTouchable(true)
                .setOnDissmissListener(new PopupWindow.OnDismissListener() {
                    @Override
                    public void onDismiss() {
                        if(listeners!=null){
                            listeners.OnViewItemListeners(4);

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
                if(listeners!=null){
                    listeners.OnViewItemListeners(position);
                }
                mListPopWindow.dissmiss();
            }
        });
        return mListPopWindow;
    }
    /**
     * 全部已读，消息设置
     */
    public void showTextLongClickPopWindow(View view, final String text){
        View contentView = LayoutInflater.from(context).inflate(R.layout.layout_im_popwindow_longclick,null);
        TextView mTvCopy =  contentView.findViewById(R.id.im_tv_copy);
        TextView mTvDelete =  contentView.findViewById(R.id.im_tv_delete);
        //创建并显示popWindow
        final IMCustomPopWindow mListPopWindow= new IMCustomPopWindow.PopupWindowBuilder(context)
                .setView(contentView)
                .size(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT)//显示大小
                .setOutsideTouchable(true)
                .setTouchable(true)
                .setAnimationStyle(R.style.CustomPopWindowStyle)
                .create()
                .showAsDropDown(view,0, IMDensityUtil.dip2px(context,10));
        mTvCopy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                copyText(text);
                mListPopWindow.dissmiss();
            }
        });
        mTvDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListPopWindow.dissmiss();
            }
        });
    }



    /**
     *删除好友
     */
    public void deleteFriendView(final Activity context, IMPromptButtonListener listener1){
        //创建对象
        IMPromptDialog IMPromptDialog = new IMPromptDialog(context);
        //设置自定义属性
        IMPromptDialog.getDefaultBuilder().touchAble(true).round(3).loadingDuration(3000);
        IMPromptButton cancle = new IMPromptButton("取消", null);
        cancle.setTextColor(context.getResources().getColor(R.color.color_449CFF));
        IMPromptButton delete = new IMPromptButton("删除好友", listener1);
        delete.setTextColor(context.getResources().getColor(R.color.red6));
        //设置显示的文字大小及颜色
//       IMPromptDialog.getAlertDefaultBuilder().textSize(12).textColor(Color.GRAY);
        //默认两个按钮为Alert对话框，大于三个按钮的为底部SHeet形式展现
        IMPromptDialog.showAlertSheet("", true, cancle,
                delete);
    }

    /**
     * 未读消息全部置为已读
     */
    public void setTotleRed(){
        DaoUtils daoUtils=DaoUtils.getInstance();
        List<IMConversationBean> beans = daoUtils.queryAllConversationData();
        if(beans!=null){
            for (int i = 0; i < beans.size(); i++) {
                IMPreferenceUtil.setPreference_String(beans.get(i).getConversationId() + IMSConfig.IM_UNEREAD_CONVERSATION_CHAT, "0");
            }
        }
        IMSManager.getInstance().getUnreadMessageNumber();
        adapter.notifyDataSetChanged();
    }


    public   interface OnViewItemClickListener{
        void OnViewItemListener(int position);
    }
    private OnViewItemClickListener listener;

    public void setOnViewItemListener(OnViewItemClickListener listener) {
        this.listener = listener;
    }
    public   interface OnViewItemClickListeners{
        void OnViewItemListeners(int position);
    }
    private OnViewItemClickListeners listeners;

    public void setOnViewItemListeners(OnViewItemClickListeners listeners) {
        this.listeners = listeners;
    }
    /**
     * 复制文字
     */
    private void copyText(String copiedText) {
        ClipboardManager clipboardManager = (ClipboardManager)context.getSystemService(Context.CLIPBOARD_SERVICE);
        clipboardManager.setPrimaryClip(ClipData.newPlainText(null, copiedText));
    }


}
