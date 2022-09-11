package com.example.tolet.Adapter;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.tolet.Fragments.PreviewAds;
import com.example.tolet.Model.ads;
import com.example.tolet.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

public class Adapter extends FirebaseRecyclerAdapter<ads,Adapter.viewHolder> {


    public Adapter(@NonNull FirebaseRecyclerOptions<ads> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull viewHolder holder, int position, @NonNull ads model) {

        holder.prize.setText(model.getPrice());
        holder.location.setText(model.getAddress());
        Glide.with(holder.image.getContext()).load(model.getImage()).into(holder.image);

        holder.image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppCompatActivity activity=(AppCompatActivity)view.getContext();
                activity.getSupportFragmentManager().beginTransaction().replace(R.id.replace,new PreviewAds(model.getAddress(),model.getCategory(),model.getContact(),model.getDescription(),model.getImage(),model.getPhone(),model.getPrice())).addToBackStack(null).commit();

            }
        });

    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.adview,parent,false);

        return new viewHolder(view);
    }

    public class viewHolder extends RecyclerView.ViewHolder {

        ImageView image;
        TextView prize,location,category;

        public viewHolder(@NonNull View itemView) {
            super(itemView);

            image=itemView.findViewById(R.id.Rimage);
            prize=itemView.findViewById(R.id.Rprice);
            location=itemView.findViewById(R.id.Raddress);
            category=itemView.findViewById(R.id.Housecategory);

        }

    }

}
