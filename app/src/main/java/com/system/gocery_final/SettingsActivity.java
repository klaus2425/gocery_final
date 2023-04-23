package com.system.gocery_final;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.system.gocery_final.Prevalent.Prevalent;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.squareup.picasso.Picasso;
import java.util.HashMap;
import de.hdodenhof.circleimageview.CircleImageView;
import com.theartofdev.edmodo.cropper.CropImage;

public class SettingsActivity extends AppCompatActivity {
    private FirebaseAuth auth;
    private FirebaseUser user;
    private CircleImageView profileImageView;
    private EditText firstNameEditText,laststNameEditText, userPhoneEditText, addressEditText;
    private TextView closeTextBtn, saveTextButton;
    private Button profileChangeTextBtn;

    private Uri imageUri;
    private String myUrl = "";
    private StorageTask uploadTask;
    private StorageReference storageProfilePrictureRef;
    private String checker = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        storageProfilePrictureRef = FirebaseStorage.getInstance().getReference().child("Profile pictures");
        profileImageView = (CircleImageView) findViewById(R.id.settings_profile_image);
        firstNameEditText = (EditText) findViewById(R.id.settings_firstname);
        laststNameEditText= (EditText) findViewById(R.id.settings_lastname);
        userPhoneEditText = (EditText) findViewById(R.id.settings_phonenumber);
        addressEditText = (EditText) findViewById(R.id.settings_address);
        profileChangeTextBtn = (Button) findViewById(R.id.profile_image_change);
        closeTextBtn = (TextView) findViewById(R.id.close_settings);
        saveTextButton = (TextView) findViewById(R.id.update_settings);
        auth = FirebaseAuth.getInstance();

        userInfoDisplay(profileImageView,firstNameEditText,laststNameEditText,userPhoneEditText,addressEditText);

        closeTextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        saveTextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checker.equals("clicked")){
                    userInfoSaved();
                }else{
                    updateOnlyUserInfo();
                }
            }


        });

        profileChangeTextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checker ="clicked";

            }
        });
    }
    private void userInfoSaved() {


    }
    private void updateOnlyUserInfo() {


    }



    private void userInfoDisplay(CircleImageView profileImageView, EditText firstNameEditText, EditText laststNameEditText, EditText userPhoneEditText, EditText addressEditText) {
        user = auth.getCurrentUser();
        DatabaseReference UserRef = FirebaseDatabase.getInstance().getReference().child(user.getUid()).child(Prevalent.currentOnlineUser.getEmail());
        UserRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
               if(snapshot.exists()){
                   if(snapshot.child("image").exists()){
                       String image = snapshot.child("image").getValue().toString();
                       String firsName = snapshot.child("firstName").getValue().toString();
                       String lastName = snapshot.child("lastName").getValue().toString();
                       String phone = snapshot.child("contact").getValue().toString();
                       String address = snapshot.child("address").getValue().toString();

                       Picasso.get().load(image).into(profileImageView);
                       firstNameEditText.setText(firsName);
                       laststNameEditText.setText(lastName);
                       userPhoneEditText.setText(phone);
                       addressEditText.setText(address);
                   }
               }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }


}