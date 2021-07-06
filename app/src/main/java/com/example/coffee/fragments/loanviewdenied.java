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

import com.example.coffee.R;
import com.example.coffee.adapters.LoanApprovedadapter;
import com.example.coffee.adapters.LoanDeniedAdapter;
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

public class loanviewdenied extends Fragment {
    private RecyclerView recyclerView;
    private LoanDeniedAdapter loanadapt;
    private List<loandbhelper> denied;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_loanviewdenied, container, false);
        recyclerView=view.findViewById(R.id.loanrecycle);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        denied=new ArrayList<>();
        viewApproved();
        return view;
    }
    private void viewApproved() {
        final FirebaseUser firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference reference= FirebaseDatabase.getInstance().getReference("Loan").child("Processed");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                denied.clear();
                for (DataSnapshot dataSnapshot:snapshot.getChildren()){
                    for (DataSnapshot snap : dataSnapshot.getChildren()){
                        loandbhelper loans=snap.getValue(loandbhelper.class);
                        String status=loans.getStatus();
                        assert loans != null;
                        //assert firebaseUser != null;
                        if (!loans.getMail().isEmpty()){
                            if (status.equals("Denied")){
                                denied.add(loans);
                            }
                        }
                    }

                }
                loanadapt=new LoanDeniedAdapter(getContext(),denied);
                recyclerView.setAdapter(loanadapt);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


}