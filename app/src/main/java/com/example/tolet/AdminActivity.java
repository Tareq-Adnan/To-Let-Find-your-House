package com.example.tolet;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.tolet.Fragments.PendingAds;
import com.example.tolet.Fragments.RecentAds;
import com.example.tolet.Fragments.UserList;
import com.example.tolet.databinding.ActivityAdminBinding;

public class AdminActivity extends AppCompatActivity {

    ActivityAdminBinding b;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        b=ActivityAdminBinding.inflate(getLayoutInflater());
        setContentView(b.getRoot());

        b.recentAd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RecentAds ads=new RecentAds();
                FragmentTransaction transaction=getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.view,ads);
                transaction.commit();
            }
        });
        b.recent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RecentAds ads=new RecentAds();
                FragmentTransaction transaction=getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.view,ads);
                transaction.commit();
            }
        });
        b.user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UserList user=new UserList();
                FragmentTransaction transaction=getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.view,user);
                transaction.commit();
            }
        });
        b.users.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UserList user=new UserList();
                FragmentTransaction transaction=getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.view,user);
                transaction.commit();
            }
        });
        b.pendings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PendingAds ads=new PendingAds();
                FragmentTransaction transaction=getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.view,ads);
                transaction.commit();
            }
        });
        b.pendinglist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PendingAds ads=new PendingAds();
                FragmentTransaction transaction=getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.view,ads);
                transaction.commit();
            }
        });
        b.logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getApplicationContext(),LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }


}