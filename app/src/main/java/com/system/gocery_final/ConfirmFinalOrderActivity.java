package com.system.gocery_final;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

public class ConfirmFinalOrderActivity extends AppCompatActivity {
    private EditText nameEditText, numberEditText, addressEditText, cityEditText;
    private Button confirmOrderBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_final_order);

        confirmOrderBtn=(Button) findViewById(R.id.confirm_final_order);
        nameEditText=(EditText) findViewById(R.id.shipment_name);
        numberEditText=(EditText) findViewById(R.id.shipment_phone_number);
        addressEditText=(EditText) findViewById(R.id.shipment_address);
        cityEditText=(EditText) findViewById(R.id.shipment_city);
    }
}