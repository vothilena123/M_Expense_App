package com.vothilena.tripapplication.databse;

public class TripEntry {
// Declarations for table elements, including table names and column names
    public static final String TABLE_NAME = "trips_list";
    public static final String COLUMN_ID = "trip_id";
    public static final String COLUMN_TRIP_NAME = "trip_name";
    public static final String COLUMN_DESTINATION = "destination";
    public static final String COLUMN_TRIP_DATE = "trip_date";
    public static final String COLUMN_TRANSPORTATION = "transportation";
    public static final String COLUMN_RISK = "risk_assessment";
    public static final String COLUMN_UPGRADE = "upgrade";
    public static final String COLUMN_DESCRIPTION = "description";

//Create table with declared elements and data types
    public static final String SQL_CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME +
            " (" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
            COLUMN_TRIP_NAME + " TEXT NOT NULL, " +
            COLUMN_DESTINATION + " TEXT NOT NULL, " +
            COLUMN_TRIP_DATE + " TEXT NOT NULL, " +
            COLUMN_TRANSPORTATION + " TEXT NOT NULL, " +
            COLUMN_RISK + " TEXT NOT NULL, " +
            COLUMN_UPGRADE + " TEXT, " +
            COLUMN_DESCRIPTION + " TEXT);";
//Declare variable with condition to delete table
    public static final String SQL_DELETE_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;
}
