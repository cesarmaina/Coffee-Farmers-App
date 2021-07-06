package com.example.coffee.fragments;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import android.widget.Toast;

import com.example.coffee.adapters.loandbhelper;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class loanDialog extends DialogFragment {
    String lid1,mail,principal,interest,totalpay,time,dates,status;
    FirebaseUser firebaseUser;
    DatabaseReference reference;
    public loanDialog(String emails, String principals,String interests,String lamount, String ltime, String date, String lstatus) {
        mail=emails;
        principal=principals;
        interest=interests;
        totalpay=lamount;
        time=ltime;
        dates=date;
        status=lstatus;
    }
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstancestate) {
        AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
        builder.setTitle("loan application");
        builder.setMessage("Apply for the loan?");
        builder.setCancelable(false);

        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

               // Toast.makeText(getActivity(),"You have"+mail+" "+loan+" "+time+""+dates+":",Toast.LENGTH_LONG).show();

                if (getActivity()!=null) {
                    firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                    reference=FirebaseDatabase.getInstance().getReference("Loan").child("Applications").child(firebaseUser.getUid());
                    loandbhelper loanA = new loandbhelper(reference.push().getKey(),mail,principal,interest,totalpay,time,dates,status);
                    reference.push()
                            .setValue(loanA);
                    Toast.makeText(getActivity(),"You have successfully applied for the loan",Toast.LENGTH_LONG).show();
                }

            }
        });
        builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(getActivity(),"You have cancelled the application",Toast.LENGTH_LONG).show();
            }
        });
        AlertDialog alertDialog=builder.create();
        return alertDialog;
    }
}