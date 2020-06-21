package com.android.im.imadapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SectionIndexer;
import android.widget.TextView;

import com.android.im.IMSManager;
import com.android.im.R;
import com.android.im.imeventbus.IMGoToFirstFragmentEvent;
import com.android.im.imui.activity.IMGroupChatActivity;
import com.android.im.imui.activity.IMMemberInforActivity;
import com.android.im.imui.activity.IMTotleGroupActivity;
import com.android.im.imutils.IMImageLoadUtil;
import com.android.im.imutils.IMStopClickFast;
import com.android.nettylibrary.greendao.entity.IMConversationBean;
import com.android.nettylibrary.greendao.entity.IMGroupBean;

import org.greenrobot.eventbus.EventBus;

import java.security.acl.Group;
import java.util.List;

/**
 * @Description:用来处理集合中数据的显示与排序
 * @author http://blog.csdn.net/finddreams
 */
public class IMGroupContactSortAdapter extends BaseAdapter implements SectionIndexer {
	private List<IMGroupBean> list = null;
	private Context mContext;

	public IMGroupContactSortAdapter(Context mContext, List<IMGroupBean> list) {
		this.mContext = mContext;
		this.list = list;
	}

	/**
	 * 当ListView数据发生变化时,调用此方法来更新ListView
	 * @param list
	 */
	public void updateListView(List<IMGroupBean> list){
		this.list = list;
		notifyDataSetChanged();
	}

	public int getCount() {
		return this.list.size();
	}

	public Object getItem(int position) {
		return list.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(final int position, View convertView, ViewGroup arg2) {
		ViewHolder holder = null;
		final IMGroupBean mContent = list.get(position);
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = LayoutInflater.from(mContext).inflate(R.layout.item_contact_list_item, null);
			holder.groupTv =  convertView.findViewById(R.id.groupTv);
			holder.name =  convertView.findViewById(R.id.tv_studentName);
			holder.phone =  convertView.findViewById(R.id.tv_phone);
			holder.line1 =  convertView.findViewById(R.id.lin1);
			holder.line2 =  convertView.findViewById(R.id.lin2);
			holder.iv_photo =  convertView.findViewById(R.id.iv_photo);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		IMGroupBean data = list.get(position);
		//根据position获取分类的首字母的Char ascii值
		int section = getSectionForPosition(position);
		//如果当前位置等于该分类首字母的Char的位置 ，则认为是第一次出现
		if(position == getPositionForSection(section)){
			holder.line1.setVisibility(View.VISIBLE);
			holder.groupTv.setText(data.getLetter()+"");
		}else{
			holder.line1.setVisibility(View.GONE);
		}
		holder.name.setText(data.getGroupName());
		if (!TextUtils.isEmpty(data.getGroupAvatar())){
			IMImageLoadUtil.ImageLoadCircle(mContext,data.getGroupAvatar(),holder.iv_photo);
		}
		holder.line2.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if(IMStopClickFast.isFastClick()) {
					IMConversationBean bean = new IMConversationBean();
					bean.setConversationName(data.getGroupName());
					bean.setGroupName(data.getGroupName());
					bean.setGroupId(data.getGroupId());
					bean.setConversationId(data.getGroupId());
					bean.setConversationavatar(data.getGroupAvatar());
					Intent intent = new Intent(mContext, IMGroupChatActivity.class);
					intent.putExtra("group", bean);
					mContext.startActivity(intent);
					EventBus.getDefault().post(new IMGoToFirstFragmentEvent());
					((IMTotleGroupActivity) mContext).finish();
				}

			}
		});
		return convertView;

	}
	final static class ViewHolder {
		TextView groupTv,name,phone;
		LinearLayout line1,line2;
		ImageView iv_photo;
	}

	/**
	 * 根据ListView的当前位置获取分类的首字母的Char ascii值
	 */
	public int getSectionForPosition(int position) {
		return list.get(position).getLetter().charAt(0);
	}

	/**
	 * 根据分类的首字母的Char ascii值获取其第一次出现该首字母的位置
	 */
	public int getPositionForSection(int section) {
		for (int i = 0; i < getCount(); i++) {
			String sortStr = list.get(i).getLetter();
			char firstChar = sortStr.toUpperCase().charAt(0);
			if (firstChar == section) {
				return i;
			}
		}
		return -1;
	}
	/**
	 * 提取英文的首字母，非英文字母用#代替。
	 *
	 * @param str
	 * @return
	 */
	private String getAlpha(String str) {
		String sortStr = str.trim().substring(0, 1).toUpperCase();
		// 正则表达式，判断首字母是否是英文字母
		if (sortStr.matches("[A-Z]")) {
			return sortStr;
		} else {
			return "#";
		}
	}
	@Override
	public Object[] getSections() {
		return null;
	}
}