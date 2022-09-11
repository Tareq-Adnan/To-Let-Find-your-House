package com.example.tolet;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.example.tolet.Model.admin;
import com.example.tolet.Model.users;
import com.example.tolet.Prevelent.prevalent;
import com.example.tolet.databinding.ActivityLoginBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import io.paperdb.Paper;

public class LoginActivity extends AppCompatActivity {

    ActivityLoginBinding b;
    SharedPreferences localdb;
    SharedPreferences.Editor dbeditor;
    ProgressDialog bar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        b = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(b.getRoot());
        localdb=getSharedPreferences("userDetails",Context.MODE_PRIVATE);
        dbeditor=localdb.edit();
        bar=new ProgressDialog(this);


        Paper.init(this);
        b.signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, RegistrationActivity.class);
                startActivity(intent);
            }
        });
        b.signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loading();
                loginUser();

            }
        });
    }

    private void loginUser() {
        String mobile = b.emailorphone.getText().toString(), pass = b.password.getText().toString();

        if (TextUtils.isEmpty(mobile)) {
            Toast.makeText(this, "Email or Mobile number is required!", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(pass)) {
            Toast.makeText(this, "password is required", Toast.LENGTH_SHORT).show();
        } else {
            if (mobile.equals("admin")){
                admin(mobile, pass);
            }
            else{
                AllowAccess(mobile, pass);
            }

        }
    }

    private void admin(String mobile, String pass) {
        if (b.RememberMe.isChecked()) {
            Paper.book().write(prevalent.userPhoneKey, mobile);
            Paper.book().write(prevalent.userPasswordKey, pass);
        }

        final DatabaseReference node = FirebaseDatabase.getInstance().getReference();
        node.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.child("Admin").child(mobile).exists()) {

                    admin userdata = snapshot.child("Admin").child(mobile).getValue(admin.class);
                    assert userdata != null;
                    if (userdata.getUser().equals(mobile)) {
                        if (userdata.getPassword().equals(pass)) {
                            Toast.makeText(LoginActivity.this, "Login Success!", Toast.LENGTH_SHORT).show();
                            bar.dismiss();
                            Intent intent = new Intent(LoginActivity.this, AdminActivity.class);
                            startActivity(intent);

                        }
                    }

                } else {
                    Toast.makeText(LoginActivity.this, "Login Failed! No Account Found!", Toast.LENGTH_SHORT).show();
                    bar.dismiss();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    private void AllowAccess(String mobile, String pass) {

        if (b.RememberMe.isChecked()) {
            Paper.book().write(prevalent.userPhoneKey, mobile);
            Paper.book().write(prevalent.userPasswordKey, pass);
        }
        dbeditor.putString("userPhoneKey",mobile);
        dbeditor.putString("userPasswordKey",pass);
        dbeditor.apply();

        final DatabaseReference node = FirebaseDatabase.getInstance().getReference();
        node.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.child("users").child(mobile).exists()) {

                    users userdata = snapshot.child("users").child(mobile).getValue(users.class);
                    if (userdata.getPhone().equals(mobile)) {
                        if (userdata.getPassword().equals(pass)) {

                            Toast.makeText(LoginActivity.this, "Login Success!", Toast.LENGTH_SHORT).show();
                            bar.dismiss();
                            Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                            prevalent.cOnlineUser = userdata;
                            startActivity(intent);

                        }
                    }

                } else {
                    Toast.makeText(LoginActivity.this, "Login Failed! No Account Found!", Toast.LENGTH_SHORT).show();
                    bar.dismiss();
                    Paper.book().destroy();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
    private void loading() {
        bar.setTitle("Login In");
        bar.setMessage("Please Wait!\n we are checking the credentials.");
        bar.setCanceledOnTouchOutside(false);
        bar.show();
    }
}