package com.practice.practice_recycle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageDecoder;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CamAndGal extends AppCompatActivity {

    static final int REQUEST_IMAGE_CAPTURE = 1; // 카메라 미리보기 이미지(= 저장하지 않은 사진)
    static final int REQUEST_TAKE_PHOTO = 1;        // 촬영 후 원본 이미지(= 저장된 사진)
    private static final int PICK_FROM_ALBUM = 2;   // 갤러리 이미지
    String currentPhotoPath;
    String imageFileName;

    private Button DialogBtn, MoveBtn;
    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cam_and_gal);

        // 화면 연결
        DialogBtn = findViewById(R.id.takePicBtn);
        imageView = findViewById(R.id.imageView);
        MoveBtn = findViewById(R.id.moveDataBtn);

        // 6.0 마쉬멜로우 이상일 경우에 권한 체크 후 권한 요청
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            if(checkSelfPermission(Manifest.permission.CAMERA)== PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)==PackageManager.PERMISSION_GRANTED){
                Log.d("Permission66","권한 설정 완료");
            }else{
                Log.d("Permission66", "권한 설정 요청");
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
            }
        }

        DialogBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(CamAndGal.this);
                View view = LayoutInflater.from(CamAndGal.this).inflate(R.layout.activity_dialog_cam, null, false);
                builder.setView(view);

                final Button LinkCameraBtn = view.findViewById(R.id.cameraBtn);
                final Button LinkGalleryBtn = view.findViewById(R.id.galleryBtn);
                final AlertDialog dialog = builder.create();

                // 다이얼로그에서 카메라 선택
                LinkCameraBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dispatchTakePictureIntent();
                        dialog.dismiss();
                    }
                });

                // 다이얼로그에서 갤러리 선택
                LinkGalleryBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        selectFromGalleryIntent();
                        dialog.dismiss();
                    }
                });

             dialog.show();
            }
        });

        MoveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                moveToNextActivity();
            }
        });

    }

    // 카메라 intent 연결하는 함수
    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
            }
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        "com.practice.practice_recycle.fileprovider", photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
            }
        }
    }


    // 화면에 이미지 가져오는 함수
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            switch (requestCode) {
                // 카메라로 찍은 이미지 불러오는 경우
                case REQUEST_TAKE_PHOTO: {
                    if (resultCode == RESULT_OK) {
                        File file = new File(currentPhotoPath);
                        Bitmap bitmap = null;
                        Uri photoUri = null;

                        photoUri = Uri.fromFile(file);
                        bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), photoUri);

                        if (bitmap != null) {
                            ExifInterface ei = new ExifInterface(currentPhotoPath);
                            int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_UNDEFINED);
                            int exifDegree = exifOrientationToDegrees(orientation);
                            Log.d("각도", exifDegree+"");

                            Bitmap rotatedBitmap = rotateImage(bitmap, exifDegree);
                            imageView.setImageBitmap(rotatedBitmap);
                            saveImage(rotatedBitmap);   // 갤러리에 저장하는 함수
                        }
                    }
                    break;
                }
                // 갤러리에서 이미지 불러오는 경우
                case PICK_FROM_ALBUM :{
                    if(resultCode == RESULT_OK){
                        Uri ImageUri = data.getData();
                        Cursor cursor = null;
                        try{
                            String[] proj = {MediaStore.Images.Media.DATA};
                            cursor = getContentResolver().query(ImageUri, proj, null, null, null);

                            if(cursor != null){
                                int index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                                cursor.moveToFirst();
                                currentPhotoPath = cursor.getString(index);     // 외부 저장소 경로를 가져옴
                                File tempFile = new File(currentPhotoPath);
                                BitmapFactory.Options options = new BitmapFactory.Options();
                                Bitmap bitmap = BitmapFactory.decodeFile(tempFile.getAbsolutePath(), options);

                                if(bitmap != null){
                                    ExifInterface ei = new ExifInterface(tempFile.getAbsolutePath());
                                    int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_UNDEFINED);
                                    int exifDegree = exifOrientationToDegrees(orientation);

                                    Bitmap rotatedBitmap = rotateImage(bitmap, exifDegree);
                                    imageView.setImageBitmap(rotatedBitmap);
                                }
                                cursor.close();
                            }else {
                                Log.d("갤러리이미지로딩", "NULL");
                            }


                            /*
                            InputStream in = getContentResolver().openInputStream(data.getData());
                            //Log.d("인풋데이터", data.getData().toString()+"");
                            //Log.d("인풋스트림", in.toString()+"");
                            Bitmap bitmap = BitmapFactory.decodeStream(in);
                            in.close();
                            imageView.setImageBitmap(bitmap);*/
                        } catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                }
            }

        } catch (Exception error) {
            error.printStackTrace();
        }
    }

    // 촬영 이미지를 파일로 저장하는 함수
    private File createImageFile() throws IOException {
        // 이미지 파일 이름
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        imageFileName =  "test_" +timeStamp ;

        // 이미지가 저장될 폴더 이름(outline) & 빈 폴더 생성
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);  // 외부 앱 공간 저장소
        // File storageDir = Environment.getExternalStorageDirectory(); // 외부 저장소 (공용공간)
        // File storageDir = new File(Environment.getExternalStorageDirectory() + "/outline/"); -> 오류남
        File image = File.createTempFile(imageFileName, ".jpg", storageDir);
        Log.d("파일", image.toString());

        currentPhotoPath = image.getAbsolutePath();
        return image;
    }

    // 이미지 회전하는 함수
    public static Bitmap rotateImage(Bitmap source, float angle) {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        Log.d("Rotate", angle+"");
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, true);
    }

    public int exifOrientationToDegrees(int exifOrientation){
        if(exifOrientation == ExifInterface.ORIENTATION_ROTATE_90){
            return 90;
        }
        else if(exifOrientation == ExifInterface.ORIENTATION_ROTATE_180){
            return 180;
        }
        else if(exifOrientation == ExifInterface.ORIENTATION_ROTATE_270){
            return 270;
        }
        else{
            return 0;
        }
    }

    // 갤러리에 저장하는 함수
    private void saveImage(Bitmap finalBitmap){
        String imageFileForGalName =  imageFileName + ".jpg";
        File myDir = new File(Environment.getExternalStorageDirectory().toString());
        myDir.mkdirs();
        File file = new File(myDir, imageFileForGalName);
        try{
            FileOutputStream out = new FileOutputStream(file);
            finalBitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
            this.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(file)));
            out.flush();
            out.close();;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /* 갤러리 연결하기 */

    // 갤러리 intent 연결하는 함수
    private void selectFromGalleryIntent() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
        startActivityForResult(intent, PICK_FROM_ALBUM);
    }

    // 갤러리에서 선택한 이미지의 절대 경로로 이미지 넣기
    private void setImageFromGallery(File file){
        BitmapFactory.Options options = new BitmapFactory.Options();
        Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath(), options);
        imageView.setImageBitmap(bitmap);
    }



    /* 다음 화면으로 이미지, 텍스트 이동하기 */
    // Extras는 한번에 100KB까지만 이동시킬 수 있는 제한이 있음
    private void moveToNextActivity(){
        // 이미지 뷰의 src
        String nextTextView = imageView.getResources().toString();
        // 이미지 뷰의 image
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        Bitmap bitmap = ((BitmapDrawable)imageView.getDrawable()).getBitmap();
        float scale = (float) (1024/(float)bitmap.getWidth());
        int image_w = (int) (bitmap.getWidth() * scale);
        int image_h = (int) (bitmap.getHeight() * scale);
        Bitmap resize = Bitmap.createScaledBitmap(bitmap, image_w, image_h, true);
        resize.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        byte[] byteArray = stream.toByteArray();

        Intent intent = new Intent(CamAndGal.this, NextCamGal.class);
        intent.putExtra("text", nextTextView);
        intent.putExtra("image", byteArray);

        startActivity(intent);
    }




}