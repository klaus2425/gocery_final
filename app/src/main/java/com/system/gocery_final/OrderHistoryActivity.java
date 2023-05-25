package com.system.gocery_final;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.system.gocery_final.Model.AdminOrders;

public class OrderHistoryActivity extends AppCompatActivity {
    private RecyclerView ordersList;
    private DatabaseReference ordersRef;
    private FirebaseAuth auth;
    private FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_history);
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        ordersRef = FirebaseDatabase.getInstance().getReference().child("Order History").child(user.getUid());
        ordersList= findViewById(R.id.history_list);
        ordersList.setLayoutManager(new LinearLayoutManager(this));

    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseRecyclerOptions<AdminOrders> options = new
                FirebaseRecyclerOptions.Builder<AdminOrders>()
                .setQuery(ordersRef.orderByChild("status").startAt("placed").endAt("placed"), AdminOrders.class)
                .build();

        FirebaseRecyclerAdapter<AdminOrders, SellerOrdersViewHolder> adapter = new FirebaseRecyclerAdapter<AdminOrders,
                SellerOrdersViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull SellerOrdersViewHolder holder, @SuppressLint("RecyclerView") int position, @NonNull AdminOrders model) {
                holder.userName.setText("Name: " + model.getName());
                holder.userPhoneNumber.setText("Phone: " + model.getNumber());
                holder.userTotalPrice.setText("Total Amount: = Php " + model.getTotalAmount());
                holder.userDateTime.setText("Order at: " + model.getDate() + " " + model.getTime());
                holder.userShippingAddress.setText("Shipping Address: " + model.getAddress() + ", " + model.getCity());
                holder.showOrderButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(OrderHistoryActivity.this, OrderHistoryDetailsActivity.class);
                        intent.putExtra("orderid", model.getOrderid());
                        intent.putExtra("uid", user.getUid());
                        startActivity(intent);
                    }
                });
            }


            @NonNull
            @Override
            public SellerOrdersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.orders_layout, parent, false);
                return new SellerOrdersViewHolder(view);
            }
        };
        ordersList.setAdapter(adapter);
        adapter.startListening();
    }
    public static class SellerOrdersViewHolder extends RecyclerView.ViewHolder{

        public TextView userName, userPhoneNumber, userTotalPrice, userDateTime, userShippingAddress;
        public Button showOrderButton;
        public SellerOrdersViewHolder(@NonNull View itemView) {
            super(itemView);

            userName = itemView.findViewById(R.id.order_user_name);
            userPhoneNumber = itemView.findViewById(R.id.order_phone_number);
            userTotalPrice = itemView.findViewById(R.id.order_total_price);
            userDateTime = itemView.findViewById(R.id.order_date_time);
            userShippingAddress = itemView.findViewById(R.id.order_address_city);
            showOrderButton = itemView.findViewById(R.id.show_all_products);
            showOrderButton.setText("More Details");
            userPhoneNumber.setVisibility(View.GONE);
            userName.setVisibility(View.GONE);

        }
    }
}