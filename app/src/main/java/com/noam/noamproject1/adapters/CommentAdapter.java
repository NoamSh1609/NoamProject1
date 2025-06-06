package com.noam.noamproject1.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.noam.noamproject1.R;
import com.noam.noamproject1.models.Comment;
import com.noam.noamproject1.models.User;
import com.noam.noamproject1.services.AuthenticationService;
import com.noam.noamproject1.services.DatabaseService;
import com.noam.noamproject1.utils.SharedPreferencesUtil;


import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.CommentViewHolder> {

    private List<User> users;
    private List<Comment> commentList;

    public CommentAdapter() {
        this.commentList = new ArrayList<>();
        this.users = new ArrayList<>();
    }

    public void setCommentList(List<Comment> comments) {
        this.commentList.clear();
        this.commentList.addAll(comments);
        this.notifyDataSetChanged();
    }

    public void setUsers(List<User> users) {
        this.users.clear();
        this.users.addAll(users);
        this.notifyDataSetChanged();
    }

    @NonNull
    @Override
    public CommentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_comment, parent, false);
        return new CommentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentViewHolder holder, int position) {
        Comment comment = commentList.get(position);

        User user = null;
        for (User u : this.users) {
            if (Objects.equals(comment.getUserId(), u.getId())) {
                user = u;
                break;
            }
        }
        if (user == null) return;

        holder.commentText.setText(comment.getCommentText());
        holder.ratingBar.setRating(comment.getRating());
        holder.ratingBar.setIsIndicator(true);
        holder.userName.setText(user.getFname() + " " + user.getLname());

    }



    @Override
    public int getItemCount() {
        if (this.users.isEmpty()) return 0;
        return commentList.size();
    }

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