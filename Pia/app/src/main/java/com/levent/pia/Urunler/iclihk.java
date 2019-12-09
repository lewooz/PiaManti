package com.levent.pia.Urunler;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.levent.pia.R;
import com.levent.pia.UrunCesitSecim;
import com.levent.pia.uyegiris;

public class iclihk extends AppCompatActivity {

    FirebaseAuth mAuth;
    FirebaseUser currentOnlineUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_iclihk);

        mAuth= FirebaseAuth.getInstance();
        currentOnlineUser= mAuth.getCurrentUser();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Animatoo.animateFade(iclihk.this);
    }

    public void siparis(View view)
    {
        if(currentOnlineUser != null)
        {
            Intent intent = new Intent(iclihk.this, UrunCesitSecim.class);
            startActivity(intent);
            Animatoo.animateWindmill(iclihk.this);
        }else
        {
            Intent intent = new Intent(iclihk.this, uyegiris.class);
            startActivity(intent);
            Animatoo.animateWindmill(iclihk.this);

        }

    }
}
