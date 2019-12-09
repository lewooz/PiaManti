package com.levent.pia;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

public class iletisim extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_iletisim);

        addFragment(new MapFragment(), false, "one");

    }

    public void addFragment(Fragment fragment, boolean addToBackStack, String tag) {
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction ft = manager.beginTransaction();

        if (addToBackStack) {
            ft.addToBackStack(tag);
        }
        ft.replace(R.id.container_frame_back, fragment, tag);
        ft.commitAllowingStateLoss();
    }

    public void instagram (View view)
    {
        Uri uri = Uri.parse("https://www.instagram.com/pia.manti/?igshid=11z42bvs3j4om");
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        intent.setPackage("com.instagram.android");
        try {
            startActivity(intent);
        }catch (ActivityNotFoundException e){
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.instagram.com/pia.manti/?igshid=11z42bvs3j4om")));
        }

    }
    public void facebook (View view)
    {
        Uri uri = Uri.parse("https://www.facebook.com/");
        Intent intent = new Intent(Intent.ACTION_VIEW,uri);
        intent.setPackage("com.facebook.android");
        try {
            startActivity(intent);
        }catch (ActivityNotFoundException e)
        {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/")));
        }
    }
    public void twitter (View view)
    {
        Uri uri = Uri.parse("https://twitter.com/");
        Intent intent = new Intent(Intent.ACTION_VIEW,uri);
        intent.setPackage("com.tripadvisor.android");
        try {
            startActivity(intent);
        }catch (ActivityNotFoundException e)
        {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://twitter.com/")));
        }
    }
    public void trip (View view)
    {
        Uri uri = Uri.parse("https://www.tripadvisor.com.tr");
        Intent intent = new Intent(Intent.ACTION_VIEW,uri);
        intent.setPackage("com.tripadvisor.android");
        try {
            startActivity(intent);
        }catch (ActivityNotFoundException e)
        {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.tripadvisor.com.tr")));
        }
    }
}
