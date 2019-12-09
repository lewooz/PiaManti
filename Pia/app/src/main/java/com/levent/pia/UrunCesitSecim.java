package com.levent.pia;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.gdacciaro.iOSDialog.iOSDialog;
import com.gdacciaro.iOSDialog.iOSDialogBuilder;
import com.gdacciaro.iOSDialog.iOSDialogClickListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.omega_r.libs.OmegaCenterIconButton;

import java.util.Calendar;

public class UrunCesitSecim extends AppCompatActivity {


    DatabaseReference myRef;
    String mail, restoranDurum;
    AlertDialog alertDialog;



    int saat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_urun_cesit_secim);

        myRef=FirebaseDatabase.getInstance().getReference();

        View view = getLayoutInflater().inflate(R.layout.loading_alert_dialog,null,false);
        alertDialog = new AlertDialog.Builder(UrunCesitSecim.this).create();
        alertDialog.setView(view);
        alertDialog.setCanceledOnTouchOutside(false);


        Calendar rightNow = Calendar.getInstance();
        saat = rightNow.get(Calendar.HOUR_OF_DAY);


    }

    @Override
    protected void onResume() {
        super.onResume();
        if(alertDialog.isShowing())
        {
            alertDialog.dismiss();
        }
    }

    public void menugiris(View view)
    {
        alertDialog.show();
        myRef.child("Restoran Durum").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                restoranDurum = dataSnapshot.child("durum").getValue().toString();

                if(saat<=10 || saat>=22)
                {
                    Toast.makeText(getApplicationContext(),"Paket servis hizmet saatleri içerisinde olmadığımızdan sipariş alamıyoruz.", Toast.LENGTH_LONG).show();
                    alertDialog.dismiss();
                }else if(restoranDurum.equals("kapalı"))
                {
                    Toast.makeText(getApplicationContext(),"ŞU ANDA YOĞUNLUKTAN DOLAYI HİZMET VEREMEMEKTEYİZ. LÜTFEN DAHA SONRA TEKRAR DENEYİNİZ", Toast.LENGTH_LONG).show();
                    alertDialog.dismiss();


                }else if(restoranDurum.equals("açık"))
                {


                    Intent intent = new Intent(UrunCesitSecim.this,MenuFeedActivity.class);
                    startActivity(intent);
                    Animatoo.animateSplit(UrunCesitSecim.this);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }




}
