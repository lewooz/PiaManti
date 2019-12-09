package com.levent.pia;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;

import com.github.techisfun.slidingswitch.SlideListener;
import com.github.techisfun.slidingswitch.SlidingSwitch;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.prefs.Preferences;

import static android.content.Context.MODE_PRIVATE;

public class CustomAdapter extends ArrayAdapter<String> {

    private final ArrayList<String> productName;
    private final ArrayList<String> productPrice;
    private final ArrayList<String> productDescription;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference myRef;


    ToggleButton toggle;
    SharedPreferences sp;

    private final Activity context;

    public CustomAdapter(ArrayList<String> productName, ArrayList<String> productPrice, ArrayList<String> productDescription, Activity context) {
        super(context,R.layout.urun_yonet_row,productName);
        this.productName = productName;
        this.productPrice = productPrice;
        this.productDescription = productDescription;
        this.context = context;
    }

    @Override
    public int getCount() {
        return productName.size();
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        LayoutInflater layoutInflater= context.getLayoutInflater();
        View view = layoutInflater.inflate(R.layout.urun_yonet_row,null,true);
        toggle=view.findViewById(R.id.switcher);
        TextView urunAdiText = view.findViewById(R.id.ürün_isim);
        TextView urunFiyatText = view.findViewById(R.id.ürün_fiyat);


        urunAdiText.setText(productName.get(position));
        urunFiyatText.setText(productPrice.get(position));
        myRef=FirebaseDatabase.getInstance().getReference();
        firebaseDatabase=FirebaseDatabase.getInstance();

        sp= PreferenceManager.getDefaultSharedPreferences(context);

        toggle.setChecked(sp.getBoolean("CheckValue"+position, true));


        toggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {


                if(b)
                {
                    HashMap<String,Object> map = new HashMap<>();
                    map.put("open","1");
                    myRef.child("Products").child("menu").child(productName.get(position)).updateChildren(map);


                    SharedPreferences.Editor editor=sp.edit();
                    editor.putBoolean("CheckValue"+position, true);
                    editor.apply();


                }else
                    {
                        HashMap<String,Object> map = new HashMap<>();
                        map.put("open","0");
                        myRef.child("Products").child("menu").child(productName.get(position)).updateChildren(map);

                        SharedPreferences.Editor editor=sp.edit();
                        editor.putBoolean("CheckValue"+position, false);
                        editor.apply();

                    }
            }
        });



        return view;
    }


}
