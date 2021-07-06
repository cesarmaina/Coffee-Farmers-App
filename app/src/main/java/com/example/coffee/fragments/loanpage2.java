package com.example.coffee.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.coffee.R;
import com.example.coffee.loan;
import com.example.coffee.registerClass;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Date;

public class loanpage2 extends DialogFragment {
    TextView amount,time,rate,interests,total;
    FirebaseUser firebaseUser;
    DatabaseReference reference;
    Button application,previous;
    String amount1,time1,revenue1;
    String emails,lamount,ltime,date,status;
    public loanpage2(String amounts,String times,String revenue){
        amount1=amounts;
        time1=times;
        revenue1=revenue;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_loanpage2, container, false);
        amount=view.findViewById(R.id.amount1);
        amount.setText(amount1);

        time=view.findViewById(R.id.time1);
        time.setText(time1);

        rate=view.findViewById(R.id.interestrate);
        rate.setText(" 7%");

        interests=view.findViewById(R.id.interest);
        int loan=new Integer(amount1);
        final Integer interestperyear=7*loan/100;
        interests.setText("Ksh: "+interestperyear+" per year");

        final String tt=String.valueOf(interestperyear);

        total=view.findViewById(R.id.totalpay);

        Integer totalpayment=loan+interestperyear*Integer.valueOf(time1);

        final String totalpay=String.valueOf(totalpayment);
        total.setText("Ksh"+totalpay);
        status="Application";

        application=view.findViewById(R.id.applyloan);
        application.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
                reference= FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid());
                reference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        registerClass users=snapshot.getValue(registerClass.class);
                        emails=users.getEmail();
                        SimpleDateFormat sdf=new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");
                        date=sdf.format(new Date());
                        FragmentManager fragmentManager=getFragmentManager();
                        loanDialog loanDialog=new loanDialog(emails,amount1,tt,totalpay,time1,date,status);
                        loanDialog.show(fragmentManager,"apply loan");
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });
        previous=view.findViewById(R.id.backn);
        previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loanpage1 loanpage1=new loanpage1(revenue1);
                FragmentManager fragmentManager=getFragmentManager();
                FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.frame,loanpage1);
                fragmentTransaction.commit();
            }
        });
        return view;
    }

}
