package com.example.amitappfit.services;

import android.util.Log;

import androidx.annotation.Nullable;

import com.example.amitappfit.model.Item;
import com.example.amitappfit.model.Look;
import com.example.amitappfit.model.User;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DatabaseService {

    private static final String TAG = "DatabaseService";  // תג לוגים
    private static DatabaseService instance;               // מופע יחיד (Singleton) של המחלקה
    private final DatabaseReference databaseReference;     // הפניה למסד הנתונים של Firebase

    private DatabaseService() {
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();  // מקבל מופע של מסד הנתונים
        databaseReference = firebaseDatabase.getReference();                // מקבל את השורש (Root) של מסד הנתונים
    }

    // מחזיר את מופע ה-Singleton של השירות (יוצר רק פעם אחת)
    public static DatabaseService getInstance() {
        if (instance == null) {
            instance = new DatabaseService();
        }
        return instance;
    }

    // פונקציה כללית לכתיבה של אובייקט בנתיב מסוים במסד הנתונים
    private void writeData(@NotNull final String path, @NotNull final Object data, final @Nullable DatabaseCallback<Void> callback) {
        databaseReference.child(path).setValue(data).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                if (callback != null) callback.onCompleted(task.getResult());  // קורא לקולבק במקרה של הצלחה
            } else {
                if (callback != null) callback.onFailed(task.getException());   // קורא לקולבק במקרה של שגיאה
            }
        });
    }

    // פונקציה לעדכון נתוני משתמש בנתיב "Users/ID"
    public void updateUser(@NotNull final User user, @Nullable final DatabaseCallback<Void> callback) {
        writeData("Users/" + user.getId(), user, callback);
    }

    // פונקציה כללית למחיקת נתונים מכתובת נתונה במסד הנתונים
    private void deleteData(@NotNull final String path, @Nullable final DatabaseCallback<Void> callback) {
        databaseReference.child(path).removeValue().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                if (callback != null) callback.onCompleted(null);
            } else {
                if (callback != null) callback.onFailed(task.getException());
            }
        });
    }

    // פונקציה ליצירת מזהה חדש ייחודי תחת נתיב מסוים במסד הנתונים
    private String generateNewId(@NotNull final String path) {
        return databaseReference.child(path).push().getKey();
    }

    // מחזירה הפניה לנתיב מסוים במסד הנתונים (לקריאה בלבד)
    private DatabaseReference readData(@NotNull final String path) {
        return databaseReference.child(path);
    }

    // פונקציה כללית לקבלת אובייקט בודד מסוג T מכתובת מסוימת
    private <T> void getData(@NotNull final String path, @NotNull final Class<T> clazz, @NotNull final DatabaseCallback<T> callback) {
        readData(path).get().addOnCompleteListener(task -> {
            if (!task.isSuccessful()) {
                Log.e(TAG, "Error getting data", task.getException());
                callback.onFailed(task.getException());
                return;
            }
            T data = task.getResult().getValue(clazz); // מפענח את הנתונים לאובייקט מסוג T
            callback.onCompleted(data);
        });
    }

    // פונקציה לקבלת רשימה של אובייקטים מסוג T מכתובת מסוימת, עם סינון לפי זוגות מפתח-ערך
    private <T> void getDataList(@NotNull final String path, @NotNull final Class<T> clazz, @NotNull Map<String, String> filter, @NotNull final DatabaseCallback<List<T>> callback) {
        Query dbRef = readData(path);
        for (Map.Entry<String, String> entry : filter.entrySet()) {
            dbRef = dbRef.orderByChild(entry.getKey()).equalTo(entry.getValue()); // הוספת סינון לשאילתה
        }
        dbRef.get().addOnCompleteListener(task -> {
            if (!task.isSuccessful()) {
                Log.e(TAG, "Error getting data", task.getException());
                callback.onFailed(task.getException());
                return;
            }
            List<T> tList = new ArrayList<>();
            task.getResult().getChildren().forEach(dataSnapshot -> {
                T t = dataSnapshot.getValue(clazz);  // מפענח כל ילד לאובייקט T
                tList.add(t);
            });
            callback.onCompleted(tList); // מחזיר את הרשימה דרך הקולבק
        });
    }

    // פונקציות ספציפיות ל-Items (פריטים):

    public String generateNewItemId() {
        return generateNewId("Items"); // יצירת מזהה חדש לפריט
    }

    public void createNewItem(@NotNull final Item item, @Nullable final DatabaseCallback<Void> callback) {
        writeData("Items/" + item.getUserId() + "/" + item.getId(), item, callback); // יצירת פריט חדש במסד תחת משתמש מסוים
    }

    public void getItem(@NotNull final String userId, @NotNull final String itemId, @NotNull final DatabaseCallback<Item> callback) {
        getData("Items/" + userId + "/" + itemId, Item.class, callback);  // קבלת פריט ספציפי לפי מזהה משתמש ומזהה פריט
    }

    public void getItemList(@NotNull final String userId, @NotNull final DatabaseCallback<List<Item>> callback) {
        getDataList("Items/" + userId, Item.class, new HashMap<>(), callback); // קבלת כל הפריטים של משתמש מסוים (ללא סינון)
    }

    public void deleteItem(@NotNull final String userId, @NotNull final String itemId, @Nullable final DatabaseCallback<Void> callback) {
        deleteData("Items/" + userId + "/" + itemId, callback); // מחיקת פריט לפי מזהה משתמש ומזהה פריט
    }

    // פונקציות ספציפיות ל-Looks (לוקים):

    public String generateNewLookId() {
        return generateNewId("looks"); // יצירת מזהה חדש ללוק
    }

    public void createNewLook(@NotNull final Look look, @Nullable final DatabaseCallback<Void> callback) {
        writeData("looks/" + look.getUserId() + "/" + look.getId(), look, callback); // יצירת לוק חדש במסד תחת משתמש מסוים
    }

    public void updateLook(@NotNull final Look look, @Nullable final DatabaseCallback<Void> callback) {
        writeData("looks/" + look.getUserId() + "/" + look.getId(), look, callback); // עדכון לוק קיים
    }

    public void getLook(@NotNull final String userId, @NotNull final String id, @NotNull final DatabaseCallback<Look> callback) {
        getData("looks/" + userId + "/" + id, Look.class, callback); // קבלת לוק ספציפי לפי מזהה משתמש ולוק
    }

    public void getLookList(@NotNull final String userId, @NotNull final DatabaseCallback<List<Look>> callback) {
        getDataList("looks/" + userId, Look.class, new HashMap<String, String>(), callback); // קבלת כל הלוקים של משתמש (ללא סינון)
    }

    public void deleteLook(@NotNull final String userId, @NotNull final String id, @Nullable final DatabaseCallback<Void> callback) {
        deleteData("looks/" + userId + "/" + id, callback); // מחיקת לוק לפי מזהה משתמש ולוק
    }

    // פונקציות למשתמשים:

    public void createNewUser(@NotNull final User user, @Nullable final DatabaseCallback<Void> callback) {
        writeData("Users/" + user.getId(), user, callback); // יצירת משתמש חדש במסד הנתונים
    }

    public void getUser(@NotNull final String uid, @NotNull final DatabaseCallback<User> callback) {
        getData("Users/" + uid, User.class, callback); // קבלת משתמש ספציפי לפי מזהה
    }

    public void getUserList(@NotNull final DatabaseCallback<List<User>> callback) {
        getDataList("Users", User.class, new HashMap<>(), callback); // קבלת רשימת כל המשתמשים (ללא סינון)
    }

    public void deleteUser(@NotNull final String id, @Nullable final DatabaseCallback<Void> callback) {
        deleteData("Users/" + id, callback); // מחיקת משתמש לפי מזהה
    }


    // ממשק קולבק כללי שמשמש את כל הקריאות למסד הנתונים (מצליח/נכשל)
    public interface DatabaseCallback<T> {
        void onCompleted(T object);
        void onFailed(Exception e);
    }
}

