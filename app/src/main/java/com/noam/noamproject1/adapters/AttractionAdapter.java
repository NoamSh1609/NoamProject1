package com.noam.noamproject1.screens;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.noam.noamproject1.R;
import com.noam.noamproject1.models.Attraction;

import java.util.List;

public class AttractionAdapter extends RecyclerView.Adapter<AttractionAdapter.AttractionViewHolder> {

    private List<Attraction> attractionsList;

    public AttractionAdapter(List<Attraction> attractionsList) {
        this.attractionsList = attractionsList;
    }

    @Override
    public AttractionViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.attraction_item, parent, false);
        return new AttractionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(AttractionViewHolder holder, int position) {
        Attraction attraction = attractionsList.get(position);
        holder.nameTextView.setText(attraction.getName());
        holder.cityTextView.setText(attraction.getCity());

        holder.ratingTextView.setText("דירוג: " + attraction.getRating());
    }

    @Override
    public int getItemCount() {
        return attractionsList.size();
    }

    public static class AttractionViewHolder extends RecyclerView.ViewHolder {

        TextView nameTextView, cityTextView, ratingTextView;

        public AttractionViewHolder(View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.attractionName);
            cityTextView = itemView.findViewById(R.id.attractionCity);
            ratingTextView = itemView.findViewById(R.id.attractionRating);
        }
    }
}
