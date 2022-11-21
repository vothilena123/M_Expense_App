package com.vothilena.tripapplication.ui.trip;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.vothilena.tripapplication.R;
import com.vothilena.tripapplication.databse.TripDAO;
import com.vothilena.tripapplication.model.Trip;


public class TripUpdateFragment extends Fragment implements AddTripFragment.FragmentListener {

    public static final String ARG_PARAM_TRIP = "trip";
    protected EditText editTripName, editDestination, editTripDate, editTransportation, editDescription, editRisk, editUpgrade;
    protected Button buttonBu;
    protected TripDAO _db;
    protected LinearLayout tripLinearLayout;

    public TripUpdateFragment(){}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // use fragment_trip_upgrade layout
        View view = inflater.inflate(R.layout.fragment_trip_update, container, false);

        if (getArguments() != null) {
            Trip trip = (Trip) getArguments().getSerializable(ARG_PARAM_TRIP);

            Bundle bundle = new Bundle();
            // move to AddTripFragment to share layout with this fragment
            bundle.putSerializable(AddTripFragment.ARG_PARAM_TRIP, trip);

            getChildFragmentManager().getFragments().get(0).setArguments(bundle);
        }
        return view;
    }

    public void sendFromTripAddFragment(long status) {
        switch ((int) status) {
            case 0:
                //update failed, the message failed
                Toast.makeText(getContext(), R.string.notification_update_fail, Toast.LENGTH_SHORT).show();
                return;

            default:
                //If the update is successful, the message will be successful
                Toast.makeText(getContext(), R.string.notification_update_success, Toast.LENGTH_SHORT).show();
                Navigation.findNavController(getView()).navigateUp();
        }
    }

}