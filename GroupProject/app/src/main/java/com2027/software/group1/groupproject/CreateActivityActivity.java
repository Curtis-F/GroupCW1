package com2027.software.group1.groupproject;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

public class CreateActivityActivity extends AppCompatActivity {

    private Button mButtonCreate = null;

    private EditText mActivityName = null;

    private EditText mBaseTarget = null;

    private EditText mStretchTarget = null;

    private Spinner mSpinnerTarget = null;

    private Spinner mSpinnerUnits = null;

    private Spinner mSpinnerGenre = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_activity);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mActivityName = (EditText) findViewById(R.id.edit_new_name);

        mBaseTarget = (EditText) findViewById(R.id.edit_base_target);

        mStretchTarget = (EditText) findViewById(R.id.edit_stretch_target);

        mSpinnerTarget = (Spinner) findViewById(R.id.spinner_targets);

        mSpinnerTarget.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, TargetType.values()));

        mSpinnerUnits = (Spinner) findViewById(R.id.spinner_units);

        mSpinnerUnits.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, UnitType.values()));

        mSpinnerGenre = (Spinner) findViewById(R.id.spinner_genre);

        mSpinnerGenre.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, GenreType.values()));

        mButtonCreate = (Button) findViewById(R.id.create);
        mButtonCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //:TODO Validate input
                ActivityItem activityItem = new ActivityItem(mActivityName.getText().toString(),
                        TargetType.getTargetType(mSpinnerTarget.getSelectedItem().toString()),
                        UnitType.getUnitType(mSpinnerUnits.getSelectedItem().toString()),
                        GenreType.getGenreType(mSpinnerGenre.getSelectedItem().toString()),
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
            Intent intent = new Intent(CreateActivityActivity.this, ProfileActivity.class);
            //TODO: Provide extras to parse user data
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

}
