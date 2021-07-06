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
import com.example.coffee.getrecords;
import com.example.coffee.userrecords;
import com.google.firebase.database.DatabaseReference;

import java.util.List;

public class RecordsAdapter extends RecyclerView.Adapter<RecordsAdapter.ViewHolder> {
    private Context mcontext;
    private List<getrecords> myrecs;
    public RecordsAdapter(Context mcontext, List<getrecords> myrecs) {
        this.myrecs = myrecs;
        this.mcontext = mcontext;
    }
    @NonNull
    @Override
    public RecordsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(mcontext).inflate(R.layout.activity_records_adapter,parent,false);
        return new RecordsAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecordsAdapter.ViewHolder holder, int position) {
        final getrecords records=myrecs.get(position);

        holder.g1.setText(records.getGrade1()+" Kgs");
        holder.g2.setText(records.getGrade2()+" Kgs");
        holder.g3.setText(records.getMbuni()+" Kgs");
        holder.d1.setText(records.getDate());

    }

    @Override
    public int getItemCount() {
        return myrecs.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView g1,g2,g3,d1;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            g1=itemView.findViewById(R.id.grade1);
            g2=itemView.findViewById(R.id.grade2);
            g3=itemView.findViewById(R.id.grade3);
            d1=itemView.findViewById(R.id.date);
        }
    }
}
