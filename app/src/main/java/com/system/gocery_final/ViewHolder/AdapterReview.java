package com.system.gocery_final.ViewHolder;

import android.content.Context;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import com.system.gocery_final.Model.ModelReview;
import com.system.gocery_final.R;

import java.util.ArrayList;
import java.util.Calendar;



import de.hdodenhof.circleimageview.CircleImageView;

public class AdapterReview extends RecyclerView.Adapter<AdapterReview.HolderReview>{
    private Context context;
    private ArrayList<ModelReview> reviewArrayList;
    private FirebaseUser user;
    private FirebaseAuth firebaseauth;



    public AdapterReview(Context context, ArrayList<ModelReview> reviewArrayList) {
        this.context = context;
        this.reviewArrayList = reviewArrayList;

    }

    @NonNull
    @Override
    public HolderReview onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.row_reviews,parent,false);
        Toast.makeText(context, "called", Toast.LENGTH_SHORT).show();

        return new HolderReview(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HolderReview holder, int position) {
        firebaseauth = FirebaseAuth.getInstance();
        user= firebaseauth.getCurrentUser();
        ModelReview modelReview = reviewArrayList.get(position);
        String uid = modelReview.getUid();
        String ratings = modelReview.getRatings();
        String timestamp = modelReview.getTimestamp();
        String review = modelReview.getReview();

        loadUserDetail(modelReview,holder);
        loadReviews(modelReview, holder);

        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(Long.parseLong(timestamp));

        String dateFormat = DateFormat.format("dd/MM/yyyy",cal).toString();

        holder.dateTv.setText(dateFormat);
        holder.ratingBar.setRating(Float.parseFloat(ratings));
        holder.reviewTv.setText(review);
    }

    private void loadUserDetail(ModelReview modelReview, HolderReview holder) {
        String uid = modelReview.getUid();

        DatabaseReference ref= FirebaseDatabase.getInstance().getReference("Users");
        ref.child(uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    String name = snapshot.child("firstName").getValue().toString() + " " + snapshot.child("lastName").getValue().toString();


                    holder.nameTv.setText(name);
                    try {
                        Picasso.get().load(snapshot.child("image").getValue().toString()).placeholder(R.drawable.ic_person_24).into(holder.profileTv);
                    } catch (Exception e) {
                        holder.profileTv.setImageResource(R.drawable.ic_person_24);
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private float ratingSum = 0;
    private void loadReviews(ModelReview modelReview, HolderReview holder) {

        String productID = modelReview.getUid();
        DatabaseReference ref =FirebaseDatabase.getInstance().getReference();
        ref.child("Products").child(productID).child("reviews").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ratingSum= 0;
                if(snapshot.exists()) {
                    Toast.makeText(context, "Retrieved1231", Toast.LENGTH_SHORT).show();
                    for (DataSnapshot s : snapshot.getChildren()) {
                        float rating = Float.parseFloat(s.child("ratings").getValue().toString());
                        ratingSum = ratingSum + rating;
                    }
                }
                long numberOfReviews = snapshot.getChildrenCount();
                float avgRating = ratingSum / numberOfReviews;
                holder.ratingBar.setRating(avgRating);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
    @Override
    public int getItemCount() {
        return reviewArrayList.size();
    }

    class HolderReview extends RecyclerView.ViewHolder{

        private CircleImageView profileTv;
        private TextView nameTv, dateTv, reviewTv;
        private RatingBar ratingBar;


        public HolderReview(@NonNull View itemView) {
            super(itemView);

            profileTv = itemView.findViewById(R.id.profileTv);
            nameTv = itemView.findViewById(R.id.nameTv);
            ratingBar = itemView.findViewById(R.id.ratingBar);
            dateTv = itemView.findViewById(R.id.dateTv);
            reviewTv = itemView.findViewById(R.id.reviewTv);
        }
    }
}
