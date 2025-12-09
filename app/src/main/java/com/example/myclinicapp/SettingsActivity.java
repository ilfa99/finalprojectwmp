package com.example.myclinicapp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class SettingsActivity extends AppCompatActivity {

    ImageView btnBack, ivProfileImage; // Tambah variabel ivProfileImage
    TextView tvProfileName;            // Tambah variabel tvProfileName
    Button btnLogout;
    LinearLayout btnEditProfile, btnAbout;
    String currentUsername;

    // Kode unik untuk request galeri
    private static final int PICK_IMAGE_REQUEST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        // --- INISIALISASI ---
        btnBack = findViewById(R.id.btnBack);
        btnLogout = findViewById(R.id.btnLogout);
        btnEditProfile = findViewById(R.id.btnEditProfile);
        btnAbout = findViewById(R.id.btnAbout);

        // Inisialisasi komponen baru
        ivProfileImage = findViewById(R.id.ivProfileImage);
        tvProfileName = findViewById(R.id.tvProfileName);

        // 1. TERIMA USERNAME & TAMPILKAN
        currentUsername = getIntent().getStringExtra("USERNAME");
        if (currentUsername != null) {
            tvProfileName.setText(currentUsername); // Ubah tulisan "User Profile" jadi Nama User

            // Muat foto yang tersimpan (jika ada)
            loadProfileImage();
        }

        // 2. LOGIKA GANTI FOTO (KLIK GAMBAR)
        ivProfileImage.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            intent.setType("image/*");
            startActivityForResult(intent, PICK_IMAGE_REQUEST);
        });

        // 3. LOGIKA TOMBOL LAINNYA
        btnBack.setOnClickListener(v -> finish());

        btnEditProfile.setOnClickListener(v -> {
            Intent intent = new Intent(SettingsActivity.this, EditProfileActivity.class);
            intent.putExtra("USERNAME", currentUsername);
            startActivity(intent);
        });

        btnAbout.setOnClickListener(v -> {
            Intent intent = new Intent(SettingsActivity.this, AboutActivity.class);
            startActivity(intent);
        });

        btnLogout.setOnClickListener(v -> {
            Intent intent = new Intent(SettingsActivity.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            Toast.makeText(this, "Logged Out Successfully", Toast.LENGTH_SHORT).show();
        });
    }

    // --- FUNGSI MENANGKAP HASIL DARI GALERI ---
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri imageUri = data.getData();

            // 1. Tampilkan gambar yang dipilih
            ivProfileImage.setImageURI(imageUri);

            // 2. Beri izin permanen agar gambar bisa diakses terus (penting!)
            try {
                getContentResolver().takePersistableUriPermission(imageUri, Intent.FLAG_GRANT_READ_URI_PERMISSION);
            } catch (SecurityException e) {
                e.printStackTrace();
            }

            // 3. Simpan lokasi gambar (URI) ke SharedPreferences
            saveProfileImage(imageUri.toString());
        }
    }

    // --- FUNGSI MENYIMPAN FOTO KE MEMORI HP ---
    private void saveProfileImage(String uriString) {
        SharedPreferences sharedPreferences = getSharedPreferences("MyClinicPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        // Simpan dengan key unik: "profile_image_" + username
        // Jadi setiap user punya foto beda-beda
        editor.putString("profile_image_" + currentUsername, uriString);
        editor.apply();
        Toast.makeText(this, "Profile Photo Updated!", Toast.LENGTH_SHORT).show();
    }

    // --- FUNGSI MEMUAT FOTO SAAT APLIKASI DIBUKA ---
    private void loadProfileImage() {
        SharedPreferences sharedPreferences = getSharedPreferences("MyClinicPrefs", MODE_PRIVATE);
        String uriString = sharedPreferences.getString("profile_image_" + currentUsername, null);

        if (uriString != null) {
            try {
                ivProfileImage.setImageURI(Uri.parse(uriString));
            } catch (Exception e) {
                // Jika gambar di galeri dihapus, balik ke default
                ivProfileImage.setImageResource(R.drawable.profile);
            }
        }
    }
}