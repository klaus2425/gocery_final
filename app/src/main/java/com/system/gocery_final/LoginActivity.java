package com.system.gocery_final;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
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
import com.system.gocery_final.Seller.SellerHomeActivity;
import com.system.gocery_final.Model.Users;
import com.system.gocery_final.Prevalent.Prevalent;

import io.paperdb.Paper;

public class LoginActivity extends AppCompatActivity {

    private TextView forgotPasswordLink;
    private Button sellerButton; // adminLink
    private Button customerButton; // notAdminLink
    private EditText inputEmail, inputPassword;
    private Button loginButton;
    private FirebaseAuth auth;
    private FirebaseUser user;
    private ProgressDialog loadingBar;
    private String parentDbName = "Users";
    private CheckBox chkBoxRememberMe;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        loginButton = (Button) findViewById(R.id.login_button);
        inputEmail = (EditText) findViewById(R.id.email);
        inputPassword = (EditText) findViewById(R.id.password);
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        loadingBar = new ProgressDialog(this);
        chkBoxRememberMe = findViewById(R.id.remember_me_chkb);
        sellerButton = (Button) findViewById(R.id.admin_panel_link);
        TextView signup = findViewById(R.id.signUp);
        customerButton = (Button) findViewById(R.id.customerLink);
        customerButton.setVisibility(View.INVISIBLE);
        forgotPasswordLink = (TextView) findViewById(R.id.forgot_password_link);
        Paper.init(this);

        forgotPasswordLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, ResetPasswordActivity.class);
                intent.putExtra("check", "login");
                startActivity(intent);
            }
        });
        signup.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(intent);
        });


        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                loginUser();
            }
        });

        sellerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginButton.setText("Sign in as Seller");
                sellerButton.setVisibility(View.INVISIBLE);
                customerButton.setVisibility(View.VISIBLE);
                parentDbName = "Seller";
            }
        });

        customerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginButton.setText("Sign in");
                sellerButton.setVisibility(View.VISIBLE);
                customerButton.setVisibility(View.INVISIBLE);
                parentDbName = "Users";
            }
        });
    }

    private void loginUser(){
        String email  = inputEmail.getText().toString();
        String password = inputPassword.getText().toString();
        if(TextUtils.isEmpty(password) || TextUtils.isEmpty(email) ) {
            Toast.makeText(LoginActivity.this, "Please fill all fields", Toast.LENGTH_SHORT).show();
        } else {
            auth.signInWithEmailAndPassword(email, password)

                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                user = auth.getCurrentUser();
                                if(user.isEmailVerified()) {
                                    loadingBar.setTitle("Logging in");
                                    loadingBar.setMessage("Please wait while we are checking credentials.");
                                    loadingBar.setCanceledOnTouchOutside(false);
                                    loadingBar.show();
                                    allowAccessToAccount(email, password);
                                }else {
                                    Toast.makeText(LoginActivity.this, "Please verify your email.", Toast.LENGTH_SHORT).show();
                                    loadingBar.dismiss();
                                    user.sendEmailVerification();
                                }
                            } else {
                                Toast.makeText(LoginActivity.this, task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                                loadingBar.dismiss();

                            }
                        }
                    });
        }
    }
    private void allowAccessToAccount(final String email, final String password){
        if(chkBoxRememberMe.isChecked()){
            Paper.book().write(Prevalent.userEmail,email);
            Paper.book().write(Prevalent.userPasswordKey,password);
            Paper.book().write(Prevalent.userType,parentDbName);

        }
        final DatabaseReference RootRef;
        RootRef = FirebaseDatabase.getInstance().getReference();
        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.child(parentDbName).child(user.getUid()).exists()){

                    Users userData = snapshot.child(parentDbName).child(user.getUid()).getValue(Users.class);
                    if(userData.getEmail().equals(email)){

                        if (userData.getPassword().equals(password)) {
                            if(parentDbName.equals("Seller")){
                                Toast.makeText(LoginActivity.this, "Seller Login Successful", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(LoginActivity.this, SellerHomeActivity.class);
                                startActivity(intent);
                                finish();
                            } else if (parentDbName.equals("Users")) {

                                Toast.makeText(LoginActivity.this, "Login Successfully", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                                Prevalent.currentOnlineUser = userData;
                                startActivity(intent);
                                finish();
                            }
                        } else {
                                loadingBar.dismiss();
                                Toast.makeText(LoginActivity.this, "Password is incorrect", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
                else {
                    Toast.makeText(LoginActivity.this, "Account " + email +  " does not exist.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}