package com.system.gocery_final.Admin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.system.gocery_final.Customers.HomeActivity;
import com.system.gocery_final.Customers.MainActivity;
import com.system.gocery_final.R;

public class AdminHomeActivity extends AppCompatActivity {
      private Button logoutBtn, checkOrdersBtn, maintainBtn, checkApproveProductsBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_home);

        logoutBtn = (Button) findViewById(R.id.admin_logout_btn);
        checkOrdersBtn = (Button) findViewById(R.id.check_orders_btn);
        maintainBtn=(Button) findViewById(R.id.maintain_btn);
        checkApproveProductsBtn = (Button) findViewById(R.id.check_approve_btn);

        maintainBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminHomeActivity.this, HomeActivity.class);
                intent.putExtra("Admin", "Admin");
                startActivity(intent);

            }
        });


        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminHomeActivity.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            }
        });

        checkOrdersBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminHomeActivity.this, AdminNewOrdersActivity.class);
                startActivity(intent);
            }
        });

        checkApproveProductsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminHomeActivity.this, AdminNewOrdersActivity.class);
                startActivity(intent);
            }
        });
    }
}