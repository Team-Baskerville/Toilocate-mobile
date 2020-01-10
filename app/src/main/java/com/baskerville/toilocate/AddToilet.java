package com.baskerville.toilocate;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
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
import androidx.core.content.FileProvider;

import com.baskerville.toilocate.classes.Config;
import com.baskerville.toilocate.dto.ImageResponseDTO;
import com.baskerville.toilocate.dto.LocationDTO;
import com.baskerville.toilocate.dto.ToiletDTO;
import com.baskerville.toilocate.dto.ToiletDescriptionDTO;
import com.baskerville.toilocate.dto.ToiletSaveResDTO;
import com.baskerville.toilocate.security.UserHandler;
import com.baskerville.toilocate.service.ToiletService;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import id.zelory.compressor.Compressor;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AddToilet extends AppCompatActivity implements OnMapReadyCallback {

    private static final String MAPVIEW_BUNDLE_KEY = "MapViewBundleKey";
    FloatingActionButton fabTakePhoto;
    ImageView imageViewPhoto;
    EditText editTextName;
    CheckBox checkBoxSink, checkBoxMirror, checkBoxShower, checkBoxUrineTank;
    ToiletDTO toiletDTO;
    private int pic_id = 111;
    private double[] lastCoordinates = new double[2];
    private MapView mMapView;
    private RadioGroup radioGroup;
    private Button btnSubmit;
    private Uri photoURI;

    private Retrofit retrofit = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_toilet);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Add Toilet");

        toiletDTO = new ToiletDTO();
        lastCoordinates = getIntent().getExtras().getDoubleArray("coordinates");

        imageViewPhoto = findViewById(R.id.imageViewPhoto);
        setupCameraButton();
        setupNameEditText();
        setupRadioGroup();
        setupSubmitButton();


        initGoogleMap(savedInstanceState);


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

    private void setupCameraButton() {
        fabTakePhoto = findViewById(R.id.fabPhoto);
        fabTakePhoto.setOnClickListener(view -> {
            //submit data or take photograph
//            Intent camera_intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//            startActivityForResult(camera_intent, pic_id);

            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            // Ensure that there's a camera activity to handle the intent
            if (takePictureIntent.resolveActivity(view.getContext().getPackageManager()) != null) {
                // Create the File where the photo should go
                File photoFile = null;
                try {
                    photoFile = createImageFile();
                } catch (IOException ex) {
                    // Error occurred while creating the File
                }
                // Continue only if the File was successfully created
                if (photoFile != null) {
                    photoURI = FileProvider.getUriForFile(view.getContext(),
                            "com.baskerville.toilocate.provider",
                            photoFile);
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                    startActivityForResult(takePictureIntent, pic_id);
                }
            }
            //take photograph
            Intent camera_intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            camera_intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
            startActivityForResult(camera_intent, pic_id);
        });

    }


    private void setupRadioGroup() {
        radioGroup = findViewById(R.id.radioGroupGender);
        radioGroup.check(R.id.radioButtonUni);
        radioGroup.setOnCheckedChangeListener((radioGroup, checkerId) -> {
            CheckBox checkBoxUrineTanks = findViewById(R.id.checkBoxUrineTanks);
            checkBoxUrineTanks.setChecked(false);
            checkBoxUrineTanks.setEnabled(checkerId != R.id.radioButtonFemale);
        });
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
            setFormDataToDTO();

            AlertDialog.Builder mBuilder = new AlertDialog.Builder(AddToilet.this);
            View mView = getLayoutInflater().inflate(R.layout.rating_dialog, null);
            final RatingBar toiletRating = mView.findViewById(R.id.ratingBarDialog);
            Button btnSubmitDialog = mView.findViewById(R.id.buttonSubmitDialog);

            mBuilder.setView(mView);
            AlertDialog dialog = mBuilder.create();


            btnSubmitDialog.setOnClickListener(view1 -> {
                toiletDTO.setRating(String.valueOf(toiletRating.getRating()));
                saveNewToilet(view);
                dialog.dismiss();
            });

            dialog.show();
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == pic_id) {
            if (requestCode == pic_id && resultCode == RESULT_OK) {
                //Perform any task using uri
                //For example set this URI to fill an ImageView like below
                this.imageViewPhoto.setImageURI(photoURI);
            }
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

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = this.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        return image;
    }

    private void setFormDataToDTO() {
        toiletDTO.setName(editTextName.getText().toString());

        LocationDTO locationDTO = new LocationDTO();
        locationDTO.setType("Point");
        locationDTO.setCoordinates(new Double[]{lastCoordinates[1], lastCoordinates[0]});
        toiletDTO.setLocation(locationDTO);

        switch (radioGroup.getCheckedRadioButtonId()) {
            case (R.id.radioButtonMale):
                toiletDTO.setGender("male");
                break;
            case (R.id.radioButtonFemale):
                toiletDTO.setGender("female");
                break;
            case (R.id.radioButtonUni):
                toiletDTO.setGender("unisex");
                break;
        }

        CheckBox urineTank = findViewById(R.id.checkBoxUrineTanks);
        CheckBox waterSink = findViewById(R.id.checkBoxSink);
        CheckBox mirror = findViewById(R.id.checkBoxMirror);
        CheckBox shower = findViewById(R.id.checkBoxShower);
        CheckBox squat = findViewById(R.id.checkBoxSquat);
        CheckBox commode = findViewById(R.id.checkBoxCommode);

        ToiletDescriptionDTO descriptionDTO = new ToiletDescriptionDTO(
                urineTank.isChecked(), waterSink.isChecked(), mirror.isChecked(), shower.isChecked(),
                squat.isChecked(), commode.isChecked());
        toiletDTO.setDescription(descriptionDTO);

        if(UserHandler.getUser() != null && UserHandler.getUser().getId() != null) {
            toiletDTO.setUserId(UserHandler.getUser().getId());
        }
    }

    private void saveNewToilet(View view) {
        Log.i("Yo DTO so far", toiletDTO.toString());
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(Config.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        ToiletService toiletService = retrofit.create(ToiletService.class);

        //Create a file object using file path
        File file1 = new File(this.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
                + photoURI.getPath().substring(19));

        File file = null;
        try {
            file = new Compressor(this)
                    .setMaxWidth(640)
                    .setMaxHeight(480)
                    .setQuality(75)
                    .setCompressFormat(Bitmap.CompressFormat.WEBP)
                    //                .setDestinationDirectoryPath(Environment.getExternalStoragePublicDirectory(
                    //                        Environment.DIRECTORY_PICTURES).getAbsolutePath())
                    .compressToFile(file1);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Create a request body with file and image media type
        RequestBody fileReqBody = RequestBody.create(MediaType.parse("image/*"), file);
        // Create MultipartBody.Part using file request-body,file name and part name
        MultipartBody.Part part = MultipartBody.Part.createFormData("image", file.getName(), fileReqBody);
//        RequestBody name = RequestBody.create(MediaType.parse("text/plain"), toiletDTO.getName());


//        Uri photoURI = FileProvider.getUriForFile(this.getContext(),
//                "com.watson.android.fileprovider",
//                file);

        Call<ImageResponseDTO> call = toiletService.uploadImage(part, toiletDTO.getName());
        Log.i("Compose Request", "Request name: " + toiletDTO.getName());

        call.enqueue(new Callback<ImageResponseDTO>() {
            @Override
            public void onResponse(Call<ImageResponseDTO> call, Response<ImageResponseDTO> response) {
                Log.d("Yo incoming", "Incoming:" + response.body().toString());
                try {
                    String data = response.body().getPayload();
                    Log.i("Yo image res", "Image upload response received: " + data);
                    toiletDTO.setImagePath(data);
                    Log.i("Yo DTO so so far", toiletDTO.toString());

                    Call<ToiletSaveResDTO> toiletSaveCall = toiletService.saveToilet(toiletDTO);
                    toiletSaveCall.enqueue(new Callback<ToiletSaveResDTO>() {
                        @Override
                        public void onResponse(Call<ToiletSaveResDTO> call,
                                               Response<ToiletSaveResDTO> response) {

                            if (response.isSuccessful()) {
                                Log.i("Yo yo save res", response.body().getMessage());
                                Toast.makeText(AddToilet.this, response.body().getMessage(),
                                        Toast.LENGTH_SHORT).show();
                                Snackbar sb = Snackbar.make(view, response.body().getMessage(),
                                        Snackbar.LENGTH_LONG)
                                        .setAction("Action", null);
                                sb.addCallback(new Snackbar.Callback() {
                                    @Override
                                    public void onDismissed(Snackbar transientBottomBar, int event) {
                                        onBackPressed();
                                    }
                                });
                                sb.show();
                            }
                        }

                        @Override
                        public void onFailure(Call<ToiletSaveResDTO> call, Throwable t) {
                            Log.i("Yo save error", t.getMessage());
                            Snackbar.make(view,
                                    "Failed to save new Toilet. Check your internet connection",
                                    Snackbar.LENGTH_LONG)
                                    .setAction("Action", null).show();
                        }
                    });

                } catch (NullPointerException e) {
                    Log.i("Yo image error", e.getMessage());
                }
            }

            @Override
            public void onFailure(Call<ImageResponseDTO> call, Throwable throwable) {
                Log.e("Yo image upload fail", throwable.toString());
            }
        });
    }

}
