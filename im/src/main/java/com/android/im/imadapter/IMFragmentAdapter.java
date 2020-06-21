//package com.android.im.imadapter;
//
//
//
//import androidx.fragment.app.Fragment;
//import androidx.fragment.app.FragmentManager;
//import androidx.fragment.app.FragmentPagerAdapter;
//
//import com.android.im.imui.fragment.IMPersonalFragment;
//import com.android.im.imui.fragment.IMGroupFragment;
//
//public class IMFragmentAdapter extends FragmentPagerAdapter {
//    private IMPersonalFragment fragment1;
//    private IMGroupFragment fragment2;
//    public IMFragmentAdapter(FragmentManager fm) {
//        super(fm);
//        fragment1 = new IMPersonalFragment();
//        fragment2 = new IMGroupFragment();
//    }
//    @Override
//    public Fragment getItem(int position) {
//        Fragment fragment= null;
//        switch (position){
//            case 0:
//                fragment = fragment1;
//                break;
//            case 1:
//                fragment = fragment2;
//                break;
//        }
//        return fragment;
//    }
//
//    @Override
//    public int getCount() {
//        return 2;
//    }
//}