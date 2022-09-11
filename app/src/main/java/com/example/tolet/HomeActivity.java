package com.example.tolet;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.tolet.Fragments.ProfileLayout;
import com.example.tolet.Fragments.RecentAds;
import com.example.tolet.databinding.ActivityHomeBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import io.paperdb.Paper;

public class HomeActivity extends AppCompatActivity {

    ActivityHomeBinding b;
    AlertDialog.Builder dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        b = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(b.getRoot());

        RecentAds ads=new RecentAds();
        FragmentTransaction transaction=getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.replace,ads);
        transaction.commit();

        b.profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ProfileLayout profileLayout=new ProfileLayout();
                FragmentTransaction transaction=getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.replace,profileLayout);
                transaction.commit();
            }
        });
        b.ads.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RecentAds ads=new RecentAds();
                FragmentTransaction transaction=getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.replace,ads);
                transaction.commit();
            }
        });
        b.createAd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(HomeActivity.this,CreatePost.class);
                startActivity(intent);
            }
        });
    }

}