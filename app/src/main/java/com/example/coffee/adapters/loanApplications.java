package com.example.coffee.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.example.coffee.R;
import com.example.coffee.registerClass;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class loanApplications extends RecyclerView.Adapter<loanApplications.ViewHolder>{
    private Context mcontext;
    private List<loandbhelper> lapps;
    String firebaseUser;
    long maxid;
    Button approve,deny,check;
    public loanApplications(Context mcontext, List<loandbhelper> lapps) {
        this.lapps = lapps;
        this.mcontext = mcontext;
    }
    long id;
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(mcontext).inflate(R.layout.activity_loanapplicationsadapter,parent,false);
        check=view.findViewById(R.id.check);
        return new loanApplications.ViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        final loandbhelper loans=lapps.get(position);
        final String mail=loans.getMail();
        final String tp=loans.getLoan();
        final String tim=loans.getTime();
        final String lid=loans.getId1();
        holder.email.setText(loans.getMail());
        holder.amount.setText(loans.getLoan());
        check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(mcontext,ApproveOrDeny.class);
                intent.putExtra("mail",mail);
                intent.putExtra("totalpay",tp);
                intent.putExtra("time",tim);
                intent.putExtra("loanid",lid);
                mcontext.startActivity(intent);

            }
        });
    }
    @Override
    public int getItemCount() {
        return lapps.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder{
        public TextView email;
        public TextView amount;

        public ViewHolder (View itemView){
            super(itemView);
            email=itemView.findViewById(R.id.loanmail);
            amount=itemView.findViewById(R.id.loanamount);
        }
    }

}

