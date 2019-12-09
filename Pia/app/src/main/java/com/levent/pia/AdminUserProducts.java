package com.levent.pia;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.levent.pia.Model.Cart;
import com.levent.pia.Model.Products;
import com.levent.pia.ViewHolder.CartViewHolder;

public class AdminUserProducts extends AppCompatActivity {

    String orderNumber,dbmail;
    RecyclerView recyclerView;
    DatabaseReference cartListRef;
    LinearLayoutManager linearLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_user_products);

        dbmail=getIntent().getStringExtra("mail");
        orderNumber=getIntent().getStringExtra("orderNumber");
        recyclerView=findViewById(R.id.siparişayrıntı_recycler_view);
        cartListRef = FirebaseDatabase.getInstance().getReference().child("Cart List").child("Admin View").child(dbmail).child(orderNumber);

        linearLayoutManager= new LinearLayoutManager(AdminUserProducts.this);

        recyclerView.setLayoutManager(linearLayoutManager);


    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerOptions<Cart> options = new FirebaseRecyclerOptions.Builder<Cart>()
                .setQuery(cartListRef,Cart.class)
                .build();

        FirebaseRecyclerAdapter<Cart, CartViewHolder> adapter = new FirebaseRecyclerAdapter<Cart, CartViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull CartViewHolder cartViewHolder, int i, @NonNull Cart cart) {
                cartViewHolder.txtProductName.setText(cart.getName());
                cartViewHolder.txtProductPrice.setText(cart.getPrice());
                cartViewHolder.txtProductQuantity.setText(cart.getQuantity()+" Adet");
                cartViewHolder.txtProductPorsiyon.setText(cart.getPorsiyon()+" Porsiyon");

                cartViewHolder.cartImage.setVisibility(View.GONE);

                if(cart.getName().equals("Kayseri Ev Mantısı")||cart.getName().equals("Çıtır Mantı")||cart.getName().equals("Patatesli Mantı"))
                {
                    cartViewHolder.txtSarimsakDurumu.setText(cart.getSarimsakdurumu());
                    cartViewHolder.txtSosDurumu.setText(cart.getSosdurumu());
                    cartViewHolder.txtSarimsakDurumu.setVisibility(View.VISIBLE);
                    cartViewHolder.txtSosDurumu.setVisibility(View.VISIBLE);
                }

                if(!cart.getTeslimat().equals("T:  S: "))
                {
                    cartViewHolder.txtTeslimatDurumu.setText(cart.getTeslimat());
                    cartViewHolder.txtTeslimatDurumu.setVisibility(View.VISIBLE);
                    cartViewHolder.txtProductPorsiyon.setText("1 KG");
                }

            }

            @NonNull
            @Override
            public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_items_layout,parent,false);

                CartViewHolder holder = new CartViewHolder(view);
                return holder;
            }
        };

        recyclerView.setAdapter(adapter);
        adapter.startListening();
    }
}
