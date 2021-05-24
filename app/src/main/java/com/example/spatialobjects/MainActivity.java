package com.example.spatialobjects;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.SearchView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MainActivity extends AppCompatActivity {

    // VIEWS
    private FloatingActionButton addBtn;
    private FloatingActionButton mapBtn;
    private RecyclerView objectsRecyclerView;

    // DB Helper
    private DbHelper dbHelper;

    // ACTION  BAR
    ActionBar actionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // INIT Views of MAIN_ACTIVITY
        addBtn = findViewById(R.id.addBtn);
        mapBtn = findViewById(R.id.mapBtn);
        objectsRecyclerView = findViewById(R.id.objectsRecyclerView);

        // INIT Action Bar
        actionBar = getSupportActionBar();
        actionBar.setTitle("Objects");

        // INIT DB Helper
        dbHelper = new DbHelper(this);

        //LOAD Objects
        LoadObjects();

        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,AddUpdateActivity.class);
                intent.putExtra("isEdit_Mode",false);
                startActivity(intent);
            }
        } );

        mapBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,LocationActivity.class));
            }
        } );
    }

    private void LoadObjects() {
        AdapterObject adapter = new AdapterObject(this,dbHelper.getAllObjects( Constants.ADDED_DATE + " DESC"));
        objectsRecyclerView.setAdapter(adapter);

        // Sum of Objects
        actionBar.setSubtitle("Total: " + dbHelper.getObjectsCount());
    }

    private void SearchObjects(String query) {
        AdapterObject adapter = new AdapterObject(this,dbHelper.searchObject(query));
        objectsRecyclerView.setAdapter(adapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        LoadObjects();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate Menu
        getMenuInflater().inflate(R.menu.menu_main,menu);

        //MenuItem
        MenuItem item = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) item.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                SearchObjects(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                SearchObjects(newText);
                return true;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item){
        //Handle Item Select
        return super.onOptionsItemSelected(item);
    }
}
