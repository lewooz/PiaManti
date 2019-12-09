package com.levent.pia.ViewHolder;

import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.levent.pia.Interface.ItemClickListener;
import com.levent.pia.R;

public class ProductViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView name,price,description;

    public ItemClickListener listener;
    public RelativeLayout relativeLayout;
    public CardView cardView;


    public ProductViewHolder(@NonNull View itemView) {
        super(itemView);

        name= itemView.findViewById(R.id.ürün_adı);
        price= itemView.findViewById(R.id.ürün_fiyatı);
        description= itemView.findViewById(R.id.ürün_açıklaması);



        relativeLayout=itemView.findViewById(R.id.menu_relative);
        cardView=itemView.findViewById(R.id.menu_card);


    }

    public void setItemClickListener (ItemClickListener listener)
    {
        this.listener=listener;
    }

    @Override
    public void onClick(View view) {

        listener.onClick(view,getAdapterPosition(),false);
    }
}
