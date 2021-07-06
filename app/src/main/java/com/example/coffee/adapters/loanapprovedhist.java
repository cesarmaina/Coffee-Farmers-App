package com.example.coffee.adapters;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.coffee.R;
import com.example.coffee.repayment;

import java.util.List;

public class loanapprovedhist extends RecyclerView.Adapter<loanapprovedhist.ViewHolder>{
    private Context mcontext;
    private List<loandbhelper> lapps;
    String status;
    public loanapprovedhist(Context mcontext, List<loandbhelper> lapps) {
        this.lapps = lapps;
        this.mcontext = mcontext;
    }
    @NonNull
    @Override
    public loanapprovedhist.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(mcontext).inflate(R.layout.fragment_loanhistory,parent,false);
        return new loanapprovedhist.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull loanapprovedhist.ViewHolder holder, int position) {
        final loandbhelper loans=lapps.get(position);
        holder.date.setText(loans.getDates());
        holder.amount.setText(loans.getLoan());
        holder.time.setText(loans.getTime()+"yrs");
        status=loans.getStatus();
        if (status.equals("approved")){
            holder.status.setText("Approved");
            holder.status.setTextColor(Color.GREEN);
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(mcontext, repayment.class);
                    String loanid=loans.getId1();
                    intent.putExtra("loanid",loanid);
                    String stat="Approved";
                    intent.putExtra("status",stat);
                    mcontext.startActivity(intent);
                }
            });
            return;
        }
        if (status.equals("Denied")){
            holder.status.setText("Denied");
            holder.status.setTextColor(Color.RED);
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(mcontext, repayment.class);
                    String loanid=loans.getId1();
                    intent.putExtra("loanid",loanid);
                    String stat="Denied";
                    intent.putExtra("status",stat);
                    mcontext.startActivity(intent);
                }
            });
            return;
        }

    }

    @Override
    public int getItemCount() {
        return lapps.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView date,amount,time,status;
        public ViewHolder(View view) {
            super(view);
            date=view.findViewById(R.id.date);
            amount=view.findViewById(R.id.amount);
            time=view.findViewById(R.id.time);
            status=view.findViewById(R.id.status);
        }
    }
}
