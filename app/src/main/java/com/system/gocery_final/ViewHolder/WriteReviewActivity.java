package com.system.gocery_final.ViewHolder;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.system.gocery_final.R;

public class WriteReviewActivity extends AppCompatActivity {

    private String pid;

    private ImageView prodImage;
    private ImageButton backBtn;
    private TextView prodName;
    private RatingBar ratingBar;
    private EditText review;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_review);

        backBtn = findViewById(R.id.backBtn);
        prodImage = findViewById(R.id.product_image_details);
        prodName = findViewById(R.id.product_name_details);
        ratingBar = findViewById(R.id.ratingBar);
        review = findViewById(R.id.reviewEt);


        pid = getIntent().getStringExtra("pid");

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

    }
}