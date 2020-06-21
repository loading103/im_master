package com.rhby.cailexun.widget.contactview;


import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.nettylibrary.utils.IMDensityUtil;
import com.android.nettylibrary.utils.IMLogUtil;
import com.rhby.cailexun.R;


/**
 * @Description:右侧的sideBar,显示的是二十六个字母以及*，和#号，
 * 点击字母，自动导航到相应拼音的汉字上
 * @author http://blog.csdn.net/finddreams
 */ 
public class SideBar extends View {
	// 触摸事件
	private OnTouchingLetterChangedListener onTouchingLetterChangedListener;
	// 26个字母
	public String b = "ABCDEFGHIJKLMNOPQRSTUVWXYZ#";
	private int choose = -1;// 选中
	private Paint paint = new Paint();
	private TextView mTextDialog;
	private Context context;

	public void setTextView(TextView mTextDialog) {
		this.mTextDialog = mTextDialog;
	}
	public SideBar(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		this.context=context;
	}

	public SideBar(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context=context;
	}

	public SideBar(Context context) {
		super(context);
		this.context=context;
	}

	/**
	 * 重写这个方法
	 */
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		// 获取焦点改变背景颜色.
		int height = getHeight();// 获取对应高度
		int width = getWidth(); // 获取对应宽度
		IMLogUtil.d("MyOwnTag:", "SideBar --width=--" +width+ ";;;SideBar --height=--" +height);

		int singleHeight = height / b.length();// 获取每一个字母的高度

		for (int i = 0; i < b.length(); i++) {
			paint.setColor(getResources().getColor(R.color.color_999999));
			paint.setAntiAlias(true);
			paint.setTextSize(IMDensityUtil.dip2px(context,11));
			// 选中的状态
			if (i == choose) {
				paint.setColor(getResources().getColor(R.color.color_157EFB));
				paint.setFakeBoldText(true);
			}
			// x坐标等于中间-字符串宽度的一半.
			float xPos = width / 2 - paint.measureText(b.charAt(i)+"") / 2;
			float yPos = singleHeight * i + singleHeight;
			canvas.drawText(b.charAt(i)+"", xPos, yPos, paint);
			paint.reset();// 重置画笔
		}

	}

	
	@SuppressLint("NewApi")
	@Override
	public boolean dispatchTouchEvent(MotionEvent event) {
		final int action = event.getAction();
		final float y = event.getY();// 点击y坐标
		final int oldChoose = choose;
		final OnTouchingLetterChangedListener listener = onTouchingLetterChangedListener;
		final int c = (int) (y / getHeight() * b.length());// 点击y坐标所占总高度的比例*b数组的长度就等于点击b中的个数.

		switch (action) {
		case MotionEvent.ACTION_UP:
			setBackgroundDrawable(new ColorDrawable(0x00000000));
//			choose = -1;//
			invalidate();
			if (mTextDialog != null) {
				mTextDialog.setVisibility(View.INVISIBLE);
			}
			break;

		default:
			setBackgroundResource(R.drawable.sidebar_background);
			setAlpha((float) 0.7);
			if (oldChoose != c) {
				if (c >= 0 && c < b.length()) {
					if (listener != null) {
						listener.onTouchingLetterChanged(b.charAt(c)+"");
					}
					if (mTextDialog != null) {
						mTextDialog.setText(b.charAt(c)+"");
						mTextDialog.setVisibility(View.VISIBLE);
					}
					
					choose = c;
					invalidate();
				}
			}

			break;
		}
		return true;
	}
	/**
	 * 这个方法提供给外部调用
	 * @param letter
	 */
	public void changeLetter(char letter) {
		for (int i = 0; i < b.length(); i++) {
			if(letter == b.charAt(i)){
				if(choose != i){
					choose = i;
					invalidate();
				}
				return;
			}
		}
	}
	/**
	 * 向外公开的方法
	 * 
	 * @param onTouchingLetterChangedListener
	 */
	public void setOnTouchingLetterChangedListener(
			OnTouchingLetterChangedListener onTouchingLetterChangedListener) {
		this.onTouchingLetterChangedListener = onTouchingLetterChangedListener;
	}

	/**
	 * 接口
	 * 
	 * @author coder
	 * 
	 */
	public interface OnTouchingLetterChangedListener {
		public void onTouchingLetterChanged(String s);
	}

	public void setLetter(String letters) {
		if(letters==null || letters.length()==0){
			return;
		}
		RelativeLayout.LayoutParams params=(RelativeLayout.LayoutParams)getLayoutParams();
		params.height=getSingleLetterHight(letters.length())*letters.length();
		setLayoutParams(params);
		this.b = letters;
		invalidate();
	}

	public int getSingleLetterHight(int number) {
		int hight = IMDensityUtil.getScreenHeight(context) - IMDensityUtil.dip2px(context, 290);
		int singleHight =0;
		if(number>15){
			singleHight=(int)(hight/27);
		}else {
			singleHight=(int)(hight/20);
		}

		return singleHight;
	}


}