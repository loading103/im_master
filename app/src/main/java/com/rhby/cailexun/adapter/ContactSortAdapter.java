package com.rhby.cailexun.adapter;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SectionIndexer;
import android.widget.TextView;
import android.widget.Toast;

import com.android.im.imeventbus.IMDeleteFriendSucessEvent;
import com.android.im.imnet.IMHttpResultObserver;
import com.android.im.imnet.http.IMHttpsService;
import com.android.im.imui.activity.IMContactDetailActivity;
import com.android.im.imutils.IMImageLoadUtil;
import com.android.im.imutils.IMStopClickFast;
import com.android.im.imview.IMDrawableCenterTextView;
import com.android.im.imview.dialog.IMDialogUtils;
import com.android.nettylibrary.greendao.entity.IMPersonBean;
import com.android.nettylibrary.http.IMPersonBeans;
import com.google.gson.Gson;
import com.mcxtzhang.swipemenulib.SwipeMenuLayout;
import com.rhby.cailexun.R;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import okhttp3.RequestBody;

import static com.android.im.imnet.IMBaseConstant.JSON;

/**
 * @Description:用来处理集合中数据的显示与排序
 * @author http://blog.csdn.net/finddreams
 */
public class ContactSortAdapter extends BaseAdapter implements SectionIndexer {
	private List<IMPersonBean> list = null;
	private Context mContext;

	public ContactSortAdapter(Context mContext, List<IMPersonBean> list) {
		this.mContext = mContext;
		this.list = list;
	}

	/**
	 * 当ListView数据发生变化时,调用此方法来更新ListView
	 * @param list
	 */
	public void updateListView(List<IMPersonBean> list){
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
		final IMPersonBean mContent = list.get(position);
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = LayoutInflater.from(mContext).inflate(R.layout.contact_list_item, null);
			holder.groupTv =  convertView.findViewById(R.id.groupTv);
			holder.name =  convertView.findViewById(R.id.tv_studentName);
			holder.phone =  convertView.findViewById(R.id.tv_phone);
			holder.line1 =  convertView.findViewById(R.id.lin1);
			holder.line2 =  convertView.findViewById(R.id.lin2);
			holder.iv_photo =  convertView.findViewById(R.id.iv_photo);
			holder.mTvDelete =  convertView.findViewById(R.id.btnDelete);
			holder.ll_im_container=  convertView.findViewById(R.id.ll_im_container);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		IMPersonBean data = list.get(position);
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
		holder.phone.setText(data.getMobile());
		IMImageLoadUtil.ImageLoadCircle(mContext,data.getAvatar(),holder.iv_photo);
		ViewHolder finalHolder = holder;
		holder.mTvDelete.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				deleteFriendDialog(data,position, finalHolder.ll_im_container);
			}
		});
		holder.line2.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if(IMStopClickFast.isFastClick()) {
					Intent intent = new Intent(mContext, IMContactDetailActivity.class);
					intent.putExtra("bean", data);
					mContext.startActivity(intent);
				}
			}
		});
		return convertView;
	}



	final static class ViewHolder {
		TextView groupTv,name,phone;
		LinearLayout line1,line2;
		ImageView iv_photo;
		IMDrawableCenterTextView mTvDelete;
		SwipeMenuLayout ll_im_container;
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

	private void deleteFriendDialog(IMPersonBean data,int position,SwipeMenuLayout container) {
		IMDialogUtils.getInstance().showCommonDialog(mContext,"删除好友将清空好友信息和聊天记录，是否确认删除好友？", new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				IMDialogUtils.getInstance().dismissCommonDiglog();
				container.smoothClose();
				deleteFriend(data,position);
			}
		});
	}

	/**
	 * 删除好友
	 */
	private void deleteFriend(IMPersonBean data,int position) {
		IMPersonBeans bean = new IMPersonBeans();
		List<String> list=new ArrayList<>();
		if(data==null || TextUtils.isEmpty(data.getCustomerId())){
			Toast.makeText(mContext,"删除失败，请稍后再试", Toast.LENGTH_SHORT).show();
			return;
		}
		list.add(data.getCustomerId());
		bean.setIds(list);
		String json = new Gson().toJson(bean);
		RequestBody body = RequestBody.create(JSON, json);
		Log.e("----",json);
		IMHttpsService.deleteFriendJson(body,new IMHttpResultObserver<String>() {
			@Override
			public void onSuccess(String datas, String message) {
				if(message.contains("成功")){
					Toast.makeText(mContext,"删除成功", Toast.LENGTH_SHORT).show();
					EventBus.getDefault().post(new IMDeleteFriendSucessEvent(data.getCustomerId()));
				}
			}
			@Override
			public void _onError(Throwable e) {
				Toast.makeText(mContext,e.getMessage(), Toast.LENGTH_SHORT).show();
			}

			@Override
			public void _onErrorLocal(Throwable e, String message, String code) {
				Toast.makeText(mContext,message, Toast.LENGTH_SHORT).show();
			}
		});
	}
}