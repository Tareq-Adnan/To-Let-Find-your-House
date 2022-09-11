package com.example.tolet.Fragments;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.SearchView;

import com.example.tolet.Adapter.Adapter;
import com.example.tolet.AdminActivity;
import com.example.tolet.Model.ads;
import com.example.tolet.R;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.FirebaseDatabase;

public class RecentAds extends Fragment {
    RecyclerView recyclerView;
    SearchView search;
    Adapter adapter;

    public RecentAds() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_recent_ads, container, false);
        recyclerView=view.findViewById(R.id.recyclerView);
        search=view.findViewById(R.id.search);

            search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String s) {
                    preporosess(s);
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String s) {
                    return false;
                }
            });

        recyclerView.setLayoutManager(new GridLayoutManager(getContext(),2));
        FirebaseRecyclerOptions<ads> options =
                new FirebaseRecyclerOptions.Builder<ads>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("recentAds"), ads.class)
                        .build();

        adapter=new Adapter(options);
        recyclerView.setAdapter(adapter);
        return view;
    }

    private void preporosess(String s) {

        FirebaseRecyclerOptions<ads> options =
                new FirebaseRecyclerOptions.Builder<ads>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("recentAds").orderByChild("address").startAt(s).endAt(s+"\uf8ff"), ads.class)
                        .build();

        adapter=new Adapter(options);
        adapter.startListening();
        recyclerView.setAdapter(adapter);

    }

    @Override
    public void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();
    }
}