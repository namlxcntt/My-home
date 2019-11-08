package com.lxn.myhome.com.lxn.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.lxn.myhome.R;
import com.lxn.myhome.com.lxn.model.Home;
import com.lxn.myhome.com.lxn.util.GPSTracker;

import java.util.ArrayList;

public class GMapsActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap googleMap;
    private GPSTracker gpsTracker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gmaps);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;
        getMylocation();
        getLocal();
    }

    private void getLocal() {
        FirebaseDatabase.getInstance().getReference().child("Home").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        Home home = snapshot.getValue(Home.class);

                        googleMap.addMarker(new MarkerOptions().position(new LatLng(home.getLatitude(), home.getLongitude())).title(home.getDiachi()).icon(BitmapDescriptorFactory.fromResource(R.drawable.placeholder)));
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    private void getMylocation() {
        gpsTracker = new GPSTracker(this);
        if (gpsTracker.canGetLocation()) {

            LatLng Hanoi = new LatLng(gpsTracker.getLatitude(), gpsTracker.getLongitude());
            googleMap.addMarker(new MarkerOptions().position(Hanoi).title("Tôi").icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_current_position_24dp)));
            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(Hanoi, 16));
        }
    }
}
