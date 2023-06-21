package com.system.gocery_final;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.system.gocery_final.Seller.SellerAddNewProductActivity;
import com.system.gocery_final.Seller.SellerCategoryActivity;
import com.system.gocery_final.Seller.SellerHomeActivity;

public class SelectCategoryActivity extends AppCompatActivity {


    private ImageView img_beverages, img_frozenGoods, img_fruits, img_vegetables, img_dairy, img_cannedGoods, img_snacks, img_condiments,
            img_toiletries, img_cleaningMaterials;
    private Button goBack;
    private String sessionID = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_category);

        goBack = (Button) findViewById(R.id.select_go_back_btn);
        sessionID = getIntent().getExtras().get("session").toString();
        Toast.makeText(this, sessionID, Toast.LENGTH_SHORT).show();
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
                Intent intent = new Intent(SelectCategoryActivity.this, HomeActivity.class);
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
                intent.putExtra("category title", "Beverages");
                intent.putExtra("session", sessionID);
                startActivity(intent);
                finish();

            }
        });

        img_frozenGoods.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SelectCategoryActivity.this, ProductCategoryActivity.class);
                intent.putExtra("category", "frozengoods");
                intent.putExtra("category title", "Frozen Goods");
                intent.putExtra("session", sessionID);
                startActivity(intent);
                finish();
            }
        });

        img_fruits.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SelectCategoryActivity.this, ProductCategoryActivity.class);
                intent.putExtra("category", "fruits");
                intent.putExtra("category title", "Fruits");
                intent.putExtra("session", sessionID);
                startActivity(intent);
                finish();
            }
        });

        img_cleaningMaterials.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SelectCategoryActivity.this, ProductCategoryActivity.class);
                intent.putExtra("category", "cleaningmaterials");
                intent.putExtra("category title", "Cleaning Materials");
                intent.putExtra("session", sessionID);
                startActivity(intent);
                finish();

            }
        });

        img_vegetables.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SelectCategoryActivity.this, ProductCategoryActivity.class);
                intent.putExtra("category", "vegetables");
                intent.putExtra("category title", "Vegetables");
                intent.putExtra("session", sessionID);
                startActivity(intent);
                finish();

            }
        });

        img_dairy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SelectCategoryActivity.this, ProductCategoryActivity.class);
                intent.putExtra("category", "dairy");
                intent.putExtra("category title", "Dairy");
                intent.putExtra("session", sessionID);
                startActivity(intent);
                finish();

            }
        });

        img_cannedGoods.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SelectCategoryActivity.this, ProductCategoryActivity.class);
                intent.putExtra("category", "cannedgoods");
                intent.putExtra("category title", "Canned Goods");
                intent.putExtra("session", sessionID);
                startActivity(intent);
                finish();

            }
        });

        img_condiments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SelectCategoryActivity.this, ProductCategoryActivity.class);
                intent.putExtra("category", "condiments");
                intent.putExtra("category title", "Condiments");
                intent.putExtra("session", sessionID);
                startActivity(intent);
                finish();

            }
        });

        img_snacks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SelectCategoryActivity.this, ProductCategoryActivity.class);
                intent.putExtra("category", "snacks");
                intent.putExtra("category title", "Snacks");
                intent.putExtra("session", sessionID);
                startActivity(intent);
                finish();

            }
        });

        img_toiletries.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SelectCategoryActivity.this, ProductCategoryActivity.class);
                intent.putExtra("category", "toiletries");
                intent.putExtra("category title", "Toiletries");
                intent.putExtra("session", sessionID);
                startActivity(intent);
                finish();

            }
        });


    }
}