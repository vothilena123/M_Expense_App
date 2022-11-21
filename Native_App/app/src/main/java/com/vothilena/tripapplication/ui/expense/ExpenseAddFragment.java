package com.vothilena.tripapplication.ui.expense;

import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.vothilena.tripapplication.R;
import com.vothilena.tripapplication.databse.MyDatabaseHelper;
import com.vothilena.tripapplication.model.Expense;
import com.vothilena.tripapplication.ui.dialog.DatePickerFragment;
import com.vothilena.tripapplication.ui.dialog.TimePickerFragment;


public class ExpenseAddFragment extends DialogFragment
        implements DatePickerFragment.FragmentListener, TimePickerFragment.FragmentListener {

    protected long _tripId;

    protected EditText addType, addAmount, addDate, addTime, addComment;
    protected Button  buttonAddExpense;

    //assign trip id of expense an initial value
    public ExpenseAddFragment() {
        _tripId = -1;
    }
    //pass trip id of expense equal to id of trip
    public ExpenseAddFragment(long tripID) {
        _tripId = tripID;
    }

    @Override
    public void sendFromDatePickerFragment(String date) {
        addDate.setText(date);
    }

    @Override
    public void sendFromTimePickerFragment(String time) {
        addTime.setText(time);
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
        // use fragment_expense_add layout
        View view = inflater.inflate(R.layout.fragment_expense_add, container, false);
        //declare variables corresponding to the ids of the elements in fragment_expense_add layout
        addType = view.findViewById(R.id.edTypeExpense);
        addAmount = view.findViewById(R.id.edAmount);
        addDate = view.findViewById(R.id.edTripDate);
        addTime = view.findViewById(R.id.edTime);
        addComment = view.findViewById(R.id.edComment);
        buttonAddExpense = view.findViewById(R.id.add_expense_button);
        // click add expense button to call addExpense() function
        buttonAddExpense.setOnClickListener(v -> addExpense());
        // touch date field to call showDateDialog() function
        addDate.setOnTouchListener((v, motionEvent) -> showDateDialog(motionEvent));
        // touch time field to call showTimeDialog() function
        addTime.setOnTouchListener((v, motionEvent) -> showTimeDialog(motionEvent));

        return view;
    }

    protected boolean showDateDialog(MotionEvent motionEvent) {
        if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
            new DatePickerFragment().show(getChildFragmentManager(), null);
            return true;
        }

        return false;
    }

    protected boolean showTimeDialog(MotionEvent motionEvent) {
        if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
            new TimePickerFragment().show(getChildFragmentManager(), null);
            return true;
        }

        return false;
    }

    private void addExpense() {
        //if the input fields are in, display error messages that require full input
        if (addType.getText().length()==0 ){

            Toast.makeText(getActivity(),"Please enter type of expense", Toast.LENGTH_LONG).show();
        }
        else if (addAmount.getText().length()==0){
            Toast.makeText(getActivity(),"Please enter amount", Toast.LENGTH_LONG).show();
        }

        else if (addDate.getText().length()==0){
            Toast.makeText(getActivity(),"Please enter trip date", Toast.LENGTH_LONG).show();
        }
        else if (addTime.length()==0){
            Toast.makeText(getActivity(),"Please enter time", Toast.LENGTH_LONG).show();

        }else{
            //declare expense object of class Expense
            Expense expense = new Expense();
            //the text() properties of the expense object will take the input values
            expense.setType(addType.getText().toString());
            expense.setAmount(Long.parseLong(addAmount.getText().toString()));
            expense.setTrip_date(addDate.getText().toString());
            expense.setTrip_time(addTime.getText().toString());
            expense.setComment(addComment.getText().toString());
            //pass expense containing the received values to the function sendFromExpenseAddFragment()
            FragmentListener listener = (FragmentListener) getParentFragment();
            listener.sendFromExpenseAddFragment(expense);
        }
        dismiss();
    }

    public interface FragmentListener {
        void sendFromExpenseAddFragment(Expense expense);
    }
}