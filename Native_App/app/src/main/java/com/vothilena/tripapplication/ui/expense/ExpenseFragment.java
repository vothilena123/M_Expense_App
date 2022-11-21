package com.vothilena.tripapplication.ui.expense;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.vothilena.tripapplication.R;
import com.vothilena.tripapplication.databinding.ActivityMainBinding;
import com.vothilena.tripapplication.databse.TripDAO;
import com.vothilena.tripapplication.model.Expense;
import com.vothilena.tripapplication.model.Trip;
import com.vothilena.tripapplication.ui.trip.TripUpdateFragment;

public class ExpenseFragment extends Fragment {
    public static final String ARG_PARAM_TRIP_ID = "tripID";
    protected ArrayList<Expense> _expenseList = new ArrayList<>();

    protected TripDAO _db;
    protected Trip _trip;
    protected RecyclerView expenseListRecyclerView;
    protected ImageView empty_box;
    protected TextView no_data_input;


    public ExpenseFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        _db = new TripDAO(context);
    }


    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // use layout fragment_expense
        View view = inflater.inflate(R.layout.fragment_expense, container, false);

        if (getArguments() != null) {
            Expense expense = new Expense();
            //expense object will get trip id from Trip table
            expense.setTrip_ID(getArguments().getLong(ARG_PARAM_TRIP_ID));
            //get the expense list from the database then assign it to the final list _expenseList
            _expenseList = _db.getExpenseList(expense, null, false);
        }
        //declare variables corresponding to the ids of the elements in fragment_expense
        expenseListRecyclerView = view.findViewById(R.id.expenseListRecylerView);
        empty_box = view.findViewById(R.id.empty_box);
        no_data_input = view.findViewById(R.id.no_data_input);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getContext(), linearLayoutManager.getOrientation());

        expenseListRecyclerView.addItemDecoration(dividerItemDecoration);
        //expenseAdapter with trip list will be loaded into recyclerView of expense
        expenseListRecyclerView.setAdapter(new ExpenseAdapter(_expenseList));
        expenseListRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        // If the trip list is empty, the empty_box image will be displayed, otherwise it will be hidden
        empty_box.setVisibility(_expenseList.isEmpty() ? View.VISIBLE : View.GONE);
        // If the trip list is empty, the no_data_input message will be displayed, otherwise it will be hidden
        no_data_input.setVisibility(_expenseList.isEmpty() ? View.VISIBLE : View.GONE);

        //showExpenseList();
        return view;
    }



}