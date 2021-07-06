package com.example.coffee.fragments;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.coffee.AdminDashboard;
import com.example.coffee.R;
import com.example.coffee.newsAdapter;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
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

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import static android.app.Activity.RESULT_OK;

public class addnews extends Fragment {
    ImageView image;
    EditText descr;
    Button addnews;
    DatabaseReference reference;
    FirebaseUser firebaseUser;
    StorageReference storageReference;
    private static final  int IMAGE_REQUEST=1;
    private Uri imguri;
    private StorageTask uploadImg;
    ProgressDialog progressDialog;
    long maxid;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_addnews, container, false);
        addnews = view.findViewById(R.id.addnews);
        addnews.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadNews();
            }
        });
        image = view.findViewById(R.id.uploadimg);
        descr = view.findViewById(R.id.newsfeed);
        storageReference = FirebaseStorage.getInstance().getReference("News");
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("News");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                maxid = (snapshot.getChildrenCount());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openPhoto();
            }

        });
        return view;
    }
    private void openPhoto(){
        Intent intent=new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,IMAGE_REQUEST);
    }
    private String getExt(Uri uri){
        ContentResolver contentResolver=getContext().getContentResolver();
        MimeTypeMap mimeTypeMap=MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }
    private void uploadNews(){
        progressDialog=new ProgressDialog(getContext());
        //display dialog
        progressDialog.show();
        //set view
        progressDialog.setContentView(R.layout.progress);
        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");
        final String date=sdf.format(new Date());
        final String text=descr.getText().toString();
        if (imguri != null) {
            final StorageReference storageReference1=storageReference.child(System.currentTimeMillis()
                    + "."+getExt(imguri));
            uploadImg=storageReference1.putFile(imguri);
            uploadImg.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
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
                        HashMap<String, Object> map=new HashMap<>();
                        newsAdapter newss=new newsAdapter(firebaseUser.getUid(),text,mUri,date);
                        FirebaseDatabase.getInstance().getReference("News").child(String.valueOf(maxid+1)).setValue(newss)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            Toast.makeText(getContext(), "Upload successful", Toast.LENGTH_LONG).show();
                                            startActivity(new Intent(getActivity(), AdminDashboard.class));
                                            progressDialog.dismiss();

                                        }
                                    }
                                });
                    }
                    else{
                        Toast.makeText(getContext(),"Failed",Toast.LENGTH_LONG).show();
                        progressDialog.dismiss();
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getContext(),e.getMessage(),Toast.LENGTH_LONG).show();
                }
            });
        }
        else {
            Toast.makeText(getContext(),"No image selected",Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null){
            imguri=data.getData();
            image.setImageURI(imguri);
            if (uploadImg!=null &&uploadImg.isInProgress()){
                Toast.makeText(getContext(),"uploading",Toast.LENGTH_LONG).show();
            }
        }
    }
}
