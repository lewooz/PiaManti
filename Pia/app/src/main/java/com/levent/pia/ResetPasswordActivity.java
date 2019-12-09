package com.levent.pia;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ResetPasswordActivity extends AppCompatActivity {

    EditText mail;
    ProgressDialog loadingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

        mail=findViewById(R.id.reset_password_mail);
        loadingBar= new ProgressDialog(this);
    }

    public void reset(View view)
    {
        if(!mail.getText().toString().equals("")) {
            loadingBar.setTitle("İşlem Yapılıyor");
            loadingBar.setMessage("Sıfırlama Mailı Gönderiliyor. Lütfen Bekleyiniz...");
            loadingBar.setCanceledOnTouchOutside(false);
            loadingBar.show();

            FirebaseAuth auth = FirebaseAuth.getInstance();
            String emailAddress = mail.getText().toString();

            auth.sendPasswordResetEmail(emailAddress)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(getApplicationContext(), "Sıfırlama mailı gönderilmiştir.", Toast.LENGTH_LONG).show();
                                Intent intent = new Intent(ResetPasswordActivity.this, loginscreen.class);
                                startActivity(intent);
                                Animatoo.animateSlideRight(ResetPasswordActivity.this);
                                loadingBar.dismiss();
                                finish();

                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                    loadingBar.dismiss();

                    Toast.makeText(getApplicationContext(), e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                }
            });
        }else
            {
                Toast.makeText(getApplicationContext(),"Lütfen mail adresi giriniz.",Toast.LENGTH_LONG).show();
            }
    }
}
