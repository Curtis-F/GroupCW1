package com2027.software.group1.groupproject;

import android.app.DatePickerDialog;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class ProfileActivity extends AppCompatActivity {

    private Button mButtonSave = null;

    private Button mButtonDelete = null;

    private Button mButtonChangePicture = null;

    private EditText mName = null;

    private EditText mBirthday = null;

    private ImageView mPicture = null;

    private Bitmap CurrentPicture = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        mName = (EditText) findViewById(R.id.edit_name);

        mBirthday = (EditText) findViewById(R.id.birthday_picker);

        final Calendar calendar = Calendar.getInstance();

        final DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, month);
                calendar.set(Calendar.DAY_OF_MONTH, day);

                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);

                mBirthday.setText(simpleDateFormat.format(calendar.getTime()));
            }
        };

        mBirthday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(ProfileActivity.this, dateSetListener,
                        calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        mPicture = (ImageView) findViewById(R.id.profile_picture);

        mPicture.setImageBitmap(CurrentPicture);

        mButtonChangePicture = (Button) findViewById(R.id.change_picture);

        mButtonChangePicture.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                //TODO: get new picture as bitmap, then set imageview and CurrentPicture
            }
        });




        mButtonSave = (Button) findViewById(R.id.save);
        mButtonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //:TODO Validate input
                Date birthday = null;
                String date = mBirthday.getText().toString();
                try {
                    SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");

                    birthday = format.parse(date);
                } catch (ParseException parseException) {
                    Log.v("Date", "Error occured when parsing date");
                    Toast.makeText(ProfileActivity.this, "Error occured when parsing date.", Toast.LENGTH_SHORT);
                }

                ProfileItem profileItem = new ProfileItem(mName.getText().toString(), birthday, CurrentPicture);

            }
        });

        mButtonDelete = (Button) findViewById(R.id.delete);
        mButtonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //:TODO Alert to ensure it's deliberate
            }
        });

    }

}
