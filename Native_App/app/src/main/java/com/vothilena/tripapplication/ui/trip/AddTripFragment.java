package com.vothilena.tripapplication.ui.trip;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.vothilena.tripapplication.R;
import com.vothilena.tripapplication.databse.TripDAO;
import com.vothilena.tripapplication.model.Trip;
import com.vothilena.tripapplication.ui.dialog.CalendarFragment;


public class AddTripFragment extends Fragment
        implements TripAddConfirmFragment.FragmentListener, CalendarFragment.FragmentListener{

    public static final String ARG_PARAM_TRIP = "trip";
    protected EditText editTripName, editDestination, editTripDate, editTransportation, editDescription, editRisk, editUpgrade;
    protected Button buttonAdd;
    protected TripDAO _db;
    protected LinearLayout tripLinearLayout;

    public AddTripFragment(){}

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        _db = new TripDAO(getContext());
    }

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //use fragment_add_trip layout
        View view = inflater.inflate(R.layout.fragment_add_trip, container, false);
        //declare variables corresponding to the ids of the elements in fragment_add_trip
        editTripName = view.findViewById(R.id.edTripName);
        editDestination = view.findViewById(R.id.edDestination);
        editTripDate = view.findViewById(R.id.edTripDate);
        editTransportation = view.findViewById(R.id.edTransportation);
        editRisk = view.findViewById(R.id.edRisk);
        editUpgrade = view.findViewById(R.id.edUpgrade);
        editDescription = view.findViewById(R.id.edDescription);
        buttonAdd = view.findViewById(R.id.button_add);
        tripLinearLayout = view.findViewById(R.id.linearLayout);
        // Click on the data field to select the date
        editTripDate.setOnTouchListener((v, motionEvent) -> showCalendar(motionEvent));
        // Let the fragment_update_trip layout use the same layout as fragment_add_trip
        if (getArguments() != null) {
            Trip trip = (Trip) getArguments().getSerializable(ARG_PARAM_TRIP);
            //Pass data from trip object of Trip class to
            // variables of elements with id in update layout
            editTripName.setText(trip.getTrip_name());
            editDestination.setText(trip.getDestination());
            editTripDate.setText(trip.getTrip_date());
            editTransportation.setText(trip.getTransportation());
            editRisk.setText(trip.getRisk());
            editUpgrade.setText(trip.getUpgrade());
            editDescription.setText(trip.getDescription());
            //set the text for the add button to Update trip
            buttonAdd.setText(R.string.label_update);
            //Click the add button to call the update function
            buttonAdd.setOnClickListener(v -> update(trip.getTrip_id()));
            return view;
        }
        // Create new trip.
        //set the text for the add button to Add trip
        buttonAdd.setText(R.string.label_add);
        //Click the add button to call the add function
        buttonAdd.setOnClickListener(v -> add());

        return view;
    }

    protected void add() {
        //if isValidForm true then
        if (isValidForm()) {
            //create a trip object of class Trip to call the function getTripFromInput()
            Trip trip = getTripFromInput(-1);
            //call TripAddConfirmFragment to display layout confirm
            new TripAddConfirmFragment(trip).show(getChildFragmentManager(), null);

            return;
        }

    }

    protected void update(long id) {
        //if isValidForm true then
        if (isValidForm()) {
            //create a trip object of class Trip to call the function getTripFromInput()
            Trip trip = getTripFromInput(id);
            //Call the updateTrip function in the database to save the updated value
            long status = _db.updateTrip(trip);

            FragmentListener listener = (FragmentListener) getParentFragment();
            listener.sendFromTripAddFragment(status);

            return;
        }

    }

    protected boolean showCalendar(MotionEvent motionEvent) {
        if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
            new CalendarFragment().show(getChildFragmentManager(), null);
        }

        return false;
    }

    protected Trip getTripFromInput(long id) {
        //create variables that receive input values from the
        // input fields of the layout fragment_add_trip
        String name = editTripName.getText().toString();
        String destination = editDestination.getText().toString();
        String date = editTripDate.getText().toString();
        String transportation = editTransportation.getText().toString();
        String risk = editRisk.getText().toString();
        String upgrade = editUpgrade.getText().toString();
        String description = editDescription.getText().toString();
        //returns variables containing the values of the input fields
        return new Trip(id, name, destination, date, transportation, risk, upgrade, description);
    }

    protected boolean isValidForm(){
        boolean isValid = true;

        String name = editTripName.getText().toString();
        String destination = editDestination.getText().toString();
        String date = editTripDate.getText().toString();
        String transportation = editTransportation.getText().toString();
        String risk = editRisk.getText().toString();

        //if the input fields are in, display error messages that require full input
        if (name == null || name.trim().isEmpty()){

            Toast.makeText(getActivity(),"Please enter trip name", Toast.LENGTH_LONG).show();
            isValid = false;

        }
        else if (destination == null || destination.trim().isEmpty()){
            Toast.makeText(getActivity(),"Please enter destination", Toast.LENGTH_LONG).show();
            isValid = false;

        }
        else if (date == null || date.trim().isEmpty()){
            Toast.makeText(getActivity(),"Please enter trip date", Toast.LENGTH_LONG).show();
            isValid = false;

        }
        else if (transportation == null || transportation.trim().isEmpty()){
            Toast.makeText(getActivity(),"Please enter transportation", Toast.LENGTH_LONG).show();
            isValid = false;

        }
        else if (risk == null || risk.trim().isEmpty()){
            Toast.makeText(getActivity(),"Please choose risk assessment", Toast.LENGTH_LONG).show();
            isValid = false;

        }
        return isValid;
    }


    public void sendFromTripAddConfirmFragment(long status) {
        switch ((int) status) {
            case -1:
                //Display error message when add trip from confirm failed
                Toast.makeText(getContext(),"Failed to add trip", Toast.LENGTH_SHORT).show();
                return;

            default:
                //Show success message
                Toast.makeText(getContext(),"Added successfully", Toast.LENGTH_SHORT).show();
                //input fields will be set to empty text.
                editTripName.setText("");
                editDestination.setText("");
                editTripDate.setText("");
                editTransportation.setText("");
                editRisk.setText("");
                editUpgrade.setText("");
                editDescription.setText("");

                editTripName.requestFocus();
        }
    }


    @Override
    public void sendFromCalendarFragment(String date) {
        editTripDate.setText(date);
    }


    public interface FragmentListener {
        void sendFromTripAddFragment(long status);
    }
}