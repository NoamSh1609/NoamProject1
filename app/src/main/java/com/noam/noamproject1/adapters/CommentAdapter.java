package com.noam.noamproject1.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.noam.noamproject1.R;
import com.noam.noamproject1.models.Comment;
import com.noam.noamproject1.models.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.CommentViewHolder> {

    // רשימת המשתמשים
    private List<User> users;

    // רשימת התגובות
    private List<Comment> commentList;

    public CommentAdapter() {
        this.commentList = new ArrayList<>();
        this.users = new ArrayList<>();
    }

    // עדכון רשימת התגובות והודעת ריענון לאדפטר
    public void setCommentList(List<Comment> comments) {
        this.commentList.clear();
        this.commentList.addAll(comments);
        this.notifyDataSetChanged();
    }

    // עדכון רשימת המשתמשים והודעת ריענון לאדפטר
    public void setUsers(List<User> users) {
        this.users.clear();
        this.users.addAll(users);
        this.notifyDataSetChanged();
    }

    @NonNull
    @Override
    public CommentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // יצירת View חדש מפריסת הפריט תגובה
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_comment, parent, false);
        return new CommentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentViewHolder holder, int position) {
        Comment comment = commentList.get(position);

        // מציאת המשתמש שכתב את התגובה
        User user = null;
        for (User u : this.users) {
            if (Objects.equals(comment.getUserId(), u.getId())) {
                user = u;
                break;
            }
        }
        if (user == null) return;

        // מילוי תוכן התגובה, דירוג ושם המשתמש ב-ViewHolder
        holder.commentText.setText(comment.getCommentText());
        holder.ratingBar.setRating(comment.getRating());
        holder.ratingBar.setIsIndicator(true); // מונע עריכה של דירוג ב-RecyclerView
        holder.userName.setText(user.getFname() + " " + user.getLname());
    }

    @Override
    public int getItemCount() {
        // אם אין משתמשים - לא להציג תגובות כלל
        if (this.users.isEmpty()) return 0;
        return commentList.size();
    }

    // מחלקה פנימית המייצגת פריט תגובה ב-RecyclerView
    public static class CommentViewHolder extends RecyclerView.ViewHolder {
        TextView commentText;
        RatingBar ratingBar;
        TextView userName;

        public CommentViewHolder(View itemView) {
            super(itemView);
            commentText = itemView.findViewById(R.id.commentText);
            ratingBar = itemView.findViewById(R.id.ratingBar);
            userName = itemView.findViewById(R.id.userName);
        }
    }
}
