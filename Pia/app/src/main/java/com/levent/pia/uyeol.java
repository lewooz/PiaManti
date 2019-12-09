package com.levent.pia;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLOutput;
import java.util.HashMap;

public class uyeol extends AppCompatActivity {

    EditText name,pass,phoneNo,adres,email;
    Button uyeol;
    ProgressDialog loadingBar;
    FirebaseAuth mAuth;

    DatabaseReference rootRef;
    String telNo;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_uyeol);

        phoneNo = findViewById(R.id.phone_number);
        name = findViewById(R.id.isim);
        pass = findViewById(R.id.sifre);
        adres = findViewById(R.id.adres);
        uyeol=findViewById(R.id.uyeol_buton);
        email=findViewById(R.id.mail);
        telNo = "05";




        loadingBar = new ProgressDialog(this);
        rootRef= FirebaseDatabase.getInstance().getReference();
        mAuth=FirebaseAuth.getInstance();




    }

    public void signup(View view)
    {
        if(!TextUtils.isEmpty(phoneNo.getText())&& !TextUtils.isEmpty(name.getText())&& !TextUtils.isEmpty(pass.getText())&& !TextUtils.isEmpty(adres.getText())&& !TextUtils.isEmpty(email.getText()))
        {
            if(!TextUtils.isEmpty(email.getText()) && email.getText().toString().toLowerCase().contains("@")) {
                loadingBar.setTitle("Üyelik Yaratılıyor");
                loadingBar.setMessage("Bilgileriniz kontrol edilirken lütfen bekleyiniz");
                loadingBar.setCanceledOnTouchOutside(false);
                loadingBar.show();

                mAuth.createUserWithEmailAndPassword(email.getText().toString(), pass.getText().toString())
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {

                                if (task.isSuccessful()) {
                                    loadingBar.dismiss();
                                    rootRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                            HashMap<String, Object> userDataMap = new HashMap<>();
                                            userDataMap.put("phone", phoneNo.getText().toString());
                                            userDataMap.put("mail", email.getText().toString());
                                            userDataMap.put("name", name.getText().toString());
                                            userDataMap.put("adres", adres.getText().toString());
                                            userDataMap.put("siparissayi", "1");
                                            userDataMap.put("userimage", "");
                                            String dbmail = email.getText().toString().replace(".", ",");
                                            rootRef.child("Users").child(dbmail).updateChildren(userDataMap);

                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError databaseError) {

                                        }
                                    });
                                    FirebaseUser currentUser = mAuth.getCurrentUser();
                                    UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                            .setDisplayName(name.getText().toString())
                                            .build();

                                    currentUser.updateProfile(profileUpdates).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if(task.isSuccessful())
                                            {
                                                System.out.println("done");

                                            }
                                        }
                                    });
                                    Intent intent = new Intent(uyeol.this, UrunCesitSecim.class);
                                    startActivity(intent);
                                    Toast toast = Toast.makeText(uyeol.this, "Tebrikler! Üye oldunuz.", Toast.LENGTH_LONG);
                                    toast.show();
                                }

                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        loadingBar.dismiss();
                        Toast toast = Toast.makeText(uyeol.this, e.getLocalizedMessage(), Toast.LENGTH_LONG);
                        toast.show();
                    }
                });
            }else
                {
                    Toast toast = Toast.makeText(uyeol.this, "Lütfen doğru bir mail adresi giriniz.", Toast.LENGTH_LONG);
                    toast.show();
                }
        }else
            {
                Toast toast = Toast.makeText(uyeol.this, "Lütfen tüm alanları doldurunuz.", Toast.LENGTH_LONG);
                toast.show();

        }
    }

}
















