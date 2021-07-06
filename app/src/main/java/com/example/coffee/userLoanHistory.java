package com.example.coffee;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;

import com.example.coffee.adapters.loanapprovedhist;
import com.example.coffee.adapters.loandbhelper;
import com.example.coffee.adapters.loanhistory;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class userLoanHistory extends AppCompatActivity {
    private RecyclerView recycler,recyclerView;
    private loanhistory loanss;
    private loanapprovedhist loan;
    private List<loandbhelper> applications,hist;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loanhistory);
        recycler=findViewById(R.id.loanhistoryrecy);
        recyclerView=findViewById(R.id.approverecyc);
        recycler.setHasFixedSize(true);
        recyclerView.setHasFixedSize(true);

        LinearLayoutManager layoutManager=new LinearLayoutManager(getApplicationContext());
        layoutManager.setStackFromEnd(true);
        layoutManager.setReverseLayout(true);

        recycler.setLayoutManager(layoutManager);
        LinearLayoutManager layoutManager1=new LinearLayoutManager(getApplicationContext());
        layoutManager1.setStackFromEnd(true);
        layoutManager1.setReverseLayout(true);
        recyclerView.setLayoutManager(layoutManager1);


        applications=new ArrayList<>();
        hist=new ArrayList<>();
        viewHistory();
    }

    private void viewHistory() {
        //active applications
        final FirebaseUser firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference reference= FirebaseDatabase.getInstance().getReference("Loan").child("Applications").child(firebaseUser.getUid());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                hist.clear();
                for (DataSnapshot dataSnapshot:snapshot.getChildren()){
                    loandbhelper loa=dataSnapshot.getValue(loandbhelper.class);
                    assert loa != null;
                    assert firebaseUser != null;
                    if (!loa.getMail().isEmpty()){
                        hist.add(loa);
                    }
                }
                loanss=new loanhistory(getApplication(),hist);
                recycler.setAdapter(loanss);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        //approved applications
        final FirebaseUser firebaseUser1= FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference reference1= FirebaseDatabase.getInstance().getReference("Loan").child("Processed").child(firebaseUser1.getUid());
        reference1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                applications.clear();
                for (DataSnapshot dataSnapshot:snapshot.getChildren()){
                    loandbhelper loa=dataSnapshot.getValue(loandbhelper.class);
                    assert loa != null;
                    assert firebaseUser1 != null;
                    if (!loa.getMail().isEmpty()){
                        applications.add(loa);
                    }
                }
                loan=new loanapprovedhist(getApplication(),applications);
                recyclerView.setAdapter(loan);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(getApplicationContext(),loan.class));
    }
}
