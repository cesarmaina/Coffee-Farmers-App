package com.example.coffee.adapters;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.coffee.Lipa;
import com.example.coffee.R;
import com.example.coffee.registerClass;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class viewsalesadapter extends RecyclerView.Adapter<viewsalesadapter.ViewHolder> {
    private Context mcontext;
    private List<Lipa> goods;
    public viewsalesadapter(Context mcontext, List<Lipa> goods) {
        this.goods = goods;
        this.mcontext = mcontext;
    }
    @NonNull
    @Override
    public viewsalesadapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(mcontext).inflate(R.layout.activity_viewsalesadapter,parent,false);
        return new viewsalesadapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final viewsalesadapter.ViewHolder holder, int position) {
        final Lipa items=goods.get(position);
        holder.receipt1.setText(items.getReceipt());
        holder.cost1.setText(items.getCost());
        holder.bags1.setText(items.getCount());
        DatabaseReference reference= FirebaseDatabase.getInstance().getReference("Users");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot:snapshot.getChildren()){
                    registerClass user=dataSnapshot.getValue(registerClass.class);
                    if (user.getId().equals(items.getId())){
                        holder.buyer1.setText(user.getName());
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        DatabaseReference ref=FirebaseDatabase.getInstance().getReference("Product");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot snapshot1:snapshot.getChildren())
                {
                    if (items.getItemid().equals(snapshot1.getKey()))
                    {
                        Lipa item=snapshot1.getValue(Lipa.class);
                        holder.item1.setText(item.getName());
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return goods.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView receipt1,buyer1,item1,bags1,cost1;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            receipt1=itemView.findViewById(R.id.receipt);
            buyer1=itemView.findViewById(R.id.buyer);
            item1=itemView.findViewById(R.id.item);
            bags1=itemView.findViewById(R.id.bags);
            cost1=itemView.findViewById(R.id.cost);
        }
    }
}
