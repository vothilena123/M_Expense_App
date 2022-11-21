package com.vothilena.tripapplication.databse;

public class ExpenseEntry {
// Declarations for table elements, including table names and column names
    public static final String TABLE_NAME = "expense";
    public static final String COLUMN_ID = "expense_id";
    public static final String COLUMN_TYPE = "expense_type";
    public static final String COLUMN_AMOUNT = "total_amount";
    public static final String COLUMN_TRIP_DATE = "trip_date";
    public static final String COLUMN_TIME = "time";
    public static final String COLUMN_COMMENT = "comment";
    public static final String COLUMN_TRIPID = "tripid";

    //Create table with declared elements and data types
    public static final String SQL_CREATE_TABLE = "CREATE TABLE " + TABLE_NAME +
            " (" + COLUMN_ID + " INTEGER PRIMARY KEY, " +
            COLUMN_TYPE + " TEXT NOT NULL, " +
            COLUMN_AMOUNT + " INTEGER NOT NULL, " +
            COLUMN_TRIP_DATE + " TEXT NOT NULL, " +
            COLUMN_TIME + " TEXT NOT NULL, " +
            COLUMN_COMMENT + " TEXT, " +
            COLUMN_TRIPID + " INTEGER NOT NULL, " +
            "FOREIGN KEY("+ COLUMN_TRIPID+ ") " +
            "REFERENCES " + TripEntry.TABLE_NAME + "(" + TripEntry.COLUMN_ID + "))";

    //Declare variable with condition to delete table
    public static final String SQL_DELETE_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;
}
