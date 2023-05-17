package com.system.gocery_final.Sellers;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.system.gocery_final.R;

public class SellerLoginActivity extends AppCompatActivity {

    private FirebaseAuth auth;
    private FirebaseUser user;
    private ProgressDialog loadingBar;
    private Button loginSellerButton;
    private EditText  emailInput, passwordInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_login);
        loadingBar = new ProgressDialog(this);
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        emailInput = findViewById(R.id.seller_login_email);
        passwordInput = findViewById(R.id.seller_login_password);
        loginSellerButton = findViewById(R.id.seller_login_button);

        loginSellerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                loginSeller();
            }
        });
    }

    private void loginSeller() {
        final String email = emailInput.getText().toString();
        final String password = passwordInput.getText().toString();
        if(!email.equals("")&&!password.equals("")) {
            loadingBar.setTitle("Logging in");
            loadingBar.setMessage("Checking credentials");
            loadingBar.setCanceledOnTouchOutside(false);
            loadingBar.show();
            auth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()){
                                Toast.makeText(SellerLoginActivity.this, "Success", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(SellerLoginActivity.this, SellerHomeActivity.class);
                                startActivity(intent);
                            }
                        }
                    });
        }
        else{
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
        }
    }
}