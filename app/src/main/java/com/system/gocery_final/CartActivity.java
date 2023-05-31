package com.system.gocery_final;

import static com.system.gocery_final.Prevalent.Prevalent.currentOnlineUser;

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
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.system.gocery_final.Model.Cart;
import com.system.gocery_final.Prevalent.Prevalent;
import com.system.gocery_final.ViewHolder.CartViewHolder;

public class CartActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private Button nextProcessButton;
    private TextView txtTotalAmount, txtMsg1;
    private FirebaseAuth auth;
    private FirebaseUser user;
    private ImageView empty;
    private FirebaseRecyclerAdapter<Cart, CartViewHolder> adapter;
    private ImageButton back;

    private int overTotalPrice = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        recyclerView = findViewById(R.id.cart_list);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        nextProcessButton = (Button) findViewById(R.id.next_process_button);
        txtTotalAmount = (TextView) findViewById(R.id.total_price);
        txtMsg1 = (TextView) findViewById(R.id.msg1);
        empty = (ImageView) findViewById(R.id.empty_logo);
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        back = findViewById(R.id.backBtn);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        nextProcessButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txtTotalAmount.setText("Total Price: ₱ " + overTotalPrice );
                Intent intent = new Intent(CartActivity.this,ConfirmFinalOrderActivity.class);
                intent.putExtra("Total Price", String.valueOf(overTotalPrice));
                intent.putExtra("session", getIntent().getExtras().get("session").toString());
                startActivity(intent);
            }
        });


    }

    @Override
    protected void onStart() {
        super.onStart();

        CheckOrderState();
        final DatabaseReference cartListRef = FirebaseDatabase.getInstance().getReference().child("Cart List");
        final DatabaseReference cartListHistory = FirebaseDatabase.getInstance().getReference();

        FirebaseRecyclerOptions<Cart> options =
                new FirebaseRecyclerOptions.Builder<Cart>().setQuery(cartListRef.child("User View").child(user.getUid()).child("Products"), Cart.class).build();

        adapter = new FirebaseRecyclerAdapter<Cart, CartViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull CartViewHolder holder, int position, @NonNull Cart model) {
                holder.txtProductPrice.setText("Price: ₱ " + model.getPrice());
                holder.txtProductName.setText(model.getPname());
                holder.txtProductQuantity.setText(model.getQuantity());
                int oneTypeProductPrice = ((Integer.valueOf(model.getPrice()))) * ((Integer.valueOf(model.getQuantity())));
                overTotalPrice = overTotalPrice+ oneTypeProductPrice;
                txtTotalAmount.setText("Price: ₱ " + overTotalPrice );

                if(!txtTotalAmount.getText().toString().equals("Cart is Empty")){
                    nextProcessButton.setVisibility(View.VISIBLE);
                    empty.setVisibility(View.GONE);
                }
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        CharSequence options[] = new CharSequence[]{
                                "Edit",
                                "Remove"
                        };
                        AlertDialog.Builder builder = new AlertDialog.Builder(CartActivity.this);
                        builder.setTitle("Cart Options");

                        builder.setItems(options, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                if(i==0){
                                    Intent intent = new Intent(CartActivity.this,ProductDetailsActivity.class);
                                    intent.putExtra("pid",model.getPid());
                                    startActivity(intent);
                                }
                                if(i==1){
                                    cartListHistory.child("Order History").child(user.getUid()).child(getIntent().getExtras().get("session").toString()).child("products")
                                                    .child(model.getPid()).removeValue();
                                    cartListRef.child("User View").child(user.getUid()).child("Products")
                                            .child(model.getPid()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {

                                                    if(task.isSuccessful()){
                                                        Toast.makeText(CartActivity.this, "Item Removed Successfully", Toast.LENGTH_SHORT).show();
                                                        startActivity(getIntent());
                                                        finish();
                                                        overridePendingTransition(0, 0);
                                                    }
                                                }
                                            });
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
        recyclerView.setAdapter(adapter);
        adapter.startListening();
    }

    private void CheckOrderState(){
        DatabaseReference ordersRef;
        ordersRef = FirebaseDatabase.getInstance().getReference().child("Orders").child(user.getUid());
//        ordersRef.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                if(snapshot.exists()){
//                    String shippingState = snapshot.child("state").getValue().toString();
//                    String userName = snapshot.child("name").getValue().toString();
//
//                    if (shippingState.equals("shipped")){
//                        txtTotalAmount.setText("Order is now in transit");
//                        recyclerView.setVisibility(View.GONE);
//                        txtMsg1.setVisibility(View.VISIBLE);
//                        nextProcessButton.setVisibility(View.GONE);
//                        empty.setVisibility(View.GONE);
//                        Toast.makeText(CartActivity.this, "Please wait for your order to arrive.",Toast.LENGTH_SHORT).show();
//                    } else if (shippingState.equals("not shipped")) {
//                        txtTotalAmount.setText("Order is still processing");
//                        recyclerView.setVisibility(View.GONE);
//                        txtMsg1.setVisibility(View.VISIBLE);
//                        txtMsg1.setText("Your order is still being verified. Wait for your order to arrive before ordering again.");
//                        nextProcessButton.setVisibility(View.GONE);
//                        empty.setVisibility(View.GONE);
//                        Toast.makeText(CartActivity.this, "Please wait for your order to be verified.",Toast.LENGTH_SHORT).show();
//                    }
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });

    }
}