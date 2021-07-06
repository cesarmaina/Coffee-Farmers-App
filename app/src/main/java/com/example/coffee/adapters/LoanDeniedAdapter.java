package com.example.coffee.adapters;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.coffee.R;
import com.google.firebase.database.DatabaseReference;

import java.util.List;

public class LoanDeniedAdapter extends RecyclerView.Adapter<LoanDeniedAdapter.ViewHolder>{
    private Context mcontext;
    private List<loandbhelper> lapps;
        public LoanDeniedAdapter(Context mcontext, List<loandbhelper> lapps) {
        this.lapps = lapps;
        this.mcontext = mcontext;
    }
    long id;
    @NonNull
    @Override
    public LoanDeniedAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(mcontext).inflate(R.layout.activity_loan_deniedadapter,parent,false);
        return new LoanDeniedAdapter.ViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull LoanDeniedAdapter.ViewHolder holder, final int position) {
        final loandbhelper loans=lapps.get(position);
        //display Denied loan applications
        holder.email.setText(loans.getMail());
        holder.amount.setText(loans.getLoan());
        holder.time.setText(loans.getTime());
    }
    @Override
    public int getItemCount() {
        return lapps.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder{
        public TextView email;
        public TextView amount;
        public TextView time;

        public ViewHolder (View itemView){
            super(itemView);
            email=itemView.findViewById(R.id.em);
            amount=itemView.findViewById(R.id.am);
            time=itemView.findViewById(R.id.ti);
        }
    }
}
