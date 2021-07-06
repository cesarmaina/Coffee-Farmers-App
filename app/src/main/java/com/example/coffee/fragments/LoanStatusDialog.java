package com.example.coffee.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.coffee.R;
import com.example.coffee.adapters.loandbhelper;
import com.example.coffee.userDashboard;
import com.example.coffee.userLoanHistory;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;


public class LoanStatusDialog extends DialogFragment {
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstancestate) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("loan application");
        builder.setMessage("You have a loan application currently under processing");
        builder.setCancelable(false);
        builder.setPositiveButton("Check History", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startActivity(new Intent(getContext(), userLoanHistory.class));
            }
        });
        builder.setNegativeButton("Back", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startActivity(new Intent(getContext(), userDashboard.class));
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.setCanceledOnTouchOutside(false);

        //back key pressed
        Intent intent=new Intent(getContext(),userDashboard.class);
        alertDialog.onBackPressed();
        return alertDialog;
    }
}
