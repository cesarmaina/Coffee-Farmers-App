package com.example.coffee.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.coffee.R;
import com.example.coffee.adapters.viewtotalrecordsadapter;
import com.example.coffee.getrecords;
import com.example.coffee.registerClass;
import com.google.android.gms.common.util.Strings;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class TotalRecords extends Fragment {
    DatabaseReference reference;
    private RecyclerView recyclerView;
    private viewtotalrecordsadapter recordsadapter;
    private List <registerClass>farmers;
    private List <Strings>totals;
    TextView gr1,gr2,gr3,tr,er;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_totalrecords, container, false);
        gr1=view.findViewById(R.id.gg11);
        gr2=view.findViewById(R.id.gg22);
        gr3=view.findViewById(R.id.gg33);
        er=view.findViewById(R.id.er11);
        tr=view.findViewById(R.id.tr11);
        DatabaseReference reference= FirebaseDatabase.getInstance().getReference("Records");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Integer total1=0;
                Integer total2=0;
                Integer total3=0;

                for (DataSnapshot dataSnapshot:snapshot.getChildren()){
                    for (DataSnapshot snapshot1:dataSnapshot.getChildren()){
                        getrecords rec=snapshot1.getValue(getrecords.class);
                        Integer total=Integer.valueOf(rec.getGrade1());
                        Integer totalgr2=Integer.valueOf(rec.getGrade2());
                        Integer totalgr3=Integer.valueOf(rec.getMbuni());

                        total1=total1+total;
                        total2=total2+totalgr2;
                        total3=total3+totalgr3;
                    }
                    gr1.setText(String.valueOf(total1)+" Kgs");
                    gr2.setText(String.valueOf(total2)+" Kgs");
                    gr3.setText(String.valueOf(total3)+" Kgs");
                    tr.setText(String.valueOf(total1+total2+total3)+" Kgs");
                    er.setText("Ksh "+String.valueOf(80*(total1+total2+total3)));
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
        recyclerView=view.findViewById(R.id.totalrecordsrecycler);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        farmers=new ArrayList<>();

        viewRecords();
        return view;
    }

    private void viewRecords() {
        final DatabaseReference reference=FirebaseDatabase.getInstance().getReference("Records");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                farmers.clear();
                for (final DataSnapshot dataSnapshot:snapshot.getChildren()){
                    //Toast.makeText(getContext(),dataSnapshot.getKey(),Toast.LENGTH_LONG).show();
                    DatabaseReference reference1=FirebaseDatabase.getInstance().getReference("Users").child(dataSnapshot.getKey());
                    reference1.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot11) {
                            registerClass users=snapshot11.getValue(registerClass.class);
                            farmers.add(users);
                            recordsadapter=new viewtotalrecordsadapter(getContext(),farmers);
                            recyclerView.setAdapter(recordsadapter);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
