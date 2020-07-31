package com.practice.practice_recycle;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

public class NextCamGal extends AppCompatActivity {
    TextView nextTitleView;
    ImageView nextImageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_next_cam_gal);

        nextTitleView = findViewById(R.id.nextTextview);
        nextImageView = findViewById(R.id.nextImageview);

        Bundle extras = getIntent().getExtras();
        String nowText = extras.getString("text");
        byte[] nowImage = getIntent().getByteArrayExtra("image");
        Bitmap bitmap = BitmapFactory.decodeByteArray(nowImage,0,nowImage.length);

        nextTitleView.setText(nowText);
        nextImageView.setImageBitmap(bitmap);
    }
}