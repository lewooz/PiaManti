package com.levent.pia;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.ImageDecoder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.HashMap;

public class UrunEkle extends AppCompatActivity {

    ImageView imageView;
    StorageReference mStorageRef;
    String category;
    Uri selectedImage;
    DatabaseReference mRef;
    EditText description,price,name, kategori, order;
    ProgressDialog loadingBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_urun_ekle);

        imageView = findViewById(R.id.resim);
        mStorageRef = FirebaseStorage.getInstance().getReference();
        mRef=FirebaseDatabase.getInstance().getReference();
        description=findViewById(R.id.description);
        order=findViewById(R.id.sıra);
        price=findViewById(R.id.fiyat);
        name=findViewById(R.id.name);
        kategori=findViewById(R.id.kategori);

        loadingBar= new ProgressDialog(this);
    }

    public void resimsec(View view)
    {
        if(ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},1 );
        }else
            {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent,2);

            }

    }

    public void upload(View view)
    {
        category= kategori.getText().toString();

        loadingBar.setTitle("Ürün Ekleniyor");
        loadingBar.setMessage("Ürün Eklenirken lütfen bekleyiniz");
        loadingBar.setCanceledOnTouchOutside(false);
        loadingBar.show();
        if(!description.getText().toString().equals("")  && !price.getText().toString().equals("") && !name.getText().toString().equals("") )
        {
            StorageReference storageReference = mStorageRef.child(name.getText().toString()+".jpg");

            storageReference.putFile(selectedImage).addOnSuccessListener(this,new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    System.out.println("İŞTE BU: "+selectedImage.toString());

                    StorageReference newReference = FirebaseStorage.getInstance().getReference(name.getText().toString()+".jpg");
                    newReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            String downloadUrl = uri.toString();

                            HashMap<String,Object> productMap = new HashMap<>();
                            productMap.put("image",downloadUrl);
                            productMap.put("name",name.getText().toString());
                            productMap.put("description",description.getText().toString());
                            productMap.put("price",price.getText().toString());
                            productMap.put("open","1");
                            productMap.put("order",Long.parseLong(order.getText().toString()));



                            mRef.child("Products").child(category).child(name.getText().toString()).updateChildren(productMap)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {

                                            if(task.isSuccessful())
                                            {
                                            loadingBar.dismiss();
                                            Intent intent = new Intent(UrunEkle.this,UrunYonet.class);
                                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                            startActivity(intent);
                                            Toast.makeText(UrunEkle.this,"Ürün Eklendi",Toast.LENGTH_LONG).show();
                                            finish();
                                            }
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    loadingBar.dismiss();
                                    Toast.makeText(UrunEkle.this,e.getLocalizedMessage(),Toast.LENGTH_LONG).show();
                                }
                            });
                        }
                    });

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    loadingBar.dismiss();
                    Toast.makeText(UrunEkle.this,e.getLocalizedMessage(),Toast.LENGTH_LONG).show();
                }
            });

        }else
            {
                loadingBar.dismiss();
                Toast.makeText(UrunEkle.this,"Lütfen tüm alanları doldurunuz",Toast.LENGTH_LONG).show();
            }


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode==1 && grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED)
        {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intent,2);
        }


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==2 && resultCode==RESULT_OK && data!=null)
        {
            selectedImage= data.getData();
            if(Build.VERSION.SDK_INT>=28){
                ImageDecoder.Source source =ImageDecoder.createSource(this.getContentResolver(),selectedImage);
                try {
                    Bitmap bitmap = ImageDecoder.decodeBitmap(source);
                    imageView.setImageBitmap(bitmap);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }else{
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(),selectedImage);
                    imageView.setImageBitmap(bitmap);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }
    }
}
