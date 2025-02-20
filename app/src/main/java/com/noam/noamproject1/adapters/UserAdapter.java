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

    public interface OnUserDeleteListener {
        void onUserDelete(User user);
    }

    public interface OnUserEditListener {
        void onUserEdit(User user);
    }

    private List<User> userList;
    private OnUserDeleteListener onUserDeleteListener;
    private OnUserEditListener onUserEditListener;

    public UserAdapter(List<User> userList, @Nullable final OnUserDeleteListener onUserDeleteListener, @Nullable final OnUserEditListener onUserEditListener) {
        this.userList = userList;
        this.onUserDeleteListener = onUserDeleteListener;
        this.onUserEditListener = onUserEditListener;
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user, parent, false);
        return new UserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        final User user = userList.get(position);

        holder.tvUserName.setText(user.getFname() + " " + user.getLname());
        holder.tvUserEmail.setText(user.getEmail());
        holder.tvUserPhone.setText(user.getPhone());

        holder.btnDelete.setOnClickListener(v -> {
            Log.d(TAG, "clicked on delete");
            if (onUserDeleteListener != null) {
                onUserDeleteListener.onUserDelete(user);
            }
        });

        holder.btnEdit.setOnClickListener(v -> {
            Log.d(TAG, "clicked on edit");
            if (onUserEditListener != null) {
                onUserEditListener.onUserEdit(user);
            }
        });
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public static class UserViewHolder extends RecyclerView.ViewHolder {
        TextView tvUserName, tvUserEmail, tvUserPhone;
        Button btnDelete, btnEdit;

        public UserViewHolder(View itemView) {
            super(itemView);
            tvUserName = itemView.findViewById(R.id.tvUserName);
            tvUserEmail = itemView.findViewById(R.id.tvUserEmail);
            tvUserPhone = itemView.findViewById(R.id.tvUserPhone);
            btnDelete = itemView.findViewById(R.id.btnDeleteUser);
            btnEdit = itemView.findViewById(R.id.btnEdit);
        }
    }
}
