package com.noam.noamproject1.adapters;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.noam.noamproject1.R;
import com.noam.noamproject1.models.Attraction;
import com.noam.noamproject1.utils.ImageUtil;

import java.util.List;

public class AttractionAdapter extends RecyclerView.Adapter<AttractionAdapter.AttractionViewHolder> {


    public interface AttractionListener {
        public void onClick(Attraction attraction);
        public void onLongClick(Attraction attraction);
    }

    private List<Attraction> attractionList;
    private AttractionListener attractionListener;

    public AttractionAdapter(List<Attraction> attractionList, final AttractionListener attractionListener) {
        this.attractionList = attractionList;
        this.attractionListener = attractionListener;
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

        holder.itemView.setOnClickListener(v -> attractionListener.onClick(attraction));

        holder.itemView.setOnLongClickListener(v -> {
            attractionListener.onLongClick(attraction);
            return false;
        });
    }

    @Override
    public int getItemCount() {
        return attractionList.size();
    }

    public static class AttractionViewHolder extends RecyclerView.ViewHolder {
        TextView tvAttractionName, tvAttractionCity;
        ImageView pic;

        @SuppressLint("WrongViewCast")
        public AttractionViewHolder(View itemView) {
            super(itemView);
            tvAttractionName = itemView.findViewById(R.id.tvAttractionName);
            tvAttractionCity = itemView.findViewById(R.id.tvAttractionCity);
            pic = itemView.findViewById(R.id.pic);
        }
    }
}
