package com.system.gocery_final.Seller;

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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.system.gocery_final.Model.AdminOrders;
import com.system.gocery_final.OrderHistoryActivity;
import com.system.gocery_final.OrderHistoryDetailsActivity;
import com.system.gocery_final.R;

public class SellerHistoryActivity extends AppCompatActivity {


    private RecyclerView ordersList;
    private DatabaseReference ordersRef;
    private FirebaseAuth auth;
    private FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_history);

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

        ordersList = findViewById(R.id.seller_history_list);
        ordersRef = FirebaseDatabase.getInstance().getReference().child("Order History");
        ordersList.setLayoutManager(new LinearLayoutManager(this));

    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerOptions<AdminOrders> options = new
                FirebaseRecyclerOptions.Builder<AdminOrders>()
                .setQuery(ordersRef, AdminOrders.class)
                .build();


        ordersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                FirebaseRecyclerAdapter<AdminOrders, SellerHistoryActivity.SellerOrdersViewHolder> adapter = new FirebaseRecyclerAdapter<AdminOrders,
                        SellerHistoryActivity.SellerOrdersViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull SellerHistoryActivity.SellerOrdersViewHolder holder, @SuppressLint("RecyclerView") int position, @NonNull AdminOrders model) {
                        holder.userName.setText("Name: " + model.getName());
                        holder.userPhoneNumber.setText("Phone: " + model.getNumber());
                        holder.userTotalPrice.setText("Total Amount: = Php " + model.getTotalAmount());
                        holder.userDateTime.setText("Order at: " + model.getDate() + " " + model.getTime());
                        holder.userShippingAddress.setText("Shipping Address: " + model.getAddress());
                        holder.showOrderButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(SellerHistoryActivity.this, OrderHistoryDetailsActivity.class);
                                intent.putExtra("orderid", model.getOrderid());
                                intent.putExtra("uid", user.getUid());
                                startActivity(intent);
                            }
                        });
                    }


                    @NonNull
                    @Override
                    public SellerHistoryActivity.SellerOrdersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.orders_layout, parent, false);
                        return new SellerHistoryActivity.SellerOrdersViewHolder(view);
                    }
                };
                ordersList.setAdapter(adapter);
                adapter.startListening();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });




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