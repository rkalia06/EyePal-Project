package com.example.a1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.ListResult;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.ortiz.touchview.TouchImageView;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Calendar;

public class NewRunActivity extends AppCompatActivity {

    TouchImageView imageView;

    Bitmap mainBitmap;
    Bitmap objectDetBitmap;

    //Run Screen Stuff
    Button fromCameraRollBtn;
    Button adjustBtn;
    Button fromCameraBtn;
    Button confirmBtn;
    Button saveToDeviceBtn;
    Button saveToCloudBtn;
    Button checkmarkBtn;

    // Save Screen Stuff
    EditText editTextFileName;
    EditText editTextPatientName;
    EditText editTextPictureName;
    Spinner patientSpinner;
    Spinner eyeSpinner;
    FloatingActionButton filenameSearchFloatingBtn;

    TextView cdInfo;
    TextView mText;
    TextView instructions;
    TextView instructionsTwo;
    TextView placeholder;
    TextView eyeText;

    boolean runFirstOrSecond; // true = first, false = second

    String cdRatioOverall;
    String date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_run);

        imageView = findViewById(R.id.imgViewTouch);

        confirmBtn = findViewById(R.id.ConfirmZoomBtn);
        adjustBtn = findViewById(R.id.AdjustBtn);
        fromCameraBtn = findViewById(R.id.NewCameraBtn);
        fromCameraRollBtn = findViewById(R.id.NewCameraRollBtn);
        checkmarkBtn = findViewById(R.id.CheckmarkBtn);

        saveToDeviceBtn = findViewById(R.id.SaveToDeviceBtn);
        saveToCloudBtn = findViewById(R.id.SaveToCloudBtn);

        editTextFileName = findViewById(R.id.editTextFileName);
        editTextPatientName = findViewById(R.id.editTextPatientName);
        editTextPictureName = findViewById(R.id.editTextPictureName);
        patientSpinner = findViewById(R.id.patientSpinner);
        eyeSpinner = findViewById(R.id.eyeSpinner);
        filenameSearchFloatingBtn = findViewById(R.id.filenameSearchFloatingBtn);

        cdInfo = findViewById(R.id.cdInfo);
        mText = findViewById(R.id.textViewM);
        instructions = findViewById(R.id.instructions);
        instructionsTwo = findViewById(R.id.instructionsPtTwo);
        placeholder = findViewById(R.id.imagePlaceholder);
        eyeText = findViewById(R.id.eyeSpinnerPrompt);

        runFirstOrSecond = true;

        cdRatioOverall = "";

        BottomNavigationView bottomNavigationView;
        bottomNavigationView = findViewById(R.id.bottomNavigationView);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String value = extras.getString("key");
            Log.e("KEY",value);
            //The key argument here must match that used in the other activity
            if (value.equals("first")){
                hideSaveShowFirst();
                Log.e("message","hiding save showing second");
                bottomNavigationView.setSelectedItemId(R.id.runItem);
            }
            else {
                hideFirstShowSave();
                Log.e("message","hiding first showing save");
                bottomNavigationView.setSelectedItemId(R.id.saveItem);
            }
        }

        Calendar c = Calendar.getInstance();
        int day = c.get(Calendar.DAY_OF_MONTH);
        int month = c.get(Calendar.MONTH);
        int year = c.get(Calendar.YEAR);
        date = day + "/" + (month+1) + "/" + year;

        bottomNavigationView.setOnItemSelectedListener(item -> {
            int id = item.getItemId();

            switch (id) {
                case R.id.runItem:
                    Log.d("1", "RUN ITEM ACTIVATED");
                    hideSaveShowFirst();
                    return true;

                case R.id.saveItem:
                    Log.d("2", "SAVE ITEM ACTIVATED");
                    hideFirstShowSave();
                    return true;

                case R.id.galleryItem:
                    Log.d("3", "GALLERY ITEM ACTIVATED");
                    Intent intentGallery = new Intent(this, GalleryActivity.class);
                    startActivity(intentGallery);
                    overridePendingTransition(0, 0);
                    return true;

                case R.id.mapItem:
                    Log.d("3", "MAP ITEM ACTIVATED");
                    Intent intentMap = new Intent(this, MapsActivity.class);
                    startActivity(intentMap);
                    overridePendingTransition(0, 0);
                    return true;
            }

            return false;
        });

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
                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(NewRunActivity.this,
                                android.R.layout.simple_spinner_item,paths);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        patientSpinner.setAdapter(adapter);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Uh-oh, an error occurred!
                    }
                });

    }

    //    SELECT IMAGE GROUP DO NOT UNGROUP
    public void selectImageBtn(View view) {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent, 250);
    }

    public void cameraBtn(View view) {
        final int REQUEST_IMAGE_CAPTURE = 1;
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        try {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        } catch (ActivityNotFoundException e) {
            Log.d("YO","CAMERA NOT WORKING!!!");
        }
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent resultData) {
        super.onActivityResult(requestCode, resultCode, resultData);
        placeholder.setVisibility(View.INVISIBLE);

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

        //camera request
        if (requestCode == 1 && resultCode == RESULT_OK) {
            Bundle extras = resultData.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            mainBitmap = imageBitmap;
            objectDetBitmap = mainBitmap;
            imageView.setImageBitmap(imageBitmap);
        }

        placeholder.setVisibility(View.INVISIBLE);

    }
    //    SELECT IMAGE GROUP DO NOT UNGROUP

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
        Bitmap zBmp = Bitmap.createBitmap(mainBitmap, Math.round(left*w), Math.round(top*h), Math.round(width*w), Math.round(height*h));
        imageView.resetZoom();
        imageView.setImageBitmap(zBmp);
        mainBitmap = zBmp;
        objectDetBitmap = zBmp;

        objDetInference();
    }

    private void hideFirst(){
        instructions.setVisibility(View.INVISIBLE);
        instructionsTwo.setVisibility(View.INVISIBLE);
        confirmBtn.setVisibility(View.INVISIBLE);
        fromCameraBtn.setVisibility(View.INVISIBLE);
        fromCameraRollBtn.setVisibility(View.INVISIBLE);
    }

    private void showFirst(){
        instructions.setVisibility(View.VISIBLE);
        instructionsTwo.setVisibility(View.VISIBLE);
        confirmBtn.setVisibility(View.VISIBLE);
        fromCameraBtn.setVisibility(View.VISIBLE);
        fromCameraRollBtn.setVisibility(View.VISIBLE);
    }

    private void hideSecond(){
        cdInfo.setVisibility(View.INVISIBLE);
        mText.setVisibility(View.INVISIBLE);
        adjustBtn.setVisibility(View.INVISIBLE);


    }

    private void showSecond(){
        cdInfo.setVisibility(View.VISIBLE);
        mText.setVisibility(View.VISIBLE);
        adjustBtn.setVisibility(View.VISIBLE);


    }

    private void hideFirstShowSecond() {
        hideFirst();
        showSecond();
    }

    private void hideSecondShowFirst() {
        hideSecond();
        showFirst();
    }

    private void showSave(){
        editTextFileName.setVisibility(View.VISIBLE);
        editTextPatientName.setVisibility(View.VISIBLE);
//        editTextPictureName.setVisibility(View.VISIBLE);
        eyeSpinner.setVisibility(View.VISIBLE);
        eyeText.setVisibility(View.VISIBLE);
        patientSpinner.setVisibility(View.VISIBLE);
        filenameSearchFloatingBtn.setVisibility(View.VISIBLE);

        saveToDeviceBtn.setVisibility(View.VISIBLE);
        saveToCloudBtn.setVisibility(View.VISIBLE);
    }

    private void hideSave(){
        editTextFileName.setVisibility(View.INVISIBLE);
        editTextPatientName.setVisibility(View.INVISIBLE);
//        editTextPictureName.setVisibility(View.INVISIBLE);
        eyeSpinner.setVisibility(View.INVISIBLE);
        eyeText.setVisibility(View.INVISIBLE);
        patientSpinner.setVisibility(View.INVISIBLE);
        filenameSearchFloatingBtn.setVisibility(View.INVISIBLE);

        saveToDeviceBtn.setVisibility(View.INVISIBLE);
        saveToCloudBtn.setVisibility(View.INVISIBLE);
    }

    private void hideFirstShowSave() {
        hideFirst();
        hideSecond();
        showSave();
    }

    private void hideSaveShowFirst() {
        hideSave();
        hideSecond();
        showFirst();
    }

    private void objDetInference() {
        hideFirstShowSecond();
        Log.d("TAG", "message1");
        ObjD instance = new ObjD(this);
        Log.d("TAG", "message2");
        objectDetBitmap = instance.runObjDet(objectDetBitmap);
//        String newString = instance.fuzzypickles("ee");
//        Log.d("TAG", newString);
        Log.d("TAG", "message3");
        imageView.setImageBitmap(objectDetBitmap);
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

        cdInfo.setText("Cup Area: " + cupArea.toString() + "\nDisc Area: " + discArea.toString() + "\nC-D Ratio: "+truncRatio);
        mText.setText(glaucPred);

        cdRatioOverall = truncRatio;

    }

    public void objDetInference(View view){
        hideFirstShowSecond();
        Log.d("TAG", "message1");
        ObjD instance = new ObjD(this);
        Log.d("TAG", "message2");
        objectDetBitmap = instance.runObjDet(objectDetBitmap);
//        String newString = instance.fuzzypickles("ee");
//        Log.d("TAG", newString);
        Log.d("TAG", "message3");
        imageView.setImageBitmap(objectDetBitmap);
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

        cdInfo.setText("Cup Area: " + cupArea.toString() + "\nDisc Area: " + discArea.toString() + "\nC-D Ratio: "+truncRatio);
        mText.setText(glaucPred);

    }


//    public void onNavigationItemSelected(@NonNull MenuItem item) {
//
//        switch (item.getItemId()) {
//            case R.id.runItem:
//                Log.d("!!!", "RUN ITEM ACTIVATED");
//
//            case R.id.saveItem:
//                Log.d("!!!", "SAVE ITEM ACTIVATED");
//
//            case R.id.galleryItem:
//                Log.d("!!!", "GALLERY ITEM ACTIVATED");
//
//            case R.id.mapItem:
//                Log.d("!!!", "MAP ITEM ACTIVATED");
//        }
//    }


    public void onNavigationItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();

        switch (id) {
            case R.id.runItem:
                Log.d("!!!", "RUN ITEM ACTIVATED");

            case R.id.saveItem:
                Log.d("!!!", "SAVE ITEM ACTIVATED");

            case R.id.galleryItem:
                Log.d("!!!", "GALLERY ITEM ACTIVATED");

            case R.id.mapItem:
                Log.d("!!!", "MAP ITEM ACTIVATED");
        }

    }

    public void myNavigationItemListener(@NonNull MenuItem item){
        int id = item.getItemId();

        switch (id) {
            case R.id.runItem:
                Log.d("!!!", "RUN ITEM ACTIVATED");

            case R.id.saveItem:
                Log.d("!!!", "SAVE ITEM ACTIVATED");

            case R.id.galleryItem:
                Log.d("!!!", "GALLERY ITEM ACTIVATED");

            case R.id.mapItem:
                Log.d("!!!", "MAP ITEM ACTIVATED");
        }
    }

    public void adjustBtn(View view) {
        hideSecondShowFirst();
    }

    public void saveBtnMethod(View view) {
        String text = patientSpinner.getSelectedItem().toString();
        String eyeText = eyeSpinner.getSelectedItem().toString();
        String dateMM = editTextFileName.getText().toString();
        String eyeTextCloud;
        if (eyeText.equals("Left Eye")){
            Log.e("LEFT EYE", "LEFT EYE");
            eyeTextCloud = "Left";
        }
        else {
            Log.e("RIGHT EYE", "RIGHT EYE");
            eyeTextCloud = "Right";
        }
        String fileText = editTextPictureName.getText().toString();
        if (!(editTextPatientName.getText().toString().equals("Patient Name (if new)"))) {
            text = editTextPatientName.getText().toString();
        }

        String dateCopy = date;
        dateCopy = "10-14-22";
        fileText = text + " " + eyeTextCloud + " " + cdRatioOverall + " " + dateCopy;
        Log.e("fileText",fileText);

//        textView.setText("Saving to Patient: "+ text);
        StorageReference listRef = FirebaseStorage.getInstance().getReference("/");
        StorageReference mountainsRef = listRef.child(text + "/" + fileText);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        mainBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();
        UploadTask uploadTask = mountainsRef.putBytes(data);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle unsuccessful uploads
//                textView.setText("Upload Failed - Please Try Again");
                Log.e("failure","failed to upload");
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, etc.
                // ...
//                textView.setText("Upload Complete");
                Log.e("success","succesful upload");
                changeBtn();
            }
        });
    }

    public void changeBtn(){
        checkmarkBtn.setVisibility(View.VISIBLE);
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                checkmarkBtn.setVisibility(View.INVISIBLE);
            }
        };
        Handler handler = new Handler(Looper.getMainLooper());
        handler.postDelayed(runnable, 1000);
    }
}

