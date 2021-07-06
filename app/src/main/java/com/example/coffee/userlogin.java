package com.example.coffee;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
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

public class userlogin extends AppCompatActivity {
    EditText email,pasw;
    Button login,register;
    TextView reset;
    private FirebaseAuth mAuth;
    FirebaseUser firebaseUser;
    DatabaseReference reference;
    ProgressDialog progressDialog;

    /*protected void onStart() {
        super.onStart();
        firebaseUser=FirebaseAuth.getInstance().getCurrentUser();
        reference= FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (firebaseUser!=null){
                    registerClass users=snapshot.getValue(registerClass.class);
                    if (users.getEmail().equals("Admin12@gmail.com")){
                        Intent intent=new Intent(userlogin.this,AdminDashboard.class);
                        startActivity(intent);
                        finish();
                    }
                    else {
                        Intent intent=new Intent(userlogin.this,userDashboard.class);
                        startActivity(intent);
                        finish();
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_userlogin);
        email=findViewById(R.id.logemail);
        pasw=findViewById(R.id.logpass);
        mAuth= FirebaseAuth.getInstance();
        reset=findViewById(R.id.forgot);
        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),passreset.class));
            }
        });
        register=findViewById(R.id.registerbtn);

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),Registration.class));
            }
        });

        login=findViewById(R.id.loginbtn);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String mail=email.getText().toString();
                final String pass=pasw.getText().toString();

                if (mail.isEmpty()) {
                    email.setError("Email address required");
                    email.requestFocus();
                    return;
                }
                if (pass.isEmpty()) {
                    pasw.setError("Password required");
                    pasw.requestFocus();
                    return;
                }
                if (!Patterns.EMAIL_ADDRESS.matcher(mail).matches()) {
                    email.setError("invalid email address");
                    email.requestFocus();
                    return;
                }
                else {
                    //init dialog
                    progressDialog=new ProgressDialog(userlogin.this);
                    //display dialog
                    progressDialog.show();
                    //set view
                    progressDialog.setContentView(R.layout.progress);
                    progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                    mAuth.signInWithEmailAndPassword(mail,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful())
                            {
                                firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
                                reference= FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid());
                                reference.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        registerClass users=snapshot.getValue(registerClass.class);
                                        if (users.getEmail().equals("Admin12@gmail.com")){
                                            Intent dashboard=new Intent(userlogin.this, AdminDashboard.class);
                                            dashboard.putExtra("email",mail);
                                            FirebaseUser firebaseUser=mAuth.getCurrentUser();
                                            String uid=firebaseUser.getUid();
                                            dashboard.putExtra("userId",uid);
                                            startActivity(dashboard);
                                            finish();
                                            progressDialog.dismiss();
                                        }
                                        else {
                                            Intent dashboard=new Intent(userlogin.this, userDashboard.class);
                                            dashboard.putExtra("email",mail);
                                            FirebaseUser firebaseUser=mAuth.getCurrentUser();
                                            String uid=firebaseUser.getUid();
                                            dashboard.putExtra("userId",uid);
                                            startActivity(dashboard);
                                            finish();
                                            progressDialog.dismiss();
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });

                            }
                            else
                            {
                                String alert="wrong username or password";
                                progressDialog.dismiss();
                                Toast.makeText(userlogin.this,alert,Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                }

            }
        });

    }
    public void onBackPressed(){
        progressDialog.dismiss();
    }
}
