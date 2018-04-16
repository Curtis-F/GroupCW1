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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class HomeActivity extends AppCompatActivity {

    private ArrayList<ActivityItem> communityActivities = null;

    private CommunityActivitiesListAdapter communityActivitiesListAdapter = null;

    private ArrayList<TargetItem> myTargets = null;

    private MyTargetsListAdapter myTargetsListAdapter = null;

    private ArrayList<GroupItem> groups = null;

    private GroupsListAdapter groupsListAdapter = null;

    private ViewPager mPager;

    private PagerAdapter mPagerAdapter;

    private TabHost mTabHost;

    public TabHost getTabHost()
    {
        return this.mTabHost;
    }

    public ArrayList<ActivityItem> getCommunityActivities()
    {
        return this.communityActivities;
    }

    public ArrayList<TargetItem> getMyTargets()
    {
        return this.myTargets;
    }

    public ArrayList<GroupItem> getGroups()
    {
        return this.groups;
    }

    public CommunityActivitiesListAdapter getCommunityActivitiesListAdapter()
    {
        return this.communityActivitiesListAdapter;
    }
    public GroupsListAdapter getGroupsListAdapter()
    {
        return this.groupsListAdapter;
    }

    public MyTargetsListAdapter getMyTargetsListAdapter()
    {
        return this.myTargetsListAdapter;
    }

    private DatabaseReference mDatabase = null;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();
        setupPages();

        communityActivities = new ArrayList<>();
        communityActivitiesListAdapter = new CommunityActivitiesListAdapter(this, this.communityActivities, this.myTargets);

        myTargets = new ArrayList<>();
        myTargetsListAdapter = new MyTargetsListAdapter(this, this.myTargets);

        groups = new ArrayList<>();
        groupsListAdapter = new GroupsListAdapter(this, this.groups);

        mDatabase = FirebaseDatabase.getInstance().getReference();

        Query activitiesQuery = mDatabase.child("Activities");
        activitiesQuery.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                ActivityItem activity = new ActivityItem(dataSnapshot.getKey(), dataSnapshot.child("name").getValue().toString(), dataSnapshot.child("unit").getValue().toString(), dataSnapshot.child("genre").getValue().toString());
                handleGetActivities(activity);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                boolean searching = true;
                int i = 0;
                while(searching)
                {
                    if(communityActivities.get(i).getKey().equals(dataSnapshot.getKey()))
                    {
                        communityActivities.remove(i);
                        communityActivitiesListAdapter.notifyDataSetChanged();
                        searching = false;
                    }
                    else
                    {
                        ++i;
                        if(i >= communityActivities.size())
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

        Query targetsQuery = mDatabase.child("users").child(user.getUid()).child("Targets");
        targetsQuery.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                TargetItem targetItem = new TargetItem(dataSnapshot.getKey(), dataSnapshot.child("activity_key").getValue().toString(), dataSnapshot.child("name").getValue().toString(), dataSnapshot.child("unit").getValue().toString(), dataSnapshot.child("genre").getValue().toString(), Integer.parseInt(dataSnapshot.child("baseTarget").getValue().toString()), Integer.parseInt(dataSnapshot.child("stretchTarget").getValue().toString()));
                handleGetActivitiesWithTargets(targetItem);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                boolean searching = true;
                int i = 0;
                while(searching)
                {
                    if(myTargets.get(i).getKey().equals(dataSnapshot.getKey()))
                    {
                        myTargets.remove(i);
                        myTargetsListAdapter.notifyDataSetChanged();
                        communityActivitiesListAdapter.setTargets(myTargets);
                        communityActivitiesListAdapter.notifyDataSetChanged();
                        searching = false;
                    }
                    else
                    {
                        ++i;
                        if(i >= myTargets.size())
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
        Query usersQuery = mDatabase.child("users").child(user.getUid()).child("groupKeys");
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
            //super.onBackPressed();
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
            Intent intent = new Intent(HomeActivity.this, SignInActivity.class);
            intent.putExtra("logout", true);
            startActivity(intent);
            //Logout
        }
        else if (id == R.id.view_profile) {
            //Open Profile Activity
            Intent intent = new Intent(HomeActivity.this, ProfileActivity.class);
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

        TabHost.TabSpec tabSpec = this.mTabHost.newTabSpec("My Activities");
        tabSpec.setContent(R.id.tab_my_activities);
        tabSpec.setIndicator("My Activities");
        this.mTabHost.addTab(tabSpec);

        tabSpec = this.mTabHost.newTabSpec("Community Activities");
        tabSpec.setContent(R.id.tab_community_targets);
        tabSpec.setIndicator("Community Activities");
        this.mTabHost.addTab(tabSpec);

        tabSpec = this.mTabHost.newTabSpec("Groups");
        tabSpec.setContent(R.id.tab_groups);
        tabSpec.setIndicator("Groups");
        this.mTabHost.addTab(tabSpec);

        this.mTabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            @Override
            public void onTabChanged(String s) {
                if(s.equals("My Activities"))
                {
                    mPager.setCurrentItem(0, true);
                }
                else if(s.equals("Community Activities"))
                {
                    mPager.setCurrentItem(1, true);
                }
                else if(s.equals("Groups"))
                {
                    mPager.setCurrentItem(2, true);
                }

            }
        });


        this.mPagerAdapter = new HomePagerAdapter(getSupportFragmentManager());
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

    public void handleGetActivities(ActivityItem activity)
    {
        communityActivities.add(activity);
        communityActivitiesListAdapter.notifyDataSetChanged();

    }

    public void handleGetGroups(GroupItem group)
    {
        groups.add(group);
        groupsListAdapter.notifyDataSetChanged();

    }

    public void handleGetActivitiesWithTargets(TargetItem targetItem)
    {
        myTargets.add(targetItem);
        myTargetsListAdapter.notifyDataSetChanged();
        communityActivitiesListAdapter.setTargets(myTargets);
        communityActivitiesListAdapter.notifyDataSetChanged();
        mPager.setCurrentItem(0);

    }


}
