package com.vothilena.tripapplication.databse;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import androidx.annotation.Nullable;

public class MyDatabaseHelper extends SQLiteOpenHelper{
//    Declare the database name
    public static final String DATABASE_NAME = "TripExpenseApp";
//    Declare the database version
    public static final int DATABASE_VERSION = 1;
    public MyDatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
//      Create Trip and Expense tables with SQL_CREATE_TABLE variable
//      containing information to create 2 tables
        sqLiteDatabase.execSQL(TripEntry.SQL_CREATE_TABLE);
        sqLiteDatabase.execSQL(ExpenseEntry.SQL_CREATE_TABLE);
    }
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
//        call SQL_DELETE_TABLE to delete two tables
        sqLiteDatabase.execSQL(TripEntry.SQL_DELETE_TABLE);
        sqLiteDatabase.execSQL(ExpenseEntry.SQL_DELETE_TABLE);
        onCreate(sqLiteDatabase);
    }
}
