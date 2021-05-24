package com.example.spatialobjects;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;

// ALL CRUD METHODS INSERT UPDATE DELETE READ
public class DbHelper extends SQLiteOpenHelper {


    public DbHelper(@Nullable Context context) {
        super(context, Constants.DB_NAME,null,Constants.DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //Create Table on SPATIAL Objects DB
        db.execSQL(Constants.CREATE_TABLE_QUERY);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //UPgrade Table on SPATIAL Objects DB if the version changes
        // First Drop table if exists already
        db.execSQL("DROP TABLE IF EXISTS " + Constants.TABLE_NAME);
        // Create New Version of The Table
        onCreate(db);
    }

    // CRUD METHODS ------------------------------------------------------------------------------
    public long insertObject(String designation,String image,String longitude,String latitude,String altitude,
                             String description,String added_date,String updated_date) {

        // GET WRITABLE DATA BASE because we want to insert data (write data like files)
        SQLiteDatabase db = this.getWritableDatabase();

        // VALUES TO BE INSERTED
        ContentValues values = new ContentValues();
        values.put(Constants.DESIGNATION,designation);
        values.put(Constants.IMAGE,image);
        values.put(Constants.LONGITUDE,longitude);
        values.put(Constants.LATITUDE,latitude);
        values.put(Constants.ALTITUDE,altitude);
        values.put(Constants.DESCRIPTION,description);
        values.put(Constants.ADDED_DATE,added_date);
        values.put(Constants.UPDATED_DATE,updated_date);

        // INSERT THE OBJECT of the values ABOVE
        long insertedID = db.insert(Constants.TABLE_NAME,null,values);

        //CLOSE DATABASE CONNECTION
        db.close();

        return insertedID;
    }

    public void updateObject(String id,String designation,String image,String longitude,String latitude,String altitude,
                             String description,String added_date,String updated_date) {

        // GET WRITABLE DATA BASE because we want to insert data (write data like files)
        SQLiteDatabase db = this.getWritableDatabase();

        // VALUES TO BE Updated
        ContentValues values = new ContentValues();
        values.put(Constants.DESIGNATION,designation);
        values.put(Constants.IMAGE,image);
        values.put(Constants.LONGITUDE,longitude);
        values.put(Constants.LATITUDE,latitude);
        values.put(Constants.ALTITUDE,altitude);
        values.put(Constants.DESCRIPTION,description);
        values.put(Constants.ADDED_DATE,added_date);
        values.put(Constants.UPDATED_DATE,updated_date);

        // Update THE OBJECT of the values ABOVE
        db.update(Constants.TABLE_NAME,values, Constants.ID + " = ?",new String[] {id});

        //CLOSE DATABASE CONNECTION
        db.close();
    }

    //get all data
    public ArrayList<Object> getAllObjects(String orderBy) {
        ArrayList<Object> objectsList = new ArrayList<>();

        // QUERY to SELECT Records
        String selectQuery = "SELECT * FROM " + Constants.TABLE_NAME + " ORDER BY " + orderBy;

        SQLiteDatabase db = this.getWritableDatabase();
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

                // ADD OBJECT to list
                objectsList.add(obj);
            }
            while(cursor.moveToNext());
        }

        db.close();

        return objectsList;
    }

    //search data
    public ArrayList<Object> searchObject(String query) {
        ArrayList<Object> objectsList = new ArrayList<>();

        // QUERY to SELECT Records
        String selectQuery = "SELECT * FROM " + Constants.TABLE_NAME + " WHERE " + Constants.DESIGNATION + " LIKE '%" +  query + "%'";

        SQLiteDatabase db = this.getWritableDatabase();
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

                // ADD OBJECT to list
                objectsList.add(obj);
            }
            while(cursor.moveToNext());
        }

        db.close();

        return objectsList;
    }

    public void deleteObject(String id) {
        SQLiteDatabase db = getWritableDatabase();
        db.delete(Constants.TABLE_NAME, Constants.ID + "=?",new String[]{id});
        db.close();
    }

    //get number of Objects
    public int getObjectsCount() {
        String query = "SELECT * FROM " + Constants.TABLE_NAME;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query,null);

        int count = cursor.getCount();

        db.close();

        return count;
    }
}
