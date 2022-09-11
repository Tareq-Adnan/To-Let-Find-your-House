package com.example.tolet.Adapter;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.tolet.CreatePost;
import com.example.tolet.HomeActivity;
import com.example.tolet.Model.pendinglist;
import com.example.tolet.Model.users;
import com.example.tolet.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class pendingAdapter extends FirebaseRecyclerAdapter<pendinglist, pendingAdapter.pendingList> {
    AlertDialog dialog;
    TextView approve, reject;
    FirebaseDatabase db;
    DatabaseReference node;


    public pendingAdapter(@NonNull FirebaseRecyclerOptions<pendinglist> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull pendingList holder, int position, @NonNull pendinglist model) {

        db=FirebaseDatabase.getInstance();
        node=db.getReference();
        holder.userName.setText(model.getCategory());
        holder.userLocation.setText(model.getAddress());
        holder.prize.setText(model.getPrice());
        Glide.with(holder.userImage.getContext()).load(model.getImage()).into(holder.userImage);
        holder.r.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {

                View v = LayoutInflater.from(view.getContext()).inflate(R.layout.ap_rj, null, false);
                dialog = new AlertDialog.Builder(view.getContext()).setView(v).create();
                dialog.setCancelable(true);
                dialog.show();
                approve = v.findViewById(R.id.approve);
                reject = v.findViewById(R.id.reject);


                approve.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        node.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {

                                HashMap<String, Object> postData = new HashMap<>();
                                postData.put("phone", model.getPhone());
                                postData.put("description", model.getDescription());
                                postData.put("price", model.getPrice());
                                postData.put("address", model.getAddress());
                                postData.put("contact", model.getContact());
                                postData.put("image", model.getImage());
                                postData.put("category", model.getCategory());

                                node.child("recentAds").child(model.getPhone()).setValue(postData).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            Toast.makeText(view.getContext(), "Approved", Toast.LENGTH_SHORT).show();
                                            node.child("pendingPost").child(model.getPhone()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    Toast.makeText(view.getContext(), "Pending Post cleared!", Toast.LENGTH_SHORT).show();
                                                    dialog.dismiss();

                                                }
                                            });

                                        } else {
                                            Toast.makeText(view.getContext(), "Something went wrong!", Toast.LENGTH_SHORT).show();
                                            dialog.dismiss();
                                        }
                                    }
                                });
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });


                        Toast.makeText(v.getContext(), "Approved", Toast.LENGTH_SHORT).show();
                    }
                });
                reject.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        node.child("pendingPost").child(model.getPhone()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                Toast.makeText(view.getContext(), "Pending Post cleared!", Toast.LENGTH_SHORT).show();
                                dialog.dismiss();
                            }
                        });
                        Toast.makeText(v.getContext(), "Rejected", Toast.LENGTH_SHORT).show();
                    }
                });



                return true;
            }
        });


    }

    @NonNull
    @Override
    public pendingList onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_design2, parent, false);
        return new pendingList(view);
    }

    public class pendingList extends RecyclerView.ViewHolder {

        ImageView userImage;
        TextView userName, userLocation, prize;
        RelativeLayout r;

        public pendingList(@NonNull View itemView) {
            super(itemView);
            r = itemView.findViewById(R.id.layout);
            userImage = itemView.findViewById(R.id.userimage);
            userName = itemView.findViewById(R.id.UserName);
            userLocation = itemView.findViewById(R.id.UserLocation);
            prize = itemView.findViewById(R.id.rentCost);

        }
    }
}
