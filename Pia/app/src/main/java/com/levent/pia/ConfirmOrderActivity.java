package com.levent.pia;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.onesignal.OneSignal;

import org.angmarch.views.NiceSpinner;
import org.angmarch.views.OnSpinnerItemSelectedListener;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;



public class ConfirmOrderActivity extends AppCompatActivity {

    String toplamUcret,mail,dbmail,siparisSayisi, username, phoneNo, odemesekli, saveCurrentTime,saveCurrentDate,address,limit,mahalleAdi, adminid, admin2id;
    Calendar calFordate;
    Double parseToplamUcret;
    TextView toplamTutar,limitYazisi,indirimyazisi,siparisnotu;
    EditText adres;
    DatabaseReference myRef;
    FirebaseAuth mAuth;
    FirebaseUser currentOnlineUser;
    List<String> odemeSecenek, mahalleisimleri;
    NiceSpinner odemeSecenekSpinner,mahallespinner;
    Integer orderNumber;
    DecimalFormat df;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_order);

        sharedPreferences= getApplicationContext().getSharedPreferences("com.levent.pia",MODE_PRIVATE);



        calFordate = Calendar.getInstance();

        SimpleDateFormat currentDate= new SimpleDateFormat("MMM,dd,yyyy");
        saveCurrentDate = currentDate.format(calFordate.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss");
        saveCurrentTime = currentTime.format(calFordate.getTime());

        toplamTutar=findViewById(R.id.toplam_tutar);
        adres=findViewById(R.id.siparişin_gideceği_adres);
        limitYazisi = findViewById(R.id.limit_yazısı);
        indirimyazisi=findViewById(R.id.indirim_yazisi);
        odemeSecenekSpinner=findViewById(R.id.ödeme_şekli_spinner);
        mahallespinner =findViewById(R.id.mahalle_spinner);
        siparisnotu=findViewById(R.id.sipariş_notu);
        adminid="61bac184-6170-45db-be39-0ab3190927d7";
        admin2id="10774cc1-cd35-4f36-8a5c-682ba2952b7a";
        limit = "20";
        mahalleAdi = "Göztepe Mah.";
        odemesekli="Nakit";
        odemeSecenek = new LinkedList<>(Arrays.asList("Nakit","Kredi Kartı","Ticket", "SetCard", "Multinet"));
        odemeSecenekSpinner.attachDataSource(odemeSecenek);

        mahalleisimleri = new LinkedList<>(Arrays.asList("İstanbul Medeniye Üniv.","Merdivenköy Mah.","19 Mayıs Mah.","Erenköy Mah.","Dumlupınar Mah.","Bilgi Üniv.","Caddebostan-Ethemefendi","Göztepe Mah.","Kozyatağı Mah.","Marmara Üniv.","Yeditepe Üniv. Hast.", "Fikirtepe Mah.","Erenköy-Ethemefendi"));
        mahallespinner.attachDataSource(mahalleisimleri);

        myRef= FirebaseDatabase.getInstance().getReference();
        mAuth= FirebaseAuth.getInstance();
        currentOnlineUser= mAuth.getCurrentUser();
        mail=currentOnlineUser.getEmail();
        dbmail = mail.replace(".", ",");
        username = currentOnlineUser.getDisplayName();

          df = new DecimalFormat("###.##");


        toplamUcret=getIntent().getStringExtra("TotalPrice");
        toplamUcret=toplamUcret.replace(",",".");
        parseToplamUcret=Double.parseDouble(toplamUcret);





        myRef.child("Users").child(dbmail).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                adres.setText(dataSnapshot.child("adres").getValue().toString());
                address =dataSnapshot.child("adres").getValue().toString();
                siparisSayisi = dataSnapshot.child("siparissayi").getValue().toString();
                phoneNo = dataSnapshot.child("phone").getValue().toString();

                if(siparisSayisi.equals("1")|| Integer.parseInt(siparisSayisi)%5 == 0)
                {
                indirimyazisi.setVisibility(View.VISIBLE);
                parseToplamUcret=parseToplamUcret-parseToplamUcret*0.1;
                toplamTutar.setText("Toplam Tutar: " + df.format(parseToplamUcret) + "₺");
                if(siparisSayisi.equals("1")){
                indirimyazisi.setText("İlk siparişinize özel %10 indirim kazandınız!");
                }else
                    {

                        indirimyazisi.setText(Integer.parseInt(siparisSayisi)+1+". Siparişinize özel %10 indirim kazandınız!");
                    }
                }else
                {
                    parseToplamUcret=Double.parseDouble(toplamUcret);
                    toplamTutar.setText("Toplam Tutar: " + df.format(parseToplamUcret) + "₺");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        mahallespinner.setOnSpinnerItemSelectedListener(new OnSpinnerItemSelectedListener() {
            @Override
            public void onItemSelected(NiceSpinner parent, View view, int position, long id) {
                //("0İstanbul Medeniye Üniv.","1Merdivenköy Mah.","219 Mayıs Mah.","3Erenköy Mah.","4Dumlupınar Mah.","5Bilgi Üniv.","6Caddebostan-Ethemefendi","7Göztepe Mah.","8Kozyatağı Mah.","9Marmara Üniv.","10Yeditepe Üniv. Hast.", "11Fikirtepe Mah.")
                if(position==0||position==1)
                {
                    limit = "20";
                    mahalleAdi = mahallespinner.getItemAtPosition(position).toString();
                    limitYazisi.setText(mahalleAdi+" için limitimiz minimum "+limit+"₺'dir");

                }
                if(position==2||position==3)
                {
                    limit = "25";
                    mahalleAdi = mahallespinner.getItemAtPosition(position).toString();
                    limitYazisi.setText(mahalleAdi+" için limitimiz minimum "+limit+"₺'dir");
                }
                if(position==4)
                {
                    limit = "30";
                    mahalleAdi = mahallespinner.getItemAtPosition(position).toString();
                    limitYazisi.setText(mahalleAdi+" için limitimiz minimum "+limit+"₺'dir");
                }
                if(position==5||position==6||position==7||position==8||position==9||position==10)
                {
                    limit = "35";
                    mahalleAdi = mahallespinner.getItemAtPosition(position).toString();
                    limitYazisi.setText(mahalleAdi+" için limitimiz minimum "+limit+"₺'dir");
                }
                if(position==11)
                {
                    limit = "37";
                    mahalleAdi = mahallespinner.getItemAtPosition(position).toString();
                    limitYazisi.setText(mahalleAdi+" için limitimiz minimum "+limit+"₺'dir");
                }
                if(position==12)
                {
                    limit = "45";
                    mahalleAdi = mahallespinner.getItemAtPosition(position).toString();
                    limitYazisi.setText(mahalleAdi+" için limitimiz minimum "+limit+"₺'dir");
                }

            }
        });

        odemeSecenekSpinner.setOnSpinnerItemSelectedListener(new OnSpinnerItemSelectedListener() {
            @Override
            public void onItemSelected(NiceSpinner parent, View view, int position, long id) {
                //"Nakit","Kredi Kartı","Ticket", "SetCard", "Multinet"

                if(position==0)
                {
                    odemesekli = "Nakit";
                }
                if(position==1)
                {
                    odemesekli = "Kredi Kartı";
                }
                if(position==2)
                {
                    odemesekli = "Ticket";
                }
                if(position==3)
                {
                    odemesekli = "SetCard";
                }
                if(position==4)
                {
                    odemesekli = "MultiNet";
                }

            }
        });

    }

    public void siparisonay(View view)
    {
        if(!toplamUcret.equals("0")) {
            if (Double.parseDouble(limit) > Double.parseDouble(toplamUcret)) {
                Toast.makeText(ConfirmOrderActivity.this, "Lütfen minimum limit tutarı üstünde bir sipariş veriniz.", Toast.LENGTH_LONG).show();
            } else {
                orderNumber = Integer.parseInt(siparisSayisi)+1;
                HashMap<String, Object> map = new HashMap<>();
                map.put("siparissayi", orderNumber.toString());
                myRef.child("Users").child(dbmail).updateChildren(map); //Sipariş sayısı +1

                DatabaseReference siparisRef = FirebaseDatabase.getInstance().getReference();
                HashMap<String, Object> map2 = new HashMap<>();
                map2.put("username", username);
                map2.put("mahalle", mahallespinner.getSelectedItem().toString());
                map2.put("adres", address);
                map2.put("siparissayi", siparisSayisi);
                map2.put("telno", phoneNo);
                map2.put("toplamucret", df.format(parseToplamUcret));
                map2.put("siparistarihi", saveCurrentDate + " " + saveCurrentTime);
                map2.put("odemesekli", odemesekli);
                map2.put("siparisnotu", siparisnotu.getText().toString());
                map2.put("siparisdurumu", "Onay Aşamasında");
                map2.put("mail",mail);

                siparisRef.child("Orders").child(dbmail).updateChildren(map2).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        if (task.isSuccessful()) {
                            FirebaseDatabase.getInstance().getReference().child("Cart List")
                                    .child("User View")
                                    .child(dbmail)
                                    .removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {

                                    if (task.isSuccessful()) {
                                        Toast.makeText(getApplicationContext(), "Tebrikler! Siparişiniz alınmıştır.", Toast.LENGTH_LONG).show();
                                        sharedPreferences.edit().putInt("counter",0).apply();
                                        try {
                                            OneSignal.postNotification(new JSONObject("{'contents': {'en':'"+username+" sipariş verdi.'}, 'include_player_ids': ['" + adminid + "','" + admin2id + "']}"), null);

                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                        Intent intent = new Intent(ConfirmOrderActivity.this, MenuFeedActivity.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        intent.putExtra("nerden","sepet");
                                        startActivity(intent);
                                        finish();

                                    }
                                }
                            });

                        }

                    }
                });


            }

        }else
            {
                Toast.makeText(ConfirmOrderActivity.this,"Sepetinize lütfen ürün ekleyiniz.",Toast.LENGTH_LONG).show();
            }
    }
}
