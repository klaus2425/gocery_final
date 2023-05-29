package com.system.gocery_final.Seller;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.google.firebase.database.DatabaseReference;
import com.system.gocery_final.R;

public class SellerHistoryDetailsActivity extends AppCompatActivity {

    private DatabaseReference cartListRef;
    private String orderId = "";
    private RecyclerView productsList;
    RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_history_details);

        orderId = getIntent().getStringExtra("orderid");
        productsList = findViewById(R.id.seller_history_details_list);
        productsList.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        productsList.setLayoutManager(layoutManager);


    }
}