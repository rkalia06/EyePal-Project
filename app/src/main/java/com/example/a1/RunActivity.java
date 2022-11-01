package com.example.a1;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.ortiz.touchview.TouchImageView;

import java.io.IOException;

public class RunActivity extends AppCompatActivity {

    TouchImageView imageView;
    Bitmap mainBitmap;
    Bitmap objectDetBitmap;
    Button fromCameraRollBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        imageView = findViewById(R.id.imgViewTouch);
        fromCameraRollBtn = findViewById(R.id.NewCameraRollBtn);

    }

    public void selectImageBtn(View view) {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent, 250);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent resultData) {
        super.onActivityResult(requestCode, resultCode, resultData);

        //camera roll request
        if (requestCode == 250){
            Uri selectedImage = resultData.getData();
            imageView.setImageURI(selectedImage);
            try {
                mainBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage);
                objectDetBitmap = mainBitmap;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
