package com.example.myclinicapp;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class RegisterActivity extends AppCompatActivity {

    EditText etUser, etPass, etPassConfirm;
    Button btnRegister;
    TextView tvLoginLink;
    CheckBox cbAgree; // Variabel Checkbox
    DatabaseHelper DB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        etUser = findViewById(R.id.etRegUser);
        etPass = findViewById(R.id.etRegPass);
        etPassConfirm = findViewById(R.id.etRegPassConfirm);
        btnRegister = findViewById(R.id.btnDoRegister);
        tvLoginLink = findViewById(R.id.tvLoginLink);
        cbAgree = findViewById(R.id.cbAgree); // Sambungkan ID Checkbox
        DB = new DatabaseHelper(this);

        btnRegister.setOnClickListener(v -> {
            String user = etUser.getText().toString();
            String pass = etPass.getText().toString();
            String confirm = etPassConfirm.getText().toString();

            // 1. Cek Kolom Kosong
            if(user.equals("") || pass.equals("") || confirm.equals("")) {
                Toast.makeText(RegisterActivity.this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            }
            // 2. CEK CHECKBOX (WAJIB DICENTANG)
            else if (!cbAgree.isChecked()) {
                Toast.makeText(RegisterActivity.this, "Please agree to terms & conditions", Toast.LENGTH_SHORT).show();
            }
            // 3. Cek Password Sama
            else if (!pass.equals(confirm)) {
                Toast.makeText(RegisterActivity.this, "Password not matching!", Toast.LENGTH_SHORT).show();
            }
            // 4. Proses Daftar
            else {
                Boolean checkInsert = DB.insertUser(user, pass);
                if(checkInsert) {
                    Toast.makeText(RegisterActivity.this, "Registered Successfully!", Toast.LENGTH_SHORT).show();
                    finish(); // Balik ke Login
                } else {
                    Toast.makeText(RegisterActivity.this, "User already exists!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        tvLoginLink.setOnClickListener(v -> finish());
    }
}