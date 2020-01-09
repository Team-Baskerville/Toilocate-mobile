package com.baskerville.toilocate;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.baskerville.toilocate.classes.Config;
import com.baskerville.toilocate.classes.Toilet;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> {

    private LayoutInflater layoutInflater;
    private List<Toilet> toilets;

    public Adapter(Context context, List<Toilet> toilets){
        this.layoutInflater = LayoutInflater.from(context);
        this.toilets = toilets;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view =  layoutInflater.inflate(R.layout.card_view, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {

        String locationString = toilets.get(position).getLocation().latitude
                + ", "+ toilets.get(position).getLocation().longitude;

        viewHolder.title.setText(toilets.get(position).getName());
        viewHolder.gender.setText(toilets.get(position).getGender());
        viewHolder.rating.setText(String.valueOf(toilets.get(position).getRating()));
        viewHolder.location.setText(locationString);
        Glide.with(layoutInflater.getContext()).load(Config.BASE_URL+toilets.get(position)
                .getImagePath())
                .apply(new RequestOptions().override(200, 300))
                .into(viewHolder.toiletImage);
    }

    @Override
    public int getItemCount() {
        return toilets.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView title, gender, rating, location;
        CircleImageView toiletImage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.textViewCardTitle);
            gender = itemView.findViewById(R.id.textViewCardGender);
            rating = itemView.findViewById(R.id.textViewCardRating);
            location = itemView.findViewById(R.id.textViewCardLocation);
            toiletImage = itemView.findViewById(R.id.imageNearbyToilet);

        }
    }
}
