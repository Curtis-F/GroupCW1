package com2027.software.group1.groupproject;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

public class GroupPagerAdapter extends FragmentStatePagerAdapter
{

    private static final int NUM_PAGES = 3;

    public GroupPagerAdapter(FragmentManager fm)
    {
        super(fm);
    }


    @Override
    public Fragment getItem(int position) {
        if(position == 0) {

            return new GroupProgressFragment();
        }
        else if (position == 1) {
            return new ChatFragment();
        }
        else {
            return new MembersFragment();
        }
    }


    @Override
    public int getCount() {
        return NUM_PAGES;
    }



}