package com.practice.practice_recycle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {
    private ArrayList<NoteList> mList;
    public static class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView RowName;
        public TextView RowNumber;

        public MyViewHolder(View v) {
            super(v);
            RowName = v.findViewById(R.id.row_name);
            RowNumber = v.findViewById(R.id.row_num);
        }
    }

    public MyAdapter(ArrayList<NoteList> myDataset) {
        mList = myDataset;
    }

    @Override
    public MyAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_row, parent, false);
        MyViewHolder vh = new MyViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.RowName.setText(mList.get(position).getName());
        holder.RowNumber.setText(mList.get(position).getNumber());

    }

    @Override
    public int getItemCount() {
        return (null != mList ? mList.size() : 0);
    }
}

