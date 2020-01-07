package com.baskerville.toilocate;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.RatingBar;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.baskerville.toilocate.dto.ToiletDTO;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class AddToilet extends AppCompatActivity implements OnMapReadyCallback {

    private static final String MAPVIEW_BUNDLE_KEY = "MapViewBundleKey";
    FloatingActionButton fabTakePhoto;
    ImageView imageViewPhoto;
    EditText editTextName;
    CheckBox checkBoxSink, checkBoxMirror, checkBoxShower, checkBoxUrineTank;
    ToiletDTO toiletDTO;
    private int pic_id = 111;
    private double[] lastCoordinates;
    private MapView mMapView;
    private RadioGroup radioGroup;
    private Button btnSubmit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_toilet);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Add Toilet");

        imageViewPhoto = findViewById(R.id.imageViewPhoto);
        fabTakePhoto = findViewById(R.id.fabPhoto);
        fabTakePhoto.setOnClickListener(view -> {
            //submit data or take photograph
            Intent camera_intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(camera_intent, pic_id);
        });


        setupNameEditText();
        setupRadioGroup();
        setupSubmitButton();

        lastCoordinates = getIntent().getExtras().getDoubleArray("coordinates");
        initGoogleMap(savedInstanceState);

        toiletDTO = new ToiletDTO();
    }

    private void initGoogleMap(Bundle savedInstanceState) {
        // *** IMPORTANT ***
        // MapView requires that the Bundle you pass contain _ONLY_ MapView SDK
        // objects or sub-Bundles.
        Bundle mapViewBundle = null;
        if (savedInstanceState != null) {
            mapViewBundle = savedInstanceState.getBundle(MAPVIEW_BUNDLE_KEY);
        }
        mMapView = findViewById(R.id.mapViewAddToilet);
        mMapView.onCreate(mapViewBundle);

        mMapView.getMapAsync(this);
    }


    private void setupRadioGroup() {
        radioGroup = findViewById(R.id.radioGroupGender);
        radioGroup.check(R.id.radioButtonUni);
        radioGroup.setOnCheckedChangeListener((radioGroup, checkerId) ->
                findViewById(R.id.checkBoxUrineTanks).setEnabled(checkerId != R.id.radioButtonFemale));
    }

    private void setupNameEditText() {
        editTextName = findViewById(R.id.editTextName);
        editTextName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                btnSubmit.setEnabled(editable.length() > 0);
            }
        });
    }

    private void setupSubmitButton() {
        btnSubmit = findViewById(R.id.btnSubmit);
        btnSubmit.setEnabled(false);
        btnSubmit.setOnClickListener(view -> {

            AlertDialog.Builder mBuilder = new AlertDialog.Builder(AddToilet.this);
            View mView = getLayoutInflater().inflate(R.layout.rating_dialog, null);
            final RatingBar toiletRating = mView.findViewById(R.id.ratingBarDialog);
            Button btnSubmitDialog = mView.findViewById(R.id.buttonSubmitDialog);

            btnSubmitDialog.setOnClickListener(view1 -> {
                Toast.makeText(AddToilet.this, Float.toString(toiletRating.getRating()), Toast.LENGTH_SHORT).show();
            });

            mBuilder.setView(mView);
            AlertDialog dialog =  mBuilder.create();
            dialog.show();



            toiletDTO.setName(editTextName.getText().toString());
            Log.i("ToiletDTO", toiletDTO.getName());
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == pic_id) {
            Bitmap photo = (Bitmap) data.getExtras()
                    .get("data");

            imageViewPhoto.setImageBitmap(photo);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        Bundle mapViewBundle = outState.getBundle(MAPVIEW_BUNDLE_KEY);
        if (mapViewBundle == null) {
            mapViewBundle = new Bundle();
            outState.putBundle(MAPVIEW_BUNDLE_KEY, mapViewBundle);
        }
        mMapView.onSaveInstanceState(mapViewBundle);
    }

    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    public void onStart() {
        super.onStart();
        mMapView.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
        mMapView.onStop();
    }

    @Override
    public void onMapReady(GoogleMap map) {
        //map.addMarker(new MarkerOptions().position(new LatLng(0, 0)).title("Marker"));
        //map.setMyLocationEnabled(true);
        map.addMarker(new MarkerOptions().position(new LatLng(lastCoordinates[0], lastCoordinates[1])).title("New Toilet"));
        map.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(lastCoordinates[0], lastCoordinates[1])));
        Log.i("Move camera", lastCoordinates[0] + ", " + lastCoordinates[1]);
        map.animateCamera(CameraUpdateFactory.zoomTo(17));
    }

    @Override
    public void onPause() {
        mMapView.onPause();
        super.onPause();
    }

    @Override
    public void onDestroy() {
        mMapView.onDestroy();
        super.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
