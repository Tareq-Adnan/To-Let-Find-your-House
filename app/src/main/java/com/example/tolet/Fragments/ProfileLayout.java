package com.example.tolet.Fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.tolet.CreatePost;
import com.example.tolet.HomeActivity;
import com.example.tolet.LoginActivity;
import com.example.tolet.Model.users;
import com.example.tolet.Prevelent.prevalent;
import com.example.tolet.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import io.paperdb.Paper;

public class ProfileLayout extends Fragment {
    TextView name,dob,add,mobile;
    ImageView pic;
    Button logOut;
    SharedPreferences localdb;
    SharedPreferences.Editor dbeditor;


    public ProfileLayout() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_profile_layout, container, false);
        localdb=getContext().getSharedPreferences("userDetails",Context.MODE_PRIVATE);
        dbeditor=localdb.edit();

        name=view.findViewById(R.id.Name);
        dob=view.findViewById(R.id.dateofBirt);
        add=view.findViewById(R.id.houseAddress);
        mobile=view.findViewById(R.id.MobileNumber);
        pic=view.findViewById(R.id.profilePic);
        logOut=view.findViewById(R.id.logout);
        Paper.init(view.getContext());


        name.setText(prevalent.cOnlineUser.getName());
        dob.setText(prevalent.cOnlineUser.getDateOfbirth());
        add.setText(prevalent.cOnlineUser.getAddress());
        mobile.setText(prevalent.cOnlineUser.getPhone());
        Glide.with(view.findViewById(R.id.profilePic).getContext()).load(prevalent.cOnlineUser.getImage()).into(pic);
        logOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Paper.book().destroy();
                dbeditor.clear();
                Intent intent=new Intent(view.getContext(), LoginActivity.class);
                startActivity(intent);
            }
        });


        return view ;


    }
}