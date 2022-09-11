package com.example.tolet.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.tolet.Adapter.userAdapter;
import com.example.tolet.Model.users;
import com.example.tolet.R;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.FirebaseDatabase;


public class UserList extends Fragment {
    RecyclerView recyclerView;
    userAdapter adapter;

    public UserList() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_list, container, false);
        recyclerView = view.findViewById(R.id.userList);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        FirebaseRecyclerOptions<users> list = new FirebaseRecyclerOptions.Builder<users>().setQuery(FirebaseDatabase.getInstance().getReference().child("users"), users.class).build();
        adapter = new userAdapter(list);
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