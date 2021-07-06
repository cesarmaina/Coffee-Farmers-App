package com.example.coffee;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.coffee.adapters.RecordsAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class adminviewuserrecord extends AppCompatActivity {
    private RecyclerView mrecyclerView;
    DatabaseReference reference;
    FirebaseUser user;
    ImageView profpic;
    TextView name,totalg1,totalg2,totalg3,Gtotal,revenue;
    ImageButton backs;
    private List<getrecords> records;
    private RecordsAdapter recordsAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adminviewuserrecord);
        Bundle bundle= getIntent().getExtras();
        assert bundle!=null;
        String userid=bundle.getString("userid");
        profpic=findViewById(R.id.pic);
        name=findViewById(R.id.name);

        backs=findViewById(R.id.back);
        backs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),AdminSaveDash.class));
            }
        });
        totalg1=findViewById(R.id.grade1);
        totalg2=findViewById(R.id.grade2);
        totalg3=findViewById(R.id.grade3);
        Gtotal=findViewById(R.id.ttotal);
        revenue=findViewById(R.id.income1);
        reference= FirebaseDatabase.getInstance().getReference("Users").child(userid);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                registerClass account=snapshot.getValue(registerClass.class);

                name.setText(account.getName());

                if (account.getImage().equals("Default")){
                    profpic.setImageResource(R.drawable.account1);
                }
                else {
                    Glide.with(getApplicationContext()).load(account.getImage()).into(profpic);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        mrecyclerView=(RecyclerView)findViewById(R.id.recycleview);
        mrecyclerView.setHasFixedSize(true);
        mrecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        records=new ArrayList<>();
        viewMyRecords();
    }

    private void viewMyRecords() {
        Bundle bundle= getIntent().getExtras();
        assert bundle!=null;
        String userid=bundle.getString("userid");
        DatabaseReference reference= FirebaseDatabase.getInstance().getReference("Records").child(userid);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                records.clear();
                Integer total1=0;
                Integer total2=0;
                Integer total3=0;

                for (DataSnapshot dataSnapshot:snapshot.getChildren()){
                    //for (DataSnapshot snap : dataSnapshot.getChildren()){
                    getrecords rec=dataSnapshot.getValue(getrecords.class);
                    records.add(rec);
                    Integer total=Integer.valueOf(rec.getGrade1());
                    Integer totalgr2=Integer.valueOf(rec.getGrade2());
                    Integer totalgr3=Integer.valueOf(rec.getMbuni());

                    total1=total1+total;
                    total2=total2+totalgr2;
                    total3=total3+totalgr3;

                }
                recordsAdapter=new RecordsAdapter(getApplicationContext(),records);
                mrecyclerView.setAdapter(recordsAdapter);
                totalg1.setText(String.valueOf(total1)+" Kgs");
                totalg2.setText(String.valueOf(total2)+" Kgs");
                totalg3.setText(String.valueOf(total3)+" Kgs");
                Integer costperkg=80;
                Integer grtotal=Integer.valueOf(total1)+Integer.valueOf(total2)+Integer.valueOf(total3);
                Gtotal.setText("Grand Total = "+ String.valueOf(grtotal)+" Kgs");
                revenue.setText("Ksh "+Integer.valueOf(grtotal)*costperkg);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }
}
