package com.example.coffee.adapters;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.coffee.R;
import com.example.coffee.adminLoanProcessing;
import com.example.coffee.fragments.denyreasons;
import com.example.coffee.fragments.loanviewapplications;
import com.example.coffee.getrecords;
import com.example.coffee.registerClass;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Date;

import static android.graphics.Color.BLUE;
import static android.graphics.Color.RED;
import static android.graphics.Color.YELLOW;

public class ApproveOrDeny extends AppCompatActivity {
    TextView mailer,name,coffid;
    TextView principal,time,interest,total;
    TextView totalr,revenue,totalrecs,msg,timecalc;
    String userid,totalpay,princ,intere;
    Button Approve,Deny,back;
    loandbhelper loan;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_approveordeny);


        Bundle bundle=getIntent().getExtras();
        assert bundle !=null;
        final String mail=bundle.getString("mail");
        final String tp=bundle.getString("totalpay");
        final String timee=bundle.getString("time");
        mailer=findViewById(R.id.mail1);
        name=findViewById(R.id.name1);
        coffid=findViewById(R.id.cid1);
        mailer.setText(mail);

        principal=findViewById(R.id.pr1);
        time=findViewById(R.id.tr1);
        interest=findViewById(R.id.Ir1);
        total=findViewById(R.id.ttr1);

        totalr=findViewById(R.id.trecords);
        revenue=findViewById(R.id.erevenue);
        totalrecs=findViewById(R.id.totalrecords);
        timecalc=findViewById(R.id.time1);
        //msg=findViewById(R.id.msg1);
        Approve=findViewById(R.id.btnapprove);
        Deny=findViewById(R.id.btndeny);
        back=findViewById(R.id.btnback);

        DatabaseReference reference= FirebaseDatabase.getInstance().getReference("Users");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (final DataSnapshot dataSnapshot:snapshot.getChildren()){
                    registerClass user=dataSnapshot.getValue(registerClass.class);
                    if (user.getEmail().equals(mail)){
                        name.setText(" "+user.getName());
                        coffid.setText(user.getCid());
                        userid=user.getId();
                        //get loan application data
                        DatabaseReference reference1=FirebaseDatabase.getInstance().getReference("Loan").child("Applications").child(userid);
                        reference1.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                for (DataSnapshot dataSnapshot1:snapshot.getChildren()){
                                    loandbhelper loans=dataSnapshot1.getValue(loandbhelper.class);
                                    assert loans != null;
                                    princ=loans.getPrincipal();
                                    principal.setText("Sh "+princ);
                                    time.setText(loans.getTime()+"Yrs");
                                    intere=loans.getInterest();
                                    interest.setText("Sh "+intere+ "p.a");
                                    total.setText("Sh "+ loans.getLoan());
                                    totalpay=loans.getLoan();
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });

                        //farmer records
                        DatabaseReference reference2= FirebaseDatabase.getInstance().getReference("Records").child(userid);
                        reference2.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                Integer total1=0;
                                Integer total2=0;
                                Integer total3=0;
                                for(DataSnapshot dataSnapshot1:snapshot.getChildren()){
                                    getrecords records=dataSnapshot1.getValue(getrecords.class);
                                    Integer total=Integer.valueOf(records.getGrade1());
                                    Integer totalgr2=Integer.valueOf(records.getGrade2());
                                    Integer totalgr3=Integer.valueOf(records.getMbuni());

                                    total1=total1+total;
                                    total2=total2+totalgr2;
                                    total3=total3+totalgr3;

                                }
                                totalr.setText("grade 1 = "+total1 +"Kgs   "+"grade 2 = "+total2 +"Kgs   "+"Mbuni = "+total3 +"Kgs   ");
                                Integer total=Integer.valueOf(total1)+Integer.valueOf(total2)+Integer.valueOf(total3);
                                Integer reven=total*80;
                                totalrecs.setText(String.valueOf(total)+ " Kgs");
                                revenue.setText(String.valueOf(reven));

                                Double totalpays=Double.valueOf(tp);
                                Double esttime=totalpays/reven;
                                Double estround=Math.round(esttime*100)/100D;
                                timecalc.setText(String.valueOf(estround)+" Yrs");
                                /*if (esttime>Double.valueOf(timee)){
                                    msg.setText("DENY !!! User Should add years in loan application");
                                    msg.setTextColor(RED);
                                }
                                else {
                                    msg.setText("APPROVE!!! The loan will be repaid within "+timee+" Yrs");
                                    msg.setTextColor(BLUE);
                                }*/
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        Approve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String status="approved";
                Bundle bundle=getIntent().getExtras();
                assert bundle !=null;
                final String mail=bundle.getString("mail");
                final String tp=bundle.getString("totalpay");
                final String timee=bundle.getString("time");
                final String lid=bundle.getString("loanid");
                DatabaseReference ref=FirebaseDatabase.getInstance().getReference("Users");
                ref.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot snapshot1:snapshot.getChildren()){
                            registerClass users=snapshot1.getValue(registerClass.class);
                            if (users.getEmail().equals(mail)){
                                String userId=users.getId();
                                SimpleDateFormat sdf=new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");
                                final String dates=sdf.format(new Date());
                                loandbhelper loans=new loandbhelper(lid,mail,princ,intere,tp,timee,dates,status);
                                DatabaseReference ref=FirebaseDatabase.getInstance().getReference("Loan").child("Processed").child(userId).child(lid);
                                ref.setValue(loans);
                                DatabaseReference ref1=FirebaseDatabase.getInstance().getReference("Loan").child("Applications").child(userId);
                                ref1.removeValue();
                                startActivity(new Intent(getApplicationContext(), adminLoanProcessing.class));
                                Toast.makeText(getApplicationContext(),"Loan Successfuly Approved",Toast.LENGTH_LONG).show();

                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });
        Deny.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String status="Denied";
                Bundle bundle=getIntent().getExtras();
                assert bundle !=null;
                final String mail=bundle.getString("mail");
                final String tp=bundle.getString("totalpay");
                final String timee=bundle.getString("time");
                final String lid1=bundle.getString("loanid");
                DatabaseReference ref=FirebaseDatabase.getInstance().getReference("Users");
                ref.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot snapshot1:snapshot.getChildren()){
                            registerClass users=snapshot1.getValue(registerClass.class);
                            if (users.getEmail().equals(mail)){
                                String userId=users.getId();
                                SimpleDateFormat sdf=new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");
                                final String dates=sdf.format(new Date());
                                new denyreasons(lid1,userId,mail,princ,intere,tp,timee,dates,status).show(getSupportFragmentManager(),"Display checkbox");
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), adminLoanProcessing.class));
            }
        });
    }
}
