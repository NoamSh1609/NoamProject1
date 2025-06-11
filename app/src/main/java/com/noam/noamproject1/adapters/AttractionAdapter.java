package com.noam.noamproject1.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.noam.noamproject1.R;
import com.noam.noamproject1.models.Attraction;
import com.noam.noamproject1.utils.ImageUtil;

import java.util.List;
import java.util.Set;

public class AttractionAdapter extends RecyclerView.Adapter<AttractionAdapter.AttractionViewHolder> {

    // ממשק לטיפול באירועי לחיצה על האטרקציה
    public interface AttractionListener {
        void onClick(Attraction attraction);
        void onLongClick(Attraction attraction);
        void onFavoriteClick(Attraction attraction);
    }

    // רשימת האטרקציות
    private final List<Attraction> attractionList;

    // המאזין שיטפל באירועים מהאטרקציה
    private final AttractionListener attractionListener;

    // סט של מזהים של אטרקציות שהמשתמש סימן כמועדפים
    private final Set<String> favoriteAttractions;

    public AttractionAdapter(List<Attraction> attractionList, AttractionListener attractionListener, Set<String> favoriteAttractions) {
        this.attractionList = attractionList;
        this.attractionListener = attractionListener;
        this.favoriteAttractions = favoriteAttractions;
    }

    @NonNull
    @Override
    public AttractionViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // יצירת View חדש מה-layout של פריט אטרקציה
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_attraction, parent, false);
        return new AttractionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AttractionViewHolder holder, int position) {
        Attraction attraction = attractionList.get(position);
        if (attraction == null) return;

        // מילוי מידע בפריט
        holder.tvAttractionName.setText(attraction.getName());
        holder.tvCapsity.setText("כמות אנשים:" + attraction.getCapacity());
        holder.tvAttractionCity.setText(attraction.getCity());
        holder.tvAttractionTemp.setText(attraction.getTemp());

        // הצגת תמונה שהומרה מ-Base64
        holder.pic.setImageBitmap(ImageUtil.convertFrom64base(attraction.getPic()));

        // הגדרת מצב כפתור מועדפים (כוכב מלא או ריק)
        if (favoriteAttractions.contains(attraction.getId())) {
            holder.btnFavorite.setImageResource(R.drawable.star_filled);
        } else {
            holder.btnFavorite.setImageResource(R.drawable.star_empty);
        }

        // טיפול בלחיצה רגילה על הפריט
        holder.itemView.setOnClickListener(v -> attractionListener.onClick(attraction));

        // טיפול בלחיצה ארוכה על הפריט
        holder.itemView.setOnLongClickListener(v -> {
            attractionListener.onLongClick(attraction);
            return true;
        });

        // טיפול בלחיצה על כפתור המועדפים
        holder.btnFavorite.setOnClickListener(v -> attractionListener.onFavoriteClick(attraction));
    }

    @Override
    public int getItemCount() {
        return attractionList.size();
    }

    // מחלקה פנימית שמייצגת את פריט האטרקציה ב-RecyclerView
    public static class AttractionViewHolder extends RecyclerView.ViewHolder {
        TextView tvAttractionName, tvAttractionCity, tvAttractionTemp, tvCapsity;
        ImageView pic;
        ImageButton btnFavorite;

        public AttractionViewHolder(View itemView) {
            super(itemView);
            tvAttractionName = itemView.findViewById(R.id.tvAttractionName);
            tvAttractionCity = itemView.findViewById(R.id.tvAttractionCity);
            tvAttractionTemp = itemView.findViewById(R.id.tvAttractionTemp);
            pic = itemView.findViewById(R.id.pic);
            btnFavorite = itemView.findViewById(R.id.btnAttractionFavorite);
            tvCapsity = itemView.findViewById(R.id.tvCapsity);
        }
    }
}
