package com.vothilena.tripapplication.ui.trip;

import static java.util.Locale.filter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.vothilena.tripapplication.R;
import com.vothilena.tripapplication.databse.TripDAO;
import com.vothilena.tripapplication.model.Trip;

import java.util.ArrayList;

public class TripFragment extends Fragment implements TripSearchFragment.FragmentListener {

    ImageView empty_box;
    TextView no_data_input;
    RecyclerView recyclerViewTrip;
    TripDAO tripDao;
    EditText searchField;
    TripAdapter tripAdapter;
    ImageButton buttonSearch, buttonResetSearch;
    ArrayList<Trip> tripList = new ArrayList<>();


    public TripFragment(){}

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        tripDao = new TripDAO(context);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        //Use layout fragment_trip
        View view = inflater.inflate(R.layout.fragment_trip, container, false);
        //declare variables corresponding to the ids of the elements in fragment_trip
        recyclerViewTrip = view.findViewById(R.id.recyclerView);
        empty_box = view.findViewById(R.id.empty_box);
        no_data_input = view.findViewById(R.id.no_data_input);
        searchField = view.findViewById(R.id.searchField);
        //when entering a name in the search field will
        // call filter() function to filter trip in the list
        searchField.addTextChangedListener(filter());
        buttonSearch = view.findViewById(R.id.searchIcon);
        //Click the search button to call TripSearchFragment
        buttonSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new TripSearchFragment().show(getChildFragmentManager(), null);
            }
        });
        buttonResetSearch = view.findViewById(R.id.resetSearch);
        //Click the reset button to call the method reloadList
        buttonResetSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchField.setText("");
                reloadList(null);
            }
        });


        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getContext(), linearLayoutManager.getOrientation());
        //create trip Adapter object to load trip list into fragment_trip layout
        tripAdapter = new TripAdapter(tripList);
        recyclerViewTrip.addItemDecoration(dividerItemDecoration);
        //tripAdapter with trip list will be loaded into recyclerViewTrip
        recyclerViewTrip.setAdapter(tripAdapter);
        recyclerViewTrip.setLayoutManager(new LinearLayoutManager(getContext()));

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        //call reloadList() function
        reloadList(null);
    }

    protected void reloadList(Trip trip) {
        //tripList retrieves updated information stored in TripDAO
        tripList = tripDao.getTripList(trip, null, false);
        //tripAdapter reloads updated information via updateList(tripList) function into recyclerView
        tripAdapter.updateList(tripList);

        // If the trip list is empty, the empty_box image will be displayed, otherwise it will be hidden
        empty_box.setVisibility(tripList.isEmpty() ? View.VISIBLE : View.GONE);
        // If the trip list is empty, the no_data_input message will be displayed, otherwise it will be hidden
        no_data_input.setVisibility(tripList.isEmpty() ? View.VISIBLE : View.GONE);
    }

    private TextWatcher filter() {
        return new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {}

            @Override
            //filter the list of trip stored in the tripAdapter object
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                tripAdapter.getFilter().filter(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        };
    }

    @Override
    public void sendFromTripSearchFragment(Trip trip) {
        //if the search result is present then
        if (!trip.isEmpty()) {
            //call reloadList() function with trip
            // parameter to get display the trip corresponds to the results found
            reloadList(trip);
            return;
        }

        reloadList(null);
    }



}