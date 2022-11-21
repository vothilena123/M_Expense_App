package com.vothilena.tripapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import com.vothilena.tripapplication.R;
import com.google.firebase.firestore.FirebaseFirestore;
import com.vothilena.tripapplication.databse.BackupEntry;
import com.vothilena.tripapplication.databse.TripDAO;
import com.vothilena.tripapplication.model.Backup;
import com.vothilena.tripapplication.model.Expense;
import com.vothilena.tripapplication.model.Trip;
import java.util.ArrayList;
import java.util.Date;

public class BackupActivity extends AppCompatActivity {

    protected TripDAO _db;
    protected Button backup;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_backup);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        _db = new TripDAO(this);

        backup = findViewById(R.id.backup);
        backup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //create trips array list object to get information from Trip table
                ArrayList<Trip> trips = _db.getTripList(null, null, false);
                //create expense array list object to get information from Expense table
                ArrayList<Expense> expenses = _db.getExpenseList(null, null, false);
                //if the data in trips and expenses is not null then
                if (null != trips && 0 < trips.size() && null != expenses && 0 < expenses.size()) {
                    String deviceName = Build.MANUFACTURER
                            + " " + Build.MODEL + " " + Build.VERSION.RELEASE
                            + " " + Build.VERSION_CODES.class.getFields()[android.os.Build.VERSION.SDK_INT].getName();

                    Backup backup = new Backup(new Date(), deviceName, trips, expenses);
                    //push data to storage on Firebase
                    FirebaseFirestore.getInstance().collection(BackupEntry.TABLE_NAME)
                            .add(backup)
                            //if the add is successful then success message
                            .addOnSuccessListener(document -> {
                                Toast.makeText(getApplicationContext(), R.string.notification_backup_success, Toast.LENGTH_SHORT).show();
                                Log.d(getResources().getString(R.string.label_backup_firestore), document.getId());
                            })
                            //if the add is not successfull then fail message
                            .addOnFailureListener(e -> {
                                Toast.makeText(getApplicationContext(), R.string.notification_backup_fail, Toast.LENGTH_SHORT).show();
                                e.printStackTrace();
                            });
                } else {
                    //If there is no data, show an empty list
                    Toast.makeText(getApplicationContext(), R.string.error_empty_list, Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}