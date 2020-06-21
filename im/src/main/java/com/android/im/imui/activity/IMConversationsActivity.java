package com.android.im.imui.activity;


import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckedTextView;
import android.widget.LinearLayout;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.android.im.R;
import com.android.im.imadapter.IMMyViewPagerAdapter;
import com.android.im.imutils.IMStatusBarUtil;
import com.android.im.imview.IMViewPagerSlide;

import java.util.ArrayList;
import java.util.List;

/**
 * @author jinxin
 * 剑之所指，心之所向
 * @date 2019/8/4
 *  *聊天列表界面（放在fragment里）
 */
public class IMConversationsActivity extends IMBaseActivity implements ViewPager.OnPageChangeListener, OnClickListener {
	private IMViewPagerSlide mViewPager;
	private List<Fragment> mFragments = new ArrayList<>();
	;
	private IMMyViewPagerAdapter mAdapter;

	private CheckedTextView[] mCheckedTextViews;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_im_main);
		IMStatusBarUtil.setTranslucentForImageViewInFragment(this, 0, null);
		mViewPager = findViewById(R.id.viewpager);
		mViewPager.setCanScrollble(true);


//		mFragments.add(new IMCommonListFragment());
//		mFragments.add(new IMGroupFragment());
//		mFragments.add(new IMFindFragment());
//		mFragments.add(new IMFindFragment());
//		mFragments.add(new IMFindFragment());
		mAdapter = new IMMyViewPagerAdapter(getSupportFragmentManager(), mFragments);
		mViewPager.setAdapter(mAdapter);
		mViewPager.setOnPageChangeListener(this);
		mViewPager.setOffscreenPageLimit(3);
		initTabs();
	}

	private void initTabs() {
		LinearLayout layout = (LinearLayout) findViewById(R.id.tabs);
		mCheckedTextViews = new CheckedTextView[layout.getChildCount()];
		for (int i = 0; i < layout.getChildCount(); i++) {
			mCheckedTextViews[i] = (CheckedTextView) layout.getChildAt(i);
			mCheckedTextViews[i].setOnClickListener(this);
		}
	}

	@Override
	public void onPageScrollStateChanged(int arg0) {

	}

	@Override
	public void onPageScrolled(int position, float offset, int offsetPixels) {

	}

	@Override
	public void onPageSelected(int position) {
		if (position == mLastIndex) {
			return;
		}
		mCheckedTextViews[position].setChecked(true);
		mCheckedTextViews[mLastIndex].setChecked(false);
		mLastIndex = position;
	}

	private int mLastIndex;

	@Override
	public void onClick(View v) {
		for (int i = 0; i < mCheckedTextViews.length; i++) {
			if (v == mCheckedTextViews[i]) {
				if (i == mLastIndex) {
					break;
				}
				mCheckedTextViews[i].setChecked(true);
				mCheckedTextViews[mLastIndex].setChecked(false);
				mLastIndex = i;
				mViewPager.setCurrentItem(i, false);
				break;
			}
		}
	}
}