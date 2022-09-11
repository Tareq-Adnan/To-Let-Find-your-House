package com.example.tolet;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.example.tolet.Model.users;
import com.example.tolet.Prevelent.prevalent;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import io.paperdb.Paper;

public class SplashActivity extends AppCompatActivity {

    ProgressDialog bar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        Paper.init(this);


        String phoneKey = Paper.book().read(prevalent.userPhoneKey);
        String passwordKey = Paper.book().read(prevalent.userPasswordKey);
        if (phoneKey != "" && passwordKey != "") {

            if (!TextUtils.isEmpty(phoneKey) && !TextUtils.isEmpty(passwordKey)) {
                Access(phoneKey, passwordKey);
            } else {

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }, 5000);
            }
        }
    }


    private void Access(String email, String pass) {

        final DatabaseReference node = FirebaseDatabase.getInstance().getReference();
        node.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.child("users").child(email).exists() || snapshot.child("users").child("email").child(email).exists()) {

                    users userdata = snapshot.child("users").child(email).getValue(users.class);
                    if (userdata.getPhone().equals(email) || userdata.getEmail().equals(email)) {
                        if (userdata.getPassword().equals(pass)) {
                            Toast.makeText(SplashActivity.this, "Login Success!", Toast.LENGTH_SHORT).show();

                            Intent intent = new Intent(SplashActivity.this, HomeActivity.class);
                            prevalent.cOnlineUser=userdata;
                            startActivity(intent);

                        }
                    }

                } else {
                    Toast.makeText(SplashActivity.this, "Login Failed! No Account Found!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}