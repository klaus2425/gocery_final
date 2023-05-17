package com.system.gocery_final.Sellers;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.ui.AppBarConfiguration;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.system.gocery_final.Admin.SellerProductCategoryActivity;
import com.system.gocery_final.Customers.MainActivity;
import com.system.gocery_final.R;

public class SellerHomeActivity extends AppCompatActivity {

    private TextView mTextMessage;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            if (item.getItemId() == R.id.navigation_home) {

                mTextMessage.setText(R.string.title_home);
                return true;
            }
            else if (item.getItemId() == R.id.navigation_add) {

                Intent intent = new Intent(SellerHomeActivity.this, SellerProductCategoryActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
                return true;
            } else if (item.getItemId() == R.id.navigation_logout) {
                final FirebaseAuth mAuth;
                mAuth = FirebaseAuth.getInstance();
                mAuth.signOut();

                Intent intent = new Intent(SellerHomeActivity.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
                return true;
            }
            else throw new IllegalStateException("Unexpected value: " + item.getItemId());
            }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_home);
        BottomNavigationView navView = findViewById(R.id.nav_view);

        navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_add, R.id.navigation_logout)
                .build();
    }

}