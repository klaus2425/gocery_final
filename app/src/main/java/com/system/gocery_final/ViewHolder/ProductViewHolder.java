package com.system.gocery_final.ViewHolder;

import android.view.View;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.system.gocery_final.Interface.ItemClickListener;
import com.system.gocery_final.R;


public class ProductViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
{
    public TextView txtProdName, txtProdDescription, txtProdPrice;
    public ImageView imageView;
    public ItemClickListener listner;


    public ProductViewHolder(View itemView)
    {
        super(itemView);


        imageView = (ImageView) itemView.findViewById(R.id.product_image);
        txtProdName = (TextView) itemView.findViewById(R.id.product_name);
        txtProdDescription = (TextView) itemView.findViewById(R.id.product_description);
        txtProdPrice = (TextView) itemView.findViewById(R.id.product_price);
    }

    public void setItemClickListner(ItemClickListener listner)
    {
        this.listner = listner;
    }

    @Override
    public void onClick(View view)
    {
        listner.onClick(view, getAdapterPosition(), false);
    }
}
