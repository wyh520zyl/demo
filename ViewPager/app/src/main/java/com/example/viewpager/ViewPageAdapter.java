package com.example.viewpager;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

public class ViewPageAdapter extends FragmentPagerAdapter {
    private static int PAGE_COUNT;//表示要展示的页面数量
    private ViewPager mViewPager;
    private Context mContext;

    public ViewPageAdapter(Context context,ViewPager viewpager, FragmentManager fm) {
        super(fm);
        this.mViewPager = viewpager;
        this.mContext = context;
        PAGE_COUNT = 3;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return DetailInfoFragment.newInstance(mContext,position,mViewPager);
    }

    @Override
    public int getCount() {
        return PAGE_COUNT;
    }

    @Override
    public CharSequence getPageTitle(int position) {//设置标题
        switch (position) {
            case 0:
                return "聊天";
            case 1:
                return "推荐";
            case 2:
                return "好友";
            default:
                break;

        }
        return null;
    }

}

