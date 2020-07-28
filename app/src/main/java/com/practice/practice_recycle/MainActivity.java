package com.practice.practice_recycle;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private Button ListBtn1, ListBtn2, CamBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ListBtn1 = findViewById(R.id.list1_btn);
        ListBtn2 = findViewById(R.id.list2_btn);
        CamBtn = findViewById(R.id.cam_btn);

        ListBtn1.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), FirstList.class);
                startActivity(intent);
            }
        });

        ListBtn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), SecondList.class);
                startActivity(intent);
            }
        });

        CamBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), CamAndGal.class);
                startActivity(intent);
            }
        });
    }
}