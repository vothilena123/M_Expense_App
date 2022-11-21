package com.vothilena.tripapplication.databse;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.vothilena.tripapplication.model.Expense;
import com.vothilena.tripapplication.model.Trip;

import java.util.ArrayList;

public class TripDAO {
    protected MyDatabaseHelper databaseHelper;
    protected SQLiteDatabase dbWrite, dbRead;

    public TripDAO(Context context){
//Declare databaseHelper object to manipulate functions in class MyDatabaseHelper
        databaseHelper = new MyDatabaseHelper(context);
//Create two variables to allow reading and copying data in the database
        dbRead = databaseHelper.getReadableDatabase();
        dbWrite = databaseHelper.getWritableDatabase();
    }

    public void close(){
        dbRead.close();
        dbWrite.close();
    }

    public void reset() {
        databaseHelper.onUpgrade(dbWrite, 0, 0);
    }
    //Allow adding trip to database
    public long insertTrip(Trip trip) {
        //get user's data and import it into Trip table
        ContentValues values = getTripValues(trip);
        return dbWrite.insert(TripEntry.TABLE_NAME, null, values);
    }

    public ArrayList<Trip> getTripList(Trip trip, String orderByColumn, boolean isDesc) {
        // Each added trip will be sorted by columns in descending order
        String orderBy = getOrderByString(orderByColumn, isDesc);

        String selection = null;
        String[] selectionArgs = null;
        //if the input trip data is not null then
        if (null != trip) {
            selection = "";
            ArrayList<String> conditionList = new ArrayList<String>();
            //If the trip name has already been entered
            if (trip.getTrip_name() != null && !trip.getTrip_name().trim().isEmpty()) {
                //name will be added corresponding to arraylist
                selection += " AND " + TripEntry.COLUMN_TRIP_NAME + " LIKE ?";
                conditionList.add("%" + trip.getTrip_name() + "%");
            }
            //If the trip destination has already been entered
            if (trip.getDestination() != null && !trip.getDestination().trim().isEmpty()) {
                //destination will be added corresponding to arraylist
                selection += " AND " + TripEntry.COLUMN_DESTINATION + " = ?";
                conditionList.add(trip.getDestination());
            }
            //If the trip date has already been entered
            if (trip.getTrip_date() != null && !trip.getTrip_date().trim().isEmpty()) {
                //date will be added corresponding to arraylist
                selection += " AND " + TripEntry.COLUMN_TRIP_DATE + " = ?";
                conditionList.add(trip.getTrip_date());
            }
            //If the trip transportation has already been entered
            if (trip.getTransportation() != null && !trip.getTransportation().trim().isEmpty()) {
                //transportation will be added corresponding toarraylist
                selection += " AND " + TripEntry.COLUMN_TRANSPORTATION + " = ?";
                conditionList.add(trip.getTransportation());
            }
            //If the trip risk has already been entered
            if (trip.getRisk() != null && !trip.getRisk().trim().isEmpty()) {
                //risk will be added corresponding to arraylist
                selection += " AND " + TripEntry.COLUMN_RISK + " = ?";
                conditionList.add(trip.getRisk());
            }
            //If the trip upgrade has already been entered
            if (trip.getUpgrade() != null && !trip.getUpgrade().trim().isEmpty()) {
                //upgrade will be added corresponding to arraylist
                selection += " AND " + TripEntry.COLUMN_UPGRADE + " = ?";
                conditionList.add(trip.getUpgrade());
            }
            //If the trip description has already been entered
            if (trip.getDescription() != null && !trip.getDescription().trim().isEmpty()) {
                //description will be added corresponding to column description
                selection += " AND " + TripEntry.COLUMN_DESCRIPTION + " = ?";
                conditionList.add(trip.getDescription());
            }


            if (!selection.trim().isEmpty()) {
                selection = selection.substring(5);
            }

            selectionArgs = conditionList.toArray(new String[conditionList.size()]);
        }

        return getTripFromDB(null, selection, selectionArgs, null, null, orderBy);
    }

    public Trip getTripById(long id) {
        //get all trip information through ID
        String selection = TripEntry.COLUMN_ID + " = ?";
        String[] selectionArgs = {String.valueOf(id)};
        //returns getTripFromDB function with no column in it
        return getTripFromDB(null, selection, selectionArgs, null, null, null).get(0);
    }


    public long deleteTrip(long id) {
        //If the id of the trip in the
        // database = the id of the trip selected for deletion
        String selection = TripEntry.COLUMN_ID + " = ?";
        String[] selectionArgs = {String.valueOf(id)};
        //remove trip from the Trip board
        return dbWrite.delete(TripEntry.TABLE_NAME, selection, selectionArgs);
    }

    protected String getOrderByString(String orderByColumn, boolean isDesc) {
        if (orderByColumn == null || orderByColumn.trim().isEmpty())
            return null;

        if (isDesc)
            return orderByColumn.trim() + " DESC";

        return orderByColumn.trim();
    }

    protected ContentValues getTripValues(Trip resident) {
        ContentValues values = new ContentValues();
        //Pass the entered values into the corresponding columns in the database
        values.put(TripEntry.COLUMN_TRIP_NAME, resident.getTrip_name());
        values.put(TripEntry.COLUMN_DESTINATION, resident.getDestination());
        values.put(TripEntry.COLUMN_TRIP_DATE, resident.getTrip_date());
        values.put(TripEntry.COLUMN_TRANSPORTATION, resident.getTransportation());
        values.put(TripEntry.COLUMN_RISK, resident.getRisk());
        values.put(TripEntry.COLUMN_UPGRADE, resident.getUpgrade());
        values.put(TripEntry.COLUMN_DESCRIPTION, resident.getDescription());

        return values;
    }

    protected ArrayList<Trip> getTripFromDB(String[] columns, String selection, String[] selectionArgs, String groupBy, String having, String orderBy) {
        //create an arraylist list to store trips as a list
        ArrayList<Trip> list = new ArrayList<>();
        //the cursor variable will read the values in the Trip table
        Cursor cursor = dbRead.query(TripEntry.TABLE_NAME, columns, selection, selectionArgs, groupBy, having, orderBy);

        while (cursor.moveToNext()) {
            Trip trip = new Trip();
            //trip object gets trip information retrieved by cursor from database
            trip.setTrip_id(cursor.getLong(cursor.getColumnIndexOrThrow(TripEntry.COLUMN_ID)));
            trip.setTrip_name(cursor.getString(cursor.getColumnIndexOrThrow(TripEntry.COLUMN_TRIP_NAME)));
            trip.setDestination(cursor.getString(cursor.getColumnIndexOrThrow(TripEntry.COLUMN_DESTINATION)));
            trip.setTrip_date(cursor.getString(cursor.getColumnIndexOrThrow(TripEntry.COLUMN_TRIP_DATE)));
            trip.setTransportation(cursor.getString(cursor.getColumnIndexOrThrow(TripEntry.COLUMN_TRANSPORTATION)));
            trip.setRisk(cursor.getString(cursor.getColumnIndexOrThrow(TripEntry.COLUMN_RISK)));
            trip.setUpgrade(cursor.getString(cursor.getColumnIndexOrThrow(TripEntry.COLUMN_UPGRADE)));
            trip.setDescription(cursor.getString(cursor.getColumnIndexOrThrow(TripEntry.COLUMN_DESCRIPTION)));
            //trip object field to store in the array list list
            list.add(trip);
        }

        cursor.close();

        return list;
    }

    // Expense

    public long insertExpense(Expense expense) {
        ContentValues values = getExpenseValues(expense);

        return dbWrite.insert(ExpenseEntry.TABLE_NAME, null, values);
    }

    public ArrayList<Expense> getExpenseList(Expense expense, String orderByColumn, boolean isDesc) {
        String orderBy = getOrderByString(orderByColumn, isDesc);

        String selection = null;
        String[] selectionArgs = null;
        //if the input expense data is not null then
        if (expense != null) {
            selection = "";
            ArrayList<String> conditionList = new ArrayList<String>();
            //If the type has already been entered
            if (expense.getType() != null && !expense.getType().trim().isEmpty()) {
                selection += " AND " + ExpenseEntry.COLUMN_TYPE + " LIKE ?";
                //type will be added corresponding to arraylist
                conditionList.add("%" + expense.getType() + "%");
            }
            //If the amount has already been entered
            if (expense.getAmount() != -1) {
                selection += " AND " + ExpenseEntry.COLUMN_AMOUNT + " = ?";
                //amount will be added corresponding to arraylist
                conditionList.add(String.valueOf(expense.getAmount()));
            }
            //If the date has already been entered
            if (expense.getTrip_date() != null && !expense.getTrip_date().trim().isEmpty()) {
                selection += " AND " + ExpenseEntry.COLUMN_TRIP_DATE + " = ?";
                //date will be added corresponding to arraylist
                conditionList.add(expense.getTrip_date());
            }
            //If the time has already been entered
            if (expense.getTrip_time() != null && !expense.getType().trim().isEmpty()) {
                selection += " AND " + ExpenseEntry.COLUMN_TIME + " = ?";
                //time will be added corresponding to arraylist
                conditionList.add(expense.getType());
            }
            //If the comment has already been entered
            if (expense.getComment() != null && !expense.getComment().trim().isEmpty()) {
                selection += " AND " + ExpenseEntry.COLUMN_COMMENT + " = ?";
                //comment will be added corresponding to arraylist
                conditionList.add(expense.getComment());
            }
            //If the trip id has already been entered
            if (expense.getTrip_ID() != -1) {
                selection += " AND " + ExpenseEntry.COLUMN_TRIPID + " = ?";
                //trip id will be added corresponding to arraylist
                conditionList.add(String.valueOf(expense.getTrip_ID()));
            }

            if (!selection.trim().isEmpty()) {
                selection = selection.substring(5);
            }
            //convert arraylist to array and store it in string selecionArgs
            selectionArgs = conditionList.toArray(new String[conditionList.size()]);
        }
        //return function get Expense FormData with no column in, but selection and selectionArgs
        return getExpenseFromDB(null, selection, selectionArgs, null, null, orderBy);
    }

    public Expense getExpenseById(long id) {
        String selection = ExpenseEntry.COLUMN_ID + " = ?";
        String[] selectionArgs = {String.valueOf(id)};

        return getExpenseFromDB(null, selection, selectionArgs, null, null, null).get(0);
    }

    public long updateTrip(Trip trip) {
        ContentValues values = getTripValues(trip);
        //get trip information edited by trip id
        String selection = TripEntry.COLUMN_ID + " = ?";
        String[] selectionArgs = {String.valueOf(trip.getTrip_id())};
        //Rewrite the edited information to the database
        return dbWrite.update(TripEntry.TABLE_NAME, values, selection, selectionArgs);
    }

    public long deleteExpense(long id) {
        String selection = ExpenseEntry.COLUMN_ID + " = ?";
        String[] selectionArgs = {String.valueOf(id)};

        return dbWrite.delete(ExpenseEntry.TABLE_NAME, selection, selectionArgs);
    }
    //get data into expense table
    protected ContentValues getExpenseValues(Expense expense) {
        ContentValues values = new ContentValues();
        //save expense information to each column in expense table
        values.put(ExpenseEntry.COLUMN_TYPE, expense.getType());
        values.put(ExpenseEntry.COLUMN_AMOUNT, expense.getAmount());
        values.put(ExpenseEntry.COLUMN_TRIP_DATE, expense.getTrip_date());
        values.put(ExpenseEntry.COLUMN_TIME, expense.getTrip_time());
        values.put(ExpenseEntry.COLUMN_COMMENT, expense.getComment());
        values.put(ExpenseEntry.COLUMN_TRIPID, expense.getTrip_ID());

        return values;
    }
    //get expense data from database
    protected ArrayList<Expense> getExpenseFromDB(String[] columns, String selection, String[] selectionArgs, String groupBy, String having, String orderBy) {
        //make the list object an array list
        ArrayList<Expense> list = new ArrayList<>();
        //cursor variable will read data in expense table
        Cursor cursor = dbRead.query(ExpenseEntry.TABLE_NAME, columns, selection, selectionArgs, groupBy, having, orderBy);

        while (cursor.moveToNext()) {
            //Create expense object of class Expenses
            Expense expense = new Expense();
            //expense object gets expense information retrieved by cursor from database
            expense.setExpense_id(cursor.getLong(cursor.getColumnIndexOrThrow(ExpenseEntry.COLUMN_ID)));
            expense.setType(cursor.getString(cursor.getColumnIndexOrThrow(ExpenseEntry.COLUMN_TYPE)));
            expense.setAmount(cursor.getLong(cursor.getColumnIndexOrThrow(ExpenseEntry.COLUMN_AMOUNT)));
            expense.setTrip_date(cursor.getString(cursor.getColumnIndexOrThrow(ExpenseEntry.COLUMN_TRIP_DATE)));
            expense.setTrip_time(cursor.getString(cursor.getColumnIndexOrThrow(ExpenseEntry.COLUMN_TIME)));
            expense.setComment(cursor.getString(cursor.getColumnIndexOrThrow(ExpenseEntry.COLUMN_COMMENT)));
            expense.setTrip_ID(cursor.getLong(cursor.getColumnIndexOrThrow(ExpenseEntry.COLUMN_TRIPID)));

            //expense object field to store in the array list list
            list.add(expense);
        }

        cursor.close();

        return list;
    }


}
