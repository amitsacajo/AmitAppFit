package com.example.amitappfit.services;

import android.util.Log;

import androidx.annotation.Nullable;

import com.example.amitappfit.model.Item;
import com.example.amitappfit.model.Look;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DatabaseService {

    private static final String TAG = "DatabaseService";
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
        databaseReference.child(path).setValue(data).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                if (callback != null) callback.onCompleted(task.getResult());
            } else {
                if (callback != null) callback.onFailed(task.getException());
            }
        });
    }

    private void deleteData(@NotNull final String path, @Nullable final DatabaseCallback<Void> callback) {
        databaseReference.child(path).removeValue().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                if (callback != null) callback.onCompleted(null);
            } else {
                if (callback != null) callback.onFailed(task.getException());
            }
        });
    }

    /// generate a new id for a new object in the database
    /// @param path the path to generate the id for
    /// @return a new id for the object
    /// @see String
    /// @see DatabaseReference#push()

    private String generateNewId(@NotNull final String path) {
        return databaseReference.child(path).push().getKey();
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

    private <T> void getDataList(@NotNull final String path, @NotNull final Class<T> clazz, @NotNull Map<String, String> filter, @NotNull final DatabaseCallback<List<T>> callback) {
        Query dbRef = readData(path);
        for (Map.Entry<String, String> entry : filter.entrySet()) {
            dbRef = dbRef.orderByChild(entry.getKey()).equalTo(entry.getValue());
        }
        dbRef.get().addOnCompleteListener(task -> {
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


    public String generateNewItemId() {
        return generateNewId("Items");
    }

    public void createNewItem(@NotNull final Item item, @Nullable final DatabaseCallback<Void> callback) {
        writeData("Items/" + item.getId(), item, callback);
    }

    public void updateLook(@NotNull final Look look, @Nullable final DatabaseCallback<Void> callback) {
        writeData("looks/" + look.getId(), look, callback);
    }


    public void getItem(@NotNull final String itemId, @NotNull final DatabaseCallback<Item> callback) {
        getData("Items/" + itemId, Item.class, callback);
    }

    public void getItemList(@NotNull final DatabaseCallback<List<Item>> callback) {
        getDataList("Items", Item.class, new HashMap<>(), callback);
    }

    public void deleteItem(@NotNull final String itemId, @Nullable final DatabaseCallback<Void> callback) {
        deleteData("Items/" + itemId, callback);
    }

    public String generateNewLookId() {
        return generateNewId("looks");
    }

    public void createNewLook(@NotNull final Look look, @Nullable final DatabaseCallback<Void> callback) {
        writeData("looks/" + look.getId(), look, callback);
    }

    public void getLook(@NotNull final String id, @NotNull final DatabaseCallback<Look> callback) {
        getData("looks/" + id, Look.class, callback);
    }

    public void getLookList(@NotNull final DatabaseCallback<List<Look>> callback) {
        getDataList("looks", Look.class, new HashMap<>(), callback);
    }

    public void deleteLook(@NotNull final String id, @Nullable final DatabaseCallback<Void> callback) {
        deleteData("looks/" + id, callback);
    }

    public interface DatabaseCallback<T> {
        void onCompleted(T object);
        void onFailed(Exception e);
    }
}
