package com2027.software.group1.groupproject;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class MembersFragment extends Fragment {

    private Button mAddMember = null;

    private Button mLeaveGroup = null;

    private EditText mMemberEmail = null;

    private GroupsActivity groupsActivity = null;

    private DatabaseReference mDatabase = null;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.content_members_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        groupsActivity = (GroupsActivity) getActivity();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        ListView listView = (ListView) getView().findViewById(R.id.members_list_view);
        listView.setAdapter(((GroupsActivity)getActivity()).getMembersListAdapter());

        //TODO: On Click show profile?
//        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                Intent intent = new Intent((getTarget()), GroupsActivity.class);
//                if(getTarget().getClass().equals(HomeActivity.class))
//                {
//                    intent.putExtra("group", ((HomeActivity) getTarget()).getGroups().get(i));
//                }
//                else
//                {
//                    intent.putExtra("group", ((TargetActivity) getTarget()).getGroups().get(i));
//                }
//                startActivity(intent);
//            }
//        });

        mAddMember = (Button) getView().findViewById(R.id.add_member);
        mAddMember.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String newMember = mMemberEmail.getText().toString().trim();
                if(!newMember.isEmpty())
                {

                    DatabaseReference ref = mDatabase.child("userEmails");//mMemberEmail.getText().toString().trim());
                    ref.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot)
                        {
                            boolean exists = false;
                            if(dataSnapshot.exists())
                            {
                                Iterable<DataSnapshot> data = dataSnapshot.getChildren();

                                boolean searching = true;
                                while(searching)
                                {
                                    if(data.iterator().hasNext())
                                    {
                                        if(data.iterator().next().getValue().toString().equals(newMember))
                                        {
                                            exists = true;
                                            searching = false;
                                        }
                                    }
                                    else
                                    {
                                        searching = false;
                                    }
                                }

                            }

                            if(exists)
                            {
                                Query usersQuery = mDatabase.child("users").orderByChild("email").equalTo(newMember);
                                usersQuery.addChildEventListener(new ChildEventListener() {
                                    @Override
                                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                                        if (dataSnapshot != null) {
                                            final String foundUserKey = dataSnapshot.getKey();
                                            DatabaseReference ref = mDatabase.child("groups").child(groupsActivity.getGroup().getKey()).child("userKeys");
                                            String key = ref.push().getKey();
                                            //User newUser = new User(dataSnapshot.child("username").getValue().toString(), dataSnapshot.child("email").getValue().toString());
                                            ref.child(key).setValue(foundUserKey);

                                            ref = mDatabase.child("users").child(foundUserKey).child("groupKeys");
                                            key = ref.push().getKey();
                                            ref.child(key).setValue(groupsActivity.getGroup().getKey());


                                            ref = mDatabase.child("users").child(foundUserKey).child("Targets");
                                            ref.addListenerForSingleValueEvent(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(DataSnapshot dataSnapshot) {
                                                    boolean exists = false;
                                                    if(dataSnapshot.exists())
                                                    {
                                                        Iterable<DataSnapshot> data = dataSnapshot.getChildren();

                                                        boolean searching = true;
                                                        while(searching)
                                                        {
                                                            if(data.iterator().hasNext())
                                                            {
                                                                DataSnapshot snap = data.iterator().next();
                                                                if(snap.child("activity_key").getValue().toString().equals(groupsActivity.getGroup().getActivity_key()))
                                                                {
//                                                                    DatabaseReference ref = mDatabase.child("users").child(foundUserKey).child("Targets").child(snap.getKey()).child("groupKeys");
//                                                                    String key = ref.push().getKey();
//                                                                    ref.child(key).setValue(groupsActivity.getGroup().getKey());
                                                                    exists = true;
                                                                    searching = false;
                                                                }
                                                            }
                                                            else
                                                            {
                                                                searching = false;
                                                            }
                                                        }
                                                        if(!exists)
                                                        {
                                                            DatabaseReference ref = mDatabase.child("users").child(dataSnapshot.getKey()).child("Targets");
                                                            String key = ref.push().getKey();
                                                            TargetItem targetItem = new TargetItem(key, groupsActivity.getGroup().getActivity_key(), groupsActivity.getGroup().getActivity_name(), groupsActivity.getGroup().getUnit(), groupsActivity.getGroup().getGenre(), 0, 0);
                                                            ref.child(key).setValue(targetItem);
                                                        }

                                                    }
                                                }

                                                @Override
                                                public void onCancelled(DatabaseError databaseError) {

                                                }
                                            });

                                        }
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
                            else
                            {
                                Toast.makeText(getContext(), "User does not exist.", Toast.LENGTH_LONG).show();
                            }

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }
            }
        });
        mLeaveGroup = (Button) getView().findViewById(R.id.leave_group);

        mMemberEmail = (EditText) getView().findViewById(R.id.member_email);

        mMemberEmail.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int keyCode, KeyEvent keyEvent) {
                if(keyCode == KeyEvent.KEYCODE_ENTER && keyEvent.getAction() == KeyEvent.ACTION_DOWN )
                {
                    mAddMember.callOnClick();
                    InputMethodManager inputMethodManager = (InputMethodManager)view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
                }
                return false;
            }
        });


    }

}
