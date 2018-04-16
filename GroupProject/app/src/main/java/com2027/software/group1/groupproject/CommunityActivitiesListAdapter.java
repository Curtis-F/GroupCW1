package com2027.software.group1.groupproject;

import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class CommunityActivitiesListAdapter extends ArrayAdapter<ActivityItem> {
    private ArrayList<ActivityItem> activities = null;
    private ArrayList<TargetItem> targets = null;
    public void setTargets(ArrayList<TargetItem> targets)
    {
        this.targets = targets;
    }

    public CommunityActivitiesListAdapter(Context context, ArrayList<ActivityItem> activities, ArrayList<TargetItem> targets)
    {
        super(context, R.layout.community_activities_list_item_layout ,activities);
        this.activities = activities;
        this.targets = targets;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View row = inflater.inflate(R.layout.community_activities_list_item_layout, parent, false);

        TextView textView = (TextView) row.findViewById(R.id.community_activities_name);
        textView.setText(activities.get(position).getName());

        Button button = (Button) row.findViewById(R.id.community_activities_join);
        boolean searching = true, exists = false;
        int index = 0;
        if(targets != null) {
            while (searching) {
                if (targets.get(index).getActivity_key().equals(activities.get(position).getKey())) {
                    searching = false;
                    exists = true;
                } else {
                    ++index;
                    if (index == targets.size()) {
                        searching = false;
                    }
                }
            }
        }
        if (!exists)
        {
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    HomeActivity homeActivity = (HomeActivity) getContext();
                    AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(homeActivity);
                    LayoutInflater layoutInflater = homeActivity.getLayoutInflater();
                    final View dialogView = layoutInflater.inflate(R.layout.dialog_edit_target, null);
                    dialogBuilder.setView(dialogView);

                    final EditText base = (EditText) dialogView.findViewById(R.id.change_base_target);
                    base.setText("0");

                    final EditText stretch = (EditText) dialogView.findViewById(R.id.change_stretch_target);
                    stretch.setText("0");


                    dialogBuilder.setTitle("Set your " + activities.get(position).getName() + " targets:");
                    dialogBuilder.setPositiveButton("Save", null);
                    dialogBuilder.setNegativeButton("Cancel", null);

                    final AlertDialog alertDialog = dialogBuilder.create();
                    alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
                        @Override
                        public void onShow(DialogInterface dialogInterface) {
                            Button save = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE);
                            save.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    if (!base.getText().toString().isEmpty() && Integer.parseInt(base.getText().toString()) > 0 && !stretch.getText().toString().isEmpty() && Integer.parseInt(stretch.getText().toString()) > 0) {
                                        if (Integer.parseInt(base.getText().toString()) < Integer.parseInt(stretch.getText().toString())) {

                                            FirebaseAuth auth = FirebaseAuth.getInstance();
                                            FirebaseUser user = auth.getCurrentUser();
                                            DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
                                            DatabaseReference ref = mDatabase.child("/users").child(user.getUid()).child("Targets");
                                            String key = ref.push().getKey();
                                            TargetItem targetItem = new TargetItem(key, activities.get(position).getKey(), activities.get(position).getName(), activities.get(position).getUnit(), activities.get(position).getGenre(), Integer.parseInt(base.getText().toString()), Integer.parseInt(stretch.getText().toString()));
                                            ref.child(key).setValue(targetItem);
                                            alertDialog.dismiss();

                                        } else {
                                            Toast.makeText(getContext(), "Stretch target must be higher than Base target.", Toast.LENGTH_SHORT).show();
                                        }
                                    } else {
                                        Toast.makeText(getContext(), "Please enter values greater than 0.", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                            Button cancel = alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE);
                            cancel.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    alertDialog.dismiss();
                                }
                            });
                        }
                    });
                    alertDialog.show();
                }
            });
        }
        else
        {
            button.setVisibility(View.INVISIBLE);
        }
        return row;
    }
}