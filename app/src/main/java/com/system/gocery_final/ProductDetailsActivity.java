package com.system.gocery_final;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rey.material.widget.FloatingActionButton;
import com.squareup.picasso.Picasso;
import com.system.gocery_final.Model.ModelReview;
import com.system.gocery_final.Model.Products;
import com.system.gocery_final.ViewHolder.AdapterReview;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

public class ProductDetailsActivity extends AppCompatActivity {
    private Button addToCartBtn, writeComment, addToCartBtn2;
    private ImageButton plusBtn, minusBtn;
    private ImageView productImage;
    private TextView productPrice, productDescription, productName, ratingsTv, stock;
    private EditText productQuantity;
    private String productID ="", state = "Normal";
    FirebaseAuth auth = FirebaseAuth.getInstance();
    private FirebaseUser user = auth.getCurrentUser();
    private int databaseQuantity;
    private String name;
    private RecyclerView reviewsRv;
    private String sessionID = "";
    private RatingBar ratingBar;
    private String allowReview = "";
    private ArrayList<ModelReview> reviewArrayList;
    private AdapterReview adapterReview;
    private ImageButton back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);
        allowReview = getIntent().getStringExtra("allow");
        reviewsRv = findViewById(R.id.reviewsRv);
        stock = (TextView) findViewById(R.id.product_stock);
        ratingBar=(RatingBar) findViewById(R.id.ratingBar);
        ratingsTv = (TextView) findViewById(R.id.ratingsTv);
        addToCartBtn2 = (Button) findViewById(R.id.pd_add_product_to_cart_btn2);
        productID = getIntent().getStringExtra("pid");
        writeComment =(Button) findViewById(R.id.pd_add_comment);
        addToCartBtn =(Button) findViewById(R.id.pd_add_product_to_cart_btn);
        minusBtn =(ImageButton) findViewById(R.id.product_minus);
        plusBtn =(ImageButton) findViewById(R.id.product_add);
        productImage=(ImageView) findViewById(R.id.product_image_details);
        productDescription=(TextView) findViewById(R.id.product_description_details);
        productQuantity= (EditText) findViewById(R.id.product_quantity);
        productName=(TextView) findViewById(R.id.product_name_details);
        productPrice=(TextView) findViewById(R.id.product_price);
        productQuantity.setText("1");
        getProductDetails(productID);
        loadReviews();
        sessionID = getIntent().getExtras().get("session").toString();
        back = findViewById(R.id.backBtn);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        plusBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String currentValue = productQuantity.getText().toString();
                int value = Integer.parseInt(currentValue);
                value++;
                productQuantity.setText(String.valueOf(value));
            }
        });

        minusBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String currentValue = productQuantity.getText().toString();
                int value = Integer.parseInt(currentValue);
//                for(int i = value;value>i)
                if(value==0){
                    value =0;
                }
                else {
                    value--;
                    productQuantity.setText(String.valueOf(value));
                }

            }
        });

        if(allowReview.equals("true")){
            addToCartBtn2.setVisibility(View.GONE);
        }else addToCartBtn2.setVisibility(View.VISIBLE);
        writeComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(ProductDetailsActivity.this, WriteReviewActivity.class);
                intent.putExtra("pid",productID);
                startActivity(intent);

            }
        });


        addToCartBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final DatabaseReference addRef = FirebaseDatabase.getInstance().getReference("Products").child(productID);


                addRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        databaseQuantity = Integer.parseInt(snapshot.child("quantity").getValue().toString());
                        if(state.equals("Order Placed") || state.equals("Order Shipped")){
                            Toast.makeText(ProductDetailsActivity.this, "You can purchase more products when it is shipped", Toast.LENGTH_LONG).show();
                        } else if (productQuantity.getText().toString().equals("0")) {
                            Toast.makeText(ProductDetailsActivity.this, "Quantity cannot be 0", Toast.LENGTH_SHORT).show();
                        } else if (databaseQuantity == 0) {
                            Toast.makeText(ProductDetailsActivity.this, "Product out of stock.", Toast.LENGTH_SHORT).show();
                        } else if (Integer.parseInt(productQuantity.getText().toString()) > databaseQuantity) {
                            Toast.makeText(ProductDetailsActivity.this, "Order quantity cannot be greater than stock.", Toast.LENGTH_SHORT).show();

                        } else {
                            addingToCartList();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


            }

        });
        addToCartBtn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final DatabaseReference addRef = FirebaseDatabase.getInstance().getReference("Products").child(productID);


                addRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        databaseQuantity = Integer.parseInt(snapshot.child("quantity").getValue().toString());
                        if(state.equals("Order Placed") || state.equals("Order Shipped")){
                            Toast.makeText(ProductDetailsActivity.this, "You can purchase more products when it is shipped", Toast.LENGTH_LONG).show();
                        } else if (productQuantity.getText().toString().equals("0")) {
                            Toast.makeText(ProductDetailsActivity.this, "Quantity cannot be 0", Toast.LENGTH_SHORT).show();
                        } else if (databaseQuantity == 0) {
                            Toast.makeText(ProductDetailsActivity.this, "Product out of stock.", Toast.LENGTH_SHORT).show();
                        } else if (Integer.parseInt(productQuantity.getText().toString()) > databaseQuantity) {
                            Toast.makeText(ProductDetailsActivity.this, "Order quantity cannot be greater than stock.", Toast.LENGTH_SHORT).show();

                        } else {
                            addingToCartList();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


            }

        });
    }

    private float ratingSum =0;
    private void loadReviews() {
        reviewArrayList = new ArrayList<>();

        DatabaseReference ref =FirebaseDatabase.getInstance().getReference();
        ref.child("Products").child(productID).child("reviews").addValueEventListener(new ValueEventListener() {
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
                    adapterReview = new AdapterReview(ProductDetailsActivity.this, reviewArrayList);
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

    @Override
    protected void onStart() {
        super.onStart();

    }

    private void addingToCartList() {

        String saveCurrentTime, saveCurrentDate;
        Calendar calForDate = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd, yyyy");
        saveCurrentDate = currentDate.format(calForDate.getTime());
        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
        saveCurrentTime = currentTime.format(calForDate.getTime());

        final DatabaseReference cartListRef = FirebaseDatabase.getInstance().getReference().child("Cart List");
        final DatabaseReference cartListRef2 = FirebaseDatabase.getInstance().getReference();
        DatabaseReference UserRef = FirebaseDatabase.getInstance().getReference().child("Users").child(user.getUid());
        UserRef.addValueEventListener(new ValueEventListener() {
                                          @Override
                                          public void onDataChange(@NonNull DataSnapshot snapshot) {
                                              if (snapshot.exists()) {
                                                  if (snapshot.child("image").exists()) {
                                                      name = snapshot.child("firstName").getValue().toString() + " " + snapshot.child("lastName").getValue().toString();
                                                  }
                                              }
                                          }

                                          @Override
                                          public void onCancelled(@NonNull DatabaseError error) {

                                          }
                                      });
        HashMap<String, Object> cartmap = new HashMap<>();
        HashMap<String, Object> cartStatus = new HashMap<>();
        cartmap.put("pid", productID);
        cartmap.put("pname", productName.getText().toString());
        cartmap.put("price", productPrice.getText().toString());
        cartmap.put("date", saveCurrentDate);
        cartmap.put("name", name);
        cartmap.put("time", saveCurrentTime);
        cartmap.put("quantity", productQuantity.getText().toString());
        cartStatus.put("status", "unconfirmed");
        cartListRef2.child("Order History").child(user.getUid()).child(getIntent().getExtras().get("session").toString()).child("products").child(productID)
                        .updateChildren(cartmap);
        cartListRef2.child("Order History").child(user.getUid()).child(getIntent().getExtras().get("session").toString()).updateChildren(cartStatus);
        cartListRef.child("User View").child(user.getUid()).child("Products").child(productID).updateChildren(cartmap)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            cartListRef.child("Admin View").child(sessionID).child("Products").child(productID)
                                    .updateChildren(cartmap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()){
                                                Toast.makeText(ProductDetailsActivity.this, "Added to Cart!", Toast.LENGTH_SHORT).show();
                                                finish();
                                            }
                                        }
                                    });
                        }
                    }
                });

    }


    private void getProductDetails(String productID) {
        DatabaseReference productsRef = FirebaseDatabase.getInstance().getReference().child("Products");

        productsRef.child(productID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    Products products = snapshot.getValue(Products.class);
                    productName.setText(products.getPname());
                    productPrice.setText(products.getPrice());
                    stock.setText("Stock: " + products.getQuantity());
                    productDescription.setText(products.getDescription());
                    Picasso.get().load(products.getImage()).into(productImage);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

}