package com.example.tolet.Fragments;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.tolet.R;



public class PreviewAds extends Fragment {
    ImageView houseImg;
    Button call;
    TextView rentprice,des,location,mobile,categories;
    String address, category, contact, description, image, phone, price;
    public PreviewAds() {

    }
    public PreviewAds(String address, String category, String contact, String description, String image, String phone, String price) {
        this.address = address;
        this.category = category;
        this.contact = contact;
        this.description = description;
        this.image = image;
        this.phone = phone;
        this.price = price;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_preview_ads, container, false);
        houseImg=view.findViewById(R.id.hImage);
        rentprice=view.findViewById(R.id.rentPrice);
        des=view.findViewById(R.id.facilities);
        location=view.findViewById(R.id.houseLocation);
        mobile=view.findViewById(R.id.contactInfo);
        call=view.findViewById(R.id.call);
        categories=view.findViewById(R.id.Housecategory);

        rentprice.setText(price);
        des.setText(description);
        location.setText(address);
        mobile.setText(phone);
        categories.setText(category);
        Glide.with(getContext()).load(image).into(houseImg);

        call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

             makeCall();
            }
        });

        return view ;
    }

    private void makeCall() {
        if (phone.isEmpty()){
            Toast.makeText(getContext(), "No Number Found!", Toast.LENGTH_SHORT).show();
        }else{
            if (ContextCompat.checkSelfPermission(getContext(),
                    Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions((Activity) getContext(),
                        new String[]{Manifest.permission.CALL_PHONE},1);
            }else{
                String n="tel:"+phone;
                Intent intent=new Intent(Intent.ACTION_CALL);
                intent.setData(Uri.parse(n));
                startActivity(intent);
            }

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode==1){
            if (grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED){
                makeCall();
            }
            else {
                Toast.makeText(getContext(), "Permission deined!", Toast.LENGTH_SHORT).show();
            }
        }
    }
}