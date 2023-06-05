package com.system.gocery_final.Seller;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import com.system.gocery_final.Model.ModelReview;
import com.system.gocery_final.ProductDetailsActivity;
import com.system.gocery_final.R;
import com.system.gocery_final.ViewHolder.AdapterReview;

import java.text.NumberFormat;
import java.util.ArrayList;

public class SellerShowProductReviewsActivity extends AppCompatActivity {


    private FirebaseUser user;

    private FirebaseAuth firebaseauth;
    private ImageView prodImage;
    private ImageButton backBtn;
    private TextView prodName,ratingsTv;
    private RatingBar ratingBar;
    private RecyclerView reviewsRv;
    private ArrayList<ModelReview> reviewArrayList;
    private String pid;
    private AdapterReview adapterReview;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_show_product_reviews);

        ratingsTv = findViewById(R.id.ratingsTv);

        backBtn = findViewById(R.id.backBtn);
        prodImage = findViewById(R.id.product_image_details);
        prodName = findViewById(R.id.product_name_details);
        ratingBar = findViewById(R.id.ratingBar);
        reviewsRv =findViewById(R.id.reviewsRv);
        pid = getIntent().getStringExtra("pid");
        firebaseauth = FirebaseAuth.getInstance();
        user= firebaseauth.getCurrentUser();
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        loadProdInfo();
        loadReviews();

    }
    private void loadProdInfo() {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
        ref.child("Products").child(pid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String productName = snapshot.child("pname").getValue().toString();
                String productImage = snapshot.child("image").getValue().toString();

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

    private float ratingSum =0;
    private void loadReviews() {
        reviewArrayList = new ArrayList<>();

        DatabaseReference ref =FirebaseDatabase.getInstance().getReference();
        ref.child("Products").child(pid).child("reviews").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                reviewArrayList.clear();
                ratingSum= 0;
                for (DataSnapshot s : snapshot.getChildren()) {
                    float rating = Float.parseFloat(s.child("ratings").getValue().toString());
                    ratingSum = ratingSum + rating;
                    ModelReview modelReview = s.getValue(ModelReview.class);
                    reviewArrayList.add(modelReview);
                }
                adapterReview = new AdapterReview(SellerShowProductReviewsActivity.this, reviewArrayList);
                reviewsRv.setAdapter(adapterReview);
                long numberOfReviews = snapshot.getChildrenCount();
                float avgRating = ratingSum / numberOfReviews;
                ratingBar.setRating(avgRating);
                NumberFormat nf= NumberFormat.getInstance();
                nf.setMaximumFractionDigits(2);
                if(snapshot.exists()) ratingsTv.setText(nf.format(avgRating));
                else ratingsTv.setText("No Ratings Yet");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }



}