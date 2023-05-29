package com.system.gocery_final;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.system.gocery_final.Model.Users;
import com.system.gocery_final.Prevalent.Prevalent;
import com.system.gocery_final.Seller.SellerHomeActivity;

import io.paperdb.Paper;


public class MainActivity extends AppCompatActivity {
    private ProgressDialog loadingBar;

    private String parentDbName = "Users";
    private Button registerButton, loginButton;
    private FirebaseUser user;
    private FirebaseAuth auth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        setContentView(R.layout.activity_main);

        Paper.init(this);

        registerButton = (Button) findViewById(R.id.main_register_btn);
        loginButton = (Button) findViewById(R.id.main_login_btn);
        loadingBar = new ProgressDialog(this);

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });




        String UserEmailKey = Paper.book().read(Prevalent.userEmail);
        String UserPasswordKey = Paper.book().read(Prevalent.userPasswordKey);
        parentDbName = Paper.book().read(Prevalent.userType);
        if(UserEmailKey != "" && UserPasswordKey != ""){
            if(!TextUtils.isEmpty(UserEmailKey) && !TextUtils.isEmpty(UserPasswordKey)){

                if(user!=null){
                    loadingBar.setTitle("Logging in");
                    loadingBar.setMessage("Please wait while we are checking credentials.");
                    loadingBar.show();
                    AllowAccess(UserEmailKey,UserPasswordKey, parentDbName);

                }

            }
        }

    }
    private void AllowAccess(final String email, final String password, final String userType) {
        final DatabaseReference RootRef;
        RootRef = FirebaseDatabase.getInstance().getReference();
        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.child(userType).child(user.getUid()).exists()){

                    Users userData = snapshot.child(userType).child(user.getUid()).getValue(Users.class);
                    if(userData.getEmail().equals(email)){

                        if (userData.getPassword().equals(password)) {
                            if(userType.equals("Seller")){
                                loadingBar.dismiss();
                                Toast.makeText(MainActivity.this, "Seller Login Successful", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(MainActivity.this, SellerHomeActivity.class);
                                startActivity(intent);
                                finish();
                            } else if (userType .equals("Users")) {
                                loadingBar.dismiss();
                                Toast.makeText(MainActivity.this, "Login Successfully", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(MainActivity.this, HomeActivity.class);
                                Prevalent.currentOnlineUser = userData;
                                startActivity(intent);
                                finish();
                            }
                        } else {
                            Toast.makeText(MainActivity.this, "Password is incorrect", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
                else {
                    Toast.makeText(MainActivity.this, "Account " + email +  " does not exist.", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}