package com.example.coffee;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.regex.Pattern;

public class passreset extends AppCompatActivity {
    EditText email;
    Button reset;
    FirebaseAuth mAuth;
    DatabaseReference reference;
    String usermail;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_passreset);
        email=findViewById(R.id.resetmail);
        reset=findViewById(R.id.btnreset);
        mAuth=FirebaseAuth.getInstance();

        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String mail=email.getText().toString();
                if (mail.isEmpty()){
                    email.setError("email address can't be empty");
                    email.requestFocus();
                }
                if (!Patterns.EMAIL_ADDRESS.matcher(mail).matches()){
                    email.setError("invalid email address");
                    email.requestFocus();
                }
                mAuth.sendPasswordResetEmail(mail).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            Toast.makeText(getApplicationContext(),"Reset email sent.Check your email address",Toast.LENGTH_LONG).show();
                            startActivity(new Intent(getApplicationContext(),userlogin.class));
                        }
                        else {
                            String error=task.getException().getMessage();
                            Toast.makeText(getApplicationContext(),error,Toast.LENGTH_LONG).show();
                        }
                    }
                });
                /*reference= FirebaseDatabase.getInstance().getReference("Users");
                reference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        registerClass user=snapshot.getValue(registerClass.class);
                        usermail=user.getEmail();
                        if (mail==usermail){

                        }
                        else{
                            Toast.makeText(getApplicationContext(),"User with that address does not exist",Toast.LENGTH_LONG).show();
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });*/

            }
        });
    }
}
