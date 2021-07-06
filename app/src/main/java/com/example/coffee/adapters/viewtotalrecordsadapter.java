package com.example.coffee.adapters;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.coffee.R;
import com.example.coffee.adminviewuserrecord;
import com.example.coffee.getrecords;
import com.example.coffee.registerClass;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class viewtotalrecordsadapter extends RecyclerView.Adapter<viewtotalrecordsadapter.ViewHolder> {
    private Context context;
    private List<registerClass> farmers;
    String g1,g2,g3;
    public viewtotalrecordsadapter(Context context, List<registerClass> farmers){
        this.context=context;
        this.farmers=farmers;
        this.g1=g1;
        this.g2=g2;
        this.g3=g3;
    }
    @NonNull
    @Override
    public viewtotalrecordsadapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.activity_viewtotalrecordsadapter,parent,false);

        return new viewtotalrecordsadapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final viewtotalrecordsadapter.ViewHolder holder, int position) {
        final registerClass users=farmers.get(position);
        holder.name.setText(users.getName());
        final String userid=users.getId();
        holder.id.setText(users.getCid());
        DatabaseReference reference= FirebaseDatabase.getInstance().getReference("Records").child(users.getId());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Integer total1=0;
                Integer total2=0;
                Integer total3=0;
                for (DataSnapshot dataSnapshot:snapshot.getChildren()) {
                    getrecords rec = dataSnapshot.getValue(getrecords.class);

                    Integer total = Integer.valueOf(rec.getGrade1());
                    Integer totalgr2 = Integer.valueOf(rec.getGrade2());
                    Integer totalgr3 = Integer.valueOf(rec.getMbuni());

                    total1 = total1 + total;
                    total2 = total2 + totalgr2;
                    total3 = total3 + totalgr3;
                }
                holder.g1.setText(String.valueOf(total1)+"kgs");
                holder.g2.setText(String.valueOf(total2)+"kgs");
                holder.g3.setText(String.valueOf(total3)+"kgs");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context, adminviewuserrecord.class);
                intent.putExtra("userid",userid);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return farmers.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView name,id,g1,g2,g3;
        public ViewHolder(View view) {
            super(view);
            name=view.findViewById(R.id.farmer);
            id=view.findViewById(R.id.idcoffee);
            g1=view.findViewById(R.id.grade11);
            g2=view.findViewById(R.id.grade12);
            g3=view.findViewById(R.id.grade13);
        }
    }
}
