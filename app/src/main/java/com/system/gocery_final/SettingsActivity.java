package com.system.gocery_final;

import static android.content.ContentValues.TAG;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.content.Intent;
import android.net.Uri;

import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
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
import com.system.gocery_final.Seller.SellerHomeActivity;


import java.util.HashMap;
import de.hdodenhof.circleimageview.CircleImageView;
import io.paperdb.Paper;


public class SettingsActivity extends AppCompatActivity {
    private FirebaseAuth auth;
    private FirebaseUser user;
    private CircleImageView profileImageView;
    private EditText firstNameEditText, lastNameEditText, userPhoneEditText, addressEditText, userEmailEditText, userPasswordEditText;
    private TextView closeTextBtn, saveTextButton;
    private Button profileChangeTextBtn, logoutBtn;
    private AuthCredential credential;
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
        userEmailEditText = (EditText) findViewById(R.id.settings_email);
        userPasswordEditText = (EditText) findViewById(R.id.settings_password);
        addressEditText = (EditText) findViewById(R.id.settings_address);
        profileChangeTextBtn = (Button) findViewById(R.id.profile_image_change);
        closeTextBtn = (TextView) findViewById(R.id.close_settings);
        logoutBtn = (Button) findViewById(R.id.settings_logout_btn);
        saveTextButton = (TextView) findViewById(R.id.update_settings);
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
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
                String[] mimeTypes = {"image/jpg", "image/png", "image/jpeg"};
                ImagePicker.with(SettingsActivity.this)
                        .cropSquare()    			//Crop image(Optional), Check Customization for more option
                        .compress(1024)//Final image size will be less than 1 MB(Optional)
                        .galleryOnly()
                        .galleryMimeTypes(mimeTypes)
                        .maxResultSize(1080, 1080)	//Final image resolution will be less than 1080 x 1080(Optional)
                        .createIntent(intent -> {
                            ActivityResultLauncher.launch(intent);
                            return null;
                        });
            }
        });

        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final FirebaseAuth mAuth;
                Paper.book().destroy();
                mAuth = FirebaseAuth.getInstance();
                mAuth.signOut();
                Intent intent = new Intent(SettingsActivity.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            }
        });
    }

    private final ActivityResultLauncher<Intent> ActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                Intent data = result.getData();
                if (data != null && result.getResultCode() == Activity.RESULT_OK){
                    imageUri = data.getData();
                    profileImageView.setImageURI(imageUri);
                }else {
                    Toast.makeText(SettingsActivity.this, ImagePicker.getError(data), Toast.LENGTH_SHORT).show();
                }
            }
    );



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
        } else if (TextUtils.isEmpty(userPasswordEditText.getText().toString())) {
            Toast.makeText(this,"Password Cannot Be Empty",Toast.LENGTH_SHORT).show();

        } else if(checker.equals("clicked")){
            uploadImage();
        }

    }

    private void uploadImage() {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Update Profile");
        progressDialog.setMessage("Please wait, while we are updating your account information");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();
        if(imageUri != null){
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

                        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Users");

                        HashMap<String,Object> userMap = new HashMap<>();
                        userMap.put("firstName",firstNameEditText.getText().toString());
                        userMap.put("lastName", lastNameEditText.getText().toString());
                        userMap.put("address",addressEditText.getText().toString());
                        userMap.put("contact",userPhoneEditText.getText().toString());
                        userMap.put("email",userEmailEditText.getText().toString());
                        userMap. put("image", myUrl);
                        ref.child(user.getUid()).updateChildren(userMap);
                        credential = EmailAuthProvider.getCredential(Prevalent.currentOnlineUser.getEmail(), userPasswordEditText.getText().toString());
                        user.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                Log.d(TAG, "User Re-authenticated.");

                                user.updateEmail(userEmailEditText.getText().toString())
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    Log.d(TAG, "User email address updated.");
                                                    user.sendEmailVerification();
                                                }
                                            }
                                        });
                                user.updatePassword(userPasswordEditText.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            Log.d(TAG, "User password updated.");
                                        }
                                    }
                                });
                            }
                        });

                        Paper.book().destroy();
                        FirebaseAuth.getInstance().signOut();
                        Toast.makeText(SettingsActivity.this,"Profile Info Update Successfully",Toast.LENGTH_SHORT);
                        startActivity(new Intent(SettingsActivity.this,MainActivity.class));
                        finish();
                    }
                    else {progressDialog.dismiss();
                       Toast.makeText(SettingsActivity.this,"Error",Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    private void updateOnlyUserInfo() {
        user = auth.getCurrentUser();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Users");
        HashMap<String,Object> userMap = new HashMap<>();
        userMap.put("firstName",firstNameEditText.getText().toString());
        userMap.put("lastName", lastNameEditText.getText().toString());
        userMap.put("address",addressEditText.getText().toString());
        userMap.put("contact",userPhoneEditText.getText().toString());
        userMap.put("email",userEmailEditText.getText().toString());
        ref.child(user.getUid()).updateChildren(userMap);
        credential = EmailAuthProvider.getCredential(Prevalent.currentOnlineUser.getEmail(), userPasswordEditText.getText().toString());
        user.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Log.d(TAG, "User Re-authenticated.");


                user.updateEmail(userEmailEditText.getText().toString())
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Log.d(TAG, "User email address updated.");
                                    user.sendEmailVerification();
                                }
                            }
                        });
                user.updatePassword(userPasswordEditText.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "User password updated.");
                        }
                    }
                });
                Paper.book().destroy();
            }
        });


        FirebaseAuth.getInstance().signOut();
        Toast.makeText(SettingsActivity.this,"Profile Info Update Successfully",Toast.LENGTH_SHORT).show();
        startActivity(new Intent(SettingsActivity.this,MainActivity.class));
        finish();

    }

    private void userInfoDisplay(CircleImageView profileImageView, EditText firstNameEditText, EditText lastNameEditText, EditText userPhoneEditText, EditText addressEditText) {

        DatabaseReference UserRef = FirebaseDatabase.getInstance().getReference().child("Users").child(user.getUid());
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
                       String email = snapshot.child("email").getValue().toString();
                       Picasso.get().load(image).into(profileImageView);
                       firstNameEditText.setText(firsName);
                       lastNameEditText.setText(lastName);
                       userEmailEditText.setText(email);
                       userPasswordEditText.setText("");
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