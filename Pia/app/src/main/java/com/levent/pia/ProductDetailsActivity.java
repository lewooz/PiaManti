package com.levent.pia;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.levent.pia.Fragmentlar.CartFragment;
import com.levent.pia.Fragmentlar.MenuFragment;
import com.squareup.picasso.Picasso;

import org.angmarch.views.NiceSpinner;
import org.angmarch.views.OnSpinnerItemSelectedListener;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import static com.levent.pia.Fragmentlar.MenuFragment.fab;


public class ProductDetailsActivity extends AppCompatActivity {

    TextView productName,productPrice, productDescription, productSosdurumu,productSarmDurumu, productPorsiyon, dateText, saatText, kilolukUrunText;
    NiceSpinner sosSpinner,sarmSpinner, porsSpinner;
    ImageView imageView;
    ElegantNumberButton elegantNumberButton;
    String pName,pPrice,pDescription,pImage,porsiyon,mail,dbmail,sosDurumu,sarımsakDurumu,dbPors,kontrol, namedbPors, portion, orderNumber, tarih, saat;
    Boolean kilolukDurumu;
    Button addCartButton, datePickButton, timePickButton;
    List<String> sarımsakSet,sosSet,porsSet;
    SharedPreferences sharedPreferences;
    Date date1,date2;
    int itemCounter;


    DatabaseReference myRef, userRef, kilolukRef;
    FirebaseAuth mAuth;
    FirebaseUser currentOnlineUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);

        productName=findViewById(R.id.product_name_details);
        productPrice=findViewById(R.id.product_price_details);
        productDescription=findViewById(R.id.product_description_details);
        productSosdurumu=findViewById(R.id.product_sos_details);
        productSarmDurumu=findViewById(R.id.product_sarimsak_details);
        productPorsiyon=findViewById(R.id.product_porsiyon_details);

        sosSpinner=findViewById(R.id.sos_spinner);
        sarmSpinner=findViewById(R.id.sarımsak_spinner);
        porsSpinner=findViewById(R.id.porsiyon_spinner);

        dateText=findViewById(R.id.tarih_text);
        saatText=findViewById(R.id.saat_seç_text);
        kilolukUrunText = findViewById(R.id.kiloluk_urun_text);

        datePickButton=findViewById(R.id.tarih_seç_buton);
        timePickButton=findViewById(R.id.saat_seç_buton);

        imageView=findViewById(R.id.product_image_details);
        addCartButton=findViewById(R.id.add_to_cart_product_details);
        elegantNumberButton=findViewById(R.id.add_drop_details);
        myRef = FirebaseDatabase.getInstance().getReference();
        kilolukRef = FirebaseDatabase.getInstance().getReference();

        sharedPreferences = this.getSharedPreferences("com.levent.pia", MODE_PRIVATE);
        itemCounter = sharedPreferences.getInt("counter",0);
        if(itemCounter==0) {
            sharedPreferences.edit().putInt("counter", 0).apply();

        }
        pName= getIntent().getStringExtra("pName");
        pPrice= getIntent().getStringExtra("pPrice");
        pDescription= getIntent().getStringExtra("pDescription");
        kilolukDurumu = getIntent().getBooleanExtra("kilolukdurumu",false);

        productName.setText(pName);
        productPrice.setText(pPrice+"₺");
        productDescription.setText(pDescription);

        userRef=FirebaseDatabase.getInstance().getReference().child("Users");

        portion=getIntent().getStringExtra("porsiyon");
        porsiyon="1";
        tarih="";
        saat="";
        dbPors="";
        sosDurumu="";
        sarımsakDurumu="";
        kontrol="0";
        kontrol=getIntent().getStringExtra("kontrol");



        elegantNumberButton.setOnValueChangeListener(new ElegantNumberButton.OnValueChangeListener() {
            @Override
            public void onValueChange(ElegantNumberButton view, int oldValue, int newValue) {

                Double sonuc;
                DecimalFormat df = new DecimalFormat("###.##");
                if(porsSpinner.getSelectedItem().toString().equals("1 porsiyon"))
                {
                    sonuc=1*Double.parseDouble(pPrice)*newValue;
                    productPrice.setText(df.format(sonuc)+"₺");
                }else if(porsSpinner.getSelectedItem().toString().equals("1,5 porsiyon"))
                {
                    sonuc=1.5*Double.parseDouble(pPrice)*newValue;
                    productPrice.setText(df.format(sonuc)+"₺");

                }

            }
        });

        if(pName.equals("Kayseri Ev Mantısı")||pName.equals("Çıtır Mantı")||pName.equals("Patatesli Mantı"))
        {
            productSosdurumu.setVisibility(View.VISIBLE);
            productSarmDurumu.setVisibility(View.VISIBLE);
            sosSpinner.setVisibility(View.VISIBLE);
            sarmSpinner.setVisibility(View.VISIBLE);
            productPorsiyon.setVisibility(View.VISIBLE);
            porsSpinner.setVisibility(View.VISIBLE);
        }

        if(pName.equals("Etli Yaprak Sarma")||pName.equals("Zeytinyağlı Yaprak Sarma")||pName.equals("Lahana Sarma")||pName.equals("Kıymalı Çiğ Börek")||pName.equals("Peynirli Çiğ Börek")||pName.equals("Haşlama İçli Köfte")||pName.equals("Kızartma İçli Köfte"))
        {
            productPorsiyon.setVisibility(View.VISIBLE);
            porsSpinner.setVisibility(View.VISIBLE);
        }

        mAuth= FirebaseAuth.getInstance();

        currentOnlineUser= mAuth.getCurrentUser();
        mail = currentOnlineUser.getEmail();
        dbmail = mail.replace(".", ",");

        userRef.child(dbmail).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                orderNumber= dataSnapshot.child("siparissayi").getValue().toString();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        if(!kilolukDurumu) {

            myRef.child("Products").child("menu").child(pName).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    pImage = dataSnapshot.child("image").getValue().toString();
                    Picasso.get().load(Uri.parse(pImage)).into(imageView);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                    Toast.makeText(ProductDetailsActivity.this, databaseError.getMessage(), Toast.LENGTH_LONG).show();
                }
            });
        }else
            {
                kilolukRef.child("Products").child("kiloluk").child(pName).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        pImage = dataSnapshot.child("image").getValue().toString();
                        Picasso.get().load(Uri.parse(pImage)).into(imageView);
                        productDescription.setVisibility(View.GONE);
                        productPorsiyon.setVisibility(View.GONE);
                        porsSpinner.setVisibility(View.GONE);
                        datePickButton.setVisibility(View.VISIBLE);
                        timePickButton.setVisibility(View.VISIBLE);
                        dateText.setVisibility(View.VISIBLE);
                        saatText.setVisibility(View.VISIBLE);
                        kilolukUrunText.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                        Toast.makeText(ProductDetailsActivity.this, databaseError.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });

            }

        sarımsakSet = new LinkedList<>(Arrays.asList("Sarımsaksız", "Sarımsaklı"));
        sarmSpinner.attachDataSource(sarımsakSet);

        sosSet = new LinkedList<>(Arrays.asList("Sossuz", "Tereyağlı Pul Biber Sos", "Tereyağlı Salça Sos"));
        sosSpinner.attachDataSource(sosSet);

        porsSet = new LinkedList<>(Arrays.asList("1 porsiyon", "1,5 porsiyon"));
        porsSpinner.attachDataSource(porsSet);

        porsSpinner.setOnSpinnerItemSelectedListener(new OnSpinnerItemSelectedListener() {
            @Override
            public void onItemSelected(NiceSpinner parent, View view, int position, long id) {
                Double price = Double.parseDouble(pPrice);
                Double finalPrice;
                DecimalFormat df = new DecimalFormat("###.##");
                String elegant = elegantNumberButton.getNumber();
                if(position==0)
                {
                    finalPrice = Double.parseDouble(elegant)*Double.parseDouble(pPrice)*1;
                    productPrice.setText( df.format(finalPrice)+"₺");
                    porsiyon="1";
                    dbPors="";
                }else if(position==1)
                {

                    finalPrice = Double.parseDouble(elegant)*Double.parseDouble(pPrice)*1.5;
                    porsiyon="1.5";
                    dbPors="15";
                    //pPrice=df.format(finalPrice);
                    productPrice.setText(df.format(finalPrice)+"₺");
                }
            }
        });

        if(kontrol.equals("0"))
        {
            addCartButton.setText("SEPETE EKLE");

        }

        if(kontrol.equals("1"))
        {

            addCartButton.setText("ÜRÜNÜ GÜNCELLE");
            if(portion.equals("15"))
            {
                Double a;
                a=Double.parseDouble(pPrice.replace(",","."));
                a=a/1.5;
                DecimalFormat df = new DecimalFormat("###.##");
                pPrice=df.format(a);

                porsSpinner.setSelectedIndex(1);

            }

        }

        if(sosSpinner.getVisibility()==View.VISIBLE) {
            sosDurumu = "Sossuz";
            sosSpinner.setOnSpinnerItemSelectedListener(new OnSpinnerItemSelectedListener() {
                @Override
                public void onItemSelected(NiceSpinner parent, View view, int position, long id) {

                    if (position == 0) {

                        sosDurumu = "Sossuz";
                    }
                    if (position == 1) {

                        sosDurumu = "Tereyağlı Pul Biber Sos";
                    }
                    if (position == 2) {

                        sosDurumu = "Tereyağlı Salça Sos";
                    }

                }
            });
        }

        if(sarmSpinner.getVisibility()==View.VISIBLE) {
            sarımsakDurumu = "Sarımsaksız";
            sarmSpinner.setOnSpinnerItemSelectedListener(new OnSpinnerItemSelectedListener() {
                @Override
                public void onItemSelected(NiceSpinner parent, View view, int position, long id) {


                    if (position == 0) {

                        sarımsakDurumu = "Sarımsaksız";
                        System.out.println("Sarımsak: "+sarımsakDurumu);
                    }
                    if (position == 1) {

                        sarımsakDurumu = "Sarımsaklı";
                        System.out.println("Sarımsak: "+sarımsakDurumu);
                    }

                }
            });
        }



    }

    public void addToCart(View view)

    {

        if(kilolukDurumu) {
            if (tarih.isEmpty() || saat.isEmpty())
            {
                Toast.makeText(getApplicationContext(),"Lütfen teslimat için tarih ve saat seçiniz", Toast.LENGTH_LONG).show();
            }else{
                if (kontrol.equals("0")) {

                    namedbPors = pName + dbPors;
                }

                if (kontrol.equals("1")) {


                    namedbPors = getIntent().getStringExtra("namedbPors");
                }


                String saveCurrentTime, saveCurrentDate;
                Calendar calFordate = Calendar.getInstance();

                SimpleDateFormat currentDate = new SimpleDateFormat("MMM,dd,yyyy");
                saveCurrentDate = currentDate.format(calFordate.getTime());

                SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss");
                saveCurrentTime = currentTime.format(calFordate.getTime());

                final DatabaseReference cartListRef = FirebaseDatabase.getInstance().getReference().child("Cart List");

                HashMap<String, Object> cartMap = new HashMap<>();
                cartMap.put("name", pName);
                cartMap.put("quantity", elegantNumberButton.getNumber());
                cartMap.put("porsiyon", porsiyon);
                cartMap.put("price", productPrice.getText().toString());
                cartMap.put("sosdurumu", sosDurumu);
                cartMap.put("sarimsakdurumu", sarımsakDurumu);
                cartMap.put("date", saveCurrentDate);
                cartMap.put("time", saveCurrentTime);
                cartMap.put("teslimat", "T: " + tarih + " S: " + saat);


                cartListRef.child("User View").child(dbmail).child("Products").child(namedbPors).updateChildren(cartMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {


                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(ProductDetailsActivity.this, e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                    }
                });

                cartListRef.child("Admin View").child(dbmail).child(orderNumber).child(namedbPors).updateChildren(cartMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            if (kontrol.equals("0")) {
                                Toast.makeText(getApplicationContext(), "Ürün sepete eklendi.", Toast.LENGTH_LONG).show();

                                Intent intent = new Intent(ProductDetailsActivity.this, MenuFeedActivity.class);
                                itemCounter++;
                                sharedPreferences.edit().putInt("counter", itemCounter).apply();
                                startActivity(intent);
                                finish();
                            }
                            if (kontrol.equals("1")) {
                                Toast.makeText(getApplicationContext(), "Ürün Güncellendi.", Toast.LENGTH_LONG).show();

                                Intent intent = new Intent(ProductDetailsActivity.this, MenuFeedActivity.class);
                                intent.putExtra("nerden", "sepet");

                                startActivity(intent);
                                finish();
                            }
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(ProductDetailsActivity.this, e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                    }
                });
            }

        }else {

            if (kontrol.equals("0")) {

                namedbPors = pName + dbPors;
            }

            if (kontrol.equals("1")) {


                namedbPors = getIntent().getStringExtra("namedbPors");
            }
            System.out.println("Sarımsak: " + sarımsakDurumu);

            String saveCurrentTime, saveCurrentDate;
            Calendar calFordate = Calendar.getInstance();

            SimpleDateFormat currentDate = new SimpleDateFormat("MMM,dd,yyyy");
            saveCurrentDate = currentDate.format(calFordate.getTime());

            SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss");
            saveCurrentTime = currentTime.format(calFordate.getTime());

            final DatabaseReference cartListRef = FirebaseDatabase.getInstance().getReference().child("Cart List");

            HashMap<String, Object> cartMap = new HashMap<>();
            cartMap.put("name", pName);
            cartMap.put("quantity", elegantNumberButton.getNumber());
            cartMap.put("porsiyon", porsiyon);
            cartMap.put("price", productPrice.getText().toString());
            cartMap.put("sosdurumu", sosDurumu);
            cartMap.put("sarimsakdurumu", sarımsakDurumu);
            cartMap.put("date", saveCurrentDate);
            cartMap.put("time", saveCurrentTime);
            cartMap.put("teslimat", "T: " + tarih + " S: " + saat);


            cartListRef.child("User View").child(dbmail).child("Products").child(namedbPors).updateChildren(cartMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {


                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(ProductDetailsActivity.this, e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                }
            });

            cartListRef.child("Admin View").child(dbmail).child(orderNumber).child(namedbPors).updateChildren(cartMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        if (kontrol.equals("0")) {
                            Toast.makeText(getApplicationContext(), "Ürün sepete eklendi.", Toast.LENGTH_LONG).show();

                            Intent intent = new Intent(ProductDetailsActivity.this, MenuFeedActivity.class);
                            itemCounter++;
                            sharedPreferences.edit().putInt("counter", itemCounter).apply();
                            startActivity(intent);
                            finish();
                        }
                        if (kontrol.equals("1")) {
                            Toast.makeText(getApplicationContext(), "Ürün Güncellendi.", Toast.LENGTH_LONG).show();

                            Intent intent = new Intent(ProductDetailsActivity.this, MenuFeedActivity.class);
                            intent.putExtra("nerden", "sepet");

                            startActivity(intent);
                            finish();
                        }
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(ProductDetailsActivity.this, e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                }
            });


        }
    }

    public void choosedate(View view)
    {
        Calendar calendar = Calendar.getInstance();

        new DatePickerDialog(this, datelistener,calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();

    }
    DatePickerDialog.OnDateSetListener datelistener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker datePicker, int year, int month, int day) {

            int gun,ay,yil,truemonth;

            truemonth = month+1;
            Calendar calendar =Calendar.getInstance();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            gun=calendar.get(Calendar.DAY_OF_MONTH);
            ay=calendar.get(Calendar.MONTH);
            yil=calendar.get(Calendar.YEAR);

            try {
                date1 = sdf.parse(yil+"-"+ay+"-"+gun);
                date2 = sdf.parse(year+"-"+month+"-"+day);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            if(date1.compareTo(date2)==0)
            {
                Toast.makeText(getApplicationContext(),"Kiloluk ürün siparişleri en az bir gün önceden alınmaktadır.",Toast.LENGTH_LONG).show();
            } else if(date1.compareTo(date2)>0)
            {
                Toast.makeText(getApplicationContext(),"Lütfen ileri bir tarih seçiniz.",Toast.LENGTH_LONG).show();
            } else if(date1.compareTo(date2)<0){
            tarih = day+"."+truemonth+"."+year;
            dateText.setText(tarih);
            }

        }
    };

    public void choosetime(View view)
    {
        Calendar calendar = Calendar.getInstance();

        new TimePickerDialog(this,timelistener,calendar.get(Calendar.HOUR_OF_DAY),calendar.get(Calendar.MINUTE),true).show();


    }
    TimePickerDialog.OnTimeSetListener timelistener =new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker timePicker, int hour, int minute) {

            saat = hour+":"+minute;
            saatText.setText(saat);


        }
    };

}
