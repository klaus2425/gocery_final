package com.system.gocery_final;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.annotation.NonNull;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.system.gocery_final.Seller.SellerMaintainProductsActivity;
import com.system.gocery_final.Model.Products;
import com.system.gocery_final.ViewHolder.ProductViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import io.paperdb.Paper;

public class HomeActivity extends AppCompatActivity{
    private DatabaseReference ProductsRef;
    private RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    private FirebaseUser user;
    private FirebaseAuth auth;
    private String type = "";
    private EditText searchHome;
    public static final String SHARED_PREFS = "sharedPrefs";
    public static final String SESSION = "session";
    public static final String ONGOING = "ongoing";
    private String session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);



        searchHome = (EditText) findViewById(R.id.search_home);
        searchHome.setFocusable(false);
        loadData();
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        if(bundle!= null){
            type = getIntent().getExtras().get("Admin").toString();
        }

        //Verification Check
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();


        ProductsRef = FirebaseDatabase.getInstance().getReference().child("Products");
        Paper.init(this);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        if(type.equals("Admin")){
            ((ViewGroup) fab.getParent()).removeView(fab);
            View bottomAppBar = findViewById(R.id.bottom_bar);
            ((ViewGroup) bottomAppBar.getParent()).removeView(bottomAppBar);
        } else {
            BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_nav_view);
            bottomNavigationView.getMenu().getItem(0).setChecked(false);
            bottomNavigationView.setOnNavigationItemSelectedListener(bNavListener);

        }
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!type.equals("Admin")){
                    Intent intent = new Intent(HomeActivity.this, CartActivity.class);
                    intent.putExtra("session", session);
                    startActivity(intent);
                }
            }
        });



        recyclerView = findViewById(R.id.recycler_menu);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getApplicationContext(), 2);
        recyclerView.setLayoutManager(gridLayoutManager);



        searchHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, SearchProductsActivity.class);
                intent.putExtra("type", type);
                intent.putExtra("session", session);
                startActivity(intent);
                Animatoo.INSTANCE.animateFade(HomeActivity.this);
            }
        });

    }


    @Override
    protected void onStart()
    {
        super.onStart();

        FirebaseRecyclerOptions<Products> options =
                new FirebaseRecyclerOptions.Builder<Products>()
                        .setQuery(ProductsRef, Products.class)
                        .build();


        FirebaseRecyclerAdapter<Products, ProductViewHolder> adapter =
                new FirebaseRecyclerAdapter<Products, ProductViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull ProductViewHolder holder, int position, @NonNull Products model)
                    {
                        holder.txtProdName.setText(model.getPname());
                        holder.txtStock.setText("Stock: " + model.getQuantity());
                        holder.txtProdPrice.setText("₱ " + model.getPrice());
                        Picasso.get().load(model.getImage()).into(holder.imageView);



                        holder.itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if(type.equals("Admin")){
                                    Intent intent = new Intent(HomeActivity.this, SellerMaintainProductsActivity.class);
                                    intent.putExtra("pid",model.getPid());
                                    startActivity(intent);
                                }else{
                                    Intent intent = new Intent(HomeActivity.this,ProductDetailsActivity.class);
                                    intent.putExtra("pid",model.getPid());
                                    intent.putExtra("session", session);
                                    intent.putExtra("allow", "false");
                                    startActivity(intent);
                                }
                            }
                        });
                    }

                    @NonNull
                    @Override
                    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
                    {
                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_items_layout, parent, false);
                        ProductViewHolder holder = new ProductViewHolder(view);
                        return holder;
                    }
                };

        recyclerView.setAdapter(adapter);
        adapter.startListening();

    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }



    private BottomNavigationView.OnNavigationItemSelectedListener bNavListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            int id = item.getItemId();
            if (id == R.id.nav_categories)
            {
                Intent intent = new Intent(HomeActivity.this, SelectCategoryActivity.class);
                intent.putExtra("session", session);
                startActivity(intent);
            }
            else if (id == R.id.nav_settings)
            {
                if(!type.equals("Admin")){
                    Intent intent = new Intent(HomeActivity.this, SettingsActivity.class);
                    startActivity(intent);
                }
            }
            else if(id == R.id.nav_history){
                Intent intent = new Intent(HomeActivity.this, OrderHistoryActivity.class);
                intent.putExtra("session", session);
                startActivity(intent);
            }
            return true;
        }
    };



    public void saveData(){
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Calendar calForDate = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd, yyyy");
        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
        DatabaseReference refKey=FirebaseDatabase.getInstance().getReference();
        session = refKey.push().getKey();

        editor.putString(SESSION, session);
        editor.putBoolean(ONGOING, true);
        editor.commit();
    }

    public void loadData(){
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        if(!sharedPreferences.getBoolean(ONGOING, false)){
            saveData();
        }
        else {
            session = sharedPreferences.getString(SESSION, "session");
        }
    }
}
