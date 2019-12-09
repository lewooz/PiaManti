package com.levent.pia;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.gdacciaro.iOSDialog.iOSDialog;
import com.gdacciaro.iOSDialog.iOSDialogBuilder;
import com.gdacciaro.iOSDialog.iOSDialogClickListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class loginscreen extends AppCompatActivity {

    FirebaseAuth mAuth;
    DatabaseReference rootRef;
    EditText email,password;
    ProgressDialog loadingBar;
    String dbmail;
    AlertDialog alertDialog;
    Button kullanıcıButon, adminButon;
    View v;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loginscreen);

        LayoutInflater layoutInflater = getLayoutInflater();
        v = layoutInflater.inflate(R.layout.logout_alertdialog,null);

        kullanıcıButon =v.findViewById(R.id.kullanıcı_buton);
        adminButon=v.findViewById(R.id.admin_buton);

        alertDialog = new AlertDialog.Builder(this).create();


        rootRef= FirebaseDatabase.getInstance().getReference();
        mAuth=FirebaseAuth.getInstance();
        email=findViewById(R.id.email);
        password = findViewById(R.id.sifre);
        loadingBar=new ProgressDialog(loginscreen.this);

    }


    public void resetpass(View view)
    {
        Intent intent = new Intent(loginscreen.this, ResetPasswordActivity.class);
        startActivity(intent);
        Animatoo.animateSlideLeft(loginscreen.this);
    }

    public void signin(View view)
    {

        if(!email.getText().toString().equals("")&&!password.getText().toString().equals("")) {
            loadingBar.setTitle("Giriş Yapılıyor");
            loadingBar.setMessage("Bilgileriniz kontrol edilirken lütfen bekleyiniz");
            loadingBar.setCanceledOnTouchOutside(false);
            loadingBar.show();
            mAuth.signInWithEmailAndPassword(email.getText().toString(), password.getText().toString())
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            if (task.isSuccessful()) {
                                dbmail = email.getText().toString().replace(".", ",");
                                rootRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        if (dataSnapshot.child("Admins").child(dbmail).exists()) {


                                            alertDialog.setView(v);
                                            alertDialog.setCanceledOnTouchOutside(false);
                                            alertDialog.show();

                                            kullanıcıButon.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View view) {
                                                    loadingBar.dismiss();
                                                    Intent intent = new Intent(loginscreen.this, UrunCesitSecim.class);

                                                    startActivity(intent);
                                                    Toast.makeText(loginscreen.this, "Kullanıcı Arayüzüne Giriş Yapıldı", Toast.LENGTH_LONG).show();
                                                    alertDialog.dismiss();
                                                    finish();
                                                }
                                            });

                                            adminButon.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View view) {
                                                    loadingBar.dismiss();
                                                    Intent intent = new Intent(loginscreen.this, AdminActivity.class);

                                                    startActivity(intent);
                                                    Toast.makeText(loginscreen.this, "Admin Arayüzüne Giriş Yapıldı", Toast.LENGTH_LONG).show();
                                                    alertDialog.dismiss();
                                                    finish();
                                                }
                                            });


                                        } else {

                                            loadingBar.dismiss();
                                            Intent intent = new Intent(loginscreen.this, UrunCesitSecim.class);
                                            startActivity(intent);
                                            Toast toast = Toast.makeText(loginscreen.this, "Lezzet kapısından giriş yaptınız!", Toast.LENGTH_LONG);
                                            toast.show();
                                        }


                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });

                            }


                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    loadingBar.dismiss();
                    Toast toast = Toast.makeText(loginscreen.this, e.getLocalizedMessage(), Toast.LENGTH_LONG);
                    toast.show();
                }
            });
        }else
            {
                Toast.makeText(getApplicationContext(),"Lütfen mail ve şifrenizi giriniz.",Toast.LENGTH_LONG).show();
            }
    }

    @Override
    public void onBackPressed() {

        super.onBackPressed();
        loadingBar.dismiss();
        alertDialog.dismiss();

    }
}
