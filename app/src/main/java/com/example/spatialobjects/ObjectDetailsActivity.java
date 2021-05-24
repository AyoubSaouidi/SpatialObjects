package com.example.spatialobjects;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.blogspot.atifsoftwares.circularimageview.CircularImageView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class ObjectDetailsActivity extends AppCompatActivity {

    // VIEWS
    CircularImageView profileImageView;
    TextView designationEditText,longitudeEditText,latitudeEditText,altitudeEditText,descriptionEditText,addedDateTextView,updatedDateTextView;
    FloatingActionButton locationBtn;

    private String objectID;

    // ACTION bar
    private ActionBar actionBar;

    // db Helper
    DbHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_object_details);

        //Init Action Bar
        actionBar = getSupportActionBar();
        //Set Title of Support Action Bar
        actionBar.setTitle("Object details");
        // Enable Back Button (HOME)
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        // ID from intent
        Intent intent = getIntent();
        objectID = intent.getStringExtra("OBJECT_ID");

        //INIT DB HELPER
        dbHelper = new DbHelper(this);

        // INIT Views
        profileImageView = findViewById(R.id.profileIv);
        designationEditText = findViewById(R.id.designationEt);
        longitudeEditText = findViewById(R.id.longitudeEt);
        latitudeEditText = findViewById(R.id.latitudeEt);
        altitudeEditText = findViewById(R.id.altitudeEt);
        descriptionEditText = findViewById(R.id.descriptionEt);
        addedDateTextView = findViewById(R.id.addedDateTv);
        updatedDateTextView = findViewById(R.id.updatedDateTv);
        //locationBtn = findViewById(R.id.locationBtn);

        ShowOjectDetails();
    }

    private void ShowOjectDetails(){

        String selectQuery = "SELECT * FROM "+ Constants.TABLE_NAME + " WHERE " + Constants.ID + "=\"" + objectID + "\"";

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery,null);

        if(cursor.moveToFirst()) {
            do {
                Object obj = new Object(
                        ""+cursor.getInt(cursor.getColumnIndex(Constants.ID)),
                        ""+cursor.getString(cursor.getColumnIndex(Constants.DESIGNATION)),
                        ""+cursor.getString(cursor.getColumnIndex(Constants.IMAGE)),
                        ""+cursor.getString(cursor.getColumnIndex(Constants.LONGITUDE)),
                        ""+cursor.getString(cursor.getColumnIndex(Constants.LATITUDE)),
                        ""+cursor.getString(cursor.getColumnIndex(Constants.ALTITUDE)),
                        ""+cursor.getString(cursor.getColumnIndex(Constants.DESCRIPTION)),
                        ""+cursor.getString(cursor.getColumnIndex(Constants.ADDED_DATE)),
                        ""+cursor.getString(cursor.getColumnIndex(Constants.UPDATED_DATE)));

                Calendar calendar1 = Calendar.getInstance(Locale.getDefault());
                calendar1.setTimeInMillis(Long.parseLong(obj.getAdded_date()));
                String addedDate = ""+DateFormat.format("dd/MM/yyyy",calendar1);

                Calendar calendar2 = Calendar.getInstance(Locale.getDefault());
                calendar1.setTimeInMillis(Long.parseLong(obj.getUpdated_date()));
                String updatedDate = ""+DateFormat.format("dd/MM/yyyy",calendar2);

                // SHOW DATA
                designationEditText.setText(obj.getDesignation());
                if(obj.getImage().equals(null)) {
                    profileImageView.setImageResource(R.drawable.ic_object);
                }
                else {
                    profileImageView.setImageURI(Uri.parse(obj.getImage()));
                }
                longitudeEditText.setText(obj.getLongitude());
                latitudeEditText.setText(obj.getLatitude());
                altitudeEditText.setText(obj.getAltitude());
                descriptionEditText.setText(obj.getDescription());
                addedDateTextView.setText(addedDate);
                updatedDateTextView.setText(updatedDate);
            }
            while(cursor.moveToNext());
        }

        db.close();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }
}