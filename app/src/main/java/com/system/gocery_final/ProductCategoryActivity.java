package com.system.gocery_final;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.TextView;

public class ProductCategoryActivity extends AppCompatActivity {

    TextView selectedCategoryTxt;
    RecyclerView categoryProducts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_category);

        selectedCategoryTxt = findViewById(R.id.selected_category);
        categoryProducts = findViewById(R.id.products_cat_recycler);
        categoryProducts.setLayoutManager(new LinearLayoutManager(ProductCategoryActivity.this));

        selectedCategoryTxt.setText(getIntent().getExtras().get("category").toString());
    }
}