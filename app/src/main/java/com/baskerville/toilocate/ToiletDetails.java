package com.baskerville.toilocate;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.baskerville.toilocate.classes.Config;
import com.baskerville.toilocate.dto.RatingUpdateDTO;
import com.baskerville.toilocate.dto.RatingUpdateResDTO;
import com.baskerville.toilocate.dto.ToiletDescriptionDTO;
import com.baskerville.toilocate.dto.ToiletLiteDTO;
import com.baskerville.toilocate.service.ToiletService;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ToiletDetails extends AppCompatActivity {

    CollapsingToolbarLayout toolbarLayout;
    ToiletLiteDTO toiletLiteDTO;
    ImageView toiletImageView;
    RatingBar ratingBar;
    private Retrofit retrofit = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_toilet_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        toiletLiteDTO = (ToiletLiteDTO) getIntent().getSerializableExtra("toilet");
        Log.i("Toilet ID", toiletLiteDTO.getId());

        toolbarLayout = findViewById(R.id.toolbar_layout);
//        toolbarLayout.setTitle(getIntent().getExtras().getString("name"));

        toiletImageView = findViewById(R.id.toolbarImageDetails);
        Glide.with(this).load(Config.BASE_URL + toiletLiteDTO.getImagePath())
                .apply(new RequestOptions().override(200, 300))
                .into(toiletImageView);

        setupFloatingActionButton();

        populateView();
    }

    private void populateView() {
        ratingBar = findViewById(R.id.ratingBarToiletDetails);
        TextView textViewGender = findViewById(R.id.textViewDetailsGender);
        CircleImageView circleImageViewCommode = findViewById(R.id.imageDetailsCommode);
        CircleImageView circleImageViewSquat = findViewById(R.id.imageDetailsSquat);
        CircleImageView circleImageViewSink = findViewById(R.id.imageDetailsSink);
        CircleImageView circleImageViewMirror = findViewById(R.id.imageDetailsMirror);
        CircleImageView circleImageViewShower = findViewById(R.id.imageDetailsShower);
        CircleImageView circleImageViewUrineTank = findViewById(R.id.imageDetailsUrineTank);

        Log.i("Toilet details ", toiletLiteDTO.getDescription().toString());

        if (toiletLiteDTO != null) {
            toolbarLayout.setTitle(toiletLiteDTO.getName());
            ratingBar.setRating(toiletLiteDTO.getRating());
            Log.i("Unformatted Rating", String.valueOf(toiletLiteDTO.getRating()));
            Log.i("Formatted Rating", String.valueOf(ratingBar.getRating()));
            textViewGender.setText(String.format("%s TOILET",
                    toiletLiteDTO.getGender().toUpperCase()));
            if (toiletLiteDTO.getDescription() != null) {
                ToiletDescriptionDTO descriptionDTO = toiletLiteDTO.getDescription();
                if (descriptionDTO.isCommode()) {
                    circleImageViewCommode.setImageResource(R.drawable.commode_true);
                } else {
                    circleImageViewCommode.setImageResource(R.drawable.commode_false);
                }

                if (descriptionDTO.isSquat()) {
                    circleImageViewSquat.setImageResource(R.drawable.squat_true);
                } else {
                    circleImageViewSquat.setImageResource(R.drawable.squat_false);
                }

                if (descriptionDTO.isWaterSink()) {
                    circleImageViewSink.setImageResource(R.drawable.sink_true);
                } else {
                    circleImageViewSink.setImageResource(R.drawable.sink_false);
                }

                if (descriptionDTO.isShower()) {
                    circleImageViewShower.setImageResource(R.drawable.shower_true);
                } else {
                    circleImageViewShower.setImageResource(R.drawable.shower_false);
                }

                if (descriptionDTO.isMirror()) {
                    circleImageViewMirror.setImageResource(R.drawable.mirror_true);
                } else {
                    circleImageViewMirror.setImageResource(R.drawable.mirror_false);
                }

                if (descriptionDTO.isUrineTanks()) {
                    circleImageViewUrineTank.setImageResource(R.drawable.urinal_true);
                } else {
                    circleImageViewUrineTank.setImageResource(R.drawable.urinal_false);
                }
            }
        }
    }

    private void setupFloatingActionButton() {

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(view -> {

            AlertDialog.Builder mBuilder = new AlertDialog.Builder(ToiletDetails.this);
            View mView = getLayoutInflater().inflate(R.layout.rating_dialog, null);
            final RatingBar toiletRating = mView.findViewById(R.id.ratingBarDialog);
            Button btnSubmitDialog = mView.findViewById(R.id.buttonSubmitDialog);

            mBuilder.setView(mView);
            AlertDialog dialog = mBuilder.create();


            btnSubmitDialog.setOnClickListener(view1 -> {
                if (retrofit == null) {
                    retrofit = new Retrofit.Builder()
                            .baseUrl(Config.BASE_URL)
                            .addConverterFactory(GsonConverterFactory.create())
                            .build();
                }
                ToiletService toiletService = retrofit.create(ToiletService.class);

                Call<RatingUpdateResDTO> ratingUpdateCall = toiletService
                        .rateToilet(new RatingUpdateDTO(toiletLiteDTO.getId(),
                                toiletRating.getRating()));
                ratingUpdateCall.enqueue(new Callback<RatingUpdateResDTO>() {
                    @Override
                    public void onResponse(Call<RatingUpdateResDTO> call,
                                           Response<RatingUpdateResDTO> response) {
                        RatingUpdateResDTO resDTO = response.body();
                        if(response.isSuccessful()){
                            if(response.body().getStatus().equals("OK")){
                                toiletLiteDTO.setRating(resDTO.getRating());
                                ratingBar.setRating(resDTO.getRating());

                            }
                        }
                        Snackbar.make(view, resDTO.getMessage(), Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();
                    }

                    @Override
                    public void onFailure(Call<RatingUpdateResDTO> call, Throwable t) {
                        Log.i("Update Rating", t.getMessage());
                        Snackbar.make(view, "Rating update failed! Check your network connection."
                                , Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();
                    }
                });
                dialog.dismiss();
            });
            dialog.show();

        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
