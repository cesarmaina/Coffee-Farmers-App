package com.example.coffee.fragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.coffee.R;
import com.example.coffee.adapters.RecordsAdapter;
import com.example.coffee.getrecords;
import com.example.coffee.registerClass;
import com.example.coffee.repayment;
import com.example.coffee.userDashboard;
import com.example.coffee.userLoanHistory;
import com.example.coffee.userrecords;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import static android.graphics.Color.RED;

public class loanpage1 extends Fragment {
    Spinner time;
    EditText amount;
    Button next,check;
    String ltime;
    TextView limit;
    String revenue1;
    DatabaseReference reference;
    FirebaseUser user;
    public loanpage1(String revenue){
        revenue1=revenue;
    }
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        user= FirebaseAuth.getInstance().getCurrentUser();
        reference= FirebaseDatabase.getInstance().getReference("Users").child(user.getUid());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                registerClass account=snapshot.getValue(registerClass.class);
                if (account.getImage().equals("Default")){
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
       final View view=inflater.inflate(R.layout.fragment_loanpage1, container, false);
       time=view.findViewById(R.id.spinnerTime);
       limit=view.findViewById(R.id.limit);
       if (Integer.valueOf(revenue1)<1000){
           limit.setText("Your account limit is very low");
           limit.setTextColor(RED);
       }
       if (Integer.valueOf(revenue1)>999){
           limit.setText("1000 - "+revenue1);
           limit.setTextColor(RED);
       }
       final ArrayList<String>times=new ArrayList<>();
       amount=view.findViewById(R.id.principal);
       times.add("1");
       times.add("2");
       times.add("3");
       times.add("4");
       times.add("5");
       times.add("6");
       times.add("7");

       ArrayAdapter<String>adapter1=new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_item,times);
       time.setAdapter(adapter1);
       time.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
           @Override
           public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
               ltime=times.get(position);
           }

           @Override
           public void onNothingSelected(AdapterView<?> parent) {

           }
       });
       next=view.findViewById(R.id.lnext);
       next.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               final String principal=amount.getText().toString();
               if (principal.isEmpty()){
                   amount.setError("ENTER AN AMOUNT");
                   amount.requestFocus();
               }
               else {
                   if (Integer.valueOf(principal)<1000){
                       Toast.makeText(getContext(),"Minimum amount is sh 1000",Toast.LENGTH_LONG).show();
                       amount.setError("Least amount is 1000");
                       amount.requestFocus();
                   }
                   else{
                       if (Integer.valueOf(principal)>Integer.valueOf(revenue1)){
                           Toast.makeText(getContext(),"Try a smaller amount",Toast.LENGTH_LONG).show();
                       }
                       else {
                           loanpage2 loanpage2=new loanpage2(principal,ltime,revenue1);
                           FragmentManager fragmentManager=getFragmentManager();
                           FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
                           fragmentTransaction.replace(R.id.frame,loanpage2);
                           fragmentTransaction.show(loanpage2);
                           fragmentTransaction.commit();
                       }
                   }
               }
           }
       });
       check=view.findViewById(R.id.checkhist);
       check.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               startActivity(new Intent(getContext(), userLoanHistory.class));
           }
       });
       return view;
    }
}
