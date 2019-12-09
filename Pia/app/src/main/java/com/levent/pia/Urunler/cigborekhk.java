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
import com.levent.pia.welcome;

public class cigborekhk extends AppCompatActivity {

    FirebaseAuth mAuth;
    FirebaseUser currentOnlineUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cigborekhk);

        mAuth=FirebaseAuth.getInstance();
        currentOnlineUser= mAuth.getCurrentUser();
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Animatoo.animateFade(cigborekhk.this);
    }

    public void siparis(View view)

    {
        if(currentOnlineUser != null)
        {
            Intent intent = new Intent(cigborekhk.this, UrunCesitSecim.class);
            startActivity(intent);
            Animatoo.animateWindmill(cigborekhk.this);
        }else
            {
            Intent intent = new Intent(cigborekhk.this, uyegiris.class);
            startActivity(intent);
            Animatoo.animateWindmill(cigborekhk.this);

        }


    }
}
