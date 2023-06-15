package com.system.gocery_final;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.system.gocery_final.Prevalent.Prevalent;

import java.util.HashMap;

public class ResetPasswordActivity extends AppCompatActivity {

    private String check = "";
    private TextView pageTitle, titleQuestions;
    private EditText email, question1, question2;
    private Button verifyButton;
    private FirebaseAuth auth;
    private FirebaseUser user;
    private AuthCredential credential;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);
        pageTitle = (TextView) findViewById(R.id.security_text);
        verifyButton = (Button) findViewById(R.id.verify_btn);
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        email = (EditText) findViewById(R.id.find_email);
    }

    @Override
    protected void onStart() {
        super.onStart();
        verifyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               if(!TextUtils.isEmpty(email.getText().toString())){
                   FirebaseAuth.getInstance().sendPasswordResetEmail(email.getText().toString())
                           .addOnCompleteListener(new OnCompleteListener<Void>() {
                               @Override
                               public void onComplete(@NonNull Task<Void> task) {
                                   if (task.isSuccessful()) {
                                       Toast.makeText(ResetPasswordActivity.this, "Reset link sent to email.", Toast.LENGTH_SHORT).show();
                                   }
                               }
                           });
               }
            }
        });

    }

}