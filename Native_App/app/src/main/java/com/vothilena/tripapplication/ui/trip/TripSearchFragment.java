package com.vothilena.tripapplication.ui.trip;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.vothilena.tripapplication.R;
import com.vothilena.tripapplication.model.Trip;
import com.vothilena.tripapplication.ui.dialog.CalendarFragment;


public class TripSearchFragment extends DialogFragment implements CalendarFragment.FragmentListener  {

    protected EditText searchDate, searchName;
    protected Button searchButtonCancel, searchButtonSearch;

    public TripSearchFragment() {}

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
        //use fragment_trip_search layout
        View view = inflater.inflate(R.layout.fragment_trip_search, container, false);
        //declare variables corresponding to the ids of the elements in fragment_trip_search
        searchDate = view.findViewById(R.id.searchDate);
        searchName = view.findViewById(R.id.searchName);
        searchButtonCancel = view.findViewById(R.id.searchButtonCancel);
        searchButtonSearch = view.findViewById(R.id.searchButtonSearch);
        // click button search to call search() function
        searchButtonSearch.setOnClickListener(v -> search());
        // click button cancel to end searching and close search dialog
        searchButtonCancel.setOnClickListener(v -> dismiss());
        // click date field to show calendar
        searchDate.setOnTouchListener((v, motionEvent) -> showCalendar(motionEvent));

        return view;
    }

    protected void search() {
        Trip trip = new Trip();
        //declare date and time variables to
        // receive input values from edit text fields
        String date = searchDate.getText().toString();
        String name = searchName.getText().toString();

        if (date != null && !date.trim().isEmpty())
            //set trip date equals the value of the variable date
            trip.setTrip_date(date);

        if (name != null && !name.trim().isEmpty())
            //set trip name equals the value of the variable name
            trip.setTrip_name(name);

        FragmentListener listener = (FragmentListener) getParentFragment();
        //listenser call senFromTripSearchFragement() to reload triplist adter searching
        listener.sendFromTripSearchFragment(trip);

        dismiss();
    }

    protected boolean showCalendar(MotionEvent motionEvent) {
        if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
            new CalendarFragment().show(getChildFragmentManager(), null);
        }

        return false;
    }

    @Override
    public void sendFromCalendarFragment(String date) {
        searchDate.setText(date);
    }

    public interface FragmentListener {
        void sendFromTripSearchFragment(Trip trip);
    }


}