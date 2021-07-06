package com.example.coffee;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.coffee.adapters.RecordsAdapter;
import com.example.coffee.fragments.LoanStatusDialog;
import com.example.coffee.fragments.loanDialog;
import com.example.coffee.fragments.loanpage1;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class loan extends AppCompatActivity {
    FirebaseUser user;
    ImageView profpic;
    TextView name;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loan);
        user= FirebaseAuth.getInstance().getCurrentUser();
        profpic=findViewById(R.id.pic);
        name=findViewById(R.id.name);
        BottomNavigationView bottomNavigationView=findViewById(R.id.bottom_nav);
        bottomNavigationView.setSelectedItemId(R.id.loan);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId())
                {
                    case R.id.loan:
                        return true;
                    case R.id.records:
                        Intent record=new Intent(getApplicationContext(), userrecords.class);
                        overridePendingTransition(0,0);
                        startActivity(record);
                        finish();
                        return true;
                    case R.id.dashboard:
                        Intent dash=new Intent(getApplicationContext(), userDashboard.class);
                        overridePendingTransition(0,0);
                        startActivity(dash);
                        finish();
                        return true;
                    case R.id.messageactivity:
                        Intent message=new Intent(getApplicationContext(), userChatDashboard.class);
                        overridePendingTransition(0,0);
                        startActivity(message);
                        finish();
                        return true;
                    case R.id.shop:
                        Intent shop=new Intent(getApplicationContext(), Shop.class);
                        overridePendingTransition(0,0);
                        startActivity(shop);
                        finish();
                        return true;

                }
                return false;
            }
        });
        TabLayout tabLayout=findViewById(R.id.tablayout);
        ViewPager viewPager=findViewById(R.id.viewpager);

        //get user pic and name onto the toolbar

        DatabaseReference ref= FirebaseDatabase.getInstance().getReference("Users").child(user.getUid());
        ref.addValueEventListener(new ValueEventListener() {
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
        //calculate total revenue
        FirebaseUser user=FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference reference1= FirebaseDatabase.getInstance().getReference("Records").child(user.getUid());
        reference1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Integer total1=0;
                Integer total2=0;
                Integer total3=0;
                for (DataSnapshot dataSnapshot:snapshot.getChildren()){
                    String  totalgr1,totalgr2,totalgr3;
                    getrecords rec=dataSnapshot.getValue(getrecords.class);
                    totalgr1=rec.getGrade1();
                    totalgr2=rec.getGrade2();
                    totalgr3=rec.getMbuni();

                    total1=total1+ Integer.valueOf(totalgr1);
                    total2=total2+ Integer.valueOf(totalgr2);
                    total3=total3+ Integer.valueOf(totalgr3);
                }
                Integer costperkg=80;
                Integer grtotal=Integer.valueOf(total1)+Integer.valueOf(total2)+Integer.valueOf(total3);
                Integer totalp =Integer.valueOf(grtotal)*costperkg;
                Integer mylimit=totalp*5;
                loanpage1 loanpage1=new loanpage1(String.valueOf(mylimit));
                FragmentManager fragmentManager=getSupportFragmentManager();
                FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.frame,loanpage1);
                fragmentTransaction.commit();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        //checking a current loan application

        DatabaseReference reference= FirebaseDatabase.getInstance().getReference("Loan").child("Applications").child(user.getUid());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                long count=snapshot.getChildrenCount();
                if ((count==1)) {
                    FragmentManager fragmentManager1=getSupportFragmentManager();
                    LoanStatusDialog Dialog=new LoanStatusDialog();
                    Dialog.show(fragmentManager1,"status");
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

}
