package com.vothilena.tripapplication.model;

import java.io.Serializable;

public class Trip implements Serializable{
    protected long trip_id;
    protected String trip_name;
    protected String destination;
    protected String trip_date;
    protected String transportation;
    protected String risk;
    protected String upgrade;
    protected String description;

//    Constructor Trip with no parameters
    public Trip() {
        this.trip_id = -1;
        this.trip_name = null;
        this.destination = null;
        this.trip_date = null;
        this.transportation = null;
        this.risk = null;
        this.upgrade = null;
        this.description = null;
    }
//    Constructor Trip with parameters
    public Trip(long trip_id, String trip_name, String destination, String trip_date, String transportation, String risk, String upgrade, String description) {
        this.trip_id = trip_id;
        this.trip_name = trip_name;
        this.destination = destination;
        this.trip_date = trip_date;
        this.transportation = transportation;
        this.risk = risk;
        this.upgrade = upgrade;
        this.description = description;
    }

    public long getTrip_id() {
        return trip_id;
    }

    public void setTrip_id(long trip_id) {
        this.trip_id = trip_id;
    }

    public String getTrip_name() {
        return trip_name;
    }

    public void setTrip_name(String trip_name) {
        this.trip_name = trip_name;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getTrip_date() {
        return trip_date;
    }

    public void setTrip_date(String trip_date) {
        this.trip_date = trip_date;
    }

    public String getTransportation() {
        return transportation;
    }

    public void setTransportation(String transportation) {
        this.transportation = transportation;
    }

    public String getRisk() {
        return risk;
    }

    public void setRisk(String risk) {
        this.risk = risk;
    }

    public String getUpgrade() {
        return upgrade;
    }

    public void setUpgrade(String upgrade) {
        this.upgrade = upgrade;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
//
    public boolean isEmpty() {
//        variable of type long = -1, and all variables of type String = null then this function is executed
        if (-1 == trip_id && null == trip_name && null == destination && null == trip_date && null == transportation &&  null == risk && null == upgrade && null == description)
            return true;

        return false;
    }

    @Override
    public String toString() {
        return "[" + trip_date + "] " + trip_name;
    }
}
