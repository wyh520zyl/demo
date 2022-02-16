package com.example.viewpager;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

public class ViewPageAdapter1 extends FragmentPagerAdapter {
    private static int PAGE_COUNT;//表示要展示的页面数量
    private ViewPager mViewPager;
    private Context mContext;

    public ViewPageAdapter1(Context context, ViewPager viewpager, FragmentManager fm) {
        super(fm);
        this.mViewPager = viewpager;
        this.mContext = context;
        PAGE_COUNT = 2;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return DetailInfoFragment1.newInstance(mContext,position,mViewPager);
    }

    @Override
    public int getCount() {
        return PAGE_COUNT;
    }


}

