package com.baskerville.toilocate;

import android.os.Bundle;
import android.view.View;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.baskerville.toilocate.dto.ToiletLiteDTO;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

public class ToiletDetails extends AppCompatActivity {

    CollapsingToolbarLayout toolbarLayout;
    ToiletLiteDTO toiletLiteDTO;

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

        populateView();
    }

    private void populateView() {
        RatingBar ratingBar = findViewById(R.id.ratingBarToiletDetails);
        TextView textViewGender = findViewById(R.id.textViewDetailsGender);

        toiletLiteDTO = (ToiletLiteDTO) getIntent().getSerializableExtra("toilet");

        if (toiletLiteDTO != null) {
            toolbarLayout.setTitle(toiletLiteDTO.getName());
            ratingBar.setRating(toiletLiteDTO.getRating());
            textViewGender.setText(String.format("%s TOILET",
                    toiletLiteDTO.getGender().toUpperCase()));
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
