package com.vothilena.tripapplication.ui.trip;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentContainerView;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.vothilena.tripapplication.R;
import com.vothilena.tripapplication.databse.TripDAO;
import com.vothilena.tripapplication.model.Expense;
import com.vothilena.tripapplication.ui.expense.ExpenseAddFragment;
import com.vothilena.tripapplication.model.Trip;
import com.vothilena.tripapplication.ui.dialog.DeleteConfirmFragment;
import com.vothilena.tripapplication.ui.expense.ExpenseFragment;

public class TripDetailFragment extends Fragment
        implements DeleteConfirmFragment.FragmentListener, ExpenseAddFragment.FragmentListener{

    public static final String ARG_PARAM_TRIP = "trip";

    protected TripDAO _db;
    protected Trip _trip;
    protected Button tripExpense, updateTrip, deleteTrip;
    protected FragmentContainerView expenseList;
    protected TextView detailName, detailDestination, detailDate, detailTransportation, detailRisk, detailUpgrade, detailDescription;

    public TripDetailFragment() {}

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        _db = new TripDAO(getContext());
    }

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_trip_detail, container, false);
        //declare variables corresponding to the ids of the elements in fragment_trip_detail
        detailName = view.findViewById(R.id.detailTripName);
        detailDestination = view.findViewById(R.id.detailTripDestination);
        detailDate = view.findViewById(R.id.detailTripDate);
        detailTransportation = view.findViewById(R.id.detailTransportation);
        detailRisk = view.findViewById(R.id.detailRisk);
        detailUpgrade = view.findViewById(R.id.detailUpgrade);
        detailDescription = view.findViewById(R.id.detailDescription);
        updateTrip = view.findViewById(R.id.updateTripButton);
        deleteTrip = view.findViewById(R.id.deleteTripButton);
        tripExpense = view.findViewById(R.id.tripExpenseButton);
        expenseList = view.findViewById(R.id.expenseList) ;

        //click updateTrip button to call showUpdateFragment() function
        updateTrip.setOnClickListener(v -> showUpdateFragment());
        //click deleteTrip button to call showDeleteFragment() function
        deleteTrip.setOnClickListener(v -> showDeleteConfirmFragment());
        //click tripExpense button to call showTripExpenseFragment() function
        tripExpense.setOnClickListener(v -> showAddExpenseFragment());
        //call showDetail() function
        showDetails();
        showExpenseList();

        return view;
    }

    protected void showDetails() {
        //Declare variables and assign initial values to them
        String name = "Not Found";
        String destination = "Not Found";
        String date = "Not Found";
        String transportation = "Not Found";
        String risk = "Not Found";
        String upgrade = "Not Found";
        String description = "Not Found";

        if (getArguments() != null) {
            _trip = (Trip) getArguments().getSerializable(ARG_PARAM_TRIP);
            //Retrieve data from Database by trip id
            _trip = _db.getTripById(_trip.getTrip_id());
            //Assign to variables values obtained from the database
            name = _trip.getTrip_name();
            destination = _trip.getDestination();
            date = _trip.getTrip_date();
            transportation = _trip.getTransportation();
            risk = _trip.getRisk();
            upgrade = _trip.getUpgrade();
            description = _trip.getDescription();
        }
        //set text for variables representing the text views
        // in the layout using the values obtained from the database
        detailName.setText(name);
        detailDestination.setText(destination);
        detailDate.setText(date);
        detailTransportation.setText(transportation);
        detailRisk.setText(risk);
        detailUpgrade.setText(upgrade);
        detailDescription.setText(description);
    }

    protected void showUpdateFragment() {
        Bundle bundle = null;

        if (_trip != null) {
            bundle = new Bundle();
            bundle.putSerializable(TripUpdateFragment.ARG_PARAM_TRIP, _trip);
        }
        //call the function to show the layout tripUpdateFragmemt
        Navigation.findNavController(getView()).navigate(R.id.tripUpdateFragment, bundle);
    }

    protected void showDeleteConfirmFragment() {
        //call Delete Confirm Fragment to show delete dialog
        new DeleteConfirmFragment(getString(R.string.notification_delete_confirm)).show(getChildFragmentManager(), null);
    }

    protected void showAddExpenseFragment() {
        new ExpenseAddFragment(_trip.getTrip_id()).show(getChildFragmentManager(), null);


    }

    @Override
    public void sendFromDeleteConfirmFragment(int status) {
        if (status == 1 && _trip != null) {
            //variable numOfDeletaRows get value from
            // database call delete function to delete trip by id
            long numOfDeletedRows = _db.deleteTrip(_trip.getTrip_id());
            //if there exist rows in table Trip
            if (numOfDeletedRows > 0) {
                Toast.makeText(getContext(), R.string.notification_delete_success, Toast.LENGTH_SHORT).show();
                Navigation.findNavController(getView()).navigateUp();

                return;
            }
        }
        Toast.makeText(getContext(), R.string.notification_delete_fail, Toast.LENGTH_SHORT).show();
    }

    protected void showExpenseList() {
        if (getArguments() != null) {
            Bundle bundle = new Bundle();
            bundle.putSerializable(ExpenseFragment.ARG_PARAM_TRIP_ID, _trip.getTrip_id());

            //get the expense list stored in getChildFragmentManager()
            getChildFragmentManager().getFragments().get(0).setArguments(bundle);
        }
    }

    protected void reloadExpenseList() {
        Bundle bundle = new Bundle();
        bundle.putSerializable(ExpenseFragment.ARG_PARAM_TRIP_ID, _trip.getTrip_id());
        //getChildFragmentManager() expense list storage
        getChildFragmentManager().beginTransaction()
                .setReorderingAllowed(true)
                //show recyclerView with expense list
                .replace(R.id.expenseList, ExpenseFragment.class, bundle)
                .commit();


    }


    @Override
    public void sendFromExpenseAddFragment(Expense expense) {
        if (expense != null) {
            //set trip id of expense equal to id of trip
            expense.setTrip_ID(_trip.getTrip_id());
//            expense.setExpense_id(1);
            //save expenses to database
            long id = _db.insertExpense(expense);
            //Appear notification when success and failure
            Toast.makeText(getContext(), id == -1 ? R.string.notification_create_fail : R.string.notification_create_success, Toast.LENGTH_SHORT).show();
            // call reloadList function
            reloadExpenseList();
        }
    }
}


