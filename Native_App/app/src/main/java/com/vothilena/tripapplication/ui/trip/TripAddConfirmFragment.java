package com.vothilena.tripapplication.ui.trip;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import com.vothilena.tripapplication.R;
import com.vothilena.tripapplication.databse.TripDAO;
import com.vothilena.tripapplication.model.Trip;


public class TripAddConfirmFragment extends DialogFragment {

    protected TripDAO _db;
    protected Trip _trip;
    protected Button buttonConfirm, buttonCancel;
    protected TextView confirmName, confirmDestination, confirmDate, confirmTransportation, confirmRisk, confirmUpgrade, confirmDescription ;


    public TripAddConfirmFragment() {
        _trip = new Trip();
    }

    public TripAddConfirmFragment(Trip trip) {
        _trip = trip;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        _db = new TripDAO(getContext());
    }

    @Override
    public void onResume() {
        super.onResume();

        ViewGroup.LayoutParams params = getDialog().getWindow().getAttributes();
        params.width = ViewGroup.LayoutParams.MATCH_PARENT;
        getDialog().getWindow().setAttributes((android.view.WindowManager.LayoutParams) params);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Use layout fragment_trip_add_confirm
        View view = inflater.inflate(R.layout.fragment_trip_add_confirm, container, false);
        //create variables and assign them initial values
        String name = "No Information";
        String destination = "No Information";
        String date = "No Information";
        String transportation = "No Information";
        String risk = "No Information";
        String upgrade = "No Information";
        String description = "No Information";
        //declare variables corresponding to the ids of the elements in fragment_add_trip
        buttonConfirm = view.findViewById(R.id.confirmButtonConfirm);
        buttonCancel = view.findViewById(R.id.confirmButtonCancel);
        confirmName = view.findViewById(R.id.confirmTripName);
        confirmDestination = view.findViewById(R.id.confirmTripDestination);
        confirmDate = view.findViewById(R.id.confirmTripDate);
        confirmTransportation = view.findViewById(R.id.confirmTransportation);
        confirmRisk = view.findViewById(R.id.confirmRisk);
        confirmUpgrade = view.findViewById(R.id.confirmUpgrade);
        confirmDescription = view.findViewById(R.id.confirmDescription);

        //The _trip object of the Trip class will call the get functions
        //If they are not null then
        if (_trip.getTrip_name() != null && !_trip.getTrip_name().trim().isEmpty()) {
            //assign the return value in the getTrip_name function to the variable name
            name = _trip.getTrip_name();
        }
        if (_trip.getDestination() != null && !_trip.getDestination().trim().isEmpty()) {
            //assign the return value in the getDestination function to the variable destination
            destination = _trip.getDestination();
        }
        //The remaining variables do the same
        if (_trip.getTrip_date() != null && !_trip.getTrip_date().trim().isEmpty()) {
            date = _trip.getTrip_date();
        }
        if (_trip.getTransportation() != null && !_trip.getTransportation().trim().isEmpty()) {
            transportation = _trip.getTransportation();
        }
        if (_trip.getRisk() != null && !_trip.getRisk().trim().isEmpty()) {
            risk = _trip.getRisk();
        }
        if (_trip.getUpgrade() != null && !_trip.getUpgrade().trim().isEmpty()) {
            upgrade = _trip.getUpgrade();
        }
        if (_trip.getDescription() != null && !_trip.getDescription().trim().isEmpty()) {
            destination = _trip.getDescription();
        }

        //The variables corresponding to the id of the elements in the layout will
        // receive the text from the variables that have got the value from the get
        // functions and set them into the elements with the corresponding id of layout fragment_confirm.
        confirmName.setText(name);
        confirmDestination.setText(destination);
        confirmDate.setText(date);
        confirmTransportation.setText(transportation);
        confirmRisk.setText(risk);
        confirmUpgrade.setText(upgrade);
        confirmDescription.setText(description);
        //Click the cancel button to cancel the confirmation and close the dialog confirm
        buttonCancel.setOnClickListener(v -> dismiss());
        //Click the confirm button to call the confirm() function.
        buttonConfirm.setOnClickListener(v -> confirm());

        return view;
    }

    protected void confirm() {
        //call insert Trip function to add data to the database
        long status = _db.insertTrip(_trip);

        FragmentListener listener = (FragmentListener) getParentFragment();
        //save insert to Fragment
        listener.sendFromTripAddConfirmFragment(status);

        dismiss();
    }

    public interface FragmentListener {
        void sendFromTripAddConfirmFragment(long status);
    }
}