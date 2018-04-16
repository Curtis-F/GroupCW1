package com2027.software.group1.groupproject;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TabHost;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.ArrayList;

public class GroupsActivity extends AppCompatActivity {

    private ViewPager mPager;

    private PagerAdapter mPagerAdapter;

    private TabHost mTabHost;

    public TabHost getTabHost()
    {
        return this.mTabHost;
    }

    private GroupItem groupItem = null;

    public GroupItem getGroup() { return this.groupItem; }

    private ArrayList<LogItem> logs = null;

    private LogsListAdapter logsListAdapter = null;

    public LogsListAdapter getLogsListAdapter()
    {
        return this.logsListAdapter;
    }

    private ArrayList<MemberItem> members = null;

    private MembersListAdapter membersListAdapter = null;

    public MembersListAdapter getMembersListAdapter()
    {
        return this.membersListAdapter;
    }

    private GroupProgressFragment groupProgressFragment = null;

    public void setGroupProgressFragment(GroupProgressFragment groupProgressFragment)
    {
        this.groupProgressFragment = groupProgressFragment;
    }
    private DatabaseReference mDatabase = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_groups);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent intent = getIntent();
        this.groupItem = (GroupItem) intent.getSerializableExtra("group");
        setupPages();

        mDatabase = FirebaseDatabase.getInstance().getReference();

        setupData();
    }

    private void setupData()
    {
        this.logs = new ArrayList<>();
        logsListAdapter = new LogsListAdapter(this, this.logs);

        this.members = new ArrayList<>();
        membersListAdapter = new MembersListAdapter(this, this.members);

        Query userKeysQuery = mDatabase.child("groups").child(groupItem.getKey()).child("userKeys");
        userKeysQuery.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                String userKey = dataSnapshot.getValue().toString();
                Query logsQuery = mDatabase.child("users").child(userKey).child("Targets").orderByChild("activity_key").equalTo(groupItem.getActivity_key());
                logsQuery.addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                        Iterable<DataSnapshot> data = dataSnapshot.child("Logs").getChildren();
                        for (DataSnapshot snap : data) {
                            MyDateTime myDateTime = new MyDateTime(Integer.parseInt(snap.child("time").child("day").getValue().toString()),
                                    Integer.parseInt(snap.child("time").child("month").getValue().toString()),
                                    Integer.parseInt(snap.child("time").child("year").getValue().toString()),
                                    Long.parseLong(snap.child("time").child("timestamp").getValue().toString()));



                            LogItem logItem = new LogItem(snap.getKey(), snap.child("activity_key").getValue().toString(), Integer.parseInt(snap.child("value").getValue().toString()), myDateTime);
                            handleGetLogs(logItem);
                        }

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
                                groupProgressFragment.UpdateProgress(logs);
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


                Query usersQuery = mDatabase.child("users").orderByKey().equalTo(userKey);
                usersQuery.addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                        MemberItem memberItem = new MemberItem(dataSnapshot.getKey(), dataSnapshot.child("username").getValue().toString(), dataSnapshot.child("email").getValue().toString());
                        handleGetMembers(memberItem);
                    }

                    @Override
                    public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                    }

                    @Override
                    public void onChildRemoved(DataSnapshot dataSnapshot) {

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
                boolean searching = true;
                int index = 0;
                while(searching) {
                    if (members.get(index).getKey().equals(dataSnapshot.getValue().toString())) {
                        members.remove(index);
                        membersListAdapter.notifyDataSetChanged();
                        setupData();
                        searching = false;
                    } else {
                        ++index;
                        if (index == members.size()) {
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
            Intent intent = new Intent(GroupsActivity.this, ProfileActivity.class);
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

        tabSpec = this.mTabHost.newTabSpec("Chat");
        tabSpec.setContent(R.id.tab_chat);
        tabSpec.setIndicator("Chat");
        this.mTabHost.addTab(tabSpec);

        tabSpec = this.mTabHost.newTabSpec("Members");
        tabSpec.setContent(R.id.tab_members);
        tabSpec.setIndicator("Members");
        this.mTabHost.addTab(tabSpec);

        this.mTabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            @Override
            public void onTabChanged(String s) {
                if(s.equals("Progress"))
                {
                    mPager.setCurrentItem(0, true);
                    if(groupProgressFragment != null) {
                        groupProgressFragment.UpdateProgress(logs);
                    }
                }
                else if(s.equals("Chat"))
                {
                    mPager.setCurrentItem(1, true);
                }
                else if(s.equals("Members"))
                {
                    mPager.setCurrentItem(2, true);
                }

            }
        });


        this.mPagerAdapter = new GroupPagerAdapter(getSupportFragmentManager());
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
        if(log != null && this.logs != null && groupProgressFragment != null ) {
            this.logs.add(log);
            logsListAdapter.notifyDataSetChanged();

            groupProgressFragment.UpdateProgress(this.logs);
        }
    }
    public void handleGetMembers(MemberItem user)
    {
        if(user != null && user != null) {
            this.members.add(user);
            membersListAdapter.notifyDataSetChanged();
        }
    }

}
