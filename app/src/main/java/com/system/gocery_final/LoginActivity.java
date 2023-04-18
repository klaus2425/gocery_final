package com.system.gocery_final;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
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
import com.system.gocery_final.Model.Users;
import com.system.gocery_final.Prevalent.Prevalent;

import java.util.HashMap;
import io.paperdb.Paper;

public class LoginActivity extends AppCompatActivity {

    private String email,password;


    private EditText inputEmail, inputPassword;
    private Button loginButton;
    private FirebaseAuth auth;
    private FirebaseUser user;

    private String parentDbName = "Users";
    private SharedPreferences loginPreferences;
    private SharedPreferences.Editor loginPrefsEditor;
    private CheckBox chkBoxRememberMe;
    private Boolean saveLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        loginButton = (Button) findViewById(R.id.login_button);
        inputEmail = (EditText) findViewById(R.id.email);
        inputPassword = (EditText) findViewById(R.id.password);
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        chkBoxRememberMe = findViewById(R.id.remember_me_chkb);
        TextView signup = findViewById(R.id.signUp);

            //PREFERENCES
//        loginPreferences = getSharedPreferences("loginPrefs", MODE_PRIVATE);
//        loginPrefsEditor = loginPreferences.edit();
//        saveLogin = loginPreferences.getBoolean("saveLogin", false);
//        if (saveLogin == true) {
//            inputEmail.setText(loginPreferences.getString("username", ""));
//            inputPassword.setText(loginPreferences.getString("password", ""));
//            chkBoxRememberMe.setChecked(true);
//        }

        Paper.init(this);


        signup.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(intent);
        });


        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //PREFERENCES//
//                email = inputEmail.getText().toString();
//                password = inputPassword.getText().toString();
//                if (chkBoxRememberMe.isChecked()) {
//                    loginPrefsEditor.putBoolean("saveLogin", true);
//                    loginPrefsEditor.putString("email", email);
//                    loginPrefsEditor.putString("password", password);
//                    loginPrefsEditor.commit();
//                } else {
//                    loginPrefsEditor.clear();
//                    loginPrefsEditor.commit();
//                }
//               if(chkBoxRememberMe.isChecked()){
//                    Paper.book().write(Prevalent.userPasswordKey,email);
//                    Paper.book().write(Prevalent.userPasswordKey,password);
//            }

                loginUser();
            }
        });
    }

    private void loginUser(){
        String email  = inputEmail.getText().toString();
        String password = inputPassword.getText().toString();
        if(TextUtils.isEmpty(password) || TextUtils.isEmpty(email) ) {
            Toast.makeText(LoginActivity.this, "Please fill all fields", Toast.LENGTH_SHORT).show();
        }auth.signInWithEmailAndPassword(email, password)

                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful() ){
                            user = auth.getCurrentUser();
                            allowAccessToAccount(email,password);
                        } else {
                            Toast.makeText(LoginActivity.this, task.getException().toString(), Toast.LENGTH_SHORT).show();

                        }
                    }
                });

    }
    private void allowAccessToAccount(String email, String password){
        if(chkBoxRememberMe.isChecked()){
            Paper.book().write(Prevalent.userPasswordKey,email);
            Paper.book().write(Prevalent.userPasswordKey,password);
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
                            Toast.makeText(LoginActivity.this, "Log in successfully...", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                            startActivity(intent);
                        } else {
                                Toast.makeText(LoginActivity.this, "Password is incorrect...", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
                else {
                    Toast.makeText(LoginActivity.this, "Account" + email +  "do not exist.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}