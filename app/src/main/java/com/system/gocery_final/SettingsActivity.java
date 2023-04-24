package com.system.gocery_final;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.net.Uri;

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


public class SettingsActivity extends AppCompatActivity {
    private FirebaseAuth auth;
    private FirebaseUser user;
    private CircleImageView profileImageView;
    private EditText firstNameEditText, lastNameEditText, userPhoneEditText, addressEditText;
    private TextView closeTextBtn, saveTextButton;
    private Button profileChangeTextBtn;

    private Uri imageUri;
    private String myUrl = "";
    private StorageTask uploadTask;
    private StorageReference storageProfilePictureRef;
    private String checker = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        storageProfilePictureRef = FirebaseStorage.getInstance().getReference().child("Profile pictures");
        profileImageView = (CircleImageView) findViewById(R.id.settings_profile_image);
        firstNameEditText = (EditText) findViewById(R.id.settings_firstname);
        lastNameEditText = (EditText) findViewById(R.id.settings_lastname);
        userPhoneEditText = (EditText) findViewById(R.id.settings_phonenumber);
        addressEditText = (EditText) findViewById(R.id.settings_address);
        profileChangeTextBtn = (Button) findViewById(R.id.profile_image_change);
        closeTextBtn = (TextView) findViewById(R.id.close_settings);
        saveTextButton = (TextView) findViewById(R.id.update_settings);
        auth = FirebaseAuth.getInstance();

        userInfoDisplay(profileImageView,firstNameEditText, lastNameEditText,userPhoneEditText,addressEditText);

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

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//
//        if (requestCode==CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE  &&  resultCode==RESULT_OK  &&  data!=null)
//        {
//            CropImage.ActivityResult result = CropImage.getActivityResult(data);
//            imageUri = result.getUri();
//
//            profileImageView.setImageURI(imageUri);
//        }
//        else{
//            Toast.makeText(this,"Error, Try Again",Toast.LENGTH_SHORT).show();
//            startActivity(new Intent(SettingsActivity.this,SettingsActivity.class));
//        }
//    }

    private void userInfoSaved() {
        if(TextUtils.isEmpty(firstNameEditText.getText().toString())){
            Toast.makeText(this,"First Name Should Not Be Empty",Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(lastNameEditText.getText().toString())){
            Toast.makeText(this,"Last Name Should Not Be Empty",Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(addressEditText.getText().toString())){
            Toast.makeText(this,"Address Should Not Be Empty",Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(userPhoneEditText.getText().toString())){
            Toast.makeText(this,"Contact Number Not Be Empty",Toast.LENGTH_SHORT).show();
        }
        else if(checker.equals("clicked")){
            uploadImage();
        }

    }

    private void uploadImage() {
        if(imageUri!=null){
            final StorageReference fileRef = storageProfilePictureRef.child(Prevalent.currentOnlineUser.getEmail()+ ".jpg");
            uploadTask = fileRef.putFile(imageUri);

            uploadTask.continueWithTask(new Continuation() {
                @Override
                public Object then(@NonNull Task task) throws Exception {
                    if(!task.isSuccessful()){
                        throw  task.getException();
                    }

                    return fileRef.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if(task.isSuccessful()){

                        Uri downloadUrl = task.getResult();
                        myUrl = downloadUrl.toString();

                        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child(user.getUid());

                        HashMap<String,Object> userMap = new HashMap<>();
                        userMap.put("firstName",firstNameEditText.getText().toString());
                        userMap.put("lastName", lastNameEditText.getText().toString());
                        userMap.put("address",addressEditText.getText().toString());
                        userMap.put("contact",userPhoneEditText.getText().toString());
                        userMap.put("image",myUrl);
                        ref.child(Prevalent.currentOnlineUser.getEmail()).updateChildren(userMap);

                        startActivity(new Intent(SettingsActivity.this,HomeActivity.class));
                        Toast.makeText(SettingsActivity.this,"Profile Info Update Successfully",Toast.LENGTH_SHORT);
                        finish();
                    }
                    else {
                       Toast.makeText(SettingsActivity.this,"Error",Toast.LENGTH_SHORT);
                    }
                }
            });
        }
    }

    private void updateOnlyUserInfo() {
        user = auth.getCurrentUser();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child(user.getUid());
        HashMap<String,Object> userMap = new HashMap<>();
        userMap.put("firstName",firstNameEditText.getText().toString());
        userMap.put("lastName", lastNameEditText.getText().toString());
        userMap.put("address",addressEditText.getText().toString());
        userMap.put("contact",userPhoneEditText.getText().toString());
        ref.child(Prevalent.currentOnlineUser.getEmail()).updateChildren(userMap);

        startActivity(new Intent(SettingsActivity.this,HomeActivity.class));
        Toast.makeText(SettingsActivity.this,"Profile Info Update Successfully",Toast.LENGTH_SHORT);

        finish();

    }

    private void userInfoDisplay(CircleImageView profileImageView, EditText firstNameEditText, EditText lastNameEditText, EditText userPhoneEditText, EditText addressEditText) {
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
                       lastNameEditText.setText(lastName);
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