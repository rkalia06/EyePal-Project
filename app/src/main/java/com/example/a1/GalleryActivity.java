package com.example.a1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.ListResult;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class GalleryActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    RecyclerView.Adapter programAdapter;
    RecyclerView.LayoutManager layoutManager;
    Spinner spinner;
    ArrayList<String> linksColGlobal;
    ArrayList<String> linksColGlobalCOPY;
    ArrayList<String> testArrayListApple;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);

        spinner = (Spinner)findViewById(R.id.spinnerGallery);

        String[] nameListStatic;

        recyclerView = findViewById(R.id.recyclerViewElement);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        linksColGlobal = new ArrayList<String>();
        linksColGlobalCOPY = new ArrayList<String>();

        testArrayListApple = new ArrayList<>();
//        testArrayListApple.add("https://firebasestorage.googleapis.com/v0/b/aoneidentification.appspot.com/o/Bobby%2FBobby%20Test?alt=media&token=24ee3180-47ad-4144-aecb-6d7cc0f6f732");
//        testArrayListApple.add("https://firebasestorage.googleapis.com/v0/b/aoneidentification.appspot.com/o/Bobby%2FBobby%20Test?alt=media&token=24ee3180-47ad-4144-aecb-6d7cc0f6f732");
//        testArrayListApple.add("https://firebasestorage.googleapis.com/v0/b/aoneidentification.appspot.com/o/Bobby%2FBobby%20Test?alt=media&token=24ee3180-47ad-4144-aecb-6d7cc0f6f732");
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
                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(GalleryActivity.this,
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
        Log.e("GLOBAL PRINT", linksColGlobal.toString());
        Log.e("GLOBAL PRINT","IM HERE");

        nameListStatic = new String[3];
        nameListStatic[0] = "HELLO";
        nameListStatic[1] = "HELLO";
        nameListStatic[2] = "HELLO";

        ArrayList<String> emptyArrayList = new ArrayList<>();

        programAdapter = new ProgramAdapter(getApplicationContext(), emptyArrayList, testArrayListApple);
        recyclerView.setAdapter(programAdapter);

        BottomNavigationView bottomNavigationView;
        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setSelectedItemId(R.id.galleryItem);

        bottomNavigationView.setOnItemSelectedListener(item -> {
            int id = item.getItemId();

            switch (id) {
                case R.id.runItem:
                    Log.d("1", "RUN ITEM ACTIVATED");
                    Intent intentRun = new Intent(this, NewRunActivity.class);
                    intentRun.putExtra("key","first");
                    startActivity(intentRun);
                    overridePendingTransition(0, 0);
                    return true;

                case R.id.saveItem:
                    Log.d("2", "SAVE ITEM ACTIVATED");
                    Intent intentSave = new Intent(this, NewRunActivity.class);
                    intentSave.putExtra("key","save");
                    startActivity(intentSave);
                    overridePendingTransition(0, 0);
                    return true;

                case R.id.galleryItem:
                    Log.d("3", "GALLERY ITEM ACTIVATED");
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
    }

    public void getLinks(View view) {
        String spinnerText = spinner.getSelectedItem().toString();
        StorageReference listRef = FirebaseStorage.getInstance().getReference("/"+spinnerText);

        linksColGlobal.clear();


        listRef.listAll()
                .addOnSuccessListener(new OnSuccessListener<ListResult>() {
                    @Override
                    public void onSuccess(ListResult listResult) {

                        Log.e("IMPORTANT", listRef.getName());
                        int counterOfRefs = 0;
                        int counterOfIts = 0;
                        for (StorageReference prefix : listResult.getPrefixes()) {
                            Log.e("IMPORTANT", prefix.getName());
                            counterOfRefs+=1;
                            Log.e("REF NUMBER",Integer.toString(counterOfRefs));
                            // This will give you a folder name
                            // You may call listAll() recursively on them.
                        }

                        ArrayList<String> itemNamesArrayList = new ArrayList<>();

                        for (StorageReference item : listResult.getItems()) {
                            Log.e("BINGBONG: ",item.getName());
                            itemNamesArrayList.add(item.getName());
                            Log.e("I MADE ITT HERE!!!!!", "I MADE ITT HERE!!!!!");
                            item.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>()
                            {
                                @Override
                                public void onSuccess(Uri downloadUrl)
                                {
                                    Log.e("UH",downloadUrl.toString());
                                    String stringVersionOfUrl = downloadUrl.toString();
                                    Log.e("LINKTOSTRING",stringVersionOfUrl);
                                    linksColGlobal.add(stringVersionOfUrl);
                                    for (int i = 0; i < linksColGlobal.size(); i++){
                                        Log.e("TESTLINKSARRAY",linksColGlobal.get(i));
                                        Log.e("printing index number",Integer.toString(i));

                                        Log.e("TESTERBESTER","TESTERBESTER");
                                        Log.e("FINALLY",linksColGlobal.toString());
                                    }

                                    String[] methodNameListStatic;
                                    methodNameListStatic = new String[3];
                                    methodNameListStatic[0] = "HELLO";
                                    methodNameListStatic[1] = "BELLO";
                                    methodNameListStatic[2] = "ZELLO";
//                                    programAdapter = new ProgramAdapter(getApplicationContext(), methodNameListStatic, linksColGlobal);
//                                    recyclerView.setAdapter(programAdapter);
                                    Log.e("LAST CHECK LETS GOOOOOO",linksColGlobal.toString());
                                    String[] smallNameListStatic = {"HELLO","HELLO","HELLO"};
                                    String[] emptyNameListStatic = {};
                                    ArrayList<String> emptyArrayList= new ArrayList<>();
                                    ArrayList<String> emptyArrayListForNames = new ArrayList<>();

                                    //reset recycler view
                                    programAdapter = new ProgramAdapter(getApplicationContext(), emptyArrayListForNames, emptyArrayList);
                                    recyclerView.setAdapter(programAdapter);

                                    // add new recycler view elements
                                    programAdapter = new ProgramAdapter(getApplicationContext(), itemNamesArrayList, linksColGlobal);
                                    recyclerView.setAdapter(programAdapter);



                                    for (int i = 0; i < linksColGlobal.size(); i++){
                                        linksColGlobalCOPY.add(linksColGlobal.get(i));
                                    }
                                    ArrayList<String> badaBing= new ArrayList<>();
                                    badaBing.add("Hello");
                                    badaBing.add("Goodbye");
                                    Log.e("TESTARRAYLISTDEMO",badaBing.toString());

                                }
                            });
                            counterOfIts += 1;
                            Log.e("ITERATIONS NUMBER",Integer.toString(counterOfIts));


                        }

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e("FAILURE","FAILURE");
                    }
                });

        Log.e("COPY OF LINKS",linksColGlobalCOPY.toString());
    }

//    public void getLinksInitialization() {
//        String spinnerText = "Bobby";
//        StorageReference listRef = FirebaseStorage.getInstance().getReference("/"+spinnerText);
//
//        listRef.listAll()
//                .addOnSuccessListener(new OnSuccessListener<ListResult>() {
//                    @Override
//                    public void onSuccess(ListResult listResult) {
//
//                        Log.e("IMPORTANT", listRef.getName());
//                        int counterOfRefs = 0;
//                        int counterOfIts = 0;
//                        for (StorageReference prefix : listResult.getPrefixes()) {
//                            Log.e("IMPORTANT", prefix.getName());
//                            counterOfRefs+=1;
//                            Log.e("REF NUMBER",Integer.toString(counterOfRefs));
//                            // This will give you a folder name
//                            // You may call listAll() recursively on them.
//                        }
//
//                        for (StorageReference item : listResult.getItems()) {
//                            Log.e("BINGBONG: ",item.getName());
//                            Log.e("I MADE ITT HERE!!!!!", "I MADE ITT HERE!!!!!");
//                            item.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>()
//                            {
//                                @Override
//                                public void onSuccess(Uri downloadUrl)
//                                {
//                                    Log.e("UH",downloadUrl.toString());
//                                    String stringVersionOfUrl = downloadUrl.toString();
//                                    Log.e("LINKTOSTRING",stringVersionOfUrl);
//                                    linksColGlobal.add(stringVersionOfUrl);
//                                    for (int i = 0; i < linksColGlobal.size(); i++){
//                                        Log.e("TESTLINKSARRAY",linksColGlobal.get(i));
//                                        Log.e("printing index number",Integer.toString(i));
//
//                                        Log.e("TESTERBESTER","TESTERBESTER");
//                                        Log.e("FINALLY",linksColGlobal.toString());
//                                    }
//
//                                    String[] methodNameListStatic;
//                                    methodNameListStatic = new String[3];
//                                    methodNameListStatic[0] = "HELLO";
//                                    methodNameListStatic[1] = "BELLO";
//                                    methodNameListStatic[2] = "ZELLO";
//                                    programAdapter = new ProgramAdapter(getApplicationContext(), methodNameListStatic, linksColGlobal);
//                                    recyclerView.setAdapter(programAdapter);
//                                    Log.e("LAST CHECK LETS GOOOOOO",linksColGlobal.toString());
//                                    ArrayList<String> badaBing= new ArrayList<>();
//                                    badaBing.add("Hello");
//                                    badaBing.add("Goodbye");
//                                    Log.e("TESTARRAYLISTDEMO",badaBing.toString());
//
//                                }
//                            });
//                            counterOfIts += 1;
//                            Log.e("ITERATIONS NUMBER",Integer.toString(counterOfIts));
//
//
//                        }
//
//                    }
//                })
//                .addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//                        Log.e("FAILURE","FAILURE");
//                    }
//                });
//    }

}