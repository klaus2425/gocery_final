package com.system.gocery_final;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

public class OrderHistoryDetailsActivity extends AppCompatActivity {

    private RecyclerView productsList;
    RecyclerView.LayoutManager layoutManager;
    private DatabaseReference cartListRef;
    private String userID = "";
    private ImageButton imgBtnBack;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_history_details);

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