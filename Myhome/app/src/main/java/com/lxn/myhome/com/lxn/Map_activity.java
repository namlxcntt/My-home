package com.lxn.myhome.com.lxn;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.common.api.Status;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;
import com.lxn.myhome.R;

import java.util.Arrays;

public class Map_activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_activity);

        if (!Places.isInitialized()) {
            Places.initialize(getApplicationContext(), "AIzaSyAoDvmEF49FQVfuh1O1ZyNUubq_C4ZSIjI");
        }

        AutocompleteSupportFragment autocompleteFragment = (AutocompleteSupportFragment)
                getSupportFragmentManager().findFragmentById(R.id.autocomplete_fragment);

        autocompleteFragment.setPlaceFields(Arrays.asList(Place.Field.ADDRESS, Place.Field.LAT_LNG));

        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                Intent intent = new Intent();
                intent.putExtra("nameAddress"   ,place.getAddress());
                intent.putExtra("latitude"      ,place.getLatLng().latitude);
                intent.putExtra("longitude"     ,place.getLatLng().longitude);
                setResult(RESULT_OK,intent);
                finish();
            }
            @Override
            public void onError(Status status) {
                Log.i("Namlxcntt", "An error occurred: " + status);
            }
        });
    }
}
