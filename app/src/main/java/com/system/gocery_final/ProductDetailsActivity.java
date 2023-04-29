package com.system.gocery_final;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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
import com.system.gocery_final.Model.Products;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class ProductDetailsActivity extends AppCompatActivity {
    private FloatingActionButton addToCartBtn;
    private Button plusBtn, minusBtn;
    private ImageView productImage;
    private TextView productPrice, productDescription, productName;
    private EditText productQuantity;
    private String productID ="";
    FirebaseAuth auth = FirebaseAuth.getInstance();
    private FirebaseUser user = auth.getCurrentUser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);
        productID = getIntent().getStringExtra("pid");
        addToCartBtn =(FloatingActionButton) findViewById(R.id.pd_add_product_to_cart_btn);
        minusBtn =(Button) findViewById(R.id.product_minus);
        plusBtn =(Button) findViewById(R.id.product_add);
        productImage=(ImageView) findViewById(R.id.product_image_details);
        productDescription=(TextView) findViewById(R.id.product_description_details);
        productQuantity= (EditText) findViewById(R.id.product_quantity);
        productName=(TextView) findViewById(R.id.product_name_details);
        productPrice=(TextView) findViewById(R.id.product_price);
        productQuantity.setText("1");
        getProductDetails(productID);

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

        addToCartBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addingToCartList();
            }
        });
    }

    private void addingToCartList() {

        String saveCurrentTime, saveCurrentDate;
        Calendar calForDate = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd, yyyy");
        saveCurrentDate = currentDate.format(calForDate.getTime());
        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
        saveCurrentTime = currentTime.format(calForDate.getTime());

        final DatabaseReference cartListRef = FirebaseDatabase.getInstance().getReference().child("Cart List");

        final HashMap<String, Object> cartmap = new HashMap<>();
        cartmap.put("pid", productID);
        cartmap.put("pname", productName.getText().toString());
        cartmap.put("price", productPrice.getText().toString());
        cartmap.put("date", saveCurrentDate);
        cartmap.put("time", saveCurrentTime);
        cartmap.put("quantity", productQuantity.getText().toString());

        cartListRef.child("User View").child(user.getUid()).child("Products").child(productID).updateChildren(cartmap)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            cartListRef.child("Admin View").child(user.getUid()).child("Products").child(productID)
                                    .updateChildren(cartmap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()){
                                                Toast.makeText(ProductDetailsActivity.this, "Added to Cart!", Toast.LENGTH_SHORT).show();
                                                Intent intent = new Intent(ProductDetailsActivity.this, HomeActivity.class);
                                                startActivity(intent);
                                            }
                                        }
                                    });
                        }
                    }
                });

    }


    private void getProductDetails(String pruductID) {
        DatabaseReference productsRef = FirebaseDatabase.getInstance().getReference().child("Products");

        productsRef.child(pruductID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    Products products = snapshot.getValue(Products.class);
                    productName.setText(products.getPname());
                    productPrice.setText(products.getPrice()+" PHP");
                    productDescription.setText(products.getDescription());
                    Picasso.get().load(products.getImage()).into(productImage);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }


//    public void plusBtn(View v){
//          count++;
//        productQuantity.setText(count);
//    }
//
//    public void minusBtn(View v){
//        if(count<=0) {count = 0;}
//        else {count--;}
//        productQuantity.setText(count);
//    }

}