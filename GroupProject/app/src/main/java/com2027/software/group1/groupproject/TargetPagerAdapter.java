package com2027.software.group1.groupproject;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

public class TargetPagerAdapter extends FragmentStatePagerAdapter
{

    private static final int NUM_PAGES = 4;

    public TargetPagerAdapter(FragmentManager fm)
    {
        super(fm);
    }


    @Override
    public Fragment getItem(int position) {
        if(position == 0) {

            return new ProgressFragment();
        }
        else if (position == 1) {
            return new CommunityFragment();
        }
        else if (position == 2){
            return new AnalyticsFragment();
        }
        else{
            return new GroupsFragment();
        }
    }


    @Override
    public int getCount() {
        return NUM_PAGES;
    }



}