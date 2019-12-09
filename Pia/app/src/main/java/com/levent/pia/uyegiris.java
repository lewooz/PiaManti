package com.levent.pia;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;

public class uyegiris extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_uyegiris);
    }

    public void login(View view)

    {
        Intent intent = new Intent(uyegiris.this,loginscreen.class);
        startActivity(intent);
    }

    public void uyeol(View view)

    {
        Intent intent = new Intent(uyegiris.this,uyeol.class);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(uyegiris.this,welcome.class);
        startActivity(intent);

    }
}
