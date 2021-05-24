package com.example.spatialobjects;

public class Constants {

    //DB Name
    public static final String DB_NAME = "SpatialObjects_DB";
    //DB Version
    public static final int DB_VERSION = 2;
    //DB Table
    public static final String TABLE_NAME = "Object";
    //DB Fields
    public static final String ID = "ID";
    public static final String DESIGNATION = "DESIGNATION";
    public static final String IMAGE = "IMAGE";
    public static final String LONGITUDE = "LONGITUDE";
    public static final String LATITUDE = "LATITUDE";
    public static final String ALTITUDE = "ALTITUDE";
    public static final String DESCRIPTION = "DESCRIPTION";
    public static final String ADDED_DATE = "ADDED_DATE";
    public static final String UPDATED_DATE = "UPDATED_DATE";

    // CREATE Table Query
    public static final String CREATE_TABLE_QUERY = "CREATE TABLE " + TABLE_NAME + " (" +
            ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "+
            DESIGNATION + " TEXT, " +
            IMAGE + " TEXT, " +
            LONGITUDE + " TEXT, " +
            LATITUDE + " TEXT, " +
            ALTITUDE + " TEXT, " +
            DESCRIPTION + " TEXT, " +
            ADDED_DATE + " TEXT, " +
            UPDATED_DATE + " TEXT "+
            ")"
            ;

}
