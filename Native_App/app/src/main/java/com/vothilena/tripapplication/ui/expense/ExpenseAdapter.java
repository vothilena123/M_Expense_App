package com.vothilena.tripapplication.ui.expense;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.vothilena.tripapplication.R;
import com.vothilena.tripapplication.model.Expense;

import java.util.ArrayList;

public class ExpenseAdapter extends RecyclerView.Adapter<ExpenseAdapter.ViewHolder> implements Filterable{
    //create the _originalLy object which is an arraylist of class Expense
    protected ArrayList<Expense> _originalList;
    //create the _filteredList object which is an arraylist of class Expense
    protected ArrayList<Expense> _filteredList;
    //create Filter item object to get filtered chip from original list
    protected ExpenseAdapter.ItemFilter _itemFilter = new ExpenseAdapter.ItemFilter();

    public ExpenseAdapter(ArrayList<Expense> list) {
        //assign to arraylist variables of class Expense
        _originalList = list;
        _filteredList = list;
    }

    public void updateList(ArrayList<Expense> list) {
        _originalList = list;
        _filteredList = list;

        notifyDataSetChanged();
    }

    @Override
    //return filtered item
    public Filter getFilter() {
        return _itemFilter;
    }

    @NonNull
    @Override
    public ExpenseAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        //use list_item_expense layout
        View view = inflater.inflate(R.layout.list_item_expense, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ExpenseAdapter.ViewHolder holder, int position) {
        //variable expense get filter list
        Expense expense = _filteredList.get(position);
        //keep type, amount, date, time, comment to display in layout list_item_trip
        holder.expenseType.setText(expense.getType());
        holder.expenseAmount.setText(String.valueOf(expense.getAmount()));
        holder.expenseDate.setText(expense.getTrip_date());
        holder.expenseTime.setText(expense.getTrip_time());
        holder.expenseComment.setText(expense.getComment());

    }

    @Override
    //Returns the number of expenses in the filter list
    public int getItemCount() {
        return _filteredList == null ? 0 : _filteredList.size();
    }



    public class ViewHolder extends RecyclerView.ViewHolder {

        protected TextView expenseType, expenseAmount, expenseDate, expenseTime, expenseComment;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            //declare variables corresponding to the ids of the elements list_item_expense layout
            expenseType = itemView.findViewById(R.id.type_txt);
            expenseAmount = itemView.findViewById(R.id.amount_txt);
            expenseDate = itemView.findViewById(R.id.trip_date_txt);
            expenseTime = itemView.findViewById(R.id.trip_time_txt);
            expenseComment = itemView.findViewById(R.id.comment_txt);

        }
    }

    private class ItemFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            final ArrayList<Expense> list = _originalList;
            final ArrayList<Expense> nlist = new ArrayList<>(list.size());
            //The format for text in the search box is lowercase
            String filterString = constraint.toString().toLowerCase();
            FilterResults results = new FilterResults();
            //the loop will iterate through each expense in the list then return the filtered result
            for (Expense request : list) {
                String filterableString = request.toString();

                if (filterableString.toLowerCase().contains(filterString))
                    nlist.add(request);
            }
            //count to get how many results are filtered out
            results.values = nlist;
            results.count = nlist.size();

            return results;
        }

        @SuppressWarnings("unchecked")
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            _filteredList = (ArrayList<Expense>) results.values;
            notifyDataSetChanged();
        }
    }
}


