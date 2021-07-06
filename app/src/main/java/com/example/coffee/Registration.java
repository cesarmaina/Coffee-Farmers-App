package com.example.coffee;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Registration extends AppCompatActivity {
    FirebaseUser firebaseUser;
    private FirebaseAuth mAuth;
    EditText fname,email,passw,coffeeid,phone,pass11;
    Button register,login;
    ProgressDialog progressDialog;
    DatabaseReference ref;
    public String coffid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();

        fname=findViewById(R.id.name);
        email=findViewById(R.id.mail);
        phone=findViewById(R.id.phone);
        passw=findViewById(R.id.pass);
        pass11=findViewById(R.id.pass1);
        coffeeid =findViewById(R.id.idno);
        register=findViewById(R.id.save);
        login=findViewById(R.id.signin);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent signin=new Intent(Registration.this,userlogin.class);
                startActivity(signin);
            }
        });
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //init dialog
                progressDialog=new ProgressDialog(Registration.this);
                //display dialog
                progressDialog.show();
                //set view
                progressDialog.setContentView(R.layout.progress);
                //transparent bg
                progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                final String usern=fname.getText().toString();
                final String pasw=passw.getText().toString();
                final String mailer=email.getText().toString();
                final String mobile=phone.getText().toString();
                final String coffee= coffeeid.getText().toString();
                final String cpass=pass11.getText().toString();
                SimpleDateFormat sdf=new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");
                final String dates=sdf.format(new Date());

                if (usern.isEmpty()||pasw.isEmpty()||mailer.isEmpty()||mobile.isEmpty())
                {
                    Toast.makeText(getApplicationContext(),"fill in all fields",Toast.LENGTH_SHORT).show();
                }
                if(usern.isEmpty()){
                    fname.setError("username required");
                    fname.requestFocus();
                    return;
                }
                if(coffee.isEmpty()){
                    coffeeid.setError("coffee id required");
                    coffeeid.requestFocus();
                    return;
                }
                if(mailer.isEmpty()){
                    email.setError("Email required");
                    email.requestFocus();
                    return;
                }
                if(pasw.isEmpty()){
                    passw.setError("Password required");
                    passw.requestFocus();
                    return;
                }
                if (coffee.length()<4){
                     coffeeid.setError("coffee id should be atleast 4 characters");
                     coffeeid.requestFocus();
                     return;
                }
                if (mobile.length()<10){
                    phone.setError("Phone Number should be at least 10 characters");
                    phone.requestFocus();
                    return;
                }
                if (pasw.length()<4){
                    passw.setError("Password should be at least than 4 characters");
                    passw.requestFocus();
                    return;
                }
                if (!Patterns.EMAIL_ADDRESS.matcher(mailer).matches()) {
                    email.setError("Invalid email address");
                    email.requestFocus();
                    return;
                }
                if (!cpass.equals(pasw)){
                    pass11.setError("incorrect try again!");
                    pass11.requestFocus();
                    return;
                }
                //progressBar.setVisibility(View.VISIBLE);
                ref=FirebaseDatabase.getInstance().getReference("Users");
                ref.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot snapshot1:snapshot.getChildren()) {
                            registerClass id = snapshot1.getValue(registerClass.class);
                            assert id != null;
                            if (id.getCid().equals(coffee)) {
                                coffeeid.setError("coffee id already registered");
                                coffeeid.requestFocus();
                                progressDialog.dismiss();
                                return;
                            }
                        }
                        mAuth.createUserWithEmailAndPassword(mailer,pasw).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful())
                                {
                                    firebaseUser=mAuth.getCurrentUser();
                                    String UserId=firebaseUser.getUid();
                                    final String image="Default";
                                    registerClass users=new registerClass(UserId,coffee,usern,mailer,mobile,pasw,dates,image);
                                    FirebaseDatabase.getInstance().getReference("Users").child(UserId)
                                            .setValue(users).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful())
                                            {
                                                Toast.makeText(Registration.this,"Registration successful",Toast.LENGTH_LONG).show();
                                                progressDialog.dismiss();
                                                startActivity(new Intent(getApplicationContext(),userlogin.class));
                                            }
                                        }
                                    });
                                }
                            }
                        });
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

            }
        });

    }
    public void onBackPressed(){
        progressDialog.dismiss();
    }
}
