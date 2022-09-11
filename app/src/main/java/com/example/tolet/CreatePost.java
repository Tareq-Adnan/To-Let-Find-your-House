package com.example.tolet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.example.tolet.Model.users;
import com.example.tolet.Prevelent.prevalent;
import com.example.tolet.databinding.ActivityCreatePostBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Objects;
import java.util.Random;

import io.paperdb.Paper;

public class CreatePost extends AppCompatActivity {

    ActivityCreatePostBinding b;
    String category;
    Uri imagePath;
    Bitmap bitmap;
    FirebaseDatabase db;
    DatabaseReference node;
    SharedPreferences localdb;
    SharedPreferences.Editor dbeditor;
    ProgressDialog bar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        b = ActivityCreatePostBinding.inflate(getLayoutInflater());
        setContentView(b.getRoot());

        Paper.init(this);
        db = FirebaseDatabase.getInstance();
        node = db.getReference();

        bar=new ProgressDialog(this);

        localdb = getSharedPreferences("userDetails", Context.MODE_PRIVATE);
        dbeditor = localdb.edit();

        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference uploader = storage.getReference("post" + new Random().nextInt(500));

        b.building.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                b.building.setImageResource(R.drawable.select30);
                b.flat.setImageResource(R.drawable.deselect31);
                b.seat.setImageResource(R.drawable.deselect32);
                b.sublet.setImageResource(R.drawable.deselect33);
                category = "Building";
                Toast.makeText(CreatePost.this, category, Toast.LENGTH_SHORT).show();
            }
        });
        b.flat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                b.building.setImageResource(R.drawable.deselect30);
                b.flat.setImageResource(R.drawable.select31);
                b.seat.setImageResource(R.drawable.deselect32);
                b.sublet.setImageResource(R.drawable.deselect33);
                category = "Flat";
                Toast.makeText(CreatePost.this, category, Toast.LENGTH_SHORT).show();
            }
        });
        b.seat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                b.building.setImageResource(R.drawable.deselect30);
                b.flat.setImageResource(R.drawable.deselect31);
                b.seat.setImageResource(R.drawable.select32);
                b.sublet.setImageResource(R.drawable.deselect33);
                category = "Seat";
                Toast.makeText(CreatePost.this, category, Toast.LENGTH_SHORT).show();
            }
        });
        b.sublet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                b.building.setImageResource(R.drawable.deselect30);
                b.flat.setImageResource(R.drawable.deselect31);
                b.seat.setImageResource(R.drawable.deselect32);
                b.sublet.setImageResource(R.drawable.select33);
                category = "Sublet";
                Toast.makeText(CreatePost.this, category, Toast.LENGTH_SHORT).show();
            }
        });
        b.houseImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dexter.withContext(getApplicationContext()).withPermission(Manifest.permission.READ_EXTERNAL_STORAGE).withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                        Intent intent = new Intent(Intent.ACTION_PICK);
                        intent.setType("image/*");
                        startActivityForResult(Intent.createChooser(intent, "Choose photos"), 1);

                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {

                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                        permissionToken.continuePermissionRequest();
                    }
                }).check();

            }
        });

        b.submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loading();
                createPost(uploader);
            }
        });


    }

    private void createPost(StorageReference uploader) {

        String description, price, address, contact, mobile;
        description = b.description.getText().toString();
        price = b.rentPrice.getText().toString();
        address = b.houseLocation.getText().toString();
        contact = b.contactInfo.getText().toString();
        mobile = localdb.getString("userPhoneKey", "");


        if (TextUtils.isEmpty(description)) {
            Toast.makeText(this, "Description is Mandatory!", Toast.LENGTH_SHORT).show();
        }
        if (imagePath == null) {
            Toast.makeText(this, "Please Select an Image!", Toast.LENGTH_SHORT).show();
        }
        if (category == null || category.equals("")) {
            Toast.makeText(this, "Category Not Selected!", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(price)) {
            Toast.makeText(this, "Price is Mandatory!", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(address)) {
            Toast.makeText(this, "Address is Mandatory!", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(contact)) {
            Toast.makeText(this, "Contact is Mandatory!", Toast.LENGTH_SHORT).show();
        } else {
            uploader.putFile(imagePath).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    uploader.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            node.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {

                                    HashMap<String, Object> postData = new HashMap<>();
                                    postData.put("phone", mobile);
                                    postData.put("description", description);
                                    postData.put("price", price);
                                    postData.put("address", address);
                                    postData.put("contact", contact);
                                    postData.put("image", uri.toString());
                                    postData.put("category", category);

                                    node.child("pendingPost").child(mobile).setValue(postData).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                Toast.makeText(CreatePost.this, "Post submission success! Waiting for approval!", Toast.LENGTH_SHORT).show();
                                                bar.dismiss();
                                                Intent intent = new Intent(CreatePost.this, HomeActivity.class);
                                                startActivity(intent);
                                            } else {
                                               bar.dismiss();
                                                Toast.makeText(CreatePost.this, "Post Creation Failed!, Try again later.", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });

                                    bar.dismiss();
                                    Intent intent = new Intent(CreatePost.this, HomeActivity.class);
                                    startActivity(intent);
                                    finish();

                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });
                        }
                    });
                }
            });


        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == 1 && resultCode == RESULT_OK) {
            imagePath = data.getData();
            try {
                InputStream inputStream = getContentResolver().openInputStream(imagePath);
                bitmap = BitmapFactory.decodeStream(inputStream);
                b.houseImage.setImageBitmap(bitmap);
            } catch (FileNotFoundException e) {

            }
        } else {
            Toast.makeText(this, "Please Select an Image!", Toast.LENGTH_SHORT).show();
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
        startActivity(intent);
        super.onBackPressed();

    }
    private void loading() {
        bar.setTitle("Posting Ads");
        bar.setMessage("Please Wait!");
        bar.setCanceledOnTouchOutside(false);
        bar.show();
    }
}