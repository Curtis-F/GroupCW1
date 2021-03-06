package com2027.software.group1.groupproject;

import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;
import java.util.Date;


public class CommunityActivitiesFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.content_community_activities_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        ListView listView = (ListView) getView().findViewById(R.id.community_list_view);
        listView.setAdapter(((HomeActivity)getActivity()).getCommunityActivitiesListAdapter());

        Button createActivity = (Button) getView().findViewById(R.id.createActivity);
        createActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HomeActivity homeActivity = (HomeActivity) getActivity();
                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(homeActivity);
                LayoutInflater layoutInflater = homeActivity.getLayoutInflater();
                final View dialogView = layoutInflater.inflate(R.layout.dialog_create_activity, null);
                dialogBuilder.setView(dialogView);

                final EditText base = (EditText) dialogView.findViewById(R.id.edit_base_target);

                final EditText stretch = (EditText) dialogView.findViewById(R.id.edit_stretch_target);

                final EditText name = (EditText) dialogView.findViewById(R.id.edit_new_name);

                final Spinner units = (Spinner) dialogView.findViewById(R.id.spinner_units);

                units.setAdapter(new ArrayAdapter<>(homeActivity, android.R.layout.simple_spinner_item, UnitType.values()));

                final Spinner genre = (Spinner) dialogView.findViewById(R.id.spinner_genre);

                genre.setAdapter(new ArrayAdapter<>(homeActivity, android.R.layout.simple_spinner_item, GenreType.values()));


                dialogBuilder.setTitle("Create your activity and Targets:");
                dialogBuilder.setPositiveButton("Save", null);
                dialogBuilder.setNegativeButton("Cancel",null);

                final AlertDialog alertDialog = dialogBuilder.create();
                alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
                    @Override
                    public void onShow(DialogInterface dialogInterface) {
                        Button save = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE);
                        save.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                if(!name.getText().toString().isEmpty()) {
                                    if (!base.getText().toString().isEmpty() && Integer.parseInt(base.getText().toString()) > 0 && !stretch.getText().toString().isEmpty() && Integer.parseInt(stretch.getText().toString()) > 0) {
                                        if (Integer.parseInt(base.getText().toString()) < Integer.parseInt(stretch.getText().toString())) {
                                            DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
                                            FirebaseAuth auth= FirebaseAuth.getInstance();
                                            FirebaseUser user = auth.getCurrentUser();

                                            DatabaseReference ref = mDatabase.child("/Activities");
                                            String key = ref.push().getKey();
                                            ActivityItem activityItem = new ActivityItem(key, name.getText().toString(), units.getSelectedItem().toString(), genre.getSelectedItem().toString());
                                            ref.child(key).setValue(activityItem);

                                            ref = mDatabase.child("/users").child(user.getUid()).child("Targets");
                                            key = ref.push().getKey();
                                            TargetItem targetItem = new TargetItem(key, activityItem.getKey(), activityItem.getName(), activityItem.getUnit(), activityItem.getGenre(), Integer.parseInt(base.getText().toString()), Integer.parseInt(stretch.getText().toString()));
                                            ref.child(key).setValue(targetItem);
                                            alertDialog.dismiss();
                                        } else {
                                            Toast.makeText(getContext(), "Stretch target must be higher than Base target.", Toast.LENGTH_SHORT).show();
                                        }
                                    } else {
                                        Toast.makeText(getContext(), "Please enter values greater than 0.", Toast.LENGTH_SHORT).show();
                                    }
                                }
                                else
                                {
                                    Toast.makeText(getContext(), "Please enter a name for the activity.", Toast.LENGTH_SHORT).show();
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
}
