package com.vothilena.tripapplication.model;
import java.io.Serializable;

public class Expense implements Serializable{
    protected long expense_id;
    protected String type;
    protected long amount;
    protected String trip_date;
    protected String trip_time;
    protected String comment;
    protected long trip_ID;

    //    Constructor Expense with no parameters
    public Expense() {
        this.expense_id = -1;
        this.type = null;
        this.amount = -1;
        this.trip_date = null;
        this.trip_time = null;
        this.comment = null;
        this.trip_ID = -1;
    }
//    Constructor Trip with parameters
    public Expense(long expense_id, String type, long amount, String trip_date, String trip_time, String comment, long trip_ID) {
        this.expense_id = expense_id;
        this.type = type;
        this.amount = amount;
        this.trip_date = trip_date;
        this.trip_time = trip_time;
        this.comment = comment;
        this.trip_ID = trip_ID;
    }

    public long getExpense_id() {
        return expense_id;
    }

    public void setExpense_id(long expense_id) {
        this.expense_id = expense_id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public long getAmount() {
        return amount;
    }

    public void setAmount(long amount) {
        this.amount = amount;
    }

    public String getTrip_date() {
        return trip_date;
    }

    public void setTrip_date(String trip_date) {
        this.trip_date = trip_date;
    }

    public String getTrip_time() {
        return trip_time;
    }

    public void setTrip_time(String trip_time) {
        this.trip_time = trip_time;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public long getTrip_ID() {
        return trip_ID;
    }

    public void setTrip_ID(long trip_ID) {
        this.trip_ID = trip_ID;
    }
}
