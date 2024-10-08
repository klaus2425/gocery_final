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
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.regex.Pattern;

public class RegisterActivity extends AppCompatActivity {


    private Button signUpButton;
    private EditText inputFirstName, inputLastName, inputEmail, inputPassword, inputConfirmPassword, inputAddress, inputContactNumber;
    private FirebaseAuth auth;
    private FirebaseUser user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        auth = FirebaseAuth.getInstance();

        signUpButton = (Button) findViewById(R.id.signUpButton);
        inputFirstName = (EditText) findViewById(R.id.firstName);
        inputLastName = (EditText) findViewById(R.id.lastName);
        inputEmail = (EditText) findViewById(R.id.email);
        inputPassword = (EditText) findViewById(R.id.password);
        inputConfirmPassword = (EditText) findViewById(R.id.confirm_password);
        inputAddress = (EditText) findViewById(R.id.address);
        inputContactNumber = (EditText) findViewById(R.id.contactNumber);
        TextView signIn = findViewById(R.id.signIn);


        signIn.setOnClickListener(v -> {
            Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
            startActivity(intent);
        });

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
            if(password.equals(confirmPassword)){
                if(password.length() >= 8&&password.matches("(.*[A-Z].*)") && password.matches("(.*[0-9].*)")  ){
//                    && password.matches("^(?=.*[_.()]).*$")
                    auth.createUserWithEmailAndPassword(email, password)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()){

                                        user = auth.getCurrentUser();
                                        ValidateEmail(firstName, lastName, contact, password, address, email);
                                        user.sendEmailVerification();
                                    } else {
                                        Toast.makeText(RegisterActivity.this, "Email is already in use!", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });

                }else{
                    Toast.makeText(RegisterActivity.this, "Password mus be 8 characters longs and contains at least one capital letter, numbers, and symbols", Toast.LENGTH_SHORT).show();
                }
            }else{
                Toast.makeText(RegisterActivity.this, "Password do not Match", Toast.LENGTH_SHORT).show();
            }






        }

    }
    private void ValidateEmail(String firstName, String lastName, String contact, String password, String address, String email){
        final DatabaseReference RootRef;
        RootRef = FirebaseDatabase.getInstance().getReference();
        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(!(snapshot.child("Users").child(user.getUid()).exists())){
                    HashMap<String, Object> userdataMap = new HashMap<>();
                    userdataMap.put("email", email);
                    userdataMap.put("firstName", firstName);
                    userdataMap.put("lastName", lastName);
                    userdataMap.put("address", address);
                    userdataMap.put("contact", contact);
                    userdataMap.put("uid", user.getUid());
                    RootRef.child("Users").child(user.getUid()).updateChildren(userdataMap)
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
                    finish();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}