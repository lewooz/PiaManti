package com.levent.pia;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
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

public class AdminPastOrdersActivity extends AppCompatActivity {



    DatabaseReference cartListRef;
    String mail,dbmail,orderNumber;
    RecyclerView pastOrdersRecyclerView;
    LinearLayoutManager linearLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_past_orders);

        linearLayoutManager = new LinearLayoutManager(this);

        pastOrdersRecyclerView = findViewById(R.id.admin_past_orders_recyclerview);

        pastOrdersRecyclerView.setLayoutManager(linearLayoutManager);

        orderNumber=getIntent().getStringExtra("siparişno");
        dbmail =getIntent().getStringExtra("mail");

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

