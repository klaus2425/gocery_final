package com.system.gocery_final.Seller;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.system.gocery_final.R;

public class SellerManageStockActivity extends AppCompatActivity {

    private FirebaseAuth firebaseauth;
    private ImageView prodImage;
    private ImageButton backBtn;
    private TextView prodName,stockInfo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_manage_stock);


    }
}