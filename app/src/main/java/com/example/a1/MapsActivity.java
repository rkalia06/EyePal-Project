package com.example.a1;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.example.a1.databinding.ActivityMapsBinding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
//    private ActivityMapsBinding binding;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private static final int Request_code=101;
    private double lat, lng;

    ImageButton atm,bank,hosp,res;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
//        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(R.layout.activity_maps);

        fusedLocationProviderClient= LocationServices.getFusedLocationProviderClient(this.getApplicationContext());

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        BottomNavigationView bottomNavigationView;
        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setSelectedItemId(R.id.mapItem);

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
                    Intent intentGallery = new Intent(this, GalleryActivity.class);
                    startActivity(intentGallery);
                    overridePendingTransition(0, 0);
                    return true;

                case R.id.mapItem:
                    Log.d("4", "MAP ITEM ACTIVATED");
                    return true;
            }

            return false;
        });

    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(@NonNull LatLng latLng) {
                MarkerOptions markerOptions = new MarkerOptions();

                double[] locationsLat = {
                        33.94145521117566,
                        33.96873062700787,
                        33.96832228486328,
                        33.974444026508465,
                        33.91334205345254,
                        33.86261165185109,
                        33.86693635001641,
                        33.89214260095125,
                        34.0014273432178,
                        33.911768542332354,
                        34.03092200100038,
                        34.03910804516431
                };
                double[] locationsLong = {
                        -84.51220035720118,
                        -84.54818353823451,
                        -84.54891599782258,
                        -84.54911710382346,
                        -84.60168881362922,
                        -84.60649533191183,
                        -84.49552592001126,
                        -84.50626687181142,
                        -84.59372613074493,
                        -84.47759427214906,
                        -84.56922144471318,
                        -84.41463824963091

                };
                String[] titles = {
                        "Eye Site Optical",
                        "Georgia Eye Specialists",
                        "Dr. John J. Miller, MD",
                        "Marietta Eye Clinic",
                        "Family Eye Care of Marietta",
                        "Pearle Vision",
                        "Clarkson Eyecare",
                        "Rothbloom Eye Care",
                        "Classic Vision Care - Kennesaw",
                        "Eye Associates of Smyrna, P.C.",
                        "Thomas Eye Group - Kennesaw Office",
                        "Thomas Eye Group - East Cobb Office",


                };

                LatLng LLA;
                for (int i = 0; i < locationsLat.length; i++){
                    double LLALat = locationsLat[i];
                    double LLALong = locationsLong[i];
                    LLA = new LatLng(LLALat,LLALong);
                    markerOptions.position(LLA);
                    markerOptions.title(titles[i]);

//                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(LLA,5));
                    mMap.addMarker(markerOptions);
                }

                double zoomLLALat = 33.941447;
                double zoomLLALong = -84.530700;
                LatLng zoomLLA = new LatLng(zoomLLALat,zoomLLALong);
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(zoomLLA,10));

            }
        });
    }

    private void getCurrentLocation(){

        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)!=PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},Request_code);
            return;

        }

        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setInterval(60000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setFastestInterval(5000);
        LocationCallback locationCallback = new LocationCallback(){

            @Override
            public void onLocationResult(LocationResult locationResult) {
                Toast.makeText(getApplicationContext(),"location result is="+locationResult, Toast.LENGTH_LONG).show();

                if (locationResult == null){
                    Toast.makeText(getApplicationContext(),"Current location is null", Toast.LENGTH_LONG).show();

                    return;
                }

                for (Location location:locationResult.getLocations()){
                    if (location!=null){
                        Toast.makeText(getApplicationContext(),"Current location is" + location.getLongitude(), Toast.LENGTH_LONG).show();
                    }
                }
            }
        };

        fusedLocationProviderClient.requestLocationUpdates(locationRequest,locationCallback,null);

        Task<Location> task = fusedLocationProviderClient.getLastLocation();
        task.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(@NonNull Location location) {

                if (location != null){

                    lat = location.getLatitude();
                    lng = location.getLongitude();

                    LatLng latLng = new LatLng(lat,lng);
                    mMap.addMarker(new MarkerOptions().position(latLng).title("current location"));
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng,15));


                }

            }
        });
    }

    @SuppressLint("MissingSuperCall")
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (Request_code){
            case Request_code:
                if(grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    getCurrentLocation();
                }
        }
    }
}