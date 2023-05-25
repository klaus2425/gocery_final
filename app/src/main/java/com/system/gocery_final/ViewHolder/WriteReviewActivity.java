package com.system.gocery_final.ViewHolder;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rey.material.widget.FloatingActionButton;
import com.squareup.picasso.Picasso;
import com.system.gocery_final.R;

import java.util.HashMap;

public class WriteReviewActivity extends AppCompatActivity {

    private String pid;

    private ImageView prodImage;
    private ImageButton backBtn;
    private TextView prodName;
    private RatingBar ratingBar;
    private EditText reviewEt;
    private FloatingActionButton submitComment;

    private FirebaseUser user;
    private FirebaseAuth firebaseauth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_review);

       

        backBtn = findViewById(R.id.backBtn);
        prodImage = findViewById(R.id.product_image_details);
        prodName = findViewById(R.id.product_name_details);
        ratingBar = findViewById(R.id.ratingBar);
        reviewEt = findViewById(R.id.reviewEt);
        submitComment = findViewById(R.id.submitComment);

        pid = getIntent().getStringExtra("pid");
        firebaseauth = FirebaseAuth.getInstance();

        loadMyReview();
        loadShopInfo();
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        submitComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inputData();
            }
        });

    }

    private void loadShopInfo() {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users");
        ref.child(pid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String productName = "" + snapshot.child("pname");
                String productImage = "" + snapshot.child("image");

                prodName.setText(productName);
                    try {
                        Picasso.get().load(productImage).placeholder(R.drawable.person).into(prodImage);
                    }
                    catch (Exception e){
                        prodImage.setImageResource(R.drawable.person);
                    }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void loadMyReview() {
            DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users");
            ref.child(pid).child("Ratings").child(firebaseauth.getUid()).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(snapshot.exists()){

                        String uid =""+snapshot.child("uid").getValue();
                        String ratings =""+snapshot.child("ratings").getValue();
                        String review =""+snapshot.child("review").getValue();
                        String timestamp =""+snapshot.child("timestamp").getValue();

                        float myRating =Float.parseFloat(ratings);
                        ratingBar.setRating(myRating);
                        reviewEt.setText(review);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

    }

    private void inputData() {
        String ratings = "" + ratingBar.getRating();
        String review = reviewEt.getText().toString().trim();

        String timestamp = "" + System.currentTimeMillis();

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("uid",""+firebaseauth.getUid());
        hashMap.put("ratings",""+ratings);
        hashMap.put("review",""+review);
        hashMap.put("timestamp",""+timestamp);

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users");
        ref.child(pid).child("Ratings").child(firebaseauth.getUid()).updateChildren(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(WriteReviewActivity.this,"Review Published Successfuly",Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(WriteReviewActivity.this,""+ e.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });


    }
}