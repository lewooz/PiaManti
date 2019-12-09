package com.levent.pia;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ListView;

import androidx.appcompat.widget.Toolbar;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class UrunYonet extends AppCompatActivity {

    ListView listView;
    ArrayList<String> productNameFromFB;
    ArrayList<String> productPriceFromFB;
    ArrayList<String> productDescriptionFromFB;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference myRef;
    CustomAdapter adapter;

    Toolbar toolbar;



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater= getMenuInflater();
        menuInflater.inflate(R.menu.additemmenu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();
        if(id==R.id.urunekle)
        {
            Intent intent = new Intent (UrunYonet.this,UrunEkle.class);
            startActivity(intent);

        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_urun_yonet);

        toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        listView=findViewById(R.id.listview);
        productNameFromFB = new ArrayList<>();
        productPriceFromFB = new ArrayList<>();
        productDescriptionFromFB = new ArrayList<>();



        firebaseDatabase = FirebaseDatabase.getInstance();
        myRef=FirebaseDatabase.getInstance().getReference();

        adapter = new CustomAdapter(productNameFromFB,productPriceFromFB,productDescriptionFromFB,this);
        listView.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        verileriCek();


    }

    private void verileriCek() {

        myRef.child("Products").child("menu").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for(DataSnapshot ds : dataSnapshot.getChildren())
                {
                    HashMap<String,String> hashMap = (HashMap<String, String>) ds.getValue();
                    productNameFromFB.add(hashMap.get("name"));
                    productDescriptionFromFB.add(hashMap.get("description"));
                    productPriceFromFB.add(hashMap.get("price"));
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


}
