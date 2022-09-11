package com.example.tolet.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.tolet.Adapter.pendingAdapter;
import com.example.tolet.Adapter.userAdapter;
import com.example.tolet.Model.pendinglist;
import com.example.tolet.Model.users;
import com.example.tolet.R;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.FirebaseDatabase;

public class PendingAds extends Fragment {
    RecyclerView recyclerView;
    pendingAdapter adapter;


    public PendingAds() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_peding_ads, container, false);
        recyclerView = view.findViewById(R.id.pendingList);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        FirebaseRecyclerOptions<pendinglist> list = new FirebaseRecyclerOptions.Builder<pendinglist>().setQuery(FirebaseDatabase.getInstance().getReference().child("pendingPost"), pendinglist.class).build();
        adapter = new pendingAdapter(list);
        recyclerView.setAdapter(adapter);


        return view;
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