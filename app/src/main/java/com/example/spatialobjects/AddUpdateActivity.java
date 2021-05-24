package com.example.spatialobjects;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.blogspot.atifsoftwares.circularimageview.CircularImageView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
import java.nio.channels.FileChannel;
import java.sql.Timestamp;
import java.util.ArrayList;

public class AddUpdateActivity extends AppCompatActivity {

    // VIEWS
    CircularImageView profileImageView;
    EditText designationEditText,longitudeEditText,latitudeEditText,altitudeEditText,descriptionEditText;
    FloatingActionButton saveBtn,addLocationBtn;

    // ACTION bar
    private ActionBar actionBar;

    // PERMISSIONS ------------
    // CAMERA Permissions Codes
    private static final int CAMERA_REQUEST_CODE = 100;
    private static final int STORAGE_REQUEST_CODE = 101;
    // IMAGE PICKING Codes
    private static final int IMAGE_PICK_CAMERA_CODE = 102;
    private static final int IMAGE_PICK_GALLERY_CODE = 103;
    // Permissions array
    private String[] cameraPermissions; // All permissions for camera uses and storing
    private String[] storagePermissions; // Just Storage

    // Image Variable choosed
    private Uri imageUri;

    // Data Base Helper
    DbHelper dbHelper;

    Intent formIntent;
    ArrayList<String> formIntentData;

    // INPUTS values
    private String id,designation, image, longitude, latitude, altitude, description,actualTimeStamp,addedDate,updatedDate;
    private boolean isEditeMode = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_update);


        //Init Action Bar
        actionBar = getSupportActionBar();
        //Set Title of Support Action Bar
        actionBar.setTitle("Add an Object");
        // Enable Back Button (HOME)
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        // INIT Views
        profileImageView = findViewById(R.id.profileImageView);
        designationEditText = findViewById(R.id.designationEditText);
        longitudeEditText = findViewById(R.id.longitudeEditText);
        latitudeEditText = findViewById(R.id.latitudeEditText);
        altitudeEditText = findViewById(R.id.altitudeEditText);
        descriptionEditText = findViewById(R.id.descriptionEditText);
        saveBtn = findViewById(R.id.saveBtn);
        addLocationBtn = findViewById(R.id.addLocationBtn);

        // GET DATA from Intent
        Intent intent = getIntent();
        isEditeMode = intent.getBooleanExtra("isEdit_Mode",false);

        if(isEditeMode){
            //Set Title of Support Action Bar
            actionBar.setTitle("Update Object");

            // UPDATE
            id = intent.getStringExtra("ID");
            designation = intent.getStringExtra("DESIGNATION");
            imageUri = Uri.parse(intent.getStringExtra("IMAGE"));
            longitude = intent.getStringExtra("LONGITUDE");
            latitude = intent.getStringExtra("LATITUDE");
            altitude = intent.getStringExtra("ALTITUDE");
            description = intent.getStringExtra("DESCRIPTION");
            addedDate = intent.getStringExtra("ADDED_DATE");
            updatedDate = intent.getStringExtra("UPDATED_DATE");

            //SET Data
            designationEditText.setText(designation);
            longitudeEditText.setText(longitude);
            latitudeEditText.setText(latitude);
            altitudeEditText.setText(altitude);
            descriptionEditText.setText(description);

            //If no image wass while adding data
            if(imageUri.toString().equals(null)){
                profileImageView.setImageResource(R.drawable.ic_object);
            }
            else {
                profileImageView.setImageURI(imageUri);
            }
        }

        // INIT DB Helper
        dbHelper = new DbHelper(this);

        //INIT Permissions Array
        cameraPermissions = new String[]{Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE};
        storagePermissions = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};

        formIntent = getIntent();
        formIntentData = formIntent.getStringArrayListExtra("formDatawithLatLong");
        if(formIntentData != null) {
            if(formIntentData.size()>0){
                designationEditText.setText(formIntentData.get(0).toString());
                profileImageView.setImageURI(Uri.parse(formIntentData.get(1).toString()));
                Toast.makeText(this, formIntentData.get(1), Toast.LENGTH_SHORT).show();
                longitudeEditText.setText(formIntentData.get(2).toString());
                latitudeEditText.setText(formIntentData.get(3).toString());
                altitudeEditText.setText(formIntentData.get(4).toString());
                descriptionEditText.setText(formIntentData.get(5).toString());
            }
        }

        // IMAGE PICKER --> SHOW DIALOG
        profileImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowPickImage();
            }
        });

        // Click to save Data in DB
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SaveObject();
            }
        });

        // Click to save Data in DB
        addLocationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String design = ""+designationEditText.getText().toString().trim();
                String img = ""+imageUri;
                String lng = ""+longitudeEditText.getText().toString().trim();
                String lat = ""+latitudeEditText.getText().toString().trim();
                String alti = ""+altitudeEditText.getText().toString().trim();
                String descrp = ""+descriptionEditText.getText().toString().trim();

                Intent intent = new Intent(AddUpdateActivity.this,AddLocationActivity.class);
                ArrayList<String> formData = new ArrayList<>();
                formData.add(design);
                formData.add(img);
                formData.add(lng);
                formData.add(lat);
                formData.add(alti);
                formData.add(descrp);
                intent.putStringArrayListExtra("formData",formData);
                startActivity(intent);
            }
        });
    }

    private void SaveObject() {
        designation = ""+designationEditText.getText().toString().trim();
        image = ""+imageUri;
        longitude = ""+longitudeEditText.getText().toString().trim();
        latitude = ""+latitudeEditText.getText().toString().trim();
        altitude = ""+altitudeEditText.getText().toString().trim();
        description = ""+descriptionEditText.getText().toString().trim();

        if(isEditeMode){

            String actualTimeStamp = ""+System.currentTimeMillis();

            dbHelper.updateObject(id,designation,image,longitude,latitude,altitude,description,addedDate,actualTimeStamp);

            Toast.makeText(this,"Updated Successfully!",Toast.LENGTH_SHORT).show();
        }
        else {
            actualTimeStamp = ""+System.currentTimeMillis();

            if(TextUtils.isEmpty(designation)) {
                designationEditText.setError("Please fill the field.");
                return;
            }
            if(TextUtils.isEmpty(longitude)) {
                longitudeEditText.setError("Please fill the field.");
                return;
            }
            if(TextUtils.isEmpty(latitude)) {
                latitudeEditText.setError("Please fill the field.");
                return;
            }
            if(TextUtils.isEmpty(altitude)) {
                altitudeEditText.setError("Please fill the field.");
                return;
            }
            if(TextUtils.isEmpty(description)) {
                descriptionEditText.setError("Please fill the field.");
                return;
            }

            long addedObjectID = dbHelper.insertObject(designation,image,longitude,latitude,altitude,description,actualTimeStamp,actualTimeStamp);

            Toast.makeText(this,"New Object Added, ID: "+addedObjectID,Toast.LENGTH_SHORT).show();
        }

    }

    private void ShowPickImage() {
        // OPTIONS [ CAMERA or GALLERY]
        String[] options = {"Camera","Gallery"};

        // SHOW DIALOG and Store option chosen
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        // SET Title
        builder.setTitle("Pick an Image From...");
        // SET items and options
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int choice) {
                // CHOICE handling
                if(choice == 0) {
                    // CAMERA is Clicked
                    if(!CheckCameraPermission()){
                        RequestCameraPermission();
                    }
                    else {
                        //Permission is granted before
                        PickFromCamera();
                    }
                }
                else if (choice == 1 ) {
                    // GALLERY is Clicked
                    if(!CheckStoragePermission()){
                        RequestStoragePermission();
                    }
                    else {
                        //Permission is granted before
                        PickFromGallery();
                    }
                }
            }
        });

        //Create and Show The DIALOG Alert
        builder.create().show();
    }

    // STORAGE PERMISSION METHODS  [ CHECK && REQUEST ] --------------------------------------------
    private boolean CheckStoragePermission() {
        // Check if Storage Permission is enabled
        boolean result = ContextCompat.checkSelfPermission(this,Manifest.permission.WRITE_EXTERNAL_STORAGE)
                ==
                PackageManager.PERMISSION_GRANTED;
        return result;
    }
    private void RequestStoragePermission() {
        ActivityCompat.requestPermissions(this,storagePermissions,STORAGE_REQUEST_CODE);
    }

    // CAMERA PERMISSIONS METHODS  [ CHECK && REQUEST ] --------------------------------------------
    private boolean CheckCameraPermission() {
        // Check if Camera picture and Storage Permissions are enabled
        boolean resultCam = ContextCompat.checkSelfPermission(this,Manifest.permission.CAMERA)
                ==
                PackageManager.PERMISSION_GRANTED;
        boolean resultStorage = ContextCompat.checkSelfPermission(this,Manifest.permission.WRITE_EXTERNAL_STORAGE)
                ==
                PackageManager.PERMISSION_GRANTED;
        return resultCam && resultStorage;
    }
    private void RequestCameraPermission() {
        ActivityCompat.requestPermissions(this,cameraPermissions,CAMERA_REQUEST_CODE);
    }

    // PICK IMAGE FROM CAMERA or GALLERY -----------------------------------------------------------
    //CAMERA
    private void PickFromCamera() {
        // ContentValues to set Image title and description
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, "Image title");
        values.put(MediaStore.Images.Media.DESCRIPTION, "Image description");

        //Store it in IMAGE URI variable
        imageUri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,values);

        // USE Intent to transfer the Image from Gallery to the AddUpdateActivity with onActivityResult()
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT,imageUri);
        startActivityForResult(cameraIntent,IMAGE_PICK_CAMERA_CODE);
    }
    //GALLERY
    private void PickFromGallery() {
        // INTENT to transfer the Image from Gallery to the AddUpdateActivity with onActivityResult()
        Intent galleryIntent = new Intent(Intent.ACTION_PICK);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent,IMAGE_PICK_GALLERY_CODE);
    }

    // File or Directory for Choosen Image
    private void CopyFileOrDirectory(String srcDir,String desDir) {
        // Create Folder in specific directory
        try {
            File src= new File(srcDir);
            File des= new File(desDir, src.getName());
            if(src.isDirectory()){
                String[] files = src.list();
                int filesLenght = files.length;
                for (String file : files ){
                    String src1 = new File(src,file).getPath();
                    String des1 = des.getPath();

                    CopyFileOrDirectory(src1,des1);
                }
            }
            else {
                CopyFile(src,des);
            }
        }
        catch (Exception err) {
            Toast.makeText(this, err.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void CopyFile(File srcDir, File desDir) throws IOException {
        if(!desDir.getParentFile().exists()){
            desDir.mkdirs();
        }

        if(!desDir.exists()){
            desDir.createNewFile();
        }

        FileChannel source = null;
        FileChannel destination= null;

        try {
            source = new FileInputStream(srcDir).getChannel();
            destination = new FileOutputStream(desDir).getChannel();
            destination.transferFrom(source,0,source.size());

            imageUri = Uri.parse(desDir.getPath());
            Log.d("ImagePath","copyFile: "+ imageUri);
        }
        catch (Exception err){
            Toast.makeText(this, err.getMessage(), Toast.LENGTH_SHORT).show();
        }
        finally {
            if(source!=null){
                source.close();
            }

            if(destination!=null){
                destination.close();
            }
        }
    }

    // OVERWRITTEN METHODS -------------------------------------------------------------------------
    // BACK to HOME ACTIVITY
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i=new Intent(this,MainActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        finish();
        startActivity(i);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }

    // Permissions Result
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        // Check The Result of the REQUEST sent
        switch (requestCode) {
            // Case of Camera request
            case CAMERA_REQUEST_CODE:
                if(grantResults.length>0){
                    boolean accessToCamera = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean accessToStorage = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                    // If permissions granted --> Pick Image
                    if(accessToCamera && accessToStorage) {
                        //Pick Image from Camera
                        PickFromCamera();
                    }
                    else {
                        Toast.makeText(this,"Access to Camera and Storage are Required!",Toast.LENGTH_SHORT).show();
                    }
                }
                break;
            // Case of Storage request Only
            case STORAGE_REQUEST_CODE:
                if(grantResults.length>0){
                    boolean accessToStorage = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    // If permissions granted --> Pick Image
                    if(accessToStorage) {
                        //Pick Image from Camera
                        PickFromGallery();
                    }
                    else {
                        Toast.makeText(this,"Access to Storage are Required!",Toast.LENGTH_SHORT).show();
                    }
                }
                break;
        }
    }

    // Activity Result (DATA)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        // The received Camera is sent to ActivityResult [ HERE ]
        if(resultCode == RESULT_OK) // -1
        {
            // Image is picked Successfully
            if( requestCode == IMAGE_PICK_GALLERY_CODE ) {
                // Picked from Gallery

                //CROP Image
                CropImage.activity(data.getData())
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .setAspectRatio(1,1)
                        .start(this);
            }
            else if( requestCode == IMAGE_PICK_CAMERA_CODE ){
                // Picked from Camera

                //CROP Image
                CropImage.activity(imageUri)
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .setAspectRatio(1,1)
                        .start(this);

            }
            else if( requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
                CropImage.ActivityResult result = CropImage.getActivityResult(data);
                if(resultCode == RESULT_OK) {
                    // CROP Image RECEIVED
                    Uri resultUri = result.getUri();
                        imageUri = resultUri;
                        //set Image of profile
                        profileImageView.setImageURI(resultUri);

                        CopyFileOrDirectory(imageUri.getPath(),""+getDir("SQLiteObjectImages",MODE_PRIVATE));
                }
                else if( resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                    // CROP Image IS NOT RECEIVED !! EXCEPTION !!!
                    Exception error = result.getError();
                    Toast.makeText(this, error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

}