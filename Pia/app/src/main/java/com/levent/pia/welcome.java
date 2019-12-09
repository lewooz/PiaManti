package com.levent.pia;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class welcome extends AppCompatActivity {

    FirebaseAuth mAuth;
    FirebaseUser currentOnlineUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        mAuth=FirebaseAuth.getInstance();
        currentOnlineUser=mAuth.getCurrentUser();
    }

    public void hakkimizda (View view)
    {
        Intent intent = new Intent(welcome.this,hakkimizda.class);
        startActivity(intent);

    }

    public void iletisim (View view)
    {
        Intent intent = new Intent(welcome.this,iletisim.class);
        startActivity(intent);

    }

    public void menu (View view)
    {
        Intent intent = new Intent(welcome.this,menu.class);
        startActivity(intent);

    }

    public void siparis (View view)
    {
        if(currentOnlineUser != null)
        {
            Intent intent = new Intent(welcome.this, UrunCesitSecim.class);
            startActivity(intent);
        }else {
            Intent intent = new Intent(welcome.this, uyegiris.class);
            startActivity(intent);
        }

    }


}
