package com.example.myclinicapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class DetailActivity extends AppCompatActivity {

    TextView tvPageTitle, tvGenericMessage;
    ImageView btnBack;
    LinearLayout comingSoonContent, allDoctorsContent;
    CardView btnDoc1, btnDoc2, btnDoc3; // Hanya 3 tombol

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        // 1. Inisialisasi ID
        tvPageTitle = findViewById(R.id.tvPageTitle);
        btnBack = findViewById(R.id.btnBack);

        // Container
        comingSoonContent = findViewById(R.id.comingSoonContent);
        allDoctorsContent = findViewById(R.id.allDoctorsContent);
        tvGenericMessage = findViewById(R.id.tvGenericMessage);

        // Kartu Dokter (Hanya 3)
        btnDoc1 = findViewById(R.id.btnDoc1);
        btnDoc2 = findViewById(R.id.btnDoc2);
        btnDoc3 = findViewById(R.id.btnDoc3);

        // 2. Ambil data judul dari Home
        String title = getIntent().getStringExtra("TITLE");

        if (title != null) {
            tvPageTitle.setText(title);

            if (title.equals("Coming Soon")) {
                // Tampilkan Coming Soon (Gigi & Neuro)
                comingSoonContent.setVisibility(View.VISIBLE);
                allDoctorsContent.setVisibility(View.GONE);
                tvGenericMessage.setVisibility(View.GONE);

            } else if (title.equals("All Doctors")) {
                // Tampilkan List Semua Dokter (3 Dokter)
                comingSoonContent.setVisibility(View.GONE);
                allDoctorsContent.setVisibility(View.VISIBLE);
                tvGenericMessage.setVisibility(View.GONE);

            } else {
                // Tampilkan pesan standar
                comingSoonContent.setVisibility(View.GONE);
                allDoctorsContent.setVisibility(View.GONE);
                tvGenericMessage.setVisibility(View.VISIBLE);
                tvGenericMessage.setText("Details for " + title + " are coming soon!");
            }
        }

        // 3. Logic Klik Dokter (Menuju Booking)
        // Dokter Bedah
        btnDoc1.setOnClickListener(v -> goToBooking("dr. Ilfa Nur Fatimah, Sp.B", "Surgeon Specialist"));
        // Dokter Jantung
        btnDoc2.setOnClickListener(v -> goToBooking("dr. Sarah Diana V.S, Sp.JP", "Cardiologist"));
        // Dokter Dalam
        btnDoc3.setOnClickListener(v -> goToBooking("dr. Annisa Regita C, Sp.PD", "Internal Medicine"));

        // Tombol Kembali
        btnBack.setOnClickListener(v -> finish());
    }

    // Fungsi helper untuk pindah ke Booking
    private void goToBooking(String docName, String specialist) {
        Intent intent = new Intent(DetailActivity.this, BookingActivity.class);
        intent.putExtra("DOCTOR_NAME", docName);
        intent.putExtra("SPECIALIST", specialist);
        startActivity(intent);
    }
}