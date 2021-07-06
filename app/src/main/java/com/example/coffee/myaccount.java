package com.example.coffee;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Instrumentation;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;

public class myaccount extends AppCompatActivity {
    ImageView image;
    EditText user,email,phone;
    TextView coffeeid;
    Button cancel,update;
    DatabaseReference reference;
    FirebaseUser firebaseUser;
    StorageReference storageReference;
    private static final  int IMAGE_REQUEST=1;
    private Uri imguri;
    private StorageTask uploadImg;
    ProgressDialog dialog;
    String img;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_updateaccount);
        image=findViewById(R.id.profpic);
        user=findViewById(R.id.username);
        cancel=findViewById(R.id.btncancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),userDashboard.class));
            }
        });
        update=findViewById(R.id.btnupdate);
        email=findViewById(R.id.email);
        phone=findViewById(R.id.phone);
        coffeeid=findViewById(R.id.coffeeid);
        storageReference= FirebaseStorage.getInstance().getReference("profile");
        firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
        reference= FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                registerClass users=snapshot.getValue(registerClass.class);
                user.setText(users.getName());
                email.setText(users.getEmail());
                phone.setText(users.getPhone());
                coffeeid.setText(users.getCid());
                img=users.getImage();

                if (users.getImage().equals("Default")){
                    image.setImageResource(R.drawable.account1);
                }
                else {
                    Glide.with(getApplicationContext()).load(users.getImage()).into(image);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog=new ProgressDialog(myaccount.this);
                dialog.show();
                dialog.setContentView(R.layout.progress);
                dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                uploadImage();
            }
        });
        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openPhoto();
            }
        });
    }
    private void openPhoto(){
        Intent intent=new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,IMAGE_REQUEST);
    }
    private String getExt(Uri uri){
        ContentResolver contentResolver=getApplicationContext().getContentResolver();
        MimeTypeMap mimeTypeMap=MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }
    private void uploadImage(){
        if (imguri != null) {
            final StorageReference storageReference1=storageReference.child(System.currentTimeMillis()
            + "."+getExt(imguri));
            uploadImg=storageReference1.putFile(imguri);
            uploadImg.continueWithTask(new Continuation<UploadTask.TaskSnapshot,Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task <UploadTask.TaskSnapshot>task) throws Exception {
                    if (!task.isSuccessful()){
                        throw  task.getException();
                    }
                    return storageReference1.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task <Uri>task) {
                    if (task.isSuccessful()){
                        Uri downloadUri=task.getResult();
                        String mUri=downloadUri.toString();
                        String username,mail,tel,cid;
                        username=user.getText().toString();
                        mail=email.getText().toString();
                        tel=phone.getText().toString();
                        cid=coffeeid.getText().toString();
                        reference=FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid());
                        HashMap<String, Object> map=new HashMap<>();
                        map.put("image",mUri);
                        map.put("name",username);
                        map.put("email",mail);
                        map.put("phone",tel);
                        map.put("cid",cid);
                        reference.updateChildren(map);
                        firebaseUser.updateEmail(mail);
                        dialog.dismiss();
                    }
                    else{
                        Toast.makeText(getApplicationContext(),"Failed",Toast.LENGTH_LONG).show();
                        dialog.dismiss();
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_LONG).show();
                    dialog.dismiss();
                }
            });
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null){
            imguri=data.getData();
            image.setImageURI(imguri);
            if (uploadImg!=null &&uploadImg.isInProgress()){
                Toast.makeText(getApplicationContext(),"uploading",Toast.LENGTH_LONG).show();
            }
        }
    }
}
