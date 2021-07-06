package com.example.coffee.fragments;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.coffee.Adminsell;
import com.example.coffee.R;
import com.example.coffee.adapters.sellhelper;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import static android.app.Activity.RESULT_OK;
import static com.example.coffee.R.layout.support_simple_spinner_dropdown_item;

public class Sell extends Fragment {
    EditText name,cost,quantity,count;
    Button sell;
    ImageView img;
    Spinner spinner;
    DatabaseReference reference;
    StorageReference storageref;
    ProgressDialog progressDialog;
    private static final  int IMAGE_REQUEST=1;
    private Uri imguri;
    private StorageTask uploadImg;
    String item;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       View view= inflater.inflate(R.layout.fragment_sell, container, false);
       name=view.findViewById(R.id.productname);
       cost=view.findViewById(R.id.productcost);
       quantity=view.findViewById(R.id.productquantity);
       sell=view.findViewById(R.id.btnsell);
       count=view.findViewById(R.id.productcount);
       sell.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               sell();
           }
       });
       img=view.findViewById(R.id.sellimg);
       //reference= FirebaseDatabase.getInstance().getReference("Products");
       storageref= FirebaseStorage.getInstance().getReference("Products");
       img.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               openPhoto();
           }
       });
       spinner=view.findViewById(R.id.spinner);
       final ArrayList<String> category=new ArrayList<>();
       category.add("Fertilizer");
       category.add("Chemicals");
       category.add("Tool");
       ArrayAdapter<String> adapter=new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_item,category);
       spinner.setAdapter(adapter);
       spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
           @Override
           public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
               item=category.get(position);
           }

           @Override
           public void onNothingSelected(AdapterView<?> parent) {

           }
       });
       return view;
    }

    private void openPhoto() {
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
    private void sell(){
        progressDialog=new ProgressDialog(getContext());
        //display dialog
        progressDialog.show();
        //set view
        progressDialog.setContentView(R.layout.progress);
        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        SimpleDateFormat sdf=new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");
        final String date=sdf.format(new Date());
        final String names,costs,quantitys,counts;
        names=name.getText().toString();
        costs=cost.getText().toString();
        quantitys=quantity.getText().toString();
        counts=count.getText().toString();
        if (names.isEmpty()){
            name.setError("Enter product name");
            name.requestFocus();
            return;
        }
        if (costs.isEmpty()){
            cost.setError("Enter product cost");
            cost.requestFocus();
            return;
        }
        if (quantitys.isEmpty()){
            quantity.setError("Enter product quantity");
            quantity.requestFocus();
            return;
        }
        if (counts.isEmpty()){
            count.setError("Enter product quantity");
            count.requestFocus();
            return;
        }
        if (img!= null) {
            final StorageReference storageReference1=storageref.child(System.currentTimeMillis() + "."+getExt(imguri));
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
                        DatabaseReference reference=FirebaseDatabase.getInstance().getReference("Product");
                        Uri downloadUri=task.getResult();
                        String mUri=downloadUri.toString();
                        HashMap<String, Object> map=new HashMap<>();
                        String key=reference.push().getKey();
                        sellhelper data=new sellhelper(names,costs,quantitys,mUri,date,item,key,counts);
                        reference.child(key).setValue(data).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            Toast.makeText(getContext(), "Item sold successfully", Toast.LENGTH_LONG).show();
                                            startActivity(new Intent(getActivity(), Adminsell.class));
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
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null){
            imguri=data.getData();
            img.setImageURI(imguri);
            if (uploadImg!=null &&uploadImg.isInProgress()){
                Toast.makeText(getContext(),"uploading",Toast.LENGTH_LONG).show();
            }
        }
    }


}
