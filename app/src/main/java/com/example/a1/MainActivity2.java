package com.example.a1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.os.Bundle;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.ListResult;
import com.google.firebase.storage.StorageReference;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class MainActivity2 extends AppCompatActivity {

    Bitmap bitmap;
    ImageView viewOne;
    ImageView viewTwo;
    ImageView viewThree;
    ImageView viewFour;
    TextView tViewOne;
    TextView tViewTwo;
    TextView tViewThree;
    TextView tViewFour;
    Spinner spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        viewOne = findViewById(R.id.imgViewOne);
        viewTwo = findViewById(R.id.imgViewTwo);
        viewThree = findViewById(R.id.imgViewThree);
        viewFour = findViewById(R.id.imgViewFour);
        tViewOne = findViewById(R.id.viewOneCaption);
        tViewTwo = findViewById(R.id.viewTwoCaption);
        tViewThree = findViewById(R.id.viewThreeCaption);
        tViewFour = findViewById(R.id.viewFourCaption);
        Bitmap bitmap;
        spinner = (Spinner)findViewById(R.id.spinner);

        StorageReference listRef = FirebaseStorage.getInstance().getReference("/");

        listRef.listAll()
                .addOnSuccessListener(new OnSuccessListener<ListResult>() {
                    @Override
                    public void onSuccess(ListResult listResult) {

//                        Log.d(TAG, listRef.getName());
                        int listCounter = 0;
                        for (StorageReference prefix : listResult.getPrefixes()) {
                            Log.e("LIST NAME: ", prefix.getName());
                            listCounter += 1;
                            // This will give you a folder name
                            // You may call listAll() recursively on them.
                        }
                        String[] paths = new String[listCounter];
                        int listCounterTwo = 0;
                        for (StorageReference prefix : listResult.getPrefixes()) {
                            paths[listCounterTwo] = prefix.getName();
                            listCounterTwo += 1;
                            // This will give you a folder name
                            // You may call listAll() recursively on them.
                        }
                        Log.e("PATHSCOUNT", String.valueOf(listCounter));
//                        String maxItem = "";
//                        for (String item: paths){
//                            maxItem += item;
//                            maxItem += " ";
//                        }
//                        textView.setText(maxItem);
                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(MainActivity2.this,
                                android.R.layout.simple_spinner_item,paths);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spinner.setAdapter(adapter);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Uh-oh, an error occurred!
                    }
                });
//        viewOne.setImageResource(R.drawable.diagram);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                Log.e("CLEAR","CLEAR");
                viewOne.setImageResource(android.R.color.transparent);
                viewTwo.setImageResource(android.R.color.transparent);
                viewThree.setImageResource(android.R.color.transparent);
                viewFour.setImageResource(android.R.color.transparent);
                tViewOne.setText("");
                tViewTwo.setText("");
                tViewThree.setText("");
                tViewFour.setText("");
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    public void returnWO(View view) {
        finish();
    }

    public void GO(View view) {
        int testValue = 55;
        String spinnerText = spinner.getSelectedItem().toString();
        StorageReference listRef = FirebaseStorage.getInstance().getReference("/"+spinnerText);

        ArrayList<String> links = new ArrayList<String>();

        listRef.listAll()
                .addOnSuccessListener(new OnSuccessListener<ListResult>() {
                    @Override
                    public void onSuccess(ListResult listResult) {

                        Log.e("IMPORTANT", listRef.getName());
                        for (StorageReference prefix : listResult.getPrefixes()) {
                            Log.e("IMPORTANT", prefix.getName());
                            // This will give you a folder name
                            // You may call listAll() recursively on them.
                        }
                        int storageCounter = 1;
                        for (StorageReference item : listResult.getItems()) {
                            Log.e("BINGBONG: ",item.getName());
                            Log.e("I MADE ITT HERE!!!!!", "I MADE ITT HERE!!!!!");
                            item.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>()
                            {
                                @Override
                                public void onSuccess(Uri downloadUrl)
                                {
                                    Log.e("UH",downloadUrl.toString());
                                    links.add(downloadUrl.toString());
                                }
                            });
                            Log.e("AND HERE!!!!!!!!!!!!!!!!!", "AND HERE!!!!!!!!!!!!!!!!!");
                            final long ONE_MEGABYTE = 1024 * 1024;
                            int finalStorageCounter = storageCounter;
                            item.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                                @Override
                                public void onSuccess(byte[] bytes) {
                                    Bitmap bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                                    if (finalStorageCounter == 1){
                                        viewOne.setImageBitmap(bmp);
                                        tViewOne.setText(item.getName());
                                    }
                                    else if (finalStorageCounter == 2){
                                        viewTwo.setImageBitmap(bmp);
                                        tViewTwo.setText(item.getName());
                                    }
                                    else if (finalStorageCounter == 3){
                                        viewThree.setImageBitmap(bmp);
                                        tViewThree.setText(item.getName());
                                    }
                                    else if (finalStorageCounter == 4){
                                        viewThree.setImageBitmap(bmp);
                                        tViewFour.setText(item.getName());
                                    }
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception exception) {
                                    Toast.makeText(getApplicationContext(), "No Such file or Path found!!", Toast.LENGTH_LONG).show();
                                }
                            });
                            storageCounter += 1;
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Uh-oh, an error occurred!
                    }
                });

//        StorageReference storageERef = FirebaseStorage.getInstance().getReference("/Bobby/");
//        StorageReference dateRef = storageERef.child("/Bobby Test"+".jpeg");
//        dateRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>()
//        {
//            @Override
//            public void onSuccess(Uri downloadUrl)
//            {
//                Log.e("LOGGERBOGGER",downloadUrl.toString());
//            }
//        });

    }

    public String createImageFromBitmap(Bitmap bitmap) {
        String fileName = "defaultImgThree";//no .png or .jpg needed
        try {
            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
            FileOutputStream fo = openFileOutput(fileName, Context.MODE_PRIVATE);
            fo.write(bytes.toByteArray());
            // remember close file output
            fo.close();
        } catch (Exception e) {
            e.printStackTrace();
            fileName = null;
        }
        return fileName;
    }

    public void viewOneClicked(View view) {
//        viewOne.buildDrawingCache();
//        Bitmap bitmap = viewOne.getDrawingCache();
//
//        ByteArrayOutputStream stream = new ByteArrayOutputStream();
//        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
//        byte[] byteArray = stream.toByteArray();
//
//        Intent in1 = new Intent(this, MainActivity.class);
//        in1.putExtra("image",byteArray);
//        startActivity(in1);
//        Log.e("IMPORTANT MESSAGE","VIEW ONE CLICKED");

        Intent intent = new Intent(this, MainActivity.class);
        createImageFromBitmap(bitmap);
        startActivity(intent);
    }


}