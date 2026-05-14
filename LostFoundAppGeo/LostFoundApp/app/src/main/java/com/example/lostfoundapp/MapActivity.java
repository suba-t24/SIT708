package com.example.lostfoundapp;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.widget.*;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.location.*;
import com.google.android.gms.maps.*;
import com.google.android.gms.maps.model.*;

import java.util.ArrayList;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback {

    GoogleMap googleMap;
    DatabaseHelper db;
    FusedLocationProviderClient fusedLocationClient;

    EditText edtRadius;
    Button btnApplyRadius;

    double userLat = 0;
    double userLng = 0;

    ArrayList<Advert> allAdverts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        db = new DatabaseHelper(this);
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        edtRadius = findViewById(R.id.edtRadius);
        btnApplyRadius = findViewById(R.id.btnApplyRadius);

        SupportMapFragment mapFragment =
                (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapFragment);

        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }

        btnApplyRadius.setOnClickListener(v -> applyRadiusSearch());
    }

    @Override
    public void onMapReady(GoogleMap map) {
        googleMap = map;
        getUserLocationAndShowItems();
    }

    private void getUserLocationAndShowItems() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(
                    this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    200
            );
            return;
        }

        googleMap.setMyLocationEnabled(true);

        fusedLocationClient.getLastLocation().addOnSuccessListener(location -> {
            if (location != null) {
                userLat = location.getLatitude();
                userLng = location.getLongitude();

                LatLng userPosition = new LatLng(userLat, userLng);

                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userPosition, 12));

                googleMap.addMarker(new MarkerOptions()
                        .position(userPosition)
                        .title("Your Current Location")
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));

                showAllItemsOnMap();
            } else {
                Toast.makeText(this, "Unable to get current location", Toast.LENGTH_SHORT).show();
                showAllItemsOnMap();
            }
        });
    }

    private void showAllItemsOnMap() {
        googleMap.clear();

        if (userLat != 0 && userLng != 0) {
            LatLng userPosition = new LatLng(userLat, userLng);
            googleMap.addMarker(new MarkerOptions()
                    .position(userPosition)
                    .title("Your Current Location")
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));
        }

        allAdverts = db.getAllAdverts();

        for (Advert advert : allAdverts) {
            if (advert.latitude != 0 && advert.longitude != 0) {
                LatLng itemPosition = new LatLng(advert.latitude, advert.longitude);

                googleMap.addMarker(new MarkerOptions()
                        .position(itemPosition)
                        .title(advert.type + ": " + advert.name)
                        .snippet(advert.category + " | " + advert.location));
            }
        }

        Toast.makeText(this, "Showing all lost and found items", Toast.LENGTH_SHORT).show();
    }

    private void applyRadiusSearch() {
        String radiusText = edtRadius.getText().toString().trim();

        if (radiusText.isEmpty()) {
            Toast.makeText(this, "Enter radius in km", Toast.LENGTH_SHORT).show();
            return;
        }

        if (userLat == 0 || userLng == 0) {
            Toast.makeText(this, "Current location not available", Toast.LENGTH_SHORT).show();
            return;
        }

        double radiusKm = Double.parseDouble(radiusText);

        googleMap.clear();

        LatLng userPosition = new LatLng(userLat, userLng);

        googleMap.addMarker(new MarkerOptions()
                .position(userPosition)
                .title("Your Current Location")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));

        googleMap.addCircle(new CircleOptions()
                .center(userPosition)
                .radius(radiusKm * 1000)
                .strokeWidth(3)
                .fillColor(0x220000FF)
                .strokeColor(0xFF0000FF));

        ArrayList<Advert> adverts = db.getAllAdverts();

        int count = 0;

        for (Advert advert : adverts) {
            float[] result = new float[1];

            Location.distanceBetween(
                    userLat,
                    userLng,
                    advert.latitude,
                    advert.longitude,
                    result
            );

            double distanceKm = result[0] / 1000.0;

            if (distanceKm <= radiusKm) {
                LatLng itemPosition = new LatLng(advert.latitude, advert.longitude);

                googleMap.addMarker(new MarkerOptions()
                        .position(itemPosition)
                        .title(advert.type + ": " + advert.name)
                        .snippet(advert.category + " | " + String.format("%.2f km away", distanceKm)));

                count++;
            }
        }

        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userPosition, 12));

        Toast.makeText(this, count + " item(s) found within " + radiusKm + " km", Toast.LENGTH_SHORT).show();
    }
}