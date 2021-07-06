package com.example.coffee.adapters;

import android.content.Context;
import android.graphics.Color;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.coffee.R;

import java.util.List;

public class loanhistory extends RecyclerView.Adapter<loanhistory.ViewHolder> {
    private Context mcontext;
    private List<loandbhelper> lapps;
    public loanhistory(Context mcontext, List<loandbhelper> lapps) {
        this.lapps = lapps;
        this.mcontext = mcontext;
    }
    @NonNull
    @Override
    public loanhistory.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(mcontext).inflate(R.layout.fragment_loanhistory,parent,false);
        return new loanhistory.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull loanhistory.ViewHolder holder, int position) { final loandbhelper loans=lapps.get(position);
        holder.date.setText(loans.getDates());
        holder.amount.setText(loans.getLoan());
        holder.time.setText(loans.getTime()+"yrs");
        holder.status.setText("Pending");
        holder.status.setTextColor(Color.BLUE);

    }

    @Override
    public int getItemCount() {
        return lapps.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView date,amount,time,status;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            date=itemView.findViewById(R.id.date);
            amount=itemView.findViewById(R.id.amount);
            time=itemView.findViewById(R.id.time);
            status=itemView.findViewById(R.id.status);
        }
    }
}
