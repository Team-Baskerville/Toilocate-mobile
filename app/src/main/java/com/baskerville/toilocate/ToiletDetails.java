package com.baskerville.toilocate;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.baskerville.toilocate.classes.Config;
import com.baskerville.toilocate.dto.ToiletDescriptionDTO;
import com.baskerville.toilocate.dto.ToiletLiteDTO;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import de.hdodenhof.circleimageview.CircleImageView;

public class ToiletDetails extends AppCompatActivity {

    CollapsingToolbarLayout toolbarLayout;
    ToiletLiteDTO toiletLiteDTO;
    ImageView toiletImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_toilet_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        toolbarLayout = findViewById(R.id.toolbar_layout);
//        toolbarLayout.setTitle(getIntent().getExtras().getString("name"));

        toiletImageView = findViewById(R.id.toolbarImageDetails);
        Glide.with(this).load(Config.TEST_IMG_URL)
                .apply(new RequestOptions().override(200, 300))
                .into(toiletImageView);

        populateView();
    }

    private void populateView() {
        RatingBar ratingBar = findViewById(R.id.ratingBarToiletDetails);
        TextView textViewGender = findViewById(R.id.textViewDetailsGender);
        CircleImageView circleImageViewCommode = findViewById(R.id.imageDetailsCommode);
        CircleImageView circleImageViewSquat = findViewById(R.id.imageDetailsSquat);
        CircleImageView circleImageViewSink = findViewById(R.id.imageDetailsSink);
        CircleImageView circleImageViewMirror = findViewById(R.id.imageDetailsMirror);
        CircleImageView circleImageViewShower = findViewById(R.id.imageDetailsShower);
        CircleImageView circleImageViewUrineTank = findViewById(R.id.imageDetailsUrineTank);

        toiletLiteDTO = (ToiletLiteDTO) getIntent().getSerializableExtra("toilet");

        if (toiletLiteDTO != null) {
            toolbarLayout.setTitle(toiletLiteDTO.getName());
            ratingBar.setRating(toiletLiteDTO.getRating());
            textViewGender.setText(String.format("%s TOILET",
                    toiletLiteDTO.getGender().toUpperCase()));
            if(toiletLiteDTO.getDescription() != null){
                ToiletDescriptionDTO descriptionDTO = toiletLiteDTO.getDescription();
                if(descriptionDTO.isCommode()) {
                    circleImageViewCommode.setImageResource(R.drawable.commode_true);
                } else {
                    circleImageViewCommode.setImageResource(R.drawable.commode_false);
                }

                if(descriptionDTO.isWaterSink()) {
                    circleImageViewSink.setImageResource(R.drawable.sink_true);
                } else {
                    circleImageViewSink.setImageResource(R.drawable.sink_false);
                }
            }
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
