package com.example.coffee;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.coffee.adapters.msgAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Messages extends AppCompatActivity {
    TextView username;
    EditText message;
    ImageButton send,phonec;
    ImageView profpic;
    FirebaseUser firebaseUser;
    DatabaseReference reference;
    Intent intent;
    msgAdapter msgadapter;
    List<getMsgs>getmsgs;
    RecyclerView recycler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messages);

        username=findViewById(R.id.msg_rcv);
        profpic=findViewById(R.id.msgimg);
        message=findViewById(R.id.msg);
        phonec=findViewById(R.id.phonecall);
        send=findViewById(R.id.btn_send);
        recycler=findViewById(R.id.recycler_msg);
        recycler.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setStackFromEnd(true);
        recycler.setLayoutManager(linearLayoutManager);

        intent = getIntent();
        final String UserId=intent.getStringExtra("UserId");
        phonec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference ref=FirebaseDatabase.getInstance().getReference("Users").child(UserId);
                ref.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        registerClass data=snapshot.getValue(registerClass.class);
                        String phone=data.getPhone();
                        Intent intent=new Intent(Intent.ACTION_DIAL);
                        intent.setData(Uri.parse("tel:"+phone));
                        startActivity(intent);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text=message.getText().toString();
                if (text.isEmpty()){
                    Toast.makeText(getApplicationContext(),"You can not send blank message",Toast.LENGTH_SHORT).show();
                }
                else {
                    sendmsg(firebaseUser.getUid(),UserId,text);
                    Toast.makeText(getApplicationContext(),"Done",Toast.LENGTH_SHORT).show();
                }
            }
        });
        firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
        assert UserId != null;
        reference= FirebaseDatabase.getInstance().getReference("Users").child(UserId);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                registerClass user=snapshot.getValue(registerClass.class);
                username.setText(user.getName());

                if (user.getImage().equals("Default")){
                    profpic.setImageResource(R.drawable.account1);
                }
                else {
                    Glide.with(getApplicationContext()).load(user.getImage()).into(profpic);
                }
                readmsg(firebaseUser.getUid(),UserId);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void sendmsg(String Sender,String Receiver,String Message){
        DatabaseReference reference=FirebaseDatabase.getInstance().getReference();
        HashMap<String,Object> hashMap=new HashMap<>();
        hashMap.put("sender",Sender);
        hashMap.put("receiver",Receiver);
        hashMap.put("message",Message);

        reference.child("Messages").push().setValue(hashMap);
    }
    private void readmsg(final String myid, final String userid){
        getmsgs=new ArrayList<>();
        reference=FirebaseDatabase.getInstance().getReference("Messages");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                getmsgs.clear();
                for (DataSnapshot dataSnapshot:snapshot.getChildren()){
                    getMsgs msg=dataSnapshot.getValue(getMsgs.class);
                    if ((msg.getReceiver().equals(myid)&&msg.getSender().equals(userid))||
                            msg.getReceiver().equals(userid)&&msg.getSender().equals(myid)){
                        getmsgs.add(msg);
                    }
                    msgadapter=new msgAdapter(Messages.this,getmsgs);
                    recycler.setAdapter(msgadapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
