package com.system.gocery_final.Seller;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.github.dhaval2404.imagepicker.ImagePicker;
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
import com.system.gocery_final.Prevalent.Prevalent;
import com.system.gocery_final.ProductDetailsActivity;
import com.system.gocery_final.R;
import com.system.gocery_final.SettingsActivity;
import com.system.gocery_final.WriteReviewActivity;

import java.util.HashMap;

public class SellerMaintainProductsActivity extends AppCompatActivity {

    private Button applyChangesBtn, deleteBtn,checkRv;
    private EditText name, price, description, quantity;
    private ImageView imageView;
    private String productID ="";
    private DatabaseReference productsRef;
    private String checker = "";
    private Uri imageUri;
    private String myUrl = "";
    private StorageTask uploadTask;
    private StorageReference storageProductPictureRef;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_maintain_products);
        storageProductPictureRef = FirebaseStorage.getInstance().getReference().child("Product Images");
        productID = getIntent().getStringExtra("pid");
        productsRef = FirebaseDatabase.getInstance().getReference().child("Products").child(productID);
        checkRv = findViewById(R.id.check_product_rv_btn);
        applyChangesBtn = findViewById(R.id.apply_changes_button);
        name = findViewById(R.id.product_name_maintain);
        price = findViewById(R.id.product_price_maintain);
        description = findViewById(R.id.product_description_maintain);
        quantity = findViewById(R.id.product_quantity_maintain);
        imageView = findViewById(R.id.product_image_maintain);
        deleteBtn = findViewById(R.id.delete_product_btn);

        displaySpecificProductInfo();

        checkRv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SellerMaintainProductsActivity.this, SellerShowProductReviewsActivity.class);
                intent.putExtra("pid",productID);
                startActivity(intent);
            }
        });

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checker = "clicked";
                String[] mimeTypes = {"image/jpg", "image/png", "image/jpeg"};
                ImagePicker.with(SellerMaintainProductsActivity.this)
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
        applyChangesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(checker.equals("clicked")){
                    applyChangeswithImage();
                }else applyChanges();
            }
        });

        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteThisProduct();

            }
        });



    }

    private final androidx.activity.result.ActivityResultLauncher<Intent> ActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                Intent data = result.getData();
                if (data != null && result.getResultCode() == Activity.RESULT_OK){
                    imageUri = data.getData();
                    imageView.setImageURI(imageUri);
                }else {
                    Toast.makeText(SellerMaintainProductsActivity.this, ImagePicker.getError(data), Toast.LENGTH_SHORT).show();
                }
            }
    );



    private void deleteThisProduct() {
        productsRef.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                finish();

                Toast.makeText(SellerMaintainProductsActivity.this, "Product Delete Successfully", Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void applyChangeswithImage(){
        if(imageUri != null) {
            final StorageReference fileRef = storageProductPictureRef.child(productID + ".jpg");
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
                    if(task.isSuccessful())     {


                        Uri downloadUrl = task.getResult();
                        myUrl = downloadUrl.toString();

                        String pName = name.getText().toString();
                        String pPrice = price.getText().toString();
                        String pDescription = description.getText().toString();
                        String pQuantity = quantity.getText().toString();
                        if(pName.equals("")) Toast.makeText(SellerMaintainProductsActivity.this, "Missing Product Name", Toast.LENGTH_SHORT).show();
                        else if(pPrice.equals("")) Toast.makeText(SellerMaintainProductsActivity.this, "Missing Product Price", Toast.LENGTH_SHORT).show();
                        else if(pDescription.equals("")) Toast.makeText(SellerMaintainProductsActivity.this, "Missing Product Description", Toast.LENGTH_SHORT).show();
                        else {
                            HashMap<String, Object> productMap = new HashMap<>();
                            productMap.put("pid", productID);
                            productMap.put("description", pDescription);
                            productMap.put("price", pPrice);
                            productMap.put("pname", pName);
                            productMap.put("quantity", pQuantity);
                            productMap.put("image", myUrl);
                            productsRef.updateChildren(productMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){
                                        Toast.makeText(SellerMaintainProductsActivity.this, "Product succcessfully updated", Toast.LENGTH_SHORT).show();
                                        finish();
                                    }
                                }
                            });
                        }
                    }
                }
            });
        }


    }
    private void applyChanges() {
        String pName = name.getText().toString();
        String pPrice = price.getText().toString();
        String pDescription = description.getText().toString();
        String pQuantity = quantity.getText().toString();
        if(pName.equals("")) Toast.makeText(this, "Missing Product Name", Toast.LENGTH_SHORT).show();
        else if(pPrice.equals("")) Toast.makeText(this, "Missing Product Price", Toast.LENGTH_SHORT).show();
        else if(pDescription.equals("")) Toast.makeText(this, "Missing Product Description", Toast.LENGTH_SHORT).show();
        else {
            HashMap<String, Object> productMap = new HashMap<>();
            productMap.put("pid", productID);
            productMap.put("description", pDescription);
            productMap.put("price", pPrice);
            productMap.put("pname", pName);
            productMap.put("quantity", pQuantity);

            productsRef.updateChildren(productMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()){
                        Toast.makeText(SellerMaintainProductsActivity.this, "Product succcessfully updated", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(SellerMaintainProductsActivity.this, SellerCategoryActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }
            });
        }
    }

    public void displaySpecificProductInfo(){
        productsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    String pName = snapshot.child("pname").getValue().toString();
                    String pPrice = snapshot.child("price").getValue().toString();
                    String pDescription = snapshot.child("description").getValue().toString();
                    String pImage = snapshot.child("image").getValue().toString();
                    String pQuantity = snapshot.child("quantity").getValue().toString();

                    name.setText(pName);
                    price.setText(pPrice);
                    description.setText(pDescription);
                    quantity.setText(pQuantity);
                    Picasso.get().load(pImage).into(imageView);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}