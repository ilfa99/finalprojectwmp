package com.example.myclinicapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences; // Import SharedPreferences
import android.net.Uri; // Import Uri
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

public class HomeActivity extends AppCompatActivity {

    // Deklarasi Variabel
    CardView cardDoctor1, cardDoctor2, cardDoctor3;
    CardView btnProfileTop, btnCatJantung, btnCatGigi, btnCatSyaraf;
    View btnCatComingSoon;

    // Tambah variabel ivProfileHome
    ImageView btnNotification, navHome, navSchedule, navChat, navProfileBottom, ivProfileHome;
    TextView btnSeeAll, tvUsername;
    EditText etSearch;

    // Data Dokter
    private static final String DR_ILFA = "dr. Ilfa Nur Fatimah, Sp.B";
    private static final String DR_SARAH = "dr. Sarah Diana Vaulina Sitorus, Sp.JP";
    private static final String DR_ANNISA = "dr. Annisa Regita Cahyani, Sp.PD";

    private static final String SPCL_SURGEON = "Surgeon Specialist";
    private static final String SPCL_CARDIO = "Cardiologist";
    private static final String SPCL_INTERNAL = "Internal Medicine";

    private static final String SCH_ILFA = "Mon, Thurs | 10:00 AM - 01:00 PM";
    private static final String SCH_SARAH = "Wed, Fri | 07:00 AM - 11:00 AM";
    private static final String SCH_ANNISA = "Tue, Sat | 09:00 AM - 03:00 PM";

    String currentUsername;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // --- INISIALISASI WIDGET ---
        tvUsername = findViewById(R.id.tvUsername);
        etSearch = findViewById(R.id.etSearch);

        // Inisialisasi Foto Profil Home
        ivProfileHome = findViewById(R.id.ivProfileHome);

        // Dokter
        cardDoctor1 = findViewById(R.id.cardDoctor1);
        cardDoctor2 = findViewById(R.id.cardDoctor2);
        cardDoctor3 = findViewById(R.id.cardDoctor3);

        // Header & Kategori
        btnProfileTop = findViewById(R.id.btnProfileTop);
        btnNotification = findViewById(R.id.btnNotification);
        btnCatJantung = findViewById(R.id.btnCatJantung);
        btnCatGigi = findViewById(R.id.btnCatGigi);
        btnCatSyaraf = findViewById(R.id.btnCatSyaraf);
        btnCatComingSoon = findViewById(R.id.btnCatComingSoon);

        btnSeeAll = findViewById(R.id.btnSeeAll);

        // Navigasi Bawah
        navHome = findViewById(R.id.navHome);
        navSchedule = findViewById(R.id.navSchedule);
        navChat = findViewById(R.id.navChat);
        navProfileBottom = findViewById(R.id.navProfileBottom);

        // --- AMBIL DATA USERNAME ---
        currentUsername = getIntent().getStringExtra("USERNAME");
        if (currentUsername != null) {
            tvUsername.setText("Hi, " + currentUsername);
        } else {
            currentUsername = "User";
        }

        // --- LOGIKA UTAMA ---
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterDoctors(s.toString());
            }
            @Override
            public void afterTextChanged(Editable s) {}
        });

        cardDoctor1.setOnClickListener(v -> openBooking(DR_ILFA, SPCL_SURGEON, SCH_ILFA));
        cardDoctor2.setOnClickListener(v -> openBooking(DR_SARAH, SPCL_CARDIO, SCH_SARAH));
        cardDoctor3.setOnClickListener(v -> openBooking(DR_ANNISA, SPCL_INTERNAL, SCH_ANNISA));

        btnCatJantung.setOnClickListener(v -> openBooking(DR_SARAH, SPCL_CARDIO, SCH_SARAH));
        btnCatGigi.setOnClickListener(v -> openBooking(DR_ILFA, SPCL_SURGEON, SCH_ILFA));
        btnCatSyaraf.setOnClickListener(v -> openBooking(DR_ANNISA, SPCL_INTERNAL, SCH_ANNISA));

        btnCatComingSoon.setOnClickListener(v -> openDetail("Coming Soon"));
        btnSeeAll.setOnClickListener(v -> openDetail("All Doctors"));

        btnNotification.setOnClickListener(v -> {
            Intent intent = new Intent(HomeActivity.this, NotificationActivity.class);
            startActivity(intent);
        });

        navHome.setOnClickListener(v -> Toast.makeText(this, "You are already at Home", Toast.LENGTH_SHORT).show());

        navSchedule.setOnClickListener(v -> {
            Intent intent = new Intent(HomeActivity.this, ScheduleActivity.class);
            startActivity(intent);
        });

        navProfileBottom.setOnClickListener(v -> {
            Intent intent = new Intent(HomeActivity.this, SettingsActivity.class);
            intent.putExtra("USERNAME", currentUsername);
            startActivity(intent);
        });

        btnProfileTop.setOnClickListener(v -> {
            Intent intent = new Intent(HomeActivity.this, SettingsActivity.class);
            intent.putExtra("USERNAME", currentUsername);
            startActivity(intent);
        });

        navChat.setOnClickListener(v -> {
            ScrollView scrollView = findViewById(R.id.scrollView);
            if (scrollView != null) scrollView.smoothScrollTo(0, 0);
            etSearch.requestFocus();
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm != null) {
                etSearch.postDelayed(() -> imm.showSoftInput(etSearch, InputMethodManager.SHOW_IMPLICIT), 100);
            }
        });
    }

    // --- PENTING: DIPANGGIL SAAT HALAMAN MUNCUL KEMBALI ---
    // Ini memastikan saat user balik dari Settings, foto di Home langsung berubah
    @Override
    protected void onResume() {
        super.onResume();
        loadProfileImage();
    }

    // Fungsi memuat foto dari memori
    private void loadProfileImage() {
        SharedPreferences sharedPreferences = getSharedPreferences("MyClinicPrefs", MODE_PRIVATE);
        String uriString = sharedPreferences.getString("profile_image_" + currentUsername, null);

        if (uriString != null) {
            try {
                ivProfileHome.setImageURI(Uri.parse(uriString));
            } catch (Exception e) {
                ivProfileHome.setImageResource(R.drawable.profile);
            }
        }
    }

    private void filterDoctors(String text) {
        String query = text.toLowerCase();
        if (DR_ILFA.toLowerCase().contains(query) || SPCL_SURGEON.toLowerCase().contains(query)) cardDoctor1.setVisibility(View.VISIBLE);
        else cardDoctor1.setVisibility(View.GONE);

        if (DR_SARAH.toLowerCase().contains(query) || SPCL_CARDIO.toLowerCase().contains(query)) cardDoctor2.setVisibility(View.VISIBLE);
        else cardDoctor2.setVisibility(View.GONE);

        if (DR_ANNISA.toLowerCase().contains(query) || SPCL_INTERNAL.toLowerCase().contains(query)) cardDoctor3.setVisibility(View.VISIBLE);
        else cardDoctor3.setVisibility(View.GONE);

        if (query.isEmpty()) {
            cardDoctor1.setVisibility(View.VISIBLE);
            cardDoctor2.setVisibility(View.VISIBLE);
            cardDoctor3.setVisibility(View.VISIBLE);
        }
    }

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