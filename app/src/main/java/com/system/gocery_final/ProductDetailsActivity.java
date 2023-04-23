package com.system.gocery_final;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import com.rey.material.widget.FloatingActionButton;

public class ProductDetailsActivity extends AppCompatActivity {
    private FloatingActionButton addToCartBtn;
    private ImageView productImage;
    private TextView productPrice, productDescription, productName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);

        addToCartBtn =(FloatingActionButton) findViewById(R.id.add_product_to_cart_btn);

        productImage=(ImageView) findViewById(R.id.product_image_details);
        productDescription=(TextView) findViewById(R.id.product_description_details);
        productName=(TextView) findViewById(R.id.product_name_details);
        productPrice=(TextView) findViewById(R.id.product_price);

    }
}