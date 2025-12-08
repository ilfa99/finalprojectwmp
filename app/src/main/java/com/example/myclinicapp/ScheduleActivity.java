package com.example.myclinicapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;
import java.util.ArrayList;

public class ScheduleActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    ImageView btnBack;
    DatabaseHelper DB;
    ArrayList<String> doctor, name, date;
    BookingAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);

        recyclerView = findViewById(R.id.recyclerView);
        btnBack = findViewById(R.id.btnBack);
        DB = new DatabaseHelper(this);

        doctor = new ArrayList<>();
        name = new ArrayList<>();
        date = new ArrayList<>();

        storeDataInArrays();

        adapter = new BookingAdapter(this, doctor, name, date);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        btnBack.setOnClickListener(v -> finish());
    }

    void storeDataInArrays() {
        Cursor cursor = DB.getBookings();
        if (cursor.getCount() == 0) {
            Toast.makeText(this, "No Appointment Data.", Toast.LENGTH_SHORT).show();
        } else {
            while (cursor.moveToNext()) {
                // index 1=name, 2=doctor, 4=date (Sesuai urutan di DatabaseHelper)
                name.add(cursor.getString(1));
                doctor.add(cursor.getString(2));
                date.add(cursor.getString(4));
            }
        }
    }
}