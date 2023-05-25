package com.system.gocery_final.ViewHolder;

import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.system.gocery_final.R;

import de.hdodenhof.circleimageview.CircleImageView;

public class AdapterReview {

    class HolderReview extends RecyclerView.ViewHolder{

        private CircleImageView profileTv;
        private TextView nameTv;
        private RatingBar ratingBar;

        public HolderReview(@NonNull View itemView) {
            super(itemView);

            profileTv = itemView.findViewById(R.id.profileTv);
            nameTv = itemView.findViewById(R.id.nameTv);
            ratingBar = itemView.findViewById(R.id.ratingBar);

        }
    }
}
