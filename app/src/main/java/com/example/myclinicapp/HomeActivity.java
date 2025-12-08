package com.example.myclinicapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class HomeActivity extends AppCompatActivity {

    // Tambahkan cardDoctor3 disini
    CardView cardDoctor1, cardDoctor2, cardDoctor3;
    CardView btnProfileTop, btnCatJantung, btnCatGigi, btnCatSyaraf, btnCatTulang;
    ImageView btnNotification, navHome, navSchedule, navChat, navProfileBottom;
    TextView btnSeeAll, tvUsername;
    EditText etSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        tvUsername = findViewById(R.id.tvUsername);
        etSearch = findViewById(R.id.etSearch);

        // Inisialisasi Kartu Dokter
        cardDoctor1 = findViewById(R.id.cardDoctor1);
        cardDoctor2 = findViewById(R.id.cardDoctor2);
        cardDoctor3 = findViewById(R.id.cardDoctor3); // ID Baru untuk Dr. Annisa

        btnProfileTop = findViewById(R.id.btnProfileTop);
        btnNotification = findViewById(R.id.btnNotification);

        btnCatJantung = findViewById(R.id.btnCatJantung);
        btnCatGigi = findViewById(R.id.btnCatGigi);
        btnCatSyaraf = findViewById(R.id.btnCatSyaraf);
        btnCatTulang = findViewById(R.id.btnCatTulang);
        btnSeeAll = findViewById(R.id.btnSeeAll);

        navHome = findViewById(R.id.navHome);
        navSchedule = findViewById(R.id.navSchedule);
        navChat = findViewById(R.id.navChat);
        navProfileBottom = findViewById(R.id.navProfileBottom);

        String usernameLop = getIntent().getStringExtra("USERNAME");
        if (usernameLop != null) {
            tvUsername.setText("Hi, " + usernameLop);
        }

        etSearch.setOnClickListener(v -> openDetail("Search Specialist"));

        // --- UPDATE: KLIK DOKTER (Kirim 3 Data: Nama, Spesialis, Jadwal) ---

        // Dokter 1: Dr. Ilfa
        cardDoctor1.setOnClickListener(v ->
                openBooking("Dr. Ilfa Nur Fatimah", "Surgeon Specialist", "Mon, Thurs | 10:00 AM - 01:00 PM")
        );

        // Dokter 2: Dr. Sarah
        cardDoctor2.setOnClickListener(v ->
                openBooking("Dr. Sarah Diana V.S", "Cardiologist", "Wed, Fri | 07:00 AM - 11:00 AM")
        );

        // Dokter 3: Dr. Annisa (BARU)
        cardDoctor3.setOnClickListener(v ->
                openBooking("Dr. Annisa Regita Cahyani", "Internal Medicine", "Tue, Sat | 09:00 AM - 03:00 PM")
        );

        // Kategori Jantung (Arahkan ke Dr. Sarah)
        btnCatJantung.setOnClickListener(v ->
                openBooking("Dr. Sarah Diana V.S", "Cardiologist", "Wed, Fri | 07:00 AM - 11:00 AM")
        );

        // --- NAVIGASI ---

        navSchedule.setOnClickListener(v -> {
            Intent intent = new Intent(HomeActivity.this, ScheduleActivity.class);
            startActivity(intent);
        });

        navProfileBottom.setOnClickListener(v -> {
            Intent intent = new Intent(HomeActivity.this, SettingsActivity.class);
            startActivity(intent);
        });

        btnProfileTop.setOnClickListener(v -> {
            Intent intent = new Intent(HomeActivity.this, SettingsActivity.class);
            startActivity(intent);
        });

        // Tombol Lainnya
        btnNotification.setOnClickListener(v -> openDetail("Notifications"));
        btnCatGigi.setOnClickListener(v -> openDetail("Dentistry"));
        btnCatSyaraf.setOnClickListener(v -> openDetail("Neurology"));
        btnCatTulang.setOnClickListener(v -> openDetail("Orthopedics"));
        btnSeeAll.setOnClickListener(v -> openDetail("All Doctors"));
        navChat.setOnClickListener(v -> openDetail("Messages"));

        navHome.setOnClickListener(v -> Toast.makeText(this, "You are already at Home", Toast.LENGTH_SHORT).show());
    }

    // --- PENTING: Metode ini diupdate menerima 3 parameter ---
    private void openBooking(String doctorName, String specialist, String schedule) {
        Intent intent = new Intent(HomeActivity.this, BookingActivity.class);
        intent.putExtra("DOCTOR_NAME", doctorName);
        intent.putExtra("SPECIALIST", specialist);
        intent.putExtra("SCHEDULE", schedule);
        startActivity(intent);
    }

    private void openDetail(String titlePage) {
        Intent intent = new Intent(HomeActivity.this, DetailActivity.class);
        intent.putExtra("TITLE", titlePage);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finishAffinity();
    }
}