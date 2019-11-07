package com.lxn.myhome.com.lxn;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.lxn.myhome.R;
import com.lxn.myhome.com.lxn.adapter.MyhomeAdapter;
import com.lxn.myhome.com.lxn.model.Home;
import com.lxn.myhome.com.lxn.util.GPSTracker;
import com.lxn.myhome.com.lxn.util.LocationUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Locale;

public class Find_around extends AppCompatActivity {
    private ArrayList<Home> mArrayHome;
    private GPSTracker gpsTracker;
    private LatLng currentPosition;
    private RecyclerView recyclerView;
    private ArrayList<Home> listhome = new ArrayList<>();;
    private MyhomeAdapter myhomeAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity__find_around_here);

        initView();
        addPlace();
        getLocal();
        createDs();
        getMylocation();
    }

    private void createDs() {
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(getApplicationContext(),2));
        myhomeAdapter = new MyhomeAdapter(listhome, getApplicationContext());
        recyclerView.setAdapter(myhomeAdapter);
    }

    private void initView() {
        recyclerView = findViewById(R.id.recyclerView);
    }

    private void addPlace() {
        if (!Places.isInitialized()) {
            Places.initialize(getApplicationContext(), "AIzaSyAoDvmEF49FQVfuh1O1ZyNUubq_C4ZSIjI", Locale.US);
        }

        AutocompleteSupportFragment autocompleteFragment = (AutocompleteSupportFragment)
                getSupportFragmentManager().findFragmentById(R.id.autocomplete_fragment);
        autocompleteFragment.setPlaceFields(Arrays.asList(Place.Field.LAT_LNG));
        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(@NonNull Place place) {
                listhome.clear();
                for (Home home : mArrayHome){
                    if (LocationUtils.getDistanceMeters(home.getLatitude(),home.getLongitude(),place.getLatLng().latitude,place.getLatLng().longitude) < 2000){
                        listhome.add(home);
                    }
                }
                myhomeAdapter.notifyDataSetChanged();
            }

            @Override
            public void onError(@NonNull Status status) {
            }
        });
    }

    private void getLocal() {
        mArrayHome= new ArrayList<>();
        FirebaseDatabase.getInstance().getReference().child("Home").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                        Home home = snapshot.getValue(Home.class);

                        mArrayHome.add(home);
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    private void getMylocation() {
        gpsTracker  = new GPSTracker(this);
        if (gpsTracker.canGetLocation()){
            currentPosition = new LatLng(gpsTracker.getLatitude(), gpsTracker.getLongitude());
        }
    }
}
