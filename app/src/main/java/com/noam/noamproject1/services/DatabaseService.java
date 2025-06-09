package com.noam.noamproject1.services;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;
import com.noam.noamproject1.models.Attraction;
import com.noam.noamproject1.models.Comment;
import com.noam.noamproject1.models.Review;
import com.noam.noamproject1.models.User;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.function.Function;


public class DatabaseService {

    private static final String TAG = "DatabaseService";


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

    private <T> void runTransaction(@NotNull final String path, @NotNull final Class<T> clazz, @NotNull Function<T,T> function, @NotNull final DatabaseCallback<T> callback) {
        readData(path).runTransaction(new Transaction.Handler() {
            @NonNull
            @Override
            public Transaction.Result doTransaction(@NonNull MutableData mutableData) {
                T t = mutableData.getValue(clazz);
                if (t == null) {
                    return Transaction.abort();
                }
                t = function.apply(t);
                // Set value and report transaction success
                mutableData.setValue(t);
                return Transaction.success(mutableData);
            }

            @Override
            public void onComplete(@Nullable DatabaseError error, boolean committed, @Nullable DataSnapshot currentData) {
                if (error != null) {
                    callback.onFailed(error.toException());
                    return;
                }
                T t = currentData.getValue(clazz);
                callback.onCompleted(t);
            }
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

    public String generateNewAttractionReviewId(String attractionId){
        return this.generateNewId("Attractions/" + attractionId + "/Reviews");
    }

    public void getComments(String attractionId, DatabaseCallback<List<Comment>> callback) {
        getAttraction(attractionId, new DatabaseCallback<Attraction>() {
            @Override
            public void onCompleted(Attraction attraction) {
                callback.onCompleted(attraction.getComments());
            }

            @Override
            public void onFailed(Exception e) {
                callback.onFailed(e);
            }
        });
    }

    public void writeNewComment(String attractionId, Comment comment, DatabaseCallback<Attraction> callback) {
        runTransaction("Attractions/" + attractionId, Attraction.class, new Function<Attraction, Attraction>() {
            @Override
            public Attraction apply(Attraction attraction) {
                List<Comment> comments = attraction.getComments();
                comments.add(comment);
                attraction.setComments(comments);
                return attraction;
            }
        }, callback);
    }

    public void increaseAttractionVisitors(String attractionId, DatabaseCallback<Attraction> callback) {
        runTransaction("Attractions/" + attractionId, Attraction.class, new Function<Attraction, Attraction>() {
            @Override
            public Attraction apply(Attraction attraction) {
                attraction.setCurrentVisitors(attraction.getCurrentVisitors()+1);
                return attraction;
            }
        }, callback);
    }

    public void getAttractionDetails(String attractionId, DatabaseCallback<Attraction> callback) {
        getData("Attractions/" + attractionId, Attraction.class, callback);
    }
}
