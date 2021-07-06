package com.example.coffee.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.coffee.Lipa;
import com.example.coffee.R;
import com.example.coffee.adapters.viewsalesadapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class viewsales extends Fragment {
    private RecyclerView recyclerView;
    private List<Lipa> goods;
    viewsalesadapter sales;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_viewsales, container, false);
        recyclerView=view.findViewById(R.id.salesrecycler);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager=new LinearLayoutManager(getContext());
        layoutManager.setStackFromEnd(true);
        layoutManager.setReverseLayout(true);
        recyclerView.setLayoutManager(layoutManager);

        goods=new ArrayList<>();
        viewSales();
        return view;
    }

    private void viewSales() {
        DatabaseReference reference= FirebaseDatabase.getInstance().getReference("Transaction");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                goods.clear();
                for (DataSnapshot dataSnapshot:snapshot.getChildren()){
                    Lipa transactions=dataSnapshot.getValue(Lipa.class);
                    assert transactions!=null;
                    goods.add(transactions);
                }
                sales=new viewsalesadapter(getContext(),goods);
                recyclerView.setAdapter(sales);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
