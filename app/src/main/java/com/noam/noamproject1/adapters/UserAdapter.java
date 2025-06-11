package com.noam.noamproject1.adapters;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.noam.noamproject1.R;
import com.noam.noamproject1.models.User;

import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder> {

    private static final String TAG = "UserAdapter";

    // ממשק לאזנה לאירוע מחיקת משתמש
    public interface OnUserDeleteListener {
        void onUserDelete(User user);
    }

    // ממשק לאזנה לאירוע עריכת משתמש
    public interface OnUserEditListener {
        void onUserEdit(User user);
    }

    private List<User> userList; // רשימת המשתמשים להצגה
    private OnUserDeleteListener onUserDeleteListener; // מאזין לאירוע מחיקה
    private OnUserEditListener onUserEditListener; // מאזין לאירוע עריכה

    // קונסטרקטור לאדפטר - מקבל רשימת משתמשים ומאזינים אופציונליים
    public UserAdapter(List<User> userList, @Nullable final OnUserDeleteListener onUserDeleteListener, @Nullable final OnUserEditListener onUserEditListener) {
        this.userList = userList;
        this.onUserDeleteListener = onUserDeleteListener;
        this.onUserEditListener = onUserEditListener;
    }

    // יצירת ViewHolder - טוען את ה-layout של פריט אחד ברשימה
    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // מנפיח את תצורת הפריט מתוך קובץ ה-XML item_user
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user, parent, false);
        return new UserViewHolder(view);
    }

    // מחבר את הנתונים מהמשתמש ל-ViewHolder עבור כל פריט ברשימה
    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        final User user = userList.get(position);

        // הצגת שם מלא
        holder.tvUserName.setText(user.getFname() + " " + user.getLname());
        // הצגת אימייל
        holder.tvUserEmail.setText(user.getEmail());
        // הצגת טלפון
        holder.tvUserPhone.setText(user.getPhone());

        // מאזין ללחיצה על כפתור המחיקה
        holder.btnDelete.setOnClickListener(v -> {
            Log.d(TAG, "clicked on delete");
            if (onUserDeleteListener != null) {
                // מעביר את האירוע למאזין החיצוני עם המשתמש למחיקה
                onUserDeleteListener.onUserDelete(user);
            }
        });

        // מאזין ללחיצה על כפתור העריכה
        holder.btnEdit.setOnClickListener(v -> {
            Log.d(TAG, "clicked on edit");
            if (onUserEditListener != null) {
                // מעביר את האירוע למאזין החיצוני עם המשתמש לעריכה
                onUserEditListener.onUserEdit(user);
            }
        });
    }

    // מחזיר את מספר הפריטים ברשימה
    @Override
    public int getItemCount() {
        return userList.size();
    }

    // מחלקת ViewHolder המייצגת את הפריט ב-RecyclerView
    public static class UserViewHolder extends RecyclerView.ViewHolder {
        TextView tvUserName, tvUserEmail, tvUserPhone;
        Button btnDelete, btnEdit;

        public UserViewHolder(View itemView) {
            super(itemView);
            // קישור אלמנטים מה-XML ל-widgets בקוד
            tvUserName = itemView.findViewById(R.id.tvUserName);
            tvUserEmail = itemView.findViewById(R.id.tvUserEmail);
            tvUserPhone = itemView.findViewById(R.id.tvUserPhone);
            btnDelete = itemView.findViewById(R.id.btnDeleteUser);
            btnEdit = itemView.findViewById(R.id.btnEdit);
        }
    }
}
