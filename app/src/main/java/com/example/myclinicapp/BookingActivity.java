package com.example.myclinicapp;

import androidx.appcompat.app.AppCompatActivity;
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

    EditText etName, etDoctor, etDate;
    TextView tvDoctorNameDisplay, tvDoctorSpecialist;
    ImageView ivDoctorPhoto;
    RadioGroup rgGender;
    RadioButton rbSelected;
    Button btnConfirm;
    ImageView btnBack;
    DatabaseHelper DB;

    // Tambahan untuk Chip yang bisa diklik
    TextView chipDate1, chipDate2, chipDate3;
    TextView chipTime1, chipTime2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking);

        // --- INISIALISASI ---
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

        // Inisialisasi Chip (Biar bisa diklik)
        chipDate1 = findViewById(R.id.chipDate1);
        chipDate2 = findViewById(R.id.chipDate2);
        chipDate3 = findViewById(R.id.chipDate3);
        chipTime1 = findViewById(R.id.chipTime1);
        chipTime2 = findViewById(R.id.chipTime2);

        btnBack.setOnClickListener(v -> finish());

        // --- TANGKAP DATA & LOGIKA FOTO ---
        String incomingDoctor = getIntent().getStringExtra("DOCTOR_NAME");
        String incomingSpecialist = getIntent().getStringExtra("SPECIALIST");

        if(incomingDoctor != null) {
            etDoctor.setText(incomingDoctor);
            tvDoctorNameDisplay.setText(incomingDoctor);
            tvDoctorSpecialist.setText(incomingSpecialist);

            // LOGIKA GANTI FOTO (Sesuai file yang kamu punya)
            if (incomingDoctor.contains("Ilfa")) {
                // Dr. Ilfa -> Bedah
                ivDoctorPhoto.setImageResource(R.drawable.dr_bedah);
            } else if (incomingDoctor.contains("Sarah")) {
                // Dr. Sarah -> Jantung
                ivDoctorPhoto.setImageResource(R.drawable.dr_jantung);
            } else {
                // Default kalau nama lain
                ivDoctorPhoto.setImageResource(R.drawable.dr_dalam);
            }
        }

        // --- FITUR KLIK CHIP (JADWAL) ---
        // Saat diklik, langsung isi kolom Date of Visit
        chipDate1.setOnClickListener(v -> etDate.setText("03/02/2025"));
        chipDate2.setOnClickListener(v -> etDate.setText("04/02/2025"));
        chipDate3.setOnClickListener(v -> etDate.setText("05/02/2025"));

        // Saat Chip Jam diklik, cuma kasih info visual (karena DB cuma simpan tanggal)
        chipTime1.setOnClickListener(v -> Toast.makeText(this, "Time selected: 08:00 AM", Toast.LENGTH_SHORT).show());
        chipTime2.setOnClickListener(v -> Toast.makeText(this, "Time selected: 10:00 AM", Toast.LENGTH_SHORT).show());

        // --- FITUR MANUAL DATE PICKER (Tetap ada buat jaga-jaga) ---
        etDate.setOnClickListener(v -> {
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(BookingActivity.this,
                    (view, year1, monthOfYear, dayOfMonth) -> {
                        etDate.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year1);
                    }, year, month, day);
            datePickerDialog.show();
        });

        // --- TOMBOL CONFIRM ---
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
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(BookingActivity.this, "Booking Failed!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}