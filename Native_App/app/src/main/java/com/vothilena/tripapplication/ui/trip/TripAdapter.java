package com.vothilena.tripapplication.ui.trip;

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
import com.vothilena.tripapplication.model.Trip;

import java.util.ArrayList;


public class TripAdapter extends RecyclerView.Adapter<TripAdapter.ViewHolder> implements Filterable{
    //create the _originalLy object which is an arraylist of class Trip
    protected ArrayList<Trip> _originalList;
    //create the _filteredList object which is an arraylist of class Trip
    protected ArrayList<Trip> _filteredList;
    //create Filter item object to get filtered chip from original list
    protected TripAdapter.ItemFilter _itemFilter = new TripAdapter.ItemFilter();

    public TripAdapter(ArrayList<Trip> list) {
        //assign to arraylist variables of class Trip
        _originalList = list;
        _filteredList = list;
    }

    public void updateList(ArrayList<Trip> list) {
        //assign to arraylist variables of class Trip
        _originalList = list;
        _filteredList = list;

        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public TripAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        //use layout list_item_trip
        View view = inflater.inflate(R.layout.list_item_trip, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TripAdapter.ViewHolder holder, int position) {
        //variable trip get filter list
        Trip trip = _filteredList.get(position);
        //keep only name, destination and date to display in layout list_item_trip
        holder.listItemTripName.setText(trip.getTrip_name());
        holder.listItemDestination.setText(trip.getDestination());
        holder.listItemDate.setText(trip.getTrip_date());

    }

    @Override
    //Returns the number of trips in the filter list
    public int getItemCount() {
        return _filteredList == null ? 0 : _filteredList.size();
    }

    @Override
    //return filtered object
    public Filter getFilter() {
        return _itemFilter;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        protected LinearLayout listItemTrip;
        protected TextView listItemTripName, listItemDestination, listItemDate;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            //declare variables corresponding to the ids of the elements list_item_trip layout
            listItemTripName = itemView.findViewById(R.id.listItemTripName);
            listItemDestination = itemView.findViewById(R.id.listItemDestination);
            listItemDate = itemView.findViewById(R.id.listItemDate);

            listItemTrip = itemView.findViewById(R.id.listItemTrip);
            //click on listItemTrip
            listItemTrip.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //filter list will get trip's current position in list
                    Trip trip = _filteredList.get(getAdapterPosition());

                    Bundle bundle = new Bundle();
                    bundle.putSerializable(TripDetailFragment.ARG_PARAM_TRIP, trip);
                    //call the tripDetailFragment layout
                    Navigation.findNavController(view).navigate(R.id.tripDetailFragment, bundle);
                }
            });
        }
    }

    private class ItemFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            final ArrayList<Trip> list = _originalList;
            final ArrayList<Trip> nlist = new ArrayList<>(list.size());
            //The format for text in the search box is lowercase
            String filterString = constraint.toString().toLowerCase();
            FilterResults results = new FilterResults();
            //the loop will iterate through each trip in the list then return the filtered result
            for (Trip resident : list) {
                String filterableString = resident.toString();
                if (filterableString.toLowerCase().contains(filterString)) nlist.add(resident);
            }
            //count to get how many results are filtered out
            results.values = nlist;
            results.count = nlist.size();
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            _filteredList = (ArrayList<Trip>) results.values;
            notifyDataSetChanged();
        }


    }
}
