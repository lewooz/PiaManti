package com.levent.pia;



import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;

import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import androidx.appcompat.widget.Toolbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.FragmentTransaction;




import com.blogspot.atifsoftwares.animatoolib.Animatoo;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.levent.pia.Fragmentlar.CartFragment;
import com.levent.pia.Fragmentlar.MenuFragment;
import com.levent.pia.Fragmentlar.OrdersFragment;
import com.levent.pia.Model.Cart;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mikepenz.materialdrawer.util.AbstractDrawerImageLoader;
import com.mikepenz.materialdrawer.util.DrawerImageLoader;

import com.squareup.picasso.Picasso;


import cn.pedant.SweetAlert.SweetAlertDialog;

public class MenuFeedActivity extends AppCompatActivity  {

    FirebaseAuth mAuth;
    FirebaseUser currentOnlineUser;

    public Toolbar toolbar;
    Drawable image, background2, sepetimbackground, menubackground, siparisbackground;

    FragmentManager manager;
    Fragment fragment;
    DrawerLayout drawerLayout;
    String mail,dbmail,dbusername;
    String nerden;
    int kontrol;
    PrimaryDrawerItem menu;




    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_feed);




        mAuth=FirebaseAuth.getInstance();
        currentOnlineUser=mAuth.getCurrentUser();
        mail = currentOnlineUser.getEmail();
        toolbar= findViewById(R.id.mytoolbar);
        image= getResources().getDrawable(R.drawable.draweralt);
        background2 = getResources().getDrawable(R.drawable.menudekiurunlerimiz);
        sepetimbackground = getResources().getDrawable(R.drawable.sepetimbackground);
        menubackground = getResources().getDrawable(R.drawable.menudekiurunlerimiz);
        siparisbackground= getResources().getDrawable(R.drawable.oncekisiparisbackground);

        dbmail = mail.replace(".", ",");



        nerden="";
        if(getIntent().getStringExtra("nerden")!=null)
        {
            nerden=getIntent().getStringExtra("nerden");
        }


        dbusername=currentOnlineUser.getDisplayName();

        manager= getFragmentManager();




        switch (nerden) {

            case "sepet":


                    CartFragment cartFragment = new CartFragment();
                    FragmentTransaction tn5 = manager.beginTransaction();
                    tn5.replace(R.id.container, cartFragment);
                    toolbar.setBackground(sepetimbackground);
                    tn5.addToBackStack(null);
                    tn5.commit();

                break;

            default:
            MenuFragment menuFragment = new MenuFragment();
            FragmentTransaction tn4 = manager.beginTransaction();
            tn4.replace(R.id.container, menuFragment);
            tn4.addToBackStack(null);
            tn4.commit();
            break;

        }





         menu = new PrimaryDrawerItem().withSelectable(false).withIcon(R.drawable.menuicon).withIdentifier(1).withName("Menü");
        PrimaryDrawerItem sepetim = new PrimaryDrawerItem().withSelectable(false).withIcon(R.drawable.basketicon).withIdentifier(1).withName("Sepetim");
        PrimaryDrawerItem siparislerim = new PrimaryDrawerItem().withSelectable(false).withIcon(R.drawable.ordersicon).withIdentifier(1).withName("Siparişlerim");
        PrimaryDrawerItem userinfo = new PrimaryDrawerItem().withSelectable(false).withIcon(R.drawable.uyeinfoicon).withIdentifier(1).withName("Üyeliğimi Yönet");
        PrimaryDrawerItem logout = new PrimaryDrawerItem().withSelectable(false).withIcon(R.drawable.logouticon).withIdentifier(1).withName("Çıkış Yap");



        AccountHeader headerResult = new AccountHeaderBuilder()
                .withActivity(this)
                .withHeaderBackground(R.drawable.drawerust)
                .withSelectionListEnabledForSingleProfile(false)
                .withProfileImagesVisible(false)
                .addProfiles(
                        new ProfileDrawerItem().withName(dbusername).withEmail(mail)
                )

                .build();





        final Drawer result = new DrawerBuilder()
                .withActivity(this)

                .withSliderBackgroundDrawable(image)
                .withSelectedItem(-1)
                .withToolbar(toolbar)
                .withDisplayBelowStatusBar(true)
                .withTranslucentStatusBar(true)
                .withHeaderPadding(true)
                .withStickyFooterShadow(true)
                .withMultiSelect(false)
                .withCloseOnClick(true)
                .withActionBarDrawerToggleAnimated(true)
                .withAccountHeader(headerResult)
                .addDrawerItems(
                        menu,
                        sepetim,
                        siparislerim,
                        userinfo,
                        logout

                )
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {

                        switch (position) {

                            case 1:
                                menu.isSelected();
                                fragment=manager.findFragmentById(R.id.container);
                                if(!(fragment instanceof MenuFragment)) {
                                    MenuFragment menuFragment = new MenuFragment();
                                    FragmentTransaction tn = manager.beginTransaction();

                                    tn.replace(R.id.container, menuFragment);
                                    tn.addToBackStack(null);
                                    tn.commit();
                                    toolbar.setBackground(menubackground);

                                }
                                    drawerLayout.closeDrawers();


                                break;

                            case 2:
                                fragment=manager.findFragmentById(R.id.container);
                                if(!(fragment instanceof CartFragment)) {
                                    CartFragment cartFragment = new CartFragment();
                                    FragmentTransaction tn2 = manager.beginTransaction();
                                    tn2.replace(R.id.container, cartFragment);
                                    tn2.addToBackStack(null);
                                    tn2.commit();
                                    toolbar.setBackground(sepetimbackground);
                                }
                                drawerLayout.closeDrawers();

                                break;

                            case 3:
                                fragment=manager.findFragmentById(R.id.container);
                                if(!(fragment instanceof OrdersFragment)) {
                                    OrdersFragment ordersFragment = new OrdersFragment();
                                    FragmentTransaction tn3 = manager.beginTransaction();
                                    tn3.replace(R.id.container, ordersFragment);
                                    tn3.addToBackStack(null);
                                    tn3.commit();
                                    toolbar.setBackground(siparisbackground);
                                }
                                drawerLayout.closeDrawers();
                                break;

                            case 4:

                                Intent intent = new Intent(MenuFeedActivity.this,ProfilAyarActivity.class);
                                intent.putExtra("usermail",dbmail);
                                startActivity(intent);
                                Animatoo.animateSlideDown(MenuFeedActivity.this);


                                drawerLayout.closeDrawers();
                                break;

                            case 5:

                                new SweetAlertDialog(MenuFeedActivity.this, SweetAlertDialog.WARNING_TYPE)
                                        .setTitleText("ÇIKIŞ")
                                        .setContentText("Çıkış yapmak istediğinize eminmisiniz? Sonradan bir kez daha giriş yapmanız gerekecektir.")
                                        .setConfirmText("Kabul Et")
                                        .setCancelText("Reddet")
                                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                            @Override
                                            public void onClick(SweetAlertDialog sDialog) {
                                                sDialog
                                                        .setTitleText("Çıkış Yapıldı")
                                                        .setContentText("Hesabınızdan çıkış yaptınız.")
                                                        .setConfirmText("Tamam")
                                                        .showCancelButton(false)
                                                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                                            @Override
                                                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                                                mAuth.signOut();
                                                                Intent intent = new Intent (MenuFeedActivity.this,uyegiris.class);
                                                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                                startActivity(intent);

                                                                finish();
                                                            }
                                                        })
                                                        .changeAlertType(SweetAlertDialog.SUCCESS_TYPE);

                                            }
                                        }).setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                                        sweetAlertDialog.cancel();
                                    }
                                })
                                        .show();


                        }

                        return  true;
                    }

                })
                .build();
        drawerLayout = result.getDrawerLayout();

        System.out.println("kontrol: " + kontrol);


    }



    @Override
    public void onBackPressed(){
        if (manager.getBackStackEntryCount() == 1){
            finish();
        }
        else {
            super.onBackPressed();
        }

        fragment=manager.findFragmentById(R.id.container);
        if(fragment instanceof MenuFragment)
        {
            toolbar.setBackground(menubackground);
        }
        if(fragment instanceof CartFragment)
        {
            toolbar.setBackground(sepetimbackground);
        }if(fragment instanceof OrdersFragment)
        {
            toolbar.setBackground(siparisbackground);
        }
    }
}








