package com2027.software.group1.groupproject;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Calendar;

public class ProgressFragment extends Fragment {

    private ProgressBar mProgressBar = null;

    private TextView mProgressText = null;

    private ImageButton mIncrementButton = null;

    private ImageButton mDecrementButton = null;

    private EditText mProgressEdit = null;

    private Button mProgressUpdate = null;

    private Button mEditTarget = null;

    private TargetItem targetItem = null;

    private ArrayList<LogItem> logs = null;

    private DatabaseReference mDatabase = null;

    private FirebaseUser user = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.content_progress_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        ((TargetActivity)getActivity()).setProgressFragment(this);

        mDatabase = FirebaseDatabase.getInstance().getReference();
        FirebaseAuth auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        setupProgressBar();

        ListView listView = (ListView) getView().findViewById(R.id.logs_list_view);
        listView.setAdapter(((TargetActivity)getActivity()).getLogsListAdapter());

        mIncrementButton = (ImageButton) getView().findViewById(R.id.progress_ammount_increment);

        mDecrementButton = (ImageButton) getView().findViewById(R.id.progress_ammount_decrement);

        mProgressEdit = (EditText) getView().findViewById(R.id.progress_ammount);

        mProgressUpdate = (Button) getView().findViewById(R.id.update_progress);

        mEditTarget = (Button) getView().findViewById(R.id.edit_target);

        mIncrementButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!mProgressEdit.getText().toString().isEmpty()) {
                    int progressAmmount = Integer.parseInt(mProgressEdit.getText().toString());
                    ++progressAmmount;
                    mProgressEdit.setText(Integer.toString(progressAmmount));
                }
                else {
                    mProgressEdit.setText("1");
                }
            }
        });

        mDecrementButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!mProgressEdit.getText().toString().isEmpty()) {
                    int progressAmmount = Integer.parseInt(mProgressEdit.getText().toString());
                    if (progressAmmount != 0) {
                        --progressAmmount;
                        mProgressEdit.setText(Integer.toString(progressAmmount));
                    }
                }
                else {
                    mProgressEdit.setText("0");
                }
            }
        });

        mProgressUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!mProgressEdit.getText().toString().isEmpty() && Integer.parseInt(mProgressEdit.getText().toString()) != 0) {

                    MyDateTime myDateTime = new MyDateTime(Calendar.getInstance().get(Calendar.DAY_OF_MONTH), Calendar.getInstance().get(Calendar.MONTH), Calendar.getInstance().get(Calendar.YEAR), Calendar.getInstance().getTimeInMillis());

                    DatabaseReference ref = mDatabase.child("users").child(user.getUid()).child("Targets").child(targetItem.getKey()).child("Logs");
                    String key = ref.push().getKey();
                    LogItem logItem = new LogItem(key, targetItem.getActivity_key(), Integer.parseInt(mProgressEdit.getText().toString()), myDateTime);
                    ref.child(key).setValue(logItem);

                }
            }
        });
        mProgressEdit.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int keyCode, KeyEvent keyEvent) {
                if(keyCode == KeyEvent.KEYCODE_ENTER && keyEvent.getAction() == KeyEvent.ACTION_DOWN )
                {
                    mProgressUpdate.callOnClick();
                    InputMethodManager inputMethodManager = (InputMethodManager)view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
                }
                return false;
            }
        });

        mEditTarget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final TargetActivity targetActivity = (TargetActivity) getActivity();
                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(targetActivity);
                LayoutInflater layoutInflater = targetActivity.getLayoutInflater();
                final View dialogView = layoutInflater.inflate(R.layout.dialog_edit_target, null);
                dialogBuilder.setView(dialogView);

                final EditText base = (EditText) dialogView.findViewById(R.id.change_base_target);
                base.setText(Integer.toString(targetItem.getBaseTarget()));

                final EditText stretch = (EditText) dialogView.findViewById(R.id.change_stretch_target);
                stretch.setText(Integer.toString(targetItem.getStretchTarget()));


                dialogBuilder.setTitle("Change your target:");
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
                                if(!base.getText().toString().isEmpty() && Integer.parseInt(base.getText().toString()) > 0 && !stretch.getText().toString().isEmpty() && Integer.parseInt(stretch.getText().toString()) > 0)
                                {
                                    if(Integer.parseInt(base.getText().toString()) < Integer.parseInt(stretch.getText().toString()))
                                    {
                                        UpdateTarget(Integer.parseInt(base.getText().toString()), Integer.parseInt(stretch.getText().toString()));
                                        alertDialog.dismiss();
                                    }
                                    else
                                    {
                                        Toast.makeText(getContext(), "Stretch target must be higher than Base target.", Toast.LENGTH_SHORT).show();
                                    }
                                }
                                else
                                {
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
        UpdateProgress(new ArrayList<LogItem>());
    }

    private void setupProgressBar()
    {


        mProgressBar = (ProgressBar) getView().findViewById(R.id.progress_bar);
        mProgressText = (TextView) getView().findViewById(R.id.progress_text);

        targetItem = ((TargetActivity)getActivity()).getTarget();
        mProgressBar.setMax(targetItem.getBaseTarget());
    }

    private void UpdateTargetText(int base, int stretch)
    {
        int progress = 0;
        for(int i = 0; i< logs.size(); i++)
        {
            progress += logs.get(i).getValue();
        }
        String text = "";
        if(progress > base)
        {
            if(progress > stretch)
            {
                text += "Stretch Target Achieved!\nYou have done " + Integer.toString(progress) + " ";
                text += UnitType.getUnitType(targetItem.getUnit()) == UnitType.NUMBER ? "" : targetItem.getUnit().toString() + " ";
                text += "of the target " + Integer.toString(stretch) + " you set to complete!";
            }
            else
            {
                mProgressBar.setMax(stretch);
                text = "Base Target Achieved!\nYou have done " + Integer.toString(progress) + " ";
                text += UnitType.getUnitType(targetItem.getUnit()) == UnitType.NUMBER ? "" : targetItem.getUnit().toString() + " ";
                text += "of the target " + Integer.toString(stretch) + " you set to complete!";
            }

        }
        else
        {
            text += "You have done " + Integer.toString(progress) + " ";
            text += UnitType.getUnitType(targetItem.getUnit()) == UnitType.NUMBER ? "" : targetItem.getUnit().toString() + " ";
            text += "of the target " + Integer.toString(base) + " you set to complete!";
        }
        mProgressText.setText(text);
        mProgressBar.setProgress(progress);
    }


    public void UpdateProgress(ArrayList<LogItem> logs)
    {
        this.logs = logs;
        UpdateTargetText(targetItem.getBaseTarget(),  targetItem.getStretchTarget());
    }

    public void UpdateTarget(int base, int stretch){
        DatabaseReference ref = mDatabase.child("users").child(user.getUid()).child("Targets").child(targetItem.getKey());
        ref.child("baseTarget").setValue(base);
        targetItem.setBaseTarget(base);
        ref.child("stretchTarget").setValue(stretch);
        targetItem.setStretchTarget(stretch);
        mProgressBar.setMax(base);
        UpdateTargetText(base, stretch);
    }
}
