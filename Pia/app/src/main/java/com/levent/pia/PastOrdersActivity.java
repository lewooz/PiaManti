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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.levent.pia.Model.Cart;
import com.levent.pia.ViewHolder.CartViewHolder;

public class PastOrdersActivity extends AppCompatActivity {

    FirebaseAuth mAuth;
    FirebaseUser currentOnlineUser;
    DatabaseReference cartListRef;
    String mail,dbmail,orderNumber;
    RecyclerView pastOrdersRecyclerView;
    LinearLayoutManager linearLayoutManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_past_orders);

        mAuth= FirebaseAuth.getInstance();
        currentOnlineUser=mAuth.getCurrentUser();

        linearLayoutManager = new LinearLayoutManager(this);

        pastOrdersRecyclerView = findViewById(R.id.past_orders_recyclerview);

        pastOrdersRecyclerView.setLayoutManager(linearLayoutManager);

        mail= currentOnlineUser.getEmail();
        dbmail=mail.replace(".",",");

        orderNumber=getIntent().getStringExtra("siparişno");


        cartListRef = FirebaseDatabase.getInstance().getReference().child("Cart List").child("Admin View").child(dbmail).child(orderNumber);

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


            }

            @NonNull
            @Override
            public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_items_layout,parent,false);

                CartViewHolder holder = new CartViewHolder(view);
                return holder;

            }
        };

        pastOrdersRecyclerView.setAdapter(adapter);
        adapter.startListening();

    }
}
