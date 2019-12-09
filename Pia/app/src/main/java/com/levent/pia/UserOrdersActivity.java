package com.levent.pia;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class UserOrdersActivity extends AppCompatActivity {

    ArrayList<String> nodes;
    DatabaseReference orderListRef;
    FirebaseAuth mAuth;
    FirebaseUser currentOnlineUser;
    String mail,dbmail;
    ListView listView;
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_orders);

        mAuth = FirebaseAuth.getInstance();

        listView = findViewById(R.id.user_orders_listView);
        textView = findViewById(R.id.user_önceki_siparişler_text);


        mail = getIntent().getStringExtra("mail");
        dbmail=mail.replace(".",",");


        nodes= new ArrayList<>();

        orderListRef = FirebaseDatabase.getInstance().getReference().child("Order List").child(dbmail);

        orderListRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                Integer count,i;
                System.out.println("İŞTE MAİL: "+dbmail);
                if(dataSnapshot.exists()) {


                    textView.setVisibility(View.GONE);

                    count = (int) (long) dataSnapshot.getChildrenCount();

                    for (i = 1; i <= count; i++) {
                        nodes.add(i.toString() + ".Sipariş");
                    }

                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(UserOrdersActivity.this, android.R.layout.simple_list_item_1, nodes);
                    listView.setAdapter(adapter);
                }else
                {
                    listView.setVisibility(View.GONE);
                    textView.setVisibility(View.VISIBLE);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                int pozisyon;
                String position;
                pozisyon = i+1;

                position= Integer.toString(pozisyon);

                Intent intent = new Intent(UserOrdersActivity.this, AdminPastOrdersActivity.class);
                intent.putExtra("siparişno", position);
                intent.putExtra("mail",dbmail);
                startActivity(intent);
                Animatoo.animateSlideLeft(UserOrdersActivity.this);

            }
        });

    }
}
