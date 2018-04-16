package com2027.software.group1.groupproject;

import android.content.Context;
import android.content.DialogInterface;
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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.ArrayList;

public class MyTargetsListAdapter extends ArrayAdapter<TargetItem> {
    private ArrayList<TargetItem> targets = null;

    public MyTargetsListAdapter(Context context, ArrayList<TargetItem> targets)
    {
        super(context, R.layout.my_activities_list_item_layout ,targets);
        this.targets = targets;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View row = inflater.inflate(R.layout.my_activities_list_item_layout, parent, false);

        TextView textView = (TextView) row.findViewById(R.id.my_activities_name);
        textView.setText(targets.get(position).getName());

        Button button = (Button) row.findViewById(R.id.my_activities_add_group);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HomeActivity homeActivity = (HomeActivity) getContext();
                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(homeActivity);
                LayoutInflater layoutInflater = homeActivity.getLayoutInflater();
                final View dialogView = layoutInflater.inflate(R.layout.dialog_create_group, null);
                dialogBuilder.setView(dialogView);

                final EditText base = (EditText) dialogView.findViewById(R.id.edit_base_target);
                base.setText("0");

                final EditText stretch = (EditText) dialogView.findViewById(R.id.edit_stretch_target);
                stretch.setText("0");

                TextView activityName = (TextView) dialogView.findViewById(R.id.activity_name);
                activityName.setText(targets.get(position).getName());

                final EditText name = (EditText) dialogView.findViewById(R.id.group_name);

                dialogBuilder.setTitle("Set your Group's name:");
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
                                if(!name.getText().toString().isEmpty()) {
                                    if (!base.getText().toString().isEmpty() && Integer.parseInt(base.getText().toString()) > 0 && !stretch.getText().toString().isEmpty() && Integer.parseInt(stretch.getText().toString()) > 0) {
                                        if (Integer.parseInt(base.getText().toString()) < Integer.parseInt(stretch.getText().toString())) {

                                            FirebaseAuth auth = FirebaseAuth.getInstance();
                                            FirebaseUser user = auth.getCurrentUser();
                                            DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
                                            DatabaseReference ref = mDatabase.child("/groups");
                                            String key = ref.push().getKey();
                                            ArrayList<String> userKeys = new ArrayList<String>();
                                            userKeys.add(user.getUid());
                                            GroupItem groupItem = new GroupItem(key, name.getText().toString(), targets.get(position).getActivity_key(), targets.get(position).getName(), targets.get(position).getUnit(), targets.get(position).getGenre(), Integer.parseInt(base.getText().toString()), Integer.parseInt(stretch.getText().toString()), userKeys);
                                            ref.child(key).setValue(groupItem);
                                            ref = mDatabase.child("users").child(user.getUid()).child("groupKeys");
                                            key = ref.push().getKey();
                                            ref.child(key).setValue(groupItem.getKey());
                                            ref = mDatabase.child("users").child(user.getUid()).child("Targets").child(targets.get(position).getKey()).child("groupKeys");
                                            key = ref.push().getKey();
                                            ref.child(key).setValue(groupItem.getKey());
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
                                    Toast.makeText(getContext(), "Please enter a name for the group.", Toast.LENGTH_SHORT).show();
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

        return row;
    }
}