package com.example.coffee.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.coffee.Messages;
import com.example.coffee.R;
import com.example.coffee.registerClass;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

public class usersAdapter extends RecyclerView.Adapter<usersAdapter.ViewHolder>{
    private Context mcontext;
    private List<registerClass> musers;
    FirebaseUser fuser;


    public usersAdapter(Context mcontext, List<registerClass> musers) {
        this.musers = musers;
        this.mcontext = mcontext;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(mcontext).inflate(R.layout.users_view,parent,false);
        return new usersAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final registerClass user=musers.get(position);
        fuser= FirebaseAuth.getInstance().getCurrentUser();
        //display users
        holder.username.setText(user.getName());
        holder.profpic.setImageResource(R.drawable.account1);
        if (user.getImage().equals("Default")){
            holder.profpic.setImageResource(R.drawable.account1);
        }
        else {
            Glide.with(mcontext).load(user.getImage()).into(holder.profpic);
        }
        fuser= FirebaseAuth.getInstance().getCurrentUser();


        //messages
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(mcontext, Messages.class);
                intent.putExtra("UserId",user.getId());
                mcontext.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return musers.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public TextView username;
        public ImageView profpic;

        public ViewHolder (View itemView){
            super(itemView);
            username=itemView.findViewById(R.id.user_name);
            profpic=itemView.findViewById(R.id.usr_img);
        }
    }

}
