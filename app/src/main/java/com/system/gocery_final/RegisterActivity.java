package com.system.gocery_final;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {


    private Button signUpButton;
    private EditText inputFirstName, inputLastName, inputEmail, inputPassword, inputConfirmPassword, inputAddress, inputContactNumber;
    private ProgressBar loadingbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        signUpButton = (Button) findViewById(R.id.signUpButton);
        inputFirstName = (EditText) findViewById(R.id.firstName);
        inputLastName = (EditText) findViewById(R.id.lastName);
        inputEmail = (EditText) findViewById(R.id.email);
        inputPassword = (EditText) findViewById(R.id.password);
        inputConfirmPassword = (EditText) findViewById(R.id.confirm_password);
        inputAddress = (EditText) findViewById(R.id.address);
        inputContactNumber = (EditText) findViewById(R.id.contactNumber);


        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createAccount();
            }
        });
    }

    public void createAccount(){
        String firstName  = inputFirstName.getText().toString();
        String lastName  = inputLastName.getText().toString();
        String contact  = inputContactNumber.getText().toString();
        String password  = inputPassword.getText().toString();
        String confirmPassword  = inputConfirmPassword.getText().toString();
        String address  = inputAddress.getText().toString();
        String email  = inputEmail.getText().toString();

        if(TextUtils.isEmpty(firstName) || TextUtils.isEmpty(lastName) || TextUtils.isEmpty(contact) || TextUtils.isEmpty(password) || TextUtils.isEmpty(confirmPassword) || TextUtils.isEmpty(address) || TextUtils.isEmpty(email) ){
            Toast.makeText(RegisterActivity.this, "Please fill all fields", Toast.LENGTH_SHORT).show();
        } else{
            ValidateEmail(firstName, lastName, contact, password, address, email);
        }

    }
    private void ValidateEmail(String firstName, String lastName, String contact, String password, String address, String email){
        final DatabaseReference Rootref;
        Rootref = FirebaseDatabase.getInstance().getReference();
        Rootref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(!(snapshot.child("Users").child(email).exists())){
                    HashMap<String, Object> userdataMap = new HashMap<>();
                    userdataMap.put("email", email);
                    userdataMap.put("password", password);
                    userdataMap.put("firstName", firstName);
                    userdataMap.put("lastName", lastName);
                    userdataMap.put("address", address);
                    userdataMap.put("contact", contact);
                    Rootref.child("Users").child(email).updateChildren(userdataMap)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){
                                        Toast.makeText(RegisterActivity.this, "Success", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                                        startActivity(intent);
                                    } else {
                                        Toast.makeText(RegisterActivity.this, "Network Error, Try again", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
                else {
                    Toast.makeText(RegisterActivity.this, "This email already exists", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                    startActivity(intent);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}