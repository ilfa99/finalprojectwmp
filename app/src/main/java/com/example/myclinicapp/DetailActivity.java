package com.example.myclinicapp;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

public class DetailActivity extends AppCompatActivity {

    TextView tvPageTitle;
    Button btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        tvPageTitle = findViewById(R.id.tvPageTitle);
        btnBack = findViewById(R.id.btnBack);

        String title = getIntent().getStringExtra("TITLE");
        if (title != null) {
            tvPageTitle.setText(title);
        }

        btnBack.setOnClickListener(v -> finish());
    }
}