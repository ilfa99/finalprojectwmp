package com.example.myclinicapp;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class EditProfileActivity extends AppCompatActivity {

    EditText etUserDisplay, etNewPass, etConfirmPass;
    Button btnSave;
    ImageView btnBack;
    DatabaseHelper DB;
    String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        etUserDisplay = findViewById(R.id.etUsernameDisplay);
        etNewPass = findViewById(R.id.etNewPass);
        etConfirmPass = findViewById(R.id.etConfirmPass);
        btnSave = findViewById(R.id.btnSaveProfile);
        btnBack = findViewById(R.id.btnBack);
        DB = new DatabaseHelper(this);

        // Ambil username dari Intent
        username = getIntent().getStringExtra("USERNAME");
        if(username != null) {
            etUserDisplay.setText(username);
        }

        btnBack.setOnClickListener(v -> finish());

        btnSave.setOnClickListener(v -> {
            String newPass = etNewPass.getText().toString();
            String confirmPass = etConfirmPass.getText().toString();

            if (newPass.equals("") || confirmPass.equals("")) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            } else {
                if (newPass.equals(confirmPass)) {
                    // Update Database
                    boolean checkUpdate = DB.updatePassword(username, newPass);
                    if (checkUpdate) {
                        Toast.makeText(this, "Password Updated Successfully", Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        Toast.makeText(this, "Update Failed", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(this, "Password Not Matching", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}