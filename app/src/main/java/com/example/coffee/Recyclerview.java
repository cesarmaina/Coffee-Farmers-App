package com.example.coffee;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class Recyclerview {
    private Context mcontext;
    private RecordAdapter mRecordAdapter;
    public void setConfig(RecyclerView recyclerView, Context context, List<getrecords> userrecords, List<String>keys){
        mcontext=context;
        mRecordAdapter=new RecordAdapter(userrecords,keys);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setAdapter(mRecordAdapter);

    }

    class UserMsgView extends RecyclerView.ViewHolder {
        private TextView mg1,mg2,mb1,md1,mid;

        private String key;

        public UserMsgView(ViewGroup parent){
            super(LayoutInflater.from(mcontext).
                    inflate(R.layout.user_record_list,parent,false));

        mg1=(TextView)itemView.findViewById(R.id.g1);
        mg2=(TextView)itemView.findViewById(R.id.g2);
        mb1=(TextView)itemView.findViewById(R.id.m1);
        md1=(TextView)itemView.findViewById(R.id.d1);
        mid=(TextView)itemView.findViewById(R.id.idid);
        }
        public void bind(getrecords userrecords, String key){
            mg1.setText(userrecords.getGrade1());
            mg2.setText(userrecords.getGrade2());
            mb1.setText(userrecords.getMbuni());
            md1.setText(userrecords.getDate());
            mid.setText(userrecords.getUseridd());
            this.key=key;
        }
    }
    class RecordAdapter extends RecyclerView.Adapter<UserMsgView>{
        private List<getrecords> mrecordlist;
        private List<String> mkeys;

        public RecordAdapter(List<getrecords> mrecordlist, List<String> mkeys) {
            this.mrecordlist = mrecordlist;
            this.mkeys = mkeys;
        }

        @NonNull
        @Override
        public UserMsgView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new UserMsgView(parent);
        }

        @Override
        public void onBindViewHolder(@NonNull UserMsgView holder, int position) {
            holder.bind(mrecordlist.get(position),mkeys.get(position));
        }

        @Override
        public int getItemCount() {
            return mrecordlist.size();
        }
    }
}
