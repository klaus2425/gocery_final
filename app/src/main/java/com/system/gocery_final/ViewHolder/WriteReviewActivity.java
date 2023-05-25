package com.system.gocery_final.ViewHolder;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.system.gocery_final.R;

public class WriteReviewActivity extends AppCompatActivity {

    private String pid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_review);

        pid = getIntent().getStringExtra("pid");

    }
}