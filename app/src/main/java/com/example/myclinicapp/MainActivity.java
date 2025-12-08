package com.example.myclinicapp;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    // 1. Deklarasi Variabel
    EditText etUsername, etPassword;
    Button btnLogin, btnRegister;
    DatabaseHelper DB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 2. Sambungkan dengan ID di XML
        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);     // Tombol Login (Biru)
        btnRegister = findViewById(R.id.btnRegister); // Tombol Register (Putih/Outline)
        DB = new DatabaseHelper(this);

        // --- A. LOGIKA TOMBOL LOGIN ---
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String user = etUsername.getText().toString();
                String pass = etPassword.getText().toString();

                // Validasi: Cek apakah kolom kosong
                if(user.equals("") || pass.equals("")) {
                    Toast.makeText(MainActivity.this, "Please enter all fields", Toast.LENGTH_SHORT).show();
                } else {
                    // Cek username & password di Database
                    Boolean checkuserpass = DB.checkLogin(user, pass);

                    if(checkuserpass) {
                        Toast.makeText(MainActivity.this, "Login Successful!", Toast.LENGTH_SHORT).show();

                        // PINDAH KE DASHBOARD (HOME)
                        Intent intent = new Intent(getApplicationContext(), HomeActivity.class);

                        // PENTING: Kirim Nama User ke HomeActivity biar bisa disapa "Hi, [Nama]"
                        intent.putExtra("USERNAME", user);

                        startActivity(intent);
                        finish(); // Tutup halaman login agar tidak bisa kembali (Back)
                    } else {
                        Toast.makeText(MainActivity.this, "Login Failed! Invalid Username or Password.", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });

        // --- B. LOGIKA TOMBOL REGISTER ---
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Pindah ke Halaman Register (Buat Akun)
                Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });
    }
}