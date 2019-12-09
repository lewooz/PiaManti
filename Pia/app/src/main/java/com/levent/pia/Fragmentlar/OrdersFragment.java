package com.levent.pia.Fragmentlar;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.app.Fragment;
import android.widget.Adapter;
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
import com.levent.pia.PastOrdersActivity;
import com.levent.pia.R;

import java.util.ArrayList;

public class OrdersFragment extends Fragment {

    ArrayList<String> nodes;
    DatabaseReference orderListRef;
    FirebaseAuth mAuth;
    FirebaseUser currentOnlineUser;
    String mail,dbmail;
    ListView listView;
    TextView textView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.ordersfragmentml, container , false);

        mAuth = FirebaseAuth.getInstance();

        listView = view.findViewById(R.id.orders_listView);
        textView = view.findViewById(R.id.önceki_siparişler_text);

        currentOnlineUser = mAuth.getCurrentUser();
        mail = currentOnlineUser.getEmail();
        dbmail=mail.replace(".",",");


        nodes= new ArrayList<>();

        orderListRef = FirebaseDatabase.getInstance().getReference().child("Order List").child(dbmail);

        orderListRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                Integer count,i;

                if(dataSnapshot.exists()) {

                    textView.setVisibility(View.GONE);

                    count = (int) (long) dataSnapshot.getChildrenCount();

                    for (i = 1; i <= count; i++) {
                        nodes.add(i.toString() + ".Sipariş");
                    }

                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, nodes);
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

                Intent intent = new Intent(getActivity(), PastOrdersActivity.class);
                intent.putExtra("siparişno", position);
                startActivity(intent);
                Animatoo.animateSlideLeft(getActivity());

            }
        });




        return view;
    }
}
