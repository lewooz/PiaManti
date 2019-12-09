package com.levent.pia.Fragmentlar;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Fragment;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.levent.pia.AdminOrdersActivity;
import com.levent.pia.ConfirmOrderActivity;
import com.levent.pia.MenuFeedActivity;
import com.levent.pia.Model.Cart;
import com.levent.pia.ProductDetailsActivity;
import com.levent.pia.R;
import com.levent.pia.ViewHolder.CartViewHolder;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.HashMap;
import java.util.Locale;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class CartFragment extends Fragment {
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    RelativeLayout ustLayout;
    FirebaseUser currentOnlineUser;
    FirebaseAuth mAuth;
    Double singletotal,overalltotal;
    String dbmail,mail,dbprice,dbprice2,kontrol,orderNumber, silincekUrun;
    Boolean kilolukdurumu;
    TextView totalPriceText,siparisDurumText;
    Button confirmOrderButton;
    DecimalFormat df;
    LottieAnimationView animasyon;
    SharedPreferences sharedPreferences;
    Integer fabcounter;


    public CartFragment(){}


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.cartfragmentml, container , false);

        mAuth= FirebaseAuth.getInstance();
        currentOnlineUser = mAuth.getCurrentUser();
        mail=currentOnlineUser.getEmail();
        kontrol="1";
        df= new DecimalFormat("###.##");

        overalltotal=0.0;

        recyclerView=view.findViewById(R.id.cart_recycle);
        recyclerView.setHasFixedSize(true);
        layoutManager= new LinearLayoutManager( getActivity());
        recyclerView.setLayoutManager(layoutManager);
        totalPriceText=view.findViewById(R.id.toplam_ücret);
        ustLayout=view.findViewById(R.id.üst_relative);
        siparisDurumText=view.findViewById(R.id.sipariş_durum_text);
        animasyon=view.findViewById(R.id.sipariş_durum_animation);
        confirmOrderButton= view.findViewById(R.id.sipariş_onayla_button);


        dbmail = mail.replace(".", ",");
        sharedPreferences=this.getActivity().getSharedPreferences("com.levent.pia", Context.MODE_PRIVATE);
        fabcounter=sharedPreferences.getInt("counter",0);


        return view;
    }
    @Override
    public void onStart() {
        super.onStart();

        final DatabaseReference cartListRef = FirebaseDatabase.getInstance().getReference()
                .child("Cart List");

        FirebaseRecyclerOptions<Cart> option = new FirebaseRecyclerOptions.Builder<Cart>()
                .setQuery(cartListRef.child("User View")
                        .child(dbmail).child("Products"),Cart.class).build();

        FirebaseRecyclerAdapter<Cart, CartViewHolder> adapter =
                new FirebaseRecyclerAdapter<Cart, CartViewHolder>(option) {


                    @Override
                    protected void onBindViewHolder(@NonNull final CartViewHolder cartViewHolder, int i, @NonNull final Cart cart) {

                        cartViewHolder.txtProductName.setText(cart.getName());
                        cartViewHolder.txtProductPrice.setText(cart.getPrice());
                        cartViewHolder.txtProductQuantity.setText(cart.getQuantity()+" Adet");
                        cartViewHolder.txtProductPorsiyon.setText(cart.getPorsiyon()+" Porsiyon");

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

                        System.out.println("Price: "+cart.getPrice());

                        dbprice2 = cart.getPrice().replace("₺", "");
                        dbprice2 = dbprice2.replace(",", ".");
                        singletotal = Double.parseDouble(dbprice2);
                        overalltotal+=singletotal;

                        totalPriceText.setText("Toplam Ücret: "+df.format(overalltotal)+"₺");

                        cartViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                if(cartViewHolder.txtTeslimatDurumu.getVisibility()==View.VISIBLE)
                                {
                                    kilolukdurumu=true;
                                }else
                                    {
                                        kilolukdurumu=false;
                                    }

                                String dbPors;
                                if(cart.getPorsiyon().equals("1"))
                                {
                                    dbPors="";
                                }else{
                                    dbPors= cart.getPorsiyon().replace(".","");
                                }
                                dbprice = cart.getPrice().replace("₺", "");
                                int i = Integer.parseInt(cart.getQuantity());
                                double b = Double.parseDouble(dbprice);
                                dbprice= String.valueOf(b/i);


                                System.out.println("Toplam:" + overalltotal);
                                String namedbPors = cart.getName() + dbPors;
                                Intent intent= new Intent(getActivity(), ProductDetailsActivity.class);
                                intent.putExtra("pName",cart.getName());
                                intent.putExtra("pDescription","");
                                intent.putExtra("pPrice",dbprice);
                                intent.putExtra("kontrol",kontrol);
                                intent.putExtra("kilolukdurumu",kilolukdurumu);
                                intent.putExtra("namedbPors",namedbPors);
                                intent.putExtra("porsiyon",dbPors);
                                startActivity(intent);
                            }
                        });

                        cartViewHolder.cartImage.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                new SweetAlertDialog(getActivity(), SweetAlertDialog.WARNING_TYPE)
                                        .setTitleText("SEPETTEN ÜRÜN ÇIKAR")
                                        .setContentText("Ürünü sepetten çıkarmak istediğinize emin misiniz?")
                                        .setConfirmText("Evet")
                                        .setCancelText("Hayır")
                                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                            @Override
                                            public void onClick(SweetAlertDialog sDialog) {

                                                final DatabaseReference myRef =FirebaseDatabase.getInstance().getReference();
                                                DatabaseReference userRef = FirebaseDatabase.getInstance().getReference();

                                                if(cart.getPorsiyon().equals("1.5"))
                                                {
                                                    silincekUrun ="15";
                                                }else{
                                                    silincekUrun ="";
                                                }

                                                userRef.child("Users").child(dbmail).addListenerForSingleValueEvent(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                                        orderNumber = dataSnapshot.child("siparissayi").getValue().toString();

                                                        myRef.child("Cart List").child("Admin View").child(dbmail).child(orderNumber).child(cart.getName()+silincekUrun).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Void> task) {

                                                            }
                                                        });
                                                    }

                                                    @Override
                                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                                    }
                                                });

                                                myRef.child("Cart List").child("User View").child(dbmail).child("Products").child(cart.getName()+silincekUrun).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {

                                                        if(task.isSuccessful()) {
                                                            Toast.makeText(getActivity(), "Ürün sepetten silindi", Toast.LENGTH_LONG).show();
                                                            String tekPrice = cart.getPrice().replace("₺","").replace(",",".");
                                                            DecimalFormat df2 = new DecimalFormat("####,###");
                                                            String overall = df2.format(overalltotal);
                                                            Double overall2 = Double.parseDouble(overall);
                                                            System.out.println("OVERALL2: "+overall2+" OVERALL: "+overall);
                                                            System.out.println("SİLİNCEK: "+Double.parseDouble(tekPrice)*Integer.parseInt(cart.getQuantity()));
                                                            overalltotal-=Double.parseDouble(tekPrice);


                                                            totalPriceText.setText("Toplam Ücret: "+overalltotal+"₺");
                                                            fabcounter--;
                                                            sharedPreferences.edit().putInt("counter",fabcounter).apply();
                                                        }
                                                    }
                                                });




                                                sDialog.cancel();
                                            }
                                        }).setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                                        sweetAlertDialog.cancel();
                                    }
                                })
                                        .show();
                            }
                        });

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

    confirmOrderButton.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            confirmOrder();
        }
    });

    DatabaseReference ordersRef = FirebaseDatabase.getInstance().getReference();
    ordersRef.child("Orders").child(dbmail).child("siparisdurumu").addValueEventListener(new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            checkOrderState();
            if(!dataSnapshot.exists())
            {
                recyclerView.setVisibility(View.VISIBLE);
                confirmOrderButton.setVisibility(View.VISIBLE);
                ustLayout.setVisibility(View.VISIBLE);
                animasyon.setVisibility(View.GONE);
                siparisDurumText.setVisibility(View.GONE);



            }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    });

    checkOrderState();
    }

    private void checkOrderState() {

        DatabaseReference ordersRef = FirebaseDatabase.getInstance().getReference().child("Orders").child(dbmail);

        ordersRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if(dataSnapshot.child("siparisdurumu").exists()) {

                    String shippingState = dataSnapshot.child("siparisdurumu").getValue().toString();
                    if (shippingState.equals("Onay Aşamasında")) {
                        recyclerView.setVisibility(View.GONE);
                        confirmOrderButton.setVisibility(View.GONE);
                        ustLayout.setVisibility(View.GONE);
                        animasyon.setVisibility(View.VISIBLE);
                        siparisDurumText.setVisibility(View.VISIBLE);
                    } else if (shippingState.equals("Siparişiniz Hazırlanıyor")) {
                        recyclerView.setVisibility(View.GONE);
                        confirmOrderButton.setVisibility(View.GONE);
                        ustLayout.setVisibility(View.GONE);
                        animasyon.setAnimation(R.raw.cooking);
                        animasyon.setVisibility(View.VISIBLE);
                        siparisDurumText.setText("Siparişiniz Hazırlanıyor");
                        siparisDurumText.setVisibility(View.VISIBLE);
                    } else if (shippingState.equals("Siparişinizi jet hızıyla getiriyoruz!!!")) {
                        recyclerView.setVisibility(View.GONE);
                        confirmOrderButton.setVisibility(View.GONE);
                        ustLayout.setVisibility(View.GONE);
                        animasyon.setAnimation(R.raw.siparisyolda);
                        animasyon.setVisibility(View.VISIBLE);
                        siparisDurumText.setText("Siparişiniz Yolda!");
                        siparisDurumText.setVisibility(View.VISIBLE);

                    } else {
                        recyclerView.setVisibility(View.VISIBLE);
                        confirmOrderButton.setVisibility(View.VISIBLE);
                        ustLayout.setVisibility(View.VISIBLE);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void confirmOrder() {

        Intent intent = new Intent(getActivity(), ConfirmOrderActivity.class);
        intent.putExtra("TotalPrice",df.format(overalltotal));
        startActivity(intent);
    }

    @Override
    public void onPause() {
        super.onPause();
        overalltotal=0.0;
     }


}
