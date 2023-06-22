package com.system.gocery_final.Seller;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import com.system.gocery_final.R;

import java.util.HashMap;

public class SellerManageStockActivity extends AppCompatActivity {

    private FirebaseAuth firebaseauth;
    private ImageView prodImage;
    private ImageButton minusStock, plusStock,backBtn;
    private TextView prodName,stockInfo;
    private String pid;
    private EditText stockInput;
    private String productID ="";
    private DatabaseReference productsRef;
    private FirebaseUser user;
    private Button plusfive, plusfifteen,plustwenty,plusfifty,applyChanges;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_manage_stock);
        prodImage = findViewById(R.id.product_image_details);
        prodName = findViewById(R.id.product_name_details);
        backBtn = findViewById(R.id.backBtn);
        stockInfo = findViewById(R.id.stock_info);
        minusStock = findViewById(R.id.stock_minus);
        plusStock = findViewById(R.id.stock_add);
        plusfive = findViewById(R.id.plus_five);
        plusfifteen = findViewById(R.id.plus_fifteen);
        plustwenty = findViewById(R.id.plus_twenty);
        plusfifty = findViewById(R.id.plus_fifty);
        applyChanges = findViewById(R.id.apply_changes);
        stockInput = findViewById(R.id.stock_input);
        stockInput.setText("0");
        firebaseauth = FirebaseAuth.getInstance();
        user= firebaseauth.getCurrentUser();
        pid = getIntent().getStringExtra("pid");
        productsRef = FirebaseDatabase.getInstance().getReference().child("Products").child(pid).child("quantity");
        loadProdInfo();
        loadstockInfo();
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        plusStock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String currentValue = stockInput.getText().toString();
                int value = Integer.parseInt(currentValue);
                value+=1;
                stockInput.setText(String.valueOf(value));
            }
        });

        minusStock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String currentValue = stockInput.getText().toString();
                int value = Integer.parseInt(currentValue);
//                for(int i = value;value>i)
                if(value==0){
                    value =0;
                }
                else {
                    value--;
                    stockInput.setText(String.valueOf(value));
                }
            }
        });

        plusfive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String currentValue = stockInput.getText().toString();
                int value = Integer.parseInt(currentValue);
                value+=5;
                stockInput.setText(String.valueOf(value));

            }
        });

        plusfifteen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String currentValue = stockInput.getText().toString();
                int value = Integer.parseInt(currentValue);
                value+=15;
                stockInput.setText(String.valueOf(value));

            }
        });
        plustwenty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String currentValue = stockInput.getText().toString();
                int value = Integer.parseInt(currentValue);
                value+=20;
                stockInput.setText(String.valueOf(value));

            }
        });
        plusfifty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String currentValue = stockInput.getText().toString();
                int value = Integer.parseInt(currentValue);
                value+=50;
                stockInput.setText(String.valueOf(value));

            }
        });

        applyChanges.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                applyChanges();
            }
        });

    }
    private void applyChanges() {


        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
        ref.child("Products").child(pid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String addStock = stockInput.getText().toString();
                int newStock = Integer.parseInt(addStock);
                String currentStock = snapshot.child("quantity").getValue().toString();
                int fbStock = Integer.parseInt(currentStock);
                int totalStock = newStock + fbStock;



                HashMap<String, Object> productMap = new HashMap<>();
                productMap.put("quantity", Integer.toString(totalStock));


                productsRef.updateChildren(productMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(SellerManageStockActivity.this, "Stock succcessfully updated", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }


    private void loadstockInfo() {
        DatabaseReference ref =FirebaseDatabase.getInstance().getReference();
        ref.child("Products").child(pid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String productStock = snapshot.child("quantity").getValue().toString();
                stockInfo.setText(productStock);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void loadProdInfo() {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
        ref.child("Products").child(pid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String productName = snapshot.child("pname").getValue().toString();
                String productImage = snapshot.child("image").getValue().toString();

                prodName.setText(productName);
                try {
                    Picasso.get().load(productImage).placeholder(R.drawable.person).into(prodImage);
                }
                catch (Exception e){
                    prodImage.setImageResource(R.drawable.person);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }


}