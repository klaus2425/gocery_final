package com.system.gocery_final;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.system.gocery_final.Model.Cart;
import com.system.gocery_final.ViewHolder.CartViewHolder;

import java.util.HashMap;

public class OrderHistoryDetailsActivity extends AppCompatActivity {

    private RecyclerView productsList;
    RecyclerView.LayoutManager layoutManager;
    private DatabaseReference cartListRef;
    private String userID = "";
    private ImageButton imgBtnBack;
    private String sessionID = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_history_details);
        sessionID = getIntent().getStringExtra("session");
        userID = getIntent().getStringExtra("uid");
        imgBtnBack = (ImageButton) findViewById(R.id.order_history_backBtn);
        productsList = findViewById(R.id.history_details_list);
        productsList.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        productsList.setLayoutManager(layoutManager);
        cartListRef = FirebaseDatabase.getInstance().getReference()
                .child("Order History").child(userID).child(getIntent().getStringExtra("orderid")).child("products");
    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerOptions<Cart> options = new FirebaseRecyclerOptions.Builder<Cart>()
                .setQuery(cartListRef, Cart.class)
                .build();

        FirebaseRecyclerAdapter<Cart, CartViewHolder> adapter = new FirebaseRecyclerAdapter<Cart, CartViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull CartViewHolder holder, int position, @NonNull Cart model) {
                holder.txtProductPrice.setText("Price = Php " + model.getPrice());
                holder.txtProductName.setText(model.getPname());
                holder.txtProductQuantity.setText(model.getQuantity());

                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        CharSequence option[] = new CharSequence[]{
                                "Yes",
                                "No"
                        };

                        AlertDialog.Builder builder = new AlertDialog.Builder(OrderHistoryDetailsActivity.this);
                        builder.setTitle("Review this product?");
                        builder.setItems(option, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                if(which == 0)
                                {
                                    Intent intent = new Intent(OrderHistoryDetailsActivity.this, ProductDetailsActivity.class);
                                    intent.putExtra("pid",model.getPid());
                                    intent.putExtra("session", sessionID);
                                    intent.putExtra("allow", "true");
                                    startActivity(intent);
                                }
                            }
                        });
                        builder.show();
                    }
                });
            }

            @NonNull
            @Override
            public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_items_layout, parent, false);
                CartViewHolder holder = new CartViewHolder(view);
                return holder;
            }
        };

        imgBtnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        productsList.setAdapter(adapter);
        adapter.startListening();
    }
}