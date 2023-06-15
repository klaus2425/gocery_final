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
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    loadingBar.dismiss();
                    if(userType.equals("Seller")){
                        Toast.makeText(MainActivity.this, "Seller Login Successful", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(MainActivity.this, SellerHomeActivity.class);
                        startActivity(intent);
                        finish();
                    } else if (userType.equals("Users")) {
                        Toast.makeText(MainActivity.this, "Login Successfully", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(MainActivity.this, HomeActivity.class);
                        startActivity(intent);
                        finish();
                    }

                }
                loadingBar.dismiss();
            }
        });

    }
}