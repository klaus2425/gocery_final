package com.system.gocery_final;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;
import com.system.gocery_final.Model.Products;
import com.system.gocery_final.Seller.SellerMaintainProductsActivity;
import com.system.gocery_final.ViewHolder.ProductViewHolder;

public class SearchProductsActivity extends AppCompatActivity {

    private ImageView SearchBtn;
    private EditText inputText;
    private RecyclerView searchList;
    private String searchInput;
    private String type;
    private String sessionID = "";
    private Button searchCat;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_products);
        searchInput = "";
        type = "pname";
        searchCat = (Button) findViewById(R.id.search_cat);
        sessionID = getIntent().getStringExtra("session");
        inputText= (EditText) findViewById(R.id.search_product_name);
        SearchBtn = (ImageView) findViewById(R.id.search_button);
        searchList = findViewById(R.id.search_list);
        type = getIntent().getStringExtra("type");
        searchList.setLayoutManager(new LinearLayoutManager(SearchProductsActivity.this));
        type = "pname";
        SearchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchInput = inputText.getText().toString();
                show_results();
            }
        });
        searchCat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchInput = inputText.getText().toString();
                cat_search();
            }
        });
        show_results();
    }

    protected void show_results() {
        Toast.makeText(this, type, Toast.LENGTH_SHORT).show();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Products");
        FirebaseRecyclerOptions<Products> options = new FirebaseRecyclerOptions.Builder<Products>().setQuery(reference.orderByChild("pname")
                .startAt(searchInput).endAt(searchInput+"\uf8ff"),Products.class).build();

        FirebaseRecyclerAdapter<Products, ProductViewHolder> adapter = new FirebaseRecyclerAdapter<Products, ProductViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull ProductViewHolder holder, int position, @NonNull Products model) {
                holder.txtProdName.setText(model.getPname());
                holder.txtProdPrice.setText("₱ " + model.getPrice());
                holder.txtStock.setText("Stock: " +model.getQuantity());
                Picasso.get().load(model.getImage()).into(holder.imageView);

                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(type.equals("Admin")){
                            Intent intent = new Intent(SearchProductsActivity.this, SellerMaintainProductsActivity.class);
                            intent.putExtra("pid",model.getPid());
                            startActivity(intent);
                        } else {
                            String session = getIntent().getStringExtra("session");
                            Intent intent = new Intent(SearchProductsActivity.this, ProductDetailsActivity.class);
                            intent.putExtra("pid", model.getPid());
                            intent.putExtra("session", session);
                            intent.putExtra("session", sessionID);
                            intent.putExtra("allow", "false");
                            startActivity(intent);
                        }
                    }
                });
            }

            @NonNull
            @Override
            public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_items_layout, parent, false);
                ProductViewHolder holder = new ProductViewHolder(view);
                return holder;
            }
        };

        searchList.setAdapter(adapter);
        adapter.startListening();
    }

    public void cat_search(){
        super.onStart();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Products");
        FirebaseRecyclerOptions<Products> options = new FirebaseRecyclerOptions.Builder<Products>().setQuery(reference.orderByChild("category")
                .startAt(searchInput).endAt(searchInput+"\uf8ff"),Products.class).build();

        FirebaseRecyclerAdapter<Products, ProductViewHolder> adapter = new FirebaseRecyclerAdapter<Products, ProductViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull ProductViewHolder holder, int position, @NonNull Products model) {
                holder.txtProdName.setText(model.getPname());
                holder.txtProdPrice.setText("₱ " + model.getPrice());
                holder.txtStock.setText("Stock: " +model.getQuantity());
                Picasso.get().load(model.getImage()).into(holder.imageView);

                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(type.equals("Admin")){
                            Intent intent = new Intent(SearchProductsActivity.this, SellerMaintainProductsActivity.class);
                            intent.putExtra("pid",model.getPid());
                            startActivity(intent);
                        } else {
                            String session = getIntent().getStringExtra("session");
                            Intent intent = new Intent(SearchProductsActivity.this, ProductDetailsActivity.class);
                            intent.putExtra("pid", model.getPid());
                            intent.putExtra("session", session);
                            intent.putExtra("session", sessionID);
                            intent.putExtra("allow", "false");
                            startActivity(intent);
                        }
                    }
                });
            }

            @NonNull
            @Override
            public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_items_layout, parent, false);
                ProductViewHolder holder = new ProductViewHolder(view);
                return holder;
            }
        };

        searchList.setAdapter(adapter);
        adapter.startListening();
    }
}