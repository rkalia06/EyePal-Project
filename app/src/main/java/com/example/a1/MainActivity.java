package com.example.a1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import org.tensorflow.lite.support.image.TensorImage;
import org.tensorflow.lite.support.label.Category;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.a1.ml.ModelLocalizedOne;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.ListResult;
import com.ortiz.touchview.TouchImageView;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;


public class MainActivity extends AppCompatActivity {

//    Button selectImage;
//    Button makePrediction;
    TouchImageView imageView;
    TextView textView;
    TextView textViewM;
    Bitmap bitmap;
//    Button cameraButton;
    FirebaseStorage storage;
//    Spinner spinner;
//    String[] pathsB = {"A", "B", "C"};
//    String[] listNames;
    EditText editTextA;
//    EditText editTextB;
    Bitmap objBitmap;
    Bitmap obDetBi;
    FloatingActionButton cameraFloat;
    FloatingActionButton cameraRollFloat;
    FloatingActionButton objectFloatingButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        imageView = findViewById(R.id.imgView);
//        selectImage = findViewById(R.id.selectImageBtn);
//        cameraButton = findViewById(R.id.cameraBtn) ;
//        makePrediction = findViewById(R.id.makePredictionBtn);
        textView = findViewById(R.id.textView);
        textViewM = findViewById(R.id.textViewM);
        editTextA = findViewById(R.id.editTextA);
//        editTextB = findViewById(R.id.editTextB);
        Bitmap bitmap;
        storage = FirebaseStorage.getInstance();
        cameraFloat = findViewById(R.id.floatingCamera);
        cameraRollFloat = findViewById(R.id.floatingRoll);
        objectFloatingButton = findViewById(R.id.floatingPredict);
//        spinner = (Spinner)findViewById(R.id.spinner);

//        spinnerSelected = spinner.getSelectedItem().toString();
//        spinner.setOnItemSelectedListener((AdapterView.OnItemSelectedListener) this);

        StorageReference listRef = FirebaseStorage.getInstance().getReference("/");

        listRef.listAll()
                .addOnSuccessListener(new OnSuccessListener<ListResult>() {
                    @Override
                    public void onSuccess(ListResult listResult) {

//                       Log.d(TAG, listRef.getName());
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
                        ArrayAdapter<String>adapter = new ArrayAdapter<String>(MainActivity.this,
                                android.R.layout.simple_spinner_item,paths);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//                        spinner.setAdapter(adapter);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Uh-oh, an error occurred!
                    }
                });

//        try {
//            Bitmap threeBitmap = BitmapFactory.decodeStream(getApplicationContext().openFileInput("defaultImgThree"));
//            textView.setText("Yes");
//            imageView.setImageBitmap(threeBitmap);
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//            textView.setText("No");
//        }
        cameraFloat.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(getApplicationContext(),R.color.teal_200)));
        cameraRollFloat.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(getApplicationContext(),R.color.teal_200)));
    }

    public void selectImageBtn(View view) {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        textView.setText("Click \"Make Prediction\" to see if the fundus image shows glaucoma or not!");
        cameraFloat.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(getApplicationContext(),R.color.strikerBlue)));
        cameraRollFloat.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(getApplicationContext(),R.color.strikerBlue)));
        objectFloatingButton.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(getApplicationContext(),R.color.teal_200)));
        textViewM.setText("");
        startActivityForResult(intent, 250);
        Log.d("TAG","SETTING BACKGROUND TINT");
    }


    public Bitmap convertToResize(Bitmap bi){
        Bitmap convertedBI = Bitmap.createScaledBitmap(bi, 640, 640, false);
        return convertedBI;
    }

    public void makePredictionBtn(View view) {
        try {
            ModelLocalizedOne model = ModelLocalizedOne.newInstance(getApplicationContext());
            // Creates inputs for reference.
            Bitmap newBI = convertToResize(bitmap);
            TensorImage image = TensorImage.fromBitmap(bitmap);
            // Runs model inference and gets result.
            ModelLocalizedOne.Outputs outputs = model.process(image);
            List<Category> probability = outputs.getProbabilityAsCategoryList();
            for (Category output: probability){
                String floatString = String.valueOf(output.getScore() * 100).substring(0,6);
                if (output.getScore() >= 0.5){
                    String messageBox = output.getLabel() + " Predicted" + "\nConfidence: " + floatString + "%";
                    textView.setText(messageBox);
                }
            }
            model.close();
            Log.d("makePredictionBtn", String.valueOf(probability));
        } catch (IOException e) {
            // TODO Handle the exception
        }
    }

//    public void objectDetectionBtn(View view) throws IOException {
//        try {
//            FinalModel model = FinalModel.newInstance(getApplicationContext());
//
//            // Creates inputs for reference.
//            TensorBuffer inputFeature0 = TensorBuffer.createFixedSize(new int[]{1, 300, 300, 3}, DataType.FLOAT32);
//            TensorImage image = TensorImage.fromBitmap(bitmap);
//            inputFeature0.loadBuffer(ByteBuffer.allocateDirect(1080000));
//
//            // Runs model inference and gets result.
//            FinalModel.Outputs outputs = model.process(inputFeature0);
//            Log.d("Outputs: ", String.valueOf(outputs));
//            TensorBuffer outputFeature0 = outputs.getOutputFeature0AsTensorBuffer();
//            TensorBuffer outputFeature1 = outputs.getOutputFeature1AsTensorBuffer();
//            TensorBuffer outputFeature2 = outputs.getOutputFeature2AsTensorBuffer();
//            TensorBuffer outputFeature3 = outputs.getOutputFeature3AsTensorBuffer();
//
////            Log.d("Buffer: ", String.valueOf(outputFeature0.getBuffer()));
//            textView.setText(String.valueOf(outputFeature0.getBuffer()));
//            // Releases model resources if no longer used.
//            float[] dataAA=outputFeature0.getFloatArray();
//            textView.setText(Arrays.toString(outputFeature0.getFloatArray()));
//
//
//
//            model.close();
//        } catch (IOException e) {
//            // TODO Handle the exception
//        }
////
//        // Initialization
////        ObjectDetector.ObjectDetectorOptions options =
////                ObjectDetector.ObjectDetectorOptions.builder()
////                        .setBaseOptions(BaseOptions.builder().useGpu().build())
////                        .setMaxResults(3)
////                        .build();
////        ObjectDetector objectDetector =
////                ObjectDetector.createFromFileAndOptions(
////                        getApplicationContext(), "model_final.tflite", options);
//
////// Run inference
////        List<Detection> results = objectDetector.detect(TensorImage.fromBitmap(bitmap));
//    }

    public void cameraBtn(View view) {
        final int REQUEST_IMAGE_CAPTURE = 1;
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        try {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        } catch (ActivityNotFoundException e) {
            textView.setText("Camera Error");
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent resultData) {
        super.onActivityResult(requestCode, resultCode, resultData);

        //camera roll request
        if (requestCode == 250){
            Uri selectedImage = resultData.getData();
            imageView.setImageURI(selectedImage);
            try {
                bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage);
                obDetBi = bitmap;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        //camera request
        if (requestCode == 1 && resultCode == RESULT_OK) {
            Bundle extras = resultData.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            bitmap = imageBitmap;
            obDetBi = bitmap;
            imageView.setImageBitmap(imageBitmap);
        }
    }

    public void storageBtn(View view){
        Intent intent = new Intent(this, MainActivity2.class);
        startActivity(intent);
    }

    public void activitySave(View view){
        Intent intent = new Intent(this, MainActivity3.class);
        createImageFromBitmap(bitmap);
        startActivity(intent);
    }

    public void activityDownload(View view) throws InterruptedException {
        String nameText = editTextA.getText().toString();
        createImageFromBitmapWithFile(bitmap, nameText);
        textView.setText("Saved " + nameText + "  !");
    }

    public void objDetInference(View view){
        objectFloatingButton.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(getApplicationContext(),R.color.strikerBlue)));
        Log.d("TAG", "message1");
        ObjD instance = new ObjD(this);
        Log.d("TAG", "message2");
        objBitmap = instance.runObjDet(obDetBi);
//        String newString = instance.fuzzypickles("ee");
//        Log.d("TAG", newString);
        Log.d("TAG", "message3");
        imageView.setImageBitmap(objBitmap);
        Log.d("TAG", "message4");
//        Float bl = instance.getBoxLeft();
//        String bls = Float.toString(bl);
//        Float br = instance.getBoxRight();
//        String brs = Float.toString(br);
//        Float bt = instance.getBoxTop();
//        String bts = Float.toString(bt);
//        Float bb = instance.getBoxBottom();
//        String bbs = Float.toString(bt);
//        Float width = Math.abs(br - bl);
//        String widthS = Float.toString(width);
//        Float height = Math.abs(bt - bb);
//        String heightS = Float.toString(height);
//        Float area = width * height;
//        String areaS = Float.toString(area);
//        Log.d("INSTANCE VAR BOX LEFT", bls);
//        Log.d("INSTANCE VAR BOX RIGHT", brs);
//        Log.d("INSTANCE VAR BOX TOP", bts);
//        Log.d("INSTANCE VAR BOX BOTTOM", bbs);
//        Log.d("INSTANCE VAR BOX WIDTH", widthS);
//        Log.d("INSTANCE VAR BOX HEIGHT", heightS);
//        Log.d("INSTANCE VAR BOX AREA", areaS);
        Float cupArea = instance.getCupArea() * 1.1F;
        Float discArea = instance.getDiscArea() * 0.8F;

        if (cupArea > discArea){
            Float temp = cupArea;
            cupArea = discArea;
            discArea = temp;
        }
        Float ratio = cupArea / (discArea);
        String truncRatio = ratio.toString().substring(0,7);
        String glaucPred = "No glaucoma predicted";

        if (ratio > 0.25){
            glaucPred = "Glaucoma predicted";
        }

        textView.setText("Cup Area: " + cupArea.toString() + "\nDisc Area: " + discArea.toString() + "\nC-D Ratio: "+truncRatio);
        textViewM.setText(glaucPred);

    }

    public void setZoomedBitmap(View view){
        imageView.invalidate();
        BitmapDrawable zDrawable = (BitmapDrawable) imageView.getDrawable();
        Bitmap zoomed = zDrawable.getBitmap();
        int w = zoomed.getWidth();
        int h = zoomed.getHeight();
//        imageView.setImageBitmap(zoomed);
        RectF rec = imageView.getZoomedRect();
        float top = rec.top;
        float left = rec.left;
        float width = rec.width();
        float height = rec.height();
        Log.d("TAG", "" + Math.round(left*w) + " " + Math.round(top*h) + " " + Math.round(width*w) + " " + Math.round(height*h));
        Bitmap zBmp = Bitmap.createBitmap(bitmap, Math.round(left*w), Math.round(top*h), Math.round(width*w), Math.round(height*h));
        imageView.resetZoom();
        imageView.setImageBitmap(zBmp);
        bitmap = zBmp;
        obDetBi = zBmp;
    }

    public String createImageFromBitmap(Bitmap bitmap) {
        String fileName = "defaultImg";//no .png or .jpg needed
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

    public String createImageFromBitmapWithFile(Bitmap bitmap, String name) {
        String fileName = name;//no .png or .jpg needed
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

    public Bitmap getBitmapFromView(View view)
    {
        Bitmap bitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        view.draw(canvas);
        return bitmap;
    }

//    public void saveBtn(View view) {
//        String text = spinner.getSelectedItem().toString();
//        String fileText = editTextB.getText().toString();
//        if (!(editTextA.getText().toString().equals("Patient Name"))){
//            text = editTextA.getText().toString();
//        }
//        textView.setText("Saving to Patient: "+ text);
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
//                textView.setText("Upload Failed - Please Try Again");
//            }
//        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//            @Override
//            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, etc.
//                // ...
//                textView.setText("Upload Complete");
//            }
//        });
//
//    }

    public void refreshBtn(View view) {
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
                        ArrayAdapter<String>adapter = new ArrayAdapter<String>(MainActivity.this,
                                android.R.layout.simple_spinner_item,paths);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//                        spinner.setAdapter(adapter);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Uh-oh, an error occurred!
                    }
                });
    }

    public void newAppBtn(View view) {
        Intent intent = new Intent(this, NewRunActivity.class);
        startActivity(intent);
    }


//    public void onItemSelected(AdapterView<?> parent, View v, int position, long id) {
//
//        switch (position) {
//            case 0:
//                spinnerSelected = "Joe";
//                textView.setText(spinnerSelected);
//                break;
//            case 1:
//                spinnerSelected = "Bob";
//                textView.setText(spinnerSelected);
//                break;
//            case 2:
//                spinnerSelected = "Bill";
//                textView.setText(spinnerSelected);
//                break;
//        }
//    }

}



