package com.system.gocery_final;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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
import com.system.gocery_final.Prevalent.Prevalent;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class ConfirmFinalOrderActivity extends AppCompatActivity {
    private EditText nameEditText, numberEditText, addressEditText, cityEditText;
    private Button confirmOrderBtn, backButton;
    private FirebaseAuth auth;
    private FirebaseUser user;
    DatabaseReference shippingRef;
    private int totalDelivery;
    private TextView confirmTotal;
    private String totalAmount = "";
    private String sessionID= "";
    private RadioGroup confirmGroup;
    private RadioButton confirmCustom, confirmAccount;
    public static final String SHARED_PREFS = "sharedPrefs";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_final_order);
        backButton = (Button) findViewById(R.id.backButtonConfirm);
        totalAmount = getIntent().getStringExtra("Total Price");
        confirmOrderBtn=(Button) findViewById(R.id.confirm_final_order);
        nameEditText=(EditText) findViewById(R.id.shipment_name);
        numberEditText=(EditText) findViewById(R.id.shipment_phone_number);
        addressEditText=(EditText) findViewById(R.id.shipment_address);
        auth = FirebaseAuth.getInstance();
        totalDelivery = Integer.parseInt(getIntent().getStringExtra("Total Price")) + 30;
        shippingRef = FirebaseDatabase.getInstance().getReference().child("Users");
        user = auth.getCurrentUser();
        confirmGroup = (RadioGroup) findViewById(R.id.confirm_radio_group);
        confirmCustom = (RadioButton) findViewById(R.id.confirm_radio_custom);
        confirmAccount = (RadioButton) findViewById(R.id.confirm_radio_account);

        confirmGroup.check(R.id.confirm_radio_custom);
        confirmTotal = (TextView) findViewById(R.id.confirm_total_text);


        confirmGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(checkedId == R.id.confirm_radio_custom){
                    nameEditText.setText("");
                    addressEditText.setText("");
                    numberEditText.setText("");
                }

                if(checkedId == R.id.confirm_radio_account){
                    shippingRef.child(user.getUid()).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if(snapshot.exists()){
                                shippingRef.child(user.getUid()).addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        if(snapshot.exists()){
                                            String name = snapshot.child("firstName").getValue().toString() + " " + snapshot.child("lastName").getValue().toString();
                                            String address = snapshot.child("address").getValue().toString();
                                            String contactNum = snapshot.child("contact").getValue().toString();

                                            nameEditText.setText(name);
                                            addressEditText.setText(address);
                                            numberEditText.setText(contactNum);
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
            }
        });
        sessionID = getIntent().getExtras().get("session").toString();
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        confirmOrderBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Check();
            }
        });

        confirmTotal.setText("Total: ₱" + String.valueOf(totalDelivery) + " (₱30 Delivery Fee included)");
    }

    private void Check() {
        if(TextUtils.isEmpty(nameEditText.getText().toString())){
            Toast.makeText(this, "Please Provide Full Name", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(numberEditText.getText().toString())){
            Toast.makeText(this, "Please Provide Phone Number", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(addressEditText.getText().toString())){
            Toast.makeText(this, "Please Provide Address", Toast.LENGTH_SHORT).show();
        }
        else {
            ConfirmOrder();
        }
    }

    private void ConfirmOrder() {
        String saveCurrentTime, saveCurrentDate;
        Calendar calForDate = Calendar.getInstance();

        SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd, yyyy");
        saveCurrentDate = currentDate.format(calForDate.getTime());
        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
        saveCurrentTime = currentTime.format(calForDate.getTime());

        final DatabaseReference ordersRef = FirebaseDatabase.getInstance().getReference().child("Orders").child(sessionID   );
        final DatabaseReference ordersHistory = FirebaseDatabase.getInstance().getReference().child("Order History").child(user.getUid())
                .child(getIntent().getExtras().get("session").toString());
        final DatabaseReference ordersStatusRef = FirebaseDatabase.getInstance().getReference().child("Order History").child(user.getUid())
                .child(getIntent().getExtras().get("session").toString());
        HashMap<String, Object> orderStatus = new HashMap<>();
        HashMap<String, Object> ordersMap = new HashMap<>();
        int calc = Integer.parseInt(totalAmount) + 30;
        ordersMap.put("totalAmount", String.valueOf(calc));
        ordersMap.put("name", nameEditText.getText().toString());
        ordersMap.put("number", numberEditText.getText().toString());
        ordersMap.put("address", addressEditText.getText().toString());
        ordersMap.put("date", saveCurrentDate);
        ordersMap.put("time", saveCurrentTime);
        ordersMap.put("state","not shipped");
        ordersMap.put("uid", user.getUid());
        ordersMap.put("orderid", getIntent().getExtras().get("session").toString());
        orderStatus.put("status", "placed");
        ordersStatusRef.updateChildren(orderStatus);
        ordersHistory.updateChildren(ordersMap);

        ordersRef.updateChildren(ordersMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    FirebaseDatabase.getInstance().getReference().child("Cart List").child("User View").child(user.getUid()).removeValue()
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){
                                        Toast.makeText(ConfirmFinalOrderActivity.this, "Order has been placed", Toast.LENGTH_SHORT).show();
                                        clearPrefs();
                                        Intent intent = new Intent(ConfirmFinalOrderActivity.this, HomeActivity.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        startActivity(intent);
                                        finish();
                                    }
                                }
                            });
                }
            }
        });

    }

    private void clearPrefs() {
        SharedPreferences sp = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.clear();
        editor.commit();
    }
}