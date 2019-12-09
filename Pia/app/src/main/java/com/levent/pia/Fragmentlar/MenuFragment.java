package com.levent.pia.Fragmentlar;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.ContactsContract;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Fragment;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.andremion.counterfab.CounterFab;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.levent.pia.MenuFeedActivity;
import com.levent.pia.Model.Products;
import com.levent.pia.ProductDetailsActivity;
import com.levent.pia.R;
import com.levent.pia.ViewHolder.ProductViewHolder;
import com.squareup.picasso.Picasso;

public class MenuFragment extends Fragment {

    public MenuFragment() {

    }

    DatabaseReference productsRef, kilolukproductsRef;
    RecyclerView recyclerView, kilolukrecyclerview;
    RecyclerView.LayoutManager layoutManager, kiloluklayoutmanager;
    ImageView ok;
    NestedScrollView nestedScrollView;
    Animator animator;
    String kontrol,mail,dbmail;
    FirebaseUser currentOnlineUser;
    ProductViewHolder holder;
    FirebaseAuth mAuth;


    static FirebaseRecyclerAdapter<Products, ProductViewHolder> adapter;
    static FirebaseRecyclerAdapter<Products, ProductViewHolder> kilolukadapter;
    public static CounterFab fab;
    int fabCounter;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        productsRef= FirebaseDatabase.getInstance().getReference().child("Products").child("menu");
        kilolukproductsRef= FirebaseDatabase.getInstance().getReference().child("Products").child("kiloluk");
        kontrol="0";

        mAuth= FirebaseAuth.getInstance();
        currentOnlineUser=mAuth.getCurrentUser();

        mail=currentOnlineUser.getEmail();
        dbmail=mail.replace(".",",");
        View view = inflater.inflate(R.layout.menufragment, container , false);

        fab = view.findViewById(R.id.counter_fab);
        SharedPreferences preferences = this.getActivity().getSharedPreferences("com.levent.pia", Context.MODE_PRIVATE);


            fabCounter = preferences.getInt("counter", 0);
            System.out.println("FABCOUNTER: "+fabCounter);

            fab.setCount(fabCounter);

        ok=view.findViewById(R.id.arrow_image);
        recyclerView=view.findViewById(R.id.recyclerview);
        kilolukrecyclerview = view.findViewById(R.id.recyclerview_kiloluk);
        nestedScrollView = view.findViewById(R.id.nestedscrollview);
        nestedScrollView.setSmoothScrollingEnabled(true);


        //kilolukrecyclerview.setHasFixedSize(true);
        //recyclerView.setHasFixedSize(true);
        layoutManager=new LinearLayoutManager(getActivity());
        kiloluklayoutmanager = new LinearLayoutManager(getActivity());

        animator = AnimatorInflater.loadAnimator(getActivity(),R.animator.arrow_animation);
        animator.setTarget(ok);
        animator.start();
        animator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {

            }

            @Override
            public void onAnimationEnd(Animator animator) {

                ok.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });

        recyclerView.setLayoutManager(layoutManager);
        kilolukrecyclerview.setLayoutManager(kiloluklayoutmanager);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getActivity(),MenuFeedActivity.class);
                intent.putExtra("nerden","sepet");
                startActivity(intent);
            }
        });

        return view;



    }



    @Override
    public void onStart() {
        super.onStart();


        FirebaseRecyclerOptions<Products> options = new FirebaseRecyclerOptions.Builder<Products>()
                .setQuery(productsRef.orderByChild("order"),Products.class)
                .build();

          adapter = new FirebaseRecyclerAdapter<Products, ProductViewHolder>(options) {

              @Override
            protected void onBindViewHolder(@NonNull ProductViewHolder productViewHolder, int i, @NonNull final Products products) {



                if(products.getOpen().equals("1")){
                productViewHolder.name.setText(products.getName());

                productViewHolder.description.setText(products.getDescription());
                productViewHolder.price.setText(products.getPrice()+"₺");



                }else
                    {
                        productViewHolder.name.setVisibility(View.GONE);
                        productViewHolder.description.setVisibility(View.GONE);
                        productViewHolder.price.setVisibility(View.GONE);
                        productViewHolder.relativeLayout.setVisibility(View.GONE);
                        productViewHolder.cardView.setVisibility(View.GONE);
                        productViewHolder.itemView.setLayoutParams(new RecyclerView.LayoutParams(0, 0));
                    }

                productViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        DatabaseReference ordersRef = FirebaseDatabase.getInstance().getReference().child("Orders").child(dbmail);

                        ordersRef.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                if(dataSnapshot.child("siparisdurumu").exists()) {

                                    String shippingState = dataSnapshot.child("siparisdurumu").getValue().toString();
                                    if (shippingState.equals("Onay Aşamasında"))
                                    {
                                        Toast.makeText(getActivity(),"Siparişiniz elinize ulaştırılana kadar sepetinize ürün ekleyemezsiniz.",Toast.LENGTH_LONG).show();
                                    } else if (shippingState.equals("Siparişiniz Hazırlanıyor"))
                                    {
                                        Toast.makeText(getActivity(),"Siparişiniz elinize ulaştırılana kadar sepetinize ürün ekleyemezsiniz.",Toast.LENGTH_LONG).show();
                                    }
                                        else if(shippingState.equals("Siparişinizi jet hızıyla getiriyoruz!!!"))
                                    {
                                        Toast.makeText(getActivity(),"Siparişiniz elinize ulaştırılana kadar sepetinize ürün ekleyemezsiniz.",Toast.LENGTH_LONG).show();
                                    }
                                    else {
                                        Intent intent = new Intent(getActivity(), ProductDetailsActivity.class);
                                        intent.putExtra("pName",products.getName());
                                        intent.putExtra("kilolukdurumu",false);
                                        intent.putExtra("pDescription",products.getDescription());
                                        intent.putExtra("pPrice",products.getPrice());
                                        intent.putExtra("kontrol",kontrol);
                                        startActivity(intent);
                                    }
                                }else
                                    {
                                        Intent intent = new Intent(getActivity(), ProductDetailsActivity.class);
                                        intent.putExtra("pName",products.getName());
                                        intent.putExtra("kilolukdurumu",false);
                                        intent.putExtra("pDescription",products.getDescription());
                                        intent.putExtra("pPrice",products.getPrice());
                                        intent.putExtra("kontrol",kontrol);
                                        startActivity(intent);
                                    }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });

                    }
                });

            }

            @NonNull
            @Override
            public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.product_items_layout,parent,false);

                holder = new ProductViewHolder(view);


                return holder;
            }


        };

        FirebaseRecyclerOptions<Products> kilolukOptions = new FirebaseRecyclerOptions.Builder<Products>()
                .setQuery(kilolukproductsRef.orderByChild("order"),Products.class)
                .build();

        kilolukadapter = new FirebaseRecyclerAdapter<Products, ProductViewHolder>(kilolukOptions) {
            @Override
            protected void onBindViewHolder(@NonNull ProductViewHolder productViewHolder, int i, @NonNull final Products products) {


                    productViewHolder.name.setText(products.getName());

                    productViewHolder.description.setVisibility(View.INVISIBLE);
                    productViewHolder.price.setText(products.getPrice()+"₺");


                productViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        DatabaseReference ordersRef = FirebaseDatabase.getInstance().getReference().child("Orders").child(dbmail);

                        ordersRef.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                if(dataSnapshot.child("siparisdurumu").exists()) {

                                    String shippingState = dataSnapshot.child("siparisdurumu").getValue().toString();
                                    if (shippingState.equals("Onay Aşamasında"))
                                    {
                                        Toast.makeText(getActivity(),"Siparişiniz elinize ulaştırılana kadar sepetinize ürün ekleyemezsiniz.",Toast.LENGTH_LONG).show();
                                    } else if (shippingState.equals("Siparişiniz Hazırlanıyor"))
                                    {
                                        Toast.makeText(getActivity(),"Siparişiniz elinize ulaştırılana kadar sepetinize ürün ekleyemezsiniz.",Toast.LENGTH_LONG).show();
                                    }
                                    else if(shippingState.equals("Siparişinizi jet hızıyla getiriyoruz!!!"))
                                    {
                                        Toast.makeText(getActivity(),"Siparişiniz elinize ulaştırılana kadar sepetinize ürün ekleyemezsiniz.",Toast.LENGTH_LONG).show();
                                    }
                                    else {
                                        Intent intent = new Intent(getActivity(), ProductDetailsActivity.class);
                                        intent.putExtra("pName",products.getName());
                                        intent.putExtra("kilolukdurumu",true);
                                        intent.putExtra("pDescription",products.getDescription());
                                        intent.putExtra("pPrice",products.getPrice());
                                        intent.putExtra("kontrol",kontrol);
                                        startActivity(intent);
                                    }
                                }else
                                {
                                    Intent intent = new Intent(getActivity(), ProductDetailsActivity.class);
                                    intent.putExtra("pName",products.getName());
                                    intent.putExtra("kilolukdurumu",true);
                                    intent.putExtra("pDescription",products.getDescription());
                                    intent.putExtra("pPrice",products.getPrice());
                                    intent.putExtra("kontrol",kontrol);
                                    startActivity(intent);
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });

                    }
                });

            }

            @NonNull
            @Override
            public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.product_items_layout,parent,false);

                ProductViewHolder holder = new ProductViewHolder(view);


                return holder;
            }
        };
        recyclerView.setAdapter(adapter);
        kilolukrecyclerview.setAdapter(kilolukadapter);
        kilolukadapter.startListening();
        adapter.startListening();
    }

}
