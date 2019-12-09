package com.levent.pia;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.levent.pia.Model.AdminOrders;

import java.util.HashMap;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class AdminOrdersActivity extends AppCompatActivity {

    RecyclerView ordersList;
    DatabaseReference ordersRef,usersRef,orderListRef,cartListRef;
    int color,yeniSiparisNo;
    String orderNumber, yeniSiparisSayi,dbmaill;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_orders);

        ordersList=findViewById(R.id.orders_recycler_view);
        ordersRef= FirebaseDatabase.getInstance().getReference().child("Orders");
        usersRef= FirebaseDatabase.getInstance().getReference().child("Users");
        cartListRef = FirebaseDatabase.getInstance().getReference().child("Cart List").child("Admin View");
        orderListRef= FirebaseDatabase.getInstance().getReference();
        color = getResources().getColor(R.color.orderApproved);

        ordersList.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerOptions<AdminOrders> options = new FirebaseRecyclerOptions.Builder<AdminOrders>()
                .setQuery(ordersRef, AdminOrders.class)
                .build();

        FirebaseRecyclerAdapter<AdminOrders,AdminViewHolder> adapter = new FirebaseRecyclerAdapter<AdminOrders, AdminViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull final AdminViewHolder adminViewHolder, int i, @NonNull final AdminOrders adminOrders) {

                adminViewHolder.nameTxt.setText(adminOrders.getUsername());
                adminViewHolder.phoneTxt.setText(adminOrders.getTelno());
                adminViewHolder.priceTxt.setText(adminOrders.getToplamucret()+"₺");
                adminViewHolder.adresTxt.setText(adminOrders.getAdres());
                adminViewHolder.dateTxt.setText(adminOrders.getSiparistarihi());
                adminViewHolder.siparisNotTxt.setText(adminOrders.getSiparisnotu());
                adminViewHolder.siparisOdemeSekli.setText(adminOrders.getOdemesekli());
                adminViewHolder.siparisMahalle.setText(adminOrders.getMahalle());



                final String dbmail = adminOrders.getMail().replace(".",",");
                String orderStatus = adminOrders.getSiparisdurumu();

                if(orderStatus.equals("Siparişiniz Hazırlanıyor"))
                {
                    adminViewHolder.siparisOnaylaButton.setVisibility(View.GONE);
                    adminViewHolder.siparisYoldaButton.setVisibility(View.VISIBLE);
                }

                if(orderStatus.equals("Siparişinizi jet hızıyla getiriyoruz!!!"))
                {
                    adminViewHolder.siparisOnaylaButton.setVisibility(View.GONE);
                    adminViewHolder.siparisYoldaButton.setVisibility(View.GONE);
                    adminViewHolder.siparisTeslimButton.setVisibility(View.VISIBLE);
                }

                if(orderStatus.equals("Sipariş Teslim Edildi"))
                {

                    adminViewHolder.siparisOnaylaButton.setVisibility(View.GONE);
                    adminViewHolder.siparisYoldaButton.setVisibility(View.GONE);
                    adminViewHolder.siparisTeslimButton.setVisibility(View.GONE);
                    adminViewHolder.siparisDurumTxt.setVisibility(View.VISIBLE);
                    adminViewHolder.relativeLayout.setBackgroundColor(color);


                }




                adminViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        usersRef.child(dbmail).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                Integer intOrderNumber;
                                orderNumber=dataSnapshot.child("siparissayi").getValue().toString();
                                intOrderNumber=Integer.parseInt(orderNumber)-1;
                                Intent intent = new Intent(AdminOrdersActivity.this,AdminUserProducts.class);
                                intent.putExtra("mail",dbmail);
                                intent.putExtra("orderNumber",intOrderNumber.toString());
                                startActivity(intent);
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });

                    }
                });

                adminViewHolder.seeOrdersButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        new SweetAlertDialog(AdminOrdersActivity.this, SweetAlertDialog.WARNING_TYPE)
                                .setTitleText("SİPARİŞ İPTAL")
                                .setContentText("Siparişi iptal etmek istediğinize eminmisiniz?")
                                .setConfirmText("Evet")
                                .setCancelText("Hayır")
                                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(SweetAlertDialog sDialog) {
                                        sDialog
                                                .setTitleText("Sipariş Sil")
                                                .setContentText("Sipariş iptal edildi.")
                                                .setConfirmText("Tamam")
                                                .showCancelButton(false)
                                                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                                    @Override
                                                    public void onClick(final SweetAlertDialog sweetAlertDialog) {
                                                        sweetAlertDialog.cancel();
                                                        String mail= adminOrders.getMail();
                                                        dbmaill = mail.replace(".",",");


                                                        ordersRef.child(dbmaill).removeValue();

                                                        cartListRef.child(dbmaill).child(adminOrders.getSiparissayi()).removeValue();
                                                        usersRef.child(dbmaill).addListenerForSingleValueEvent(new ValueEventListener() {
                                                            @Override
                                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                                                yeniSiparisSayi=dataSnapshot.child("siparissayi").getValue().toString();
                                                                yeniSiparisNo = Integer.parseInt(yeniSiparisSayi);
                                                                yeniSiparisNo--;
                                                                yeniSiparisSayi=String.valueOf(yeniSiparisNo);
                                                                usersRef.child(dbmaill).child("siparissayi").setValue(yeniSiparisSayi);
                                                                Toast.makeText(AdminOrdersActivity.this,"Sipariş iptal edildi",Toast.LENGTH_LONG).show();

                                                            }

                                                            @Override
                                                            public void onCancelled(@NonNull DatabaseError databaseError) {

                                                            }
                                                        });
                                                    }
                                                })
                                                .changeAlertType(SweetAlertDialog.SUCCESS_TYPE);

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

                adminViewHolder.siparisOnaylaButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        adminViewHolder.siparisOnaylaButton.setVisibility(View.GONE);
                        Toast.makeText(AdminOrdersActivity.this,"Sipariş Hazırlanma Aşamasında",Toast.LENGTH_LONG).show();
                        adminViewHolder.siparisYoldaButton.setVisibility(View.VISIBLE);
                        adminViewHolder.seeOrdersButton.setVisibility(View.INVISIBLE);

                        ordersRef.child(dbmail).child("siparisdurumu").setValue("Siparişiniz Hazırlanıyor");

                    }
                });

                adminViewHolder.siparisYoldaButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        adminViewHolder.siparisYoldaButton.setVisibility(View.GONE);
                        Toast.makeText(AdminOrdersActivity.this,"Sipariş Yolda",Toast.LENGTH_LONG).show();
                        adminViewHolder.siparisTeslimButton.setVisibility(View.VISIBLE);
                        adminViewHolder.seeOrdersButton.setVisibility(View.INVISIBLE);

                        ordersRef.child(dbmail).child("siparisdurumu").setValue("Siparişinizi jet hızıyla getiriyoruz!!!");

                    }
                });

                adminViewHolder.siparisTeslimButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        new SweetAlertDialog(AdminOrdersActivity.this, SweetAlertDialog.WARNING_TYPE)
                                .setTitleText("ÜRÜN TESLİMATI")
                                .setContentText("Ürün müşteriye teslim edildi mi?")
                                .setConfirmText("Evet")
                                .setCancelText("Hayır")
                                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(SweetAlertDialog sDialog) {


                                                        adminViewHolder.siparisTeslimButton.setVisibility(View.GONE);
                                                        Toast.makeText(AdminOrdersActivity.this,"Sipariş Teslim Edildi",Toast.LENGTH_LONG).show();
                                                        adminViewHolder.siparisDurumTxt.setVisibility(View.VISIBLE);
                                                        adminViewHolder.relativeLayout.setBackgroundColor(color);



                                                        usersRef.child(dbmail).addListenerForSingleValueEvent(new ValueEventListener() {
                                                            @Override
                                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                                                Integer intOrderNumber;
                                                                orderNumber=dataSnapshot.child("siparissayi").getValue().toString();
                                                                intOrderNumber=Integer.parseInt(orderNumber)-1;
                                                                orderNumber=intOrderNumber.toString();
                                                                HashMap<String, Object> map2 = new HashMap<>();

                                                                map2.put("toplamucret", adminOrders.getToplamucret());
                                                                map2.put("siparistarihi", adminOrders.getSiparistarihi());

                                                                orderListRef.child("Order List").child(dbmail).child(orderNumber).updateChildren(map2);

                                                                ordersRef.child(dbmail).removeValue();

                                                            }

                                                            @Override
                                                            public void onCancelled(@NonNull DatabaseError databaseError) {

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
            public AdminViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.orders_layout, parent,false);

                return new AdminViewHolder(view);
            }
        };

        ordersList.setAdapter(adapter);
        adapter.startListening();
    }

    public static class AdminViewHolder extends RecyclerView.ViewHolder
    {
        public TextView nameTxt,phoneTxt,adresTxt,priceTxt,dateTxt,siparisDurumTxt, siparisNotTxt, siparisMahalle, siparisOdemeSekli;
        public RelativeLayout relativeLayout;
        public Button seeOrdersButton,siparisOnaylaButton, siparisYoldaButton, siparisTeslimButton;


        public AdminViewHolder(@NonNull View itemView) {
            super(itemView);

            nameTxt=itemView.findViewById(R.id.orders_username);
            phoneTxt=itemView.findViewById(R.id.tel_no_orders);
            adresTxt=itemView.findViewById(R.id.orders_adres);
            priceTxt=itemView.findViewById(R.id.orders_price);
            dateTxt=itemView.findViewById(R.id.orders_date);
            relativeLayout=itemView.findViewById(R.id.orders_relative_layout);
            siparisOnaylaButton=itemView.findViewById(R.id.orders_sipariş_onayla);
            siparisYoldaButton=itemView.findViewById(R.id.orders_sipariş_yolda);
            siparisTeslimButton=itemView.findViewById(R.id.orders_sipariş_teslim_edildi);
            siparisDurumTxt=itemView.findViewById(R.id.orders_sipariş_teslim_edildi_textview);
            siparisOdemeSekli=itemView.findViewById(R.id.orders_odeme_şekli);
            siparisMahalle=itemView.findViewById(R.id.orders_mahalle);
            siparisNotTxt=itemView.findViewById(R.id.orders_not);


            seeOrdersButton=itemView.findViewById(R.id.orders_see_order);


        }
    }
}
