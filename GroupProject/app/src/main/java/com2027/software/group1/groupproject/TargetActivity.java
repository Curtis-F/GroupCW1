package com2027.software.group1.groupproject;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TabHost;

public class TargetActivity extends AppCompatActivity {


    private ViewPager mPager;

    private PagerAdapter mPagerAdapter;

    private TabHost mTabHost;

    public TabHost getTabHost()
    {
        return this.mTabHost;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_target);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        setupPages();
    }


    @Override
    public void onBackPressed() {
        if(mPager.getCurrentItem() == 0)
        {
            super.onBackPressed();
        }
        else
        {
            mPager.setCurrentItem(mPager.getCurrentItem() - 1);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.logout) {
            //Logout
        }
        else if (id == R.id.view_profile) {
            //Open Profile Activity
            Intent intent = new Intent(TargetActivity.this, ProfileActivity.class);
            //TODO: Provide extras to parse user data
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

    private void setupPages()
    {
        this.mPager = (ViewPager) findViewById(R.id.pager);

        this.mTabHost = (TabHost)findViewById(R.id.tabhost);
        this.mTabHost.setup();

        TabHost.TabSpec tabSpec = this.mTabHost.newTabSpec("Progress");
        tabSpec.setContent(R.id.tab_my_activities);
        tabSpec.setIndicator("Progress");
        this.mTabHost.addTab(tabSpec);

        tabSpec = this.mTabHost.newTabSpec("Analytics");
        tabSpec.setContent(R.id.tab_community_targets);
        tabSpec.setIndicator("Analytics");
        this.mTabHost.addTab(tabSpec);

        tabSpec = this.mTabHost.newTabSpec("Groups");
        tabSpec.setContent(R.id.tab_groups);
        tabSpec.setIndicator("Groups");
        this.mTabHost.addTab(tabSpec);

        this.mTabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            @Override
            public void onTabChanged(String s) {
                if(s.equals("Progress"))
                {
                    mPager.setCurrentItem(0, true);
                }
                else if(s.equals("Analytics"))
                {
                    mPager.setCurrentItem(1, true);
                }
                else if(s.equals("Groups"))
                {
                    mPager.setCurrentItem(2, true);
                }

            }
        });


        this.mPagerAdapter = new TargetPagerAdapter(getSupportFragmentManager());
        this.mPager.setAdapter(mPagerAdapter);



        this.mPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                getTabHost().setCurrentTab(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

}
