package com.android.im.imadapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SectionIndexer;
import android.widget.TextView;

import com.android.im.R;
import com.android.im.imutils.IMImageLoadUtil;
import com.android.nettylibrary.greendao.entity.IMGroupMemberBean;
import com.android.nettylibrary.utils.IMLogUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * @Description:用来处理集合中数据的显示与排序
 * @author http://blog.csdn.net/finddreams
 */ 
public class IMCreatContactSortAdapter extends BaseAdapter implements SectionIndexer {
	private List<IMGroupMemberBean> list = null;
	private Context mContext;

	public IMCreatContactSortAdapter(Context mContext, List<IMGroupMemberBean> list) {
		this.mContext = mContext;
		this.list = list;
	}
	
	/**
	 * 当ListView数据发生变化时,调用此方法来更新ListView
	 * @param list
	 */
	public void updateListView(List<IMGroupMemberBean> list){
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
		final IMGroupMemberBean mContent = list.get(position);
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = LayoutInflater.from(mContext).inflate(R.layout.item_contact_list_creat, null);
			holder.groupTv =  convertView.findViewById(R.id.groupTv);
			holder.name =  convertView.findViewById(R.id.tv_studentName);
			holder.phone =  convertView.findViewById(R.id.tv_phone);
			holder.line1 =  convertView.findViewById(R.id.lin1);
			holder.line2 =  convertView.findViewById(R.id.lin2);
			holder.iv_photo =  convertView.findViewById(R.id.iv_photo);
			holder.mcb =  convertView.findViewById(R.id.cb_choosed);

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		IMGroupMemberBean data = list.get(position);

		if(data.getIschoosed()){
			holder.mcb.setChecked(true);
		}else {
			holder.mcb.setChecked(false);
		}
		ViewHolder finalHolder = holder;
		holder.line2.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				boolean ischoosed = list.get(position).getIschoosed();
				if(ischoosed){
					finalHolder.mcb.setChecked(false);
				}else {
					finalHolder.mcb.setChecked(true);
				}
				list.get(position).setIschoosed(!ischoosed);
			}
		});

		//根据position获取分类的首字母的Char ascii值
		int section = getSectionForPosition(position);
		//如果当前位置等于该分类首字母的Char的位置 ，则认为是第一次出现
		if(position == getPositionForSection(section)){
			holder.line1.setVisibility(View.VISIBLE);
			holder.groupTv.setText(data.getLetter()+"");
		}else{
			holder.line1.setVisibility(View.GONE);
		}
		holder.name.setText(data.getNickName());
		if (!TextUtils.isEmpty(data.getAvatar())){
			IMImageLoadUtil.ImageLoadCircle(mContext,data.getAvatar(),holder.iv_photo);
		}
		return convertView;

	}
	final static class ViewHolder {
		TextView groupTv,name,phone;
		LinearLayout line1,line2;
		ImageView iv_photo;
		CheckBox  mcb;
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


	public List<IMGroupMemberBean> getChoosedList() {
		List<IMGroupMemberBean> data=new ArrayList<>();
		for (int i = 0; i < list.size(); i++) {
			if(list.get(i).getIschoosed()){
				data.add(list.get(i));
			}
		}
		return data;
	}

	public  interface  ChooseListener{
		void  setOnChooseListener(int position);
	}
	private ChooseListener onChooseListener;

	public void setOnChooseListener(ChooseListener chooseListener) {
		this.onChooseListener = chooseListener;
	}
}