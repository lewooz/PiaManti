package com.levent.pia.ViewHolder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.levent.pia.Interface.ItemClickListener;
import com.levent.pia.R;

public class CartViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView txtProductName, txtProductQuantity, txtProductPorsiyon, txtProductPrice, txtSarimsakDurumu, txtSosDurumu, txtTeslimatDurumu;
    public ImageView cartImage;
    private ItemClickListener itemClickListener;

    public TextView getTxtProductName() {
        return txtProductName;
    }

    public void setTxtProductName(TextView txtProductName) {
        this.txtProductName = txtProductName;
    }

    public TextView getTxtProductQuantity() {
        return txtProductQuantity;
    }

    public void setTxtProductQuantity(TextView txtProductQuantity) {
        this.txtProductQuantity = txtProductQuantity;
    }

    public TextView getTxtProductPorsiyon() {
        return txtProductPorsiyon;
    }

    public void setTxtProductPorsiyon(TextView txtProductPorsiyon) {
        this.txtProductPorsiyon = txtProductPorsiyon;
    }

    public TextView getTxtProductPrice() {
        return txtProductPrice;
    }

    public void setTxtProductPrice(TextView txtProductPrice) {
        this.txtProductPrice = txtProductPrice;
    }

    public ItemClickListener getItemClickListener() {
        return itemClickListener;
    }

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public CartViewHolder(@NonNull View itemView) {
        super(itemView);

        txtProductName = itemView.findViewById(R.id.cart_name);
        txtProductQuantity = itemView.findViewById(R.id.cart_quantity);
        txtProductPorsiyon = itemView.findViewById(R.id.cart_porsiyon);
        txtProductPrice = itemView.findViewById(R.id.cart_price);
        txtSarimsakDurumu = itemView.findViewById(R.id.cart_sarımsak_durumu);
        txtSosDurumu = itemView.findViewById(R.id.cart_sos_durumu);
        txtTeslimatDurumu=itemView.findViewById(R.id.teslimat);
        cartImage = itemView.findViewById(R.id.cart_bin);
    }

    @Override
    public void onClick(View view) {

        itemClickListener.onClick(view,getAdapterPosition(),false);

    }

}
