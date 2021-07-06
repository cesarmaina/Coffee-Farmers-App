package com.example.coffee.adapters;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.coffee.Lipa;
import com.example.coffee.R;
import com.example.coffee.mpesa;
import com.example.coffee.registerClass;
import com.example.coffee.updatesale;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class adminbuyadapter extends RecyclerView.Adapter<adminbuyadapter.ViewHolder> {
    private Context mcontext;
    Integer counts;
    private List<sellhelper> items;
    public adminbuyadapter(Context mcontext, List<sellhelper> items) {
        this.items = items;
        this.mcontext = mcontext;
    }
    @NonNull
    @Override
    public adminbuyadapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(mcontext).inflate(R.layout.activity_adminbuyadapter,parent,false);
        return new adminbuyadapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final adminbuyadapter.ViewHolder holder, final int position) {
        final sellhelper item=items.get(position);
        final FirebaseUser user= FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference reference= FirebaseDatabase.getInstance().getReference("Transaction");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Integer count=0;
                for(DataSnapshot dataSnapshot:snapshot.getChildren()){
                    Lipa data=dataSnapshot.getValue(Lipa.class);
                    final sellhelper item=items.get(position);
                    String itemid=item.getId();
                    if (data.getItemid().equals(itemid)){
                        String bcount=data.getCount();
                        Integer bcounts=Integer.valueOf(bcount);
                        count=count+bcounts;

                    }
                    String scount=item.getCount();
                    counts=Integer.valueOf(scount)-Integer.valueOf(count);
                    if (counts==0){
                        items.remove(holder.getAdapterPosition());
                        notifyItemRemoved(holder.getAdapterPosition());
                        notifyItemRangeChanged(holder.getAdapterPosition(),items.size());
                    }
                    holder.count.setText(String.valueOf(counts)+" remaining");
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        holder.name.setText(item.getName());
        holder.cost.setText(item.getCost());
        holder.quantity.setText(item.getQuantity());
        //minus bought bags
        //get remaining bags
        Glide.with(mcontext).load(item.getMuri()).into(holder.img);
        //BUY BUTTON
        /*holder.buy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final sellhelper item=items.get(position);
                DatabaseReference reference= FirebaseDatabase.getInstance().getReference("Users");
                reference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        final String userid,name,quantity,cost,itemid,count;
                        String phone;
                        userid= user.getUid();
                        itemid=item.getId();
                        name=item.getName();
                        quantity=item.getQuantity();
                        cost=item.getCost();
                        count=String.valueOf(counts);
                        for (DataSnapshot dataSnapshot:snapshot.getChildren()) {
                            registerClass users = dataSnapshot.getValue(registerClass.class);
                            if (userid.equals(users.getId())){
                                phone=users.getPhone();
                                Intent intent=new Intent(mcontext, mpesa.class);
                                intent.putExtra("phone",phone);
                                intent.putExtra("userid",userid);
                                intent.putExtra("itemid",itemid);
                                intent.putExtra("name",name);
                                intent.putExtra("quantity",quantity);
                                intent.putExtra("cost",cost);
                                intent.putExtra("count",count);

                                mcontext.startActivity(intent);
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });*/
        holder.update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String itemid=item.getId();
                Intent intent=new Intent(mcontext, updatesale.class);
                intent.putExtra("itemid",itemid);
                mcontext.startActivity(intent);
            }
        });
       /* holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference reference= FirebaseDatabase.getInstance().getReference("Product").child(item.getId());
                reference.removeValue();
            }
        });*/
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView name,cost,quantity,count;
        Button update;
        public ImageView img;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name=itemView.findViewById(R.id.productname);
            cost=itemView.findViewById(R.id.productcost);
            quantity=itemView.findViewById(R.id.productquantity);
            img=itemView.findViewById(R.id.sellimg);
            //delete=itemView.findViewById(R.id.btndelete);
            update=itemView.findViewById(R.id.btnupdate);
            count=itemView.findViewById(R.id.productcount);
        }
    }
}
