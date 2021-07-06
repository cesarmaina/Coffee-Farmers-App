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

import com.example.coffee.Messages;
import com.example.coffee.R;
import com.example.coffee.getMsgs;
import com.example.coffee.registerClass;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

public class msgAdapter extends RecyclerView.Adapter<msgAdapter.ViewHolder>{
    private Context mcontext;
    private List<getMsgs> getmsgs;
    FirebaseUser firebaseUser;
    public static final int MSG_LEFT=0;
    public static final int MSG_RIGHT=1;
    public msgAdapter(Context mcontext, List<getMsgs> getmsgs) {
        this.getmsgs = getmsgs;
        this.mcontext = mcontext;
    }

    @NonNull
    @Override
    public msgAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType==MSG_RIGHT){
            View view= LayoutInflater.from(mcontext).inflate(R.layout.readmsg,parent,false);
            return new msgAdapter.ViewHolder(view);
        }
        else {
            View view= LayoutInflater.from(mcontext).inflate(R.layout.msglist,parent,false);
            return new msgAdapter.ViewHolder(view);
        }

    }

    @Override
    public void onBindViewHolder(@NonNull msgAdapter.ViewHolder holder, int position) {
        getMsgs getmsg=getmsgs.get(position);
        holder.msg.setText(getmsg.getMessage());
        //holder.profpic.setImageResource(R.drawable.account1);
    }

    @Override
    public int getItemCount() {
        return getmsgs.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public TextView msg;
        public ImageView profpic;

        public ViewHolder (View itemView){
            super(itemView);
            msg=itemView.findViewById(R.id.viewmsg);
            profpic=itemView.findViewById(R.id.usr_img);
        }
    }

    @Override
    public int getItemViewType(int position) {
        firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
        if (getmsgs.get(position).getSender().equals(firebaseUser.getUid())){
            return MSG_RIGHT;
        }
        else {
            return MSG_LEFT;
        }
    }
}
