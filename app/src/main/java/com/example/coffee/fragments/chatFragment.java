package com.example.coffee.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSmoothScroller;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.coffee.R;
import com.example.coffee.adapters.usersAdapter;
import com.example.coffee.getMsgs;
import com.example.coffee.registerClass;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class chatFragment extends Fragment {
    private RecyclerView recyclerView;
    private usersAdapter usersadapter;
    private List<registerClass>musers;
    private List<String>users;
    FirebaseUser firebaseUser;
    DatabaseReference reference;
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_chat, container, false);
        recyclerView=view.findViewById(R.id.recycler);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
        users=new ArrayList<>();
        reference= FirebaseDatabase.getInstance().getReference("Messages");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                users.clear();
                for (DataSnapshot dataSnapshot:snapshot.getChildren()){
                    getMsgs msg=dataSnapshot.getValue(getMsgs.class);
                    if (msg.getSender().equals(firebaseUser.getUid())){
                        users.add(msg.getReceiver());
                    }
                    if (msg.getReceiver().equals(firebaseUser.getUid())){
                        users.add(msg.getSender());
                    }
                }
                readmsg();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        return view;
    }
    private void readmsg(){
        musers=new ArrayList<>();
        reference=FirebaseDatabase.getInstance().getReference("Users");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                musers.clear();
                for (DataSnapshot dataSnapshot:snapshot.getChildren()){
                    registerClass user=dataSnapshot.getValue(registerClass.class);
                    for (String id :users){
                        if (user.getId().equals(id)){
                            if (musers.size()!=0){
                                for (registerClass user1:musers){
                                    if (!user.getId().equals(user1.getId())){
                                        musers.add(user);
                                    }
                                }
                            }
                            else {
                                musers.add(user);
                            }
                        }
                    }
                }
                usersadapter=new usersAdapter(getContext(),musers);
                recyclerView.setAdapter(usersadapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}
