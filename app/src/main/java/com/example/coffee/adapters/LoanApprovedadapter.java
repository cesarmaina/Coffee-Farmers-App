package com.example.coffee.adapters;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.coffee.R;
import com.example.coffee.repayment;

import java.util.List;

public class LoanApprovedadapter extends RecyclerView.Adapter<LoanApprovedadapter.ViewHolder>{
    private Context mcontext;
    private List<loandbhelper> lapps;
    public LoanApprovedadapter(Context mcontext, List<loandbhelper> lapps) {
        this.lapps = lapps;
        this.mcontext = mcontext;
    }
    @NonNull
    @Override
    public LoanApprovedadapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(mcontext).inflate(R.layout.activity_loan_approvedadapter,parent,false);
        return new LoanApprovedadapter.ViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull LoanApprovedadapter.ViewHolder holder, final int position) {
        final loandbhelper loans=lapps.get(position);
        //display Approved loan applications
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
