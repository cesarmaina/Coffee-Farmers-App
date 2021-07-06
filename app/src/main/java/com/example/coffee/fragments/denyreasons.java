package com.example.coffee.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.coffee.R;
import com.example.coffee.adapters.loandbhelper;
import com.example.coffee.adminLoanProcessing;
import com.example.coffee.getMsgs;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class denyreasons extends DialogFragment {
    List <String> reasons;
    List <String>strreasons=new ArrayList<>();
    String lid1,userId1,mail1,princ1,intere1,tp1,timee1,dates1,status1;
    public denyreasons(String lid,String userId,String mail, String princ, String intere, String tp, String timee, String dates, String status) {
        lid1=lid;
        userId1=userId;
        mail1=mail;
        princ1=princ;
        intere1=intere;
        tp1=tp;
        timee1=timee;
        dates1=dates;
        status1=status;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState){
        reasons=new ArrayList<>();
        reasons.add("Try a adding afew years for repayment.");
        reasons.add("Try a smaller amount of loan.");
        reasons.add("Your previous loan has not been fully repaid.");

        final AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
        builder.setTitle("reason");
        builder.setMultiChoiceItems(reasons.toArray(new String[0]),null,new DialogInterface.OnMultiChoiceClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                if (isChecked){
                    strreasons.add(reasons.get(which));
                }
                else {
                    strreasons.remove(reasons.get(which));
                }
            }
        });
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String select="";
                for (String item:strreasons){
                    select=select+ "\n" +item;
                }
                DatabaseReference ref= FirebaseDatabase.getInstance().getReference("Loan").child("Processed").child(userId1).child(lid1);
                loandbhelper loans=new loandbhelper(lid1,mail1,princ1,intere1,tp1,timee1,dates1,status1);
                ref.setValue(loans);
                DatabaseReference ref1=FirebaseDatabase.getInstance().getReference("Loan").child("Applications").child(userId1);
                ref1.removeValue();
                startActivity(new Intent(getContext(), adminLoanProcessing.class));
                Toast.makeText(getContext(),"Loan Application Denied",Toast.LENGTH_LONG).show();
                //sending message
                FirebaseUser user=FirebaseAuth.getInstance().getCurrentUser();
                String Adminid=user.getUid();
                String chat="Hello. Your loan application of "+ princ1+" was unsuccessful. \n"+ select;
                getMsgs msg=new getMsgs(Adminid,userId1,chat);
                DatabaseReference reff= FirebaseDatabase.getInstance().getReference("Messages");
                reff.push().setValue(msg);

                Toast.makeText(getActivity(),select,Toast.LENGTH_LONG).show();

            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        return builder.create();
    }
}
