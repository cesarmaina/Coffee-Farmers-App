package com.example.coffee;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.coffee.adapters.loandbhelper;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import static android.graphics.Color.BLUE;
import static android.graphics.Color.RED;

public class repayment extends AppCompatActivity {
    TextView loans,mi,er,ep,time,stats;
    Button back;
    FirebaseUser user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_repayment);
        Bundle bundle=getIntent().getExtras();
        assert bundle!=null;
        String loanid=bundle.getString("loanid");
        String stat=bundle.getString("status");
        loans=findViewById(R.id.loan11);
        stats=findViewById(R.id.status);
        mi=findViewById(R.id.loan22);
        er=findViewById(R.id.loan33);
        ep=findViewById(R.id.loan44);
        time=findViewById(R.id.loan122);
        user= FirebaseAuth.getInstance().getCurrentUser();
        if (stat.equals("Denied")){
            stats.setText("Loan was Denied");
            stats.setTextColor(RED);
        }
        if (stat.equals("Approved")){
            stats.setText("Loan was Approved");
            stats.setTextColor(BLUE);
        }
        DatabaseReference ref= FirebaseDatabase.getInstance().getReference("Loan").child("Processed").child(user.getUid()).child(loanid);
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                loandbhelper loan=snapshot.getValue(loandbhelper.class);
                String am=loan.getLoan();
                Integer x=Integer.valueOf(loan.getTime())+2021;
                time.setText(loan.getTime()+"years"+ ("    ("+2021+"-"+x+")"));
                loans.setText(String.valueOf(am));
                final Integer installment=Integer.valueOf(am)/Integer.valueOf(loan.getTime());
                mi.setText(String.valueOf(installment));
                DatabaseReference reference= FirebaseDatabase.getInstance().getReference("Records").child(user.getUid());
                reference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        Integer total1=0;
                        Integer total2=0;
                        Integer total3=0;

                        for (DataSnapshot dataSnapshot:snapshot.getChildren()){
                            //for (DataSnapshot snap : dataSnapshot.getChildren()){
                            getrecords rec=dataSnapshot.getValue(getrecords.class);
                            Integer total=Integer.valueOf(rec.getGrade1());
                            Integer totalgr2=Integer.valueOf(rec.getGrade2());
                            Integer totalgr3=Integer.valueOf(rec.getMbuni());

                            total1=total1+total;
                            total2=total2+totalgr2;
                            total3=total3+totalgr3;

                        }
                        Integer costperkg=80;
                        Integer grtotal=Integer.valueOf(total1)+Integer.valueOf(total2)+Integer.valueOf(total3);
                        Integer sss=Integer.valueOf(grtotal)*costperkg;
                        er.setText("Ksh "+String.valueOf(sss));
                        Integer rev=Integer.valueOf(grtotal)*costperkg;
                        Integer pay=rev-installment;
                        ep.setText(String.valueOf(pay));
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }
                });
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        back=findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),userLoanHistory.class));
            }
        });
    }
}
