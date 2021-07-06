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
import com.example.coffee.adapters.sellhelper;
import com.example.coffee.adapters.buyadapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ChemicalsFrag extends Fragment {
    RecyclerView recyclerView;
    private List<sellhelper> items;
    buyadapter sell;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_products, container, false);
        recyclerView=view.findViewById(R.id.recyitems);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager=new LinearLayoutManager(getContext());
        layoutManager.setStackFromEnd(true);
        layoutManager.setReverseLayout(true);
        recyclerView.setLayoutManager(layoutManager);
        items=new ArrayList<>();
        viewItems();
        return view;
    }

    private void viewItems() {
        DatabaseReference reference= FirebaseDatabase.getInstance().getReference("Product");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                items.clear();
                for (DataSnapshot dataSnapshot:snapshot.getChildren()){
                    sellhelper item=dataSnapshot.getValue(sellhelper.class);
                    assert item != null;
                    if (item.getCategory().equals("Chemicals")){
                        if (!item.getCount().equals("0")){
                            items.add(item);
                        }

                    }
                }
                sell=new buyadapter(getContext(),items);
                recyclerView.setAdapter(sell);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
