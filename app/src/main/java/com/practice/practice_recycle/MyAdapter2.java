package com.practice.practice_recycle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MyAdapter2 extends RecyclerView.Adapter<MyAdapter2.MyViewHolder2> {
    private ArrayList<NoteList> mList;
    public static class MyViewHolder2 extends RecyclerView.ViewHolder {
        public TextView RowName;
        public TextView RowNumber;
        public Button RowDeleteBtn;

        public MyViewHolder2(View v) {
            super(v);
            RowName = v.findViewById(R.id.row_name2);
            RowNumber = v.findViewById(R.id.row_num2);
            RowDeleteBtn = v.findViewById(R.id.deleteBtn);
        }
    }

    public MyAdapter2(ArrayList<NoteList> myDataset) {
        mList = myDataset;
    }

    @Override
    public MyAdapter2.MyViewHolder2 onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_row2, parent, false);
        MyViewHolder2 vh = new MyViewHolder2(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder2 holder, final int position) {
        holder.RowName.setText(mList.get(position).getName());
        holder.RowNumber.setText(mList.get(position).getNumber());
        holder.RowDeleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mList.remove(position);
                notifyItemRemoved(position);
                notifyItemRangeChanged(position, mList.size());
            }
        });
    }

    @Override
    public int getItemCount() {
        return (null != mList ? mList.size() : 0);
    }
}
