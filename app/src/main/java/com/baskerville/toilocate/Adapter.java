package com.baskerville.toilocate;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.baskerville.toilocate.classes.Config;
import com.baskerville.toilocate.classes.Toilet;
import com.baskerville.toilocate.dto.ToiletLiteDTO;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> {

    private LayoutInflater layoutInflater;
    private List<Toilet> toilets;
    private Toilet currentToilet;
    private Context context;

    public Adapter(Context context, List<Toilet> toilets) {
        this.layoutInflater = LayoutInflater.from(context);
        this.toilets = toilets;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = layoutInflater.inflate(R.layout.card_view, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        currentToilet = toilets.get(position);

        viewHolder.title.setText(toilets.get(position).getName());
        viewHolder.gender.setText(String.format("Gender :%s", toilets.get(position).getGender()));
        viewHolder.ratingBar.setRating(toilets.get(position).getRating());
        Glide.with(layoutInflater.getContext()).load(Config.BASE_URL + toilets.get(position)
                .getImagePath())
                .apply(new RequestOptions().override(200, 300))
                .into(viewHolder.toiletImage);

    }

    @Override
    public int getItemCount() {
        return toilets.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView title, gender, rating;
        CircleImageView toiletImage;
        RatingBar ratingBar;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.textViewCardTitle);
            gender = itemView.findViewById(R.id.textViewCardGender);
            ratingBar = itemView.findViewById(R.id.ratingBarCard);
            toiletImage = itemView.findViewById(R.id.imageNearbyToilet);

            itemView.setOnClickListener(v ->
            {
                Intent toiletDetailsIntent = new Intent(itemView.getContext(), ToiletDetails.class);
                toiletDetailsIntent.putExtra("toilet", new ToiletLiteDTO(currentToilet));
                context.startActivity(toiletDetailsIntent);
            });

        }
    }
}
