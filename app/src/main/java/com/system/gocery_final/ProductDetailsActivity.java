package com.system.gocery_final;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rey.material.widget.FloatingActionButton;
import com.squareup.picasso.Picasso;
import com.system.gocery_final.Model.Products;

public class ProductDetailsActivity extends AppCompatActivity {
    private FloatingActionButton addToCartBtn;
    private Button plusBtn, minusBtn;
    private ImageView productImage;
    private TextView productPrice, productDescription, productName,productQuantity;

    private String productID ="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);
        productID = getIntent().getStringExtra("pid");
        addToCartBtn =(FloatingActionButton) findViewById(R.id.add_product_to_cart_btn);
        minusBtn =(Button) findViewById(R.id.product_minus);
        plusBtn =(Button) findViewById(R.id.product_add);
        productImage=(ImageView) findViewById(R.id.product_image_details);
        productDescription=(TextView) findViewById(R.id.product_description_details);
        productQuantity=(TextView)findViewById(R.id.product_quantity);
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