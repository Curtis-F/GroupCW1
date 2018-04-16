package com2027.software.group1.groupproject;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

public class HomePagerAdapter extends FragmentStatePagerAdapter
{

    private static final int NUM_PAGES = 3;

    public HomePagerAdapter(FragmentManager fm)
    {
        super(fm);
    }


    @Override
    public Fragment getItem(int position) {
        if(position == 0) {

            return new MyTargetsFragment();
        }
        else if (position == 1) {
            return new CommunityActivitiesFragment();
        }
        else {
            return new GroupsFragment();
        }
    }


    @Override
    public int getCount() {
        return NUM_PAGES;
    }



}