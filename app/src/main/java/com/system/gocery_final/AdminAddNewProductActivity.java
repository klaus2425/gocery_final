package com.system.gocery_final;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Toast;

public class AdminAddNewProductActivity extends AppCompatActivity {

    private String categoryName; //CategoryName
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_add_new_product);

        categoryName = getIntent().getExtras().get("category").toString(); // Get category from AdminCategoryActivity

        Toast.makeText(this,categoryName, Toast.LENGTH_SHORT).show();

    }
}