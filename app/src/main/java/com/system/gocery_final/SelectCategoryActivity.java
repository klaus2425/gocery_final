package com.system.gocery_final;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.system.gocery_final.Seller.SellerAddNewProductActivity;
import com.system.gocery_final.Seller.SellerCategoryActivity;
import com.system.gocery_final.Seller.SellerHomeActivity;

public class SelectCategoryActivity extends AppCompatActivity {


    private ImageView img_beverages, img_frozenGoods, img_fruits, img_vegetables, img_dairy, img_cannedGoods, img_snacks, img_condiments,
            img_toiletries, img_cleaningMaterials;
    private Button goBack;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_category);

        goBack = (Button) findViewById(R.id.select_go_back_btn);


        img_beverages = (ImageView) findViewById(R.id.select_beverages);
        img_frozenGoods = (ImageView) findViewById(R.id.select_frozengoods);
        img_fruits = (ImageView) findViewById(R.id.select_fruits);
        img_vegetables = (ImageView) findViewById(R.id.select_vegetables);
        img_dairy = (ImageView) findViewById(R.id.select_dairy);
        img_cannedGoods = (ImageView) findViewById(R.id.select_cannedgoods);
        img_snacks = (ImageView) findViewById(R.id.select_snacks);
        img_condiments = (ImageView) findViewById(R.id.select_condiments);
        img_toiletries = (ImageView) findViewById(R.id.select_toiletries);
        img_cleaningMaterials = (ImageView) findViewById(R.id.select_cleaningmaterials);

        goBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SelectCategoryActivity.this, SellerHomeActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            }
        });

        img_beverages.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SelectCategoryActivity.this, ProductCategoryActivity.class);
                intent.putExtra("category", "beverages"); // store category to pass
                startActivity(intent);
            }
        });

        img_frozenGoods.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SelectCategoryActivity.this, ProductCategoryActivity.class);
                intent.putExtra("category", "frozengoods");
                startActivity(intent);
            }
        });

        img_fruits.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SelectCategoryActivity.this, ProductCategoryActivity.class);
                intent.putExtra("category", "fruits");
                startActivity(intent);
            }
        });

        img_cleaningMaterials.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SelectCategoryActivity.this, ProductCategoryActivity.class);
                intent.putExtra("category", "cleaningmaterials");
                startActivity(intent);
            }
        });

        img_vegetables.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SelectCategoryActivity.this, ProductCategoryActivity.class);
                intent.putExtra("category", "vegetables");
                startActivity(intent);
            }
        });

        img_dairy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SelectCategoryActivity.this, ProductCategoryActivity.class);
                intent.putExtra("category", "dairy");
                startActivity(intent);
            }
        });

        img_cannedGoods.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SelectCategoryActivity.this, ProductCategoryActivity.class);
                intent.putExtra("category", "cannedgoods");
                startActivity(intent);
            }
        });

        img_condiments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SelectCategoryActivity.this, ProductCategoryActivity.class);
                intent.putExtra("category", "condiments");
                startActivity(intent);
            }
        });

        img_snacks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SelectCategoryActivity.this, ProductCategoryActivity.class);
                intent.putExtra("category", "snacks");
                startActivity(intent);
            }
        });

        img_toiletries.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SelectCategoryActivity.this, ProductCategoryActivity.class);
                intent.putExtra("category", "toiletries");
                startActivity(intent);
            }
        });


    }
}