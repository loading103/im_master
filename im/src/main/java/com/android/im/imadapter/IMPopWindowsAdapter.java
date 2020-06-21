package com.android.im.imadapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.im.R;
import com.android.nettylibrary.IMSConfig;
import com.android.nettylibrary.greendao.entity.IMGroupBean;
import com.android.nettylibrary.greendao.entity.IMPersonBean;
import com.android.nettylibrary.greendao.greenutils.DaoUtils;
import com.android.nettylibrary.utils.IMPreferenceUtil;

import java.util.ArrayList;
import java.util.List;

public class IMPopWindowsAdapter extends BaseAdapter {
    private Context ctx;
    private LayoutInflater li;
    private List<String> dataList=new ArrayList<>();

    public IMPopWindowsAdapter(Context ctx, List<String> datas) {
        this.ctx = ctx;
        this.li = LayoutInflater.from(ctx);
        this.dataList=datas;
    }

    @Override
    public int getCount() {
        return dataList.size();
    }

    @Override
    public String getItem(int position) {
        return dataList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;

        if (convertView == null) {
            holder = new ViewHolder();
            convertView = li.inflate( R.layout.item_im_spinner,null);
            holder.mlogo= convertView.findViewById(R.id.iv_im_logo);
            holder.mlogoRed= convertView.findViewById(R.id.iv_im_logo_red);
            holder.mlogoName= convertView.findViewById(R.id.tv_im_logo_name);
            holder.mll_im_container=convertView.findViewById(R.id.ll_im_container);
            holder.view=convertView.findViewById(R.id.view);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.mlogoName.setText(getItem(position));
        if(position==0){
            holder.mlogoRed.setVisibility(View.INVISIBLE);
            holder.mlogo.setImageResource(R.mipmap.im_add_group);
            holder.view.setVisibility(View.VISIBLE);
        }else   if(position==1){
            holder.mlogoRed.setVisibility(View.INVISIBLE);
            holder.mlogo.setImageResource(R.mipmap.im_add_person);
            holder.view.setVisibility(View.VISIBLE);
        }else   if(position==2){
            holder.mlogoRed.setVisibility(View.INVISIBLE);
            holder.mlogo.setImageResource(R.mipmap.im_add_scan);
            holder.view.setVisibility(View.GONE);
        }else    if(position==3){
            holder.mlogo.setImageResource(R.mipmap.im_add_readed);
            DaoUtils daoUtils=DaoUtils.getInstance();
            //检查是不是有未读消息
            boolean hasRed=false;
            List<IMGroupBean> imGroupBeans = daoUtils.queryAllGroupData();
            List<IMPersonBean> imPersonBeans = daoUtils.queryAllMessageData();
            for (int i = 0; i < imGroupBeans.size(); i++) {
                String a = IMPreferenceUtil.getPreference_String(imGroupBeans.get(i).getGroupId() + IMSConfig.IM_UNEREAD_CONVERSATION_CHAT, "0");
                if(Integer.parseInt(a)>0){
                    hasRed=true;
                    break;
                }
            }
            if(!hasRed){
                for (int i = 0; i < imPersonBeans.size(); i++) {
                    String b = IMPreferenceUtil.getPreference_String(imPersonBeans.get(i).getMemberId() + IMSConfig.IM_UNEREAD_CONVERSATION_CHAT, "0");
                    if(Integer.parseInt(b)>0){
                        hasRed=true;
                        break;
                    }
                }
            }
            if(hasRed){
                holder.mlogoRed.setVisibility(View.VISIBLE);
            }else {
                holder.mlogoRed.setVisibility(View.INVISIBLE);
            }
        }
        return convertView;
    }

    class ViewHolder {
        ImageView mlogo;
        View mlogoRed;
        TextView mlogoName;
        View view;
        LinearLayout  mll_im_container;
    }
}