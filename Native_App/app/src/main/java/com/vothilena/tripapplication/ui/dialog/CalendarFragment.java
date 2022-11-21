package com.vothilena.tripapplication.ui.dialog;

import android.annotation.SuppressLint;
import android.os.Bundle;
import androidx.fragment.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import com.vothilena.tripapplication.R;


public class CalendarFragment extends DialogFragment {

    CalendarView calendar;

    public CalendarFragment() {}

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_calendar, container, false);

        calendar = view.findViewById(R.id.calendar);
        calendar.setOnDateChangeListener((calendarView, year, month, day) -> {
            String date = "";

            ++month;

            date += day < 10 ? "0" + day : day;
            date += "/";
            date += month < 10 ? "0" + month : month;
            date += "/";
            date += year;

            FragmentListener listener = (FragmentListener) getParentFragment();
            listener.sendFromCalendarFragment(date);

            dismiss();
        });

        return view;
    }

    public interface FragmentListener {
        void sendFromCalendarFragment(String date);
    }
}