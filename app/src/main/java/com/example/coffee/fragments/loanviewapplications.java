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
import com.example.coffee.adapters.loanApplications;
import com.example.coffee.adapters.loandbhelper;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class loanviewapplications extends Fragment {
    private RecyclerView recyclerView;
    private loanApplications loanadapt;
    private List<loandbhelper> applications;
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_loanviewapplications, container, false);

        recyclerView=view.findViewById(R.id.loanrecycler);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager=new LinearLayoutManager(getContext());
        layoutManager.setStackFromEnd(true);
        layoutManager.setReverseLayout(true);
        recyclerView.setLayoutManager(layoutManager);

        applications=new ArrayList<>();
        viewApplications();
        return view;
    }

    private void viewApplications() {
        final FirebaseUser firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference reference= FirebaseDatabase.getInstance().getReference("Loan").child("Applications");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                applications.clear();
                for (DataSnapshot dataSnapshot:snapshot.getChildren()){
                    for (DataSnapshot snapshot1:dataSnapshot.getChildren()){
                        loandbhelper loans=snapshot1.getValue(loandbhelper.class);
                        assert loans != null;
                        assert firebaseUser != null;
                        if (!loans.getMail().isEmpty()){
                            applications.add(loans);
                        }
                    }
                }
                loanadapt=new loanApplications(getContext(),applications);
                recyclerView.setAdapter(loanadapt);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}
