package com.baskerville.toilocate;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.baskerville.toilocate.classes.Config;
import com.baskerville.toilocate.classes.Toilet;
import com.baskerville.toilocate.dto.ResponseDTO;
import com.baskerville.toilocate.dto.ToiletDTO;
import com.baskerville.toilocate.dto.ToiletLiteDTO;
import com.baskerville.toilocate.dto.ToiletSearchDTO;
import com.baskerville.toilocate.security.UserHandler;
import com.baskerville.toilocate.service.ToiletService;
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
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, LocationListener, GoogleMap.OnMarkerClickListener {

    private static final long MIN_TIME = 400;
    private static final float MIN_DISTANCE = 5;
    private static Retrofit retrofit = null;
    private RecyclerView recyclerView;
    private Adapter adapter;
    private GoogleMap mMap;
    private LocationManager locationManager;
    private double[] coordinates = new double[2];
    private ArrayList<Toilet> nearbyToilets;
    private ArrayList<Toilet> toiletCards;
    private FloatingActionButton addToilet;
    private TextView recyclerTitleText;
    private LinearLayout linearLayoutRecycler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        nearbyToilets = new ArrayList<>();

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        }
        if (checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
        }

        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, MIN_TIME,
                MIN_DISTANCE, this);

        setupAddToiletButton();

        linearLayoutRecycler = findViewById(R.id.linearLayoutRecycler);

        ImageView imageViewUpDown = findViewById(R.id.imageViewUpDown);
        recyclerTitleText = findViewById(R.id.textViewRecyclerTitle);
        toiletCards = new ArrayList<>();
        recyclerTitleText.setOnClickListener(view -> {
            if (toiletCards.isEmpty()) {
                toiletCards.addAll(nearbyToilets);
                if (!toiletCards.isEmpty()) {
                    imageViewUpDown.setImageResource(R.drawable.down);
                }
            } else {
                toiletCards.clear();
                imageViewUpDown.setImageResource(R.drawable.up);

            }
            adapter.notifyDataSetChanged();
        });


        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new Adapter(this, toiletCards);
        recyclerView.setAdapter(adapter);
    }

    ;

    @Override
    protected void onResume() {
        super.onResume();
        if (!isLocationServiceAvailable()) {
            buildAlertMessage(Config.NO_GPS_MESSAGE, MobileServiceEnum.LOCATION, false);
        }
        if (!isNetworkAvailable()) {
            buildAlertMessage(Config.NO_INTERNET_MESSAGE, MobileServiceEnum.INTERNET, true);
        }
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
        if (!isLocationServiceAvailable()) {
            buildAlertMessage(Config.NO_GPS_MESSAGE, MobileServiceEnum.LOCATION, false);
        }

        mMap.setMyLocationEnabled(true);
        mMap.setOnMarkerClickListener(this);

    }

    @Override
    public void onLocationChanged(Location location) {
        mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(location.getLatitude(),
                location.getLongitude())));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(17));
        updateLocationToArray(location);
        getData();
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
        Log.i("Mark Nerby ", nearbyToilets.toString());
        if (nearbyToilets != null) {
            mMap.clear();
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

    @Override
    public boolean onMarkerClick(Marker marker) {
        if (marker.getTag() != null) {
            Intent toiletDetailsIntent =
                    new Intent(MapsActivity.this, ToiletDetails.class);
            toiletDetailsIntent.putExtra("toilet", new ToiletLiteDTO((Toilet) marker.getTag()));
            startActivity(toiletDetailsIntent);
        }
        return false;
    }

    private void setupAddToiletButton() {
        addToilet = findViewById(R.id.addToiletFab);
        Bundle currentLocationBundle = new Bundle();
        currentLocationBundle.putDoubleArray("coordinates", coordinates);
        addToilet.setOnClickListener((view) -> {
            if (UserHandler.getUser() != null) {
                Intent addToiletIntent = new Intent(MapsActivity.this, AddToilet.class);
                addToiletIntent.putExtras(currentLocationBundle);
                startActivity(addToiletIntent);
//            nearbyToilets.remove(0);
//            adapter.notifyDataSetChanged();
            } else {
                Intent loginIntent = new Intent(MapsActivity.this, LoginActivity.class);
                loginIntent.putExtras(currentLocationBundle);
                startActivity(loginIntent);
            }
        });
    }

    private void getData() {

        if (!isNetworkAvailable()) {
            buildAlertMessage(Config.NO_INTERNET_MESSAGE, MobileServiceEnum.INTERNET, true);
        }

        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(Config.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        ToiletService toiletService = retrofit.create(ToiletService.class);


        List<Toilet> toiletList = new ArrayList<>();

        Log.i("Coordinators", coordinates[1] + "," + coordinates[0]);

        Call<ResponseDTO> getNearbyToiletsCall = toiletService.getNearbyToilets(new ToiletSearchDTO(
                coordinates[1], coordinates[0], Config.MAX_DISTANCE
        ));
        getNearbyToiletsCall.enqueue(new Callback<ResponseDTO>() {
            @Override
            public void onResponse(Call<ResponseDTO> call, Response<ResponseDTO> response) {

                if (!response.isSuccessful()) {
                    Log.i("Nearby Response", Integer.toString(response.code()));
                    return;
                }

                List<ToiletDTO> toiletDTOList = response.body().getPayload().getToilets();
                nearbyToilets.clear();
                for (ToiletDTO toiletDto : toiletDTOList) {
                    nearbyToilets.add(new Toilet(toiletDto));
                }

                markNearbyToilets();
                toiletCards.clear();

                Log.i("Response", response.body().getPayload().toString());
            }

            @Override
            public void onFailure(Call<ResponseDTO> call, Throwable t) {
                Log.i("Response", t.getMessage());
            }
        });
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    private boolean isLocationServiceAvailable() {
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }

    private void buildAlertMessage(String message, MobileServiceEnum serviceType, boolean cancelable) {
        AlertDialog alertDialog = new AlertDialog.Builder(MapsActivity.this)
                .setCancelable(cancelable)
                .create();
        alertDialog.setTitle("Cannot Load Results");
        alertDialog.setMessage(message);
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        if (serviceType == MobileServiceEnum.LOCATION
                                && !isLocationServiceAvailable()) {
                            startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                        }
                        dialog.dismiss();
                    }
                });
        alertDialog.show();
    }

    private enum MobileServiceEnum {LOCATION, INTERNET}
}
