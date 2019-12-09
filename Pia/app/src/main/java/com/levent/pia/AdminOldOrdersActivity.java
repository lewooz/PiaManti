package com.levent.pia;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class AdminOldOrdersActivity extends AppCompatActivity {

    ListView listView;
    ArrayAdapter adapter;
    List<String> userList;
    DatabaseReference myRef;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_old_orders);

        listView=findViewById(R.id.admin_orders_listView);
        userList= new ArrayList<>();
        myRef = FirebaseDatabase.getInstance().getReference().child("Users");
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for(DataSnapshot data : dataSnapshot.getChildren())
                {

                    String orderNumber;
                    Integer ordNumber;
                    if(!data.child("siparissayi").getValue().toString().equals("1")) {
                        orderNumber = data.child("siparissayi").getValue().toString();
                        ordNumber = Integer.parseInt(orderNumber) - 1;
                        orderNumber = ordNumber.toString();
                    }else
                        {
                            orderNumber="0";
                        }
                    userList.add(data.child("mail").getValue().toString()+"    "+orderNumber+" Sipariş");
                    adapter = new ArrayAdapter(AdminOldOrdersActivity.this,android.R.layout.simple_list_item_1,userList);
                    listView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                }



            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                String mail = userList.get(i);
                mail= mail.substring(0, mail.indexOf(" "));
                Intent intent = new Intent(AdminOldOrdersActivity.this,UserOrdersActivity.class);
                intent.putExtra("mail",mail);
                startActivity(intent);
                Animatoo.animateSlideLeft(AdminOldOrdersActivity.this);
            }
        });

    }
}
