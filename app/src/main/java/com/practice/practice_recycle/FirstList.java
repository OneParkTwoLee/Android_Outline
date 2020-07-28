package com.practice.practice_recycle;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class FirstList extends AppCompatActivity {

    private ArrayList<NoteList> mArrayList;
    private int count = 0;
    private Button addButton;
    private RecyclerView recyclerView;
    private MyAdapter mAdapter;
    private LinearLayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_list);

        // 리스트 생성자
        mArrayList = new ArrayList<>();

        // recyclerveiw 생성
        recyclerView = findViewById(R.id.recyclerview);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);

        mAdapter = new MyAdapter(mArrayList);
        recyclerView.setAdapter(mAdapter);

        // 리스트 추가 버튼
        addButton = (Button) findViewById(R.id.addBtn);
        addButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                count++;
                NoteList list = new NoteList("리스트 "+count, count+"");
                mArrayList.add(list);
                mAdapter.notifyDataSetChanged();
            }
        });
    }
}