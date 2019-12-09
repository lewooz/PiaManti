package com.levent.pia;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.ImageDecoder;
import android.media.Image;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;

public class ProfilAyarActivity extends AppCompatActivity {

    String dbmail, downloadUrl;
    TextView username,adres,phoneNo;


    FirebaseDatabase database;
    DatabaseReference myRef;
    Uri selectedImage,imageUri;
    ProgressDialog loadingBar;
    FirebaseUser currentOnlineUser;
    FirebaseAuth mAuth;
    StorageReference mStorageReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profil_ayar);

        dbmail=getIntent().getStringExtra("usermail");

        username=findViewById(R.id.isim_user_ayar);
        adres=findViewById(R.id.adres_user_ayar);
        phoneNo=findViewById(R.id.tel_no_user_ayar);

        myRef = FirebaseDatabase.getInstance().getReference();
        database=FirebaseDatabase.getInstance();
        loadingBar= new ProgressDialog(this);
        mStorageReference=FirebaseStorage.getInstance().getReference();
        downloadUrl="";
        mAuth=FirebaseAuth.getInstance();
        currentOnlineUser=mAuth.getCurrentUser();



        myRef.child("Users").child(dbmail).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                username.setText(dataSnapshot.child("name").getValue().toString());
                adres.setText(dataSnapshot.child("adres").getValue().toString());
                phoneNo.setText(dataSnapshot.child("phone").getValue().toString());


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }



    public void updateprofile(View view) {
        if (!username.getText().toString().equals("") && !phoneNo.getText().toString().equals("") && !adres.getText().toString().equals("")) {

            loadingBar.setTitle("Profiliniz Güncelleniyor");
            loadingBar.setMessage("Profiliniz güncellenirken lütfen bekleyiniz");
            loadingBar.setCanceledOnTouchOutside(false);
            loadingBar.show();
            //if(selectedImage!=null) {



            HashMap<String, Object> map = new HashMap<>();
            map.put("name", username.getText().toString());
            map.put("phone", phoneNo.getText().toString());
            map.put("adres", adres.getText().toString());

            myRef.child("Users").child(dbmail).updateChildren(map);

            loadingBar.dismiss();
            Intent intent = new Intent(ProfilAyarActivity.this,MenuFeedActivity.class);

            startActivity(intent);
            Animatoo.animateCard(ProfilAyarActivity.this);
            finish();

        }else
            {
                loadingBar.dismiss();
                Toast.makeText(ProfilAyarActivity.this,"Lütfen tüm alanları doğru bir şekilde doldurunuz",Toast.LENGTH_LONG).show();
            }
    }
}
