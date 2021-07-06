package com.example.coffee.fragments;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.example.coffee.R;
import com.example.coffee.recordclass;
import com.example.coffee.registerClass;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Adminsaverecord extends Fragment {
    EditText userid,grade1,grade2,mbuni;
    Button save;
    String firebaseUser;
    DatabaseReference ref;
    ProgressDialog progressDialog;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_adminsaverecord, container, false);
        userid = view.findViewById(R.id.farmer);
        grade1 = view.findViewById(R.id.amount);
        grade2 =view.findViewById(R.id.g2amount);
        mbuni =view.findViewById(R.id.mbamount);
        save = view.findViewById(R.id.record);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finduserid();
            }
        });
        return view;
    }
    private void finduserid(){
        DatabaseReference reference= FirebaseDatabase.getInstance().getReference("Users");
        final String user, g1,g2,mbu,dates;
        user = userid.getText().toString();
        g1 = grade1.getText().toString();
        g2=grade2.getText().toString();
        mbu=mbuni.getText().toString();
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");
        dates=sdf.format(new Date());
        if (user.isEmpty()){
            userid.setError("id required");
            userid.requestFocus();
            return;
        }
        if (g1.isEmpty()){
            grade1.setError("grade1 required");
            grade1.requestFocus();
            return;
        }
        if (g2.isEmpty()){
            grade2.setError("grade2 required");
            grade2.requestFocus();
            return;
        }
        if (mbu.isEmpty()){
            mbuni.setError("Mbuni required");
            mbuni.requestFocus();
            return;
        }
        progressDialog=new ProgressDialog(getContext());
        //display dialog
        progressDialog.show();
        //set view
        progressDialog.setContentView(R.layout.progress);
        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot:snapshot.getChildren()){
                    registerClass users=dataSnapshot.getValue(registerClass.class);
                    assert users != null;
                    if (users.getCid().equals(user)){
                        firebaseUser=users.getId();
                        final recordclass data = new recordclass(user, g1,g2,mbu,dates);
                        ref = FirebaseDatabase.getInstance().getReference("Records").child(firebaseUser);
                        ref.push().setValue(data);
                        progressDialog.dismiss();
                        Toast.makeText(getContext(), "Successfully saved",Toast.LENGTH_LONG).show();
                    }
                   /* if (!users.getId().equals(user)){
                        userid.setError("invalid id");
                        userid.requestFocus();
                        progressDialog.dismiss();
                        Toast.makeText(getContext(), "Successfully saved",Toast.LENGTH_LONG).show();
                    }*/
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }
}
