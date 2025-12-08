package com.example.myclinicapp;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;
import android.widget.Button;

public class SettingsActivity extends AppCompatActivity {

    ImageView btnBack;
    Button btnLogout;
    LinearLayout btnEditProfile, btnAbout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        btnBack = findViewById(R.id.btnBack);
        btnLogout = findViewById(R.id.btnLogout);
        btnEditProfile = findViewById(R.id.btnEditProfile);
        btnAbout = findViewById(R.id.btnAbout);

        // 1. LOGIKA TOMBOL BACK
        btnBack.setOnClickListener(v -> finish());

        // 2. LOGIKA EDIT PROFILE & ABOUT (Cuma Toast aja buat hiasan)
        btnEditProfile.setOnClickListener(v ->
                Toast.makeText(this, "Edit Profile Feature Coming Soon!", Toast.LENGTH_SHORT).show()
        );

        btnAbout.setOnClickListener(v ->
                Toast.makeText(this, "MyClinic App v1.0 \nCreated by Alfadzri", Toast.LENGTH_LONG).show()
        );

        // 3. LOGIKA LOGOUT (PENTING!)
        btnLogout.setOnClickListener(v -> {
            // Hapus sesi login (kalau pakai shared pref), tapi disini kita clear activity stack
            Intent intent = new Intent(SettingsActivity.this, MainActivity.class);

            // Kode ini menghapus semua halaman sebelumnya (Home, dll) dari memori
            // Jadi pas udah logout, user gak bisa tekan tombol Back buat masuk lagi
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

            startActivity(intent);
            Toast.makeText(this, "Logged Out Successfully", Toast.LENGTH_SHORT).show();
        });
    }
}