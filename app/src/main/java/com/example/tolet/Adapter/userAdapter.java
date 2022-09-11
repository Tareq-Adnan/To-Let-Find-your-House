package com.example.tolet.Adapter;

import android.content.DialogInterface;
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
import com.example.tolet.Model.users;
import com.example.tolet.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class userAdapter extends FirebaseRecyclerAdapter<users, userAdapter.userList> {
    AlertDialog.Builder dialog;
    FirebaseDatabase db;
    DatabaseReference node;

    public userAdapter(@NonNull FirebaseRecyclerOptions<users> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull userList holder, int position, @NonNull users model) {
        db = FirebaseDatabase.getInstance();
        node = db.getReference();
        holder.userName.setText(model.getName());
        holder.userLocation.setText(model.getAddress());
        Glide.with(holder.userImage.getContext()).load(model.getImage()).into(holder.userImage);
        holder.layout.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {

                dialog = new AlertDialog.Builder(view.getContext());
                dialog.setMessage("Do you want to delete this user?");
                dialog.setTitle("Delete User");
                dialog.create();
                dialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        node.child("users").child(model.getPhone()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                Toast.makeText(view.getContext(), "User Deleted!", Toast.LENGTH_SHORT).show();
                            }
                        });
                        dialogInterface.dismiss();
                    }
                });
                dialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
                dialog.show();
                return true;
            }

        });


    }

    @NonNull
    @Override
    public userList onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_design, parent, false);
        return new userList(view);
    }

    public class userList extends RecyclerView.ViewHolder {

        ImageView userImage;
        TextView userName, userLocation;
        RelativeLayout layout;

        public userList(@NonNull View itemView) {
            super(itemView);
            layout = itemView.findViewById(R.id.userss);
            userImage = itemView.findViewById(R.id.userimage);
            userName = itemView.findViewById(R.id.UserName);
            userLocation = itemView.findViewById(R.id.UserLocation);

        }
    }
}
