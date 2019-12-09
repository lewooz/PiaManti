package com.levent.pia;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.gdacciaro.iOSDialog.iOSDialog;
import com.gdacciaro.iOSDialog.iOSDialogBuilder;
import com.gdacciaro.iOSDialog.iOSDialogClickListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class AdminActivity extends AppCompatActivity {

    FirebaseAuth mAuth;
    DatabaseReference myRef;
    Switch restoranDurumu;
    HashMap<String,Object> map;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        mAuth= FirebaseAuth.getInstance();
        myRef= FirebaseDatabase.getInstance().getReference();
        map= new HashMap<>();
        restoranDurumu=findViewById(R.id.restoran_durumu_switch);

        myRef.child("Restoran Durum").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if(dataSnapshot.child("durum").getValue().equals("açık"))
                {
                    restoranDurumu.setChecked(true);
                    restoranDurumu.setSelected(true);
                    restoranDurumu.setText("RESTORAN AÇIK");

                }else
                {
                    restoranDurumu.setChecked(false);
                    restoranDurumu.setSelected(false);
                    restoranDurumu.setText("RESTORAN KAPALI");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        restoranDurumu.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                if(restoranDurumu.isChecked())
                {
                    map.put("durum","açık");
                    myRef.child("Restoran Durum").updateChildren(map);
                    restoranDurumu.setText("RESTORAN AÇIK");
                }else
                    {
                        map.put("durum","kapalı");
                        myRef.child("Restoran Durum").updateChildren(map);
                        restoranDurumu.setText("RESTORAN KAPALI");
                    }
            }
        });




    }

    public void logout(View view)
    {

        new iOSDialogBuilder(AdminActivity.this)
                .setTitle("ÇIKIŞ")
                .setSubtitle("Çıkış yapmak istediğinize eminmisiniz? Sonradan bir kez daha giriş yapmanız gerekecektir.")
                .setBoldPositiveLabel(true)
                .setCancelable(false)
                .setPositiveListener("Kabul et",new iOSDialogClickListener() {
                    @Override
                    public void onClick(iOSDialog dialog) {
                        mAuth.signOut();
                        Intent intent = new Intent (AdminActivity.this,uyegiris.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        Toast.makeText(AdminActivity.this,"Çıkış Yapıldı",Toast.LENGTH_LONG).show();
                        dialog.dismiss();
                        finish();

                    }
                })
                .setNegativeListener("Reddet", new iOSDialogClickListener() {
                    @Override
                    public void onClick(iOSDialog dialog) {
                        dialog.dismiss();
                    }
                })
                .build().show();

    }

    public void seeorders(View view)
    {
        Intent intent = new Intent (AdminActivity.this,AdminOrdersActivity.class);

        startActivity(intent);
        Animatoo.animateSlideLeft(AdminActivity.this);
    }

    public void urunyonet(View view)
    {
        Intent intent = new Intent (AdminActivity.this,UrunYonet.class);

        startActivity(intent);
        Animatoo.animateSlideLeft(AdminActivity.this);

    }

    public void gecmissiparisgor(View view)
    {
        Intent intent = new Intent (AdminActivity.this,AdminOldOrdersActivity.class);
        startActivity(intent);
        Animatoo.animateSlideLeft(AdminActivity.this);

    }
}
