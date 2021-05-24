package com.example.spatialobjects;

import androidx.fragment.app.FragmentActivity;

import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

public class LocationActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    private ArrayList<Object> objectsList;

    private DbHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        //DB helper
        dbHelper = new DbHelper(this);
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        objectsList = dbHelper.getAllObjects(Constants.ADDED_DATE + " DESC");

        for(Object obj : objectsList) {
            LatLng position = new LatLng(Double.valueOf(obj.getLatitude()),Double.valueOf(obj.getLongitude()));
            mMap.addMarker(new MarkerOptions().position(position).title(obj.getDesignation().toString()));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(position,5));
        }


    }
}