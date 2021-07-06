package com.example.coffee;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.coffee.adapters.sellhelper;
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
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class updatesale extends AppCompatActivity {
    ImageView image;
    EditText name,cost,quantity,count;
    Button update;
    DatabaseReference reference;
    FirebaseUser firebaseUser;
    StorageReference storageReference;
    private static final  int IMAGE_REQUEST=1;
    private Uri imguri;
    private StorageTask uploadImg;
    ProgressDialog dialog;
    String item;
    Spinner spinner;
    String coffee;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_updatesale);
        image=findViewById(R.id.sellimg);
        name=findViewById(R.id.productname);
        update=findViewById(R.id.btnupdate);

        cost=findViewById(R.id.productcost);
        quantity=findViewById(R.id.productquantity);
        count=findViewById(R.id.productcount);
        spinner=findViewById(R.id.spinner);
        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openPhoto();
            }
        });
        Bundle bundle=getIntent().getExtras();
        assert bundle!=null;
        coffee=bundle.getString("itemid");
        storageReference= FirebaseStorage.getInstance().getReference("Products");
        final ArrayList<String> category=new ArrayList<>();
        category.add("Fertilizer");
        category.add("Chemicals");
        category.add("Tool");
        ArrayAdapter<String> adapter=new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_spinner_item,category);
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
        firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
        reference= FirebaseDatabase.getInstance().getReference("Product").child(coffee);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                sellhelper item=snapshot.getValue(sellhelper.class);
                name.setText(item.getName());
                cost.setText(item.getCost());
                quantity.setText(item.getQuantity());
                count.setText(item.getCount());
                Glide.with(getApplicationContext()).load(item.getMuri()).into(image);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog=new ProgressDialog(updatesale.this);
                dialog.show();
                dialog.setContentView(R.layout.progress);
                dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                uploadImage();

            }
        });
       /* delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog=new ProgressDialog(updatesale.this);
                dialog.show();
                dialog.setContentView(R.layout.progress);
                dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                startActivity(new Intent(getApplicationContext(),Adminsell.class));
                reference= FirebaseDatabase.getInstance().getReference("Product").child(coffee);
                reference.removeValue();
                dialog.dismiss();
            }
        });*/
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
                        String name1,cost1,quantity1,category,count1;
                        name1=name.getText().toString();
                        cost1=cost.getText().toString();
                        count1=count.getText().toString();
                        quantity1=quantity.getText().toString();
                        category=item;
                        reference=FirebaseDatabase.getInstance().getReference("Product").child(coffee);
                        HashMap<String, Object> map=new HashMap<>();
                        map.put("muri",mUri);
                        map.put("name",name1);
                        map.put("cost",cost1);
                        map.put("count",count1);
                        map.put("quantity",quantity1);
                        map.put("category",category);
                        reference.updateChildren(map);
                        dialog.dismiss();
                        startActivity(new Intent(getApplicationContext(),Adminsell.class));
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
