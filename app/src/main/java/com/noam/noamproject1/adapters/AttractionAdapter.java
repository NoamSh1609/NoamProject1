package com.noam.noamproject1.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.noam.noamproject1.R;
import com.noam.noamproject1.models.Attraction;
import com.noam.noamproject1.utils.ImageUtil;

import java.util.List;
import java.util.Set;

public class AttractionAdapter extends RecyclerView.Adapter<AttractionAdapter.AttractionViewHolder> {

    public interface AttractionListener {
        void onClick(Attraction attraction);
        void onLongClick(Attraction attraction);
        void onFavoriteClick(Attraction attraction);
    }

    private final List<Attraction> attractionList;
    private final AttractionListener attractionListener;
    private final Set<String> favoriteAttractions;

    public AttractionAdapter(List<Attraction> attractionList, AttractionListener attractionListener, Set<String> favoriteAttractions) {
        this.attractionList = attractionList;
        this.attractionListener = attractionListener;
        this.favoriteAttractions = favoriteAttractions;
    }

    @NonNull
    @Override
    public AttractionViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_attraction, parent, false);
        return new AttractionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AttractionViewHolder holder, int position) {
        Attraction attraction = attractionList.get(position);
        if (attraction == null) return;

        holder.tvAttractionName.setText(attraction.getName());
        holder.tvAttractionCity.setText(attraction.getCity());
        holder.pic.setImageBitmap(ImageUtil.convertFrom64base(attraction.getPic()));

        // Set favorite button state
        if (favoriteAttractions.contains(attraction.getId())) {
            holder.btnFavorite.setImageResource(R.drawable.star_filled);
        }else {
            holder.btnFavorite.setImageResource(R.drawable.star_empty);
        }

        // Handle click events
        holder.itemView.setOnClickListener(v -> attractionListener.onClick(attraction));

        holder.itemView.setOnLongClickListener(v -> {
            attractionListener.onLongClick(attraction);
            return true; // Prevents unintended onClick triggering
        });

        // Handle favorite button click
        holder.btnFavorite.setOnClickListener(v -> attractionListener.onFavoriteClick(attraction));
    }

    @Override
    public int getItemCount() {
        return attractionList.size();
    }

    public static class AttractionViewHolder extends RecyclerView.ViewHolder {
        TextView tvAttractionName, tvAttractionCity;
        ImageView pic;
        ImageButton btnFavorite;

        public AttractionViewHolder(View itemView) {
            super(itemView);
            tvAttractionName = itemView.findViewById(R.id.tvAttractionName);
            tvAttractionCity = itemView.findViewById(R.id.tvAttractionCity);
            pic = itemView.findViewById(R.id.pic);
            btnFavorite = itemView.findViewById(R.id.btnAttractionFavorite); // Ensure this exists in XML
        }
    }
}
