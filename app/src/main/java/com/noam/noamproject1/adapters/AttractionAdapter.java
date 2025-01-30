package com.noam.noamproject1.adapters;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.noam.noamproject1.R;
import com.noam.noamproject1.models.Attraction;
import com.noam.noamproject1.utils.ImageUtil;

import java.util.List;

public class AttractionAdapter extends RecyclerView.Adapter<AttractionAdapter.AttractionViewHolder> {

    private List<Attraction> attractionList;

    public AttractionAdapter(List<Attraction> attractionList) {
        this.attractionList = attractionList;
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
        holder.tvAttractionDetails.setText(attraction.getDetail());
        holder.tvAttractionCity.setText(attraction.getCity());
        holder.pic.setImageBitmap(ImageUtil.convertFrom64base(attraction.getPic()));

        // התנהגות כפתור "קרא עוד"
        holder.tvAttractionDetails.setText("קרא עוד...."); // מתחילים עם הטקסט "קרא עוד"

        // מוסתר בהתחלה את הטקסט של העיר
        holder.tvAttractionCity.setVisibility(View.GONE);

        holder.tvAttractionDetails.setOnClickListener(v -> {
            // בדיקה אם הטקסט של הכפתור הוא "קרא עוד"
            if (holder.tvAttractionDetails.getText().toString().equals("קרא עוד....")) {
                holder.tvAttractionDetails.setText("הסתר");
                // הצגת התוכן הנוסף (כמו city, אם צריך)
                holder.tvAttractionCity.setVisibility(View.VISIBLE);  // הצגת התוכן הנוסף
            } else {
                holder.tvAttractionDetails.setText("קרא עוד....");
                // הסתרת התוכן הנוסף
                holder.tvAttractionCity.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public int getItemCount() {
        return attractionList.size();
    }

    public static class AttractionViewHolder extends RecyclerView.ViewHolder {
        TextView tvAttractionName, tvAttractionDetails, tvAttractionCity;
        ImageView pic;

        @SuppressLint("WrongViewCast")
        public AttractionViewHolder(View itemView) {
            super(itemView);
            tvAttractionName = itemView.findViewById(R.id.tvAttractionName);
            tvAttractionDetails = itemView.findViewById(R.id.tvAttractionDetails);
            tvAttractionCity = itemView.findViewById(R.id.tvAttractionCity);
            pic = itemView.findViewById(R.id.pic);
        }
    }
}
