package com.levent.pia;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.levent.pia.Urunler.cigborekhk;
import com.levent.pia.Urunler.citirhk;
import com.levent.pia.Urunler.etlihk;
import com.levent.pia.Urunler.gozlemehk;
import com.levent.pia.Urunler.iclihk;
import com.levent.pia.Urunler.kilolukurunler;
import com.levent.pia.Urunler.mantihk;
import com.levent.pia.Urunler.schnitzelhk;
import com.levent.pia.Urunler.zyaglihk;
import com.smarteist.autoimageslider.SliderView;

public class menu extends AppCompatActivity {

    SliderView s1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        s1 = findViewById(R.id.imageSlider);
        s1.setSliderAdapter(new SliderAdapter(this));
    }

    public void manti(View view) {
        int pos;
        pos = s1.getCurrentPagePosition();

        switch (pos) {

            case 0:
                Intent i0 = new Intent(menu.this, mantihk.class);
                startActivity(i0);
                Animatoo.animateZoom(menu.this);
                break;
            case 1:
                Intent i1 = new Intent(menu.this, citirhk.class);
                startActivity(i1);
                Animatoo.animateZoom(menu.this);
                break;
            case 2:
                Intent i2= new Intent(menu.this, zyaglihk.class);
                startActivity(i2);
                Animatoo.animateZoom(menu.this);
                break;
            case 3:
                Intent i3 = new Intent(menu.this, gozlemehk.class);
                startActivity(i3);
                Animatoo.animateZoom(menu.this);
                break;
            case 4:
                Intent i4 = new Intent(menu.this, cigborekhk.class);
                startActivity(i4);
                Animatoo.animateZoom(menu.this);
                break;
            case 5:
                Intent i5 = new Intent(menu.this, iclihk.class);
                startActivity(i5);
                Animatoo.animateZoom(menu.this);
                break;
            case 6:
                Intent i6 = new Intent(menu.this, schnitzelhk.class);
                startActivity(i6);
                Animatoo.animateZoom(menu.this);
                break;
            case 7:
                Intent i7 = new Intent(menu.this, etlihk.class);
                startActivity(i7);
                Animatoo.animateZoom(menu.this);
                break;

        }
    }

    public void kilolukurunler(View view)
    {
        Intent intent = new Intent(menu.this, kilolukurunler.class);
        startActivity(intent);
        Animatoo.animateZoom(menu.this);
    }
}
