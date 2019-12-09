package com.levent.pia.Urunler;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.levent.pia.KilolukAdapter;
import com.levent.pia.R;
import com.levent.pia.kilolukurun;

public class kilolukurunler extends AppCompatActivity {
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kilolukurunler);

        recyclerView=findViewById(R.id.kilolukurunliste);

        KilolukAdapter adapter = new KilolukAdapter(kilolukurunler.this, kilolukurun.setData());
        recyclerView.setAdapter(adapter);
        recyclerView.setHasFixedSize(true);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(kilolukurunler.this,2);
        recyclerView.setLayoutManager(gridLayoutManager);
    }
}
