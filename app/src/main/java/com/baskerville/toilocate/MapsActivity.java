package com.baskerville.toilocate;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.baskerville.toilocate.classes.MockToilets;
import com.baskerville.toilocate.classes.Toilet;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, LocationListener {

    private static final long MIN_TIME = 400;
    private static final float MIN_DISTANCE = 5;
    private GoogleMap mMap;
    private LocationManager locationManager;
    private double[] coordinates = new double[2];
    private ArrayList<Toilet> nearbyToilets;

    private ArrayList<Toilet> toiletCards;

    private FloatingActionButton addToilet;

    private TextView recyclerTitleText;
    private LinearLayout linearLayoutRecycler;

     RecyclerView recyclerView;
     Adapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        nearbyToilets = MockToilets.getMockToilets();

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            return;
        }
        if (checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
            return;
        }

        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, MIN_TIME,
                MIN_DISTANCE, this);



        addToilet = findViewById(R.id.addToiletFab);
        Bundle currentLocationBundle = new Bundle();
        currentLocationBundle.putDoubleArray("coordinates", coordinates);
        addToilet.setOnClickListener((view) -> {
            Intent intent = new Intent(MapsActivity.this, AddToilet.class);
            intent.putExtras(currentLocationBundle);
            startActivity(intent);
//            nearbyToilets.remove(0);
//            adapter.notifyDataSetChanged();
            return;
        });

        linearLayoutRecycler = findViewById(R.id.linearLayoutRecycler);

        recyclerTitleText = findViewById(R.id.textViewRecyclerTitle);
        toiletCards = new ArrayList<>();
        recyclerTitleText.setOnClickListener(view -> {
            if(toiletCards.isEmpty()) {
                toiletCards.addAll(nearbyToilets);
            } else {
                toiletCards.clear();
            }
            adapter.notifyDataSetChanged();
        });


        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new Adapter(this, toiletCards);
        recyclerView.setAdapter(adapter);
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

        // Add a marker in Sydney and move the camera
//        LatLng sydney = new LatLng(-34, 151);
//        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
        mMap.setPadding(0,0,0, linearLayoutRecycler.getHeight());
        mMap.setMyLocationEnabled(true);
    }


    @Override
    public void onLocationChanged(Location location) {
        mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(location.getLatitude(), location.getLongitude())));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(17));
        markNearbyToilets();
        updateLocationToArray(location);
        mMap.setPadding(0,0,0, linearLayoutRecycler.getHeight());
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }

    private void markNearbyToilets() {
        if (nearbyToilets != null) {
            for (Toilet toilet : nearbyToilets) {

                BitmapDescriptor bitmapDescriptor;

                switch (toilet.getGender()) {
                    case "male":
                        bitmapDescriptor = BitmapDescriptorFactory
                                .fromResource(R.drawable.toilet_small);
                        break;
                    case "female":
                        bitmapDescriptor = BitmapDescriptorFactory
                                .fromResource(R.drawable.toilet_female_small);
                        break;
                    case "unisex":
                        bitmapDescriptor = BitmapDescriptorFactory
                                .fromResource(R.drawable.toilet_unisex_small);
                        break;
                    default:
                        bitmapDescriptor = BitmapDescriptorFactory
                                .fromResource(R.drawable.toilet_neutral_small);
                        break;
                }
                Marker toiletMarker = mMap.addMarker(new MarkerOptions()
                        .position(toilet.getLocation())
                        .title(toilet.getName())
                        .icon(bitmapDescriptor));
                toiletMarker.setTag(toilet);
            }
        }
    }

    private void updateLocationToArray(Location location) {
        this.coordinates[0] = location.getLatitude();
        this.coordinates[1] = location.getLongitude();
    }
}
