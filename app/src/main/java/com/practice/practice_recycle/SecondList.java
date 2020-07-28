package com.practice.practice_recycle;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class SecondList extends AppCompatActivity {

    private ArrayList<NoteList> mArrayList;
    private int count = 0;
    private Button addButton;
    private RecyclerView recyclerView;
    private MyAdapter2 mAdapter;
    private LinearLayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second_list);

        // 리스트 생성자
        mArrayList = new ArrayList<>();

        // recyclerveiw 생성
        recyclerView = findViewById(R.id.recyclerview2);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);

        mAdapter = new MyAdapter2(mArrayList);
        recyclerView.setAdapter(mAdapter);

        // 리스트 추가 버튼
        addButton = (Button) findViewById(R.id.addBtn2);
        addButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(SecondList.this);
                View view = LayoutInflater.from(SecondList.this).inflate(R.layout.activity_dialog_for2, null, false);
                builder.setView(view);

                final EditText editRowName = view.findViewById(R.id.name_dialog);
                final EditText editRowNum = view.findViewById(R.id.num_dialog);
                final Button editButton = view.findViewById(R.id.insertBtn);
                editButton.setText("데이터 삽입");

                final AlertDialog dialog = builder.create();

                editButton.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View v) {
                        String text =  editRowName.getText().toString();
                        String num = editRowNum.getText().toString();

                        NoteList list = new NoteList(text, num);
                        //mArrayList.add(0, list);     첫번째 줄에 삽입
                        //mAdapter.notifyItemChanged(0);

                        mArrayList.add(list);
                        mAdapter.notifyDataSetChanged();

                        dialog.dismiss();
                    }
                });
                dialog.show();
            }
        });
    }
}