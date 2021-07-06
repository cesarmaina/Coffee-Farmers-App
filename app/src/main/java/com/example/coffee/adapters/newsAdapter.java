package com.example.coffee.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.coffee.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

public class newsAdapter extends RecyclerView.Adapter<newsAdapter.ViewHolder>{
    private Context mcontext;
    private List<com.example.coffee.newsAdapter> musers;
    FirebaseUser fuser;


    public newsAdapter(Context mcontext, List<com.example.coffee.newsAdapter> musers) {
        this.musers = musers;
        this.mcontext = mcontext;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(mcontext).inflate(R.layout.news_view,parent,false);
        return new newsAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final com.example.coffee.newsAdapter user=musers.get(position);
        fuser= FirebaseAuth.getInstance().getCurrentUser();
        //display users
        holder.username.setText(user.getText());
        Glide.with(mcontext).load(user.getImage()).into(holder.profpic);
        fuser= FirebaseAuth.getInstance().getCurrentUser();
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
            username=itemView.findViewById(R.id.news_about);
            profpic=itemView.findViewById(R.id.news_img);
        }
    }

}
