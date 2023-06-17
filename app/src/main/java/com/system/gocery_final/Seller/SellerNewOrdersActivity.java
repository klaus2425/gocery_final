package com.system.gocery_final.Seller;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.system.gocery_final.Model.AdminOrders;
import com.system.gocery_final.R;

import java.util.HashMap;

public class SellerNewOrdersActivity extends AppCompatActivity {

    private RecyclerView ordersList;
    private DatabaseReference ordersRef;
    private FirebaseAuth auth;
    private FirebaseUser user;
    private Button btnHistory;
    private ImageButton back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_new_orders);
        ordersRef = FirebaseDatabase.getInstance().getReference().child("Orders");
        ordersList= findViewById(R.id.orders_list);
        ordersList.setLayoutManager(new LinearLayoutManager(this));

        btnHistory = (Button) findViewById(R.id.seller_history_button);

        btnHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SellerNewOrdersActivity.this, SellerHistoryActivity.class);
                startActivity(intent);
            }
        });
        back = findViewById(R.id.backBtn);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }


    @Override
    protected void onStart() {
        super.onStart();

       FirebaseRecyclerOptions<AdminOrders> options = new
               FirebaseRecyclerOptions.Builder<AdminOrders>()
               .setQuery(ordersRef.orderByChild("state").startAt("not shipped").endAt("not shipped"), AdminOrders.class)
               .build();
       FirebaseRecyclerAdapter<AdminOrders, AdminOrdersViewHolder> adapter = new FirebaseRecyclerAdapter<AdminOrders, AdminOrdersViewHolder>(options) {
           @Override
           protected void onBindViewHolder(@NonNull AdminOrdersViewHolder holder, @SuppressLint("RecyclerView") int position, @NonNull AdminOrders model) {
               holder.userName.setText("Name: " + model.getName());
               holder.userPhoneNumber.setText("Phone: " + model.getNumber());
               holder.userTotalPrice.setText("Total Amount:  ₱ " + model.getTotalAmount());
               holder.userDateTime.setText("Order at: " + model.getDate() + " " + model.getTime());
               holder.userShippingAddress.setText("Shipping Address: " + model.getAddress());
               holder.userOrderStatus.setText("Order Status: " + model.getState());
               auth = FirebaseAuth.getInstance();
               user = auth.getCurrentUser();
               holder.showOrderButton.setOnClickListener(new View.OnClickListener() {
                   @Override
                   public void onClick(View v) {
                       Intent intent = new Intent(SellerNewOrdersActivity.this, SellerUserProductsActivity.class);
                       intent.putExtra("orderID", model.getOrderid());
                       startActivity(intent);
                   }
               });

               holder.itemView.setOnClickListener(new View.OnClickListener() {
                   @Override
                   public void onClick(View v) {

                       CharSequence option[] = new CharSequence[]{
                               "Yes",
                               "No"
                       };
                       AlertDialog.Builder builder = new AlertDialog.Builder(SellerNewOrdersActivity.this);
                       builder.setTitle("Have you shipped this ordered products");
                       builder.setItems(option, new DialogInterface.OnClickListener() {
                           @Override
                           public void onClick(DialogInterface dialog, int which) {
                                DatabaseReference orderHistory = FirebaseDatabase.getInstance().getReference().child("Order History").child(model.getUid())
                                                .child(model.getOrderid());
                               if(which == 0)
                               {
                                   String uid = getRef(position).getKey();
                                   HashMap<String, Object> ordersMap = new HashMap<>();
                                   ordersMap.put("date", model.getDate());
                                   ordersMap.put("time", model.getTime());
                                   ordersMap.put("totalAmount", model.getTotalAmount());
                                   ordersMap.put("uid", model.getUid());
                                   ordersMap.put("address", model.getAddress());
                                   ordersMap.put("orderid", model.getOrderid());
                                   ordersMap.put("state", "shipped");
                                   orderHistory.updateChildren(ordersMap);
                                   ordersRef.child(model.getOrderid()).updateChildren(ordersMap);
                               }
                           }
                       });
                       builder.show();

                   }
               });

           }

           @NonNull
           @Override
           public AdminOrdersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
               View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.orders_layout, parent, false);
               return new AdminOrdersViewHolder(view);
           }
       };
       ordersList.setAdapter(adapter);
       adapter.startListening();
    }

//    private void RemoveOrder() {
//
//    }

    public static class AdminOrdersViewHolder extends RecyclerView.ViewHolder{

        public TextView userName, userPhoneNumber, userTotalPrice, userDateTime, userShippingAddress, userOrderStatus;
        public Button showOrderButton;
        public AdminOrdersViewHolder(@NonNull View itemView) {
            super(itemView);

            userName = itemView.findViewById(R.id.order_user_name);
            userPhoneNumber = itemView.findViewById(R.id.order_phone_number);
            userTotalPrice = itemView.findViewById(R.id.order_total_price);
            userDateTime = itemView.findViewById(R.id.order_date_time);
            userShippingAddress = itemView.findViewById(R.id.order_address_city);
            showOrderButton = itemView.findViewById(R.id.show_all_products);
            userOrderStatus = itemView.findViewById(R.id.status_order);


        }
    }
}