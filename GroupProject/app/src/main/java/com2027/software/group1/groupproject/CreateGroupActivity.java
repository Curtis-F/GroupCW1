package com2027.software.group1.groupproject;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.List;

public class CreateGroupActivity extends AppCompatActivity {

    private Button mButtonCreate = null;

    private EditText mGroupName = null;

    private EditText mBaseTarget = null;

    private EditText mStretchTarget = null;

    private Spinner mSpinnerActivity = null;

    private List<ActivityItem> mActivities = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_group);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent intent = getIntent();

        mActivities = (List<ActivityItem>) intent.getSerializableExtra("Activities");

        mGroupName = (EditText) findViewById(R.id.edit_new_name);

        mBaseTarget = (EditText) findViewById(R.id.edit_base_target);

        mStretchTarget = (EditText) findViewById(R.id.edit_stretch_target);

        mSpinnerActivity = (Spinner) findViewById(R.id.spinner_activities);

        //List of activities given to this activity is to be used
        mSpinnerActivity.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, mActivities));

        mButtonCreate = (Button) findViewById(R.id.create);
        mButtonCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //:TODO Validate input
                GroupItem groupItem = new GroupItem(mGroupName.getText().toString(), mActivities.get(mSpinnerActivity.getSelectedItemPosition()),
                        Integer.parseInt(mBaseTarget.getText().toString()), Integer.parseInt(mStretchTarget.getText().toString()));

            }
        });
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
            Intent intent = new Intent(CreateGroupActivity.this, ProfileActivity.class);
            //TODO: Provide extras to parse user data
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }
}
