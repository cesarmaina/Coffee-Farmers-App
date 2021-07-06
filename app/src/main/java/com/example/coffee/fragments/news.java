package com.example.coffee.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.coffee.R;
import com.example.coffee.newsAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class news extends Fragment {


    private RecyclerView recyclerView;
    private com.example.coffee.adapters.newsAdapter newsadapter;
    private List<newsAdapter> mnews;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view=inflater.inflate(R.layout.fragment_news, container, false);
        recyclerView=view.findViewById(R.id.recycler_news);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager=new LinearLayoutManager(getContext());
        layoutManager.setStackFromEnd(true);
        layoutManager.setReverseLayout(true);
        recyclerView.setLayoutManager(layoutManager);
        mnews=new ArrayList<>();
        readNews();

        return view;

    }
    private void readNews(){
        DatabaseReference reference= FirebaseDatabase.getInstance().getReference("News");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                mnews.clear();
                for (DataSnapshot dataSnapshot:snapshot.getChildren()){
                    newsAdapter user=dataSnapshot.getValue(newsAdapter.class);
                    assert user != null;
                    mnews.add(user);
                }
                newsadapter=new com.example.coffee.adapters.newsAdapter(getContext(),mnews);
                recyclerView.setAdapter(newsadapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}
