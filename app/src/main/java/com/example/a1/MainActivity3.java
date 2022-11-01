package com.example.a1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.ListResult;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.checkerframework.checker.nullness.qual.NonNull;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

public class MainActivity3 extends AppCompatActivity {

    Bitmap bitmap;
    ImageView imageViewTransfer;
    TextView textView;
    EditText editTextA;
    EditText editTextB;
    EditText editTextC;
    FirebaseStorage storage;
//    Spinner spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);
        textView = findViewById(R.id.textViewTitle);
        imageViewTransfer = findViewById(R.id.imgViewTransfer);
        editTextA = findViewById(R.id.editTextA);
        editTextB = findViewById(R.id.editTextB);
        editTextC = findViewById(R.id.editTextC);

        storage = FirebaseStorage.getInstance();
        StorageReference listRef = FirebaseStorage.getInstance().getReference("/");

//        spinner = (Spinner)findViewById(R.id.spinner);
//        String spinnerSelected = spinner.getSelectedItem().toString();
//        spinner.setOnItemSelectedListener((AdapterView.OnItemSelectedListener) this);

        try {
            bitmap = BitmapFactory.decodeStream(getApplicationContext().openFileInput("defaultImg"));
//            textView.setText("Yes");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
//            textView.setText("No");
        }
        imageViewTransfer.setImageBitmap(bitmap);

//        listRef.listAll()
//                .addOnSuccessListener(new OnSuccessListener<ListResult>() {
//                    @Override
//                    public void onSuccess(ListResult listResult) {
//
////                       Log.d(TAG, listRef.getName());
//                        int listCounter = 0;
//                        for (StorageReference prefix : listResult.getPrefixes()) {
//                            Log.e("LIST NAME: ", prefix.getName());
//                            listCounter += 1;
//                            // This will give you a folder name
//                            // You may call listAll() recursively on them.
//                        }
//                        String[] paths = new String[listCounter];
//                        int listCounterTwo = 0;
//                        for (StorageReference prefix : listResult.getPrefixes()) {
//                            paths[listCounterTwo] = prefix.getName();
//                            listCounterTwo += 1;
//                            // This will give you a folder name
//                            // You may call listAll() recursively on them.
//                        }
//                        Log.e("PATHSCOUNT", String.valueOf(listCounter));
////                        String maxItem = "";
////                        for (String item: paths){
////                            maxItem += item;
////                            maxItem += " ";
////                        }
////                        textView.setText(maxItem);
//                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(MainActivity3.this,
//                                android.R.layout.simple_spinner_item,paths);
//                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
////                        spinner.setAdapter(adapter);
//                    }
//                })
//                .addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@androidx.annotation.NonNull Exception e) {
//                        // Uh-oh, an error occurred!
//                    }
//                });
    }

    public void backBtn(View view){
        finish();
    }

    public void accessBtn(View view){
        Intent intent = new Intent(this, MainActivity2.class);
        startActivity(intent);
    }

    public void retrieveBtn(View view){

        String fileText = editTextC.getText().toString();

        try {
            bitmap = BitmapFactory.decodeStream(getApplicationContext().openFileInput(fileText));
//            textView.setText("YesE");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
//            textView.setText("NoE");
        }
        imageViewTransfer.setImageBitmap(bitmap);
    }

//    public void saveBtn(View view) {
//        String text = spinner.getSelectedItem().toString();
//        String fileText = editTextB.getText().toString();
//        if (!(editTextA.getText().toString().equals("Patient Name"))){
//            text = editTextA.getText().toString();
//        }
////        textView.setText("Saving to Patient: "+ text);
//        StorageReference storageRef = storage.getReference();
//        StorageReference mountainsRef = storageRef.child(text+"/"+fileText);
//        ByteArrayOutputStream baos = new ByteArrayOutputStream();
//        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
//        byte[] data = baos.toByteArray();
//        UploadTask uploadTask = mountainsRef.putBytes(data);
//        uploadTask.addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull Exception exception) {
//                // Handle unsuccessful uploads
////                textView.setText("Upload Failed - Please Try Again");
//            }
//        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//            @Override
//            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, etc.
//                // ...
////                textView.setText("Upload Complete");
//            }
//        });



}