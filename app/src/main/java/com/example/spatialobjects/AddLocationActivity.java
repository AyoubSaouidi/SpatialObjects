package com.example.spatialobjects;

import androidx.appcompat.app.ActionBar;
import androidx.fragment.app.FragmentActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

public class AddLocationActivity extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnMapLongClickListener {

    private GoogleMap mMap;
    private ArrayList<String> formData;


    Intent formIntent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_location);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        formIntent = getIntent();
        formData= formIntent.getStringArrayListExtra("formData");
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setOnMapLongClickListener(this);

    }


    @Override
    public void onMapLongClick(LatLng latLng) {

        Toast.makeText(this, "Latitude: "+ latLng.latitude +"; Longitude: "+latLng.longitude, Toast.LENGTH_SHORT).show();


        formData.set(2,String.valueOf(latLng.longitude));
        formData.set(3,String.valueOf(latLng.latitude));

        Intent intent = new Intent(AddLocationActivity.this,AddUpdateActivity.class);
        intent.putStringArrayListExtra("formDatawithLatLong",formData);
        startActivity(intent);
    }
}