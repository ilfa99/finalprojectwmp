package com.example.myclinicapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat; // Penting untuk mengubah warna teks
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import java.util.Calendar;

public class BookingActivity extends AppCompatActivity {

    // Deklarasi Variabel UI
    EditText etName, etDoctor, etDate;
    TextView tvDoctorNameDisplay, tvDoctorSpecialist;
    ImageView ivDoctorPhoto, ivHeaderBackground; // Header background ditambahkan
    RadioGroup rgGender;
    RadioButton rbSelected;
    Button btnConfirm;
    ImageView btnBack;
    DatabaseHelper DB;

    // Deklarasi Chip Tanggal dan Jam
    TextView chipDate1, chipDate2, chipDate3, chipDate4;
    TextView chipTime1, chipTime2, chipTime3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking);

        // --- 1. INISIALISASI ID DARI XML ---
        etName = findViewById(R.id.etName);
        etDoctor = findViewById(R.id.etDoctorName);
        etDate = findViewById(R.id.etDate);
        rgGender = findViewById(R.id.rgGender);
        btnConfirm = findViewById(R.id.btnConfirmBooking);
        btnBack = findViewById(R.id.btnBack);
        DB = new DatabaseHelper(this);

        tvDoctorNameDisplay = findViewById(R.id.tvDoctorNameDisplay);
        tvDoctorSpecialist = findViewById(R.id.tvDoctorSpecialist);
        ivDoctorPhoto = findViewById(R.id.ivDoctorPhoto);
        ivHeaderBackground = findViewById(R.id.ivHeaderBackground); // Inisialisasi Header

        // Chip Tanggal
        chipDate1 = findViewById(R.id.chipDate1);
        chipDate2 = findViewById(R.id.chipDate2);
        chipDate3 = findViewById(R.id.chipDate3);
        chipDate4 = findViewById(R.id.chipDate4);

        // Chip Jam
        chipTime1 = findViewById(R.id.chipTime1);
        chipTime2 = findViewById(R.id.chipTime2);
        chipTime3 = findViewById(R.id.chipTime3);

        // --- 2. LOGIKA BACK BUTTON ---
        btnBack.setOnClickListener(v -> finish());

        // --- 3. TANGKAP DATA DOKTER & GANTI GAMBAR ---
        String incomingDoctor = getIntent().getStringExtra("DOCTOR_NAME");
        String incomingSpecialist = getIntent().getStringExtra("SPECIALIST");

        if(incomingDoctor != null) {
            etDoctor.setText(incomingDoctor);
            tvDoctorNameDisplay.setText(incomingDoctor);
            tvDoctorSpecialist.setText(incomingSpecialist);

            // Logika Ganti Foto Profil & Background Header
            if (incomingDoctor.contains("Ilfa")) {
                // Dr. Ilfa -> Bedah
                ivDoctorPhoto.setImageResource(R.drawable.dr_bedah);
                ivHeaderBackground.setImageResource(R.drawable.dr_bedah);
            } else if (incomingDoctor.contains("Sarah")) {
                // Dr. Sarah -> Jantung
                ivDoctorPhoto.setImageResource(R.drawable.dr_jantung);
                ivHeaderBackground.setImageResource(R.drawable.dr_jantung);
            } else {
                // Default / Dr. Annisa -> Dalam
                ivDoctorPhoto.setImageResource(R.drawable.dr_dalam);
                ivHeaderBackground.setImageResource(R.drawable.dr_dalam);
            }
        }

        // --- 4. LOGIKA CHIP (PILIHAN JADWAL) ---

        // Reset tampilan chip ke default saat pertama kali buka
        resetDateChips();
        resetTimeChips();

        // Listener untuk Chip Tanggal
        chipDate1.setOnClickListener(v -> {
            resetDateChips();
            setChipSelected(chipDate1);
            etDate.setText("03/02/2025");
        });
        chipDate2.setOnClickListener(v -> {
            resetDateChips();
            setChipSelected(chipDate2);
            etDate.setText("04/02/2025");
        });
        chipDate3.setOnClickListener(v -> {
            resetDateChips();
            setChipSelected(chipDate3);
            etDate.setText("05/02/2025");
        });
        chipDate4.setOnClickListener(v -> {
            resetDateChips();
            setChipSelected(chipDate4);
            etDate.setText("06/02/2025");
        });

        // Listener untuk Chip Jam
        chipTime1.setOnClickListener(v -> {
            resetTimeChips();
            setChipSelected(chipTime1);
            Toast.makeText(this, "Time: 08:00 AM", Toast.LENGTH_SHORT).show();
        });
        chipTime2.setOnClickListener(v -> {
            resetTimeChips();
            setChipSelected(chipTime2);
            Toast.makeText(this, "Time: 10:00 AM", Toast.LENGTH_SHORT).show();
        });
        chipTime3.setOnClickListener(v -> {
            resetTimeChips();
            setChipSelected(chipTime3);
            Toast.makeText(this, "Time: 01:00 PM", Toast.LENGTH_SHORT).show();
        });

        // --- 5. LOGIKA DATE PICKER MANUAL ---
        etDate.setOnClickListener(v -> {
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(BookingActivity.this,
                    (view, year1, monthOfYear, dayOfMonth) -> {
                        etDate.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year1);
                        // Jika pilih manual, reset semua chip tanggal
                        resetDateChips();
                    }, year, month, day);
            datePickerDialog.show();
        });

        // --- 6. TOMBOL KONFIRMASI BOOKING ---
        btnConfirm.setOnClickListener(v -> {
            String name = etName.getText().toString();
            String doctor = etDoctor.getText().toString();
            String date = etDate.getText().toString();
            String gender = "";

            int selectedId = rgGender.getCheckedRadioButtonId();
            if (selectedId != -1) {
                rbSelected = findViewById(selectedId);
                gender = rbSelected.getText().toString();
            } else {
                Toast.makeText(this, "Please select gender", Toast.LENGTH_SHORT).show();
                return;
            }

            if(name.equals("") || date.equals("")) {
                Toast.makeText(BookingActivity.this, "Please fill all details!", Toast.LENGTH_SHORT).show();
            } else {
                Boolean checkInsert = DB.insertBooking(name, doctor, gender, date);
                if(checkInsert) {
                    Toast.makeText(BookingActivity.this, "Booking Successful!", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(BookingActivity.this, HomeActivity.class);
                    // Clear activity stack agar tidak bisa back ke booking
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(BookingActivity.this, "Booking Failed!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    // --- METODE BANTUAN UNTUK TAMPILAN CHIP ---

    // Mengubah chip menjadi Hijau (Selected)
    private void setChipSelected(TextView chip) {
        // Pastikan Anda sudah membuat bg_chip_selected.xml di folder drawable
        chip.setBackgroundResource(R.drawable.bg_chip_selected);
        chip.setTextColor(ContextCompat.getColor(this, R.color.white));
    }

    // Reset Chip Tanggal ke Putih (Default)
    private void resetDateChips() {
        TextView[] dateChips = {chipDate1, chipDate2, chipDate3, chipDate4};
        for (TextView chip : dateChips) {
            chip.setBackgroundResource(R.drawable.bg_search); // bg_search = putih border abu
            chip.setTextColor(ContextCompat.getColor(this, R.color.text_dark));
        }
    }

    // Reset Chip Jam ke Putih (Default)
    private void resetTimeChips() {
        TextView[] timeChips = {chipTime1, chipTime2, chipTime3};
        for (TextView chip : timeChips) {
            chip.setBackgroundResource(R.drawable.bg_search);
            chip.setTextColor(ContextCompat.getColor(this, R.color.text_dark));
        }
    }
}