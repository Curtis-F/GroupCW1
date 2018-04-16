package com2027.software.group1.groupproject;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TabHost;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import java.util.ArrayList;
import java.util.Locale;

public class TargetActivity extends AppCompatActivity {


    private ViewPager mPager;

    private PagerAdapter mPagerAdapter;

    private TabHost mTabHost;

    private TargetItem targetItem = null;

    private ArrayList<LogItem> logs = null;

    private ArrayList<GroupItem> groups = null;

    private LogsListAdapter logsListAdapter = null;

    private GroupsListAdapter groupsListAdapter = null;

    private ProgressFragment progressFragment = null;

    public void setProgressFragment(ProgressFragment progressFragment)
    {
        this.progressFragment = progressFragment;
    }

    public ArrayList<LogItem> getLogs()
    {
        return this.logs;
    }

    public LogsListAdapter getLogsListAdapter()
    {
        return this.logsListAdapter;
    }
    public ArrayList<GroupItem> getGroups()
    {
        return this.groups;
    }

    public GroupsListAdapter getGroupsListAdapter()
    {
        return this.groupsListAdapter;
    }

    public TabHost getTabHost()
    {
        return this.mTabHost;
    }

    public TargetItem getTarget() { return this.targetItem; }

    private DatabaseReference mDatabase = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_target);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent intent = getIntent();
        this.targetItem = (TargetItem) intent.getSerializableExtra("target");

        setupPages();

        this.logs = new ArrayList<>();
        logsListAdapter = new LogsListAdapter(this, this.logs);

        this.groups = new ArrayList<>();
        groupsListAdapter = new GroupsListAdapter(this, this.groups);

        mDatabase = FirebaseDatabase.getInstance().getReference();
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();
        Query logsQuery = mDatabase.child("users").child(user.getUid()).child("Targets").child(targetItem.getKey()).child("Logs");
        logsQuery.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                MyDateTime myDateTime = new MyDateTime(Integer.parseInt(dataSnapshot.child("time").child("day").getValue().toString()),
                        Integer.parseInt(dataSnapshot.child("time").child("month").getValue().toString()),
                        Integer.parseInt(dataSnapshot.child("time").child("year").getValue().toString()),
                        Long.parseLong(dataSnapshot.child("time").child("timestamp").getValue().toString()));



                LogItem logItem = new LogItem(dataSnapshot.getKey(), dataSnapshot.child("activity_key").getValue().toString(), Integer.parseInt(dataSnapshot.child("value").getValue().toString()), myDateTime);
                handleGetLogs(logItem);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                boolean searching = true;
                int index = 0;
                while(searching)
                {
                    if(logs.get(index).getKey().equals(dataSnapshot.getKey().toString()))
                    {
                        logs.remove(index);
                        logsListAdapter.notifyDataSetChanged();
                        progressFragment.UpdateProgress(logs);
                        searching = false;
                    }
                    else
                    {
                        ++index;
                        if(index == logs.size())
                        {
                            searching = false;
                        }
                    }
                }
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        Query usersQuery = mDatabase.child("users").child(user.getUid()).child("Targets").child(targetItem.getKey().toString()).child("groupKeys");
        usersQuery.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Query groupsQuery = mDatabase.child("groups").orderByKey().equalTo(dataSnapshot.getValue().toString());//.equalTo(dataSnapshot.getValue().toString());
                groupsQuery.addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                        ArrayList<String> userKeys = new ArrayList<String>();
                        Iterable<DataSnapshot> list = dataSnapshot.child("userKeys").getChildren();
                        for (DataSnapshot snap :list)
                        {
                            userKeys.add(snap.getValue().toString());
                        }
                        GroupItem groupItem = new GroupItem(dataSnapshot.getKey(), dataSnapshot.child("name").getValue().toString(), dataSnapshot.child("activity_key").getValue().toString(), dataSnapshot.child("activity_name").getValue().toString(), dataSnapshot.child("unit").getValue().toString(), dataSnapshot.child("genre").getValue().toString(), Integer.parseInt(dataSnapshot.child("baseTarget").getValue().toString()), Integer.parseInt(dataSnapshot.child("stretchTarget").getValue().toString()), userKeys);
                        handleGetGroups(groupItem);
                    }

                    @Override
                    public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                    }

                    @Override
                    public void onChildRemoved(DataSnapshot dataSnapshot) {
                        boolean searching = true;
                        int i = 0;
                        if(groups.size() != 0) {
                            while (searching) {
                                if (groups.get(i).getKey().equals(dataSnapshot.getKey())) {
                                    groups.remove(i);
                                    groupsListAdapter.notifyDataSetChanged();
                                    searching = false;
                                } else {
                                    ++i;
                                    if (i >= groups.size()) {
                                        searching = false;
                                    }
                                }
                            }
                        }
                    }

                    @Override
                    public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                int index = 0;
                boolean searching = true;
                if(groups.size() != 0) {
                    while (searching) {
                        if (groups.get(index).getKey().equals(dataSnapshot.getValue().toString())) {
                            groups.remove(index);
                            groupsListAdapter.notifyDataSetChanged();
                            searching = false;
                        } else {
                            ++index;
                            if (index >= groups.size()) {
                                searching = false;
                            }
                        }
                    }
                }
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

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
        tabSpec.setContent(R.id.tab_progress);
        tabSpec.setIndicator("Progress");
        this.mTabHost.addTab(tabSpec);

        tabSpec = this.mTabHost.newTabSpec("Community");
        tabSpec.setContent(R.id.tab_community);
        tabSpec.setIndicator("Community");
        this.mTabHost.addTab(tabSpec);

        tabSpec = this.mTabHost.newTabSpec("Analytics");
        tabSpec.setContent(R.id.tab_analytics);
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
                    if(progressFragment != null) {
                        progressFragment.UpdateProgress(logs);
                    }
                }
                else if(s.equals("Community"))
                {
                    mPager.setCurrentItem(1, true);
                }
                else if(s.equals("Analytics"))
                {
                    mPager.setCurrentItem(2, true);
                }
                else if(s.equals("Groups"))
                {
                    mPager.setCurrentItem(3, true);
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

    public void handleGetLogs(LogItem log)
    {
        if(log != null && this.logs != null && progressFragment != null ) {
            this.logs.add(log);
            logsListAdapter.notifyDataSetChanged();

            progressFragment.UpdateProgress(this.logs);
        }
    }

    public void handleGetGroups(GroupItem group)
    {
        if(group != null && groups != null) {
            this.groups.add(group);
            groupsListAdapter.notifyDataSetChanged();
        }
    }

}
