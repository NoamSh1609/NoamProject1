package com.noam.noamproject1.services;

import android.util.Log;

import androidx.annotation.Nullable;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.noam.noamproject1.models.Attraction;
import com.noam.noamproject1.models.Comment;
import com.noam.noamproject1.models.Review;
import com.noam.noamproject1.models.User;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;


public class DatabaseService {

    private static final String TAG = "DatabaseService";

    public void getAttractionDetails(String attractionId, DatabaseCallback<Attraction> callback) {
        getData("Attractions/" + attractionId, Attraction.class, callback);
    }

    public interface DatabaseCallback<T> {
        void onCompleted(T object);

        void onFailed(Exception e);
    }

    private static DatabaseService instance;

    private final DatabaseReference databaseReference;

    private DatabaseService() {
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
    }

    public static DatabaseService getInstance() {
        if (instance == null) {
            instance = new DatabaseService();
        }
        return instance;
    }

    private void writeData(@NotNull final String path, @NotNull final Object data, final @Nullable DatabaseCallback<Void> callback) {
        readData(path).setValue(data).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                if (callback == null) return;
                callback.onCompleted(task.getResult());
            } else {
                if (callback == null) return;
                callback.onFailed(task.getException());
            }
        });
    }

    private DatabaseReference readData(@NotNull final String path) {
        return databaseReference.child(path);
    }

    private <T> void getData(@NotNull final String path, @NotNull final Class<T> clazz, @NotNull final DatabaseCallback<T> callback) {
        readData(path).get().addOnCompleteListener(task -> {
            if (!task.isSuccessful()) {
                Log.e(TAG, "Error getting data", task.getException());
                callback.onFailed(task.getException());
                return;
            }
            T data = task.getResult().getValue(clazz);
            callback.onCompleted(data);
        });
    }

    private <T> void getDataList(@NotNull final String path, @NotNull final Class<T> clazz, @NotNull final DatabaseCallback<List<T>> callback) {
        readData(path).get().addOnCompleteListener(task -> {
            if (!task.isSuccessful()) {
                Log.e(TAG, "Error getting data", task.getException());
                callback.onFailed(task.getException());
                return;
            }
            List<T> tList = new ArrayList<>();
            task.getResult().getChildren().forEach(dataSnapshot -> {
                T t = dataSnapshot.getValue(clazz);
                tList.add(t);
            });

            callback.onCompleted(tList);
        });
    }

    private void deleteData(String path, DatabaseCallback<Boolean> databaseCallback) {
        readData(path).removeValue()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        databaseCallback.onCompleted(true);
                    } else {
                        databaseCallback.onFailed(task.getException());
                    }
                });
    }

    private String generateNewId(@NotNull final String path) {
        return databaseReference.child(path).push().getKey();
    }

    public String generateNewAttractionId(){
        return this.generateNewId("Attractions/");
    }

    public void createNewUser(@NotNull final User user, @Nullable final DatabaseCallback<Void> callback) {
        writeData("Users/" + user.getId(), user, callback);
    }

    public void deleteUser(String id, DatabaseCallback<Boolean> databaseCallback) {
        deleteData("Users/" + id, databaseCallback);
    }

    public void updateUser(User currentUser, DatabaseCallback<Void> databaseCallback) {
        createNewUser(currentUser, databaseCallback);
    }

    public void getAttraction(@NotNull final String aid, @NotNull final DatabaseCallback<Attraction> callback) {
        getData("Attractions/" + aid, Attraction.class, callback);
    }

    public void createNewAttraction(@NotNull final Attraction att, @Nullable final DatabaseCallback<Void> callback) {
        writeData("Attractions/" + att.getId(), att, callback);
    }

    public void getAttractionList(@NotNull final DatabaseCallback<List<Attraction>> callback) {
        getDataList("Attractions", Attraction.class, callback);
    }

    public void getUser(@NotNull final String uid, @NotNull final DatabaseCallback<User> callback) {
        getData("Users/" + uid, User.class, callback);
    }

    public void getUserList(DatabaseCallback<List<User>> callback) {
        getDataList("Users", User.class, callback);
    }

    public void deleteAttraction(String id, DatabaseCallback<Boolean> databaseCallback) {
        deleteData("Attractions/" + id, databaseCallback);
    }

    public void saveReview(String attractionId, Review review, DatabaseCallback<Void> callback) {
        writeData("Attractions/" + attractionId + "/Reviews/"+ review.getReviewId(), review, callback);
    }

    public String generateNewAttractionReviewId(String attractionId){
        return this.generateNewId("Attractions/" + attractionId + "/Reviews");
    }

    // קבלת רשימה של תגובות ודירוגים עבור אטרקציה מסוימת
    public void getReviews(String attractionId, DatabaseCallback<List<Review>> callback) {
        getDataList("Attractions/" + attractionId + "/Reviews", Review.class, callback);
    }

    public void getComments(String itemId, DatabaseCallback<List<Comment>> callback) {
        getDataList("comments/" + itemId, Comment.class, callback);
    }

    public void writeNewComment(String itemId, Comment comment, DatabaseCallback<Void> callback) {
        writeData("comments/" + itemId + "/" + comment.getCommentId(), comment, callback);
    }

    public String generateNewCommentId(String itemId) {
        return generateNewId("comments/" + itemId);
    }

    public void removeComment(String itemId, String commentId, DatabaseCallback<Boolean> callback) {
        deleteData("comments/" + itemId + "/"+ commentId, callback)   ;
    }
}
