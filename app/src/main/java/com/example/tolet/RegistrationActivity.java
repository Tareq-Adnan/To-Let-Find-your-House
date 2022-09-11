package com.example.tolet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.tolet.databinding.ActivityRegistrationBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
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
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Random;

public class RegistrationActivity extends AppCompatActivity {

    ActivityRegistrationBinding b;
    ProgressDialog bar;
    Uri filepath;
    Bitmap bitmap;
    FirebaseDatabase db;
    DatabaseReference node;
    int year,month,day;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        b=ActivityRegistrationBinding.inflate(getLayoutInflater());
        setContentView(b.getRoot());

        db=FirebaseDatabase.getInstance();
        node=db.getReference();
        FirebaseStorage storage=FirebaseStorage.getInstance();
        StorageReference uploader=storage.getReference("image"+new Random().nextInt(50));
        Calendar calendar=Calendar.getInstance();

        bar=new ProgressDialog(this);
        b.loginText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(RegistrationActivity.this,LoginActivity.class);
                startActivity(intent);
            }
        });

        b.image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dexter.withContext(getApplicationContext())
                        .withPermission(Manifest.permission.READ_EXTERNAL_STORAGE).withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                        Intent intent=new Intent(Intent.ACTION_PICK);
                        intent.setType("image/*");
                        startActivityForResult(Intent.createChooser(intent,"Please select an image"),1);
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
        b.dateofbirth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            year=calendar.get(Calendar.YEAR);
            month=calendar.get(Calendar.MONTH);
            day=calendar.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePicker=new DatePickerDialog(RegistrationActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                    b.dateofbirth.setText(i2+"."+i1+"."+i);
                    }
                },year,month,day);
                datePicker.show();
            }
        });
        b.submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                CreateAccount(uploader);
            }
        });



    }

    private void CreateAccount(StorageReference uploader) {
        String name,email,mobile,password,dateOfbirth,confirmPassword,address;
        name=b.name.getText().toString();
        email=b.email.getText().toString();
        dateOfbirth=b.dateofbirth.getText().toString();
        password=b.password.getText().toString();
        mobile=b.mobile.getText().toString();
        confirmPassword=b.confirmPassword.getText().toString();
        address=b.Address.getText().toString();

        if (TextUtils.isEmpty(name)){
            Toast.makeText(this, "You Must Enter your Name", Toast.LENGTH_SHORT).show();
        }
        if (filepath == null) {
            Toast.makeText(this, "Please Select an Image!", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(dateOfbirth)){
            Toast.makeText(this, "You Must Enter your Username", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(email)){
            Toast.makeText(this, "You Must Enter your Email", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(password)){
            Toast.makeText(this, "You Must Enter your password", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(b.confirmPassword.getText().toString())){
            Toast.makeText(this, "You Must Enter your confirm password", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(mobile)){
            Toast.makeText(this, "You Must Enter your mobile number", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(address)){
            Toast.makeText(this, "You Must Enter your mobile number", Toast.LENGTH_SHORT).show();
        }
        else{
           loading();
            uploader.putFile(filepath).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    uploader.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {

                            node.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {

                                    if (!snapshot.child("users").child(mobile).exists()){
                                        HashMap<String,Object>  userdata=new HashMap<>();
                                        userdata.put("phone",mobile);
                                        userdata.put("name",name);
                                        userdata.put("dateOfbirth",dateOfbirth);
                                        userdata.put("password",password);
                                        userdata.put("email",email);
                                        userdata.put("image",uri.toString());
                                        userdata.put("address",address);

                                        node.child("users").child(mobile).setValue(userdata).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()){
                                                    Toast.makeText(RegistrationActivity.this, "Congratulations!", Toast.LENGTH_SHORT).show();
                                                    bar.dismiss();
                                                    Intent intent=new Intent(RegistrationActivity.this,LoginActivity.class);
                                                    startActivity(intent);
                                                }
                                                else{
                                                    bar.dismiss();
                                                    Toast.makeText(RegistrationActivity.this, "Reg Failed!, Try again later.", Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });

                                    }
                                    else{
                                        Toast.makeText(RegistrationActivity.this, "already registered!", Toast.LENGTH_SHORT).show();
                                        bar.dismiss();
                                        Intent intent=new Intent(RegistrationActivity.this,LoginActivity.class);
                                        startActivity(intent);
                                        finish();
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });

                        }
                    });

                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {

                }
            });


        }

        }



    private void loading() {
        bar.setTitle("Creating Account");
        bar.setMessage("Please Wait!\n we are checking the credentials.");
        bar.setCanceledOnTouchOutside(false);
        bar.show();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        if (requestCode==1 && resultCode==RESULT_OK){
            filepath=data.getData();
            try {
                InputStream is=getContentResolver().openInputStream(filepath);
                bitmap= BitmapFactory.decodeStream(is);
                b.image.setImageBitmap(bitmap);

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

        }

        super.onActivityResult(requestCode, resultCode, data);
    }
}